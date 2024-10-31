package com.kindred.mir.controls;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.util.Color;

import java.util.Optional;

public class MirButton extends MirImageControl {

    private MirLabel label;

    private int hoverIndex;
    public ControlCommonListener hoverIndexChanged;

    private int pressedIndex;
    public ControlCommonListener pressedIndexChanged;

    private boolean isCenterText;

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

    public int getHoverIndex()
    {
        return hoverIndex;
    }
    public void setHoverIndex(int hoverIndex)
    {
        if (this.hoverIndex == hoverIndex)
            return;
        this.hoverIndex = hoverIndex;
        onHoverIndexChanged();
    }

    private void onHoverIndexChanged()
    {
        if (hoverIndexChanged != null)
            hoverIndexChanged.doAction(this, null);
    }

    @Override
    public int getIndex()
    {
        if (!isEnabled)
            return super.getIndex();

        if (pressedIndex >= 0 && ActiveControl == this && MouseControl == this)
            return pressedIndex;

        if (hoverIndex >= 0 && MouseControl == this)
            return hoverIndex;

        return super.getIndex();
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
            label.setDrawFormat(TextFormatFlags.HorizontalCenter | TextFormatFlags.VerticalCenter);
        }
        else
            label.setIsAutoSize(true);
    }

    public int getPressedIndex()
    {
        return pressedIndex;
    }
    public void setPressedIndex(int pressedIndex)
    {
        if (this.pressedIndex == pressedIndex)
            return;
        this.pressedIndex = pressedIndex;
        onPressedIndexChanged();
    }

    private void onPressedIndexChanged()
    {
        if (pressedIndexChanged != null)
            pressedIndexChanged.doAction(this, null);
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

    public MirButton()
    {
        hoverIndex = -1;
        pressedIndex = -1;
        sound = SoundList.ButtonB;

        label = new MirLabel();
        label.setIsNotControl(true);
        label.setParent(this);
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

        hoverIndexChanged = null;
        hoverIndex = 0;

        if (label != null && !label.getIsDisposed())
            label.dispose();
        label = null;

        pressedIndexChanged = null;
        pressedIndex = 0;
    }

}
