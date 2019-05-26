package com.base.cameralibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.FrameLayout;

import com.base.cameralibrary.callback.CameraCallBack;
import com.base.cameralibrary.callback.CameraPhotoGraphCallback;
import com.base.cameralibrary.presenter.CameraOpenPresenter;
import com.base.cameralibrary.presenter.CameraPhotoPresenter;
import com.base.cameralibrary.utils.CameraPhotoFromPhotoAlbum;
import com.base.cameralibrary.view.CameraPreview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CameraUtils implements CameraContact.CommonCameraInterface {


    private static CameraUtils mCameraUtils;
    private final CameraOpenPresenter mCameraOpenPresenter;
    private final CameraPhotoPresenter mCameraPhotoPresenter;

    private CameraUtils() {
        mCameraOpenPresenter = new CameraOpenPresenter();
        mCameraPhotoPresenter = new CameraPhotoPresenter();
    }
    public static CameraUtils getInstance() {
        if (mCameraUtils == null) {
            mCameraUtils = new CameraUtils();
        }
        return mCameraUtils;
    }
    public CameraUtils initCamerView(Context context, FrameLayout frameLayout, int picWidth, int picHeight, CameraCallBack callBack) {
        mCameraOpenPresenter.init(context,frameLayout,picWidth,picHeight,callBack);
        return mCameraUtils;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mCameraOpenPresenter.onRequestPermissionsResult(permissions);
    }
    @Override
    public void destore() {
        mCameraOpenPresenter.destore();
    }

    @Override
    public void changeCameraClick() {
        mCameraOpenPresenter.changeCameraClick();
    }
    @Override
    public CameraUtils setContinuous(boolean continuous) {
        mCameraOpenPresenter.setContinuous(continuous);
        return mCameraUtils;
    }
    @Override
    public void reCameraClick() {
        mCameraOpenPresenter.reCameraClick();
    }
    @Override
    public void start(){
        mCameraOpenPresenter.start();
    }
    @Override
    public void stop(){
        mCameraOpenPresenter.stop();
    }

    @Override
    public void onCameraClick() {
        mCameraOpenPresenter.onCameraClick();
    }
    @Override
    public void openCapTureGroupFunction(Context context) {
        mCameraPhotoPresenter.openCapTureGroupFunction(context);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, Context context, CameraPhotoGraphCallback callback) {
        mCameraPhotoPresenter.onActivityResult(requestCode,resultCode,data,context,callback);
    }
    @Override
    public void openCameraFlashFunction() {
        mCameraOpenPresenter.openCameraFlashFunction();
    }
}
