#include <stdio.h>
#include <string.h>
#include "depends/include/SDL2/SDL.h"
#include "depends/include/SDL2/SDL_rect.h"
#include "depends/include/SDL2/SDL_image.h"

#include "jvm.h"
#include "media.h"

int com_kindred_sdl_SDL_SDL_Init(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    s32 flags = env->localvar_getInt(runtime->localvar, pos++);
    int ret = SDL_Init(flags);
    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_SDL_SetHint(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    Instance *name_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *name = NULL;
    if (name_arr) {
        name = name_arr->arr_body;
    }

    Instance *value_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *value = NULL;
    if (value_arr) {
        value = value_arr->arr_body;
    }
    
    SDL_bool ret = SDL_SetHint(name, value);

    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_SDL_CreateWindow(Runtime *runtime, JClass *clazz) {
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

int com_kindred_sdl_SDL_SDL_SetWindowOpacity(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Window *window = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    Int2Float popacity;
    popacity.i = env->localvar_getInt(runtime->localvar, pos++);
    float opacity = (float)popacity.f;

    int ret = SDL_SetWindowOpacity(window, opacity);

    env->push_int(runtime->stack, ret);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetError(Runtime *runtime, JClass *clazz) {
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

int com_kindred_sdl_SDL_SDL_CreateRenderer(Runtime *runtime, JClass *clazz) {
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

int com_kindred_sdl_SDL_SDL_RWFromConstMem(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    Instance *img_data_arr = env->localvar_getRefer(runtime->localvar, pos++);
    s32 size = env->localvar_getInt(runtime->localvar, pos++);
    c8 *data = img_data_arr->arr_body;
    printf("data: %lld, size: %d\n", data, size);
    SDL_RWops * res = SDL_RWFromConstMem((void *)data, size);
    if (!res) {
        fprintf(stderr, "Failed to create RWops\n");
    }
    //
    env->push_long(runtime->stack, (s64) (intptr_t) res);
    return 0;
}

int com_kindred_sdl_SDL_SDL_RWclose(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_RWops *rwops = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    int res = SDL_RWclose(rwops);
    env->push_int(runtime->stack, res);
    return 0;
}

int com_kindred_sdl_SDL_SDL_CreateTextureFromSurface(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *render = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Texture *texture = SDL_CreateTextureFromSurface(render, surface);
    if (!texture) {
        fprintf(stderr, "Failed to create texture from surface. \n");
    }

    env->push_long(runtime->stack, (s64) (intptr_t) texture);
    return 0;
}

int com_kindred_sdl_SDL_SDL_SetTextureColorMod(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 r = env->localvar_getInt(runtime->localvar, pos++);
    s32 g = env->localvar_getInt(runtime->localvar, pos++);
    s32 b = env->localvar_getInt(runtime->localvar, pos++);

    int res = SDL_SetTextureColorMod(texture, (Uint8)r, (Uint8)g, (Uint8)b);
    env->push_int(runtime->stack, res);
    return 0;
}

int com_kindred_sdl_SDL_SDL_SetTextureBlendMode(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 mod = env->localvar_getInt(runtime->localvar, pos++);
    int res = SDL_SetTextureBlendMode(texture, mod);
    env->push_int(runtime->stack, res);
    return 0;
}

int com_kindred_sdl_SDL_SDL_SetTextureAlphaMod(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 alpha = env->localvar_getInt(runtime->localvar, pos++);
    int res = SDL_SetTextureAlphaMod(texture, alpha);
    env->push_int(runtime->stack, res);
    return 0;
} 

int com_kindred_sdl_SDL_SDL_RenderCopy(Runtime *runtime, JClass *clazz) {
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

int com_kindred_sdl_SDL_SDL_SetRenderDrawColor(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *renderer = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 r = env->localvar_getInt(runtime->localvar, pos++);
    s32 g = env->localvar_getInt(runtime->localvar, pos++);
    s32 b = env->localvar_getInt(runtime->localvar, pos++);
    s32 alpha = env->localvar_getInt(runtime->localvar, pos++);

    int res = SDL_SetRenderDrawColor(renderer, r, g, b, alpha);
    env->push_int(runtime->stack, res);
    return 0;
}

int com_kindred_sdl_SDL_SDL_GetRenderDrawColor(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *renderer = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    Uint8 defaultR, defaultG, defaultB, defaultA;
    int ret = SDL_GetRenderDrawColor(renderer, &defaultR, &defaultG, &defaultB, &defaultA);
    Instance *_arr = NULL;
    if (!ret)
    {
        _arr = env->jarray_create_by_type_index(runtime, 4, DATATYPE_INT);
        _arr->arr_body[0] = defaultR;
        _arr->arr_body[1] = defaultG;
        _arr->arr_body[2] = defaultB;
        _arr->arr_body[3] = defaultA;
        env->push_ref(runtime->stack, _arr);
    }
    
    return 0;
}

int com_kindred_sdl_SDL_SDL_RenderClear(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *render = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    int res = SDL_RenderClear(render);
    env->push_int(runtime->stack, res);
    return 0;
}

int com_kindred_sdl_SDL_SDL_RenderPresent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *render = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_RenderPresent(render);

    return 0;
}

int com_kindred_sdl_SDL_SDL_RenderDrawLine(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *render = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    s32 startX = env->localvar_getInt(runtime->localvar, pos++);
    s32 startY = env->localvar_getInt(runtime->localvar, pos++);
    s32 endX = env->localvar_getInt(runtime->localvar, pos++);
    s32 endY = env->localvar_getInt(runtime->localvar, pos++);

    int ret = SDL_RenderDrawLine(render, startX, startY, endX, endY);

    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_SDL_CreateEvent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event * event = malloc(sizeof(SDL_Event));

    env->push_long(runtime->stack, (s64) (intptr_t) event);

    return 0;
}

int com_kindred_sdl_SDL_SDL_FreeEvent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    free(event);

    return 0;
}

int com_kindred_sdl_SDL_SDL_PollEvent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    int res = SDL_PollEvent(event);
    env->push_int(runtime->stack, res);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetWindowFlags(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Window *window = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    Uint32 falgs = SDL_GetWindowFlags(window);

    env->push_int(runtime->stack, (int)falgs);
    return 0;
}

int com_kindred_sdl_SDL_SDL_DestroyWindow(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Window *window = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_DestroyWindow(window);

    return 0;
}

int com_kindred_sdl_SDL_SDL_DestroyRenderer(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *renderer = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_DestroyRenderer(renderer);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetEventType(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    env->push_int(runtime->stack, event->type);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetKeyEventKeySym(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    //if (event->key && event->key.keysym) {
        env->push_int(runtime->stack, event->key.keysym.sym);
    //}

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetEventWindowEvent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    //if (event->window) {
        env->push_int(runtime->stack, event->window.event);
    //}

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetEventMouseButtonButton(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    //if (event->window) {
        env->push_int(runtime->stack, event->button.button);
    //}

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetEventMouseButtonPos(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    Instance *_arr = env->jarray_create_by_type_index(runtime, 2, DATATYPE_INT);
    _arr->arr_body[0] = event->button.x;
    _arr->arr_body[1] = event->button.y;
    env->push_ref(runtime->stack, _arr);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetEventMouseMotionPos(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    Instance *_arr = env->jarray_create_by_type_index(runtime, 2, DATATYPE_INT);
    _arr->arr_body[0] = event->motion.x;
    _arr->arr_body[1] = event->motion.y;
    env->push_ref(runtime->stack, _arr);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetEventMouseWheelPos(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    Instance *_arr = env->jarray_create_by_type_index(runtime, 2, DATATYPE_INT);
    _arr->arr_body[0] = event->wheel.x;
    _arr->arr_body[1] = event->wheel.y;
    env->push_ref(runtime->stack, _arr);

    return 0;
}

int com_kindred_sdl_SDL_SDL_Quit(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Quit();

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetTicks(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    Uint32 ts = SDL_GetTicks();
    env->push_long(runtime->stack, ts);
    return 0;
}

int com_kindred_sdl_SDL_SDL_RWFromFile(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    Instance *file_name_inst = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *file_name = file_name_inst->arr_body;

    Instance *mode_inst = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *mode = mode_inst->arr_body;

    SDL_RWops * ops= SDL_RWFromFile(file_name, mode);
    if (!ops) {
        fprintf(stderr, "Failed to create RWops from file: %s. \n", file_name);
    }

    env->push_long(runtime->stack, (s64) (intptr_t) ops);

    return 0;
}

int com_kindred_sdl_SDL_SDL_IMG_LoadPNG_RW(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_RWops *rwops = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Surface * surface = IMG_LoadPNG_RW(rwops);
    env->push_long(runtime->stack, (s64) (intptr_t) surface);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetSurfaceWidth(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    env->push_int(runtime->stack, surface->w);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetSurfaceHeight(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    env->push_int(runtime->stack, surface->h);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetSurfacePitch(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    env->push_int(runtime->stack, surface->pitch);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetSurfacePixelFormat(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    env->push_int(runtime->stack, surface->format->format);

    return 0;
}

int com_kindred_sdl_SDL_SDL_GetWindowPixelFormat(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Window *window = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    int ret = SDL_GetWindowPixelFormat(window);

    if(!ret)
    {
        fprintf(stderr, "Unable to get pixel form from window! SDL Error: %s\n", SDL_GetError() );
    }
    env->push_int(runtime->stack, ret);

    return 0;
}

int com_kindred_sdl_SDL_SDL_ConvertSurfaceFormat(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    s32 pixel_format = env->localvar_getInt(runtime->localvar, pos++);
    s32 flags = env->localvar_getInt(runtime->localvar, pos++);

    SDL_Surface *dst_surface = SDL_ConvertSurfaceFormat(surface, pixel_format, flags);

    env->push_long(runtime->stack, (s64) (intptr_t) dst_surface);

    return 0;
}

// int com_kindred_sdl_SDL_SDL_GetSurfacePixelData(Runtime *runtime, JClass *clazz) {
//     JniEnv *env = runtime->jnienv;
//     s32 pos = 0;

//     SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
//     pos += 2;

//     long total_size = surface->pitch * surface->h;
//     s32 _j_t_bytes = sizeof(c8);
//     Instance *_arr = env->jarray_create_by_type_index(runtime, total_size / _j_t_bytes, DATATYPE_BYTE);
//     //memcpy(_arr->arr_body, _ptr_re_val,_struct_bytes);
//     _arr->arr_body = 
//     env->push_ref(runtime->stack, _arr);

//     return 0;
// }

int com_kindred_sdl_SDL_SDL_LockSurface(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    int ret = SDL_LockSurface(surface);

    if(ret)
    {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
    }
    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_SDL_UnlockSurface(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_UnlockSurface(surface);

    return 0;
}

int com_kindred_sdl_SDL_SDL_FreeSurface(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_FreeSurface(surface);

    return 0;
}

int com_kindred_sdl_SDL_SDL_CreateTexture(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *renderer = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 pixel_format = env->localvar_getInt(runtime->localvar, pos++);
    s32 access_method = env->localvar_getInt(runtime->localvar, pos++);
    s32 width = env->localvar_getInt(runtime->localvar, pos++);
    s32 height = env->localvar_getInt(runtime->localvar, pos++);

    SDL_Texture * texture = SDL_CreateTexture(renderer, pixel_format, access_method, width, height);
    if(!texture)
    {
        fprintf(stderr, "Unable to create texture! SDL Error: %s\n", SDL_GetError() );
    }

    env->push_long(runtime->stack, (s64) (intptr_t) texture);
    return 0;
}

int com_kindred_sdl_SDL_SDL_UpdateTexture(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    
    Instance *rect_ref = env->localvar_getRefer(runtime->localvar, pos++);
    __refer ptr_rect = NULL;
    if (rect_ref) {
        ptr_rect = rect_ref->arr_body;
    }

    SDL_Rect rect;

    if (ptr_rect) {
        rect.x = ((int*)ptr_rect)[0];
        rect.y = ((int*)ptr_rect)[1];
        rect.w = ((int*)ptr_rect)[2];
        rect.h = ((int*)ptr_rect)[3];
    }

    Instance *pixel_data_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *data = NULL;
    int ret = -1;
    if (pixel_data_arr) {
        data = pixel_data_arr->arr_body;
        s32 pitch = env->localvar_getInt(runtime->localvar, pos++);
        ret = SDL_UpdateTexture(texture, (ptr_rect == NULL ? NULL : &rect), data, pitch);
    }
    else {
        fprintf(stdout, "Pixel data is empty, do nothing. \n");
    }
    if(ret) {
        fprintf(stderr, "Unable to update texture! SDL Error: %s\n", SDL_GetError() );
    }
    env->push_int(runtime->stack, ret);

    return 0;
}

static java_native_method method_sdl_table[] = {
    {"com/kindred/sdl/SDL", "SDL_Init",                       "(I)I",                       com_kindred_sdl_SDL_SDL_Init},
    {"com/kindred/sdl/SDL", "SDL_SetHint",                    "([B[B)Z",                    com_kindred_sdl_SDL_SDL_SetHint},
    {"com/kindred/sdl/SDL", "SDL_CreateWindow",               "([BIIIII)J",                 com_kindred_sdl_SDL_SDL_CreateWindow},
    {"com/kindred/sdl/SDL", "SDL_SetWindowOpacity",           "(JF)I",                      com_kindred_sdl_SDL_SDL_SetWindowOpacity},
    {"com/kindred/sdl/SDL", "SDL_GetError",                   "()Ljava/lang/String;",       com_kindred_sdl_SDL_SDL_GetError},
    {"com/kindred/sdl/SDL", "SDL_CreateRenderer",             "(JII)J",                     com_kindred_sdl_SDL_SDL_CreateRenderer},
    {"com/kindred/sdl/SDL", "SDL_RWFromConstMem",             "([BI)J",                     com_kindred_sdl_SDL_SDL_RWFromConstMem},
    {"com/kindred/sdl/SDL", "SDL_RWclose",                    "(J)I",                       com_kindred_sdl_SDL_SDL_RWclose},
    {"com/kindred/sdl/SDL", "SDL_CreateTextureFromSurface",   "(JJ)J",                      com_kindred_sdl_SDL_SDL_CreateTextureFromSurface},
    {"com/kindred/sdl/SDL", "SDL_SetTextureColorMod",         "(JIII)I",                    com_kindred_sdl_SDL_SDL_SetTextureColorMod},
    {"com/kindred/sdl/SDL", "SDL_SetTextureBlendMode",        "(JI)I",                      com_kindred_sdl_SDL_SDL_SetTextureBlendMode},
    {"com/kindred/sdl/SDL", "SDL_SetTextureAlphaMod",         "(JI)I",                      com_kindred_sdl_SDL_SDL_SetTextureAlphaMod},
    {"com/kindred/sdl/SDL", "SDL_RenderCopy",                 "(JJ[I[I)I",                  com_kindred_sdl_SDL_SDL_RenderCopy},
    {"com/kindred/sdl/SDL", "SDL_RenderClear",                "(J)I",                       com_kindred_sdl_SDL_SDL_RenderClear},
    {"com/kindred/sdl/SDL", "SDL_RenderPresent",              "(J)V",                       com_kindred_sdl_SDL_SDL_RenderPresent},
    {"com/kindred/sdl/SDL", "SDL_RenderDrawLine",             "(JIIII)I",                   com_kindred_sdl_SDL_SDL_RenderDrawLine},
    {"com/kindred/sdl/SDL", "SDL_SetRenderDrawColor",         "(JIIII)I",                   com_kindred_sdl_SDL_SDL_SetRenderDrawColor},
    {"com/kindred/sdl/SDL", "SDL_GetRenderDrawColor",         "(J)[I",                      com_kindred_sdl_SDL_SDL_GetRenderDrawColor},

    {"com/kindred/sdl/SDL", "SDL_CreateEvent",                "()J",                        com_kindred_sdl_SDL_SDL_CreateEvent},
    {"com/kindred/sdl/SDL", "SDL_FreeEvent",                  "(J)V",                       com_kindred_sdl_SDL_SDL_FreeEvent},
    {"com/kindred/sdl/SDL", "SDL_GetKeyEventKeySym",          "(J)I",                       com_kindred_sdl_SDL_SDL_GetKeyEventKeySym},
    {"com/kindred/sdl/SDL", "SDL_GetEventType",               "(J)I",                       com_kindred_sdl_SDL_SDL_GetEventType},
    {"com/kindred/sdl/SDL", "SDL_GetEventWindowEvent",        "(J)I",                       com_kindred_sdl_SDL_SDL_GetEventWindowEvent},
    {"com/kindred/sdl/SDL", "SDL_GetEventMouseButtonButton",  "(J)I",                       com_kindred_sdl_SDL_SDL_GetEventMouseButtonButton},
    {"com/kindred/sdl/SDL", "SDL_GetEventMouseButtonPos",     "(J)[I",                      com_kindred_sdl_SDL_SDL_GetEventMouseButtonPos},
    {"com/kindred/sdl/SDL", "SDL_GetEventMouseMotionPos",     "(J)[I",                      com_kindred_sdl_SDL_SDL_GetEventMouseMotionPos},
    {"com/kindred/sdl/SDL", "SDL_GetEventMouseWheelPos",      "(J)[I",                      com_kindred_sdl_SDL_SDL_GetEventMouseWheelPos},
    {"com/kindred/sdl/SDL", "SDL_PollEvent",                  "(J)I",                       com_kindred_sdl_SDL_SDL_PollEvent},
    {"com/kindred/sdl/SDL", "SDL_GetWindowFlags",             "(J)I",                       com_kindred_sdl_SDL_SDL_GetWindowFlags},
    {"com/kindred/sdl/SDL", "SDL_DestroyWindow",              "(J)V",                       com_kindred_sdl_SDL_SDL_DestroyWindow},
    {"com/kindred/sdl/SDL", "SDL_DestroyRenderer",            "(J)V",                       com_kindred_sdl_SDL_SDL_DestroyRenderer},
    {"com/kindred/sdl/SDL", "SDL_Quit",                       "()V",                        com_kindred_sdl_SDL_SDL_Quit},
    {"com/kindred/sdl/SDL", "SDL_GetTicks",                   "()J",                        com_kindred_sdl_SDL_SDL_GetTicks},

    {"com/kindred/sdl/SDL", "SDL_IMG_LoadPNG_RW",             "(J)J",                       com_kindred_sdl_SDL_SDL_IMG_LoadPNG_RW},
    {"com/kindred/sdl/SDL", "SDL_RWFromFile",                 "([B[B)J",                    com_kindred_sdl_SDL_SDL_RWFromFile},
    {"com/kindred/sdl/SDL", "SDL_GetSurfaceWidth",            "(J)I",                       com_kindred_sdl_SDL_SDL_GetSurfaceWidth},
    {"com/kindred/sdl/SDL", "SDL_GetSurfaceHeight",           "(J)I",                       com_kindred_sdl_SDL_SDL_GetSurfaceHeight},
    {"com/kindred/sdl/SDL", "SDL_GetSurfacePitch",            "(J)I",                       com_kindred_sdl_SDL_SDL_GetSurfacePitch},
//    {"com/kindred/sdl/SDL", "SDL_GetSurfacePixelData",        "(J)[B",                      com_kindred_sdl_SDL_SDL_GetSurfacePixelData},
    {"com/kindred/sdl/SDL", "SDL_GetSurfacePixelFormat",      "(J)I",                       com_kindred_sdl_SDL_SDL_GetSurfacePixelFormat},
    {"com/kindred/sdl/SDL", "SDL_GetWindowPixelFormat",       "(J)I",                       com_kindred_sdl_SDL_SDL_GetWindowPixelFormat},
    {"com/kindred/sdl/SDL", "SDL_ConvertSurfaceFormat",       "(JII)J",                     com_kindred_sdl_SDL_SDL_ConvertSurfaceFormat},
    {"com/kindred/sdl/SDL", "SDL_LockSurface",                "(J)I",                       com_kindred_sdl_SDL_SDL_LockSurface},
    {"com/kindred/sdl/SDL", "SDL_UnlockSurface",              "(J)V",                       com_kindred_sdl_SDL_SDL_UnlockSurface},
    {"com/kindred/sdl/SDL", "SDL_FreeSurface",                "(J)V",                       com_kindred_sdl_SDL_SDL_FreeSurface},
    {"com/kindred/sdl/SDL", "SDL_CreateTexture",              "(JIIII)J",                   com_kindred_sdl_SDL_SDL_CreateTexture},
    {"com/kindred/sdl/SDL", "SDL_UpdateTexture",              "(J[I[BI)I",                  com_kindred_sdl_SDL_SDL_UpdateTexture},
    
    
    
    // {"com/kindred/sdl/SDL", "Mir_SurfaceToGray",              "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceToGray},
    // {"com/kindred/sdl/SDL", "Mir_SurfaceBlackEffect",         "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceBlackEffect},
    // {"com/kindred/sdl/SDL", "Mir_SurfaceInverse",             "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceInverse},
    // {"com/kindred/sdl/SDL", "Mir_SurfaceAlpha",               "(JF)I",                      com_kindred_sdl_SDL_Mir_SurfaceAlpha},
    // {"com/kindred/sdl/SDL", "Mir_SurfaceBlendNormal",         "(JJIIF)I",                   com_kindred_sdl_SDL_Mir_SurfaceBlendNormal},
    // {"com/kindred/sdl/SDL", "Mir_SurfaceBlendNormalTransparent",         "(JJIIFIII)I",     com_kindred_sdl_SDL_Mir_SurfaceBlendNormalTransparent},
    // {"com/kindred/sdl/SDL", "Mir_SurfaceBlendAdd",            "(JJIIF)I",                   com_kindred_sdl_SDL_Mir_SurfaceBlendAdd},
    // {"com/kindred/sdl/SDL", "Mir_SurfaceBlendAddTransparent", "(JJIIFIII)I",                com_kindred_sdl_SDL_Mir_SurfaceBlendAddTransparent},
};

s32 count_SDL2FuncTable() {
    return sizeof(method_sdl_table) / sizeof(java_native_method);
}

__refer ptr_SDLFuncTable() {
    return &method_sdl_table[0];
}