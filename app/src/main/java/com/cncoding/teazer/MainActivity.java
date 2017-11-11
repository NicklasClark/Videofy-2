package com.cncoding.teazer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.authentication.BlurBuilder;
import com.cncoding.teazer.authentication.ConfirmOtpFragment;
import com.cncoding.teazer.authentication.ForgotPasswordFragment;
import com.cncoding.teazer.authentication.ForgotPasswordResetFragment;
import com.cncoding.teazer.authentication.LoginFragment;
import com.cncoding.teazer.authentication.SignupFragment;
import com.cncoding.teazer.authentication.SignupFragment2;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.home.Interests;
import com.cncoding.teazer.utilities.AuthUtils;
import com.cncoding.teazer.utilities.OfflineUserProfile;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Authorize;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.home.Interests.LAUNCH_TYPE_SIGNUP;
import static com.cncoding.teazer.utilities.AuthUtils.getDeviceId;
import static com.cncoding.teazer.utilities.AuthUtils.getFcmToken;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.LoginInteractionListener,
        SignupFragment.OnEmailSignupInteractionListener,
        SignupFragment2.OnFinalSignupInteractionListener,
        WelcomeFragment.OnWelcomeInteractionListener,
        ForgotPasswordFragment.OnForgotPasswordInteractionListener,
        ConfirmOtpFragment.OnOtpInteractionListener,
        ForgotPasswordResetFragment.OnResetForgotPasswordInteractionListener,
        TextureView.SurfaceTextureListener {

//    public static final String USER_PROFILE = "profile";
//    public static final String CURRENT_LOGIN_ACTION = "currentLoginAction";
    public static final int DEVICE_TYPE_ANDROID = 2;
//    public static final int OPEN_CAMERA_ACTION = 98;
    private static final int SOCIAL_LOGIN_TYPE_FACEBOOK = 1;
    private static final int SOCIAL_LOGIN_TYPE_GOOGLE = 2;
    private static final String TAG_WELCOME_FRAGMENT = "welcomeFragment";
    public static final String TAG_LOGIN_FRAGMENT = "loginFragment";
    public static final String TAG_FORGOT_PASSWORD_FRAGMENT = "forgotPasswordFragment";
    private static final String TAG_FORGOT_PASSWORD_RESET_FRAGMENT = "forgotPasswordResetFragment";
    public static final String TAG_SIGNUP_FRAGMENT = "signupFragment";
    public static final String TAG_SELECT_CATEGORIES = "selectCategories";
    private static final String TAG_SECOND_SIGNUP_FRAGMENT = "secondSignupFragment";
    private static final String TAG_OTP_FRAGMENT = "otpFragment";
    public static final int LOGIN_WITH_PASSWORD_ACTION = 10;
    public static final int LOGIN_WITH_OTP_ACTION = 11;
    public static final int FACEBOOK_SIGNUP_BTN_CLICKED = 18;
    public static final int GOOGLE_SIGNUP_BTN_CLICKED = 19;
    public static final int SIGNUP_WITH_FACEBOOK_ACTION = 20;
    public static final int SIGNUP_WITH_GOOGLE_ACTION = 21;
    public static final int SIGNUP_WITH_EMAIL_ACTION = 22;
    public static final int EMAIL_SIGNUP_PROCEED_ACTION = 23;
    public static final int SIGNUP_OTP_VERIFICATION_ACTION = 41;
    public static final int LOGIN_OTP_VERIFICATION_ACTION = 42;
    public static final int FORGOT_PASSWORD_ACTION = 5;
    public static final int BACK_PRESSED_ACTION = 6;
    public static final int RESUME_WELCOME_VIDEO_ACTION = 8;
    public static final String BASE_URL = "http://restdev.ap-south-1.elasticbeanstalk.com/";
    private String VIDEO_PATH;

    @BindView(R.id.main_fragment_bg) ImageView blurBg;
    @BindView(R.id.welcome_video) TextureView welcomeVideo;
    @BindView(R.id.main_fragment_container) FrameLayout mainFragmentContainer;
    @BindView(R.id.up_btn) ImageView upBtn;

//    private int currentLoginAction;
//    private GoogleSignInAccount googleAccount;
//    private Profile facebookProfile;
    private MediaPlayer mediaPlayer;
    private ProfileTracker facebookProfileTracker;
    private FragmentManager fragmentManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private TransitionDrawable transitionDrawable;
    private boolean isFirebaseSignup;
    private Bitmap bitmap;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        VIDEO_PATH = "android.resource://" + getPackageName() + "/" + R.raw.welcome_video;

        welcomeVideo.setSurfaceTextureListener(MainActivity.this);
        isFirebaseSignup = false;
        firebaseAuth = FirebaseAuth.getInstance();
        setFirebaseAuthStateListener();
    }

    private void setFirebaseAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
