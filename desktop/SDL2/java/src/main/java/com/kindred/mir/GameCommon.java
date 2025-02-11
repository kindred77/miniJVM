package com.kindred.mir;

import com.kindred.mir.util.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * 使用duckdb生成枚举代码参考:

 COPY (select t.s4 from (select trim(string_split(trim(test),'//')[1]) as s1,
 trim(string_split(trim(test),'//')[2]) as s2,
 concat(trim(string_split(s1,'=')[1]),'(',trim(string_split(s1,'=')[2]),'"',trim(string_split(s1,'=')[1]),'"),') as s3,
 (case when s2 is null then s3 else concat(s3,'//',s2) end) as s4 from read_csv('c:/mywork/test.txt') where trim(test)<>''
 ) t) TO 'c:/mywork/result.txt' (HEADER false, DELIMITER ',', quote '');

*/
public class GameCommon {

  @Builder
  @Data
  public class Door {
    private byte index;
    private byte doorState;//0: closed, 1: opening, 2: open, 3: closing
    private byte imageIndex;
    private long lastTick;
    private Point location;
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

  public enum Spell
  {
    None((byte)0,"None"),

    //Warrior
    Fencing((byte)1,"Fencing"),
    Slaying((byte)2,"Slaying"),
    Thrusting((byte)3,"Thrusting"),
    HalfMoon((byte)4,"HalfMoon"),
    ShoulderDash((byte)5,"ShoulderDash"),
    TwinDrakeBlade((byte)6,"TwinDrakeBlade"),
    Entrapment((byte)7,"Entrapment"),
    FlamingSword((byte)8,"FlamingSword"),
    LionRoar((byte)9,"LionRoar"),
    CrossHalfMoon((byte)10,"CrossHalfMoon"),
    BladeAvalanche((byte)11,"BladeAvalanche"),
    ProtectionField((byte)12,"ProtectionField"),
    Rage((byte)13,"Rage"),
    CounterAttack((byte)14,"CounterAttack"),
    SlashingBurst((byte)15,"SlashingBurst"),
    Fury((byte)16,"Fury"),
    ImmortalSkin((byte)17,"ImmortalSkin"),

    //Wizard
    FireBall((byte)31,"FireBall"),
    Repulsion((byte)32,"Repulsion"),
    ElectricShock((byte)33,"ElectricShock"),
    GreatFireBall((byte)34,"GreatFireBall"),
    HellFire((byte)35,"HellFire"),
    ThunderBolt((byte)36,"ThunderBolt"),
    Teleport((byte)37,"Teleport"),
    FireBang((byte)38,"FireBang"),
    FireWall((byte)39,"FireWall"),
    Lightning((byte)40,"Lightning"),
    FrostCrunch((byte)41,"FrostCrunch"),
    ThunderStorm((byte)42,"ThunderStorm"),
    MagicShield((byte)43,"MagicShield"),
    TurnUndead((byte)44,"TurnUndead"),
    Vampirism((byte)45,"Vampirism"),
    IceStorm((byte)46,"IceStorm"),
    FlameDisruptor((byte)47,"FlameDisruptor"),
    Mirroring((byte)48,"Mirroring"),
    FlameField((byte)49,"FlameField"),
    Blizzard((byte)50,"Blizzard"),
    MagicBooster((byte)51,"MagicBooster"),
    MeteorStrike((byte)52,"MeteorStrike"),
    IceThrust((byte)53,"IceThrust"),
    FastMove((byte)54,"FastMove"),
    StormEscape((byte)55,"StormEscape"),

    //Taoist
    Healing((byte)61,"Healing"),
    SpiritSword((byte)62,"SpiritSword"),
    Poisoning((byte)63,"Poisoning"),
    SoulFireBall((byte)64,"SoulFireBall"),
    SummonSkeleton((byte)65,"SummonSkeleton"),
    Hiding((byte)67,"Hiding"),
    MassHiding((byte)68,"MassHiding"),
    SoulShield((byte)69,"SoulShield"),
    Revelation((byte)70,"Revelation"),
    BlessedArmour((byte)71,"BlessedArmour"),
    EnergyRepulsor((byte)72,"EnergyRepulsor"),
    TrapHexagon((byte)73,"TrapHexagon"),
    Purification((byte)74,"Purification"),
    MassHealing((byte)75,"MassHealing"),
    Hallucination((byte)76,"Hallucination"),
    UltimateEnhancer((byte)77,"UltimateEnhancer"),
    SummonShinsu((byte)78,"SummonShinsu"),
    Reincarnation((byte)79,"Reincarnation"),
    SummonHolyDeva((byte)80,"SummonHolyDeva"),
    Curse((byte)81,"Curse"),
    Plague((byte)82,"Plague"),
    PoisonCloud((byte)83,"PoisonCloud"),
    EnergyShield((byte)84,"EnergyShield"),
    PetEnhancer((byte)85,"PetEnhancer"),
    HealingCircle((byte)86,"HealingCircle"),

    //Assassin
    FatalSword((byte)91,"FatalSword"),
    DoubleSlash((byte)92,"DoubleSlash"),
    Haste((byte)93,"Haste"),
    FlashDash((byte)94,"FlashDash"),
    LightBody((byte)95,"LightBody"),
    HeavenlySword((byte)96,"HeavenlySword"),
    FireBurst((byte)97,"FireBurst"),
    Trap((byte)98,"Trap"),
    PoisonSword((byte)99,"PoisonSword"),
    MoonLight((byte)100,"MoonLight"),
    MPEater((byte)101,"MPEater"),
    SwiftFeet((byte)102,"SwiftFeet"),
    DarkBody((byte)103,"DarkBody"),
    Hemorrhage((byte)104,"Hemorrhage"),
    CrescentSlash((byte)105,"CrescentSlash"),
    MoonMist((byte)106,"MoonMist"),

