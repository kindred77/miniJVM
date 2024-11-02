#include <stdio.h>
#include <string.h>
#include "depends/include/SDL2/SDL.h"
#include "depends/include/SDL2/SDL_rect.h"
#include "depends/include/SDL2/SDL_image.h"

#include "jvm.h"
#include "media.h"

GlobeRefer refers;

int com_kindred_sdl_SDL_SDL_Init(Runtime *runtime, JClass *clazz)
{
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    s32 flags = env->localvar_getInt(runtime->localvar, pos++);
    int ret = SDL_Init(flags);
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

    SDL_Renderer *render = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 r = env->localvar_getInt(runtime->localvar, pos++);
    s32 g = env->localvar_getInt(runtime->localvar, pos++);
    s32 b = env->localvar_getInt(runtime->localvar, pos++);
    s32 alpha = env->localvar_getInt(runtime->localvar, pos++);

    int res = SDL_SetRenderDrawColor(render, r, g, b, alpha);
    env->push_int(runtime->stack, res);
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

int com_kindred_sdl_SDL_SDL_GetWindowEvent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    //if (event->window) {
        env->push_int(runtime->stack, event->window.event);
    //}

    return 0;
}

int com_kindred_sdl_SDL_SDL_Quit(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Quit();

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

void surfaceToGray_ABGR8888(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = (pixels[index] & 0xff000000) >> 24;
            Uint8 blue = (pixels[index] & 0x000000ff);
            Uint8 green = (pixels[index] & 0x0000ff00) >> 8;
            Uint8 red = (pixels[index] & 0x00ff0000) >> 16;

            red *= 0.299;
            green *= 0.587;
            blue *= 0.114;

            pixels[index] = (alpha << 24) | blue | (green << 8) | (red << 16);
        }
    }
}

void surfaceToGray_RGB24(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * pitch + x * 3;

            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 red = pixels[index];
            Uint8 green = pixels[index + 1];
            Uint8 blue = pixels[index + 2];

            red *= 0.299;
            green *= 0.587;
            blue *= 0.114;

            // 将处理后的颜色值重新写回像素数据
            pixels[index] = red;
            pixels[index + 1] = green;
            pixels[index + 2] = blue;

        }
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceToGray(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    //SDL_Surface *dst_surface = SDL_ConvertSurfaceFormat(surface, SDL_PIXELFORMAT_RGB24, 0);
    //if (!dst_surface) {
    //    fprintf(stderr, "Unable to convert surface to SDL_PIXELFORMAT_RGB24! SDL Error: %s\n", SDL_GetError() );
    //    return 0;
    //}

    int ret = SDL_LockSurface(surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }

    Uint8 * pixels = ((Uint8*)surface->pixels);
    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        fprintf(stdout, "----c-----rgb24 \n");
        surfaceToGray_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ABGR8888) {
        fprintf(stdout, "----c-----abgr8888 \n");
        surfaceToGray_ABGR8888(pixels, width, height, pitch);
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockSurface(surface);

    env->push_int(runtime->stack, ret);

    return 0;
}

int com_kindred_sdl_SDL_Mir_SurfaceBlackEffect(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    //SDL_Surface *dst_surface = SDL_ConvertSurfaceFormat(surface, SDL_PIXELFORMAT_RGB24, 0);
    //if (!dst_surface) {
    //    fprintf(stderr, "Unable to convert surface to SDL_PIXELFORMAT_RGB24! SDL Error: %s\n", SDL_GetError() );
    //    return 0;
    //}

    int ret = SDL_LockSurface(surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }

    Uint8 * pixels = ((Uint8*)surface->pixels);
    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 计算当前像素在像素数据中的索引
                int index = y * pitch + x * 3;

                // 获取当前像素的红、绿、蓝颜色通道值
                Uint8 red = pixels[index];
                Uint8 green = pixels[index + 1];
                Uint8 blue = pixels[index + 2];

                int a4=(int)((double)((red+green+blue)/3*0.6)+0.5);
                red=max(a4,1);
                green=red;
                blue=red;

                // 将处理后的颜色值重新写回像素数据
                pixels[index] = red;
                pixels[index + 1] = green;
                pixels[index + 2] = blue;

            }
        }
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockSurface(surface);

    env->push_int(runtime->stack, ret);

    return 0;
}

