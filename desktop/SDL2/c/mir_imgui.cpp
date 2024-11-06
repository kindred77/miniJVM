#include "depends/include/SDL2/SDL.h"
#include "depends/include/SDL2/SDL_rect.h"
#include "depends/include/SDL2/SDL_image.h"

#include "imgui.h"
#include "imgui_impl_sdl2.h"
#include "imgui_impl_sdlrenderer2.h"

extern "C" {

void Mir_ImGui_SDL2_Init(SDL_Window * window, SDL_Renderer *renderer) {
    IMGUI_CHECKVERSION();
    ImGui::CreateContext();
    ImGuiIO& io = ImGui::GetIO(); (void)io;
    io.ConfigFlags |= ImGuiConfigFlags_NavEnableKeyboard;     // Enable Keyboard Controls
    io.ConfigFlags |= ImGuiConfigFlags_NavEnableGamepad;      // Enable Gamepad Controls

    // Setup Dear ImGui style
    ImGui::StyleColorsDark();

    // Setup Platform/Renderer backends
    ImGui_ImplSDL2_InitForSDLRenderer(window, renderer);
    ImGui_ImplSDLRenderer2_Init(renderer);

    ImFont* font = io.Fonts->AddFontFromFileTTF("C:/Windows/Fonts/SIMLI.TTF", 18.0f, nullptr, io.Fonts->GetGlyphRangesChineseSimplifiedCommon());
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

int Mir_ImGui_Begin()
{
    return ImGui::Begin("Hello, world!");
}

void Mir_ImGui_Text()
{
    ImGui::Text("This is some useful text.");
}

void Mir_ImGui_InputText(const char * title, char * buf, int buf_length)
{
    ImGui::InputText(title, buf, buf_length);
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