package com.kindred.mir.scene.game.objects;

import lombok.Getter;
import lombok.Setter;

public class Frame {
  @Setter
  @Getter
  private int start;
  @Setter
  @Getter
  private int count;
  @Setter
  @Getter
  private int skip;
  @Setter
  @Getter
  private int effectStart;
  @Setter
  @Getter
  private int effectCount;
  @Setter
  @Getter
  private int effectSkip;
  @Setter
  @Getter
  private int interval, effectInterval;
  @Setter
  @Getter
  private boolean reverse;
  private boolean blend;

  public int getOffSet() {
    return count + skip;
  }

  public int getEffectOffSet()
  {
    return effectCount + effectSkip;
  }

  public Frame(int start, int count, int skip, int interval, int effectstart, int effectcount, int effectskip, int effectinterval)
  {
    this.start = start;
    this.count = count;
    this.skip = skip;
    this.interval = interval;
    this.effectStart = effectstart;
    this.effectCount = effectcount;
    this.effectSkip = effectskip;
    this.effectInterval = effectinterval;
  }

  public Frame(int start, int count, int skip, int interval)
  {
    this(start, count, skip, interval, 0, 0, 0, 0);
  }
}