void surfaceInverse_ABGR8888(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            int index = y * pitch + x * 4;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index];
            Uint8 blue = pixels[index+1];
            Uint8 green = pixels[index+2];
            Uint8 red = pixels[index+3];

            red ^= 0xff;
            green ^= 0xff;
            blue ^= 0xff;

            pixels[index] = alpha;
            pixels[index+1] = blue;
            pixels[index+2] = green;
            pixels[index+3] = red;
        }
    }
}

void surfaceInverse_RGB24(Uint8 * pixels, int width, int height, int pitch) {
    for (int i = 0; i < pitch * height; ++i) {
        pixels[i] ^= 0xff;
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceInverse(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    int ret = SDL_LockSurface(surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }

    Uint8 * pixels = ((Uint8*)surface->pixels);
    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        surfaceInverse_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ABGR8888) {
        surfaceInverse_ABGR8888(pixels, width, height, pitch);
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockSurface(surface);

    env->push_int(runtime->stack, ret);

    return 0;
}

void surfaceAlpha_ABGR8888(Uint8 * pixels, float alpha, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            int index = y * pitch + x * 4;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index];
            Uint8 blue = pixels[index+1];
            Uint8 green = pixels[index+2];
            Uint8 red = pixels[index+3];

            red ^= 0xff;
            green ^= 0xff;
            blue ^= 0xff;

            pixels[index] = alpha;
            pixels[index+1] = blue;
            pixels[index+2] = green;
            pixels[index+3] = red;
        }
    }
}

