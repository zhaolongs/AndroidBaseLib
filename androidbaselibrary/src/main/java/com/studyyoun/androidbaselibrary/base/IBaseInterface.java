package com.studyyoun.androidbaselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by xiaochan on 2017/6/26.
 */

public interface IBaseInterface {
    Activity getAty();

    Context getCtx();

    void skipActivity(Class<? extends IBaseInterface> activity) ;

    void skipActivity(Class<? extends IBaseInterface> activity, Bundle bundle);

    void skipActivityByFinish(Class<? extends IBaseInterface> activity);

    void skipActivityByFinish(Class<? extends IBaseInterface> activity, Bundle bundle) ;
}
