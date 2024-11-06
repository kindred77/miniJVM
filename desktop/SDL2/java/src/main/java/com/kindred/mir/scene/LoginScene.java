package com.kindred.mir.scene;

import com.kindred.mir.controls.*;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;

public class LoginScene extends MirScene {

    private MirAnimatedControl _background;
    public MirLabel Version;

    private LoginDialog _login;
//    private NewAccountDialog _account;
//    private ChangePasswordDialog _password;
//
//    private MirMessageBox _connectBox;
//
//    private InputKeyDialog _ViewKey;

    public MirImageControl TestLabel, ViolenceLabel, MinorLabel, YouthLabel;

    public LoginScene() {
        SoundManager.playSound(SoundList.IntroMusic, true);
        disposing = new ControlCommonListener() {
            @Override
            public void doAction(MirControl control, Object argObj) {
                SoundManager.stopSound(SoundList.IntroMusic);
            }
        };
    }

    @Override
    public void process() {

    }
}
