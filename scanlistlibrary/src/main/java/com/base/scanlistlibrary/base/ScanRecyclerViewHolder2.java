package com.base.scanlistlibrary.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * RecyclerView ViewHolder基类
 * <p>
 * 使用 {@link #mViews} 对ItemView的子view进行存储，同时使用 {@link #getView(int)} 方法进行ItemView
 * 中的子View的获取。获取方式是：如果mViews中存在则直接使用，不存在则从ItemView中find。
 * <p>
 * Created by zhaolong on 2018/5/30.
 */

public class ScanRecyclerViewHolder2 extends ScanRecyclerViewHolder {

    ScanRecyclerViewHolder2(View itemView) {
        super(itemView);
       init(itemView,200,100);
    }
    ScanRecyclerViewHolder2(View itemView,int height,int random) {
        super(itemView);
        init(itemView,height,random);
    }

    private void init(View itemView,int height,int random) {
        float lScaledDensity = itemView.getContext().getResources().getDisplayMetrics().scaledDensity;
        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        lp.height = (int) (height*lScaledDensity + Math.random() * random*lScaledDensity);
        itemView.setLayoutParams(lp);
    }
}
