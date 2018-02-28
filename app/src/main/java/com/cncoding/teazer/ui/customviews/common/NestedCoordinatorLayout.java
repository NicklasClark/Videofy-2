package com.cncoding.teazer.ui.customviews.common;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * Created by Prem $ on 10/30/2017.
 */

public class NestedCoordinatorLayout extends CoordinatorLayout implements NestedScrollingChild {

    private NestedScrollingChildHelper mChildHelper;
    private volatile boolean mPassToParent;

    public NestedCoordinatorLayout(Context context) {
        super(context);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    public NestedCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    public NestedCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    /**
     * Locks the nested scrolling. Further scroll events will
     * be passed to the nested scrolling parent.
     */
    public void lockNestedScrolling() {
        mPassToParent = true;
    }

    /**
     * Unlocks the nested scrolling. Further scroll events will
     * be dispatched to this layout's own scrolling children.
     */
    public void unlockNestedScrolling() {
        mPassToParent = false;
    }

    /*
     * NestedScrollingParent implementation
     */

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        /* Enable the scrolling behavior of our own children */
        super.onStartNestedScroll(child, target, nestedScrollAxes);
        /* Enable the scrolling behavior of the parent's other children  */
        startNestedScroll(nestedScrollAxes);
        /* Start tracking the current scroll */
        return true;
    }

    @Override
    public void onStopNestedScroll(View target) {
        /* Disable the scrolling behavior of our own children */
        super.onStopNestedScroll(target);
        /* Disable the scrolling behavior of the parent's other children  */
        stopNestedScroll();
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (mPassToParent) {
            dispatchNestedPreScroll(dx, dy, consumed, null);
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed);
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        if (mPassToParent) {
            dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null);
        } else {
            super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (mPassToParent) {
            return dispatchNestedPreFling(velocityX, velocityY);
        } else {
            return super.onNestedPreFling(target, velocityX, velocityY);
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (mPassToParent) {
            return dispatchNestedFling(velocityX, velocityY, consumed);
        } else {
            return super.onNestedFling(target, velocityX, velocityY, consumed);
        }
    }

    /*
     * NestedScrollingChild implementation
     */
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}