void surfaceAlpha_RGB24(Uint8 * pixels, float alpha, int width, int height, int pitch) {
    for (int i = 0; i < pitch * height; ++i) {
        pixels[i] *= alpha;
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceAlpha(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    Int2Float psize;
    psize.i = env->localvar_getInt(runtime->localvar, pos++);
    float alpha = (float)psize.f;

    int ret = SDL_LockSurface(surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }

    Uint8 * pixels = ((Uint8*)surface->pixels);
    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        surfaceAlpha_RGB24(pixels, alpha, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ABGR8888) {
        surfaceAlpha_ABGR8888(pixels, alpha, width, height, pitch);
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockSurface(surface);

    env->push_int(runtime->stack, ret);

    return 0;
}

int com_kindred_sdl_SDL_Mir_SurfaceBlendNormal(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *dst_surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    SDL_Surface *src_surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 x = env->localvar_getInt(runtime->localvar, pos++);
    s32 y = env->localvar_getInt(runtime->localvar, pos++);
    Int2Float psize;
    psize.i = env->localvar_getInt(runtime->localvar, pos++);
    float alpha = (float)psize.f;

    Uint8 * dst_pixels = ((Uint8*)dst_surface->pixels);
    int dst_width = dst_surface->w;
    int dst_height = dst_surface->h;
    int dst_pitch = dst_surface->pitch;

    Uint8 * src_pixels = ((Uint8*)src_surface->pixels);
    int src_width = src_surface->w;
    int src_height = src_surface->h;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout, "Do noting. \n");
        return 0;
    }

    int ret = SDL_LockSurface(dst_surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }
    
    if (dst_surface->format->format == SDL_PIXELFORMAT_RGB24
        && src_surface->format->format == SDL_PIXELFORMAT_RGB24) {
        
        int left = x < 0 ? 0 : x;
        int top = y < 0 ? 0 : y;
        int tarleft = x < 0 ? -x : 0;
        int tartop = y < 0 ? -y : 0;
        int rx = left + src_width - tarleft;
        if (rx >= dst_width)
            rx = dst_width - 1;
        int by = top + src_height - tartop;
        if (by >= dst_height)
            by = dst_height - 1;
        for (int i = top; i < by; ++i) {
            for (int j = left; j < rx; ++j) {
                int _idx_this = (j + i * dst_width) * 3;
                int _idx_that = (j - left + tarleft + (i - top + tartop) * src_width) * 3;
                dst_pixels[_idx_this] = (Uint8) (src_pixels[_idx_that] * alpha);
                dst_pixels[_idx_this + 1] = (Uint8) (src_pixels[_idx_that + 1] * alpha);
                dst_pixels[_idx_this + 2] = (Uint8) (src_pixels[_idx_that + 2] * alpha);
            }
        }
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockSurface(dst_surface);

    env->push_int(runtime->stack, ret);

    return 0;
}

int com_kindred_sdl_SDL_Mir_SurfaceBlendNormalTransparent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *dst_surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    SDL_Surface *src_surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 x = env->localvar_getInt(runtime->localvar, pos++);
    s32 y = env->localvar_getInt(runtime->localvar, pos++);
    Int2Float psize;
    psize.i = env->localvar_getInt(runtime->localvar, pos++);
    float alpha = (float)psize.f;
    s32 r = env->localvar_getInt(runtime->localvar, pos++);
    s32 g = env->localvar_getInt(runtime->localvar, pos++);
    s32 b = env->localvar_getInt(runtime->localvar, pos++);

    Uint8 * dst_pixels = ((Uint8*)dst_surface->pixels);
    int dst_width = dst_surface->w;
    int dst_height = dst_surface->h;
    int dst_pitch = dst_surface->pitch;

    Uint8 * src_pixels = ((Uint8*)src_surface->pixels);
    int src_width = src_surface->w;
    int src_height = src_surface->h;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout, "Do noting. \n");
        return 0;
    }

    int ret = SDL_LockSurface(dst_surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }
    
    if (dst_surface->format->format == SDL_PIXELFORMAT_RGB24
        && src_surface->format->format == SDL_PIXELFORMAT_RGB24) {
        
        int left = x < 0 ? 0 : x;
        int top = y < 0 ? 0 : y;
        int tarleft = x < 0 ? -x : 0;
        int tartop = y < 0 ? -y : 0;
        int rx = left + src_width - tarleft;
        if (rx >= dst_width)
            rx = dst_width - 1;
        int by = top + src_height - tartop;
        if (by >= dst_height)
            by = dst_height - 1;
        for (int i = top; i < by; ++i) {
            for (int j = left; j < rx; ++j) {
                int _idx_this = (j + i * dst_width) * 3;
                int _idx_that = (j - left + tarleft + (i - top + tartop) * src_width) * 3;
                Uint8 _r = src_pixels[_idx_that];
                Uint8 _g = src_pixels[_idx_that + 1];
                Uint8 _b = src_pixels[_idx_that + 2];
                if (r != _r || _g != g || _b != b) {
                    dst_pixels[_idx_this] = (Uint8) (_r * alpha);
                    dst_pixels[_idx_this + 1] = (Uint8) (_g * alpha);
                    dst_pixels[_idx_this + 2] = (Uint8) (_b * alpha);
                }
            }
        }
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockSurface(dst_surface);

    env->push_int(runtime->stack, ret);

    return 0;
}

