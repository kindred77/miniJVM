package com.kindred.mir.libs;

import com.kindred.mir.util.MyRandomAccessFile;
import com.kindred.sdl.constcode.SDL_PixelFormatEnum;

import com.kindred.mir.engine.*;
import com.kindred.sdl.constcode.SDL_TextureAccess;

import static com.kindred.sdl.constcode.SDL_PixelFormatEnum.SDL_PIXELFORMAT_ARGB8888;

class MirImageHeader
{
    public short width, height, x, y, shadowX, shadowY;
    public byte shadow;
    public int length;
}

public class MirImage {

    //we use SDL_PIXELFORMAT_ARGB8888
    //will be converted if not.
    public static final int DEFAULT_PIXEL_FORMAT = SDL_PIXELFORMAT_ARGB8888;
    public MirImageHeader header;
    //public boolean initialized = false;
    //const int index = -1;
    //public byte[] data;

    private long surface_id=0;

    public long getSurface()
    {
        return surface_id;
    }

    public int getDataLengthInHeader()
    {
        return header == null ? 0 : header.length;
    }

    public int getWidth()
    {
        return header == null ? 0 : header.width;
    }

    public int getHeight()
    {
        return header == null ? 0 : header.height;
    }

    public int getX()
    {
        return header == null ? 0 : header.x;
    }

    public int getY()
    {
        return header == null ? 0 : header.y;
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

    public static long createTexture(MirImage img, long renderer_id) throws Exception
    {
        long texture_id = MirJNI.SDL_CreateTexture(renderer_id, DEFAULT_PIXEL_FORMAT,
                SDL_TextureAccess.SDL_TEXTUREACCESS_STREAMING, img.getWidth(),
                img.getHeight());

        if (texture_id == 0) {
            throw new IllegalStateException("Unable to create texture for the image: " + MirJNI.SDL_GetError());
        }

        int ret = MirJNI.SDL_UpdateTextureWithSurface(texture_id, img.getSurface(), null);
        if (ret != 0) {
            throw new IllegalStateException("Unable to update texture from surface for the image: " + MirJNI.SDL_GetError());
        }

        //MirJNI.SDL_FreeSurface(surface_id);
        //surface_id=0;

        return texture_id;
    }

    private void convertPixelFormat(byte[] data) throws Exception
    {
        long rwops_id = MirJNI.SDL_RWFromConstMem(data, getDataLengthInHeader());
        if (rwops_id == 0) {
            throw new IllegalStateException("Unable to create rwops from image data: " + MirJNI.SDL_GetError());
        }
        long surface_tmp = MirJNI.SDL_IMG_LoadPNG_RW(rwops_id);
        if (surface_tmp == 0) {
            throw new IllegalStateException("Unable to create surface from rwops: " + MirJNI.SDL_GetError());
        }

        int pixel_format = MirJNI.SDL_GetSurfacePixelFormat(surface_tmp);
        System.out.println("pixel format: "+SDL_PixelFormatEnum.toString(pixel_format));
        if (pixel_format != DEFAULT_PIXEL_FORMAT) {
            surface_id = MirJNI.SDL_ConvertSurfaceFormat(surface_tmp, DEFAULT_PIXEL_FORMAT, 0);
            System.out.println("after convert pixel format: "+SDL_PixelFormatEnum.toString(MirJNI.SDL_GetSurfacePixelFormat(surface_id)));
            MirJNI.SDL_FreeSurface(surface_tmp);
        }
        else {
            surface_id = surface_tmp;
        }
        MirJNI.SDL_RWclose(rwops_id);
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
