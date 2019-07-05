package com;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.czt.mp3recorder.MP3Recorder;
import com.shuyu.waveview.AudioPlayer;
import com.shuyu.waveview.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class AudioMp3RecoderUtils {
	
	public interface RecoderCallBack {
		void onFaile(String message);
		
		void onSuccess(String filePath);
		
		void onIsRecord(boolean isRecord);
	}
	
	private RecoderCallBack mRecoderCallBack;
	
	MP3Recorder mRecorder;
	String filePath;
	boolean mIsRecord = false;
	private Context mContext;
	
	public AudioMp3RecoderUtils(Context context){
		this.mContext = context;
	}
	
	public void setRecoderCallBack(RecoderCallBack recoderCallBack) {
		mRecoderCallBack = recoderCallBack;
	}
	
	/**
	 * 开始录音
	 */
	public void resolveRecord() {
		filePath = FileUtils.getAppPath();
		File file = null;
		try {
			file = new File(filePath);
			
			if (!file.exists()) {
				if (!file.mkdirs()) {
					if (mRecoderCallBack != null) {
						mRecoderCallBack.onFaile("创建文件失败");
					}
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			displayFrameworkBugMessageAndExit("需要访问您的内存信息，以保存录音文件");
		}
		
		
		int offset = dip2px(mContext, 1);
		filePath = FileUtils.getAppPath() + UUID.randomUUID().toString() + ".mp3";
		mRecorder = new MP3Recorder(new File(filePath));
		
		//控件默认的间隔是1
		int size = getScreenWidth(mContext) / offset;
		//mRecorder.setDataList(audioWave.getRecList(), size);
		
		//高级用法
		//int size = (getScreenWidth(this) / 2) / dip2px(this, 1);
		//mRecorder.setWaveSpeed(600);
		//mRecorder.setDataList(audioWave.getRecList(), size);
		//audioWave.setDrawStartOffset((getScreenWidth(this) / 2));
		//audioWave.setDrawReverse(true);
		//audioWave.setDataReverse(true);
		
		//自定义paint
		//Paint paint = new Paint();
		//paint.setColor(Color.GRAY);
		//paint.setStrokeWidth(4);
		//audioWave.setLinePaint(paint);
		//audioWave.setOffset(offset);
		
		mRecorder.setErrorHandler(new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == MP3Recorder.ERROR_TYPE) {
					resolveError();
					displayFrameworkBugMessageAndExit("需要访问您的麦克风");
				}
			}
		});
		
		//audioWave.setBaseRecorder(mRecorder);
		
		try {
			mRecorder.start();
		} catch (IOException e) {
			e.printStackTrace();
			resolveError();
			if (e instanceof FileNotFoundException){
				displayFrameworkBugMessageAndExit("需要访问您的内存信息，以保存录音文件");
			}else {
				if (mRecoderCallBack != null) {
					mRecoderCallBack.onFaile("录音出现异常");
				}
			}
			
			return;
		}
		mIsRecord = true;
		if (mRecoderCallBack != null) {
			mRecoderCallBack.onIsRecord(mIsRecord);
		}
	}
	
	
	/**
	 * 停止录音
	 */
	public void resolveStopRecord() {
		if (mRecorder != null && mRecorder.isRecording()) {
			mRecorder.setPause(false);
			mRecorder.stop();
		}
		mIsRecord = false;
		if (mRecoderCallBack != null) {
			mRecoderCallBack.onIsRecord(mIsRecord);
			mRecoderCallBack.onSuccess(filePath);
		}
		
	}

	/**
	 * 暂停
	 */
	public void resolvePause() {
		if (!mIsRecord)
			return;
		if (mRecorder.isPause()) {
			mRecorder.setPause(false);
		} else {
			mRecorder.setPause(true);
		}
	}
	/**
	 * 录音异常
	 */
	private void resolveError() {
		FileUtils.deleteFile(filePath);
		filePath = "";
		if (mRecorder != null && mRecorder.isRecording()) {
			mRecorder.stop();
		}
	}
	
	private void displayFrameworkBugMessageAndExit(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("提示");
		builder.setMessage(message);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showInstalledAppDetails(mContext.getApplicationContext(), mContext.getPackageName());
			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
				if (mRecoderCallBack != null) {
					mRecoderCallBack.onFaile("申请权限失败");
				}
			}
		});
		builder.show();
	}
	
	private static final String SCHEME = "package";
	/**
	 * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
	 */
	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
	/**
	 * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
	 */
	private static final String APP_PKG_NAME_22 = "pkg";
	/**
	 * InstalledAppDetails所在包名
	 */
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	/**
	 * InstalledAppDetails类名
	 */
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	
	/**
	 * 调用系统InstalledAppDetails界面显示已安装应用程序的详细信息。
	 * 对于Android 2.3（Api Level9）以上，使用SDK提供的接口； 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）。
	 *
	 * @param context
	 * @param packageName 应用程序的包名
	 */
	
	public static void showInstalledAppDetails(Context context, String packageName) {
		Intent intent = new Intent();
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts(SCHEME, packageName, null);
			intent.setData(uri);
		} else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
			// 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
			final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
					: APP_PKG_NAME_21);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName(APP_DETAILS_PACKAGE_NAME,
					APP_DETAILS_CLASS_NAME);
			intent.putExtra(appPkgName, packageName);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	
	
	/**
	 * 获取屏幕的宽度px
	 *
	 * @param context 上下文
	 * @return 屏幕宽px
	 */
	public static int getScreenWidth(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
		windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
		return outMetrics.widthPixels;
	}
	
	/**
	 * 获取屏幕的高度px
	 *
	 * @param context 上下文
	 * @return 屏幕高px
	 */
	public static int getScreenHeight(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
		windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
		return outMetrics.heightPixels;
	}
	
	/**
	 * dip转为PX
	 */
	public static int dip2px(Context context, float dipValue) {
		float fontScale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * fontScale + 0.5f);
	}
}
