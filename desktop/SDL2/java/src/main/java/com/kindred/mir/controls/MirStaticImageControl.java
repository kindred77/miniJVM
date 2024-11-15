package com.kindred.mir.controls;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;
import com.kindred.sdl.constcode.SDLBlendMode;

public class MirStaticImageControl extends MirControl{

    private boolean isUseOffSet = true;
    private ControlCommonListener useOffSetChanged;

    private boolean isDrawImage = true;
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
        if (image == null)
        {
            System.out.println("MirStaticImageControl: No default image.");
        }
        isDrawImage = true;
        //index = -1;
        foreColor = Color.White;
        this.image = image;
    }

    @Override
    public Point getDisplayLocation()
    {
        if (isUseOffSet)
        {
            //没有默认图片的话，就没有offset
            return Point.add(super.getDisplayLocation(), image == null? new Point(0,0):image.getOffset());
        }
        else
        {
            return super.getDisplayLocation();
        }
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
            long texture_id = MirJNI.SDL_CreateTextureFromSurface(renderer_id, image.getSurface());
            if (isBlending)
            {
                MirJNI.SDL_SetTextureBlendMode(texture_id, SDLBlendMode.SDL_BLENDMODE_BLEND);
            }

            Point pos = getDisplayLocation();

            int[] dstRect = {pos.getX(), pos.getY(), image.getWidth(), image.getHeight()};
            MirJNI.SDL_RenderCopy(renderer_id, texture_id, null, dstRect);
        }
    }

    @Override
    public boolean isMouseOver(Point p)
    {
        if (image == null)
        {
            return super.isMouseOver(p) && (!isPixelDetect || isMoving);
        }
        else
        {
            return super.isMouseOver(p) && (!isPixelDetect || image.isVisiblePixel(Point.subtract(p, getDisplayLocation()),true) || isMoving);
        }
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
