package com.kindred.mir.libs;

import com.kindred.mir.Settings;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage.ImageEffect;
import com.kindred.mir.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class MirLibFactory implements Runnable{

    public static volatile boolean Loaded;
    public static volatile int Progress;

    public static final MirLib
    ChrSel = new MirLib(Settings.DataPath + "ChrSel"),
    Prguse = new MirLib(Settings.DataPath + "Prguse"),
    //Prguse2 = new MirLib(Settings.DataPath + "Prguse2"),
    Prguse3 = new MirLib(Settings.DataPath + "Prguse3"),
    BuffIcon = new MirLib(Settings.DataPath + "BuffIcon"),
    Help = new MirLib(Settings.DataPath + "Help"),
    MiniMap = new MirLib(Settings.DataPath + "MMap"),
    //Title = new MirLib(Settings.DataPath + "Title"),
    MagIcon = new MirLib(Settings.DataPath + "MagIcon"),
    MagIcon2 = new MirLib(Settings.DataPath + "MagIcon2"),
    Magic = new MirLib(Settings.DataPath + "Magic"),
    Magic2 = new MirLib(Settings.DataPath + "Magic2"),
    Magic3 = new MirLib(Settings.DataPath + "Magic3"),
    Effect = new MirLib(Settings.DataPath + "Effect"),
    MagicC = new MirLib(Settings.DataPath + "MagicC"),
    GuildSkill = new MirLib(Settings.DataPath + "GuildSkill");

    public static final MirLib
    Background = new MirLib(Settings.DataPath + "Background");

    public static final MirLib
    Dragon = new MirLib(Settings.DataPath + "Dragon");

    private final static MirLib[] mapLibs=new MirLib[400];

    //Items
    public static final MirLib
    Items = new MirLib(Settings.DataPath + "Items"),
    StateItems = new MirLib(Settings.DataPath + "StateItem"),
    FloorItems = new MirLib(Settings.DataPath + "DNItems");

    //Deco
    public static final MirLib
    Deco = new MirLib(Settings.DataPath + "Deco");

    public static final MirLib[] CArmours = new MirLib[42],
    CWeapons = new MirLib[55],
    CHair = new MirLib[9],
    CHumEffect = new MirLib[3],
    AArmours = new MirLib[17],
    AWeaponsL = new MirLib[14],
    AWeaponsR = new MirLib[14],
    AHair = new MirLib[9],
    AHumEffect = new MirLib[3],
    ARArmours = new MirLib[17],
    ARWeapons = new MirLib[19],
    ARWeaponsS = new MirLib[19],
    ARHair = new MirLib[9],
    ARHumEffect = new MirLib[3],
    Monsters = new MirLib[406],
    Gates = new MirLib[2],
    Mounts = new MirLib[12],
    NPCs = new MirLib[200],
    Fishing = new MirLib[2],
    Pets = new MirLib[12],
    Transform = new MirLib[28],
    TransformMounts = new MirLib[28],
    TransformEffect = new MirLib[2],
    TransformWeaponEffect = new MirLib[1];

    private static final int Count = mapLibs.length + Monsters.length + Gates.length + NPCs.length + CArmours.length +
    CHair.length + CWeapons.length + AArmours.length + AHair.length + AWeaponsL.length + AWeaponsR.length +
    ARArmours.length + ARHair.length + ARWeapons.length + ARWeaponsS.length +
    CHumEffect.length + AHumEffect.length + ARHumEffect.length + Mounts.length + Fishing.length + Pets.length +
    Transform.length + TransformMounts.length + TransformEffect.length + TransformWeaponEffect.length + 17;

//    public static MirLib getMirLib(String file_name)
//    {
//        MirLib lib = new MirLib(MIR_LIB_BASE_DIR+file_name);
//        try
//        {
//            lib.Initialize();
//        }
//        catch(Exception e)
//        {
//            System.out.println("Can not initialize mir lib: "+file_name);
//            e.printStackTrace();
//            return null;
//        }
//        return lib;
//    }

    public static void StartToInit() {

        //Wiz/War/Tao
        for (int i = 0; i < CArmours.length; i++)
            CArmours[i] = new MirLib(Settings.CArmourPath + String.format("%02d", i));

        for (int i = 0; i < CHair.length; i++)
            CHair[i] = new MirLib(Settings.CHairPath + String.format("%02d", i));

        for (int i = 0; i < CWeapons.length; i++)
            CWeapons[i] = new MirLib(Settings.CWeaponPath + String.format("%02d", i));

        for (int i = 0; i < CHumEffect.length; i++)
            CHumEffect[i] = new MirLib(Settings.CHumEffectPath + String.format("%02d", i));

//Assassin
        for (int i = 0; i < AArmours.length; i++)
            AArmours[i] = new MirLib(Settings.AArmourPath + String.format("%02d", i));

        for (int i = 0; i < AHair.length; i++)
            AHair[i] = new MirLib(Settings.AHairPath + String.format("%02d", i));

        for (int i = 0; i < AWeaponsL.length; i++)
            AWeaponsL[i] = new MirLib(Settings.AWeaponPath + String.format("%02d", i) + " L");

        for (int i = 0; i < AWeaponsR.length; i++)
            AWeaponsR[i] = new MirLib(Settings.AWeaponPath + String.format("%02d", i) + " R");

        for (int i = 0; i < AHumEffect.length; i++)
            AHumEffect[i] = new MirLib(Settings.AHumEffectPath + String.format("%02d", i));

//Archer
        for (int i = 0; i < ARArmours.length; i++)
            ARArmours[i] = new MirLib(Settings.ARArmourPath + String.format("%02d", i));

        for (int i = 0; i < ARHair.length; i++)
            ARHair[i] = new MirLib(Settings.ARHairPath + String.format("%02d", i));

        for (int i = 0; i < ARWeapons.length; i++)
            ARWeapons[i] = new MirLib(Settings.ARWeaponPath + String.format("%02d", i));

        for (int i = 0; i < ARWeaponsS.length; i++)
            ARWeaponsS[i] = new MirLib(Settings.ARWeaponPath + String.format("%02d", i) + " S");

        for (int i = 0; i < ARHumEffect.length; i++)
            ARHumEffect[i] = new MirLib(Settings.ARHumEffectPath + String.format("%02d", i));

//Other
        for (int i = 0; i < Monsters.length; i++)
            Monsters[i] = new MirLib(Settings.MonsterPath + String.format("%03d", i));

        for (int i = 0; i < Gates.length; i++)
            Gates[i] = new MirLib(Settings.GatePath + String.format("%02d", i));

        for (int i = 0; i < NPCs.length; i++)
            NPCs[i] = new MirLib(Settings.NPCPath + String.format("%02d", i));

        for (int i = 0; i < Mounts.length; i++)
            Mounts[i] = new MirLib(Settings.MountPath + String.format("%02d", i));

        for (int i = 0; i < Fishing.length; i++)
            Fishing[i] = new MirLib(Settings.FishingPath + String.format("%02d", i));

        for (int i = 0; i < Pets.length; i++)
            Pets[i] = new MirLib(Settings.PetsPath + String.format("%02d", i));

        for (int i = 0; i < Transform.length; i++)
            Transform[i] = new MirLib(Settings.TransformPath + String.format("%02d", i));

        for (int i = 0; i < TransformMounts.length; i++)
            TransformMounts[i] = new MirLib(Settings.TransformMountsPath + String.format("%02d", i));

        for (int i = 0; i < TransformEffect.length; i++)
            TransformEffect[i] = new MirLib(Settings.TransformEffectPath + String.format("%02d", i));

        for (int i = 0; i < TransformWeaponEffect.length; i++)
            TransformWeaponEffect[i] = new MirLib(Settings.TransformWeaponEffectPath + String.format("%02d", i));


        //map libs
        //wemade mir2 (allowed from 0-99)
        mapLibs[0] = new MirLib(Settings.DataPath+"Map\\WemadeMir2\\Tiles");
        mapLibs[1] = new MirLib(Settings.DataPath+"Map\\WemadeMir2\\Smtiles");
        mapLibs[2] = new MirLib(Settings.DataPath+"Map\\WemadeMir2\\Objects");
        for (int i = 2; i < 24; i++) {
            mapLibs[i + 1] = new MirLib(Settings.DataPath+"Map\\WemadeMir2\\Objects" + i);
        }
        //shanda mir2 (allowed from 100-199)
        mapLibs[100] = new MirLib(Settings.DataPath+"Map\\ShandaMir2\\Tiles");
        for (int i = 1; i < 10; i++) {
            mapLibs[100 + i] = new MirLib(Settings.DataPath+"Map\\ShandaMir2\\Tiles" + (i + 1));
        }
        mapLibs[110] = new MirLib(Settings.DataPath+"Map\\ShandaMir2\\SmTiles");
        for (int i = 1; i < 10; i++)
        {
            mapLibs[110 + i] = new MirLib(Settings.DataPath+"Map\\ShandaMir2\\SmTiles" + (i + 1));
        }
        mapLibs[120] = new MirLib(Settings.DataPath+"Map\\ShandaMir2\\Objects");
        for (int i = 1; i < 31; i++) {
            mapLibs[120 + i] = new MirLib(Settings.DataPath+"Map\\ShandaMir2\\Objects" + (i + 1));
        }
        mapLibs[190] = new MirLib(Settings.DataPath+"Map\\ShandaMir2\\AniTiles1");
        //wemade mir3 (allowed from 200-299)
        String[] mapState = { "", "wood\\", "sand\\", "snow\\", "forest\\"};
        for (int i = 0; i < mapState.length; i++) {
            mapLibs[200 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Tilesc");
            mapLibs[201 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Tiles30c");
            mapLibs[202 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Tiles5c");
            mapLibs[203 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Smtilesc");
            mapLibs[204 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Housesc");
            mapLibs[205 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Cliffsc");
            mapLibs[206 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Dungeonsc");
            mapLibs[207 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Innersc");
            mapLibs[208 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Furnituresc");
            mapLibs[209 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Wallsc");
            mapLibs[210 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "smObjectsc");
            mapLibs[211 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Animationsc");
            mapLibs[212 +(i*15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Object1c");
            mapLibs[213 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\WemadeMir3\\" + mapState[i] + "Object2c");
        }
        mapState = new String[] { "", "wood", "sand", "snow", "forest"};
        //shanda mir3 (allowed from 300-399)
        for (int i = 0; i < mapState.length; i++) {
            mapLibs[300 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Tilesc" + mapState[i]);
            mapLibs[301 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Tiles30c" + mapState[i]);
            mapLibs[302 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Tiles5c" + mapState[i]);
            mapLibs[303 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Smtilesc" + mapState[i]);
            mapLibs[304 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Housesc" + mapState[i]);
            mapLibs[305 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Cliffsc" + mapState[i]);
            mapLibs[306 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Dungeonsc" + mapState[i]);
            mapLibs[307 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Innersc" + mapState[i]);
            mapLibs[308 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Furnituresc" + mapState[i]);
            mapLibs[309 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Wallsc" + mapState[i]);
            mapLibs[310 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "smObjectsc" + mapState[i]);
            mapLibs[311 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Animationsc" + mapState[i]);
            mapLibs[312 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Object1c" + mapState[i]);
            mapLibs[313 + (i * 15)] = new MirLib(Settings.DataPath+"Map\\ShandaMir3\\" + "Object2c" + mapState[i]);
        }

        //开启后台线程异步加载其它素材
        Thread thread = new Thread(new MirLibFactory());
        thread.start();

        try {
            //基础素材,如果加载不成功就不能启动
            loadBaseLibraries();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        //TODO 异步加载素材会有冲突，而且启用异步加载素材似乎也什么用，暂时不用启用
        //loadGameLibraries();
        //System.out.println("All libs initialized.");
    }

    private static void loadBaseLibraries()
    {
        if(!ChrSel.Initialize(false)) {
            throw new RuntimeException("ChrSel initialize failed.");
        }
        Progress++;

        if(!Prguse.Initialize(false)) {
            throw new RuntimeException("Prguse initialize failed.");
        }
        Progress++;

        //Prguse2.Initialize();
        //Progress++;

        if(!Prguse3.Initialize(false)) {
            throw new RuntimeException("Prguse3 initialize failed.");
        }
        Progress++;

        //Title.Initialize();
        //Progress++;
    }

    private static void loadGameLibraries()
    {
        Dragon.Initialize(true);
        Progress++;

        BuffIcon.Initialize(true);
        Progress++;

        Help.Initialize(true);
        Progress++;

        MiniMap.Initialize(true);
        Progress++;

        MagIcon.Initialize(true);
        Progress++;
        MagIcon2.Initialize(true);
        Progress++;

        Magic.Initialize(true);
        Progress++;
        Magic2.Initialize(true);
        Progress++;
        Magic3.Initialize(true);
        Progress++;
        MagicC.Initialize(true);
        Progress++;

        Effect.Initialize(true);
        Progress++;

        GuildSkill.Initialize(true);
        Progress++;

        Background.Initialize(true);
        Progress++;

        Deco.Initialize(true);
        Progress++;

        Items.Initialize(true);
        Progress++;
        StateItems.Initialize(true);
        Progress++;
        FloorItems.Initialize(true);
        Progress++;
        for (int i = 0; i < mapLibs.length; i++) {
            if (mapLibs[i] == null)
                mapLibs[i] = new MirLib("");
            else
                mapLibs[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < Monsters.length; i++) {
            Monsters[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < Gates.length; i++) {
            Gates[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < NPCs.length; i++) {
            NPCs[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < CArmours.length; i++) {
            CArmours[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < CHair.length; i++) {
            CHair[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < CWeapons.length; i++) {
            CWeapons[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < AArmours.length; i++) {
            AArmours[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < AHair.length; i++) {
            AHair[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < AWeaponsL.length; i++) {
            AWeaponsL[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < AWeaponsR.length; i++) {
            AWeaponsR[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < ARArmours.length; i++) {
            ARArmours[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < ARHair.length; i++) {
            ARHair[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < ARWeapons.length; i++) {
            ARWeapons[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < ARWeaponsS.length; i++) {
            ARWeaponsS[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < CHumEffect.length; i++) {
            CHumEffect[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < AHumEffect.length; i++) {
            AHumEffect[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < ARHumEffect.length; i++) {
            ARHumEffect[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < Mounts.length; i++) {
            Mounts[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < Fishing.length; i++) {
            Fishing[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < Pets.length; i++) {
            Pets[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < Transform.length; i++) {
            Transform[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < TransformEffect.length; i++) {
            TransformEffect[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < TransformWeaponEffect.length; i++) {
            TransformWeaponEffect[i].Initialize(true);
            Progress++;
        }
        for (int i = 0; i < TransformMounts.length; i++) {
            TransformMounts[i].Initialize(true);
            Progress++;
        }
        Loaded = true;
    }

    public static MirLib getMapLib(int idx) {
        return mapLibs[idx];
    }

    public static void tryToDraw(
        long targetSurface,
        MirLib lib,
        int imgIdx,
        ImageEffect effect,
        int x,
        int y,
        Rectangle srcRect,
        boolean isBlend
    ) {
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
