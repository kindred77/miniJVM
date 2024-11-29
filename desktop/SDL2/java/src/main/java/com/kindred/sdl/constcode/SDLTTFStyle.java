package com.kindred.sdl.constcode;

public class SDLTTFStyle {
    /** normal */
    public static final int TTF_STYLE_NORMAL = 0x00000000;

    /** bold */
    public static final int TTF_STYLE_BOLD = 0x00000001;

    /** italic */
    public static final int TTF_STYLE_ITALIC = 0x00000002;

    /** underline */
    public static final int TTF_STYLE_UNDERLINE = 0x00000004;

    /** strike through */
    public static final int TTF_STYLE_STRIKETHROUGH = 0x00000008;

    public static String toString(int type) {
        StringBuilder result = new StringBuilder(36);
        if ((type & TTF_STYLE_NORMAL) > 0) {
            result.append("TTF_STYLE_NORMAL");
        }
        if ((type & TTF_STYLE_BOLD) > 0) {
            result.append("TTF_STYLE_BOLD");
        }
        if ((type & TTF_STYLE_ITALIC) > 0) {
            result.append("TTF_STYLE_ITALIC");
        }
        if ((type & TTF_STYLE_UNDERLINE) > 0) {
            result.append("TTF_STYLE_UNDERLINE");
        }
        if ((type & TTF_STYLE_STRIKETHROUGH) > 0) {
            result.append("TTF_STYLE_STRIKETHROUGH");
        }

        if (result.length() == 0) {
            result.append("0");
        }
        return result.toString();
    }

    private SDLTTFStyle() {
    }
}
