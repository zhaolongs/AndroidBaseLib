package com.studyyoun.androidbaselibrary.base;

import android.app.Application;
import android.content.Context;

import com.studyyoun.androidbaselibrary.activity.ViewManager;
import com.studyyoun.androidbaselibrary.utils.ToastUtils;

public class BaseApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ViewManager.init(this);
        ToastUtils.getInstance().initToast(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public static Context getInstance() {
        return mContext;
    }
}
