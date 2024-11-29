package com.kindred.mir.controls;

import com.kindred.mir.libs.MirImage;
import com.kindred.mir.util.Point;

public class MirCheckBox extends MirButton {

    private MirImage tickedImage;
    private MirImage unTickedImage;
    private boolean isChecked;

    public MirImage getTickedImage()
    {
        return tickedImage;
    }
    public void setTickedImage(MirImage tickedImage)
    {
        this.tickedImage=tickedImage;
    }

    public MirImage getUnTickedImage()
    {
        return unTickedImage;
    }
    public void setUnTickedImage(MirImage unTickedImage)
    {
        this.unTickedImage=unTickedImage;
    }

    public boolean getIsChecked()
    {
        return isChecked;
    }
    public void setIsChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
        //setImage(isChecked ? tickedImage : unTickedImage);
        //redraw();
    }

    public MirCheckBox(MirControl parent, long renderer_id, MirImage unTickedImage) throws Exception
    {
        super(parent, renderer_id, unTickedImage, null, null);
        this.tickedImage = null;
        this.unTickedImage = unTickedImage;
        this.onMouseLeftClick = (control, argObj) -> {
            isChecked = !isChecked;
//            if (isChecked) setImage(tickedImage);
//            else setImage(unTickedImage);
//            redraw();
        };

        label.setIsAutoSize(true);
        label.setLocation(new Point(15, -2));
    }
}
