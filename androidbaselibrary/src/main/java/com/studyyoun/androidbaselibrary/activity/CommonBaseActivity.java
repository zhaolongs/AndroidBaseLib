package com.studyyoun.androidbaselibrary.activity;

/**
 * Created by androidlongs
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.studyyoun.androidbaselibrary.base.IBaseInterface;
import com.studyyoun.androidbaselibrary.manager.AppManager;
import com.studyyoun.androidbaselibrary.utils.LogUtils;
import com.studyyoun.androidbaselibrary.utils.ToastUtils;


/**
 * CommonBaseActivity Base基类封装
 */

public abstract class CommonBaseActivity extends AppCompatActivity implements IBaseInterface {

    protected Context mContext;
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    public DisplayMetrics mDisplayMetrics;
    public float mScaledDensity;
    private TextView mTitleTextView;
    protected int mScreenHeight;
    protected int mScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        View view = View.inflate(this, getCommonLayoutId(), null);
        setContentView(view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Intent intent = getIntent();
        getAllIntentExtraDatas(intent);
        commonInitView(view);
        commonFunction();
        AppManager.addActivity(this);
        if (view instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) view;
            if (linearLayout.getChildCount() > 0) {
                View childAt = linearLayout.getChildAt(0);
                if (childAt != null && childAt instanceof RelativeLayout && childAt.getTag() != null && childAt.getTag().equals("101")) {
                    ((RelativeLayout) childAt).getChildAt(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    mTitleTextView = (TextView) ((RelativeLayout) childAt).getChildAt(1);
                }
            }
        }

        mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        mScaledDensity = mDisplayMetrics.scaledDensity;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                commonDelayFunction();
            }
        }, 200);


        //3、获取屏幕的默认分辨率
        Display display = getWindowManager().getDefaultDisplay();
        mScreenHeight = display.getHeight();
        mScreenWidth = display.getWidth();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
        }
    }

    public void showMsg(String msg) {
        ToastUtils.onSuccessShowToast(msg);
    }

    public void setTitle(String lTitle) {
        if (mTitleTextView != null) {
            mTitleTextView.setText(lTitle);
        }
    }

    public void logd(String msg) {
        LogUtils.d(msg);
    }

    public void loge(String msg) {
        LogUtils.e(msg);
    }

    //获取所有的传递数据
    protected abstract void getAllIntentExtraDatas(Intent intent);

    //获取布局ID
    protected abstract int getCommonLayoutId();

    //初始化控件
    protected abstract void commonInitView(View view);

    //常用功能 处理
    protected abstract void commonFunction();

    //延时常用功能 处理
    protected abstract void commonDelayFunction();


    @Override
    public void skipActivity(Class<? extends IBaseInterface> view) {
        Intent intent1 = new Intent(this, view);
        startActivity(intent1);
    }

    @Override
    public void skipActivity(Class<? extends IBaseInterface> view, Bundle bundle) {
        Intent intent1 = new Intent(this, view);
        intent1.putExtras(bundle);
        startActivity(intent1);
    }

    @Override
    public void skipActivityByFinish(Class<? extends IBaseInterface> view) {
        skipActivity(view);
        ActivityManager.getInstance().finishView();
    }

    @Override
    public void skipActivityByFinish(Class<? extends IBaseInterface> view, Bundle bundle) {
        skipActivity(view, bundle);
        ActivityManager.getInstance().finishView();
    }

    @Override
    public Context getCtx() {
        return mContext;
    }

    @Override
    public Activity getAty() {
        return this;
    }
}
