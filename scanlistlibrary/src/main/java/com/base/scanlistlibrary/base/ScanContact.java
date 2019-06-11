package com.base.scanlistlibrary.base;

import android.view.View;

import com.base.scanlistlibrary.presenter.ScanPresenter;
import com.base.scanlistlibrary.scanlist.ScanRecyclerView;
import java.util.List;

public interface ScanContact {

    //刷新数据
    public interface OnRefreshDataListener {
        //下拉刷新
        void onRefresh();
        //上拉加载
        void onLoadMore();
    }

    /**
     * item点击监听器
     */
    public interface OnItemClickListener {
        /**
         * item点击回调
         *
         * @param adapter  The Adapter where the click happened.
         * @param view        The view that was clicked.
         * @param position The position of the view in the adapter.
         */
        void onItemClick(ScanBaseRecyclerViewAdapter adapter, View view, int position);
    }
    public interface OnItemLongClickListener {
        /**
         * item点击回调
         *
         * @param adapter  The Adapter where the click happened.
         * @param view        The view that was clicked.
         * @param position The position of the view in the adapter.
         */
        void onItemLongClick(ScanBaseRecyclerViewAdapter adapter, View view, int position);
    }

    public interface OnPageSelectListener<A> {
        void onPageSelected(int position, A bean, ScanBaseRecyclerViewAdapter<A> adapter, ScanRecyclerViewHolder holder);

        void onPageRelease(int position, A a, ScanBaseRecyclerViewAdapter<A> adapter, ScanRecyclerViewHolder holder);
    }
    public interface OnPageSelectScrollListener<A> {
        void onPageRelease(int position, A a, ScanBaseRecyclerViewAdapter<A> adapter, ScanRecyclerViewHolder holder);

        <A> void onPageSelected(List<A> list, ScanBaseRecyclerViewAdapter<A> recyclerViewAdapter, ScanRecyclerView scanRecyclerView);
    }
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

    public interface ScanModel {
        void load(int currentPage, int currentPageSize,ScanPresenter.OnInterNetRequestCallback onInterNetRequestCallback);
    }

}
