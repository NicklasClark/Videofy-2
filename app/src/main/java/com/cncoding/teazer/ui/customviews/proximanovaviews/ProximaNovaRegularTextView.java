package com.cncoding.teazer.ui.customviews.proximanovaviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.text.emoji.widget.EmojiAppCompatTextView;
import android.util.AttributeSet;

import static com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiboldButton.initAttrs;

/**
 *
 * Created by Prem $ on 9/19/2017.
 */

public class ProximaNovaRegularTextView extends EmojiAppCompatTextView {
    public ProximaNovaRegularTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public ProximaNovaRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
        initAttrs(context, attrs, this);
    }

    public ProximaNovaRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
        initAttrs(context, attrs, this);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_regular.otf");
        setTypeface(customFont);
    }
}
