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

public abstract class OkhttpCommonCallBack implements Callback {
    /**
     * 请求接口返回数据结构异常
     */
    public static int EXCEPTION_SERVICE_DATA_STRU=-7;
    /**
     * 请求接口返回数据为NULL
     */
    public static int EXCEPTION_SERVICE_DATA_NULL=-4;
    /**
     * 请求接口操作异常
     */
    public static int EXCEPTION_SERVICE=-3;
    /**
     * 请求网络异常
     */
    public static int EXCEPTION_INTERNET=-2;
    /**
     * JSON解析异常
     */
    public static int EXCEPTION_JSON=-1;

    private Class<?> mTClass;
    private Handler mHandler;
    private boolean mIsList = false;

    public OkhttpCommonCallBack() {

        mHandler = new Handler(Looper.getMainLooper());
    }

    public OkhttpCommonCallBack(Class<?> clas) {

        mTClass = clas;
        mHandler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void onFailure(Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onFaile(new OkhttpException(e.getMessage(), -2));
            }
        });

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        final String string = response.body().string();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                handlerSuccessResult(string);
            }
        });
    }

    /**
     * 处理请求响应成功结果
     */
    private void handlerSuccessResult(String string) {

        if (TextUtils.isEmpty(string)) {
            onFaile(new OkhttpException("请求异常", -4));
        } else {
            try {

                JSONObject jsonObject = new JSONObject(string);
                int status = jsonObject.getInt("code");
                String message = jsonObject.getString("msg");
                String content = jsonObject.getString("data");

                if (mTClass == null) {
                    if (status==1000) {
                        onSuccess(string);
                    }else {
                        onFaile(new OkhttpException("服务数据结构异常 "+message, -7));
                    }
                } else {
                    if (status==1000){
                        if (!content.equals("")){
                            if (content.startsWith("{") && content.endsWith("}")) {
                                Object o = FastJsonUtil.toBean(content, mTClass);
                                onSuccess(o);
                            } else if (content.startsWith("[") && content.endsWith("]")) {
                                List<?> objects = FastJsonUtil.toList(content, mTClass);
                                onSuccess(objects);
                            } else {
                                onFaile(new OkhttpException("服务数据结构异常 ", -7));
                            }
                        }else {
                            onFaile(new OkhttpException(message, -3));
                        }
                    }else {
                        onFaile(new OkhttpException(message, -3));
                    }
                }
            } catch (Exception e) {
                if (e instanceof JSONException) {
                    onFaile(new OkhttpException("JSON解析异常 " + e.getMessage(), -1));
                } else {
                    onFaile(new OkhttpException("网络异常 " + e.getMessage(), -2));
                }

            }
        }
    }


    /**
     * 请求成功回调方法
     */
    public abstract void onSuccess(Object obj);

    /**
     * 请求失败回调方法
     */
    public abstract void onFaile(Object obj);

}
