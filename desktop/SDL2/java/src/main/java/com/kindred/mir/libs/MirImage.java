package com.kindred.mir.libs;

class MirImageHeader
{
    public short width, height, x, y, shadowX, shadowY;
    public byte shadow;
    public int length;
}

public class MirImage {
    public MirImageHeader header;
    public boolean initialized = false;
    //const int index = -1;
    public byte[] data;

    public int getDataLengthInHeader()
    {
        return header.length;
    }
}
