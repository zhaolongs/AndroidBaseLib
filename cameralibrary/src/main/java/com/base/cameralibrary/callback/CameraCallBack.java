package com.base.cameralibrary.callback;

public interface CameraCallBack {
    void cameraFaile(int errCode, String message);
    void cameraSuccess(String mFilePath);
    void cameraPermisExit();
}
