package com.kindred.mir.scene.game.objects;

import com.kindred.mir.Env;
import com.kindred.mir.GameCommon.BuffType;
import com.kindred.mir.GameCommon.MirAction;
import com.kindred.mir.GameCommon.MirDirection;
import com.kindred.mir.GameCommon.ObjectType;
import com.kindred.mir.GameCommon.PoisonType;
import com.kindred.mir.GameCommon.Spell;
import com.kindred.mir.Settings;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithTexture;
import com.kindred.mir.controls.MirLabel;
import com.kindred.mir.engine.Font;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirImage.ImageEffect;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.scene.game.GameScene;
import com.kindred.mir.scene.game.map.MapCommonComponent;
import com.kindred.mir.scene.game.map.MapMainControl;
import com.kindred.mir.scene.game.objects.effects.BuffEffect;
import com.kindred.mir.scene.game.objects.effects.Effect;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Rectangle;
import com.kindred.mir.util.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图上的物件通用类，包括人物，怪物，NPC，地面物品等
 */
public abstract class MapObject extends MirControlWithTexture {

  public static class QueuedAction
  {
    public MirAction Action;
    public Point Location;
    public MirDirection Direction;
    public List<Object> Params;
  }

  public static Font ChatFont = Settings.FONT_SIZE10;
  public static List<MirLabel> LabelList = new ArrayList<>();

  //for test
  public static UserObject User=new UserObject(null, 0l, 1);
  static {
    User.Movement=new Point(328,300);
    User.OffSetMove=new Point(20,20);
  }
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
  public boolean InTrapRock;

  public boolean Blend = true;

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
  public Color DrawColor = Color.White, NameColor = Color.White, LightColor = Color.White;
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
  public abstract void draw(long surface);
  public abstract void process();

  public abstract void drawBehindEffects(boolean effectsEnabled);
  public abstract void drawEffects(boolean effectsEnabled);

  public ObjectType getRace() {
    return this.race;
  }

  public void setRace(ObjectType race) {
    this.race=race;
  }

  protected MapObject(MirControl parent, long renderer_id, long objectID) {
    super(parent, renderer_id);
    ObjectID = objectID;

    for (int i = MapMainControl.Objects.size() - 1; i >= 0; i--) {
      MapObject ob = MapMainControl.Objects.get(i);
      if (ob.ObjectID != ObjectID) {
        continue;
      }
      ob.remove();
    }

    MapMainControl.Objects.add(this);
  }

  public void remove() {
    //if (!(this is PlayerObject))
    //{
    //GameScene.Scene.ChatDialog.ReceiveChat("MapObject-Remove: ID: " + ObjectID + ", name: " + Name + ", locX:" + CurrentLocation.X + ",locY:" + CurrentLocation.Y, ChatType.System);
    //}

    if (MouseObject == this) {
      MouseObject = null;
    }
    if (TargetObject == this) {
      TargetObject = null;
    }
    if (MagicObject == this) {
      MagicObject = null;
    }

    if (this == User.NextMagicObject) {
      User.clearMagic();
    }

    MapMainControl.Objects.remove(this);
    GameScene gameScene=(GameScene) Env.ActiveScene;
    gameScene.getMapControl().removeObject(this);

    if (ObjectID != gameScene.getNpcID()) {
      return;
    }

    gameScene.setNpcID(0);
    ((GameScene) Env.ActiveScene).getNpcDialog().setIsVisible(false);
  }

