package com.cncoding.teazer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoding.teazer.WelcomeFragment.OnWelcomeInteractionListener;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.utilities.BlurBuilder;
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
import com.cncoding.teazer.tagsAndCategories.Interests;
import com.cncoding.teazer.tagsAndCategories.Interests.OnInterestsInteractionListener;
import com.cncoding.teazer.utilities.OfflineUserProfile;
import com.cncoding.teazer.utilities.Pojos.Authorize;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.cncoding.teazer.utilities.ViewUtils;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.AuthUtils.getDeviceId;
import static com.cncoding.teazer.utilities.AuthUtils.getFcmToken;

public class MainActivity extends AppCompatActivity
        implements SurfaceTextureListener,
        OnWelcomeInteractionListener,
        LoginInteractionListener, OnOtpInteractionListener,
        OnInitialSignupInteractionListener, OnFinalSignupInteractionListener,
        OnForgotPasswordInteractionListener, OnResetForgotPasswordInteractionListener, OnInterestsInteractionListener {

//    public static final String USER_PROFILE = "profile";
//    public static final String CURRENT_LOGIN_ACTION = "currentLoginAction";
    public static final int DEVICE_TYPE_ANDROID = 2;
    public static final int OPEN_CAMERA_ACTION = 98;
    private static final int SOCIAL_LOGIN_TYPE_FACEBOOK = 1;
    private static final int SOCIAL_LOGIN_TYPE_GOOGLE = 2;
    private static final String TAG_WELCOME_FRAGMENT = "welcomeFragment";
    public static final String TAG_LOGIN_FRAGMENT = "loginFragment";
    public static final String TAG_FORGOT_PASSWORD_FRAGMENT = "forgotPasswordFragment";
    private static final String TAG_FORGOT_PASSWORD_RESET_FRAGMENT = "forgotPasswordResetFragment";
    public static final String TAG_SIGNUP_FRAGMENT = "signupFragment";
    private static final String TAG_SELECT_INTERESTS = "selectInterests";
    private static final String TAG_SECOND_SIGNUP_FRAGMENT = "secondSignupFragment";
    private static final String TAG_OTP_FRAGMENT = "otpFragment";
    public static final int LOGIN_WITH_PASSWORD_ACTION = 10;
//    public static final int LOGIN_WRONG_CREDENTIALS_ACTION = 9;
    public static final int LOGIN_WITH_OTP_ACTION = 11;
//    public static final int FACEBOOK_SIGNUP_BTN_CLICKED = 18;
//    public static final int GOOGLE_SIGNUP_BTN_CLICKED = 19;
    public static final int SIGNUP_WITH_FACEBOOK_ACTION = 20;
    public static final int SIGNUP_WITH_GOOGLE_ACTION = 21;
    public static final int SIGNUP_WITH_EMAIL_ACTION = 22;
    public static final int EMAIL_SIGNUP_PROCEED_ACTION = 23;
//    public static final int SIGNUP_OTP_VERIFICATION_ACTION = 41;
//    public static final int LOGIN_OTP_VERIFICATION_ACTION = 42;
    public static final int FORGOT_PASSWORD_ACTION = 5;
    public static final int BACK_PRESSED_ACTION = 6;
    public static final int RESUME_WELCOME_VIDEO_ACTION = 8;
    public static final String BASE_URL = "http://restdev.ap-south-1.elasticbeanstalk.com/";
    private String VIDEO_PATH;

    @BindView(R.id.welcome_video) TextureView welcomeVideo;
    @BindView(R.id.main_fragment_container) FrameLayout mainFragmentContainer;
    @BindView(R.id.up_btn) ImageView upBtn;

    private MediaPlayer mediaPlayer;
    private ProfileTracker facebookProfileTracker;
    private FragmentManager fragmentManager;
    private TransitionDrawable transitionDrawable;
    private boolean loggingIn;

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

        VIDEO_PATH = "android.resource://" + getPackageName() + "/" + R.raw.welcome_video;

        welcomeVideo.setSurfaceTextureListener(MainActivity.this);
        loggingIn = false;

        if (fragmentManager.getBackStackEntryCount() == 0 && !isFragmentActive(TAG_WELCOME_FRAGMENT))
            setFragment(TAG_WELCOME_FRAGMENT, false, null);

        facebookProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                saveUserProfile(MainActivity.this,
                        currentProfile.getId(),
                        currentProfile.getFirstName(),
                        currentProfile.getLastName(),
                        currentProfile.getName(),
                        "", currentProfile.getProfilePictureUri(100, 100)
                );
            }
        };
    }

    private void successfullyLoggedIn() {
        startActivity(new Intent(MainActivity.this, BaseBottomBarActivity.class));
        finish();
    }

    private void setFragment(String tag, boolean addToBackStack, Object[] args) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        switch (tag) {
            case TAG_WELCOME_FRAGMENT:
                transaction.replace(R.id.main_fragment_container, new WelcomeFragment(), TAG_WELCOME_FRAGMENT);
                break;
            case TAG_LOGIN_FRAGMENT:
                if (args!= null) {
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
                        SignupFragment2.newInstance((Authorize) args[0]), TAG_SECOND_SIGNUP_FRAGMENT);
                break;
            case TAG_OTP_FRAGMENT:
                transaction.replace(R.id.main_fragment_container, ConfirmOtpFragment.newInstance(args), TAG_OTP_FRAGMENT);
                break;
            case TAG_SELECT_INTERESTS:
                toggleUpBtnVisibility(View.VISIBLE);
                transaction.replace(R.id.main_fragment_container, Interests.newInstance(), TAG_SELECT_INTERESTS);
                break;
            default:
                break;
        }
        if (addToBackStack)
            transaction.addToBackStack(tag);
        transaction.commit();
    }

    @SuppressLint("StaticFieldLeak")
    private void startFragmentTransition(boolean reverse, final String tag, final boolean addToBackStack) {
        if (!reverse) {
            setFragment(tag, addToBackStack, null);
            new Blur(this).execute();
        }
        else {
            mainFragmentContainer.setBackgroundColor(Color.TRANSPARENT);
//            if (transitionDrawable != null)
//                transitionDrawable.reverseTransition(600);
            if (mediaPlayer != null && !mediaPlayer.isPlaying())
                mediaPlayer.start();
        }
    }

    private static class Blur extends AsyncTask<Void, Void, Bitmap> {

        private WeakReference<MainActivity> reference;

        Blur(MainActivity context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            if (reference.get().mediaPlayer.isPlaying())
                reference.get().mediaPlayer.pause();
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = reference.get().welcomeVideo.getBitmap();
            return BlurBuilder.blur(reference.get(),
                    Bitmap.createBitmap(bitmap,
                            bitmap.getWidth() / 5, bitmap.getHeight() / 5,
                            bitmap.getWidth() / 3, bitmap.getHeight() / 3));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Drawable[] backgrounds = {reference.get().getResources().getDrawable(R.drawable.bg_transparent),
                    new BitmapDrawable(reference.get().getResources(), bitmap)};
            reference.get().transitionDrawable = new TransitionDrawable(backgrounds);
            reference.get().transitionDrawable.setCrossFadeEnabled(true);
            reference.get().mainFragmentContainer.setBackground(reference.get().transitionDrawable);
            reference.get().transitionDrawable.startTransition(400);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Surface surface = new Surface(surfaceTexture);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setScreenOnWhilePlaying(true);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(VIDEO_PATH));
            mediaPlayer.setSurface(surface);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    @Override
    public void onWelcomeInteraction(int action, Profile facebookProfile, Bundle facebookData,
                                     @Nullable GoogleSignInAccount googleAccount, ProximaNovaSemiboldButton button) {
        switch (action) {
            case LOGIN_WITH_PASSWORD_ACTION:
                startFragmentTransition(false, TAG_LOGIN_FRAGMENT, true);
                break;
            case SIGNUP_WITH_FACEBOOK_ACTION:
                mediaPlayer.pause();
                loggingIn = true;
                if (facebookProfile != null && facebookData != null) {
                    handleFacebookLogin(facebookProfile, facebookData, button);
                }
                break;
            case SIGNUP_WITH_GOOGLE_ACTION:
                mediaPlayer.pause();
                loggingIn = true;
                if (googleAccount != null)
                    handleGoogleSignIn(googleAccount, button);
                break;
            case SIGNUP_WITH_EMAIL_ACTION:
                startFragmentTransition(false, TAG_SIGNUP_FRAGMENT, true);
                break;
            case BACK_PRESSED_ACTION:
                onBackPressed();
                break;
            case RESUME_WELCOME_VIDEO_ACTION:
                if (mediaPlayer != null && loggingIn)
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();
                break;
            case OPEN_CAMERA_ACTION:
//                ViewUtils.launchReactionCamera(this, 37);
                break;
            default:
                break;
        }
    }

    private void handleFacebookLogin(final Profile facebookProfile, final Bundle facebookData,
                                     final ProximaNovaSemiboldButton button) {
        ApiCallingService.Auth.socialSignUp(new Authorize(
                getFcmToken(this),
                getDeviceId(this),
                DEVICE_TYPE_ANDROID,
                facebookProfile.getId(),                                            //social ID
                SOCIAL_LOGIN_TYPE_FACEBOOK,
                facebookData.getString("email"),                               //email
                facebookProfile.getName(),                                          //Username
                facebookProfile.getFirstName(),
                facebookProfile.getLastName()))

                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        loggingIn = false;
                        SharedPrefs.saveAuthToken(MainActivity.this, response.body().getAuthToken());
                        switch (response.code()) {
                            case 201:
                                if (response.body().getStatus()) {
                                    verificationSuccessful(true, null,
                                            facebookData, facebookProfile, button, false);
                                }
                                break;
                            case 200:
                                if (response.body().getStatus()) {
                                    verificationSuccessful(true, null,
                                            facebookData, facebookProfile, button, true);
                                } else {
                                    new UserNameAlreadyAvailableDialog(true, MainActivity.this, null,
                                            facebookProfile, button);
                                }
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        loggingIn = false;
                    }
                });
    }

    private void handleGoogleSignIn(final GoogleSignInAccount googleAccount,
                                    final ProximaNovaSemiboldButton button) {
        ApiCallingService.Auth.socialSignUp(new Authorize(
                getFcmToken(this),
                getDeviceId(this),
                DEVICE_TYPE_ANDROID,
                googleAccount.getId(),              //Social ID
                SOCIAL_LOGIN_TYPE_GOOGLE,
                googleAccount.getEmail(),
                googleAccount.getDisplayName(),     //Username
                googleAccount.getGivenName(),       //First name
                googleAccount.getFamilyName()))     //Last name

                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        loggingIn = false;
                        SharedPrefs.saveAuthToken(MainActivity.this, response.body().getAuthToken());
                        switch (response.code()) {
                            case 201:
                                if (response.body().getStatus()) {
                                    verificationSuccessful(false, googleAccount,
                                            null, null, button, false);
                                }
                                break;
                            case 200:
                                if (response.body().getStatus()) {
                                    verificationSuccessful(false, googleAccount,
                                            null, null, button, true);
                                } else {
                                    new UserNameAlreadyAvailableDialog(false, MainActivity.this, googleAccount,
                                            null, button);
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        loggingIn = false;
                    }
                });
    }

    private void verificationSuccessful(boolean isFacebookAccount, GoogleSignInAccount googleAccount, Bundle facebookData,
                                        Profile facebookProfile, ProximaNovaSemiboldButton button, final boolean accountAlreadyExists) {
        if (isFacebookAccount) {
            saveUserProfile(MainActivity.this,
                    facebookProfile.getId(),
                    facebookProfile.getFirstName(),
                    facebookProfile.getLastName(),
                    facebookProfile.getName(),
                    facebookData.getString("email"),
                    facebookProfile.getProfilePictureUri(500, 500));
            button.doneLoadingAnimation(Color.parseColor("#4469AF"),
                    getBitmapFromVectorDrawable(MainActivity.this, R.drawable.ic_check_white));
        } else {
            saveUserProfile(MainActivity.this,
                    googleAccount.getId(),
                    googleAccount.getGivenName(),
                    googleAccount.getFamilyName(),
                    googleAccount.getDisplayName(),
                    googleAccount.getEmail(),
                    googleAccount.getPhotoUrl());
            button.doneLoadingAnimation(Color.parseColor("#DC4E41"),
                    getBitmapFromVectorDrawable(MainActivity.this, R.drawable.ic_check_white));
        }

            new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!accountAlreadyExists)
                    startFragmentTransition(false, TAG_SELECT_INTERESTS, false);
                else
                    successfullyLoggedIn();
            }
        }, 500);
    }

    private void saveUserProfile(Context context, String id, String firstName,
                                 String lastName, String name, String email, Uri profilePicUri) {
        new OfflineUserProfile(context)
                .setUserId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setUsername(name)
                .setEmail(email)
                .setProfilePicUri(profilePicUri);
    }

    @Override
    public void onLoginFragmentInteraction(int action, Authorize authorize) {
        switch (action) {
            case LOGIN_WITH_PASSWORD_ACTION:
                successfullyLoggedIn();
                break;
            case LOGIN_WITH_OTP_ACTION:
                setFragment(TAG_OTP_FRAGMENT, true, new Object[] {authorize, LOGIN_WITH_OTP_ACTION});
                break;
            case FORGOT_PASSWORD_ACTION:
                setFragment(TAG_FORGOT_PASSWORD_FRAGMENT, true, new Object[] {authorize.getUsername()});
                break;
            default:
                break;
        }
    }

    @Override
    public void onOtpInteraction(Authorize verificationDetails, boolean isVerified) {
        if (isVerified)
            startFragmentTransition(false, TAG_SELECT_INTERESTS, false);
        else {
            setFragment(TAG_LOGIN_FRAGMENT, true,
                    new Object[] {verificationDetails.getEmail(), verificationDetails.getCountryCode(), true});
        }
    }

    @Override
    public void onForgotPasswordInteraction(String enteredText, int countryCode, boolean isEmail) {
        setFragment(TAG_FORGOT_PASSWORD_RESET_FRAGMENT, true, new Object[] {enteredText, countryCode, isEmail});
    }

    @Override
    public void onResetForgotPasswordInteraction(String enteredText, int countryCode, boolean isEmail) {
        setFragment(TAG_LOGIN_FRAGMENT, true, new Object[]{enteredText, countryCode, isEmail});
    }

    @Override
    public void onInitialEmailSignupInteraction(int action, final Authorize signUpDetails) {
        setFragment(TAG_SECOND_SIGNUP_FRAGMENT, true, new Object[]{signUpDetails});
    }

    @Override
    public void onFinalEmailSignupInteraction(int action, final Authorize signUpDetails) {
        setFragment(TAG_OTP_FRAGMENT, true, new Object[] {signUpDetails, SIGNUP_WITH_EMAIL_ACTION});
    }

    @Override
    public void onInterestsInteraction() {
        successfullyLoggedIn();
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

        UserNameAlreadyAvailableDialog(boolean isFacebook, @NonNull Context context, final GoogleSignInAccount googleAccount,
                                       Profile facebookProfile, final ProximaNovaSemiboldButton button) {
            super(context);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
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
                        handleGoogleSignIn(googleAccount, button);
                    }
                }
            });
            dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    fragmentManager.popBackStackImmediate();
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        }

        private void setupEditText(final ProximaNovaRegularAutoCompleteTextView editText) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (editText.getText().toString().equals("")) {
                        editText.setCompoundDrawablesWithIntrinsicBounds(
                                0, 0, R.drawable.ic_error, 0);
                    }
                    else {
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
                    }
                    else {
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
        if (mediaPlayer != null)
            if (!mediaPlayer.isPlaying())
                mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null)
            mediaPlayer.pause();
        facebookProfileTracker.stopTracking();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            // Make sure we stop video and release resources when activity is destroyed.
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @OnClick(R.id.up_btn) public void backPressed() {
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
        }
        else super.onBackPressed();
    }
}