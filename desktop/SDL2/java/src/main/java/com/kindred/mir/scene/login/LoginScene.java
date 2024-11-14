package com.kindred.mir.scene.login;

import com.kindred.mir.controls.*;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.scene.login.LoginDialog;
import com.kindred.mir.util.Util;

public class LoginScene extends MirScene {

    private MirAnimatedControl background;
    public MirLabel Version;

    private LoginDialog login;

    private NewAccountDialog account;
//    private ChangePasswordDialog _password;
//
//    private MirMessageBox _connectBox;
//
//    private InputKeyDialog _ViewKey;

    public MirStaticImageControl TestLabel, ViolenceLabel, MinorLabel, YouthLabel;

    public LoginScene(MirControl parent) {
        super(parent);
        SoundManager.playSound(SoundList.IntroMusic, true);
        disposing = new ControlCommonListener() {
            @Override
            public void doAction(MirControl control, Object argObj) {
                SoundManager.stopSound(SoundList.IntroMusic);
            }
        };

        background = new MirAnimatedControl(this, MirLibFactory.getMirLib(MirLibFactory.ChrSel), Util.genSeq(22, 32));
        background.setIsAnimated(false);
        background.setAnimationCount(19);
        background.setAnimationDelay(100);

        login = new LoginDialog(background,MirLibFactory.getMirLib(MirLibFactory.Prguse), 1084);
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
