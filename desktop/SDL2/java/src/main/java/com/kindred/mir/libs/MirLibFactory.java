package com.kindred.mir.libs;

import com.kindred.mir.Settings;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage.ImageEffect;
import com.kindred.mir.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class MirLibFactory {

    public static String LIB_SUFFIX=".Lib";
    public static String MAP_SUFFIX=".map";

    public static String MIR_LIB_BASE_DIR = "../mir_client/";

    public static String ChrSel = "ChrSel";

    public static String Prguse = "Prguse";
    public static String Prguse3 = "Prguse3";

    public static String Magic2 = "Magic2";

    public static String Magic3 = "Magic3";

    public static String Background="Background";

    private static MirLib[] mapLibs=new MirLib[400];

    static {
        initMapLibs();
    }

    public static MirLib getMirLib(String file_name)
    {
        MirLib lib = new MirLib(MIR_LIB_BASE_DIR+file_name);
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

    public static void initMapLibs() {
        //wemade mir2 (allowed from 0-99)
        mapLibs[0] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir2\\Tiles");
        mapLibs[1] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir2\\Smtiles");
        mapLibs[2] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir2\\Objects");
        for (int i = 2; i < 24; i++) {
            mapLibs[i + 1] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir2\\Objects" + i);
        }
        //shanda mir2 (allowed from 100-199)
        mapLibs[100] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir2\\Tiles");
        for (int i = 1; i < 10; i++) {
            mapLibs[100 + i] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir2\\Tiles" + (i + 1));
        }
        mapLibs[110] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir2\\SmTiles");
        for (int i = 1; i < 10; i++)
        {
            mapLibs[110 + i] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir2\\SmTiles" + (i + 1));
        }
        mapLibs[120] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir2\\Objects");
        for (int i = 1; i < 31; i++) {
            mapLibs[120 + i] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir2\\Objects" + (i + 1));
        }
        mapLibs[190] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir2\\AniTiles1");
        //wemade mir3 (allowed from 200-299)
        String[] mapState = { "", "wood\\", "sand\\", "snow\\", "forest\\"};
        for (int i = 0; i < mapState.length; i++) {
            mapLibs[200 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Tilesc");
            mapLibs[201 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Tiles30c");
            mapLibs[202 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Tiles5c");
            mapLibs[203 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Smtilesc");
            mapLibs[204 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Housesc");
            mapLibs[205 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Cliffsc");
            mapLibs[206 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Dungeonsc");
            mapLibs[207 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Innersc");
            mapLibs[208 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Furnituresc");
            mapLibs[209 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Wallsc");
            mapLibs[210 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "smObjectsc");
            mapLibs[211 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Animationsc");
            mapLibs[212 +(i*15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Object1c");
            mapLibs[213 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\WemadeMir3\\" + mapState[i] + "Object2c");
        }
        mapState = new String[] { "", "wood", "sand", "snow", "forest"};
        //shanda mir3 (allowed from 300-399)
        for (int i = 0; i < mapState.length; i++) {
            mapLibs[300 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Tilesc" + mapState[i]);
            mapLibs[301 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Tiles30c" + mapState[i]);
            mapLibs[302 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Tiles5c" + mapState[i]);
            mapLibs[303 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Smtilesc" + mapState[i]);
            mapLibs[304 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Housesc" + mapState[i]);
            mapLibs[305 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Cliffsc" + mapState[i]);
            mapLibs[306 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Dungeonsc" + mapState[i]);
            mapLibs[307 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Innersc" + mapState[i]);
            mapLibs[308 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Furnituresc" + mapState[i]);
            mapLibs[309 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Wallsc" + mapState[i]);
            mapLibs[310 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "smObjectsc" + mapState[i]);
            mapLibs[311 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Animationsc" + mapState[i]);
            mapLibs[312 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Object1c" + mapState[i]);
            mapLibs[313 + (i * 15)] = new MirLib(MIR_LIB_BASE_DIR+"Map\\ShandaMir3\\" + "Object2c" + mapState[i]);
        }

        for(MirLib lib : mapLibs) {
            if(null != lib){
                try{
                    lib.Initialize();
                } catch(Exception e) {
                    //System.out.println(lib.GetFilName() + " load failed: " + e.getMessage());
                }
            }
        }
    }

    public static MirLib getMapLib(int idx) {
        return mapLibs[idx];
    }

    public static void tryToDraw(
        long targetSurface,
        String libName,
        int imgIdx,
        ImageEffect effect,
        int x,
        int y,
        Rectangle srcRect,
        boolean isBlend
    ) {
        MirLib lib=MirLibFactory.getMirLib(libName);
        if(null != lib){
            MirImage img=null;
            try {
                img = lib.GetMirImage(imgIdx);
            } catch (Exception e) {
                System.out.println("Warn: can not draw MirImage: "+e.getMessage());
                return;
            }
            long srcSurface=img.getSurface(effect);
            int[] rct = null;
            if(srcRect!=null){
                rct=new int[]{srcRect.getLeft(),srcRect.getTop(),srcRect.getRight(),srcRect.getBottom()};
            }
            if(!isBlend) {
                MirJNI.Mir_SurfaceBlendNormalTransparent(targetSurface,
                    srcSurface,x, y,rct, 1,
                    0,0,0);
            } else {
                MirJNI.Mir_SurfaceBlendAddTransparent(targetSurface,
                    srcSurface, x, y, rct,
                    1,0,0,0);
            }
        }

    }
}
