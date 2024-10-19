package org.mini.SDL2;

import java.io.UnsupportedEncodingException;

import org.mini.SDL2.MirImage;
import org.mini.SDL2.MirLib;
import org.mini.SDL2.constcode.SDLBlendMode;
import org.mini.SDL2.constcode.SDLRendererFlags;
import org.mini.SDL2.constcode.SDLBlendMode;
import org.mini.SDL2.constcode.SDLEventType;
import org.mini.SDL2.constcode.SDLKeyCode;
import org.mini.SDL2.constcode.SDLRendererFlags;
import org.mini.SDL2.constcode.SdlSubSystemConst;
import org.mini.SDL2.constcode.SDLWindowFlags;

public class SDL2 {

    static {
        SDL2.loadLib();
    }

    static boolean loaded;

    public static void loadLib() {
        if (!loaded) {
            System.setProperty("java.library.path", "./");
            System.loadLibrary("sdl2");
            //System.load("libsdl2.dll");

        }
        loaded = true;
    }

    public static native int SDL_Init(int flags);

    public static native long SDL_CreateWindow(byte[] title, int x, int y, int width, int height, int flags);

    public static native String SDL_GetError();

    public static native long SDL_CreateRenderer(long window_id, int idx, int flags);

    public static native long SDL_RWFromConstMem(byte[] data, int size);

    public static native int SDL_RWclose(long rwops_id);

    public static native long SDL_CreateTextureFromSurface(long render_id, long surface_id);

    public static native int SDL_SetTextureColorMod(long texture_id, byte r, byte g, byte b);

    public static native int SDL_SetTextureBlendMode(long texture_id, int blend_mod);

    public static native int SDL_SetTextureAlphaMod(long texture_id, int alpha);

    public static native int SDL_RenderCopy(long renderer_id, long texture_id, int[] src_rect, int[] dst_rect);

    public static native int SDL_RenderClear(long renderer_id);

    public static native void SDL_RenderPresent(long renderer_id);

    public static native long SDL_CreateEvent();
    public static native int SDL_GetEventType(long event_id);
    public static native int SDL_GetWindowEvent(long event_id);
    public static native int SDL_GetKeyEventKeySym(long event_id);
    public static native void SDL_FreeEvent(long event_id);

    public static native int SDL_PollEvent(long event_id);

    public static native void SDL_Quit();

    public static native int SDL_GetSurfaceWidth(long surface_id);
    public static native int SDL_GetSurfaceHeight(long surface_id);
    public static native long SDL_IMG_LoadPNG_RW(long rwops_id);

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

    public static void main(String args[]) throws Exception
    {
        System.out.println("--00--");
        int result = SDL_Init(SdlSubSystemConst.SDL_INIT_EVERYTHING);
        System.out.println("--11--");
        if (result != 0) {
            throw new IllegalStateException("Unable to initialize SDL library (Error code " + result + "): " + SDL_GetError());
        }
        System.out.println("--22--");
        // Create and init the window
        long win_id = SDL_CreateWindow(toCstyleBytes("Demo SDL2"), 0, 0, 1024, 768, SDLWindowFlags.SDL_WINDOW_SHOWN | SDLWindowFlags.SDL_WINDOW_RESIZABLE);
        System.out.println("--33--");
        if (win_id == 0) {
            throw new IllegalStateException("Unable to create SDL window: " + SDL_GetError());
        }

        long renderer_id = SDL_CreateRenderer(win_id, -1, SDLRendererFlags.SDL_RENDERER_ACCELERATED);
        if (renderer_id == 0) {
            throw new IllegalStateException("Unable to create SDL renderer: " + SDL_GetError());
        }

        MirLib mir_lib =new MirLib("/home/kindred/mywork/projects/cpp/devilutionX_kindred/my_asset/Prguse2_png.Lib");
        mir_lib.Initialize();
        MirImage img = mir_lib.GetMirImage(542);

        long rwops_id = SDL_RWFromConstMem(img.data, img.header.length);
        long surface_id = SDL_IMG_LoadPNG_RW(rwops_id);

        SDL_RWclose(rwops_id);

        long testTexture_id = SDL_CreateTextureFromSurface(renderer_id, surface_id);
        SDL_SetTextureColorMod(testTexture_id, (byte)255, (byte)0, (byte)255);
        SDL_SetTextureBlendMode(testTexture_id, SDLBlendMode.SDL_BLENDMODE_BLEND);
        SDL_SetTextureAlphaMod(testTexture_id, (byte)255);
        int[] dstRect = {0, 0, SDL_GetSurfaceWidth(surface_id), SDL_GetSurfaceHeight(surface_id)};
        SDL_RenderCopy(renderer_id, testTexture_id, null, dstRect);

        SDL_RenderClear(renderer_id);
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

    }
}
