package com.kindred.mir.controls;

import com.kindred.mir.controls.events.CommonEvent;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.util.Point;


public class MirControlWithDynamicImagesMouseEventDriven extends MirControlWithDynamicImagesEventDriven {

    protected MirImage hoverImage;
    public ControlCommonListener hoverImageChanged;

    protected MirImage pressedImage;
    public ControlCommonListener pressedImageChanged;

    public MirControlWithDynamicImagesMouseEventDriven(MirControl parent, long renderer_id, MirImage[] images) throws Exception{
        super(parent, renderer_id, images);

        this.hoverImage=images[1];
        this.pressedImage=images[2];

        register(CommonEvent.EventEnum.MouseLeave,() -> {
            return images[0];
        });
        register(CommonEvent.EventEnum.MouseEnter,() -> {
            return images[1];
        });
        register(CommonEvent.EventEnum.MouseLeftDown,() -> {
            return images[2];
        });

        this.onMouseEnter = (control, obj) -> {
            MirImage img = getEventImage(CommonEvent.EventEnum.MouseEnter);
            if(img!=null) {
                updateTexture(img.getSurface(MirImage.ImageEffect.None));
            }
        };

        this.onMouseLeftDown = (control, obj) -> {
            MirImage img = getEventImage(CommonEvent.EventEnum.MouseLeftDown);
            if(img!=null) {
                updateTexture(img.getSurface(MirImage.ImageEffect.None));
            }
        };

        this.onMouseLeftUp = (control, obj) -> {
            MirImage img = getEventImage(CommonEvent.EventEnum.MouseLeftUp);
            if(img!=null) {
                updateTexture(img.getSurface(MirImage.ImageEffect.None));
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
