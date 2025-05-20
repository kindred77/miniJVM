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

void surfaceToGray_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            red *= 0.299;
            green *= 0.587;
            blue *= 0.114;

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceToGray_ARGB8888(pixels, surface->format, width, height, pitch);
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
        surfaceToGray_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceBlackEffect_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            int a4=(int)((double)((red+green+blue)/3*0.6)+0.5);
            red=max(a4,1);
            green=red;
            blue=red;

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceBlackEffect_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceWhiteEffect_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            int a4=(int)((double)((red+green+blue)/3*0.6)+0.5);
            red=min(a4,255);
            green=red;
            blue=red;

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceWhiteEffect_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceRedEffect_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=max(a4,0x20);
            green=0;
            blue=0;

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceRedEffect_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceGreenEffect_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=0;
            green=max(a4,0x20);
            blue=0;

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceGreenEffect_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceBlueEffect_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=0;
            green=0;
            blue=max(a4,0x08);

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceBlueEffect_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceYellowEffect_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=a4;
            green=red;
            blue=0;

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceYellowEffect_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceFuchsiaEffect_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            int a4=(int)((double)(red+green+blue)/3 + 0.5);
            red=a4;
            //green=green;
            blue=red;

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceFuchsiaEffect_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceBrightEffect_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            red=(Uint8)min((int)((double)(red*1.3)+0.5),255);
            green=(Uint8)min((int)((double)(green*1.3)+0.5),255);
            blue=(Uint8)min((int)((double)(blue*1.3)+0.5),255);

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceBrightEffect_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceGrayEffect_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            int a4=(int)((double)(red+green+blue)/3+0.5);
            red=(Uint8)a4;
            green=red;
            blue=red;

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceGrayEffect_ARGB8888(pixels, surface->format, width, height, pitch);
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
        surfaceBlackEffect_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceInverse_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            red ^= 0xff;
            green ^= 0xff;
            blue ^= 0xff;

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceInverse_ARGB8888(pixels, surface->format, width, height, pitch);
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
        surfaceInverse_ARGB8888(pixels, surface->format, width, height, pitch);
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

