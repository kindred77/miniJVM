package com.kindred.mir;

import com.kindred.mir.util.Point;

public class GameCommon {
  public class Door {
    public byte index;
    public byte DoorState;//0: closed, 1: opening, 2: open, 3: closing
    public byte ImageIndex;
    public long LastTick;
    public Point Location;
  }

  public enum MirDirection {
    Up((byte)0,"Up"),
    UpRight((byte)1,"UpRight"),
    Right((byte)2,"Right"),
    DownRight((byte)3,"DownRight"),
    Down((byte)4,"Down"),
    DownLeft((byte)5,"DownLeft"),
    Left((byte)6,"Left"),
    UpLeft((byte)7,"UpLeft"),;

    private final byte code;
    private final String value;

    MirDirection(byte code,String value) {
      this.code = code;
      this.value=value;
    }

    public final byte code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }

  public enum LightSetting
  {
    Normal((byte)0,"Normal"),
    Dawn((byte)1,"Dawn"),
    Day((byte)2,"Day"),
    Evening((byte)3,"Evening"),
    Night((byte)4,"Night"),;

    private final byte code;
    private final String value;

    LightSetting(byte code,String value) {
      this.code = code;
      this.value=value;
    }

    public final byte code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }

  public enum PoisonType
  {
    None(0,"None"),
    Green(1,"Green"),
    Red(2,"Red"),
    Slow(4,"Slow"),
    Frozen(8,"Frozen"),
    Stun(16,"Stun"),
    Paralysis(32,"Paralysis"),
    DelayedExplosion(64,"DelayedExplosion"),
    Bleeding(128,"Bleeding"),
    LRParalysis(256,"LRParalysis"),;

    private final int code;
    private final String value;

    PoisonType(int code,String value) {
      this.code = code;
      this.value=value;
    }

    public final int code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }

  public enum ChatType
  {
    Normal((byte)0,"Normal"),
    Shout((byte)1,"Shout"),
    System((byte)2,"System"),
    Hint((byte)3,"Hint"),
    Announcement((byte)4,"Announcement"),
    Group((byte)5,"Group"),
    WhisperIn((byte)6,"WhisperIn"),
    WhisperOut((byte)7,"WhisperOut"),
    Guild((byte)8,"Guild"),
    Trainer((byte)9,"Trainer"),
    LevelUp((byte)10,"LevelUp"),
    System2((byte)11,"System2"),
    Relationship((byte)12,"Relationship"),
    Mentor((byte)13,"Mentor"),
    Shout2((byte)14,"Shout2"),
    Shout3((byte)15,"Shout3"),;

    private final byte code;
    private final String value;

    ChatType(byte code,String value) {
      this.code = code;
      this.value=value;
    }

    public final byte code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }

  public enum MirAction {
    Standing((byte)0,"Standing"),
    Walking((byte)1,"Walking"),
    Running((byte)2,"Running"),
    Pushed((byte)3,"Pushed"),
    DashL((byte)4,"DashL"),
    DashR((byte)5,"DashR"),
    DashFail((byte)6,"DashFail"),
    Stance((byte)7,"Stance"),
    Stance2((byte)8,"Stance2"),
    Attack1((byte)9,"Attack1"),
    Attack2((byte)10,"Attack2"),
    Attack3((byte)11,"Attack3"),
    Attack4((byte)12,"Attack4"),
    Attack5((byte)13,"Attack5"),
    AttackRange1((byte)14,"AttackRange1"),
    AttackRange2((byte)15,"AttackRange2"),
    AttackRange3((byte)16,"AttackRange3"),
    Special((byte)17,"Special"),
    Struck((byte)18,"Struck"),
    Harvest((byte)19,"Harvest"),
    Spell((byte)20,"Spell"),
    Die((byte)21,"Die"),
    Dead((byte)22,"Dead"),
    Skeleton((byte)23,"Skeleton"),
    Show((byte)24,"Show"),
    Hide((byte)25,"Hide"),
    Stoned((byte)26,"Stoned"),
    Appear((byte)27,"Appear"),
    Revive((byte)28,"Revive"),
    SitDown((byte)29,"SitDown"),
    Mine((byte)30,"Mine"),
    Sneek((byte)31,"Sneek"),
    DashAttack((byte)32,"DashAttack"),
    Lunge((byte)33,"Lunge"),

