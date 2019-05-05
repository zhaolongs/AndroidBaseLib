# AndroidBaseLib
android基础开发包

#### com.studyyoun.androidbaselibrary.manager.AppManager

     Activity 栈管理器 使用时在baseActivity 中 AppManager.addActivity(this); 然后在退出程序时 可使用 AppManager.finishAllActivity();关闭所有的Activity
   
   
#### 新增AES 加密工具类

    ```
            //密钥
            String key = "AD2FF87425CA729C";
            //明文
            String content = "{\"password\":\"123456\",\"username\":\"145\"}";
            //加密
            String lEncrypt =AesUtils.encrypt(key, content);
            //密文
            System.out.println("" + lEncrypt);
            //解密
            String lDecrypt = AesUtils.decrypt(key, lEncrypt);
            //还原明文
            System.out.println(lDecrypt);

    ```