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
    
#### 新增webview 常用初时化工具
 
    ```java
        使用  WebViewFunction.getInstance().initWebViewSettings(mWebView, mContext);
    ```
    
    
#### 新增 自定义像机modlue 

```java

    private CameraUtils mCameraUtils;
    
    //初始化回调
    mCameraUtils = CameraUtils.getInstance().setContinuous(true).initCamerView(this, rootView, lWidthPixels, lHeightPixels, new CameraCallBack() {
                @Override
                public void cameraFaile(int errCode, String message) {
                    LogUtils.e("errCode " + errCode + "  message " + message);
                }
    
                @Override
                public void cameraSuccess(String mFilePath) {
                    LogUtils.d("success  " + mFilePath);
                    //显示图片
                    showImageFunction(mFilePath);
                }
    
                @Override
                public void cameraPermisExit() {
                    //无权限，应退出页面
                    TestCameraActivity.this.finish();
                }
            });
    
    
    //点击拍照
    CameraUtils.getInstance().onCameraClick();
```


#### LoadingDialogUtils 加载等待提示框

    ```
    //显示提示框
    LoadingDialogUtils.getInstance().show();
    
    //隐藏提示框
    LoadingDialogUtils.getInstance().dismiss();
    ```