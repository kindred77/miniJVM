package com.kindred.mir.controls.imgui;

import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirImGuiLayout;
import com.kindred.mir.util.Point;

/*
imgui对象，不包括layout
 */
public class ImGuiControl extends MirControl {

  private long window_id;
  private long renderer_id;
  private String label;
  public ImGuiControl(MirControl parent, long window_id, long renderer_id, String label, Point location) throws Exception{
    super(parent);
    //这里有点丑
    if (!(parent instanceof MirImGuiLayout) &&
        !(parent instanceof ImGuiWindow)) {
      throw new Exception("ImGui control must be in MirImGuiLayout or ImGuiWindow!");
    }
    this.window_id=window_id;
    this.renderer_id=renderer_id;
    this.label=label;
    setLocation(location);
  }

  public final String getLabel() {
    return this.label;
  }

  @Override
  public final MirImGuiLayout getParent(){
    return (MirImGuiLayout)super.getParent();
  }

}
