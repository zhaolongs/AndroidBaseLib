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

public abstract class ScanBaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<ScanRecyclerViewHolder> implements View.OnClickListener,View.OnLongClickListener {

    protected Context mContext;
    protected List<T> mData;
    private int mLayoutId;
    private int mHolderType;
    private int mItemHeight;
    private int mItemRandom;
    private ScanContact.OnItemClickListener mOnItemClickListener;
    private ScanContact.OnItemLongClickListener mOnItemLongClickListener;
    private Point mScreenPoint = new Point();

    public ScanBaseRecyclerViewAdapter(Context context, List<T> data, int layoutId) {
        init(context, data, layoutId, 1, 200, 100);
    }

    public ScanBaseRecyclerViewAdapter(Context context, List<T> data, int layoutId, int type) {
        init(context, data, layoutId, type, 200, 100);
    }

    public ScanBaseRecyclerViewAdapter(Context context, List<T> data, int layoutId, int type, int height, int random) {
        init(context, data, layoutId, type, height, random);
    }

    protected void init(Context context, List<T> data, int layoutId, int type, int height, int random) {
        this.mContext = context;
        this.mData = data;
        this.mItemHeight = height;
        this.mItemRandom = random;
        this.mHolderType = type;
        this.mLayoutId = layoutId;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mScreenPoint.x = displayMetrics.widthPixels;
        mScreenPoint.y = displayMetrics.heightPixels;
    }

    ;

    @Override
    public ScanRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        if (this.mHolderType == 1) {
            return new ScanRecyclerViewHolder(view);
        } else {
            return new ScanRecyclerViewHolder2(view, mItemHeight, mItemRandom);
        }

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
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(this, v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(this,v,(Integer) v.getTag());
        }
        return true;
    }

    public void setOnItemClickListener(ScanContact.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(ScanContact.OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
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
