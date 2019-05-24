package com.base.cameralibrary.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.base.cameralibrary.CameraUtils;
import com.base.cameralibrary.R;
import com.base.cameralibrary.callback.CameraCallBack;
import com.base.cameralibrary.callback.CameraPhotoGraphCallback;

import static com.base.cameralibrary.CameraConfig.LOGTAG;

/**
 * 1、打开自定义相机
 * 2、前后镜头切换
 * 3、打开相册
 */
public class CameraExampOpenActivity extends AppCompatActivity {

    private CameraUtils mCameraUtils;
    private LinearLayout mBackLayout;
    private Uri mImageUri;
    private String mMImagePath;
    private String mKey;
    private String mFilePath;
    private FrameLayout mRootView;
    private Context mContext;
    private DisplayMetrics mDisplayMetrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestNoTitle();
        setContentView(getCommonLayoutId());
        mContext = this;
        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        commonInitView();
        commonFunction();
        commonDelayFunction();
    }

    protected void requestNoTitle() {
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    protected int getCommonLayoutId() {
        return R.layout.camera_examp_open_activity_layout;
    }


    protected void commonInitView() {
        mBackLayout = findViewById(R.id.ll_base_back);
        mRootView = findViewById(R.id.fr_root_view);
    }


    protected void commonFunction() {
        //初始化自定义相机
        mCameraUtils = CameraUtils.getInstance().setContinuous(true).initCamerView(this, mRootView, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, mCameraCallBack);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mCameraUtils != null) {
            mCameraUtils.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraUtils.destore();
    }


    protected void commonDelayFunction() {

        findViewById(R.id.aliyun_record_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.getInstance().onCameraClick();
            }
        });
        findViewById(R.id.camera_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUtils.getInstance().changeCameraClick();
            }
        });

        findViewById(R.id.tv_photo_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOGTAG, "打开相册");
                CameraUtils.getInstance().openCapTureGroupFunction(CameraExampOpenActivity.this);
            }
        });

        mBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraExampOpenActivity.this.finish();
            }
        });

    }

    private CameraCallBack mCameraCallBack = new CameraCallBack() {
        @Override
        public void cameraFaile(int errCode, String message) {
            Log.d(LOGTAG, errCode + " + errCode + " + message + "  message");
        }

        @Override
        public void cameraSuccess(String mFilePath) {
            Log.d(LOGTAG, "success  " + mFilePath);
            showImageFunction(mFilePath);
        }

        @Override
        public void cameraPermisExit() {
            CameraExampOpenActivity.this.finish();
        }
    };

    private CameraPhotoGraphCallback mCameraPhotoGraphCallback = new CameraPhotoGraphCallback() {
        @Override
        public void onSuccess(String s) {
            Log.d(LOGTAG, "相册路径处理 " + s);
            showImageFunction(s);
        }

        @Override
        public void onFaile(String s) {
            //异常
            Log.e(LOGTAG, "相册选取图片失败" + s);
        }
    };

    private void showImageFunction(String mFilePath) {
        this.mFilePath = mFilePath;
        //加载显示图片
        final Intent lIntent = new Intent(CameraExampOpenActivity.this, CameraExampShowActivity.class);
        lIntent.putExtra("imageUrl", mFilePath);
        Log.d(LOGTAG, "imageUrl " + mFilePath);
        CameraExampOpenActivity.this.startActivity(lIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //权限回调
        mCameraUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mCameraUtils.onActivityResult(requestCode, resultCode, data, mContext, mCameraPhotoGraphCallback);
        }
    }
}