//                    user is signed in
                    new OfflineUserProfile(MainActivity.this).setEmail(user.getEmail());
                    if (!isFirebaseSignup) {
                        startActivity(new Intent(MainActivity.this, BaseBottomBarActivity.class));
                        finish();
                    } else {
                        setFragment(TAG_SELECT_CATEGORIES, false, null);
                    }
                } else {
//                    user is signed out, set Welcome fragment
                    if (fragmentManager.getBackStackEntryCount() == 0 && !isFragmentActive(TAG_WELCOME_FRAGMENT))
                        setFragment(TAG_WELCOME_FRAGMENT, false, null);
                }
            }
        };
        facebookProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                saveUserProfile(MainActivity.this,
                        currentProfile.getId(),
                        currentProfile.getFirstName(),
                        currentProfile.getLastName(),
                        currentProfile.getName(),
                        currentProfile.getProfilePictureUri(100, 100)
                );
            }
        };
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
            case TAG_SELECT_CATEGORIES:
                toggleUpBtnVisibility(View.INVISIBLE);
                transaction.replace(R.id.main_fragment_container, Interests.newInstance(LAUNCH_TYPE_SIGNUP), TAG_SELECT_CATEGORIES);
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
//            showCircularRevealAnimation(revealLayout, (int) touchCoordinates[0], (int) touchCoordinates[1], Color.argb(200, 0, 0, 0));
//            revealLayout.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
//            revealLayout.setVisibility(View.INVISIBLE);
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.pause();
                    setFragment(tag, addToBackStack, null);
                    bitmap = welcomeVideo.getBitmap();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    bitmap = BlurBuilder.blur(MainActivity.this,
                            Bitmap.createBitmap(bitmap,
                                    bitmap.getWidth() / 5, bitmap.getHeight() / 5,
                                    bitmap.getWidth() / 3, bitmap.getHeight() / 3));
                    return null;
                }

                @SuppressWarnings("deprecation")
                @Override
                protected void onPostExecute(Void aVoid) {
                    Drawable[] backgrounds = {getResources().getDrawable(R.drawable.bg_transparent),
                            new BitmapDrawable(getResources(), bitmap)};
                    transitionDrawable = new TransitionDrawable(backgrounds);
                    transitionDrawable.setCrossFadeEnabled(true);
                    mainFragmentContainer.setBackground(transitionDrawable);
                    transitionDrawable.startTransition(400);
                }
            }.execute();
        }
        else {
            if (transitionDrawable != null)
                transitionDrawable.reverseTransition(600);
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
    public void onWelcomeInteraction(int action, @Nullable Object token, Profile facebookProfile, Bundle facebookData,
                                     @Nullable GoogleSignInAccount googleAccount, ProximaNovaSemiboldButton button) {
        switch (action) {
            case LOGIN_WITH_PASSWORD_ACTION:
                startFragmentTransition(false, TAG_LOGIN_FRAGMENT, true);
                break;
            case SIGNUP_WITH_FACEBOOK_ACTION:
                if (token != null && facebookProfile != null && facebookData != null) {
                    handleFacebookLogin((AccessToken) token, facebookProfile, facebookData, button);
                }
                break;
            case SIGNUP_WITH_GOOGLE_ACTION:
                if (token != null && googleAccount != null)
                    handleGoogleSignIn((String) token,  googleAccount, button);
                break;
            case SIGNUP_WITH_EMAIL_ACTION:
                startFragmentTransition(false, TAG_SIGNUP_FRAGMENT, true);
                break;
            case BACK_PRESSED_ACTION:
                onBackPressed();
                break;
            case RESUME_WELCOME_VIDEO_ACTION:
                if (mediaPlayer != null)
                    if (!mediaPlayer.isPlaying())
                        mediaPlayer.start();
                break;
//            case OPEN_CAMERA_ACTION:
////                setFragment("interests", true, null);
//                startActivity(new Intent(this, BaseBottomBarActivity.class));
////                finish();
//                break;
            default:
                break;
        }
    }

    private void handleFacebookLogin(final AccessToken token, final Profile facebookProfile, Bundle facebookData, final ProximaNovaSemiboldButton button) {
        ApiCallingService.Auth.socialSignUp(new Authorize(
                getFcmToken(this),
                getDeviceId(),
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
                        SharedPrefs.saveAuthToken(MainActivity.this, response.body().getAuthToken());
                        if (response.code() == 200 || response.code() == 201) {
                            if (response.body().getStatus()) {
                                saveUserProfile(MainActivity.this,
                                        facebookProfile.getId(),
                                        facebookProfile.getFirstName(),
                                        facebookProfile.getLastName(),
                                        facebookProfile.getName(),
                                        facebookProfile.getProfilePictureUri(100, 100));

                                firebaseCredentialSignin(FacebookAuthProvider.getCredential(token.getToken()), button);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                    }
                });
    }

    private void handleGoogleSignIn(final String token, final GoogleSignInAccount googleAccount, final ProximaNovaSemiboldButton button) {
        ApiCallingService.Auth.socialSignUp(new Authorize(
                getFcmToken(this),
                getDeviceId(),
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
                        SharedPrefs.saveAuthToken(MainActivity.this, response.body().getAuthToken());
                        if (response.code() == 200 || response.code() == 201) {
                            if (response.body().getStatus()) {
                                saveUserProfile(MainActivity.this,
                                        googleAccount.getId(),
                                        googleAccount.getGivenName(),
                                        googleAccount.getFamilyName(),
                                        googleAccount.getDisplayName(),
                                        googleAccount.getPhotoUrl());

                                firebaseCredentialSignin(GoogleAuthProvider.getCredential(token, null), button);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                    }
                });
    }

    private void saveUserProfile(Context context, String id, String firstName,
                                 String lastName, String name, Uri profilePicUri) {
        new OfflineUserProfile(context)
//                .reset()
                .setUserId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setUsername(name)
                .setProfilePicUri(profilePicUri);
    }

    @Override
    public void onLoginFragmentInteraction(int action, Authorize authorize, Pojos.User.Profile userProfile) {
        switch (action) {
            case LOGIN_WITH_PASSWORD_ACTION:
                firebaseUserNamePasswordSignin(authorize, userProfile);
//                firebaseAuthTokenSignin(SharedPrefs.getAuthToken(this));
                break;
            case LOGIN_WITH_OTP_ACTION:
                setFragment(TAG_OTP_FRAGMENT, true, new Object[] {authorize, LOGIN_WITH_OTP_ACTION});
                break;
            case FORGOT_PASSWORD_ACTION:
                setFragment(TAG_FORGOT_PASSWORD_FRAGMENT, true, new Object[] {authorize.getUsername()});
                break;
            case BACK_PRESSED_ACTION:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onOtpInteraction(int action, Authorize verificationDetails,
                                 Pojos.User.Profile userProfile, boolean isSignUp, String authToken) {
        switch (action) {
            case SIGNUP_OTP_VERIFICATION_ACTION:
                if (isSignUp) {
                    isFirebaseSignup = true;
                    firebaseSignup(verificationDetails);
                } else {
                    firebaseUserNamePasswordSignin(verificationDetails, userProfile);
                }
                break;
            case LOGIN_OTP_VERIFICATION_ACTION:
//                The user can't signin with username and password in the firebase because the password is not returned.
//                Using anonymous signin instead.
                firebaseAuthTokenSignin(authToken);
                Toast.makeText(this, "Login!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onEmailSignupInteraction(int action, final Authorize signUpDetails) {
        switch (action) {
            case EMAIL_SIGNUP_PROCEED_ACTION:
                if (signUpDetails != null) {
                    setFragment(TAG_SECOND_SIGNUP_FRAGMENT, true, new Object[]{signUpDetails});
                }
                break;
            case BACK_PRESSED_ACTION:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onFinalSignupInteraction(int action, final Authorize signUpDetails) {
        switch (action) {
            case SIGNUP_WITH_EMAIL_ACTION:
                setFragment(TAG_OTP_FRAGMENT, true, new Object[] {signUpDetails, SIGNUP_WITH_EMAIL_ACTION});
                break;
            default:
                break;
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

    private void firebaseSignup(final Authorize verificationDetails) {
        firebaseAuth.createUserWithEmailAndPassword(verificationDetails.getEmail(), verificationDetails.getPassword())
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            new OfflineUserProfile(MainActivity.this)
//                                    .reset()
                                    .setFirstName(verificationDetails.getFirstName())
                                    .setLastName(verificationDetails.getLastName())
                                    .setUsername(verificationDetails.getUsername())
                                    .setEmail(verificationDetails.getEmail())
                                    .setCountryCode(verificationDetails.getCountryCode())
                                    .setPhoneNumber(verificationDetails.getPhoneNumber())
                                    .setPassword(verificationDetails.getPassword());
                            isFirebaseSignup = false;
                        } else {
                            Snackbar.make(upBtn, "FirebaseSignup failed!", Snackbar.LENGTH_SHORT).show();
                        }
                        isFirebaseSignup = false;
                    }
                });
    }

    private void firebaseUserNamePasswordSignin(final Authorize verificationDetails, final Pojos.User.Profile userProfile) {
        firebaseAuth.signInWithEmailAndPassword(userProfile.getUserProfile().getEmail(), verificationDetails.getPassword())
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    userProfile.getUserProfile().setPassword(verificationDetails.getPassword());
                                    OfflineUserProfile.saveUserProfileOffline(userProfile, MainActivity.this);
                                }
                            }).start();
                            Snackbar.make(mainFragmentContainer, "Login!", Snackbar.LENGTH_SHORT).show();
                        } else {
                            AuthUtils.logout(MainActivity.this);
                            Snackbar.make(upBtn, "FirebaseUserNamePasswordSignin failed!", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseCredentialSignin(AuthCredential credential, final ProximaNovaSemiboldButton button) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(mainFragmentContainer, "Welcome!", Snackbar.LENGTH_SHORT).show();
                        } else {
                            AuthUtils.logout(MainActivity.this);
                            Snackbar.make(upBtn, "FirebaseCredentialSignin failed!", Snackbar.LENGTH_SHORT).show();
                        }
                        button.revertAnimation();
                    }
                });
    }

    private void firebaseAuthTokenSignin(String authToken) {
        firebaseAuth.signInWithCustomToken(authToken).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Note: password is always null here.
//                        OfflineUserProfile.saveUserProfileOffline(userProfile, MainActivity.this);
//                    }
//                }).start();
                if (task.isSuccessful()) {
                    Snackbar.make(mainFragmentContainer, "Login!", Snackbar.LENGTH_SHORT).show();
                } else {
                    AuthUtils.logout(MainActivity.this);
                    Snackbar.make(upBtn, "FirebaseAuthTokenSignin failed!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toggleUpBtnVisibility(int visibility) {
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

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null)
            mediaPlayer.pause();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
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

    @OnClick(R.id.up_btn)
    public void BackPressed() {
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