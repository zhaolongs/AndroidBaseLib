package com.studyyoun.androidbaselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by androidlongs on 17/7/12.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 * <p>
 * <p>
 * <p>
 * 软键盘操作工具类
 */

public class SoftInputUtils {

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * EditText获取焦点并隐藏软键盘
     */
    public static void hideSoftInputFromWindow(EditText editText) {


        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            //editText.requestFocus();
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public static void hideSoftInputFromWindow(EditText editText, Activity activity) {


        boolean keyboardShown = isKeyboardShown(activity);

        LogUtils.d("隐藏软件盘 "+keyboardShown);
//        if (keyboardShown) {
//            editText.setFocusable(false);
//            editText.setFocusableInTouchMode(false);
//            InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//
//        }

         /*隐藏软键盘*/
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isActive()){
            inputMethodManager.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
        }

    }

    private static boolean isKeyboardShown(Activity activity) {
        final View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

}
