package com.kindred.mir.controls;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.Font;
import com.kindred.mir.engine.TextFormatFlags;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Size;

import java.util.Optional;

public class MirLabel extends MirControl{

    private boolean isAutoSize;
    public ControlCommonListener autoSizeChanged;

    private String text;
    public ControlCommonListener textChanged;

    private TextFormatFlags drawFormat;
    public ControlCommonListener drawFormatChanged;

    private Font font;
    public ControlCommonListener fontChanged;

    private boolean isOutLine;
    public ControlCommonListener outLineChanged;

    private Color outLineColor;
    public ControlCommonListener outLineColorChanged;

    public MirLabel(MirControl parent)
    {
        super(parent);
        isDrawControlTexture = true;
        drawFormat = TextFormatFlags.WordBreak;

        //font = new Font(Settings.FontName, 8F);
        isOutLine = true;
        outLineColor = Color.Black;
        text = "";

    }

    public boolean getIsAutoSize()
    {
        return isAutoSize;
    }
    public void setIsAutoSize(boolean isAutoSize)
    {
        if (this.isAutoSize == isAutoSize)
            return;
        this.isAutoSize = isAutoSize;
        onAutoSizeChanged();
    }

    private void updateSize()
    {
        if (!isAutoSize)
            return;

        if (Optional.ofNullable(text).orElse("").isEmpty())
            size = Size.Empty;
        else
        {
            //size = TextRenderer.MeasureText(CMain.Graphics, text, Font);
            //Size = new Size(Size.Width, Size.Height + 5);

            //if (OutLine && size != Size.Empty)
            //    size = new Size(size.getWidth() + 2, size.getHeight() + 2);
        }
    }

    private void onAutoSizeChanged()
    {
        isTextureValid = false;
        updateSize();
        if (autoSizeChanged != null)
            autoSizeChanged.doAction(this, null);
    }

    public TextFormatFlags getDrawFormat()
    {
        return  drawFormat;
    }

    public void setDrawFormat(TextFormatFlags drawFormat)
    {
        if (this.drawFormat == drawFormat)
        {
            return;
        }
        this.drawFormat = drawFormat;
        onDrawFormatChanged();
    }

    private void onDrawFormatChanged()
    {
        isTextureValid = false;

        if (drawFormatChanged != null)
            drawFormatChanged.doAction(this, null);
    }

    public Font getFont()
    {
        return font;
    }
    public void setFont(Font font)
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
        isTextureValid = false;

        updateSize();

        if (fontChanged != null)
            fontChanged.doAction(this, null);
    }

    public boolean getIsOutLine()
    {
        return isOutLine;
    }
    public void setIsOutLine(boolean isOutLine)
    {
        if (this.isOutLine == isOutLine)
            return;
        this.isOutLine = isOutLine;
        onOutLineChanged();
    }
    private void onOutLineChanged()
    {
        isTextureValid = false;
        updateSize();

        if (outLineChanged != null)
            outLineChanged.doAction(this, null);
    }

    public Color getOutLineColor()
    {
        return outLineColor;
    }
    public void setOutLineColor(Color outLineColor)
    {
        if (this.outLineColor == outLineColor)
            return;
        this.outLineColor = outLineColor;
        onOutLineColorChanged();
    }

    private void onOutLineColorChanged()
    {
        isTextureValid = false;

        if (outLineColorChanged != null)
            outLineColorChanged.doAction(this, null);
    }

    public String getText()
    {
        return text;
    }
    public void setText(String text)
    {
        if (this.text == text)
            return;

        this.text = text;
        onTextChanged();
    }

    private void onTextChanged()
    {
        isDrawControlTexture = !Optional.ofNullable(text).orElse("").isEmpty();
        isTextureValid = false;
        redraw();

        updateSize();

        if (textChanged != null)
            textChanged.doAction(this, null);
    }

    @Override
    protected void createTexture()
    {
        if (Optional.ofNullable(text).orElse("").isEmpty())
            return;

        if (size.getWidth() == 0 || size.getHeight() == 0)
            return;

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
        isTextureValid = true;
    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

        if (!disposing) return;

        autoSizeChanged = null;
        isAutoSize = false;

        drawFormatChanged = null;
        //drawFormat = 0;

        fontChanged = null;
        font = null;

        outLineChanged = null;
        isOutLine = false;

        outLineColorChanged = null;
        outLineColor = Color.Empty;

        textChanged = null;
        text = null;
    }

}