void surfaceAlpha_ARGB8888(Uint32 * pixels, const SDL_PixelFormat* format, float alpha_convert, int width, int height, int pitch) {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值
            // Uint8 alpha = pixels[index] >> 24 & 0xFF;
            // Uint8 red = pixels[index] >> 16 & 0xFF;
            // Uint8 green = pixels[index] >> 8 & 0xFF;
            // Uint8 blue = pixels[index] & 0xFF;

            Uint8 alpha, red, green, blue;
            SDL_GetRGBA(pixels[index], format, &red, &green, &blue, &alpha);

            alpha *= alpha_convert;

            //pixels[index] = (alpha << 24) | (red << 16) | (green << 8) | blue;
            pixels[index] = SDL_MapRGBA(format, red, green, blue, alpha);
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
        surfaceAlpha_ARGB8888(pixels, surface->format, alpha, width, height, pitch);
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
        surfaceAlpha_ARGB8888(pixels, surface->format, alpha, width, height, pitch);
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

//以下应该是一种更标准的像素级处理方式
/*
void customBlit(SDL_Surface* src, SDL_Surface* dst) {
    if (src->format->BitsPerPixel != dst->format->BitsPerPixel) {
        fprintf(stderr, "Source and destination surfaces must have the same pixel format.\n");
        return;
    }

    Uint32 rmask, gmask, bmask, amask;
    SDL_PixelFormatEnumToMasks(src->format->format, &src->format->BitsPerPixel, &rmask, &gmask, &bmask, &amask);

    // 锁定源和目标 Surface
    if (SDL_MUSTLOCK(src)) {
        if (SDL_LockSurface(src) < 0) return;
    }
    if (SDL_MUSTLOCK(dst)) {
        if (SDL_LockSurface(dst) < 0) {
            if (SDL_MUSTLOCK(src)) SDL_UnlockSurface(src);
            return;
        }
    }

    int pitch = src->pitch; // 每行字节数
    int bytes_per_pixel = src->format->BytesPerPixel;

    for (int y = 0; y < src->h; y++) {
        Uint8* src_row = (Uint8*)src->pixels + y * pitch;
        Uint8* dst_row = (Uint8*)dst->pixels + y * pitch;

        for (int x = 0; x < src->w; x++) {
            Uint32 pixel = *((Uint32*)src_row);
            Uint8 r, g, b, a;
            SDL_GetRGBA(pixel, src->format, &r, &g, &b, &a);

            // 自定义逻辑：将红色通道值加倍
            r = SDL_min(r * 2, 255);

            Uint32 new_pixel = SDL_MapRGBA(dst->format, r, g, b, a);
            *((Uint32*)dst_row) = new_pixel;

            src_row += bytes_per_pixel;
            dst_row += bytes_per_pixel;
        }
    }

    // 解锁 Surface
    if (SDL_MUSTLOCK(src)) SDL_UnlockSurface(src);
    if (SDL_MUSTLOCK(dst)) SDL_UnlockSurface(dst);
}
*/

int com_kindred_sdl_SDL_Mir_SurfaceBlendNormal(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *dst_surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    SDL_Surface *src_surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    s32 x = env->localvar_getInt(runtime->localvar, pos++);
    s32 y = env->localvar_getInt(runtime->localvar, pos++);

    Instance *rect_ref = env->localvar_getRefer(runtime->localvar, pos++);
    __refer ptr_rect = NULL;
    if (rect_ref) {
        ptr_rect = rect_ref->arr_body;
    }

    int srcLeft=0;
    int srcTop=0;
    int srcRight=src_surface->w;
    int srcBottom=src_surface->h;
    if (ptr_rect) {
        srcLeft = ((int*)ptr_rect)[0];
        srcLeft = srcLeft <= 0 ? 0 : srcLeft;

        srcTop = ((int*)ptr_rect)[1];
        srcTop = srcTop <= 0 ? 0 : srcTop;

        srcRight = ((int*)ptr_rect)[2];
        srcRight = srcRight >= src_surface->w ? src_surface->w : srcRight;

        srcBottom = ((int*)ptr_rect)[3];
        srcBottom = srcBottom >= src_surface->h ? src_surface->h : srcBottom;

        if (srcLeft >= src_surface->w ||
            srcTop >= src_surface->h ||
            srcRight <= 0 ||
            srcBottom <= 0 ||
            srcLeft >= srcRight ||
            srcTop >= srcBottom) {
            env->push_int(runtime->stack, 0);
            fprintf(stdout,
                "Do noting in com_kindred_sdl_SDL_Mir_SurfaceBlendNormal, out of bounds, srcLeft: %d, srcTop: %d, srcRight: %d, srcBottom: %d \n",
                srcLeft,srcTop,srcRight,srcBottom);
            return 0;
        }
    }
    Int2Float psize;
    psize.i = env->localvar_getInt(runtime->localvar, pos++);
    float alpha = (float)psize.f;

    Uint32 * dst_pixels = ((Uint32*)dst_surface->pixels);
    int dst_width = dst_surface->w;
    int dst_height = dst_surface->h;
    int dst_pitch = dst_surface->pitch;
    Uint32 * src_pixels = ((Uint32*)src_surface->pixels);
    int src_width = srcRight - srcLeft;
    int src_origin_width = src_surface->w;
    int src_height = srcBottom - srcTop;
    int src_pitch = src_surface->pitch;
    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout,
            "Do noting in com_kindred_sdl_SDL_Mir_SurfaceBlendNormal, out of bounds,x: %d, y: %d, src_width: %d, src_height: %d, dst_width: %d, dst_height: %d\n",
            x,y,src_width,src_height,dst_width,dst_height);
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
        //left, top, rx, by其实就是dest rect
        int left = x < 0 ? 0 : x;
        int top = y < 0 ? 0 : y;
        //tarleft和tartop是辅助变量
        int tarleft = x < 0 ? -x : 0;
        int tartop = y < 0 ? -y : 0;
        int rx = left + src_width - tarleft;
        //超过了dest width
        if (rx >= dst_width)
            rx = dst_width - 1;
        int by = top + src_height - tartop;
        //超过了dest height
        if (by >= dst_height)
            by = dst_height - 1;

        for (int i = top; i < by; ++i) {
            for (int j = left; j < rx; ++j) {
                //j和i是dest中的坐标
                int _idx_this = (j + i * dst_width);
                //((j - left + tarleft) + srcLeft)和( (i - top + tartop) + srcTop)是相对source中的坐标
                int _idx_that = ((j - left + tarleft) + srcLeft) + ( (i - top + tartop) + srcTop) * src_origin_width;

                Uint8 da, dr, dg, db;
                SDL_GetRGBA(dst_pixels[_idx_this], dst_surface->format, &dr, &dg, &db, &da);

                Uint8 sa, sr, sg, sb;
                SDL_GetRGBA(src_pixels[_idx_that], src_surface->format, &sr, &sg, &sb, &sa);

                Uint8 a = da;
                Uint8 r = sr * alpha;
                Uint8 g = sg * alpha;
                Uint8 b = sb * alpha;

                dst_pixels[_idx_this] = SDL_MapRGBA(dst_surface->format, r, g, b, a);
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

    Instance *rect_ref = env->localvar_getRefer(runtime->localvar, pos++);
    __refer ptr_rect = NULL;
    if (rect_ref) {
        ptr_rect = rect_ref->arr_body;
    }

    int srcLeft=0;
    int srcTop=0;
    int srcRight=src_surface->w;
    int srcBottom=src_surface->h;
    if (ptr_rect) {
        srcLeft = ((int*)ptr_rect)[0];
        srcLeft = srcLeft <= 0 ? 0 : srcLeft;

        srcTop = ((int*)ptr_rect)[1];
        srcTop = srcTop <= 0 ? 0 : srcTop;

        srcRight = ((int*)ptr_rect)[2];
        srcRight = srcRight >= src_surface->w ? src_surface->w : srcRight;

        srcBottom = ((int*)ptr_rect)[3];
        srcBottom = srcBottom >= src_surface->h ? src_surface->h : srcBottom;

        if (srcLeft >= src_surface->w ||
            srcTop >= src_surface->h ||
            srcRight <= 0 ||
            srcBottom <= 0 ||
            srcLeft >= srcRight ||
            srcTop >= srcBottom) {
            env->push_int(runtime->stack, 0);
            fprintf(stdout,
                "Do noting in com_kindred_sdl_SDL_Mir_SurfaceBlendNormalTransparent, out of bounds, srcLeft: %d, srcTop: %d, srcRight: %d, srcBottom: %d \n",
                srcLeft,srcTop,srcRight,srcBottom);
            return 0;
        }
    }

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
    int src_width = srcRight - srcLeft;
    int src_origin_width = src_surface->w;
    int src_height = srcBottom - srcTop;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout,
            "Do noting in com_kindred_sdl_SDL_Mir_SurfaceBlendNormalTransparent, out of bounds,x: %d, y: %d, src_width: %d, src_height: %d, dst_width: %d, dst_height: %d\n",
            x,y,src_width,src_height,dst_width,dst_height);
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
                int _idx_that = ((j - left + tarleft) + srcLeft) + ( (i - top + tartop) + srcTop) * src_origin_width;

                Uint8 da, dr, dg, db;
                SDL_GetRGBA(dst_pixels[_idx_this], dst_surface->format, &dr, &dg, &db, &da);

                Uint8 sa, sr, sg, sb;
                SDL_GetRGBA(src_pixels[_idx_that], src_surface->format, &sr, &sg, &sb, &sa);

                if (r == sr && g == sg && b == sb || !sa) continue;

                sr *= alpha;
                sg *= alpha;
                sb *= alpha;
                dst_pixels[_idx_this] = SDL_MapRGBA(dst_surface->format, sr, sg, sb, da);
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

    Instance *rect_ref = env->localvar_getRefer(runtime->localvar, pos++);
    __refer ptr_rect = NULL;
    if (rect_ref) {
        ptr_rect = rect_ref->arr_body;
    }

    int srcLeft=0;
    int srcTop=0;
    int srcRight=src_surface->w;
    int srcBottom=src_surface->h;
    if (ptr_rect) {
        srcLeft = ((int*)ptr_rect)[0];
        srcLeft = srcLeft <= 0 ? 0 : srcLeft;

        srcTop = ((int*)ptr_rect)[1];
        srcTop = srcTop <= 0 ? 0 : srcTop;

        srcRight = ((int*)ptr_rect)[2];
        srcRight = srcRight >= src_surface->w ? src_surface->w : srcRight;

        srcBottom = ((int*)ptr_rect)[3];
        srcBottom = srcBottom >= src_surface->h ? src_surface->h : srcBottom;

        if (srcLeft >= src_surface->w ||
            srcTop >= src_surface->h ||
            srcRight <= 0 ||
            srcBottom <= 0 ||
            srcLeft >= srcRight ||
            srcTop >= srcBottom) {
            env->push_int(runtime->stack, 0);
            fprintf(stdout,
                "Do noting in com_kindred_sdl_SDL_Mir_SurfaceBlendAdd, out of bounds, srcLeft: %d, srcTop: %d, srcRight: %d, srcBottom: %d \n",
                srcLeft,srcTop,srcRight,srcBottom);
            return 0;
        }
    }

    Int2Float psize;
    psize.i = env->localvar_getInt(runtime->localvar, pos++);
    float alpha = (float)psize.f;

    Uint32 * dst_pixels = ((Uint32*)dst_surface->pixels);
    int dst_width = dst_surface->w;
    int dst_height = dst_surface->h;
    int dst_pitch = dst_surface->pitch;

    Uint32 * src_pixels = ((Uint32*)src_surface->pixels);
    int src_width = srcRight - srcLeft;
    int src_origin_width = src_surface->w;
    int src_height = srcBottom - srcTop;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout,
            "Do noting in com_kindred_sdl_SDL_Mir_SurfaceBlendAdd, out of bounds,x: %d, y: %d, src_width: %d, src_height: %d, dst_width: %d, dst_height: %d\n",
            x,y,src_width,src_height,dst_width,dst_height);
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
                int _idx_that = ((j - left + tarleft) + srcLeft) + ( (i - top + tartop) + srcTop) * src_origin_width;

                Uint8 da, dr, dg, db;
                SDL_GetRGBA(dst_pixels[_idx_this], dst_surface->format, &dr, &dg, &db, &da);

                Uint8 sa, sr, sg, sb;
                SDL_GetRGBA(src_pixels[_idx_that], src_surface->format, &sr, &sg, &sb, &sa);

                float fr = sr * alpha;
                float fg = sg * alpha;
                float fb = sb * alpha;

                dst_pixels[_idx_this] = SDL_MapRGBA(dst_surface->format, 
                    (Uint8) min(255, fr*fr/255+dr), 
                    (Uint8) min(255, fg*fg/255+dg), 
                    (Uint8) min(255, fb*fb/255+db), 
                    da);
                // dst_pixels[_idx_this] = (da << 24) | ((Uint8) min(255, fr*fr/255+dr) << 16) 
                //     | ((Uint8) min(255, fg*fg/255+dg) << 8) 
                //     | (Uint8) min(255, fb*fb/255+db);

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

    Instance *rect_ref = env->localvar_getRefer(runtime->localvar, pos++);
    __refer ptr_rect = NULL;
    if (rect_ref) {
        ptr_rect = rect_ref->arr_body;
    }

    int srcLeft=0;
    int srcTop=0;
    int srcRight=src_surface->w;
    int srcBottom=src_surface->h;
    if (ptr_rect) {
        srcLeft = ((int*)ptr_rect)[0];
        srcLeft = srcLeft <= 0 ? 0 : srcLeft;

        srcTop = ((int*)ptr_rect)[1];
        srcTop = srcTop <= 0 ? 0 : srcTop;

        srcRight = ((int*)ptr_rect)[2];
        srcRight = srcRight >= src_surface->w ? src_surface->w : srcRight;

        srcBottom = ((int*)ptr_rect)[3];
        srcBottom = srcBottom >= src_surface->h ? src_surface->h : srcBottom;

        if (srcLeft >= src_surface->w ||
            srcTop >= src_surface->h ||
            srcRight <= 0 ||
            srcBottom <= 0 ||
            srcLeft >= srcRight ||
            srcTop >= srcBottom) {
            env->push_int(runtime->stack, 0);
            fprintf(stdout,
                "Do noting in com_kindred_sdl_SDL_Mir_SurfaceBlendAddTransparent, out of bounds, srcLeft: %d, srcTop: %d, srcRight: %d, srcBottom: %d \n",
                srcLeft,srcTop,srcRight,srcBottom);
            return 0;
        }
    }

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
    int src_width = srcRight - srcLeft;
    int src_origin_width = src_surface->w;
    int src_height = srcBottom - srcTop;
    int src_pitch = src_surface->pitch;

    if (x > dst_width || y > dst_height || (x < 0 && -x >= src_width) || (y < 0 && -y >= src_height)) {
        fprintf(stdout,
            "Do noting in com_kindred_sdl_SDL_Mir_SurfaceBlendAddTransparent, out of bounds,x: %d, y: %d, src_width: %d, src_height: %d, dst_width: %d, dst_height: %d\n",
            x,y,src_width,src_height,dst_width,dst_height);
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
                int _idx_that = ((j - left + tarleft) + srcLeft) + ( (i - top + tartop) + srcTop) * src_origin_width;

                Uint8 da, dr, dg, db;
                SDL_GetRGBA(dst_pixels[_idx_this], dst_surface->format, &dr, &dg, &db, &da);

                Uint8 sa, sr, sg, sb;
                SDL_GetRGBA(src_pixels[_idx_that], src_surface->format, &sr, &sg, &sb, &sa);

                float fr = sr * alpha;
                float fg = sg * alpha;
                float fb = sb * alpha;

                if (r == sr && g == sg && b == sb || !sa) continue;

                dst_pixels[_idx_this] = SDL_MapRGBA(dst_surface->format, 
                    (Uint8) min(255, fr*fr/255+dr), 
                    (Uint8) min(255, fg*fg/255+dg), 
                    (Uint8) min(255, fb*fb/255+db), 
                    da);

                // dst_pixels[_idx_this] = (da << 24) | ((Uint8) min(255, fr*fr/255+dr) << 16) 
                //     | ((Uint8) min(255, fg*fg/255+dg) << 8) 
                //     | (Uint8) min(255, fb*fb/255+db);
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

int com_kindred_sdl_SDL_Mir_FillRect(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    
    s32 width = env->localvar_getInt(runtime->localvar, pos++);
    s32 height = env->localvar_getInt(runtime->localvar, pos++);
    
    Instance *color_ref = env->localvar_getRefer(runtime->localvar, pos++);
    __refer ptr_color = NULL;
    if (color_ref) {
        ptr_color = color_ref->arr_body;
    }
    
    SDL_Color color;
    if (ptr_color) {
        color.r = ((int*)ptr_color)[0];
        color.g = ((int*)ptr_color)[1];
        color.b = ((int*)ptr_color)[2];
        color.a = ((int*)ptr_color)[3];
    }
    
    SDL_Surface * surface = SDL_CreateRGBSurfaceWithFormat(0, width, height, 32, SDL_PIXELFORMAT_ARGB8888);
    if (!surface) {
        fprintf(stderr, "Unable to create surface! SDL Error: %s\n", SDL_GetError() );
        env->push_long(runtime->stack, 0L);
        return 0;
    }
    
    int ret = SDL_LockSurface(surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError());
        env->push_int(runtime->stack, 0);
        return 0;
    }
    
    int pitch = surface->pitch;
    Uint32 * pixels = ((Uint32*)surface->pixels);

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            // 计算当前像素在像素数据中的索引
            int index = y * width + x;
            // 获取当前像素的红、绿、蓝颜色通道值

            pixels[index] = SDL_MapRGBA(surface->format, color.r, color.g, color.b, color.a);
            //pixels[index] = (color.a << 24) | (color.r << 16) | (color.g << 8) | color.b;
        }
    }
    
    SDL_UnlockSurface(surface);
    env->push_long(runtime->stack, (intptr_t) surface);
    return 0;
}