    WalkingBow((byte)34,"WalkingBow"),
    RunningBow((byte)35,"RunningBow"),
    Jump((byte)36,"Jump"),

    MountStanding((byte)37,"MountStanding"),
    MountWalking((byte)38,"MountWalking"),
    MountRunning((byte)39,"MountRunning"),
    MountStruck((byte)40,"MountStruck"),
    MountAttack((byte)41,"MountAttack"),

    FishingCast((byte)42,"FishingCast"),
    FishingWait((byte)43,"FishingWait"),
    FishingReel((byte)44,"FishingReel"),;

    private final byte code;
    private final String value;

    MirAction(byte code,String value) {
      this.code = code;
      this.value=value;
    }

    public final byte code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }

  public enum BuffType {
    None((byte)0,"None"),
    //magics
    TemporalFlux((byte)1,"TemporalFlux"),
    Hiding((byte)2,"Hiding"),
    Haste((byte)3,"Haste"),
    SwiftFeet((byte)4,"SwiftFeet"),
    Fury((byte)5,"Fury"),
    SoulShield((byte)6,"SoulShield"),
    BlessedArmour((byte)7,"BlessedArmour"),
    LightBody((byte)8,"LightBody"),
    UltimateEnhancer((byte)9,"UltimateEnhancer"),
    ProtectionField((byte)10,"ProtectionField"),
    Rage((byte)11,"Rage"),
    Curse((byte)12,"Curse"),
    MoonLight((byte)13,"MoonLight"),
    DarkBody((byte)14,"DarkBody"),
    Concentration((byte)15,"Concentration"),
    VampireShot((byte)16,"VampireShot"),
    PoisonShot((byte)17,"PoisonShot"),
    CounterAttack((byte)18,"CounterAttack"),
    MentalState((byte)19,"MentalState"),
    EnergyShield((byte)20,"EnergyShield"),
    MagicBooster((byte)21,"MagicBooster"),
    PetEnhancer((byte)22,"PetEnhancer"),
    ImmortalSkin((byte)23,"ImmortalSkin"),
    MagicShield((byte)24,"MagicShield"),

    //special
    GameMaster((byte)100,"GameMaster"),
    General((byte)101,"General"),
    Exp((byte)102,"Exp"),
    Drop((byte)103,"Drop"),
    Gold((byte)104,"Gold"),
    BagWeight((byte)105,"BagWeight"),
    Transform((byte)106,"Transform"),
    RelationshipEXP((byte)107,"RelationshipEXP"),
    Mentee((byte)108,"Mentee"),
    Mentor((byte)109,"Mentor"),
    Guild((byte)110,"Guild"),
    Prison((byte)111,"Prison"),
    Rested((byte)112,"Rested"),

    //stats
    Impact((byte)201,"Impact"),
    Magic((byte)202,"Magic"),
    Taoist((byte)203,"Taoist"),
    Storm((byte)204,"Storm"),
    HealthAid((byte)205,"HealthAid"),
    ManaAid((byte)206,"ManaAid"),
    Defence((byte)207,"Defence"),
    MagicDefence((byte)208,"MagicDefence"),
    WonderDrug((byte)209,"WonderDrug"),
    Knapsack((byte)210,"Knapsack"),;

    private final byte code;
    private final String value;

    BuffType(byte code,String value) {
      this.code = code;
      this.value=value;
    }

    public final byte code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }

  public enum ObjectType {
    None((byte)0,"None"),
    Player((byte)1,"Player"),
    Item((byte)2,"Item"),
    Merchant((byte)3,"Merchant"),
    Spell((byte)4,"Spell"),
    Monster((byte)5,"Monster"),
    Deco((byte)6,"Deco"),
    Creature((byte)7,"Creature"),;

    private final byte code;
    private final String value;

    ObjectType(byte code,String value) {
      this.code = code;
      this.value=value;
    }

    public final byte code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }
}
