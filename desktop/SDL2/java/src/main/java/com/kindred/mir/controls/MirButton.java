package com.kindred.mir.controls;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.util.Color;

import java.util.Optional;

public class MirButton extends MirStaticImageControl {

    protected MirLabel label;

    //protected int hoverIndex;
    //public ControlCommonListener hoverIndexChanged;
    protected MirImage hoverImage;
    public ControlCommonListener hoverImageChanged;

    //protected int pressedIndex;
    //public ControlCommonListener pressedIndexChanged;
    protected MirImage pressedImage;
    public ControlCommonListener pressedImageChanged;

    private boolean isCenterText;

    public MirButton(MirControl parent, MirImage normalImage, MirImage hoverImage, MirImage pressedImage)
    {
        super(parent, normalImage);
        //hoverIndex = -1;
        //pressedIndex = -1;
        this.hoverImage=hoverImage;
        this.pressedImage=pressedImage;
        sound = SoundList.ButtonB;

        label = new MirLabel(this);
        label.setIsNotControl(true);
    }

    public Color getFontColor()
    {
        if (label != null && !label.getIsDisposed())
            return label.getForeColor();
        return Color.Empty;
    }
    public void setFontColor(Color fontColor)
    {
        if (label != null && !label.getIsDisposed())
            label.setForeColor(fontColor);
    }

    public MirImage getHoverImage()
    {
        return hoverImage;
    }
    public void setHoverImage(MirImage hoverImage)
    {
        if (this.hoverImage == hoverImage)
            return;
        this.hoverImage = hoverImage;
        onHoverImageChanged();
    }

    private void onHoverImageChanged()
    {
        if (hoverImageChanged != null)
            hoverImageChanged.doAction(this, null);
    }

    @Override
    public MirImage getMirImage()
    {
        if (!isEnabled)
            return super.getMirImage();

        if (pressedImage != null && ActiveControl == this && MouseControl == this)
            return pressedImage;

        if (hoverImage != null && MouseControl == this)
            return hoverImage;

        return super.getMirImage();
    }

    public boolean getIsCenterText()
    {
        return isCenterText;
    }
    public void setIsCenterText(boolean isCenterText)
    {
        this.isCenterText = isCenterText;
        if (this.isCenterText)
        {
            label.setSize(size);
            //label.setDrawFormat(TextFormatFlags.HorizontalCenter | TextFormatFlags.VerticalCenter);
        }
        else
            label.setIsAutoSize(true);
    }

    public MirImage getPressedImage()
    {
        return pressedImage;
    }
    public void setPressedImage(MirImage pressedImage)
    {
        if (this.pressedImage == pressedImage)
            return;
        this.pressedImage = pressedImage;
        onPressedImageChanged();
    }

    private void onPressedImageChanged()
    {
        if (pressedImageChanged != null)
            pressedImageChanged.doAction(this, null);
    }

    @Override
    protected void onSizeChanged()
    {
        super.onSizeChanged();
        if (label != null && !label.getIsDisposed())
            label.setSize(size);
    }

    public void setText(String text)
    {
        if (label == null || label.getIsDisposed())
            return;
        label.setText(text);
        label.setIsVisible(!Optional.ofNullable(text).orElse("").isEmpty());
    }

    @Override
    protected void highlight()
    {
        redraw();
        super.highlight();
    }

    @Override
    protected void activate()
    {
        redraw();
        super.activate();
    }

    @Override
    protected void dehighlight()
    {
        redraw();
        super.dehighlight();
    }

    @Override
    protected void deactivate()
    {
        redraw();
        super.deactivate();
    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

        if (!disposing) return;

        hoverImageChanged = null;
        hoverImage = null;

        if (label != null && !label.getIsDisposed())
            label.dispose();
        label = null;

        pressedImageChanged = null;
        pressedImage = null;
    }

}
