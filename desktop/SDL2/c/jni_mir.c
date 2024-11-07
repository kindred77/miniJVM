#include <stdio.h>
#include <string.h>
#include "depends/include/SDL2/SDL.h"
#include "depends/include/SDL2/SDL_rect.h"
#include "depends/include/SDL2/SDL_image.h"

#include "jvm.h"
#include "media.h"

int com_kindred_sdl_SDL_SDL_UpdateTextureWithSurface(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
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

    int ret = SDL_UpdateTexture(texture, (ptr_rect == NULL ? NULL : &rect), surface->pixels, surface->pitch);
    if (ret) {
        fprintf(stderr, "Unable to update texture! SDL Error: %s\n", SDL_GetError() );
    }

    env->push_int(runtime->stack, ret);
    return 0;
}

void surfaceToGray_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            red *= 0.299;
            green *= 0.587;
            blue *= 0.114;

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
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
        env->push_int(runtime->stack, ret);
        return 0;
    }

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceToGray_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceToGray_ARGB8888(pixels, width, height, pitch);
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

int com_kindred_sdl_SDL_Mir_TextureToGray(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Surface *surface = NULL;
    int ret = SDL_LockTextureToSurface(texture, NULL, &surface);
    if (ret) {
        fprintf(stderr, "Unable to lock texture! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, ret);
        return 0;
    }

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceToGray_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceToGray_ARGB8888(pixels, width, height, pitch);
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockTexture(texture);

    env->push_int(runtime->stack, ret);

    return 0;
}

void surfaceBlackEffect_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            int a4=(int)((double)((red+green+blue)/3*0.6)+0.5);
            red=max(a4,1);
            green=red;
            blue=red;

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }
}

void surfaceBlackEffect_RGB24(Uint8 * pixels, int width, int height, int pitch) {
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceBlackEffect_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceBlackEffect_ARGB8888(pixels, width, height, pitch);
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

void surfaceWhiteEffect_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            int a4=(int)((double)((red+green+blue)/3*0.6)+0.5);
            red=min(a4,255);
            green=red;
            blue=red;

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }
}

void surfaceWhiteEffect_RGB24(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * pitch + x * 3;

            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 red = pixels[index];
            Uint8 green = pixels[index + 1];
            Uint8 blue = pixels[index + 2];

            int a4=(int)((double)((red+green+blue)/3*0.6)+0.5);
            red=min(a4,255);
            green=red;
            blue=red;

            // 将处理后的颜色值重新写回像素数据
            pixels[index] = red;
            pixels[index + 1] = green;
            pixels[index + 2] = blue;

        }
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceWhiteEffect(Runtime *runtime, JClass *clazz) {
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceWhiteEffect_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceWhiteEffect_ARGB8888(pixels, width, height, pitch);
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

void surfaceRedEffect_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=max(a4,0x20);
            green=0;
            blue=0;

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }
}

void surfaceRedEffect_RGB24(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * pitch + x * 3;

            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 red = pixels[index];
            Uint8 green = pixels[index + 1];
            Uint8 blue = pixels[index + 2];

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=max(a4,0x20);
            green=0;
            blue=0;

            // 将处理后的颜色值重新写回像素数据
            pixels[index] = red;
            pixels[index + 1] = green;
            pixels[index + 2] = blue;

        }
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceRedEffect(Runtime *runtime, JClass *clazz) {
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceRedEffect_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceRedEffect_ARGB8888(pixels, width, height, pitch);
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

void surfaceGreenEffect_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=0;
            green=max(a4,0x20);
            blue=0;

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }
}

void surfaceGreenEffect_RGB24(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * pitch + x * 3;

            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 red = pixels[index];
            Uint8 green = pixels[index + 1];
            Uint8 blue = pixels[index + 2];

            int a4=(int)((double)(red+green+blue)/3+0.5);
            red=0;
            green=max(a4,0x20);
            blue=0;

            // 将处理后的颜色值重新写回像素数据
            pixels[index] = red;
            pixels[index + 1] = green;
            pixels[index + 2] = blue;

        }
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceGreenEffect(Runtime *runtime, JClass *clazz) {
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceGreenEffect_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceGreenEffect_ARGB8888(pixels, width, height, pitch);
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

void surfaceBlueEffect_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=0;
            green=0;
            blue=max(a4,0x08);

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }
}

