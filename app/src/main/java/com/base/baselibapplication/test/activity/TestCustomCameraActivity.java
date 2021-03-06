package com.base.baselibapplication.test.activity;

import android.content.Intent;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.base.baselibapplication.R;
import com.base.cameralibrary.CameraUtils;
import com.base.cameralibrary.activity.CameraExampCorpActivity;
import com.base.cameralibrary.callback.CameraCallBack;
import com.base.cameralibrary.callback.CameraDarkCallBack;
import com.base.cameralibrary.presenter.CameraImageShowPresenter;
import com.base.scanlistlibrary.videoplay.LogUtil;
import com.studyyoun.androidbaselibrary.utils.LogUtils;

/**
 * 自定义相机使用案例
 * 选择相册使用案例
 */
public class TestCustomCameraActivity extends AppCompatActivity {

    private ImageView mShowImageViw;
    private CameraUtils mCameraUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zl_test_custom_camera_activity_layout);

        FrameLayout rootView = findViewById(R.id.fr_root_view);
        mShowImageViw = findViewById(R.id.iv_show_carmera);

        int lWidthPixels = this.getResources().getDisplayMetrics().widthPixels;
        int lHeightPixels = this.getResources().getDisplayMetrics().heightPixels;


        //初始化自定义相机
        mCameraUtils = CameraUtils.getInstance().setContinuous(true).initCamerView(this, rootView, lWidthPixels, lHeightPixels, new CameraCallBack() {
            @Override
            public void cameraFaile(int errCode, String message) {
                LogUtils.e("errCode " + errCode + "  message " + message);
            }

            @Override
            public void cameraSuccess(String mFilePath) {
                LogUtils.d("success  " + mFilePath);
                showImageFunction(mFilePath);
            }

            @Override
            public void cameraPermisExit() {
                TestCustomCameraActivity.this.finish();
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

        mShowImageViw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilePath == null) {
                    Toast.makeText(TestCustomCameraActivity.this, "请拍照", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent lIntent = new Intent(TestCustomCameraActivity.this, CameraExampCorpActivity.class);
                lIntent.putExtra("imageUrl", mFilePath);
                TestCustomCameraActivity.this.startActivity(lIntent);
            }
        });

        mCameraUtils.setLineDarkCallBack(new CameraDarkCallBack() {
            @Override
            public void onLineDark(long pCameraLight) {
                LogUtils.d("onLineDark: "+pCameraLight);
            }

            @Override
            public void onLineNoDark(long pCameraLight) {
                LogUtils.d("onLineNoDark: "+pCameraLight);
            }

            @Override
            public void onDarkList(long[] pDarkList, long pCameraLight) {

            }

            @Override
            public void onSensorChanged(SensorEvent pEvent, float pLux) {
                LogUtil.d("光线强度 "+ pLux);
            }
        });

    }

    private String mFilePath;
    private void showImageFunction(String mFilePath) {
        this.mFilePath = mFilePath;
        //加载显示图片
        CameraImageShowPresenter.getInstance().showImage(mShowImageViw, mFilePath);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //权限回调
        mCameraUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCameraUtils.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraUtils.destore();
    }
}
