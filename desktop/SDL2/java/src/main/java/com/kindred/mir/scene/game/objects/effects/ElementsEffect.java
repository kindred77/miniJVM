package com.kindred.mir.scene.game.objects.effects;

import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.objects.MapObject;

public class ElementsEffect extends Effect {
  int myType;//1 = green orb, 2 = blue orb, 3 = red orb, 4 = mixed orbs
  long killAt;//holds the exp value for 4 orbs : kills all orbs when myType 4 is reached
  boolean loopit = false;//soundloop

  public ElementsEffect(MirLib library, int baseIndex, int count, int duration,
      MapObject owner, long starttime, boolean drawBehind) {
    super(library, baseIndex, count, duration, owner, starttime, drawBehind);
  }

//  public ElementsEffect(MLibrary library, int baseIndex, int count, int duration, MapObject owner, bool blend, int elementType, int killtime, bool loopon = false)
//    : base(library, baseIndex, count, duration, owner)
//  {
//    Repeat = true;
//    Blend = blend;
//    myType = elementType;
//    killAt = killtime;
//    //
//    loopit = loopon;
//    StopSounds();
//    StartSound();
//  }
//
//  public override void Process()
//  {
//
//    base.Process();
//  }
//
//  private void StartSound()
//  {
//    SoundManager.PlaySound(20000 + 126 * 10 + 4 + myType, loopit);
//  }
//
//  private void StopSounds()
//  {
//    for (int i = 0; i <= 3; i++)
//      SoundManager.StopSound(20000 + 126 * 10 + 5 + i);
//  }
//
//  public override void Remove()
//  {
//    SoundManager.StopSound(20000 + 126 * 10 + 4 + myType);
//    base.Remove();
//  }
//
//  public override void Draw()
//  {
//    if (!((PlayerObject)Owner).HasElements)
//      Remove();
//    if (((PlayerObject)Owner).ElementsLevel >= killAt && myType < 4)
//      Remove();
//    base.Draw();
//  }
}
