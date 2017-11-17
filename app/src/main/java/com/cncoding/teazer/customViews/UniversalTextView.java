package com.cncoding.teazer.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.cncoding.teazer.R;

/**
 *
 * Created by Prem $ on 11/16/2017.
 */

public class UniversalTextView extends AppCompatTextView {

    private int typefaceType;
    private TypeFactory mFontFactory;

    public UniversalTextView(Context context) {
        super(context);
    }

    public UniversalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UniversalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView, 0, 0);
        try {
            typefaceType = array.getInteger(R.styleable.FontFamily_font_name, 0);
        } finally {
            array.recycle();
        }
        if (!isInEditMode()) {
            setTypeface(getTypeFace(typefaceType));
        }
    }

    public Typeface getTypeFace(int type) {
        if (mFontFactory == null)
            mFontFactory = new TypeFactory(getContext());

        switch (type) {
            case 1:
                return mFontFactory.bold;
            case 2:
                return mFontFactory.condensed;
            case 3:
                return mFontFactory.regular;
            case 4:
                return mFontFactory.semiBold;
            case 5:
                return mFontFactory.signPainter;
            default:
                return mFontFactory.regular;
        }
    }
}