int com_kindred_sdl_SDL_Mir_IsVisiblePixelInSurface(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Surface *surface = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    s32 x = env->localvar_getInt(runtime->localvar, pos++);
    s32 y = env->localvar_getInt(runtime->localvar, pos++);

    if (x < 0 || y < 0 || x >= surface->w || y >= surface->h) {
        env->push_int(runtime->stack, 0);
        return 0;
    }

    int ret = SDL_LockSurface(surface);
    if (ret) {
        fprintf(stderr, "Unable to lock surface! SDL Error: %s\n", SDL_GetError());
        env->push_int(runtime->stack, 0);
        return 0;
    }
    Uint32 * pixels = ((Uint32*)surface->pixels);
    int index = y * surface->w + x;
    //完全为0,或者alpha为0
    if (pixels[index] == 0 || ((pixels[index] >> 24) & 0xFF) == 0) {
        ret=0;
    } else {
        ret=1;
    }
    SDL_UnlockSurface(surface);
    env->push_int(runtime->stack, ret);
    return 0;
}


intptr_t Mir_ImGui_SDL2_InitImGuiContext();
int Mir_ImGui_ImplSDL2_InitForSDLRenderer(SDL_Window * window, SDL_Renderer *renderer);
int Mir_ImGui_ImplSDLRenderer2_Init(SDL_Renderer *renderer);
void Mir_ImGui_SetCurrentContext(intptr_t context_ptr);
void Mir_ImGui_InitBackColor(float r, float g, float b, float alpha);
void Mir_ImGui_InitForeColor(float r, float g, float b, float alpha);
intptr_t Mir_ImGui_InitFont(const char * font_name, float size);
void Mir_ImGui_PushFont(intptr_t font_ptr);
void Mir_ImGui_PopFont();
int Mir_ImGui_SDL2_ProcessEvent(SDL_Event * event);
void Mir_ImGui_SDLRenderer2_NewFrame();
void Mir_ImGui_SDL2_NewFrame();
void Mir_ImGui_NewFrame();
void Mir_ImGui_EndFrame();
int Mir_ImGui_Begin(const char * label, float x, float y, float width, float height,
    int no_titlebar_arg, int no_scrollbar_arg, int no_menu_arg, int no_move_arg, 
    int no_resize_arg, int no_collapse_arg, int no_close_arg, int no_nav_arg,
    int no_background_arg, int no_bring_to_front_arg, int unsaved_document_arg,
    int no_saved_settings_arg);
