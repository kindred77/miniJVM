package com.kindred.mir.controls.listener;

import com.kindred.mir.controls.MirControl;

@FunctionalInterface
public interface ControlCommonListener {
    void doAction(MirControl control, Object argObj);
}
