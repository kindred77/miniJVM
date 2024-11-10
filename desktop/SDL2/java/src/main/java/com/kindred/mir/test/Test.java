package com.kindred.mir.test;

import com.kindred.mir.test.event.Derived;

import java.io.*;
import java.nio.ByteBuffer;

public class Test {

    static byte[] toCstyleBytes(String s) {
        if (s == null) {
            return null;
        }
        //if (s.length() == 0 || s.charAt(s.length() - 1) != '\000') {
        //    s += '\000';
        //}
        byte[] barr = null;
        try {
            barr = s.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return barr;
    }

    static int byteArrayToInt(byte[] byteArray) {
        if (byteArray.length != 3) {
            throw new IllegalArgumentException("字节数组长度必须为3");
        }

        // 使用位操作将字节数组合并为一个int值
//        return ((byteArray[0] & 0xFF) << 16) |
//                ((byteArray[1] & 0xFF) << 8)  |
//                (byteArray[2] & 0xFF);

        return ((byteArray[0] & 0x0F) << 12) | ((byteArray[1] & 0x3F) << 6) | (byteArray[2] & 0x3F);
    }

    public static void fileOut(String str)
    {
        try
        {
            FileOutputStream fos =new FileOutputStream("test.txt", true);
            fos.write(str.getBytes("utf-8"));
            fos.flush();
            fos.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void testCharset() throws Exception
    {
        System.out.println("这是字符集测试例子....");
        FileOutputStream bw =new FileOutputStream("test.txt");
        String str = "主";

        System.out.println("----------------str: "+str);
        byte[] b1=str.getBytes("utf-8");
        bw.write(b1);
        for (byte b : b1)
        {
            System.out.print((b & 0xff)+",");
        }
        System.out.println("----------------byteArrayToInt: "+byteArrayToInt(b1));
        String str2 = new String(b1, "utf-8");
        byte[] b2 = str2.getBytes("utf-8");
        for (byte b : b2)
        {
            System.out.print((b & 0xff)+",");
        }
        bw.write(b2);
        bw.flush();
        bw.close();
        System.out.println("----------------str2: "+str2);
        System.out.println("----------------byteArrayToInt: "+byteArrayToInt(b2));

        if(str.equals(str2))
        {
            System.out.println("----------------str == str2------");
        }
//        byte[] b3=new String("主").getBytes("utf-8");
//        for (byte b : b3)
//        {
//            System.out.print(b+",");
//        }
//        System.out.println("----------------");
    }

    public static void charsetTest2()
    {
        String str = "主";
        System.out.println(str);
        String str2=new String(str.toCharArray());
        System.out.println(str2);
    }

    public static void testListener()
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

    public static void main(String args[]) throws Exception
    {
        //testCharset();
        charsetTest2();
    }
}
