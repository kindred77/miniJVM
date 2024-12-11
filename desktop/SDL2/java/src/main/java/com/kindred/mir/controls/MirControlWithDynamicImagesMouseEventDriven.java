package com.kindred.mir.controls;

import com.kindred.mir.controls.events.CommonEvent;
import com.kindred.mir.controls.events.CommonEvent.EventEnum;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.libs.MirImage;

public class MirControlWithDynamicImagesMouseEventDriven extends MirControlWithDynamicImagesEventDriven {

    protected MirImage hoverImage;
    public ControlCommonListener hoverImageChanged;

    protected MirImage pressedImage;
    public ControlCommonListener pressedImageChanged;

    public MirControlWithDynamicImagesMouseEventDriven(MirControl parent, long renderer_id, MirImage[] images) throws Exception{
        super(parent, renderer_id, images);

        this.hoverImage=images[1];
        this.pressedImage=images[2];

        register(CommonEvent.EventEnum.MouseLeave,() -> images[0]);
        register(CommonEvent.EventEnum.MouseLeftUp,() -> images[0]);
        register(CommonEvent.EventEnum.MouseEnter,() -> images[1]);
        register(CommonEvent.EventEnum.MouseLeftDown,() -> images[2]);

        this.onMouseEnter = (control, obj) -> {
            MirImage img = getEventImage(CommonEvent.EventEnum.MouseEnter);
            if(img!=null) {
                updateTexture(img.getSurface(MirImage.ImageEffect.None));
            } else {
                updateTexture(0L);
            }
        };

        this.onMouseLeftDown = (control, obj) -> {
            MirImage img = getEventImage(CommonEvent.EventEnum.MouseLeftDown);
            if(img!=null) {
                updateTexture(img.getSurface(MirImage.ImageEffect.None));
            } else {
                updateTexture(0L);
            }
        };

        this.onMouseLeftUp = (control, obj) -> {
            MirImage img = getEventImage(CommonEvent.EventEnum.MouseLeftUp);
            if(img!=null) {
                updateTexture(img.getSurface(MirImage.ImageEffect.None));
            } else {
                updateTexture(0L);
            }
        };
        this.onMouseLeave = (control, obj) -> {
            MirImage img = getEventImage(EventEnum.MouseLeave);
            if(img!=null) {
                updateTexture(img.getSurface(MirImage.ImageEffect.None));
            } else {
                updateTexture(0L);
            }
        };
    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

        if (!disposing) {
            return;
        }

        hoverImageChanged = null;
        hoverImage = null;

        pressedImageChanged = null;
        pressedImage = null;
    }
}
