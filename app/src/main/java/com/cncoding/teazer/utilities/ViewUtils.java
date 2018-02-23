package com.cncoding.teazer.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.coachMark.MaterialShowcaseView;
import com.cncoding.teazer.customViews.coachMark.ShowcaseConfig;
import com.cncoding.teazer.customViews.coachMark.shape.CircleShape;
import com.cncoding.teazer.home.camera.CameraActivity;
import com.cncoding.teazer.model.base.Dimension;
import com.cncoding.teazer.model.base.UploadParams;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.react.MyReactions;
import com.cncoding.teazer.ui.fragment.activity.ExoPlayerActivity;
import com.cncoding.teazer.ui.fragment.activity.ReactionPlayerActivity;

import java.io.File;

import static com.cncoding.teazer.customViews.coachMark.MaterialShowcaseView.TYPE_DISCOVER;
import static com.cncoding.teazer.customViews.coachMark.MaterialShowcaseView.TYPE_NORMAL;
import static com.cncoding.teazer.customViews.coachMark.MaterialShowcaseView.TYPE_POST_DETAILS;
import static com.cncoding.teazer.utilities.SharedPrefs.saveReactionUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.saveVideoUploadSession;

/**
 *
 * Created by Prem $ on 10/27/2017.
 */

@SuppressWarnings("WeakerAccess")
public class ViewUtils {

    //    private String email;
//    public static final int GALLERY_ACTIVITY_CODE=200;
//    public static final int RESULT_CROP = 400;
    public static final String BLANK_SPACE = " ";
    public static final String IS_REACTION = "isCameraLaunchedForReaction";
    public static final String IS_GALLERY = "IsFromGallery";
    public static final String POST_DETAILS = "postId";
    public static final String UPLOAD_PARAMS = "uploadParams";
    public static final int POST_REACTION = 0;
    public static final int SELF_REACTION = 1;

    public static void enableView(View view) {
        view.setEnabled(true);
        view.setAlpha(1);
        view.setBackgroundTintList(null);
    }

