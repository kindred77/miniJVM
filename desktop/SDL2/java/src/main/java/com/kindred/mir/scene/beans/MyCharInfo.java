package com.kindred.mir.scene.beans;

import com.kindred.mir.constcode.MirEnums;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyCharInfo {

  private String name;
  private int level;
  private MirEnums.MirJob job;
  private MirEnums.MirGender gender;
}

