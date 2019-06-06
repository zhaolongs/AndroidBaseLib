package com.studyyoun.androidbaselibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

/**
 * 动态设置 点击事件 selector 的工具类  可以从本地添加  也可以从网络添加
 * <p>
 * //设置是否按压状态，一般在true时设置该属性，表示已按压状态，默认为false
 * android:state_pressed
 * //设置是否选中状态，true表示已选中，false表示未选中
 * android:state_selected
 * //设置是否勾选状态，主要用于CheckBox和RadioButton，true表示已被勾选，false表示未被勾选
 * android:state_checked
 * //设置勾选是否可用状态，类似state_enabled，只是state_enabled会影响触摸或点击事件，state_checkable影响勾选事件
 * android:state_checkable
 * //设置是否获得焦点状态，true表示获得焦点，默认为false，表示未获得焦点
 * android:state_focused
 * //设置触摸或点击事件是否可用状态，一般只在false时设置该属性，表示不可用状态
 * android:state_enabled
 * <p>
 * <p>
 * //设置当前窗口是否获得焦点状态，true表示获得焦点,false 表示未获得焦点，例如拉下通知栏或弹出对话框时， 当前界面就会失去焦点；另外，ListView的ListItem获得焦点时也会触发true状态，可以理解为当前窗口就是ListItem本身
 * android:state_window_focused
 * //设置是否被激活状态，true表示被激活，false表示未激活，API Level 11及以上才支持，可通过代码调用控件的
 * android:state_activated
 * //方法设置是否激活该控件
 * setActivated(boolean)
 * //设置是否鼠标在上面滑动的状态**，true表示鼠标在上面滑动，默认为false，API Level 14及以上才支持
 * //补充：selector标签下有两个比较有用的属性要说一下，添加了下面两个属性之后，则会在状态改变时出现淡入淡出效果，
 * //但必须在API Level 11及以上才支持
 * android:state_hovered
 * //状态改变时，旧状态消失时的淡出时间，以毫秒为单位
 * android:exitFadeDuration
 * //状态改变时，新状态展示时的淡入时间，以毫秒为单位
 * android:enterFadeDuration
 */

public class SelectorUtil {

    public static void addSelector(Context context, int normalColor, int presseColor, View view) {
        addSelector(context, normalColor, presseColor, view, 4);
    }

    public static void addSelector(Context context, int normalColor, int presseColor, View view, float radius) {

        Drawable normal = generateDrawable(context, normalColor, radius);
        Drawable pressed = generatePressDrawable(context, presseColor, radius);
        view.setBackgroundDrawable(generateSelector(pressed, normal));
    }
    public static void addSelectorFormDrawable(Context context, int normalId, int presseId, View view) {
        Drawable normal = context.getResources().getDrawable(normalId);
        Drawable pressed = context.getResources().getDrawable(presseId);
        view.setBackgroundDrawable(generateSelector(pressed, normal));
    }

    public static StateListDrawable generateSelector(Drawable pressed, Drawable normal) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressed);//设置按下的图片
        drawable.addState(new int[]{}, normal);//设置默认的图片
        return drawable;
    }

    public static GradientDrawable generateDrawable(Context context, int argb, float radius) {
        float displayMetrics = context.getResources().getDisplayMetrics().scaledDensity;
        radius = radius * displayMetrics;
        float mScaledDensity = displayMetrics;
        int den = (int) (0.5 * mScaledDensity);
        if (den == 0) {
            den = 1;
        }
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);//设置为矩形，默认就是矩形
        drawable.setStroke(den, argb);
        drawable.setCornerRadius(radius);//设置圆角的半径
        drawable.setColor(argb);
        return drawable;
    }

    public static GradientDrawable generatePressDrawable(Context context, int argb, float radius) {
        float displayMetrics = context.getResources().getDisplayMetrics().scaledDensity;
        radius = radius * displayMetrics;
        float mScaledDensity = displayMetrics;
        int den = (int) (0.5 * mScaledDensity);
        if (den == 0) {
            den = 1;
        }
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);//设置为矩形，默认就是矩形
        drawable.setStroke(den, argb);
        drawable.setCornerRadius(radius);//设置圆角的半径
        drawable.setColor(argb);
        return drawable;
    }


}