package com.studyyoun.androidbaselibrary.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.studyyoun.androidbaselibrary.R;


/**
 * Created by androidlongs on 17/3/16.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public class YouKuProgressView   extends View {
    private Paint mPaint;
    private Path mPath;
    private Path mDstPath;
    private Path mDst2Path;
    private PathMeasure mPathMeasure;
    private float mLength;


    private int mYouKuColor = Color.BLUE;

    private float mRadius = 50;
    private float mNormalRadius = 50;

    private float mRadiusWidth = mRadius / 5;
    private ValueAnimator mYouKuProgressValueAnimator;
    private float mAnimatorValue;
    private boolean mIsDefaultStart;
    private int mViewHeight;
    private int mViewWidth;

    private Handler mHandler = new Handler();

    //当前旋转角度
    private float mCurrentAngle;
    //绘制背景颜色
    private int mBackGroundColor;
    //绘制背景圆角半径
    private int mBackGroundRx;
    //绘制背景圆角半径
    private int mBackGroundRy;
    //绘制背景标识
    private boolean mIsShowBackGround;

    public YouKuProgressView(Context context) {
        super(context);
        initFunction(context, null, 0);
    }

    public YouKuProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFunction(context, attrs, 0);
    }

    public YouKuProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFunction(context, attrs, defStyleAttr);
    }

    private void initFunction(Context context, AttributeSet attrs, int defStyleAttr) {
        initCommonFunction(context);
        if (attrs != null) {
            initAttrsFunction(context, attrs);
        }
    }

    private void initCommonFunction(Context context) {


        //paint 想着设置
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mYouKuColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRadiusWidth);

        //主Path构造
        mPath = new Path();
        mPath.addCircle(0, 0, mRadius, Path.Direction.CW);
        mPath.close();

        //目标 path构造
        mDstPath = new Path();
        mDst2Path = new Path();

        //Path测量
        mPathMeasure = new PathMeasure(mPath, false);
        //绘制总长度
        mLength = mPathMeasure.getLength();


        initValueAnimatorFunction();


    }

    private void initValueAnimatorFunction() {

        if (mYouKuProgressValueAnimator != null) {
            mYouKuProgressValueAnimator.cancel();
            mYouKuProgressValueAnimator = null;
        }


        mYouKuProgressValueAnimator = ValueAnimator.ofFloat(0, 1);
        mYouKuProgressValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });


        mYouKuProgressValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //切换绘制颜色

                switch (mCurrentProStatue) {
                    case LEVE1_TRANSLATE:
                        mYouKuProgressValueAnimator.setInterpolator(new LinearInterpolator());
                        mCurrentProStatue = PROGRE_STATUE.LEVE2_DRAW_CICRE;
                        mAnimatorValue = 0;
                        break;
                    case LEVE2_DRAW_CICRE:
                        mCurrentProStatue = PROGRE_STATUE.LEVE3_ROTE;
                        break;
                    case LEVE3_ROTE:
                        break;
                    case LEVE4_CLOSE:
                        mStopRepeatCount++;
                        break;

                }
            }
        });
        mYouKuProgressValueAnimator.setDuration(mDuration / 4);
        mYouKuProgressValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mYouKuProgressValueAnimator.setInterpolator(new OvershootInterpolator());
    }

    private void stopValueAnimatorFunction() {
        if (mYouKuProgressValueAnimator != null) {
            mYouKuProgressValueAnimator.cancel();
            mYouKuProgressValueAnimator = null;
        }
        if (mYouKuCloseListener != null) {
            mYouKuCloseListener.onClose();
        }
    }

    private void initAttrsFunction(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.YouKuProgressView);

        //半径
        int radius = (int) typedArray.getDimension(R.styleable.YouKuProgressView_YouKuViewRadius, 20);
        setRadius(radius);

        //时间
        int duration = typedArray.getInteger(R.styleable.YouKuProgressView_YouKuViewDuration, 600);
        setDuration(duration);

        //时间
        int unitNumber = typedArray.getInteger(R.styleable.YouKuProgressView_YouKuViewUnitNumber, 10);
        setUnitNumber(unitNumber);

        //颜色
        int color1 = typedArray.getColor(R.styleable.YouKuProgressView_YouKuViewColor1, Color.BLUE);
        int color2 = typedArray.getColor(R.styleable.YouKuProgressView_YouKuViewColor2, Color.RED);

        setYouKuColor(color1, color2);
        //开始绘制标识
        mIsDefaultStart = typedArray.getBoolean(R.styleable.YouKuProgressView_YouKuViewIsDefaultStart, false);
        Log.d("youku","you ku progress view -- mIsDefaultStart " + mIsDefaultStart);
        if (mIsDefaultStart) {
            setYouKuStart();
        }

        //背景颜色
        mBackGroundColor = typedArray.getColor(R.styleable.YouKuProgressView_YouKuViewBackgrouncColor, Color.WHITE);
        //圆角半径
        mBackGroundRx = (int) typedArray.getDimension(R.styleable.YouKuProgressView_YouKuViewBackgrouncRx, 8);
        mBackGroundRy = (int) typedArray.getDimension(R.styleable.YouKuProgressView_YouKuViewBackgrouncRy, 8);
        //显示圆角半径
        mIsShowBackGround = typedArray.getBoolean(R.styleable.YouKuProgressView_YouKuViewBackgrouncIsShow, true);

    }

    private PROGRE_STATUE mCurrentProStatue = PROGRE_STATUE.LEVE1_TRANSLATE;
    private PROGRE_STATUE mPreProStatue = PROGRE_STATUE.LEVE1_TRANSLATE;

    public enum PROGRE_STATUE {
        LEVE1_TRANSLATE, LEVE2_DRAW_CICRE, LEVE3_ROTE, LEVE4_CLOSE
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //宽度
        mViewWidth = w;
        //高度
        mViewHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        //上下左右 内边距
        int left = getPaddingLeft();
        int bottom = getPaddingBottom();
        int top = getPaddingTop();
        int right = getPaddingRight();

        //定义默认宽
        int defaulWidth = (int) (mRadius * 3 + left + right);
        //定义默认高
        int defaulHeight = (int) (mRadius * 3 + top + bottom);

        Log.e("youku","v defaul width " + defaulWidth + "  " + defaulHeight);
        /**
         * MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求
         * MeasureSpec由size和mode组成。
         * 三种Mode：
         * 1.UNSPECIFIED
         * 父不没有对子施加任何约束，子可以是任意大小（也就是未指定）
         * (UNSPECIFIED在源码中的处理和EXACTLY一样。当View的宽高值设置为0的时候或者没有设置宽高时，模式为UNSPECIFIED
         * 2.EXACTLY
         * 父决定子的确切大小，子被限定在给定的边界里，忽略本身想要的大小。
         * (当设置width或height为match_parent时，模式为EXACTLY，因为子view会占据剩余容器的空间，所以它大小是确定的)
         * 3.AT_MOST
         * 子最大可以达到的指定大小
         * (当设置为wrap_content时，模式为AT_MOST, 表示子view的大小最多是多少，这样子view会根据这个上限来设置自己的尺寸)
         *
         * MeasureSpecs使用了二进制去减少对象的分配。
         */
        //宽度
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        if (widthModel == MeasureSpec.AT_MOST) {
            Log.e("youku","view  width model is at_most");
        } else if (widthModel == MeasureSpec.EXACTLY) {
            Log.e("youku","view width model is exactly ");
            defaulWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            Log.e("youku","view width model is UNSPECIFIED");
        }

        //高度
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);
        if (heightModel == MeasureSpec.AT_MOST) {
            Log.e("youku","view  height model  at_most ");
        } else if (heightModel == MeasureSpec.EXACTLY) {
            Log.e("youku","view  height model is  exatly ");
            defaulHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            Log.e("youku","view height model is UNSPECIFIED  ");
        }
        //设置
        setMeasuredDimension(defaulWidth, defaulHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        commonDrawFunction(canvas);
        canvas.restore();
    }


    private void commonDrawFunction(Canvas convas) {
        try {
            //画布平移
            convas.translate(mViewWidth / 2, mViewHeight / 2);
            //背景颜色
            mPaint.setColor(mBackGroundColor);
            mPaint.setStyle(Paint.Style.FILL);
            //背景
            convas.drawRoundRect(new RectF(-mViewWidth / 2, -mViewHeight / 2, mViewWidth / 2, mViewHeight), mBackGroundRx, mBackGroundRy, mPaint);
           // LogUtils.d("mCurrentAngle " + mCurrentAngle + " mAnimatorValue " + mAnimatorValue);
            if (mCurrentProStatue == PROGRE_STATUE.LEVE1_TRANSLATE) {
                //绘制 阶段一 平移
                drawOriginFunction(1, convas);
                drawOriginFunction(2, convas);
            } else if (mCurrentProStatue == PROGRE_STATUE.LEVE2_DRAW_CICRE) {
                //绘制 阶段二 画弧
                if (mPreProStatue == PROGRE_STATUE.LEVE1_TRANSLATE) {
                    mPreProStatue = PROGRE_STATUE.LEVE2_DRAW_CICRE;
                    drawOriginFunction(1, convas);
                    drawOriginFunction(2, convas);
                } else {
                    //LogUtils.e("you ku progress view -- LEVE2_DRAW_CICRE  " + mAnimatorValue);
                    //----------------------------------------------------------------------
                    defaulDrawFunction(1, convas);
                    //----------------------------------------------------------------------
                    defaulDrawFunction(2, convas);
                }
            } else if (mCurrentProStatue == PROGRE_STATUE.LEVE3_ROTE) {
                //绘制 阶段三 旋转
                //角度计算
                mCurrentAngle = mAnimatorValue * 360;
                convas.rotate(mCurrentAngle);
                //----------------------------------------------------------------------
                defaulDrawFunction(1, convas);
                //----------------------------------------------------------------------
                defaulDrawFunction(2, convas);

            } else if (mCurrentProStatue == PROGRE_STATUE.LEVE4_CLOSE) {
                //绘制 阶段四 结束
                if (mStopRepeatCount == 3) {
                    drawCloseFunction(1, convas);
                    drawCloseFunction(2, convas);
                } else if (mStopRepeatCount < 3) {
                    //角度计算
                    mCurrentAngle = mAnimatorValue * 360;
                    convas.rotate(mCurrentAngle);
                    drawCloseFunction(1, convas);
                    drawCloseFunction(2, convas);
                } else {
                    stopValueAnimatorFunction();
                    drawCloseFunction(1, convas);
                    drawCloseFunction(2, convas);
                }


            }


        } catch (Exception e) {
            Log.e("youku","you ku exception : " + e.getMessage());
        } finally {

        }

    }

    private void drawCloseFunction(int i, Canvas convas) {

        if (i == 1) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mColor1);
            if (mStopRepeatCount < 3) {
                convas.drawCircle(mRadius, 0, mRadiusWidth / 2, mPaint);
            } else if (mStopRepeatCount == 3) {
                convas.drawCircle(mRadius * (1 - mAnimatorValue), 0, mRadiusWidth / 2, mPaint);
            }else if (mStopRepeatCount == 4) {
                convas.drawCircle(0, 0, mRadiusWidth / 2, mPaint);
            }
        } else {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mColor2);
            if (mStopRepeatCount < 3) {
                convas.drawCircle(-mRadius, 0, mRadiusWidth / 2, mPaint);
            } else if (mStopRepeatCount == 3) {
                convas.drawCircle(-mRadius * (1 - mAnimatorValue), 0, mRadiusWidth / 2, mPaint);
            }else if (mStopRepeatCount == 4) {
                convas.drawCircle(0, 0, mRadiusWidth / 2, mPaint);
            }
        }

    }

    private void drawOriginFunction(int i, Canvas convas) {
        if (i == 1) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mColor1);
            convas.drawCircle(mRadius * mAnimatorValue, 0, mRadiusWidth / 2, mPaint);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mColor2);
            convas.drawCircle(-mRadius * mAnimatorValue, 0, mRadiusWidth / 2, mPaint);
        }


    }

    private void defaulDrawFunction(int number, Canvas convas) {

        for (int i = 0; i < mUnitNumber; i++) {
            drawFunction(number, convas, i);
        }

    }

    private float[] mPos1 = new float[2];
    private float[] mPos2 = new float[2];
    private float[] mTan = new float[2];

    private void drawFunction(int number, Canvas convas, int i) {

        //透明度设置
        float alpha = 255f / mUnitNumber * i;
        if (alpha >= 255) {
            alpha = 254;
        }


        mDstPath.reset();

        if (mPaint.getStrokeWidth() != mRadiusWidth) {
            mPaint.setStrokeWidth(mRadiusWidth);
        }

        //变化度计算
        float stopLength;
        float unit = 0;
        if (mCurrentProStatue == PROGRE_STATUE.LEVE2_DRAW_CICRE) {
            stopLength = mLength / 2 * mAnimatorValue;
            unit = stopLength / mUnitNumber;
        } else if (mCurrentProStatue == PROGRE_STATUE.LEVE3_ROTE) {
            stopLength = mLength / 2 * 1;
            unit = stopLength / mUnitNumber;
        }

        //绘制第一段
        if (number == 1) {

            mPaint.setColor(mColor1);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAlpha((int) alpha);
            float start = i * unit;
            mPathMeasure.getSegment(start, (i + 1) * unit, mDstPath, true);
            convas.drawPath(mDstPath, mPaint);
            if (i == (mUnitNumber - 1)) {
                mPaint.setStyle(Paint.Style.FILL);
                mPathMeasure.getPosTan((i + 1) * unit, mPos1, mTan);
                float x = mPos1[0];
                float y = mPos1[1];
                convas.drawCircle(x, y, mRadiusWidth / 2, mPaint);
            }
        } else if (number == 2) {
            //绘制第二段
            mDstPath.reset();
            mPaint.setColor(mColor2);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setAlpha((int) alpha);
            mPathMeasure.getSegment(mLength * 0.5f + i * unit, mLength * 0.5f + (i + 1) * unit, mDstPath, true);
            convas.drawPath(mDstPath, mPaint);
            if (i == (mUnitNumber - 1)) {
                mPaint.setStyle(Paint.Style.FILL);
                mPathMeasure.getPosTan(mLength * 0.5f + (i + 1) * unit, mPos2, mTan);
                float x = mPos2[0];
                float y = mPos2[1];
                convas.drawCircle(x, y, mRadiusWidth / 2, mPaint);
            }
        }
    }


    /**
     * 设置半径
     *
     * @param radius 半径
     */
    public void setRadius(int radius) {
        if (radius <= 20) {
            radius = 20;
        } else if (radius >= 80) {
            radius = 80;
        }
        this.mNormalRadius = radius;
        this.mRadius = radius;
        this.mRadiusWidth = radius / 3;
        //设置
        mPaint.setStrokeWidth(mRadiusWidth);
        //重设path
        mPath.reset();
        mPath.addCircle(0, 0, mRadius, Path.Direction.CW);
        mPath.close();
        mPathMeasure.setPath(mPath, false);
        mLength = mPathMeasure.getLength();


    }


    //更新一周期时间
    private int mDuration = 2000;

    private int mUnitNumber = 10;


    /**
     * 设置转动时间
     */
    public void setDuration(long duration) {
        if (duration <= 100) {
            duration = 100;
        } else if (duration >= 4000) {
            duration = 4000;
        }
        //更新一周期时间
        this.mDuration = (int) duration;
        //周期时间 设置

        Log.e("youku","duration " + duration);
    }

    /**
     * 半周期分割段数
     */
    public void setUnitNumber(int unitNumber) {
        if (unitNumber <= 8) {
            unitNumber = 8;
        } else if (unitNumber > 25) {
            unitNumber = 25;
        }
        this.mUnitNumber = unitNumber;
    }

    /**
     * 绘制颜色
     */
    private int mColor1 = Color.BLUE;
    private int mColor2 = Color.RED;

    public void setYouKuColor(int color1, int color2) {
        this.mColor1 = color1;
        this.mColor2 = color2;
    }


    private int mStopRepeatCount = 0;

    public void setYouKuStop() {
        //mYouKuProgressValueAnimator.cancel();
        mPreProStatue = mCurrentProStatue;
        mCurrentProStatue = PROGRE_STATUE.LEVE4_CLOSE;
        mAnimatorValue = 0;
        mStopRepeatCount = 0;
        //停止动画
        //stopValueAnimatorFunction();
    }


    public void setYouKuStart() {
        mAnimatorValue = 0f;
        mCurrentProStatue = PROGRE_STATUE.LEVE1_TRANSLATE;
        mPreProStatue = PROGRE_STATUE.LEVE1_TRANSLATE;

        //初始化
        initValueAnimatorFunction();
        //开始
        mYouKuProgressValueAnimator.start();
    }


    public interface OnYouKuCloseListener {
        void onClose();
    }

    private OnYouKuCloseListener mYouKuCloseListener;

    public void setYouKuCloseListener(OnYouKuCloseListener youKuCloseListener) {
        mYouKuCloseListener = youKuCloseListener;
    }
}
