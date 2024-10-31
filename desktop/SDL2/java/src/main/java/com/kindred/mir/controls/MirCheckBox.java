package com.kindred.mir.controls;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.util.Point;

public class MirCheckBox extends MirButton {

    private int tickedIndex;
    private int unTickedIndex;
    private boolean isChecked;

    public int getTickedIndex()
    {
        return tickedIndex;
    }
    public void setTickedIndex(int tickedIndex)
    {
        this.tickedIndex=tickedIndex;
    }

    public int getUnTickedIndex()
    {
        return unTickedIndex;
    }
    public void setUnTickedIndex(int unTickedIndex)
    {
        this.unTickedIndex=unTickedIndex;
    }

    public boolean getIsChecked()
    {
        return isChecked;
    }
    public void setIsChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
        index = isChecked ? tickedIndex : unTickedIndex;
        redraw();
    }

    public MirCheckBox()
    {
        super();
        tickedIndex = -1;
        unTickedIndex = -1;
        click = new ControlCommonListener() {
            @Override
            public void doAction(MirControl control, Object argObj) {
                isChecked = !isChecked;
                if (isChecked) index = tickedIndex;
                else index = unTickedIndex;
                redraw();
            }
        };

        label.setIsAutoSize(true);
        label.setLocation(new Point(15, -2));
    }
}
