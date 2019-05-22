package com.studyyoun.androidbaselibrary.mvp;

import com.studyyoun.androidbaselibrary.activity.CommonBaseActivity;

public abstract class CommonBaseMvpActivity<T extends BasePresenter>  extends CommonBaseActivity implements BaseView {

    protected T mPresenter;
    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        super.onDestroy();
    }
}
