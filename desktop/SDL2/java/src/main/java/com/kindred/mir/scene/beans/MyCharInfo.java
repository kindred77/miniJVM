package com.kindred.mir.scene.beans;

import com.kindred.mir.constcode.MirEnums;
import com.kindred.mir.constcode.MirEnums.MirGender;
import com.kindred.mir.constcode.MirEnums.MirJob;

public class MyCharInfo {

  private String name;
  private int level;
  private MirEnums.MirJob job;
  private MirEnums.MirGender gender;

  public MyCharInfo(String name, int level, MirEnums.MirJob job,MirEnums.MirGender gender) {
    this.name=name;
    this.level=level;
    this.job=job;
    this.gender=gender;
  }

  public String getName() {
    return name;
  }

  public int getLevel() {
    return level;
  }

  public MirJob getJob() {
    return job;
  }

  public MirGender getGender() {
    return gender;
  }
}

