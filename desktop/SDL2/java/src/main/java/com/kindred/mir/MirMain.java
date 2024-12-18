
package com.kindred.mir;

import static com.kindred.sdl.constcode.SDLWindowFlags.SDL_WINDOW_MINIMIZED;

import com.kindred.mir.controls.events.CommonEvent;
import com.kindred.mir.controls.events.CommonEvent.EventEnum;
import com.kindred.mir.scene.charsel.CharSelScene;
import com.kindred.mir.util.ExecutionService;
import com.kindred.mir.scene.MirScene;
import com.kindred.mir.engine.MirJNI;

import com.kindred.mir.scene.game.GameScene;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Util;
import com.kindred.sdl.constcode.*;


public class MirMain {

    //public static Point MPoint;
    public static long Time = 0L;

    static boolean shouldRun = true;

    private static void updateEnviroment()
    {
        if (MirScene.ActiveScene != null) {
            MirScene.ActiveScene.process();
        }

//        for (int i = 0; i < MirAnimatedControl.animations.size(); i++) {
//            MirAnimatedControl.animations.get(i).updateOffSet();
//        }

//        for (int i = 0; i < MirAnimatedButton.animations.size(); i++) {
//            MirAnimatedButton.animations.get(i).updateOffSet();
//        }
    }

    private static void updateTime()
    {
        Time = MirJNI.SDL_GetTicks();
    }

