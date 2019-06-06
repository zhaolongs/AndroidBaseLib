package com.base.scanlistlibrary.scanlist;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.base.scanlistlibrary.base.ScanContact;

/**
 * ViewPager效果的LayoutManager
 *
 * @author zhaolong
 */
public class StagerPagerLayoutManager extends StaggeredGridLayoutManager {

    private PagerSnapHelper mPagerSnapHelper;
    private ScanContact.OnViewPagerListener mOnViewPagerListener;

    /**
     * 移动方向
     */
    private int direction;
    private int[] lastPositions;

    public StagerPagerLayoutManager(Context context, int colum, boolean isPaging) {
        super(colum, StaggeredGridLayoutManager.VERTICAL);
        init(isPaging);
    }

    private void init(boolean isPaging) {
        if (isPaging) {
            mPagerSnapHelper = new PagerSnapHelper();
        }

    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
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
        //防止第一行到顶部有空白区域
        this.invalidateSpanAssignments();
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:

                if (lastPositions == null) {
                    lastPositions = new int[this.getSpanCount()];
                }
                this.findLastVisibleItemPositions(lastPositions);
                int lastVisibleItemPosition = findMax(lastPositions);
                if (mPagerSnapHelper != null) {
                    View viewIdle = mPagerSnapHelper.findSnapView(this);
                    if (viewIdle == null) {
                        return;
                    }
                    if (mOnViewPagerListener != null && getChildCount() == 1) {
                        mOnViewPagerListener.onPageSelected(lastVisibleItemPosition, lastVisibleItemPosition == getItemCount() - 1);
                    }
                } else {
                    int lItemCount = getItemCount();
                    int falg = lItemCount - 2;
                    if (mOnViewPagerListener != null && lastVisibleItemPosition >= falg  ) {
                        mOnViewPagerListener.onPageSelected(lastVisibleItemPosition, lastVisibleItemPosition == getItemCount() - 1);
                    }
                }

                break;
            default:
                break;
        }
    }

    /**
     * 取数组中最大值
     *
     * @param lastPositions
     * @return
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    private int findMin(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value < max) {
                max = value;
            }
        }

        return max;
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
    public void setOnViewPagerListener(ScanContact.OnViewPagerListener listener) {
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

    public int findLastVisibleItemPosition() {
        if (lastPositions == null) {
            lastPositions = new int[this.getSpanCount()];
        }
        this.findLastVisibleItemPositions(lastPositions);
        return findMax(lastPositions);
    }
}
