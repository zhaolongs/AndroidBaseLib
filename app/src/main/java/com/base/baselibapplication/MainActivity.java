package com.base.baselibapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.base.baselibapplication.example.VideoMainActivity;
import com.base.baselibapplication.test.activity.TestAutoScannerViewActivity;
import com.base.baselibapplication.test.activity.TestGirdPicPageListActivity;
import com.base.baselibapplication.test.activity.TestGlideLoadingImageActivity;
import com.base.baselibapplication.test.activity.TestRecyclerViewActivity;
import com.base.baselibapplication.test.activity.TestPicPageListActivity;
import com.base.baselibapplication.test.activity.TestRecyclerViewMvpActivity;
import com.base.baselibapplication.test.activity.TestStagPicPageListActivity;
import com.base.baselibapplication.test.activity.TestVideoPageListActivity;
import com.base.baselibapplication.zxing.ZxingMainActivity;
import com.base.cameralibrary.activity.CameraExampOpenActivity;
import com.base.cameralibrary.activity.CameraExampShowActivity;
import com.base.scanlistlibrary.videoplay.LogUtil;
import com.studyyoun.androidbaselibrary.utils.SelectorUtil;

public class MainActivity extends AppCompatActivity {

    private FinishActivityRecivier mFinishActivityRecivier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView camerTextView =findViewById(R.id.camer_tv);
        SelectorUtil.addSelector(this,Color.parseColor("#9955ff"),Color.parseColor("#7700ff"),camerTextView);
        camerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lIntent = new Intent(MainActivity.this, CameraExampOpenActivity.class);
                lIntent.putExtra("cropWidth",500);
                lIntent.putExtra("cropHeight",200);
                MainActivity.this.startActivity(lIntent);
            }
        });
        findViewById(R.id.tv_auto_scaner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, TestAutoScannerViewActivity.class));
            }
        });
        findViewById(R.id.tv_video_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, TestVideoPageListActivity.class));
            }
        });
        findViewById(R.id.tv_pic_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, TestPicPageListActivity.class));
            }
        });
        findViewById(R.id.tv_data_gird_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, TestGirdPicPageListActivity.class));
            }
        });
        findViewById(R.id.tv_data_stage_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, TestStagPicPageListActivity.class));
            }
        });


        findViewById(R.id.tv_data_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, TestRecyclerViewActivity.class));
            }
        });
        findViewById(R.id.tv_data2_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, TestRecyclerViewMvpActivity.class));
            }
        });
        findViewById(R.id.tv_video_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, VideoMainActivity.class));
            }
        });
        findViewById(R.id.tv_video_zxing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, ZxingMainActivity.class));
            }
        });

        TextView testShowImageTextView =findViewById(R.id.tv_img_show);
        SelectorUtil.addSelector(this,Color.parseColor("#9955ff"),Color.parseColor("#7700ff"),testShowImageTextView);
        testShowImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, TestGlideLoadingImageActivity.class));
            }
        });



        mFinishActivityRecivier = new FinishActivityRecivier();
        registerReceiver(mFinishActivityRecivier, new IntentFilter("cameraactivityfinish"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mFinishActivityRecivier);
    }

    class FinishActivityRecivier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("reciver ");
            int lCode = intent.getIntExtra("code", 0);
            String imagPath = intent.getStringExtra("filePath");
        }
    }
}