void surfaceBlueEffect_RGB24(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * pitch + x * 3;

            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 red = pixels[index];
            Uint8 green = pixels[index + 1];
            Uint8 blue = pixels[index + 2];

            int a4=(int)((double)(red+green+blue)/3+0.5);
            red=0;
            green=0;
            blue=max(a4,0x08);

            // 将处理后的颜色值重新写回像素数据
            pixels[index] = red;
            pixels[index + 1] = green;
            pixels[index + 2] = blue;

        }
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceBlueEffect(Runtime *runtime, JClass *clazz) {
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceBlueEffect_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceBlueEffect_ARGB8888(pixels, width, height, pitch);
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

void surfaceYellowEffect_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=a4;
            green=red;
            blue=0;

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }
}

void surfaceYellowEffect_RGB24(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * pitch + x * 3;

            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 red = pixels[index];
            Uint8 green = pixels[index + 1];
            Uint8 blue = pixels[index + 2];

            int a4=(int)((double)(red+green+blue)/3+0.5);
            red=a4;
            green=red;
            blue=0;

            // 将处理后的颜色值重新写回像素数据
            pixels[index] = red;
            pixels[index + 1] = green;
            pixels[index + 2] = blue;

        }
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceYellowEffect(Runtime *runtime, JClass *clazz) {
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceYellowEffect_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceYellowEffect_ARGB8888(pixels, width, height, pitch);
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

void surfaceFuchsiaEffect_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=a4;
            //green=green;
            blue=red;

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }
}

void surfaceFuchsiaEffect_RGB24(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * pitch + x * 3;

            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 red = pixels[index];
            Uint8 green = pixels[index + 1];
            Uint8 blue = pixels[index + 2];

            int a4=(int)((double)(red+green+blue)/3+0.5);
            red=a4;
            //green=green;
            blue=red;

            // 将处理后的颜色值重新写回像素数据
            pixels[index] = red;
            pixels[index + 1] = green;
            pixels[index + 2] = blue;

        }
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceFuchsiaEffect(Runtime *runtime, JClass *clazz) {
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceFuchsiaEffect_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceFuchsiaEffect_ARGB8888(pixels, width, height, pitch);
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

void surfaceBrightEffect_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            red=(Uint8)min((int)((double)(red*1.3)+0.5),255);
            green=(Uint8)min((int)((double)(green*1.3)+0.5),255);
            blue=(Uint8)min((int)((double)(blue*1.3)+0.5),255);

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }
}

void surfaceBrightEffect_RGB24(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * pitch + x * 3;

            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 red = pixels[index];
            Uint8 green = pixels[index + 1];
            Uint8 blue = pixels[index + 2];

            red=(Uint8)min((int)((double)(red*1.3)+0.5),255);
            green=(Uint8)min((int)((double)(green*1.3)+0.5),255);
            blue=(Uint8)min((int)((double)(blue*1.3)+0.5),255);

            // 将处理后的颜色值重新写回像素数据
            pixels[index] = red;
            pixels[index + 1] = green;
            pixels[index + 2] = blue;

        }
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceBrightEffect(Runtime *runtime, JClass *clazz) {
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceBrightEffect_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceBrightEffect_ARGB8888(pixels, width, height, pitch);
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

void surfaceGrayEffect_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            int a4=(int)((double)(red+green+blue)/3+0.5);
            red=(Uint8)a4;
            green=red;
            blue=red;

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
        }
    }
}

void surfaceGrayEffect_RGB24(Uint8 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * pitch + x * 3;

            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 red = pixels[index];
            Uint8 green = pixels[index + 1];
            Uint8 blue = pixels[index + 2];

            int a4=(int)((double)(red+green+blue)/3+0.5);
            red=(Uint8)a4;
            green=red;
            blue=red;

            // 将处理后的颜色值重新写回像素数据
            pixels[index] = red;
            pixels[index + 1] = green;
            pixels[index + 2] = blue;

        }
    }
}

