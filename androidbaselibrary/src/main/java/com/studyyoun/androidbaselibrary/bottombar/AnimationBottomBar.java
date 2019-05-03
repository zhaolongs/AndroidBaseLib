package com.studyyoun.androidbaselibrary.bottombar;
/**
 * Created by zhaolong on 2017/10/23.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.studyyoun.androidbaselibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * class infation
 */
public class AnimationBottomBar extends ViewGroup {

    //bottombar中能存在的item最大数;
    private static final int CHILDCOUNTMAX = 5;

    private ArrayList<BottomItem> mBottomItemArrayList = new ArrayList<>();
    private Paint mPaint = new Paint();
    private Context mContext;
    private int itemCount;//item的数量
    private int childCount;//子view的数量
    private int barHeight;//bar的高度
    private int barWidth;//bar的宽度
    private int itemWidth;//平均每个item的宽度
    private int selectIndex = 0;//当前选中的位置
    private int selectLastIndex = 0;//上次选中的位置
    private float touchDownX;//记录手指第一次按下的X位置

    private int itemMoveLeft = 0;//item左边的位置
    private int itemMoveRight = 0;//item移动右边的位置
    private int itemMoveCenter = 0;//item移动中心的位置

    private int itemMoveLastLeft = 0;//上次item移动左边的位置
    private int itemMoveLastRight = 0;//上次item移动右边的位置
    private BottomAnimation mBottomAnimation = new BottomAnimation();


    private int backGroundColor;//背景的颜色
    private float textSize;
    private int textColor;
    private int selectTextColor;
    private ArrayList<ImageView> mImageViews = new ArrayList<>();
    private ArrayList<TextView> mTextViews = new ArrayList<>();
    private List<Integer> itemcolors = new ArrayList<>();//记录每个item的颜色
    private int[] itemCenterX = {0, 0, 0, 0, 0};//记录每个item的中心位置
    private float[] itemScale = {0.5f, 0.5f, 0.5f, 0.5f, 0.5f};//记录每个item的缩放比例
    private OnItemSelectListener mOnItemSelectListener;
    private AttributeSet mAttributeSet;
    private float mScaledDensity;
    private DisplayMetrics mDisplayMetrics;

    public AnimationBottomBar(Context context) {
        super(context);
//        init(context);
    }

    public AnimationBottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AnimationBottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        mAttributeSet = attrs;
        mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        mDisplayMetrics = context.getResources().getDisplayMetrics();
        getAttrs();
        setWillNotDraw(false);/*设置false之后才能调用ondraw()*/
        mBottomAnimation.setDuration(300);

        mBottomAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clearAnimation();
                itemMoveLastLeft = itemMoveLeft;
                itemMoveLastRight = itemMoveRight;
                selectLastIndex = selectIndex;

