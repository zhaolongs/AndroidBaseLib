package com.studyyoun.androidbaselibrary.okhttp.exce;

/**
 * Created by androidlongs on 17/6/28.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public class OkhttpException extends Exception {
    /**
     * 请求结果响应码
     * -1 网络错误
     * -2 JSON 解析错误
     * -3 未知错误
     */
    public int code = -1;
    public String exception = "网络错误";


    public OkhttpException (String message, int code) {

        this.code = code;
        this.exception = message;
    }
}
