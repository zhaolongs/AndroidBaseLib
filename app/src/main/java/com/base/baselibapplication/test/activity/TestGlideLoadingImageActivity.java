package com.base.baselibapplication.test.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.base.baselibapplication.R;
import com.studyyoun.androidbaselibrary.activity.CommonBaseActivity;
import com.studyyoun.androidbaselibrary.utils.CommonGlideUtils;

public class TestGlideLoadingImageActivity extends CommonBaseActivity {
    @Override
    protected void getAllIntentExtraDatas(Intent intent) {

    }

    @Override
    protected int getCommonLayoutId() {
        return R.layout.ab_activity_show_img_layout;
    }

    @Override
    protected void commonInitView(View view) {
        mHeaderImageView = findViewById(R.id.ab_show_image);
    }

    @Override
    protected void commonFunction() {

    }

    private ImageView mHeaderImageView;
    @Override
    protected void commonDelayFunction() {
        String headerImagUrl ="http://img3.imgtn.bdimg.com/it/u=3462602620,493082519&fm=26&gp=0.jpg";
        CommonGlideUtils.showImageView(mContext,R.mipmap.ab_ic_tip_fail,headerImagUrl,mHeaderImageView);
    }
}
