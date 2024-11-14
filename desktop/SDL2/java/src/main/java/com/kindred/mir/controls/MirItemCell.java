package com.kindred.mir.controls;

import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;

public class MirItemCell extends MirStaticImageControl {
    public MirItemCell(MirControl parent, MirImage image) {
        super(parent, image);
    }

    public MirItemCell(MirControl parent, MirLib lib, int index) {
        super(parent, lib, index);
    }
}
