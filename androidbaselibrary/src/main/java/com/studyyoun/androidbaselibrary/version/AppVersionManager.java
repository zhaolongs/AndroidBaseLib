package com.studyyoun.androidbaselibrary.version;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.studyyoun.androidbaselibrary.utils.LogUtils;

import java.io.File;

public class AppVersionManager implements AppVersionContact {
    private Context mContext;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog mDialog;

    private PermissinCallBack mPermissinCallBack;
    private InstallApkCallBack mInstallApkCallBack;
    private String mApkFilePath;

    private static class AppVersionSiglen {
        private static AppVersionManager sMAppVersionManager = new AppVersionManager();
    }

    public static AppVersionManager getInstance() {
        return AppVersionSiglen.sMAppVersionManager;
    }

    //检查文件写入权限 在下载apk前就需要调用
    @Override
    public void checkSelfPermission(Context context, PermissinCallBack permissinCallBack) {
        if (permissinCallBack == null) {
            throw new RuntimeException("callback is null");
        }
        if (!(context instanceof Activity)) {
            throw new RuntimeException("context not is Activity");
        }
        mContext = context;
        mPermissinCallBack = permissinCallBack;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(context, permissions[0]);
            if (i != PackageManager.PERMISSION_GRANTED) {
                showDialogTipUserRequestPermission(context);
            } else {
                mPermissinCallBack.onPermissSuccess();
            }
        } else {
            mPermissinCallBack.onPermissSuccess();
        }
    }

    //下载完成apk 调用此方法安装apk 安装失败 回调installApkCallBack
    @Override
    public AppVersionManager install(String apkFilePath,InstallApkCallBack installApkCallBack) {
        mInstallApkCallBack =installApkCallBack;
        mApkFilePath = apkFilePath;
        installApkFunction();
        return getInstance();
    }

    private void showDialogTipUserRequestPermission(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle("需要您的同意")
                .setMessage("需要获取存储空间 以正常使用")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission(context);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPermissinCallBack.onPermissFaile(-1, "取消权限");
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, permissions, 321);
    }

    //权限请求回调 文件写权限 需要在Activity 的onRequestPermissionsResult 回调方法中处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = ((Activity) mContext).shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else {
                        mPermissinCallBack.onPermissFaile(-2, "您已拒绝使用");
                    }

                } else {
                    mPermissinCallBack.onPermissSuccess();
                }
            }
        }
    }


    private void showDialogTipUserGoToAppSettting() {

        // 跳转到应用设置界面
        mDialog = new AlertDialog.Builder(mContext)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许支付宝使用存储权限来保存用户数据")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPermissinCallBack.onPermissFaile(-3, "您已拒绝使用");
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        ((Activity) mContext).startActivityForResult(intent, 123);
    }

    //权限 允许安装未知来源的应用 回调 或者是安装apk失败的回调 应在activity的 onActivityResult 方法中回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(mContext, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
//
                    mPermissinCallBack.onPermissSuccess();
                }
            }
        } else if (requestCode == 10) {
            if (mApkFilePath == null) {
                mInstallApkCallBack.onInstallFaile(-1, " apk path is nul");
            } else {
                //从手机设置页面【允许安装未知来源的应用】返回到当前页面回调此方法
                installApkFunction();
            }

        } else if (requestCode == 12) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setCancelable(false);
            builder.setMessage("安装失败，请重新进入！！！！");
            builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mInstallApkCallBack.onInstallFaile(-4, " 安装失败，请重新进入！！！");
                }
            });
            builder.create().show();
        }
    }

    private void installApkFunction() {
        File lFile = new File(mApkFilePath);
        if (lFile.exists()) {
            new InstallUtil(mContext, mApkFilePath).install(new InstallCallBack() {
                @Override
                public void onFaile(String msg) {
                    LogUtils.e("intall apk faile " + msg);
                    mInstallApkCallBack.onInstallFaile(-3, " 安装失败:请到应用市场手动安装 ");
                }
            });
        } else {
            mInstallApkCallBack.onInstallFaile(-2, " apk file is nul");
        }
    }
}
