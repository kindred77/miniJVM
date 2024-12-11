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

    private MirButton accountButton, closeButton, OKButton, passButton, viewKeyButton;
    private ImGuiLayout imGuiLayout;
    private ImGuiWindow imGuiWindow;
    private ImGuiTextBox accountIDTextBox, passwordTextBox;
    private boolean isAccountIDValid, isPasswordValid;

    public LoginDialog(MirControl parent, long window_id, long renderer_id, MirImage img) throws Exception
    {
        super(parent, renderer_id, img);

        imGuiLayout = new ImGuiLayout(this, window_id, renderer_id);
        System.out.println("imGuiLayout ID: "+imGuiLayout.getID());
        imGuiWindow=new ImGuiWindow(imGuiLayout, window_id, renderer_id, "##login_dialog",new Point(93,80),new Size(150, 60),
            false,false,false,true,false);
        imGuiWindow.setIsBorder(true);
        imGuiWindow.setBorderColor(Color.Green);
        System.out.println("imGuiWindow ID: "+imGuiWindow.getID());
        accountIDTextBox = new ImGuiTextBox(imGuiWindow, window_id, renderer_id,"##log_account",
            new Point(5,5),140,64,
            Settings.FONT_SIZE15, Color.Black,Color.White,false, 1);
        System.out.println("accountIDTextBox ID: "+accountIDTextBox.getID());
        passwordTextBox = new ImGuiTextBox(imGuiWindow, window_id, renderer_id,"##log_password",
            new Point(5,36),140,64,
            Settings.FONT_SIZE15, Color.Black,Color.White,true, 1);
        System.out.println("passwordTextBox ID: "+passwordTextBox.getID());

        MirImage okBtnPressedImg = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(62);
        OKButton = new MirButton(this, renderer_id, null, null, okBtnPressedImg,
            new Size(76,33), new Point(170,163));
        OKButton.setIsBorder(true);

        OKButton.setOnMouseLeftClick((control, argObj) -> {
            login();
        });
        System.out.println("OKButton ID: "+OKButton.getID());

        this.onShown=(control, argObj) -> {
            if (accountIDTextBox!=null) {
                //accountIDTextBox.setFocus();
            }
        };
    }

    private void login()
    {
        System.out.println("----login----");
        //OKButton.setIsEnabled(false);
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
