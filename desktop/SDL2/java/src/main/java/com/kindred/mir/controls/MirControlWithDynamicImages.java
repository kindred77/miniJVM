package com.kindred.mir.controls;

import com.kindred.mir.engine.MirTexture;
import com.kindred.mir.libs.MirImage;

/*
带有多张图片的控件，会因为鼠标事件，键盘事件，时间等因素变化
 */
public class MirControlWithDynamicImages extends MirControlWithTexture {

    protected MirImage images[];

    public MirControlWithDynamicImages(MirControl parent, long renderer_id, MirImage images[]) {
        super(parent, renderer_id);
        this.images=images;
        //默认图片为第一张
        if(images!=null && images.length>1 && images[0]!=null) {
            setSize(images[0].getTrueSize());
            //默认在正中
            setLocation(this.Center());
            super.updateTexture(images[0].getSurface(MirImage.ImageEffect.None),false);
        }
    }
}
