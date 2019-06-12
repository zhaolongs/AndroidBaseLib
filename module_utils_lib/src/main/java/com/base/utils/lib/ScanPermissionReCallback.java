package com.base.utils.lib;

/**
 * Create by alv1 on 2019/6/12
 * 动态权限申请回调接口
 */
public interface ScanPermissionReCallback {
    //申请的所有的权限通过回调成功
    void onSuccess();
    //申请的权限组有一项未通过便回调失败
    void onFaile();
}
