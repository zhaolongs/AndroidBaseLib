package com.base.baselibapplication.test.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.base.baselibapplication.R;
import com.base.baselibapplication.example.adapter.holder.VideoViewHolder;
import com.base.baselibapplication.test.bean.TestVideoListBean;
import com.base.baselibapplication.test.adapter.VideoPageScanAdapter;
import com.base.scanlistlibrary.base.ScanBaseRecyclerViewAdapter;
import com.base.scanlistlibrary.base.ScanContact;
import com.base.scanlistlibrary.base.ScanRecyclerViewHolder;
import com.base.scanlistlibrary.scanlist.ScanVideoPlayView;
import com.base.scanlistlibrary.videoplay.NiceVideoPlayer;
import com.base.scanlistlibrary.videoplay.NiceVideoPlayerManager;
import com.studyyoun.androidbaselibrary.activity.CommonBaseActivity;
import com.studyyoun.androidbaselibrary.utils.CommonViewOnClickLiserner;
import com.studyyoun.androidbaselibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class TestVideoPageListActivity extends CommonBaseActivity {
    private ScanVideoPlayView videoPlayView;
    //数据请求是否为加载更多数据
    private boolean isLoadMoreData = false;

    private ImageView mBackImageView;

    @Override
    protected void getAllIntentExtraDatas(Intent intent) {

    }

    @Override
    protected int getCommonLayoutId() {
        return R.layout.vote_activity_candidate_list;
    }

    @Override
    protected void commonInitView(View view) {
        videoPlayView = findViewById(R.id.vote_video_play);
        mBackImageView = findViewById(R.id.vote_video_list_back);
    }

    @Override
    protected void commonFunction() {

    }

    @Override
    protected void commonDelayFunction() {
        mBackImageView.setOnClickListener(new CommonViewOnClickLiserner() {
            @Override
            public void onCommonClick(View v) {
                TestVideoPageListActivity.this.finish();
            }
        });


        List<TestVideoListBean> lTestVideoListBeans = new ArrayList<>();
        for (int lI = 0; lI < 10; lI++) {
            TestVideoListBean lTestVideoListBean = new TestVideoListBean();
            lTestVideoListBean.title = System.currentTimeMillis() + " 初始化数据";
            lTestVideoListBeans.add(lTestVideoListBean);
        }

        VideoPageScanAdapter lLittleVideoListAdapter = new VideoPageScanAdapter(this, lTestVideoListBeans, R.layout.item_candidate_ticket_layout);


        /**
         * 设置 videoPlayView 下拉刷新 与上拉加载更多监听
         */
        videoPlayView.setOnRefreshDataListener(new ScanContact.OnRefreshDataListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                List<TestVideoListBean> refreshList = new ArrayList<>();
                for (int lI = 0; lI < 40; lI++) {
                    TestVideoListBean lTestVideoListBean = new TestVideoListBean();
                    lTestVideoListBean.title = System.currentTimeMillis() + " 下拉刷新 ";
                    refreshList.add(lTestVideoListBean);
                }
                videoPlayView.refreshVideoList(refreshList);
            }

            @Override
            public void onLoadMore() {
                //上拉加载更多
                List<TestVideoListBean> moreList = new ArrayList<>();
                for (int lI = 0; lI < 40; lI++) {
                    TestVideoListBean lTestVideoListBean = new TestVideoListBean();
                    lTestVideoListBean.title = System.currentTimeMillis() + " 上拉加载更多 ";
                    moreList.add(lTestVideoListBean);
                }
                videoPlayView.addMoreData(moreList);
            }
        });

        videoPlayView.setOnPageSelectListener(new ScanContact.OnPageSelectListener() {
            @Override
            public void onPageSelected(int position, Object bean, ScanBaseRecyclerViewAdapter adapter, ScanRecyclerViewHolder holder) {

                if (holder != null) {
                    NiceVideoPlayer lNiceVideoPlayer = (NiceVideoPlayer) holder.getView(R.id.nice_video_player);
                    //lNiceVideoPlayer.start();
                    LogUtils.d("lNiceVideoPlayer.start();");
                }

            }

            @Override
            public void onPageRelease(int position, Object o, ScanBaseRecyclerViewAdapter adapter, ScanRecyclerViewHolder holder) {
                LogUtils.d("onPageRelease " + position);
                if (holder != null) {
                    NiceVideoPlayer lNiceVideoPlayer = (NiceVideoPlayer) holder.getView(R.id.nice_video_player);
                    if (lNiceVideoPlayer.isPlaying()) {
                        LogUtils.d("onPageRelease  isPlaying");
                        lNiceVideoPlayer.release();
                    }
                }
            }
        });

        videoPlayView.setOnRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                NiceVideoPlayer niceVideoPlayer = ((VideoViewHolder) holder).mVideoPlayer;
                if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                    NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                }
            }
        });

        /**
         * 初始化 videoPlayView 传入数据适配 Adapter Adapter需要继承于BaseRecyclerViewAdapter
         */
        videoPlayView.initPlayListView(lLittleVideoListAdapter,0,true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }
}
