package com.kindred.mir;

import com.kindred.mir.scene.MirScene;
import com.kindred.mir.util.ExecutionService;
import java.util.ArrayList;
import java.util.List;

public class Env {

  public static ExecutionService BackGroundExeService;

  public static MirScene ActiveScene = null;

  public static long Time = 0L;
  public static long LastRunTime = 0L;

  public static boolean CanRun;
  public static boolean CanMove;

  //队伍列表
  public static List<Long> MyGroupList=new ArrayList<>();

}
