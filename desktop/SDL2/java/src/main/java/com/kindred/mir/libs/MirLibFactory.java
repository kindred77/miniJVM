package com.kindred.mir.libs;

public class MirLibFactory {

    public static String MIR_LIB_DIR = "../mir_client/";

    public static String ChrSel = "ChrSel.Lib";

    public static String Prguse = "Prguse.Lib";

    public static MirLib getMirLib(String file_name)
    {
        MirLib lib = new MirLib(MIR_LIB_DIR+file_name);
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
