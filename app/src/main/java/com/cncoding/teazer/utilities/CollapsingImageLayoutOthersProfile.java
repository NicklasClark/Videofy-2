package com.cncoding.teazer.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.cncoding.teazer.R;

/**
 * Created by farazhabib on 22/02/18.
 */

public class CollapsingImageLayoutOthersProfile extends FrameLayout {

    private static final String TAG = "CollapsingImageLayoutO";

    private WindowInsetsCompat mLastInsets;

    private int mImageLeftExpanded;

    private int mImageTopExpanded;
    private int mLayoutTopExpanded;

    private int mLayoutLeftExpanded;


    private int mTitleLeftExpanded;

    private int mTitleTopExpanded;

    private int mSubtitleLeftExpanded;

    private int mSubtitleTopExpanded;

    private int mImageLeftCollapsed;

    private int mImageTopCollapsed;

    private int mLayoutTopCollapsed;

    private int mLayoutLeftCollapsed;

    private int mTitleLeftCollapsed;

    private int mFollowLeftCollapsed;

    private int mTitleTopCollapsed;

    private int mSubtitleLeftCollapsed;

    private int mSubtitleTopCollapsed;

    private int mDetailTopCollapsed;

    private int mDetailLeftCollapse;

    private int mDetailTopExpanded;

    private int mDetailLeftExpanded;

    private int mFollowTopExpaned;

    private int mFollowLeftExpaned;

    private int mFollowTopCollapsed;


    private CollapsingImageLayoutOthersProfile.OnOffsetChangedListener mOnOffsetChangedListener;

    public CollapsingImageLayoutOthersProfile(Context context) {
        this(context, null);
    }

