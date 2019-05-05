package com.studyyoun.androidbaselibrary.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.studyyoun.androidbaselibrary.utils.LogUtils;

public class WebViewFunction {
    private static final String APP_CACAHE_DIRNAME = "/wisdom/webcache";
    private WebView mWebView;

    public static WebViewFunction getInstance(){
        return new WebViewFunction();
    }

    public void initWebViewSettings(WebView webView, Context context) {
        mWebView = webView;
        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //支持插件
        webSettings.setPluginState(WebSettings.PluginState.ON);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(false); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //设置缓冲大小，我设的是8M
        webSettings.setAppCacheMaxSize(1024 * 1024 * 105);

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式

        // 开启 DOM storage API 功能

        webSettings.setDomStorageEnabled(true);


        //开启 database storage API 功能

        webSettings.setDatabaseEnabled(true);

        String cacheDirPath = context.getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;

//      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;


        //设置数据库缓存路径

        webSettings.setDatabasePath(cacheDirPath);

        //设置  Application Caches 缓存目录

        webSettings.setAppCachePath(cacheDirPath);

        //开启 Application Caches 功能

        webSettings.setAppCacheEnabled(true);

        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setBlockNetworkImage(false);
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.addJavascriptInterface(new ReadFinishClass(), "controll");
        //获取网页对话框
        webView.setWebChromeClient(mWebChromeClient);

        webView.setWebViewClient(new CustomWebViewClient());
    }

    private Handler mHandler = new Handler(Looper.myLooper());

    public class ReadFinishClass {
        /**
         * 页面加载成功
         */
        @JavascriptInterface
        public void pageFinish(final float height) {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    LogUtils.d("设置 webview 大小 ");
                    mWebView.measure(0, 0);
                    // mArtWebView.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height*getResources().getDisplayMetrics().density)));
                }
            });
        }

    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            //构建一个来显示网页中的对话框
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            LogUtils.d("console message is " + consoleMessage.message() + "\n\t\t from line " + consoleMessage.lineNumber() + "\n\t\t of"
                    + consoleMessage.sourceId());
            return true;
        }

        //加载进度
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        //扩充缓存的容量
        //webview可以设置一个WebChromeClient对象，在其onReachedMaxAppCacheSize函数对扩充缓冲做出响应
        @Override
        public void onReachedMaxAppCacheSize(long spaceNeeded,
                                             long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {

        }

        /**
         * 当WebView加载之后，返回 HTML 页面的标题 Title
         * @param view
         * @param title
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {


        }
    };


    class CustomWebViewClient extends WebViewClient {
        private View mErrorView;
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mWebView.loadUrl(url);
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            LogUtils.e("webview  shouldInterceptRequest");
            return super.shouldInterceptRequest(view, request);

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            LogUtils.e("webview  shouldInterceptRequest  url is " + url);
            return super.shouldInterceptRequest(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogUtils.d("web view loading start ");


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


            LogUtils.d("web view loading finish " + url);

            mWebView.getSettings().setBlockNetworkImage(false);
            // mMainWebView.loadUrl("javascript:readBookDesPageFinish(" + mArticleModel.id + ")");
            mWebView.loadUrl("javascript:controll.pageFinish(document.body.getBoundingClientRect().height)");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (error != null) {
                LogUtils.e("web view loading err  " + error.toString());
            }
        }
    }
}