  public void addBuffEffect(BuffType type) {
    for (int i = 0; i < Effects.size(); i++) {
      if (!(Effects.get(i) instanceof BuffEffect)) {
        continue;
      }
      if (((BuffEffect)(Effects.get(i))).BuffType == type) {
        return;
      }
    }

    PlayerObject ob = null;

    if (getRace() == ObjectType.Player) {
      ob = (PlayerObject)this;
    }

    BuffEffect effect=null;
    switch (type) {
      case Fury:
        effect=new BuffEffect(MirLibFactory.getMirLib(MirLibFactory.Magic3), 190, 7, 1400, this, true, type);
        effect.Repeat=true;
        Effects.add(effect);
        break;
      case ImmortalSkin:
        effect=new BuffEffect(MirLibFactory.getMirLib(MirLibFactory.Magic3), 570, 5, 1400, this, true, type);
        effect.Repeat=true;
        Effects.add(effect);
        break;
      case SwiftFeet:
        if (ob != null) {
          ob.sprint = true;
        }
        break;
      case MoonLight:
      case DarkBody:
        if (ob != null) {
          ob.isSneaking = true;
        }
        break;
      case VampireShot:
        effect=new BuffEffect(MirLibFactory.getMirLib(MirLibFactory.Magic3), 2110, 6, 1400, this, true, type);
        effect.Repeat=false;
        Effects.add(effect);
        break;
      case PoisonShot:
        effect=new BuffEffect(MirLibFactory.getMirLib(MirLibFactory.Magic3), 2310, 7, 1400, this, true, type);
        effect.Repeat=false;
        Effects.add(effect);
        break;
      case EnergyShield:
        effect = new BuffEffect(MirLibFactory.getMirLib(MirLibFactory.Magic2), 1880, 9, 900, this, true, type);
        effect.Repeat=false;
        Effects.add(effect);
        SoundManager.playSound(20000 + (int) Spell.EnergyShield.code() * 10 + 0, false);

        effect.onComplete=(control, argObj) -> {
          BuffEffect bufEffect=new BuffEffect(MirLibFactory.getMirLib(MirLibFactory.Magic2), 1900, 2, 800, this, true, type);
          bufEffect.Repeat=true;
          Effects.add(bufEffect);
        };
      break;
      case MagicBooster:
        effect = new BuffEffect(MirLibFactory.getMirLib(MirLibFactory.Magic3), 90, 6, 1200, this, true, type);
        effect.Repeat=true;
        Effects.add(effect);
        break;
      case PetEnhancer:
        effect = new BuffEffect(MirLibFactory.getMirLib(MirLibFactory.Magic3), 230, 6, 1200, this, true, type);
        effect.Repeat=true;
        Effects.add(effect);
        break;
    }
  }

  public void removeBuffEffect(BuffType type) {
    PlayerObject ob = null;

    if (getRace() == ObjectType.Player) {
      ob = (PlayerObject)this;
    }

    for (int i = 0; i < Effects.size(); i++) {
      if (!(Effects.get(i) instanceof BuffEffect)) {
        continue;
      }
      if (((BuffEffect)(Effects.get(i))).BuffType != type) {
        continue;
      }
      Effects.get(i).Repeat = false;
    }

    switch (type) {
      case SwiftFeet:
        if (ob != null) {
          ob.sprint = false;
        }
        break;
      case MoonLight:
      case DarkBody:
        if (ob != null) {
          ob.isSneaking = false;
        }
        break;
    }
  }

  public void chat(String text) {
    if (ChatLabel != null && !ChatLabel.getIsDisposed()) {
      ChatLabel.dispose();
      ChatLabel = null;
    }

    final int chatWidth = 200;
    List<String> chat = new ArrayList<>();

//    int index = 0;
//    for (int i = 1; i < text.length(); i++) {
//      if (TextRenderer.MeasureText(CMain.Graphics, text.Substring(index, i - index), ChatFont).Width > chatWidth) {
//        chat.add(text.Substring(index, i - index - 1));
//        index = i - 1;
//      }
//    }
//
//    chat.add(text.Substring(index, text.Length - index));

    chat.add(text);

    text = chat.get(0);
    for (int i = 1; i < chat.size(); i++) {
      text += String.format("\n{0}", chat.get(i));
    }

    ChatLabel = new MirLabel(this,getRenderer(),
        new Size(70,30),
        new Point(100,100),
        ChatFont,
        text,
        Color.White,
        Color.Empty,
        100
        );
//    {
//      AutoSize = true,
//          BackColour = Color.Transparent,
//          ForeColour = Color.White,
//          OutLine = true,
//          OutLineColour = Color.Black,
//          DrawFormat = TextFormatFlags.HorizontalCenter,
//          Text = text,
//    };
    ChatTime = Env.Time + 5000;
  }

