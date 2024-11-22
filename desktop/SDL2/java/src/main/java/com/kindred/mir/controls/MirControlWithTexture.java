package com.kindred.mir.controls;

import com.kindred.mir.Settings;
import com.kindred.mir.constcode.MirBlendMode;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.engine.MirTexture;
import com.kindred.mir.util.*;
import com.kindred.sdl.constcode.SDLBlendMode;

import java.util.List;

/*
带有texture的控件，需要画图的
 */
public class MirControlWithTexture extends MirControl {

    protected MirTexture controlTexture;
    //protected boolean isDrawControlTexture=true;
    protected long renderer_id;

    private Rectangle borderRectangle=Rectangle.Empty;
    private boolean isBorder=false;
    private Vector2[] borderInfo;
    private ControlCommonListener onBorderChanged;

    private Color borderColor=Color.Red;
    private ControlCommonListener onBorderColorChanged;

    protected boolean isGrayScale;
    protected boolean isBlending;
    protected float blendingRate;
    private MirBlendMode.BlendMode blendMode;

    protected Color backColor = Color.Black;
    private ControlCommonListener onBackColorChanged;

    protected Color foreColor = Color.White;
    private ControlCommonListener onForeColorChanged;

    protected ControlCommonListener onBeforeDraw , onAfterDraw;

    public MirControlWithTexture(MirControl parent, long renderer_id) {
        super(parent);
        this.renderer_id=renderer_id;
        isDrawControlTexture=true;
        controlTexture=new MirTexture();
    }

    public Point getDisplayLocation() {
        if (parent!=null && parent instanceof MirControlWithTexture) {
            MirControlWithTexture controlWithTexture = (MirControlWithTexture)parent;
            return Point.add(controlWithTexture.getDisplayLocation(), getLocation());
        }
        return getLocation();
    }

    public final Rectangle getDisplayRectangle()
    {
        return new Rectangle(getDisplayLocation(), getSize());
    }


//    public Size getTrueSize()
//    {
//        return size;
//    }

    /*
    有任何影响texture渲染变动的操作都要调用此方法,
    texture有变动则返回true
     */
    protected boolean updateTexture(long surface_id)
    {
//        if (controlTexture != null && !controlTexture.getIsDisposed() && !controlTexture.getSize().equals(size)) {
//            controlTexture.dispose();
//            return false;
//        }
        controlTexture.update(renderer_id, surface_id);

        //TODO 在surface上画边框，再render
        if(isBorder)
        {
            updateBorderInfo();
        }

        return true;
    }

    /*
    只有MirControlWithTexture才能有border
     */
    @Override
    protected boolean drawControl()
    {
        beforeDrawControl();

        if (/*isDrawImage && */controlTexture != null && controlTexture.getIsValid()) {
            if (isBlending) {
                MirJNI.SDL_SetTextureBlendMode(controlTexture.getTexture(), SDLBlendMode.SDL_BLENDMODE_BLEND);
            }

            Rectangle dstRect = getDisplayRectangle();

            int[] rct = {dstRect.getX(), dstRect.getY(), dstRect.getWidth(), dstRect.getHeight()};
            MirJNI.SDL_RenderCopy(renderer_id, controlTexture.getTexture(), null, rct);
        }

        if (isBorder) {
            drawBorder();
        }

        afterDrawControl();

        return true;
    }

    protected final void beforeDrawControl()
    {
        if (onBeforeDraw != null) {
            onBeforeDraw.doAction(this, null);
        }
    }

