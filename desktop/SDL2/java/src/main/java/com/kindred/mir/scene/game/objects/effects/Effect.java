package com.kindred.mir.scene.game.objects.effects;

import com.kindred.mir.Settings;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.objects.MapObject;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;

public class Effect {
  public MirLib Library;

  public int BaseIndex, Count, Duration;
  public long Start= Settings.getTime();

  public int CurrentFrame;
  public long NextFrame;

  public Point Source;
  public MapObject Owner;

  public int Light = 6;
  public Color LightColour = Color.White;

  public boolean Blend = true;
  public float Rate = 1F;
  public Point DrawLocation;
  public boolean Repeat;
  public long RepeatUntil;

  public boolean DrawBehind = false;

  public long CurrentDelay;
  public long Delay;

  public ControlCommonListener onComplete;
  public ControlCommonListener onPlayed;

  public Effect(MirLib library, int baseIndex, int count, int duration, MapObject owner, long starttime, boolean drawBehind)
  {
    Library = library;
    BaseIndex = baseIndex;
    Count = count == 0 ? 1 : count;
    Duration = duration;
    if (starttime!=0) {
      Start=starttime;
    }

    NextFrame = Start + (Duration / Count) * (CurrentFrame + 1);
    Owner = owner;
    Source = Owner.CurrentLocation;

    DrawBehind = drawBehind;
  }
  public Effect(MirLib library, int baseIndex, int count, int duration, Point source, long starttime, boolean drawBehind)
  {
    Library = library;
    BaseIndex = baseIndex;
    Count = count == 0 ? 1 : count;
    Duration = duration;
    if (starttime!=0) {
      Start=starttime;
    }

    NextFrame = Start + (Duration / Count) * (CurrentFrame + 1);
    Source = source;

    DrawBehind = drawBehind;
  }

  public void SetStart(long start)
  {
    Start = start;

    NextFrame = Start + (Duration / Count) * (CurrentFrame + 1);
  }

//  public void Process()
//  {
//    if (CurrentFrame == 1) {
//      if (onPlayed != null) {
//        onPlayed.doAction(this, null);
//      }
//    }
//    if (Settings.getTime() <= NextFrame) {
//      return;
//    }
//
//    if (Owner != null && Owner.SkipFrames) {
//      CurrentFrame++;
//    }
//
//    if (++CurrentFrame >= Count) {
//      if (Repeat && (RepeatUntil == 0 || Settings.getTime() < RepeatUntil)) {
//        CurrentFrame = 0;
//        Start = Settings.getTime() + Delay;
//        NextFrame = Start + (Duration / Count) * (CurrentFrame + 1);
//      }
//      else {
//        Remove();
//      }
//    } else {
//      NextFrame = Start + (Duration / Count) * (CurrentFrame + 1);
//    }
//
//    GameScene.Scene.MapControl.TextureValid = false;
//  }

//  public void Remove()
//  {
//    if (Owner != null) {
//      Owner.Effects.Remove(this);
//    } else {
//      MapControl.Effects.Remove(this);
//    }
//
//    if (Complete != null) {
//      Complete(this, EventArgs.Empty);
//    }
//  }

  public void Draw()
  {
    if (Settings.getTime() < Start) {
      return;
    }

//    if (Owner != null) {
//      DrawLocation = Owner.DrawLocation;
//    } else {
//      DrawLocation = new Point((Source.X - MapObject.User.Movement.X + MapControl.OffSetX) * MapControl.CellWidth,
//          (Source.Y - MapObject.User.Movement.Y + MapControl.OffSetY) * MapControl.CellHeight);
//      DrawLocation.Offset(MapObject.User.OffSetMove);
//    }
//
//
//    if (Blend) {
//      Library.DrawBlend(BaseIndex + CurrentFrame, DrawLocation, Color.White, true, Rate);
//    } else {
//      Library.Draw(BaseIndex + CurrentFrame, DrawLocation, Color.White, true);
//    }
  }

  public void Clear()
  {
    onComplete = null;
    onPlayed = null;
  }
}
