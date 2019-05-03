package com.studyyoun.androidbaselibrary;

import com.studyyoun.androidbaselibrary.okhttp.callback.OkhttpNormalCallBack;
import com.studyyoun.androidbaselibrary.okhttp.client.OkhttpCommonClient;
import com.studyyoun.androidbaselibrary.utils.LogUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testRequest() {
        String json="{\"url\": \"http://47.96.40.202:8082//50405\",\"method\": \"get\",\"data\": {\"id\":99}}";
        OkhttpCommonClient.httpRequest(json, new OkhttpNormalCallBack() {
            @Override
            public void onSuccess(Object obj) {
                LogUtils.d("请求完成");
            }

            @Override
            public void onFaile(Object obj) {
            LogUtils.e("请求失败");
            }
        });
    }
}