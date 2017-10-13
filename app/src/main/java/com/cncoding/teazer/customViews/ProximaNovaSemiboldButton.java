package com.cncoding.teazer.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 *
 * Created by Prem $ on 9/19/2017.
 */

public class ProximaNovaSemiboldButton extends AppCompatButton {
    public ProximaNovaSemiboldButton(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public ProximaNovaSemiboldButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public ProximaNovaSemiboldButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_semibold.ttf");
        setTypeface(customFont);
    }
}
