package com.studyyoun.androidbaselibrary.utils;


/**
 * Created by zhaolong on 2017/9/28.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.studyyoun.androidbaselibrary.R;
import com.studyyoun.androidbaselibrary.view.CustomShareLoadingView;


/**
 * class infation
 */

/**
 * 使用案例  《使进度条显示1200ms》
 * private long mPreLoadingTime =0;
 * <p>
 * <p>
 * //开始
 * <p>
 * CommonLoadingUtils.getInstance().showLoading(mUserHeaderImageView);
 * mPreLoadingTime= System.currentTimeMillis();
 * <p>
 * <p>
 * //结束
 * long current = System.currentTimeMillis();
 * long flagTime = current -mPreLoadingTime;
 * if (flagTime<1200){
 * flagTime = 1200-flagTime;
 * }else {
 * flagTime=0;
 * }
 * mHandler.postDelayed(new Runnable() {
 *
 * @Override public void run() {
 * CommonLoadingUtils.getInstance().closeFunction();
 * }
 * },flagTime);
 */
public class CommonLoadingUtils {

    private static PopupWindow mPopupWindow;
    private CustomShareLoadingView mCustomShareLoadingView;

    private CommonLoadingUtils() {

    }

    private static class SingleCommonLoadingUtils {
        private static CommonLoadingUtils sCommonLoadingUtils = new CommonLoadingUtils();
    }

    public static CommonLoadingUtils getInstance() {
        return SingleCommonLoadingUtils.sCommonLoadingUtils;
    }

    public void showLoading(View view) {

        if (mPopupWindow != null||view==null) {
            return;
        }
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = View.inflate(view.getContext(), R.layout.ab_pop_progress_loading, null);

        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        mPopupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);


        mCustomShareLoadingView = popupWindow_view.findViewById(R.id.cslv_loading);
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
                if (mCustomShareLoadingView != null) {
                    mCustomShareLoadingView.close();
                }
            }
        });
        //显示
        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, 0);


        mCustomShareLoadingView.start();

    }

    public void closeFunction() {
        if (mPopupWindow != null) {
            if (mCustomShareLoadingView != null) {
                mCustomShareLoadingView.close();
            }
            mPopupWindow.dismiss();
            mPopupWindow=null;
        }
    }

}
