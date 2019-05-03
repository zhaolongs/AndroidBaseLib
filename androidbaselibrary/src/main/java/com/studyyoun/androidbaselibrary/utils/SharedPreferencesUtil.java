package com.studyyoun.androidbaselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by androidlongs on 16/12/18.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public class SharedPreferencesUtil {
    private static String scacheFileName = "wisdom_android";

    private SharedPreferences mSharedPreferences;
    private static SharedPreferencesUtil sSSharedPreferencesUtil;

    private SharedPreferencesUtil (Context context) {
        initFunction(context);
    }

    private void initFunction (Context context) {
        mSharedPreferences = context.getSharedPreferences(scacheFileName,
                Activity.MODE_PRIVATE);

    }


    public static SharedPreferencesUtil getInstance (Context context) {
        if (sSSharedPreferencesUtil == null) {
            sSSharedPreferencesUtil = new SharedPreferencesUtil(context);
        }

        return sSSharedPreferencesUtil;
    }


    public void saveData (String name, String value) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
            //实例化SharedPreferences.Editor对象
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            //用putString的方法保存数据
            editor.putString(name, value);
            //提交当前数据
            editor.apply();
        }
    }

    public String getData (String name) {
        if (!TextUtils.isEmpty(name)) {
            return mSharedPreferences.getString(name, "");
        }
        return null;
    }

    public void removeData (String name) {
        if (!TextUtils.isEmpty(name)) {
            mSharedPreferences.edit().remove(name).commit();
        }
    }


}
