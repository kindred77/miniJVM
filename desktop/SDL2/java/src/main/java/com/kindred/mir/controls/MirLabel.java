package com.kindred.mir.controls;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.Font;
import com.kindred.mir.engine.TextFormatFlags;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;

import java.util.Optional;

public class MirLabel extends MirControlWithTexture{

    private boolean isAutoSize;
    protected ControlCommonListener onAutoSizeChanged;

    private String text;
    protected ControlCommonListener onTextChanged;

    private TextFormatFlags drawFormat;
    protected ControlCommonListener onDrawFormatChanged;

    private Font font;
    protected ControlCommonListener onFontChanged;

    private boolean isOutLine;
    protected ControlCommonListener onOutLineChanged;

    private Color outLineColor;
    protected ControlCommonListener onOutLineColorChanged;

    private int wraplength;

    public MirLabel(MirControl parent, long renderer_id, Size size, Point pos, Font font, String text, Color foreColor, Color backColor, int wraplength)
    {
        super(parent,renderer_id);
        drawFormat = TextFormatFlags.WordBreak;
        isOutLine = true;
        outLineColor = Color.Black;
        this.text = text;
        setLocation(pos);
        setSize(size);
        this.font=font;
        this.foreColor=foreColor;
        this.backColor=backColor;
        this.wraplength=wraplength;

        //int[] back_color_arr = new int[]{backColor.getRed(), backColor.getGreen(), backColor.getBlue(), backColor.getAlpha()};
        //long main_surface = MirJNI.Mir_FillRect(size.getWidth(), size.getHeight(), back_color_arr);

        long font_surface =Font.generateTextSurface(this.font,text,foreColor,backColor,this.wraplength);
        //MirJNI.Mir_SurfaceBlendNormal(main_surface,font_surface,0,0,1f);
        //MirJNI.SDL_FreeSurface(font_surface);
        updateTexture(font_surface,true);
    }

    public final boolean getIsAutoSize()
    {
        return isAutoSize;
    }
    public final void setIsAutoSize(boolean isAutoSize)
    {
        if (this.isAutoSize == isAutoSize) {
            return;
        }
        this.isAutoSize = isAutoSize;
        onAutoSizeChanged();
    }

    private void updateSize()
    {
        if (!isAutoSize) {
            return;
        }

        if (Optional.ofNullable(text).orElse("").isEmpty()) {
            size = Size.Empty;
        }
        else {
            //size = TextRenderer.MeasureText(CMain.Graphics, text, Font);
            //Size = new Size(Size.Width, Size.Height + 5);

            //if (OutLine && size != Size.Empty)
            //    size = new Size(size.getWidth() + 2, size.getHeight() + 2);
        }
    }

    private void onAutoSizeChanged()
    {
        updateSize();
        if (onAutoSizeChanged != null) {
            onAutoSizeChanged.doAction(this, null);
        }
    }

    public final TextFormatFlags getDrawFormat()
    {
        return  drawFormat;
    }

    public final void setDrawFormat(TextFormatFlags drawFormat)
    {
        if (this.drawFormat == drawFormat) {
            return;
        }
        this.drawFormat = drawFormat;
        onDrawFormatChanged();
    }

    private void onDrawFormatChanged()
    {
        if (onDrawFormatChanged != null) {
            onDrawFormatChanged.doAction(this, null);
        }
    }

    public final Font getFont()
    {
        return font;
    }
    public final void setFont(Font font)
    {
        if (this.font == font)
        {
            return;
        }
        this.font = font;
        onFontChanged();
    }

    private void onFontChanged()
    {
        updateSize();

        if (onFontChanged != null) {
            onFontChanged.doAction(this, null);
        }
    }

    public final boolean getIsOutLine()
    {
        return isOutLine;
    }
    public final void setIsOutLine(boolean isOutLine)
    {
        if (this.isOutLine == isOutLine) {
            return;
        }
        this.isOutLine = isOutLine;
        onOutLineChanged();
    }
    private void onOutLineChanged()
    {
        updateSize();

        if (onOutLineChanged != null) {
            onOutLineChanged.doAction(this, null);
        }
    }

    public final Color getOutLineColor()
    {
        return outLineColor;
    }
    public final void setOutLineColor(Color outLineColor)
    {
        if (this.outLineColor == outLineColor) {
            return;
        }
        this.outLineColor = outLineColor;
        onOutLineColorChanged();
    }

    private void onOutLineColorChanged()
    {
        if (onOutLineColorChanged != null) {
            onOutLineColorChanged.doAction(this, null);
        }
    }

    public final String getText()
    {
        return text;
    }
    public final void setText(String text)
    {
        if (this.text == text) {
            return;
        }

        this.text = text;

        long font_surface =Font.generateTextSurface(this.font,text,foreColor,backColor,this.wraplength);
        updateTexture(font_surface,true);

        onTextChanged();
    }

    private void onTextChanged()
    {
        isDrawControlTexture = !Optional.ofNullable(text).orElse("").isEmpty();

        updateSize();

        if (onTextChanged != null) {
            onTextChanged.doAction(this, null);
        }
    }

    @Override
    protected boolean updateTexture(long surface_id,boolean ifReleaseSurface) {

        controlTexture.update(getRenderer(),surface_id,ifReleaseSurface);
        return true;
    }

//    @Override
//    protected boolean drawControl() {
//
//        Rectangle rct = getDisplayRectangle();
//        int[] rct_arr = {rct.getX(), rct.getY(), rct.getWidth(), rct.getHeight()};
//        //暂存原来的颜色
//        int[] origin_color = MirJNI.SDL_GetRenderDrawColor(getRenderer());
//        Color backColor=getBackColor();
//        //设置现在的颜色
//        MirJNI.SDL_SetRenderDrawColor(getRenderer(),backColor.getRed(),backColor.getGreen(),backColor.getBlue(),backColor.getAlpha());
//        MirJNI.SDL_RenderFillRect(getRenderer(), rct_arr);
//        //画完以后恢复原来的颜色
//        MirJNI.SDL_SetRenderDrawColor(getRenderer(),origin_color[0],origin_color[1],origin_color[2],origin_color[3]);
//        return super.drawControl();
//    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

        if (!disposing) {
            return;
        }

        onAutoSizeChanged = null;
        isAutoSize = false;

        onDrawFormatChanged = null;
        //drawFormat = 0;

        onFontChanged = null;
        font = null;

        onOutLineChanged = null;
        isOutLine = false;

        onOutLineColorChanged = null;
        outLineColor = Color.Empty;

        onTextChanged = null;
        text = null;
    }

}
