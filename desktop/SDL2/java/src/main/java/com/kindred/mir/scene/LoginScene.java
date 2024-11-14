package com.kindred.mir.scene;

import com.kindred.mir.controls.*;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.libs.MirLibFactory;

public class LoginScene extends MirScene {

    private MirAnimatedControl background;
    public MirLabel Version;

    private LoginDialog login;
//    private NewAccountDialog _account;
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

        background = new MirAnimatedControl(this, MirLibFactory.ChrSel, new int[]{1,2,3});
        background.setIsAnimated(false);
        background.setAnimationCount(19);
        background.setAnimationDelay(100);

    }

    @Override
    public void process() {

    }
}
