package com.kindred.mir.scene.game;

import com.kindred.mir.Env;
import com.kindred.mir.GameCommon.ChatType;
import com.kindred.mir.GameCommon.Door;
import com.kindred.mir.GameCommon.LightSetting;
import com.kindred.mir.GameCommon.MirDirection;
import com.kindred.mir.Settings;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithTexture;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirImage.ImageEffect;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.libs.map.MapCellInfo;
import com.kindred.mir.libs.map.MirMap;
import com.kindred.mir.scene.MirScene.SceneEnumType;
import com.kindred.mir.scene.game.objects.MapObject;
import com.kindred.mir.scene.game.objects.MonsterObject;
import com.kindred.mir.scene.game.objects.UserObject;
import com.kindred.mir.scene.game.objects.effects.Effect;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;
import java.util.ArrayList;
import java.util.List;

public class MapControl extends MirControlWithTexture {

  public static List<MapObject> Objects = new ArrayList<MapObject>();

  public static final int CellWidth = 48;
  public static final int CellHeight = 32;

  public static int OffSetX;
  public static int OffSetY;

  public static int ViewRangeX;
  public static int ViewRangeY;

  //public static MouseButtons MapButtons;
  public static Point MouseLocation;
  public static long InputDelay;
  public static long NextAction;

  public MapCellInfo[][] M2CellInfo;
  public List<Door> Doors = new ArrayList<Door>();
  public int Width, Height;

  public String FileName = "";
  public String Title = "";
  public int MiniMap, BigMap, Music, SetMusic;
  public LightSetting Lights;
  public boolean isLightning, isFire;
  public byte MapDarkLight;
  public long LightningTime, FireTime;

  public boolean isFloorValid, isLightsValid;

//  private Texture floorTexture, lightTexture;
//  private Surface floorSurface, lightSurface;

  public long OutputDelay;

  private boolean isUpgradingAction;

  private boolean isAutoRun;

  public boolean AutoHit;

  public int AnimationCount;

  public List<Effect> Effects = new ArrayList<>();

  public MapControl(MirControl parent, long renderer_id) {
    super(parent, renderer_id);

    //MapButtons = MouseButtons.None;

    OffSetX = Settings.ScreenWidth / 2 / CellWidth;
    OffSetY = Settings.ScreenHeight / 2 / CellHeight - 1;

    ViewRangeX = OffSetX + 4;
    ViewRangeY = OffSetY + 4;

    setSize(new Size(Settings.ScreenWidth, Settings.ScreenHeight));

    //DrawControlTexture = true;
    setBackColor(Color.Black);


//    MouseDown += OnMouseDown;
//    MouseMove += (o, e) => MouseLocation = e.Location;
//    Click += OnMouseClick;
  }

  public UserObject getUser() {
    return MapObject.User;
  }

  public Point getMapLocation() {
    if (GameScene.getUser() == null) {
      return Point.Empty;
    } else {
      return Point.add(
          new Point(MouseLocation.getX() / CellWidth - OffSetX, MouseLocation.getY() / CellHeight - OffSetY),
          GameScene.getUser().CurrentLocation
      );
    }
  }

  public boolean getIsUpgradingAction() {
    return isUpgradingAction;
  }
  public void setIsUpgradingAction(boolean isUpgradingAction) {
    if (this.isUpgradingAction == isUpgradingAction) {
      return;
    }
    this.isUpgradingAction = isUpgradingAction;
  }


  public boolean getIsAutoRun() {
    return this.isAutoRun;
  }
  public void setIsAutoRun(boolean isAutoRun) {
    if (this.isAutoRun == isAutoRun) {
      return;
    }
    this.isAutoRun = isAutoRun;
    if (Env.ActiveScene!=null && Env.ActiveScene.getSceneType() == SceneEnumType.Game) {
      ((GameScene)Env.ActiveScene).receiveChat(isAutoRun ? "[自动行走: 开]" : "[自动行走: 关]", ChatType.Hint);
    }
  }

