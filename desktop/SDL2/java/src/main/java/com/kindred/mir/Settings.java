package com.kindred.mir;

import com.kindred.mir.engine.Font;

public class Settings {

    public static int ScreenWidth = 800;
    public static int ScreenHeight = 600;
    public static boolean IsFullScreen = false;
    public static long DoubleClickIntervalTime=200L;

    public static final String MIRFONT="NotoEmoji+NotoSansCJKSC-Regular.ttf";
    public static Font FONT_SIZE15;
    public static Font FONT_SIZE20;

    public static long getTime()
    {
        return System.currentTimeMillis();
    }
}
