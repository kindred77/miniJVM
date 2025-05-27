package com.kindred.mir.engine;

import static com.kindred.sdl.constcode.SDLTTFStyle.*;

import com.kindred.mir.util.Color;
import com.kindred.mir.util.Util;

public final class Font {

  //注意：加锁不要使用synchronized代码块的方式，有问题
  private final static Object lock = new Object();

  private String fontFileName;
  private float size;
  private long imgui_font_id;
  private long sdl_font_id;

  //style
  private boolean isBold;
  private boolean isItalic;
  private boolean isUnderLine;
  private boolean isStrikeThrough;

  private int styleFlags=TTF_STYLE_NORMAL;

  public Font(String fontFileName, float size) throws Exception
  {
    this.fontFileName=fontFileName;
    this.size=size;
  }

  /*
  需要选初始化imgui context，并且设置正确的context
   */
  public final void initImGuiFont() throws Exception{
    imgui_font_id=MirJNI.ImGui_InitFont(Util.toCstyleBytes(fontFileName), size);
    if (imgui_font_id == 0) {
      throw new Exception("Can not init imgui font: "+fontFileName);
    }
  }

  public final void initSDLFont() throws Exception{
    sdl_font_id=MirJNI.SDL_TTF_OpenFont(Util.toCstyleBytes(fontFileName), size);
    if (sdl_font_id == 0) {
      throw new Exception("Can not init sdl font: "+fontFileName+", "+MirJNI.SDL_GetError());
    }
  }

  public final float getSize() {
    return size;
  }

  public final String getFontFileName() {
    return fontFileName;
  }

  public final long getImGuiFontID() {
    return imgui_font_id;
  }

  public final long getSDLFontID() {
    return sdl_font_id;
  }

  public final void setIsBold(boolean isBold) {
    if (isBold) {
      this.styleFlags |= TTF_STYLE_BOLD;
    } else {
      this.styleFlags ^= TTF_STYLE_BOLD;
    }
  }

  public final boolean getIsBold() {
    return (this.styleFlags & TTF_STYLE_BOLD)!=0;
  }

  public final void setIsItalic(boolean isItalic) {
    if (isItalic) {
      this.styleFlags |= TTF_STYLE_ITALIC;
    } else {
      this.styleFlags ^= TTF_STYLE_ITALIC;
    }
  }

  public final boolean getIsItalic() {
    return (this.styleFlags & TTF_STYLE_ITALIC)!=0;
  }

  public final void setIsUnderLine(boolean isUnderLine) {
    if (isUnderLine) {
      this.styleFlags |= TTF_STYLE_UNDERLINE;
    } else {
      this.styleFlags ^= TTF_STYLE_UNDERLINE;
    }
  }

  public final boolean getIsUnderLine() {
    return (this.styleFlags & TTF_STYLE_UNDERLINE)!=0;
  }

  public final void setIsStrikeThrough(boolean isStrikeThrough) {
    if (isStrikeThrough) {
      this.styleFlags |= TTF_STYLE_STRIKETHROUGH;
    } else {
      this.styleFlags ^= TTF_STYLE_STRIKETHROUGH;
    }
  }

  public final boolean getIsStrikeThrough() {
    return (this.styleFlags & TTF_STYLE_STRIKETHROUGH)!=0;
  }

  public final int getStyleFlags() {
      return this.styleFlags;
  }

  public static long generateTextSurface(Font font,String text, Color foreColor, Color backColor,int wraplength) {
    synchronized (lock) {
      MirJNI.SDL_TTF_SetFontStyle(font.getSDLFontID(), font.getStyleFlags());
    }
    return MirJNI.SDL_TTF_RenderUTF8_LCD_Wrapped(font.getSDLFontID(), Util.toCstyleBytes(text),
        new int[]{foreColor.getRed(),foreColor.getGreen(),foreColor.getBlue(),foreColor.getAlpha()},
        new int[]{backColor.getRed(),backColor.getGreen(),backColor.getBlue(),backColor.getAlpha()},
        wraplength);
  }
}