    public CollapsingImageLayoutOthersProfile(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingImageLayoutOthersProfile(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mImageLeftCollapsed = getResources().getDimensionPixelOffset(R.dimen.image_left_margin_collapsed2);
        mImageTopCollapsed = getResources().getDimensionPixelOffset(R.dimen.image_top_margin_collapsed);
        mTitleLeftCollapsed = getResources().getDimensionPixelOffset(R.dimen.title_left_margin_collapsed2);
        mFollowLeftCollapsed = getResources().getDimensionPixelOffset(R.dimen.follow_left_margin_collapsed);
        mFollowTopCollapsed = getResources().getDimensionPixelOffset(R.dimen.follow_top_margin_collapsed);
        mTitleTopCollapsed = getResources().getDimensionPixelOffset(R.dimen.title_top_margin_collapsed);
        mSubtitleLeftCollapsed = getResources().getDimensionPixelOffset(R.dimen.subtitle_left_margin_collapsed2);
        mSubtitleTopCollapsed = getResources().getDimensionPixelOffset(R.dimen.subtitle_top_margin_collapsed);
        mLayoutTopCollapsed = getResources().getDimensionPixelOffset(R.dimen.layout_top_margin_collapsed);
        mLayoutLeftCollapsed = getResources().getDimensionPixelOffset(R.dimen.layout_left_margin_collapsed);
        mDetailLeftCollapse = getResources().getDimensionPixelOffset(R.dimen.details_left_margin_collapsed);
        mDetailTopCollapsed = getResources().getDimensionPixelOffset(R.dimen.detasils_top_margin_collapsed);

        ViewCompat.setOnApplyWindowInsetsListener(this,
                new android.support.v4.view.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                        return setWindowInsets(insets);
                    }
                });
    }

    @TargetApi(21)
    public CollapsingImageLayoutOthersProfile(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Add an OnOffsetChangedListener if possible
        final ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            if (mOnOffsetChangedListener == null) {
                mOnOffsetChangedListener = new CollapsingImageLayoutOthersProfile.OnOffsetChangedListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(mOnOffsetChangedListener);
        }

        // We're attached, so lets request an inset dispatch
        ViewCompat.requestApplyInsets(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        // Remove our OnOffsetChangedListener if possible and it exists
        final ViewParent parent = getParent();
        if (mOnOffsetChangedListener != null && parent instanceof AppBarLayout) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(mOnOffsetChangedListener);
        }

        super.onDetachedFromWindow();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // Update our child view offset helpers

        for (int i = 0, z = getChildCount(); i < z; i++) {
            final View child = getChildAt(i);

            if (mLastInsets != null && !ViewCompat.getFitsSystemWindows(child)) {
                final int insetTop = mLastInsets.getSystemWindowInsetTop();
                if (child.getTop() < insetTop) {
                    // If the child isn't set to fit system windows but is drawing within the inset
                    // offset it down
                    ViewCompat.offsetTopAndBottom(child, insetTop);
                }
            }

            getViewOffsetHelper(child).onViewLayout();

            switch (child.getId()) {
                case R.id.profilepic:
                    mImageLeftExpanded = child.getLeft();
                    mImageTopExpanded = child.getTop();
                    break;
                case R.id.username:
                    mTitleLeftExpanded = child.getLeft();
                    mTitleTopExpanded = child.getTop();
                    break;
                case R.id.name:
                    mSubtitleLeftExpanded = child.getLeft();
                    mSubtitleTopExpanded = child.getTop();
                    break;

                case R.id.detaillayout:
                    mLayoutLeftExpanded = child.getLeft();
                    mLayoutTopExpanded = child.getTop();
                    break;


                    case R.id.follow:
                        mFollowLeftExpaned = child.getLeft();
                        mFollowTopExpaned = child.getTop();
                    break;

//                case R.id.detail:
//                    mDetailLeftExpanded = child.getLeft();
//                    mDetailTopExpanded = child.getTop();
//                    break;
//
//                case R.id.sliding_tabs:
//                    mDetailLeftExpanded = child.getLeft();
//                    mDetailTopExpanded = child.getTop();
//                    break;
            }
        }
    }

    private WindowInsetsCompat setWindowInsets(WindowInsetsCompat insets) {
        if (mLastInsets != insets) {
            mLastInsets = insets;
            requestLayout();
        }
        return insets.consumeSystemWindowInsets();
    }

    class OnOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            final int insetTop = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
            final int scrollRange = appBarLayout.getTotalScrollRange();
            float offsetFactor = (float) (-verticalOffset) / (float) scrollRange;
            final int heightDiff = getHeight() - getMinimumHeight();

            Log.d(TAG, "onOffsetChanged(), offsetFactor = " + offsetFactor);


            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                final CollapsingImageLayoutOthersProfile.ViewOffsetHelper offsetHelper = getViewOffsetHelper(child);

                if (child instanceof Toolbar) {
                    if (getHeight() - insetTop + verticalOffset >= child.getHeight()) {
                        offsetHelper.setTopAndBottomOffset(-verticalOffset); // pin
                    }

                }

                if (child.getId() == R.id.background) {
                    int offset = Math.round(-verticalOffset * .2F);
                    offsetHelper.setTopAndBottomOffset(offset); // parallax
                }

                if (child.getId() == R.id.profilepic) {

                    float scaleFactor = 1F - offsetFactor * .65F ;
                    child.setScaleX(scaleFactor);
                    child.setScaleY(scaleFactor);

                    int topOffset = (int) ((mImageTopCollapsed - mImageTopExpanded) * offsetFactor) - verticalOffset;
                    int leftOffset = (int) ((mImageLeftCollapsed - mImageLeftExpanded) * offsetFactor);
                    child.setPivotX(0);
                    child.setPivotY(0);
                    offsetHelper.setTopAndBottomOffset(topOffset);
                    offsetHelper.setLeftAndRightOffset(leftOffset);
                }

                if (child.getId() == R.id.username) {

                    int topOffset = (int) ((mTitleTopCollapsed - mTitleTopExpanded) * offsetFactor) - verticalOffset;
                    int leftOffset = (int) ((mTitleLeftCollapsed - mTitleLeftExpanded) * offsetFactor);
                    offsetHelper.setTopAndBottomOffset(topOffset);
                    offsetHelper.setLeftAndRightOffset(leftOffset);
                    Log.d(TAG, "onOffsetChanged(), offsetting title top = " + topOffset + ", left = " + leftOffset);
                    Log.d(TAG, "onOffsetChanged(), offsetting title mTitleLeftCollapsed = " + mTitleLeftCollapsed + ", mTitleLeftExpanded = " + mTitleLeftExpanded);
                }

                if (child.getId() == R.id.name) {

                    int topOffset = (int) ((mSubtitleTopCollapsed - mSubtitleTopExpanded) * offsetFactor) - verticalOffset;
                    int leftOffset = (int) ((mSubtitleLeftCollapsed - mSubtitleLeftExpanded) * offsetFactor);
                    offsetHelper.setTopAndBottomOffset(topOffset);
                    offsetHelper.setLeftAndRightOffset(leftOffset);
                }

                if (child.getId() == R.id.follow) {

                    int topOffset = (int) ((mFollowTopCollapsed - mFollowTopExpaned) * offsetFactor) - verticalOffset;
                    int leftOffset = (int) ((mFollowLeftCollapsed - mFollowLeftExpaned) * offsetFactor);
                    offsetHelper.setTopAndBottomOffset(topOffset);
                    offsetHelper.setLeftAndRightOffset(leftOffset);
                }

                if (child.getId() == R.id.detaillayout) {
                    int offset = Math.round(-verticalOffset * .4F);
                    offsetHelper.setTopAndBottomOffset(offset);
////
//
//                    float scaleFactor = 1F - offsetFactor * .99F ;
//                    child.setScaleX(scaleFactor);
//                    child.setScaleY(scaleFactor);
//
//                    int topOffset = (int) ((mLayoutTopCollapsed - mLayoutTopExpanded) * offsetFactor) - verticalOffset;
//                    int leftOffset = (int) ((mLayoutLeftCollapsed - mLayoutLeftExpanded) * offsetFactor);
//                    child.setPivotX(0);
//                    child.setPivotY(0);
//                    offsetHelper.setTopAndBottomOffset(topOffset);
//                    offsetHelper.setLeftAndRightOffset(leftOffset);
                }
