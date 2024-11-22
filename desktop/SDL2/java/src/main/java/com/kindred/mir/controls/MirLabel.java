package com.kindred.mir.controls;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.Font;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.engine.TextFormatFlags;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Size;

import com.kindred.mir.util.Util;
import java.util.Optional;

public class MirLabel extends MirControlWithTexture{

    private long drawData_ptr=0L;

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

    public MirLabel(String text, MirControl parent, long renderer_id, Size size)
    {
        super(parent,renderer_id);

        isDrawControlTexture = true;
        drawFormat = TextFormatFlags.WordBreak;

        //font = new Font(Settings.FontName, 8F);
        isOutLine = true;
        outLineColor = Color.Black;
        this.text = text;
        setSize(size);
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
    protected boolean updateTexture(long surface_id) {
        MirJNI.ImGui_NewFrame();
        if (!MirJNI.ImGui_Begin(Util.toCstyleBytes("login"), 100,50, 200, 25, true)) {
            MirJNI.ImGui_End();
            return false;
        } else {
            //MirJNI.ImGui_Text(toCstyleBytes("标签"));

            //动态修改一些属性
//            long cur_ts = MirJNI.SDL_GetTicks();
//            if ((cur_ts - prev_ts) > 5000)
//            {
//                color_mod++;
//                //color_mod = color_mod % 3;
//                prev_ts = cur_ts;
//                MirJNI.ImGui_SetWindowFontScale(color_mod);
//                MirJNI.ImGui_InitBackColor(color_mod%3 == 1 ? 1.0f:0f, color_mod%3 == 2 ? 1.0f:0f, color_mod%3 == 0 ? 1.0f:0f, 0.5f);
//                MirJNI.ImGui_InitForeColor(color_mod%3 == 0 ? 1.0f:0f, color_mod%3 == 1 ? 1.0f:0f, color_mod%3 == 2 ? 1.0f:0f, 0.5f);
//            }

            MirJNI.ImGui_Text(Util.toCstyleBytes("这是一个标签的例子"));

        }

        drawData_ptr = MirJNI.ImGui_RenderAndGetDrawData();
        return true;
    }

    @Override
    protected boolean drawControl() {
        if (drawData_ptr!=0) {
            MirJNI.ImGui_Render(renderer_id, drawData_ptr);
        }

        return true;
    }

//    @Override
//    protected boolean updateTexture()
//    {
//        if (Optional.ofNullable(text).orElse("").isEmpty())
//            return false;
//
//        if (size.getWidth() == 0 || size.getHeight() == 0)
//            return false;

//        if (controlTexture != null && !controlTexture.Disposed && textureSize != size)
//            controlTexture.Dispose();
//
//        if (controlTexture == null || controlTexture.Disposed)
//        {
//            DXManager.ControlList.Add(this);
//
//            controlTexture = new Texture(DXManager.Device, Size.Width, Size.Height, 1, Usage.None, Format.A8R8G8B8, Pool.Managed);
//            controlTexture.Disposing += ControlTexture_Disposing;
//            textureSize = size;
//        }
//
//        using (GraphicsStream stream = ControlTexture.LockRectangle(0, LockFlags.Discard))
//        using (Bitmap image = new Bitmap(Size.Width, Size.Height, Size.Width * 4, PixelFormat.Format32bppArgb, (IntPtr) stream.InternalDataPointer))
//        {
//            using (Graphics graphics = Graphics.FromImage(image))
//            {
//                graphics.SmoothingMode = SmoothingMode.AntiAlias;
//                graphics.TextRenderingHint = TextRenderingHint.AntiAliasGridFit;
//                graphics.CompositingQuality = CompositingQuality.HighQuality;
//                graphics.InterpolationMode = InterpolationMode.NearestNeighbor;
//                graphics.PixelOffsetMode = PixelOffsetMode.HighQuality;
//                graphics.TextContrast = 0;
//                graphics.Clear(BackColour);
//
//
//                if (OutLine)
//                {
//                    TextRenderer.DrawText(graphics, Text, Font, new Rectangle(1, 0, Size.Width, Size.Height), OutLineColour, DrawFormat);
//                    TextRenderer.DrawText(graphics, Text, Font, new Rectangle(0, 1, Size.Width, Size.Height), OutLineColour, DrawFormat);
//                    TextRenderer.DrawText(graphics, Text, Font, new Rectangle(2, 1, Size.Width, Size.Height), OutLineColour, DrawFormat);
//                    TextRenderer.DrawText(graphics, Text, Font, new Rectangle(1, 2, Size.Width, Size.Height), OutLineColour, DrawFormat);
//                    TextRenderer.DrawText(graphics, Text, Font, new Rectangle(1, 1, Size.Width, Size.Height), ForeColour, DrawFormat);
//
//                    //LinearGradientBrush brush = new LinearGradientBrush(new Rectangle(0, 0, this.Size.Width, this.Size.Height), Color.FromArgb(239, 243, 239), Color.White, LinearGradientMode.Vertical);
//                    ////graphics.DrawString(Text, Font, brush, 37, 9);
//                    ////graphics.DrawString(this.Text, this.Font, new SolidBrush(Color.Black), 39, 9, StringFormat.GenericDefault);
//                }
//                else
//                    TextRenderer.DrawText(graphics, Text, Font, new Rectangle(1, 0, Size.Width, Size.Height), ForeColour, DrawFormat);
//            }
//        }
//        ControlTexture.UnlockRectangle(0);
//        DXManager.Sprite.Flush();

//        return true;
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
