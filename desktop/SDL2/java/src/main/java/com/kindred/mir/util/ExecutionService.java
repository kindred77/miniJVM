package com.kindred.mir.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ExecutionService {

  public static class Future<T> {

    @FunctionalInterface
    public interface Callable<T> {
      T doAction() throws Exception;
    }

    //Lock lock;
    boolean isDone=false;
    T obj;
    Callable action;

    protected Future(Callable action) {
      //lock=new ReentrantLock();
      this.action=action;
      //lock.lock();
    }

    protected void done() {
      isDone=true;
      //lock.unlock();
    }

    public T get() {
      while(!isDone) {
        try{Thread.sleep(1000);}catch(Exception e){}
      }
      return obj;
    }
  }

  class Worker implements Runnable {

    @Override
    public void run() {
      while (!isShutdown) {
        Future future=null;
        //synchronized (queue) {
          future=queue.poll();
        //}
        if (future==null) {
          try{Thread.sleep(500);}catch(Exception e){}
          continue;
        }

        try {
          future.obj=future.action.doAction();
        } catch(Exception e) {
          e.printStackTrace();
          future.obj=null;
        } finally {
          future.done();
        }
      }
    }
  }

  private Thread threads[];
  //private LinkedList<Future> queue;
  private ConcurrentLinkedQueue<Future> queue;
  private volatile boolean isShutdown=false;

  public ExecutionService(int threadPoolSize) {
    queue=new ConcurrentLinkedQueue();
    threads=new Thread[threadPoolSize];
    for (int i = 0; i<threadPoolSize; ++i) {
      threads[i]=new Thread(new Worker());
      threads[i].start();
    }
  }

  public Future submit(Future.Callable action) {
    Future future=new Future(action);
    boolean isPut=false;
    //synchronized (queue) {
      isPut=queue.offer(future);
    //}
    if (!isPut) {
      future.done();
      return null;
    }
    return future;
  }

  public void shutdown() {
    isShutdown=true;
  }

}
