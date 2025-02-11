package com.kindred.mir.scene.game.objects;

import static com.kindred.mir.GameCommon.MirDirection.Up;

import com.kindred.mir.GameCommon.MirDirection;
import com.kindred.mir.util.Point;
import java.util.ArrayList;

public class UserObject extends PlayerObject{

  public long Id;

  public int HP, MaxHP, MP, MaxMP;

  public int AC,
      MAC,
      DC,
      MC,
      SC;

  public byte Accuracy, Agility;
  public short ASpeed, Luck;
  public int AttackSpeed;

  public int CurrentHandWeight, MaxHandWeight,
      CurrentWearWeight, MaxWearWeight;
  public int CurrentBagWeight, MaxBagWeight;
  public long Experience, MaxExperience;
  public byte LifeOnHit;

  public boolean TradeLocked;
  public boolean AllowTrade;

  public boolean HasTeleportRing, HasProtectionRing, HasRevivalRing, HasClearRing,
      HasMuscleRing, HasParalysisRing, HasFireRing, HasHealRing, HasProbeNecklace, HasSkillNecklace, NoDuraLoss;

  public byte MagicResist, PoisonResist, HealthRecovery, SpellRecovery, PoisonRecovery, CriticalRate, CriticalDamage, Holy, Freezing, PoisonAttack, HpDrainRate;
  public BaseStats CoreStats = new BaseStats(0);


  public UserItem[] Inventory = new UserItem[46], Equipment = new UserItem[14], Trade = new UserItem[10], QuestInventory = new UserItem[40];
  public int BeltIdx = 6;
  public List<ClientMagic> Magics = new ArrayList<>();
  public List<ItemSets> ItemSets = new ArrayList<>();
  public List<EquipmentSlot> MirSet = new ArrayList<>();

  public List<ClientIntelligentCreature> IntelligentCreatures = new ArrayList<>();//IntelligentCreature
  public IntelligentCreatureType SummonedCreatureType = IntelligentCreatureType.None;//IntelligentCreature
  public boolean CreatureSummoned;//IntelligentCreature
  public int PearlCount = 0;

  public List<ClientQuestProgress> CurrentQuests = new ArrayList<>();
  public List<int> CompletedQuests = new ArrayList<>();
  public List<ClientMail> Mail = new ArrayList<>();

  public ClientMagic NextMagic;
  public Point NextMagicLocation;
  public MapObject NextMagicObject;
  public MirDirection NextMagicDirection;
  public QueuedAction QueuedAction;



  public void clearMagic()
  {
    NextMagic = null;
    NextMagicDirection = Up;
    NextMagicLocation = Point.Empty;
    NextMagicObject = null;
  }
}
