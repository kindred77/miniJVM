package com.kindred.mir.scene.game.objects;

import com.kindred.mir.GameCommon.MapObjectType;
import com.kindred.mir.GameCommon.MirDirection;
import com.kindred.mir.GameCommon.PoisonType;
import com.kindred.mir.controls.MirLabel;
import com.kindred.mir.util.Point;
import java.util.ArrayList;
import java.util.List;

public abstract class MapObject {
  //public static Font ChatFont = new Font(Settings.FontName, 10F);
  public static List<MirLabel> LabelList = new ArrayList<MirLabel>();

  public static UserObject User;
  public static MapObject MouseObject, TargetObject, MagicObject;
  public abstract MapObjectType getRace();
  public abstract boolean getIsBlocking();

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
}
