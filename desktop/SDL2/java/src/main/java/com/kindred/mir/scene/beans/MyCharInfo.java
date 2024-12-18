package com.kindred.mir.scene.beans;

import com.kindred.mir.constcode.MirEnums;

public class MyCharInfo {

  private String name;
  private int level;
  private MirEnums.MirJob job;

  public MyCharInfo(String name, int level, MirEnums.MirJob job) {
    this.name=name;
    this.level=level;
    this.job=job;
  }
}
