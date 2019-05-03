package com.studyyoun.androidbaselibrary.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.studyyoun.androidbaselibrary.R;
import com.studyyoun.androidbaselibrary.view.CustomShareLoadingView;

public class CommonMsgUtils {


    private static PopupWindow mPopupWindow;
    private CustomShareLoadingView mCustomShareLoadingView;

    private CommonMsgUtils() {

    }

    private PopMsgCallBack mPopMsgCallBack;

    public interface PopMsgCallBack {

        void showFinish(View lView);
    }

    private PopMsgCloseCallBack mPopMsgCloseCallBack;

    public interface PopMsgCloseCallBack {

        void closeFinish(View lView);
    }

    private static class SingleCommonLoadingUtils {
        private static CommonMsgUtils sCommonLoadingUtils = new CommonMsgUtils();
    }

    public static CommonMsgUtils getInstance() {
        return SingleCommonLoadingUtils.sCommonLoadingUtils;
    }

    /**
     * @param prentView
     * @param view            子View
     * @param lPopMsgCallBack 回调设置显示
     */
    public void showPopMsg(View prentView, View view, PopMsgCallBack lPopMsgCallBack) {
        showPopMsg(prentView, view, 1, lPopMsgCallBack);
    }

    public void showPopMsg(View prentView, View view, int showAnimation, PopMsgCallBack lPopMsgCallBack) {
        showPopMsg(prentView, view, showAnimation, lPopMsgCallBack, null);
    }

    public void showPopMsg(View prentView, View view, int showAnimation, PopMsgCallBack lPopMsgCallBack, PopMsgCloseCallBack popMsgCloseCallBack) {

        mPopMsgCallBack = lPopMsgCallBack;
        if (mPopupWindow != null || view == null) {
            mPopupWindow.dismiss();
        }
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        //焦点设置
        mPopupWindow.setFocusable(true);
        //设置点击背景不消失
        mPopupWindow.setOutsideTouchable(true);
        // 设置透明背景
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        //设置动画

        switch (showAnimation) {
            case 0:
                mPopupWindow.setAnimationStyle(R.style.popwin_from_center_anim_style);
                break;
            case 1:
                mPopupWindow.setAnimationStyle(R.style.popwin_from_bottom_anim_style);
                break;
            default:
                mPopupWindow.setAnimationStyle(R.style.popwin_from_center_anim_style);
                break;
        }


        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        //显示
        mPopupWindow.showAtLocation(prentView, Gravity.NO_GRAVITY, 0, 0);

        if (mPopMsgCallBack != null) {
            mPopMsgCallBack.showFinish(view);
        }

    }

    public void showPopDownMsg(View prentView, View view, PopMsgCallBack lPopMsgCallBack) {

        mPopMsgCallBack = lPopMsgCallBack;
        if (mPopupWindow != null || view == null) {
            mPopupWindow.dismiss();
        }
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        //焦点设置
        mPopupWindow.setFocusable(true);
        //设置点击背景不消失
        mPopupWindow.setOutsideTouchable(true);
        // 设置透明背景
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        //设置动画

        mPopupWindow.setAnimationStyle(R.style.popwin_from_center_anim_style);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        //显示
        mPopupWindow.showAsDropDown(prentView, Gravity.NO_GRAVITY, 0, 0);

        if (mPopMsgCallBack != null) {
            mPopMsgCallBack.showFinish(view);
        }

    }

    public void closeFunction() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

}
