package com.unionv.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

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
}
