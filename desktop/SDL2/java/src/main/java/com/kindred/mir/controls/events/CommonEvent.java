package com.kindred.mir.controls.events;

public class CommonEvent {

    public enum EventEnum
    {
        //原始事件
        MouseLeftDown,
        MouseLeftUp,
        MouseRightDown,
        MouseRightUp,
        MouseMove,

        KeyBoardPressed,

        //派生事件
        MouseLeftClick,
        MouseLeftDoubleClick,
        MouseRightClick,
        MouseRightDoubleClick,
        MouseEnter,
        MouseLeave,
    }

    private EventEnum eventType;
    private Object arg;

    public CommonEvent(EventEnum eventType, Object arg) {
        this.eventType=eventType;
        this.arg=arg;
    }

    public EventEnum getEventType() {
        return eventType;
    }

    public Object getArg() {
        return arg;
    }
}
