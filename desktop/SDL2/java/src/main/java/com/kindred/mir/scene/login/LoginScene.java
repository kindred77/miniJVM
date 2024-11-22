package com.kindred.mir.scene.login;

import com.kindred.mir.Settings;
import com.kindred.mir.controls.*;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Util;

public class LoginScene extends MirScene {

    private MirAnimatedControl background;
    public MirLabel Version;

    private LoginDialog loginDialog;

    private NewAccountDialog account;
//    private ChangePasswordDialog _password;
//
//    private MirMessageBox _connectBox;
//
//    private InputKeyDialog _ViewKey;

    public MirLabel TestLabel, ViolenceLabel, MinorLabel, YouthLabel;

    public LoginScene(MirControl parent, long renderer_id) throws Exception
    {
        super(parent);
        SoundManager.playSound(SoundList.IntroMusic, true);

        onDisposing = (control, argObj) -> {
            SoundManager.stopSound(SoundList.IntroMusic);
        };

        MirImage[] animImgs = MirLibFactory.getMirLib(MirLibFactory.ChrSel).GetMirImages(Util.genSeq(22, 32));
        background = new MirAnimatedControl(this,renderer_id, animImgs);
        background.setIsAnimated(false);
        background.setAnimationCount(19);
        background.setAnimationDelay(100);

        MirImage loginDialogImg = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(60);
        //Point loginDialogPos = new Point((Settings.ScreenWidth - loginDialogImg.getWidth())/2, (Settings.ScreenHeight - loginDialogImg.getHeight())/2);
        loginDialog = new LoginDialog(background,renderer_id,loginDialogImg);
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
