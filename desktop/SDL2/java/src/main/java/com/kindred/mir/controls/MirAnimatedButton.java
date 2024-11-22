package com.kindred.mir.controls;

import com.kindred.mir.MirMain;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLib;
import com.kindred.mir.util.Point;

import java.util.ArrayList;
import java.util.List;

public class MirAnimatedButton extends MirButton {

    public static List<MirAnimatedButton> animations = new ArrayList();

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

    private long nextOffSet;

    private int offSet;
    public ControlCommonListener offSetChanged;

    private MirImage[] images;

    public MirAnimatedButton(MirControl parent, long renderer_id, MirImage[] animImages, MirImage normalImage, MirImage hoverImage, MirImage pressedImage) throws Exception
    {
        super(parent,renderer_id,normalImage,hoverImage,pressedImage);
        this.images=animImages;
        this.animationCount=this.images.length;
        isLoop = true;
        nextFadeTime = MirMain.Time;
        nextOffSet = MirMain.Time;
        animations.add(this);

        onMouseEnter=(control, obj) -> {
            setOffSet(0);
        };
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
        nextOffSet = MirMain.Time + fadeInDelay;
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
        return this.animationCount;
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
        return isFadeIn;
    }
    public void setIsFadeIn(boolean isFadeIn)
    {
        if (this.isFadeIn == isFadeIn) {
            return;
        }
        this.nextFadeTime = MirMain.Time + fadeInDelay;
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
        return fadeInRate;
    }

    public void setFadeInRate(float fadeInRate)
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
        return fadeInDelay;
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
        return isLoop;
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
        return offSet;
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
        if (isFadeIn && MirMain.Time > nextFadeTime)
        {
            if ((opacity += fadeInRate) > 1F)
            {
                setOpacity(1F);
                isFadeIn = false;
            }

            nextFadeTime = MirMain.Time + fadeInDelay;
        }

//        if (isMouseOver(pos))
//        {
//            setOffSet(0);
//            return;
//        }

        if (!getIsVisible()|| !isAnimated || animationDelay == 0 || animationCount == 0) {
            return;
        }

        if (MirMain.Time < nextOffSet) {
            return;
        }

        //redraw();

        nextOffSet = MirMain.Time + animationDelay;

        setOffSet(offSet+1);
        if (offSet < animationCount) return;

        ControlCommonListener temp = afterAnimation;
        afterAnimation = null;

        if (!isLoop) {
            setIsAnimated(false);
        } else {
            setOffSet(0);
        }

        if (temp != null) {
            temp.doAction(this, null);
        }
    }

}
