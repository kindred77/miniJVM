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

static java_native_method method_mir_table[] = {
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceToGray",              "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceToGray},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlackEffect",         "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceBlackEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceInverse",             "(J)I",                       com_kindred_sdl_SDL_Mir_SurfaceInverse},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceAlpha",               "(JF)I",                      com_kindred_sdl_SDL_Mir_SurfaceAlpha},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendNormal",         "(JJIIF)I",                   com_kindred_sdl_SDL_Mir_SurfaceBlendNormal},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendNormalTransparent",         "(JJIIFIII)I",     com_kindred_sdl_SDL_Mir_SurfaceBlendNormalTransparent},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendAdd",            "(JJIIF)I",                   com_kindred_sdl_SDL_Mir_SurfaceBlendAdd},
    {"com/kindred/mir/engine/MirJNI", "Mir_SurfaceBlendAddTransparent", "(JJIIFIII)I",                com_kindred_sdl_SDL_Mir_SurfaceBlendAddTransparent},

    {"com/kindred/mir/engine/MirJNI", "SDL_UpdateTextureWithSurface",   "(JJ[I)I",                    com_kindred_sdl_SDL_SDL_UpdateTextureWithSurface},

    {"com/kindred/mir/engine/MirJNI", "Mir_TextureToGray",              "(J)I",                       com_kindred_sdl_SDL_Mir_TextureToGray},
    {"com/kindred/mir/engine/MirJNI", "Mir_TextureBlackEffect",         "(J)I",                       com_kindred_sdl_SDL_Mir_TextureBlackEffect},
    {"com/kindred/mir/engine/MirJNI", "Mir_TextureInverse",             "(J)I",                       com_kindred_sdl_SDL_Mir_TextureInverse},
    {"com/kindred/mir/engine/MirJNI", "Mir_TextureAlpha",               "(JF)I",                      com_kindred_sdl_SDL_Mir_TextureAlpha},

};

s32 count_MIRFuncTable() {
    return sizeof(method_mir_table) / sizeof(java_native_method);
}

__refer ptr_MIRFuncTable() {
    return &method_mir_table[0];
}