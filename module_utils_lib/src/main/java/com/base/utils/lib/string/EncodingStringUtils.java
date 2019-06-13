package com.base.utils.lib.string;

import java.io.UnsupportedEncodingException;

/**
 * Create by alv1 on 2019/6/13
 * <p>
 * * 站在顶峰，看世界
 * * 落在谷底，思人生
 *  1  获取指定String 的编码方式
 *  2  将未知编码方式的String重新按照utf-8的方式编码成新的String
 *
 */
public class EncodingStringUtils {
    /**
     * 獲取當前String 的編碼方式
     *
     * @param str
     * @return
     */
    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GB2312
                String s = encode;
                return s; //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    /**
     * 重新將未知編碼格式的String 編碼為 utf-8格式
     *
     * @param string
     * @return
     */
    public static String reEncodeingString(String string) {
        if (string == null || string.equals("")) {
            return "";
        }
        String lEncoding = getEncoding(string);
        if (lEncoding.equals("")) {
            return "";
        }
        try {
            return new String(string.getBytes(lEncoding), lEncoding);
        } catch (UnsupportedEncodingException pE) {
            pE.printStackTrace();
            return "";
        }
    }
}
