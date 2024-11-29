package com.kindred.mir.controls;

import com.kindred.mir.engine.SoundList;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.util.Size;
import java.util.Optional;

public class MirButton extends MirControlWithDynamicImagesMouseEventDriven {

    protected MirLabel label;



    private boolean isCenterText;

    public MirButton(MirControl parent, long renderer_id, MirImage normalImage, MirImage hoverImage, MirImage pressedImage) throws Exception
    {
        super(parent, renderer_id, new MirImage[]{ normalImage, hoverImage, pressedImage});

        sound = SoundList.ButtonB;

        label = new MirLabel("",this, renderer_id, new Size(20,10));
        label.setIsNotControl(true);

        onSizeChanged = (control, obj) -> {
            if (label != null && !label.getIsDisposed()) {
                label.setSize(size);
            }
        };
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

    public boolean getIsCenterText()
    {
        return isCenterText;
    }
    public void setIsCenterText(boolean isCenterText)
    {
        this.isCenterText = isCenterText;
        if (this.isCenterText) {
            label.setSize(size);
            //label.setDrawFormat(TextFormatFlags.HorizontalCenter | TextFormatFlags.VerticalCenter);
        } else {
            label.setIsAutoSize(true);
        }
    }

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

    public void setText(String text)
    {
        if (label == null || label.getIsDisposed()) {
            return;
        }
        label.setText(text);
        label.setIsVisible(!Optional.ofNullable(text).orElse("").isEmpty());
    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

        if (label != null && !label.getIsDisposed()) {
            label.dispose();
        }
        label = null;
    }

}
