package com.kindred.mir.test;

import com.kindred.mir.test.event.Derived;

public class Test {

    public static void main(String args[])
    {
        Derived test=new Derived();
        test.setListener((obj) -> {
            //val=val+100;
            if(obj instanceof  Derived)
            {
                Derived dev =(Derived)obj;
                dev.a();
            }
        });
        test.setSomethingChange(1);
        test.setListener((val) -> {
            //val=val+200;
            System.out.println("-----"+val.something);
        });
        test.setSomethingChange(2);
    }
}
