package com.cncoding.teazer.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 *
 * Created by Prem $ on 9/19/2017.
 */

public class ProximaNovaRegularTextView extends android.support.v7.widget.AppCompatTextView {
    public ProximaNovaRegularTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public ProximaNovaRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public ProximaNovaRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_regular.ttf");
        setTypeface(customFont);
    }
}