  public void drawChat() {
    if (ChatLabel == null || ChatLabel.getIsDisposed()) {
      return;
    }

    if (Env.Time > ChatTime) {
      ChatLabel.dispose();
      ChatLabel = null;
      return;
    }

    ChatLabel.setForeColor(isDead ? Color.Gray : Color.White);
    ChatLabel.setLocation(new Point(
        DisplayRectangle.getX() + (48 - ChatLabel.getSize().getWidth()) / 2,
        DisplayRectangle.getY() - (60 + ChatLabel.getSize().getHeight()) - (isDead ? 35 : 0)));
    //ChatLabel.Draw();
  }

  private void createLabel() {
    NameLabel = null;
    for (int i = 0; i < LabelList.size(); i++) {
      if (!LabelList.get(i).getText().equals(Name)
          || !LabelList.get(i).getForeColor().equals(NameColor)) {
        continue;
      }
      NameLabel = LabelList.get(i);
      break;
    }


    if (NameLabel != null && !NameLabel.getIsDisposed()) {
      return;
    }

    NameLabel = new MirLabel(this,getRenderer(),
        new Size(70,30),
        new Point(100,100),
        ChatFont,
        Name,
        NameColor,
        Color.Empty,
        100
    );

//    new MirLabel
//    {
//      AutoSize = true,
//          BackColour = Color.Transparent,
//          ForeColour = NameColour,
//          OutLine = true,
//          OutLineColour = Color.Black,
//          Text = Name,
//    };

    //NameLabel.Disposing += (o, e) => LabelList.remove(NameLabel);

    LabelList.add(NameLabel);

  }


  public void drawName(long surface) {
    createLabel();

    if (NameLabel == null) {
      return;
    }

    NameLabel.setText(Name);
    NameLabel.setLocation(new Point(
        DisplayRectangle.getX() + (50 - NameLabel.getSize().getWidth()) / 2,
        DisplayRectangle.getY() - (32 - NameLabel.getSize().getHeight() / 2) + (isDead ? 35 : 8))); //was 48 -

    //NameLabel.Draw();
  }


  //public void drawBlend(long surface) {
    //DXManager.SetBlend(true, 0.3F); //0.8
    //draw(surface);
    //DXManager.SetBlend(false);
  //}


  public void drawDamages(long surface) {
    for (int i = Damages.size() - 1; i >= 0; i--) {
      Damage info = Damages.get(i);
      if (Env.Time > info.ExpireTime) {
        Damages.remove(i);
      } else {
        info.draw(surface, DisplayRectangle.getLocation());
      }
    }
  }

  public void drawHealth(long surface) throws Exception{
    String name = Name;

    if (Name.contains("(")) {
      name = Name.substring(Name.indexOf("(") + 1, Name.length() - Name.indexOf("(") - 2);
    }

    if (isDead) {
      return;
    }
    if (getRace() != ObjectType.Player && getRace() != ObjectType.Monster) {
      return;
    }

    if (Env.Time >= HealthTime) {
      //只能看到自己的宠物,组队成员的血条
      if (getRace() == ObjectType.Monster) {
        MonsterObject mo=(MonsterObject)this;
        if(mo.MasterID != User.ObjectID){
          return;
        }
      }
      if (getRace() == ObjectType.Player && this != User && !Env.MyGroupList.contains(ObjectID)) {
        return;
      }
      if (this == User && Env.MyGroupList.size() == 0) {
        return;
      }
    }

    MirLibFactory.tryToDraw(surface,
        MirLibFactory.Prguse3,0,
        ImageEffect.None,DisplayRectangle.getX() + 8,
        DisplayRectangle.getY() - 64,false);
    //Libraries.Prguse2.Draw(0, DisplayRectangle.getX() + 8, DisplayRectangle.getY() - 64);
    int index = 1;

//    switch (getRace()) {
//      case Player:
//        if (Env.MyGroupList.contains(ObjectID)) {
//          index = 10;
//        }
//        break;
//      case Monster:
//        if (Env.MyGroupList.contains(ObjectID) || name == User.Name) {
//          index = 11;
//        }
//        break;
//    }

//    Libraries.Prguse2.Draw(index,
//        new Rectangle(0, 0, (int)(32 * PercentHealth / 100F), 4),
//        new Point(DisplayRectangle.X + 8, DisplayRectangle.Y - 64),
//        Color.White, false);
//
//    MirLibFactory.tryToDraw(surface,
//        MirLibFactory.Prguse3,index,
//        ImageEffect.None,DisplayRectangle.getX() + 8,
//        DisplayRectangle.getY() - 64,false);
  }

