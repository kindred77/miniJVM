package com.kindred.mir.scene.game.objects;

import com.kindred.mir.GameCommon.MirAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FrameSet {

  private FrameSet players;
  private List<FrameSet> npcs; //Make Array
  private List<FrameSet> monsters;
  private List<FrameSet> helperPets; //IntelligentCreature
  private List<FrameSet> gates;
  private List<FrameSet> walls;

  private Map<MirAction, Frame> frames = new HashMap<>();

}