    public static void disableView(View view, boolean setAlpha) {
        view.setEnabled(false);
        if (setAlpha) {
            view.setAlpha(0.5f);
            view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#999999")));
        }
    }

//    public static void playVideo(Context context, String videoPath, boolean isOnlineVideo) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        if (isOnlineVideo) {
//            intent.setDataAndType(Uri.parse(videoPath), "video/*");
//        } else {
//            File file = new File(videoPath);
//            intent.setDataAndType(Uri.fromFile(file), "video/*");
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        }
//        context.startActivity(intent);
//    }

    public static void playVideoInExoPlayer(Context context, String videoPath) {
        Intent intent = new Intent(context, ExoPlayerActivity.class);
        intent.putExtra("VIDEO_URL", videoPath);
//        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void playOnlineVideoInExoPlayer(Context context, Integer source, PostReaction postReaction, MyReactions reaction) {
        switch (source) {
            case POST_REACTION: {
                Intent intent = new Intent(context, ReactionPlayerActivity.class);
                intent.putExtra("VIDEO_URL", postReaction.getMediaDetail().getReactMediaUrl());
                intent.putExtra("POST_INFO", postReaction);
                intent.putExtra("SOURCE", POST_REACTION);
//                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            }
            case SELF_REACTION: {
                Intent intent = new Intent(context, ReactionPlayerActivity.class);
                intent.putExtra("VIDEO_URL", reaction.getMediaDetail().getReactMediaUrl());
                intent.putExtra("POST_INFO", reaction);
                intent.putExtra("SOURCE", SELF_REACTION);
//                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            }
        }
    }

    /**
     * Used to show the snackbar above the bottom nav bar.
     * This assumes that the parent of each child activity is a coordinator layout.
     * */
    public static void makeSnackbarWithBottomMargin(Activity activity, View view, CharSequence text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/proxima_nova_regular.otf");
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

    public static boolean isYInScreen(Context context, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        float screenBound = getDeviceHeight(context) -
                context.getResources().getDimension(R.dimen.navigation_bar_height) -
                context.getResources().getDimension(R.dimen.action_bar_height);
        return location[1] < screenBound;
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

    public static void launchVideoUploadCamera(Context packageContext) {
        Intent intent = new Intent(packageContext, CameraActivity.class);
        intent.putExtra(IS_REACTION, false);
        packageContext.startActivity(intent);
    }

    public static void launchReactionCamera(Context packageContext, PostDetails postDetails) {
        Intent intent = new Intent(packageContext, CameraActivity.class);
        intent.putExtra(IS_REACTION, true);
        intent.putExtra(POST_DETAILS, postDetails);
        packageContext.startActivity(intent);
    }

    public static void performVideoUpload(Context packageContext, UploadParams uploadParams) {
        saveVideoUploadSession(packageContext, uploadParams);
        Intent intent = new Intent(packageContext, BaseBottomBarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        packageContext.startActivity(intent);
        ((AppCompatActivity) packageContext).finish();
    }

    public static void performReactionUpload(Context packageContext, UploadParams uploadParams) {
        saveReactionUploadSession(packageContext, uploadParams);
        Intent intent = new Intent(packageContext, BaseBottomBarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        packageContext.startActivity(intent);
        ((AppCompatActivity) packageContext).finish();
    }

    public static GradientDrawable getBackground(Context context, TextView textView, int bgColor,
                                                 int strokeColor, int textColor, float cornerRadius) {
        float density = context.getResources().getDisplayMetrics().density;
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(bgColor);
        gradientDrawable.setCornerRadius((float) (cornerRadius * density + 0.5));
        gradientDrawable.setStroke((int) (1 * density + 0.5), strokeColor);
        textView.setTextColor(textColor);
        return gradientDrawable;
    }

    public static void setActionButtonText(Context context, TextView textView, int resId) {
        textView.setText(resId);
        switch (resId) {
            case R.string.accept:
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.TRANSPARENT,
                        context.getResources().getColor(R.color.colorAccent),
                        context.getResources().getColor(R.color.colorAccent), 2));
                break;
            case R.string.follow:
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.TRANSPARENT,
                        context.getResources().getColor(R.color.colorAccent),
                        context.getResources().getColor(R.color.colorAccent), 2));
                break;
            case R.string.following:
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.TRANSPARENT,
                        context.getResources().getColor(R.color.colorPrimary_text),
                        context.getResources().getColor(R.color.colorPrimary_text), 2));
                break;
            case R.string.requested:
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.TRANSPARENT,
                        context.getResources().getColor(R.color.colorPrimary_text),
                        context.getResources().getColor(R.color.colorPrimary_text), 2));
                break;

            case R.string.unblock:
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.TRANSPARENT,
                        context.getResources().getColor(R.color.colorAccent),
                        context.getResources().getColor(R.color.colorAccent), 2));
                break;
            default:
                break;
        }
    }

    public static void hideKeyboard(Activity activity, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
    //            imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.RESULT_HIDDEN);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

//    public static byte[] getByteArrayFromImage(ImageView imageView) {
//        if (imageView.getDrawable() != null) {
//            try {
//                Bitmap bitmap;
//                if (imageView.getDrawable() instanceof TransitionDrawable)
//                    bitmap = ((GlideBitmapDrawable) ((TransitionDrawable) imageView.getDrawable()).getDrawable(1)).getBitmap();
//                else
//                    bitmap = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
//                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                return outputStream.toByteArray();
//            } catch (ClassCastException e) {
//                if (e.getMessage() != null)
//                    Log.e("Getting thumbnail", e.getMessage());
//                return null;
//            }
//        } else
//            return null;
//    }

    public static void initializeShimmer(ViewGroup shimmerLayout, ViewGroup topLayout,
                                         ViewGroup bottomLayout, ViewGroup vignetteLayout) {
        try {
            shimmerLayout.setVisibility(View.VISIBLE);
            topLayout.setVisibility(View.INVISIBLE);
            bottomLayout.setVisibility(View.INVISIBLE);
            vignetteLayout.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public static void adjustViewSize(Context context, int postWidth, int postHeight, ViewGroup.LayoutParams layoutParams,
                                      int position, SparseArray<Dimension> dimensionSparseArray, boolean isPostList) {
        try {
            int width = isPostList ?
                    getDeviceWidth(context) :
    //                in case of home page post lists, decrease 0.5dp from half the screen width
    //                (ViewUtils.getDeviceWidth(context) - (int)((0.5 * context.getResources().getDisplayMetrics().density) + 0.5)):
    //                in case of other lists, decrease 21dp (14 dp + 7dp) from half the screen width
                    (ViewUtils.getDeviceWidth(context) - (int)((21 * context.getResources().getDisplayMetrics().density) + 0.5)) / 2;

            layoutParams.width = width;
//                resize posts in shimmerLayout
            if (!isPostList) {
                if (postHeight <= postWidth) {
                    postHeight = width;
                    layoutParams.height = postHeight;
                } else {
                    layoutParams.height = width * postHeight / postWidth;
                }
            } else {
                layoutParams.height = layoutParams.width * 3 / 4;
            }
//                int normalHeight = width * postHeight / postWidth;                                  //original aspect ratio
//                int maxHeight = (width * 6) / 5;                                                    //height according to 6:5 ratio
//                layoutParams.height = normalHeight > maxHeight ? maxHeight : normalHeight;
            if (dimensionSparseArray != null)
                dimensionSparseArray.put(position, new Dimension(layoutParams.height, layoutParams.width));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void prepareLayout(ViewGroup layout, ViewGroup shimmerLayout, ViewGroup topLayout,
                                     ViewGroup bottomLayout, ViewGroup vignetteLayout, int width, int height) {
        try {
            layout.setBackgroundResource(height < width ? R.color.material_grey200 : R.color.material_grey200);
            shimmerLayout.setVisibility(View.INVISIBLE);
            topLayout.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
            vignetteLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getPixels(Context context, float dp) {
        return (int)((dp * context.getResources().getDisplayMetrics().density) + 0.5);
    }

    @Nullable
    public static View getTabChild(TabLayout tabLayout, int tabIndex) {
        try {
            return ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(tabIndex);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ShowcaseConfig getShowcaseConfig(final Context context, int layoutType) {
        ShowcaseConfig config = new ShowcaseConfig();
//        config.setMaskColor(Color.parseColor("#CC000000"));
        config.setRenderOverNavigationBar(true);
        config.setShapePadding(layoutType == TYPE_POST_DETAILS || layoutType == TYPE_DISCOVER ? getPixels(context, 10) : 0);
        CircleShape circleShape = new CircleShape(getPixels(context, 50)) {
            @Override
            public void draw(Canvas canvas, Paint paint, int x, int y, int padding) {
                super.draw(canvas, paint, x, y, padding);
                if (this.radius > 0) {
                    Paint paint1 = new Paint();
                    paint1.setStyle(Paint.Style.STROKE);
                    paint1.setColor(Color.WHITE);
                    paint1.setAntiAlias(true);
                    paint1.setStrokeWidth(getPixels(context, 1));
                    canvas.drawCircle(x, y, this.radius + padding, paint1);
                }
            }
        };
        config.setShape(circleShape);
        return config;
    }

    public static void getCoachMark(final BaseBottomBarActivity activity, final Fragment fragment,
                                    final View targetView, final String uniqueId, final int titleResId,
                                    final int contentResId, final int dismissResId, final int type) {
        try {
            if (fragment.isAdded()) {
                if (type == TYPE_NORMAL) {
                    showCoachMark(activity, targetView, uniqueId, titleResId, contentResId, dismissResId, type).show(activity);
                } else {
                    if (isYInScreen(activity, targetView)) {
                        showCoachMark(activity, targetView, uniqueId, titleResId, contentResId, dismissResId, type).show(activity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MaterialShowcaseView showCoachMark(final BaseBottomBarActivity activity,
                                     final View targetView, final String uniqueId, final int titleResId,
                                     final int contentResId, final int dismissResId, final int type) {
        MaterialShowcaseView.Builder builder = new MaterialShowcaseView.Builder(activity)
                .singleUse(uniqueId)
                .renderOverNavigationBar()
                .setTitleText(titleResId)
                .setContentText(contentResId)
                .setDismissText(dismissResId)
                .setDismissOnTouch(true)
                .setTarget(targetView);
        MaterialShowcaseView materialShowcaseView = builder.build();
        materialShowcaseView.setConfig(getShowcaseConfig(activity, type));
        return materialShowcaseView;
    }

    public static GradientDrawable getClassicCategoryBackground(TextView title, String colorString) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        int color = Color.parseColor(colorString);
        gradientDrawable.setColor(Color.TRANSPARENT);
        gradientDrawable.setCornerRadius(getPixels(title.getContext(), 3));
        gradientDrawable.setStroke(getPixels(title.getContext(), 1), color);
        title.setTextColor(color);
        return gradientDrawable;
    }

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
