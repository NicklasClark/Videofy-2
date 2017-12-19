package com.cncoding.teazer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cncoding.teazer.WelcomeFragment.OnWelcomeInteractionListener;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.asynctasks.GenerateBitmapFromUrl;
import com.cncoding.teazer.authentication.ConfirmOtpFragment;
import com.cncoding.teazer.authentication.ConfirmOtpFragment.OnOtpInteractionListener;
import com.cncoding.teazer.authentication.ForgotPasswordFragment;
import com.cncoding.teazer.authentication.ForgotPasswordFragment.OnForgotPasswordInteractionListener;
import com.cncoding.teazer.authentication.ForgotPasswordResetFragment;
import com.cncoding.teazer.authentication.ForgotPasswordResetFragment.OnResetForgotPasswordInteractionListener;
import com.cncoding.teazer.authentication.LoginFragment;
import com.cncoding.teazer.authentication.LoginFragment.LoginInteractionListener;
import com.cncoding.teazer.authentication.SignupFragment;
import com.cncoding.teazer.authentication.SignupFragment.OnInitialSignupInteractionListener;
import com.cncoding.teazer.authentication.SignupFragment2;
import com.cncoding.teazer.authentication.SignupFragment2.OnFinalSignupInteractionListener;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.home.tagsAndCategories.Interests;
import com.cncoding.teazer.home.tagsAndCategories.Interests.OnInterestsInteractionListener;
import com.cncoding.teazer.model.base.Authorize;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.cncoding.teazer.utilities.ViewUtils;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.File;
import java.lang.ref.WeakReference;

import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.AuthUtils.getDeviceId;
import static com.cncoding.teazer.utilities.AuthUtils.getFcmToken;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;

public class MainActivity extends AppCompatActivity
        implements
