package com.cncoding.teazer.ui.customviews.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Prem $ on 28/10/2017
 */
public class ResizableSurfaceView extends SurfaceView {
//    private static final int MARGIN_DP = 0;//margin of ResizeSurfaceView
    public ResizableSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizableSurfaceView(Context context) {
        super(context);
    }

    /**
     * adjust SurfaceView area according to video width and height
     * @param surfaceViewWidth original width
     * @param surfaceViewHeight original height
     */
    public void adjustSize(int surfaceViewWidth, int surfaceViewHeight, final int videoWidth, final int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            final ViewGroup.LayoutParams lp = getLayoutParams();
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            int windowWidth = displayMetrics.widthPixels;
            int windowHeight = displayMetrics.heightPixels;
//            int margin = (int) (getTheContext().getResources().getDisplayMetrics().density* MARGIN_DP);
            float videoRatio;
            if (windowWidth < windowHeight) {
                videoRatio = ((float) (videoWidth)) / videoHeight;
            } else {
                videoRatio = ((float) (videoHeight)) / videoWidth;
            }
            if (windowWidth < windowHeight) {// portrait
                if (videoWidth > videoHeight) {
                    if (surfaceViewWidth / videoRatio > surfaceViewHeight) {
                        lp.height = surfaceViewHeight;
                        lp.width = (int) (surfaceViewHeight * videoRatio);
                    } else {
                        lp.height = (int) (surfaceViewWidth / videoRatio);
                        lp.width = surfaceViewWidth;
                    }
                } else if (videoWidth <= videoHeight) {
                    if (surfaceViewHeight * videoRatio > surfaceViewWidth) {
                        lp.height = (int) (surfaceViewWidth / videoRatio);
                        lp.width = surfaceViewWidth;
                    } else {
                        lp.height = surfaceViewHeight;
                        lp.width = (int) (surfaceViewHeight * videoRatio);
                    }
                }
            } else if (windowWidth > windowHeight) {// landscape
                if (videoWidth > videoHeight) {//video is landscape
                    if (windowWidth * videoRatio > videoHeight) {
                        lp.height = windowHeight;
//                        lp.width = (int) ((windowHeight - margin) / videoRatio);
                        lp.height = windowHeight;
                        lp.width = (int) ((windowHeight) / videoRatio);
                    } else {
                        lp.height = (int) (windowWidth * videoRatio);
                        lp.width = windowWidth;
                    }
                } else if (videoWidth < videoHeight) {//video is portrait
//                    lp.width = (int) ((windowHeight - margin) / videoRatio);
                    lp.width = (int) ((windowHeight) / videoRatio);
//                    lp.height = windowHeight - margin;
                    lp.height = windowHeight;
                } else {
//                    lp.height = windowHeight- margin;
                    lp.height = windowHeight;
                    //noinspection SuspiciousNameCombination
                    lp.width = lp.height;
                }
            }
            setLayoutParams(lp);
            getHolder().setFixedSize(videoWidth, videoHeight);
            setVisibility(View.VISIBLE);
        }
    }
}