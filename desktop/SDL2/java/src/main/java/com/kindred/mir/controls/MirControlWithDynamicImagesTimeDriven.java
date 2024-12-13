package com.kindred.mir.controls;

import com.kindred.mir.Settings;
import com.kindred.mir.controls.events.CommonEvent;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage;

/*
因时间驱动而变化图片
 */
public class MirControlWithDynamicImagesTimeDriven extends MirControlWithDynamicImagesEventDriven {

    protected boolean isLoop=false;
    private long animationDelay=500;
    private long nextOffSet;
    private boolean isAnimated;

    protected ControlCommonListener afterAnimation;

    private int index=0;

    public MirControlWithDynamicImagesTimeDriven(MirControl parent, long renderer_id, MirImage[] images) throws Exception{
        super(parent, renderer_id, images);
        register(CommonEvent.EventEnum.TimeInterval,() -> images[index]);
    }

    /**
     * offset change事件触发
     */
    private void fireOffsetEvent() {
        index++;
        if (this.index < images.length) {
            MirImage img = getEventImage(CommonEvent.EventEnum.TimeInterval);
            if(img!=null) {
                updateTexture(img.getSurface(MirImage.ImageEffect.None));
            } else {
                updateTexture(0L);
            }
            return;
        }
        //达到结尾
        ControlCommonListener temp = afterAnimation;
        afterAnimation = null;

        if (!isLoop) {
            isAnimated = false;
        } else {
            index=0;
        }

        if (temp != null) {
            temp.doAction(this, null);
        }
    }

    /**
     * 不停的执行，到点触发offset change事件
     */
    public final void update() {
        if (!getIsVisible() || !isAnimated || animationDelay == 0 || this.images.length == 0) {
            return;
        }

        long time = MirJNI.SDL_GetTicks();

        if (time < nextOffSet) {
            return;
        }

        nextOffSet = time + animationDelay;
        fireOffsetEvent();
    }

    public final void setIsAnimated(boolean isAnimated)
    {
        if (this.isAnimated == isAnimated) {
            return;
        }
        this.isAnimated = isAnimated;
        nextOffSet = MirJNI.SDL_GetTicks() + animationDelay;
    }

    @Override
    protected boolean drawControl() {
        update();
        return super.drawControl();
    }
}