//        SurfaceTextureListener,
        OnWelcomeInteractionListener,
        LoginInteractionListener, OnOtpInteractionListener,
        OnInitialSignupInteractionListener, OnFinalSignupInteractionListener,
        OnForgotPasswordInteractionListener, OnResetForgotPasswordInteractionListener, OnInterestsInteractionListener {

    //    public static final String USER_PROFILE = "profile";
//    public static final String CURRENT_LOGIN_ACTION = "currentLoginAction";
    public static final int ACCOUNT_TYPE_PRIVATE = 1;
    public static final int ACCOUNT_TYPE_PUBLIC = 2;

    public static final int DEVICE_TYPE_ANDROID = 2;
    //    public static final int OPEN_CAMERA_ACTION = 98;
    private static final int SOCIAL_LOGIN_TYPE_FACEBOOK = 1;
    private static final int SOCIAL_LOGIN_TYPE_GOOGLE = 2;
    private static final String TAG_WELCOME_FRAGMENT = "welcomeFragment";
    public static final String TAG_LOGIN_FRAGMENT = "loginFragment";
    public static final String TAG_FORGOT_PASSWORD_FRAGMENT = "forgotPasswordFragment";
    public static final String TAG_FORGOT_PASSWORD_RESET_FRAGMENT = "forgotPasswordResetFragment";
    public static final String TAG_SIGNUP_FRAGMENT = "signupFragment";
    public static final String TAG_SELECT_INTERESTS = "selectInterests";
    private static final String TAG_SECOND_SIGNUP_FRAGMENT = "secondSignupFragment";
    public static final String TAG_OTP_FRAGMENT = "otpFragment";
    public static final int LOGIN_WITH_PASSWORD_ACTION = 10;
    //    public static final int LOGIN_WRONG_CREDENTIALS_ACTION = 9;
    public static final int LOGIN_WITH_OTP_ACTION = 11;
    //    public static final int FACEBOOK_SIGNUP_BTN_CLICKED = 18;
//    public static final int GOOGLE_SIGNUP_BTN_CLICKED = 19;
    public static final int SIGNUP_WITH_FACEBOOK_ACTION = 20;
    public static final int SIGNUP_WITH_GOOGLE_ACTION = 21;
    public static final int SIGNUP_WITH_EMAIL_ACTION = 22;
    //    public static final int SIGNUP_FAILED_ACTION = 23;
    public static final int EMAIL_SIGNUP_PROCEED_ACTION = 23;
    //    public static final int SIGNUP_OTP_VERIFICATION_ACTION = 41;
//    public static final int LOGIN_OTP_VERIFICATION_ACTION = 42;
    public static final int FORGOT_PASSWORD_ACTION = 5;
    private static final String VIDEO_PATH = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.welcome_video;

    @BindView(R.id.container)
    RelativeLayout rootView;
    @BindView(R.id.welcome_video)
    VideoView welcomeVideo;
    @BindView(R.id.main_fragment_container)
    FrameLayout mainFragmentContainer;
    @BindView(R.id.up_btn)
    ImageView upBtn;

    //    private MediaPlayer mediaPlayer;
    private FragmentManager fragmentManager;
    //    private BlurView.ControllerSettings settings;
//    private ValueAnimator animator;
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

//        welcomeVideo.setSurfaceTextureListener(MainActivity.this);
        try {
            welcomeVideo.setKeepScreenOn(true);
            welcomeVideo.setVideoPath(VIDEO_PATH);
            welcomeVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
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
                    String name = fragmentManager.getBackStackEntryAt(0).getName();
                    fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            case TAG_FORGOT_PASSWORD_RESET_FRAGMENT:
                if (args.length >= 3) {
                    transaction.replace(R.id.main_fragment_container,
                            ForgotPasswordResetFragment.newInstance(((String) args[0]), ((int) args[1]), ((boolean) args[2])),
                            TAG_FORGOT_PASSWORD_FRAGMENT);
                }
                break;
            case TAG_SIGNUP_FRAGMENT:
                toggleUpBtnVisibility(View.VISIBLE);
                transaction.replace(R.id.main_fragment_container, new SignupFragment(), TAG_SIGNUP_FRAGMENT);
                break;
            case TAG_SECOND_SIGNUP_FRAGMENT:
                transaction.replace(R.id.main_fragment_container,
                        SignupFragment2.newInstance(((String) args[0]), ((String) args[1]), ((String) args[2])),
                        TAG_SECOND_SIGNUP_FRAGMENT);
                break;
            case TAG_OTP_FRAGMENT:
                transaction.replace(R.id.main_fragment_container, ConfirmOtpFragment.newInstance(args), TAG_OTP_FRAGMENT);
                break;
            case TAG_SELECT_INTERESTS:
                toggleUpBtnVisibility(View.VISIBLE);
                try {
                    String name = fragmentManager.getBackStackEntryAt(0).getName();
                    fragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                transaction.replace(R.id.main_fragment_container, Interests.newInstance(
                        false, false, null, null),
                        TAG_SELECT_INTERESTS);
                break;
            default:
                break;
        }
        if (addToBackStack)
            transaction.addToBackStack(tag);
        transaction.commit();
    }

    private void startFragmentTransition(boolean reverse, final String tag, final boolean addToBackStack) {
        hideKeyboard(this, upBtn);
        if (!reverse) {
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

//    private static class Blur extends AsyncTask<Void, Void, Bitmap> {
//
//        private WeakReference<MainActivity> reference;
//
//        Blur(MainActivity context) {
//            reference = new WeakReference<>(context);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            if (reference.get().mediaPlayer.isPlaying())
//                reference.get().mediaPlayer.pause();
//        }
//
//        @Override
//        protected Bitmap doInBackground(Void... voids) {
//            Bitmap bitmap = reference.get().welcomeVideo.getBitmap();
//            return BlurBuilder.blur(reference.get(),
//                    Bitmap.createBitmap(bitmap,
//                            bitmap.getWidth() / 5, bitmap.getHeight() / 5,
//                            bitmap.getWidth() / 3, bitmap.getHeight() / 3));
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            Drawable[] backgrounds = {reference.get().getResources().getDrawable(R.drawable.bg_transparent),
//                    new BitmapDrawable(reference.get().getResources(), bitmap)};
//            reference.get().transitionDrawable = new TransitionDrawable(backgrounds);
//            reference.get().transitionDrawable.setCrossFadeEnabled(true);
//            reference.get().secondFragmentContainer.setBackground(reference.get().transitionDrawable);
//            reference.get().transitionDrawable.startTransition(400);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
//        Surface surface = new Surface(surfaceTexture);
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setScreenOnWhilePlaying(true);
//        try {
//            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(VIDEO_PATH));
//            mediaPlayer.setSurface(surface);
//            mediaPlayer.setLooping(true);
//            mediaPlayer.prepareAsync();
//
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    mediaPlayer.start();
//                }
//            });
//        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//        return false;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//    }

    @Override
    public void onWelcomeInteraction(int action, Profile facebookProfile, Bundle facebookData,
                                     @Nullable GoogleSignInAccount googleAccount, ProximaNovaSemiboldButton button) {
        switch (action) {
            case LOGIN_WITH_PASSWORD_ACTION:
//                setFragment(TAG_LOGIN_FRAGMENT, true, null);
                startFragmentTransition(false, TAG_LOGIN_FRAGMENT, true);
                break;
            case SIGNUP_WITH_FACEBOOK_ACTION:
//                mediaPlayer.pause();
                if (facebookProfile != null && facebookData != null) {
                    handleFacebookLogin(facebookProfile, facebookData, button, null);
                }
                break;
            case SIGNUP_WITH_GOOGLE_ACTION:
//                mediaPlayer.pause();
                if (googleAccount != null)
                    handleGoogleSignIn(googleAccount, button, null);
                break;
            case SIGNUP_WITH_EMAIL_ACTION:
//                setFragment(TAG_SIGNUP_FRAGMENT, true, null);
                startFragmentTransition(false, TAG_SIGNUP_FRAGMENT, true);
                break;
//            case RESUME_WELCOME_VIDEO_ACTION:
//                if (mediaPlayer != null && loggingIn)
//                    if (!mediaPlayer.isPlaying())
//                        mediaPlayer.start();
//                break;
            default:
                break;
        }
    }

    private void handleFacebookLogin(final Profile facebookProfile, final Bundle facebookData,
                                     final ProximaNovaSemiboldButton button, String username) {

        username = username == null ? facebookProfile.getName() : username.replace(" ", "");


        ApiCallingService.Auth.socialSignUp(new Authorize(
                getFcmToken(this),
                getDeviceId(this),
                DEVICE_TYPE_ANDROID,
                facebookProfile.getId(),                                            //social ID
                SOCIAL_LOGIN_TYPE_FACEBOOK,
                facebookData.getString("email"),                               //email
                username,                                                           //Username
                facebookProfile.getFirstName(),
                facebookProfile.getLastName(),
                facebookData.getString("profile_pic")))

                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
//                        SharedPrefs.saveAuthToken(getApplicationContext(), response.body().getAuthToken());//1
                        try {
                            switch (response.code()) {
                                case 201:
                                    if (response.body().getStatus()) {
                                        SharedPrefs.saveAuthToken(getApplicationContext(), response.body().getAuthToken());//1
                                        verificationSuccessful(true,
                                                button, false);
                                    }
                                    break;
                                case 200:
                                    SharedPrefs.saveAuthToken(getApplicationContext(), response.body().getAuthToken());//1
                                    if (response.body().getStatus()) {
                                        verificationSuccessful(true,
                                                button, true);
                                    } else {
                                        new UserNameAlreadyAvailableDialog(true, MainActivity.this,
                                                null,
                                                facebookProfile, facebookData, button);
                                    }
                                    break;
                                default:
                                    Log.d("handleFacebookLogin()", response.code() + "_" + response.message());
                                    button.revertAnimation(new OnAnimationEndListener() {
                                        @Override
                                        public void onAnimationEnd() {
                                            button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook_white,
                                                    0, 0, 0);
                                        }
                                    });
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        Log.d("handleFacebookLogin()", t.getMessage());
                        button.revertAnimation(new OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook_white, 0,
                                        0, 0);
                            }
                        });
                    }
                });

        GenerateBitmapFromUrl generateBitmapFromUrl = new GenerateBitmapFromUrl(this);
        generateBitmapFromUrl.execute(facebookData.getString("profile_pic"));
    }

    private void handleGoogleSignIn(final GoogleSignInAccount googleAccount, final ProximaNovaSemiboldButton button,
                                    String username) {

//        String profilePicUrl = googleAccount.getPhotoUrl().toString();

        username = username == null ? googleAccount.getDisplayName() : username;

        Authorize authorize = new Authorize(
                getFcmToken(this),
                getDeviceId(this),
                DEVICE_TYPE_ANDROID,
                googleAccount.getId(),              //Social ID
                SOCIAL_LOGIN_TYPE_GOOGLE,
                googleAccount.getEmail(),
                username,                           //Username
                googleAccount.getGivenName(),       //First name
                googleAccount.getFamilyName(),      //Last name
                googleAccount.getPhotoUrl() != null ? googleAccount.getPhotoUrl().toString() : null);

        ApiCallingService.Auth.socialSignUp(authorize)

                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        switch (response.code()) {
                            case 201:
                                if (response.body().getStatus()) {
                                    SharedPrefs.saveAuthToken(getApplicationContext(), response.body().getAuthToken());//2
                                    verificationSuccessful(false,
                                            button, false);
                                }
                                break;
                            case 200:
                                SharedPrefs.saveAuthToken(getApplicationContext(), response.body().getAuthToken());//2
                                if (response.body().getStatus()) {
                                    verificationSuccessful(false,
                                            button, true);
                                } else {
                                    new UserNameAlreadyAvailableDialog(false, MainActivity.this, googleAccount,
                                            null, null, button);
                                }
                                break;
                            default:
                                Log.d("handleGoogleSignIn()", response.code() + "_" + response.message());
                                button.revertAnimation(new OnAnimationEndListener() {
                                    @Override
                                    public void onAnimationEnd() {
                                        button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_google_white,
                                                0, 0, 0);
                                    }
                                });
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        Log.d("handleGoogleSignIn()", t.getMessage());
                        button.revertAnimation(new OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_google_white, 0,
                                        0, 0);
                            }
                        });
                    }
                });

        GenerateBitmapFromUrl generateBitmapFromUrl = new GenerateBitmapFromUrl(this);
        generateBitmapFromUrl.execute(googleAccount.getPhotoUrl().toString());
    }

    private void verificationSuccessful(boolean isFacebookAccount, ProximaNovaSemiboldButton button,
                                        final boolean accountAlreadyExists) {
        if (isFacebookAccount) {
            button.doneLoadingAnimation(Color.parseColor("#4469AF"),
                    getBitmapFromVectorDrawable(MainActivity.this, R.drawable.ic_check_white));
        } else {
            button.doneLoadingAnimation(Color.parseColor("#DC4E41"),
                    getBitmapFromVectorDrawable(MainActivity.this, R.drawable.ic_check_white));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!accountAlreadyExists)
//                    setFragment(TAG_SELECT_INTERESTS, false, null);
                    startFragmentTransition(false, TAG_SELECT_INTERESTS, false);
                else
                    successfullyLoggedIn();
            }
        }, 500);
    }

    @Override
    public void onLoginFragmentInteraction(int action, Authorize authorize) {
        switch (action) {
            case LOGIN_WITH_PASSWORD_ACTION:
                successfullyLoggedIn();
                break;
            case LOGIN_WITH_OTP_ACTION:
                setFragment(TAG_OTP_FRAGMENT, true, new Object[]{authorize, LOGIN_WITH_OTP_ACTION});
                break;
            case FORGOT_PASSWORD_ACTION:
                setFragment(TAG_FORGOT_PASSWORD_FRAGMENT, true, new Object[]{authorize.getUsername()});
                break;
            default:
                break;
        }
    }

    @Override
    public void onOtpInteraction(Authorize verificationDetails, String picturePath) {
        if (verificationDetails != null) {
//            setFragment(TAG_SELECT_INTERESTS, false, null);
            startFragmentTransition(false, TAG_SELECT_INTERESTS, false);
            new UpdateProfilePic(this).execute(picturePath);
        } else {
//            if (isVerified)
            successfullyLoggedIn();
        }
    }

    @Override
    public void onForgotPasswordInteraction(String enteredText, int countryCode, boolean isEmail) {
        setFragment(TAG_FORGOT_PASSWORD_RESET_FRAGMENT, true, new Object[]{enteredText, countryCode, isEmail});
    }

    @Override
    public void onResetForgotPasswordInteraction(String enteredText, int countryCode, boolean isEmail) {
        setFragment(TAG_LOGIN_FRAGMENT, true, new Object[]{enteredText, countryCode, isEmail});
    }

    @Override
    public void onInitialEmailSignupInteraction(int action, final Authorize signUpDetails, String picturePath) {
        setFragment(TAG_SECOND_SIGNUP_FRAGMENT, true,
                new Object[]{signUpDetails.getUsername(), signUpDetails.getPassword(), picturePath});
    }

    @Override
    public void onFinalEmailSignupInteraction(final Authorize signUpDetails, String picturePath) {
        setFragment(TAG_OTP_FRAGMENT, true, new Object[]{signUpDetails, SIGNUP_WITH_EMAIL_ACTION, picturePath});
    }

    @Override
    public void onInterestsInteraction() {
        successfullyLoggedIn();
    }

    @Override
    public void onInterestsSelected(String resultToShow, String resultToSend, int count) {
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

    private class UserNameAlreadyAvailableDialog extends AlertDialog.Builder {

        UserNameAlreadyAvailableDialog(final boolean isFacebook, @NonNull Context context,
                                       final GoogleSignInAccount googleAccount,
                                       final Profile facebookProfile, final Bundle facebookData,
                                       final ProximaNovaSemiboldButton button) {
            super(context);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogTheme);
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_alternate_email, null);
            dialogBuilder.setView(dialogView);

            final ProximaNovaRegularAutoCompleteTextView editText = dialogView.findViewById(R.id.edit_query);

            setupEditText(editText);

            dialogBuilder.setTitle(R.string.this_is_embarrassing);
            if (isFacebook) {
                dialogBuilder.setMessage(getString(R.string.the_username) + facebookProfile.getName()
                        + getString(R.string.username_already_exists));
            } else {
                dialogBuilder.setMessage(getString(R.string.the_username) + googleAccount.getDisplayName()
                        + getString(R.string.username_already_exists));
            }
            dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (!editText.getText().toString().equals("")) {
                        if (isFacebook)
                            handleFacebookLogin(facebookProfile, facebookData, button, editText.getText().toString());
                        else
                            handleGoogleSignIn(googleAccount, button, editText.getText().toString());
                    }
                }
            });
            dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    fragmentManager.popBackStackImmediate();
                }
            });
            AlertDialog dialog = dialogBuilder.create();
