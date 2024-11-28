package com.kindred.mir.controls.imgui;

import com.kindred.mir.controls.MirImGuiLayout;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;
import com.kindred.mir.util.Util;

/*
imgui window
 */
public class ImGuiWindow extends ImGuiControl {

  public ImGuiWindow(MirImGuiLayout parent, long window_id, long renderer_id, String label, Point location, Size size) throws Exception {
    super(parent, window_id, renderer_id, label, location);
    setSize(size);
  }

  @Override
  public boolean beginDraw() {
    if (!MirJNI.ImGui_Begin(Util.toCstyleBytes(this.getLabel()), getLocation().getX(), getLocation().getY(), size.getWidth(), size.getHeight(),
        false,true,true,false,false,
        false,true, true, false, false, true, true))
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
