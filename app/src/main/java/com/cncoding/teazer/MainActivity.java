package com.cncoding.teazer;

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
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.apiCalls.UserAuth;
import com.cncoding.teazer.authentication.BlurBuilder;
import com.cncoding.teazer.authentication.ConfirmOtpFragment;
import com.cncoding.teazer.authentication.ForgotPasswordFragment;
import com.cncoding.teazer.authentication.ForgotPasswordResetFragment;
import com.cncoding.teazer.authentication.LoginFragment;
import com.cncoding.teazer.authentication.SignupFragment;
import com.cncoding.teazer.authentication.UserProfile;
import com.cncoding.teazer.authentication.WelcomeFragment;
import com.cncoding.teazer.camera.CameraActivity;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity
        implements LoginFragment.LoginInteractionListener,
        SignupFragment.OnEmailSignupInteractionListener,
        WelcomeFragment.OnWelcomeInteractionListener,
        ForgotPasswordFragment.OnForgotPasswordInteractionListener,
        ConfirmOtpFragment.OnOtpInteractionListener,
        ForgotPasswordResetFragment.OnResetForgotPasswordInteractionListener,
        TextureView.SurfaceTextureListener {

//    public static final String USER_PROFILE = "profile";
//    public static final String CURRENT_LOGIN_ACTION = "currentLoginAction";
    public static final int DEVICE_TYPE_ANDROID = 2;
    public static final int OPEN_CAMERA_ACTION = 98;
    private static final int SOCIAL_LOGIN_TYPE_FACEBOOK = 1;
    private static final int SOCIAL_LOGIN_TYPE_GOOGLE = 2;
    private static final String TAG_WELCOME_FRAGMENT = "welcomeFragment";
    public static final String TAG_LOGIN_FRAGMENT = "loginFragment";
    public static final String TAG_FORGOT_PASSWORD_FRAGMENT = "forgotPasswordFragment";
    public static final String TAG_SIGNUP_FRAGMENT = "signupFragment";
    private static final String TAG_OTP_FRAGMENT = "otpFragment";
    public static final int LOGIN_THROUGH_PASSWORD_ACTION = 10;
    public static final int LOGIN_THROUGH_OTP_ACTION = 11;
    public static final int SIGNUP_WITH_FACEBOOK_ACTION = 20;
    public static final int SIGNUP_WITH_GOOGLE_ACTION = 21;
    public static final int SIGNUP_WITH_EMAIL_ACTION = 22;
    public static final int SIGNUP_OTP_VERIFICATION_ACTION = 41;
    public static final int LOGIN_OTP_VERIFICATION_ACTION = 42;
    public static final int FORGOT_PASSWORD_ACTION = 5;
    public static final int BACK_PRESSED_ACTION = 6;
    public static final int RESUME_WELCOME_VIDEO_ACTION = 8;
    public static final String BASE_URL = "http://restdev.ap-south-1.elasticbeanstalk.com/";

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

        welcomeVideo.setSurfaceTextureListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        setFirebaseAuthStateListener();
    }

    private void setFirebaseAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    new UserProfile(MainActivity.this).setEmail(user.getEmail());
