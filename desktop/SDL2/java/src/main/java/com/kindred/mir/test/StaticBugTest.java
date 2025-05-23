package com.kindred.mir.test;

public class StaticBugTest implements Runnable{

  public static volatile int Count;
  public static volatile boolean Loaded=false;

  static {
    //开启后台线程异步加载其它素材
    Thread thread = new Thread(new StaticBugTest());
    thread.start();

    try {
      //基础素材,如果加载不成功就不能启动
      LoadBaseLibraries();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    while(!Loaded){
      try{Thread.sleep(500);} catch (Exception e) {}
    }
  }

  @Override
  public void run() {

    LoadGameLibraries();
  }

  private static void LoadBaseLibraries() {
    System.out.println("LoadBaseLibraries-----000-----");
    Loaded=true;
  }
  private static void LoadGameLibraries() {
    System.out.println("LoadGameLibraries-----000-----");
    Count = 1;
    System.out.println("LoadGameLibraries-----111-----");
  }

}
