package com.kindred.mir.scene.game.bean;

import com.kindred.mir.GameCommon.OutputMessageType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OutPutMessage {
  private String message;
  private long expireTime;
  private OutputMessageType type;
}
