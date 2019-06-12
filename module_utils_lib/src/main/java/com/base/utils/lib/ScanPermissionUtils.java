package com.base.utils.lib;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import static com.base.utils.lib.PermissionConfig.sPermissAlertTitle;


/**
 * Create by alv1 on 2019/6/12
 */
public class ScanPermissionUtils {

    public ScanPermissionUtils(Context context, ScanPermissionReCallback pAnalyzeCallback,String ... permissions){
        init(context,pAnalyzeCallback,permissions);
    }
    public ScanPermissionUtils(Context context, ScanPermissionReCallback pAnalyzeCallback){
        init(context,pAnalyzeCallback, mDefaultPermissions);
    }

    private void init(Context context, ScanPermissionReCallback pAnalyzeCallback, String[] permissions) {
        mContext = context;
        mScanPermissionReCallback=pAnalyzeCallback;
        initDefaultPermissionFunction();
    }

    private ScanPermissionReCallback mScanPermissionReCallback;
    private Context mContext;
    private String[] mDefaultPermissions = new String[3];

    /**
     * 初始化默认的权限申请列表
     */
    private void initDefaultPermissionFunction() {
        mDefaultPermissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        mDefaultPermissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        mDefaultPermissions[2] = Manifest.permission.CAMERA;
    }
    public void initLocationPermissionFunction() {
        mDefaultPermissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        mDefaultPermissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        mDefaultPermissions[2] = Manifest.permission.ACCESS_FINE_LOCATION;
    }

    private boolean checkPermissions(String[] permissions) {
        for (String perm : permissions) {
            if (ActivityCompat.checkSelfPermission(mContext, perm) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 权限申请的回调 需要在 申请权限的 activity 中 onRequestPermissionsResult 的方法中回调此方法
     * @param permissions
     */
    public void onRequestPermissionsResult(String[] permissions) {
        if (checkPermissions(permissions)) {
            mScanPermissionReCallback.onSuccess();
        } else {
            showReasonAndRequestAgainIfCouldbe();
        }
    }

    private void showReasonAndRequestAgainIfCouldbe() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA)) {
            showAlert(PermissionConfig.sShowApplyUseCamera, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, 0);
                }
            });
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA) && ActivityCompat.checkSelfPermission(((Activity) mContext), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {//用户拒绝并勾选了“不再提示”框，跳转应用设置界面

            showAlert(PermissionConfig.sShowApplyRefauseCamera, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri packageURI = Uri.parse("package:" + ((Activity) mContext).getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    ((Activity) mContext).startActivity(intent);
                }
            });
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity) mContext), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showAlert(PermissionConfig.sShowApplyReadPic, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(((Activity) mContext), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            });

        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(((Activity) mContext), Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.checkSelfPermission(((Activity) mContext), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //用户拒绝并勾选了“不再提示”框，跳转应用设置界面
            showAlert(PermissionConfig.sShowApplyRefauseReadPic, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri packageURI = Uri.parse("package:" + ((Activity) mContext).getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    ((Activity) mContext).startActivity(intent);
                }
            });
        }
    }

    private void showAlert(String message, DialogInterface.OnClickListener agreesListerner) {
        new AlertDialog.Builder((Activity) mContext)
                .setTitle(sPermissAlertTitle)
                .setMessage(message)
                .setPositiveButton("同意", agreesListerner)
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (PermissionConfig.sIsShowRefuseToast) {
                            Toast.makeText(mContext, PermissionConfig.sShowRefuseText, Toast.LENGTH_SHORT).show();
                        }

                        mScanPermissionReCallback.onFaile();
                    }
                })
                .create().show();
    }

    /**
     * 检查权限
     */
    public void checkPermissing(){
        if (!checkPermissions(mDefaultPermissions)) {
            ActivityCompat.requestPermissions((Activity) mContext, mDefaultPermissions, 0);
        }else {
            mScanPermissionReCallback.onSuccess();
        }
    }
}
