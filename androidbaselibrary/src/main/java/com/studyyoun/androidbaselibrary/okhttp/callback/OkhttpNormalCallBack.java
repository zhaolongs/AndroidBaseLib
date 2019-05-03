package com.studyyoun.androidbaselibrary.okhttp.callback;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.studyyoun.androidbaselibrary.okhttp.exce.OkhttpException;
import com.studyyoun.androidbaselibrary.utils.FastJsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
