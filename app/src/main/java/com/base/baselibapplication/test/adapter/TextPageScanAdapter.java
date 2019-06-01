package com.base.baselibapplication.test.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.baselibapplication.R;
import com.base.baselibapplication.test.bean.TestVideoListBean;
import com.base.scanlistlibrary.base.ScanBaseRecyclerViewAdapter;
import com.base.scanlistlibrary.base.ScanRecyclerViewHolder;
import com.studyyoun.androidbaselibrary.utils.CommonGlideUtils;

import java.util.List;

/**
 * 视频列表的adapter
 *
 * @author zhaolong
 */
public class TextPageScanAdapter extends ScanBaseRecyclerViewAdapter<TestVideoListBean> {
    public static final String TAG = TextPageScanAdapter.class.getSimpleName();

    public Context mContext;
    public TextPageScanAdapter(Context context, List<TestVideoListBean> data, int layoutId) {
        super(context, data, layoutId);
        mContext = context;
    }

    @Override
    protected void onBindData(ScanRecyclerViewHolder holder, TestVideoListBean bean, int position) {
        TextView lTextView = (TextView) holder.getView(R.id.test_video_list_page);
        lTextView.setText(""+bean.title);
    }
}
