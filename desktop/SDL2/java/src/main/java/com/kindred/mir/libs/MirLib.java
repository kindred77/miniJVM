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

    MirLib(String file_name)
    {
        this.file_name=file_name;
        this.file=new File(file_name);
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
                throw new Exception("Can not initialize image, lib name: "+file_name+", index "+index);
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

    public MirImage GetMirImage(int index)
    {
        if (!initialized)
        {
            System.out.println("Lib not initialized.");
            return null;
        }
        try
        {
            if (!initializeImage(index))
            {
                System.out.println("Can not initialize image.");
                return null;
            }
        }
        catch(Exception e)
        {
            System.out.println("Can not initialize image: ");
            e.printStackTrace();
            return null;
        }
        return images[index];
    }

    public MirImage[] GetMirImages(int[] indexes)
    {
        MirImage[] images=new MirImage[indexes.length];
        for (int i = 0; i < indexes.length; i++)
        {
            images[i] = GetMirImage(i);
        }

        return images;
    }

    public int GetImageCount()
    {
        return imageCnt;
    }

    public String GetFilName()
    {
        return file_name;
    }


}
