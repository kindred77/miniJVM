package com.kindred.mir.engine;

import com.kindred.sdl.SDL;

public class MirJNI extends SDL {

    public static native int Mir_SurfaceToGray(long surface_id);
    public static native int Mir_SurfaceBlackEffect(long surface_id);
    public static native int Mir_SurfaceInverse(long surface_id);
    public static native int Mir_SurfaceAlpha(long surface_id, float alpha);
    public static native int Mir_SurfaceBlendNormal(long dst_surface_id, long src_surface_id, int x, int y, float alpha);
    public static native int Mir_SurfaceBlendNormalTransparent(long dst_surface_id, long src_surface_id, int x, int y, float alpha, int r, int g, int b);
    public static native int Mir_SurfaceBlendAdd(long dst_surface_id, long src_surface_id, int x, int y, float alpha);
    public static native int Mir_SurfaceBlendAddTransparent(long dst_surface_id, long src_surface_id, int x, int y, float alpha, int r, int g, int b);

    public static native int SDL_UpdateTextureWithSurface(long texture_id, long surface, int[] rect);

    //we mainly use surface, texture is for test
    public static native int Mir_TextureToGray(long texture_id);
    public static native int Mir_TextureBlackEffect(long texture_id);
    public static native int Mir_TextureInverse(long texture_id);
    public static native int Mir_TextureAlpha(long texture_id, float alpha);
}
