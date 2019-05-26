package com.base.cameralibrary.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.base.cameralibrary.R;
import com.base.cameralibrary.callback.CameraSaveCallBack;
import com.base.cameralibrary.presenter.CameraImageSavePresenter;
import com.base.cameralibrary.presenter.CameraImageShowPresenter;
import com.base.cameralibrary.view.CropLayout;

public class CameraExampCorpActivity extends AppCompatActivity {
    private String mUrl;
    private CropLayout mCropLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mUrl = getIntent().getStringExtra("imageUrl");
        setContentView(R.layout.camera_examp_crop_activity_layout);
        mCropLayout = findViewById(R.id.cropLayout);

        commonDelayFunction();
    }


    protected void commonDelayFunction() {
        findViewById(R.id.tv_crop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //保存裁剪内容
                saveCropImageFunction();
            }
        });
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(CameraExampCorpActivity.this,"保存异常 ",Toast.LENGTH_LONG).show();
            }

            @Override
            public void cameraSuccess(String mFilePath) {
                Toast.makeText(CameraExampCorpActivity.this,"已保存 "+mFilePath,Toast.LENGTH_LONG).show();
            }

            @Override
            public void cameraPermisExit() {

            }
        });

    }
}
