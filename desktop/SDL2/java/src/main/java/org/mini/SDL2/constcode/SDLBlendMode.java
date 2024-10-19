package org.mini.SDL2.constcode;

public class SDLBlendMode {
    /**
     * no blending
     * <pre>dstRGBA = srcRGBA</pre>
     */
    public static final int SDL_BLENDMODE_NONE = 0x00000000;

    /**
     * alpha blending
     * <pre>
     * dstRGB = (srcRGB * srcA) + (dstRGB * (1-srcA))
     * dstA = srcA + (dstA * (1-srcA))
     * </pre>
     */
    public static final int SDL_BLENDMODE_BLEND = 0x00000001;

    /**
     * additive blending
     * <pre>
     * dstRGB = (srcRGB * srcA) + dstRGB
     * dstA = dstA
     * </pre>
     */
    public static final int SDL_BLENDMODE_ADD = 0x00000002;

    /**
     * color modulate
     * <pre>
     * dstRGB = srcRGB * dstRGB
     * dstA = dstA
     * </pre>
     */
    public static final int SDL_BLENDMODE_MOD = 0x00000004;

    /**
     * color multiply
     * <pre>
     * dstRGB = (srcRGB * dstRGB) + (dstRGB * (1-srcA))
     * dstA = dstA
     * </pre>
     */
    public static final int SDL_BLENDMODE_MUL = 0x00000008;

    public static final int SDL_BLENDMODE_INVALID = 0x7FFFFFFF;

    // TODO: Generate public static String toString(int value)

    private SDLBlendMode() {
    }
}
