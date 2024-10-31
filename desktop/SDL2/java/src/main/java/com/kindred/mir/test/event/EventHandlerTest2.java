package com.kindred.mir.test.event;


import com.kindred.mir.test.event.listener.SomethingChangeListener2;

public class EventHandlerTest2{

    public SomethingChangeListener2 listener;

    public int something=0;

    public void setSomethingChange(int something)
    {
        if (this.something == something)
        {
            return;
        }
        this.something=something;
        onSomethingChanged();
    }

    public void onSomethingChanged()
    {
        if(listener!=null)
        {
            listener.somethingChangeAction(this);
        }
    }

    public void setListener(SomethingChangeListener2 listener)
    {
        this.listener=listener;
    }
}
