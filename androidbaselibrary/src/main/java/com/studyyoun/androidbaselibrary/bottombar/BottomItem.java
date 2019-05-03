package com.studyyoun.androidbaselibrary.bottombar;
/**
 * Created by zhaolong on 2017/10/23.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

import android.support.annotation.DrawableRes;

/**
 * class infation
 */
public class BottomItem {
    //图标资源
    public int drawableRes;
    //选择中的图标
    public int selectDrawableRes;
    //标题
    public String title;
    //图标的宽度
    public int width;
    //选中的颜色
    public int selectColor;
    public BottomItem(@DrawableRes int drawableRes,@DrawableRes int selectDrawableRes, String title,int width,int itemSelectColor){
        this.drawableRes=drawableRes;
        this.title=title;
        this.width = width;
        this.selectColor =itemSelectColor;
        this.selectDrawableRes=selectDrawableRes;
    }
}
