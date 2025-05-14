package com.kindred.mir.libs;

import com.kindred.mir.util.MyRandomAccessFile;

import java.io.File;

public class MirLib {
    private final int LibVersion = 2;
    private boolean initialized = false;
    private int imageCnt = 0;
    private File file=null;
    private MyRandomAccessFile myRAF=null;

    private MirImage[] images;
    private int[] indexList;

    MirLib(String fileName)
    {
        this.file=new File(fileName+MirLibFactory.LIB_SUFFIX);
    }

    private synchronized boolean initializeImage(int index) throws Exception
    {
        if (images == null || index < 0 || index >= imageCnt)
        {
            throw new Exception("Can not initialize image, mirlib do not initialized or invalid index "+index);
        }
        if (images[index]==null)
        {
            myRAF.seek(indexList[index]);
            try
            {
                images[index]=new MirImage(myRAF, index);
            }
            catch(Exception e)
            {
                throw new Exception("Can not initialize image, lib name: "+this.file.getAbsolutePath()+", index "+index);
            }
        }
        return true;
    }

    boolean Initialize() throws Exception
    {
        if (initialized)
        {
            return true;
        }
        if (!file.exists())
        {
            throw new Exception("File not exists: "+this.file.getAbsolutePath());
        }
        if (!file.canRead())
        {
            throw new Exception("File can not read: "+this.file.getAbsolutePath());
        }
        if (!file.isFile())
        {
            throw new Exception("File is not file: "+this.file.getAbsolutePath());
        }

        myRAF = new MyRandomAccessFile(file, "r");
        int libVersion = myRAF.readIntLE();
        if (libVersion != LibVersion)
        {
            throw new Exception("Invalid mir lib file "+this.file.getAbsolutePath()+". Wrong version: "+libVersion);
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
        try
        {
            if (!initializeImage(index))
            {
                throw new Exception("Can not initialize image.");
            }
        }
        catch(Exception e)
        {
            throw e;
        }
        return images[index];
    }

    public MirImage[] GetMirImages(int[] indexes) throws Exception
    {
        MirImage[] images=new MirImage[indexes.length];
        for (int i = 0; i < indexes.length; i++)
        {
            images[i] = GetMirImage(indexes[i]);
        }

        return images;
    }

    public int GetImageCount()
    {
        return imageCnt;
    }

    public String GetFilName()
    {
        return this.file.getAbsolutePath();
    }


}
