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


}
