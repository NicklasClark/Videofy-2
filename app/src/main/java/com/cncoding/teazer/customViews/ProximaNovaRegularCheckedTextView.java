package com.cncoding.teazer.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.AttributeSet;

import static com.cncoding.teazer.customViews.ProximaNovaSemiboldButton.initAttrs;

/**
 *
 * Created by Prem $ on 9/19/2017.
 */

public class ProximaNovaRegularCheckedTextView extends AppCompatCheckedTextView {
    public ProximaNovaRegularCheckedTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public ProximaNovaRegularCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
        initAttrs(context, attrs, this);
    }

    public ProximaNovaRegularCheckedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
        initAttrs(context, attrs, this);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_regular.ttf");
        setTypeface(customFont);
    }
}