  public void loadMap() throws Exception
  {
    if (Env.ActiveScene!=null && Env.ActiveScene.getSceneType() == SceneEnumType.Game) {
      ((GameScene)Env.ActiveScene).npcDialog.setIsVisible(false);
    }
    Objects.clear();
    Effects.clear();
    Doors.clear();

//    if (User != null) {
//      Objects.Add(User);
//    }

    MapObject.MouseObject = null;
    MapObject.TargetObject = null;
    MapObject.MagicObject = null;
    MirMap mirMap = new MirMap(FileName);
    M2CellInfo = mirMap.getMapCells();
    Width = mirMap.getWidth();
    Height = mirMap.getHeight();

    try {
      if (SetMusic != Music) {
        //SoundManager.Device.Dispose();
        //SoundManager.Create();
        SoundManager.playMusic(Music, true);
      }
    } catch (Exception e) {
      // Do nothing. index was not valid.
    }

    SetMusic = Music;
    SoundList.Music = Music;
  }

  public void removeObject(MapObject ob)
  {
    M2CellInfo[ob.MapLocation.getX()][ob.MapLocation.getY()].removeObject(ob);
  }
  public void addObject(MapObject ob)
  {
    M2CellInfo[ob.MapLocation.getX()][ob.MapLocation.getY()].addObject(ob);
  }
  public MapObject findObject(long ObjectID, int x, int y)
  {
    return M2CellInfo[x][y].findObject(ObjectID);
  }
  public void sortObject(MapObject ob)
  {
    M2CellInfo[ob.MapLocation.getX()][ob.MapLocation.getY()].sort();
  }

  public Door getDoor(byte Index)
  {
    for (int i = 0; i < Doors.size(); i++)
    {
      if (Doors.get(i).getIndex() == Index) {
        return Doors.get(i);
      }
    }
    return null;
  }

  public void processdoors()
  {
    for (int i = 0; i < Doors.size(); i++)
    {
      if ((Doors.get(i).getDoorState() == 1) || (Doors.get(i).getDoorState() == 3))
      {
        if (Doors.get(i).getLastTick() + 50 < Settings.getTime())
        {
          Doors.get(i).setLastTick(Settings.getTime());
          Doors.get(i).setImageIndex((byte)(Doors.get(i).getImageIndex()+1));
          if (Doors.get(i).getImageIndex() == 1)//change the 1 if you want to actualy animate doors opening/closing
          {
            Doors.get(i).setImageIndex((byte)0);
            Doors.get(i).setDoorState((byte)(Doors.get(i).getDoorState()+1 % 4));
          }
          isFloorValid = false;
        }
      }
      if (Doors.get(i).getDoorState() == 2)
      {
        if (Doors.get(i).getLastTick() + 5000 < Settings.getTime())
        {
          Doors.get(i).setLastTick(Settings.getTime());
          Doors.get(i).setDoorState((byte)3);
          isFloorValid = false;
        }
      }
    }
  }

  public void process()
  {
    processdoors();
    MapObject.User.process();

    for (int i = Objects.size() - 1; i >= 0; i--) {
      MapObject ob = Objects.get(i);
      if (ob == MapObject.User) {
        continue;
      }
      //  if (ob.ActionFeed.Count > 0 || ob.Effects.Count > 0 || GameScene.CanMove || CMain.Time >= ob.NextMotion)
      ob.process();
    }

    for (int i = Effects.size() - 1; i >= 0; i--) {
      Effects.get(i).Process();
    }

    if (MapObject.TargetObject != null && MapObject.TargetObject instanceof MonsterObject
        && MapObject.TargetObject.AI == 64) {
      MapObject.TargetObject = null;
    }
    if (MapObject.MagicObject != null && MapObject.MagicObject instanceof MonsterObject
        && MapObject.MagicObject.AI == 64) {
      MapObject.MagicObject = null;
    }

    //CheckInput();

    MapObject bestmouseobject = null;
    for (int y = getMapLocation().getY() + 2; y >= getMapLocation().getY() - 2; y--)
    {
      if (y >= Height) {
        continue;
      }
      if (y < 0) {
        break;
      }
      for (int x = getMapLocation().getX() + 2; x >= getMapLocation().getX() - 2; x--) {
        if (x >= Width) {
          continue;
        }
        if (x < 0) {
          break;
        }
        MapCellInfo cell = M2CellInfo[x][y];
        if (cell.CellObjects == null) {
          continue;
        }

//        for (int i = cell.CellObjects.size() - 1; i >= 0; i--) {
//          MapObject ob = cell.CellObjects.get(i);
//          if (ob == MapObject.User || !ob.MouseOver(CMain.MPoint)) {
//            continue;
//          }
//
//          if (MapObject.MouseObject != ob) {
//            if (ob.Dead) {
//              if (!Settings.TargetDead && GameScene.TargetDeadTime <= CMain.Time) {
//                continue;
//              }
//
//              bestmouseobject = ob;
//              //continue;
//            }
//            MapObject.MouseObject = ob;
//          }
//          if (bestmouseobject != null && MapObject.MouseObject == null) {
//            MapObject.MouseObject = bestmouseobject;
//          }
//          return;
//        }
      }
    }


    if (MapObject.MouseObject != null) {
      MapObject.MouseObject = null;
    }
  }

