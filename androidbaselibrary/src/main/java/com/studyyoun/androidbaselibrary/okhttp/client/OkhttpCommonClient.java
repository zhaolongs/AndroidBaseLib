package com.studyyoun.androidbaselibrary.okhttp.client;

import com.studyyoun.androidbaselibrary.model.RequestModel;
import com.studyyoun.androidbaselibrary.okhttp.callback.OkhttpCommonCallBack;
import com.studyyoun.androidbaselibrary.okhttp.callback.OkhttpNormalCallBack;
import com.studyyoun.androidbaselibrary.okhttp.https.HttpsUtils;
import com.studyyoun.androidbaselibrary.okhttp.qequest.OkhttpCommonRequest;
import com.studyyoun.androidbaselibrary.utils.FastJsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by androidlongs on 17/6/28.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 * <p>
 * <p>
 * <p>
 * okhttp client 封装
 */

public class OkhttpCommonClient {
    //设置超时时间
    private static final int TIME_OUT = 30;

    private static OkHttpClient sOkHttpClient;

    //设置client配置参数
    static {
        //创建 OkHttpClient.Builder
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        //设置超时时间
        builder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);

        //允许重定向
        builder.followRedirects(true);


        // 支持
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });

        builder.sslSocketFactory(HttpsUtils.getSslFactory());

        //初始化OKhttpClient
        sOkHttpClient = builder.build();
    }


    /**
     * 发送具体的http[表情]tps请求
     */
    private static Call sentRequest(Request request, OkhttpCommonCallBack callback) {
        Call call = sOkHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }


    /**
     * 发送get请求
     */
    public static Call sentGetRequest(String url, ConcurrentHashMap<String, String> map, OkhttpCommonCallBack callback) {

        Call call = sOkHttpClient.newCall(OkhttpCommonRequest.createGetRequest(url, map));
        call.enqueue(callback);
        return call;
    }

    /**
     * 发送Post请求
     */
    public static Call sentPostRequest(String url, ConcurrentHashMap<String, String> map, OkhttpCommonCallBack callback) {
        Call call = sOkHttpClient.newCall(OkhttpCommonRequest.createPosRequest(url, map));
        call.enqueue(callback);
        return call;
    }

    /**
     * 发送Post File 请求
     */
    public static Call sentPostFileRequest(String url, ConcurrentHashMap<String, Object> map, OkhttpCommonCallBack callback) {

        Call call = sOkHttpClient.newCall(OkhttpCommonRequest.createPosFileRequest(url, map));
        call.enqueue(callback);
        return call;
    }

    /**
     * 发送Post File 请求
     *
     * @param url     请求链接
     * @param keyMap  附加请求参数
     * @param picList 图片文件
     */
    public static Call sentPostFileListRequest(String url, ConcurrentHashMap<String, Object> keyMap, List<File> picList, OkhttpCommonCallBack callback) {
        Call call = sOkHttpClient.newCall(OkhttpCommonRequest.createPosFileListRequest(url, keyMap, picList));
        call.enqueue(callback);
        return call;
    }

    /**
     * 下载文件
     */
    public static Call getDownFileRequest(String url, okhttp3.Callback callback) {
        Call call = sOkHttpClient.newCall(OkhttpCommonRequest.createGetRequest(url, null));
        call.enqueue(callback);
        return call;
    }


    public static void httpRequest(String json, final OkhttpNormalCallBack callBack) {
        if (json == null || json.equals("")) {
            if (callBack != null) {
                callBack.onFaile("请求参数为Null");
            }
            return;
        }
        RequestModel lRequestModel = FastJsonUtil.toBean(json, RequestModel.class);
        String lMethod = lRequestModel.method;
        lMethod = lMethod.toLowerCase();
        if (lMethod.equals("post")) {
            Call call = sOkHttpClient.newCall(OkhttpCommonRequest.createPosBodyHtmlRequest(lRequestModel));
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callBack != null) {
                        callBack.onFaile(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (callBack != null) {
                        callBack.onSuccess(response);
                    }
                }
            });
        } else {
            Call call = sOkHttpClient.newCall(OkhttpCommonRequest.createGetBodyHtmlRequest(lRequestModel));
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (callBack != null) {
                        callBack.onFaile(e.getMessage());
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (callBack != null) {
                        callBack.onSuccess(response);
                    }
                }
            });
        }

    }
}
