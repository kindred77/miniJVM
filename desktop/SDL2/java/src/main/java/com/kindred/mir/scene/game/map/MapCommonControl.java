package com.kindred.mir.scene.game.map;

import com.kindred.mir.Env;
import com.kindred.mir.GameCommon.ChatType;
import com.kindred.mir.GameCommon.Door;
import com.kindred.mir.GameCommon.LightSetting;
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
import com.kindred.mir.scene.game.GameScene;
import com.kindred.mir.scene.game.objects.MapObject;
import com.kindred.mir.scene.game.objects.MonsterObject;
import com.kindred.mir.scene.game.objects.UserObject;
import com.kindred.mir.scene.game.objects.effects.Effect;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;
import java.util.ArrayList;
import java.util.List;

public class MapCommonControl extends MirControlWithTexture {

  public static List<MapObject> Objects = new ArrayList<MapObject>();

  public static final int CellWidth = 48;
  public static final int CellHeight = 32;

  protected int offSetX;
  protected int offSetY;

  protected int viewRangeX;
  protected int viewRangeY;

  //public static MouseButtons MapButtons;
  private Point MouseLocation;
  private long InputDelay;
  private long NextAction;

  protected MapCellInfo[][] M2CellInfo;
  protected List<Door> doors = new ArrayList<Door>();
  protected int mapWidth, mapHeight;

  private String FileName = "";
  private String Title = "";
  private int MiniMap, BigMap, Music, SetMusic;
  private LightSetting Lights;
  private boolean isLightning, isFire;
  private byte MapDarkLight;
  private long LightningTime, FireTime;

  private boolean isFloorValid, isLightsValid;

  private long floorSurface=0L, lightSurface=0L;

//  private Texture floorTexture, lightTexture;
//  private Surface floorSurface, lightSurface;

  private long OutputDelay;

  private boolean isUpgradingAction;

  private boolean isAutoRun;

  private boolean AutoHit;

  private int AnimationCount;

  private List<Effect> Effects = new ArrayList<>();

  public MapCommonControl(MirControl parent, long renderer_id) {
    super(parent, renderer_id);

    //MapButtons = MouseButtons.None;

    offSetX = Settings.ScreenWidth / 2 / CellWidth;
    offSetY = Settings.ScreenHeight / 2 / CellHeight - 1;

    viewRangeX = offSetX + 4;
    viewRangeY = offSetY + 4;

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
          new Point(MouseLocation.getX() / CellWidth - offSetX, MouseLocation.getY() / CellHeight - offSetY),
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
    doors.clear();

//    if (User != null) {
//      Objects.Add(User);
//    }

    MapObject.MouseObject = null;
    MapObject.TargetObject = null;
    MapObject.MagicObject = null;
    MirMap mirMap = new MirMap(FileName);
    M2CellInfo = mirMap.getMapCells();
    mapWidth = mirMap.getWidth();
    mapHeight = mirMap.getHeight();

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
      if (y >= MapHeight) {
        continue;
      }
      if (y < 0) {
        break;
      }
      for (int x = getMapLocation().getX() + 2; x >= getMapLocation().getX() - 2; x--) {
        if (x >= MapWidth) {
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

}
