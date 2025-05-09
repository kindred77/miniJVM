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

public abstract class MapCommonControl extends MirControlWithTexture {

  public static List<MapObject> Objects = new ArrayList<MapObject>();

  protected long surface=0L;

  public static final int CellWidth = 48;
  public static final int CellHeight = 32;

  protected int offSetX;
  protected int offSetY;

  protected int viewRangeX;
  protected int viewRangeY;

  //public static MouseButtons MapButtons;

//  private long InputDelay;
//  private long NextAction;

  protected MapCellInfo[][] M2CellInfo;
  protected List<Door> doors = new ArrayList<Door>();
  protected int mapWidth, mapHeight;


//  private String Title = "";
//  private int MiniMap, BigMap, Music, SetMusic;
//  private LightSetting Lights;
//  private boolean isLightning, isFire;
//  private byte MapDarkLight;
//  private long LightningTime, FireTime;
//
//  private boolean isFloorValid, isLightsValid;
//
//  private long floorSurface=0L, lightSurface=0L;
//
////  private Texture floorTexture, lightTexture;
////  private Surface floorSurface, lightSurface;
//
//  private long OutputDelay;
//
//
//
//
//
//  private boolean AutoHit;

  public MapCommonControl(MirControl parent, long renderer_id) {
    super(parent, renderer_id);
  }

  public MapCommonControl(MirControl parent, long renderer_id,
      int width, int height,
      int mapWidth, int mapHeight,
      int viewRangeX,
      int viewRangeY,
      int offSetX,
      int offSetY,
      MapCellInfo[][] M2CellInfo,
      List<Door> doors) {
    super(parent, renderer_id);
    setSize(new Size(width, height));

    this.mapWidth=mapWidth;
    this.mapHeight=mapHeight;

    this.offSetX=offSetX;
    this.offSetY=offSetY;
    this.viewRangeX=viewRangeX;
    this.viewRangeY=viewRangeY;

    this.M2CellInfo=M2CellInfo;
    this.doors=doors;
  }

  public Door getDoor(byte Index)
  {
    for (int i = 0; i < doors.size(); i++)
    {
      if (doors.get(i).getIndex() == Index) {
        return doors.get(i);
      }
    }
    return null;
  }

  public UserObject getUser() {
    return MapObject.User;
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

  protected abstract void updateSurface(
      int userMoveX,//getUser().Movement.getY()
      int userMoveY,
      int userOffsetMoveX,//getUser().OffSetMove.getY()
      int userOffsetMoveY) throws Exception;

  public final void updateTexture(
      int userMoveX,//getUser().Movement.getY()
      int userMoveY,
      int userOffsetMoveX,//getUser().OffSetMove.getY()
      int userOffsetMoveY
  ) throws Exception {
    updateSurface(userMoveX, userMoveY, userOffsetMoveX, userOffsetMoveY);
    super.updateTexture(surface);
  }

}
