package com.kindred.mir.scene.login;

import com.kindred.mir.Settings;
import com.kindred.mir.controls.*;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;

public class LoginDialog extends MirStaticImageControl {

    private MirLabel titleLabel, accountIDLabel, passLabel;
    private MirButton accountButton, closeButton, OKButton, passButton, viewKeyButton;
    private MirTextBox accountIDTextBox, passwordTextBox;
    private boolean isAccountIDValid, isPasswordValid;

    public LoginDialog(MirControl parent, MirLib lib, int index) {
        super(parent, lib, index);
        setSize(new Size(328, 220));
        setLocation(new Point((Settings.ScreenWidth - getSize().getWidth())/2, (Settings.ScreenHeight - getSize().getHeight())/2));
        setIsPixelDetect(false);

        titleLabel = new MirLabel(this);
        titleLabel.setText("title label");
        titleLabel.setSize(new Size(50, 20));
        titleLabel.setLocation(new Point((getSize().getWidth() - titleLabel.getSize().getWidth())/2, 12));

        accountIDLabel = new MirLabel(this);
        accountIDLabel.setText("account label");
        accountIDLabel.setSize(new Size(50, 20));
        accountIDLabel.setLocation(new Point(52, 83));

        passLabel = new MirLabel(this);
        passLabel.setText("password label");
        passLabel.setSize(new Size(50, 20));
        passLabel.setLocation(new Point(43, 105));

        OKButton = new MirButton(this, null, null,
                MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(63));
        OKButton.setSize(new Size(42,42));
        OKButton.setIsEnabled(false);

        OKButton.setMouseClick((control, argObj) -> {
            login();
        });

    }

    private void login()
    {
        OKButton.setIsEnabled(false);
        //Network.Enqueue(new C.Login {AccountID = AccountIDTextBox.Text, Password = PasswordTextBox.Text});
    }

    public void hide()
    {
        if (!getIsVisible()) return;
        setIsVisible(false);
    }

    public void show()
    {
        if (getIsVisible()) return;
        setIsVisible(true);
        accountIDTextBox.setFocus();

//        if (Settings.Password != string.Empty && Settings.AccountID != string.Empty)
//        {
//            login();
//        }
    }

    public void clear()
    {
        accountIDTextBox.setText("");
        passwordTextBox.setText("");
    }

    @Override
    protected void dispose(boolean disposing)
    {
        if (disposing)
        {
            titleLabel.dispose();
            titleLabel=null;
            accountIDLabel.dispose();
            accountIDLabel = null;
            passLabel.dispose();
            passLabel = null;
            accountButton.dispose();
            accountButton = null;
            closeButton.dispose();
            closeButton = null;
            OKButton.dispose();
            OKButton = null;
            passButton.dispose();
            passButton = null;
            accountIDTextBox.dispose();
            accountIDTextBox = null;
            passwordTextBox.dispose();
            passwordTextBox = null;

        }

        super.dispose(disposing);
    }
}
