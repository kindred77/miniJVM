package com.kindred.mir.scene.game;

import com.kindred.mir.GameCommon.AttackMode;
import com.kindred.mir.GameCommon.ChatItem;
import com.kindred.mir.GameCommon.ChatType;
import com.kindred.mir.GameCommon.ClientQuestInfo;
import com.kindred.mir.GameCommon.ItemInfo;
import com.kindred.mir.GameCommon.LightSetting;
import com.kindred.mir.GameCommon.PetMode;
import com.kindred.mir.GameCommon.UserId;
import com.kindred.mir.GameCommon.UserItem;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithStaticImage;
import com.kindred.mir.controls.MirItemCell;
import com.kindred.mir.controls.MirLabel;
import com.kindred.mir.scene.MirScene;
import com.kindred.mir.scene.game.bean.OutPutMessage;
import com.kindred.mir.scene.game.dialogs.BeltDialog;
import com.kindred.mir.scene.game.dialogs.BigMapDialog;
import com.kindred.mir.scene.game.dialogs.CharacterDialog;
import com.kindred.mir.scene.game.dialogs.CharacterDuraPanel;
import com.kindred.mir.scene.game.dialogs.ChatControlBar;
import com.kindred.mir.scene.game.dialogs.ChatDialog;
import com.kindred.mir.scene.game.dialogs.ChatNoticeDialog;
import com.kindred.mir.scene.game.dialogs.ChatOptionDialog;
import com.kindred.mir.scene.game.dialogs.DuraStatusDialog;
import com.kindred.mir.scene.game.dialogs.FishingDialog;
import com.kindred.mir.scene.game.dialogs.FishingStatusDialog;
import com.kindred.mir.scene.game.dialogs.FriendDialog;
import com.kindred.mir.scene.game.dialogs.GameShopDialog;
import com.kindred.mir.scene.game.dialogs.GroupDialog;
import com.kindred.mir.scene.game.dialogs.GuestTradeDialog;
import com.kindred.mir.scene.game.dialogs.GuildDialog;
import com.kindred.mir.scene.game.dialogs.HelpDialog;
import com.kindred.mir.scene.game.dialogs.InspectDialog;
import com.kindred.mir.scene.game.dialogs.IntelligentCreatureDialog;
import com.kindred.mir.scene.game.dialogs.IntelligentCreatureOptionsDialog;
import com.kindred.mir.scene.game.dialogs.IntelligentCreatureOptionsGradeDialog;
import com.kindred.mir.scene.game.dialogs.InventoryDialog;
import com.kindred.mir.scene.game.dialogs.KeyboardLayoutDialog;
import com.kindred.mir.scene.game.dialogs.MailComposeLetterDialog;
import com.kindred.mir.scene.game.dialogs.MailComposeParcelDialog;
import com.kindred.mir.scene.game.dialogs.MailListDialog;
import com.kindred.mir.scene.game.dialogs.MailReadLetterDialog;
import com.kindred.mir.scene.game.dialogs.MailReadParcelDialog;
import com.kindred.mir.scene.game.dialogs.MainDialog;
import com.kindred.mir.scene.game.dialogs.MemoDialog;
import com.kindred.mir.scene.game.dialogs.MentorDialog;
import com.kindred.mir.scene.game.dialogs.MenuDialog;
import com.kindred.mir.scene.game.dialogs.MiniMapDialog;
import com.kindred.mir.scene.game.dialogs.MountDialog;
import com.kindred.mir.scene.game.dialogs.NPCDialog;
import com.kindred.mir.scene.game.dialogs.NPCDropDialog;
import com.kindred.mir.scene.game.dialogs.NPCGoodsDialog;
import com.kindred.mir.scene.game.dialogs.NPCUpgradeDialog;
import com.kindred.mir.scene.game.dialogs.OptionDialog;
import com.kindred.mir.scene.game.dialogs.QuestDetailDialog;
import com.kindred.mir.scene.game.dialogs.QuestDiaryDialog;
import com.kindred.mir.scene.game.dialogs.QuestListDialog;
import com.kindred.mir.scene.game.dialogs.QuestTrackingDialog;
import com.kindred.mir.scene.game.dialogs.RankingDialog;
import com.kindred.mir.scene.game.dialogs.RefineDialog;
import com.kindred.mir.scene.game.dialogs.RelationshipDialog;
import com.kindred.mir.scene.game.dialogs.ReportDialog;
import com.kindred.mir.scene.game.dialogs.SkillBarDialog;
import com.kindred.mir.scene.game.dialogs.StorageDialog;
import com.kindred.mir.scene.game.dialogs.TradeDialog;
import com.kindred.mir.scene.game.dialogs.TrustMerchantDialog;
import com.kindred.mir.scene.game.map.MapCommonControl;
import com.kindred.mir.scene.game.map.MapMainControl;
import com.kindred.mir.scene.game.objects.MapObject;
import com.kindred.mir.scene.game.objects.UserObject;
import com.kindred.mir.scene.game.bean.Buff;
import java.util.ArrayList;
import java.util.List;

