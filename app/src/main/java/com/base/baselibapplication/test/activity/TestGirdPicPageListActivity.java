package com.base.baselibapplication.test.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.baselibapplication.R;
import com.base.baselibapplication.test.adapter.PicGirdPageScanAdapter;
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
 * 九宫格样式
 */
public class TestGirdPicPageListActivity extends CommonBaseActivity {
    private ScanVideoPlayView videoPlayView;

    private ImageView mBackImageView;
    private TextView mPageIndexTextView;
    private Button mUpdateAllButon;
    private PicGirdPageScanAdapter mLittleVideoListAdapter;

    @Override
    protected void getAllIntentExtraDatas(Intent intent) {

    }

    @Override
    protected int getCommonLayoutId() {
        return R.layout.vote_activity_candidate_list;
    }

    @Override
    protected void commonInitView(View view) {
        videoPlayView = findViewById(R.id.scan_video_play);
        mBackImageView = findViewById(R.id.scan_video_list_back);
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
                TestGirdPicPageListActivity.this.finish();
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
        for (int lI = 0; lI < 10; lI++) {
            TestVideoListBean lTestVideoListBean = new TestVideoListBean();
            lTestVideoListBean.title = System.currentTimeMillis() + " 初始化数据";
            lTestVideoListBeans.add(lTestVideoListBean);
        }

        mLittleVideoListAdapter = new PicGirdPageScanAdapter(this, lTestVideoListBeans, R.layout.item_list_page_picglayout);
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
                for (int lI = 0; lI < 10; lI++) {
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
                for (int lI = 0; lI < 10; lI++) {
                    TestVideoListBean lTestVideoListBean = new TestVideoListBean();
                    lTestVideoListBean.title = System.currentTimeMillis() + " 上拉加载更多 ";
                    moreList.add(lTestVideoListBean);
                }
                videoPlayView.addMoreData(moreList);
            }
        });

        /**
         * 初始化 videoPlayView 传入数据适配 Adapter Adapter需要继承于BaseRecyclerViewAdapter
         * 参数二 colum 是九宫格显示时所要显示的列数 这里显示3列
         * 参数三 emptyLayoutId 是无数据时 显示的布局样式，传0 显示默认
         * 参数四 设置为false 只在整页切换数据时使用
         */
        videoPlayView.initPlayListView(mLittleVideoListAdapter,3,0,false);
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
