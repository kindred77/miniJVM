package com.kindred.mir.scene.game.bean;

import com.kindred.mir.GameCommon.BuffType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Buff {
  private BuffType type;
  private String caster;
  private boolean visible;
  private long objectID;
  private long expire;
  private int[] values;
  private boolean infinite;
}
