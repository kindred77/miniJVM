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
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.libs.MirLibFactory;
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
    GameScene.Scene.GetMapControl().addObject(this);

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
    drawBehindEffects(Settings.IsEffect);

    //float oldOpacity = DXManager.Opacity;
    //if (isHidden && !DXManager.Blending) DXManager.SetOpacity(0.5F);

    drawMount(surface);

    if (!isRidingMount) {
      if (Direction == MirDirection.Left ||
          Direction == MirDirection.Up ||
          Direction == MirDirection.UpLeft ||
          Direction == MirDirection.DownLeft)
        drawWeapon(surface);
      else
        drawWeapon2(surface);
    }

    drawBody(surface);

    if (Direction == MirDirection.Up ||
        Direction == MirDirection.UpLeft ||
        Direction == MirDirection.UpRight ||
        Direction == MirDirection.Right ||
        Direction == MirDirection.Left) {
      drawHead(surface);
      if (this != User) {
        drawWings(surface);
      }
    } else {
      if (this != User) {
        drawWings(surface);
      }
      drawHead(surface);
    }


    if (!isRidingMount) {
      if (Direction == MirDirection.UpRight ||
          Direction == MirDirection.Right ||
          Direction == MirDirection.DownRight ||
          Direction == MirDirection.Down)
        drawWeapon(surface);
      else
        drawWeapon2(surface);

      if (mirClass == MirClass.Archer && getHasClassWeapon())
        drawWeapon2(surface);
    }

    //DXManager.SetOpacity(oldOpacity);
  }

  public void drawBody(long surface) {
    if (BodyLibrary != null) {
      //BodyLibrary.draw(DrawFrame + armourOffSet, DrawLocation, DrawColor, true);
    }

    //BodyLibrary.DrawTinted(DrawFrame + ArmourOffSet, DrawLocation, DrawColour, Color.DarkSeaGreen);
  }
  public void drawHead(long surface) {
    if (hairLibrary != null) {
      //hairLibrary.draw(DrawFrame + hairOffSet, DrawLocation, DrawColor, true);
    }
  }
  public void drawWeapon(long surface) {
    if (weapon < 0) return;

    if (weaponLibrary1 != null) {
      //weaponLibrary1.draw(DrawFrame + weaponOffSet, DrawLocation, DrawColor, true);
    }
  }
  public void drawWeapon2(long surface) {
    if (weapon == -1) return;

    if (weaponLibrary2 != null) {
      //weaponLibrary2.draw(DrawFrame + weaponOffSet, DrawLocation, DrawColor, true);
    }
  }
  public void drawWings(long surface)
  {
    if (wingEffect <= 0 || wingEffect >= 100) return;

    if (wingLibrary != null) {
      //wingLibrary.drawBlend(DrawWingFrame + wingOffset, DrawLocation, DrawColor, true);
    }
  }


  public void drawMount(long surface)
  {
    if (mountType < 0 || !isRidingMount) return;

    if (mountLibrary != null) {
      //mountLibrary.draw(DrawFrame - 416 + mountOffset, DrawLocation, DrawColor, true);
    }
  }

  public void setLibraries()
  {
    //fishing broken
    //10
    //11
    //12
    //13

    //almost all broken
    //20 - black footballer - 791
    //21 - red footballer - 791
    //22 - blue footballer - 791
    //23 - green footballer - 791
    //24 - red2 footballer - 791

    boolean altAnim = false;

    boolean showMount = true;
    boolean showFishing = true;

    if (transformType > -1)
    {
      //Transform
      switch (transformType) {
        case 4:
        case 5:
        case 7:
        case 8:
        case 26:
          showFishing = false;
          break;
        case 6:
        case 9:
          showMount = false;
          showFishing = false;
          break;
        default:
          break;
      }

      switch (CurrentAction) {
        case Standing:
        case Jump:
          frame=frames.Frames.get(MirAction.Standing);
          break;
        case Walking:
        case WalkingBow:
          frame=frames.Frames.get(MirAction.Walking);
          break;
        case Running:
        case RunningBow:
          frame=frames.Frames.get(MirAction.Running);
          break;
        case Attack1:
        case Attack2:
        case Attack3:
        case Attack4:
        case AttackRange1:
        case AttackRange2:
        case AttackRange3:
          frame=frames.Frames.get(MirAction.Attack1);
          break;
      }

      if (mountType > 6 && isRidingMount) {
        armourOffSet = -416;
        BodyLibrary = transformType < MirLibFactory.TransformMounts.length
            ? MirLibFactory.TransformMounts[transformType] : MirLibFactory.TransformMounts[0];
      } else {
        armourOffSet = 0;
        BodyLibrary = transformType < MirLibFactory.Transform.length
            ? MirLibFactory.Transform[transformType] : MirLibFactory.Transform[0];
      }

      hairLibrary = null;
      weaponLibrary1 = null;
      weaponLibrary2 = null;

      if (transformType == 19) {
        wingEffect = 2;
        wingLibrary = wingEffect - 1 < MirLibFactory.TransformEffect.length
            ? MirLibFactory.TransformEffect[wingEffect - 1] : null;
      } else {
        wingLibrary = null;
      }

      hairOffSet = 0;
      weaponOffSet = 0;
      wingOffset = 0;
      mountOffset = 0;

      // end Transform
    } else {

      switch (mirClass) {
        //Archer
        case Archer:
          //WeaponType
          if (getHasClassWeapon()) {
            switch (CurrentAction) {
              case Walking:
              case Running:
              case AttackRange1:
              case AttackRange2:
                altAnim = true;
                break;
            }
          }

          if (CurrentAction == MirAction.Jump) altAnim = true;

          //end WeaponType

          //Armours
          if (altAnim) {
            switch (armour) {
              case 9: //heaven
              case 10: //mir
              case 11: //oma
              case 12: //spirit
                BodyLibrary = armour + 1 < MirLibFactory.ARArmours.length
                    ? MirLibFactory.ARArmours[armour + 1] : MirLibFactory.ARArmours[0];
                break;
              case 19:
                BodyLibrary = armour - 5 < MirLibFactory.ARArmours.length
                    ? MirLibFactory.ARArmours[armour - 5] : MirLibFactory.ARArmours[0];
                break;
              case 29:
              case 30:
                BodyLibrary = armour - 14 < MirLibFactory.ARArmours.length
                    ? MirLibFactory.ARArmours[armour - 14] : MirLibFactory.ARArmours[0];
                break;
              case 35:
              case 36:
              case 37:
              case 38:
              case 39:
              case 40:
              case 41:
                BodyLibrary = armour - 32 < MirLibFactory.ARArmours.length
                    ? MirLibFactory.ARArmours[armour - 32] : MirLibFactory.ARArmours[0];
                break;
              default:
                BodyLibrary = armour < MirLibFactory.ARArmours.length
                    ? MirLibFactory.ARArmours[armour] : MirLibFactory.ARArmours[0];
                break;
            }
            hairLibrary = hair < MirLibFactory.ARHair.length
                ? MirLibFactory.ARHair[hair] : null;
          } else {
            BodyLibrary = armour < MirLibFactory.CArmours.length
                ? MirLibFactory.CArmours[armour] : MirLibFactory.CArmours[0];
            hairLibrary = hair < MirLibFactory.CHair.length
                ? MirLibFactory.CHair[hair] : null;
          }
          //end Armours

          //Weapons
          if (getHasClassWeapon()) {
            int Index = weapon - 200;
            if (altAnim) {
              weaponLibrary2 = Index < MirLibFactory.ARWeaponsS.length
                  ? MirLibFactory.ARWeaponsS[Index] : null;
            } else {
              weaponLibrary2 = Index < MirLibFactory.ARWeapons.length
                  ? MirLibFactory.ARWeapons[Index] : null;
            }
            weaponLibrary1 = null;
          } else {
            if (weapon >= 0) {
              weaponLibrary1 = weapon < MirLibFactory.CWeapons.length
                  ? MirLibFactory.CWeapons[weapon] : null;
            } else {
              weaponLibrary1 = null;
            }
            weaponLibrary2 = null;
          }
          // end Weapons

          //WingEffects
          if (wingEffect > 0 && wingEffect < 100) {
            if (altAnim) {
              wingLibrary =
                  (wingEffect - 1) < MirLibFactory.ARHumEffect.length
                      ? MirLibFactory.ARHumEffect[wingEffect - 1] : null;
            } else {
              wingLibrary =
                  (wingEffect - 1) < MirLibFactory.CHumEffect.length
                      ? MirLibFactory.CHumEffect[wingEffect - 1] : null;
            }
          }
          //end WingEffects

          //Offsets
          armourOffSet = gender == MirGender.Man ? 0 : altAnim ? 352 : 808;
          hairOffSet = gender == MirGender.Man ? 0 : altAnim ? 352 : 808;
          weaponOffSet = gender == MirGender.Man ? 0 : altAnim ? 352 : 416;
          wingOffset = gender == MirGender.Man ? 0 : altAnim ? 352 : 840;
          mountOffset = 0;
          //end Offsets

          break;
        //end Archer
        //Assassin
        case Assassin:
          //WeaponType
          if (getHasClassWeapon() || weapon < 0) {
            switch (CurrentAction) {
              case Standing:
              case Stance:
              case Walking:
              case Running:
              case Die:
              case Struck:
              case Attack1:
              case Attack2:
              case Attack3:
              case Attack4:
              case Sneek:
              case Spell:
              case DashAttack:
                altAnim = true;
                break;
            }
          }
          //end WeaponType

          //Armours
          if (altAnim) {
            switch (armour) {
              case 9: //heaven
              case 10: //mir
              case 11: //oma
              case 12: //spirit
                BodyLibrary = armour + 3 < MirLibFactory.AArmours.length
                    ? MirLibFactory.AArmours[armour + 3] : MirLibFactory.AArmours[0];
                break;
              case 19:
                BodyLibrary = armour - 3 < MirLibFactory.AArmours.length
                    ? MirLibFactory.AArmours[armour - 3] : MirLibFactory.AArmours[0];
                break;
              case 20:
              case 21:
              case 22:
              case 23: //red bone
              case 24:
                BodyLibrary = armour - 17 < MirLibFactory.AArmours.length
                    ? MirLibFactory.AArmours[armour - 17] : MirLibFactory.AArmours[0];
                break;
              case 28:
              case 29:
              case 30:
                BodyLibrary = armour - 20 < MirLibFactory.AArmours.length
                    ? MirLibFactory.AArmours[armour - 20] : MirLibFactory.AArmours[0];
                break;
              case 34:
                BodyLibrary = armour - 23 < MirLibFactory.AArmours.length
                    ? MirLibFactory.AArmours[armour - 23] : MirLibFactory.AArmours[0];
                break;
              default:
                BodyLibrary = armour < MirLibFactory.AArmours.length
                    ? MirLibFactory.AArmours[armour] : MirLibFactory.AArmours[0];
                break;
            }

            hairLibrary = hair < MirLibFactory.AHair.length ? MirLibFactory.AHair[hair] : null;
          } else {
            BodyLibrary = armour < MirLibFactory.CArmours.length
                ? MirLibFactory.CArmours[armour] : MirLibFactory.CArmours[0];
            hairLibrary = hair < MirLibFactory.CHair.length ? MirLibFactory.CHair[hair] : null;
          }
          //end Armours

          //Weapons
          if (getHasClassWeapon()) {
            int Index = weapon - 100;
            weaponLibrary1 = Index < MirLibFactory.AWeaponsL.length
                ? MirLibFactory.AWeaponsR[Index] : null;
            weaponLibrary2 = Index < MirLibFactory.AWeaponsR.length
                ? MirLibFactory.AWeaponsL[Index] : null;
          } else {
            if (weapon >= 0) {
              weaponLibrary1 = weapon < MirLibFactory.CWeapons.length
                  ? MirLibFactory.CWeapons[weapon] : null;
            } else {
              weaponLibrary1 = null;
            }
            weaponLibrary2 = null;
          }
          //end Weapons

          //WingEffects
          if (wingEffect > 0 && wingEffect < 100) {
            if (altAnim) {
              wingLibrary =
                  (wingEffect - 1) < MirLibFactory.AHumEffect.length
                      ? MirLibFactory.AHumEffect[wingEffect - 1] : null;
            } else {
              wingLibrary =
                  (wingEffect - 1) < MirLibFactory.CHumEffect.length
                      ? MirLibFactory.CHumEffect[wingEffect - 1] : null;
            }
          }
          //end WingEffects

          //Offsets
          armourOffSet = gender == MirGender.Man ? 0 : altAnim ? 512 : 808;
          hairOffSet = gender == MirGender.Man ? 0 : altAnim ? 512 : 808;
          weaponOffSet = gender == MirGender.Man ? 0 : altAnim ? 512 : 416;
          wingOffset = gender == MirGender.Man ? 0 : altAnim ? 544 : 840;
          mountOffset = 0;
          //end Offsets
          break;
        //end Assassin


        //Others
        case Warrior:
        case Taoist:
        case Wizard:

          //Armours
          BodyLibrary = armour < MirLibFactory.CArmours.length
              ? MirLibFactory.CArmours[armour] : MirLibFactory.CArmours[0];
          hairLibrary = hair < MirLibFactory.CHair.length
              ? MirLibFactory.CHair[hair] : null;
          //end Armours

          //Weapons
          if (weapon >= 0) {
            weaponLibrary1 = weapon < MirLibFactory.CWeapons.length
                ? MirLibFactory.CWeapons[weapon] : null;
          } else {
            weaponLibrary1 = null;
          }
          weaponLibrary2 = null;
          //end Weapons

          //WingEffects
          if (wingEffect > 0 && wingEffect < 100) {
            wingLibrary = (wingEffect - 1) < MirLibFactory.CHumEffect.length
                ? MirLibFactory.CHumEffect[wingEffect - 1] : null;
          }
          //end WingEffects

          //Offsets
          armourOffSet = gender == MirGender.Man ? 0 : 808;
          hairOffSet = gender == MirGender.Man ? 0 : 808;
          weaponOffSet = gender == MirGender.Man ? 0 : 416;
          wingOffset = gender == MirGender.Man ? 0 : 840;
          mountOffset = 0;
          //end Offsets
          break;
        //end Others
      }
    }

    //Common
    //Harvest
    if (CurrentAction == MirAction.Harvest && transformType < 0) {
      weaponLibrary1 = 1 < MirLibFactory.CWeapons.length ? MirLibFactory.CWeapons[1] : null;
    }

    //Mounts
    if (mountType > -1 && isRidingMount && showMount) {
      mountLibrary = mountType < MirLibFactory.Mounts.length
          ? MirLibFactory.Mounts[mountType] : null;
    } else {
      mountLibrary = null;
    }

    //Fishing
    if (getHasFishingRod() && showFishing) {
      if (CurrentAction == MirAction.FishingCast ||
          CurrentAction == MirAction.FishingWait ||
          CurrentAction == MirAction.FishingReel) {
        weaponLibrary1 = 0 < MirLibFactory.Fishing.length
            ? MirLibFactory.Fishing[weapon - 49] : null;
        weaponLibrary2 = null;
        weaponOffSet = -632;
      }
    }

    dieSound = gender == MirGender.Man ? SoundList.MaleDie : SoundList.FemaleDie;
    flinchSound = gender == MirGender.Man ? SoundList.MaleFlinch : SoundList.FemaleFlinch;
    //end Common
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
            OffSetMove = new Point(0,
                (int)((Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
          case UpRight:
            OffSetMove = new Point(
                (int)((-Settings.CellWidth * i / (float)(count)) * (index + 1)),
                (int)((Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
          case Right:
            OffSetMove = new Point(
                (int)((-Settings.CellWidth * i / (float)(count)) * (index + 1)),
                0);
            break;
          case DownRight:
            OffSetMove = new Point(
                (int)((-Settings.CellWidth * i / (float)(count)) * (index + 1)),
                (int)((-Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
          case Down:
            OffSetMove = new Point(0,
                (int)((-Settings.CellHeight * i / (float)(count)) * (index + 1))
            );
            break;
          case DownLeft:
            OffSetMove = new Point(
                (int)((Settings.CellWidth * i / (float)(count)) * (index + 1)),
                (int)((-Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
          case Left:
            OffSetMove = new Point(
                (int)((Settings.CellWidth * i / (float)(count)) * (index + 1)),
                0);
            break;
          case UpLeft:
            OffSetMove = new Point(
                (int)((Settings.CellWidth * i / (float)(count)) * (index + 1)),
                (int)((Settings.CellHeight * i / (float)(count)) * (index + 1)));
            break;
        }

        OffSetMove = new Point(
            OffSetMove.getX() % 2 + OffSetMove.getX(),
            OffSetMove.getY() % 2 + OffSetMove.getY());
        break;
      default:
        OffSetMove = Point.Empty;
        Movement = CurrentLocation;
        break;
    }

    DrawY = Math.max(Movement.getY(), CurrentLocation.getY());

    DrawLocation = new Point(
        (Movement.getX() - User.Movement.getX() + MapMainControl.OffSetX) * Settings.CellWidth,
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
}
