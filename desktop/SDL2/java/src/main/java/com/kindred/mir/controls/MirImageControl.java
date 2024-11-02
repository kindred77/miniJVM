package com.kindred.mir.controls;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;

public class MirImageControl extends MirControl{

    private boolean isUseOffSet;
    private ControlCommonListener useOffSetChanged;

    private boolean isDrawImage;
    private ControlCommonListener drawImageChanged;

    protected int index;
    private ControlCommonListener indexChanged;

    private boolean isPixelDetect;
    public ControlCommonListener pixelDetectChanged;

    private MirLib mirLib;

    @Override
    public Point getDisplayLocation()
    {
        return isUseOffSet ? Point.add(super.getDisplayLocation(), mirLib.getOffset(index)) : super.getDisplayLocation();
    }

    public Point getDisplayLocationWithoutOffSet()
    {
        return super.getDisplayLocation();
    }

    public boolean getIsDrawImage()
    {
        return isDrawImage;
    }
    public void setIsDrawImage(boolean isDrawImage)
    {
        if (this.isDrawImage == isDrawImage)
            return;
        this.isDrawImage = isDrawImage;
        onDrawImageChanged();
    }

    private void onDrawImageChanged()
    {
        redraw();
        if (drawImageChanged != null)
            drawImageChanged.doAction(this, null);
    }

    public int getIndex()
    {
        return index;
    }
    public void setIndex(int index)
    {
        if (this.index == index)
            return;
        this.index = index;
        onIndexChanged();
    }

    protected void onIndexChanged()
    {
        onSizeChanged();
        if (indexChanged != null)
            indexChanged.doAction(this, null);
    }

    protected void setIsPixelDetect()
    {
        if (this.isPixelDetect == isPixelDetect)
            return;
        this.isPixelDetect = isPixelDetect;
        onPixelDetectChanged();
    }

    private void onPixelDetectChanged()
    {
        redraw();
        if (pixelDetectChanged != null)
            pixelDetectChanged.doAction(this, null);
    }

    public boolean getIsUseOffSet()
    {
        return isUseOffSet;
    }
    public void setIsUseOffSet(boolean isUseOffSet)
    {
        if (this.isUseOffSet == isUseOffSet)
            return;
        this.isUseOffSet = isUseOffSet;
        onUseOffSetChanged();
    }

    private void onUseOffSetChanged()
    {
        onLocationChanged();
        if (useOffSetChanged != null)
            useOffSetChanged.doAction(this, null);
    }

    @Override
    public Size getSize()
    {
        if (mirLib != null && index >= 0)
            return mirLib.getTrueSize(index);
        return super.getSize();
    }

    @Override
    public void setSize(Size size)
    {
        super.setSize(size);
    }

    @Override
    public Size getTrueSize()
    {
        if (mirLib != null && index >= 0)
            return mirLib.getTrueSize(index);
        return super.getTrueSize();
    }

    public MirImageControl()
    {
        isDrawImage = true;
        index = -1;
        foreColor = Color.White;
    }

    @Override
    protected void drawControl()
    {
        super.drawControl();

        if (isDrawImage && mirLib != null)
        {
//            if (isGrayScale) DXManager.SetGrayscale(1F, Color.White);
//            else if (isBlending) Library.DrawBlend(index, DisplayLocation, foreColor, false, blendingRate);
//            else Library.Draw(index, DisplayLocation, foreColor, false, opacity);
//            if (isGrayScale) DXManager.SetNormal(1F, Color.White);
        }
    }

    @Override
    public boolean isMouseOver(Point p)
    {
        return super.isMouseOver(p) && (!isPixelDetect || mirLib.visiblePixel(index, Point.subtract(p, getDisplayLocation()),true) || isMoving);
    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

        if (!disposing) return;

        drawImageChanged = null;
        isDrawImage = false;

        indexChanged = null;
        index = 0;

        //LibraryChanged = null;
        //Library = null;

        pixelDetectChanged = null;
        isPixelDetect = false;

        useOffSetChanged = null;
        isUseOffSet = false;
    }

}
