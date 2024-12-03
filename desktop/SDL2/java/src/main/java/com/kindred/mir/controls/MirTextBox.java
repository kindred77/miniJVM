package com.kindred.mir.controls;

import com.kindred.mir.engine.Font;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Util;

public class MirTextBox extends MirControlWithTexture {

    private boolean canLoseFocus;
    //private TextBox textBox;
    private byte[] textBuf=new byte[128];
    private long drawData_ptr=0L;

    public MirTextBox(MirControl parent, long renderer_id)
    {
        super(parent, renderer_id);
        backColor = Color.Black;

        MirJNI.ImGui_InitFont(Util.toCstyleBytes("NotoEmoji+NotoSansCJKSC-Regular.ttf"), 10f);
        MirJNI.ImGui_InitBackColor(1.0f, 1.0f, 1.0f, 0.5f);
        MirJNI.ImGui_InitForeColor(1.0f, 0.0f, 0.0f, 0.5f);

//        TextBox = new TextBox
//        {
//            BackColor = BackColour,
//                    BorderStyle = BorderStyle.None,
//                    Font = new Font(Settings.FontName, 10F),
//                    ForeColor = ForeColour,
//                    Location = DisplayLocation,
//                    Size = Size,
//                    Visible = Visible,
//                    Tag = this,
//        };

//        TextBox.VisibleChanged += TextBox_VisibleChanged;
//        TextBox.ParentChanged += TextBox_VisibleChanged;
//        TextBox.KeyUp += TextBoxOnKeyUp;
//        TextBox.KeyPress += TextBox_KeyPress;
//
//        Shown += MirTextBox_Shown;
//        TextBox.MouseMove += CMain.CMain_MouseMove;
    }

    public int getMaxLength()
    {
//        if (textBox != null && !textBox.IsDisposed)
//            return textBox.MaxLength;
        return -1;
    }

    public void setMaxLength(int maxLength)
    {
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.MaxLength = value;
    }

    public boolean getIsPassword()
    {
//        if (TextBox != null && !TextBox.IsDisposed)
//            return TextBox.UseSystemPasswordChar;
        return false;
    }

    public void setIsPassword(boolean isPassword)
    {
//        if (TextBox != null && !TextBox.IsDisposed)
//            TextBox.UseSystemPasswordChar = value;
    }

    public Font getFont()
    {
//        if (TextBox != null && !TextBox.IsDisposed)
//            return TextBox.Font;
        return null;
    }

    public void setFont(Font font)
    {
//        if (TextBox != null && !TextBox.IsDisposed)
//            TextBox.Font = value;
    }

    public String getText()
    {
//        if (textBox != null && !textBox.IsDisposed)
//            return textBox.Text;
        return null;
    }
    public void setText(String text)
    {
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.Text = text;
    }

    public String[] getMultiText()
    {
//        if (textBox != null && !textBox.IsDisposed)
//            return textBox.Lines;
        return null;
    }

    public void setMultiText(String[] texts)
    {
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.Lines = texts;
    }

    public void setFocus()
    {

    }

//    private void textBox_VisibleChanged(object sender, EventArgs e)
//    {
//        dialogChanged();
//
//        if (TextBox.Visible && TextBox.CanFocus)
//            if (Program.Form.ActiveControl == null || Program.Form.ActiveControl == Program.Form)
//                Program.Form.ActiveControl = TextBox;
//
//        if (!TextBox.Visible)
//            if (Program.Form.ActiveControl == TextBox)
//                Program.Form.Focus();
//    }

//    private void SetFocus(object sender, EventArgs e)
//    {
//        if (TextBox.Visible)
//            TextBox.VisibleChanged -= SetFocus;
//        if (TextBox.Parent != null)
//            TextBox.ParentChanged -= SetFocus;
//
//        if (TextBox.CanFocus) TextBox.Focus();
//        else if (TextBox.Visible && TextBox.Parent != null)
//            Program.Form.ActiveControl = TextBox;
//
//
//    }

    @Override
    protected boolean updateTexture(long surface_id)
    {
        MirJNI.ImGui_NewFrame();

        if (!MirJNI.ImGui_Begin(Util.toCstyleBytes("login"), 100,350, 200, 25,
            true,true,true,true,true,
            true,true, true, true, true, true, true))
        {
            MirJNI.ImGui_End();
            return false;
        }
        else
        {
            //MirJNI.ImGui_Text(toCstyleBytes("标签"));

            //动态修改一些属性
//            long cur_ts = MirJNI.SDL_GetTicks();
//            if ((cur_ts - prev_ts) > 5000)
//            {
//                color_mod++;
//                //color_mod = color_mod % 3;
//                prev_ts = cur_ts;
//                MirJNI.ImGui_SetWindowFontScale(color_mod);
//                MirJNI.ImGui_InitBackColor(color_mod%3 == 1 ? 1.0f:0f, color_mod%3 == 2 ? 1.0f:0f, color_mod%3 == 0 ? 1.0f:0f, 0.5f);
//                MirJNI.ImGui_InitForeColor(color_mod%3 == 0 ? 1.0f:0f, color_mod%3 == 1 ? 1.0f:0f, color_mod%3 == 2 ? 1.0f:0f, 0.5f);
//            }

            //if(MirJNI.ImGui_InputTextMultiline(toCstyleBytes("##"),buf, 200.0f, 25))
            if(MirJNI.ImGui_InputText(0, 0, 198, Util.toCstyleBytes("##"), Util.toCstyleBytes("请输入内容..."), textBuf, false))
            {
                System.out.println("----------------------enter return--------------------");
            }

            //MirJNI.ImGui_End();
        }

        drawData_ptr = MirJNI.ImGui_RenderAndGetDrawData();
        return true;
    }

    @Override
    protected boolean drawControl() {

        if (drawData_ptr!=0) {
            MirJNI.ImGui_Render(getRenderer(), drawData_ptr);
        }

        return true;
    }
}
