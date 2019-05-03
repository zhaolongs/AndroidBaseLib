package com.studyyoun.androidbaselibrary.utils;

import android.view.View;

public abstract class CommonViewOnClickLiserner implements View.OnClickListener {


    private long preTime;

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime-preTime>200){
            preTime=currentTime;
            onCommonClick(v);
        }
    }

    public abstract void onCommonClick(View v);
}
