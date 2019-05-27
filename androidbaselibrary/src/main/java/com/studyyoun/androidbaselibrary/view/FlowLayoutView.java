package com.studyyoun.androidbaselibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/3/11.
 */
public class FlowLayoutView extends ViewGroup {
    private int DEFAULT_SPACING = 10;
    private int horizontalSpacing = 1;//水平间距
    private float mScaledDensity;

    public FlowLayoutView(Context context) {
        this(context, null);
    }

    public FlowLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        mScaledDensity = displayMetrics.scaledDensity;
        DEFAULT_SPACING = (int) (DEFAULT_SPACING * mScaledDensity);
        horizontalSpacing = 8;
        verticalSpacing = (int) (verticalSpacing * mScaledDensity);
        horizontalSpacing = (int) (horizontalSpacing * mScaledDensity);

    }

    /**
     * 设置子View直接的水平间距
     *
     * @param horizontalSpacing
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        if (horizontalSpacing > 0) {
            this.horizontalSpacing = horizontalSpacing;
        }
    }

    private ArrayList<Line> lineList = new ArrayList<Line>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        lineList.clear();
        //1.获取FlowLayout的宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //2.计算用于实际比较的宽度，就是width减去左右的padding值
        int noPaddingWidth = width - getPaddingLeft() - getPaddingRight();
        //3.遍历所有的子TextView，在遍历过程中进行比较，进行分行操作
        //创建一个行
        //3.遍历所有的子TextView，进行分行操作
        Line line = new Line();//只要不换行，始终都是同一个Line对象
        for (int i = 0; i < getChildCount(); i++) {
            //获取子TextView
            View childView = getChildAt(i);
            //引起view的onMeasure方法回调，从而保证后面的方法能够有值
            childView.measure(0, 0);

            //4.如果当前line中 没有TextView，则直接放入当前Line中
            if (line.getViewList().size() == 0) {
                line.addLineView(childView);
            } else if (line.getWidth() + horizontalSpacing + childView.getMeasuredWidth() > noPaddingWidth) {
                //5.如果当前line的宽+水平间距+childView的宽大于noPaddingWidth，则换行
                lineList.add(line);//先保存之前的line对象

                line = new Line();//重新创建Line
                line.addLineView(childView);//将chidlView放入新的Line
            } else {
                //6.如果小于noPaddingWidth，则将childView放入当前Line中
                line.addLineView(childView);
            }

            //7.如果当前childView是最后一个，那么就会造成最后的一个Line对象丢失,
            if (i == (getChildCount() - 1)) {
                lineList.add(line);//保存最后的line对象
            }
        }

        //for循环结束后，lineList就存放了所有的Line对象，而每个line中有记录自己的所有TextView
        //为了能够垂直的摆放所有的Line的TextView，所以要给当前FlowLayout设置对应的宽高,
        //计算所需要的高度：上下的padding + 所有line的高度   + 所有line之间的垂直间距
        int height = getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < lineList.size(); i++) {
            height += lineList.get(i).getHeight();
        }
        height += (lineList.size() - 1) * verticalSpacing;
        //向父View申请对应的宽高
        setMeasuredDimension(width, height);
    }

    //行与行之间的垂直间距
    private int verticalSpacing = 14;

    /**
     * 设置行与行之间的垂直间距
     *
     * @param verticalSpacing
     */
    public void setVerticalSpacing(int verticalSpacing) {
        if (verticalSpacing > 0) {
            this.verticalSpacing = verticalSpacing;
        }
    }

    /**
     * 摆放操作，让所有的子TextView摆放到指定的位置上面
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (int i = 0; i < lineList.size(); i++) {
            Line line = lineList.get(i);//获取line对象
            //从第二行开始，他们的top总是比上一行多一个行高+垂直间距
            if (i > 0) {
                paddingTop += lineList.get(i - 1).getHeight() + verticalSpacing;
            }
            ArrayList<View> viewList = line.getViewList();//获取line所有的TextView
            //1.计算出当前line的留白区域的值
            int remainSpacing = getLineRemainSpacing(line);
            //2.计算每个TextView分到多少留白
            float perSpacing = remainSpacing / viewList.size();

            perSpacing = 0;

            for (int j = 0; j < viewList.size(); j++) {
                View childView = viewList.get(j);//获取每个TextView

                if (j == 0) {
                    if (viewList.size() == 1) {
                        //3.将perSpacing增加到每个TextView的宽度上
                        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childView.getMeasuredWidth(), MeasureSpec.EXACTLY);
                        childView.measure(widthMeasureSpec, 0);
                        //摆放每行的第一个TextView
                        childView.layout(paddingLeft, paddingTop, paddingLeft + childView.getMeasuredWidth()
                                , paddingTop + childView.getMeasuredHeight());
                    } else {
                        //3.将perSpacing增加到每个TextView的宽度上
                        //int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth()+perSpacing),MeasureSpec.EXACTLY);
                        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childView.getMeasuredWidth(), MeasureSpec.EXACTLY);
                        childView.measure(widthMeasureSpec, 0);

                        //摆放每行的第一个TextView
                        childView.layout(paddingLeft, paddingTop, paddingLeft + childView.getMeasuredWidth()
                                , paddingTop + childView.getMeasuredHeight());
                    }
                } else {
                    //3.将perSpacing增加到每个TextView的宽度上
                    int widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth() + perSpacing), MeasureSpec.EXACTLY);
                    childView.measure(widthMeasureSpec, 0);

                    //摆放后面的TextView，需要参照前一个View
                    View preView = viewList.get(j - 1);
                    int left = preView.getRight() + horizontalSpacing;
                    childView.layout(left, preView.getTop(), left + childView.getMeasuredWidth(),
                            preView.getBottom());
                }
            }
        }
    }

    /**
     * 获取line的留白区域
     *
     * @param line
     * @return
     */
    private int getLineRemainSpacing(Line line) {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - line.getWidth();
    }

    /**
     * 定义行对象，用来封装每行的所有TextView，以及宽和高
     *
     * @author Administrator
     */
    class Line {
        //用来记录当前行的所有TextView
        private ArrayList<View> viewList = new ArrayList<View>();
        //表示当前行所有TextView的宽，还有他们之间的水平间距
        private int width;
        //当前行的高度
        private int height;

        /**
         * 获取当前Line中的所有TextView
         *
         * @return
         */
        public ArrayList<View> getViewList() {
            return viewList;
        }

        /**
         * 获取当前Line的宽度
         *
         * @return
         */
        public int getWidth() {
            return width;
        }

        /**
         * 获取当前Line的高度
         *
         * @return
         */
        public int getHeight() {
            return height;
        }

        /**
         * 添加一个TextView到viewList中
         *
         * @param lineView
         */
        public void addLineView(View lineView) {
            if (!viewList.contains(lineView)) {
                viewList.add(lineView);

                //更新width
                if (viewList.size() == 1) {
                    //如果是第一个TextView，那么width就是lineView的宽度
                    width = lineView.getMeasuredWidth();
                } else {
                    //如果不是第一个，则要在当前width的基础上+水平间距+lineView的宽度
                    width += horizontalSpacing + lineView.getMeasuredWidth();
                }
                //更新height,在此所有的TextView的高度都是一样的
                height = Math.max(height, lineView.getMeasuredHeight());
            }
        }
    }
}
