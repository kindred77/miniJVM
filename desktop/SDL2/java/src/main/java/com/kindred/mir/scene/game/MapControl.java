package com.kindred.mir.scene.game;

import com.kindred.mir.Env;
import com.kindred.mir.GameCommon.ChatType;
import com.kindred.mir.GameCommon.Door;
import com.kindred.mir.GameCommon.LightSetting;
import com.kindred.mir.Settings;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithTexture;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
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

  public Point getMapLocation() {
    if (GameScene.User == null) {
      return Point.Empty;
    } else {
      return Point.add(
          new Point(MouseLocation.getX() / CellWidth - OffSetX, MouseLocation.getY() / CellHeight - OffSetY),
          GameScene.User.CurrentLocation
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

  public void LoadMap() throws Exception
  {
    if (Env.ActiveScene!=null && Env.ActiveScene.getSceneType() == SceneEnumType.Game) {
      ((GameScene)Env.ActiveScene).NPCDialog.setIsVisible(false);
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

  public void Processdoors()
  {
    for (int i = 0; i < Doors.size(); i++)
    {
      if ((Doors.get(i).DoorState == 1) || (Doors.get(i).DoorState == 3))
      {
        if (Doors.get(i).LastTick + 50 < Settings.getTime())
        {
          Doors.get(i).LastTick = Settings.getTime();
          Doors.get(i).ImageIndex++;
          if (Doors.get(i).ImageIndex == 1)//change the 1 if you want to actualy animate doors opening/closing
          {
            Doors.get(i).ImageIndex = 0;
            Doors.get(i).DoorState = (byte)(++Doors.get(i).DoorState % 4);
          }
          isFloorValid = false;
        }
      }
      if (Doors.get(i).DoorState == 2)
      {
        if (Doors.get(i).LastTick + 5000 < Settings.getTime())
        {
          Doors.get(i).LastTick = Settings.getTime();
          Doors.get(i).DoorState = 3;
          isFloorValid = false;
        }
      }
    }
  }

  public void Process()
  {
    Processdoors();
    MapObject.User.Process();

    for (int i = Objects.size() - 1; i >= 0; i--) {
      MapObject ob = Objects.get(i);
      if (ob == MapObject.User) {
        continue;
      }
      //  if (ob.ActionFeed.Count > 0 || ob.Effects.Count > 0 || GameScene.CanMove || CMain.Time >= ob.NextMotion)
      ob.Process();
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

  public static MapObject GetObject(long targetID)
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

}