public class GameScene extends MirScene {

    public static class GameSceneData extends MirSceneData {

    }

    public static GameScene Scene;

    public static UserObject getUser() {
        return MapObject.User;
    }
    public static void setUser(UserObject userObject) {
        MapObject.User = userObject;
    }

    public static long MoveTime, AttackTime, NextRunTime, LogTime, LastRunTime;
    public static boolean CanMove, CanRun;

    public MapMainControl mapControl;
    public MainDialog mainDialog;
    public ChatDialog chatDialog;
    public ChatControlBar chatControl;
    public InventoryDialog inventoryDialog;
    public CharacterDialog characterDialog;
    public StorageDialog storageDialog;
    public BeltDialog beltDialog;
    public MiniMapDialog miniMapDialog;
    public InspectDialog inspectDialog;
    public OptionDialog optionDialog;
    public MenuDialog menuDialog;
    public NPCDialog npcDialog;
    public NPCGoodsDialog npcGoodsDialog;
    public NPCDropDialog npcDropDialog;
    public NPCUpgradeDialog npcUpgradeDialog;
    public HelpDialog helpDialog;
    public MountDialog mountDialog;
    public FishingDialog fishingDialog;
    public FishingStatusDialog fishingStatusDialog;
    public RefineDialog refineDialog;

    public GroupDialog groupDialog;
    public GuildDialog guildDialog;

    public BigMapDialog bigMapDialog;
    public TrustMerchantDialog trustMerchantDialog;
    public CharacterDuraPanel characterDuraPanel;
    public DuraStatusDialog duraStatusPanel;
    public TradeDialog tradeDialog;
    public GuestTradeDialog guestTradeDialog;

    //public SkillBarDialog SkillBarDialog;
    public List<SkillBarDialog> skillBarDialogs = new ArrayList<>();
    public ChatOptionDialog chatOptionDialog;
    public ChatNoticeDialog chatNoticeDialog;

    public QuestListDialog questListDialog;
    public QuestDetailDialog questDetailDialog;
    public QuestDiaryDialog questLogDialog;
    public QuestTrackingDialog questTrackingDialog;

    public RankingDialog rankingDialog;

    public MailListDialog mailListDialog;
    public MailComposeLetterDialog mailComposeLetterDialog;
    public MailComposeParcelDialog mailComposeParcelDialog;
    public MailReadLetterDialog mailReadLetterDialog;
    public MailReadParcelDialog mailReadParcelDialog;

    public IntelligentCreatureDialog intelligentCreatureDialog;
    public IntelligentCreatureOptionsDialog intelligentCreatureOptionsDialog;
    public IntelligentCreatureOptionsGradeDialog intelligentCreatureOptionsGradeDialog;

    public FriendDialog friendDialog;
    public MemoDialog memoDialog;
    public RelationshipDialog relationshipDialog;
    public MentorDialog mentorDialog;
    public GameShopDialog gameShopDialog;

    public ReportDialog reportDialog;

    //not added yet
    public KeyboardLayoutDialog keyboardLayoutDialog;

    public static List<ItemInfo> itemInfoList = new ArrayList<>();
    public static List<UserId> userIdList = new ArrayList<>();
    public static List<ChatItem> chatItemList = new ArrayList<>();
    public static List<ClientQuestInfo> questInfoList = new ArrayList<>();

    public List<Buff> buffs = new ArrayList<>();

    public static UserItem[] storage = new UserItem[80];
    public static UserItem[] guildStorage = new UserItem[112];
    public static UserItem[] refine = new UserItem[16];
    public static UserItem hoverItem;
    public static MirItemCell selectedCell;

    public static boolean pickedUpGold;
    public MirControl itemLabel, mailLabel, memoLabel, guildBuffLabel;
    public static long useItemTime, pickUpTime, dropViewTime, targetDeadTime;
    public static long gold, credit, rmb;
    public static long inspectTime;
    public boolean showReviveMessage;


    public boolean newMail;
    public int newMailCounter = 0;


    public AttackMode aMode;
    public PetMode pMode;
    public LightSetting Lights;

    public static long npcTime;
    public static long npcID;
    public static float npcRate;
    public static long defaultNPCID;


    public long toggleTime;
    public static boolean slaying, thrusting, halfMoon, crossHalfMoon, doubleSlash, twinDrakeBlade, flamingSword;
    public static long spellTime;

    public long pingTime;
    public long nextPing = 10000;

    public MirLabel[] outputLines = new MirLabel[10];
    public List<OutPutMessage> outputMessages = new ArrayList<>();

    public List<MirControlWithStaticImage> buffList = new ArrayList<>();

    public long outputDelay;

    public GameScene(MirControl parent, long window_id, long renderer_id,MirSceneData sceneData) throws Exception{
        super(parent, window_id, renderer_id,SceneEnumType.Game,sceneData);
        this.mapControl=new MapMainControl(this,renderer_id,"");
    }

    public void receiveChat(String msg, ChatType chatType) {

    }

    @Override
    public void process() {

    }
}
