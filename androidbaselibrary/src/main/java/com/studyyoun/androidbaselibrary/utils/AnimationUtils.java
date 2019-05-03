package com.studyyoun.androidbaselibrary.utils;
/**
 * Created by zhaolong on 2017/10/4.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * class infation
 */
public class AnimationUtils {
    /**
     * 从屏幕左边 进入的动画
     *
     * @param view
     * @param animationTime
     * @param offsetTime
     */
    public static void initLeftToRightAnimationFunction(final View view, long animationTime, long offsetTime) {
        // 从屏幕左边 进入的动画
        TranslateAnimation fromLeftToRightAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        fromLeftToRightAnimation.setDuration(animationTime);
        fromLeftToRightAnimation.setStartOffset(offsetTime);
        fromLeftToRightAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(fromLeftToRightAnimation);
    }

    /**
     * 从屏幕左边 进入的动画
     *
     * @param view
     * @param animationTime
     * @param offsetTime
     */
    public static void initRightToLeftAnimationFunction(final View view, long animationTime, long offsetTime) {
        // 从屏幕左边 进入的动画
        TranslateAnimation fromLeftToRightAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        fromLeftToRightAnimation.setDuration(animationTime);
        fromLeftToRightAnimation.setStartOffset(offsetTime);
        fromLeftToRightAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(fromLeftToRightAnimation);
    }

    public static void initAlphaAnimation(final View view, long animationTime, long offsetTime) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1f);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        alphaAnimation.setDuration(animationTime);
        alphaAnimation.setStartOffset(offsetTime);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(alphaAnimation);
    }
}
