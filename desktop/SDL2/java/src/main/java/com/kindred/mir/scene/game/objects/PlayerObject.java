package com.kindred.mir.scene.game.objects;

import com.kindred.mir.Env;
import com.kindred.mir.GameCommon.LevelEffects;
import com.kindred.mir.GameCommon.MirAction;
import com.kindred.mir.GameCommon.MirClass;
import com.kindred.mir.GameCommon.MirDirection;
import com.kindred.mir.GameCommon.ObjectType;
import com.kindred.mir.GameCommon.PoisonType;
import com.kindred.mir.GameCommon.Spell;
import com.kindred.mir.GameCommon.SpellEffect;
import com.kindred.mir.Settings;
import com.kindred.mir.constcode.MirEnums.MirGender;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.scene.game.GameScene;
import com.kindred.mir.scene.game.bean.PlayerInfo;
import com.kindred.mir.scene.game.map.MapMainControl;
import com.kindred.mir.scene.game.objects.effects.Effect;
import com.kindred.mir.scene.game.objects.effects.InterruptionEffect;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.MirUtil;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Rectangle;

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

  public boolean isRidingMount, isSprint, isFastRun, isFishing, isFoundFish;
  public long stanceTime, mountTime, fishingTime;
  public long blizzardStopTime, reincarnationStopTime, slashingBurstTime;

  public short mountType = -1, transformType = -1;

  public String guildName;
  public String guildRankName;

  public Point fishingPoint;

  public LevelEffects levelEffects;

  public boolean getHasClassWeapon() {
    switch (weapon / 100)
    {
      case 1:
        return mirClass == MirClass.Assassin;
      case 2:
        return mirClass == MirClass.Archer;
      default:
        return mirClass == MirClass.Wizard || mirClass == MirClass.Warrior
            || mirClass == MirClass.Taoist;
    }
  }

  public boolean  getHasFishingRod() {
    return weapon == 49 || weapon == 50;
  }

  protected PlayerObject(MirControl parent, long renderer_id,long objectID) {
    super(parent, renderer_id, objectID);
    frames = FrameSet.Players;
  }

  public void Load(PlayerInfo info)
  {
    Name = info.getName();
    NameColor = info.getNameColor();
    guildName = info.getGuildName();
    guildRankName = info.getGuildRankName();
    mirClass = info.getMirClass();
    gender = info.getGender();
    level = info.getLevel();

    CurrentLocation = info.getCurrentLocation();
    MapLocation = info.getMapLocation();
    GameScene.Scene.getMapControl().addObject(this);

    Direction = info.getDirection();
    hair = info.getHair();

    weapon = info.getWeapon();
    armour = info.getArmour();
    Light = info.getLight();

    Poison = info.getPoison();

    isDead = info.isDead();
    isHidden = info.isHidden();

    wingEffect = info.getWingEffect();
    currentEffect = info.getCurrentEffect();

    mountType = info.getMountType();
    isRidingMount = info.isRidingMount();

    isFishing = info.isFishing();

    transformType = info.getTransformType();

    //SetLibraries();

    //if (isDead) ActionFeed.add(new QueuedAction { Action = MirAction.Dead, Direction = Direction, Location = CurrentLocation });
    //if (info.isExtra()) Effects.add(new Effect(Libraries.Magic2, 670, 10, 800, this));

    elementEffect = (int)info.getElementEffect();
    elementsLevel = (int)info.getElementsLevel();
    elementOrbMax = (int)info.getElementOrbMax();

    Buffs = info.getBuffs();

    levelEffects = info.getLevelEffects();

//    ProcessBuffs();
//
//    SetAction();
//
//    SetEffects();
  }

  @Override
  public ObjectType getRace() {
    return ObjectType.Player;
  }

  @Override
  public boolean getIsBlocking() {
    return false;
  }

  @Override
  public void draw(long surface) {

  }

  @Override
  public void process() {
    boolean update = Env.Time >= NextMotion || Env.CanMove;

    if (this == User)
    {
      if (Env.Time - Env.LastRunTime > 899)
        Env.CanRun = false;
    }

    SkipFrames = this != User && ActionFeed.size() > 1;

    //ProcessFrames();

    if (frame == null)
    {
      DrawFrame = 0;
      DrawWingFrame = 0;
    }
    else
    {
      DrawFrame = frame.getStart() + (frame.getOffSet() * Direction.code()) + frameIndex;
      DrawWingFrame = frame.getEffectStart() + (frame.getEffectOffSet() * Direction.code()) + effectFrameIndex;
    }

    switch (CurrentAction)
    {
      case Walking:
      case Running:
      case MountWalking:
      case MountRunning:
      case Pushed:
      case DashL:
      case DashR:
      case Sneek:
      case Jump:
      case DashAttack:
        if (frame == null)
        {
          OffSetMove = Point.Empty;
          Movement = CurrentLocation;
          break;
        }

        int i = 0;
        if (CurrentAction == MirAction.MountRunning) i = 3;
        else if (CurrentAction == MirAction.Running)
          i = (isSprint && !isSneaking ? 3 : 2);
        else i = 1;

        if (CurrentAction == MirAction.Jump) i = -jumpDistance;
        if (CurrentAction == MirAction.DashAttack) i = jumpDistance;

        Movement = MirUtil.PointMove(CurrentLocation, Direction, CurrentAction == MirAction.Pushed ? 0 : -i);

        int count = frame.getCount();
        int index = frameIndex;

        if (CurrentAction == MirAction.DashR || CurrentAction == MirAction.DashL)
        {
          count = 3;
          index %= 3;
        }

        switch (Direction)
        {
          case Up:
            OffSetMove = new Point(0, (int)((Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
          case UpRight:
            OffSetMove = new Point((int)((-Settings.CellWidth * i / (float)(count)) * (index + 1)), (int)((Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
          case Right:
            OffSetMove = new Point((int)((-Settings.CellWidth * i / (float)(count)) * (index + 1)), 0);
            break;
          case DownRight:
            OffSetMove = new Point((int)((-Settings.CellWidth * i / (float)(count)) * (index + 1)), (int)((-Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
          case Down:
            OffSetMove = new Point(0, (int)((-Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
          case DownLeft:
            OffSetMove = new Point((int)((Settings.CellWidth * i / (float)(count)) * (index + 1)), (int)((-Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
          case Left:
            OffSetMove = new Point((int)((Settings.CellWidth * i / (float)(count)) * (index + 1)), 0);
            break;
          case UpLeft:
            OffSetMove = new Point((int)((Settings.CellWidth * i / (float)(count)) * (index + 1)), (int)((Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
        }

        OffSetMove = new Point(OffSetMove.getX() % 2 + OffSetMove.getX(), OffSetMove.getY() % 2 + OffSetMove.getY());
        break;
      default:
        OffSetMove = Point.Empty;
        Movement = CurrentLocation;
        break;
    }

    DrawY = Math.max(Movement.getY(), CurrentLocation.getY());

    DrawLocation = new Point((Movement.getX() - User.Movement.getX() + MapMainControl.OffSetX) * Settings.CellWidth,
        (Movement.getY() - User.Movement.getY() + MapMainControl.OffSetY) * Settings.CellHeight);
    DrawLocation.Offset(Settings.GlobalDisplayLocationOffset);

    if (this != User) {
      DrawLocation.Offset(User.OffSetMove);
      DrawLocation.Offset(-OffSetMove.getX(), -OffSetMove.getY());
    }

    if (BodyLibrary != null && update) {
      MirImage img=null;
      try {
        img = BodyLibrary.GetMirImage(DrawFrame);
        FinalDrawLocation = Point.add(DrawLocation, img.getOffset());
        DisplayRectangle = new Rectangle(DrawLocation, img.getTrueSize());
      }catch(Exception e) {
        e.printStackTrace();
      }

    }

    for(Effect effect : Effects) {
      effect.Process();
    }

    Color colour = DrawColor;
    DrawColor = Color.White;
    if (Poison != PoisonType.None)
    {

      if (MirUtil.EnumHasFlag(Poison.code(), PoisonType.Green.code()))
        DrawColor = Color.Green;
      if (MirUtil.EnumHasFlag((int)Poison.code(), (int)PoisonType.Red.code()))
        DrawColor = Color.Red;
      if (MirUtil.EnumHasFlag(Poison.code(), PoisonType.Bleeding.code()))
        DrawColor = Color.DarkRed;
      if (MirUtil.EnumHasFlag(Poison.code(), PoisonType.Slow.code()))
        DrawColor = Color.Purple;
      if (MirUtil.EnumHasFlag(Poison.code(), PoisonType.Stun.code()))
        DrawColor = Color.Yellow;
      if (MirUtil.EnumHasFlag(Poison.code(), PoisonType.Frozen.code()))
        DrawColor = Color.Blue;
      if (MirUtil.EnumHasFlag(Poison.code(), PoisonType.Paralysis.code())
          || MirUtil.EnumHasFlag(Poison.code(), PoisonType.LRParalysis.code()))
        DrawColor = Color.Gray;
      if (MirUtil.EnumHasFlag(Poison.code(), PoisonType.DelayedExplosion.code()))
        DrawColor = Color.Orange;
    }


    //if (colour != DrawColor) GameScene.Scene.getMapControl().TextureValid = false;
  }

  @Override
  public void drawBehindEffects(boolean effectsEnabled) {

  }

  @Override
  public void drawEffects(boolean effectsEnabled) {

  }

  public void drawBody()
  {
//    if (BodyLibrary != null) {
//      BodyLibrary.draw(DrawFrame + armourOffSet, DrawLocation, DrawColor, true);
//    }

    //BodyLibrary.DrawTinted(DrawFrame + ArmourOffSet, DrawLocation, DrawColour, Color.DarkSeaGreen);
  }
}
