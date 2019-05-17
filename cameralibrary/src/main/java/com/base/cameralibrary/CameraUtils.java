package com.base.cameralibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraUtils implements CameraInterface{


    public interface CallBack {
        void cameraFaile(int errCode, String message);

        void cameraSuccess(String mFilePath);
    }

    private CallBack mCallBack;

    private Camera mCamera;

    private CameraUtils() {

    }

    private static CameraUtils mCameraUtils;
    public static CameraUtils getInstance() {
        if (mCameraUtils == null) {
            mCameraUtils = new CameraUtils();
        }
        return mCameraUtils;
    }

    private FrameLayout mFrameLayout;
    private Context mContext;
    public CameraUtils initCamerView(Context context, FrameLayout frameLayout, CallBack callBack) {
        this.mContext = context;
        this.mCallBack = callBack;
        this.mFrameLayout = frameLayout;
        onPermissionRequests(Manifest.permission.CAMERA);
        return mCameraUtils;
    }

    private CameraUtils openCamer(int i) {
        if (mCamera != null) {
            mFrameLayout.removeAllViews();
            mCamera.stopPreview();//停掉原来摄像头的预览
            mCamera.release();//释放资源
            mCamera = null;//取消原来摄像头
        }
        //初始化 Camera对象
        mCamera = Camera.open(i);
        //自动连续对焦
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            // 连续对焦模式
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        mCamera.setParameters(parameters);
        CameraPreview mPreview = new CameraPreview(mContext, mCamera);
        mFrameLayout.addView(mPreview);
        return mCameraUtils;
    }


    //获取照片中的接口回调
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream fos = null;
            String mFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + System.currentTimeMillis() + ".png";
            //文件
            File tempFile = new File(mFilePath);
            try {
                fos = new FileOutputStream(tempFile);
                fos.write(data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                if (mCallBack != null) {
                    mCallBack.cameraFaile(-1, "创建文件失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (mCallBack != null) {
                    mCallBack.cameraFaile(-2, "读写文件失败");
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                amendRotatePhoto(mFilePath,mContext);
            }
        }
    };

    public void reCameraClick() {
        //实现连续拍多张的效果
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }


    private void onPermissionRequests(String permission) {
        if (ContextCompat.checkSelfPermission(mContext,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            Log.d("MainActivity", "1");
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                    Manifest.permission.READ_CONTACTS)) {
                //权限已有
                openCamer(0);
            } else {
                //没有权限，申请一下
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{permission},
                        1);
            }
        } else {
            openCamer(0);
        }
    }

    /**
     * 处理旋转后的图片
     *
     * @param originpath 原图路径
     * @param context    上下文
     * @return 返回修复完毕后的图片路径
     */
    public  String amendRotatePhoto(String originpath, Context context) {

        // 取得图片旋转角度
        int angle = 90;
        if (cameraPosition ==1) {
            angle=90;
        }else {
            angle=-90;
        }
        // 把原图压缩后得到Bitmap对象
        Bitmap bmp = getCompressPhoto(originpath);;

        // 修复图片被旋转的角度
        Bitmap bitmap = rotaingImageView(angle, bmp);

        // 保存修复后的图片并返回保存后的图片路径
        return savePhotoToSD(bitmap, context,originpath);
    }

    /**
     * 存放拍摄图片的文件夹
     */
    private static final String FILES_NAME = "/MyPhoto";
    /**
     * 图片种类
     */
    public static final String IMAGE_TYPE = ".png";
    /**
     * 使用当前系统时间作为上传图片的名称
     *
     * @return 存储的根路径+图片名称
     */
    public static String getPhotoFileName(Context context) {
        return Environment.getExternalStorageDirectory().getPath() + File.separator + System.currentTimeMillis() + IMAGE_TYPE;
    }
    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param mbitmap 需要保存的Bitmap图片
     * @param originpath
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    public  String savePhotoToSD(Bitmap mbitmap, Context context, String originpath) {
        FileOutputStream outStream = null;
        String fileName = getPhotoFileName(context);
        try {
            outStream = new FileOutputStream(fileName);
            // 把数据写入文件，100表示不压缩
            mbitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            if (mCallBack != null) {
                mCallBack.cameraSuccess(fileName);
            }
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (mbitmap != null) {
                    mbitmap.recycle();
                }
                File lFile = new File(originpath);
                if (lFile.exists()) {
                    lFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 把原图按1/10的比例压缩
     *
     * @param path 原图的路径
     * @return 压缩后的图片
     */
    public static Bitmap getCompressPhoto(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;  // 图片的大小设置为原来的十分之一
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        options = null;
        return bmp;
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }


    public void destore() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }


    //切换摄像头
    //当前选用的摄像头，1后置 0前置
    private int cameraPosition = 1;

    @Override
    public void changeCameraClick() {
        int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            //得到每一个摄像头的信息
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraPosition == 1) {
                //现在是后置，变更为前置
                //代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    //重新打开
                    reStartCamera(i);
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                //代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    reStartCamera(i);
                    cameraPosition = 1;
                    break;
                }
            }
        }
    }

    //重新打开预览
    private void reStartCamera(int i) {
        openCamer(i);
    }




    /**
     * 拍照
     */
    @Override
    public void onCameraClick() {
        //得到照相机的参数
        Camera.Parameters parameters = mCamera.getParameters();
        //图片的格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        //预览的大小是多少
        ViewGroup.LayoutParams lLayoutParams = mFrameLayout.getLayoutParams();
        parameters.setPreviewSize(lLayoutParams.width, lLayoutParams.height);
        mCamera.takePicture(null, null, mPictureCallback);

    }

}
