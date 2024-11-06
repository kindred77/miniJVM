package com.kindred.mir.engine;

import com.kindred.sdl.constcode.SDL_PixelFormatEnum;

public class MirTexture {
    private boolean isDisposed;
    private int width;
    private int height;
    private int pixelFormat = SDL_PixelFormatEnum.SDL_PIXELFORMAT_ARGB8888;

    public boolean getIsDisposed()
    {
        return this.isDisposed;
    }

    public void dispose()
    {

    }

    public MirTexture(int width, int height, int pixel_format)
    {
        this.width=width;
        this.height=height;
        this.pixelFormat=pixel_format;
    }
}
