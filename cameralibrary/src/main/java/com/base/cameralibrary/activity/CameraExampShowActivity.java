package com.base.cameralibrary.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.base.cameralibrary.R;
import com.base.cameralibrary.presenter.CameraImageShowPresenter;

public class CameraExampShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.camera_examp_show_activity_layout);

        final ImageView showImageView = findViewById(R.id.iv_photo_select);
        final String lImageUrl = getIntent().getStringExtra("imageUrl");
        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                CameraImageShowPresenter.getInstance().showImage(showImageView, lImageUrl);
            }
        }, 1000);


        findViewById(R.id.ll_base_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraExampShowActivity.this.finish();
            }
        });
    }
}
