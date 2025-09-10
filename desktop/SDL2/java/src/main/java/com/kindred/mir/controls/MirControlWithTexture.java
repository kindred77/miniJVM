package com.kindred.mir.controls;

import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.engine.MirTexture;
import com.kindred.mir.util.*;
import com.kindred.sdl.constcode.SDLBlendMode;

/*
带有texture的控件，画图的方式为updateTexture，渲染流程直接渲染texture
 */
public class MirControlWithTexture extends MirControlCanBeDrawn {

    protected final MirTexture controlTexture=new MirTexture();;

    public MirControlWithTexture(MirControl parent, long renderer_id) {
        super(parent, renderer_id);
    }


//    public Size getTrueSize()
//    {
//        return size;
//    }

    /**
    有任何影响texture渲染变动的操作都要调用此方法,
    texture有变动则返回true.
     注意：surface不会被销毁，需要手动销毁
     */
    protected boolean updateTexture(long surface_id,boolean ifReleaseSurface)
    {
//        if (controlTexture != null && !controlTexture.getIsDisposed() && !controlTexture.getSize().equals(size)) {
//            controlTexture.dispose();
//            return false;
//        }
        synchronized (controlTexture) {
            controlTexture.update(getRenderer(), surface_id,ifReleaseSurface);
        }


        //TODO 在surface上画边框，再render
        if(getIsBorder())
        {
            updateBorderInfo();
        }

        return true;
    }

    //屏蔽掉
    @Override
    protected final boolean beginDraw() {
        return true;
    }

    //屏蔽掉
    @Override
    protected final boolean endDraw() {
        return true;
    }

    /*
    只有MirControlWithTexture才能有border
     */
    @Override
    protected boolean drawControl()
    {
        //beforeDrawControl();

        synchronized (controlTexture) {
            if (controlTexture.getIsValid()) {
                if (isBlending) {
                    MirJNI.SDL_SetTextureBlendMode(controlTexture.getTexture(), SDLBlendMode.SDL_BLENDMODE_BLEND);
                }

                Rectangle dstRect = getAbsoluteRectangle();

                int[] rct = {dstRect.getX(), dstRect.getY(), dstRect.getWidth(), dstRect.getHeight()};
                MirJNI.SDL_RenderCopy(getRenderer(), controlTexture.getTexture(), null, rct);
            }
        }


//        if (getIsBorder()) {
//            drawBorder();
//        }

        //afterDrawControl();

        return true;
    }


}
