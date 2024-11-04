package com.kindred.mir.libs;

import com.kindred.mir.util.MyRandomAccessFile;
import com.kindred.sdl.constcode.SDL_PixelFormatEnum;

import static com.kindred.sdl.SDL.*;
import static com.kindred.sdl.SDL.SDL_RWclose;
import static com.kindred.sdl.constcode.SDL_PixelFormatEnum.SDL_PIXELFORMAT_ARGB8888;
import static com.kindred.sdl.constcode.SDL_TextureAccess.SDL_TEXTUREACCESS_STATIC;

class MirImageHeader
{
    public short width, height, x, y, shadowX, shadowY;
    public byte shadow;
    public int length;
}

public class MirImage {

    public static final int DEFAULT_PIXEL_FORMAT = SDL_PIXELFORMAT_ARGB8888;
    public MirImageHeader header;
    //public boolean initialized = false;
    //const int index = -1;
    //public byte[] data;

    public long surface_id;
    public long texture_id;

    public int getDataLengthInHeader()
    {
        return header == null ? 0 : header.length;
    }

    private boolean initHeader(MyRandomAccessFile myRAF) throws Exception
    {
        this.header=new MirImageHeader();
        this.header.width=myRAF.readShortLE();
        this.header.height=myRAF.readShortLE();
        this.header.x=myRAF.readShortLE();
        this.header.y=myRAF.readShortLE();
        this.header.shadowX=myRAF.readShortLE();
        this.header.shadowY=myRAF.readShortLE();
        this.header.shadow=myRAF.readByte();
        this.header.length=myRAF.readIntLE();
        return true;
    }

    public void initTexture(long renderer_id) throws Exception
    {
        texture_id = SDL_CreateTexture(renderer_id,
                DEFAULT_PIXEL_FORMAT, SDL_TEXTUREACCESS_STATIC,
                this.header.width, this.header.height);
        if (texture_id == 0) {
            throw new IllegalStateException("Unable to create texture for the image: " + SDL_GetError());
        }
    }

    private void convertPixelFormat(byte[] data) throws Exception
    {
        long rwops_id = SDL_RWFromConstMem(data, getDataLengthInHeader());
        if (rwops_id == 0) {
            throw new IllegalStateException("Unable to create rwops from image data: " + SDL_GetError());
        }
        long surface_tmp = SDL_IMG_LoadPNG_RW(rwops_id);
        if (surface_tmp == 0) {
            throw new IllegalStateException("Unable to create surface from rwops: " + SDL_GetError());
        }

        int pixel_format = SDL_GetSurfacePixelFormat(surface_tmp);
        System.out.println("pixel format: "+SDL_PixelFormatEnum.toString(pixel_format));
        if (pixel_format != DEFAULT_PIXEL_FORMAT) {
            surface_id = SDL_ConvertSurfaceFormat(surface_tmp, DEFAULT_PIXEL_FORMAT, 0);
            System.out.println("after convert pixel format: "+SDL_PixelFormatEnum.toString(SDL_GetSurfacePixelFormat(surface_id)));
            SDL_FreeSurface(surface_tmp);
        }
        else {
            surface_id = surface_tmp;
        }
        SDL_RWclose(rwops_id);
    }

    public MirImage(MyRandomAccessFile myRAF, int index) throws Exception
    {
        if (!initHeader(myRAF))
        {
            throw new Exception("Image header init failed. image index: "+index);
        }

        byte[] data = new byte[this.header.length];
        myRAF.readFully(data);

        convertPixelFormat(data);
    }
}
