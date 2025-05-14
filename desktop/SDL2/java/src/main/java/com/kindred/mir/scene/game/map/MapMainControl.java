package com.kindred.mir.scene.game.map;

import com.kindred.mir.Env;
import com.kindred.mir.GameCommon.ChatType;
import com.kindred.mir.GameCommon.Door;
import com.kindred.mir.Settings;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithTexture;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.libs.map.MapCellInfo;
import com.kindred.mir.libs.map.MirMap;
import com.kindred.mir.scene.MirScene.SceneEnumType;
import com.kindred.mir.scene.game.GameScene;
import com.kindred.mir.scene.game.objects.MapObject;
import com.kindred.mir.scene.game.objects.MonsterObject;
import com.kindred.mir.scene.game.objects.effects.Effect;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;
import java.util.ArrayList;
import java.util.List;

public class MapMainControl extends MirControlWithTexture {

  public static List<MapObject> Objects = new ArrayList<MapObject>();

  private String fileNameWithoutSuffix;
  private String fileName;
  private Point mouseLocation;
  private boolean isUpgradingAction;
  private boolean isAutoRun;
  private List<Effect> effects = new ArrayList<>();
  private int music, setMusic;

  protected MapCellInfo[][] M2CellInfo;
  protected List<Door> doors = new ArrayList<Door>();
  protected int mapWidth, mapHeight;
  protected int offSetX,offSetY;
  protected int viewRangeX,viewRangeY;

  private MapBackGroundComponent backGroundComponent;
  private MapFloorComponent floorComponent;
  private MapObjectsComponent objectsComponent;

  public MapMainControl(MirControl parent, long renderer_id, String fileName) throws Exception{
    super(parent, renderer_id);
    this.fileNameWithoutSuffix=fileName;
    initMap();

    //MapButtons = MouseButtons.None;

    offSetX = Settings.ScreenWidth / 2 / MapCommonComponent.CellWidth;
    offSetY = Settings.ScreenHeight / 2 / MapCommonComponent.CellHeight - 1;

    viewRangeX = offSetX + 4;
    viewRangeY = offSetY + 4;

    setSize(new Size(Settings.ScreenWidth, Settings.ScreenHeight));

    //DrawControlTexture = true;
    setBackColor(Color.Black);


//    MouseDown += OnMouseDown;
//    MouseMove += (o, e) => MouseLocation = e.Location;
//    Click += OnMouseClick;

    backGroundComponent=new MapBackGroundComponent(
        this.getSize().getWidth(),
        this.getSize().getHeight(),
        mapWidth,
        mapHeight,
        viewRangeX,
        viewRangeY,
        offSetX,
        offSetY,
        M2CellInfo,
        doors,
        this.fileName);

    floorComponent=new MapFloorComponent(
        this.getSize().getWidth(),
        this.getSize().getHeight(),
        mapWidth,
        mapHeight,
        viewRangeX,
        viewRangeY,
        offSetX,
        offSetY,
        M2CellInfo,
        doors);

    objectsComponent=new MapObjectsComponent(
        this.getSize().getWidth(),
        this.getSize().getHeight(),
        mapWidth,
        mapHeight,
        viewRangeX,
        viewRangeY,
        offSetX,
        offSetY,
        M2CellInfo,
        doors);

  }

  private void initMap() throws Exception
  {
    if (Env.ActiveScene!=null && Env.ActiveScene.getSceneType() == SceneEnumType.Game) {
      ((GameScene)Env.ActiveScene).npcDialog.setIsVisible(false);
    }
    Objects.clear();
    effects.clear();
    doors.clear();

//    if (User != null) {
//      Objects.Add(User);
//    }

    MapObject.MouseObject = null;
    MapObject.TargetObject = null;
    MapObject.MagicObject = null;
    MirMap mirMap = new MirMap(fileNameWithoutSuffix);
    this.fileName=mirMap.getFileName();
    M2CellInfo = mirMap.getMapCells();
    mapWidth = mirMap.getWidth();
    mapHeight = mirMap.getHeight();

    try {
      if (setMusic != music) {
        //SoundManager.Device.Dispose();
        //SoundManager.Create();
        SoundManager.playMusic(music, true);
      }
    } catch (Exception e) {
      // Do nothing. index was not valid.
    }

    setMusic = music;
    SoundList.Music = music;
  }

  public Point getMapLocation() {
    if (GameScene.getUser() == null) {
      return Point.Empty;
    } else {
      return Point.add(
          new Point(mouseLocation.getX() / MapCommonComponent.CellWidth - offSetX,
              mouseLocation.getY() / MapCommonComponent.CellHeight - offSetY),
          GameScene.getUser().CurrentLocation
      );
    }
  }

  public MapObject getObject(long targetID)
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

  public void removeEffect(Effect effect) {
    this.effects.remove(effect);
  }

  public void process()
  {
    floorComponent.processdoors();
    MapObject.User.process();

    for (int i = Objects.size() - 1; i >= 0; i--) {
      MapObject ob = Objects.get(i);
      if (ob == MapObject.User) {
        continue;
      }
      //  if (ob.ActionFeed.Count > 0 || ob.Effects.Count > 0 || GameScene.CanMove || CMain.Time >= ob.NextMotion)
      ob.process();
    }

    for (int i = effects.size() - 1; i >= 0; i--) {
      effects.get(i).Process();
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
      if (y >= mapHeight) {
        continue;
      }
      if (y < 0) {
        break;
      }
      for (int x = getMapLocation().getX() + 2; x >= getMapLocation().getX() - 2; x--) {
        if (x >= mapWidth) {
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

  public final void updateSurface(
      int userMoveX,//getUser().Movement.getY()
      int userMoveY,
      int userOffsetMoveX,//getUser().OffSetMove.getY()
      int userOffsetMoveY) throws Exception {
    long surface = MirJNI.Mir_FillRect(getSize().getWidth(),getSize().getHeight(),new int[]{0,0,0,255});
    //floorComponent.updateSurface(userMoveX, userMoveY, userOffsetMoveX, userOffsetMoveY,surface);
    //backGroundComponent.updateSurface(userMoveX, userMoveY, userOffsetMoveX, userOffsetMoveY,surface);
    objectsComponent.updateSurface(userMoveX, userMoveY, userOffsetMoveX, userOffsetMoveY,surface);
    updateTexture(surface);
  }
}
