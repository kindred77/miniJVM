package com.kindred.mir.scene.game.objects;

import com.kindred.mir.GameCommon.BuffType;
import com.kindred.mir.GameCommon.MirAction;
import com.kindred.mir.GameCommon.MirDirection;
import com.kindred.mir.GameCommon.ObjectType;
import com.kindred.mir.GameCommon.PoisonType;
import com.kindred.mir.controls.MirLabel;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.objects.effects.Effect;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

public abstract class MapObject {

  public static class QueuedAction
  {
    public MirAction Action;
    public Point Location;
    public MirDirection Direction;
    public List<Object> Params;
  }

  //public static Font ChatFont = new Font(Settings.FontName, 10F);
  public static List<MirLabel> LabelList = new ArrayList<MirLabel>();

  public static UserObject User;
  public static MapObject MouseObject, TargetObject, MagicObject;

  protected ObjectType race;

  public long ObjectID;
  public String Name = "";
  public Point CurrentLocation, MapLocation;
  public MirDirection Direction;
  public boolean isDead, isHidden, isSitDown, isSneaking;
  public PoisonType Poison;
  public long DeadTime;
  public byte AI;
  public boolean isInTrapRock;

  public boolean isBlend = true;

  public long TradeGoldAmount;

  public byte PercentHealth;
  public long HealthTime;

  public List<QueuedAction> ActionFeed = new ArrayList<QueuedAction>();
  public QueuedAction getNextAction() {
    return ActionFeed.size() > 0 ? ActionFeed.get(0) : null;
  }

  public List<Effect> Effects = new ArrayList<>();
  public List<BuffType> Buffs = new ArrayList<>();

  public MirLib BodyLibrary;
  public Color DrawColour = Color.White, NameColour = Color.White, LightColour = Color.White;
  public MirLabel NameLabel, ChatLabel, GuildLabel;
  public long ChatTime;
  public int DrawFrame, DrawWingFrame;
  public Point DrawLocation, Movement, FinalDrawLocation, OffSetMove;
  public Rectangle DisplayRectangle;
  public int Light, DrawY;
  public long NextMotion, NextMotion2;
  public MirAction CurrentAction;
  public boolean SkipFrames;

  //Sound
  public int StruckWeapon;

  public MirLabel TempLabel;

  public static List<MirLabel> DamageLabelList = new ArrayList<>();
  public List<Damage> Damages = new ArrayList<>();

  public abstract boolean getIsBlocking();
  public abstract void Process();

  public ObjectType getRace() {
    return this.race;
  }

  public void setRace(ObjectType race) {
    this.race=race;
  }
}
