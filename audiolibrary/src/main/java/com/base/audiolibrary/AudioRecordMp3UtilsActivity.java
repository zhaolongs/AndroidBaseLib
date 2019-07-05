package com.base.audiolibrary;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.AudioMp3RecoderUtils;
import com.shuyu.waveview.AudioWaveView;


public class AudioRecordMp3UtilsActivity extends Activity {
	private static final String TAG = AudioRecordMp3UtilsActivity.class.getSimpleName();
	
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
	private AudioMp3RecoderUtils mAudioMp3RecoderUtils;
	
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
		
		mAudioMp3RecoderUtils = new AudioMp3RecoderUtils(this);
		mAudioMp3RecoderUtils.setRecoderCallBack(new AudioMp3RecoderUtils.RecoderCallBack() {
			@Override
			public void onFaile(String message) {
				Log.e(TAG,message);
			}
			
			@Override
			public void onSuccess(String filePath) {
				Log.d(TAG,filePath);
			}
			
			@Override
			public void onIsRecord(boolean isRecord) {
				Log.e(TAG,"onIsRecord "+isRecord);
			}
		});
		record.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAudioMp3RecoderUtils.resolveRecord();
			}
		});
		recordPause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAudioMp3RecoderUtils.resolvePause();
			}
		});
		stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAudioMp3RecoderUtils.resolveStopRecord();
			}
		});

		
	}
	
	
}
