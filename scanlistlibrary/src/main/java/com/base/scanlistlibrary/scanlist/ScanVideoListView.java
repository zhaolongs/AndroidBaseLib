package com.base.scanlistlibrary.scanlist;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.base.scanlistlibrary.R;
import com.base.scanlistlibrary.base.ScanBaseRecyclerViewAdapter;
import com.base.scanlistlibrary.base.ScanContact;
import com.base.scanlistlibrary.base.ScanRecyclerViewHolder;

import java.util.List;


/**
 * 视频列表view
 *
 * @author zhaolong
 */
public class ScanVideoListView<A> extends FrameLayout {

    private String TAG = ScanVideoListView.this.getClass().getName();
    private Context mContext;
    private ScanRecyclerView mScanRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ScanBaseRecyclerViewAdapter<A> mRecyclerViewAdapter;
    private ScanPagerLayoutManager mScanPagerLayoutManager;
    private View mEmptyView;


    /**
     * 数据是否到达最后一页
     */
    private boolean isEnd;

    //刷新数据listener
    private ScanContact.OnRefreshDataListener onRefreshDataListener;
    private ScanContact.OnPageSelectListener mOnPageSelectListener;
    private RecyclerView.RecyclerListener mRecyclerListener;
    /**
     * 判断是否处于加载数据的状态中
     */
    private boolean isLoadingData;
    /**
     * 预加载位置, 默认离底部还有5条数据时请求下一页视频列表
     */
    private static final int DEFAULT_PRELOAD_NUMBER = 5;
    /**
     * 是否点击暂停
     */
    private boolean isPauseClick = false;
    /**
     * 当前选中位置
     */
    private int mCurrentPosition;
    /**
     * 正常滑动，上一个被暂停的位置
     */
    private int mLastStopPosition = -1;


    public ScanVideoListView(@NonNull Context context, int emptyLayoutId, boolean isPaging) {
        super(context);
        this.mContext = context;
        init(isPaging, emptyLayoutId);
    }


