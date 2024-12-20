package com.kindred.mir.scene.login;

import com.kindred.mir.Env;
import com.kindred.mir.Settings;
import com.kindred.mir.controls.*;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.scene.MirScene;
import com.kindred.mir.scene.charsel.CharSelScene.CharSelSceneData;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.ExecutionService.Future;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;
import com.kindred.mir.util.Util;

public class LoginScene extends MirScene {

    public static class LoginSceneData extends MirSceneData {

    }

    private MirControlWithStaticImage background;
    private MirAnimatedControl openDoorAnimation;
    public MirLabel Version;
    private MirLabel titleLabel;

    private LoginDialog loginDialog;

    private SelectServerDialog selectServerDialog;

    private NewAccountDialog account;
//    private ChangePasswordDialog _password;
//
//    private MirMessageBox _connectBox;
//
//    private InputKeyDialog _ViewKey;

    private String selectedServerName="";

    public MirLabel TestLabel, ViolenceLabel, MinorLabel, YouthLabel;

    private Future<MirScene> charSelScene;

    public LoginScene(MirControl parent, long window_id,long renderer_id,MirSceneData sceneData) throws Exception
    {
        super(parent,window_id,renderer_id,SceneEnumType.Login,sceneData);
        SoundManager.playSound(SoundList.IntroMusic, true);
        System.out.println("LoginScene-----ID: "+this.getID());
        onDisposing = (control, argObj) -> {
            SoundManager.stopSound(SoundList.IntroMusic);
        };

        MirImage backgroundImg = MirLibFactory.getMirLib(MirLibFactory.ChrSel).GetMirImage(22);
        background=new MirControlWithStaticImage(this,renderer_id,backgroundImg);
        background.setIsUseOffSet(false);
        System.out.println("background-----ID: "+background.getID()+"----Size: "+background.getSize());
        //setSize(background.getSize());
        //setLocation(backgroundImg.getOffset());

        MirImage[] animImgs = MirLibFactory.getMirLib(MirLibFactory.ChrSel).GetMirImages(Util.genSeq(23, 32));
        openDoorAnimation = new MirAnimatedControl(background,renderer_id, animImgs,false,200);
        openDoorAnimation.setIsAnimated(false);
        openDoorAnimation.setAfterAnimation((mirControl, obj) -> {
            if (this.charSelScene != null) {
                SwitchToScene(this.charSelScene.get());
            }
        });

        //title label
        titleLabel = new MirLabel(this, renderer_id, new Size(70, 20),
            new Point(0,0), Settings.FONT_SIZE20, "", Color.Yellow, Color.Empty,0);
        titleLabel.setLocation(titleLabel.Top());
        System.out.println("titleLabel-----ID: "+titleLabel.getID());
        MirImage loginDialogImg = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(60);
        loginDialog = new LoginDialog(this,window_id,renderer_id,loginDialogImg);
        loginDialog.setOnSuccessClose((control, argObj) -> {
            loginDialog.setIsVisible(false);
            openDoorAnimation.setIsAnimated(true);
            this.charSelScene= Env.BackGroundExeService.submit(() -> PrepareNextScene(window_id,renderer_id,new CharSelSceneData(selectedServerName)));
            if (this.charSelScene==null) {
                System.out.println("Can not prepare next scene!");
            }
        });
        loginDialog.setIsVisible(false);

        System.out.println("loginDialog-----ID: "+loginDialog.getID());
        MirImage selectServerDialogImg = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(256);
        String[] servers={"逐鹿中原","九天烈焰"};
        selectServerDialog = new SelectServerDialog(this,renderer_id,selectServerDialogImg,servers);
        selectServerDialog.setOnSuccessClose((control, argObj) -> {
            this.selectedServerName=(String)argObj;
            titleLabel.setText(this.selectedServerName);
            loginDialog.setIsVisible(true);
        });
        System.out.println("selectServerDialog-----ID: "+selectServerDialog.getID());

//        login.accountButton.click += (o, e) =>
//        {
//            _login.Hide();
//            if(_ViewKey != null && !_ViewKey.IsDisposed) _ViewKey.Dispose();
//            _account = new NewAccountDialog { Parent = _background };
//            _account.Disposing += (o1, e1) => _login.Show();
//        };
//        login.PassButton.Click += (o, e) =>
//        {
//            _login.Hide();
//            if (_ViewKey != null && !_ViewKey.IsDisposed) _ViewKey.Dispose();
//            _password = new ChangePasswordDialog { Parent = _background };
//            _password.Disposing += (o1, e1) => _login.Show();
//        };
//
//        login.ViewKeyButton.Click += (o, e) =>     //ADD
//        {
//            if (_ViewKey != null && !_ViewKey.IsDisposed) return;
//
//            _ViewKey = new InputKeyDialog(_login) { Parent = _background };
//        };
    }

    @Override
    public void process() {

    }
}
