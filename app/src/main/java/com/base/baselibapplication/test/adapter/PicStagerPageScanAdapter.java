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
public class PicStagerPageScanAdapter extends ScanBaseRecyclerViewAdapter<TestVideoListBean> {

    public Context mContext;

    public PicStagerPageScanAdapter(Context context, List<TestVideoListBean> data, int layoutId) {
        //瀑布流使用时 需要设置为 2，默认为1 显示列表数据
        super(context, data, layoutId, 2);
        mContext = context;
    }

    @Override
    protected void onBindData(ScanRecyclerViewHolder holder, TestVideoListBean bean, int position) {
        ImageView lView = (ImageView) holder.getView(R.id.test_video_list_iv_page);
        String url = "http://b-ssl.duitang.com/uploads/blog/201409/11/20140911024410_nX8Gs.jpeg";
        CommonGlideUtils.showImageView(mContext, R.mipmap.ab_toast_error, url, lView);

        TextView lTextView = (TextView) holder.getView(R.id.test_video_list_tv_page);
        lTextView.setText("" + bean.title);
    }
}
