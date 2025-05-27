package com.kindred.mir.test;

class TestMirLib {
  private final Object lock = new Object();
  boolean Initialize(){
    System.out.println("Initialize-------000-------");
    synchronized (lock){
      throw new RuntimeException("error");
    }
  }

  boolean TryToInitialize(boolean isSilence) {
    System.out.println("TryToInitialize-------000-------");
    try{
      System.out.println("TryToInitialize-------111-------");
      return Initialize();
    } catch (Exception e) {
      System.out.println("TryToInitialize-------2222-------");
      e.printStackTrace();
      System.out.println("TryToInitialize-------333-------");
    }

    return false;
  }
}

public class ExceptionInThreadBugTest implements Runnable{

  private static final TestMirLib testMirLib=new TestMirLib();
  private static void loadBaseLibraries() throws Exception {
    System.out.println("loadBaseLibraries-----000-----");
  }
  public static void main(String args[]) throws Exception{
    new Thread(new ExceptionInThreadBugTest()).start();
    loadBaseLibraries();
  }

  @Override
  public void run() {
    LoadGameLibraries();
  }

  private static void LoadGameLibraries() {
    System.out.println("LoadGameLibraries-----000-----");
    testMirLib.TryToInitialize(true);
    System.out.println("LoadGameLibraries-----111-----");
  }

}
