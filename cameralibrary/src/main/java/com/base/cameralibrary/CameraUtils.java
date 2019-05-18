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
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CameraUtils implements CameraInterface {


    private CameraCallBack mCallBack;
    private Camera mCamera;
    private static CameraUtils mCameraUtils;

    private CameraUtils() {
        initPicSelectFunction();
    }


    public static CameraUtils getInstance() {
        if (mCameraUtils == null) {
            mCameraUtils = new CameraUtils();
        }
        return mCameraUtils;
    }

    private FrameLayout mFrameLayout;
    private Context mContext;
    private int mPicWidth;
    private int mPicHeight;

    public CameraUtils initCamerView(Context context, FrameLayout frameLayout, int picWidth, int picHeight, CameraCallBack callBack) {
        this.mContext = context;
        this.mCallBack = callBack;
        this.mFrameLayout = frameLayout;
        this.mPicWidth = picWidth;
        this.mPicHeight = picHeight;
        if (!checkPermissions(permissions)) {
            ActivityCompat.requestPermissions((Activity) context, permissions, 0);
        } else {
            openCamer(0);
        }
        return mCameraUtils;
    }

    private boolean checkPermissions(String[] permissions) {
        for (String perm : permissions) {
            if (ActivityCompat.checkSelfPermission(mContext, perm) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
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
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            // 连续对焦模式
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        //图片的格式
        parameters.setPictureFormat(ImageFormat.JPEG);
        //获取Camera所支持的图片尺寸
        List<Camera.Size> pictureSizes = mCamera.getParameters().getSupportedPictureSizes();
        List<Camera.Size> previewSizes = mCamera.getParameters().getSupportedPreviewSizes();
        //校验用户要获取的图片的尺寸
        if (pictureSizes != null && pictureSizes.size() > 0) {

            Collections.sort(pictureSizes, new Comparator<Camera.Size>() {
                public int compare(Camera.Size h1, Camera.Size h2) {
                    if (h1.width > h2.width) {
                        return 1;
                    } else if (h1.width == h2.width) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });

            if (pictureSizes.contains(mPicWidth)) {
                for (Camera.Size lPictureSize : pictureSizes) {
                    if (lPictureSize.width == mPicWidth) {
                        mPicHeight = lPictureSize.height;
                        break;
                    }
                }
            } else {
                //取出最大的
                Camera.Size lSize = pictureSizes.get(pictureSizes.size() - 1);
                if (mPicWidth > lSize.width) {
                    mPicWidth = lSize.width;
                    mPicHeight = lSize.height;
                } else {
                    for (Camera.Size lPictureSize : pictureSizes) {
                        if (lPictureSize.width >= mPicWidth) {
                            mPicWidth = lPictureSize.width;
                            mPicHeight = lPictureSize.height;
                            break;
                        }
                    }
                }
            }
        }
        parameters.setPictureSize(mPicWidth, mPicHeight);
        try {
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            try {
                parameters.setPictureSize(1920, 1080);
                mCamera.setParameters(parameters);
            } catch (Exception ignored) {
            }
        }

        CameraPreview mPreview = new CameraPreview(mContext, mCamera);
        mFrameLayout.addView(mPreview);
        return mCameraUtils;
    }

    //获取照片中的接口回调
    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            if (CameraUtils.this.mIsContinuous) {
                mTakePictureCount = 0;
                mCamera.startPreview();
            }
            CameraImageShowAsyncTask lAsyncTask = new CameraImageShowAsyncTask();
            lAsyncTask.execute(data);
        }
    };

    private String[] permissions = new String[3];

    private void initPicSelectFunction() {
        permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        permissions[1] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[2] = Manifest.permission.CAMERA;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (checkPermissions(permissions)) {
            openCamer(0);
        } else {
            showReasonAndRequestAgainIfCouldbe();
        }
    }

    private void showReasonAndRequestAgainIfCouldbe() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA)) {
            showAlert("该功能需要您授予拍照权限,否则将无法使用", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA}, 0);
                }
            });
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.CAMERA) && ActivityCompat.checkSelfPermission(((Activity) mContext), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {//用户拒绝并勾选了“不再提示”框，跳转应用设置界面

            showAlert("该功能需要您授予拍照权限,请手动开启权限", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri packageURI = Uri.parse("package:" + ((Activity) mContext).getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    ((Activity) mContext).startActivity(intent);
                }
            });
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity) mContext), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showAlert("该功能需要读取您的内存卡上的图片", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(((Activity) mContext), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            });

        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(((Activity) mContext), Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.checkSelfPermission(((Activity) mContext), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //用户拒绝并勾选了“不再提示”框，跳转应用设置界面
            showAlert("该功能需要读取您的内存卡上的图片,请手动开启权限", new DialogInterface.OnClickListener() {
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
                .setTitle("需要权限")
                .setMessage(message)
                .setPositiveButton("同意", agreesListerner)
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mCallBack != null) {
                            mCallBack.cameraPermisExit();
                        }
                    }
                })
                .create().show();
    }

    class CameraImageShowAsyncTask extends AsyncTask<byte[], Void, String> {

        @Override
        protected String doInBackground(byte[]... bytes) {
            byte[] data = bytes[0];
            Bitmap lBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            // 取得图片旋转角度
            int angle = 90;
            if (cameraPosition == 1) {
                angle = 90;
            } else {
                angle = -90;
            }
            String lS;
            Bitmap returnBm = null;
            // 根据旋转角度，生成旋转矩阵
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            try {
                // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
                returnBm = Bitmap.createBitmap(lBitmap, 0, 0, lBitmap.getWidth(), lBitmap.getHeight(), matrix, true);
                lS = savePhotoToSD(returnBm, mContext);

            } catch (OutOfMemoryError e) {
                returnBm = null;
                lS = null;
            }
            return lS;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if (string != null) {
                if (mCallBack != null) {
                    mCallBack.cameraSuccess(string);
                }
            } else {
                if (mCallBack != null) {
                    mCallBack.cameraFaile(-1, "内存异常");
                }
            }
        }
    }


    /**
     * 图片种类
     */
    public static final String IMAGE_TYPE = ".jpeg";

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
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    public String savePhotoToSD(Bitmap mbitmap, Context context) {
        FileOutputStream outStream = null;
        String fileName = getPhotoFileName(context);
        try {
            outStream = new FileOutputStream(fileName);
            // 把数据写入文件，100表示不压缩
            mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    @Override
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

    //是否默认连续拍照
    private boolean mIsContinuous = false;

    //重新打开预览
    private void reStartCamera(int i) {
        openCamer(i);
    }

    public CameraUtils setContinuous(boolean continuous) {
        this.mIsContinuous = continuous;
        return mCameraUtils;
    }

    @Override
    public void reCameraClick() {
        //实现连续拍多张的效果
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }


    private int mTakePictureCount = 0;

    /**
     * 拍照
     */
    @Override
    public void onCameraClick() {
        //camera资源是典型的C/S架构的服务，每次使用完毕需注意回收release()
        if (mTakePictureCount == 0) {
            this.mTakePictureCount++;
            mCamera.takePicture(null, null, mPictureCallback);
        } else {
            mCamera.startPreview();
            this.mTakePictureCount = 0;
        }
    }
}
