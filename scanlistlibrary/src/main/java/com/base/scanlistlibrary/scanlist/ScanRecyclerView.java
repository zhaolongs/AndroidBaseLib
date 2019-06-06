package com.base.scanlistlibrary.scanlist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.base.scanlistlibrary.R;

/**
 * @author zl
 */
public class ScanRecyclerView extends RecyclerView {
    private View mEmptyView;
    private String mEmptyViewStr;
    private String mLoadingStr;
    private int mLoadingStatus = 0;
    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                if (adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    ScanRecyclerView.this.setVisibility(View.GONE);
                    TextView emptyTextView = mEmptyView.findViewById(R.id.zl_page_tv_main_empty_view);
                    if (emptyTextView != null) {
                        if (mLoadingStatus == 0) {
                            if (mLoadingStr == null) {
                                mLoadingStr = "加载中...";
                            }
                            emptyTextView.setText(mLoadingStr);
                        } else {
                            if (mEmptyViewStr == null) {
                                mEmptyViewStr = "暂无数据 点击刷新";
                            }
                            emptyTextView.setText(mEmptyViewStr);
                        }

                    }
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    ScanRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }

        }
    };

    public ScanRecyclerView(Context context) {
        super(context);
    }

    public ScanRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScanRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
        }
        mAdapterDataObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    public void setEmptyViewMsg(String emptyMsg) {
        if (emptyMsg == null) {
            emptyMsg = "暂无数据 点击刷新";
        }
        this.mEmptyViewStr = emptyMsg;
    }

    public void setLoadingStr(String loadingStr) {
        if (loadingStr == null) {
            loadingStr = "加载中...";
        }
        mLoadingStr = loadingStr;
    }

    public void setLoadingStatus(int loadingStatus) {
        mLoadingStatus = loadingStatus;
    }
}