                Log.d("AnimationBottomBar", "onAnimationEnd:itemMoveLastRight " + itemMoveLastRight);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        textSize = 4 * mScaledDensity;
    }

    /*添加item*/
    public AnimationBottomBar addItem(BottomItem bottomItem) {
        mBottomItemArrayList.add(bottomItem);
        return this;
    }


    public void build() throws Exception {
        itemCount = mBottomItemArrayList.size();
        if (itemCount > CHILDCOUNTMAX) {
            throw new Exception("The maximum number of items is 5");
        }
        itemWidth = getLayoutParams().width / itemCount;

        barWidth = mDisplayMetrics.widthPixels;/*bottombar的宽度*/
        barHeight = (int) (49 * mScaledDensity);/*--的高度*/
        itemWidth = barWidth / itemCount;

        itemcolors.clear();

        int imageViewWidth = (int) (mScaledDensity * 20);
        for (int i = 0; i < mBottomItemArrayList.size(); i++) {
            BottomItem bottomItem = mBottomItemArrayList.get(i);

            itemcolors.add(bottomItem.selectColor);


            LinearLayout linearLayout = new LinearLayout(mContext);
            LayoutParams layoutParams = new LayoutParams(itemWidth, LayoutParams.MATCH_PARENT);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            ImageView imageView = new ImageView(mContext);
            if (i == 0) {
                imageView.setImageResource(bottomItem.selectDrawableRes);
            } else {
                imageView.setImageResource(bottomItem.drawableRes);
            }
            LinearLayout.LayoutParams layoutParamsImageView = new LinearLayout.LayoutParams(imageViewWidth, imageViewWidth);
            layoutParamsImageView.setMargins(0, (int) (5 * mScaledDensity), 0, 0);
            imageView.setLayoutParams(layoutParamsImageView);
            mImageViews.add(imageView);
            linearLayout.addView(imageView);


            TextView textView = new TextView(mContext);
            textView.setTextSize(textSize);
            textView.setText(bottomItem.title);
            if (i == 0) {
                textView.setTextColor(selectTextColor);
            } else {
                textView.setTextColor(textColor);
            }

            textView.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParamsTextView = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            layoutParamsTextView.setMargins(0, (int) (-1*mScaledDensity),0,0);
            textView.setLayoutParams(layoutParamsTextView);
            mTextViews.add(textView);

            linearLayout.addView(textView);

            addView(linearLayout);
        }


        Log.d("AnimationBottomBar", "build: itemwidth" + itemWidth);
    }

    /*手动选择选择的item*/


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            childView.getLayoutParams().width = itemWidth;
        }
        setSelectIndex(0);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //绘制item颜色
        for (int i = 0; i < itemcolors.size(); i++) {
            mPaint.setColor(itemcolors.get(i));
            canvas.drawRect(itemWidth * i, 0, itemWidth * (i + 1), barHeight, mPaint);
            canvas.save();
        }
        mPaint.setColor(backGroundColor);
        //画出背景,两个长方形
        canvas.drawRect(0, 0, itemMoveLeft, barHeight, mPaint);
        canvas.drawRect(itemMoveRight, 0, itemWidth * 5, barHeight, mPaint);
        canvas.save();
        for (int i = 0; i < itemCount; i++) {
            /*获得当前item移动中心点和item固定中心点的距离*/
            int deltaX = Math.abs(itemMoveCenter - itemCenterX[i]);
            if (deltaX < itemWidth) {
                /*当距离小于一个item的宽度时调整item的缩放系数*/
                itemScale[i] = (float) (-0.1 * deltaX / itemWidth + 1);
            } else {
                /*非选中的item的缩放系数固定为0.5*/
                itemScale[i] = 1f;
            }
            /*对item的大小进行调整*/

            mImageViews.get(i).setScaleX(itemScale[i]);
            mImageViews.get(i).setScaleY(itemScale[i]);
//            View childTextView = getChildAt(itemCount + i);
            // mTextViews.get(i).setScaleX(itemScale[i]);
            // mTextViews.get(i).setScaleY(itemScale[i]);
        }
        super.onDraw(canvas);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        for (int i = 0; i < itemCount; i++) {
            /*//遍历每一个item,放置item的位置*/
            itemCenterX[i] = (int) (itemWidth * (i + 0.5));
            /*//记录每个item的中心位置*/
            LinearLayout childLayout = (LinearLayout) getChildAt(i);
            childLayout.layout(itemWidth * i, 0, itemWidth * (i + 1), (int) (49 * mScaledDensity));
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (ev.getX() / itemWidth == touchDownX / itemWidth) {
                    selectIndex = (int) (ev.getX() / itemWidth);
                    changeTitleColor();
                    sendListenerPosition(selectIndex);
                    /*点击时开始动画*/
                    startAnimation(mBottomAnimation);
                }
                break;

        }
        return true;
    }

    private void changeTitleColor() {

        for (int i = 0; i < itemCount; i++) {
            if (i == selectIndex) {
                mTextViews.get(i).setTextColor(selectTextColor);
                mImageViews.get(i).setImageDrawable(mContext.getResources().getDrawable(mBottomItemArrayList.get(i).selectDrawableRes));
            } else {
                mTextViews.get(i).setTextColor(textColor);
                mImageViews.get(i).setImageDrawable(mContext.getResources().getDrawable(mBottomItemArrayList.get(i).drawableRes));
            }
        }
    }

    private void getAttrs() {
        TypedArray typedArray = mContext.obtainStyledAttributes(mAttributeSet, R.styleable.AnimationBottomBar);


        backGroundColor = typedArray.getColor(R.styleable.AnimationBottomBar_AbBackgruond, 0xFF5C4E71);
        textSize = typedArray.getDimension(R.styleable.AnimationBottomBar_AbTextSize, 14.0f);
        textColor = typedArray.getColor(R.styleable.AnimationBottomBar_AbTextColor, 0xffffff);
        selectTextColor = typedArray.getColor(R.styleable.AnimationBottomBar_AbSelectTextColor, 0x639fff);
        Log.d("AnimationBottomBar", "getAttrs:textSize " + textSize);
        typedArray.recycle();
    }

    /*发送点击监听*/
    private void sendListenerPosition(int position) {
        if (mOnItemSelectListener != null)
            mOnItemSelectListener.onItemSelectListener(position);
    }

    public void setSelectIndex(int i) {
        selectIndex = i;
        itemMoveLeft = itemWidth * selectIndex;
        itemMoveRight = itemWidth * (selectIndex + 1);
        itemMoveLastRight = itemMoveRight;
        itemMoveLastLeft = itemMoveLeft;
        itemMoveCenter = itemMoveLeft + itemWidth / 2;
        selectLastIndex = i;
        postInvalidate();
    }

    public AnimationBottomBar setItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.mOnItemSelectListener = onItemSelectListener;
        return this;
    }

    public interface OnItemSelectListener {
        void onItemSelectListener(int position);
    }

    private class BottomAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            int position = selectIndex - selectLastIndex;
            /*判断不同方向的移动*/
            if (position < 0) {/*向左滑动*/
                itemMoveRight = (int) (itemMoveLastRight + interpolatedTime * itemWidth * position);
                itemMoveLeft = (int) (itemMoveLastLeft + setFirst(interpolatedTime) * itemWidth * position);
                itemMoveCenter = (int) (itemMoveLastRight + interpolatedTime * itemWidth * position) - itemWidth / 2;/*记录中心点移动的位置*/
            } else {/*向右滑动*/

                itemMoveRight = (int) (itemMoveLastRight + setFirst(interpolatedTime) * itemWidth * position);
                itemMoveLeft = (int) (itemMoveLastLeft + interpolatedTime * itemWidth * position);
                itemMoveCenter = (int) (itemMoveLastLeft + interpolatedTime * itemWidth * position) + itemWidth / 2;/*/记录中心点移动的位置*/

            }
            postInvalidate();
        }

        /*为了实现果冻效果,先移动的一侧要有快速效果*/
        private float setFirst(float interpolatedTime) {
            return (float) Math.sin(interpolatedTime * 0.5 * Math.PI);
        }

    }

}

