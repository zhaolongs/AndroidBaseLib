package com.studyyoun.androidbaselibrary.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class CommonListBaseActivity<T>  extends CommonBaseActivity {


    private CommonBaseListAdapter mCommonBaseListAdapter;

    public interface CommListBaseCallBack<T>{

        void callBack(View itemView, T object, int pos);
    }

    private CommListBaseCallBack mCommListBaseCallBack;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mHomeDateRecyclerView;

    public void setRecyclerViewFunction(RecyclerView lRecyclerView, List<T> lObjectList,int layoutId,Context lContext,CommListBaseCallBack lCommListBaseCallBack) {
        mHomeDateRecyclerView = lRecyclerView;
        mCommListBaseCallBack = lCommListBaseCallBack;
        setRecyclerDefaultConfigFunction();
        mCommonBaseListAdapter = new CommonBaseListAdapter(lObjectList,layoutId,lContext);
        mHomeDateRecyclerView.setAdapter(mCommonBaseListAdapter);
    }

    public void updateList(List<T> lObjectList){
        mObjectList=lObjectList;
        mCommonBaseListAdapter.notifyDataSetChanged();
    }

    private List<T> mObjectList;
    private void setRecyclerDefaultConfigFunction() {
        mLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                RecyclerView.SmoothScroller smoothScroller = new CenterSmoothScroller(recyclerView.getContext());
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

            class CenterSmoothScroller extends LinearSmoothScroller {

                CenterSmoothScroller(Context context) {
                    super(context);
                }

                @Override
                public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
                    return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2);
                }

                @Override
                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return 150f / displayMetrics.densityDpi;
                }
            }
        };
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setSmoothScrollbarEnabled(true);
        /**
         * LinearLayoutManager，这里RecyclerView在onMeasure回调中会调用LinearLayoutManager的onMeasure方法
         */
        mLayoutManager.setAutoMeasureEnabled(true);
        mHomeDateRecyclerView.setLayoutManager(mLayoutManager);
        /**
         * 当我们确定Item的改变不会影响RecyclerView的宽高的时候可以设置setHasFixedSize(true)，
         * 并通过Adapter的增删改插方法去刷新RecyclerView，而不是通过notifyDataSetChanged()。
         * （其实可以直接设置为true，当需要改变宽高的时候就用notifyDataSetChanged()去整体刷新一下）
         *
         * 查看源码 不难发现当调用方法onItemRangeChanged(),onItemRangeInserted(),onItemRangeRemoved(),onItemRangeMoved()，这样看就很明白了，
         * 当调用Adapter的增删改插方法，最后就会根据mHasFixedSize这个值来判断需要不需要requestLayout()
         *
         * 而在方法 notifyDataSetChanged()执行的代码，最后是调用了onChanged，调用了requestLayout()，会去重新测量宽高
         */
        mHomeDateRecyclerView.setHasFixedSize(true);
        /**
         * 使用NestedScrollView嵌套RecyclerView的时候会解决在RecyclerView上滑动的时候没有滚动的效果的问题
         */
        mHomeDateRecyclerView.setNestedScrollingEnabled(false);

        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mHomeDateRecyclerView);


    }

    private class CommonBaseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private int layoutId;
        private Context mContext;

        public CommonBaseListAdapter(List<T> lObjectList, int lLayoutId, Context lContext) {
            mContext = lContext;
            layoutId =lLayoutId;
            mObjectList=lObjectList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup lViewGroup, int lI) {
            return new CommonBaseListViewHolder(View.inflate(mContext,layoutId,null),mCommListBaseCallBack);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder lViewHolder, int lI) {

            ((CommonBaseListViewHolder)lViewHolder).setDatas(mObjectList.get(lI),lI);
        }

        @Override
        public int getItemCount() {
            return mObjectList==null?0:mObjectList.size();
        }
    }

    private static class CommonBaseListViewHolder<T> extends RecyclerView.ViewHolder{

        private CommListBaseCallBack mCommListBaseCallBack;
        public CommonBaseListViewHolder(View itemView, CommListBaseCallBack lCommListBaseCallBack) {
            super(itemView);
            mCommListBaseCallBack = lCommListBaseCallBack;
        }

        public void setDatas(T lO, int lI) {
            if (mCommListBaseCallBack != null) {
                mCommListBaseCallBack.callBack(itemView,lO,lI);
            }
        }
    }
}
