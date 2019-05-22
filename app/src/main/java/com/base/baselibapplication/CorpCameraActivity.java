package com.base.baselibapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.base.cameralibrary.callback.CameraSaveCallBack;
import com.base.cameralibrary.presenter.CameraImageSavePresenter;
import com.base.cameralibrary.presenter.CameraImageShowPresenter;
import com.studyyoun.androidbaselibrary.activity.CommonBaseActivity;
import com.studyyoun.androidbaselibrary.view.CropLayout;

public class CorpCameraActivity extends CommonBaseActivity {
    private String mUrl;
    private CropLayout mCropLayout;

    @Override
    protected void getAllIntentExtraDatas(Intent intent) {
        mUrl = intent.getStringExtra("imageUrl");
    }

    @Override
    protected int getCommonLayoutId() {
        return R.layout.activity_crop;
    }

    @Override
    protected void commonInitView(View view) {
        mCropLayout = findViewById(com.studyyoun.androidbaselibrary.R.id.cropLayout);
    }

    @Override
    protected void commonFunction() {

    }

    @Override
    protected void commonDelayFunction() {
        findViewById(com.studyyoun.androidbaselibrary.R.id.tv_crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //保存裁剪内容
                saveCropImageFunction();
            }
        });
        findViewById(com.studyyoun.androidbaselibrary.R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CameraImageShowPresenter.getInstance().showImage(mCropLayout.getImageView(),mUrl);

        mCropLayout.start();
    }

    private void saveCropImageFunction() {
        CameraImageSavePresenter.getInstance().saveImage(this, mCropLayout.cropBitmap(), new CameraSaveCallBack() {
            @Override
            public void cameraFaile(int errCode, String message) {
                Toast.makeText(CorpCameraActivity.this,"保存异常 ",Toast.LENGTH_LONG).show();
            }

            @Override
            public void cameraSuccess(String mFilePath) {
                Toast.makeText(CorpCameraActivity.this,"已保存 "+mFilePath,Toast.LENGTH_LONG).show();
            }

            @Override
            public void cameraPermisExit() {

            }
        });

    }
}
