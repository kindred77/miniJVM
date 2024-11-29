package com.kindred.mir.controls.imgui;

import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlCanBeDrawn;
import com.kindred.mir.util.Point;

/*
imgui对象，不包括layout
 */
public class ImGuiControl extends MirControlCanBeDrawn {

  private long window_id;
  private long renderer_id;
  private String label;
  public ImGuiControl(MirControl parent, long window_id, long renderer_id, String label, Point location) throws Exception{
    super(parent, renderer_id);
    //这里有点丑
    if (parent == null ||
        !(parent instanceof ImGuiLayout) &&
        !(parent instanceof ImGuiWindow) &&
        !(this instanceof ImGuiLayout)) {
      throw new Exception("ImGui control must be in ImGuiLayout or ImGuiWindow, only the ImGuiLayout can be in MirControl!");
    }
    this.window_id=window_id;
    this.renderer_id=renderer_id;
    this.label=label;
    setLocation(location);
  }

  public final String getLabel() {
    return this.label;
  }

  //屏蔽掉
  @Override
  protected final boolean drawControl() {
    return true;
  }

  @Override
  public final ImGuiLayout getParent(){
    return (ImGuiLayout)super.getParent();
  }

  /*
  imgui在获取显示坐标的时候有点特殊，
  ImGuiWindow需要通过父窗口计算绝对坐标，也就是使用MirControlCanBeDrawn的方式。
  但是在ImGuiWindow中的子控件需要使用相对父控件的坐标。
   */
  @Override
  public final Point getDisplayLocation() {
    if (parent instanceof ImGuiWindow) {
      return super.getLocation();
    } else {
      return super.getDisplayLocation();
    }
  }
}
