package com.kindred.mir.scene.game.objects.effects;

import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.objects.MapObject;
import com.kindred.mir.util.Point;
import java.util.ArrayList;
import java.util.List;

public class Missile extends Effect {
  public static List<Missile> Missiles = new ArrayList<Missile>();
  public MapObject Target;
  public Point Destination;
  public int Interval, FrameCount, Skip;
  public int Direction;
  public boolean Explode;

  public Missile(MirLib library, int baseIndex, int count, int duration,
      MapObject owner, long starttime, boolean drawBehind) {
    super(library, baseIndex, count, duration, owner, starttime, drawBehind);
  }

//  public Missile(MirLib library, int baseIndex, int count, int duration, MapObject owner, Point target, boolean direction16 = true) {
//    super(library, baseIndex, count, duration, owner);
//    Missiles.Add(this);
//    Source = Owner.CurrentLocation;
//    Destination = target;
//    Direction = direction16 ? MapControl.Direction16(Source, Destination) : (int)Functions.DirectionFromPoint(Source, Destination);
//  }
//
//  public Missile(MirLib library, int baseIndex, int count, int duration, Point source, Point target) {
//    super(library, baseIndex, count, duration, source)
//    Missiles.Add(this);
//    Destination = target;
//
//    Direction = MapControl.Direction16(Source, Destination);
//
//
//  }
//
//  @Override
//  public void Process()
//  {
//    if (CMain.Time < Start) {
//      return;
//    }
//
//    if (Target != null) {
//      Destination = Target.CurrentLocation;
//    } else if (!Explode) {
//      int dist = Functions.MaxDistance(Owner.CurrentLocation, Destination);
//
//      if (dist < Globals.DataRange) {
//        Destination.Offset(Destination.X - Source.X, Destination.Y - Source.Y);
//      }
//    }
//
//    Duration = Functions.MaxDistance(Source, Destination) * 50;
//    Count = Duration / Interval;
//    if (Count == 0) {
//      Count = 1;
//    }
//
//    super.Process();
//  }
//
//  @Override
//  public void Remove()
//  {
//    super.Remove();
//    Missiles.Remove(this);
//  }
//
//  @Override
//  public void Draw()
//  {
//    if (CMain.Time < Start) {
//      return;
//    }
//
//
//    int index = BaseIndex + (CurrentFrame % FrameCount) + Direction * (Skip + FrameCount);
//
//    DrawLocation = new Point((Source.X - MapObject.User.Movement.X + MapControl.OffSetX) * MapControl.CellWidth,
//        (Source.Y - MapObject.User.Movement.Y + MapControl.OffSetY) * MapControl.CellHeight);
//    DrawLocation.Offset(MapObject.User.OffSetMove);
//
//    int x = (Destination.X - Source.X) * MapControl.CellWidth;
//    int y = (Destination.Y - Source.Y) * MapControl.CellHeight;
//
//
//    DrawLocation.Offset(x * CurrentFrame / Count, y * CurrentFrame / Count);
//
//    if (!Blend) {
//      Library.Draw(index, DrawLocation, Color.White, true);
//    } else {
//      Library.DrawBlend(index, DrawLocation, Color.White, true, Rate);
//    }
//  }
}