int com_kindred_sdl_SDL_Mir_SurfaceGrayEffect(Runtime *runtime, JClass *clazz) {
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceGrayEffect_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceGrayEffect_ARGB8888(pixels, width, height, pitch);
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

int com_kindred_sdl_SDL_Mir_TextureBlackEffect(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Surface *surface = NULL;
    int ret = SDL_LockTextureToSurface(texture, NULL, &surface);
    if (ret) {
        fprintf(stderr, "Unable to lock texture! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, ret);
        return 0;
    }

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceBlackEffect_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceBlackEffect_ARGB8888(pixels, width, height, pitch);
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockTexture(texture);

    env->push_int(runtime->stack, ret);

    return 0;
}

void surfaceInverse_ARGB8888(Uint32 * pixels, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            red ^= 0xff;
            green ^= 0xff;
            blue ^= 0xff;

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceInverse_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceInverse_ARGB8888(pixels, width, height, pitch);
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

int com_kindred_sdl_SDL_Mir_TextureInverse(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Surface *surface = NULL;
    int ret = SDL_LockTextureToSurface(texture, NULL, &surface);
    if (ret) {
        fprintf(stderr, "Unable to lock texture! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, ret);
        return 0;
    }

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceInverse_RGB24(pixels, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceInverse_ARGB8888(pixels, width, height, pitch);
    }
    else {
        fprintf(stderr, "Unable to convert texture, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockTexture(texture);

    env->push_int(runtime->stack, ret);

    return 0;
}

void surfaceAlpha_ARGB8888(Uint32 * pixels, float alpha_convert, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            Uint8 alpha = pixels[index] >> 24 & 0xFF;
            Uint8 red = pixels[index] >> 16 & 0xFF;
            Uint8 green = pixels[index] >> 8 & 0xFF;
            Uint8 blue = pixels[index] & 0xFF;

            alpha *= alpha_convert;

            pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
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

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceAlpha_RGB24(pixels, alpha, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceAlpha_ARGB8888(pixels, alpha, width, height, pitch);
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

int com_kindred_sdl_SDL_Mir_TextureAlpha(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Texture *texture = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Surface *surface = NULL;
    int ret = SDL_LockTextureToSurface(texture, NULL, &surface);
    if (ret) {
        fprintf(stderr, "Unable to lock texture! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, ret);
        return 0;
    }

    Int2Float psize;
    psize.i = env->localvar_getInt(runtime->localvar, pos++);
    float alpha = (float)psize.f;

    int width = surface->w;
    int height = surface->h;
    int pitch = surface->pitch;
    
    if (surface->format->format == SDL_PIXELFORMAT_RGB24) {
        Uint8 * pixels = ((Uint8*)surface->pixels);
        surfaceAlpha_RGB24(pixels, alpha, width, height, pitch);
    }
    else if (surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        Uint32 * pixels = ((Uint32*)surface->pixels);
        surfaceAlpha_ARGB8888(pixels, alpha, width, height, pitch);
    }
    else {
        fprintf(stderr, "Unable to convert surface, pixel format unsupported! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockTexture(texture);

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

    Uint32 * dst_pixels = ((Uint32*)dst_surface->pixels);
    int dst_width = dst_surface->w;
    int dst_height = dst_surface->h;
    int dst_pitch = dst_surface->pitch;

    Uint32 * src_pixels = ((Uint32*)src_surface->pixels);
    int src_width = src_surface->w;
    int src_height = src_surface->h;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout, "Do noting. \n");
        env->push_int(runtime->stack, 0);
        return 0;
    }

    int ret = SDL_LockSurface(dst_surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }
    
    if (dst_surface->format->format == SDL_PIXELFORMAT_ARGB8888
        && src_surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        
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
                
                int _idx_this = (j + i * dst_width);
                int _idx_that = (j - left + tarleft + (i - top + tartop) * src_width);

                Uint32 d = dst_pixels[_idx_this];
                Uint32 s = src_pixels[_idx_that];
                // 提取ARGB分量
                Uint8 da = (d >> 24) & 0xFF;
                Uint8 dr = (d >> 16) & 0xFF;
                Uint8 dg = (d >> 8) & 0xFF;
                Uint8 db = d & 0xFF;

                Uint8 sa = (s >> 24) & 0xFF;
                Uint8 sr = (s >> 16) & 0xFF;
                Uint8 sg = (s >> 8) & 0xFF;
                Uint8 sb = s & 0xFF;

                Uint8 a = da;
                Uint8 r = sr * alpha;
                Uint8 g = sg * alpha;
                Uint8 b = sb * alpha;

                dst_pixels[_idx_this] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
    }
    else {
        fprintf(stderr, "Unable to blend surfaces, pixel format unsupported, both must be SDL_PIXELFORMAT_ARGB8888! \n");
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

    Uint32 * dst_pixels = ((Uint32*)dst_surface->pixels);
    int dst_width = dst_surface->w;
    int dst_height = dst_surface->h;
    int dst_pitch = dst_surface->pitch;

    Uint32 * src_pixels = ((Uint32*)src_surface->pixels);
    int src_width = src_surface->w;
    int src_height = src_surface->h;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout, "Do noting. \n");
        env->push_int(runtime->stack, 0);
        return 0;
    }

    int ret = SDL_LockSurface(dst_surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }
    
    if (dst_surface->format->format == SDL_PIXELFORMAT_ARGB8888
        && src_surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        
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

                int _idx_this = (j + i * dst_width);
                int _idx_that = (j - left + tarleft + (i - top + tartop) * src_width);

                Uint32 d = dst_pixels[_idx_this];
                Uint32 s = src_pixels[_idx_that];
                // 提取ARGB分量
                Uint8 da = (d >> 24) & 0xFF;
                // Uint8 dr = (d >> 16) & 0xFF;
                // Uint8 dg = (d >> 8) & 0xFF;
                // Uint8 db = d & 0xFF;

                Uint8 sa = (s >> 24) & 0xFF;
                Uint8 sr = (s >> 16) & 0xFF;
                Uint8 sg = (s >> 8) & 0xFF;
                Uint8 sb = s & 0xFF;

                if (r == sr && g == sg && b == sb || !sa) continue;

                //依然用dst的alpha值
                sr *= alpha;
                sg *= alpha;
                sb *= alpha;
                dst_pixels[_idx_this] = (da << 24) | (sr << 16) | (sg << 8) | sb;
            }
        }
    }
    else {
        fprintf(stderr, "Unable to blend surface, pixel format unsupported, both must be SDL_PIXELFORMAT_ARGB8888! \n");
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

    Uint32 * dst_pixels = ((Uint32*)dst_surface->pixels);
    int dst_width = dst_surface->w;
    int dst_height = dst_surface->h;
    int dst_pitch = dst_surface->pitch;

    Uint32 * src_pixels = ((Uint32*)src_surface->pixels);
    int src_width = src_surface->w;
    int src_height = src_surface->h;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout, "Do noting. \n");
        env->push_int(runtime->stack, 0);
        return 0;
    }

    int ret = SDL_LockSurface(dst_surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }
    
    if (dst_surface->format->format == SDL_PIXELFORMAT_ARGB8888
        && src_surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        
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

                int _idx_this = (j + i * dst_width);
                int _idx_that = (j - left + tarleft + (i - top + tartop) * src_width);

                Uint32 d = dst_pixels[_idx_this];
                Uint32 s = src_pixels[_idx_that];
                // 提取ARGB分量
                Uint8 da = (d >> 24) & 0xFF;
                Uint8 dr = (d >> 16) & 0xFF;
                Uint8 dg = (d >> 8) & 0xFF;
                Uint8 db = d & 0xFF;

                Uint8 sa = (s >> 24) & 0xFF;
                Uint8 sr = (s >> 16) & 0xFF;
                Uint8 sg = (s >> 8) & 0xFF;
                Uint8 sb = s & 0xFF;

                float fr = sr * alpha;
                float fg = sg * alpha;
                float fb = sb * alpha;

                dst_pixels[_idx_this] = (da << 24) | ((Uint8) min(255, fr*fr/255+dr) << 16) 
                    | ((Uint8) min(255, fg*fg/255+dg) << 8) 
                    | (Uint8) min(255, fb*fb/255+db);

            }
        }
    }
    else {
        fprintf(stderr, "Unable to blend surface, pixel format unsupported, both must be SDL_PIXELFORMAT_ARGB8888! \n");
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

    Uint32 * dst_pixels = ((Uint32*)dst_surface->pixels);
    int dst_width = dst_surface->w;
    int dst_height = dst_surface->h;
    int dst_pitch = dst_surface->pitch;

    Uint32 * src_pixels = ((Uint32*)src_surface->pixels);
    int src_width = src_surface->w;
    int src_height = src_surface->h;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout, "Do noting. \n");
        env->push_int(runtime->stack, 0);
        return 0;
    }

    int ret = SDL_LockSurface(dst_surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError() );
        env->push_int(runtime->stack, -1);
        return 0;
    }
    
    if (dst_surface->format->format == SDL_PIXELFORMAT_ARGB8888
        && src_surface->format->format == SDL_PIXELFORMAT_ARGB8888) {
        
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

                int _idx_this = (j + i * dst_width);
                int _idx_that = (j - left + tarleft + (i - top + tartop) * src_width);

                Uint32 d = dst_pixels[_idx_this];
                Uint32 s = src_pixels[_idx_that];
                // 提取ARGB分量
                Uint8 da = (d >> 24) & 0xFF;
                Uint8 dr = (d >> 16) & 0xFF;
                Uint8 dg = (d >> 8) & 0xFF;
                Uint8 db = d & 0xFF;

                Uint8 sa = (s >> 24) & 0xFF;
                Uint8 sr = (s >> 16) & 0xFF;
                Uint8 sg = (s >> 8) & 0xFF;
                Uint8 sb = s & 0xFF;

                float fr = sr * alpha;
                float fg = sg * alpha;
                float fb = sb * alpha;

                if (r == sr && g == sg && b == sb || !sa) continue;

                dst_pixels[_idx_this] = (da << 24) | ((Uint8) min(255, fr*fr/255+dr) << 16) 
                    | ((Uint8) min(255, fg*fg/255+dg) << 8) 
                    | (Uint8) min(255, fb*fb/255+db);
            }
        }
    }
    else {
        fprintf(stderr, "Unable to blend surface, pixel format unsupported, both must be SDL_PIXELFORMAT_ARGB8888! \n");
        env->push_int(runtime->stack, -1);
        return 0;
    }

    SDL_UnlockSurface(dst_surface);

    env->push_int(runtime->stack, ret);

    return 0;
}



