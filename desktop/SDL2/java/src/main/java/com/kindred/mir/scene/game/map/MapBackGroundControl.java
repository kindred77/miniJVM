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
import java.io.File;
import java.util.List;

public class MapBackGroundControl extends MapCommonControl {

  private String fileName = "";

  public MapBackGroundControl(MirControl parent, long renderer_id,
      int width, int height,
      int mapWidth, int mapHeight,
      int viewRangeX,
      int viewRangeY,
      int offSetX,
      int offSetY,
      MapCellInfo[][] M2CellInfo,
      List<Door> doors,
      String fileName
  ) {
    super(
        parent,
        renderer_id,
        width,
        height,
        mapWidth,
        mapHeight,
        viewRangeX,
        viewRangeY,
        offSetX,
        offSetY,
        M2CellInfo,
        doors
    );

    this.fileName=fileName;
  }

  @Override
  public final void updateSurface(
      int userMoveX,
      int userMoveY,
      int userOffsetMoveX,
      int userOffsetMoveY
  ) throws Exception {

    if(surface==0L){
      surface = MirJNI.Mir_FillRect(getSize().getWidth(),getSize().getHeight(),new int[]{0,0,0,0});
    }
    File file=new File(fileName);
    if(!file.exists() || !file.isFile()){
      throw new Exception(fileName+" 不是文件!");
    }

    String cleanFilename = file.getName();
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
}
