#include "depends/include/SDL2/SDL.h"
#include "depends/include/SDL2/SDL_rect.h"
#include "depends/include/SDL2/SDL_image.h"

#include "imgui.h"
#include "imgui_internal.h"
#include "imgui_impl_sdl2.h"
#include "imgui_impl_sdlrenderer2.h"

#include <stdio.h>
#include <iostream>

extern "C" {

intptr_t Mir_ImGui_SDL2_InitImGuiContext() {
    IMGUI_CHECKVERSION();
    ImGuiContext * res = ImGui::CreateContext();
    
    //io.ConfigInputTextEnterKeepActive=true;

    // Setup Dear ImGui style
    ImGui::StyleColorsDark();

    return reinterpret_cast<intptr_t>(res);
}

bool Mir_ImGui_ImplSDL2_InitForSDLRenderer(SDL_Window * window, SDL_Renderer *renderer) {
    return ImGui_ImplSDL2_InitForSDLRenderer(window, renderer);
}

bool Mir_ImGui_ImplSDLRenderer2_Init(SDL_Renderer *renderer) {
    return ImGui_ImplSDLRenderer2_Init(renderer);
}

void Mir_ImGui_SetCurrentContext(intptr_t context_ptr) {
    ImGuiContext * ctx = reinterpret_cast<ImGuiContext*>(context_ptr);
    ImGui::SetCurrentContext(ctx);
}

void Mir_ImGui_InitBackColor(float r, float g, float b, float alpha) {
    ImGuiStyle* style = &ImGui::GetStyle();
    ImVec4* colors = style->Colors;
    colors[ImGuiCol_FrameBg] = ImVec4(r, g, b, alpha);
}

void Mir_ImGui_InitForeColor(float r, float g, float b, float alpha) {
    ImGuiStyle* style = &ImGui::GetStyle();
    ImVec4* colors = style->Colors;
    colors[ImGuiCol_Text] = ImVec4(r, g, b, alpha);
}

intptr_t Mir_ImGui_InitFont(const char * font_name, float size) {
    ImGuiIO& io = ImGui::GetIO(); (void)io;
    io.ConfigFlags |= ImGuiConfigFlags_NavEnableKeyboard;     // Enable Keyboard Controls
    io.ConfigFlags |= ImGuiConfigFlags_NavEnableGamepad;      // Enable Gamepad Controls
    io.ConfigFlags |= ImGuiConfigFlags_DockingEnable;
    io.ConfigInputTextCursorBlink=true;
    ImFont* font = io.Fonts->AddFontFromFileTTF(font_name, size, nullptr, io.Fonts->GetGlyphRangesChineseFull());
    IM_ASSERT(font != nullptr);
    io.Fonts->Build();
    return reinterpret_cast<intptr_t>(font);
}

void Mir_ImGui_PushFont(intptr_t font_ptr) {
    ImFont* font = reinterpret_cast<ImFont*>(font_ptr);
    ImGui::PushFont(font);
}

void Mir_ImGui_PopFont() {
    ImGui::PopFont();
}

int Mir_ImGui_SDL2_ProcessEvent(SDL_Event * event)
{
    return ImGui_ImplSDL2_ProcessEvent(event);
}

void Mir_ImGui_SDLRenderer2_NewFrame()
{
    ImGui_ImplSDLRenderer2_NewFrame();
}

void Mir_ImGui_SDL2_NewFrame()
{
    ImGui_ImplSDL2_NewFrame();
}

void Mir_ImGui_NewFrame()
{
    ImGui::NewFrame();
}

void Mir_ImGui_EndFrame()
{
    ImGui::EndFrame();
}

int Mir_ImGui_Begin(const char * label, float x, float y, float width, float height,
    int no_titlebar_arg, int no_scrollbar_arg, int no_menu_arg, int no_move_arg, 
    int no_resize_arg, int no_collapse_arg, int no_close_arg, int no_nav_arg,
    int no_background_arg, int no_bring_to_front_arg, int unsaved_document_arg,
    int no_saved_settings_arg)
{
    static bool no_titlebar = no_titlebar_arg;
    static bool no_scrollbar = no_scrollbar_arg;
    static bool no_menu = no_menu_arg;
    static bool no_move = no_move_arg;
    static bool no_resize = no_resize_arg;
    static bool no_collapse = no_collapse_arg;
    static bool no_close = no_close_arg;
    static bool no_nav = no_nav_arg;
    static bool no_background = no_background_arg;
    static bool no_bring_to_front = no_bring_to_front_arg;
    static bool unsaved_document = unsaved_document_arg;
    static bool no_saved_settings = no_saved_settings_arg;

    bool * p_open = NULL;

    ImGuiWindowFlags window_flags = 0;
    if (no_titlebar)        window_flags |= ImGuiWindowFlags_NoTitleBar;
    if (no_scrollbar)       window_flags |= ImGuiWindowFlags_NoScrollbar;
    if (!no_menu)           window_flags |= ImGuiWindowFlags_MenuBar;
    if (no_move)            window_flags |= ImGuiWindowFlags_NoMove;
    if (no_resize)          window_flags |= ImGuiWindowFlags_NoResize;
    if (no_collapse)        window_flags |= ImGuiWindowFlags_NoCollapse;
    if (no_nav)             window_flags |= ImGuiWindowFlags_NoNav;
    if (no_background)      window_flags |= ImGuiWindowFlags_NoBackground;
    if (no_bring_to_front)  window_flags |= ImGuiWindowFlags_NoBringToFrontOnFocus;
    if (unsaved_document)   window_flags |= ImGuiWindowFlags_UnsavedDocument;
    if (no_saved_settings)   window_flags |= ImGuiWindowFlags_NoSavedSettings;
    if (no_close)           p_open = NULL;

    ImGui::SetNextWindowPos(ImVec2(x, y), ImGuiCond_FirstUseEver);
    ImGui::SetNextWindowSize(ImVec2(width, height), ImGuiCond_FirstUseEver);

    

    // const ImGuiViewport* main_viewport = ImGui::GetMainViewport();
    // ImGui::SetNextWindowPos(ImVec2(main_viewport->WorkPos.x + 650, main_viewport->WorkPos.y + 20), ImGuiCond_FirstUseEver);
    // ImGui::SetNextWindowSize(ImVec2(128, 30), ImGuiCond_FirstUseEver);
    
    int ret = ImGui::Begin(!label ? "##" : label, p_open, window_flags);
    
    return ret;
}

void Mir_ImGui_Text(const char* text)
{
    ImGui::Text(!text ? "##" : text);
}

static int InputTextCallback(ImGuiInputTextCallbackData* data)
{
    // 在这里处理输入文本的变化
    if (data->EventFlag == ImGuiInputTextFlags_CallbackCharFilter)
    {
        std::cout << "InputTextCallback--------000" << std::endl;
        // 过滤掉特定字符，例如只允许数字
        if (data->EventChar < '0' || data->EventChar > '9')
            return 0; // 返回1表示忽略这个字符
    }
    return 0; // 返回0表示接受这个字符
}

int Mir_ImGui_InputText(float x, float y, float width, const char* label, const char * hint, char * buf, int buf_length, int isPassword)
{
    ImGui::SetCursorPos(ImVec2(x, y));
    ImGui::SetNextItemWidth(width);
    
    ImGuiInputTextCallbackData cb_user_data;

    int ret = -1;
    if (isPassword) {
        ret = ImGui::InputTextWithHint(!label ? "##" : label, hint, buf, buf_length, ImGuiInputTextFlags_Password | ImGuiInputTextFlags_EnterReturnsTrue/* , InputTextCallback, &cb_user_data */);
    }
    else {
        ret = ImGui::InputTextWithHint(!label ? "##" : label, hint, buf, buf_length, ImGuiInputTextFlags_EnterReturnsTrue/* , InputTextCallback, &cb_user_data */);
    }
    
    // ImGuiWindow* window = ImGui::GetCurrentWindow();
    // const ImGuiID id = window->GetID(label);
    // ImGuiContext& g = *GImGui;
    // g.ActiveId = id;

    return ret;
}

int Mir_ImGui_InputTextMultiline(float x, float y, const char* label, char * buf, int buf_length, float width, int line_height_cnt)
{
    ImGui::SetCursorPos(ImVec2(x, y));
    int ret = ImGui::InputTextMultiline(!label ? "##" : label, buf, buf_length, ImVec2(width, ImGui::GetTextLineHeight() * line_height_cnt), ImGuiInputTextFlags_AllowTabInput);

    return ret;
}

int Mir_SetWindowFontScale(float scale) {
    ImGui::SetWindowFontScale(scale);

    // ImGuiContext * context = ImGui::GetCurrentContext();
    // ImGuiContext& g = *context;
    // g.FontSize = scale;

    return 0;
}

void Mir_ImGui_End()
{
    ImGui::End();
}

void Mir_ImGui_Render(SDL_Renderer * renderer, intptr_t drawData_ptr)
{
    ImDrawData * drawData = reinterpret_cast<ImDrawData *>(drawData_ptr);
    ImGui_ImplSDLRenderer2_RenderDrawData(drawData, renderer);
}

intptr_t Mir_ImGui_RenderAndGetDrawData()
{
    ImGui::Render();
    ImDrawData * drawData = ImGui::GetDrawData();
    return reinterpret_cast<intptr_t>(drawData);
}

void Mir_ImGui_Destroy()
{
    ImGui_ImplSDLRenderer2_Shutdown();
    ImGui_ImplSDL2_Shutdown();
    ImGui::DestroyContext();
    
    //SDL_GL_DeleteContext(gl_context);
}

//for test(转换失败)
SDL_Texture* convertDrawDataToTexture(intptr_t draw_data_ptr, SDL_Renderer* renderer) {
    ImDrawData* draw_data = reinterpret_cast<ImDrawData*>(draw_data_ptr);
    // 获取总的顶点和索引数量
    ImVec2 clip_off = draw_data->DisplayPos;
    printf("----000-----%d\n",draw_data->CmdListsCount);
    for (int n = 0; n < draw_data->CmdListsCount; n++) {

        const ImDrawList* cmd_list = draw_data->CmdLists[n];
        const ImDrawVert* vtx_buffer = cmd_list->VtxBuffer.Data;  // vertex buffer generated by Dear ImGui
        const ImDrawIdx* idx_buffer = cmd_list->IdxBuffer.Data;
        printf("----111-----%d\n",cmd_list->CmdBuffer.Size);
        for (int cmd_i = 0; cmd_i < cmd_list->CmdBuffer.Size; cmd_i++)
        {
            const ImDrawCmd* pcmd = &cmd_list->CmdBuffer[cmd_i];
            if (pcmd->UserCallback)
            {
                pcmd->UserCallback(cmd_list, pcmd);
            }
            else
            {
                // Project scissor/clipping rectangles into framebuffer space
                ImVec2 clip_min(pcmd->ClipRect.x - clip_off.x, pcmd->ClipRect.y - clip_off.y);
                ImVec2 clip_max(pcmd->ClipRect.z - clip_off.x, pcmd->ClipRect.w - clip_off.y);
                if (clip_max.x <= clip_min.x || clip_max.y <= clip_min.y)
                    continue;

                // We are using scissoring to clip some objects. All low-level graphics API should support it.
                // - If your engine doesn't support scissoring yet, you may ignore this at first. You will get some small glitches
                //   (some elements visible outside their bounds) but you can fix that once everything else works!
                // - Clipping coordinates are provided in imgui coordinates space:
                //   - For a given viewport, draw_data->DisplayPos == viewport->Pos and draw_data->DisplaySize == viewport->Size
                //   - In a single viewport application, draw_data->DisplayPos == (0,0) and draw_data->DisplaySize == io.DisplaySize, but always use GetMainViewport()->Pos/Size instead of hardcoding those values.
                //   - In the interest of supporting multi-viewport applications (see 'docking' branch on github),
                //     always subtract draw_data->DisplayPos from clipping bounds to convert them to your viewport space.
                // - Note that pcmd->ClipRect contains Min+Max bounds. Some graphics API may use Min+Max, other may use Min+Size (size being Max-Min)
                //MyEngineSetScissor(clip_min.x, clip_min.y, clip_max.x, clip_max.y);

                // The texture for the draw call is specified by pcmd->GetTexID().
                // The vast majority of draw calls will use the Dear ImGui texture atlas, which value you have set yourself during initialization.
                //MyEngineBindTexture((MyTexture*)pcmd->GetTexID());
                printf("----222-----\n");
                return (SDL_Texture*)pcmd->GetTexID();

                // Render 'pcmd->ElemCount/3' indexed triangles.
                // By default the indices ImDrawIdx are 16-bit, you can change them to 32-bit in imconfig.h if your engine doesn't support 16-bit indices.
                //MyEngineDrawIndexedTriangles(pcmd->ElemCount, sizeof(ImDrawIdx) == 2 ? GL_UNSIGNED_SHORT : GL_UNSIGNED_INT, idx_buffer + pcmd->IdxOffset, vtx_buffer, pcmd->VtxOffset);
            }
        }
    }

    return 0;
}

}