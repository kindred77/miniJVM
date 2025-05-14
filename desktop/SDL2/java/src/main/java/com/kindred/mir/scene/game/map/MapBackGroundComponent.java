package com.kindred.mir.scene.game.map;

import com.kindred.mir.GameCommon.Door;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirImage.ImageEffect;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.libs.map.MapCellInfo;
import java.util.List;

public class MapBackGroundComponent extends MapCommonComponent {

  private String cleanFilename;

  public MapBackGroundComponent(
      int width, int height,
      int mapWidth, int mapHeight,
      int viewRangeX,
      int viewRangeY,
      int offSetX,
      int offSetY,
      MapCellInfo[][] M2CellInfo,
      List<Door> doors,
      String mapFileName
  ) {
    super(
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

    this.cleanFilename=mapFileName;
  }

  @Override
  protected final void updateSurface(
      int userMoveX,
      int userMoveY,
      int userOffsetMoveX,
      int userOffsetMoveY,
      long targetSurface
  ) throws Exception {

    if(cleanFilename.startsWith("ID1") || cleanFilename.startsWith("ID2")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(10);
      drawToSurface(targetSurface,img.getSurface(ImageEffect.None),0,0);
    } else if(cleanFilename.startsWith("ID3_013")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(22);
      drawToSurface(targetSurface,img.getSurface(ImageEffect.None),0,0);
    } else if (cleanFilename.startsWith("ID3_015")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(23);
      drawToSurface(targetSurface,img.getSurface(ImageEffect.None),0,0);
    } else if (cleanFilename.startsWith("ID3_023") || cleanFilename.startsWith("ID3_025")) {
      MirImage img = MirLibFactory.getMirLib(MirLibFactory.Background).GetMirImage(21);
      drawToSurface(targetSurface,img.getSurface(ImageEffect.None),0,0);
    }
  }
}
