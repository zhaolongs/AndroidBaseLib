package com.studyyoun.androidbaselibrary.view;
/**
 * Created by zhaolong on 2017/9/27.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * class infation
 */
public class CustomShareLoadingView extends View {

    private Paint mBackgroundPaint;
    private float mScaledDensity;
    //背景的宽与高
    private int mBgWidth;
    private int mBgHeight;

    //屏幕的宽与高
    private int mScreenWidth=0;
    private int mScreenHeight=0;

    //圆角
    private int mBgRx;
    private int mBgRy;
    private RectF mBgRectF;

    //误差
    private int flag;
    private Paint mTextdPaint;
    private String mMsgText="W";
    private float mMsgTextLength;
    private float mMsgTextHeight;
    private Paint mCirclPaint;
    private Paint mCirclRingPaint;


    private float mCirclRingRoateAle=0;
    private ValueAnimator mValueAnimator;

    public CustomShareLoadingView(Context context) {
        super(context);
        init(context,null,0);
    }

    public CustomShareLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public CustomShareLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr){


        mValueAnimator = ValueAnimator.ofFloat(0,1f);
        mValueAnimator.setDuration(380);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCirclRingRoateAle=360*((Float)valueAnimator.getAnimatedValue());
                invalidate();
            }
        });
        mValueAnimator.setRepeatCount(Integer.MAX_VALUE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        //屏幕密度缩放比例
        mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

        //背景
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(Color.parseColor("#002A2A2A"));

        //圆
        mCirclPaint = new Paint();
        mCirclPaint.setAntiAlias(true);
        mCirclPaint.setDither(true);
        mCirclPaint.setStyle(Paint.Style.FILL);
        mCirclPaint.setColor(Color.parseColor("#ffffff"));

        //环
        mCirclRingPaint = new Paint();
        mCirclRingPaint.setAntiAlias(true);
        mCirclRingPaint.setDither(true);
        mCirclRingPaint.setStyle(Paint.Style.STROKE);
        mCirclRingPaint.setStrokeWidth(2.6f*mScaledDensity);
        mCirclRingPaint.setColor(Color.parseColor("#ffcc00"));

        mBgWidth = (int) (60*mScaledDensity);
        mBgHeight = (int) (60*mScaledDensity);
        mBgRx= (int) (8*mScaledDensity);
        mBgRy= (int) (8*mScaledDensity);

        flag= (int) (16*mScaledDensity);


        mTextdPaint = new Paint();
        mTextdPaint.setAntiAlias(true);
        mTextdPaint.setDither(true);
        mTextdPaint.setTextSize(20*mScaledDensity);
        mTextdPaint.setStyle(Paint.Style.STROKE);
        mTextdPaint.setColor(Color.parseColor("#000000"));




    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth=w;
        mScreenHeight=h;

        mBgRectF = new RectF( -mBgWidth / 2, - mBgHeight / 2,  mBgWidth/2, mBgHeight/2);

        mMsgTextLength = mTextdPaint.measureText(mMsgText);
        //文字的y轴坐标
        Paint.FontMetrics fontMetrics = mTextdPaint.getFontMetrics();
        mMsgTextHeight = (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;

        int[] mMinColors = {Color.YELLOW, Color.GREEN, Color.WHITE, Color.YELLOW};
        //先创建一个渲染器
        SweepGradient mSweepGradient = new SweepGradient(0,
                0, //以圆弧中心作为扫描渲染的中心以便实现需要的效果
                mMinColors, //这是我定义好的颜色数组，包含2个颜色：#35C3D7、#2894DD
                null);
        //把渐变设置到笔刷
        mCirclRingPaint.setShader(mSweepGradient);



    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.translate(mScreenWidth/2,mScreenHeight/2);

        if (mBgRectF != null) {
            canvas.drawRoundRect(mBgRectF,mBgRx,mBgRy,mBackgroundPaint);
        }

        canvas.drawCircle(0,0,mBgWidth*0.4f,mCirclPaint);

        canvas.drawText(mMsgText,-mMsgTextLength/2,mMsgTextHeight,mTextdPaint);


        canvas.rotate(mCirclRingRoateAle);

        canvas.drawCircle(0,0,mBgWidth*0.34f,mCirclRingPaint);
    }


    public void start(){

            mValueAnimator.start();

    }

    public void close(){
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }

    }
}
