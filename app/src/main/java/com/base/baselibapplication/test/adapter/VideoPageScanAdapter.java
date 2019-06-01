package com.base.baselibapplication.test.adapter;

import android.content.Context;
import android.widget.TextView;

import com.base.baselibapplication.R;
import com.base.baselibapplication.test.bean.TestVideoListBean;
import com.base.scanlistlibrary.base.ScanBaseRecyclerViewAdapter;
import com.base.scanlistlibrary.base.ScanRecyclerViewHolder;
import com.base.scanlistlibrary.videoplay.NiceVideoPlayer;
import com.base.scanlistlibrary.videoplay.TxVideoPlayerController;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 视频列表的adapter
 *
 * @author zhaolong
 */
public class VideoPageScanAdapter extends ScanBaseRecyclerViewAdapter<TestVideoListBean> {
    public static final String TAG = VideoPageScanAdapter.class.getSimpleName();

    public Context mContext;
    public VideoPageScanAdapter(Context context, List<TestVideoListBean> data, int layoutId) {
        super(context, data, layoutId);
        mContext = context;
    }

    @Override
    protected void onBindData(ScanRecyclerViewHolder holder, TestVideoListBean bean, int position) {
        TextView lView = (TextView) holder.getView(R.id.test_video_list_page);
        lView.setText(""+bean.title);
        TxVideoPlayerController controller = new TxVideoPlayerController(mContext);
        NiceVideoPlayer lNiceVideoPlayer = (NiceVideoPlayer) holder.getView(R.id.nice_video_player);
        lNiceVideoPlayer.setController(controller);

        TestVideoListBean lBean= (TestVideoListBean) bean;
        controller.setTitle(lBean.title);
        controller.setLenght(lBean.length);
//        Glide.with(mContext)
//                .load(lBean.imageUrl)
//                .into(controller.imageView());
        lNiceVideoPlayer.setUp(lBean.videoUrl, null);
    }
}
