package com.example.limingyan.imd.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;

public class ChangeUtil {

    /**
     * 10转16
     * @param message
     * @return
     */
   public static  byte[] getHexBytes(String message) {
        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }
    public static String gb2312decode( String string) throws UnsupportedEncodingException{
        byte[] bytes = new byte[string.length() / 2];
        for(int i = 0; i < bytes.length; i ++){
            byte high = Byte.parseByte(string.substring(i * 2, i * 2 + 1), 16);
            byte low = Byte.parseByte(string.substring(i * 2 + 1, i * 2 + 2), 16);
            bytes[i] = (byte) (high << 4 | low);
        }
        return new String(bytes, "gb2312");
    }
    public static void main(String[] args) {
        String s="A55A058200110001";
//        for (int i = 0; i < getHexBytes(s).length; i++) {
//
//            System.out.println(getHexBytes(s)[i]);
//        }
        try {
            System.out.println(gb2312decode("你好")
            );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