//                    user is signed in
                    startActivity(new Intent(MainActivity.this, HomeScreenActivity.class));
                    finish();
                } else {
//                    user is signed out
                    if (fragmentManager.getBackStackEntryCount() == 0)
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
        switch (tag) {
            case TAG_WELCOME_FRAGMENT:
                if (addToBackStack)
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.float_up, R.anim.slide_out_left,
                                    R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.main_fragment_container, new WelcomeFragment(), TAG_WELCOME_FRAGMENT)
                            .addToBackStack(TAG_WELCOME_FRAGMENT)
                            .commit();
                else fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.float_up, R.anim.slide_out_left,
                                R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_fragment_container, new WelcomeFragment(), TAG_WELCOME_FRAGMENT)
                        .commit();
                break;
            case TAG_LOGIN_FRAGMENT:
                toggleUpBtnVisibility(View.VISIBLE);
                if (addToBackStack)
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                    R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.main_fragment_container, new LoginFragment(), TAG_LOGIN_FRAGMENT)
                            .addToBackStack(TAG_LOGIN_FRAGMENT)
                            .commit();
                else fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_fragment_container, new LoginFragment(), TAG_LOGIN_FRAGMENT)
                        .commit();
                break;
            case TAG_FORGOT_PASSWORD_FRAGMENT:
                if (addToBackStack)
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                    R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.main_fragment_container, ForgotPasswordFragment.newInstance((String) args[0]),
                                    TAG_FORGOT_PASSWORD_FRAGMENT)
                            .addToBackStack(TAG_FORGOT_PASSWORD_FRAGMENT)
                            .commit();
                else fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_fragment_container, ForgotPasswordFragment.newInstance((String) args[0]),
                                TAG_FORGOT_PASSWORD_FRAGMENT)
                        .commit();
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                break;
            case TAG_SIGNUP_FRAGMENT:
                toggleUpBtnVisibility(View.VISIBLE);
                if (addToBackStack)
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                    R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.main_fragment_container, new SignupFragment(), TAG_SIGNUP_FRAGMENT)
                            .addToBackStack(TAG_SIGNUP_FRAGMENT)
                            .commit();
                else fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_fragment_container, new SignupFragment(), TAG_SIGNUP_FRAGMENT)
                        .commit();
                break;
            case TAG_OTP_FRAGMENT:
                toggleUpBtnVisibility(View.VISIBLE);
                if (addToBackStack)
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                    R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.main_fragment_container, ConfirmOtpFragment.newInstance(args), TAG_SIGNUP_FRAGMENT)
                            .addToBackStack(TAG_SIGNUP_FRAGMENT)
                            .commit();
                else fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                R.anim.slide_in_left, R.anim.slide_out_right)
                        .replace(R.id.main_fragment_container, ConfirmOtpFragment.newInstance(args), TAG_SIGNUP_FRAGMENT)
                        .commit();
                break;
            default:
                break;
        }
    }

    private void startFragmentTransition(boolean reverse, final String tag, final boolean addToBackStack) {
        if (!reverse) {
            new AsyncTask<Void, Void, Void>() {

                private Bitmap bitmap;

                @Override
                protected void onPreExecute() {
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.pause();
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
                    setFragment(tag, addToBackStack, null);
                }
            }.execute();
        }
        else {
            if (transitionDrawable != null)
                transitionDrawable.reverseTransition(400);
//            mainFragmentContainer.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Surface surface = new Surface(surfaceTexture);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setScreenOnWhilePlaying(true);
        try {
            mediaPlayer.setDataSource(getApplicationContext(),
                    Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.welcome_video));
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
    public void onWelcomeInteraction(int action, @Nullable Object token,
                                     Profile facebookProfile, @Nullable GoogleSignInAccount googleAccount) {
        switch (action) {
            case LOGIN_THROUGH_PASSWORD_ACTION:
                startFragmentTransition(false, TAG_LOGIN_FRAGMENT, true);
                break;
            case SIGNUP_WITH_FACEBOOK_ACTION:
                if (token != null && facebookProfile != null) {
                    handleFacebookLogin((AccessToken) token, facebookProfile);
                }
                break;
            case SIGNUP_WITH_GOOGLE_ACTION:
                if (token != null && googleAccount != null)
                    handleGoogleSignIn((String) token,  googleAccount);
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
            case OPEN_CAMERA_ACTION:
                startActivity(new Intent(this, CameraActivity.class));
//                finish();
            default:
                break;
        }
    }

    private void handleFacebookLogin(final AccessToken token, final Profile facebookProfile) {
        ApiCallingService.socialSignUp(new UserAuth.SignUp.Social(
                token.getToken(),
                getDeviceId(),
                DEVICE_TYPE_ANDROID,
                facebookProfile.getId(),            //social ID
                SOCIAL_LOGIN_TYPE_FACEBOOK,
                null,                               //email
                facebookProfile.getName(),          //Username
                facebookProfile.getFirstName(),
                facebookProfile.getLastName()))

                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        if (response.code() == 200 || response.code() == 201) {
                            if (response.body().getStatus()) {
                                saveUserProfile(MainActivity.this,
                                        facebookProfile.getId(),
                                        facebookProfile.getFirstName(),
                                        facebookProfile.getLastName(),
                                        facebookProfile.getName(),
                                        facebookProfile.getProfilePictureUri(100, 100));
                                firebaseAuth.signInWithCredential(FacebookAuthProvider.getCredential(token.getToken()))
                                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Snackbar.make(mainFragmentContainer, "Welcome!", Snackbar.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                    }
                });
    }

    private void handleGoogleSignIn(final String token, final GoogleSignInAccount googleAccount) {
        ApiCallingService.socialSignUp(new UserAuth.SignUp.Social(
                token,
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
                        if (response.code() == 200 || response.code() == 201) {
                            if (response.body().getStatus()) {
                                saveUserProfile(MainActivity.this,
                                        googleAccount.getId(),
                                        googleAccount.getGivenName(),
                                        googleAccount.getFamilyName(),
                                        googleAccount.getDisplayName(),
                                        googleAccount.getPhotoUrl());

                                firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(token, null))
                                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MainActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
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
        new UserProfile(context)
                .reset()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setUsername(name)
                .setProfilePicUri(profilePicUri);
    }

    @Override
    public void onLoginFragmentInteraction(int action, UserAuth.SignUp userDetails) {
        switch (action) {
            case LOGIN_THROUGH_PASSWORD_ACTION:
                if (userDetails.getUsername() != null && userDetails.getPassword() != null) {
                    firebaseAuth.signInWithEmailAndPassword(userDetails.getUsername(), userDetails.getPassword())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        //noinspection ThrowableResultOfMethodCallIgnored,ConstantConditions
                                        String message = task.getException().getMessage();
                                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                Toast.makeText(this, "Login!", Toast.LENGTH_SHORT).show();
                break;
            case LOGIN_THROUGH_OTP_ACTION:
                setFragment(TAG_OTP_FRAGMENT, true, new Object[] {userDetails, LOGIN_THROUGH_OTP_ACTION});
                break;
            case FORGOT_PASSWORD_ACTION:
                setFragment(TAG_FORGOT_PASSWORD_FRAGMENT, true, new Object[] {userDetails.getUsername()});
                break;
            case BACK_PRESSED_ACTION:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onEmailSignupInteraction(int action, final UserAuth.SignUp signUpDetails) {
        switch (action) {
            case SIGNUP_WITH_EMAIL_ACTION:
                if (signUpDetails != null) {
                    ApiCallingService.performSignUp(signUpDetails).enqueue(new Callback<ResultObject>() {
                        @Override
                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                            if (response.code() == 200) {
//                                setFragment(TAG_OTP_FRAGMENT, true, null);
                                if (response.body().getStatus()) {
                                    setFragment(TAG_OTP_FRAGMENT, true, new Object[] {signUpDetails, SIGNUP_WITH_EMAIL_ACTION});
                                } else {
                                    Toast.makeText(MainActivity.this, "Username, email or phone number already exists.\n" +
                                            "Or you may have reached maximum OTP retry attempts", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultObject> call, Throwable t) {
                        }
                    });
                }
                break;
            case BACK_PRESSED_ACTION:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onOtpInteraction(int action, final UserAuth.SignUp.Verify verificationDetails, boolean isSignUp) {
        switch (action) {
            case SIGNUP_OTP_VERIFICATION_ACTION:
                if (isSignUp) {
                    firebaseAuth.createUserWithEmailAndPassword(verificationDetails.getEmail(), verificationDetails.getPassword())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        new UserProfile(MainActivity.this)
                                                .reset()
                                                .setFirstName(verificationDetails.getFirstName())
                                                .setLastName(verificationDetails.getLastName())
                                                .setUsername(verificationDetails.getUsername())
                                                .setEmail(verificationDetails.getEmail())
                                                .setCountryCode(verificationDetails.getCountryCode())
                                                .setPhoneNumber(verificationDetails.getPhoneNumber())
                                                .setPassword(verificationDetails.getPassword());
                                    } else {
                                        Toast.makeText(MainActivity.this, "Signup failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    firebaseAuth.signInWithEmailAndPassword(verificationDetails.getEmail(), verificationDetails.getPassword())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        new UserProfile(MainActivity.this)
                                                .reset()
                                                .setFirstName(verificationDetails.getFirstName())
                                                .setLastName(verificationDetails.getLastName())
                                                .setUsername(verificationDetails.getUsername())
                                                .setEmail(verificationDetails.getEmail())
                                                .setCountryCode(verificationDetails.getCountryCode())
                                                .setPhoneNumber(verificationDetails.getPhoneNumber())
                                                .setPassword(verificationDetails.getPassword());
                                    } else {
                                        Toast.makeText(MainActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            case LOGIN_OTP_VERIFICATION_ACTION:
                break;
            default:
                break;
        }
    }

    @Override
    public void onForgotPasswordInteraction(int action) {
        switch (action) {
            case BACK_PRESSED_ACTION:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onResetForgotPasswordInteraction(int action) {
    }

    public static String getDeviceId() {
        return Settings.Secure.ANDROID_ID;
    }

    public static String getFcmToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    private void toggleUpBtnVisibility(int visibility) {
        switch (visibility) {
            case View.VISIBLE:
                if (upBtn.getVisibility() != View.VISIBLE) {
                    upBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
                    upBtn.setVisibility(View.VISIBLE);
                }
                break;
            case View.INVISIBLE:
                if (upBtn.getVisibility() != View.INVISIBLE) {
                    upBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_out));
                    upBtn.setVisibility(View.INVISIBLE);
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