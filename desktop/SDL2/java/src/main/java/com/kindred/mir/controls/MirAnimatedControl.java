package com.kindred.mir.controls;

import com.kindred.mir.Settings;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;

import java.util.ArrayList;
import java.util.List;

public class MirAnimatedControl extends MirControlWithDynamicImagesTimeDriven {
    public static List<MirAnimatedControl> animations = new ArrayList();

    private boolean isAnimated;
    public ControlCommonListener animatedChanged;

    private int animationCount;
    public ControlCommonListener animationCountChanged;

    private long animationDelay;
    public ControlCommonListener animationDelayChanged;

    private long nextFadeTime;
    private boolean isFadeIn;
    public ControlCommonListener fadeInChanged;

    private float fadeInRate;
    public ControlCommonListener fadeInRateChanged;

    private long fadeInDelay;
    public ControlCommonListener fadeInDelayChanged;

    public ControlCommonListener afterAnimation;

    private boolean isLoop;
    public ControlCommonListener loopChanged;

    private int offSet;
    private long nextOffSet;
    public ControlCommonListener offSetChanged;

    //private MirImage[] images;

    public MirAnimatedControl(MirControl parent, long renderer_id, MirImage[] images) throws Exception
    {
        super(parent, renderer_id, images);
        isLoop = true;
        nextFadeTime = Settings.getTime();
        nextOffSet = Settings.getTime();
        animationCount=images.length;
        animations.add(this);
    }

    public boolean getIsAnimated()
    {
        return isAnimated;
    }
    public void setIsAnimated(boolean isAnimated)
    {
        if (this.isAnimated == isAnimated) {
            return;
        }
        this.isAnimated = isAnimated;
        nextOffSet = Settings.getTime() + fadeInDelay;
        onAnimatedChanged();
    }

    protected void onAnimatedChanged()
    {
        //redraw();
        if (animatedChanged != null) {
            animatedChanged.doAction(this, null);
        }
    }

    public int getAnimationCount()
    {
        return animationCount;
    }
    public void setAnimationCount(int animationCount)
    {
        if (this.animationCount == animationCount) {
            return;
        }
        this.animationCount = animationCount;
        onAnimationCountChanged();
    }

    protected void onAnimationCountChanged()
    {
        if (animationCountChanged != null) {
            animationCountChanged.doAction(this, null);
        }
    }

    public long getAnimationDelay()
    {
        return animationDelay;
    }
    public void setAnimationDelay(long animationDelay)
    {
        if (this.animationDelay == animationDelay) {
            return;
        }
        this.animationDelay = animationDelay;
        onAnimationDelayChanged();
    }

    protected void onAnimationDelayChanged()
    {
        if (animationDelayChanged != null) {
            animationDelayChanged.doAction(this, null);
        }
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

    public boolean getIsLoop()
    {
        return this.isLoop;
    }
    public void setIsLoop(boolean isLoop)
    {
        if (this.isLoop == isLoop) {
            return;
        }
        this.isLoop = isLoop;
        onLoopChanged();
    }

    protected void onLoopChanged()
    {
        if (loopChanged != null) {
            loopChanged.doAction(this, null);
        }
    }

    public int getOffSet()
    {
        return this.offSet;
    }
    public void setOffSet(int offSet)
    {
        if (this.offSet == offSet) {
            return;
        }
        this.offSet = offSet;
        onOffSetChanged();
    }

    protected void onOffSetChanged()
    {
        //setMirImage(images[this.offSet]);
        if (offSetChanged != null) {
            offSetChanged.doAction(this, null);
        }
    }

    public void updateOffSet()
    {
        if (isFadeIn && Settings.getTime() > nextFadeTime) {
            if ((opacity += fadeInRate) > 1F) {
                opacity = 1F;
                isFadeIn = false;
            }

            nextFadeTime = Settings.getTime() + fadeInDelay;
        }

        if (!getIsVisible() || !isAnimated || animationDelay == 0 || animationCount == 0) {
            return;
        }

        if (Settings.getTime() < nextOffSet) {
            return;
        }

        //redraw();

        nextOffSet = Settings.getTime() + animationDelay;

        setOffSet(this.offSet+1);
        if (this.offSet < animationCount) {
            return;
        }

        ControlCommonListener temp = afterAnimation;
        afterAnimation = null;

        if (!isLoop) {
            isAnimated = false;
        }
        else {
            setOffSet(0);
        }

        if (temp != null) {
            temp.doAction(this, null);
        }
    }

    @Override
    protected void dispose(boolean disposing)
    {
        super.dispose(disposing);

        if (!disposing) {
            return;
        }

        animatedChanged = null;
        isAnimated = false;

        animationCountChanged = null;
        animationCount = 0;

        animationDelayChanged = null;
        animationDelay = 0;

        afterAnimation = null;

        loopChanged = null;
        isLoop = false;

        offSetChanged = null;
        offSet = 0;

        nextOffSet = 0;

        animations.remove(this);
    }

}
