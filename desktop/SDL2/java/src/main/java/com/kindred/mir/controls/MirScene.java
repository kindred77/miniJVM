package com.kindred.mir.controls;

import com.kindred.mir.util.Color;
import com.kindred.mir.util.Size;
import com.sun.scenario.Settings;

public abstract class MirScene extends MirControl {

    public static MirScene ActiveScene = null;

    private static MouseButtons mouseButtons;
    private static long lastClickTime;
    private static MirControl clickedControl;

    protected MirScene()
    {
        isDrawControlTexture = true;
        backColor = Color.Black;
        size = new Size(Settings.ScreenWidth, Settings.ScreenHeight);

    }

    @Override
    public void draw()
    {
        if (isDisposed || !isVisible)
            return;

        onBeforeShown();

        drawControl();

        if (CMain.DebugBaseLabel != null && !CMain.DebugBaseLabel.IsDisposed)
            CMain.DebugBaseLabel.Draw();

        if (CMain.HintBaseLabel != null && !CMain.HintBaseLabel.IsDisposed)
            CMain.HintBaseLabel.Draw();

        onShown();
    }

    @Override
    protected void createTexture()
    {
        if (controlTexture != null && !controlTexture.getIsDisposed() && size != textureSize)
            controlTexture.dispose();

        if (controlTexture == null || controlTexture.getIsDisposed())
        {
            DXManager.ControlList.Add(this);
            controlTexture = new Texture(DXManager.Device, Size.Width, Size.Height, 1, Usage.RenderTarget, Format.A8R8G8B8, Pool.Default);
            controlTexture.Disposing += ControlTexture_Disposing;
            textureSize = size;
        }
        Surface oldSurface = DXManager.CurrentSurface;
        Surface surface = ControlTexture.GetSurfaceLevel(0);
        DXManager.SetSurface(surface);


        DXManager.Device.Clear(ClearFlags.Target, BackColour, 0, 0);

        beforeDrawControl();
        drawChildren();
        afterDrawControl();

        DXManager.Sprite.Flush();


        DXManager.SetSurface(oldSurface);
        isTextureValid = true;
        surface.Dispose();

    }

    @Override
    public void onMouseDown(MouseEventArgs e)
    {
        if (!getIsEnabled())
            return;

        if (MouseControl != null && MouseControl != this)
            MouseControl.onMouseDown(e);
        else
            super.onMouseDown(e);
    }

    @Override
    public void onMouseUp(MouseEventArgs e)
    {
        if (!getIsEnabled())
            return;
        if (MouseControl != null && MouseControl != this)
            MouseControl.onMouseUp(e);
        else
            super.onMouseUp(e);
    }

    @Override
    public void onMouseMove(MouseEventArgs e)
    {
        if (!getIsEnabled())
            return;

        if (MouseControl != null && MouseControl != this && MouseControl.isMoving)
            MouseControl.onMouseMove(e);
        else
            super.onMouseMove(e);
    }

    @Override
    public void onMouseWheel(MouseEventArgs e)
    {
        if (!getIsEnabled())
            return;

        if (MouseControl != null && MouseControl != this)
            MouseControl.onMouseWheel(e);
        else
            super.onMouseWheel(e);
    }


    @Override
    public void onMouseClick(MouseEventArgs e)
    {
        if (!getIsEnabled())
            return;
        if (mouseButtons == e.Button)
        {
            if (lastClickTime + SystemInformation.DoubleClickTime >= CMain.Time)
            {
                onMouseDoubleClick(e);
                return;
            }
        }
        else
            lastClickTime = 0;

        if (ActiveControl != null && ActiveControl.isMouseOver(CMain.MPoint) && ActiveControl != this)
            ActiveControl.onMouseClick(e);
        else
            super.onMouseClick(e);

        clickedControl = ActiveControl;

        lastClickTime = CMain.Time;
        mouseButtons = e.Button;
    }

    @Override
    public void onMouseDoubleClick(MouseEventArgs e)
    {
        if (!getIsEnabled())
            return;
        lastClickTime = 0;
        mouseButtons = MouseButtons.None;

        if (ActiveControl != null && ActiveControl.isMouseOver(CMain.MPoint) && ActiveControl != this)
        {
            if (ActiveControl == clickedControl)
                ActiveControl.onMouseDoubleClick(e);
            else
                ActiveControl.onMouseClick(e);
        }
        else
        {
            if (ActiveControl == clickedControl)
                super.onMouseDoubleClick(e);
            else
                super.onMouseClick(e);
        }
    }

    @Override
    public void redraw()
    {
        isTextureValid = false;
    }

    public abstract void process();

    @Override
    protected void dispose(boolean disposing)
    {

        super.dispose(disposing);

        if (!disposing) return;

        if (ActiveScene == this) ActiveScene = null;

        mouseButtons = 0;
        lastClickTime = 0;
        clickedControl = null;
    }
}
