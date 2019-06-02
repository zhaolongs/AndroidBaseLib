package com.studyyoun.androidbaselibrary.version;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
/**
 * Created by androidlongs
 * 站在顶峰，看世界
 * 落在谷底，思人生
 *
 * android 版本更新终极解决方案
 * 1 获取本公司服务器 版本信息 判断是否需要进行更新
 * 2 需要更新  下载apk到本地 下载前 调用checkSelfPermission方法，在permissinCallBack 中处理权限申请情况
 * 3 用户同意文件写入权限后会触发在permissinCallBack回调的onPermissSuccess（）方法，在些方法中开启下载apk
 * 4 下载成功后 调用 install方法触发安装操作，参数为刚刚下载保存的 apk的路径，apk 安装失败 回调installApkCallBack
 *
 * 需要注意的是 在使用版本更新的activity中，在方法onRequestPermissionsResult 中回调本AppVersionContact的方法onRequestPermissionsResult，
 * 以处理权限回调
 * 在使用版本更新的activity中，在方法 onActivityResult 中回调本AppVersionContact的方法onActivityResult，apk安装失败和
 * 9.0 手机 从应用设置页面 允许安装未知来源的应用页面 回调
 */
public interface AppVersionContact {

    //检查文件写入权限 在下载apk前就需要调用
    void checkSelfPermission(Context context, PermissinCallBack permissinCallBack);

    //下载完成apk 调用此方法安装apk 安装失败 回调installApkCallBack
    AppVersionManager install(String apkFilePath, InstallApkCallBack installApkCallBack);

    //权限请求回调 文件写权限
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    //权限 允许安装未知来源的应用 回调 或者是安装apk失败的回调 应在activity的 onActivityResult 方法中回调
    void onActivityResult(int requestCode, int resultCode, Intent data);

    public interface PermissinCallBack {

        void onPermissSuccess();

        void onPermissFaile(int i, String errMsg);
    }

    public interface InstallApkCallBack {

        void onInstallSuccess();

        void onInstallFaile(int i, String errMsg);
    }
    public interface InstallCallBack {
        void onFaile(String msg);
    }

}
