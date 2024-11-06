package com.kindred.mir.controls;

import com.kindred.mir.engine.Font;
import com.kindred.mir.util.Color;

public class MirTextBox extends MirControl {

    private boolean canLoseFocus;
//    private TextBox textBox;
//
//    public MirTextBox()
//    {
//        backColor = Color.Black;
//
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
//
//        TextBox.VisibleChanged += TextBox_VisibleChanged;
//        TextBox.ParentChanged += TextBox_VisibleChanged;
//        TextBox.KeyUp += TextBoxOnKeyUp;
//        TextBox.KeyPress += TextBox_KeyPress;
//
//        Shown += MirTextBox_Shown;
//        TextBox.MouseMove += CMain.CMain_MouseMove;
//    }
//
//    @Override
//    protected void onBackColorChanged()
//    {
//        super.onBackColorChanged();
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.BackColor = backColour;
//    }
//
//    @Override
//    protected void onEnabledChanged()
//    {
//        super.onEnabledChanged();
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.Enabled = isEnabled;
//    }
//
//    @Override
//    protected void onForeColorChanged()
//    {
//        super.onForeColorChanged();
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.ForeColor = foreColor;
//    }
//
//    @Override
//    protected void onLocationChanged()
//    {
//        super.onLocationChanged();
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.Location = DisplayLocation;
//    }
//
//    public int getMaxLength()
//    {
//        if (textBox != null && !textBox.IsDisposed)
//            return textBox.MaxLength;
//        return -1;
//    }
//
//    public void setMaxLength(int maxLength)
//    {
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.MaxLength = value;
//    }
//
//    @Override
//    protected void onParentChanged()
//    {
//        super.onParentChanged();
//        if (textBox != null && !textBox.IsDisposed)
//            onVisibleChanged();
//    }
//
//    public boolean getIsPassword()
//    {
//        if (TextBox != null && !TextBox.IsDisposed)
//            return TextBox.UseSystemPasswordChar;
//        return false;
//    }
//
//    public void setIsPassword(boolean isPassword)
//    {
//        if (TextBox != null && !TextBox.IsDisposed)
//            TextBox.UseSystemPasswordChar = value;
//    }
//
//    public Font getFont()
//    {
//        if (TextBox != null && !TextBox.IsDisposed)
//            return TextBox.Font;
//        return null;
//    }
//
//    public void setFont(Font font)
//    {
//        if (TextBox != null && !TextBox.IsDisposed)
//            TextBox.Font = value;
//    }
//
//    @Override
//    protected void onSizeChanged()
//    {
//        textBox.Size = getSize();
//        size = textBox.Size;
//
//        if (textBox != null && !textBox.IsDisposed)
//            super.onSizeChanged();
//    }
//
//    public String getText()
//    {
//        if (textBox != null && !textBox.IsDisposed)
//            return textBox.Text;
//        return null;
//    }
//    public void setText(String text)
//    {
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.Text = text;
//    }
//
//    public String[] getMultiText()
//    {
//        if (textBox != null && !textBox.IsDisposed)
//            return textBox.Lines;
//        return null;
//    }
//
//    public void setMultiText(String[] texts)
//    {
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.Lines = texts;
//    }
//
//    @Override
//    protected void onVisibleChanged()
//    {
//        super.onVisibleChanged();
//
//        if (textBox != null && !textBox.IsDisposed)
//            textBox.Visible = Visible;
//    }
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

}
