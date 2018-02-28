package com.cncoding.teazer.ui.customviews.common;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 *
 * Created by Prem $ on 9/29/2017.
 */

public class FlippedProgressBar extends ProgressBar {

    public FlippedProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlippedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlippedProgressBar(Context context) {
        super(context);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.scale(-1f, 1f, super.getWidth() * 0.5f, super.getHeight() * 0.5f);
        super.onDraw(canvas);
    }

}