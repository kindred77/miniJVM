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

public class MapObjectsControl extends MapCommonControl {

  private long objectsSurface=0L;

  public MapObjectsControl(MirControl parent, long renderer_id,
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
    drawObjects(userMoveX, userMoveY, userOffsetMoveX, userOffsetMoveY);
  }

  private void drawObjects(
      int userMoveX,
      int userMoveY,
      int userOffsetMoveX,
      int userOffsetMoveY) throws Exception
  {
    UserObject userObject=getUser();
    for (int y = userMoveY - viewRangeY; y <= userMoveY + viewRangeY + 25; y++) {
      if (y <= 0) {
        continue;
      }
      if (y >= mapHeight) {
        break;
      }
      for (int x = userObject.Movement.getX() - viewRangeX; x <= userObject.Movement.getX() + ViewRangeX; x++) {
        if (x < 0) {
          continue;
        }
        if (x >= mapWidth) {
          break;
        }
        M2CellInfo[x][y].drawDeadObjects(surface);
      }
    }

    for (int y = userObject.Movement.getY() - viewRangeY; y <= userObject.Movement.getY() + ViewRangeY + 25; y++) {
      if (y <= 0) {
        continue;
      }
      if (y >= MapHeight) {
        break;
      }
      int drawY = (y - userObject.Movement.getY() + OffSetY + 1) * CellHeight + userObject.OffSetMove.getY();

      for (int x = userObject.Movement.getX() - ViewRangeX; x <= userObject.Movement.getX() + ViewRangeX; x++) {
        if (x < 0) {
          continue;
        }
        if (x >= MapWidth) {
          break;
        }
        int drawX = (x - userObject.Movement.getX() + OffSetX) * CellWidth - OffSetX + userObject.OffSetMove.getX();
        int index;
        byte animation;
        boolean blend;
        Size s;

        index = M2CellInfo[x][y].TileAnimationImage;
        animation = M2CellInfo[x][y].TileAnimationFrames;
        if ((index > 0) & (animation > 0)) {
          index--;
          int animationoffset = M2CellInfo[x][y].TileAnimationOffset ^ 0x2000;
          index += animationoffset * (AnimationCount % animation);
          MirImage img = MirLibFactory.getMapLib(190).GetMirImage(index);
          MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY,1);
        }

        if ((M2CellInfo[x][y].MiddleIndex > 199) && (M2CellInfo[x][y].MiddleIndex != -1)) {
          index = M2CellInfo[x][y].MiddleImage - 1;
          if (index > 0)
          {
            animation = M2CellInfo[x][y].MiddleAnimationFrame;
            blend = false;
            if ((animation > 0) && (animation < 255))
            {
              if ((animation & 0x0f) > 0)
              {
                blend = true;
                animation &= 0x0f;
              }
              if (animation > 0)
              {
                byte animationTick = M2CellInfo[x][y].MiddleAnimationTick;
                index += (AnimationCount % (animation + (animation * animationTick))) / (1 + animationTick);

                if (blend && (animation == 10 || animation == 8)) //diamond mines, abyss blends
                {
                  MirImage img = MirLibFactory.getMapLib(M2CellInfo[x][y].MiddleIndex).GetMirImage(index);
                  MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY,1);
                  //Libraries.MapLibs[M2CellInfo[x][y].MiddleIndex].DrawUpBlend(index, new Point(drawX, drawY));
                }
                else
                {
                  MirImage img = MirLibFactory.getMapLib(M2CellInfo[x][y].MiddleIndex).GetMirImage(index);
                  MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY,1);
                  //Libraries.MapLibs[M2CellInfo[x][y].MiddleIndex].DrawUp(index, drawX, drawY);
                }
              }
            }
            MirImage img = MirLibFactory.getMapLib(M2CellInfo[x][y].MiddleIndex).GetMirImage(index);
            s = img.getTrueSize();
            if ((s.getWidth() != CellWidth || s.getHeight() != CellHeight) && (s.getWidth() != (CellWidth * 2) || s.getHeight() != (CellHeight * 2)) && !blend)
            {
              MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY,1);
              //Libraries.MapLibs[M2CellInfo[x][y].MiddleIndex].DrawUp(index, drawX, drawY);
            }
          }
        }

        index = (M2CellInfo[x][y].FrontImage & 0x7FFF) - 1;

        if (index < 0) {
          continue;
        }

        int fileIndex = M2CellInfo[x][y].FrontIndex;
        if (fileIndex == -1) {
          continue;
        }
        animation = M2CellInfo[x][y].FrontAnimationFrame;

        if ((animation & 0x80) > 0) {
          blend = true;
          animation &= 0x7F;
        } else {
          blend = false;
        }

        if (animation > 0) {
          byte animationTick = M2CellInfo[x][y].FrontAnimationTick;
          index += (AnimationCount % (animation + (animation * animationTick))) / (1 + animationTick);
        }


        if (M2CellInfo[x][y].DoorIndex > 0){
          Door DoorInfo = getDoor(M2CellInfo[x][y].DoorIndex);
          if (DoorInfo == null) {
            DoorInfo = Door.builder().index(M2CellInfo[x][y].DoorIndex)
                .doorState((byte)0).imageIndex((byte)0).lastTick(Settings.getTime())
                .build();
            Doors.add(DoorInfo);
          } else {
            if (DoorInfo.getDoorState() != 0) {
              index += (DoorInfo.getImageIndex() + 1) * M2CellInfo[x][y].DoorOffset;//'bad' code if you want to use animation but it's gonna depend on the animation > has to be custom designed for the animtion
            }
          }
        }
        MirImage img = MirLibFactory.getMapLib(fileIndex).GetMirImage(index);
        s = img.getTrueSize();
        if (s.getWidth() == CellWidth && s.getHeight() == CellHeight && animation == 0) {
          continue;
        }
        if ((s.getWidth() == CellWidth * 2) && (s.getHeight() == CellHeight * 2) && (animation == 0)) {
          continue;
        }

        if (blend) {
          if ((fileIndex > 99) & (fileIndex < 199)) {
            //img = MirLibFactory.getMapLib(fileIndex).GetMirImage(index);
            MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY - (3 * CellHeight),1);
            //Libraries.MapLibs[fileIndex]
            //    .DrawBlend(index, new Point(drawX, drawY - (3 * CellHeight)), Color.White, true);
          } else {
            //img = MirLibFactory.getMapLib(fileIndex).GetMirImage(index);
            MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY - s.getHeight(),1);
            //Libraries.MapLibs[fileIndex]
            //    .DrawBlend(index, new Point(drawX, drawY - s.Height), Color.White, (index >= 2723 && index <= 2732));
          }
        } else {
          MirJNI.Mir_SurfaceBlendAdd(surface,img.getSurface(ImageEffect.None),drawX,drawY - s.getHeight(),1);
          //Libraries.MapLibs[fileIndex].Draw(index, drawX, drawY - s.Height);
        }
      }

      for (int x = userObject.Movement.getX() - ViewRangeX; x <= userObject.Movement.getX() + ViewRangeX; x++) {
        if (x < 0) {
          continue;
        }
        if (x >= MapWidth) {
          break;
        }
        M2CellInfo[x][y].drawObjects(surface);
      }
    }

