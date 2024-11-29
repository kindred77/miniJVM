package com.kindred.mir.scene.login;

import com.kindred.mir.Settings;
import com.kindred.mir.controls.*;
import com.kindred.mir.controls.imgui.ImGuiLayout;
import com.kindred.mir.controls.imgui.ImGuiTextBox;
import com.kindred.mir.controls.imgui.ImGuiWindow;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;

public class LoginDialog extends MirControlWithStaticImage {

    private MirLabel titleLabel, accountIDLabel, passLabel;
    private MirButton accountButton, closeButton, OKButton, passButton, viewKeyButton;
    private ImGuiLayout imGuiLayout;
    private ImGuiWindow imGuiWindow;
    private ImGuiTextBox accountIDTextBox, passwordTextBox;
    private boolean isAccountIDValid, isPasswordValid;

    public LoginDialog(MirControl parent, long window_id, long renderer_id, MirImage img) throws Exception
    {
        super(parent, renderer_id, img);

        setIsPixelDetect(false);

        //title label
        titleLabel = new MirLabel("title label",this, renderer_id, new Size(50, 20));
        titleLabel.setLocation(titleLabel.Top());
        titleLabel.setIsBorder(true);
        titleLabel.setBorderColor(Color.Green);

        accountIDLabel = new MirLabel("account label", this, renderer_id, new Size(50, 20));
        accountIDLabel.setLocation(new Point(52, 83));

        passLabel = new MirLabel("password label",this, renderer_id, new Size(50, 20));
        passLabel.setLocation(new Point(43, 105));


        imGuiLayout = new ImGuiLayout(this, window_id, renderer_id);
        imGuiWindow=new ImGuiWindow(imGuiLayout, window_id, renderer_id, "##login_dialog",new Point(0,0),new Size(210, 70),
            false,false,false,true,false);
        accountIDTextBox = new ImGuiTextBox(imGuiWindow, window_id, renderer_id,"##log_account",
            new Point(5,5),200,64,
            Settings.FONT_SIZE15, new Color(0, 0, 0, 255),
            new Color(255, 0, 0, 255),false);

        passwordTextBox = new ImGuiTextBox(imGuiWindow, window_id, renderer_id,"##log_password",
            new Point(5,40),200,64,
            Settings.FONT_SIZE15, new Color(0, 0, 0, 255),
            new Color(255, 0, 0, 255),true);


        MirImage okBtnPressedImg = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(63);
        OKButton = new MirButton(this, renderer_id, null, null, okBtnPressedImg);
        OKButton.setSize(new Size(42,42));
        OKButton.setIsBorder(true);

        OKButton.setOnMouseLeftClick((control, argObj) -> {
            login();
        });

        this.onShown=(control, argObj) -> {
            if (accountIDTextBox!=null) {
                //accountIDTextBox.setFocus();
            }
        };
    }

    private void login()
    {
        OKButton.setIsEnabled(false);
        //Network.Enqueue(new C.Login {AccountID = AccountIDTextBox.Text, Password = PasswordTextBox.Text});
    }

    public void hide()
    {
        if (!getIsVisible()) {
            return;
        }
        setIsVisible(false);
    }

    public void clear()
    {
        //accountIDTextBox.setText("");
        //passwordTextBox.setText("");
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
