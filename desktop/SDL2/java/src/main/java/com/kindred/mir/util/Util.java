package com.kindred.mir.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static byte[] toCstyleBytes(String s) {
        if (s == null) {
            return null;
        }
        if (s.length() == 0 || s.charAt(s.length() - 1) != '\000') {
            s += '\000';
        }
        byte[] barr = null;
        try {
            barr = s.getBytes("utf-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return barr;
    }

    public static String zeroEndBytesToString(byte[]  buf)
    {
        int acture_length=0;
        for (byte b : buf) {
            if (b != 0) {
                acture_length++;
            }
            else {
                break;
            }
        }
        String result;
        try {
            result=new String(buf, 0, acture_length, "utf-8");
        } catch (Exception e) {
            return "";
        }
        return result;
    }

    public static int[] genSeq(int startInclude, int endInclude)
    {
        int[] res = new int[endInclude-startInclude+1];
        for (int i =0; i<res.length; i++)
        {
            res[i] = startInclude+i;
        }

        return res;
    }

    public static short ToInt16(byte[] bytes, int index) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, index, 2);
        //buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.getShort();
    }

    public static int ToInt32(byte[] bytes, int index) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, index, 4);
        //buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.getInt();
    }

    public static int ToUInt16(byte[] bytes, int index) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, index, 2);
        //buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.getShort() & 0xFFFF;
    }

    public static boolean EnumHasFlag(int srcVal, int tgtVal)
    {
        if ((srcVal & tgtVal) == tgtVal)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
