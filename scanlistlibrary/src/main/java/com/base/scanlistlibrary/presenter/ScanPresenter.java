package com.base.scanlistlibrary.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.scanlistlibrary.R;
import com.base.scanlistlibrary.base.ScanBaseRecyclerViewAdapter;
import com.base.scanlistlibrary.base.ScanContact;
import com.base.scanlistlibrary.base.ScanRecyclerViewHolder;
import com.base.scanlistlibrary.scanlist.ScanVideoPlayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScanPresenter<T> {

    private String LOGTAG = ScanPresenter.class.getName();
    private Context mContext;
    private ScanVideoPlayView mScanVideoPlayView;
    private OnBindDataCallback mOnBindDataCallback;
    private int mCurrentPage = 1;
    private int mCurrentPageSize = 50;
    private ScanContact.ScanModel mScanModel;
    private boolean mIsLoading;
    private int mStartIndex;

    public void loading(Context context, ViewGroup viewGroup, int itemLayoutId, String url, Class<?> clas, OnBindDataCallback bindDataCallback) {
        loading(context, viewGroup, null, itemLayoutId, mCurrentPage, mCurrentPageSize, 0, false, bindDataCallback);
    }

    public void loading(Context context, ViewGroup viewGroup, int itemLayoutId, String url, HashMap<String, Object> keyParameter, Class<?> clas, OnBindDataCallback bindDataCallback) {
        loading(context, viewGroup, null, itemLayoutId, mCurrentPage, mCurrentPageSize, 0,  false, bindDataCallback);
    }

    public void loading(Context context, ViewGroup viewGroup, View emptyView, int itemLayoutId, String url, HashMap<String, Object> keyParameter, Class<?> clas, OnBindDataCallback bindDataCallback) {
        loading(context, viewGroup, emptyView, itemLayoutId, mCurrentPage, mCurrentPageSize, 0,false, bindDataCallback);
    }

    public void loading(Context context, ViewGroup viewGroup, View emptyView, int itemLayoutId, String url, HashMap<String, Object> keyParameter, Class<?> clas, boolean isPaging, OnBindDataCallback bindDataCallback) {
        loading(context, viewGroup, emptyView, itemLayoutId, mCurrentPage, mCurrentPageSize, 0, isPaging, bindDataCallback);
    }

    /**
     * @param context
     * @param viewGroup        列表数据的父布局
     * @param emptyView        无数据时的占位布局
     * @param itemLayoutId     列表适配器的布局
     * @param currentPage      分页加载 当前页数
     * @param currentPageSize  分页加载 每页加载的数量
     * @param startIndex       列表默认滑动到的位置
     * @param isPaging         是否启用单屏切换功能
     * @param bindDataCallback 设置数据的回调
     */
    public void loading(Context context, ViewGroup viewGroup, View emptyView, int itemLayoutId, int currentPage, int currentPageSize, int startIndex, boolean isPaging, OnBindDataCallback bindDataCallback) {


        if (context == null) {
            throw new RuntimeException(" context is null ");
        } else if (viewGroup == null) {
            throw new RuntimeException(" parent viewGroup is null ");
        } else if (itemLayoutId <= 0) {
            throw new RuntimeException(" itemLayoutId is <0 ");
        } else if (bindDataCallback == null) {
            throw new RuntimeException(" bindDataCallback is null");
        }
        this.mStartIndex = startIndex;
        this.mContext = context;
        this.mCurrentPage = currentPage;
        this.mCurrentPageSize = currentPageSize;
        this.mOnBindDataCallback = bindDataCallback;
        View lView = View.inflate(context, R.layout.zl_scan_presenter_list, null);
        mScanVideoPlayView = lView.findViewById(R.id.svpv_list);
        try {
            mScanVideoPlayView.setOnRefreshDataListener(mOnRefreshDataListener);
        } catch (Exception e) {
            e.printStackTrace();
            bindDataCallback.onFaile(2, "mOnRefreshDataListener :" + e.getMessage());
        }
        viewGroup.addView(lView);
        List<T> refreshList = new ArrayList<>();
        PicPageScanAdapter lPicPageScanAdapter = new PicPageScanAdapter(context, refreshList, itemLayoutId, bindDataCallback);
        mScanVideoPlayView.initPlayListView(lPicPageScanAdapter, 0, isPaging);
        mScanVideoPlayView.startLoading(true);
        loadDataFunction();

    }

    private ScanContact.OnRefreshDataListener mOnRefreshDataListener = new ScanContact.OnRefreshDataListener() {
        @Override
        public void onRefresh() {
            //下拉刷新
            refreshDataFunction();
        }

        @Override
        public void onLoadMore() {
            //上拉加载更多
            loadMoreDataFunction();
        }
    };


    private class PicPageScanAdapter extends ScanBaseRecyclerViewAdapter<T> {
        private OnBindDataCallback mOnBindDataCallback;

        public PicPageScanAdapter(Context context, List<T> data, int layoutId, OnBindDataCallback bindDataCallback) {
            super(context, data, layoutId);
            this.mOnBindDataCallback = bindDataCallback;
        }

        @Override
        protected void onBindData(ScanRecyclerViewHolder holder, T bean, int position) {
            if (this.mOnBindDataCallback != null) {
                this.mOnBindDataCallback.onBindData(holder, bean, position);
            }
        }
    }

    private OnInterNetRequestCallback mOnInterNetRequestCallback = new OnInterNetRequestCallback() {
        @Override
        public void onSuccess(Object object) {
            mIsLoading = false;
            List<T> lTList = (List<T>) object;
            if (mCurrentPage == 1) {
                mScanVideoPlayView.refreshVideoList(lTList,mStartIndex);
            } else {
                mScanVideoPlayView.addMoreData(lTList);
            }
        }

        @Override
        public void onFaile(int code, String msg) {
            mIsLoading = false;
            if (mCurrentPage == 1) {
                List<T> lTList = new ArrayList<>();
                mScanVideoPlayView.refreshVideoList(lTList);
            } else {
                mCurrentPage -= 1;
            }

        }
    };

    //加载更多
    private void loadMoreDataFunction() {
        mCurrentPage += 1;
        loadDataFunction();
    }

    //下拉刷新
    private void refreshDataFunction() {
        mCurrentPage = 1;
        loadDataFunction();
    }

    private void loadDataFunction() {
        try {
            if (!mIsLoading) {
                mIsLoading = true;
                if (mScanModel != null  && mOnInterNetRequestCallback != null) {
                    mScanModel.load(mCurrentPage, mCurrentPageSize,mOnInterNetRequestCallback);
                } else {
                    mIsLoading = false;
                    Log.e(LOGTAG, " model is null");
                    throw new RuntimeException(" model is null");
                }
            } else {
                Log.e(LOGTAG, " data request net is Loading");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mIsLoading = false;
            mOnBindDataCallback.onFaile(1, "loadDataFunction() " + e.getMessage());
        }
    }

    public void setScanModel(ScanContact.ScanModel scanModel) {
        mScanModel = scanModel;
    }

    public int getCurrentPostion(){
        return mScanVideoPlayView.getCurrentPostion();
    }
    public interface OnBindDataCallback<T> {
        void onBindData(ScanRecyclerViewHolder holder, T bean, int position);

        void onFaile(int i, String message);
    }


    public interface OnInterNetRequestCallback<T> {

        void onSuccess(Object object);

        void onFaile(int code, String msg);
    }
}
