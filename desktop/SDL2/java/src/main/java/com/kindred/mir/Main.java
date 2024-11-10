
package com.kindred.mir;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;

import com.kindred.mir.test.Test;
import com.kindred.sdl.constcode.*;

import static com.kindred.sdl.constcode.SDLWindowFlags.SDL_WINDOW_MINIMIZED;


public class Main {

    static public byte[] toCstyleBytes(String s) {
        if (s == null) {
            return null;
        }
        if (s.length() == 0 || s.charAt(s.length() - 1) != '\000') {
            s += '\000';
        }
        byte[] barr = null;
        try {
            barr = s.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return barr;
    }

    public static void main(String args[])
    {
        //System.setProperty("microedition.encoding","utf-8");
        try
        {
            int result = MirJNI.SDL_Init(SdlSubSystemConst.SDL_INIT_EVERYTHING);
            if (result != 0) {
                throw new IllegalStateException("Unable to initialize SDL library (Error code " + result + "): " + MirJNI.SDL_GetError());
            }
            // Create and init the window
            long win_id = MirJNI.SDL_CreateWindow(toCstyleBytes("窗口-kindred"),
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
            //---------------------

            MirLib mir_lib =new MirLib("D:\\mywork\\projects\\cpp\\devilutionX\\my_asset\\Prguse2_png.Lib");
            mir_lib.Initialize();
            
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

            MirJNI.SDL_SetRenderDrawColor(renderer_id, 0, 0, 0, 255);
            MirJNI.SDL_RenderClear(renderer_id);

            //SDL_SetTextureColorMod(testTexture_id, 255, 0, 255);
            //SDL_SetTextureBlendMode(testTexture_id, SDLBlendMode.SDL_BLENDMODE_NONE);
            MirJNI.SDL_SetTextureBlendMode(testTexture_id, SDLBlendMode.SDL_BLENDMODE_BLEND);
            //SDL_SetTextureAlphaMod(testTexture_id, 255);
            int[] dstRect = {0, 0, img.getWidth(), img.getHeight()};


            boolean shouldRun = true;
            long event_id = MirJNI.SDL_CreateEvent();
            byte[] buf=new byte[128];
            byte[] const_str=toCstyleBytes("请输入你的text");
            //Test.fileOut(new String(const_str,0,const_str.length,"utf-8"));
            System.out.println("111111-------"+new String(const_str,0,const_str.length,"utf-8"));
            Test.fileOut("1--------"+new String(const_str,0,const_str.length,"utf-8")+"\n");
            System.arraycopy(const_str,0,buf,0,const_str.length);
            System.out.println("222222-------"+new String(buf,0,const_str.length,"utf-8"));
            Test.fileOut("2--------"+new String(buf,0,const_str.length,"utf-8")+"\n");
            //Test.fileOut(new String(buf,0,const_str.length,"utf-8"));

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
                            System.out.println("Window event " + MirJNI.SDL_GetWindowEvent(event_id));
                            break;
                        case SDLEventType.SDL_MOUSEBUTTONDOWN:
                            System.out.println("mouse down " + MirJNI.SDL_GetWindowEvent(event_id));
                            break;
                        case SDLEventType.SDL_MOUSEBUTTONUP:
                            System.out.println("mouse up " + MirJNI.SDL_GetWindowEvent(event_id));
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

                if (!MirJNI.ImGui_Begin())
                {
                    System.out.println("-------------------0000-----------------------");
                    MirJNI.ImGui_End();
                }
                else
                {
                    MirJNI.ImGui_Text(toCstyleBytes("标签"));

                    if(MirJNI.ImGui_InputText(toCstyleBytes("##"),buf))
                    {
                        for (byte b : buf)
                        {
                            System.out.print("-"+b);
                            if (b == 0)
                            {
                                break;
                            }
                        }
                        System.out.println("----------------------111--------------------"+new String(buf,0,const_str.length,"utf-8"));
                        Test.fileOut(new String(buf,0,buf.length,"utf-8")+"\n");
                    }
                    else
                    {
                        //System.out.println("----------------------222--------------------");
                    }

                    MirJNI.ImGui_End();
                }

                //MirJNI.SDL_RenderCopy(renderer_id, testTexture_id, null, dstRect);

                MirJNI.ImGui_Render(renderer_id);
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