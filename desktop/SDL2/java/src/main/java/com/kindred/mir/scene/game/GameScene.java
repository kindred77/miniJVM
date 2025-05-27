package com.kindred.mir.scene.game;

import com.kindred.mir.Env;
import com.kindred.mir.GameCommon.AttackMode;
import com.kindred.mir.GameCommon.ChatItem;
import com.kindred.mir.GameCommon.ChatType;
import com.kindred.mir.GameCommon.ClientQuestInfo;
import com.kindred.mir.GameCommon.ItemInfo;
import com.kindred.mir.GameCommon.LightSetting;
import com.kindred.mir.GameCommon.MirClass;
import com.kindred.mir.GameCommon.PetMode;
import com.kindred.mir.GameCommon.UserId;
import com.kindred.mir.GameCommon.UserItem;
import com.kindred.mir.MirMain;
import com.kindred.mir.Settings;
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
import com.kindred.mir.scene.game.map.MapMainControl;
import com.kindred.mir.scene.game.objects.MapObject;
import com.kindred.mir.scene.game.objects.UserObject;
import com.kindred.mir.scene.game.bean.Buff;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.MirUtil;
import com.kindred.mir.util.Point;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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

    protected long moveTime, AttackTime, NextRunTime, LogTime, LastRunTime;
    protected boolean canMove, CanRun;


    protected MapMainControl mapControl;
    protected MainDialog mainDialog;
    protected ChatDialog chatDialog;
    protected ChatControlBar chatControl;
    protected InventoryDialog inventoryDialog;
    protected CharacterDialog characterDialog;
    protected StorageDialog storageDialog;
    protected BeltDialog beltDialog;
    protected MiniMapDialog miniMapDialog;
    protected InspectDialog inspectDialog;
    protected OptionDialog optionDialog;
    protected MenuDialog menuDialog;

    protected NPCDialog npcDialog;
    protected NPCGoodsDialog npcGoodsDialog;
    protected NPCDropDialog npcDropDialog;
    protected NPCUpgradeDialog npcUpgradeDialog;
    protected HelpDialog helpDialog;
    protected MountDialog mountDialog;
    protected FishingDialog fishingDialog;
    protected FishingStatusDialog fishingStatusDialog;
    protected RefineDialog refineDialog;

    protected GroupDialog groupDialog;
    protected GuildDialog guildDialog;

    protected BigMapDialog bigMapDialog;
    protected TrustMerchantDialog trustMerchantDialog;
    protected CharacterDuraPanel characterDuraPanel;
    protected DuraStatusDialog duraStatusPanel;
    protected TradeDialog tradeDialog;
    protected GuestTradeDialog guestTradeDialog;

    //public SkillBarDialog SkillBarDialog;
    protected List<SkillBarDialog> skillBarDialogs = new ArrayList<>();
    protected ChatOptionDialog chatOptionDialog;
    protected ChatNoticeDialog chatNoticeDialog;

    protected QuestListDialog questListDialog;
    protected QuestDetailDialog questDetailDialog;
    protected QuestDiaryDialog questLogDialog;
    protected QuestTrackingDialog questTrackingDialog;

    protected RankingDialog rankingDialog;

    protected MailListDialog mailListDialog;
    protected MailComposeLetterDialog mailComposeLetterDialog;
    protected MailComposeParcelDialog mailComposeParcelDialog;
    protected MailReadLetterDialog mailReadLetterDialog;
    protected MailReadParcelDialog mailReadParcelDialog;

    protected IntelligentCreatureDialog intelligentCreatureDialog;
    protected IntelligentCreatureOptionsDialog intelligentCreatureOptionsDialog;
    protected IntelligentCreatureOptionsGradeDialog intelligentCreatureOptionsGradeDialog;

    protected FriendDialog friendDialog;
    protected MemoDialog memoDialog;
    protected RelationshipDialog relationshipDialog;
    protected MentorDialog mentorDialog;
    protected GameShopDialog gameShopDialog;

    protected ReportDialog reportDialog;

    //not added yet
    protected KeyboardLayoutDialog keyboardLayoutDialog;

    protected List<ItemInfo> itemInfoList = new ArrayList<>();
    protected List<UserId> userIdList = new ArrayList<>();
    protected List<ChatItem> chatItemList = new ArrayList<>();
    protected List<ClientQuestInfo> questInfoList = new ArrayList<>();

    protected List<Buff> buffs = new ArrayList<>();

    protected UserItem[] storage = new UserItem[80];
    protected UserItem[] guildStorage = new UserItem[112];
    protected UserItem[] refine = new UserItem[16];
    protected UserItem hoverItem;
    protected MirItemCell selectedCell;

    protected boolean pickedUpGold;
    protected MirControl itemLabel, mailLabel, memoLabel, guildBuffLabel;
    protected long useItemTime, pickUpTime, dropViewTime, targetDeadTime;
    protected long gold, credit, rmb;
    protected long inspectTime;
    protected boolean showReviveMessage;


    protected boolean newMail;
    protected int newMailCounter = 0;


    protected AttackMode aMode;
    protected PetMode pMode;
    protected LightSetting Lights;

    protected long npcTime;

    protected long npcID;
    protected float npcRate;
    protected long defaultNPCID;


    protected long toggleTime;
    protected boolean slaying, thrusting, halfMoon, crossHalfMoon, doubleSlash, twinDrakeBlade, flamingSword;
    protected long spellTime;

    protected long pingTime;
    protected long nextPing = 10000;

    protected MirLabel[] outputLines = new MirLabel[10];
    protected List<OutPutMessage> outputMessages = new ArrayList<>();

    protected List<MirControlWithStaticImage> buffList = new ArrayList<>();

    protected long outputDelay;

    public GameScene(MirControl parent, long window_id, long renderer_id,MirSceneData sceneData) throws Exception{
        super(parent, window_id, renderer_id,SceneEnumType.Game,sceneData);
        this.mapControl=new MapMainControl(this,renderer_id,"../mir_client/map/0");
        this.mapControl.updateSurface();
    }

    public void receiveChat(String msg, ChatType chatType) {

    }

    public void disposeItemLabel() {
        if (itemLabel != null && !itemLabel.getIsDisposed()) {
            itemLabel.dispose();
        }
        itemLabel = null;
    }

    public MapMainControl GetMapControl() {
        return mapControl;
    }

    public void SetNpcID(long npcID) {
        this.npcID = npcID;
    }

    public long GetNpcID() {
        return npcID;
    }

    public NPCDialog GetNPCDialog() {
        return npcDialog;
    }

    public void createItemLabel(UserItem item, boolean isInspect) {
//        if (item == null) {
//            disposeItemLabel();
//            hoverItem = null;
//            return;
//        }
//        if (item == hoverItem && itemLabel != null && !itemLabel.getIsDisposed()) return;
//        int level = isInspect ? inspectDialog.getLevel() : MapObject.User.Level;
//        MirClass job = isInspect ? inspectDialog.getMirClass() : MapObject.User.Class;
//        hoverItem = item;
//        ItemInfo realItem = Functions.GetRealItem(item.Info, level, job, ItemInfoList);
//        itemLabel = MirControl.builder()
//        {
//            BackColour = Color.FromArgb(255, 50, 50, 50),
//                Border = true,
//                BorderColour = Color.Gray,
//                DrawControlTexture = true,
//                NotControl = true,
//                Parent = this,
//                Opacity = 0.7F,
//            //  Visible = false
//        };
//        //Name Info Label
//        MirControl[] outlines = new MirControl[9];
//        outlines[0] = NameInfoLabel(item, isInspect);
//        //Attribute Info1 Label - Attack Info
//        outlines[1] = AttackInfoLabel(item, isInspect);
//        //Attribute Info2 Label - Defence Info
//        outlines[2] = DefenceInfoLabel(item, isInspect);
//        //Attribute Info3 Label - Weight Info
//        outlines[3] = WeightInfoLabel(item, isInspect);
//        //Awake Info Label
//        outlines[4] = UpgradeInfoLabel(item, isInspect);
//        //need Info Label
//        outlines[5] = NeedInfoLabel(item, isInspect);
//        //Bind Info Label
//        outlines[6] = BindInfoLabel(item, isInspect);
//        //Overlap Info Label
//        outlines[7] = OverlapInfoLabel(item, isInspect);
//        //Story Label
//        outlines[8] = StoryInfoLabel(item, isInspect);
//        for(MirControl outline : outlines)
//        {
//            if (outline != null)
//            {
//                outline.setSize(itemLabel.getSize());
//            }
//        }

        //ItemLabel.Visible = true;
    }

    public void updateBuffs() {
//        for (int i = 0; i < buffList.size(); i++)
//        {
//            MirControlWithStaticImage image = buffList.get(i);
//            Buff buff = buffs.get(i);
//
//            int buffImage = MirUtil.BuffImage(buff.getType());
//            MLibrary buffLibrary = Libraries.BuffIcon;
//
//            //ArcherSpells - VampireShot,PoisonShot
//            if (buffImage >= 20000)
//            {
//                buffImage -= 20000;
//                buffLibrary = Libraries.MagIcon;
//            }
//
//            if (buffImage >= 10000)
//            {
//                buffImage -= 10000;
//                buffLibrary = Libraries.Prguse2;
//            }
//
//            image.setLocation(new Point((Settings.ScreenWidth - 150) - i * 23 + ((10 * 23) * (i / 10)), 2 + ((i / 10) * 25)));
//            image.Hint = buff.ToString();
//            image.Index = buffImage;
//            image.Library = buffLibrary;
//
//            if (!buff.Infinite && Math.Round((buff.Expire - Env.Time) / 1000D) <= 5)
//            {
//                double time = (buff.Expire - Env.Time) / 100D;
//
//                if (Math.round(time) % 10 < 5) image.Index = -1;
//            }
//
//            //((MirLabel)image.Controls[0]).Text = buff.Infinite ? "" : timeRemaining.ToString();
//        }
    }

    public void dialogProcess() {
//        if(Settings.SkillBar) {
//            for(SkillBarDialog bar : Scene.skillBarDialogs) {
//                bar.setIsVisible(true);
//            }
//        } else {
//            for(SkillBarDialog bar : Scene.skillBarDialogs) {
//                bar.setIsVisible(false);
//            }
//        }
//
//        for (int i = 0; i < Scene.skillBarDialogs.size(); i++) {
//            if (i * 2 > Settings.SkillbarLocation.Length) break;
//            if ((Settings.SkillbarLocation[i, 0] > Settings.Resolution - 100) || (Settings.SkillbarLocation[i, 1] > 700)) continue;//in theory you'd want the y coord to be validated based on resolution, but since client only allows for wider screens and not higher :(
//            Scene.skillBarDialogs.get(i).setLocation(new Point(Settings.SkillbarLocation[i, 0], Settings.SkillbarLocation[i, 1]));
//        }
//
//        if (Settings.DuraView) {
//            characterDuraPanel.setIsVisible(true);
//        } else {
//            characterDuraPanel.setIsVisible(false);
//        }
    }

    private void processOuput() {
        for (int i = 0; i < outputMessages.size(); i++) {
            if (Env.Time >= outputMessages.get(i).getExpireTime()) {
                outputMessages.remove(i);
            }
        }

        for (int i = 0; i < outputLines.length; i++) {
            if (outputMessages.size() > i) {
                Color color;
                switch (outputMessages.get(i).getType()) {
                    case Quest:
                        color = Color.Gold;
                        break;
                    default:
                        color = Color.LimeGreen;
                        break;
                }

                outputLines[i].setText(outputMessages.get(i).getMessage());
                outputLines[i].setForeColor(color);
                outputLines[i].setIsVisible(true);
            } else {
                outputLines[i].setText("");
                outputLines[i].setIsVisible(false);
            }
        }
    }

    @Override
    public void process() {
        if (mapControl == null || getUser() == null) {
            return;
        }

        if (Env.Time >= moveTime) {
            moveTime += 100; //Move Speed
            canMove = true;
            mapControl.increAnimationCount();
            //mapControl.TextureValid = false;
        } else {
            canMove = false;
        }

        if (Env.Time >= nextPing) {
            nextPing = Env.Time + 60000;
            //Network.Enqueue(new C.KeepAlive() { Time = CMain.Time });
        }

        if(MouseControl instanceof MirItemCell) {
            MirItemCell cell = (MirItemCell)MouseControl;
            if(hoverItem != cell.getItem()){
                disposeItemLabel();
                hoverItem = null;
                createItemLabel(cell.getItem(),false);
            }
        }

        if (itemLabel != null && !itemLabel.getIsDisposed()) {
            itemLabel.bringToFront();

//            int x = CMain.MPoint.X + 15, y = CMain.MPoint.Y;
//            if (x + itemLabel.getSize().getWidth() > Settings.ScreenWidth)
//                x = Settings.ScreenWidth - itemLabel.getSize().getWidth();
//
//            if (y + itemLabel.getSize().getHeight() > Settings.ScreenHeight)
//                y = Settings.ScreenHeight - itemLabel.getSize().getHeight();
//            itemLabel.setLocation(new Point(x, y));
        }

        if (mailLabel != null && !mailLabel.getIsDisposed()) {
            mailLabel.bringToFront();

//            int x = CMain.MPoint.X + 15, y = CMain.MPoint.Y;
//            if (x + mailLabel.getSize().getWidth() > Settings.ScreenWidth)
//                x = Settings.ScreenWidth - mailLabel.getSize().getWidth();
//
//            if (y + mailLabel.getSize().getHeight() > Settings.ScreenHeight)
//                y = Settings.ScreenHeight - mailLabel.getSize().getHeight();
//            mailLabel.setLocation(new Point(x, y));
        }

        if (memoLabel != null && !memoLabel.getIsDisposed()) {
//            memoLabel.bringToFront();
//            int x = CMain.MPoint.X + 15, y = CMain.MPoint.Y;
//            if (x + memoLabel.getSize().getWidth() > Settings.ScreenWidth)
//                x = Settings.ScreenWidth - memoLabel.getSize().getWidth();
//
//            if (y + memoLabel.getSize().getHeight() > Settings.ScreenHeight)
//                y = Settings.ScreenHeight - memoLabel.getSize().getHeight();
//            memoLabel.setLocation(new Point(x, y));
        }

        if (guildBuffLabel != null && !guildBuffLabel.getIsDisposed()) {
//            guildBuffLabel.bringToFront();
//            int x = CMain.MPoint.X + 15, y = CMain.MPoint.Y;
//            if (x + guildBuffLabel.getSize().getWidth() > Settings.ScreenWidth)
//                x = Settings.ScreenWidth - guildBuffLabel.getSize().getWidth();
//
//            if (y + guildBuffLabel.getSize().getHeight() > Settings.ScreenHeight)
//                y = Settings.ScreenHeight - guildBuffLabel.getSize().getHeight();
//            guildBuffLabel.setLocation(new Point(x, y));
        }

        if (!getUser().isDead) showReviveMessage = false;

//        if (showReviveMessage && Env.Time > getUser().DeadTime && getUser().CurrentAction == MirAction.Dead) {
//            showReviveMessage = false;
//            MirMessageBox messageBox = new MirMessageBox("你挂了,是否需要回城镇复活?", MirMessageBoxButtons.YesNo, false);
//            messageBox.YesButton.Click += (o, e) =>
//            {
//                if (getUser().isDead) Network.Enqueue(new C.TownRevive());
//            };
//            messageBox.AfterDraw += (o, e) =>
//            {
//                if (!getUser().isDead) messageBox.Dispose();
//            };
//            messageBox.Show();
//        }

        updateBuffs();
        //mapControl.process();
        //mainDialog.process();
        //inventoryDialog.process();
        //gameShopDialog.process();
        //miniMapDialog.process();
//        for(SkillBarDialog bar : Scene.skillBarDialogs) {
//            bar.process();
//        }
//
//        dialogProcess();
//
//        processOuput();
    }
}
