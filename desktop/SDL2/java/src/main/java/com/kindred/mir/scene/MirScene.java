package com.kindred.mir.scene;

import com.kindred.mir.Env;
import com.kindred.mir.Settings;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlCanBeDrawn;
import com.kindred.mir.scene.game.GameScene;
import com.kindred.mir.scene.charsel.CharSelScene;
import com.kindred.mir.scene.login.LoginScene;
import com.kindred.mir.util.Size;

public abstract class MirScene extends MirControlCanBeDrawn {

    public static class MirSceneData {

    }

    public static class SceneEnumType {
        public static int None = 0;
        public static int Login=1;
        public static int CharSel=2;
        public static int Game=3;
    }

    //private static MouseButtons mouseButtons;
    protected int sceneType=SceneEnumType.None;
    private static long lastClickTime;
    private static MirControl clickedControl;
    private long window_id;
    //private long renderer_id;
    protected MirSceneData sceneData;

    protected MirScene(MirControl parent, long window_id, long renderer_id, int sceneType,MirSceneData sceneData)
    {
        super(parent,renderer_id);
        //isDrawControlTexture = true;
        //backColor = Color.Black;
        size = new Size(Settings.ScreenWidth, Settings.ScreenHeight);
        this.window_id=window_id;
        //this.renderer_id=renderer_id;
        this.sceneType=sceneType;
        this.sceneData=sceneData;
    }

    public final int getSceneType() {
        return this.sceneType;
    }

    public final static void SwitchToScene(MirScene scene) {
        if (Env.ActiveScene==scene) {
            return;
        }
        if (Env.ActiveScene!=null) {
            Env.ActiveScene.setIsVisible(false);
        }
        Env.ActiveScene=scene;
        Env.ActiveScene.setIsVisible(true);
    }

    public final static MirScene PrepareNextScene(long window_id, long renderer_id,MirSceneData sceneData) throws Exception{
        if (null==Env.ActiveScene) {
            LoginScene loginScene=new LoginScene(null,window_id,renderer_id,sceneData);
            return loginScene;
        } else if (Env.ActiveScene.sceneType == SceneEnumType.Login) {
            CharSelScene charSelScene=new CharSelScene(null,window_id,renderer_id, sceneData);
            return charSelScene;
        } else if (Env.ActiveScene.sceneType == SceneEnumType.CharSel) {
            GameScene gameScene=new GameScene(null,window_id,renderer_id, sceneData);
            return gameScene;
        } else {
            throw new Exception("No scene after scene "+Env.ActiveScene.sceneType);
        }
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

        //mouseButtons = 0;
        lastClickTime = 0;
        clickedControl = null;
    }
}
