
package com.kindred.mir;

import java.io.UnsupportedEncodingException;

import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;

import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.test.Test;
import com.kindred.mir.util.Util;
import com.kindred.sdl.constcode.*;


public class TestMain {

    public static void main(String args[])
    {
        //System.setProperty("microedition.encoding","utf-8");
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

            //test imgui
            MirJNI.ImGui_SDL2_Init(win_id, renderer_id);
            MirJNI.ImGui_InitFont(Util.toCstyleBytes("NotoEmoji+NotoSansCJKSC-Regular.ttf"), 10f);
            MirJNI.ImGui_InitBackColor(1.0f, 1.0f, 1.0f, 0.5f);
            MirJNI.ImGui_InitForeColor(1.0f, 0.0f, 0.0f, 0.5f);
            //---------------------

            MirLib mir_lib = MirLibFactory.getMirLib("../../desktop/SDL2/java/src/main/resource/mir_res/Prguse2_png.Lib");
            
            MirImage img = mir_lib.GetMirImage(1360);
            MirImage img2 = mir_lib.GetMirImage(1205);


            //effect
            //int ret = MirJNI.Mir_SurfaceInverse(img.getSurface());
            //int ret = MirJNI.Mir_SurfaceAlpha(img.getSurface(), 0.4f);
            //int ret = MirJNI.Mir_SurfaceBlackEffect(img.getSurface());
            //int ret = MirJNI.Mir_SurfaceWhiteEffect(img.getSurface());
            //int ret = MirJNI.Mir_SurfaceGreenEffect(img.getSurface());
            //int ret = MirJNI.Mir_SurfaceBlueEffect(img.getSurface());
            //int ret = MirJNI.Mir_SurfaceYellowEffect(img.getSurface());
            //int ret = MirJNI.Mir_SurfaceFuchsiaEffect(img.getSurface());
            //int ret = MirJNI.Mir_SurfaceBrightEffect(img.getSurface());
            //int ret = MirJNI.Mir_SurfaceGrayEffect(img.getSurface());
            int ret = MirJNI.Mir_SurfaceRedEffect(img.getSurface());
            //int ret = MirJNI.Mir_SurfaceToGray(img.getSurface());
            if (ret != 0) {
                throw new IllegalStateException("Unable to convert surface.");
            }


            //test texture
            //int ret = Mir_TextureInverse(img.getTexture());

            //blend
            //ret = MirJNI.Mir_SurfaceBlendNormal(img.getSurface(), img2.getSurface(), 50, 50, 0.5f);
            //ret = MirJNI.Mir_SurfaceBlendNormalTransparent(img.getSurface(), img2.getSurface(), 50, 50, 0.5f, 0, 0, 0);
            //ret = MirJNI.Mir_SurfaceBlendAdd(img.getSurface(), img2.getSurface(), 50, 50, 1f);
            ret = MirJNI.Mir_SurfaceBlendAddTransparent(img.getSurface(), img2.getSurface(), 60, 60, 1f, 0, 0, 0);
            if (ret != 0) {
                throw new IllegalStateException("Unable to blend surface.");
            }

            long draw_surface = img.getSurface();
            //draw with texture
            long testTexture_id = MirJNI.SDL_CreateTextureFromSurface(renderer_id, draw_surface);
            if (testTexture_id == 0) {
                throw new IllegalStateException("Unable to create texture from surface: " + MirJNI.SDL_GetError());
            }

            long testTexture_id2 = MirJNI.SDL_CreateTextureFromSurface(renderer_id, img2.getSurface());
            if (testTexture_id2 == 0) {
                throw new IllegalStateException("Unable to create texture from surface2: " + MirJNI.SDL_GetError());
            }

            MirJNI.SDL_SetRenderDrawColor(renderer_id, 0, 0, 0, 255);
            MirJNI.SDL_RenderClear(renderer_id);

            //SDL_SetTextureColorMod(testTexture_id, 255, 0, 255);
            //SDL_SetTextureBlendMode(testTexture_id, SDLBlendMode.SDL_BLENDMODE_NONE);
            MirJNI.SDL_SetTextureBlendMode(testTexture_id, SDLBlendMode.SDL_BLENDMODE_BLEND);
            //SDL_SetTextureAlphaMod(testTexture_id, 255);
            int[] dstRect = {0, 0, img.getWidth(), img.getHeight()};
            int[] dstRect2 = {10, 10, img2.getWidth(), img2.getHeight()};


            boolean shouldRun = true;
            long event_id = MirJNI.SDL_CreateEvent();
            byte[] buf=new byte[64];
            //byte[] const_str=toCstyleBytes("请输入你的text");
//            System.out.println("111111-------"+new String(const_str,0,const_str.length,"utf-8"));
//            Test.fileOut("1--------"+new String(const_str,0,const_str.length,"utf-8")+"\n");
//            System.arraycopy(const_str,0,buf,0,const_str.length);
//            System.out.println("222222-------"+new String(buf,0,const_str.length,"utf-8"));
//            Test.fileOut("2--------"+new String(buf,0,const_str.length,"utf-8")+"\n");

            long prev_ts = 0l;
            int color_mod=0;
            while (shouldRun) {

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
                            break;
                        case SDLEventType.SDL_MOUSEBUTTONUP:
                            int[] pos_up = MirJNI.SDL_GetEventMouseButtonPos(event_id);
                            System.out.println("mouse up: x: " + pos_up[0]+", y: "+pos_up[1]);
                            break;
                        default:
                            break;
                    }
                }

//                if (MirJNI.SDL_GetWindowFlags(win_id) & SDL_WINDOW_MINIMIZED)
//                {
//                    SDL_Delay(10);
//                    continue;
//                }

