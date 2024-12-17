package com.kindred.mir.test;

import java.util.LinkedList;

public class TestQueue {

  class Worker implements Runnable {

    @Override
    public void run() {
      while (true) {
        String str;
        synchronized (queue) {
          str=queue.poll();
        }
        if (str==null) {
          System.out.println("------continue poll-----get null----"+queue.size());
          try{Thread.sleep(1000);}catch(Exception e){}
          continue;
        }
        System.out.println("get str: "+str);
      }
    }
  }

  LinkedList<String> queue = new LinkedList();
  //ConcurrentLinkedQueue<String> queue=new ConcurrentLinkedQueue();

  public TestQueue() {
    Thread thread=new Thread(new Worker());
    thread.start();
  }

  public void push(String e) {
    this.queue.offer(e);
  }

  public static void main(String args[]) throws Exception
  {
    TestQueue test = new TestQueue();
    Thread.sleep(1000);
    synchronized (test.queue) {
      test.push("test");
    }
  }

}
