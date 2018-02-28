package com.cncoding.teazer.ui.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.hbb20.CountryCodePicker;

/**
 *
 * Created by Prem $ on 12/5/2017.
 */

public class CustomCountryCodePicker extends CountryCodePicker {
    public CustomCountryCodePicker(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomCountryCodePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomCountryCodePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_semibold.otf");
        setTypeFace(customFont);
    }
}
