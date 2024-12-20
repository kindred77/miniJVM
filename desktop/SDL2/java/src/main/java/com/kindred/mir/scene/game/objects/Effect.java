package com.kindred.mir.scene.game.objects;

import com.kindred.mir.libs.MirLib;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;

public class Effect {
  public MirLib Library;

  public int BaseIndex, Count, Duration;
  public long Start;

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

  public event EventHandler Complete;
  public event EventHandler Played;
}
