package com.kindred.mir.test;

import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import lombok.Data;

public class TestLombok {

  @FunctionalInterface
  public interface Action {
    void doAction();

    @Override
    String toString();
  }

  @Data
  @Builder
  public static class Bean {

    @Data
    @Builder
    public static class SubBean {
      private int id;
      private List<String> strs;
    }
    private Action action;
    private String name;
    private Long id;
    private SubBean subBean;
  }

  public static void main(String args[]) throws Exception {
    Bean bean=Bean.builder().action(new Action(){
      @Override
      public void doAction(){
        System.out.println("do action");
      }

      @Override
      public String toString() {
        return "action object";
      }
    }).id(1L).name("name").subBean(Bean.SubBean.builder().id(2).strs(Arrays.asList("str1")).build()).build();

    System.out.println(bean);

    System.out.println("---------------------------");

    bean.getAction().doAction();
  }

}
