package com.studyyoun.androidbaselibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by androidlongs on 17/5/27.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public class ObservableScrollView extends ScrollView {
    private final int mTouchSlop;
    private OnScollChangedListener onScollChangedListener = null;
    private int downX;
    private int downY;

    public ObservableScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public ObservableScrollView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setOnScollChangedListener(OnScollChangedListener onScollChangedListener) {
        this.onScollChangedListener = onScollChangedListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onScollChangedListener != null) {
            onScollChangedListener.onScrollChanged(this, x, y, oldx, oldy);
        }
        Log.e("scrollview "," scrollview  is onScrollChanged y is  "+y+"  pre y is "+oldy);
    }


    public interface OnScollChangedListener {

        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

    }
    public interface OnScollStateListener {

        void onScrollStateChanged(String up);

    }

    private OnScollStateListener mOnScollStateListener;

    public void setOnScollStateListener (OnScollStateListener onScollStateListener) {
        mOnScollStateListener = onScollStateListener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                 downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent (MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {

            if (mOnScollStateListener != null) {
                mOnScollStateListener.onScrollStateChanged("up");
            }

            int scrollY = this.getScrollY();
            int height = this.getHeight();


            Log.e("scrollview ", "scrollview  is action up   and "+scrollY+"  "+height);

        }else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e("scrollview "," scrollview  is action down ");
            if (mOnScollStateListener != null) {
                mOnScollStateListener.onScrollStateChanged("down");
            }
        }else{
            Log.e("scrollview "," scrollview  is action move ");
            if (mOnScollStateListener != null) {
                mOnScollStateListener.onScrollStateChanged("move");
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean isVerticalScrollBarEnabled () {
        return false;
    }
}