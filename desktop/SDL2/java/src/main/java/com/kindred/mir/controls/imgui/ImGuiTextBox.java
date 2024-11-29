package com.kindred.mir.controls.imgui;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.Font;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.test.Test;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;
import com.kindred.mir.util.Util;

public class ImGuiTextBox extends ImGuiControl{

  private Font font;
  byte[] text_buf;
  private Color backColor;
  private Color foreColor;
  private boolean isPassword;

  protected ControlCommonListener onInputFinished;

  public ImGuiTextBox(ImGuiWindow parent, long window_id, long renderer_id, String label, Point location,
      int width, int text_max_length,Font font, Color backColor, Color foreColor, boolean isPassword) throws Exception{
    super(parent, window_id, renderer_id, label, location);
    if (font.getFontID() == 0) {
      throw new Exception("Invalid font.");
    }
    setSize(new Size(width,0));
    this.font=font;
    this.backColor=backColor;
    this.foreColor=foreColor;
    this.isPassword=isPassword;
    MirJNI.ImGui_InitBackColor(this.backColor.getRedF(), this.backColor.getGreenF(), this.backColor.getBlueF(), this.backColor.getAlphaF());
    MirJNI.ImGui_InitForeColor(this.foreColor.getRedF(), this.foreColor.getGreenF(), this.foreColor.getBlueF(), this.foreColor.getAlphaF());
    text_buf=new byte[text_max_length];
  }

  @Override
  public boolean beginDraw() {
    MirJNI.ImGui_PushFont(font.getFontID());

    //MirJNI.ImGui_InitBackColor(color_mod%3 == 1 ? 1.0f:0f, color_mod%3 == 2 ? 1.0f:0f, color_mod%3 == 0 ? 1.0f:0f, 0.5f);
    //MirJNI.ImGui_InitForeColor(color_mod%3 == 0 ? 1.0f:0f, color_mod%3 == 1 ? 1.0f:0f, color_mod%3 == 2 ? 1.0f:0f, 0.5f);
    Point pos = getDisplayLocation();
    if(MirJNI.ImGui_InputText(pos.getX(), pos.getY(), this.getSize().getWidth(), Util.toCstyleBytes(getLabel()), Util.toCstyleBytes("请输入内容..."), text_buf, this.isPassword)) {
      if (onInputFinished != null) {
        onInputFinished.doAction(this, Util.zeroEndBytesToString(text_buf));
      }
    }
    return true;
  }

  @Override
  public boolean endDraw() {
    MirJNI.ImGui_PopFont();
    return true;
  }

}
