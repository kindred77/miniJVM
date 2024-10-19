package org.mini.SDL2;

import java.io.UnsupportedEncodingException;

public class SDL2 {

    static {
        SDL2.loadLib();
    }

    static boolean loaded;

    public static void loadLib() {
        if (!loaded) {
            System.setProperty("java.library.path", "./");
            System.loadLibrary("sdl2");

        }
        loaded = true;
    }

    public static final int SDL_INIT_TIMER = 0x00000001;
    public static final int SDL_INIT_AUDIO = 0x00000010;
    public static final int SDL_INIT_VIDEO = 0x00000020;
    public static final int SDL_INIT_JOYSTICK = 0x00000200;
    public static final int SDL_INIT_HAPTIC = 0x00001000;
    public static final int SDL_INIT_GAMECONTROLLER = 0x00002000;
    public static final int SDL_INIT_EVENTS = 0x00004000;
    public static final int SDL_INIT_SENSOR = 0x00008000;
    public static final int SDL_INIT_NOPARACHUTE = 0x00100000;
    public static final int SDL_INIT_EVERYTHING = SDL_INIT_TIMER
            | SDL_INIT_AUDIO
            | SDL_INIT_VIDEO
            | SDL_INIT_EVENTS
            | SDL_INIT_JOYSTICK
            | SDL_INIT_HAPTIC
            | SDL_INIT_GAMECONTROLLER
            | SDL_INIT_SENSOR;

    public static native int SDL_Init(int flags);

    /** fullscreen window */
    public static final int SDL_WINDOW_FULLSCREEN = 0x00000001;

    /** window usable with OpenGL context */
    public static final int SDL_WINDOW_OPENGL = 0x00000002;

    /** window is visible */
    public static final int SDL_WINDOW_SHOWN = 0x00000004;

    /** window is not visible */
    public static final int SDL_WINDOW_HIDDEN = 0x00000008;

    /** no window decoration */
    public static final int SDL_WINDOW_BORDERLESS = 0x00000010;

    /** window can be resized */
    public static final int SDL_WINDOW_RESIZABLE = 0x00000020;

    /** window is minimized */
    public static final int SDL_WINDOW_MINIMIZED = 0x00000040;

    /** window is maximized */
    public static final int SDL_WINDOW_MAXIMIZED = 0x00000080;

    /** window has grabbed mouse input */
    public static final int SDL_WINDOW_MOUSE_GRABBED = 0x00000100;

    /** equivalent to SDL_WINDOW_MOUSE_GRABBED for compatibility */
    public static final int SDL_WINDOW_INPUT_GRABBED = SDL_WINDOW_MOUSE_GRABBED;

    /** window has input focus */
    public static final int SDL_WINDOW_INPUT_FOCUS = 0x00000200;

    /** window has mouse focus */
    public static final int SDL_WINDOW_MOUSE_FOCUS = 0x00000400;

    public static final int SDL_WINDOW_FULLSCREEN_DESKTOP = SDL_WINDOW_FULLSCREEN | 0x00001000;

    /** window not created by SDL */
    public static final int SDL_WINDOW_FOREIGN = 0x00000800;

    /**
     * window should be created in high-DPI mode if supported.
     * On macOS NSHighResolutionCapable must be set true in the
     * application's Info.plist for this to have any effect.
     */
    public static final int SDL_WINDOW_ALLOW_HIGHDPI = 0x00002000;

    /** window has mouse captured (unrelated to MOUSE_GRABBED) */
    public static final int SDL_WINDOW_MOUSE_CAPTURE = 0x00004000;

    /** window should always be above others */
    public static final int SDL_WINDOW_ALWAYS_ON_TOP = 0x00008000;

    /** window should not be added to the taskbar */
    public static final int SDL_WINDOW_SKIP_TASKBAR = 0x00010000;

    /** window should be treated as a utility window */
    public static final int SDL_WINDOW_UTILITY = 0x00020000;

    /** window should be treated as a tooltip */
    public static final int SDL_WINDOW_TOOLTIP = 0x00040000;

    /** window should be treated as a popup menu */
    public static final int SDL_WINDOW_POPUP_MENU = 0x00080000;

    /** window has grabbed keyboard input */
    public static final int SDL_WINDOW_KEYBOARD_GRABBED = 0x00100000;

    /** window usable for Vulkan surface */
    public static final int SDL_WINDOW_VULKAN = 0x10000000;

    /** window usable for Metal view */
    public static final int SDL_WINDOW_METAL = 0x20000000;

    public static native long SDL_CreateWindow(byte[] title, int x, int y, int width, int height, int flags);

    static public byte[] toCstyleBytes(String s) {
        if (s == null) {
            return null;
        }
        if (s.length() == 0 || s.charAt(s.length() - 1) != '\000') {
            s += '\000';
        }
        byte[] barr = null;
        try {
            barr = s.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return barr;
    }

    public static void main(String args[])
    {
        System.out.println("--00--");
        int result = SDL_Init(SDL_INIT_EVERYTHING);
        System.out.println("--11--");
        if (result != 0) {
            throw new IllegalStateException("Unable to initialize SDL library");
        }
        System.out.println("--22--");
        // Create and init the window
        long win_id = SDL_CreateWindow(toCstyleBytes("Demo SDL2"), 0, 0, 1024, 768, SDL_WINDOW_SHOWN | SDL_WINDOW_RESIZABLE);
        System.out.println("--33--");
        if (win_id == 0) {
            throw new IllegalStateException("Unable to create SDL window.");
        }
    }
}
