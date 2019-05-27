package com.studyyoun.androidbaselibrary.okhttp.callback;

/**
 * Created by androidlongs on 17/6/28.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public abstract class OkhttpNormalCallBack  {
    /**
     * 请求成功回调方法
     */
    public abstract void onSuccess(Object obj);

    /**
     * 请求失败回调方法
     */
    public abstract void onFaile(Object obj);

}
