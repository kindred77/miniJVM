
package com.kindred.mir;

import java.io.UnsupportedEncodingException;

import com.kindred.mir.engine.Texture;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;

import com.kindred.sdl.constcode.*;

import static com.kindred.sdl.SDL.*;

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
        try
        {
            int result = SDL_Init(SdlSubSystemConst.SDL_INIT_EVERYTHING);
            if (result != 0) {
                throw new IllegalStateException("Unable to initialize SDL library (Error code " + result + "): " + SDL_GetError());
            }
            // Create and init the window
            long win_id = SDL_CreateWindow(toCstyleBytes("窗口-kindred"), 0, 0, 800, 600, SDLWindowFlags.SDL_WINDOW_SHOWN | SDLWindowFlags.SDL_WINDOW_RESIZABLE);
            if (win_id == 0) {
                throw new IllegalStateException("Unable to create SDL window: " + SDL_GetError());
            }

            int win_pf = SDL_GetWindowPixelFormat(win_id);
            System.out.println("----windows---pixelformat: "+ SDL_PixelFormatEnum.toString(win_pf));

            long renderer_id = SDL_CreateRenderer(win_id, -1, SDLRendererFlags.SDL_RENDERER_ACCELERATED);
            if (renderer_id == 0) {
                throw new IllegalStateException("Unable to create SDL renderer: " + SDL_GetError());
            }

            MirLib mir_lib =new MirLib("C:\\mywork\\projects\\cpp\\devilutionX\\kindred\\devilutionX\\my_asset\\Prguse2_png.Lib");
            mir_lib.Initialize();
            
            MirImage img = mir_lib.GetMirImage(1205);
            MirImage img2 = mir_lib.GetMirImage(930);

            //convert pixel format
//            System.out.println("convert to SDL_PIXELFORMAT_RGB24...");
//            long surface_rgb24 = SDL_ConvertSurfaceFormat(surface_id, SDL_PixelFormatEnum.SDL_PIXELFORMAT_RGB24, 0);
//            if (surface_rgb24 == 0) {
//                throw new IllegalStateException("Unable to convert surface from : " + SDL_GetSurfacePixelFormat(surface_id)+" to "+ SDL_PixelFormatEnum.toString(SDL_PixelFormatEnum.SDL_PIXELFORMAT_RGB24)+ ", because of : "+ SDL_GetError());
//            }
//            System.out.println("after convert, surface width: "+SDL_GetSurfaceWidth(surface_rgb24)+", height: "+SDL_GetSurfaceHeight(surface_rgb24)+", pixel format: "+SDL_PixelFormatEnum.toString(SDL_GetSurfacePixelFormat(surface_rgb24)));

            //--------------start--------do something
            //Texture java_texture=new Texture(SDL_GetSurfaceWidth(surface_rgb24), SDL_GetSurfaceHeight(surface_rgb24));
            //int ret = Mir_SurfaceToGray(img.surface_id);
            //int ret = Mir_SurfaceInverse(img.surface_id);
            int ret = Mir_SurfaceAlpha(img.surface_id, 0.4f);
            //int ret = Mir_SurfaceBlackEffect(img.surface_id);

            long surface2_rgb24 = SDL_ConvertSurfaceFormat(img2.surface_id, SDL_PixelFormatEnum.SDL_PIXELFORMAT_RGB24, 0);
            if (surface2_rgb24 == 0) {
                throw new IllegalStateException("Unable to convert surface from : " + SDL_GetSurfacePixelFormat(img2.surface_id)+" to "+ SDL_PixelFormatEnum.toString(SDL_PixelFormatEnum.SDL_PIXELFORMAT_RGB24)+ ", because of : "+ SDL_GetError());
            }
            //int ret = Mir_SurfaceBlendNormal(surface_rgb24, surface2_rgb24, 0, 0, 0.5f);
            //int ret = Mir_SurfaceBlendNormalTransparent(surface_rgb24, surface2_rgb24, 0, 0, 0.5f, 0, 0, 0);
            //int ret = Mir_SurfaceBlendAdd(surface_rgb24, surface2_rgb24, 0, 0, 1.0f);
            //int ret = Mir_SurfaceBlendAddTransparent(surface_rgb24, surface2_rgb24, 0, 0, 0.5f, 0, 0, 0);
            if (ret != 0) {
                throw new IllegalStateException("Unable to cconvert surface to gray.");
            }
            //--------------end
//            long surface_convert = SDL_ConvertSurfaceFormat(surface_rgb24, win_pf, 0);
//            if (surface_convert == 0) {
//                throw new IllegalStateException("Unable to convert surface from : " + SDL_GetSurfacePixelFormat(surface_id)+" to "+ SDL_PixelFormatEnum.toString(win_pf)+ ", because of : "+ SDL_GetError());
//            }
//            System.out.println("after convert2, surface width: "+SDL_GetSurfaceWidth(surface_convert)+", height: "+SDL_GetSurfaceHeight(surface_convert)+", pixel format: "+SDL_PixelFormatEnum.toString(SDL_GetSurfacePixelFormat(surface_convert)));

            long draw_surface = img.surface_id;
            //draw with texture
            long testTexture_id = SDL_CreateTextureFromSurface(renderer_id, draw_surface);
            if (testTexture_id == 0) {
                throw new IllegalStateException("Unable to create texture from surface: " + SDL_GetError());
            }

            SDL_SetRenderDrawColor(renderer_id, 255, 0, 255, 255);
            SDL_RenderClear(renderer_id);

            //SDL_SetTextureColorMod(testTexture_id, 255, 0, 255);
            //SDL_SetTextureBlendMode(testTexture_id, SDLBlendMode.SDL_BLENDMODE_NONE);
            SDL_SetTextureBlendMode(testTexture_id, SDLBlendMode.SDL_BLENDMODE_BLEND);
            //SDL_SetTextureAlphaMod(testTexture_id, 255);
            int[] dstRect = {0, 0, SDL_GetSurfaceWidth(draw_surface), SDL_GetSurfaceHeight(draw_surface)};
            SDL_RenderCopy(renderer_id, testTexture_id, null, dstRect);

            SDL_RenderPresent(renderer_id);

            boolean shouldRun = true;
            long event_id = SDL_CreateEvent();
            while (shouldRun) {
                while (SDL_PollEvent(event_id) != 0) {
                    switch (SDL_GetEventType(event_id)) {
                        case SDLEventType.SDL_QUIT:
                            shouldRun = false;
                            break;
                        case SDLEventType.SDL_KEYDOWN:
                            if (SDL_GetKeyEventKeySym(event_id) == SDLKeyCode.SDLK_SPACE) {
                                System.out.println("SPACE pressed");
                            }
                            break;
                        case SDLEventType.SDL_WINDOWEVENT:
                            System.out.println("Window event " + SDL_GetWindowEvent(event_id));
                        default:
                            break;
                    }
                }
            }

            SDL_Quit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}