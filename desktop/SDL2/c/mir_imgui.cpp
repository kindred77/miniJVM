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

void Mir_ImGui_InitFont(const char * font_name, float size) {
    ImGuiIO& io = ImGui::GetIO(); (void)io;
    io.ConfigFlags |= ImGuiConfigFlags_NavEnableKeyboard;     // Enable Keyboard Controls
    io.ConfigFlags |= ImGuiConfigFlags_NavEnableGamepad;      // Enable Gamepad Controls
    io.ConfigInputTextCursorBlink=true;
    ImFont* font = io.Fonts->AddFontFromFileTTF(font_name, size, nullptr, io.Fonts->GetGlyphRangesChineseFull());
    IM_ASSERT(font != nullptr);
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

int Mir_ImGui_InputText(float x, float y, float width, const char* label, const char * hint, char * buf, int buf_length, int isPassword)
{
    ImGui::SetCursorPos(ImVec2(x, y));
    ImGui::SetNextItemWidth(width);
    
    int ret = -1;
    if (isPassword) {
        ret = ImGui::InputTextWithHint(!label ? "##" : label, hint, buf, buf_length, ImGuiInputTextFlags_Password);
    }
    else {
        ret = ImGui::InputTextWithHint(!label ? "##" : label, hint, buf, buf_length);

        //ImVec2 inputSize(500, 20);
        //ret = ImGui::InputTextWithHint(!label ? "##" : label, hint, buf, buf_length, ImGuiInputTextFlags_None, NULL, (void*)&inputSize);
    }
    
    return ret;
}

int Mir_ImGui_InputTextMultiline(const char* label, char * buf, int buf_length, float width, int line_height_cnt)
{
    int ret = ImGui::InputTextMultiline(!label ? "##" : label, buf, buf_length, ImVec2(width, ImGui::GetTextLineHeight() * line_height_cnt), ImGuiInputTextFlags_AllowTabInput);

    return ret;
}

void Mir_ImGui_End()
{
    ImGui::End();
}

void Mir_ImGui_Render(SDL_Renderer * renderer)
{
    ImVec4 clear_color = ImVec4(0.45f, 0.55f, 0.60f, 1.00f);
    ImGui::Render();
    //SDL_RenderSetScale(renderer, io.DisplayFramebufferScale.x, io.DisplayFramebufferScale.y);
    //SDL_SetRenderDrawColor(renderer, (Uint8)(clear_color.x * 255), (Uint8)(clear_color.y * 255), (Uint8)(clear_color.z * 255), (Uint8)(clear_color.w * 255));
    //SDL_RenderClear(renderer);
    ImGui_ImplSDLRenderer2_RenderDrawData(ImGui::GetDrawData(), renderer);
}

void Mir_ImGui_Destroy()
{
    ImGui_ImplSDLRenderer2_Shutdown();
    ImGui_ImplSDL2_Shutdown();
    ImGui::DestroyContext();
    
    //SDL_GL_DeleteContext(gl_context);
}

}