package com.example.limingyan.imd.util;

public class ChangeUtil {


    public static byte[] changeASCII(String string) {
        StringBuffer sb = new StringBuffer();
        char[] ch = string.toCharArray();

        for (int i = 0; i < ch.length; i++) {
       sb.append(Integer.valueOf(ch[i]).intValue());
       sb.append("/");
        }

        return sb.toString().getBytes();

    }
    public static String str2HexStr(String str)
    {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    public static void main(String[] args) {
      byte[] ss=changeASCII("我的的撒的爱上");

        for(int i=0;i<ss.length;i++){
            System.out.print(ss[i]);
        }
    }
}



