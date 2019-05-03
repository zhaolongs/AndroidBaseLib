package com.studyyoun.androidbaselibrary.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Created by androidlongs on 17/3/11.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public class DropDownSpringbackScrollView extends ScrollView {

    // 拖动的距离 size = 4 的意思 只允许拖动屏幕的1/4
    private static final int size = 4;
    private View mInnerView;
    private float mDownY;
    private Rect mNormalRect = new Rect();

    public DropDownSpringbackScrollView(Context context) {
        super(context);
    }

    public DropDownSpringbackScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }




    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            mInnerView = getChildAt(0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mInnerView == null) {
            return super.onTouchEvent(ev);
        } else {
            commOnTouchEvent(ev);
        }
        return super.onTouchEvent(ev);
    }

    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    // Log.v("mlguitar", "will up and animation");
                    animation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float preY = mDownY;
                float nowY = ev.getY();
                /**
                 * size=4 表示 拖动的距离为屏幕的高度的1/4
                 */
                int deltaY = (int) (preY - nowY) / size;
                // 滚动
                // scrollBy(0, deltaY);

                mDownY= nowY;
                // 当滚动到最上或者最下时就不会再滚动，这时移动布局
                if (isNeedMove()) {
                    if (mNormalRect.isEmpty()) {
                        // 保存正常的布局位置
                        mNormalRect.set(mInnerView.getLeft(), mInnerView.getTop(),
                                mInnerView.getRight(), mInnerView.getBottom());
                        return;
                    }
                    int yy = mInnerView.getTop() - deltaY;

                    // 移动布局
                    mInnerView.layout(mInnerView.getLeft(), yy, mInnerView.getRight(),
                            mInnerView.getBottom() - deltaY);
                }
                break;
            default:
                break;
        }
    }

    // 开启动画移动

    public void animation() {
        // 开启移动动画
        TranslateAnimation ta = new TranslateAnimation(0, 0, mInnerView.getTop(),
                mNormalRect.top);
        ta.setDuration(200);
        mInnerView.startAnimation(ta);
        // 设置回到正常的布局位置
        mInnerView.layout(mNormalRect.left, mNormalRect.top, mNormalRect.right, mNormalRect.bottom);
        mNormalRect.setEmpty();
    }

    // 是否需要开启动画
    public boolean isNeedAnimation() {
        return !mNormalRect.isEmpty();
    }

    // 是否需要移动布局
    public boolean isNeedMove() {
        int offset = mInnerView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        return scrollY == 0 || scrollY == offset;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnCustomScrollChangeLisernet != null) {
            mOnCustomScrollChangeLisernet.onScrollChanged(l,t,oldl,oldt);
        }
    }


    public interface  OnCustomScrollChangeLisernet{
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    private OnCustomScrollChangeLisernet mOnCustomScrollChangeLisernet;

    public void setOnCustomScrollChangeLisernet(OnCustomScrollChangeLisernet lisernet){
        mOnCustomScrollChangeLisernet = lisernet;
    }
}