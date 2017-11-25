package com.cncoding.teazer.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.home.camera.CameraActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Locale;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 *
 * Created by Prem $ on 10/27/2017.
 */

@SuppressWarnings("WeakerAccess")
public class ViewUtils {

    public static final String BLANK_SPACE = " ";
    public static final String IS_REACTION = "isCameraLaunchedForReaction";
    public static final String POST_DETAILS = "postId";
    public static final String UPLOAD_PARAMS = "uploadParams";

    public static void playVideo(Context context, String videoPath, boolean isOnlineVideo) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (isOnlineVideo) {
            intent.setDataAndType(Uri.parse(videoPath), "video/*");
        } else {
            File file = new File(videoPath);
            intent.setDataAndType(Uri.fromFile(file), "video/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        context.startActivity(intent);
    }

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
        int statusBarHeight = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, statusBarHeight);

//        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin,
//                activity.getResources().getDimensionPixelSize(
//                        activity.getResources().getIdentifier("status_bar_height", "dimen", "android")));
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

    public static void showSnackBar(View view, String message) {
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

    public static void launchReactionCamera(Context packageContext, Pojos.Post.PostDetails postDetails) {
        Intent intent = new Intent(packageContext, CameraActivity.class);
        intent.putExtra(IS_REACTION, true);
        intent.putExtra(POST_DETAILS, postDetails);
        packageContext.startActivity(intent);
    }

    public static void performUpload(Context packageContext, Pojos.UploadParams uploadParams) {
        Intent intent = new Intent(packageContext, BaseBottomBarActivity.class);
        intent.putExtra(UPLOAD_PARAMS, uploadParams);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        packageContext.startActivity(intent);
        ((AppCompatActivity) packageContext).finish();
    }

//    public static void showCircularRevealAnimation(final View mRevealView, int centerX, int centerY,
//                                                   float startRadius, float endRadius, int duration, int color, final boolean isReversed){
//        mRevealView.setBackgroundColor(color);
//        Animator animator;
//        if (!isReversed)
//            animator = ViewAnimationUtils.createCircularReveal(mRevealView, centerX, centerY, startRadius, endRadius);
//        else
//            animator = ViewAnimationUtils.createCircularReveal(mRevealView, centerX, centerY, endRadius, startRadius);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        animator.setDuration(duration);
//        animator.start();
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                if (isReversed) {
//                    mRevealView.setVisibility(View.INVISIBLE);
//                    mRevealView.setBackgroundResource(android.R.color.transparent);
//                }
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
//        if (!isReversed)
//            mRevealView.setVisibility(View.VISIBLE);
//    }

    public static void hideKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void updateMediaStoreDatabase(Context context, String videoPath) {
        context.sendBroadcast(
                new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                        .setData(Uri.fromFile(new File(videoPath)))
        );
    }

    public static void deleteFileFromMediaStoreDatabase(Context context, String videoPath) {
        Uri rootUri = MediaStore.Audio.Media.getContentUriForPath(videoPath);  // Change file types here
        context.getContentResolver().delete(rootUri, MediaStore.MediaColumns.DATA + "=?", new String[]{videoPath});
    }

    public static byte[] getByteArrayFromImage(ImageView imageView) {
        if (imageView.getDrawable() != null) {
            Bitmap bitmap;
            if (imageView.getDrawable() instanceof TransitionDrawable)
                bitmap = ((GlideBitmapDrawable) ((TransitionDrawable) imageView.getDrawable()).getDrawable(1)).getBitmap();
            else
                bitmap = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return outputStream.toByteArray();
        } else
            return null;
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
