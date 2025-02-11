package com.kindred.mir.scene.game.objects;

import com.kindred.mir.GameCommon.MirDirection;
import com.kindred.mir.GameCommon.Monster;
import com.kindred.mir.GameCommon.ObjectType;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;

public class MonsterObject extends MapObject {

  @Override
  public ObjectType getRace() {
    return ObjectType.Monster;
  }

  @Override
  public void drawBehindEffects(boolean effectsEnabled) {

  }

  @Override
  public void drawEffects(boolean effectsEnabled) {

  }

  @Override
  public boolean getIsBlocking() {
    return AI == 64 || (AI == 72 && Direction == MirDirection.Left) ? false : !Dead;
  }

  @Override
  public void draw(long surface) {

  }

  public Point ManualLocationOffset() {
    switch (BaseImage) {
      case EvilMir:
        return new Point(-21, -15);
      case PalaceWall2:
      case PalaceWallLeft:
      case PalaceWall1:
      case GiGateSouth:
      case GiGateWest:
      case SSabukWall1:
      case SSabukWall2:
      case SSabukWall3:
        return new Point(-10, 0);
      case GiGateEast:
        return new Point(-45, 7);
      default:
        return new Point(0, 0);
    }
  }

  public Monster BaseImage;
  public byte Effect;
  public boolean Skeleton;

  public FrameSet Frames;
  public Frame Frame;
  public int FrameIndex, FrameInterval, EffectFrameIndex, EffectFrameInterval;

  public long TargetID;
  public Point TargetPoint;

  public boolean Stoned;
  public byte Stage;
  public int BaseSound;

  public long ShockTime;
  public boolean BindingShotCenter;

  public Color OldNameColor;

  @Override
  public void process() {

  }
}
