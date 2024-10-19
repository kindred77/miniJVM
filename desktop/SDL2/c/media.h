//
// Created by gust on 2018/1/17.
//

#ifndef JNI_GUI_JNI_SDL2_H
#define JNI_GUI_JNI_SDL2_H

//tag dont delete this line, builder will auto insert here



#define NUTIL_API extern

typedef struct _GlobeRefer GlobeRefer;
extern GlobeRefer refers;


__refer ptr_SDLFuncTable();

s32 count_SDL2FuncTable();

//
Runtime *getRuntimeCurThread(JniEnv *env);

static void init(){}

struct _GlobeRefer {
    MiniJVM *jvm;
    JniEnv *env;
    Runtime *runtime;
    Instance *glfw_callback;
    MethodInfo *_callback_error;
    MethodInfo *_callback_key;
    MethodInfo *_callback_character;
    MethodInfo *_callback_drop;
    MethodInfo *_button_callback_mouse;
    MethodInfo *_scroll_callback;
    MethodInfo *_callback_cursor_pos;
    MethodInfo *_callback_cursor_enter;
    MethodInfo *_callback_window_close;
    MethodInfo *_callback_window_size;
    MethodInfo *_callback_window_pos;
    MethodInfo *_callback_window_focus;
    MethodInfo *_callback_window_iconify;
    MethodInfo *_callback_window_refresh;
    MethodInfo *_callback_framebuffer_size;

    //
    MethodInfo *_callback_minial_on_send_frames;
    MethodInfo *_callback_minial_on_recv_frames;
    MethodInfo *_callback_minial_on_stop;

    //
    Pairlist *runtime_list;
};


// dont delete the comment .for generate jni
/*

 */
//implementation



#endif //JNI_GUI_JNI_SDL2_H
