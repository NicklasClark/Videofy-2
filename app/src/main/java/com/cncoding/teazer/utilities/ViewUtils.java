package com.cncoding.teazer.utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.home.camera.CameraActivity;

import java.util.Locale;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 *
 * Created by Prem $ on 10/27/2017.
 */

@SuppressWarnings("WeakerAccess")
public class ViewUtils {

    public static final String IS_REACTION = "isCameraLaunchedForReaction";
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
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        }
        return mDisplayMetrics.widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        }
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

    public static void launchVideoUploadCamera(Context packageContext) {
        Intent intent = new Intent(packageContext, CameraActivity.class);
        intent.putExtra(IS_REACTION, false);
        packageContext.startActivity(intent);
    }

    public static void launchReactionCamera(Context packageContext) {
        Intent intent = new Intent(packageContext, CameraActivity.class);
        intent.putExtra(IS_REACTION, true);
        packageContext.startActivity(intent);
    }

    public static void showCircularRevealAnimation(final View mRevealView, int centerX, int centerY,
                                                   float startRadius, float endRadius, int duration, int color, final boolean isReversed){
        mRevealView.setBackgroundColor(color);
        Animator animator;
        if (!isReversed)
            animator = ViewAnimationUtils.createCircularReveal(mRevealView, centerX, centerY, startRadius, endRadius);
        else
            animator = ViewAnimationUtils.createCircularReveal(mRevealView, centerX, centerY, endRadius, startRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(duration);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isReversed) {
                    mRevealView.setVisibility(View.INVISIBLE);
                    mRevealView.setBackgroundResource(android.R.color.transparent);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        if (!isReversed)
            mRevealView.setVisibility(View.VISIBLE);
    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void zoomImageFromThumb(final ImageView thumbView, final ImageView expandedImageView, View container,
                                          Animator currentAnimator, final long duration) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        final Animator[] mCurrentAnimator = {currentAnimator};
        if (mCurrentAnimator[0] != null) {
            mCurrentAnimator[0].cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        expandedImageView.setBackground(thumbView.getDrawable());

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        container.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(duration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator[0] = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator[0] = null;
            }
        });
        set.start();
        mCurrentAnimator[0] = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator[0] != null) {
                    mCurrentAnimator[0].cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(duration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator[0] = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator[0] = null;
                    }
                });
                set.start();
                mCurrentAnimator[0] = set;
            }
        });
    }

//    /**
//     * Color is #4469AF for Facebook and #DC4E41 for Google.
//     * */
//    private void startCircularReveal(final View revealLayout, final String colorString) {
//        Animator animator = ViewAnimationUtils.createCircularReveal(revealLayout,
//                revealLayout.getWidth() / 2, revealLayout.getBottom() - 50, 0,
//                (float) Math.hypot(revealLayout.getWidth(), revealLayout.getHeight()));
//        animator.setDuration(500);
//        animator.setInterpolator(new DecelerateInterpolator());
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//                revealLayout.setVisibility(View.VISIBLE);
//                revealLayout.setBackgroundColor(Color.parseColor(colorString));
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
////                revealLayout.setVisibility(View.INVISIBLE);
////                revealLayout.setBackgroundColor(Color.TRANSPARENT);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//            }
//        });
//        animator.start();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progressBar.animate().scaleX(1).scaleY(1).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();
//                progressBar.setVisibility(View.VISIBLE);
//            }
//        }, 680);
//    }
//
//    private void stopCircularReveal(final View revealLayout) {
//        final Animator animator = ViewAnimationUtils.createCircularReveal(revealLayout,
//                revealLayout.getWidth() / 2, revealLayout.getBottom() - 50,
//                (float) Math.hypot(revealLayout.getWidth(), revealLayout.getHeight()), 0);
//        animator.setDuration(500);
//        animator.setInterpolator(new DecelerateInterpolator());
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                revealLayout.setVisibility(View.INVISIBLE);
//                revealLayout.setBackgroundColor(Color.TRANSPARENT);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//            }
//        });
//        progressBar.animate().scaleX(0).scaleY(0).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progressBar.setVisibility(View.VISIBLE);
//                animator.start();
//            }
//        }, 250);
//    }

//    public static void unbindDrawables(View view) {
//        if (view != null) {
//            if (view.getBackground() != null) {
//                view.getBackground().setCallback(null);
//            }
//            if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
//                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                    unbindDrawables(((ViewGroup) view).getChildAt(i));
//                }
//                ((ViewGroup) view).removeAllViews();
//            }
//        }
//    }

//    public static void showSnackBarAboveNavBar(Context context, Snackbar snackbar) {
//        final View snackBarView = snackbar.getView();
//        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();
//
//        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin,
//                (int) (params.bottomMargin + TypedValue.applyDimension(
//                        TypedValue.COMPLEX_UNIT_DIP, 48, context.getResources().getDisplayMetrics())));
//
//        snackBarView.setLayoutParams(params);
//        snackbar.show();
//    }
}
