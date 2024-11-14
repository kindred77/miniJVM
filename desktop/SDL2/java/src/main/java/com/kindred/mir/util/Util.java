package com.kindred.mir.util;

import java.io.UnsupportedEncodingException;
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

    public static String zeroEndBytesToString(byte[]  buf) throws Exception
    {
        int acture_length=0;
        for (byte b : buf) {
            if (b != 0) acture_length++;
            else break;
        }
        return new String(buf, 0, acture_length, "utf-8");
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
}
