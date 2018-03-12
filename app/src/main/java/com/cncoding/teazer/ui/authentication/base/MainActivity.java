package com.cncoding.teazer.ui.authentication.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.cncoding.teazer.BuildConfig;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.auth.BaseAuth;
import com.cncoding.teazer.data.model.auth.ForgotPassword;
import com.cncoding.teazer.data.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.data.model.auth.ProceedSignup;
import com.cncoding.teazer.data.model.auth.VerifySignUp;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.ui.authentication.ConfirmOtpFragment;
import com.cncoding.teazer.ui.authentication.ConfirmOtpFragment.OnOtpInteractionListener;
import com.cncoding.teazer.ui.authentication.ForgotPasswordFragment;
import com.cncoding.teazer.ui.authentication.ForgotPasswordFragment.OnForgotPasswordInteractionListener;
import com.cncoding.teazer.ui.authentication.LoginFragment;
import com.cncoding.teazer.ui.authentication.LoginFragment.LoginInteractionListener;
import com.cncoding.teazer.ui.authentication.ResetPasswordFragment;
import com.cncoding.teazer.ui.authentication.ResetPasswordFragment.OnResetForgotPasswordInteractionListener;
import com.cncoding.teazer.ui.authentication.SignupFragment;
import com.cncoding.teazer.ui.authentication.SignupFragment.OnInitialSignupInteractionListener;
import com.cncoding.teazer.ui.authentication.SignupFragment2;
import com.cncoding.teazer.ui.authentication.SignupFragment2.OnFinalSignupInteractionListener;
import com.cncoding.teazer.ui.authentication.WelcomeFragment;
import com.cncoding.teazer.ui.authentication.WelcomeFragment.OnWelcomeInteractionListener;
import com.cncoding.teazer.ui.base.BaseAuthActivity;
import com.cncoding.teazer.ui.base.FragmentNavigation;
import com.cncoding.teazer.ui.common.Interests;
import com.cncoding.teazer.ui.common.Interests.OnInterestsInteractionListener;
import com.cncoding.teazer.ui.home.base.BaseBottomBarActivity;
import com.cncoding.teazer.utilities.common.SharedPrefs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.common.FabricAnalyticsUtil.logSignUpEvent;
import static com.cncoding.teazer.utilities.common.ViewUtils.hideKeyboard;

