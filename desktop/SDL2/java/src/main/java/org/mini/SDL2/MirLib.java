package org.mini.SDL2;

import org.mini.SDL2.util.BinaryReader;

import java.io.File;

public class MirLib {
    private final int LibVersion = 2;
    private boolean initialized = false;
    private int imageCnt = 0;
    private String file_name;
    private File file=null;
    private BinaryReader br=null;

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
        image.header.width=br.readShortLE();
        image.header.height=br.readShortLE();
        image.header.x=br.readShortLE();
        image.header.y=br.readShortLE();
        image.header.shadowX=br.readShortLE();
        image.header.shadowY=br.readShortLE();
        image.header.shadow=br.readByte();
        image.header.length=br.readIntLE();
        System.out.println("x: "+image.header.x+", y: "+image.header.y+", width: "+image.header.width+", height: "+image.header.height+", length: "+image.header.length);
        return true;
    }

    private boolean initializeImage(int index) throws Exception
    {
        if (!initialized)
        {
            throw new Exception("Lib not initialized.");
        }
        if (images == null || index < 0 || index >= imageCnt)
        {
            throw new Exception("Can not initialize image, mirlib do not initialized or invalid index "+index);
        }
        if (images[index]==null || !images[index].initialized)
        {
            br.seek(indexList[index]);
            if (images[index]==null)
            {
                images[index]=new MirImage();
            }
            if (!initImageHeader(images[index]))
            {
                throw new Exception("Image header init failed, mir lib file "+file_name+". image index: "+index);
            }
            images[index].data = new byte[images[index].header.length];
            int read_len = br.read(images[index].data);
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

        br = new BinaryReader(file);
        int libVersion = br.readIntLE();
        if (libVersion != LibVersion)
        {
            throw new Exception("Invalid mir lib file "+file_name+". Wrong version: "+libVersion);
        }
        imageCnt = br.readIntLE();
        images = new MirImage[imageCnt];
        indexList = new int[imageCnt];
        for(int i = 0; i < indexList.length; ++i)
        {
            indexList[i] = br.readIntLE();
        }

        initialized=true;

        return true;
    }

    public MirImage GetMirImage(int index) throws Exception
    {
        initializeImage(index);
        return images[index];
    }

    public String GetFilName()
    {
        return file_name;
    }
}
