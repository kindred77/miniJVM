package com.kindred.mir.controls;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;

public class MirStaticImageControl extends MirControl{

    private boolean isUseOffSet;
    private ControlCommonListener useOffSetChanged;

    private boolean isDrawImage;
    private ControlCommonListener drawImageChanged;

    //protected int index;
    //private ControlCommonListener indexChanged;

    private boolean isPixelDetect;
    public ControlCommonListener pixelDetectChanged;

    private MirImage image;
    public ControlCommonListener imageChanged;

    public MirStaticImageControl(MirControl parent, MirImage image)
    {
        super(parent);
        isDrawImage = true;
        //index = -1;
        foreColor = Color.White;
        setImage(image);
    }

    public MirStaticImageControl(MirControl parent, MirLib lib, int index)
    {
        super(parent);
        MirImage img = lib.GetMirImage(index);
        isDrawImage = true;
        //index = index;
        foreColor = Color.White;
        setImage(img);
    }

    @Override
    public Point getDisplayLocation()
    {
        return isUseOffSet ? Point.add(super.getDisplayLocation(), image.getOffset()) : super.getDisplayLocation();
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

    public MirImage getMirImage()
    {
        return image;
    }
    public void setMirImage(MirImage image)
    {
        if (this.image == image)
            return;
        this.image = image;
        onMirImageChanged();
    }

    protected void onMirImageChanged()
    {
        onSizeChanged();
        if (imageChanged != null)
            imageChanged.doAction(this, null);
    }

    protected void setIsPixelDetect(boolean isPixelDetect)
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

    public MirImage getImage()
    {
        return this.image;
    }

    public void setImage(MirImage image)
    {
        if (this.image == image)
            return;
        this.image = image;
        onImageChanged();
    }

    private void onImageChanged()
    {
        onSizeChanged();
        if (imageChanged != null)
            imageChanged.doAction(this, null);
    }

    @Override
    public Size getSize()
    {
        if (image != null)
            return image.getTrueSize();
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
        if (image != null)
            return image.getTrueSize();
        return super.getTrueSize();
    }

    @Override
    protected void drawControl(long renderer_id)
    {
        super.drawControl(renderer_id);

        if (isDrawImage && image != null)
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
        return super.isMouseOver(p) && (!isPixelDetect || image.isVisiblePixel(Point.subtract(p, getDisplayLocation()),true) || isMoving);
    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

        if (!disposing) return;

        drawImageChanged = null;
        isDrawImage = false;

        imageChanged = null;
        image = null;

        //LibraryChanged = null;
        //Library = null;

        pixelDetectChanged = null;
        isPixelDetect = false;

        useOffSetChanged = null;
        isUseOffSet = false;
    }

}
