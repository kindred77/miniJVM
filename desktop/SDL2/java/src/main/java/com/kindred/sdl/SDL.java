package com.kindred.sdl;

public class SDL {

    static {
        SDL.loadLib();
    }

    static boolean loaded;

    public static void loadLib() {
        if (!loaded) {
            System.setProperty("java.library.path", "./");
            System.loadLibrary("jni_mir");
        }
        loaded = true;
    }

    public static native int SDL_Init(int flags);

    public static native boolean SDL_SetHint(byte[] name, byte[] value);

    public static native long SDL_CreateWindow(byte[] title, int x, int y, int width, int height, int flags);

    public static native int SDL_SetWindowOpacity(long window_id, float opacity);

    public static native String SDL_GetError();

    public static native long SDL_CreateRenderer(long window_id, int idx, int flags);

    public static native long SDL_RWFromConstMem(byte[] data, int size);

    public static native int SDL_RWclose(long rwops_id);

    public static native long SDL_CreateTextureFromSurface(long render_id, long surface_id);

    public static native int SDL_SetTextureColorMod(long texture_id, int r, int g, int b);

    public static native int SDL_SetTextureBlendMode(long texture_id, int blend_mod);

    public static native int SDL_SetTextureAlphaMod(long texture_id, int alpha);

    public static native int SDL_RenderCopy(long renderer_id, long texture_id, int[] src_rect, int[] dst_rect);

    public static native int SDL_SetRenderDrawColor(long renderer_id, int r, int g, int b, int alpha);
    public static native int[] SDL_GetRenderDrawColor(long renderer_id);
    public static native int SDL_RenderClear(long renderer_id);

    public static native void SDL_RenderPresent(long renderer_id);
    public static native int SDL_RenderDrawLine(long renderer_id, int startX, int startY, int endX, int endY);

    public static native long SDL_CreateEvent();
    public static native int SDL_GetEventType(long event_id);
    public static native int SDL_GetEventWindowEvent(long event_id);
    public static native int SDL_GetEventMouseButtonButton(long event_id);
    public static native int[] SDL_GetEventMouseButtonPos(long event_id);
    public static native int[] SDL_GetEventMouseMotionPos(long event_id);
    public static native int[] SDL_GetEventMouseWheelPos(long event_id);
    public static native String SDL_GetEventTextText(long event_id);
    public static native int SDL_GetKeyEventKeySym(long event_id);
    public static native void SDL_FreeEvent(long event_id);

    public static native int SDL_PollEvent(long event_id);
    public static native int SDL_GetWindowFlags(long window_id);
    public static native void SDL_DestroyWindow(long window_id);
    public static native void SDL_DestroyRenderer(long renderer_id);
    public static native void SDL_DestroyTexture(long texture_id);
    public static native long SDL_GetTicks();

    public static native void SDL_Quit();

    public static native int SDL_GetSurfaceWidth(long surface_id);
    public static native int SDL_GetSurfaceHeight(long surface_id);
    public static native int SDL_GetSurfacePitch(long surface_id);
    public static native long SDL_RWFromFile(byte[] file_name, byte[] mode);
    public static native long SDL_IMG_LoadPNG_RW(long rwops_id);
    public static native int SDL_GetWindowPixelFormat(long window_id);
    public static native int SDL_GetSurfacePixelFormat(long surface_id);
    public static native long SDL_ConvertSurfaceFormat(long surface_id, int pixel_format, int flags);
    //public static native byte[] SDL_GetSurfacePixelData(long surface_id);
    public static native int SDL_LockSurface(long surface_id);
    public static native void SDL_UnlockSurface(long surface_id);
    public static native void SDL_FreeSurface(long surface_id);
    public static native long SDL_CreateTexture(long renderer_id, int pixel_format, int access_method, int width, int height);
    public static native int SDL_UpdateTexture(long texture_id, int[] rect, byte[] pixel, int pitch);

    //for try
    public static native void SDL_StartTextInput();
    public static native void SDL_StopTextInput();
    public static native void SDL_SetTextInputRect(int[] rect);
    public static native int SDL_RenderDrawRect(long renderer_id, int[] rect);
}
