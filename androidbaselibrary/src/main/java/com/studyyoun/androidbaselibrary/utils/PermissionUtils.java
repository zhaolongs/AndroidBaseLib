package com.studyyoun.androidbaselibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class PermissionUtils {


    private String[] permissions = new String[5];
    private Context sMContext;
    private Activity mActivity;
    private static PermissionUtils sMPermissionUtils;

    private void initPicSelectFunction() {
        permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[2] = Manifest.permission.CAMERA;
        permissions[3] = Manifest.permission.RECORD_AUDIO;
        permissions[4] =Manifest.permission.ACCESS_FINE_LOCATION;
    }

    public static PermissionUtils getInstance() {
        if (sMPermissionUtils == null) {
            sMPermissionUtils = new PermissionUtils();
        }
        return sMPermissionUtils;
    }

    public PermissionUtils init(Activity activity) {
        mActivity = activity;
        sMContext = mActivity.getApplicationContext();
        initPicSelectFunction();
        return sMPermissionUtils;
    }


    /**
     * 检测权限
     *
     * @return
     */
    public PermissionUtils checkPermissions() {
        if (checkPermissions(permissions)) {
            Log.d("perssion ", "permission to apply for");
            if (mPermissionCallback != null) {
                mPermissionCallback.onAgreement();
            }
        } else {
            showReasonAndRequestAgainIfCouldbe();
        }
        return sMPermissionUtils;
    }

    /**
     * 用来确定用户是否授权
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (checkPermissions(permissions)) {
            Log.d("perssion ", "permission to apply for");
            if (mPermissionCallback != null) {
                mPermissionCallback.onAgreement();
            }
        } else {
            showReasonAndRequestAgainIfCouldbe();
        }
    }

    /**
     * 检查所需权限是否都授予
     *
     * @param permissions 权限数组
     * @return 判断结果
     */
    private boolean checkPermissions(String[] permissions) {
        for (String perm : permissions) {
            if (ActivityCompat.checkSelfPermission(sMContext, perm) != PackageManager.PERMISSION_GRANTED||ActivityCompat.checkSelfPermission(sMContext,perm) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 弹出对话框向用户解释为何需要该权限，并尝试再次请求权限
     * <p>
     * 注:shouldShowRequestPermissionRationale(Activity activity,String permission)方法
     * 如果用户已经授予该权限，此方法将返回false。
     * 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
     * 如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
     */
    private void showReasonAndRequestAgainIfCouldbe() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("需要您的同意")
                    .setMessage("该功能需要您授予拍照权限")
                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, 0);
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionCallback != null) {
                                mPermissionCallback.onRefuse();
                            }
                        }
                    })
                    .create().show();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA) && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {//用户拒绝并勾选了“不再提示”框，跳转应用设置界面
            new AlertDialog.Builder(mActivity)
                    .setTitle("需要您的同意")
                    .setMessage("该功能需要您授予拍照权限,请手动开启权限")
                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri packageURI = Uri.parse("package:" + mActivity.getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            mActivity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionCallback != null) {
                                mPermissionCallback.onRefuse();
                            }
                        }
                    })
                    .create().show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("需要您的同意")
                    .setMessage("该功能需要读取您的内存卡上的图片")
                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionCallback != null) {
                                mPermissionCallback.onRefuse();
                            }
                        }
                    })
                    .create().show();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {//用户拒绝并勾选了“不再提示”框，跳转应用设置界面
            new AlertDialog.Builder(mActivity)
                    .setTitle("需要您的同意")
                    .setMessage("该功能需要读取您的内存卡上的图片,请手动开启权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri packageURI = Uri.parse("package:" + mActivity.getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            mActivity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionCallback != null) {
                                mPermissionCallback.onRefuse();
                            }
                        }
                    })
                    .create().show();
        }else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.RECORD_AUDIO)) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("需要您的同意")
                    .setMessage("该功能需要读取您的内存卡上的图片")
                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionCallback != null) {
                                mPermissionCallback.onRefuse();
                            }
                        }
                    })
                    .create().show();
        }else if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.RECORD_AUDIO) && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {//用户拒绝并勾选了“不再提示”框，跳转应用设置界面
            new AlertDialog.Builder(mActivity)
                    .setTitle("需要您的同意")
                    .setMessage("该功能需要使用您的录音功能,请手动开启权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri packageURI = Uri.parse("package:" + mActivity.getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            mActivity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionCallback != null) {
                                mPermissionCallback.onRefuse();
                            }
                        }
                    })
                    .create().show();
            // Manifest.permission.RECORD_AUDIO
        }else if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(mActivity)
                    .setTitle("需要您的同意")
                    .setMessage("需要获取您的位置用来打卡")
                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionCallback != null) {
                                mPermissionCallback.onRefuse();
                            }
                        }
                    })
                    .create().show();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {//用户拒绝并勾选了“不再提示”框，跳转应用设置界面
            new AlertDialog.Builder(mActivity)
                    .setTitle("需要您的同意")
                    .setMessage("需要获取您的位置用来打卡,请手动开启权限")
                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri packageURI = Uri.parse("package:" + mActivity.getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            mActivity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mPermissionCallback != null) {
                                mPermissionCallback.onRefuse();
                            }
                        }
                    })
                    .create().show();
        }

    }


    public interface PermissionCallback {
        void onAgreement();

        void onRefuse();
    }

    private PermissionCallback mPermissionCallback;

    public void setPermissionCallback(PermissionCallback permissionCallback) {
        mPermissionCallback = permissionCallback;
    }
}