  public static MapObject getObject(long targetID)
  {
    for (int i = 0; i < Objects.size(); i++) {
      MapObject ob = Objects.get(i);
      if (ob.ObjectID != targetID) {
        continue;
      }
      return ob;
    }
    return null;
  }

  private void drawFloor(long surface) throws Exception{
    int index;
    int drawY, drawX;

    long surface = MirJNI.Mir_FillRect(Settings.ScreenWidth,Settings.ScreenHeight,new int[]{0,0,0,0});

    for (int y = getUser().Movement.getY() - ViewRangeY; y <= getUser().Movement.getY() + ViewRangeY; y++) {
      if (y <= 0 || y % 2 == 1) {
        continue;
      }
      if (y >= Height) {
        break;
      }
      drawY = (y - getUser().Movement.getY() + OffSetY) * CellHeight + getUser().OffSetMove.getY(); //Moving OffSet

      for (int x = getUser().Movement.getX() - ViewRangeX; x <= getUser().Movement.getX() + ViewRangeX; x++) {
        if (x <= 0 || x % 2 == 1) {
          continue;
        }
        if (x >= Width) {
          break;
        }
        drawX = (x - getUser().Movement.getX() + OffSetX) * CellWidth - OffSetX + getUser().OffSetMove.getX(); //Moving OffSet
        if ((M2CellInfo[x][y].BackImage == 0) || (M2CellInfo[x][y].BackIndex == -1)) {
          continue;
        }
        index = (M2CellInfo[x][y].BackImage & 0x1FFFF) - 1;
        MirImage img = MirLibFactory.getMapLib(M2CellInfo[x][y].BackIndex).GetMirImage(index);
        MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX, drawY,1);
      }
    }

