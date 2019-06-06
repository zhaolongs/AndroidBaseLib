package com.base.baselibapplication.test.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.base.baselibapplication.R;
import com.base.baselibapplication.test.adapter.TextPageScanAdapter;
import com.base.baselibapplication.test.bean.TestVideoListBean;
import com.base.scanlistlibrary.base.ScanContact;
import com.base.scanlistlibrary.scanlist.ScanVideoPlayView;
import com.studyyoun.androidbaselibrary.activity.CommonBaseActivity;
import com.studyyoun.androidbaselibrary.utils.CommonViewOnClickLiserner;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView 数据加载封装使用 案例
 * 支持下拉刷新
 * 支持上拉加载更多
 */
public class TestRecyclerViewActivity extends CommonBaseActivity {
    private ScanVideoPlayView mScanVideoPlayView;
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
        mScanVideoPlayView = findViewById(R.id.scan_video_play);
        mBackImageView = findViewById(R.id.scan_video_list_back);
    }

    @Override
    protected void commonFunction() {

    }

    @Override
    protected void commonDelayFunction() {
        mBackImageView.setOnClickListener(new CommonViewOnClickLiserner() {
            @Override
            public void onCommonClick(View v) {
                TestRecyclerViewActivity.this.finish();
            }
        });


        //模拟数据  TestVideoListBean 只是一个普通的数据模型bean
        List<TestVideoListBean> lTestVideoListBeans = new ArrayList<>();
        for (int lI = 0; lI < 20; lI++) {
            TestVideoListBean lTestVideoListBean = new TestVideoListBean();
            lTestVideoListBean.title = System.currentTimeMillis() + " 初始化数据";
            lTestVideoListBeans.add(lTestVideoListBean);
        }
        //数据适配器
        TextPageScanAdapter lLittleVideoListAdapter = new TextPageScanAdapter(this, lTestVideoListBeans, R.layout.item_list_page_data_layout);
        //设置 mScanVideoPlayView 下拉刷新 与上拉加载更多监听
        mScanVideoPlayView.setOnRefreshDataListener(mOnRefreshDataListener);
        //  初始化 mScanVideoPlayView 传入数据适配 Adapter Adapter需要继承于BaseRecyclerViewAdapter
        mScanVideoPlayView.initPlayListView(lLittleVideoListAdapter);
        //下拉刷新功能使用设置 false 不启用 ，true 启用
        mScanVideoPlayView.setOnRefresh(true);
    }

    ScanContact.OnRefreshDataListener mOnRefreshDataListener = new ScanContact.OnRefreshDataListener() {
        @Override
        public void onRefresh() {
            //下拉刷新
            List<TestVideoListBean> refreshList = new ArrayList<>();
            for (int lI = 0; lI < 10; lI++) {
                TestVideoListBean lTestVideoListBean = new TestVideoListBean();
                lTestVideoListBean.title = System.currentTimeMillis() + " 下拉刷新 ";
                refreshList.add(lTestVideoListBean);
            }
            mScanVideoPlayView.refreshVideoList(refreshList);
        }

        @Override
        public void onLoadMore() {
            //上拉加载更多
            List<TestVideoListBean> moreList = new ArrayList<>();
            for (int lI = 0; lI < 10; lI++) {
                TestVideoListBean lTestVideoListBean = new TestVideoListBean();
                lTestVideoListBean.title = System.currentTimeMillis() + " 上拉加载更多 ";
                moreList.add(lTestVideoListBean);
            }
            mScanVideoPlayView.addMoreData(moreList);
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
