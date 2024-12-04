package com.kindred.mir;

import com.kindred.mir.engine.Font;

public class Settings {

    public static int ScreenWidth = 800;
    public static int ScreenHeight = 600;
    public static boolean IsFullScreen = false;
    public static long DoubleClickIntervalTime=200L;

    public static boolean isImguiUsed=false;

    //fonts
    public static final String MIRFONT="FZSSJW.TTF";
    public static Font FONT_SIZE15;
    public static Font FONT_SIZE20;
    public static void makeSureImGuiFontsInited() throws Exception{
        if (FONT_SIZE15==null) {
            FONT_SIZE15 =new Font(MIRFONT, 15);
            FONT_SIZE20 =new Font(MIRFONT, 20);
        }
        if (FONT_SIZE15.getImGuiFontID()==0) {
            FONT_SIZE15.initImGuiFont();
            FONT_SIZE20.initImGuiFont();
        }
    }
    public static void makeSureSDLFontsInited() throws Exception{
        if (FONT_SIZE15==null) {
            FONT_SIZE15 =new Font(MIRFONT, 15);
            FONT_SIZE20 =new Font(MIRFONT, 20);
        }
        if (FONT_SIZE15.getSDLFontID()==0) {
            FONT_SIZE15.initSDLFont();
            FONT_SIZE20.initSDLFont();
        }
    }

    public static long getTime()
    {
        return System.currentTimeMillis();
    }
}
