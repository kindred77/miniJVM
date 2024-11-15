package com.kindred.mir.controls;

import com.kindred.mir.engine.MirTexture;

public class MirControlWithTexture extends MirControl {

    protected MirTexture controlTexture;
    protected boolean isDrawControlTexture=true;

    public MirControlWithTexture(MirControl parent, long renderer_id) {
        super(parent);
    }

    public boolean getIsDrawControlTexture()
    {
        return isDrawControlTexture;
    }
    public void setIsDrawControlTexture(boolean isDrawControlTexture)
    {
        if (this.isDrawControlTexture == isDrawControlTexture)
            return;
        this.isDrawControlTexture = isDrawControlTexture;
        redraw();
    }

    /*
    有任何影响texture渲染变动的操作都要调用此方法,
    texture有变动则返回true
     */
    protected boolean updateTexture()
    {
        if (controlTexture != null && !controlTexture.getIsDisposed() && !controlTexture.getSize().equals(size)) {
            controlTexture.dispose();
            return false;
        }

        //controlTexture = new MirTexture(size.getWidth(), size.getHeight(), SDL_PixelFormatEnum.SDL_PIXELFORMAT_ARGB8888, getBackColor());
        //isTextureValid = true;
        return true;
    }

    @Override
    protected boolean drawControl(long renderer_id)
    {
        if (!isDrawControlTexture)
            return false;

        //if (!isTextureValid)
        //    updateTexture();

        if (controlTexture == null || controlTexture.getIsDisposed())
            return false;

        //float oldOpacity = DXManager.Opacity;

        //DXManager.SetOpacity(opacity);
        //DXManager.Sprite.Draw2D(controlTexture, Point.Empty, 0F, DisplayLocation, Color.White);
        //DXManager.SetOpacity(oldOpacity);

        //cleanTime = CMain.Time + Settings.CleanDelay;

        return true;
    }

    protected void controlTexture_Disposing()
    {
        controlTexture = null;
        isTextureValid = false;
        //textureSize = Size.Empty;

        //DXManager.ControlList.Remove(this);
    }
    private void disposeTexture()
    {
        if (controlTexture == null || controlTexture.getIsDisposed()) return;
        controlTexture.dispose();
    }

    @Override
    protected void dispose(boolean isDisposing)
    {
        super.dispose(isDisposing);
        isDrawControlTexture = false;
        if (controlTexture != null && !controlTexture.getIsDisposed())
            controlTexture.dispose();
        controlTexture = null;
    }
}
