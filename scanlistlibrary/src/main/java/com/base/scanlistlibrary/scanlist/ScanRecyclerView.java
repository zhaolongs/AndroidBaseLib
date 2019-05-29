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
    private AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {


        @Override
        public void onChanged() {
            Adapter<?> adapter =  getAdapter();
            if(adapter != null && mEmptyView != null) {
                if(adapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    ScanRecyclerView.this.setVisibility(View.GONE);
                    TextView emptyTextView = mEmptyView.findViewById(R.id.zl_page_tv_main_empty_view);
                    if (emptyTextView != null) {
                        emptyTextView.setText("暂无数据 点击刷新");
                    }
                }
                else {
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

        if(adapter != null) {
            adapter.registerAdapterDataObserver(mAdapterDataObserver);
        }

        mAdapterDataObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }
}