package com.kindred.mir.controls.listener;

import com.kindred.mir.libs.MirImage;

@FunctionalInterface
public interface ControlEventChooseImageAction {
    MirImage getImage();
}
