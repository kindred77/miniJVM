#include <stdio.h>
#include <string.h>
#include "depends/include/SDL2/SDL.h"

#include "jvm.h"
#include "media.h"

GlobeRefer refers;

int org_mini_SDL2_SDL2_SDL_Init(Runtime *runtime, JClass *clazz)
{
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    s32 flags = env->localvar_getInt(runtime->localvar, pos++);
    int ret = SDL_Init(flags);
    env->push_int(runtime->stack, ret);
    return 0;
}

int org_mini_SDL2_SDL2_SDL_CreateWindow(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    Instance *title_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *title = title_arr->arr_body;
    s32 x = env->localvar_getInt(runtime->localvar, pos++);
    s32 y = env->localvar_getInt(runtime->localvar, pos++);
    s32 width = env->localvar_getInt(runtime->localvar, pos++);
    s32 height = env->localvar_getInt(runtime->localvar, pos++);
    s32 flags = env->localvar_getInt(runtime->localvar, pos++);

    SDL_Window *window = SDL_CreateWindow(title, x, y, width, height, flags);
    if (!window) {
        fprintf(stderr, "Failed to create window\n");
    }
    //
    env->push_long(runtime->stack, (s64) (intptr_t) window);
    return 0;
}

static java_native_method method_sdl_table[] = {
    {"org/mini/SDL2/SDL2", "SDL_Init",                   "(I)I",                   org_mini_SDL2_SDL2_SDL_Init},
    {"org/mini/SDL2/SDL2", "SDL_CreateWindow",           "([BIIIII)J",             org_mini_SDL2_SDL2_SDL_CreateWindow},
};

s32 count_SDL2FuncTable() {
    return sizeof(method_sdl_table) / sizeof(java_native_method);
}

__refer ptr_SDLFuncTable() {
    return &method_sdl_table[0];
}