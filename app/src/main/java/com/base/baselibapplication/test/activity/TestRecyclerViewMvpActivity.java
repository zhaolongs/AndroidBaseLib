package com.base.baselibapplication.test.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.baselibapplication.R;
import com.base.baselibapplication.test.bean.TestMvpBean;
import com.base.scanlistlibrary.base.ScanContact;
import com.base.scanlistlibrary.base.ScanRecyclerViewHolder;
import com.base.scanlistlibrary.presenter.ScanPresenter;
import com.studyyoun.androidbaselibrary.activity.CommonBaseActivity;
import com.studyyoun.androidbaselibrary.utils.CommonViewOnClickLiserner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * RecyclerView 数据加载封装使用 案例
 * 支持下拉刷新
 * 支持上拉加载更多
 */
public class TestRecyclerViewMvpActivity extends CommonBaseActivity {

    private ImageView mBackImageView;
    private RelativeLayout mRootViewLayout;

    @Override
    protected void getAllIntentExtraDatas(Intent intent) {

    }

    @Override
    protected int getCommonLayoutId() {
        return R.layout.zl_test_activity_recy_list;
    }

    @Override
    protected void commonInitView(View view) {
        mRootViewLayout = findViewById(R.id.root_view);
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
                TestRecyclerViewMvpActivity.this.finish();
            }
        });


        ScanPresenter<TestMvpBean> lPresenter = new ScanPresenter<>();
        RequestModel lRequestModel = new RequestModel();
        lPresenter.setScanModel(lRequestModel);
        lPresenter.loading(
                this,
                mRootViewLayout,
                null,
                R.layout.item_list_page_data_layout,
                1,
                50,
                0,
                "http://www.test.com",
                null,
                TestMvpBean.class,
                false,
                new ScanPresenter.OnBindDataCallback() {
                    @Override
                    public void onBindData(ScanRecyclerViewHolder holder, Object bean, int position) {
                        TestMvpBean lBean = (TestMvpBean) bean;
                        TextView lView = (TextView) holder.getView(R.id.test_video_list_page);
                        lView.setText("" + lBean.title);
                    }

                    @Override
                    public void onFaile(int i, String message) {

                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class RequestModel implements ScanContact.ScanModel {

        @Override
        public void load(String urlString, final int currentPage, int currentPageSize, HashMap<String, Object> keyParameter, Class tClass, final ScanPresenter.OnInterNetRequestCallback onInterNetRequestCallback) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<TestMvpBean> lList = new ArrayList<>();
                    for (int lI = 0; lI < 10; lI++) {
                        TestMvpBean lBean = new TestMvpBean();
                        lBean.title = System.currentTimeMillis() + " 测试 " + currentPage;
                        lList.add(lBean);
                    }
                    onInterNetRequestCallback.onSuccess(lList);
                }
            }, 1000);
        }
    }
}
