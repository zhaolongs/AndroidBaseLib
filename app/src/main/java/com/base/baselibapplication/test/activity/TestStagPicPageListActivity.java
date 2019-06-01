package com.base.baselibapplication.test.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.baselibapplication.R;
import com.base.baselibapplication.test.adapter.PicGirdPageScanAdapter;
import com.base.baselibapplication.test.adapter.PicStagerPageScanAdapter;
import com.base.baselibapplication.test.bean.TestVideoListBean;
import com.base.scanlistlibrary.base.ScanBaseRecyclerViewAdapter;
import com.base.scanlistlibrary.base.ScanContact;
import com.base.scanlistlibrary.base.ScanRecyclerViewHolder;
import com.base.scanlistlibrary.scanlist.ScanVideoPlayView;
import com.studyyoun.androidbaselibrary.activity.CommonBaseActivity;
import com.studyyoun.androidbaselibrary.utils.CommonViewOnClickLiserner;
import com.studyyoun.androidbaselibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 瀑布流样式
 */
public class TestStagPicPageListActivity extends CommonBaseActivity {
    private ScanVideoPlayView videoPlayView;

    private ImageView mBackImageView;
    private TextView mPageIndexTextView;
    private Button mUpdateAllButon;
    private PicStagerPageScanAdapter mLittleVideoListAdapter;

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
        mPageIndexTextView = findViewById(R.id.test_video_list_tv_page);
        mUpdateAllButon = findViewById(R.id.test_video_list_bt_update_all_page);
    }

    @Override
    protected void commonFunction() {

    }

    @Override
    protected void commonDelayFunction() {
        mBackImageView.setOnClickListener(new CommonViewOnClickLiserner() {
            @Override
            public void onCommonClick(View v) {
                TestStagPicPageListActivity.this.finish();
            }
        });
        mUpdateAllButon.setOnClickListener(new CommonViewOnClickLiserner() {
            @Override
            public void onCommonClick(View v) {
                if (mLittleVideoListAdapter != null) {
                    mLittleVideoListAdapter.notifyDataSetChanged();
                }
            }
        });


        List<TestVideoListBean> lTestVideoListBeans = new ArrayList<>();
        for (int lI = 0; lI < 2; lI++) {
            TestVideoListBean lTestVideoListBean = new TestVideoListBean();
            lTestVideoListBean.title = System.currentTimeMillis() + " 初始化数据";
            lTestVideoListBeans.add(lTestVideoListBean);
        }

        mLittleVideoListAdapter = new PicStagerPageScanAdapter(this, lTestVideoListBeans, R.layout.item_list_page_pic_stag_layout);
        videoPlayView.setOnPageSelectListener(new ScanContact.OnPageSelectListener() {
            @Override
            public void onPageSelected(int position, Object bean, ScanBaseRecyclerViewAdapter adapter, ScanRecyclerViewHolder holder) {
                LogUtils.d("onPageSelected "+position+"  holder "+ holder);
                mPageIndexTextView.setText("s "+position);
            }

            @Override
            public void onPageRelease(int position, Object o, ScanBaseRecyclerViewAdapter adapter, ScanRecyclerViewHolder holder) {

            }
        });

        /**
         * 设置 videoPlayView 下拉刷新 与上拉加载更多监听
         */
        videoPlayView.setOnRefreshDataListener(new ScanContact.OnRefreshDataListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                List<TestVideoListBean> refreshList = new ArrayList<>();
                for (int lI = 0; lI < 2; lI++) {
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
                for (int lI = 0; lI < 2; lI++) {
                    TestVideoListBean lTestVideoListBean = new TestVideoListBean();
                    lTestVideoListBean.title = System.currentTimeMillis() + " 上拉加载更多 ";
                    moreList.add(lTestVideoListBean);
                }
                videoPlayView.addMoreData(moreList);
            }
        });

        /**
         * 初始化 videoPlayView 传入数据适配 Adapter Adapter需要继承于BaseRecyclerViewAdapter
         */
        videoPlayView.initPlayListView(mLittleVideoListAdapter,2,0,null,true,false);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