    for (int y = getUser().Movement.getY() - ViewRangeY; y <= getUser().Movement.getY() + ViewRangeY + 5; y++) {
      if (y <= 0) {
        continue;
      }
      if (y >= Height) {
        break;
      }
      drawY = (y - getUser().Movement.getY() + OffSetY) * CellHeight + getUser().OffSetMove.getY(); //Moving OffSet

      for (int x = getUser().Movement.getX() - ViewRangeX; x <= getUser().Movement.getX() + ViewRangeX; x++) {
        if (x < 0) {
          continue;
        }
        if (x >= Width) {
          break;
        }
        drawX = (x - getUser().Movement.getX() + OffSetX) * CellWidth - OffSetX + getUser().OffSetMove.getX(); //Moving OffSet

        index = M2CellInfo[x][y].MiddleImage - 1;

        if ((index < 0) || (M2CellInfo[x][y].MiddleIndex == -1)) {
          continue;
        }
        MirImage img = MirLibFactory.getMapLib(M2CellInfo[x][y].MiddleIndex).GetMirImage(index);
        if (M2CellInfo[x][y].MiddleIndex > 199){//mir3 mid layer is same level as front layer not real middle + it cant draw index -1 so 2 birds in one stone :p
          Size s = img.getTrueSize();

          if (s.getWidth() != CellWidth || s.getHeight() != CellHeight) {
            continue;
          }
        }
        MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX, drawY,1);
      }
    }
    for (int y = getUser().Movement.getY() - ViewRangeY; y <= getUser().Movement.getY() + ViewRangeY + 5; y++) {
      if (y <= 0) {
        continue;
      }
      if (y >= Height) {
        break;
      }
      drawY = (y - getUser().Movement.getY() + OffSetY) * CellHeight + getUser().OffSetMove.getY(); //Moving OffSet

      for (int x = getUser().Movement.getX() - ViewRangeX; x <= getUser().Movement.getX() + ViewRangeX; x++)
      {
        if (x < 0) {
          continue;
        }
        if (x >= Width) {
          break;
        }
        drawX = (x - getUser().Movement.getX() + OffSetX) * CellWidth - OffSetX + getUser().OffSetMove.getX(); //Moving OffSet

        index = (M2CellInfo[x][y].FrontImage & 0x7FFF) - 1;
        if (index == -1) {
          continue;
        }
        int fileIndex = M2CellInfo[x][y].FrontIndex;
        if (fileIndex == -1) {
          continue;
        }
        if (fileIndex == 200) {
          continue; //fixes random bad spots on old school 4.map
        }
        //Size s = Libraries.MapLibs[fileIndex].GetSize(index);
        MirImage img = MirLibFactory.getMapLib(fileIndex).GetMirImage(index);
        if (M2CellInfo[x][y].DoorIndex > 0){
          Door DoorInfo = getDoor(M2CellInfo[x][y].DoorIndex);
          if (DoorInfo == null) {
            DoorInfo = Door.builder().index(M2CellInfo[x][y].DoorIndex)
                .doorState((byte)0).imageIndex((byte)0).lastTick(Settings.getTime())
                .build();
            Doors.add(DoorInfo);
          } else {
            if (DoorInfo.getDoorState() != 0) {
              index += (DoorInfo.getImageIndex() + 1) * M2CellInfo[x][y].DoorOffset;//'bad' code if you want to use animation but it's gonna depend on the animation > has to be custom designed for the animtion
            }
          }
        }

        if (index < 0
            || ((img.getTrueSize().getWidth() != CellWidth || img.getTrueSize().getHeight() != CellHeight)
            && ((img.getTrueSize().getWidth() != CellWidth * 2)
            || (img.getTrueSize().getHeight() != CellHeight * 2)))) {
          continue;
        }
        img = MirLibFactory.getMapLib(fileIndex).GetMirImage(index);
        MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX, drawY,1);
      }
    }
  }

  private void drawBackground(long surface) throws Exception {
    String cleanFilename = FileName.replace(Settings.MapPath, "");
    //long surface = MirJNI.Mir_FillRect(Settings.ScreenWidth,Settings.ScreenHeight,new int[]{0,0,0,0});
    if(cleanFilename.startsWith("ID1") || cleanFilename.startsWith("ID2")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(10);
      MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),0,0,1);
    } else if(cleanFilename.startsWith("ID3_013")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(22);
      MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),0,0,1);
    } else if (cleanFilename.startsWith("ID3_015")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(23);
      MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),0,0,1);
    } else if (cleanFilename.startsWith("ID3_023") || cleanFilename.startsWith("ID3_025")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(21);
      MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),0,0,1);
    }
  }

  private void drawObjects(long surface) throws Exception
  {
    UserObject userObject=getUser();
    for (int y = userObject.Movement.getY() - ViewRangeY; y <= userObject.Movement.getY() + ViewRangeY + 25; y++) {
      if (y <= 0) {
        continue;
      }
      if (y >= Height) {
        break;
      }
      for (int x = userObject.Movement.getX() - ViewRangeX; x <= userObject.Movement.getX() + ViewRangeX; x++) {
        if (x < 0) {
          continue;
        }
        if (x >= Width) {
          break;
        }
        M2CellInfo[x][y].drawDeadObjects(surface);
      }
    }

    for (int y = userObject.Movement.getY() - ViewRangeY; y <= userObject.Movement.getY() + ViewRangeY + 25; y++) {
      if (y <= 0) {
        continue;
      }
      if (y >= Height) {
        break;
      }
      int drawY = (y - userObject.Movement.getY() + OffSetY + 1) * CellHeight + userObject.OffSetMove.getY();

      for (int x = userObject.Movement.getX() - ViewRangeX; x <= userObject.Movement.getX() + ViewRangeX; x++) {
        if (x < 0) {
          continue;
        }
        if (x >= Width) {
          break;
        }
        int drawX = (x - userObject.Movement.getX() + OffSetX) * CellWidth - OffSetX + userObject.OffSetMove.getX();
        int index;
        byte animation;
        boolean blend;
        Size s;

        index = M2CellInfo[x][y].TileAnimationImage;
        animation = M2CellInfo[x][y].TileAnimationFrames;
        if ((index > 0) & (animation > 0)) {
          index--;
          int animationoffset = M2CellInfo[x][y].TileAnimationOffset ^ 0x2000;
          index += animationoffset * (AnimationCount % animation);
          MirImage img = MirLibFactory.getMapLib(190).GetMirImage(index);
          MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY,1);
        }

        if ((M2CellInfo[x][y].MiddleIndex > 199) && (M2CellInfo[x][y].MiddleIndex != -1)) {
          index = M2CellInfo[x][y].MiddleImage - 1;
          if (index > 0)
          {
            animation = M2CellInfo[x][y].MiddleAnimationFrame;
            blend = false;
            if ((animation > 0) && (animation < 255))
            {
              if ((animation & 0x0f) > 0)
              {
                blend = true;
                animation &= 0x0f;
              }
              if (animation > 0)
              {
                byte animationTick = M2CellInfo[x][y].MiddleAnimationTick;
                index += (AnimationCount % (animation + (animation * animationTick))) / (1 + animationTick);

                if (blend && (animation == 10 || animation == 8)) //diamond mines, abyss blends
                {
                  MirImage img = MirLibFactory.getMapLib(M2CellInfo[x][y].MiddleIndex).GetMirImage(index);
                  MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY,1);
                  //Libraries.MapLibs[M2CellInfo[x][y].MiddleIndex].DrawUpBlend(index, new Point(drawX, drawY));
                }
                else
                {
                  MirImage img = MirLibFactory.getMapLib(M2CellInfo[x][y].MiddleIndex).GetMirImage(index);
                  MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY,1);
                  //Libraries.MapLibs[M2CellInfo[x][y].MiddleIndex].DrawUp(index, drawX, drawY);
                }
              }
            }
            MirImage img = MirLibFactory.getMapLib(M2CellInfo[x][y].MiddleIndex).GetMirImage(index);
            s = img.getTrueSize();
            if ((s.getWidth() != CellWidth || s.getHeight() != CellHeight) && (s.getWidth() != (CellWidth * 2) || s.getHeight() != (CellHeight * 2)) && !blend)
            {
              MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY,1);
              //Libraries.MapLibs[M2CellInfo[x][y].MiddleIndex].DrawUp(index, drawX, drawY);
            }
          }
        }

        index = (M2CellInfo[x][y].FrontImage & 0x7FFF) - 1;

        if (index < 0) {
          continue;
        }

        int fileIndex = M2CellInfo[x][y].FrontIndex;
        if (fileIndex == -1) {
          continue;
        }
        animation = M2CellInfo[x][y].FrontAnimationFrame;

        if ((animation & 0x80) > 0) {
          blend = true;
          animation &= 0x7F;
        } else {
          blend = false;
        }

        if (animation > 0) {
          byte animationTick = M2CellInfo[x][y].FrontAnimationTick;
          index += (AnimationCount % (animation + (animation * animationTick))) / (1 + animationTick);
        }


        if (M2CellInfo[x][y].DoorIndex > 0){
          Door DoorInfo = getDoor(M2CellInfo[x][y].DoorIndex);
          if (DoorInfo == null) {
            DoorInfo = Door.builder().index(M2CellInfo[x][y].DoorIndex)
                .doorState((byte)0).imageIndex((byte)0).lastTick(Settings.getTime())
                .build();
            Doors.add(DoorInfo);
          } else {
            if (DoorInfo.getDoorState() != 0) {
              index += (DoorInfo.getImageIndex() + 1) * M2CellInfo[x][y].DoorOffset;//'bad' code if you want to use animation but it's gonna depend on the animation > has to be custom designed for the animtion
            }
          }
        }
        MirImage img = MirLibFactory.getMapLib(fileIndex).GetMirImage(index);
        s = img.getTrueSize();
        if (s.getWidth() == CellWidth && s.getHeight() == CellHeight && animation == 0) {
          continue;
        }
        if ((s.getWidth() == CellWidth * 2) && (s.getHeight() == CellHeight * 2) && (animation == 0)) {
          continue;
        }

        if (blend) {
          if ((fileIndex > 99) & (fileIndex < 199)) {
            //img = MirLibFactory.getMapLib(fileIndex).GetMirImage(index);
            MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY - (3 * CellHeight),1);
            //Libraries.MapLibs[fileIndex]
            //    .DrawBlend(index, new Point(drawX, drawY - (3 * CellHeight)), Color.White, true);
          } else {
            //img = MirLibFactory.getMapLib(fileIndex).GetMirImage(index);
            MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY - s.getHeight(),1);
            //Libraries.MapLibs[fileIndex]
            //    .DrawBlend(index, new Point(drawX, drawY - s.Height), Color.White, (index >= 2723 && index <= 2732));
          }
        } else {
          MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY - s.getHeight(),1);
          //Libraries.MapLibs[fileIndex].Draw(index, drawX, drawY - s.Height);
        }
      }

      for (int x = userObject.Movement.getX() - ViewRangeX; x <= userObject.Movement.getX() + ViewRangeX; x++) {
        if (x < 0) {
          continue;
        }
        if (x >= Width) {
          break;
        }
        M2CellInfo[x][y].drawObjects(surface);
      }
    }

