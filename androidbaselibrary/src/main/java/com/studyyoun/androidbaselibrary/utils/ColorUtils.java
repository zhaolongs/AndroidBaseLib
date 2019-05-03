package com.studyyoun.androidbaselibrary.utils;
/**
 * Created by zhaolong on 2017/10/1.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

import android.graphics.Color;

import java.util.Random;

/**
 * class infation
 */
public class ColorUtils {
    /**
     * 随机生成漂亮的颜色
     *
     * @return
     */
    public static int randomColor () {
        Random random = new Random();
        //如果值太大，会偏白，太小则会偏黑，所以需要对颜色的值进行范围限定
        int red = random.nextInt(150) + 50;//50-199
        int green = random.nextInt(150) + 50;//50-199
        int blue = random.nextInt(150) + 50;//50-199
        return Color.rgb(red, green, blue);//根据rgb混合生成一种新的颜色
    }
}
