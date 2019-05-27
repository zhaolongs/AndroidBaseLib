package com.studyyoun.androidbaselibrary.okhttp.qequest;

import com.studyyoun.androidbaselibrary.model.RequestModel;
import com.studyyoun.androidbaselibrary.utils.FastJsonUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by androidlongs on 17/6/28.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 * <p>
 * <p>
 * Okhttp 请求Request封装
 */

public class OkhttpCommonRequest {


    /**
     * 创建 post Request对象
     *
     * @return post Request对象
     */
    public static Request createPosRequest(String url, ConcurrentHashMap<String, String> hashMap) {

        FormBody.Builder formBodyBuild = new FormBody.Builder();

        //封装请求参数
        if (hashMap != null && !hashMap.isEmpty()) {
            for (Entry<String, String> entry : hashMap.entrySet()) {
                formBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }

        //构建请求对象
        FormBody formBody = formBodyBuild.build();

        return new Request.Builder().url(url).post(formBody).build();
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static Request createPosBodyRequest(String url, ConcurrentHashMap<String, Object> hashMap) {

        String json = FastJsonUtil.toJSONString(hashMap);
        //封装请求参数
        RequestBody body = RequestBody.create(JSON, json);
        return new Request.Builder().url(url).post(body).build();
    }

    public static Request createPosBodyHtmlRequest(RequestModel requestModel) {

        String json = requestModel.data;
        if (json == null) {
            json = "";
        }
        //封装请求参数
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder lBuilder = new Request.Builder().url(requestModel.url);
        if (requestModel.header != null) {
            Map<String, Object> hashMap = FastJsonUtil.stringToCollect(json);
            if (hashMap != null && !hashMap.isEmpty()) {
                for (Entry<String, Object> lEntry : hashMap.entrySet()) {
                    lBuilder.addHeader(lEntry.getKey(), lEntry.getValue().toString());
                }
            }
        }
        return lBuilder.post(body).build();
    }

    public static Request createGetBodyHtmlRequest(RequestModel requestModel) {

        String json = requestModel.data;
        Map<String, Object> hashMap = FastJsonUtil.stringToCollect(json);
        StringBuilder stringBuilder = new StringBuilder(requestModel.url).append("?");
        //封装请求参数
        if (hashMap != null && !hashMap.isEmpty()) {
            for (Entry<String, Object> entry : hashMap.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        Request.Builder lBuilder = new Request.Builder().url(stringBuilder.substring(0, stringBuilder.length() - 1));
        if (requestModel.header != null) {
            Map<String, Object> headerHashMap = FastJsonUtil.stringToCollect(json);
            if (headerHashMap != null && !headerHashMap.isEmpty()) {
                for (Entry<String, Object> lEntry : headerHashMap.entrySet()) {
                    lBuilder.addHeader(lEntry.getKey(), lEntry.getValue().toString());
                }
            }
        }
        return lBuilder.get().build();
    }

    /**
     * 创建 Get Request对象
     *
     * @return Get Request对象
     */
    public static Request createGetRequest(String url, ConcurrentHashMap<String, String> hashMap) {


        StringBuilder stringBuilder = new StringBuilder(url).append("?");
        //封装请求参数
        if (hashMap != null && !hashMap.isEmpty()) {
            for (Entry<String, String> entry : hashMap.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        return new Request.Builder().url(stringBuilder.substring(0, stringBuilder.length() - 1)).get().build();
    }

    /**
     * 创建 post File Request对象
     *
     * @return post File Request对象
     */
    public static Request createPosFileRequest(String url, ConcurrentHashMap<String, Object> hashMap) {


        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        if (hashMap != null) {
            //追加参数
            for (String key : hashMap.keySet()) {
                Object object = hashMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
                }
            }

        }

        //创建RequestBody
        RequestBody body = builder.build();

        return new Request.Builder().url(url).post(body).build();
    }

    /**
     * 创建 post File Request对象
     * <p>
     * 发送多个文件
     *
     * @return post File Request对象
     */
    public static Request createPosFileListRequest(String url, ConcurrentHashMap<String, Object> keyMap, List<File> imgList) {


        MultipartBody.Builder builder = new MultipartBody.Builder();
        //设置类型
        builder.setType(MultipartBody.FORM);
        if (keyMap != null) {
            //追加参数
            for (String key : keyMap.keySet()) {
                Object object = keyMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
                }
            }
        }


        if (imgList != null) {
            for (File file : imgList) {
                builder.addFormDataPart("img", file.getName(), RequestBody.create(null, file));
            }
        }

        //创建RequestBody
        RequestBody body = builder.build();

        return new Request.Builder().url(url).post(body).build();
    }
}
