package com.kindred.mir.scene.game.map;

import com.kindred.mir.GameCommon.Door;
import com.kindred.mir.Settings;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirImage.ImageEffect;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.libs.map.MapCellInfo;
import com.kindred.mir.scene.game.objects.UserObject;
import com.kindred.mir.util.Size;
import java.util.List;

public class MapBackGroundControl extends MapCommonControl {

  private long backGroundSurface=0L;

  public MapBackGroundControl(MirControl parent, long renderer_id,
      int width, int height,
      int mapWidth, int mapHeight,
      int viewRangeX,
      int viewRangeY,
      int offSetX,
      int offSetY,
      MapCellInfo[][] M2CellInfo,
      List<Door> doors
  ) {
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

  public void update(
      int userMoveX,//getUser().Movement.getY()
      int userMoveY,
      int userOffsetMoveX,//getUser().OffSetMove.getY()
      int userOffsetMoveY
  ) throws Exception {
    drawBackground(userMoveX, userMoveY, userOffsetMoveX, userOffsetMoveY);
  }

  private void drawBackground(
      int userMoveX,
      int userMoveY,
      int userOffsetMoveX,
      int userOffsetMoveY
  ) throws Exception {
    String cleanFilename = FileName.replace(Settings.MapPath, "");
    //long surface = MirJNI.Mir_FillRect(Settings.ScreenWidth,Settings.ScreenHeight,new int[]{0,0,0,0});
    if(cleanFilename.startsWith("ID1") || cleanFilename.startsWith("ID2")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(10);
      MirJNI.Mir_SurfaceBlendAdd(backGroundSurface,img.getSurface(ImageEffect.None),0,0,1);
    } else if(cleanFilename.startsWith("ID3_013")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(22);
      MirJNI.Mir_SurfaceBlendAdd(backGroundSurface,img.getSurface(ImageEffect.None),0,0,1);
    } else if (cleanFilename.startsWith("ID3_015")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(23);
      MirJNI.Mir_SurfaceBlendAdd(backGroundSurface,img.getSurface(ImageEffect.None),0,0,1);
    } else if (cleanFilename.startsWith("ID3_023") || cleanFilename.startsWith("ID3_025")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(21);
      MirJNI.Mir_SurfaceBlendAdd(backGroundSurface,img.getSurface(ImageEffect.None),0,0,1);
    }
  }
}
