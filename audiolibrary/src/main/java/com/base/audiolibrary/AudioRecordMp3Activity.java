package com.base.audiolibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.czt.mp3recorder.MP3Recorder;
import com.shuyu.waveview.AudioPlayer;
import com.shuyu.waveview.AudioWaveView;
import com.shuyu.waveview.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.UUID;


public class AudioRecordMp3Activity extends Activity {
	private static final String TAG = "AudioRecordMp3Activity";
	
	AudioWaveView audioWave;
	Button record;
	Button stop;
	Button play;
	Button reset;
	Button wavePlay;
	TextView playText;
	ImageView colorImg;
	Button recordPause;
	Button popWindow;
	ViewGroup rootView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_audio_mp3_record);
		
		audioWave = findViewById(R.id.audioWave);
		play = findViewById(R.id.play);
		stop = findViewById(R.id.stop);
		record = findViewById(R.id.record);
		
		rootView = findViewById(R.id.rootView);
		popWindow = findViewById(R.id.popWindow);
		recordPause = findViewById(R.id.recordPause);
		colorImg = findViewById(R.id.colorImg);
		
		playText = findViewById(R.id.playText);
		wavePlay = findViewById(R.id.wavePlay);
		reset = findViewById(R.id.reset);
		
		
		audioPlayer = new AudioPlayer(this, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
					case AudioPlayer.HANDLER_CUR_TIME://更新的时间
						curPosition = (int) msg.obj;
						playText.setText(toTime(curPosition) + " / " + toTime(duration));
						break;
					case AudioPlayer.HANDLER_COMPLETE://播放结束
						playText.setText(" ");
						mIsPlay = false;
						break;
					case AudioPlayer.HANDLER_PREPARED://播放开始
						duration = (int) msg.obj;
						playText.setText(toTime(curPosition) + " / " + toTime(duration));
						break;
					case AudioPlayer.HANDLER_ERROR://播放错误
						resolveResetPlay();
						break;
				}
				
			}
		});
		
		
		record.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resolveRecord();
			}
		});
		recordPause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resolvePause();
			}
		});
		stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resolveStopRecord();
			}
		});
		
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resolvePlayRecord();
			}
		});
		
		reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resolveResetPlay();
				
			}
		});
	}
	
	
	MP3Recorder mRecorder;
	AudioPlayer audioPlayer;
	String filePath;
	boolean mIsRecord = false;
	boolean mIsPlay = false;
	int duration;
	int curPosition;
	
	@Override
	public void onPause() {
		super.onPause();
		if (mIsRecord) {
			resolveStopRecord();
		}
		if (mIsPlay) {
			audioPlayer.pause();
			audioPlayer.stop();
		}
		
	}
	
	
	
	/**
	 * 开始录音
	 */
	private void resolveRecord() {
		filePath = FileUtils.getAppPath();
		File file = new File(filePath);
		if (!file.exists()) {
			if (!file.mkdirs()) {
				Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		int offset = dip2px(this, 1);
		filePath = FileUtils.getAppPath() + UUID.randomUUID().toString() + ".mp3";
		mRecorder = new MP3Recorder(new File(filePath));
		int size = getScreenWidth(this) / offset;//控件默认的间隔是1
		mRecorder.setDataList(audioWave.getRecList(), size);
		
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
					Toast.makeText(AudioRecordMp3Activity.this, "没有麦克风权限", Toast.LENGTH_SHORT).show();
					resolveError();
				}
			}
		});
		
		audioWave.setBaseRecorder(mRecorder);
		
		try {
			mRecorder.start();
			audioWave.startView();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "录音出现异常", Toast.LENGTH_SHORT).show();
			resolveError();
			return;
		}
		resolveRecordUI();
		mIsRecord = true;
	}
	
	/**
	 * 停止录音
	 */
	private void resolveStopRecord() {
		resolveStopUI();
		if (mRecorder != null && mRecorder.isRecording()) {
			mRecorder.setPause(false);
			mRecorder.stop();
			audioWave.stopView();
		}
		mIsRecord = false;
		recordPause.setText("暂停");
		
	}
	
	/**
	 * 录音异常
	 */
	private void resolveError() {
		resolveNormalUI();
		FileUtils.deleteFile(filePath);
		filePath = "";
		if (mRecorder != null && mRecorder.isRecording()) {
			mRecorder.stop();
			audioWave.stopView();
		}
	}
	
	/**
	 * 播放
	 */
	private void resolvePlayRecord() {
		if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
			Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
			return;
		}
		Log.d(TAG,"filePath "+filePath);
		playText.setText(" ");
		mIsPlay = true;
		audioPlayer.playUrl(filePath);
		resolvePlayUI();
	}
	
	/**
	 * 播放
	 */
	private void resolvePlayWaveRecord() {
		if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
			Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
			return;
		}
		resolvePlayUI();
		Intent intent = new Intent(this, AudioPlayActivity.class);
		intent.putExtra("uri", filePath);
		startActivity(intent);
	}
	
	/**
	 * 重置
	 */
	private void resolveResetPlay() {
		filePath = "";
		playText.setText("");
		if (mIsPlay) {
			mIsPlay = false;
			audioPlayer.pause();
		}
		resolveNormalUI();
	}
	
	/**
	 * 暂停
	 */
	private void resolvePause() {
		if (!mIsRecord)
			return;
		resolvePauseUI();
		if (mRecorder.isPause()) {
			resolveRecordUI();
			audioWave.setPause(false);
			mRecorder.setPause(false);
			recordPause.setText("暂停");
		} else {
			audioWave.setPause(true);
			mRecorder.setPause(true);
			recordPause.setText("继续");
		}
	}
	
	private String toTime(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
		String dateString = formatter.format(time);
		return dateString;
	}
	
	private void resolveNormalUI() {
		record.setEnabled(true);
		recordPause.setEnabled(false);
		stop.setEnabled(false);
		play.setEnabled(false);
		wavePlay.setEnabled(false);
		reset.setEnabled(false);
	}
	
	private void resolveRecordUI() {
		record.setEnabled(false);
		recordPause.setEnabled(true);
		stop.setEnabled(true);
		play.setEnabled(false);
		wavePlay.setEnabled(false);
		reset.setEnabled(false);
	}
	
	private void resolveStopUI() {
		record.setEnabled(true);
		stop.setEnabled(false);
		recordPause.setEnabled(false);
		play.setEnabled(true);
		wavePlay.setEnabled(true);
		reset.setEnabled(true);
	}
	
	private void resolvePlayUI() {
		record.setEnabled(false);
		stop.setEnabled(false);
		recordPause.setEnabled(false);
		play.setEnabled(true);
		wavePlay.setEnabled(true);
		reset.setEnabled(true);
	}
	
	private void resolvePauseUI() {
		record.setEnabled(false);
		recordPause.setEnabled(true);
		stop.setEnabled(false);
		play.setEnabled(false);
		wavePlay.setEnabled(false);
		reset.setEnabled(false);
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
