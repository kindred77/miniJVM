package com.kindred.mir.libs;

import com.kindred.mir.util.MyRandomAccessFile;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;

import java.io.File;

public class MirLib {
    private final int LibVersion = 2;
    private boolean initialized = false;
    private int imageCnt = 0;
    private String file_name;
    private File file=null;
    private MyRandomAccessFile myRAF=null;

    private MirImage[] images;
    private int[] indexList;

    public MirLib(String file_name)
    {
        this.file_name=file_name;
        this.file=new File(file_name);
    }

    private boolean initImageHeader(MirImage image) throws Exception
    {
        if (!initialized)
        {
            throw new Exception("Lib not initialized.");
        }
        if (image==null)
        {
            throw new Exception("Image must be constructed.");
        }
        image.header=new MirImageHeader();
        image.header.width=myRAF.readShortLE();
        image.header.height=myRAF.readShortLE();
        image.header.x=myRAF.readShortLE();
        image.header.y=myRAF.readShortLE();
        image.header.shadowX=myRAF.readShortLE();
        image.header.shadowY=myRAF.readShortLE();
        image.header.shadow=myRAF.readByte();
        image.header.length=myRAF.readIntLE();
        return true;
    }

    private synchronized boolean initializeImage(int index) throws Exception
    {
        if (images == null || index < 0 || index >= imageCnt)
        {
            throw new Exception("Can not initialize image, mirlib do not initialized or invalid index "+index);
        }
        if (images[index]==null || !images[index].initialized)
        {
            myRAF.seek(indexList[index]);
            if (images[index]==null)
            {
                images[index]=new MirImage();
            }
            if (!initImageHeader(images[index]))
            {
                throw new Exception("Image header init failed, mir lib file "+file_name+". image index: "+index);
            }
            images[index].data = new byte[images[index].header.length];
            int read_len = myRAF.read(images[index].data);
            if(read_len != images[index].data.length)
            {
                throw new Exception("Read failed, expected: "+images[index].data.length+", read: "+read_len);
            }
            images[index].initialized = true;
        }
        return true;
    }

    public boolean Initialize() throws Exception
    {
        if (initialized)
        {
            return true;
        }
        if (!file.exists())
        {
            throw new Exception("File not exists: "+file_name);
        }
        if (!file.canRead())
        {
            throw new Exception("File can not read: "+file_name);
        }
        if (!file.isFile())
        {
            throw new Exception("File is not file: "+file_name);
        }

        myRAF = new MyRandomAccessFile(file, "r");
        int libVersion = myRAF.readIntLE();
        if (libVersion != LibVersion)
        {
            throw new Exception("Invalid mir lib file "+file_name+". Wrong version: "+libVersion);
        }
        imageCnt = myRAF.readIntLE();
        images = new MirImage[imageCnt];
        indexList = new int[imageCnt];
        for(int i = 0; i < indexList.length; ++i)
        {
            indexList[i] = myRAF.readIntLE();
        }

        initialized=true;

        return true;
    }

    public MirImage GetMirImage(int index) throws Exception
    {
        if (!initialized)
        {
            throw new Exception("Lib not initialized.");
        }
        initializeImage(index);
        return images[index];
    }

    public int GetImageCount()
    {
        return imageCnt;
    }

    public String GetFilName()
    {
        return file_name;
    }

    public Point getOffset(int index)
    {
        return null;
    }

    public Size getTrueSize(int index)
    {
        return null;
    }

    public boolean visiblePixel(int index, Point pt, boolean b)
    {
        return false;
    }
}