//    DXManager.Sprite.Flush();
//    float oldOpacity = DXManager.Opacity;
//    DXManager.SetOpacity(0.4F);
//
//    //MapObject.User.DrawMount();
//
//    MapObject.User.drawBody();
//
//    if ((MapObject.User.Direction == MirDirection.Up) ||
//        (MapObject.User.Direction == MirDirection.UpLeft) ||
//        (MapObject.User.Direction == MirDirection.UpRight) ||
//        (MapObject.User.Direction == MirDirection.Right) ||
//        (MapObject.User.Direction == MirDirection.Left)) {
//      MapObject.User.DrawHead();
//      MapObject.User.DrawWings();
//    } else {
//      MapObject.User.DrawWings();
//      MapObject.User.DrawHead();
//    }
//
//    DXManager.SetOpacity(oldOpacity);
//
//    if (MapObject.MouseObject != null && !MapObject.MouseObject.Dead && MapObject.MouseObject != MapObject.TargetObject && MapObject.MouseObject.Blend) {//Far
//      MapObject.MouseObject.DrawBlend();
//    }
//
//    if (MapObject.TargetObject != null) {
//      MapObject.TargetObject.DrawBlend();
//    }
//
//    for (int i = 0; i < Objects.size(); i++) {
//      Objects[i].DrawEffects(Settings.Effect);
//
//      if (Settings.NameView && !(Objects[i] is ItemObject) && !Objects[i].Dead){
//        Objects[i].DrawName();
//      }
//
//      Objects[i].DrawChat();
//      Objects[i].DrawHealth();
//      Objects[i].DrawPoison();
//
//      Objects[i].DrawDamages();
//    }
//
//
//    if (!Settings.Effect) {
//      return;
//    }
//
//    for (int i = Effects.size() - 1; i >= 0; i--) {
//      Effects.get(i).Draw();
//    }
  }
}
