package com.cncoding.teazer.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;

import java.util.Locale;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 *
 * Created by Prem $ on 10/27/2017.
 */

public class ViewUtils {

    /**
     * Used to show the snackbar above the bottom nav bar.
     * This assumes that the parent of each child activity is a coordinator layout.
     * */
    public static void makeSnackbarWithBottomMargin(Activity activity, View view, CharSequence text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/proxima_nova_regular.ttf");
        TextView tv = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_text);
        tv.setTypeface(font);
        Button button = (snackbar.getView()).findViewById(android.support.design.R.id.snackbar_action);
        button.setTypeface(font);
        CoordinatorLayout.LayoutParams params
                = (CoordinatorLayout.LayoutParams) snackbar.getView().getLayoutParams();
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin,
                activity.getResources().getDimensionPixelSize(
                        activity.getResources().getIdentifier("status_bar_height", "dimen", "android")));
        snackbar.getView().setLayoutParams(params);
        snackbar.show();
    }

    public static int getDeviceWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    public static void setTextViewDrawableEnd(AppCompatTextView view, @DrawableRes int drawableResId) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableResId, 0);
    }

    public static void setTextViewDrawableStart(AppCompatTextView view, @DrawableRes int drawableResId) {
        view.setCompoundDrawablesWithIntrinsicBounds(drawableResId, 0, 0, 0);
    }

    public static void setEditTextDrawableEnd(AppCompatAutoCompleteTextView view, @DrawableRes int drawableResId) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableResId, 0);
    }

    public static void clearDrawables(AppCompatAutoCompleteTextView view) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static CountDownTimer startCountDownTimer(final Context context, final ProximaNovaRegularTextView otpVerifiedTextView,
                                                     final ProximaNovaSemiboldButton otpResendBtn) {
        return new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String remainingTime = context.getString(R.string.retry_in) + " " + String.format(Locale.UK, "%02d:%02d",
                        MILLISECONDS.toMinutes(millisUntilFinished),
                        MILLISECONDS.toSeconds(millisUntilFinished) -
                                MINUTES.toSeconds(MILLISECONDS.toMinutes(millisUntilFinished)));
                otpVerifiedTextView.setText(remainingTime);
//                if (isAdded()) {
//                }
            }

            @Override
            public void onFinish() {
                otpVerifiedTextView.setText(R.string.you_can_try_again);
                otpResendBtn.setEnabled(true);
            }
        };
    }
}
