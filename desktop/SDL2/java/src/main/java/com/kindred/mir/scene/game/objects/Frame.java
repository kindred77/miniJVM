package com.kindred.mir.scene.game.objects;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Frame {
  private int start, count, skip, effectStart, effectCount, effectSkip;
  private int interval, effectInterval;
  private boolean reverse, blend;
}
