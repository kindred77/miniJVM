package com.kindred.mir.engine;

import com.kindred.mir.util.Util;

public class Font {
  private String fontFileName;
  private float size;
  private long font_id;

  public Font(String fontFileName, float size)
  {
    this.fontFileName=fontFileName;
    this.size=size;

    font_id=MirJNI.ImGui_InitFont(Util.toCstyleBytes(fontFileName), size);
  }

  public float getSize() {
    return size;
  }

  public String getFontFileName() {
    return fontFileName;
  }

  public long getFontID() {
    return font_id;
  }
}
