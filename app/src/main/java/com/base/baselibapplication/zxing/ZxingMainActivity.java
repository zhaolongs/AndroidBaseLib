package com.base.baselibapplication.zxing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.base.baselibapplication.R;


public class ZxingMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zxing_activity_main);
        Button defaultStart = (Button) findViewById(R.id.default_start);
        defaultStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZxingMainActivity.this, DefaultCaptureActivity.class);
                startActivity(intent);
            }
        });

        Button weStart = (Button) findViewById(R.id.wechat_start);
        weStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZxingMainActivity.this, WeChatCaptureActivity.class);
                startActivity(intent);
            }
        });

        Button aliStart = (Button) findViewById(R.id.alipay_start);
        aliStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZxingMainActivity.this, AliCaptureActivity.class);
                startActivity(intent);
            }
        });

    }
}
