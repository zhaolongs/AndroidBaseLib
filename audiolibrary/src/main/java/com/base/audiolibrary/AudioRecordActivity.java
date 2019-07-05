package com.base.audiolibrary;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.AudioFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class AudioRecordActivity extends Activity{
    private static final String TAG = "AudioRecordActivity";


    private View mRecordBtn;
    private View mDeleteBtn;
    private View mConcatBtn;

    private String mRecordErrorMsg;
    private AudioRecoderUtils mAudioRecoderUtils;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_audio_record);
    
    
        mAudioRecoderUtils = new AudioRecoderUtils();
        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {
            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                //根据分贝值来设置录音时话筒图标的上下波动，下面有讲解
                
            }
            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                Toast.makeText(AudioRecordActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
              
            }
        });
        
        mRecordBtn = findViewById(R.id.record);
        mDeleteBtn = findViewById(R.id.delete);
        mConcatBtn = findViewById(R.id.concat);
        
        mRecordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                      
                        mAudioRecoderUtils.startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        mAudioRecoderUtils.stopRecord();  //结束录音（保存录音文件）
//      mAudioRecoderUtils.cancelRecord(); //取消录音（不保存录音文件）
                      
                        break;
                }
                return true;
            }
        });
    }
}
