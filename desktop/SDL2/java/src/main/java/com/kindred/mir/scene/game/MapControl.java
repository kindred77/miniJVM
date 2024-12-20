package com.kindred.mir.scene.game;

import com.kindred.mir.Env;
import com.kindred.mir.GameCommon.ChatType;
import com.kindred.mir.GameCommon.Door;
import com.kindred.mir.GameCommon.LightSetting;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithTexture;
import com.kindred.mir.libs.map.MapCellInfo;
import com.kindred.mir.scene.MirScene.SceneEnumType;
import com.kindred.mir.scene.game.objects.MapObject;
import com.kindred.mir.scene.game.objects.UserObject;
import com.kindred.mir.util.Point;
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

  private Texture floorTexture, lightTexture;
  private Surface floorSurface, lightSurface;

  public long OutputDelay;

  private boolean isUpgradingAction;

  private boolean isAutoRun;

  public static boolean AutoHit;

  public int AnimationCount;

  public static List<Effect> Effects = new ArrayList<Effect>();

  public MapControl(MirControl parent, long renderer_id) {
    super(parent, renderer_id);
  }

//  public static UserObject getUser() {
//    return MapObject.User;
//  }
//  public static void setUser(UserObject user) {
//    MapObject.User = user;
//  }

  public static Point getMapLocation() {
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

}
