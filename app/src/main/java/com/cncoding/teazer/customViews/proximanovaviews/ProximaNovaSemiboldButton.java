package com.cncoding.teazer.customViews.proximanovaviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cncoding.teazer.R;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

/**
 *
 * Created by Prem $ on 9/19/2017.
 */

public class ProximaNovaSemiboldButton extends CircularProgressButton {
//    private Bitmap mIcon;
//    private Paint mPaint;
//    private Rect mSrcRect;
//    private int mIconPadding;
//    private int mIconSize;
    public ProximaNovaSemiboldButton(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public ProximaNovaSemiboldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
        initAttrs(context, attrs, this);
//        init(context, attrs);
    }

    public ProximaNovaSemiboldButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
        initAttrs(context, attrs, this);
//        init(context, attrs);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(), "fonts/proxima_nova_semibold.otf");
        setTypeface(customFont);
    }

    static void initAttrs(Context context, AttributeSet attrs, TextView button) {
        if (attrs != null) {
            @SuppressLint("CustomViewStyleable")
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

            Drawable drawableStart = attributeArray.getDrawable(R.styleable.CustomTextView_drawableStartCompat);
            Drawable drawableEnd = attributeArray.getDrawable(R.styleable.CustomTextView_drawableEndCompat);
            Drawable drawableBottom = attributeArray.getDrawable(R.styleable.CustomTextView_drawableBottomCompat);
            Drawable drawableTop = attributeArray.getDrawable(R.styleable.CustomTextView_drawableTopCompat);
            button.setCompoundDrawablesWithIntrinsicBounds(drawableStart, drawableTop, drawableEnd, drawableBottom);
            attributeArray.recycle();
            //            if (Build.VERSION.SDK_INT >= 21) {
//            }
//            else {
//                final int drawableStartId = attributeArray.getResourceId(R.styleable.CustomTextView_drawableStartCompat, -1);
//                final int drawableEndId = attributeArray.getResourceId(R.styleable.CustomTextView_drawableEndCompat, -1);
//                final int drawableBottomId = attributeArray.getResourceId(R.styleable.CustomTextView_drawableBottomCompat, -1);
//                final int drawableTopId = attributeArray.getResourceId(R.styleable.CustomTextView_drawableTopCompat, -1);
//
//                if (drawableStartId != -1)
//                    drawableStart = AppCompatResources.getDrawable(context, drawableStartId);
//                if (drawableEndId != -1)
//                    drawableEnd = AppCompatResources.getDrawable(context, drawableEndId);
//                if (drawableBottomId != -1)
//                    drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId);
//                if (drawableTopId != -1)
//                    drawableTop = AppCompatResources.getDrawable(context, drawableTopId);
//            }
        }
    }
//    @Override
//    protected void onDraw(Canvas canvas) {
//        int shift = (mIconSize + mIconPadding) / 2;
//
//        canvas.save();
//        canvas.translate(-shift, 0);
//
//        super.onDraw(canvas);
//
//        if (mIcon != null) {
//            float textWidth = getPaint().measureText((String)getText());
//            int right = (int)((getWidth() / 2f) + (textWidth / 2f) + mIconSize + mIconPadding);
//            int top = getHeight()/2 - mIconSize/2;
//
//            @SuppressLint("DrawAllocation") Rect destRect = new Rect(right - mIconSize, top, right, top + mIconSize);
//            canvas.drawBitmap(mIcon, mSrcRect, destRect, mPaint);
//        }
//        canvas.restore();
////        super.onDraw(canvas);
//    }

//    private void init(Context context, AttributeSet attrs) {
//        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ButtonIcon);
//
//        for (int i = 0; i < array.getIndexCount(); ++i) {
//            int attr = array.getIndex(i);
//            switch (attr) {
//                case R.styleable.ButtonIcon_iconSrc:
//                    mIcon = drawableToBitmap(array.getDrawable(attr));
//                    break;
//                case R.styleable.ButtonIcon_iconPadding:
//                    mIconPadding = array.getDimensionPixelSize(attr, 0);
//                    break;
//                case R.styleable.ButtonIcon_iconSize:
//                    mIconSize = array.getDimensionPixelSize(attr, 0);
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        array.recycle();
//
//        //If we didn't supply an icon in the XML
//        if(mIcon != null){
//            mPaint = new Paint();
//            mSrcRect = new Rect(0, 0, mIcon.getWidth(), mIcon.getHeight());
//        }
//    }

//    public static Bitmap drawableToBitmap (Drawable drawable) {
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable)drawable).getBitmap();
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        drawable.draw(canvas);
//
//        return bitmap;
//    }
}
