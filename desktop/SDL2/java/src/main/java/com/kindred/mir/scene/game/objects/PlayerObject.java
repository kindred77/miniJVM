package com.kindred.mir.scene.game.objects;

import com.kindred.mir.GameCommon.LevelEffects;
import com.kindred.mir.GameCommon.MirClass;
import com.kindred.mir.GameCommon.ObjectType;
import com.kindred.mir.GameCommon.Spell;
import com.kindred.mir.GameCommon.SpellEffect;
import com.kindred.mir.constcode.MirEnums.MirGender;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.objects.effects.Effect;
import com.kindred.mir.scene.game.objects.effects.InterruptionEffect;
import com.kindred.mir.util.Point;

public class PlayerObject extends MapObject {

  public MirGender gender;
  public MirClass mirClass;
  public byte hair;
  public int level;

  public MirLib weaponLibrary1, weaponLibrary2, hairLibrary, wingLibrary, mountLibrary;
  public int armour, weapon, armourOffSet, hairOffSet, weaponOffSet, wingOffset, mountOffset;

  public int dieSound, flinchSound, attackSound;


  public FrameSet frames;
  public Frame frame, wingFrame;
  public int frameIndex, frameInterval, effectFrameIndex, effectFrameInterval, slowFrameIndex;
  public byte skipFrameUpdate = 0;

  public boolean getHasClassWeapon() {
    switch (weapon / 100)
    {
      default:
        return mirClass == MirClass.Wizard || mirClass == MirClass.Warrior || mirClass == MirClass.Taoist;
      case 1:
        return mirClass == MirClass.Assassin;
      case 2:
        return mirClass == MirClass.Archer;
    }
  }

  public boolean  getHasFishingRod() {
    return weapon == 49 || weapon == 50;
  }

  public Spell spell;
  public byte spellLevel;
  public int jumpDistance;
  public boolean cast;
  public long targetID;
  public Point targetPoint;

  public boolean magicShield;
  public Effect shieldEffect;

  public boolean elementalBarrier;
  public Effect elementalBarrierEffect;

  public byte wingEffect;
  private short stanceDelay = 2500;

  //ArcherSpells - Elemental system
  public boolean elementalBuff;
  public boolean concentrating;
  public InterruptionEffect concentratingEffect;
  public boolean concentrateInterrupted;
  public boolean hasElements;
  public boolean elementCasted;
  public int elementEffect;//hold orb count for player(object) load
  public int elementsLevel;
  public int elementOrbMax;
//Elemental system END

  public SpellEffect currentEffect;

  public boolean ridingMount, sprint, fastRun, fishing, foundFish;
  public long stanceTime, mountTime, fishingTime;
  public long blizzardStopTime, reincarnationStopTime, slashingBurstTime;

  public short mountType = -1, transformType = -1;

  public String guildName;
  public String guildRankName;

  public Point fishingPoint;

  public LevelEffects levelEffects;

  @Override
  public ObjectType getRace() {
    return null;
  }

  @Override
  public boolean getIsBlocking() {
    return false;
  }

  @Override
  public void process() {

  }

  public void drawBody()
  {
//    if (BodyLibrary != null) {
//      BodyLibrary.draw(DrawFrame + armourOffSet, DrawLocation, DrawColor, true);
//    }

    //BodyLibrary.DrawTinted(DrawFrame + ArmourOffSet, DrawLocation, DrawColour, Color.DarkSeaGreen);
  }
}
