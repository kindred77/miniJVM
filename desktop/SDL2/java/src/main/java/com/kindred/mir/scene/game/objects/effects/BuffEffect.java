package com.kindred.mir.scene.game.objects.effects;

import com.kindred.mir.GameCommon;
import com.kindred.mir.GameCommon.BuffType;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.objects.MapObject;

public class BuffEffect extends Effect {

  public BuffEffect(MirLib library, int baseIndex, int count, int duration,
      MapObject owner, long starttime, boolean drawBehind) {
    super(library, baseIndex, count, duration, owner, starttime, drawBehind);
  }
  public GameCommon.BuffType BuffType;
//
//  public BuffEffect(MLibrary library, int baseIndex, int count, int duration, MapObject owner, bool blend, BuffType buffType)
//    : base(library, baseIndex, count, duration, owner, 0)
//  {
//    Repeat = true;
//    Blend = blend;
//    BuffType = buffType;
//    Light = -1;
//  }
//
//  public override void Process()
//  {
//    base.Process();
//  }
}
