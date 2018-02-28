package com.cncoding.teazer.ui.customviews.proximanovaviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 *
 * Created by Prem $ on 9/19/2017.
 */

public class SignPainterTextView extends android.support.v7.widget.AppCompatTextView {
    public SignPainterTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public SignPainterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public SignPainterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/sign_painter_house_script.ttf");
        setTypeface(customFont);
    }
}