    private void init(boolean isPaging, int emptyLayoutId) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.zl_page_list_main_layout, this, true);
        mScanRecyclerView = view.findViewById(R.id.zl_page_list_main_recycler);
        mSwipeRefreshLayout = view.findViewById(R.id.zl_page_list_main_refresh_view);
        mSwipeRefreshLayout.setColorSchemeColors(Color.YELLOW, Color.GREEN, Color.BLUE, Color.RED);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshDataListener != null) {
                    isLoadingData = true;
                    onRefreshDataListener.onRefresh();
                }
            }
        });
        if (this.mRecyclerListener != null) {
            mScanRecyclerView.setRecyclerListener(this.mRecyclerListener);
        }
        mScanRecyclerView.setHasFixedSize(true);
        mScanPagerLayoutManager = new ScanPagerLayoutManager(mContext, isPaging);
        mScanPagerLayoutManager.setItemPrefetchEnabled(true);
        mScanRecyclerView.setLayoutManager(mScanPagerLayoutManager);
        if (emptyLayoutId <= 0) {
            View emptyView = view.findViewById(R.id.zl_page_list_main_empty_view);
            mEmptyView = emptyView;
            mScanRecyclerView.setEmptyView(emptyView);
        } else {
            View emptyView = View.inflate(mContext, emptyLayoutId, null);
            FrameLayout rootEmptyView = view.findViewById(R.id.zl_page_list_main_empty_rootview);
            rootEmptyView.removeAllViews();
            rootEmptyView.addView(emptyView);
            mEmptyView = emptyView;
            mScanRecyclerView.setEmptyView(emptyView);
        }

        mEmptyView.setOnClickListener(mOnRefreshClickListener);
        mScanPagerLayoutManager.setOnViewPagerListener(new ScanPagerLayoutManager.OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                int position = mScanPagerLayoutManager.findFirstVisibleItemPosition();
                Log.e(TAG, "onInitComplete mCurrentPosition= " + mCurrentPosition + "  position " + position);
                if (position != -1) {
                    mCurrentPosition = position;
                }
                startPlay(mCurrentPosition);
                mLastStopPosition = -1;
                Log.e(TAG, "onInitComplete mCurrentPosition= " + mCurrentPosition);


            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                if (mCurrentPosition == position) {
                    mLastStopPosition = position;
                    stopPlay(position);
                }

            }

            @Override
            public void onPageSelected(int position, boolean b) {
                //重新选中视频不播放，如果该位置被stop过则会重新播放视频
                if (mCurrentPosition == position && mLastStopPosition != position) {
                    return;
                }
                mCurrentPosition = position;
                int itemCount = mRecyclerViewAdapter.getItemCount();
                if (itemCount - position < DEFAULT_PRELOAD_NUMBER && !isLoadingData && !isEnd) {
                    // 正在加载中, 防止网络太慢或其他情况造成重复请求列表
                    isLoadingData = true;
                    loadMore();
                }
                //开始播放选中视频
                startPlay(position);

            }
        });

    }

    private void stopPlay(int position) {
        if (position < 0 || position > list.size()) {
            return;
        }
        if (mOnPageSelectListener != null) {
            mOnPageSelectListener.onPageRelease(position, list.get(position), mRecyclerViewAdapter, (ScanRecyclerViewHolder) mScanRecyclerView.findViewHolderForAdapterPosition(position));
        }
    }

    private void startPlay(int position) {
        if (position < 0 || position > list.size()) {
            return;
        }
        if (mOnPageSelectListener != null) {
            mOnPageSelectListener.onPageSelected(position, list.get(position), mRecyclerViewAdapter, (ScanRecyclerViewHolder) mScanRecyclerView.findViewHolderForAdapterPosition(position));
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        if (onRefreshDataListener != null) {
            onRefreshDataListener.onLoadMore();
        }
    }

    /**
     * 刷新数据
     *
     * @param list 刷新数据
     */
    public void refreshData(List<A> list) {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            // 加载完毕, 重置加载状态
        }
        isEnd = false;
        isLoadingData = false;
        mRecyclerViewAdapter.refreshData(list);
    }

    /**
     * 刷新数据，并播放指定位置的视频
     *
     * @param list     视频列表数据
     * @param position 刷新完成之后播放位置
     */
    public void refreshData(List<A> list, int position) {
        int size = list.size();
        if (position < 0) {
            position = 0;
        }
        if (size <= position) {
            position = size - 1;
        }
        //获取不进行显示
        int notShowVideoCount = 0;
        mCurrentPosition = position - notShowVideoCount;
        refreshData(list);
        mScanRecyclerView.scrollToPosition(mCurrentPosition);

    }


    /**
     * 加载更多数据
     *
     * @param list
     */
    public void addMoreData(List<A> list) {
        isLoadingData = false;
        if (mRecyclerViewAdapter != null) {
            mRecyclerViewAdapter.addMoreData(list);
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    public void setOnRefreshDataListener(ScanContact.OnRefreshDataListener onRefreshDataListener) {
        this.onRefreshDataListener = onRefreshDataListener;
    }

    public void setOnPageSelectListener(ScanContact.OnPageSelectListener onPageSelectListener) {
        mOnPageSelectListener = onPageSelectListener;
    }

    public void setOnRecyclerListener(RecyclerView.RecyclerListener recyclerListener) {
        mRecyclerListener = recyclerListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * Activity生命周期onPause发生时调用,防止切换到后台视频继续播放的问题
     */
    public void onPause() {


    }

    /**
     * Activity生命周期onResume发生时调用,防止切换到后台视频继续播放的问题
     */
    public void onResume() {

    }


    /**
     * 设置adapter
     *
     * @param recyclerViewAdapter
     */
    public void setRecyclerViewAdapter(ScanBaseRecyclerViewAdapter<A> recyclerViewAdapter) {
        this.mRecyclerViewAdapter = recyclerViewAdapter;
        mScanRecyclerView.setAdapter(recyclerViewAdapter);
        this.list = recyclerViewAdapter.getData();
    }

    public void setRefreshColorSchemeColors(int colors) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeColors(colors);
        }
    }

    private List<A> list;

    private OnClickListener mOnRefreshClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mSwipeRefreshLayout != null) {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    if (onRefreshDataListener != null) {
                        startLoading();
                        onRefreshDataListener.onRefresh();
                    }
                }
            }
        }
    };

    public void startLoading() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        if (mEmptyView != null) {
            TextView emptyTextView = mEmptyView.findViewById(R.id.zl_page_tv_main_empty_view);
            if (emptyTextView != null) {
                emptyTextView.setText("加载中...");
            }
        }
    }
}
