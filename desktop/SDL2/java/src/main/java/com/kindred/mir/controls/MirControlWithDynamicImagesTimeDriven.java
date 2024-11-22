package com.kindred.mir.controls;

import com.kindred.mir.libs.MirImage;

/*
因时间驱动而变化图片
 */
public class MirControlWithDynamicImagesTimeDriven extends MirControlWithDynamicImages {

    public MirControlWithDynamicImagesTimeDriven(MirControl parent, long renderer_id, MirImage[] images) {
        super(parent, renderer_id, images);
    }
}
