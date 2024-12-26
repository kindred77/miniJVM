package com.kindred.mir.scene.game.objects;

import com.kindred.mir.GameCommon.MapObjectType;

public class PlayerObject extends MapObject {

  @Override
  public MapObjectType getRace() {
    return null;
  }

  @Override
  public boolean getIsBlocking() {
    return false;
  }
}