                MirJNI.ImGui_SDLRenderer2_NewFrame();

                MirJNI.ImGui_SDL2_NewFrame();

                MirJNI.ImGui_NewFrame();

                if (!MirJNI.ImGui_Begin(Util.toCstyleBytes("login"), 100,350, 200, 25, true))
                {
                    System.out.println("-------------------0000-----------------------");
                    MirJNI.ImGui_End();
                }
                else
                {
                    //MirJNI.ImGui_Text(toCstyleBytes("标签"));

                    //动态修改一些属性
                    long cur_ts = MirJNI.SDL_GetTicks();
                    if ((cur_ts - prev_ts) > 5000)
                    {
                        color_mod++;
                        //color_mod = color_mod % 3;
                        prev_ts = cur_ts;
                        MirJNI.ImGui_SetWindowFontScale(color_mod);
                        MirJNI.ImGui_InitBackColor(color_mod%3 == 1 ? 1.0f:0f, color_mod%3 == 2 ? 1.0f:0f, color_mod%3 == 0 ? 1.0f:0f, 0.5f);
                        MirJNI.ImGui_InitForeColor(color_mod%3 == 0 ? 1.0f:0f, color_mod%3 == 1 ? 1.0f:0f, color_mod%3 == 2 ? 1.0f:0f, 0.5f);
                    }

                    //if(MirJNI.ImGui_InputTextMultiline(toCstyleBytes("##"),buf, 200.0f, 25))
                    if(MirJNI.ImGui_InputText(0, 0, 198, Util.toCstyleBytes("##"), Util.toCstyleBytes("请输入内容..."), buf, false))
                    {
                        Test.fileOut(Util.zeroEndBytesToString(buf)+"\n");
                        System.out.println("----------------------enter return--------------------");
                    }

                    MirJNI.ImGui_End();
                }

                MirJNI.SDL_SetWindowOpacity(win_id,0.5f);
                MirJNI.ImGui_Render(renderer_id);
                //覆盖inputtext
                //MirJNI.SDL_RenderCopy(renderer_id, testTexture_id2, null, dstRect2);
                MirJNI.SDL_RenderPresent(renderer_id);

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