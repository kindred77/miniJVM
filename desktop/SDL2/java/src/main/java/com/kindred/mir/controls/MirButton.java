package com.kindred.mir.controls;

import com.kindred.mir.Settings;
import com.kindred.mir.engine.Font;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;

public class MirButton extends MirControlWithDynamicImagesMouseEventDriven {

    private boolean isCenterText;
    protected MirLabel textLabel;

    /**
     * 没有文本
     * @param parent
     * @param renderer_id
     * @param normalImage
     * @param hoverImage
     * @param pressedImage
     * @param size
     * @param pos
     * @throws Exception
     */
    public MirButton(MirControl parent, long renderer_id, MirImage normalImage, MirImage hoverImage, MirImage pressedImage,
        Size size, Point pos) throws Exception
    {
        super(parent, renderer_id, new MirImage[]{ normalImage, hoverImage, pressedImage});
        if (normalImage==null) {
            setSize(size);
        }
        setLocation(pos);
        sound = SoundList.ButtonB;
    }

    public MirButton(MirControl parent, long renderer_id, MirImage normalImage, MirImage hoverImage, MirImage pressedImage,
        String text,Size labelSize, Font font, Color foreColor, Color backColor, int wraplength) throws Exception
    {
        super(parent, renderer_id, new MirImage[]{ normalImage, hoverImage, pressedImage});
        if (normalImage==null) {
            throw new Exception("Normal image can not be null!");
        }
        sound = SoundList.ButtonB;

        textLabel= new MirLabel(this, renderer_id, labelSize, new Point(0,0),
            font, text, foreColor, backColor, wraplength);
        textLabel.setIsByPassEvent(true);
        textLabel.setLocation(textLabel.Center());
    }

//    public Color getFontColor()
//    {
//        if (label != null && !label.getIsDisposed())
//            return label.getForeColor();
//        return Color.Empty;
//    }
//    public void setFontColor(Color fontColor)
//    {
//        if (label != null && !label.getIsDisposed())
//            label.setForeColor(fontColor);
//    }

    public MirImage getHoverImage()
    {
        return hoverImage;
    }
    public void setHoverImage(MirImage hoverImage)
    {
        if (this.hoverImage == hoverImage) {
            return;
        }
        this.hoverImage = hoverImage;
        onHoverImageChanged();
    }

    private void onHoverImageChanged()
    {
        if (hoverImageChanged != null) {
            hoverImageChanged.doAction(this, null);
        }
    }

//    @Override
//    public MirImage getMirImage()
//    {
//        if (!isEnabled)
//            return super.getMirImage();
//
//        if (pressedImage != null && ActiveControl == this && MouseControl == this)
//            return pressedImage;
//
//        if (hoverImage != null && MouseControl == this)
//            return hoverImage;
//
//        return super.getMirImage();
//    }

//    public boolean getIsCenterText()
//    {
//        return isCenterText;
//    }
//    public void setIsCenterText(boolean isCenterText)
//    {
//        this.isCenterText = isCenterText;
//        if (this.isCenterText) {
//            label.setSize(size);
//            //label.setDrawFormat(TextFormatFlags.HorizontalCenter | TextFormatFlags.VerticalCenter);
//        } else {
//            label.setIsAutoSize(true);
//        }
//    }

    public MirImage getPressedImage()
    {
        return pressedImage;
    }
    public void setPressedImage(MirImage pressedImage)
    {
        if (this.pressedImage == pressedImage) {
            return;
        }
        this.pressedImage = pressedImage;
        onPressedImageChanged();
    }

    private void onPressedImageChanged()
    {
        if (pressedImageChanged != null) {
            pressedImageChanged.doAction(this, null);
        }
    }

//    public void setText(String text)
//    {
//        if (label == null || label.getIsDisposed()) {
//            return;
//        }
//        label.setText(text);
//        label.setIsVisible(!Optional.ofNullable(text).orElse("").isEmpty());
//    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

//        if (label != null && !label.getIsDisposed()) {
//            label.dispose();
//        }
//        label = null;
    }

}
