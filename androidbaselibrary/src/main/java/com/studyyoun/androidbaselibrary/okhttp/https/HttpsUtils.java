package com.studyyoun.androidbaselibrary.okhttp.https;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by androidlongs on 17/6/28.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public class HttpsUtils {

    public static SSLSocketFactory getSslFactory () {

        //创建 X509TrustManager 性能管理基类
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted (X509Certificate[] x509Certificates, String s) {

            }

            @Override
            public void checkServerTrusted (X509Certificate[] x509Certificates, String s) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers () {
                return new X509Certificate[0];
            }
        };

        //创建加密上下文
        SSLContext sslContext = null;
        try {
            //与服务器要保持一致的算法
            sslContext = SSLContext.getInstance("SSL");
            X509TrustManager[] x509TrustManagers = new X509TrustManager[] {trustManager};
            sslContext.init(null, x509TrustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        return sslContext.getSocketFactory();
    }
}
