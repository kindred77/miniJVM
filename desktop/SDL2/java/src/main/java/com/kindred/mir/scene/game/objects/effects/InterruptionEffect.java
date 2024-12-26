package com.kindred.mir.scene.game.objects.effects;

import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.objects.MapObject;
import java.util.ArrayList;
import java.util.List;

public class InterruptionEffect extends Effect {
  public static List<InterruptionEffect> effectlist = new ArrayList<InterruptionEffect>();
  boolean noProcess = false;

  public InterruptionEffect(MirLib library, int baseIndex, int count, int duration,
      MapObject owner, long starttime, boolean drawBehind) {
    super(library, baseIndex, count, duration, owner, starttime, drawBehind);
  }

//  public InterruptionEffect(MirLib library, int baseIndex, int count, int duration, MapObject owner, boolean blend, long starttime = 0) {
//    super(library, baseIndex, count, duration, owner)
//    Repeat = true;
//    Blend = blend;
//    effectlist.Add(this);
//  }
//
//  @Override
//  public void Process()
//  {
//    if (!noProcess) {
//      super.Process();
//    }
//  }
//
//  @Override
//  public void Remove()
//  {
//    base.Remove();
//    effectlist.Remove(this);
//  }
//
//  public override void Draw()
//  {
//    if (!((PlayerObject)Owner).Concentrating)
//      Remove();
//    else if (((PlayerObject)Owner).ConcentrateInterrupted)
//      noProcess = true;
//    else
//      noProcess = false;
//    if (!noProcess)
//      base.Draw();
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
