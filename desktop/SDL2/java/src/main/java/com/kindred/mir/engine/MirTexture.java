package com.kindred.mir.engine;

import com.kindred.mir.libs.MirImage;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Size;
import com.kindred.sdl.constcode.SDL_PixelFormatEnum;

public class MirTexture {
    private boolean isValid=true;
    private int width = 0;
    private int height = 0;
    private int pixelFormat = SDL_PixelFormatEnum.SDL_PIXELFORMAT_ARGB8888;
    private Color color = Color.Black;
    private long texture_id = 0;
    private MirImage image;

    public boolean getIsValid()
    {
        return this.isValid;
    }

    public void dispose()
    {
        this.width=0;
        this.height=0;
        this.color = Color.Black;
        this.isValid=true;
    }

    public MirTexture(MirImage image,long renderer_id)
    {
        this.image=image;
        update(renderer_id, this.image.getSurface(MirImage.ImageEffect.None));
    }

    /*
    创建空的
     */
    public MirTexture()
    {
        //初始化为无效的
        isValid=false;
    }

//    public MirTexture(int width, int height, int pixel_format, Color color)
//    {
//        this.width=width;
//        this.height=height;
//        this.pixelFormat=pixel_format;
//        this.color = color;
//    }

    //this is a heavy operation
    public void update(long renderer_id,long surface_id)
    {
        if (this.texture_id !=0)
        {
            MirJNI.SDL_DestroyTexture(this.texture_id);
        }
        if (surface_id != 0) {
            this.texture_id=MirJNI.SDL_CreateTextureFromSurface(renderer_id,surface_id);
            isValid=true;
        }

    }

    public Size getSize()
    {
        return new Size(this.width,this.height);
    }

    public long getTexture() {
        return this.texture_id;
    }
}
