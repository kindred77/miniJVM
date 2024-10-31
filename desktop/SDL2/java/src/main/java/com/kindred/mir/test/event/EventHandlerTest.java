package com.kindred.mir.test.event;

interface SomethingChangeListener
{
    void somethingChangeAction(int changeVal);
}

public class EventHandlerTest implements SomethingChangeListener{

    public SomethingChangeListener listener;

    private int something=0;
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
            listener.somethingChangeAction(this.something);
        }
        else
        {
            somethingChangeAction(this.something);
        }
    }

    @Override
    public void somethingChangeAction(int changeVal) {

    }

    public static void main(String args[])
    {
        EventHandlerTest test=new EventHandlerTest(){
            public void somethingChangeAction(int changedVal)
            {
                System.out.println("this original action: "+changedVal);
            }
        };

        test.setSomethingChange(1);
        test.listener=new SomethingChangeListener(){
            public void somethingChangeAction(int changedVal)
            {
                System.out.println("this new action: "+changedVal);
            }
        };
        test.setSomethingChange(2);
    }
}