int com_kindred_sdl_SDL_Mir_SurfaceBlendAdd(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *dst_surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    SDL_Surface *src_surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 x = env->localvar_getInt(runtime->localvar, pos++);
    s32 y = env->localvar_getInt(runtime->localvar, pos++);
    Int2Float psize;
    psize.i = env->localvar_getInt(runtime->localvar, pos++);
    float alpha = (float)psize.f;

    Uint8 * dst_pixels = ((Uint8*)dst_surface->pixels);
    int dst_width = dst_surface->w;
    int dst_height = dst_surface->h;
    int dst_pitch = dst_surface->pitch;

    Uint8 * src_pixels = ((Uint8*)src_surface->pixels);
    int src_width = src_surface->w;
    int src_height = src_surface->h;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout, "Do noting. \n");
        return 0;
    }

    int ret = SDL_LockSurface(dst_surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }
    
    if (dst_surface->format->format == SDL_PIXELFORMAT_RGB24
        && src_surface->format->format == SDL_PIXELFORMAT_RGB24) {
        
        int left = x < 0 ? 0 : x;
        int top = y < 0 ? 0 : y;
        int tarleft = x < 0 ? -x : 0;
        int tartop = y < 0 ? -y : 0;
        int rx = left + src_width - tarleft;
        if (rx >= dst_width)
            rx = dst_width - 1;
        int by = top + src_height - tartop;
        if (by >= dst_height)
            by = dst_height - 1;
        for (int i = top; i < by; ++i) {
            for (int j = left; j < rx; ++j) {
                int _idx_this = (j + i * dst_width) * 3;
                int _idx_that = (j - left + tarleft + (i - top + tartop) * src_width) * 3;
                float Rd = (src_pixels[_idx_that] & 0xff) * alpha;
                float Gd = (src_pixels[_idx_that + 1] & 0xff) * alpha;
                float Bd = (src_pixels[_idx_that + 2] & 0xff) * alpha;
                int Rs = dst_pixels[_idx_this] & 0xff;
                int Gs = dst_pixels[_idx_this + 1] & 0xff;
                int Bs = dst_pixels[_idx_this + 2] & 0xff;
                dst_pixels[_idx_this] = (Uint8) min(255, Rd*Rd/255+Rs);
                dst_pixels[_idx_this + 1] = (Uint8) min(255, Gd*Gd/255+Gs);
                dst_pixels[_idx_this + 2] = (Uint8) min(255, Bd*Bd/255+Bs);
            }
        }
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockSurface(dst_surface);

    env->push_int(runtime->stack, ret);

    return 0;
}

int com_kindred_sdl_SDL_Mir_SurfaceBlendAddTransparent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *dst_surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    SDL_Surface *src_surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 x = env->localvar_getInt(runtime->localvar, pos++);
    s32 y = env->localvar_getInt(runtime->localvar, pos++);
    Int2Float psize;
    psize.i = env->localvar_getInt(runtime->localvar, pos++);
    float alpha = (float)psize.f;
    s32 r = env->localvar_getInt(runtime->localvar, pos++);
    s32 g = env->localvar_getInt(runtime->localvar, pos++);
    s32 b = env->localvar_getInt(runtime->localvar, pos++);

    Uint8 * dst_pixels = ((Uint8*)dst_surface->pixels);
    int dst_width = dst_surface->w;
    int dst_height = dst_surface->h;
    int dst_pitch = dst_surface->pitch;

    Uint8 * src_pixels = ((Uint8*)src_surface->pixels);
    int src_width = src_surface->w;
    int src_height = src_surface->h;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout, "Do noting. \n");
        return 0;
    }

    int ret = SDL_LockSurface(dst_surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }
    
    if (dst_surface->format->format == SDL_PIXELFORMAT_RGB24
        && src_surface->format->format == SDL_PIXELFORMAT_RGB24) {
        
        int left = x < 0 ? 0 : x;
        int top = y < 0 ? 0 : y;
        int tarleft = x < 0 ? -x : 0;
        int tartop = y < 0 ? -y : 0;
        int rx = left + src_width - tarleft;
        if (rx >= dst_width)
            rx = dst_width - 1;
        int by = top + src_height - tartop;
        if (by >= dst_height)
            by = dst_height - 1;
        for (int i = top; i < by; ++i) {
            for (int j = left; j < rx; ++j) {
                int _idx_this = (j + i * dst_width) * 3;
                int _idx_that = (j - left + tarleft + (i - top + tartop) * src_width) * 3;
                if(src_pixels[_idx_that] == r && src_pixels[_idx_that + 1] == g && src_pixels[_idx_that + 2] == b) continue;
                float Rd = (src_pixels[_idx_that] & 0xff) * alpha;
                float Gd = (src_pixels[_idx_that + 1] & 0xff) * alpha;
                float Bd = (src_pixels[_idx_that + 2] & 0xff) * alpha;
                int Rs = dst_pixels[_idx_this] & 0xff;
                int Gs = dst_pixels[_idx_this + 1] & 0xff;
                int Bs = dst_pixels[_idx_this + 2] & 0xff;
                dst_pixels[_idx_this] = (Uint8) min(255, Rd*Rd/255+Rs);
                dst_pixels[_idx_this + 1] = (Uint8) min(255, Gd*Gd/255+Gs);
                dst_pixels[_idx_this + 2] = (Uint8) min(255, Bd*Bd/255+Bs);
            }
        }
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockSurface(dst_surface);

    env->push_int(runtime->stack, ret);

    return 0;
}

