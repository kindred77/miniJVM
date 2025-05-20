package com.kindred.mir.util;

import com.kindred.mir.GameCommon.BuffType;
import com.kindred.mir.GameCommon.MirDirection;

public class MirUtil {

  public static int BuffImage(BuffType type)
  {
    switch (type)
    {
      //Skills
      case Fury:
        return 76;
      case Rage:
        return 49;
      case ImmortalSkin:
        return 80;
      case CounterAttack:
        return 7;
      case MagicBooster:
        return 73;
      case MagicShield:
        return 30;
      case Hiding:
        return 17;
      case Haste:
        return 60;
      case SoulShield:
        return 13;
      case BlessedArmour:
        return 14;
      case ProtectionField:
        return 50;
      case UltimateEnhancer:
        return 35;
      case Curse:
        return 45;
      case EnergyShield:
        return 57;
      case SwiftFeet:
        return 67;
      case LightBody:
        return 68;
      case MoonLight:
        return 65;
      case DarkBody:
        return 70;
      case Concentration:
        return 96;
      case VampireShot:
        return 100;
      case PoisonShot:
        return 102;
      case MentalState:
        return 199;
      //Special
      case GameMaster:
        return 173;
      case General:
        return 182;
      case Exp:
        return 260;
      case Drop:
        return 162;
      case Gold:
        return 168;
      case Knapsack:
      case BagWeight:
        return 235;
      case Transform:
        return 241;
      case Mentor:
        return 248;
      case Mentee:
        return 248;
      case RelationshipEXP:
        return 201;
      case Guild:
        return 203;
      case Rested:
        return 240;
      case TemporalFlux:
        return 261;
      //Stats
      case Impact:
        return 249;
      case Magic:
        return 165;
      case Taoist:
        return 250;
      case Storm:
        return 170;
      case HealthAid:
        return 161;
      case ManaAid:
        return 169;
      case Defence:
        return 166;
      case MagicDefence:
        return 158;
      case WonderDrug:
        return 252;
      default:
        return 0;
    }
  }

  public static boolean EnumHasFlag(int srcVal, int tgtVal)
  {
    if ((srcVal & tgtVal) == tgtVal)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public static Point PointMove(Point p, MirDirection d, int i)
  {
    switch (d)
    {
      case Up:
        p.Offset(0, -i);
        break;
      case UpRight:
        p.Offset(i, -i);
        break;
      case Right:
        p.Offset(i, 0);
        break;
      case DownRight:
        p.Offset(i, i);
        break;
      case Down:
        p.Offset(0, i);
        break;
      case DownLeft:
        p.Offset(-i, i);
        break;
      case Left:
        p.Offset(-i, 0);
        break;
      case UpLeft:
        p.Offset(-i, -i);
        break;
    }
    return p;
  }

}
