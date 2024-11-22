package com.kindred.mir.controls;

import com.kindred.mir.controls.events.CommonEvent;
import com.kindred.mir.controls.listener.ControlEventChooseImageAction;
import com.kindred.mir.libs.MirImage;

import java.util.HashMap;
import java.util.Map;

/*
因事件驱动而变化图片
 */
public class MirControlWithDynamicImagesEventDriven extends MirControlWithDynamicImages {

    protected Map<CommonEvent.EventEnum, ControlEventChooseImageAction> eventActionMap;

    public MirControlWithDynamicImagesEventDriven(MirControl parent, long renderer_id,
                                                  MirImage[] images) throws Exception{
        super(parent, renderer_id, images);
        this.eventActionMap=new HashMap();
    }

    protected void register(CommonEvent.EventEnum event, ControlEventChooseImageAction action)
    {
        eventActionMap.put(event, action);
    }

    protected MirImage getEventImage(CommonEvent.EventEnum event)
    {
        ControlEventChooseImageAction action = eventActionMap.get(event);
        if(action!=null)
        {
            return action.getImage();
        }

        return this.images[0];
    }
}
