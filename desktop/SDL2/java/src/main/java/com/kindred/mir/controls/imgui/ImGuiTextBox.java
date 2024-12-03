package com.kindred.mir.controls.imgui;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.Font;
import com.kindred.mir.engine.MirJNI;
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
  private int multiLines;

  protected ControlCommonListener onInputFinished;

  public ImGuiTextBox(ImGuiWindow parent, long window_id, long renderer_id, String label, Point location,
      int width, int text_max_length,Font font, Color backColor, Color foreColor, boolean isPassword, int multiLines) throws Exception{
    super(parent, window_id, renderer_id, label, location);
    if (font.getImGuiFontID() == 0) {
      throw new Exception("Invalid font.");
    }
    if (multiLines == 0) {
      throw new Exception("Invalid multiLines: "+multiLines);
    }
    if (multiLines>1 && isPassword) {
      throw new Exception("Do not support password textbox with multilines yet.");
    }
    setSize(new Size(width,0));
    this.font=font;
    this.backColor=backColor;
    this.foreColor=foreColor;
    this.isPassword=isPassword;
    this.multiLines=multiLines;
    MirJNI.ImGui_InitBackColor(this.backColor.getRedF(), this.backColor.getGreenF(), this.backColor.getBlueF(), this.backColor.getAlphaF());
    MirJNI.ImGui_InitForeColor(this.foreColor.getRedF(), this.foreColor.getGreenF(), this.foreColor.getBlueF(), this.foreColor.getAlphaF());
    text_buf=new byte[text_max_length];
  }

  @Override
  public boolean beginDraw() {
    MirJNI.ImGui_PushFont(font.getImGuiFontID());
    Point pos = getDisplayLocation();
    if (this.multiLines <= 1) {
      if(MirJNI.ImGui_InputText(pos.getX(), pos.getY(), this.getSize().getWidth(), Util.toCstyleBytes(getLabel()), Util.toCstyleBytes("请输入内容..."), text_buf, this.isPassword)) {
        if (onInputFinished != null) {
          onInputFinished.doAction(this, Util.zeroEndBytesToString(text_buf));
        }
      }
    } else {
      if(MirJNI.ImGui_InputTextMultiline(pos.getX(), pos.getY(), Util.toCstyleBytes(getLabel()), text_buf, this.getSize().getWidth(), this.multiLines)) {
        if (onInputFinished != null) {
          onInputFinished.doAction(this, Util.zeroEndBytesToString(text_buf));
        }
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
