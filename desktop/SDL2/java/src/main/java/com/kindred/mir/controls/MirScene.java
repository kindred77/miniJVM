package com.kindred.mir.controls;

import com.kindred.mir.Settings;
import com.kindred.mir.util.Size;

public abstract class MirScene extends MirControlCanBeDrawn {

    public static MirScene ActiveScene = null;

    //private static MouseButtons mouseButtons;
    private static long lastClickTime;
    private static MirControl clickedControl;
    private long window_id;
    private long renderer_id;

    protected MirScene(MirControl parent, long window_id, long renderer_id)
    {
        super(parent,renderer_id);
        //isDrawControlTexture = true;
        //backColor = Color.Black;
        size = new Size(Settings.ScreenWidth, Settings.ScreenHeight);
        this.window_id=window_id;
        this.renderer_id=renderer_id;
    }

    public final long getWindow() {
        return this.window_id;
    }

    public final long getRenderer() {
        return this.renderer_id;
    }

//
//    @Override
//    public void onMouseWheel(MouseEventArgs e)
//    {
//        if (!getIsEnabled())
//            return;
//
//        if (MouseControl != null && MouseControl != this)
//            MouseControl.onMouseWheel(e);
//        else
//            super.onMouseWheel(e);
//    }
//
//
//    @Override
//    public void onMouseClick(MouseEventArgs e)
//    {
//        if (!getIsEnabled())
//            return;
//        if (mouseButtons == e.Button)
//        {
//            if (lastClickTime + SystemInformation.DoubleClickTime >= CMain.Time)
//            {
//                onMouseDoubleClick(e);
//                return;
//            }
//        }
//        else
//            lastClickTime = 0;
//
//        if (ActiveControl != null && ActiveControl.isMouseOver(CMain.MPoint) && ActiveControl != this)
//            ActiveControl.onMouseClick(e);
//        else
//            super.onMouseClick(e);
//
//        clickedControl = ActiveControl;
//
//        lastClickTime = CMain.Time;
//        mouseButtons = e.Button;
//    }
//
//    @Override
//    public void onMouseDoubleClick(MouseEventArgs e)
//    {
//        if (!getIsEnabled())
//            return;
//        lastClickTime = 0;
//        mouseButtons = MouseButtons.None;
//
//        if (ActiveControl != null && ActiveControl.isMouseOver(CMain.MPoint) && ActiveControl != this)
//        {
//            if (ActiveControl == clickedControl)
//                ActiveControl.onMouseDoubleClick(e);
//            else
//                ActiveControl.onMouseClick(e);
//        }
//        else
//        {
//            if (ActiveControl == clickedControl)
//                super.onMouseDoubleClick(e);
//            else
//                super.onMouseClick(e);
//        }
//    }

    public abstract void process();

    @Override
    protected void dispose(boolean disposing)
    {

        super.dispose(disposing);

        if (!disposing) {
            return;
        }

        if (ActiveScene == this) {
            ActiveScene = null;
        }

        //mouseButtons = 0;
        lastClickTime = 0;
        clickedControl = null;
    }
}
