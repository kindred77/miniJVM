#include <stdio.h>
#include <string.h>
#include "SDL2/SDL.h"
#include "SDL2/SDL_rect.h"
#include "SDL2/SDL_image.h"

#include "kiss_sdl.h"

#include "jvm.h"
#include "media.h"

void button_event(kiss_button *button, SDL_Event *e, int *draw,
    int *quit)
 {
    if (kiss_button_event(button, e, draw)) *quit = 1;
 }

 static void textbox1_event(kiss_textbox *textbox, SDL_Event *e, int *draw)
{
	int index;

	if (kiss_textbox_event(textbox, e, draw)) {
		index = textbox->firstline + textbox->selectedline;
		*draw = 1;
	}
}

int com_kindred_mir_MirJNI_Kiss_Init(Runtime *runtime, JClass *clazz) {
    JniEnv *env = runtime->jnienv;
    s32 pos = 0;

    SDL_Renderer *renderer;
    SDL_Event e;
    kiss_array objects;
    kiss_window window;
    kiss_label label = {0};
    kiss_button button = {0};
    kiss_textbox textbox1 = {0};
    int textbox_width, textbox_height;
    char message[KISS_MAX_LENGTH];
    int draw, quit;
    quit = 0;
    draw = 1;
    textbox_width = 250;
	textbox_height = 50;
    kiss_array_new(&objects);
    renderer = kiss_init("Hello kiss_sdl", &objects, 600, 400);
    if (!renderer) return 1;
    kiss_window_new(&window, NULL, 0, 0, 0, kiss_screen_width,
    kiss_screen_height);
    strcpy(message, "Hello World!");
    kiss_label_new(&label, &window, message,
        window.rect.w / 2 - strlen(message) *
        kiss_textfont.advance / 2,
        window.rect.h / 2 - (kiss_textfont.fontheight +
        2 * kiss_normal.h) / 2);
    label.textcolor.r = 255;
    kiss_button_new(&button, &window, "OK",
        window.rect.w / 2 - kiss_normal.w / 2, label.rect.y +
        kiss_textfont.fontheight + kiss_normal.h);
    kiss_textbox_new(&textbox1, &window, 0, &objects, kiss_screen_width / 2 -
		(2 * textbox_width + 2 * kiss_up.w - kiss_edge) / 2,
		3 * kiss_normal.h, textbox_width, textbox_height);
    textbox1.focus = 1;
    textbox1.maxlines = 1;
    window.visible = 1;
    while (!quit) {
        SDL_Delay(10);
        while (SDL_PollEvent(&e)) {
            if (e.type == SDL_QUIT) quit = 1;
            kiss_window_event(&window, &e, &draw);
            button_event(&button, &e, &draw, &quit);
            textbox1_event(&textbox1, &e, &draw);
        }
        if (!draw) continue;
        SDL_RenderClear(renderer);
        kiss_window_draw(&window, renderer);
        kiss_label_draw(&label, renderer);
        kiss_button_draw(&button, renderer);
        kiss_textbox_draw(&textbox1, renderer);
        SDL_RenderPresent(renderer);
        draw = 0;
    }
    kiss_clean(&objects);
    return 0;

    //env->push_long(runtime->stack, (s64) (intptr_t) renderer);
}

static java_native_method method_kiss_table[] = {
    {"com/kindred/mir/engine/MirJNI", "Kiss_Init",   "()V",                    com_kindred_mir_MirJNI_Kiss_Init},

};

s32 count_KissFuncTable() {
    return sizeof(method_kiss_table) / sizeof(java_native_method);
}

__refer ptr_KissFuncTable() {
    return &method_kiss_table[0];
}