void Mir_ImGui_SDL2_Init(SDL_Window * window, SDL_Renderer *renderer);
int Mir_ImGui_SDL2_ProcessEvent(SDL_Event * event);
void Mir_ImGui_SDLRenderer2_NewFrame();
void Mir_ImGui_SDL2_NewFrame();
void Mir_ImGui_NewFrame();
int Mir_ImGui_Begin();
void Mir_ImGui_Text();
int Mir_ImGui_InputText(const char * title, char * buf, int length);
void Mir_ImGui_End();
void Mir_ImGui_Render(SDL_Renderer * renderer);
void Mir_ImGui_Destroy();

int com_kindred_sdl_SDL_ImGui_SDL2_Init(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    SDL_Window *window = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    SDL_Renderer *renderer = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    Mir_ImGui_SDL2_Init(window, renderer);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_SDL2_ProcessEvent(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    SDL_Event *event = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    int ret = Mir_ImGui_SDL2_ProcessEvent(event);
    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_SDLRenderer2_NewFrame(Runtime *runtime, JClass *clazz) {
    Mir_ImGui_SDLRenderer2_NewFrame();
    return 0;
}

int com_kindred_sdl_SDL_ImGui_SDL2_NewFrame(Runtime *runtime, JClass *clazz) {
    Mir_ImGui_SDL2_NewFrame();
    return 0;
}

int com_kindred_sdl_SDL_ImGui_NewFrame(Runtime *runtime, JClass *clazz) {
    Mir_ImGui_NewFrame();
    return 0;
}

int com_kindred_sdl_SDL_ImGui_Begin(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;

    int ret = Mir_ImGui_Begin();

    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_Text(Runtime *runtime, JClass *clazz) {
    Mir_ImGui_Text();
    return 0;
}

int com_kindred_sdl_SDL_ImGui_InputText(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    Instance *title_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *title = NULL;
    if (title_arr) {
        title = title_arr->arr_body;
    }

    Instance *buf_arr = env->localvar_getRefer(runtime->localvar, pos++);
    int ret = -1;
    if (!buf_arr) {
        fprintf(stderr, "Failed to create inputtext, buffer is null. \n");
    }
    else {
        c8 *buf = buf_arr->arr_body;
        ret = Mir_ImGui_InputText(title, buf, buf_arr->arr_length);
    }
    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_End(Runtime *runtime, JClass *clazz) {
    Mir_ImGui_End();
    return 0;
}

int com_kindred_sdl_SDL_ImGui_Render(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    SDL_Renderer *renderer = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    Mir_ImGui_Render(renderer);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_Destroy(Runtime *runtime, JClass *clazz) {
    Mir_ImGui_Destroy();
    return 0;
}

static java_native_method method_mir_table[] = {

    //for test and use in future
    {"com/kindred/mir/engine/MirJNI", "SDL_UpdateTextureWithSurface",   "(JJ[I)I",                    com_kindred_sdl_SDL_SDL_UpdateTextureWithSurface},
    {"com/kindred/mir/engine/MirJNI", "Mir_TextureToGray",              "(J)I",                       com_kindred_sdl_SDL_Mir_TextureToGray},
    {"com/kindred/mir/engine/MirJNI", "Mir_TextureBlackEffect",         "(J)I",                       com_kindred_sdl_SDL_Mir_TextureBlackEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_TextureInverse",             "(J)I",                       com_kindred_sdl_SDL_Mir_TextureInverse},
    {"com/kindred/mir/engine/MirJNI", "Mir_TextureAlpha",               "(JF)I",                      com_kindred_sdl_SDL_Mir_TextureAlpha},

    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceToGray",              "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceToGray},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlackEffect",         "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceBlackEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceWhiteEffect",         "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceWhiteEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceRedEffect",           "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceRedEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceGreenEffect",         "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceGreenEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlueEffect",          "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceBlueEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceYellowEffect",        "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceYellowEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceFuchsiaEffect",       "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceFuchsiaEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBrightEffect",        "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceBrightEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceGrayEffect",          "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceGrayEffect},
    
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceInverse",             "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceInverse},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceAlpha",               "(JF)I",                      com_kindred_sdl_SDL_Mir_SurfaceAlpha},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendNormal",         "(JJIIF)I",                   com_kindred_sdl_SDL_Mir_SurfaceBlendNormal},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendNormalTransparent",         "(JJIIFIII)I",     com_kindred_sdl_SDL_Mir_SurfaceBlendNormalTransparent},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendAdd",            "(JJIIF)I",                   com_kindred_sdl_SDL_Mir_SurfaceBlendAdd},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendAddTransparent", "(JJIIFIII)I",                com_kindred_sdl_SDL_Mir_SurfaceBlendAddTransparent},

    //test imgui
    {"com/kindred/mir/engine/MirJNI", "ImGui_SDL2_Init", "(JJ)V",                com_kindred_sdl_SDL_ImGui_SDL2_Init},
    {"com/kindred/mir/engine/MirJNI", "ImGui_SDL2_ProcessEvent", "(J)I",                com_kindred_sdl_SDL_ImGui_SDL2_ProcessEvent},
    {"com/kindred/mir/engine/MirJNI", "ImGui_SDLRenderer2_NewFrame", "()V",                com_kindred_sdl_SDL_ImGui_SDLRenderer2_NewFrame},
    {"com/kindred/mir/engine/MirJNI", "ImGui_SDL2_NewFrame", "()V",                com_kindred_sdl_SDL_ImGui_SDL2_NewFrame},
    {"com/kindred/mir/engine/MirJNI", "ImGui_NewFrame", "()V",                com_kindred_sdl_SDL_ImGui_NewFrame},
    {"com/kindred/mir/engine/MirJNI", "ImGui_Begin", "()Z",                com_kindred_sdl_SDL_ImGui_Begin},
    {"com/kindred/mir/engine/MirJNI", "ImGui_Text", "()V",                com_kindred_sdl_SDL_ImGui_Text},
    {"com/kindred/mir/engine/MirJNI", "ImGui_InputText", "([B[B)Z",                com_kindred_sdl_SDL_ImGui_InputText},
    {"com/kindred/mir/engine/MirJNI", "ImGui_End", "()V",                com_kindred_sdl_SDL_ImGui_End},
    {"com/kindred/mir/engine/MirJNI", "ImGui_Render", "(J)V",                com_kindred_sdl_SDL_ImGui_Render},
    {"com/kindred/mir/engine/MirJNI", "ImGui_Destroy", "()V",                com_kindred_sdl_SDL_ImGui_Destroy},
};

s32 count_MIRFuncTable() {
    return sizeof(method_mir_table) / sizeof(java_native_method);
}

__refer ptr_MIRFuncTable() {
    return &method_mir_table[0];
}