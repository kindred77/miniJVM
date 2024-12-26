package com.kindred.mir.scene.game.objects.effects;

import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.objects.MapObject;

public class LightEffect extends Effect {

  public LightEffect(MirLib library, int baseIndex, int count, int duration,
      MapObject owner, long starttime, boolean drawBehind) {
    super(library, baseIndex, count, duration, owner, starttime, drawBehind);
  }

//  public LightEffect(int duration, MapObject owner, long starttime = 0, int lightDistance = 6, Color? lightColour = null)
//    : base(null, 0, 0, duration, owner, starttime)
//  {
//    Light = lightDistance;
//    LightColour = lightColour == null ? Color.White : (Color)lightColour;
//  }
//
//  public LightEffect(int duration, Point source, long starttime = 0, int lightDistance = 6, Color? lightColour = null)
//    : base(null, 0, 0, duration, source, starttime)
//  {
//    Light = lightDistance;
//    LightColour = lightColour == null ? Color.White : (Color)lightColour;
//  }
//
//  public override void Process()
//  {
//    if (CMain.Time >= Start + Duration)
//      Remove();
//    GameScene.Scene.MapControl.TextureValid = false;
//
//  }
//
//  public override void Draw()
//  {
//
//    if (CMain.Time < Start) return;
//
//
//    if (Owner != null)
//    {
//      DrawLocation = Owner.DrawLocation;
//    }
//    else
//    {
//      DrawLocation = new Point((Source.X - MapObject.User.Movement.X + MapControl.OffSetX) * MapControl.CellWidth,
//          (Source.Y - MapObject.User.Movement.Y + MapControl.OffSetY) * MapControl.CellHeight);
//      DrawLocation.Offset(MapObject.User.OffSetMove);
//    }
//  }
}
