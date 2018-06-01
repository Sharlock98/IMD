package com.example.limingyan.imd.util;

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

    public static void main(String[] args) {
        String s="10";
        for (int i = 0; i < getHexBytes(s).length; i++) {

            System.out.println(getHexBytes(s)[i]);
        }

    }
}
