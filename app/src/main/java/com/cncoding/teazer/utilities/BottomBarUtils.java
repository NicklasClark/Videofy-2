package com.cncoding.teazer.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.VectorDrawable;
import android.support.v4.content.ContextCompat;

/**
 *
 * Created by Prem $ on 11/02/17.
 */

public class BottomBarUtils {

    public static Drawable setDrawableSelector(Context context, int normal) {

        Drawable state_normal = ContextCompat.getDrawable(context, normal);

        Bitmap state_normal_bitmap;
        if (state_normal instanceof VectorDrawable) {
            state_normal_bitmap = Bitmap.createBitmap(state_normal.getIntrinsicWidth(),
                    state_normal.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(state_normal_bitmap);
            state_normal.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            state_normal.draw(canvas);
        }
        else
            state_normal_bitmap = ((BitmapDrawable)state_normal).getBitmap();

        // Setting alpha directly just didn't work, so we draw a new bitmap!
        Bitmap disabledBitmap = Bitmap.createBitmap(
                state_normal.getIntrinsicWidth(),
                state_normal.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(disabledBitmap);

        Paint paint = new Paint();
        paint.setAlpha(126);
        canvas.drawBitmap(state_normal_bitmap, 0, 0, paint);

        BitmapDrawable state_normal_drawable = new BitmapDrawable(context.getResources(), disabledBitmap);

        StateListDrawable drawable = new StateListDrawable();

        drawable.addState(new int[]{android.R.attr.state_selected},
                state_normal);
        drawable.addState(new int[]{android.R.attr.state_enabled},
                state_normal_drawable);

        return drawable;
    }

//    public static final void showToast(Context context, String message) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }

//    public static final String getDeviceID(Context context) {
//        return Settings.Secure.getString(context.getContentResolver(),
//                Settings.Secure.ANDROID_ID);
//    }

//    public static final String getVersionName(Context context) {
//        PackageInfo packageInfo = null;
//        try {
//            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return packageInfo.versionName;
//    }

//    public static void setButtonBackgroundColor(Context context, Button button, int color) {
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            button.setBackgroundColor(context.getResources().getColor(color, null));
//        } else {
//            button.setBackgroundColor(context.getResources().getColor(color));
//        }
//    }

//    public static void setButtonBackgroundColor(Context context, TextView textView, int color) {
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            textView.setBackgroundColor(context.getResources().getColor(color, null));
//        } else {
//            textView.setBackgroundColor(context.getResources().getColor(color));
//        }
//    }

//    public static StateListDrawable selectorRadioImage(Context context, Drawable normal, Drawable pressed) {
//        StateListDrawable states = new StateListDrawable();
//        states.addState(new int[]{android.R.attr.state_checked}, pressed);
//        states.addState(new int[]{}, normal);
//        //                imageView.setImageDrawable(states);
//        return states;
//    }

//    public static StateListDrawable selectorRadioButton(Context context, int normal, int pressed) {
//        StateListDrawable states = new StateListDrawable();
//        states.addState(new int[]{android.R.attr.state_checked}, new ColorDrawable(pressed));
//        states.addState(new int[]{}, new ColorDrawable(normal));
//        return states;
//    }

//    public static ColorStateList selectorRadioText(Context context, int normal, int pressed) {
//        ColorStateList colorStates = new ColorStateList(new int[][]{new int[]{android.R.attr.state_checked}, new int[]{}}, new int[]{pressed, normal});
//        return colorStates;
//    }

//    public static StateListDrawable selectorRadioDrawable(Drawable normal, Drawable pressed) {
//        StateListDrawable states = new StateListDrawable();
//        states.addState(new int[]{android.R.attr.state_checked}, pressed);
//        states.addState(new int[]{}, normal);
//        return states;
//    }

//    public static StateListDrawable selectorBackgroundColor(Context context, int normal, int pressed) {
//        StateListDrawable states = new StateListDrawable();
//        states.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(pressed));
//        states.addState(new int[]{}, new ColorDrawable(normal));
//        return states;
//    }

//    public static StateListDrawable selectorBackgroundDrawable(Drawable normal, Drawable pressed) {
//        StateListDrawable states = new StateListDrawable();
//        states.addState(new int[]{android.R.attr.state_pressed}, pressed);
//        states.addState(new int[]{}, normal);
//        return states;
//    }

//    public static ColorStateList selectorText(Context context, int normal, int pressed) {
//        ColorStateList colorStates = new ColorStateList(new int[][]{new int[]{android.R.attr.state_pressed}, new int[]{}}, new int[]{pressed, normal});
//        return colorStates;
//    }
}