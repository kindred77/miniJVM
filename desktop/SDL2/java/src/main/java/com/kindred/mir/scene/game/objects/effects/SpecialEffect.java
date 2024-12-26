package com.kindred.mir.scene.game.objects.effects;

import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.objects.MapObject;

public class SpecialEffect extends Effect {

  public SpecialEffect(MirLib library, int baseIndex, int count, int duration,
      MapObject owner, long starttime, boolean drawBehind) {
    super(library, baseIndex, count, duration, owner, starttime, drawBehind);
  }
//  public uint EffectType = 0;
//
//  public SpecialEffect(MLibrary library, int baseIndex, int count, int duration, MapObject owner, bool blend, bool drawBehind, uint type)
//    : base(library, baseIndex, count, duration, owner, 0, drawBehind)
//  {
//    Blend = blend;
//    DrawBehind = drawBehind;
//    EffectType = type;
//    Light = -1;
//  }
//
//  public override void Process()
//  {
//    base.Process();
//  }
}
