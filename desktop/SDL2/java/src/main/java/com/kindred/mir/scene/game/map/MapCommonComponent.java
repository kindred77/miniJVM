package com.kindred.mir.scene.game.map;

import com.kindred.mir.GameCommon.Door;
import com.kindred.mir.Settings;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirImage.ImageEffect;
import com.kindred.mir.libs.map.MapCellInfo;
import com.kindred.mir.scene.game.objects.MapObject;
import com.kindred.mir.scene.game.objects.UserObject;
import java.util.ArrayList;
import java.util.List;

public abstract class MapCommonComponent {

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

  public MapCommonComponent(int width, int height,
      int mapWidth, int mapHeight,
      int viewRangeX,
      int viewRangeY,
      int offSetX,
      int offSetY,
      MapCellInfo[][] M2CellInfo,
      List<Door> doors) {

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

  protected final void drawUp(
      long targetSurface,
      MirImage img,
      boolean isBlend,
      int x, int y)
  {
    if (x >= Settings.ScreenWidth)
      return;

    y -= img.getHeight();
    if (y >= Settings.ScreenHeight)
      return;
    if (x + img.getWidth() < 0 || y + img.getHeight() < 0)
      return;

    drawToSurface(targetSurface, img.getSurface(ImageEffect.None), isBlend, x, y);
  }

  protected final void drawToSurface(
      long targetSurface,
      long srcSurface,
      boolean isBlend,
      int x, int y) {
    if(!isBlend) {
      MirJNI.Mir_SurfaceBlendNormalTransparent(targetSurface,srcSurface,x, y,null, 1,0,0,0);
    } else {
      MirJNI.Mir_SurfaceBlendAddTransparent(targetSurface,
          srcSurface, x, y, null,
          1,0,0,0);
    }
  }

  protected abstract void updateSurface(
      int userMoveX,//getUser().Movement.getY()
      int userMoveY,
      int userOffsetMoveX,//getUser().OffSetMove.getY()
      int userOffsetMoveY,
      long targetSurface) throws Exception;

}
