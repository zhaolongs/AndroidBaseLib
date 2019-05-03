package com.studyyoun.androidbaselibrary.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.studyyoun.androidbaselibrary.crop.CropActivity;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;

public abstract class CommonBasePicSelectActivity extends CommonBaseActivity {

    private ArrayList<String> mSelectPath;
    private String[] permissions = new String[3];
    private static final int REQUEST_IMAGE = 2;

    private void initPicSelectFunction(){
        permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[2] = Manifest.permission.CAMERA;
    }

    protected void selectPicImageFunction(){
        pickPreImage(true,1);
    }
    private boolean mIsCropBoolean = false;
    protected void selectPicAndCropImageFunction(){
        mIsCropBoolean=true;
        pickPreImage(true,1);
    }
    protected void selectPicImageFunction(boolean showCamera){
        pickPreImage(showCamera,1);
    }
    protected void selectPicImageFunction(int maxNum ){
        pickPreImage(true,maxNum);
    }
    protected void selectPicImageFunction(boolean showCamera,int maxNum ){
        pickPreImage(showCamera,maxNum);
    }

    /**
     * 发起选择图片
     * @param showCamera  是否显示相机功能
     * @param maxNum  多选 1为单选，大于1为多选
     */
    private void pickPreImage(boolean showCamera,int maxNum) {
        initPicSelectFunction();
        if (!checkPermissions(permissions)) {
            ActivityCompat.requestPermissions(CommonBasePicSelectActivity.this, permissions, 0);
        } else {
            pickImage(showCamera,maxNum);
        }
    }
    /**
     * 调起选择图片功能
     */
    private void pickImage(boolean showCamera,int maxNum) {

        //多选 最多选择的图片数
        if (maxNum<=0) {
            maxNum=1;
        }

        MultiImageSelector selector = MultiImageSelector.create();
        selector.showCamera(showCamera);
        selector.count(maxNum);
        if (maxNum==1) {
            //单选
            selector.single();
        }else {
            //多选
            selector.multi();
        }
        selector.origin(mSelectPath);
        selector.start(CommonBasePicSelectActivity.this, REQUEST_IMAGE);
    }

    @Override   //只要开发者调用了requestPermissions方法,就算用户勾选了"不再提示"框，也会回调本方法
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (checkPermissions(permissions)) {
            pickImage(true,1);
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
            if (ActivityCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("需要权限")
                    .setMessage("该功能需要您授予拍照权限")
                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CommonBasePicSelectActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                        }
                    })
                    .setNegativeButton("拒绝", null)
                    .create().show();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {//用户拒绝并勾选了“不再提示”框，跳转应用设置界面
            new AlertDialog.Builder(this)
                    .setTitle("需要权限")
                    .setMessage("该功能需要您授予拍照权限,请手动开启权限")
                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri packageURI = Uri.parse("package:" + getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("拒绝", null)
                    .create().show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("需要权限")
                    .setMessage("该功能需要读取您的内存卡上的图片")
                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CommonBasePicSelectActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                        }
                    })
                    .setNegativeButton("拒绝", null)
                    .create().show();
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {//用户拒绝并勾选了“不再提示”框，跳转应用设置界面
            new AlertDialog.Builder(this)
                    .setTitle("需要权限")
                    .setMessage("该功能需要读取您的内存卡上的图片,请手动开启权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri packageURI = Uri.parse("package:" + getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .create().show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (mSelectPath != null&&mSelectPath.size()>0) {
                    if (mIsCropBoolean) {
                        //剪切
                        CropActivity.show(CommonBasePicSelectActivity.this,mSelectPath.get(0));
                    }else {
                        if (mPicSelectCallBack != null) {
                            mPicSelectCallBack.callBack(mSelectPath);
                        }
                    }
                }else {
                    if (mPicSelectCallBack != null) {
                        mPicSelectCallBack.callBack(null);
                    }
                }

            }
        }else if (requestCode==0x04){
            if (resultCode == RESULT_OK) {
                if (mPicSelectCallBack != null) {
                    String lCrop_path = data.getStringExtra("crop_path");
                    if (TextUtils.isEmpty(lCrop_path)) {
                        mPicSelectCallBack.callBack(null);
                    }else {
                        ArrayList<String> lStrings = new ArrayList<>();
                        lStrings.add(lCrop_path);
                        mPicSelectCallBack.callBack(lStrings);
                    }
                }
            }
        }
    }

    protected PicSelectCallBack  mPicSelectCallBack;
    public interface PicSelectCallBack{
        void callBack(List<String> selectPathList);
    }

}
