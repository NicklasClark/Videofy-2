package com.cncoding.teazer.customViews;

import android.content.Context;
import android.support.compat.BuildConfig;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * Created by Prem $ on 10/24/2017.
 */

public class FlowLayout extends ViewGroup {

    private int line_height;

    private static class LayoutParams extends ViewGroup.LayoutParams {

        final int horizontal_spacing;
        final int vertical_spacing;

        /**
         * @param horizontal_spacing Pixels between items, horizontally
         * @param vertical_spacing Pixels between items, vertically
         */
        LayoutParams(int horizontal_spacing, int vertical_spacing) {
            super(0, 0);
            this.horizontal_spacing = horizontal_spacing;
            this.vertical_spacing = vertical_spacing;
        }
    }

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (BuildConfig.DEBUG && MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED)
            throw new AssertionError();

        final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        final int count = getChildCount();
        int line_height = 0;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        int childHeightMeasureSpec;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        } else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }


        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), childHeightMeasureSpec);
                final int childMeasuredWidth = child.getMeasuredWidth();
                line_height = Math.max(line_height, child.getMeasuredHeight() + lp.vertical_spacing);

                if (paddingLeft + childMeasuredWidth > width) {
                    paddingLeft = getPaddingLeft();
                    paddingTop += line_height;
                }

                paddingLeft += childMeasuredWidth + lp.horizontal_spacing;
            }
        }
        this.line_height = line_height;

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = paddingTop + line_height;

        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if (paddingTop + line_height < height) {
                height = paddingTop + line_height;
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(10, 10); // default of 1px spacing
    }

    @Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(
            android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(10, 10);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = r - l;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childMeasuredWidth = child.getMeasuredWidth();
                final int childMeasuredHeight = child.getMeasuredHeight();
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (paddingLeft + childMeasuredWidth > width) {
                    paddingLeft = getPaddingLeft();
                    paddingTop += line_height;
                }
                child.layout(paddingLeft, paddingTop, paddingLeft + childMeasuredWidth, paddingTop + childMeasuredHeight);
                paddingLeft += childMeasuredWidth + lp.horizontal_spacing;
            }
        }
    }
}
