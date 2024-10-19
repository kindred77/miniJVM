#include <stdio.h>
#include <string.h>
#include "depends/include/SDL2/SDL.h"
#include "depends/include/SDL2/SDL_rect.h"
#include "depends/include/SDL2/SDL_image.h"

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

int org_mini_SDL2_SDL2_SDL_GetError(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    c8 *cstr = (c8 *) SDL_GetError();
    if (cstr) {
        Utf8String *ustr = env->utf8_create_part_c(cstr, 0, strlen(cstr));
        Instance *jstr = env->jstring_create(ustr, runtime);
        env->utf8_destory(ustr);
        env->push_ref(runtime->stack, jstr);
    } else {
        env->push_ref(runtime->stack, NULL);
    }
    return 0;
}

int org_mini_SDL2_SDL2_SDL_CreateRenderer(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Window *window = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 index = env->localvar_getInt(runtime->localvar, pos++);
    u32 flags = env->localvar_getInt(runtime->localvar, pos++);
    SDL_Renderer *render = SDL_CreateRenderer(window, index, flags);
    if (!render) {
        fprintf(stderr, "Failed to create render\n");
    }
    //
    env->push_long(runtime->stack, (s64) (intptr_t) render);
    return 0;
}

int org_mini_SDL2_SDL2_SDL_RWFromConstMem(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    Instance *title_arr = env->localvar_getRefer(runtime->localvar, pos++);
    s32 size = env->localvar_getInt(runtime->localvar, pos++);
    c8 *title = title_arr->arr_body;

    SDL_RWops * res = SDL_RWFromConstMem((void *)title, size);
    if (!res) {
        fprintf(stderr, "Failed to create RWops\n");
    }
    //
    env->push_long(runtime->stack, (s64) (intptr_t) res);
    return 0;
}

int org_mini_SDL2_SDL2_SDL_RWclose(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_RWops *rwops = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    int res = SDL_RWclose(rwops);
    env->push_int(runtime->stack, res);
    return 0;
}

int org_mini_SDL2_SDL2_SDL_CreateTextureFromSurface(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *render = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Texture *texture = SDL_CreateTextureFromSurface(render, surface);
    if (!texture) {
        fprintf(stderr, "Failed to create texture\n");
    }

    env->push_long(runtime->stack, (s64) (intptr_t) texture);
    return 0;
}

int org_mini_SDL2_SDL2_SDL_SetTextureColorMod(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 r = env->localvar_getInt(runtime->localvar, pos++);
    s32 g = env->localvar_getInt(runtime->localvar, pos++);
    s32 b = env->localvar_getInt(runtime->localvar, pos++);

    int res = SDL_SetTextureColorMod(texture, r, g, b);
    env->push_int(runtime->stack, res);
    return 0;
}

int org_mini_SDL2_SDL2_SDL_SetTextureBlendMode(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 mod = env->localvar_getInt(runtime->localvar, pos++);
    int res = SDL_SetTextureBlendMode(texture, mod);
    env->push_int(runtime->stack, res);
    return 0;
}

int org_mini_SDL2_SDL2_SDL_SetTextureAlphaMod(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 alpha = env->localvar_getInt(runtime->localvar, pos++);
    int res = SDL_SetTextureAlphaMod(texture, alpha);
    env->push_int(runtime->stack, res);
    return 0;
} 

int org_mini_SDL2_SDL2_SDL_RenderCopy(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *render = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    
    Instance *src_rect = env->localvar_getRefer(runtime->localvar, pos++);
    __refer ptr_src_rect = NULL;
    if (src_rect) {
        ptr_src_rect = src_rect->arr_body;
    }

    Instance *dst_rect = env->localvar_getRefer(runtime->localvar, pos++);
    __refer ptr_dst_rect = NULL;
    if (dst_rect) {
        ptr_dst_rect = dst_rect->arr_body;
    }

    SDL_Rect srect;
    SDL_Rect drect;

    if (ptr_src_rect) {
        srect.x = ((int*)ptr_src_rect)[0];
        srect.y = ((int*)ptr_src_rect)[1];
        srect.w = ((int*)ptr_src_rect)[2];
        srect.h = ((int*)ptr_src_rect)[3];
    }

    if (ptr_dst_rect) {
        drect.x = ((int*)ptr_dst_rect)[0];
        drect.y = ((int*)ptr_dst_rect)[1];
        drect.w = ((int*)ptr_dst_rect)[2];
        drect.h = ((int*)ptr_dst_rect)[3];
    }

    int res = SDL_RenderCopy(render, texture,
        (ptr_src_rect == NULL ? NULL : &srect),
        (ptr_dst_rect == NULL ? NULL : &drect));
    env->push_int(runtime->stack, res);
    return 0;
}

int org_mini_SDL2_SDL2_SDL_RenderClear(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *render = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    int res = SDL_RenderClear(render);
    env->push_int(runtime->stack, res);
    return 0;
}

int org_mini_SDL2_SDL2_SDL_RenderPresent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *render = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_RenderPresent(render);

    return 0;
}

int org_mini_SDL2_SDL2_SDL_CreateEvent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event * event = malloc(sizeof(SDL_Event));

    env->push_long(runtime->stack, (s64) (intptr_t) event);

    return 0;
}

int org_mini_SDL2_SDL2_SDL_FreeEvent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    free(event);

    return 0;
}

int org_mini_SDL2_SDL2_SDL_PollEvent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    int res = SDL_PollEvent(event);
    env->push_int(runtime->stack, res);

    return 0;
}