    //Archer
    Focus((byte)121,"Focus"),
    StraightShot((byte)122,"StraightShot"),
    DoubleShot((byte)123,"DoubleShot"),
    ExplosiveTrap((byte)124,"ExplosiveTrap"),
    DelayedExplosion((byte)125,"DelayedExplosion"),
    Meditation((byte)126,"Meditation"),
    BackStep((byte)127,"BackStep"),
    ElementalShot((byte)128,"ElementalShot"),
    Concentration((byte)129,"Concentration"),
    Stonetrap((byte)130,"Stonetrap"),
    ElementalBarrier((byte)131,"ElementalBarrier"),
    SummonVampire((byte)132,"SummonVampire"),
    VampireShot((byte)133,"VampireShot"),
    SummonToad((byte)134,"SummonToad"),
    PoisonShot((byte)135,"PoisonShot"),
    CrippleShot((byte)136,"CrippleShot"),
    SummonSnakes((byte)137,"SummonSnakes"),
    NapalmShot((byte)138,"NapalmShot"),
    OneWithNature((byte)139,"OneWithNature"),
    BindingShot((byte)140,"BindingShot"),
    MentalState((byte)141,"MentalState"),

    //Custom
    Blink((byte)151,"Blink"),
    Portal((byte)152,"Portal"),

    //Map Events
    DigOutZombie((byte)200,"DigOutZombie"),
    Rubble((byte)201,"Rubble"),
    MapLightning((byte)202,"MapLightning"),
    MapLava((byte)203,"MapLava"),
    MapQuake1((byte)204,"MapQuake1"),
    MapQuake2((byte)205,"MapQuake2"),;

    private final byte code;
    private final String value;

