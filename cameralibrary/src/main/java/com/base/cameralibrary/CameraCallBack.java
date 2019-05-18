package com.base.cameralibrary;

public interface CameraCallBack {
    void cameraFaile(int errCode, String message);
    void cameraSuccess(String mFilePath);
    void cameraPermisExit();
}
