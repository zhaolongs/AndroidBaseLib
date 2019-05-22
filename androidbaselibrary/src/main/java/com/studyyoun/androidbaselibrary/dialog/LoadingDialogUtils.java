package com.studyyoun.androidbaselibrary.dialog;

import android.content.Context;

public class LoadingDialogUtils {
    private static volatile LoadingDialogUtils instance;
    private LoadingDialog mInfoDialog;
    private LoadingDialog.Builder mBuilder;

    private LoadingDialogUtils() {
    }

    public static LoadingDialogUtils getInstance() {
        if (instance == null) {
            synchronized (LoadingDialogUtils.class) {
                if (instance == null) {
                    instance = new LoadingDialogUtils();
                }
            }
        }
        return instance;
    }


    public void show(Context mContext) {
        mBuilder = new LoadingDialog.Builder(mContext);
        mInfoDialog = mBuilder.create();
        mInfoDialog.show();
    }

    public void dismiss() {
        mBuilder.dismiss();
    }
}
