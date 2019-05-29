package com.base.scanlistlibrary.base;

import com.base.scanlistlibrary.presenter.ScanPresenter;

import java.util.HashMap;

public interface ScanContact {

    //刷新数据
    public interface OnRefreshDataListener {
        //下拉刷新
        void onRefresh();
        //上拉加载
        void onLoadMore();
    }

    public interface OnPageSelectListener<A> {
        void onPageSelected(int position, A bean, ScanBaseRecyclerViewAdapter<A> adapter, ScanRecyclerViewHolder holder);

        void onPageRelease(int position, A a, ScanBaseRecyclerViewAdapter<A> adapter, ScanRecyclerViewHolder holder);
    }

    public interface ScanModel {
        void load(String urlString, int currentPage, int currentPageSize, HashMap<String, Object> keyParameter, Class tClass, ScanPresenter.OnInterNetRequestCallback onInterNetRequestCallback);
    }

}
