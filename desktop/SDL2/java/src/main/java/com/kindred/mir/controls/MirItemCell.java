package com.kindred.mir.controls;

import com.kindred.mir.GameCommon.UserItem;
import com.kindred.mir.libs.MirImage;

public class MirItemCell extends MirControlWithStaticImage {

    public MirItemCell(MirControl parent, long renderer_id, MirImage image) throws Exception
    {
        super(parent, renderer_id, image);
    }

    public UserItem getItem() {
        return UserItem.builder().build();
    }
}
