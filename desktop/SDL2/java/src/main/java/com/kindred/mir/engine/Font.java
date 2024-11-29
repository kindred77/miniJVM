package com.kindred.mir.engine;

import com.kindred.mir.util.Util;

public class Font {
  private String fontFileName;
  private float size;
  private long font_id;

  public Font(String fontFileName, float size) throws Exception
  {
    this.fontFileName=fontFileName;
    this.size=size;
  }

  /*
  需要选初始化imgui context，并且设置正确的context
   */
  public void initFont() throws Exception{
    font_id=MirJNI.ImGui_InitFont(Util.toCstyleBytes(fontFileName), size);
    if (font_id == 0) {
      throw new Exception("Can not init font: "+fontFileName);
    }
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