    protected final void drawBorder()
    {
        if (borderInfo == null || borderInfo.length == 0 || borderInfo.length % 2 != 0) {
            return;
        }
        int[] rgba = MirJNI.SDL_GetRenderDrawColor(renderer_id);
        MirJNI.SDL_SetRenderDrawColor(renderer_id, borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), borderColor.getAlpha());
        for (int i = 0; i<borderInfo.length-1; i+=2) {
            MirJNI.SDL_RenderDrawLine(renderer_id, borderInfo[i].getX(), borderInfo[i].getY(), borderInfo[i+1].getX(), borderInfo[i+1].getY());
        }
        //如果没有拿到原来的颜色，则设置成黑色
        if(rgba!=null && rgba.length==4) {
            MirJNI.SDL_SetRenderDrawColor(renderer_id, rgba[0], rgba[1], rgba[2], rgba[3]);
        } else {
            MirJNI.SDL_SetRenderDrawColor(renderer_id, Color.Black.getRed(), Color.Black.getGreen(), Color.Black.getBlue(), Color.Black.getAlpha());
        }
    }
    protected final void afterDrawControl()
    {
        if (onAfterDraw != null) {
            onAfterDraw.doAction(this, null);
        }
    }

    protected final void updateBorderInfo()
    {
        if (size.equals(Size.Empty)) {
            borderInfo=null;
            borderRectangle=null;
            return;
        }

        Rectangle displayRectangle=getDisplayRectangle();
        if (!borderRectangle.equals(displayRectangle)) {
            borderInfo = new Vector2[]{
                    new Vector2(displayRectangle.getLeft() - 1, displayRectangle.getTop() - 1),
                    new Vector2(displayRectangle.getRight(), displayRectangle.getTop() - 1),

                    new Vector2(displayRectangle.getLeft() - 1, displayRectangle.getTop() - 1),
                    new Vector2(displayRectangle.getLeft() - 1, displayRectangle.getBottom()),

                    new Vector2(displayRectangle.getLeft() - 1, displayRectangle.getBottom()),
                    new Vector2(displayRectangle.getRight(), displayRectangle.getBottom()),

                    new Vector2(displayRectangle.getRight(), displayRectangle.getTop() - 1),
                    new Vector2(displayRectangle.getRight(), displayRectangle.getBottom())
            };

            borderRectangle = displayRectangle;
        }
    }

    public final boolean getIsBorder()
    {
        return isBorder;
    }

    public final void setIsBorder(boolean isBorder)
    {
        if (this.isBorder == isBorder) {
            return;
        }
        this.isBorder = isBorder;

        onBorderChanged();
    }


    protected final void onBorderChanged()
    {
        updateBorderInfo();
        if (onBorderChanged != null) {
            onBorderChanged.doAction(this, null);
        }
    }

    public final Color getBorderColor()
    {
        return borderColor;
    }

    public final void setBorderColor(Color borderColor)
    {
        if (this.borderColor.equals(borderColor)) {
            return;
        }
        this.borderColor = borderColor;
        onBorderColorChanged();
    }

    protected final void onBorderColorChanged()
    {
        updateBorderInfo();
        if (onBorderColorChanged != null) {
            onBorderColorChanged.doAction(this, null);
        }
    }

    public final boolean getIsGrayScale()
    {
        return isGrayScale;
    }
    public final void setIsGrayScale(boolean isGrayScale)
    {
        this.isGrayScale=isGrayScale;
    }
    public final boolean getIsBlending()
    {
        return isBlending;
    }
    public final void setIsBlending(boolean isBlending)
    {
        this.isBlending=isBlending;
    }
    public final float getBlendingRate()
    {
        return blendingRate;
    }
    public final void setBlendingRate(float blendingRate)
    {
        this.blendingRate=blendingRate;
    }
    public final MirBlendMode.BlendMode getBlendMode()
    {
        return blendMode;
    }

    public final void setBlendMode(MirBlendMode.BlendMode blendMode)
    {
        this.blendMode=blendMode;
    }


    public final Color getBackColor()
    {
        return backColor;
    }

    public final void setBackColor(Color backColor)
    {
        if (this.backColor == backColor) {
            return;
        }
        this.backColor = backColor;
        onBackColorChanged();
    }

    protected final void onBackColorChanged()
    {
        //isTextureValid = false;
        //redraw();
        if (onBackColorChanged != null) {
            onBackColorChanged.doAction(this, null);
        }
    }

    public final Color getForeColor()
    {
        return foreColor;
    }
    public final void setForeColor(Color foreColor)
    {
        if (this.foreColor == foreColor) {
            return;
        }
        this.foreColor = foreColor;
        onForeColorChanged();
    }

    protected final void onForeColorChanged()
    {
        //isTextureValid = false;
        if (onForeColorChanged != null) {
            onForeColorChanged.doAction(this, null);
        }
    }

    protected final void controlTexture_Disposing()
    {
        controlTexture = null;
        //isTextureValid = false;
        //textureSize = Size.Empty;

        //DXManager.ControlList.Remove(this);
    }
    private void disposeTexture()
    {
        if (controlTexture == null || !controlTexture.getIsValid()) {
            return;
        }
        controlTexture.dispose();
    }

    @Override
    protected void dispose(boolean isDisposing)
    {
        super.dispose(isDisposing);
        isDrawControlTexture = false;
        if (controlTexture != null && controlTexture.getIsValid()) {
            controlTexture.dispose();
        }
        controlTexture = null;

        onBorderChanged = null;
        isBorder = false;
        borderRectangle = Rectangle.Empty;
        borderInfo = null;

        onBorderColorChanged = null;
        borderColor = Color.Empty;
    }
}