int org_mini_SDL2_SDL2_SDL_GetEventType(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    env->push_int(runtime->stack, event->type);

    return 0;
}

int org_mini_SDL2_SDL2_SDL_GetKeyEventKeySym(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    //if (event->key && event->key.keysym) {
        env->push_int(runtime->stack, event->key.keysym.sym);
    //}

    return 0;
}

int org_mini_SDL2_SDL2_SDL_GetWindowEvent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    //if (event->window) {
        env->push_int(runtime->stack, event->window.event);
    //}

    return 0;
}

int org_mini_SDL2_SDL2_SDL_Quit(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Quit();

    return 0;
}

int org_mini_SDL2_SDL2_SDL_IMG_LoadPNG_RW(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_RWops *rwops = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Surface * surface = IMG_LoadPNG_RW(rwops);
    env->push_long(runtime->stack, (s64) (intptr_t) surface);

    return 0;
}

int org_mini_SDL2_SDL2_SDL_GetSurfaceWidth(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    env->push_int(runtime->stack, surface->w);

    return 0;
}

int org_mini_SDL2_SDL2_SDL_GetSurfaceHeight(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    env->push_int(runtime->stack, surface->h);

    return 0;
}

static java_native_method method_sdl_table[] = {
    {"org/mini/SDL2/SDL2", "SDL_Init",                       "(I)I",                       org_mini_SDL2_SDL2_SDL_Init},
    {"org/mini/SDL2/SDL2", "SDL_CreateWindow",               "([BIIIII)J",                 org_mini_SDL2_SDL2_SDL_CreateWindow},
    {"org/mini/SDL2/SDL2", "SDL_GetError",                   "()Ljava/lang/String;",       org_mini_SDL2_SDL2_SDL_GetError},
    {"org/mini/SDL2/SDL2", "SDL_CreateRenderer",             "(JII)J",                     org_mini_SDL2_SDL2_SDL_CreateRenderer},
    {"org/mini/SDL2/SDL2", "SDL_RWFromConstMem",             "([BI)J",                     org_mini_SDL2_SDL2_SDL_RWFromConstMem},
    {"org/mini/SDL2/SDL2", "SDL_RWclose",                    "(J)I",                       org_mini_SDL2_SDL2_SDL_RWclose},
    {"org/mini/SDL2/SDL2", "SDL_CreateTextureFromSurface",   "(JJ)J",                      org_mini_SDL2_SDL2_SDL_CreateTextureFromSurface},
    {"org/mini/SDL2/SDL2", "SDL_SetTextureColorMod",         "(JIII)I",                    org_mini_SDL2_SDL2_SDL_SetTextureColorMod},
    {"org/mini/SDL2/SDL2", "SDL_SetTextureBlendMode",        "(JI)I",                      org_mini_SDL2_SDL2_SDL_SetTextureBlendMode},
    {"org/mini/SDL2/SDL2", "SDL_SetTextureAlphaMod",         "(JI)I",                      org_mini_SDL2_SDL2_SDL_SetTextureAlphaMod},
    {"org/mini/SDL2/SDL2", "SDL_RenderCopy",                 "(JJ[I[I)I",                  org_mini_SDL2_SDL2_SDL_RenderCopy},
    {"org/mini/SDL2/SDL2", "SDL_RenderClear",                "(J)I",                       org_mini_SDL2_SDL2_SDL_RenderClear},
    {"org/mini/SDL2/SDL2", "SDL_RenderPresent",              "(J)V",                       org_mini_SDL2_SDL2_SDL_RenderPresent},
    
    {"org/mini/SDL2/SDL2", "SDL_CreateEvent",                "()J",                        org_mini_SDL2_SDL2_SDL_CreateEvent},
    {"org/mini/SDL2/SDL2", "SDL_FreeEvent",                  "(J)V",                       org_mini_SDL2_SDL2_SDL_FreeEvent},
    {"org/mini/SDL2/SDL2", "SDL_GetKeyEventKeySym",          "(J)I",                       org_mini_SDL2_SDL2_SDL_GetKeyEventKeySym},
    {"org/mini/SDL2/SDL2", "SDL_GetEventType",               "(J)I",                       org_mini_SDL2_SDL2_SDL_GetEventType},
    {"org/mini/SDL2/SDL2", "SDL_GetWindowEvent",             "(J)I",                       org_mini_SDL2_SDL2_SDL_GetWindowEvent},
    {"org/mini/SDL2/SDL2", "SDL_PollEvent",                  "(J)I",                       org_mini_SDL2_SDL2_SDL_PollEvent},
    
    {"org/mini/SDL2/SDL2", "SDL_Quit",                       "()V",                        org_mini_SDL2_SDL2_SDL_Quit},

    {"org/mini/SDL2/SDL2", "SDL_IMG_LoadPNG_RW",              "(J)J",                       org_mini_SDL2_SDL2_SDL_IMG_LoadPNG_RW},
    {"org/mini/SDL2/SDL2", "SDL_GetSurfaceWidth",            "(J)I",                       org_mini_SDL2_SDL2_SDL_GetSurfaceWidth},
    {"org/mini/SDL2/SDL2", "SDL_GetSurfaceHeight",           "(J)I",                       org_mini_SDL2_SDL2_SDL_GetSurfaceHeight},
};

s32 count_SDL2FuncTable() {
    return sizeof(method_sdl_table) / sizeof(java_native_method);
}

__refer ptr_SDLFuncTable() {
    return &method_sdl_table[0];
}