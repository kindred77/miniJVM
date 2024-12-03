package com.kindred.mir.controls;

import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.engine.MirTexture;
import com.kindred.mir.util.*;
import com.kindred.sdl.constcode.SDLBlendMode;

/*
带有texture的控件，画图的方式为updateTexture，渲染流程直接渲染texture
 */
public class MirControlWithTexture extends MirControlCanBeDrawn {

    protected MirTexture controlTexture;

    public MirControlWithTexture(MirControl parent, long renderer_id) {
        super(parent, renderer_id);
        controlTexture=new MirTexture();
    }


//    public Size getTrueSize()
//    {
//        return size;
//    }

    /*
    有任何影响texture渲染变动的操作都要调用此方法,
    texture有变动则返回true
     */
    protected boolean updateTexture(long surface_id)
    {
//        if (controlTexture != null && !controlTexture.getIsDisposed() && !controlTexture.getSize().equals(size)) {
//            controlTexture.dispose();
//            return false;
//        }
        controlTexture.update(getRenderer(), surface_id);

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

        if (controlTexture != null && controlTexture.getIsValid()) {
            if (isBlending) {
                MirJNI.SDL_SetTextureBlendMode(controlTexture.getTexture(), SDLBlendMode.SDL_BLENDMODE_BLEND);
            }

            Rectangle dstRect = getDisplayRectangle();

            int[] rct = {dstRect.getX(), dstRect.getY(), dstRect.getWidth(), dstRect.getHeight()};
            MirJNI.SDL_RenderCopy(getRenderer(), controlTexture.getTexture(), null, rct);
        }

        if (getIsBorder()) {
            drawBorder();
        }

        //afterDrawControl();

        return true;
    }


}
