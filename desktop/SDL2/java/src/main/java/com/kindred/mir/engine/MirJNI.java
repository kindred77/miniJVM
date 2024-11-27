package com.kindred.mir.engine;

import com.kindred.sdl.SDL;

public class MirJNI extends SDL {

    //we mainly use surface, texture is for test
    public static native int SDL_UpdateTextureWithSurface(long texture_id, long surface, int[] rect);
    public static native int Mir_TextureToGray(long texture_id);
    public static native int Mir_TextureBlackEffect(long texture_id);
    public static native int Mir_TextureInverse(long texture_id);
    public static native int Mir_TextureAlpha(long texture_id, float alpha);


    public static native int Mir_SurfaceToGray(long surface_id);
    public static native int Mir_SurfaceBlackEffect(long surface_id);
    public static native int Mir_SurfaceWhiteEffect(long surface_id);
    public static native int Mir_SurfaceRedEffect(long surface_id);
    public static native int Mir_SurfaceGreenEffect(long surface_id);
    public static native int Mir_SurfaceBlueEffect(long surface_id);
    public static native int Mir_SurfaceYellowEffect(long surface_id);
    public static native int Mir_SurfaceFuchsiaEffect(long surface_id);
    public static native int Mir_SurfaceBrightEffect(long surface_id);
    public static native int Mir_SurfaceGrayEffect(long surface_id);

    public static native int Mir_SurfaceInverse(long surface_id);
    public static native int Mir_SurfaceAlpha(long surface_id, float alpha);
    public static native int Mir_SurfaceBlendNormal(long dst_surface_id, long src_surface_id, int x, int y, float alpha);
    public static native int Mir_SurfaceBlendNormalTransparent(long dst_surface_id, long src_surface_id, int x, int y, float alpha, int transparent_r, int transparent_g, int transparent_b);
    //looks the same
    public static native int Mir_SurfaceBlendAdd(long dst_surface_id, long src_surface_id, int x, int y, float alpha);
    public static native int Mir_SurfaceBlendAddTransparent(long dst_surface_id, long src_surface_id, int x, int y, float alpha, int transparent_r, int transparent_g, int transparent_b);


    public static native long ImGui_SDL2_InitImGuiContext();
    public static native void ImGui_SetCurrentContext(long context_ptr);
    public static native boolean ImGui_ImplSDL2_InitForSDLRenderer(long window_id,long renderer_id);
    public static native boolean ImGui_ImplSDLRenderer2_Init(long renderer_id);
    public static native void ImGui_InitBackColor(float red, float green, float blue, float alpha);
    public static native void ImGui_InitForeColor(float red, float green, float blue, float alpha);
    public static native long ImGui_InitFont(byte[] font_name, float size);
    public static native void ImGui_PushFont(long font_id);
    public static native void ImGui_PopFont();
    public static native int ImGui_SDL2_ProcessEvent(long event_id);
    public static native void ImGui_SDLRenderer2_NewFrame();
    public static native void ImGui_SDL2_NewFrame();
    public static native void ImGui_NewFrame();
    public static native void ImGui_EndFrame();

    public static native boolean ImGui_Begin(byte[] label, float x, float y, float width, float height,
        boolean no_titlebar,boolean no_scrollbar,boolean no_menu,boolean no_move,
        boolean no_resize,boolean no_collapse,boolean no_close,boolean no_nav,
        boolean no_background,boolean no_bring_to_front,boolean unsaved_document,
        boolean no_saved_settings);
    public static native void ImGui_Text(byte[] text);
    public static native boolean ImGui_InputText(float x, float y, float width, byte[] label, byte[] hint, byte[] buf, boolean isPassword);
    public static native boolean ImGui_InputTextMultiline(byte[] label, byte[] buf, float width, int line_height_cnt);
    public static native void ImGui_SetWindowFontScale(float scale);
    public static native void ImGui_End();
    public static native void ImGui_Render(long renderer_id, long drawData_ptr);
    public static native long ImGui_RenderAndGetDrawData();
    public static native void ImGui_Destroy();

    //for test(不可用)
    public static native long ImGui_ConvertDrawDataToTexture(long drawData_ptr, long renderer_id);


    //for kiss test
    public static native void Kiss_Init();


}
