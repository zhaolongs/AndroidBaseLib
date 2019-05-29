package com.base.scanlistlibrary.base;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView adapter基类
 */

public abstract class ScanBaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<ScanRecyclerViewHolder> implements View.OnClickListener {

    private Context mContext;
    protected List<T> mData;
    private int mLayoutId;

    private OnItemClickListener mListener;
    private Point mScreenPoint = new Point();
    public ScanBaseRecyclerViewAdapter(Context context, List<T> data, int layoutId) {
        this.mContext = context;
        this.mData = data;
        this.mLayoutId = layoutId;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenPoint.x = displayMetrics.widthPixels;
        mScreenPoint.y = displayMetrics.heightPixels;
    }

    @Override
    public ScanRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        view.setOnClickListener(this);
        return new ScanRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScanRecyclerViewHolder holder, int position) {
        holder.itemView.setTag(position);
        T model = mData.get(position);
        onBindData(holder, model, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(this, v, (Integer) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    /**
     * 数据绑定，由实现类实现
     *
     * @param holder   The reference of the all view within the item.
     * @param bean     The data bean related to the position.
     * @param position The position to bind data.
     */
    protected abstract void onBindData(ScanRecyclerViewHolder holder, T bean, int position);

    /**
     * item点击监听器
     */
    public interface OnItemClickListener {
        /**
         * item点击回调
         *
         * @param adapter  The Adapter where the click happened.
         * @param v        The view that was clicked.
         * @param position The position of the view in the adapter.
         */
        void onItemClick(RecyclerView.Adapter adapter, View v, int position);
    }

    /**
     * 刷新数据
     *
     * @param list
     */
    public void refreshData(List<T> list) {
        this.mData.clear();
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param list
     */
    public void addMoreData(List<T> list) {
        this.mData.addAll(list);
        notifyItemRangeInserted(this.mData.size() - list.size(), list.size());
    }

    public List<T> getData() {
        return mData;
    }
}
