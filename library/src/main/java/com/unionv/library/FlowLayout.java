package com.unionv.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @项目名: FlowLayout
 * @包名: com.unionv.library
 * @文件名: FlowLayout
 * @创建者: zengyaoliang
 * @创建时间: 2015/12/2 11:06
 * @描述: TODO:
 *
 */
public class FlowLayout
        extends ViewGroup
{
    private static final String     TAG    = "FlowLayout";
    //用来关系当前布局中的所有行
    private              List<Line> mLines = new ArrayList<>();

    //当前行
    private Line mCurrentLine;

    //控件之间的间隙
    private int mChildHorizontalSpace = 15;

    //行和行之间的间隙
    private int mLineVerticalSpace = 15;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.d(TAG, "onMeasure");

        //每次测量清空一下容器
        mLines.clear();
        mCurrentLine = null;

        int width = MeasureSpec.getSize(widthMeasureSpec);

        int childWidth = width - getPaddingLeft() - getPaddingRight();
        //TODO:
        //1.测量孩子
        //获取孩子的个数
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                //不测量,gone不占位置,invisible占位置
                continue;
            }

            //测量每一个孩子,如果孩子都是自适应宽度(wrap_content),那么使用下面的测量方法
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            //测量好一个孩子,在当前位置添加到布局中
            //如果布局中一行都没有
            if (mCurrentLine == null) {
                //一行都没有
                mCurrentLine = new Line(childWidth, mChildHorizontalSpace);
                //将行添加到管理行的集合中
                mLines.add(mCurrentLine);

                //同时将view添加到行中
                mCurrentLine.addChild(child);
            } else {
                //布局中已经存在至少一行,那么可以直接将孩子添加到line中去
                //不过要先判断当前行是否可以容纳得下该孩子
                if (mCurrentLine.canAdd(child)) {
                    //可以容纳
                    mCurrentLine.addChild(child);
                } else {
                    //容纳不下,那么得新建一行,
                    mCurrentLine = new Line(childWidth, mChildHorizontalSpace);

                    //将新的一行,添加到布局中去
                    mLines.add(mCurrentLine);

                    //将孩子,添加到line中去
                    mCurrentLine.addChild(child);
                }
            }
        }
        //2.设置自己的宽高
        //宽度使用父亲期望的宽度
        //所有行的高度和 + 行和行的间隙和 + paddingTop + paddingBottom;
        int height = getPaddingTop() + getPaddingBottom();

        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            height += line.mLineHeight;

            //如果不是第一行,那么需要加上行和行之间的垂直间隙
            if (i != mLines.size() - 1) {
                height += mLineVerticalSpace;
            }
        }

        setMeasuredDimension(width, height);

        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //TODO:

        //布局line
        //获取paddingtop的值
        int top = getPaddingTop();
        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);

            //第一行的情况是,top就是padddingtop
            line.layout(getPaddingLeft(), top);

            //当第一行布局完了,top的值就会改变
            top += line.mLineHeight + mLineVerticalSpace;
        }
    }

    private class Line {
        //用来记录每行中的控件和相关属性
        //1.属性
        //2.行为
        //3.构造

        //用来管理当前行中的子控件
        private List<View> mViews = new ArrayList<>();

        private int mLineUsedWidth;   //当前行已经使用的宽度
        private int mLineMaxWidth;   //当前行最大的宽度
        private int mLineHeight;    //当前行的高度
        private int mSpace;        //子控件之间的间隙

        //构造方法,把行的最大宽度,和间隙的值暴露出去,给使用者来设定
        public Line(int maxWidth, int space) {
            this.mLineMaxWidth = maxWidth;
            this.mSpace = space;
        }

        /**
         * 判断当前行是否可以继续添加孩子
         * @param view
         * @return
         */
        public boolean canAdd(View view) {

            //如果当前行一个孩子都没有,那么不管这个孩子的宽度是多宽,
            // 都应该添加到当前行中去
            if (mViews.size() == 0) {
                //当前行一个孩子都没有
                return true;
            }

            int childWidth = view.getMeasuredWidth();

            if (mLineUsedWidth + mSpace + childWidth <= mLineMaxWidth) {
                //可以添加
                return true;
            }
            return false;
        }

        /**
         * 添加子控件,添加之前,先校验当前行是否可以继续添加子view,通过canAdd()方法校验
         * @param view
         */
        public void addChild(View view) {

            int childWidth  = view.getMeasuredWidth();
            int childHeight = view.getMeasuredHeight();

            if (mViews.size() == 0) {
                //当前一个孩子都没有
                //计算行的宽度
                mLineUsedWidth = childWidth;

                //计算行的高度
                mLineHeight = childHeight;
            } else {
                //当前至少有一个孩子

                //计算行的宽度
                mLineUsedWidth += childWidth + mSpace;

                //计算行的高度,去最高的孩子的高度
                mLineHeight = mLineHeight > childHeight
                              ? mLineHeight
                              : childHeight;
            }

            mViews.add(view);
        }

        public void layout(int left, int top) {

            //遍历一行中的每一个孩子
            for (int i = 0; i < mViews.size(); i++) {
                View child = mViews.get(i);

                //获取子控件测量后的宽高
                int childWidth  = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                //第一行第一个的情况
                int l = left;
                int t = top;
                int r = l + childWidth;
                int b = t + childHeight;

                child.layout(l, t, r, b);

                //第一行其他个的情况,top和bottom不变,left和right变,
                //然而,right的变化是随着left的改变而变,因此只需要
                //在每一次的layout后,更改left的值即可
                left += childWidth + mSpace;

            }

        }
    }
}
