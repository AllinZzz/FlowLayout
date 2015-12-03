package com.unionv.library;

import android.content.Context;
import android.util.AttributeSet;
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
    //用来关系当前布局中的所有行
    private List<Line> mLines = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //TODO:
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //TODO:
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

    }
}
