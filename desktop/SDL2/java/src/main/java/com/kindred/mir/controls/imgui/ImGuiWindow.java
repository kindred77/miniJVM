package com.kindred.mir.controls.imgui;

import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Rectangle;
import com.kindred.mir.util.Size;
import com.kindred.mir.util.Util;

/*
imgui window
 */
public class ImGuiWindow extends ImGuiControl {

  private boolean isHasTitlebar;
  private boolean isMovable;
  private boolean isResizable;
  private boolean isHasBackground;
  private boolean isCanBringToFront;


  public ImGuiWindow(ImGuiLayout parent, long window_id, long renderer_id, String label, Point location, Size size,
      boolean isHasTitlebar, boolean isMovable, boolean isResizable, boolean isHasBackground, boolean isCanBringToFront) throws Exception {
    super(parent, window_id, renderer_id, label, location);
    setSize(size);
    this.isHasTitlebar=isHasTitlebar;
    this.isMovable=isMovable;
    this.isResizable=isResizable;
    this.isHasBackground=isHasBackground;
    this.isCanBringToFront=isCanBringToFront;
  }

  @Override
  public boolean beginDraw() {
    //Point pos = getDisplayLocation();
    Rectangle displayRectangle=getDisplayRectangle();
    if (!MirJNI.ImGui_Begin(Util.toCstyleBytes(this.getLabel()), displayRectangle.getX(), displayRectangle.getY(), displayRectangle.getWidth(), displayRectangle.getHeight(),
        !this.isHasTitlebar,true,true,!this.isMovable,!this.isResizable,
        true,true, true, !this.isHasBackground, !this.isCanBringToFront, true, true))
    {
      MirJNI.ImGui_End();
      return false;
    }

    return true;
  }

  @Override
  public boolean endDraw() {
    MirJNI.ImGui_End();
    return true;
  }

}
