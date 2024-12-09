package com.kindred.mir.controls;

import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.engine.MirTexture;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;
import com.kindred.sdl.constcode.SDLBlendMode;

/*
只有一个静态MirImage的控件，不会变化的，不会根鼠标事件，键盘事件，时间等变化的
 */
public class MirControlWithStaticImage extends MirControlWithTexture{

    private boolean isUseOffSet = true;
    private ControlCommonListener useOffSetChanged;

//    private boolean isDrawImage = true;
//    private ControlCommonListener drawImageChanged;

    //protected int index;
    //private ControlCommonListener indexChanged;

    private boolean isPixelDetect=false;
    public ControlCommonListener pixelDetectChanged;

    private MirImage image;
    public ControlCommonListener imageChanged;

    /*
    带有texture的控件需要使用到renderer
    注意: image可能为空
    本类要考虑image为空的情况
     */
    public MirControlWithStaticImage(MirControl parent, long renderer_id, MirImage image) throws Exception
    {
        super(parent, renderer_id);
        if (image == null)
        {
            throw new Exception("Image is null!");
        }
        //isDrawImage = true;
        //index = -1;
        foreColor = Color.White;
        this.image = image;
        if(this.image != null) {
            //controlTexture = new MirTexture(this.image, renderer_id);
            setSize(this.image.getTrueSize());
            //默认在正中
            setLocation(this.Center());
            updateTexture(this.image.getSurface(MirImage.ImageEffect.None));
            isPixelDetect=true;
        }
    }

    @Override
    public Point getDisplayLocation()
    {
        if (isUseOffSet && image != null) {
            return Point.add(super.getDisplayLocation(), image.getOffset());
        } else {
            return super.getDisplayLocation();
        }
    }

//    public boolean getIsDrawImage()
//    {
//        return isDrawImage;
//    }
//    public void setIsDrawImage(boolean isDrawImage)
//    {
//        if (this.isDrawImage == isDrawImage)
//            return;
//        this.isDrawImage = isDrawImage;
//        onDrawImageChanged();
//    }
//
//    private void onDrawImageChanged()
//    {
//        redraw();
//        if (drawImageChanged != null)
//            drawImageChanged.doAction(this, null);
//    }

    protected void setIsPixelDetect(boolean isPixelDetect)
    {
        if (this.isPixelDetect == isPixelDetect) {
            return;
        }
        this.isPixelDetect = isPixelDetect;
        onPixelDetectChanged();
    }

    private void onPixelDetectChanged()
    {
        //redraw();
        if (pixelDetectChanged != null) {
            pixelDetectChanged.doAction(this, null);
        }
    }

    public boolean getIsUseOffSet()
    {
        return isUseOffSet;
    }
    public void setIsUseOffSet(boolean isUseOffSet)
    {
        if (this.isUseOffSet == isUseOffSet) {
            return;
        }
        this.isUseOffSet = isUseOffSet;
        onUseOffSetChanged();
    }

    private void onUseOffSetChanged()
    {
        onLocationChanged();
        if (useOffSetChanged != null) {
            useOffSetChanged.doAction(this, null);
        }
    }

    public MirImage getImage()
    {
        return this.image;
    }

    public void setImage(MirImage image)
    {
        if (this.image == image) {
            return;
        }
        this.image = image;
        if (image!=null) {
            updateTexture(image.getSurface(MirImage.ImageEffect.None));
        }

        onImageChanged();
    }

    private void onImageChanged()
    {
        onSizeChanged();
        if (imageChanged != null) {
            imageChanged.doAction(this, null);
        }
    }

//    @Override
//    public Size getTrueSize()
//    {
//        if (image != null)
//            return image.getTrueSize();
//        return super.getTrueSize();
//    }

    @Override
    public boolean isMouseOver(Point p)
    {
        if (image == null)
        {
            return super.isMouseOver(p) && !isPixelDetect;
        }
        else
        {
            return super.isMouseOver(p) && (!isPixelDetect || image.isVisiblePixel(Point.subtract(p, getDisplayLocation()),true));
        }
    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

        if (!disposing) {
            return;
        }

//        drawImageChanged = null;
//        isDrawImage = false;

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
