package com.kindred.mir.scene.game.objects;

import com.kindred.mir.Settings;
import com.kindred.mir.controls.MirLabel;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;

public class Damage {
  public String Text;
  public Color Color;
  public int Distance=50;
  public long ExpireTime;
  public double Factor;
  public int Offset;

  MirLabel DamageLabel;

  public Damage(String text, int duration, Color colour, int distance)
  {
    ExpireTime = (long)(Settings.getTime() + duration);
    Text = text;
    Distance = distance;
    Factor = duration / this.Distance;
    Color = colour;
  }

//  public void Draw(Point displayLocation)
//  {
//    long timeRemaining = ExpireTime - Settings.getTime();
//
//    if (DamageLabel == null)
//    {
//      DamageLabel = new MirLabel
//      {
//        AutoSize = true,
//            BackColour = Color.Transparent,
//            ForeColour = Colour,
//            OutLine = true,
//            OutLineColour = Color.Black,
//            Text = Text,
//            Font = new Font(Settings.FontName, 10F, FontStyle.Bold)
//      };
//      DamageLabel.Disposing += label_Disposing;
//
//      MapObject.DamageLabelList.Add(DamageLabel);
//    }
//
//    displayLocation.Offset((int)(15 - (Text.Length * 3)), (int)(((int)((double)timeRemaining / Factor)) - Distance) - 75 - Offset);
//
//    DamageLabel.Location = displayLocation;
//    DamageLabel.Draw();
//  }
//
//  private void label_Disposing(object sender, EventArgs e)
//  {
//    MapObject.DamageLabelList.Remove(DamageLabel);
//  }
}
