
package com.kindred.mir;

import java.io.UnsupportedEncodingException;

import com.kindred.mir.controls.MirAnimatedButton;
import com.kindred.mir.controls.MirAnimatedControl;
import com.kindred.mir.controls.MirScene;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;

import com.kindred.mir.scene.GameScene;
import com.kindred.mir.test.Test;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Util;
import com.kindred.sdl.constcode.*;


public class MirMain {

    public static Point MPoint;
    public static long Time = 0l;

    private static void updateEnviroment()
    {
        if (MirScene.ActiveScene != null)
            MirScene.ActiveScene.process();

        for (int i = 0; i < MirAnimatedControl.animations.size(); i++)
            MirAnimatedControl.animations.get(i).updateOffSet();

        for (int i = 0; i < MirAnimatedButton.animations.size(); i++)
            MirAnimatedButton.animations.get(i).updateOffSet();
    }

    private static void updateTime()
    {
        Time = MirJNI.SDL_GetTicks();
    }

    public static void mainMouseMove(Point pos)
    {
        //if (Settings.IsFullScreen)
        //    Cursor.Clip = new Rectangle(0, 0, Settings.ScreenWidth, Settings.ScreenHeight);

        MPoint = pos;

        try
        {
            if (MirScene.ActiveScene != null)
                MirScene.ActiveScene.onMouseMove(pos);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void mainMouseDown(Point pos, int type)
    {
        if (type == SDL_Button.SDL_BUTTON_RIGHT && (GameScene.SelectedCell != null || GameScene.PickedUpGold))
        {
            GameScene.SelectedCell = null;
            GameScene.PickedUpGold = false;
            return;
        }

        try
        {
            if (MirScene.ActiveScene != null)
                MirScene.ActiveScene.onMouseDown(pos);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void mainMouseUp(Point pos, int type)
    {

//        MapControl.MapButtons &= ~e.Button;
//
//        if (!GlobalUtil.EnumHasFlag((int)MapControl.MapButtons, (int)MouseButtons.Right))
//            GameScene.CanRun = false;

        try
        {
            if (MirScene.ActiveScene != null)
                MirScene.ActiveScene.onMouseUp(pos);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static void renderEnvironment(long renderer_id)
    {
        if (MirScene.ActiveScene != null)
        {
            MirScene.ActiveScene.draw(renderer_id);
            MirJNI.SDL_RenderPresent(renderer_id);
        }
    }

    public static void main(String args[])
    {
        try
        {
            int result = MirJNI.SDL_Init(SdlSubSystemConst.SDL_INIT_EVERYTHING);
            if (result != 0) {
                throw new IllegalStateException("Unable to initialize SDL library (Error code " + result + "): " + MirJNI.SDL_GetError());
            }
            if (!MirJNI.SDL_SetHint(Util.toCstyleBytes(SDL_Hints.SDL_HINT_IME_SHOW_UI), Util.toCstyleBytes("1")))
            {
                throw new IllegalStateException("Unable to set hint: " + MirJNI.SDL_GetError());
            }
            // Create and init the window
            long win_id = MirJNI.SDL_CreateWindow(Util.toCstyleBytes("窗口-kindred"),
                    SdlVideoConst.SDL_WINDOWPOS_CENTERED,
                    SdlVideoConst.SDL_WINDOWPOS_CENTERED,
                    800, 600,
                    //SDLWindowFlags.SDL_WINDOW_SHOWN | SDLWindowFlags.SDL_WINDOW_RESIZABLE);
                    SDLWindowFlags.SDL_WINDOW_OPENGL | SDLWindowFlags.SDL_WINDOW_RESIZABLE | SDLWindowFlags.SDL_WINDOW_ALLOW_HIGHDPI);
            if (win_id == 0) {
                throw new IllegalStateException("Unable to create SDL window: " + MirJNI.SDL_GetError());
            }

            int win_pf = MirJNI.SDL_GetWindowPixelFormat(win_id);
            System.out.println("----windows---pixelformat: "+ SDL_PixelFormatEnum.toString(win_pf));

            long renderer_id = MirJNI.SDL_CreateRenderer(win_id, -1, SDLRendererFlags.SDL_RENDERER_ACCELERATED);
            if (renderer_id == 0) {
                throw new IllegalStateException("Unable to create SDL renderer: " + MirJNI.SDL_GetError());
            }

            //init imgui
            MirJNI.ImGui_SDL2_Init(win_id, renderer_id);

            boolean shouldRun = true;
            long event_id = MirJNI.SDL_CreateEvent();

            while (shouldRun) {
                updateTime();
                updateEnviroment();
                while (MirJNI.SDL_PollEvent(event_id) != 0) {
                    MirJNI.ImGui_SDL2_ProcessEvent(event_id);
                    switch (MirJNI.SDL_GetEventType(event_id)) {
                        case SDLEventType.SDL_QUIT:
                            shouldRun = false;
                            break;
                        case SDLEventType.SDL_KEYDOWN:
                            if (MirJNI.SDL_GetKeyEventKeySym(event_id) == SDLKeyCode.SDLK_SPACE) {
                                System.out.println("SPACE pressed");
                            }
                            break;
                        case SDLEventType.SDL_WINDOWEVENT:
                            System.out.println("Window event " + MirJNI.SDL_GetEventWindowEvent(event_id));
                            break;
                        case SDLEventType.SDL_MOUSEBUTTONDOWN:
                            int[] pos_down = MirJNI.SDL_GetEventMouseButtonPos(event_id);
                            System.out.println("mouse down: x: " + pos_down[0]+", y: "+pos_down[1]);
                            mainMouseDown(new Point(pos_down[0], pos_down[1]), MirJNI.SDL_GetEventMouseButtonButton(event_id));
                            break;
                        case SDLEventType.SDL_MOUSEBUTTONUP:
                            int[] pos_up = MirJNI.SDL_GetEventMouseButtonPos(event_id);
                            System.out.println("mouse up: x: " + pos_up[0]+", y: "+pos_up[1]);
                            mainMouseUp(new Point(pos_up[0], pos_up[1]), MirJNI.SDL_GetEventMouseButtonButton(event_id));
                            break;
                        case SDLEventType.SDL_MOUSEMOTION:
                            int[] pos_motion = MirJNI.SDL_GetEventMouseMotionPos(event_id);
                            mainMouseMove(new Point(pos_motion[0], pos_motion[1]));
                            System.out.println("mouse move: x: " + pos_motion[0]+", y: "+pos_motion[1]);
                            break;
                        case SDLEventType.SDL_MOUSEWHEEL:
                            int[] pos_wheel = MirJNI.SDL_GetEventMouseWheelPos(event_id);
                            System.out.println("mouse wheel: x: " + pos_wheel[0]+", y: "+pos_wheel[1]);
                            break;
                        default:
                            break;
                    }
                }

                renderEnvironment(renderer_id);

            }

            MirJNI.ImGui_Destroy();

            MirJNI.SDL_DestroyRenderer(renderer_id);
            MirJNI.SDL_DestroyWindow(win_id);
            MirJNI.SDL_Quit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}