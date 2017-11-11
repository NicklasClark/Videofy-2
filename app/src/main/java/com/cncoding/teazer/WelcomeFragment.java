package com.cncoding.teazer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.utilities.NetworkStateReceiver;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_PASSWORD_ACTION;
import static com.cncoding.teazer.MainActivity.OPEN_CAMERA_ACTION;
import static com.cncoding.teazer.MainActivity.RESUME_WELCOME_VIDEO_ACTION;
import static com.cncoding.teazer.MainActivity.SIGNUP_WITH_EMAIL_ACTION;
import static com.cncoding.teazer.MainActivity.SIGNUP_WITH_FACEBOOK_ACTION;
import static com.cncoding.teazer.MainActivity.SIGNUP_WITH_GOOGLE_ACTION;

public class WelcomeFragment extends Fragment implements NetworkStateReceiver.NetworkStateListener {

    //    @BindView(R.id.welcome_video) VideoView welcomeVideo;
    @BindView(R.id.login_page_btn) ProximaNovaSemiboldButton loginBtn;
    @BindView(R.id.signup_with_facebook) ProximaNovaSemiboldButton signupWithFbBtn;
    @BindView(R.id.fb_login_btn) LoginButton fbLoginButton;
    @BindView(R.id.signup_with_google) ProximaNovaSemiboldButton signupWithGoogleBtn;
//    @BindView(R.id.google_signin_btn) SignInButton googleSignInBtn;
    @BindView(R.id.signup_page_btn) ProximaNovaSemiboldButton signupWithEmailBtn;
    @BindView(R.id.marquee_text) ProximaNovaBoldTextView marqueeText;

    private boolean isConnected;

    private NetworkStateReceiver networkStateReceiver;
    private OnWelcomeInteractionListener mListener;
    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;
    private Snackbar notConnectedSnackbar;
    private View rootView;
    private FragmentActivity fragmentActivity;
    private Context context;
//    private AccessToken accessToken;
//    private AccessTokenTracker accessTokenTracker;
    private Profile facebookProfile;
    private ProfileTracker profileTracker;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        fragmentActivity = getActivity();
        context = getContext();
        fragmentActivity.registerReceiver(networkStateReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        ButterKnife.bind(this, rootView);

        marqueeText.setSelected(true);

        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.setReadPermissions("email");
        fbLoginButton.setFragment(this);
        registerFacebookCallback();

        setupGoogleSignIn();

        return rootView;
    }

//    private boolean isConnected() {
//        ConnectivityManager conman = (ConnectivityManager) fragmentActivity.getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = conman.getActiveNetworkInfo();
//        return networkInfo != null && networkInfo.isConnected();
//    }

    private void showNotConnectedDialog(AppCompatButton view) {
        final Snackbar snackbar = Snackbar.make(view, R.string.not_connected_message, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
//                        view.performClick();
                    }
                })
                .setActionTextColor(Color.rgb(105, 240, 174));
        snackbar.show();
    }

    @OnClick(R.id.teazer_header) public void openCamera() {
        mListener.onWelcomeInteraction(OPEN_CAMERA_ACTION, null, null, null);
    }

    @OnClick(R.id.login_page_btn) public void onLoginBtnClick() {
        if (isConnected)
            mListener.onWelcomeInteraction(LOGIN_WITH_PASSWORD_ACTION, null, null, null);
        else showNotConnectedDialog(loginBtn);
    }

    @OnClick(R.id.signup_page_btn) public void onSignupOptionClick() {
        if (isConnected)
            mListener.onWelcomeInteraction(SIGNUP_WITH_EMAIL_ACTION, null, null, null);
        else showNotConnectedDialog(signupWithEmailBtn);
    }

    @OnClick(R.id.signup_with_google) public void onGoogleSignupClick() {
        if (isConnected) {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, SIGNUP_WITH_GOOGLE_ACTION);
        }
        else showNotConnectedDialog(signupWithGoogleBtn);
    }

    @OnClick(R.id.signup_with_facebook) public void onFacebookSignupClick() {
        if (isConnected)
            fbLoginButton.performClick();
        else showNotConnectedDialog(signupWithFbBtn);
    }

    @OnClick(R.id.fb_login_btn) public void fbSignup() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    private void setupGoogleSignIn() {
        if (googleApiClient == null) {
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.google_web_client_id))
                    .requestEmail()
                    .requestProfile()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(context)
                    .enableAutoManage(fragmentActivity, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Toast.makeText(context, "Connection Failed!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                    .build();
            googleApiClient.connect();
        }
    }

    private void registerFacebookCallback() {
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                accessToken = currentAccessToken;
//            }
//        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                facebookProfile = currentProfile;
            }
        };
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                accessToken = loginResult.getAccessToken();
                facebookProfile = Profile.getCurrentProfile();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        mListener.onWelcomeInteraction(SIGNUP_WITH_FACEBOOK_ACTION, accessToken, facebookProfile, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
         * Google login action
         * */
        if (requestCode == SIGNUP_WITH_GOOGLE_ACTION) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null)
                    mListener.onWelcomeInteraction(SIGNUP_WITH_GOOGLE_ACTION, account.getIdToken(), null, account);
                else Log.d("GOOGLE_SIGN_IN: ", "account is null!!!!");
            } else
                Toast.makeText(context, "Google sign in failed!", Toast.LENGTH_SHORT).show();
        }
        /*
         * Facebook login action
         * */
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mListener.onWelcomeInteraction(RESUME_WELCOME_VIDEO_ACTION, null, null, null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWelcomeInteractionListener) {
            mListener = (OnWelcomeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWelcomeInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(fragmentActivity);
        googleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        fragmentActivity.unregisterReceiver(networkStateReceiver);
        googleApiClient.stopAutoManage(fragmentActivity);
        googleApiClient.disconnect();
        profileTracker.stopTracking();
//        accessTokenTracker.stopTracking();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onNetworkAvailable() {
        isConnected = true;
        if (notConnectedSnackbar != null && notConnectedSnackbar.isShown())
            notConnectedSnackbar.dismiss();
    }

    @Override
    public void onNetworkUnavailable() {
        isConnected = false;
        notConnectedSnackbar = Snackbar.make(rootView, R.string.not_connected_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                })
                .setActionTextColor(Color.rgb(105, 240, 174));
        notConnectedSnackbar.show();
    }

    public interface OnWelcomeInteractionListener {
        void onWelcomeInteraction(int action, @Nullable Object token,
                                  Profile facebookProfile, @Nullable GoogleSignInAccount googleAccount);
    }
}