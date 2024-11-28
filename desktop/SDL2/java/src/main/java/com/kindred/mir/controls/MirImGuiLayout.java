package com.kindred.mir.controls;

import com.kindred.mir.Settings;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.util.Size;

/*
mir中嵌入的imgui图层，
imgui不能跨图层渲染，同一个图层使用同一个imguiContext
 */
public class MirImGuiLayout extends MirControl {

  private long imgui_context;

  private long window_id;
  private long renderer_id;

  public MirImGuiLayout(MirControl parent, long window_id, long renderer_id) throws Exception{
    super(parent);
    imgui_context = MirJNI.ImGui_SDL2_InitImGuiContext();
    MirJNI.ImGui_SetCurrentContext(imgui_context);
    if (!MirJNI.ImGui_ImplSDL2_InitForSDLRenderer(window_id, renderer_id)
      || !MirJNI.ImGui_ImplSDLRenderer2_Init(renderer_id)) {
      throw new Exception("Can not init imgui.");
    }
    this.window_id=window_id;
    this.renderer_id=renderer_id;
    //默认和总窗口一样大
    super.setSize(new Size(Settings.ScreenWidth,Settings.ScreenHeight));
  }

  @Override
  public boolean beginDraw() {
    MirJNI.ImGui_SetCurrentContext(imgui_context);
    MirJNI.ImGui_SDLRenderer2_NewFrame();
    MirJNI.ImGui_SDL2_NewFrame();
    MirJNI.ImGui_NewFrame();
    return true;
  }

  @Override
  public boolean endDraw() {
    long imgui_drawdata=MirJNI.ImGui_RenderAndGetDrawData();
    MirJNI.ImGui_Render(renderer_id, imgui_drawdata);
    return true;
  }

  public final long getImGuiContext()
  {
    return this.imgui_context;
  }
}