public class MainActivity extends BaseAuthActivity
        implements FragmentNavigation, OnWelcomeInteractionListener, LoginInteractionListener, OnOtpInteractionListener,
        OnInitialSignupInteractionListener, OnFinalSignupInteractionListener,
        OnForgotPasswordInteractionListener, OnResetForgotPasswordInteractionListener, OnInterestsInteractionListener {

    public static final int DEVICE_TYPE_ANDROID = 2;
    private static final String TAG_WELCOME_FRAGMENT = "welcomeFragment";
    public static final String TAG_LOGIN_FRAGMENT = "loginFragment";
    public static final String TAG_FORGOT_PASSWORD_FRAGMENT = "forgotPasswordFragment";
    public static final String TAG_RESET_PASSWORD_FRAGMENT = "forgotPasswordResetFragment";
    public static final String TAG_SIGNUP_FRAGMENT = "signupFragment";
    public static final String TAG_SELECT_INTERESTS = "selectInterests";
    private static final String TAG_SECOND_SIGNUP_FRAGMENT = "secondSignupFragment";
    public static final String TAG_OTP_FRAGMENT = "otpFragment";
    public static final int LOGIN_WITH_PASSWORD_ACTION = 10;
    public static final int LOGIN_WITH_OTP_ACTION = 11;
    public static final int SOCIAL_SIGNUP_ACTION = 21;
    public static final int SIGNUP_WITH_EMAIL_ACTION = 22;
    public static final int EMAIL_SIGNUP_PROCEED_ACTION = 23;
    public static final int FORGOT_PASSWORD_ACTION = 5;
    private static final String VIDEO_PATH = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.welcome_video;
    static byte[] bte;

    @BindView(R.id.container) RelativeLayout rootView;
    @BindView(R.id.welcome_video) VideoView welcomeVideo;
    @BindView(R.id.main_fragment_container) FrameLayout mainFragmentContainer;
    @BindView(R.id.up_btn) ImageView upBtn;

    String imageUri;
    private FragmentManager fragmentManager;
    private TransitionDrawable transitionDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
////                .penaltyDialog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
////                .penaltyLog()
//                .build());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        try {
            welcomeVideo.setKeepScreenOn(true);
            welcomeVideo.setVideoPath(VIDEO_PATH);
            welcomeVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                    mediaPlayer.start();
                }
            });
            welcomeVideo.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Drawable[] backgrounds = {getResources().getDrawable(android.R.color.transparent),
                getResources().getDrawable(R.color.colorTranslucentBgMainActivity)};
        transitionDrawable = new TransitionDrawable(backgrounds);
        transitionDrawable.setCrossFadeEnabled(true);
        mainFragmentContainer.setBackground(transitionDrawable);

        SharedPrefs.resetAuthToken(getApplicationContext());

        if (fragmentManager.getBackStackEntryCount() == 0 && !isFragmentActive(TAG_WELCOME_FRAGMENT))
            setFragment(TAG_WELCOME_FRAGMENT, false, null);

        SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_USER_PROFILE", Context.MODE_PRIVATE);
        imageUri = prfs.getString("USER_DP_IMAGES", null);

        if (imageUri != null) {
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), Uri.parse(imageUri));
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
                bte = bitmapToByte(scaledBitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void successfullyLoggedIn() {
        startActivity(new Intent(MainActivity.this, BaseBottomBarActivity.class));
        finish();
    }

    private void setFragment(String tag, boolean addToBackStack, Object[] args) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        switch (tag) {
            case TAG_WELCOME_FRAGMENT:
                transaction.replace(R.id.main_fragment_container, new WelcomeFragment(), TAG_WELCOME_FRAGMENT);
                break;
            case TAG_LOGIN_FRAGMENT:
                if (args != null) {
                    popAllBackStack();
                    transaction.replace(R.id.main_fragment_container,
                            LoginFragment.newInstance(((String) args[0]), ((int) args[1]), ((boolean) args[2])),
                            TAG_LOGIN_FRAGMENT);
                } else {
                    toggleUpBtnVisibility(View.VISIBLE);
                    transaction.replace(R.id.main_fragment_container, new LoginFragment(), TAG_LOGIN_FRAGMENT);
                }
                break;
            case TAG_FORGOT_PASSWORD_FRAGMENT:
                transaction.replace(R.id.main_fragment_container,
                        ForgotPasswordFragment.newInstance((String) args[0]), TAG_FORGOT_PASSWORD_FRAGMENT);
                break;
            case TAG_RESET_PASSWORD_FRAGMENT:
                if (args.length >= 3) {
                    transaction.replace(R.id.main_fragment_container,
                            ResetPasswordFragment.newInstance(((String) args[0]), ((int) args[1]), ((boolean) args[2])),
                            TAG_RESET_PASSWORD_FRAGMENT);
                }
                break;
            case TAG_SIGNUP_FRAGMENT:
                toggleUpBtnVisibility(View.VISIBLE);
                transaction.replace(R.id.main_fragment_container, new SignupFragment(), TAG_SIGNUP_FRAGMENT);
                break;
            case TAG_SECOND_SIGNUP_FRAGMENT:
                transaction.replace(R.id.main_fragment_container,
                        SignupFragment2.newInstance(((ProceedSignup) args[0]), ((String) args[1])),
                        TAG_SECOND_SIGNUP_FRAGMENT);
                break;
            case TAG_OTP_FRAGMENT:
                transaction.replace(R.id.main_fragment_container, ConfirmOtpFragment.newInstance(args), TAG_OTP_FRAGMENT);
                break;
            case TAG_SELECT_INTERESTS:
                toggleUpBtnVisibility(View.GONE);
                try {
                    popAllBackStack();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                transaction.replace(R.id.main_fragment_container, Interests.newInstance(
                        false, false, null, null, false),
                        TAG_SELECT_INTERESTS);
                break;
            default:
                break;
        }
        if (addToBackStack)
            transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }

    private void popAllBackStack() {
        try {
            if(fragmentManager != null) {
                String name = fragmentManager.getBackStackEntryAt(0).getName();
                fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startFragmentTransition(boolean reverse, final String tag, final boolean addToBackStack) {
        hideKeyboard(this, upBtn);
        if (!reverse) {
            if (Objects.equals(tag, TAG_SELECT_INTERESTS)) popAllBackStack();
            setFragment(tag, addToBackStack, null);
            transitionDrawable.startTransition(400);
//            new Blur(this).execute();
        } else {
            if (transitionDrawable != null)
                transitionDrawable.reverseTransition(400);
            if (welcomeVideo != null && !welcomeVideo.isPlaying())
                welcomeVideo.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onWelcomeInteraction(int action, final Boolean accountAlreadyExists) {
        switch (action) {
            case LOGIN_WITH_PASSWORD_ACTION:
                startFragmentTransition(false, TAG_LOGIN_FRAGMENT, true);
                break;
            case SIGNUP_WITH_EMAIL_ACTION:
                startFragmentTransition(false, TAG_SIGNUP_FRAGMENT, true);
                break;
            case SOCIAL_SIGNUP_ACTION:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!accountAlreadyExists)
                            startFragmentTransition(false, TAG_SELECT_INTERESTS, false);
                        else
                            successfullyLoggedIn();
                    }
                }, 500);
                break;
            default:
                break;
        }
    }

    @Override public void onLoginFragmentInteraction(int action, BaseAuth baseAuth) {
        try {
            switch (action) {
                case LOGIN_WITH_PASSWORD_ACTION:
                    successfullyLoggedIn();
                    break;
                case LOGIN_WITH_OTP_ACTION:
                    if (baseAuth instanceof InitiateLoginWithOtp)
                    setFragment(TAG_OTP_FRAGMENT, true, new Object[]{baseAuth, LOGIN_WITH_OTP_ACTION});
                    break;
                case FORGOT_PASSWORD_ACTION:
                    if (baseAuth instanceof ForgotPassword)
                        setFragment(TAG_FORGOT_PASSWORD_FRAGMENT, true, new Object[]{((ForgotPassword) baseAuth).getUserName()});
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void onOtpInteraction(VerifySignUp verificationDetails, String picturePath) {
        if (verificationDetails != null) {
            //fabric event
            logSignUpEvent("Email/Phone", true, verificationDetails.getEmail());
            startFragmentTransition(false, TAG_SELECT_INTERESTS, false);
            new UpdateProfilePic(this).execute(picturePath);
        }
        else successfullyLoggedIn();
    }

    @Override
    public void onForgotPasswordInteraction(String enteredText, int countryCode, boolean isEmail) {
        setFragment(TAG_RESET_PASSWORD_FRAGMENT, true, new Object[] {enteredText, countryCode, isEmail});
    }

    @Override
    public void onResetForgotPasswordInteraction(String enteredText, int countryCode, boolean isEmail) {
        setFragment(TAG_LOGIN_FRAGMENT, true, new Object[]{enteredText, countryCode, isEmail});
    }

    @Override
    public void onInitialEmailSignupInteraction(int action, final ProceedSignup proceedSignup, String picturePath) {
        setFragment(TAG_SECOND_SIGNUP_FRAGMENT, true, new Object[]{proceedSignup, picturePath});
    }

    @Override
    public void onFinalEmailSignupInteraction(final VerifySignUp verifySignUp, String picturePath) {
        setFragment(TAG_OTP_FRAGMENT, true, new Object[] {verifySignUp, SIGNUP_WITH_EMAIL_ACTION, picturePath});
    }

    @Override
    public void onInterestsInteraction(boolean isFromDiscover, ArrayList<Category> categories) {
        successfullyLoggedIn();
    }

    @Override
    public void onInterestsSelected(String resultToShow, String resultToSend, int count) {
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    public void pushFragment(Fragment fragment) {

    }

    @Override
    public void pushFragmentOnto(Fragment fragment) {

    }

    @Override
    public void popFragment() {

    }

    private static class UpdateProfilePic extends AsyncTask<String, Void, MultipartBody.Part> {

        WeakReference<MainActivity> reference;

        UpdateProfilePic(MainActivity context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected MultipartBody.Part doInBackground(String... strings) {
            try {
                File profileImage = new File(strings[0]);
                if (profileImage.exists()) {
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), profileImage);
                    return MultipartBody.Part.createFormData("media", profileImage.getName(), requestBody);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(MultipartBody.Part part) {
            if (part != null) {
                ApiCallingService.User.updateUserProfileMedia(part, reference.get()).enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                       // Log.d("IMAGE Url",picturePath)
//                        Toast.makeText(reference.get(), "Profile pic Updated", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(reference.get(), R.string.could_not_set_profile_pic, Toast.LENGTH_SHORT).show();
                    }
                });
            } else
                Toast.makeText(reference.get(), R.string.could_not_set_profile_pic, Toast.LENGTH_SHORT).show();
        }
    }

    public void toggleUpBtnVisibility(int visibility) {
        switch (visibility) {
            case View.VISIBLE:
                if (upBtn.getVisibility() != View.VISIBLE) {
                    upBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));
//                    upBtn.animate().scaleX(1).scaleY(1).alpha(1).setDuration(280).start();
                    upBtn.setVisibility(View.VISIBLE);
                }
                break;
            case View.INVISIBLE:
                if (upBtn.getVisibility() != View.INVISIBLE) {
                    upBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_left));
//                    upBtn.animate().scaleX(0).scaleY(0).alpha(0).setDuration(280).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            upBtn.setVisibility(View.INVISIBLE);
                        }
                    }, 280);
                }
                break;
            default:
                break;
        }
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        Bitmap bitmap = null;
        if (drawable != null) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (welcomeVideo != null)
            if (!welcomeVideo.isPlaying())
                welcomeVideo.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (welcomeVideo != null)
            welcomeVideo.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (welcomeVideo != null) {
            // Make sure we stop video and release resources when activity is destroyed.
            welcomeVideo.stopPlayback();
//            welcomeVideo.release();
            welcomeVideo = null;
        }
    }

    @OnClick(R.id.up_btn) public void backPressed() {
        hideKeyboard(this, upBtn);
        onBackPressed();
    }

    private boolean isFragmentActive(String tag) {
        return fragmentManager.findFragmentByTag(tag) != null;
    }

    @Override
    public void onBackPressed() {
        int backStackCount = fragmentManager.getBackStackEntryCount();
        if (backStackCount > 0) {
            fragmentManager.popBackStack();
            if (backStackCount == 1) {
                toggleUpBtnVisibility(View.INVISIBLE);
                startFragmentTransition(true, null, false);
            }
        } else super.onBackPressed();
    }
}