package com.cncoding.teazer.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import com.cncoding.teazer.R;

/**
 *
 * Created by Prem $ on 10/23/2017.
 */

public class RadioButtonPlus extends android.support.v7.widget.AppCompatRadioButton {

    private Drawable buttonDrawable;

    public RadioButtonPlus(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public RadioButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompoundButton, 0, 0);
        buttonDrawable = a.getDrawable(R.styleable.CompoundButton_drawable);  //R.styleable.CompoundButton_drawable
        setButtonDrawable(android.R.color.transparent);
        a.recycle();
    }

    public RadioButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_regular.ttf");
        setTypeface(customFont);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (buttonDrawable != null) {
            buttonDrawable.setState(getDrawableState());
            final int verticalGravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
            final int height = buttonDrawable.getIntrinsicHeight();

            int y = 0;

            switch (verticalGravity) {
                case Gravity.BOTTOM:
                    y = getHeight() - height;
                    break;
                case Gravity.CENTER_VERTICAL:
                    y = (getHeight() - height) / 2;
                    break;
            }

            int buttonWidth = buttonDrawable.getIntrinsicWidth();
            int buttonLeft = (getWidth() - buttonWidth) / 2;
            buttonDrawable.setBounds(buttonLeft, y, buttonLeft+buttonWidth, y + height);
            buttonDrawable.draw(canvas);
        }
    }
}