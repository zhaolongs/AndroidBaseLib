package com.base.scanlistlibrary.scanlist;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ViewPager效果的LayoutManager
 *
 * @author zhaolong
 */
public class ScanPagerLayoutManager extends LinearLayoutManager {

    private PagerSnapHelper mPagerSnapHelper;
    private OnViewPagerListener mOnViewPagerListener;

    /**
     * 移动方向
     */
    private int direction;

    public ScanPagerLayoutManager(Context context, boolean isPaging) {
        super(context);
        init(isPaging);
    }

    private void init(boolean isPaging) {
        if (isPaging) {
            mPagerSnapHelper = new PagerSnapHelper();
        }

    }

    @Override
    public void onAttachedToWindow(RecyclerView recyclerView) {
        super.onAttachedToWindow(recyclerView);
        if (mPagerSnapHelper != null) {
            mPagerSnapHelper.attachToRecyclerView(recyclerView);
        }

        recyclerView.addOnChildAttachStateChangeListener(mChildAttachStateChangeListener);
    }

    /**
     * 滑动状态的改变
     *
     * @param state 滑动状态
     */
    @Override
    public void onScrollStateChanged(int state) {
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:

                int positionIdle = findFirstCompletelyVisibleItemPosition();

                if (mPagerSnapHelper != null) {
                    View viewIdle = mPagerSnapHelper.findSnapView(this);
                    if (viewIdle == null) {
                        return;
                    }
                    positionIdle = getPosition(viewIdle);
                    if (mOnViewPagerListener != null && getChildCount() == 1) {
                        mOnViewPagerListener.onPageSelected(positionIdle, positionIdle == getItemCount() - 1);
                    }
                } else {
                    if (mOnViewPagerListener != null && findLastVisibleItemPosition() >= getItemCount() - 2) {
                        mOnViewPagerListener.onPageSelected(positionIdle, positionIdle == getItemCount() - 1);
                    }
                }

                break;
            default:
                break;
        }
    }

    /**
     * 监听竖直方向的相对偏移量
     *
     * @param dy       y方向位移
     * @param recycler recyclerView
     * @param state    滑动状态
     * @return
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.direction = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    /**
     * 监听水平方向的相对偏移量
     *
     * @param dx
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.direction = dx;
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setOnViewPagerListener(OnViewPagerListener listener) {
        this.mOnViewPagerListener = listener;
    }

    private RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            if (mOnViewPagerListener != null && getChildCount() == 1) {
                mOnViewPagerListener.onInitComplete();
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {
            if (direction >= 0) {
                if (mOnViewPagerListener != null) {
                    mOnViewPagerListener.onPageRelease(true, getPosition(view));
                }
            } else {
                if (mOnViewPagerListener != null) {
                    mOnViewPagerListener.onPageRelease(false, getPosition(view));
                }
            }

        }
    };

    public interface OnViewPagerListener {
        /**
         * 初始化完成
         */
        void onInitComplete();

        /**
         * 页面不可见, 释放
         *
         * @param isNext   是否有下一个
         * @param position 下标
         */
        void onPageRelease(boolean isNext, int position);

        /**
         * 选中的index
         *
         * @param position 下标
         * @param b        是否到底部
         */
        void onPageSelected(int position, boolean b);
    }
}
