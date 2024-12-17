package com.kindred.mir.controls;

import com.kindred.mir.Settings;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.libs.MirImage;

public class MirAnimatedControl extends MirControlWithDynamicImagesTimeDriven {

    private long nextFadeTime;
    private boolean isFadeIn;
    public ControlCommonListener fadeInChanged;

    private float fadeInRate;
    public ControlCommonListener fadeInRateChanged;

    private long fadeInDelay;
    public ControlCommonListener fadeInDelayChanged;

    public MirAnimatedControl(MirControl parent, long renderer_id, MirImage[] images, boolean isLoop, long animationDelay) throws Exception
    {
        super(parent, renderer_id, images,isLoop,animationDelay);
    }

    public boolean getIsFadeIn()
    {
        return this.isFadeIn;
    }
    public void setIsFadeIn(boolean isFadeIn)
    {
        if (this.isFadeIn == isFadeIn) {
            return;
        }
        nextFadeTime = Settings.getTime() + fadeInDelay;
        this.isFadeIn = isFadeIn;
        onFadeInChanged();
    }

    protected void onFadeInChanged()
    {
        if (fadeInChanged != null) {
            fadeInChanged.doAction(this, null);
        }
    }

    public float getFadeInRate()
    {
        return this.fadeInRate;
    }
    public void setFadeInRate()
    {
        if (this.fadeInRate == fadeInRate) {
            return;
        }
        this.fadeInRate = fadeInRate;
        onFadeInRateChanged();
    }

    protected void onFadeInRateChanged()
    {
        if (fadeInRateChanged != null) {
            fadeInRateChanged.doAction(this, null);
        }
    }

    public long getFadeInDelay()
    {
        return this.fadeInDelay;
    }
    public void setFadeInDelay(long fadeInDelay)
    {
        if (this.fadeInDelay == fadeInDelay) {
            return;
        }
        this.fadeInDelay = fadeInDelay;
        onFadeInDelayChanged();
    }

    protected void onFadeInDelayChanged()
    {
        if (fadeInDelayChanged != null) {
            fadeInDelayChanged.doAction(this, null);
        }
    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

        if (!disposing) {
            return;
        }

        afterAnimation = null;
        isLoop = false;
    }

}