    public static void mainMouseMove(Point pos)
    {
        //if (Settings.IsFullScreen)
        //    Cursor.Clip = new Rectangle(0, 0, Settings.ScreenWidth, Settings.ScreenHeight);

        //MPoint = pos;

        try {
            if (MirScene.ActiveScene != null) {
                MirScene.ActiveScene.onCommonEvent(new CommonEvent(EventEnum.MouseMove,pos));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void mainMouseDown(Point pos, int type)
    {
        if (type == SDL_Button.SDL_BUTTON_RIGHT && (GameScene.SelectedCell != null || GameScene.PickedUpGold)) {
            GameScene.SelectedCell = null;
            GameScene.PickedUpGold = false;
            return;
        }

        try {
            if (MirScene.ActiveScene != null) {
                if (type == SDL_Button.SDL_BUTTON_LEFT) {
                    MirScene.ActiveScene.onCommonEvent(new CommonEvent(EventEnum.MouseLeftDown,pos));
                } else if(type == SDL_Button.SDL_BUTTON_RIGHT) {
                    MirScene.ActiveScene.onCommonEvent(new CommonEvent(EventEnum.MouseRightDown,pos));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void mainMouseUp(Point pos, int type) {

//        MapControl.MapButtons &= ~e.Button;
//
//        if (!GlobalUtil.EnumHasFlag((int)MapControl.MapButtons, (int)MouseButtons.Right))
//            GameScene.CanRun = false;

        try {
            if (MirScene.ActiveScene != null) {
                if (type == SDL_Button.SDL_BUTTON_LEFT) {
                    MirScene.ActiveScene.onCommonEvent(new CommonEvent(EventEnum.MouseLeftUp,pos));
                } else if(type == SDL_Button.SDL_BUTTON_RIGHT) {
                    MirScene.ActiveScene.onCommonEvent(new CommonEvent(EventEnum.MouseRightUp,pos));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void renderEnvironment(long renderer_id) {
        if (MirScene.ActiveScene != null) {
            MirScene.ActiveScene.show();
            MirJNI.SDL_RenderPresent(renderer_id);
        }
    }

    public static void exit() {
        shouldRun=false;
    }

    public static void main(String args[]) throws Exception{
        try {
            Env.BackGroundExeService=new ExecutionService(Settings.BACKGROUND_WORKER_THREADS_CNT);
            int result = MirJNI.SDL_Init(SdlSubSystemConst.SDL_INIT_EVERYTHING);
            if (result != 0) {
                throw new IllegalStateException("Unable to initialize SDL library (Error code " + result + "): " + MirJNI.SDL_GetError());
            }
            result=MirJNI.SDL_TTF_Init();
            if (result != 0) {
                throw new IllegalStateException("Unable to initialize SDL ttf (Error code " + result + "): " + MirJNI.SDL_GetError());
            }
            if (!MirJNI.SDL_SetHint(Util.toCstyleBytes(SDL_Hints.SDL_HINT_IME_SHOW_UI), Util.toCstyleBytes("1"))) {
                throw new IllegalStateException("Unable to set hint: " + MirJNI.SDL_GetError());
            }
            // Create and init the window
            long win_id = MirJNI.SDL_CreateWindow(Util.toCstyleBytes("窗口-kindred"),
                    SdlVideoConst.SDL_WINDOWPOS_CENTERED,
                    SdlVideoConst.SDL_WINDOWPOS_CENTERED,
                    Settings.ScreenWidth, Settings.ScreenHeight,
                    //SDLWindowFlags.SDL_WINDOW_SHOWN | SDLWindowFlags.SDL_WINDOW_RESIZABLE);
                    SDLWindowFlags.SDL_WINDOW_OPENGL | SDLWindowFlags.SDL_WINDOW_RESIZABLE | SDLWindowFlags.SDL_WINDOW_ALLOW_HIGHDPI);
            if (win_id == 0) {
                throw new IllegalStateException("Unable to create SDL window: " + MirJNI.SDL_GetError());
            }

            int win_pf = MirJNI.SDL_GetWindowPixelFormat(win_id);
            System.out.println("----windows---pixelformat: "+ SDL_PixelFormatEnum.toString(win_pf));

            long renderer_id = MirJNI.SDL_CreateRenderer(win_id, -1, SDLRendererFlags.SDL_RENDERER_PRESENTVSYNC|SDLRendererFlags.SDL_RENDERER_ACCELERATED);
            if (renderer_id == 0) {
                throw new IllegalStateException("Unable to create SDL renderer: " + MirJNI.SDL_GetError());
            }

            Settings.makeSureSDLFontsInited();

            long event_id = MirJNI.SDL_CreateEvent();

            MirScene.SwitchToScene(MirScene.PrepareNextScene(win_id, renderer_id,null));
            //TODO for test
            //MirScene.SwitchToScene(new CharSelScene(null,win_id,renderer_id,new CharSelScene.CharSelSceneData("")));

            while (shouldRun) {
                updateTime();
                updateEnviroment();
                while (MirJNI.SDL_PollEvent(event_id) != 0) {
                    if (Settings.IsImguiUsed.get()) {
                        MirJNI.ImGui_SDL2_ProcessEvent(event_id);
                    }
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
                            int win_event=MirJNI.SDL_GetEventWindowEvent(event_id);
                            if (win_event==SDL_WindowEventID.SDL_WINDOWEVENT_ENTER) {
                                System.out.println("Mouse enter.");
                            } else if (win_event==SDL_WindowEventID.SDL_WINDOWEVENT_LEAVE) {
                                System.out.println("Mouse leave.");
                            } else {
                                System.out.println("Window event " + MirJNI.SDL_GetEventWindowEvent(event_id));
                            }
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
                            //System.out.println("mouse move: x: " + pos_motion[0]+", y: "+pos_motion[1]);
                            break;
                        case SDLEventType.SDL_MOUSEWHEEL:
                            int[] pos_wheel = MirJNI.SDL_GetEventMouseWheelPos(event_id);
                            System.out.println("mouse wheel: x: " + pos_wheel[0]+", y: "+pos_wheel[1]);
                            break;
                        default:
                            break;
                    }
                }

                if ((MirJNI.SDL_GetWindowFlags(win_id) & SDL_WINDOW_MINIMIZED)!=0)
                {
                    MirJNI.SDL_Delay(10);
                    continue;
                }

                renderEnvironment(renderer_id);

            }

            if (Settings.IsImguiUsed.get()) {
                MirJNI.ImGui_Destroy();
            }

            MirJNI.SDL_DestroyRenderer(renderer_id);
            MirJNI.SDL_DestroyWindow(win_id);
            MirJNI.SDL_TTF_Quit();
            MirJNI.SDL_Quit();
            Env.BackGroundExeService.shutdown();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}