package com.base.baselibapplication.test.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.baselibapplication.R;
import com.base.baselibapplication.test.bean.TestVideoListBean;
import com.base.baselibapplication.test.adapter.PicPageScanAdapter;
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
 * 仿抖音 整页页面切换功能
 */
public class TestPicPageListActivity extends CommonBaseActivity {
    private ScanVideoPlayView mVideoPlayView;

    private ImageView mBackImageView;
    private TextView mPageIndexTextView;
    private Button mUpdateAllButon;
    private PicPageScanAdapter mLittleVideoListAdapter;


    @Override
    protected void getAllIntentExtraDatas(Intent intent) {

    }

    @Override
    protected int getCommonLayoutId() {
        return R.layout.vote_activity_candidate_list;
    }

    @Override
    protected void commonInitView(View view) {
        mVideoPlayView = findViewById(R.id.scan_video_play);
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
                TestPicPageListActivity.this.finish();
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
        for (int lI = 0; lI < 1; lI++) {
            TestVideoListBean lTestVideoListBean = new TestVideoListBean();
            lTestVideoListBean.title = System.currentTimeMillis() + " 初始化数据";
            lTestVideoListBeans.add(lTestVideoListBean);
        }

        mLittleVideoListAdapter = new PicPageScanAdapter(this, lTestVideoListBeans, R.layout.item_list_page_pic_layout);
        //页面选中监听
        mVideoPlayView.setOnPageSelectListener(mOnPageSelectListener);
        //下拉刷新监听
        mVideoPlayView.setOnRefreshDataListener(mOnRefreshDataListener);


        /**
         * 初始化 mVideoPlayView 传入数据适配 Adapter Adapter需要继承于BaseRecyclerViewAdapter
         * 参数二 colum 设置为1 展示数据为列表样式
         * 参数三 emptyLayoutId 是无数据时 显示的布局样式，传0 显示默认
         * 参数四 设置为true 整页切换功能启动
         */
        mVideoPlayView.initPlayListView(mLittleVideoListAdapter, 1, 0, true);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    /**
     * 设置 mVideoPlayView 下拉刷新 与上拉加载更多监听
     */
    private ScanContact.OnRefreshDataListener mOnRefreshDataListener = new ScanContact.OnRefreshDataListener() {
        @Override
        public void onRefresh() {
            //下拉刷新
            List<TestVideoListBean> refreshList = new ArrayList<>();
            for (int lI = 0; lI < 40; lI++) {
                TestVideoListBean lTestVideoListBean = new TestVideoListBean();
                lTestVideoListBean.title = System.currentTimeMillis() + " 下拉刷新 ";
                refreshList.add(lTestVideoListBean);
            }
            LogUtils.d("下拉刷新 ");
            //更新所有的数据
            mVideoPlayView.refreshVideoList(refreshList);
            //更新所有的数据 并滑动到第10条数据显示
            mVideoPlayView.refreshVideoList(refreshList,10);
        }

        @Override
        public void onLoadMore() {
            //上拉加载更多
            LogUtils.d("上拉加载更多 ");
            List<TestVideoListBean> moreList = new ArrayList<>();
            for (int lI = 0; lI < 40; lI++) {
                TestVideoListBean lTestVideoListBean = new TestVideoListBean();
                lTestVideoListBean.title = System.currentTimeMillis() + " 上拉加载更多 ";
                moreList.add(lTestVideoListBean);
            }
            mVideoPlayView.addMoreData(moreList);
        }
    };
    private ScanContact.OnPageSelectListener mOnPageSelectListener = new ScanContact.OnPageSelectListener() {
        @Override
        public void onPageSelected(int position, Object bean, ScanBaseRecyclerViewAdapter adapter, ScanRecyclerViewHolder holder) {
            LogUtils.d("onPageSelected " + position + "  holder " + holder);
            mPageIndexTextView.setText("s" + position);
            TestVideoListBean lTestVideoListBean = (TestVideoListBean) bean;
            lTestVideoListBean.title = "su " + System.currentTimeMillis();
            mLittleVideoListAdapter.notifyItemChanged(position);
        }

        @Override
        public void onPageRelease(int position, Object o, ScanBaseRecyclerViewAdapter adapter, ScanRecyclerViewHolder holder) {

        }
    };
}
