package com.kindred.mir.libs;

import com.kindred.mir.Settings;
import com.kindred.mir.util.MyRandomAccessFile;

import java.io.File;

/**
 * 只能通过MirLibFactory使用
 */
public class MirLib {
    //注意：miniJVM加锁不要使用synchronized代码块的方式，有问题
    private final Object lock = new Object();

    private final int LibVersion = 2;
    private boolean initialized = false;
    private int imageCnt = 0;
    private File file=null;
    private MyRandomAccessFile myRAF=null;

    private MirImage[] images;
    private int[] indexList;

    MirLib(String fileName)
    {
        this.file=new File(fileName + Settings.LIB_SUFFIX);
    }

    private boolean initializeImage(int index)
    {
        if (!Initialize(false)) {
            return false;
        }
        synchronized (lock) {
            if (images == null) {
                System.out.println("Error: Can not initialize image, mirlib do not initialized: "+this.file.getName());
                return false;
            }
            if(index < 0 || index >= imageCnt) {
                System.out.println("Error: Can not initialize image, invalid index: "+index+", total images: "+imageCnt);
                return false;
            }
            if (images[index]==null) {
                try {
                    myRAF.seek(indexList[index]);
                    images[index]=new MirImage(myRAF, index);
                } catch(Exception e) {
                    e.printStackTrace();
                    System.out.println("Error: Can not initialize image, lib name: "+this.file.getAbsolutePath()+", index "+index);
                    return false;
                }
            }
        }

        return true;
    }

    boolean Initialize(boolean isSilence) {
        synchronized (lock) {
            if (initialized) {
                return true;
            }
            if (!file.exists()) {
                if(!isSilence){
                    System.out.println("Error: File not exists: "+this.file.getAbsolutePath());
                }
                return false;
            }
            if (!file.canRead()) {
                if(!isSilence){
                    System.out.println("Error: File can not read: "+this.file.getAbsolutePath());
                }
                return false;
            }
            if (!file.isFile()) {
                if(!isSilence){
                    System.out.println("Error: File is not file: "+this.file.getAbsolutePath());
                }
                return false;
            }
            try {
                myRAF = new MyRandomAccessFile(file, "r");
                int libVersion = myRAF.readIntLE();
                if (libVersion != LibVersion) {
                    throw new Exception("Invalid mir lib file "+this.file.getAbsolutePath()+". Wrong version: "+libVersion);
                }
                imageCnt = myRAF.readIntLE();
                images = new MirImage[imageCnt];
                indexList = new int[imageCnt];
                for(int i = 0; i < indexList.length; ++i) {
                    indexList[i] = myRAF.readIntLE();
                }
            } catch (Exception e) {
                if(!isSilence){
                    e.printStackTrace();
                    System.out.println("Error: Invalid mir lib file: "+this.file.getAbsolutePath());
                }
                try {
                    if (myRAF != null) {
                        myRAF.close();
                    }
                } catch (Exception ex) {}

                return false;
            }

            initialized=true;
        }
        return true;
    }

    public MirImage GetMirImage(int index)
    {
        if (!initializeImage(index)) {
            return null;
        }
        return images[index];
    }

    public MirImage[] GetMirImages(int[] indexes) throws Exception
    {
        MirImage[] images=new MirImage[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            images[i] = GetMirImage(indexes[i]);
        }

        return images;
    }

    public int GetImageCount() {
        if (!Initialize(false)) {
            throw new RuntimeException("Lib not initialized.");
        }
        return imageCnt;
    }

    public String GetFilName()
    {
        return this.file.getAbsolutePath();
    }

    public boolean isInitialized() {
        return initialized;
    }
}
