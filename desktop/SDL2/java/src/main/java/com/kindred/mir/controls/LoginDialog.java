package com.kindred.mir.controls;

import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;

public class LoginDialog extends MirStaticImageControl {

    private MirStaticImageControl titleLabel, accountIDLabel, passLabel;
    private MirButton accountButton, closeButton, OKButton, passButton, viewKeyButton;
    private MirTextBox AccountIDTextBox, PasswordTextBox;
    private boolean isAccountIDValid, isPasswordValid;

    public LoginDialog(MirControl parent, MirImage image) {
        super(parent, image);
    }

    public LoginDialog(MirControl parent, MirLib lib, int index) throws Exception {
        super(parent, lib, index);
    }
}
