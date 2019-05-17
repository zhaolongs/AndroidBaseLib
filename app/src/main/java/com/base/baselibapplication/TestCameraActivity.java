package com.base.baselibapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.base.cameralibrary.CameraImageShowPresenter;
import com.base.cameralibrary.CameraUtils;
import com.studyyoun.androidbaselibrary.utils.LogUtils;

public class TestCameraActivity extends AppCompatActivity {

    private ImageView mShowImageViw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camer);

        FrameLayout rootView = findViewById(R.id.fr_root_view);
        mShowImageViw = findViewById(R.id.iv_show_carmera);

        CameraUtils.getInstance().initCamerView(this, rootView, new CameraUtils.CallBack() {
            @Override
            public void cameraFaile(int errCode, String message) {
                LogUtils.e("errCode " + errCode + "  message " + message);
            }

            @Override
            public void cameraSuccess(String mFilePath) {
                LogUtils.d("success  " + mFilePath);
                showImageFunction(mFilePath);
            }
        });

        findViewById(R.id.bt_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.getInstance().onCameraClick();
            }
        });
        findViewById(R.id.bt_recamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.getInstance().reCameraClick();
            }
        });
        findViewById(R.id.bt_face_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.getInstance().changeCameraClick();
            }
        });



    }

    private void showImageFunction(String mFilePath) {
        CameraImageShowPresenter.getInstance().showImage(mShowImageViw,mFilePath);
    }
}
