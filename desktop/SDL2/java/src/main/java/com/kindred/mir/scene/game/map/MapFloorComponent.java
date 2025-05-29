package com.kindred.mir.scene.game.map;

import com.kindred.mir.GameCommon.Door;
import com.kindred.mir.Settings;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirImage.ImageEffect;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.libs.map.MapCellInfo;
import com.kindred.mir.util.Size;
import java.util.List;

public class MapFloorComponent extends MapCommonComponent {

  private boolean isFloorValid=false;

  public MapFloorComponent(int width, int height,
      int mapWidth, int mapHeight,
      int viewRangeX,
      int viewRangeY,
      int offSetX,
      int offSetY,
      MapCellInfo[][] M2CellInfo,
      List<Door> doors
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
  }

  /**
   * 刷新floor surface
   * @throws Exception
   */
  @Override
  protected final void updateSurface(
      int userMoveX,//getUser().Movement.getY()
      int userMoveY,
      int userOffsetMoveX,//getUser().OffSetMove.getY()
      int userOffsetMoveY,
      long targetSurface
  ) throws Exception{
    int index;
    int drawY, drawX;

    //getUser().Movement.getY()
    for (int y = userMoveY - viewRangeY; y <= userMoveY + viewRangeY; y++) {
      if (y <= 0 || y % 2 == 1) {
        continue;
      }
      if (y >= mapHeight) {
        break;
      }
      drawY = (y - userMoveY + offSetY) * Settings.CellHeight + userOffsetMoveY; //Moving OffSet

      for (int x = userMoveX - viewRangeX; x <= userMoveX + viewRangeX; x++) {
        if (x <= 0 || x % 2 == 1) {
          continue;
        }
        if (x >= mapWidth) {
          break;
        }
        drawX = (x - userMoveX + offSetX) * Settings.CellWidth - offSetX + userOffsetMoveX; //Moving OffSet
        if ((M2CellInfo[x][y].BackImage == 0) || (M2CellInfo[x][y].BackIndex == -1)) {
          continue;
        }
        index = (M2CellInfo[x][y].BackImage & 0x1FFFF) - 1;
        MirImage img = MirLibFactory.GetMirMapImage(M2CellInfo[x][y].BackIndex,index);
        drawToSurface(targetSurface,img.getSurface(ImageEffect.None),false,drawX,drawY);
      }
    }

    for (int y = userMoveY - viewRangeY; y <= userMoveY + viewRangeY + 5; y++) {
      if (y <= 0) {
        continue;
      }
      if (y >= mapHeight) {
        break;
      }
      drawY = (y - userMoveY + offSetY) * Settings.CellHeight + userOffsetMoveY; //Moving OffSet

      for (int x = userMoveX - viewRangeX; x <= userMoveX + viewRangeX; x++) {
        if (x < 0) {
          continue;
        }
        if (x >= mapWidth) {
          break;
        }
        drawX = (x - userMoveX + offSetX) * Settings.CellWidth - offSetX + userOffsetMoveX; //Moving OffSet

        index = M2CellInfo[x][y].MiddleImage - 1;

        if ((index < 0) || (M2CellInfo[x][y].MiddleIndex == -1)) {
          continue;
        }
        MirImage img = MirLibFactory.GetMirMapImage(M2CellInfo[x][y].MiddleIndex,index);
        if (M2CellInfo[x][y].MiddleIndex > 199){//mir3 mid layer is same level as front layer not real middle + it cant draw index -1 so 2 birds in one stone :p
          Size s = img.getTrueSize();

          if (s.getWidth() != Settings.CellWidth || s.getHeight() != Settings.CellHeight) {
            continue;
          }
        }
        drawToSurface(targetSurface,img.getSurface(ImageEffect.None),false,drawX,drawY);
      }
    }

    for (int y = userMoveY - viewRangeY; y <= userMoveY + viewRangeY + 5; y++) {
      if (y <= 0) {
        continue;
      }
      if (y >= mapHeight) {
        break;
      }
      drawY = (y - userMoveY + offSetY) * Settings.CellHeight + userOffsetMoveY; //Moving OffSet

      for (int x = userMoveX - viewRangeX; x <= userMoveX + viewRangeX; x++)
      {
        if (x < 0) {
          continue;
        }
        if (x >= mapWidth) {
          break;
        }
        drawX = (x - userMoveX + offSetX) * Settings.CellWidth - offSetX + userOffsetMoveX; //Moving OffSet

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
        MirImage img = MirLibFactory.GetMirMapImage(fileIndex,index);
        if (M2CellInfo[x][y].DoorIndex > 0){
          Door DoorInfo = getDoor(M2CellInfo[x][y].DoorIndex);
          if (DoorInfo == null) {
            DoorInfo = Door.builder().index(M2CellInfo[x][y].DoorIndex)
                .doorState((byte)0).imageIndex((byte)0).lastTick(Settings.getTime())
                .build();
            doors.add(DoorInfo);
          } else {
            if (DoorInfo.getDoorState() != 0) {
              index += (DoorInfo.getImageIndex() + 1) * M2CellInfo[x][y].DoorOffset;//'bad' code if you want to use animation but it's gonna depend on the animation > has to be custom designed for the animtion
            }
          }
        }

        if (index < 0
            || ((img.getTrueSize().getWidth() != Settings.CellWidth || img.getTrueSize().getHeight() != Settings.CellHeight)
            && ((img.getTrueSize().getWidth() != Settings.CellWidth * 2)
            || (img.getTrueSize().getHeight() != Settings.CellHeight * 2)))) {
          continue;
        }
        img = MirLibFactory.GetMirMapImage(fileIndex,index);
        drawToSurface(targetSurface,img.getSurface(ImageEffect.None),false,drawX,drawY);
      }
    }
  }

  public void processdoors()
  {
    for (int i = 0; i < doors.size(); i++)
    {
      if ((doors.get(i).getDoorState() == 1) || (doors.get(i).getDoorState() == 3))
      {
        if (doors.get(i).getLastTick() + 50 < Settings.getTime())
        {
          doors.get(i).setLastTick(Settings.getTime());
          doors.get(i).setImageIndex((byte)(doors.get(i).getImageIndex()+1));
          if (doors.get(i).getImageIndex() == 1)//change the 1 if you want to actualy animate doors opening/closing
          {
            doors.get(i).setImageIndex((byte)0);
            doors.get(i).setDoorState((byte)(doors.get(i).getDoorState()+1 % 4));
          }
          isFloorValid = false;
        }
      }
      if (doors.get(i).getDoorState() == 2)
      {
        if (doors.get(i).getLastTick() + 5000 < Settings.getTime())
        {
          doors.get(i).setLastTick(Settings.getTime());
          doors.get(i).setDoorState((byte)3);
          isFloorValid = false;
        }
      }
    }
  }
}