//            button.setTextColor(Color.parseColor("#757575"));
//            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#5a000000"));
//            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#5a000000"));
//            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(
// MainActivity.this, R.color.black));
            dialog.show();
        }

        private void setupEditText(final ProximaNovaRegularAutoCompleteTextView editText) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (editText.getText().toString().equals("")) {
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, R.drawable.ic_error, 0);
                    } else {
                        if (editText.getCompoundDrawables()[2] != null) {
                            editText.setCompoundDrawablesWithIntrinsicBounds(
                                    0, 0, 0, 0);
                        }
                    }
                }
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().equals("")) {
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, R.drawable.ic_error, 0);
                    } else {
                        if (editText.getCompoundDrawables()[2] != null) {
                            editText.setCompoundDrawablesWithIntrinsicBounds(
                                    0, 0, 0, 0);
                        }
                    }
                }
            });

            editText.requestFocus();

            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    ViewUtils.hideKeyboard(MainActivity.this, textView);
                    return true;
                }
            });
        }
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

    @OnClick(R.id.up_btn)
    public void backPressed() {
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



//    public void getBitmapFromUrl(String imageUrl)
//    {
//        if (imageUrl != null) {
//            try {
//                Bitmap bitmap = null;
//    //            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(url));
//                try {
//                    URL url = new URL(imageUrl);
//                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                } catch(IOException e) {
//                    e.printStackTrace();
//                }
//    //            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
//
//
//                byte[] bte = bitmaptoByte(bitmap);
//    //            SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
//    //            SharedPreferences.Editor editor = preferences.edit();
//    //            editor.putString("MYIMAGES", url);
//    //            editor.apply();
//
//                // File profileImage = new File(r.getPath());
//                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), bte);
//                MultipartBody.Part body = MultipartBody.Part.createFormData("media", "profile_image.jpg", reqFile);
//                saveDataToDatabase(body);
//
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }


}