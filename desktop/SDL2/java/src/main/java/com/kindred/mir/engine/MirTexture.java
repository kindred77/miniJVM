package com.kindred.mir.engine;

import com.kindred.mir.util.Color;
import com.kindred.mir.util.Size;
import com.kindred.sdl.constcode.SDL_PixelFormatEnum;

public class MirTexture {
    private boolean isDisposed;
    private int width = 0;
    private int height = 0;
    private int pixelFormat = SDL_PixelFormatEnum.SDL_PIXELFORMAT_ARGB8888;
    private Color color = Color.Black;
    private long texture_id;

    public boolean getIsDisposed()
    {
        return this.isDisposed;
    }

    public void dispose()
    {
        this.width=0;
        this.height=0;
        this.color = Color.Black;
        this.isDisposed=true;
    }

    public MirTexture(int width, int height, int pixel_format, Color color)
    {
        this.width=width;
        this.height=height;
        this.pixelFormat=pixel_format;
        this.color = color;
    }

    public void init(long renderer_id)
    {
        //texture_id = MirJNI.SDL_CreateTexture(renderer_id, pixelFormat, access_method, width, height);
    }

    public Size getSize()
    {
        return new Size(this.width,this.height);
    }
}