void Mir_ImGui_Text(const char * text);
int Mir_ImGui_InputText(float x, float y, float width, const char * label, const char * hint, char * buf, int length, int isPassword);
int Mir_ImGui_InputTextMultiline(float x, float y, const char* label, char * buf, int buf_length, float width, int line_height_cnt);
int Mir_SetWindowFontScale(float scale);
void Mir_ImGui_End();
void Mir_ImGui_Render(SDL_Renderer * renderer, intptr_t drawData_ptr);
intptr_t Mir_ImGui_RenderAndGetDrawData();
void Mir_ImGui_Destroy();

int com_kindred_sdl_SDL_ImGui_SDL2_InitImGuiContext(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    // SDL_Window *window = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    // pos += 2;
    // SDL_Renderer *renderer = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    // pos += 2;
    intptr_t res = Mir_ImGui_SDL2_InitImGuiContext();
    env->push_long(runtime->stack, res);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_ImplSDL2_InitForSDLRenderer(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    SDL_Window *window = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    SDL_Renderer *renderer = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;
    int ret = Mir_ImGui_ImplSDL2_InitForSDLRenderer(window, renderer);
    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_ImplSDLRenderer2_Init(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *renderer = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    int ret = Mir_ImGui_ImplSDLRenderer2_Init(renderer);
    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_SetCurrentContext(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    intptr_t context_ptr = (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);

    Mir_ImGui_SetCurrentContext(context_ptr);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_InitBackColor(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    Int2Float pred;
    pred.i = env->localvar_getInt(runtime->localvar, pos++);
    float red = (float)pred.f;

    Int2Float pgreen;
    pgreen.i = env->localvar_getInt(runtime->localvar, pos++);
    float green = (float)pgreen.f;

    Int2Float pblue;
    pblue.i = env->localvar_getInt(runtime->localvar, pos++);
    float blue = (float)pblue.f;

    Int2Float palpha;
    palpha.i = env->localvar_getInt(runtime->localvar, pos++);
    float alpha = (float)palpha.f;

    Mir_ImGui_InitBackColor(red, green, blue, alpha);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_InitForeColor(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    Int2Float pred;
    pred.i = env->localvar_getInt(runtime->localvar, pos++);
    float red = (float)pred.f;

    Int2Float pgreen;
    pgreen.i = env->localvar_getInt(runtime->localvar, pos++);
    float green = (float)pgreen.f;

    Int2Float pblue;
    pblue.i = env->localvar_getInt(runtime->localvar, pos++);
    float blue = (float)pblue.f;

    Int2Float palpha;
    palpha.i = env->localvar_getInt(runtime->localvar, pos++);
    float alpha = (float)palpha.f;

    Mir_ImGui_InitForeColor(red, green, blue, alpha);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_InitFont(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    Instance *font_name_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *font_name = NULL;
    if (font_name_arr) {
        font_name = font_name_arr->arr_body;
    }

    Int2Float psize;
    psize.i = env->localvar_getInt(runtime->localvar, pos++);
    float size = (float)psize.f;
    intptr_t font_ptr = Mir_ImGui_InitFont(font_name, size);

    env->push_long(runtime->stack, font_ptr);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_PushFont(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    intptr_t font_ptr = (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);

    Mir_ImGui_PushFont(font_ptr);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_PopFont(Runtime *runtime, JClass *clazz) {
    Mir_ImGui_PopFont();
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

int com_kindred_sdl_SDL_ImGui_EndFrame(Runtime *runtime, JClass *clazz) {
    Mir_ImGui_EndFrame();
    return 0;
}

int com_kindred_sdl_SDL_ImGui_Begin(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    Instance *label_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *label = NULL;
    if (label_arr) {
        label = label_arr->arr_body;
    }

    Int2Float px;
    px.i = env->localvar_getInt(runtime->localvar, pos++);
    float x = (float)px.f;

    Int2Float py;
    py.i = env->localvar_getInt(runtime->localvar, pos++);
    float y = (float)py.f;

    Int2Float pwidth;
    pwidth.i = env->localvar_getInt(runtime->localvar, pos++);
    float width = (float)pwidth.f;

    Int2Float pheight;
    pheight.i = env->localvar_getInt(runtime->localvar, pos++);
    float height = (float)pheight.f;

    s32 no_titlebar_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 no_scrollbar_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 no_menu_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 no_move_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 no_resize_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 no_collapse_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 no_close_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 no_nav_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 no_background_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 no_bring_to_front_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 unsaved_document_arg = env->localvar_getInt(runtime->localvar, pos++);
    s32 no_saved_settings_arg = env->localvar_getInt(runtime->localvar, pos++);

    int ret = Mir_ImGui_Begin(label, x, y, width, height, no_titlebar_arg, no_scrollbar_arg,
        no_menu_arg, no_move_arg, no_resize_arg, no_collapse_arg, no_close_arg, no_nav_arg,
        no_background_arg, no_bring_to_front_arg, unsaved_document_arg, no_saved_settings_arg);

    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_Text(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    Instance *title_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *title = NULL;
    if (title_arr) {
        title = title_arr->arr_body;
    }

    Mir_ImGui_Text(title);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_InputText(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;
    
    Int2Float px;
    px.i = env->localvar_getInt(runtime->localvar, pos++);
    float x = (float)px.f;

    Int2Float py;
    py.i = env->localvar_getInt(runtime->localvar, pos++);
    float y = (float)py.f;

    Int2Float pwidth;
    pwidth.i = env->localvar_getInt(runtime->localvar, pos++);
    float width = (float)pwidth.f;

    Instance *label_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *label = NULL;
    if (label_arr) {
        label = label_arr->arr_body;
    }

    Instance *hint_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *hint = NULL;
    if (hint_arr) {
        hint = hint_arr->arr_body;
    }

    Instance *buf_arr = env->localvar_getRefer(runtime->localvar, pos++);
    s32 isPassword = env->localvar_getInt(runtime->localvar, pos++);
    int ret = 0;
    if (!buf_arr) {
        fprintf(stderr, "Failed to create inputtext, buffer is null. \n");
    }
    else {
        c8 *buf = buf_arr->arr_body;
        ret = Mir_ImGui_InputText(x, y, width, label, hint, buf, buf_arr->arr_length, isPassword);
    }
    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_InputTextMultiline(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    Int2Float px;
    px.i = env->localvar_getInt(runtime->localvar, pos++);
    float x = (float)px.f;

    Int2Float py;
    py.i = env->localvar_getInt(runtime->localvar, pos++);
    float y = (float)py.f;

    Instance *label_arr = env->localvar_getRefer(runtime->localvar, pos++);
    c8 *label = NULL;
    if (label_arr) {
        label = label_arr->arr_body;
    }

    Instance *buf_arr = env->localvar_getRefer(runtime->localvar, pos++);
    
    Int2Float pwidth;
    pwidth.i = env->localvar_getInt(runtime->localvar, pos++);
    float width = (float)pwidth.f;

    s32 line_height_cnt = env->localvar_getInt(runtime->localvar, pos++);

    int ret = 0;
    if (!buf_arr) {
        fprintf(stderr, "Failed to create multiline inputtext, buffer is null. \n");
    }
    else {
        c8 *buf = buf_arr->arr_body;
        ret = Mir_ImGui_InputTextMultiline(x, y, label, buf, buf_arr->arr_length, width, line_height_cnt);
    }
    env->push_int(runtime->stack, ret);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_SetWindowFontScale(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    Int2Float pscale;
    pscale.i = env->localvar_getInt(runtime->localvar, pos++);
    float scale = (float)pscale.f;

    Mir_SetWindowFontScale(scale);

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

    intptr_t drawData_ptr = (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    Mir_ImGui_Render(renderer, drawData_ptr);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_RenderAndGetDrawData(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    intptr_t drawData_ptr = Mir_ImGui_RenderAndGetDrawData();
    env->push_long(runtime->stack, drawData_ptr);
    return 0;
}

int com_kindred_sdl_SDL_ImGui_Destroy(Runtime *runtime, JClass *clazz) {
    Mir_ImGui_Destroy();
    return 0;
}

//for test
SDL_Texture* convertDrawDataToTexture(intptr_t draw_data_ptr, SDL_Renderer* renderer);
int com_kindred_sdl_SDL_ImGui_ConvertDrawDataToTexture(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    intptr_t drawData_ptr = (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Renderer *renderer = (__refer) (intptr_t) env->localvar_getLong_2slot(runtime->localvar, pos);
    pos += 2;

    SDL_Texture* texture = convertDrawDataToTexture(drawData_ptr, renderer);

    env->push_long(runtime->stack, (intptr_t) texture);
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
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendNormal",         "(JJII[IF)I",                 com_kindred_sdl_SDL_Mir_SurfaceBlendNormal},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendNormalTransparent",         "(JJII[IFIII)I",   com_kindred_sdl_SDL_Mir_SurfaceBlendNormalTransparent},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendAdd",            "(JJII[IF)I",                 com_kindred_sdl_SDL_Mir_SurfaceBlendAdd},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendAddTransparent", "(JJII[IFIII)I",              com_kindred_sdl_SDL_Mir_SurfaceBlendAddTransparent},
    {"com/kindred/mir/engine/MirJNI", "Mir_FillRect",                   "(II[I)J",                    com_kindred_sdl_SDL_Mir_FillRect},
    {"com/kindred/mir/engine/MirJNI", "Mir_IsVisiblePixelInSurface",    "(JII)Z",                     com_kindred_sdl_SDL_Mir_IsVisiblePixelInSurface},

    //test imgui
    {"com/kindred/mir/engine/MirJNI", "ImGui_SDL2_InitImGuiContext", "()J",                com_kindred_sdl_SDL_ImGui_SDL2_InitImGuiContext},
    {"com/kindred/mir/engine/MirJNI", "ImGui_SetCurrentContext", "(J)V",                com_kindred_sdl_SDL_ImGui_SetCurrentContext},
    {"com/kindred/mir/engine/MirJNI", "ImGui_ImplSDL2_InitForSDLRenderer", "(JJ)Z",                com_kindred_sdl_SDL_ImGui_ImplSDL2_InitForSDLRenderer},
    {"com/kindred/mir/engine/MirJNI", "ImGui_ImplSDLRenderer2_Init", "(J)Z",                com_kindred_sdl_SDL_ImGui_ImplSDLRenderer2_Init},
    {"com/kindred/mir/engine/MirJNI", "ImGui_InitBackColor", "(FFFF)V",                com_kindred_sdl_SDL_ImGui_InitBackColor},
    {"com/kindred/mir/engine/MirJNI", "ImGui_InitForeColor", "(FFFF)V",                com_kindred_sdl_SDL_ImGui_InitForeColor},
    {"com/kindred/mir/engine/MirJNI", "ImGui_InitFont", "([BF)J",                com_kindred_sdl_SDL_ImGui_InitFont},
    {"com/kindred/mir/engine/MirJNI", "ImGui_PushFont", "(J)V",                com_kindred_sdl_SDL_ImGui_PushFont},
    {"com/kindred/mir/engine/MirJNI", "ImGui_PopFont", "()V",                com_kindred_sdl_SDL_ImGui_PopFont},
    {"com/kindred/mir/engine/MirJNI", "ImGui_SDL2_ProcessEvent", "(J)I",                com_kindred_sdl_SDL_ImGui_SDL2_ProcessEvent},
    {"com/kindred/mir/engine/MirJNI", "ImGui_SDLRenderer2_NewFrame", "()V",                com_kindred_sdl_SDL_ImGui_SDLRenderer2_NewFrame},
    {"com/kindred/mir/engine/MirJNI", "ImGui_SDL2_NewFrame", "()V",                com_kindred_sdl_SDL_ImGui_SDL2_NewFrame},
    {"com/kindred/mir/engine/MirJNI", "ImGui_NewFrame", "()V",                com_kindred_sdl_SDL_ImGui_NewFrame},
    {"com/kindred/mir/engine/MirJNI", "ImGui_EndFrame", "()V",                com_kindred_sdl_SDL_ImGui_EndFrame},
    {"com/kindred/mir/engine/MirJNI", "ImGui_Begin", "([BFFFFZZZZZZZZZZZZ)Z",                com_kindred_sdl_SDL_ImGui_Begin},
    {"com/kindred/mir/engine/MirJNI", "ImGui_Text", "([B)V",                com_kindred_sdl_SDL_ImGui_Text},
    {"com/kindred/mir/engine/MirJNI", "ImGui_InputText", "(FFF[B[B[BZ)Z",                com_kindred_sdl_SDL_ImGui_InputText},
    {"com/kindred/mir/engine/MirJNI", "ImGui_InputTextMultiline", "(FF[B[BFI)Z",                com_kindred_sdl_SDL_ImGui_InputTextMultiline},
    {"com/kindred/mir/engine/MirJNI", "ImGui_SetWindowFontScale", "(F)V",                com_kindred_sdl_SDL_ImGui_SetWindowFontScale},
    {"com/kindred/mir/engine/MirJNI", "ImGui_End", "()V",                com_kindred_sdl_SDL_ImGui_End},
    {"com/kindred/mir/engine/MirJNI", "ImGui_Render", "(JJ)V",                com_kindred_sdl_SDL_ImGui_Render},
    {"com/kindred/mir/engine/MirJNI", "ImGui_RenderAndGetDrawData", "()J",                com_kindred_sdl_SDL_ImGui_RenderAndGetDrawData},
    {"com/kindred/mir/engine/MirJNI", "ImGui_Destroy", "()V",                com_kindred_sdl_SDL_ImGui_Destroy},

    //for test(不可用)
    {"com/kindred/mir/engine/MirJNI", "ImGui_ConvertDrawDataToTexture", "(JJ)J",                com_kindred_sdl_SDL_ImGui_ConvertDrawDataToTexture},
};

s32 count_MIRFuncTable() {
    return sizeof(method_mir_table) / sizeof(java_native_method);
}

__refer ptr_MIRFuncTable() {
    return &method_mir_table[0];
}