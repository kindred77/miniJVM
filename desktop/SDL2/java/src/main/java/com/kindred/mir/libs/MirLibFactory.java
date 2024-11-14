package com.kindred.mir.libs;

public class MirLibFactory {

    public static MirLib ChrSel;

    public static MirLib getMirLib(String file_name)
    {
        MirLib lib = new MirLib(file_name);
        try
        {
            lib.Initialize();
        }
        catch(Exception e)
        {
            System.out.println("Can not initialize mir lib: "+file_name);
            e.printStackTrace();
            return null;
        }
        return lib;
    }
}
