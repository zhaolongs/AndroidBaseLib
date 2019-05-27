package com.studyyoun.androidbaselibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.studyyoun.androidbaselibrary.R;


/**
 * 自动上下扫描
 */

public class AutoScannerView extends View {

    private static final String TAG = AutoScannerView.class.getSimpleName();

    private Paint maskPaint;
    private Paint linePaint;
    private Paint traAnglePaint;
    private Paint textPaint;

    private final int maskColor = Color.parseColor("#60000000");                          //蒙在摄像头上面区域的半透明颜色
    private final int triAngleColor = Color.parseColor("#76EE00");                        //边角的颜色
    private final int lineColor = Color.parseColor("#FF0000");                            //中间线的颜色
    private final int textColor = Color.parseColor("#CCCCCC");                            //文字的颜色
    private final int triAngleLength = dp2px(20);                                         //每个角的点距离
    private final int triAngleWidth = dp2px(4);                                           //每个角的点宽度
    private final int textMarinTop = dp2px(30);                                           //文字距离识别框的距离
    private int lineOffsetCount = 0;

    public AutoScannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskPaint.setColor(maskColor);

        traAnglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        traAnglePaint.setColor(triAngleColor);
        traAnglePaint.setStrokeWidth(triAngleWidth);
        traAnglePaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(dp2px(14));
    }

    //默认view的宽度
    private int mDefaultWidth = dp2px(100);
    private int mDefaultHeight = mDefaultWidth;
    private int mDefaultPadding = dp2px(10);
    //绘制进度条的实际宽度
    private int mMeasureHeight;
    private int mMeasureWidth;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量计算宽度
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            //当specMode = EXACTLY时，精确值模式，即当我们在布局文件中为View指定了具体的大小
            mMeasureWidth = widthSpecSize;
        } else {
            //指定默认大小
            mMeasureWidth = mDefaultWidth;
            if (widthSpecMode == MeasureSpec.AT_MOST) {
                mMeasureWidth = Math.min(mMeasureWidth, widthSpecSize);
            }
        }

        //测量计算View的高
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            //当specMode = EXACTLY时，精确值模式，即当我们在布局文件中为View指定了具体的大小
            mMeasureHeight = heightSpecSize;
        } else {
            //指定默认大小
            mMeasureHeight = mDefaultHeight;
            if (heightSpecMode == MeasureSpec.AT_MOST) {
                mMeasureHeight = Math.min(mMeasureHeight, heightSpecSize);
            }
        }
        mMeasureHeight = mMeasureHeight - getPaddingBottom() - getPaddingTop();
        mMeasureWidth = mMeasureWidth - getPaddingLeft() - getPaddingBottom();
        //重新测量
        setMeasuredDimension(mMeasureWidth, mMeasureHeight);

        /**
         * 竖屏状态下
         * 将view 的宽度平均分为6份，扫描区域距左右各1份，那么扫描区域的宽度占4份
         * 将view 的调试平均分为4份，距离顶部1份，为保持与宽相等，bottom =mMeasureHeight/4+ width;
         */
        int left =mMeasureWidth/6;
        int width = mMeasureWidth/6*4;
        int top = mMeasureHeight/4;
        int bottom =mMeasureHeight/4+ width;

        if (mMeasureHeight<mMeasureWidth){
            top = mMeasureHeight/8;
            bottom =mMeasureHeight/8*6;
            left =(mMeasureWidth-(bottom-top))/2;
            width = (bottom-top);

        }
        frame = new Rect( left,top, left+width, bottom);
    }


    Rect frame;

    @Override
    protected void onDraw(Canvas canvas) {
        if (frame == null) {
            return;
        }

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // 除了中间的识别区域，其他区域都将蒙上一层半透明的图层
        canvas.drawRect(0, 0, width, frame.top, maskPaint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, maskPaint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, maskPaint);
        canvas.drawRect(0, frame.bottom + 1, width, height, maskPaint);

        String text = "将二维码放入框内，即可自动扫描";
        canvas.drawText(text, (width - textPaint.measureText(text)) / 2, frame.bottom + textMarinTop, textPaint);

        // 四个角落的三角
        Path leftTopPath = new Path();
        leftTopPath.moveTo(frame.left + triAngleLength, frame.top + triAngleWidth / 2);
        leftTopPath.lineTo(frame.left + triAngleWidth / 2, frame.top + triAngleWidth / 2);
        leftTopPath.lineTo(frame.left + triAngleWidth / 2, frame.top + triAngleLength);
        canvas.drawPath(leftTopPath, traAnglePaint);

        Path rightTopPath = new Path();
        rightTopPath.moveTo(frame.right - triAngleLength, frame.top + triAngleWidth / 2);
        rightTopPath.lineTo(frame.right - triAngleWidth / 2, frame.top + triAngleWidth / 2);
        rightTopPath.lineTo(frame.right - triAngleWidth / 2, frame.top + triAngleLength);
        canvas.drawPath(rightTopPath, traAnglePaint);

        Path leftBottomPath = new Path();
        leftBottomPath.moveTo(frame.left + triAngleWidth / 2, frame.bottom - triAngleLength);
        leftBottomPath.lineTo(frame.left + triAngleWidth / 2, frame.bottom - triAngleWidth / 2);
        leftBottomPath.lineTo(frame.left + triAngleLength, frame.bottom - triAngleWidth / 2);
        canvas.drawPath(leftBottomPath, traAnglePaint);

        Path rightBottomPath = new Path();
        rightBottomPath.moveTo(frame.right - triAngleLength, frame.bottom - triAngleWidth / 2);
        rightBottomPath.lineTo(frame.right - triAngleWidth / 2, frame.bottom - triAngleWidth / 2);
        rightBottomPath.lineTo(frame.right - triAngleWidth / 2, frame.bottom - triAngleLength);
        canvas.drawPath(rightBottomPath, traAnglePaint);

        //循环划线，从上到下
        if (lineOffsetCount > frame.bottom - frame.top - dp2px(10)) {
            lineOffsetCount = 0;
        } else {
            lineOffsetCount = lineOffsetCount + 6;
//            canvas.drawLine(frame.left, frame.top + lineOffsetCount, frame.right, frame.top + lineOffsetCount, linePaint);    //画一条红色的线
            Rect lineRect = new Rect();
            lineRect.left = frame.left;
            lineRect.top = frame.top + lineOffsetCount;
            lineRect.right = frame.right;
            lineRect.bottom = frame.top + dp2px(10) + lineOffsetCount;
            canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(R.mipmap.scanline))).getBitmap(), null, lineRect, linePaint);
        }
        postInvalidateDelayed(10L, frame.left, frame.top, frame.right, frame.bottom);
    }

    private int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