  public void drawPoison(long surface) {
//    byte poisoncount = 0;
//    if (Poison != PoisonType.None) {
//      if (Util.EnumHasFlag(Poison.code(), PoisonType.Green.code())) {
//        DXManager.Sprite.Draw2D(DXManager.PoisonDotBackground, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 7 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 21)), Color.Black);
//        DXManager.Sprite.Draw2D(DXManager.RadarTexture, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 8 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 20)), Color.Green);
//        poisoncount++;
//      }
//      if (Util.EnumHasFlag(Poison.code(), PoisonType.Red.code())) {
//        DXManager.Sprite.Draw2D(DXManager.PoisonDotBackground, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 7 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 21)), Color.Black);
//        DXManager.Sprite.Draw2D(DXManager.RadarTexture, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 8 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 20)), Color.Red);
//        poisoncount++;
//      }
//      if (Util.EnumHasFlag(Poison.code(), PoisonType.Bleeding.code())) {
//        DXManager.Sprite.Draw2D(DXManager.PoisonDotBackground, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 7 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 21)), Color.Black);
//        DXManager.Sprite.Draw2D(DXManager.RadarTexture, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 8 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 20)), Color.DarkRed);
//        poisoncount++;
//      }
//      if (Util.EnumHasFlag(Poison.code(), PoisonType.Slow.code())) {
//        DXManager.Sprite.Draw2D(DXManager.PoisonDotBackground, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 7 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 21)), Color.Black);
//        DXManager.Sprite.Draw2D(DXManager.RadarTexture, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 8 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 20)), Color.Purple);
//        poisoncount++;
//      }
//      if (Util.EnumHasFlag(Poison.code(), PoisonType.Stun.code())) {
//        DXManager.Sprite.Draw2D(DXManager.PoisonDotBackground, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 7 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 21)), Color.Black);
//        DXManager.Sprite.Draw2D(DXManager.RadarTexture, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 8 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 20)), Color.Yellow);
//        poisoncount++;
//      }
//      if (Util.EnumHasFlag(Poison.code(), PoisonType.Frozen.code())) {
//        DXManager.Sprite.Draw2D(DXManager.PoisonDotBackground, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 7 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 21)), Color.Black);
//        DXManager.Sprite.Draw2D(DXManager.RadarTexture, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 8 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 20)), Color.Blue);
//        poisoncount++;
//      }
//      if (Util.EnumHasFlag(Poison.code(), PoisonType.Paralysis.code()) || Util.EnumHasFlag(Poison.code(), PoisonType.LRParalysis.code())) {
//        DXManager.Sprite.Draw2D(DXManager.PoisonDotBackground, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 7 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 21)), Color.Black);
//        DXManager.Sprite.Draw2D(DXManager.RadarTexture, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 8 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 20)), Color.Gray);
//        poisoncount++;
//      }
//      if (Util.EnumHasFlag(Poison.code(), PoisonType.DelayedExplosion.code())) {
//        DXManager.Sprite.Draw2D(DXManager.PoisonDotBackground, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 7 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 21)), Color.Black);
//        DXManager.Sprite.Draw2D(DXManager.RadarTexture, Point.Empty, 0, new PointF((int)(DisplayRectangle.X + 8 + (poisoncount * 3)), (int)(DisplayRectangle.Y - 20)), Color.Orange);
//        poisoncount++;
//      }
//    }
  }

}
