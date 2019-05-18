package com.base.cameralibrary;

public interface CameraInterface {

    //权限请求回调
    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    //销毁
    void destore();
    //切换摄像头
    void changeCameraClick();
    //重新拍照
    void reCameraClick();
    //拍照
    void onCameraClick();
}
