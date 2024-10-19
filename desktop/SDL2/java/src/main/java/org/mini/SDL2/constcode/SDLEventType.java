package org.mini.SDL2.constcode;

public class SDLEventType {

    /** Unused (do not remove) */
    public static final int SDL_FIRSTEVENT = 0;

    /** User-requested quit */
    public static final int SDL_QUIT = 0x100;

    /**
     * The application is being terminated by the OS
     * Called on iOS in applicationWillTerminate()
     * Called on Android in onDestroy()
     */
    public static final int SDL_APP_TERMINATING = 0x101;

    /**
     * The application is low on memory, free memory if possible.
     * Called on iOS in applicationDidReceiveMemoryWarning()
     * Called on Android in onLowMemory()
     */
    public static final int SDL_APP_LOWMEMORY = 0x102;

    /**
     * The application is about to enter the background
     * Called on iOS in applicationWillResignActive()
     * Called on Android in onPause()
     */
    public static final int SDL_APP_WILLENTERBACKGROUND = 0x103;

    /**
     * The application did enter the background and may not get CPU for some time
     * Called on iOS in applicationDidEnterBackground()
     * Called on Android in onPause()
     */
    public static final int SDL_APP_DIDENTERBACKGROUND = 0x104;

    /**
     * The application is about to enter the foreground
     * Called on iOS in applicationWillEnterForeground()
     * Called on Android in onResume()
     */
    public static final int SDL_APP_WILLENTERFOREGROUND = 0x105;

    /**
     * The application is now interactive
     * Called on iOS in applicationDidBecomeActive()
     * Called on Android in onResume()
     */
    public static final int SDL_APP_DIDENTERFOREGROUND = 0x106;

    /** The user's locale preferences have changed. */
    public static final int SDL_LOCALECHANGED = 0x107;

    /** Display state change */
    public static final int SDL_DISPLAYEVENT = 0x150;

    /** Window state change */
    public static final int SDL_WINDOWEVENT = 0x200;

    /** System specific event */
    public static final int SDL_SYSWMEVENT = 0x201;

    /** Key pressed */
    public static final int SDL_KEYDOWN = 0x300;

    /** Key released */
    public static final int SDL_KEYUP = 0x301;

    /** Keyboard text editing (composition) */
    public static final int SDL_TEXTEDITING = 0x302;

    /** Keyboard text input */
    public static final int SDL_TEXTINPUT = 0x303;

    /**
     * Keymap changed due to a system event such as an
     * input language or keyboard layout change.
     */
    public static final int SDL_KEYMAPCHANGED = 0x304;

    /** Extended keyboard text editing (composition) */
    public static final int SDL_TEXTEDITING_EXT = 0x305;

    /** Mouse moved */
    public static final int SDL_MOUSEMOTION = 0x400;

    /** Mouse button pressed */
    public static final int SDL_MOUSEBUTTONDOWN = 0x401;

    /** Mouse button released */
    public static final int SDL_MOUSEBUTTONUP = 0x402;

    /** Mouse wheel motion */
    public static final int SDL_MOUSEWHEEL = 0x403;

    /** Joystick axis motion */
    public static final int SDL_JOYAXISMOTION = 0x600;

    /** Joystick trackball motion */
    public static final int SDL_JOYBALLMOTION = 0x601;

    /** Joystick hat position change */
    public static final int SDL_JOYHATMOTION = 0x602;

    /** Joystick button pressed */
    public static final int SDL_JOYBUTTONDOWN = 0x603;

    /** Joystick button released */
    public static final int SDL_JOYBUTTONUP = 0x604;

    /** A new joystick has been inserted into the system */
    public static final int SDL_JOYDEVICEADDED = 0x605;

    /** An opened joystick has been removed */
    public static final int SDL_JOYDEVICEREMOVED = 0x606;

    /** Joystick battery level change */
    public static final int SDL_JOYBATTERYUPDATED = 0x607;

    /** Game controller axis motion */
    public static final int SDL_CONTROLLERAXISMOTION = 0x650;

    /** Game controller button pressed */
    public static final int SDL_CONTROLLERBUTTONDOWN = 0x651;

    /** Game controller button released */
    public static final int SDL_CONTROLLERBUTTONUP = 0x652;

    /** A new Game controller has been inserted into the system */
    public static final int SDL_CONTROLLERDEVICEADDED = 0x653;

    /** An opened Game controller has been removed */
    public static final int SDL_CONTROLLERDEVICEREMOVED = 0x654;

    /** The controller mapping was updated */
    public static final int SDL_CONTROLLERDEVICEREMAPPED = 0x655;

    /** Game controller touchpad was touched */
    public static final int SDL_CONTROLLERTOUCHPADDOWN = 0x656;

    /** Game controller touchpad finger was moved */
    public static final int SDL_CONTROLLERTOUCHPADMOTION = 0x657;

    /** Game controller touchpad finger was lifted */
    public static final int SDL_CONTROLLERTOUCHPADUP = 0x658;

    /** Game controller sensor was updated */
    public static final int SDL_CONTROLLERSENSORUPDATE = 0x659;

    public static final int SDL_FINGERDOWN = 0x700;
    public static final int SDL_FINGERUP = 0x701;
    public static final int SDL_FINGERMOTION = 0x702;

    public static final int SDL_DOLLARGESTURE = 0x800;
    public static final int SDL_DOLLARRECORD = 0x801;
    public static final int SDL_MULTIGESTURE = 0x802;

    /** The clipboard or primary selection changed */
    public static final int SDL_CLIPBOARDUPDATE = 0x900;

    /** The system requests a file open */
    public static final int SDL_DROPFILE = 0x1000;

    /** text/plain drag-and-drop event */
    public static final int SDL_DROPTEXT = 0x1001;

    /** A new set of drops is beginning (NULL filename) */
    public static final int SDL_DROPBEGIN = 0x1002;

    /** Current set of drops is now complete (NULL filename) */
    public static final int SDL_DROPCOMPLETE = 0x1003;

    /** A new audio device is available */
    public static final int SDL_AUDIODEVICEADDED = 0x1100;

    /** An audio device has been removed. */
    public static final int SDL_AUDIODEVICEREMOVED = 0x1101;

    /** A sensor was updated */
    public static final int SDL_SENSORUPDATE = 0x1200;

    /** The render targets have been reset and their contents need to be updated */
    public static final int SDL_RENDER_TARGETS_RESET = 0x2000;

    /** The device has been reset and all textures need to be recreated */
    public static final int SDL_RENDER_DEVICE_RESET = 0x2001;

    /** Signals the end of an event poll cycle */
    public static final int SDL_POLLSENTINEL = 0x7F00;

    /**
     * Events {@link #SDL_USEREVENT} through {@link #SDL_LASTEVENT} are for your use,
     * and should be allocated with SDL_RegisterEvents()
     */
    public static final int SDL_USEREVENT = 0x8000;

    /**
     * This last event is only for bounding internal arrays
     */
    public static final int SDL_LASTEVENT = 0xFFFF;

    // TODO: Generate public static String toString(int value)

    private SDLEventType() {
    }
    
}
