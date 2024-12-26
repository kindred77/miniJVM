package com.kindred.mir.scene.game.objects.effects;

import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.objects.MapObject;

public class DelayedExplosionEffect extends Effect {
  //public static List<DelayedExplosionEffect> effectlist = new List<DelayedExplosionEffect>();
  public int stage;

  public DelayedExplosionEffect(MirLib library, int baseIndex, int count, int duration,
      MapObject owner, long starttime, boolean drawBehind) {
    super(library, baseIndex, count, duration, owner, starttime, drawBehind);
  }

//  public DelayedExplosionEffect(MLibrary library, int baseIndex, int count, int duration, MapObject owner, bool blend, int Stage, long until)
//    : base(library, baseIndex, count, duration, owner)
//  {
//    Repeat = (Stage == 2) ? false : true;
//    Blend = blend;
//    stage = Stage;
//    effectlist.Add(this);
//    if (stage == 1)
//      SoundManager.PlaySound(20000 + 125 * 10);
//    if (stage == 2)
//      SoundManager.PlaySound(20000 + 125 * 10 + 5);
//  }
//
//  public override void Process()
//  {
//    base.Process();
//  }
//
//  public override void Remove()
//  {
//    base.Remove();
//    effectlist.Remove(this);
//  }
//
//  public override void Draw()
//  {
//    if (Owner.Dead)
//    {
//      Remove();
//      return;
//    }
//    base.Draw();
//  }
//
//  public static int GetOwnerEffectID(uint objectID)
//  {
//    for (int i = 0; i < effectlist.Count; i++)
//    {
//      if (effectlist[i].Owner.ObjectID != objectID) continue;
//      return i;
//    }
//    return -1;
//  }
}
