package com.kindred.mir;

import com.kindred.mir.engine.Font;
import com.kindred.mir.util.Point;
import java.util.concurrent.atomic.AtomicBoolean;

public class Settings {

    public static int ScreenWidth = 800;
    public static int ScreenHeight = 600;

    public static final int CellWidth = 48;
    public static final int CellHeight = 32;

    public static boolean IsFullScreen = false;
    public static long DoubleClickIntervalTime=200L;

    public static int BACKGROUND_WORKER_THREADS_CNT=2;
    public static int BACKGROUND_FUTURE_GET_INTERVAL=200;
    public static int BACKGROUND_WORKER_POLL_INTERVAL=200;

    public final static int MIR_INIT_USERITEMSLOTS_SIZE = 5;

    public static boolean IsTargetDead=false;
    public static boolean IsEffect=true;

    public static final String LIB_SUFFIX=".Lib";
    public static final String MAP_SUFFIX=".map";
    public static final String MIR_LIB_BASE_DIR = "../mir_client/";
    public static boolean IS_WEMADEMIR2_TILES_FILE_SPLIT=true;
    public static final int WEMADEMIR2_TILES_FILE_SPLIT_CNT=20000;
    public static final String DataPath = MIR_LIB_BASE_DIR+"Data/",
    MapPath = MIR_LIB_BASE_DIR+"Map/",
    SoundPath = MIR_LIB_BASE_DIR+"Sound/",
    ExtraDataPath = MIR_LIB_BASE_DIR+"Data/Extra/",
    ShadersPath = MIR_LIB_BASE_DIR+"Data/Shaders/",
    MonsterPath = MIR_LIB_BASE_DIR+"Data/Monster/",
    GatePath = MIR_LIB_BASE_DIR+"Data/Gate/",
    NPCPath = MIR_LIB_BASE_DIR+"Data/NPC/",
    CArmourPath = MIR_LIB_BASE_DIR+"Data/CArmour/",
    CWeaponPath = MIR_LIB_BASE_DIR+"Data/CWeapon/",
    CHairPath = MIR_LIB_BASE_DIR+"Data/CHair/",
    AArmourPath = MIR_LIB_BASE_DIR+"Data/AArmour/",
    AWeaponPath = MIR_LIB_BASE_DIR+"Data/AWeapon/",
    AHairPath = MIR_LIB_BASE_DIR+"Data/AHair/",
    ARArmourPath = MIR_LIB_BASE_DIR+"Data/ARArmour/",
    ARWeaponPath = MIR_LIB_BASE_DIR+"Data/ARWeapon/",
    ARHairPath = MIR_LIB_BASE_DIR+"Data/ARHair/",
    CHumEffectPath = MIR_LIB_BASE_DIR+"Data/CHumEffect/",
    AHumEffectPath = MIR_LIB_BASE_DIR+"Data/AHumEffect/",
    ARHumEffectPath = MIR_LIB_BASE_DIR+"Data/ARHumEffect/",
    MountPath = MIR_LIB_BASE_DIR+"Data/Mount/",
    FishingPath = MIR_LIB_BASE_DIR+"Data/Fishing/",
    PetsPath = MIR_LIB_BASE_DIR+"Data/Pet/",
    TransformPath = MIR_LIB_BASE_DIR+"Data/Transform/",
    TransformMountsPath = MIR_LIB_BASE_DIR+"Data/TransformRide2/",
    TransformEffectPath = MIR_LIB_BASE_DIR+"Data/TransformEffect/",
    TransformWeaponEffectPath = MIR_LIB_BASE_DIR+"Data/TransformWeaponEffect/",
    TmpPath=MIR_LIB_BASE_DIR+"Tmp/";

    public static Point GlobalDisplayLocationOffset=new Point(0, 0);

    //public static boolean isImguiUsed=false;
    public static volatile AtomicBoolean IsImguiUsed=new AtomicBoolean(false);

    //fonts
    public static final String MIRFONT="FZSSJW.TTF";
    public static Font FONT_SIZE10;
    public static Font FONT_SIZE15;
    public static Font FONT_SIZE20;
    public static void makeSureImGuiFontsInited() throws Exception{
        if (FONT_SIZE10==null) {
            FONT_SIZE10 =new Font(MIRFONT, 10);
            FONT_SIZE15 =new Font(MIRFONT, 15);
            FONT_SIZE20 =new Font(MIRFONT, 20);
        }
        if (FONT_SIZE10.getImGuiFontID()==0) {
            FONT_SIZE10.initImGuiFont();
            FONT_SIZE15.initImGuiFont();
            FONT_SIZE20.initImGuiFont();
        }
    }
    public static void makeSureSDLFontsInited() throws Exception{
        if (FONT_SIZE10==null) {
            FONT_SIZE10 =new Font(MIRFONT, 10);
            FONT_SIZE15 =new Font(MIRFONT, 15);
            FONT_SIZE20 =new Font(MIRFONT, 20);
        }
        if (FONT_SIZE10.getSDLFontID()==0) {
            FONT_SIZE10.initSDLFont();
            FONT_SIZE15.initSDLFont();
            FONT_SIZE20.initSDLFont();
        }
    }

    public static long getTime()
    {
        return System.currentTimeMillis();
    }

}
