#include "depends/include/SDL2/SDL.h"
#include "depends/include/SDL2/SDL_rect.h"
#include "depends/include/SDL2/SDL_image.h"

#include "imgui.h"
#include "imgui_impl_sdl2.h"
#include "imgui_impl_sdlrenderer2.h"

#include <stdio.h>
#include <iostream>

extern "C" {

void Mir_ImGui_SDL2_Init(SDL_Window * window, SDL_Renderer *renderer) {
    IMGUI_CHECKVERSION();
    ImGui::CreateContext();
    
    //io.ConfigInputTextEnterKeepActive=true;

    // Setup Dear ImGui style
    ImGui::StyleColorsDark();

    // Setup Platform/Renderer backends
    ImGui_ImplSDL2_InitForSDLRenderer(window, renderer);
    ImGui_ImplSDLRenderer2_Init(renderer);

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
    //io.ConfigFlags |= ImGuiConfigFlags_NavEnableKeyboard;     // Enable Keyboard Controls
    //io.ConfigFlags |= ImGuiConfigFlags_NavEnableGamepad;      // Enable Gamepad Controls
    //io.ConfigInputTextCursorBlink=true;
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

int Mir_ImGui_Begin(const char * label, float x, float y, float width, float height, int no_background_arg)
{
    static bool no_titlebar = true;
    static bool no_scrollbar = true;
    static bool no_menu = true;
    static bool no_move = true;
    static bool no_resize = true;
    static bool no_collapse = true;
    static bool no_close = true;
    static bool no_nav = true;
    static bool no_background = no_background_arg;
    static bool no_bring_to_front = true;
    static bool unsaved_document = true;
    static bool no_saved_settings = true;

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
    
    return ret;
}

int Mir_ImGui_InputTextMultiline(const char* label, char * buf, int buf_length, float width, int line_height_cnt)
{
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

}