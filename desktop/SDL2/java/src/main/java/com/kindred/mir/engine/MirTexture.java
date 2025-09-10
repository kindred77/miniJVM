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
        update(renderer_id, this.image.getSurface(MirImage.ImageEffect.None), false);
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

    /**
     * 可以支持surface为0
     * @param renderer_id
     * @param surface_id
     */
    //this is a heavy operation
    public void update(long renderer_id,long surface_id,boolean ifReleaseSurface)
    {
        //让render停止渲染
        this.isValid=false;
        long new_texture_id = 0L;
        if (surface_id != 0L) {
            new_texture_id = MirJNI.SDL_CreateTextureFromSurface(renderer_id,surface_id);
            if(ifReleaseSurface){
                MirJNI.SDL_FreeSurface(surface_id);
            }
        }
        long old_texture_id = this.texture_id;
        this.texture_id = new_texture_id;
        if (old_texture_id != 0L) {
            MirJNI.SDL_DestroyTexture(old_texture_id);
        }
        this.isValid = this.texture_id != 0L;
    }

    public Size getSize()
    {
        return new Size(this.width,this.height);
    }

    public long getTexture() {
        return this.texture_id;
    }
}