//    DXManager.Sprite.Flush();
//    float oldOpacity = DXManager.Opacity;
//    DXManager.SetOpacity(0.4F);
//
//    //MapObject.User.DrawMount();
//
//    MapObject.User.drawBody();
//
//    if ((MapObject.User.Direction == MirDirection.Up) ||
//        (MapObject.User.Direction == MirDirection.UpLeft) ||
//        (MapObject.User.Direction == MirDirection.UpRight) ||
//        (MapObject.User.Direction == MirDirection.Right) ||
//        (MapObject.User.Direction == MirDirection.Left)) {
//      MapObject.User.DrawHead();
//      MapObject.User.DrawWings();
//    } else {
//      MapObject.User.DrawWings();
//      MapObject.User.DrawHead();
//    }
//
//    DXManager.SetOpacity(oldOpacity);
//
//    if (MapObject.MouseObject != null && !MapObject.MouseObject.Dead && MapObject.MouseObject != MapObject.TargetObject && MapObject.MouseObject.Blend) {//Far
//      MapObject.MouseObject.DrawBlend();
//    }
//
//    if (MapObject.TargetObject != null) {
//      MapObject.TargetObject.DrawBlend();
//    }
//
//    for (int i = 0; i < Objects.size(); i++) {
//      Objects[i].DrawEffects(Settings.Effect);
//
//      if (Settings.NameView && !(Objects[i] is ItemObject) && !Objects[i].Dead){
//        Objects[i].DrawName();
//      }
//
//      Objects[i].DrawChat();
//      Objects[i].DrawHealth();
//      Objects[i].DrawPoison();
//
//      Objects[i].DrawDamages();
//    }
//
//
//    if (!Settings.Effect) {
//      return;
//    }
//
//    for (int i = Effects.size() - 1; i >= 0; i--) {
//      Effects.get(i).Draw();
//    }
  }

}