//                if (child.getId() == R.id.detail) {
//
//                    int offset = Math.round(-verticalOffset * .2F);
//                    offsetHelper.setTopAndBottomOffset(offset);


//                    float scaleFactor = 1F - offsetFactor * .99F ;
//                    child.setScaleX(scaleFactor);
//                    child.setScaleY(scaleFactor);
//                    int topOffset = (int) ((mDetailTopCollapsed - mDetailTopExpanded) * offsetFactor) - verticalOffset;
//                    int leftOffset = (int) ((mDetailLeftCollapse - mDetailLeftExpanded) * offsetFactor);
//                    child.setPivotX(0);
//                    child.setPivotY(0);
//                    offsetHelper.setTopAndBottomOffset(topOffset);
//                    offsetHelper.setLeftAndRightOffset(leftOffset);
                //   }

                //  if (child.getId() == R.id.sliding_tabs) {

//                    int offset = Math.round(-verticalOffset * .2F);
//                    offsetHelper.setTopAndBottomOffset(offset);


//                    float scaleFactor = 1F - offsetFactor * .99F ;
//                    child.setScaleX(scaleFactor);
//                    child.setScaleY(scaleFactor);
//                    int topOffset = (int) ((mDetailTopCollapsed - mDetailTopExpanded) * offsetFactor) - verticalOffset;
//                    int leftOffset = (int) ((mDetailLeftCollapse - mDetailLeftExpanded) * offsetFactor);
//                    child.setPivotX(0);
//                    child.setPivotY(0);
//                    offsetHelper.setTopAndBottomOffset(topOffset);
//                    offsetHelper.setLeftAndRightOffset(leftOffset);
                // }
            }
        }
    }

    private static CollapsingImageLayoutOthersProfile.ViewOffsetHelper getViewOffsetHelper(View view) {
        CollapsingImageLayoutOthersProfile.ViewOffsetHelper offsetHelper = (CollapsingImageLayoutOthersProfile.ViewOffsetHelper) view.getTag(R.id.view_offset_helper);
        if (offsetHelper == null) {
            offsetHelper = new CollapsingImageLayoutOthersProfile.ViewOffsetHelper(view);
            view.setTag(R.id.view_offset_helper, offsetHelper);
        }
        return offsetHelper;
    }

    static class ViewOffsetHelper {

        private final View mView;

        private int mLayoutTop;
        private int mLayoutLeft;
        private int mOffsetTop;
        private int mOffsetLeft;

        public ViewOffsetHelper(View view) {
            mView = view;
        }

        public void onViewLayout() {
            // Now grab the intended top
            mLayoutTop = mView.getTop();
            mLayoutLeft = mView.getLeft();

            // And offset it as needed
            updateOffsets();
        }

        private void updateOffsets() {
            ViewCompat.offsetTopAndBottom(mView, mOffsetTop - (mView.getTop() - mLayoutTop));
            ViewCompat.offsetLeftAndRight(mView, mOffsetLeft - (mView.getLeft() - mLayoutLeft));

            // Manually invalidate the view and parent to make sure we get drawn pre-M
            if (Build.VERSION.SDK_INT < 23) {
                tickleInvalidationFlag(mView);
                final ViewParent vp = mView.getParent();
                if (vp instanceof View) {
                    tickleInvalidationFlag((View) vp);
                }
            }
        }

        private static void tickleInvalidationFlag(View view) {
            final float y = ViewCompat.getTranslationY(view);
            ViewCompat.setTranslationY(view, y + 1);
            ViewCompat.setTranslationY(view, y);
        }

        /**
         * Set the top and bottom offset for this {@link CollapsingImageLayoutOthersProfile.ViewOffsetHelper}'s view.
         *
         * @param offset the offset in px.
         * @return true if the offset has changed
         */
        public boolean setTopAndBottomOffset(int offset) {
            if (mOffsetTop != offset) {
                mOffsetTop = offset;
                updateOffsets();
                return true;
            }
            return false;
        }

        /**
         * Set the left and right offset for this {@link CollapsingImageLayoutOthersProfile.ViewOffsetHelper}'s view.
         *
         * @param offset the offset in px.
         * @return true if the offset has changed
         */
        public boolean setLeftAndRightOffset(int offset) {
            if (mOffsetLeft != offset) {
                mOffsetLeft = offset;
                updateOffsets();
                return true;
            }
            return false;
        }

        public int getTopAndBottomOffset() {
            return mOffsetTop;
        }

        public int getLeftAndRightOffset() {
            return mOffsetLeft;
        }
    }}