static java_native_method method_sdl_table[] = {
    {"com/kindred/sdl/SDL", "SDL_Init",                       "(I)I",                       com_kindred_sdl_SDL_SDL_Init},
    {"com/kindred/sdl/SDL", "SDL_CreateWindow",               "([BIIIII)J",                 com_kindred_sdl_SDL_SDL_CreateWindow},
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
    {"com/kindred/sdl/SDL", "SDL_SetRenderDrawColor",         "(JIIII)I",                   com_kindred_sdl_SDL_SDL_SetRenderDrawColor},

    {"com/kindred/sdl/SDL", "SDL_CreateEvent",                "()J",                        com_kindred_sdl_SDL_SDL_CreateEvent},
    {"com/kindred/sdl/SDL", "SDL_FreeEvent",                  "(J)V",                       com_kindred_sdl_SDL_SDL_FreeEvent},
    {"com/kindred/sdl/SDL", "SDL_GetKeyEventKeySym",          "(J)I",                       com_kindred_sdl_SDL_SDL_GetKeyEventKeySym},
    {"com/kindred/sdl/SDL", "SDL_GetEventType",               "(J)I",                       com_kindred_sdl_SDL_SDL_GetEventType},
    {"com/kindred/sdl/SDL", "SDL_GetWindowEvent",             "(J)I",                       com_kindred_sdl_SDL_SDL_GetWindowEvent},
    {"com/kindred/sdl/SDL", "SDL_PollEvent",                  "(J)I",                       com_kindred_sdl_SDL_SDL_PollEvent},
    
    {"com/kindred/sdl/SDL", "SDL_Quit",                       "()V",                        com_kindred_sdl_SDL_SDL_Quit},

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
    {"com/kindred/sdl/SDL", "Mir_SurfaceToGray",              "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceToGray},
    {"com/kindred/sdl/SDL", "Mir_SurfaceBlackEffect",         "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceBlackEffect},
    {"com/kindred/sdl/SDL", "Mir_SurfaceInverse",             "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceInverse},
    {"com/kindred/sdl/SDL", "Mir_SurfaceAlpha",               "(JF)I",                      com_kindred_sdl_SDL_Mir_SurfaceAlpha},
    {"com/kindred/sdl/SDL", "Mir_SurfaceBlendNormal",         "(JJIIF)I",                   com_kindred_sdl_SDL_Mir_SurfaceBlendNormal},
    {"com/kindred/sdl/SDL", "Mir_SurfaceBlendNormalTransparent",         "(JJIIFIII)I",     com_kindred_sdl_SDL_Mir_SurfaceBlendNormalTransparent},
    {"com/kindred/sdl/SDL", "Mir_SurfaceBlendAdd",            "(JJIIF)I",                   com_kindred_sdl_SDL_Mir_SurfaceBlendAdd},
    {"com/kindred/sdl/SDL", "Mir_SurfaceBlendAddTransparent", "(JJIIFIII)I",                com_kindred_sdl_SDL_Mir_SurfaceBlendAddTransparent},
};

s32 count_SDL2FuncTable() {
    return sizeof(method_sdl_table) / sizeof(java_native_method);
}

__refer ptr_SDLFuncTable() {
    return &method_sdl_table[0];
}