    Spell(byte code,String value) {
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

  public enum ItemType
  {
    Nothing((byte)0,"Nothing"),
    Weapon((byte)1,"Weapon"),
    Armour((byte)2,"Armour"),
    Helmet((byte)4,"Helmet"),
    Necklace((byte)5,"Necklace"),
    Bracelet((byte)6,"Bracelet"),
    Ring((byte)7,"Ring"),
    Amulet((byte)8,"Amulet"),
    Belt((byte)9,"Belt"),
    Boots((byte)10,"Boots"),
    Stone((byte)11,"Stone"),
    Torch((byte)12,"Torch"),
    Potion((byte)13,"Potion"),
    Ore((byte)14,"Ore"),
    Meat((byte)15,"Meat"),
    CraftingMaterial((byte)16,"CraftingMaterial"),
    Scroll((byte)17,"Scroll"),
    Gem((byte)18,"Gem"),
    Mount((byte)19,"Mount"),
    Book((byte)20,"Book"),
    Script((byte)21,"Script"),
    Reins((byte)22,"Reins"),
    Bells((byte)23,"Bells"),
    Saddle((byte)24,"Saddle"),
    Ribbon((byte)25,"Ribbon"),
    Mask((byte)26,"Mask"),
    Food((byte)27,"Food"),
    Hook((byte)28,"Hook"),
    Float((byte)29,"Float"),
    Bait((byte)30,"Bait"),
    Finder((byte)31,"Finder"),
    Reel((byte)32,"Reel"),
    Fish((byte)33,"Fish"),
    Quest((byte)34,"Quest"),
    Awakening((byte)35,"Awakening"),
    Pets((byte)36,"Pets"),
    Transform((byte)37,"Transform"),;

    private final byte code;
    private final String value;

    ItemType(byte code,String value) {
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

  public enum ItemGrade
  {
    None((byte)0,"None"),
    Common((byte)1,"Common"),
    Rare((byte)2,"Rare"),
    Legendary((byte)3,"Legendary"),
    Mythical((byte)4,"Mythical"),;

    private final byte code;
    private final String value;

    ItemGrade(byte code,String value) {
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

  public enum RequiredType {
    Level((byte)0,"Level"),
    AC((byte)1,"AC"),
    MAC((byte)2,"MAC"),
    DC((byte)3,"DC"),
    MC((byte)4,"MC"),
    SC((byte)5,"SC"),;

    private final byte code;
    private final String value;

    RequiredType(byte code,String value) {
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

  public enum MirClass
  {
    Warrior((byte)0,"Warrior"),
    Wizard((byte)1,"Wizard"),
    Taoist((byte)2,"Taoist"),
    Assassin((byte)3,"Assassin"),
    Archer((byte)4,"Archer"),;

    private final byte code;
    private final String value;

    MirClass(byte code,String value) {
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

  public enum SpellEffect
  {
    None((byte)0,"None"),
    FatalSword((byte)1,"FatalSword"),
    Teleport((byte)2,"Teleport"),
    Healing((byte)3,"Healing"),
    RedMoonEvil((byte)4,"RedMoonEvil"),
    TwinDrakeBlade((byte)5,"TwinDrakeBlade"),
    MagicShieldUp((byte)6,"MagicShieldUp"),
    MagicShieldDown((byte)7,"MagicShieldDown"),
    GreatFoxSpirit((byte)8,"GreatFoxSpirit"),
    Entrapment((byte)9,"Entrapment"),
    Reflect((byte)10,"Reflect"),
    Critical((byte)11,"Critical"),
    Mine((byte)12,"Mine"),
    ElementalBarrierUp((byte)13,"ElementalBarrierUp"),
    ElementalBarrierDown((byte)14,"ElementalBarrierDown"),
    DelayedExplosion((byte)15,"DelayedExplosion"),
    MPEater((byte)16,"MPEater"),
    Hemorrhage((byte)17,"Hemorrhage"),
    Bleeding((byte)18,"Bleeding"),
    AwakeningSuccess((byte)19,"AwakeningSuccess"),
    AwakeningFail((byte)20,"AwakeningFail"),
    AwakeningMiss((byte)21,"AwakeningMiss"),
    AwakeningHit((byte)22,"AwakeningHit"),
    StormEscape((byte)23,"StormEscape"),
    TurtleKing((byte)24,"TurtleKing"),
    Behemoth((byte)25,"Behemoth"),
    Stunned((byte)26,"Stunned"),
    IcePillar((byte)27,"IcePillar"),;

    private final byte code;
    private final String value;

    SpellEffect(byte code,String value) {
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

  public enum LevelEffects
  {
    None((byte)0,"None"),
    Mist((byte)0x0001,"Mist"),
    RedDragon((byte)0x0002,"RedDragon"),
    BlueDragon((byte)0x0004,"BlueDragon"),;

    private final byte code;
    private final String value;

    LevelEffects(byte code,String value) {
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

  public enum RequiredClass
  {
    Warrior((byte)1,"Warrior"),
    Wizard((byte)2,"Wizard"),
    Taoist((byte)4,"Taoist"),
    Assassin((byte)8,"Assassin"),
    Archer((byte)16,"Archer"),
    WarWizTao((byte)(Warrior.code | Wizard.code | Taoist.code),"WarWizTao"),
    None((byte)(WarWizTao.code | Assassin.code | Archer.code),"None"),;

    private final byte code;
    private final String value;

    RequiredClass(byte code,String value) {
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

  public enum RequiredGender
  {
    Male((byte)1,"Male"),
    Female((byte)2,"Female"),
    None((byte)(Male.code | Female.code),"None");

    private final byte code;
    private final String value;

    RequiredGender(byte code,String value) {
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

  public enum ItemSet
  {
    None((byte)0,"None"),
    Spirit((byte)1,"Spirit"),
    Recall((byte)2,"Recall"),
    RedOrchid((byte)3,"RedOrchid"),
    RedFlower((byte)4,"RedFlower"),
    Smash((byte)5,"Smash"),
    HwanDevil((byte)6,"HwanDevil"),
    Purity((byte)7,"Purity"),
    FiveString((byte)8,"FiveString"),
    Mundane((byte)9,"Mundane"),
    NokChi((byte)10,"NokChi"),
    TaoProtect((byte)11,"TaoProtect"),
    Mir((byte)12,"Mir"),
    Bone((byte)13,"Bone"),
    Bug((byte)14,"Bug"),
    WhiteGold((byte)15,"WhiteGold"),
    WhiteGoldH((byte)16,"WhiteGoldH"),
    RedJade((byte)17,"RedJade"),
    RedJadeH((byte)18,"RedJadeH"),
    Nephrite((byte)19,"Nephrite"),
    NephriteH((byte)20,"NephriteH"),
    Whisker1((byte)21,"Whisker1"),
    Whisker2((byte)22,"Whisker2"),
    Whisker3((byte)23,"Whisker3"),
    Whisker4((byte)24,"Whisker4"),
    Whisker5((byte)25,"Whisker5"),
    Hyeolryong((byte)26,"Hyeolryong"),
    Monitor((byte)27,"Monitor"),
    Oppressive((byte)28,"Oppressive"),
    Paeok((byte)29,"Paeok"),
    Sulgwan((byte)30,"Sulgwan"),;

    private final byte code;
    private final String value;

    ItemSet(byte code,String value) {
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

  public enum BindMode
  {
    None((short)0,"None"),
    DontDeathdrop((short)1,"DontDeathdrop"),//0x0001
    DontDrop((short)2,"DontDrop"),//0x0002
    DontSell((short)4,"DontSell"),//0x0004
    DontStore((short)8,"DontStore"),//0x0008
    DontTrade((short)16,"DontTrade"),//0x0010
    DontRepair((short)32,"DontRepair"),//0x0020
    DontUpgrade((short)64,"DontUpgrade"),//0x0040
    DestroyOnDrop((short)128,"DestroyOnDrop"),//0x0080
    BreakOnDeath((short)256,"BreakOnDeath"),//0x0100
    BindOnEquip((short)512,"BindOnEquip"),//0x0200
    NoSRepair((short)1024,"NoSRepair"),//0x0400
    NoWeddingRing((short)2048,"NoWeddingRing"),;//0x0800

    private final short code;
    private final String value;

    BindMode(short code,String value) {
      this.code = code;
      this.value=value;
    }

    public final short code() {
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

  public enum SpecialItemMode
  {
    None((short)0,"None"),
    Paralize((short)0x0001,"Paralize"),
    Teleport((short)0x0002,"Teleport"),
    Clearring((short)0x0004,"Clearring"),
    Protection((short)0x0008,"Protection"),
    Revival((short)0x0010,"Revival"),
    Muscle((short)0x0020,"Muscle"),
    Flame((short)0x0040,"Flame"),
    Healing((short)0x0080,"Healing"),
    Probe((short)0x0100,"Probe"),
    Skill((short)0x0200,"Skill"),
    NoDuraLoss((short)0x0400,"NoDuraLoss"),;

    private final short code;
    private final String value;

    SpecialItemMode(short code,String value) {
      this.code = code;
      this.value=value;
    }

    public final short code() {
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

  public enum RefinedValue
  {
    None((byte)0,"None"),
    DC((byte)1,"DC"),
    MC((byte)2,"MC"),
    SC((byte)3,"SC"),;

    private final byte code;
    private final String value;

    RefinedValue(byte code,String value) {
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

  public enum QuestType {
    General((byte)0,"General"),
    Daily((byte)1,"Daily"),
    Repeatable((byte)2,"Repeatable"),
    Story((byte)3,"Story"),;

    private final byte code;
    private final String value;

    QuestType(byte code,String value) {
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

  public enum AttackMode
  {
    Peace((byte)0,"Peace"),
    Group((byte)1,"Group"),
    Guild((byte)2,"Guild"),
    EnemyGuild((byte)3,"EnemyGuild"),
    RedBrown((byte)4,"RedBrown"),
    All((byte)5,"All"),;

    private final byte code;
    private final String value;

    AttackMode(byte code,String value) {
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

  public enum PetMode {
    Both((byte)0,"Both"),
    MoveOnly((byte)1,"MoveOnly"),
    AttackOnly((byte)2,"AttackOnly"),
    None((byte)3,"None"),;

    private final byte code;
    private final String value;

    PetMode(byte code,String value) {
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

  public enum OutputMessageType
  {
    Normal((byte)0,"Normal"),
    Quest((byte)1,"Quest"),;

    private final byte code;
    private final String value;

    OutputMessageType(byte code,String value) {
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

  public enum Monster {
    Guard(0,"Guard"),
    TaoistGuard(1,"TaoistGuard"),
    Guard2(2,"Guard2"),
    Hen(3,"Hen"),
    Deer(4,"Deer"),
    Scarecrow(5,"Scarecrow"),
    HookingCat(6,"HookingCat"),
    RakingCat(7,"RakingCat"),
    Yob(8,"Yob"),
    Oma(9,"Oma"),
    CannibalPlant(10,"CannibalPlant"),
    ForestYeti(11,"ForestYeti"),
    SpittingSpider(12,"SpittingSpider"),
    ChestnutTree(13,"ChestnutTree"),
    EbonyTree(14,"EbonyTree"),
    LargeMushroom(15,"LargeMushroom"),
    CherryTree(16,"CherryTree"),
    OmaFighter(17,"OmaFighter"),
    OmaWarrior(18,"OmaWarrior"),
    CaveBat(19,"CaveBat"),
    CaveMaggot(20,"CaveMaggot"),
    Scorpion(21,"Scorpion"),
    Skeleton(22,"Skeleton"),
    BoneFighter(23,"BoneFighter"),
    AxeSkeleton(24,"AxeSkeleton"),
    BoneWarrior(25,"BoneWarrior"),
    BoneElite(26,"BoneElite"),
    Dung(27,"Dung"),
    Dark(28,"Dark"),
    WoomaSoldier(29,"WoomaSoldier"),
    WoomaFighter(30,"WoomaFighter"),
    WoomaWarrior(31,"WoomaWarrior"),
    FlamingWooma(32,"FlamingWooma"),
    WoomaGuardian(33,"WoomaGuardian"),
    WoomaTaurus(34,"WoomaTaurus"),
    WhimperingBee(35,"WhimperingBee"),
    GiantWorm(36,"GiantWorm"),
    Centipede(37,"Centipede"),
    BlackMaggot(38,"BlackMaggot"),
    Tongs(39,"Tongs"),
    EvilTongs(40,"EvilTongs"),
    EvilCentipede(41,"EvilCentipede"),
    BugBat(42,"BugBat"),
    BugBatMaggot(43,"BugBatMaggot"),
    WedgeMoth(44,"WedgeMoth"),
    RedBoar(45,"RedBoar"),
    BlackBoar(46,"BlackBoar"),
    SnakeScorpion(47,"SnakeScorpion"),
    WhiteBoar(48,"WhiteBoar"),
    EvilSnake(49,"EvilSnake"),
    BombSpider(50,"BombSpider"),
    RootSpider(51,"RootSpider"),
    SpiderBat(52,"SpiderBat"),
    VenomSpider(53,"VenomSpider"),
    GangSpider(54,"GangSpider"),
    GreatSpider(55,"GreatSpider"),
    LureSpider(56,"LureSpider"),
    BigApe(57,"BigApe"),
    EvilApe(58,"EvilApe"),
    GrayEvilApe(59,"GrayEvilApe"),
    RedEvilApe(60,"RedEvilApe"),
    CrystalSpider(61,"CrystalSpider"),
    RedMoonEvil(62,"RedMoonEvil"),
    BigRat(63,"BigRat"),
    ZumaArcher(64,"ZumaArcher"),
    ZumaStatue(65,"ZumaStatue"),
    ZumaGuardian(66,"ZumaGuardian"),
    RedThunderZuma(67,"RedThunderZuma"),
    ZumaTaurus(68,"ZumaTaurus"),
    DigOutZombie(69,"DigOutZombie"),
    ClZombie(70,"ClZombie"),
    NdZombie(71,"NdZombie"),
    CrawlerZombie(72,"CrawlerZombie"),
    ShamanZombie(73,"ShamanZombie"),
    Ghoul(74,"Ghoul"),
    KingScorpion(75,"KingScorpion"),
    KingHog(76,"KingHog"),
    DarkDevil(77,"DarkDevil"),
    BoneFamiliar(78,"BoneFamiliar"),
    Shinsu(79,"Shinsu"),
    Shinsu1(80,"Shinsu1"),
    SpiderFrog(81,"SpiderFrog"),
    HoroBlaster(82,"HoroBlaster"),
    BlueHoroBlaster(83,"BlueHoroBlaster"),
    KekTal(84,"KekTal"),
    VioletKekTal(85,"VioletKekTal"),
    Khazard(86,"Khazard"),
    RoninGhoul(87,"RoninGhoul"),
    ToxicGhoul(88,"ToxicGhoul"),
    BoneCaptain(89,"BoneCaptain"),
    BoneSpearman(90,"BoneSpearman"),
    BoneBlademan(91,"BoneBlademan"),
    BoneArcher(92,"BoneArcher"),
    BoneLord(93,"BoneLord"),
    Minotaur(94,"Minotaur"),
    IceMinotaur(95,"IceMinotaur"),
    ElectricMinotaur(96,"ElectricMinotaur"),
    WindMinotaur(97,"WindMinotaur"),
    FireMinotaur(98,"FireMinotaur"),
    RightGuard(99,"RightGuard"),
    LeftGuard(100,"LeftGuard"),
    MinotaurKing(101,"MinotaurKing"),
    FrostTiger(102,"FrostTiger"),
    Sheep(103,"Sheep"),
    Wolf(104,"Wolf"),
    ShellNipper(105,"ShellNipper"),
    Keratoid(106,"Keratoid"),
    GiantKeratoid(107,"GiantKeratoid"),
    SkyStinger(108,"SkyStinger"),
    SandWorm(109,"SandWorm"),
    VisceralWorm(110,"VisceralWorm"),
    RedSnake(111,"RedSnake"),
    TigerSnake(112,"TigerSnake"),
    Yimoogi(113,"Yimoogi"),
    GiantWhiteSnake(114,"GiantWhiteSnake"),
    BlueSnake(115,"BlueSnake"),
    YellowSnake(116,"YellowSnake"),
    HolyDeva(117,"HolyDeva"),
    AxeOma(118,"AxeOma"),
    SwordOma(119,"SwordOma"),
    CrossbowOma(120,"CrossbowOma"),
    WingedOma(121,"WingedOma"),
    FlailOma(122,"FlailOma"),
    OmaGuard(123,"OmaGuard"),
    YinDevilNode(124,"YinDevilNode"),
    YangDevilNode(125,"YangDevilNode"),
    OmaKing(126,"OmaKing"),
    BlackFoxman(127,"BlackFoxman"),
    RedFoxman(128,"RedFoxman"),
    WhiteFoxman(129,"WhiteFoxman"),
    TrapRock(130,"TrapRock"),
    GuardianRock(131,"GuardianRock"),
    ThunderElement(132,"ThunderElement"),
    CloudElement(133,"CloudElement"),
    GreatFoxSpirit(134,"GreatFoxSpirit"),
    HedgeKekTal(135,"HedgeKekTal"),
    BigHedgeKekTal(136,"BigHedgeKekTal"),
    RedFrogSpider(137,"RedFrogSpider"),
    BrownFrogSpider(138,"BrownFrogSpider"),
    ArcherGuard(139,"ArcherGuard"),
    KatanaGuard(140,"KatanaGuard"),
    Pig(142,"Pig"),
    Bull(143,"Bull"),
    Bush(144,"Bush"),
    ChristmasTree(145,"ChristmasTree"),
    HighAssassin(146,"HighAssassin"),
    DarkDustPile(147,"DarkDustPile"),
    DarkBrownWolf(148,"DarkBrownWolf"),
    Football(149,"Football"),
    GingerBreadman(150,"GingerBreadman"),
    HalloweenScythe(151,"HalloweenScythe"),
    GhastlyLeecher(152,"GhastlyLeecher"),
    CyanoGhast(153,"CyanoGhast"),
    MutatedManworm(154,"MutatedManworm"),
    CrazyManworm(155,"CrazyManworm"),
    MudPile(156,"MudPile"),
    TailedLion(157,"TailedLion"),
    Behemoth(158,"Behemoth"),//done BOSS
    DarkDevourer(159,"DarkDevourer"),//done
    PoisonHugger(160,"PoisonHugger"),//done
    Hugger(161,"Hugger"),//done
    MutatedHugger(162,"MutatedHugger"),//done
    DreamDevourer(163,"DreamDevourer"),//done
    Treasurebox(164,"Treasurebox"),//done
    SnowPile(165,"SnowPile"),//done
    Snowman(166,"Snowman"),//done
    SnowTree(167,"SnowTree"),//done
    GiantEgg(168,"GiantEgg"),//done
    RedTurtle(169,"RedTurtle"),//done
    GreenTurtle(170,"GreenTurtle"),//done
    BlueTurtle(171,"BlueTurtle"),//done
    Catapult(172,"Catapult"),//not added frames
    SabukWallSection(173,"SabukWallSection"),//not added frames
    NammandWallSection(174,"NammandWallSection"),//not added frames
    SiegeRepairman(175,"SiegeRepairman"),//not added frames
    BlueSanta(176,"BlueSanta"),//done
    BattleStandard(177,"BattleStandard"),//done
    ArcherGuard2(178,"ArcherGuard2"),//done
    RedYimoogi(179,"RedYimoogi"),//done
    LionRiderMale(180,"LionRiderMale"),//frames not added
    LionRiderFemale(181,"LionRiderFemale"),//frames not added
    Tornado(182,"Tornado"),//done
    FlameTiger(183,"FlameTiger"),//done
    WingedTigerLord(184,"WingedTigerLord"),//done BOSS
    TowerTurtle(185,"TowerTurtle"),//done
    FinialTurtle(186,"FinialTurtle"),//done
    TurtleKing(187,"TurtleKing"),//done BOSS
    DarkTurtle(188,"DarkTurtle"),//done
    LightTurtle(189,"LightTurtle"),//done
    DarkSwordOma(190,"DarkSwordOma"),//done
    DarkAxeOma(191,"DarkAxeOma"),//done
    DarkCrossbowOma(192,"DarkCrossbowOma"),//done
    DarkWingedOma(193,"DarkWingedOma"),//done
    BoneWhoo(194,"BoneWhoo"),//done
    DarkSpider(195,"DarkSpider"),//done
    ViscusWorm(196,"ViscusWorm"),//done
    ViscusCrawler(197,"ViscusCrawler"),//done
    CrawlerLave(198,"CrawlerLave"),//done
    DarkYob(199,"DarkYob"),//done
    FlamingMutant(200,"FlamingMutant"),//FINISH
    StoningStatue(201,"StoningStatue"),//FINISH BOSS
    FlyingStatue(202,"FlyingStatue"),//FINISH
    ValeBat(203,"ValeBat"),//done
    Weaver(204,"Weaver"),//done
    VenomWeaver(205,"VenomWeaver"),//done
    CrackingWeaver(206,"CrackingWeaver"),//done
    ArmingWeaver(207,"ArmingWeaver"),//done
    CrystalWeaver(208,"CrystalWeaver"),//done
    FrozenZumaStatue(209,"FrozenZumaStatue"),//done
    FrozenZumaGuardian(210,"FrozenZumaGuardian"),//done
    FrozenRedZuma(211,"FrozenRedZuma"),//done
    GreaterWeaver(212,"GreaterWeaver"),//done
    SpiderWarrior(213,"SpiderWarrior"),//done
    SpiderBarbarian(214,"SpiderBarbarian"),//done
    HellSlasher(215,"HellSlasher"),//done
    HellPirate(216,"HellPirate"),//done
    HellCannibal(217,"HellCannibal"),//done
    HellKeeper(218,"HellKeeper"),//done BOSS
    HellBolt(219,"HellBolt"),//done
    WitchDoctor(220,"WitchDoctor"),//done
    ManectricHammer(221,"ManectricHammer"),//done
    ManectricClub(222,"ManectricClub"),//done
    ManectricClaw(223,"ManectricClaw"),//done
    ManectricStaff(224,"ManectricStaff"),//done
    NamelessGhost(225,"NamelessGhost"),//done
    DarkGhost(226,"DarkGhost"),//done
    ChaosGhost(227,"ChaosGhost"),//done
    ManectricBlest(228,"ManectricBlest"),//done
    ManectricKing(229,"ManectricKing"),//done
    FrozenDoor(230,"FrozenDoor"),//done
    IcePillar(231,"IcePillar"),//done
    FrostYeti(232,"FrostYeti"),//done
    ManectricSlave(233,"ManectricSlave"),//done
    TrollHammer(234,"TrollHammer"),//done
    TrollBomber(235,"TrollBomber"),//done
    TrollStoner(236,"TrollStoner"),//done
    TrollKing(237,"TrollKing"),//done BOSS
    FlameSpear(238,"FlameSpear"),//done
    FlameMage(239,"FlameMage"),//done
    FlameScythe(240,"FlameScythe"),//done
    FlameAssassin(241,"FlameAssassin"),//done
    FlameQueen(242,"FlameQueen"),//finish BOSS
    HellKnight1(243,"HellKnight1"),//done
    HellKnight2(244,"HellKnight2"),//done
    HellKnight3(245,"HellKnight3"),//done
    HellKnight4(246,"HellKnight4"),//done
    HellLord(247,"HellLord"),//done BOSS
    WaterGuard(248,"WaterGuard"),//done
    IceGuard(249,"IceGuard"),
    ElementGuard(250,"ElementGuard"),
    DemonGuard(251,"DemonGuard"),
    KingGuard(252,"KingGuard"),
    Snake10(253,"Snake10"),//done
    Snake11(254,"Snake11"),//done
    Snake12(255,"Snake12"),//done
    Snake13(256,"Snake13"),//done
    Snake14(257,"Snake14"),//done
    Snake15(258,"Snake15"),//done
    Snake16(259,"Snake16"),//done
    Snake17(260,"Snake17"),//done
    DeathCrawler(261,"DeathCrawler"),
    BurningZombie(262,"BurningZombie"),
    MudZombie(263,"MudZombie"),
    FrozenZombie(264,"FrozenZombie"),
    UndeadWolf(265,"UndeadWolf"),
    Demonwolf(266,"Demonwolf"),
    WhiteMammoth(267,"WhiteMammoth"),
    DarkBeast(268,"DarkBeast"),
    LightBeast(269,"LightBeast"),
    BloodBaboon(270,"BloodBaboon"),
    HardenRhino(271,"HardenRhino"),
    AncientBringer(272,"AncientBringer"),
    FightingCat(273,"FightingCat"),
    FireCat(274,"FireCat"),
    CatWidow(275,"CatWidow"),
    StainHammerCat(276,"StainHammerCat"),
    BlackHammerCat(277,"BlackHammerCat"),
    StrayCat(278,"StrayCat"),
    CatShaman(279,"CatShaman"),
    Jar1(280,"Jar1"),
    Jar2(281,"Jar2"),
    SeedingsGeneral(282,"SeedingsGeneral"),
    RestlessJar(283,"RestlessJar"),
    GeneralJinmYo(284,"GeneralJinmYo"),
    Bunny(285,"Bunny"),
    Tucson(286,"Tucson"),
    TucsonFighter(287,"TucsonFighter"),
    TucsonMage(288,"TucsonMage"),
    TucsonWarrior(289,"TucsonWarrior"),
    Armadillo(290,"Armadillo"),
    ArmadilloElder(291,"ArmadilloElder"),
    TucsonEgg(292,"TucsonEgg"),
    PlaguedTucson(293,"PlaguedTucson"),
    SandSnail(294,"SandSnail"),
    CannibalTentacles(295,"CannibalTentacles"),
    TucsonGeneral(296,"TucsonGeneral"),
    GasToad(297,"GasToad"),
    Mantis(298,"Mantis"),
    SwampWarrior(299,"SwampWarrior"),
    AssassinBird(300,"AssassinBird"),
    RhinoWarrior(301,"RhinoWarrior"),
    RhinoPriest(302,"RhinoPriest"),
    SwampSlime(303,"SwampSlime"),
    RockGuard(304,"RockGuard"),
    MudWarrior(305,"MudWarrior"),
    SmallPot(306,"SmallPot"),
    TreeQueen(307,"TreeQueen"),
    ShellFighter(308,"ShellFighter"),
    DarkBaboon(309,"DarkBaboon"),
    TwinHeadBeast(310,"TwinHeadBeast"),
    OmaCannibal(311,"OmaCannibal"),
    OmaBlest(312,"OmaBlest"),
    OmaSlasher(313,"OmaSlasher"),
    OmaAssassin(314,"OmaAssassin"),
    OmaMage(315,"OmaMage"),
    OmaWitchDoctor(316,"OmaWitchDoctor"),
    LightningBead(317,"LightningBead"),
    HealingBead(318,"HealingBead"),
    PowerUpBead(319,"PowerUpBead"),
    DarkOmaKing(320,"DarkOmaKing"),
    CaveMage(321,"CaveMage"),
    Mandrill(322,"Mandrill"),
    PlagueCrab(323,"PlagueCrab"),
    CreeperPlant(324,"CreeperPlant"),
    FloatingWraith(325,"FloatingWraith"),
    ArmedPlant(326,"ArmedPlant"),
    AvengerPlant(327,"AvengerPlant"),
    Nadz(328,"Nadz"),
    AvengingSpirit(329,"AvengingSpirit"),
    AvengingWarrior(330,"AvengingWarrior"),
    AxePlant(331,"AxePlant"),
    WoodBox(332,"WoodBox"),
    ClawBeast(333,"ClawBeast"),
    KillerPlant(334,"KillerPlant"),
    SackWarrior(335,"SackWarrior"),
    WereTiger(336,"WereTiger"),
    KingHydrax(337,"KingHydrax"),
    Hydrax(338,"Hydrax"),
    HornedMage(339,"HornedMage"),
    Basiloid(340,"Basiloid"),
    HornedArcher(341,"HornedArcher"),
    ColdArcher(342,"ColdArcher"),
    HornedWarrior(343,"HornedWarrior"),
    FloatingRock(344,"FloatingRock"),
    ScalyBeast(345,"ScalyBeast"),
    HornedSorceror(346,"HornedSorceror"),
    BoulderSpirit(347,"BoulderSpirit"),
    HornedCommander(348,"HornedCommander"),
    MoonStone(349,"MoonStone"),
    SunStone(350,"SunStone"),
    LightningStone(351,"LightningStone"),
    Turtlegrass(352,"Turtlegrass"),
    Mantree(353,"Mantree"),
    Bear(354,"Bear"),
    Leopard(355,"Leopard"),
    ChieftainArcher(356,"ChieftainArcher"),
    ChieftainSword(357,"ChieftainSword"),
    StoningSpider(358,"StoningSpider"),//Archer Spell mob (not yet coded)
    VampireSpider(359,"VampireSpider"),//Archer Spell mob
    SpittingToad(360,"SpittingToad"),//Archer Spell mob
    SnakeTotem(361,"SnakeTotem"),//Archer Spell mob
    CharmedSnake(362,"CharmedSnake"),//Archer Spell mob
    FrozenSoldier(363,"FrozenSoldier"),
    FrozenFighter(364,"FrozenFighter"),
    FrozenArcher(365,"FrozenArcher"),
    FrozenKnight(366,"FrozenKnight"),
    FrozenGolem(367,"FrozenGolem"),
    IcePhantom(368,"IcePhantom"),
    SnowWolf(369,"SnowWolf"),
    SnowWolfKing(370,"SnowWolfKing"),
    WaterDragon(371,"WaterDragon"),
    BlackTortoise(372,"BlackTortoise"),
    Manticore(373,"Manticore"),
    DragonWarrior(374,"DragonWarrior"),
    DragonArcher(375,"DragonArcher"),
    Kirin(376,"Kirin"),
    Guard3(377,"Guard3"),
    ArcherGuard3(378,"ArcherGuard3"),
    Bunny2(379,"Bunny2"),
    FrozenMiner(380,"FrozenMiner"),
    FrozenAxeman(381,"FrozenAxeman"),
    FrozenMagician(382,"FrozenMagician"),
    SnowYeti(383,"SnowYeti"),
    IceCrystalSoldier(384,"IceCrystalSoldier"),
    DarkWraith(385,"DarkWraith"),
    DarkSpirit(386,"DarkSpirit"),
    CrystalBeast(387,"CrystalBeast"),
    RedOrb(388,"RedOrb"),
    BlueOrb(389,"BlueOrb"),
    YellowOrb(390,"YellowOrb"),
    GreenOrb(391,"GreenOrb"),
    WhiteOrb(392,"WhiteOrb"),
    FatalLotus(393,"FatalLotus"),
    AntCommander(394,"AntCommander"),
    CargoBoxwithlogo(395,"CargoBoxwithlogo"),
    Doe(396,"Doe"),
    Reindeer(397,"Reindeer"),//frames not added
    AngryReindeer(398,"AngryReindeer"),
    CargoBox(399,"CargoBox"),
    Ram1(400,"Ram1"),
    Ram2(401,"Ram2"),
    Kite(402,"Kite"),
    EvilMir(900,"EvilMir"),
    EvilMirBody(901,"EvilMirBody"),
    DragonStatue(902,"DragonStatue"),
    HellBomb1(903,"HellBomb1"),
    HellBomb2(904,"HellBomb2"),
    HellBomb3(905,"HellBomb3"),
    SabukGate(950,"SabukGate"),
    PalaceWallLeft(951,"PalaceWallLeft"),
    PalaceWall1(952,"PalaceWall1"),
    PalaceWall2(953,"PalaceWall2"),
    GiGateSouth(954,"GiGateSouth"),
    GiGateEast(955,"GiGateEast"),
    GiGateWest(956,"GiGateWest"),
    SSabukWall1(957,"SSabukWall1"),
    SSabukWall2(958,"SSabukWall2"),
    SSabukWall3(959,"SSabukWall3"),
    BabyPig(10000,"BabyPig"),//Permanent
    Chick(10001,"Chick"),//Special
    Kitten(10002,"Kitten"),//Permanent
    BabySkeleton(10003,"BabySkeleton"),//Special
    Baekdon(10004,"Baekdon"),//Special
    Wimaen(10005,"Wimaen"),//Event
    BlackKitten(10006,"BlackKitten"),//unknown
    BabyDragon(10007,"BabyDragon"),//unknown
    OlympicFlame(10008,"OlympicFlame"),//unknown
    BabySnowMan(10009,"BabySnowMan"),//unknown
    Frog(10010,"Frog"),//unknown
    BabyMonkey(10011,"BabyMonkey"),;//unknown

    private final int code;
    private final String value;

    Monster(int code,String value) {
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

  @Data
  @Builder
  public class RandomItemStat {
    private byte maxDuraChance, maxDuraStatChance, maxDuraMaxStat;
    private byte maxAcChance, maxAcStatChance, maxAcMaxStat, maxMacChance, maxMacStatChance, maxMacMaxStat, maxDcChance, maxDcStatChance, maxDcMaxStat, maxMcChance, maxMcStatChance, maxMcMaxStat, maxScChance, maxScStatChance, maxScMaxStat;
    private byte accuracyChance, accuracyStatChance, accuracyMaxStat, agilityChance, agilityStatChance, agilityMaxStat, hpChance, hpStatChance, hpMaxStat, mpChance, mpStatChance, mpMaxStat, strongChance, strongStatChance, strongMaxStat;
    private byte magicResistChance, magicResistStatChance, magicResistMaxStat, poisonResistChance, poisonResistStatChance, poisonResistMaxStat;
    private byte hpRecovChance, hpRecovStatChance, hpRecovMaxStat, mpRecovChance, mpRecovStatChance, mpRecovMaxStat, poisonRecovChance, poisonRecovStatChance, poisonRecovMaxStat;
    private byte criticalRateChance, criticalRateStatChance, criticalRateMaxStat, criticalDamageChance, criticalDamageStatChance, criticalDamageMaxStat;
    private byte freezeChance, freezeStatChance, freezeMaxStat, poisonAttackChance, poisonAttackStatChance, poisonAttackMaxStat;
    private byte attackSpeedChance, attackSpeedStatChance, attackSpeedMaxStat, luckChance, luckStatChance, luckMaxStat;
    private byte curseChance;
  }

  @Data
  @Builder
  public class ChatItem {
    private long recievedTick = 0;
    private long id = 0;
    private UserItem itemStats;
  }

  @Data
  @Builder
  public class ExpireInfo {
    private Date ExpiryDate;
  }

  @Data
  @Builder
  public class UserId {
    private long id = 0;
    private String userName = "";
  }

  @Data
  @Builder
  public class ItemInfo
  {
    private int Index;
    private String name = "";
    private ItemType type;
    private ItemGrade grade;
    private RequiredType requiredType = RequiredType.Level;
    private RequiredClass requiredClass = RequiredClass.None;
    private RequiredGender requiredGender = RequiredGender.None;
    private ItemSet set;

    private short shape;
    private byte weight, light, requiredAmount;

    private int image, durability;

    private long price, stackSize = 1;

    private byte ac, mac, dc, mc, sc, accuracy, agility;
    private int hp, mp;
    private byte attackSpeed, luck;
    private byte bagWeight, handWeight, wearWeight;

    private boolean startItem;
    private byte effect;

    private byte strong;
    private byte magicResist, poisonResist, healthRecovery, spellRecovery, poisonRecovery, HPrate, MPrate;
    private byte criticalRate, criticalDamage;
    private boolean needIdentify, showGroupPickup;
    private boolean classBased;
    private boolean levelBased;
    private boolean canMine;
    private boolean canFastRun;
    private boolean canAwakening;
    private byte maxAcRate, maxMacRate, holy, freezing, poisonAttack, HPDrainRate;

    private BindMode bind = BindMode.None;
    private byte reflect;
    private SpecialItemMode unique = SpecialItemMode.None;
    private byte randomStatsId;
    private RandomItemStat randomStats;
    private String toolTip = "";

  }

  @Data
  @Builder
  public class UserItem {

    private long uniqueID;
    private int itemIndex;

    private ItemInfo info;
    private int currentDura, maxDura;
    private long count = 1, gemCount = 0;

    private byte ac, mac, dc, mc, sc, accuracy, agility, HP, MP, strong, magicResist, poisonResist, healthRecovery, manaRecovery, poisonRecovery, criticalRate, criticalDamage, freezing, poisonAttack;
    private byte attackSpeed, luck;
    private byte upgradeLevel;
    private RefinedValue refinedValue = RefinedValue.None;
    private byte refineAdded = 0;

    private boolean duraChanged;
    private int soulBoundId = -1;
    private boolean identified = false;
    private boolean cursed = false;

    private int weddingRing = -1;

    private UserItem[] slots = new UserItem[Settings.MIR_INIT_USERITEMSLOTS_SIZE];

    private Date buybackExpiryDate;

    private ExpireInfo expireInfo;

    private byte[] awakeTypes;
  }

  @Data
  @Builder
  public class QuestItemReward {
    private ItemInfo item;
    private long count = 1;
  }

  @Data
  @Builder
  public class ClientQuestInfo {

    private int index;
    private long npcIndex;

    private String name, group;
    private List<String> description = new ArrayList<>();
    private List<String> taskDescription = new ArrayList<>();
    private List<String> completionDescription = new ArrayList<>();

    private int minLevelNeeded, maxLevelNeeded;
    private int questNeeded;
    private RequiredClass classNeeded;

    private QuestType type;

    private long RewardGold;
    private long RewardExp;
    private long RewardCredit;
    private List<QuestItemReward> RewardsFixedItem = new ArrayList<>();
    private List<QuestItemReward> RewardsSelectItem = new ArrayList<>();

    private long finishNPCIndex;
  }
}
