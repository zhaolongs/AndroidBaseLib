package com.base.baselibapplication.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.base.baselibapplication.R;
import com.base.scanlistlibrary.videoplay.NiceVideoPlayerManager;
;


public class ProcessHome2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_home2);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new DemoProcessHomeKeyFragenment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }
}
