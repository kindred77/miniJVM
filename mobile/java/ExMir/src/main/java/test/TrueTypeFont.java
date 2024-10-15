/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.mini.glfw.Glfw;
import static org.mini.nanovg.Nanovg.stbi_write_png;
import static org.mini.nanovg.Nanovg.stbtt_GetCodepointBitmapBox;
import static org.mini.nanovg.Nanovg.stbtt_GetCodepointHMetrics;
import static org.mini.nanovg.Nanovg.stbtt_GetCodepointKernAdvance;
import static org.mini.nanovg.Nanovg.stbtt_GetFontVMetrics;
import static org.mini.nanovg.Nanovg.stbtt_InitFont;
import static org.mini.nanovg.Nanovg.stbtt_MakeCodepointBitmapOffset;
import static org.mini.nanovg.Nanovg.stbtt_MakeFontInfo;
import static org.mini.nanovg.Nanovg.stbtt_ScaleForPixelHeight;

import static org.mini.nanovg.Nanovg.stbtt_FindGlyphIndex;

/**
 *
 * @author gust
 */
public class TrueTypeFont {

    static {
        Glfw.loadLib();
    }

    public static void main(String[] args) {
        TrueTypeFont gt = new TrueTypeFont();
        gt.t1();

    }
    byte[] fontBuffer;
    byte[] info;

    void t1() {
        /* load font file */
        int size;


        //fontBuffer = GToolkit.readFileFromJar("/res/NotoEmoji+NotoSansCJKSC-Regular.ttf");
        //fontBuffer = GToolkit.readFileFromFile("NotoEmoji+NotoSansCJKSC-Regular.ttf");
        fontBuffer = GToolkit.readFileFromFile("STXINGKA.TTF");
        if (fontBuffer != null)
        {
            System.out.println("ttf file size: " + fontBuffer.length);
        }
        /* prepare font */
        info = stbtt_MakeFontInfo();
        long infoPtr = GToolkit.getArrayDataPtr(info);
        if (stbtt_InitFont(infoPtr, fontBuffer, 0) == 0) {
            System.out.println("failed\n");
        }
        System.out.println("----0000---");
        int b_w = 2560;
        /* bitmap width */
        int b_h = 128;
        /* bitmap height */
        int l_h = 64;
        /* line height */
 /* create a bitmap for the phrase */
        byte[] bitmap = new byte[b_w * b_h];
        long bitmapPtr = GToolkit.getArrayDataPtr(bitmap);
        System.out.println("----1111---");
        /* calculate font scaling */
        float scale = stbtt_ScaleForPixelHeight(infoPtr, l_h);
        String word = "唐";
        System.out.println("----2222---scale: "+scale);
        int x = 0;

        int[] ascent = {0}, descent = {0}, lineGap = {0};
        stbtt_GetFontVMetrics(infoPtr, ascent, descent, lineGap);
        System.out.println("----3333---"+ascent[0]+"----"+descent[0]+"------"+lineGap[0]);
        ascent[0] *= scale;
        descent[0] *= scale;

        int i;
        for (i = 0; i < word.length(); ++i) {
            int ch = word.charAt(i);//word[i];
            int nch = i < word.length() - 1 ? word.charAt(i + 1) : 0;//word[i + 1];
            /* get bounding box for character (may be offset to account for chars that dip above or below the line */
            int[] c_x1 = {0}, c_y1 = {0}, c_x2 = {0}, c_y2 = {0};
            stbtt_GetCodepointBitmapBox(infoPtr, ch, scale, scale, c_x1, c_y1, c_x2, c_y2);
            System.out.println("----4444---"+c_x1[0]+"---"+c_y1[0]+"---"+c_x2[0]+"---"+c_y2[0]+"----scale:"+scale+"---ch:"+ch);
            /* compute y (different characters have different heights */
            int y = ascent[0] + c_y1[0];

            /* render character (stride and offset is important here) */
            int byteOffset = x + (y * b_w);
            int glyph_index = stbtt_FindGlyphIndex(infoPtr, ch);
            System.out.println("----444----2222---byteOffset: "+byteOffset+"---c_x2[0] - c_x1[0]: "+(c_x2[0] - c_x1[0])+"---c_y2[0] - c_y1[0]: "+(c_y2[0] - c_y1[0])+"---b_w:"+b_w+"---scale:"+scale+"-----glyph_index:"+glyph_index);
            stbtt_MakeCodepointBitmapOffset(infoPtr, bitmap, byteOffset, c_x2[0] - c_x1[0], c_y2[0] - c_y1[0], b_w, scale, scale, ch);
            //stbtt_MakeCodepointBitmapOffset(infoPtr, bitmap, 20, 10, 10, 2560, 0.05f, 0.05f, ch);
            System.out.println("----5555---byteOffset: "+byteOffset);
            /* how wide is this character */
            int[] ax = {0}, bx = {0};
            stbtt_GetCodepointHMetrics(infoPtr, ch, ax, bx);
            x += ax[0] * scale;
            System.out.println("----6666---");
            /* add kerning */
            int kern;
            kern = stbtt_GetCodepointKernAdvance(infoPtr, ch, nch);
            x += kern * scale;
        }
        System.out.println("----7777---");
        /* save out a 1 channel image */
        stbi_write_png("./out.png\000".getBytes(), b_w, b_h, 1, bitmapPtr, b_w);

    }
}
