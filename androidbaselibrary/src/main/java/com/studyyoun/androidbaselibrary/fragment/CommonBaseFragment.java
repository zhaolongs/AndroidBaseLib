package com.studyyoun.androidbaselibrary.fragment;

/**
 * Created by androidlongs on 17/7/14.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.studyyoun.androidbaselibrary.R;
import com.studyyoun.androidbaselibrary.base.IBaseInterface;
import com.studyyoun.androidbaselibrary.utils.CommonGlideUtils;
import com.studyyoun.androidbaselibrary.utils.LogUtils;
import com.studyyoun.androidbaselibrary.utils.ToastUtils;

import java.io.Serializable;


/**
 * CommonBaseActivity Base基类封装
 */

public abstract class CommonBaseFragment extends Fragment implements IBaseInterface {


    protected Context mContext;
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    protected View mItemView;
    protected float mScaledDensity;
    protected int mScreenHeightPixels;
    protected Bundle mBundle;
    private ProgressBar mLoadingBaseProgerssBar;
    private TextView mLoadingBaseTextView;
    private LinearLayout mLoadlayoutView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mItemView != null) {
            ViewGroup parent = (ViewGroup) mItemView.getParent();
            if (parent != null)
                parent.removeView(mItemView);
        } else {
            mItemView = inflater.inflate(getCommonLayoutId(), container, false);
        }


        return mItemView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        initBundle(mBundle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mScaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
        mScreenHeightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
        commonInitView(mItemView);
        commonFunction();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                commonDelayFunction();
            }
        }, 200);


    }

    public void showMsg(String msg) {
        ToastUtils.show(msg, mContext);
    }

    public void lod(String msg) {
        LogUtils.d(msg);
    }

    public void loe(String msg) {
        LogUtils.e(msg);
    }

    //获取布局ID
    protected abstract int getCommonLayoutId();

    //初始化控件
    protected abstract void commonInitView(View view);

    //常用功能 处理
    protected abstract void commonFunction();

    //延时常用功能 处理
    protected abstract void commonDelayFunction();

    protected void initBundle(Bundle bundle) {

    }

    @Override
    public Activity getAty() {
        return getActivity();
    }

    @Override
    public Context getCtx() {
        return mContext;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBundle = null;
    }


    protected <T extends View> T findView(int viewId) {
        return (T) mItemView.findViewById(viewId);
    }

    protected <T extends Serializable> T getBundleSerializable(String key) {
        if (mBundle == null) {
            return null;
        }
        return (T) mBundle.getSerializable(key);
    }

    protected void setImageFromNet(int viewId, String imageUrl) {
        ImageView imageView = findView(viewId);
        CommonGlideUtils.showImageView(mContext, R.mipmap.ab_toast_error, imageUrl, imageView);
    }

    protected void setText(int viewId, String text) {
        TextView textView = findView(viewId);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        textView.setText(text);
    }

    protected void setText(int viewId, String text, String emptyTip) {
        TextView textView = findView(viewId);
        if (TextUtils.isEmpty(text)) {
            textView.setText(emptyTip);
            return;
        }
        textView.setText(text);
    }

    protected void setTextEmptyGone(int viewId, String text) {
        TextView textView = findView(viewId);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setText(text);
    }

    protected <T extends View> T setGone(int id) {
        T view = findView(id);
        view.setVisibility(View.GONE);
        return view;
    }

    protected <T extends View> T setVisibility(int id) {
        T view = findView(id);
        view.setVisibility(View.VISIBLE);
        return view;
    }

    protected void setInVisibility(int id) {
        findView(id).setVisibility(View.INVISIBLE);
    }


    protected void setStatusBarPadding() {
        mItemView.setPadding(0, getStatusHeight(mContext), 0, 0);
    }

    @SuppressLint("ObsoleteSdkInt,PrivateApi")
    private static int getStatusHeight(Context context) {
        int statusHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class<?> clazz = Class.forName("com.android.internal.R$dimen");
                Object object = clazz.newInstance();
                int height = Integer.parseInt(clazz.getField("status_bar_height")
                        .get(object).toString());
                statusHeight = context.getResources().getDimensionPixelSize(height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


    @Override
    public void skipActivity(Class<? extends IBaseInterface> view) {
        Intent intent = new Intent(getContext(), view);
        startActivity(intent);
    }

    @Override
    public void skipActivity(Class<? extends IBaseInterface> view, Bundle bundle) {
        Intent intent = new Intent(getContext(), view);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void skipActivityByFinish(Class<? extends IBaseInterface> view) {
        skipActivity(view);
        getActivity().finish();
    }

    @Override
    public void skipActivityByFinish(Class<? extends IBaseInterface> view, Bundle bundle) {
        skipActivity(view, bundle);
        getActivity().finish();
    }
}
