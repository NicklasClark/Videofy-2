package com.cncoding.teazer.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 *
 * Created by Prem $ on 9/19/2017.
 */

public class ProximaNovaRegularCheckBox extends android.support.v7.widget.AppCompatCheckBox {
    public ProximaNovaRegularCheckBox(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public ProximaNovaRegularCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public ProximaNovaRegularCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_regular.ttf");
        setTypeface(customFont);
    }
}
