package com.kindred.mir.scene.game.objects;

import com.kindred.mir.controls.MirControl;

public class ItemObject extends MapObject{

  protected ItemObject(long objectID) {
    super(objectID);
  }

  @Override
  public boolean getIsBlocking() {
    return false;
  }

  @Override
  public void draw(long surface) {

  }

  public void drawName(long surface,int y) {

  }

  @Override
  public void process() {

  }

  @Override
  public void drawBehindEffects(boolean effectsEnabled) {

  }

  @Override
  public void drawEffects(boolean effectsEnabled) {

  }
}
