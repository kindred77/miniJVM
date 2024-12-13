package com.kindred.mir.scene.login;

import com.kindred.mir.MirMain;
import com.kindred.mir.Settings;
import com.kindred.mir.controls.*;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;
import com.kindred.mir.util.Util;

public class LoginScene extends MirScene {

    private MirAnimatedControl background;
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

    public LoginScene(MirControl parent, long window_id,long renderer_id) throws Exception
    {
        super(parent,window_id,renderer_id);
        SoundManager.playSound(SoundList.IntroMusic, true);
        System.out.println("LoginScene-----ID: "+this.getID());
        onDisposing = (control, argObj) -> {
            SoundManager.stopSound(SoundList.IntroMusic);
        };

        MirImage[] animImgs = MirLibFactory.getMirLib(MirLibFactory.ChrSel).GetMirImages(Util.genSeq(22, 32));
        background = new MirAnimatedControl(this,renderer_id, animImgs);
        background.setIsAnimated(false);
        background.setAnimationCount(19);
        background.setAnimationDelay(100);
        System.out.println("background-----ID: "+background.getID());
        setSize(background.getSize());

        //title label
        titleLabel = new MirLabel(this, renderer_id, new Size(70, 20),
            new Point(0,0), Settings.FONT_SIZE20, "", Color.Yellow, Color.Empty,0);
        titleLabel.setLocation(titleLabel.Top());
        System.out.println("titleLabel-----ID: "+titleLabel.getID());
        MirImage loginDialogImg = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(60);
        loginDialog = new LoginDialog(background,window_id,renderer_id,loginDialogImg);
        loginDialog.setOnSuccessClose((control, argObj) -> {
            loginDialog.setIsVisible(false);
            background.setIsAnimated(true);
        });
        loginDialog.setIsVisible(false);

        System.out.println("loginDialog-----ID: "+loginDialog.getID());
        MirImage selectServerDialogImg = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(256);
        String[] servers={"逐鹿中原","九天烈焰"};
        selectServerDialog = new SelectServerDialog(background,renderer_id,selectServerDialogImg,servers);
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
