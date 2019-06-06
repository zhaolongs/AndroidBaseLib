package com.base.baselibapplication;

import android.content.Intent;
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
import com.studyyoun.androidbaselibrary.utils.SelectorUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView camerTextView =findViewById(R.id.camer_tv);
        SelectorUtil.addSelector(this,Color.parseColor("#9955ff"),Color.parseColor("#7700ff"),camerTextView);
        camerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, CameraExampOpenActivity.class));
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
    }
}
