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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.utilities.NetworkStateReceiver;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import br.com.simplepass.loading_button_lib.interfaces.OnAnimationEndListener;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_PASSWORD_ACTION;
import static com.cncoding.teazer.MainActivity.SIGNUP_WITH_EMAIL_ACTION;
import static com.cncoding.teazer.MainActivity.SIGNUP_WITH_FACEBOOK_ACTION;
import static com.cncoding.teazer.MainActivity.SIGNUP_WITH_GOOGLE_ACTION;
import static com.cncoding.teazer.utilities.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.ViewUtils.enableView;

public class WelcomeFragment extends Fragment implements NetworkStateReceiver.NetworkStateListener {

    @BindView(R.id.login_page_btn) ProximaNovaSemiboldButton loginBtn;
    @BindView(R.id.signup_with_facebook) ProximaNovaSemiboldButton signupWithFbBtn;
    @BindView(R.id.fb_login_btn) LoginButton fbLoginButton;
    @BindView(R.id.signup_with_google) ProximaNovaSemiboldButton signupWithGoogleBtn;
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
    private Profile facebookProfile;
    private ProfileTracker profileTracker;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity = getActivity();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        fragmentActivity.registerReceiver(networkStateReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    private void showNotConnectedDialog(ProximaNovaSemiboldButton view) {
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

    @OnClick(R.id.login_page_btn) public void onLoginBtnClick() {
        if (isConnected)
            mListener.onWelcomeInteraction(LOGIN_WITH_PASSWORD_ACTION, null, null, null, null);
        else showNotConnectedDialog(loginBtn);
    }

    @OnClick(R.id.signup_page_btn) public void onSignupOptionClick() {
        if (isConnected)
            mListener.onWelcomeInteraction(SIGNUP_WITH_EMAIL_ACTION, null, null, null, null);
        else showNotConnectedDialog(signupWithEmailBtn);
    }

    @OnClick(R.id.signup_with_google) public void onGoogleSignupClick() {
        if (isConnected) {
            signupWithGoogleBtn.startAnimation();
            disableViews(signupWithGoogleBtn);
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, SIGNUP_WITH_GOOGLE_ACTION);
        }
        else showNotConnectedDialog(signupWithGoogleBtn);
    }

    @OnClick(R.id.signup_with_facebook) public void onFacebookSignupClick() {
        if (isConnected) {
            signupWithFbBtn.startAnimation();
            disableViews(signupWithFbBtn);
            fbSignup();
        } else showNotConnectedDialog(signupWithFbBtn);
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
            public void onSuccess(final LoginResult loginResult) {
//                accessToken = loginResult.getAccessToken();
                facebookProfile = Profile.getCurrentProfile();
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("registerFbCallback()", response.toString());
                        // Get facebook data from login
                        Bundle facebookData = getFacebookData(object);
                        handleFacebookAccessToken(facebookData);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
//                enableViews();
            }

            @Override
            public void onCancel() {
                signupWithFbBtn.revertAnimation(new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        signupWithFbBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook_white,
                                0, 0, 0);
                    }
                });
                enableViews();
            }

            @Override
            public void onError(FacebookException error) {
                signupWithFbBtn.revertAnimation(new OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        signupWithFbBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook_white,
                                0, 0, 0);
                    }
                });
                enableViews();
               // Log.v("registerFbCallback()", error.getCause().toString());
            }
        });
    }

    private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=400&height=400");
//                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));

            return bundle;
        }
        catch(Exception e) {
            Log.d("getFacebookData()","Error parsing JSON");
        }
        return null;
    }

    private void handleFacebookAccessToken(Bundle facebookData) {
        try {
            mListener.onWelcomeInteraction(SIGNUP_WITH_FACEBOOK_ACTION, facebookProfile,
                    facebookData, null, signupWithFbBtn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disableViews(View viewToExclude) {
        disableView(loginBtn, true);
        disableView(signupWithEmailBtn, true);
        disableView(signupWithFbBtn, true);
        disableView(signupWithGoogleBtn, true);
        enableView(viewToExclude);
    }

    private void enableViews() {
        enableView(loginBtn);
        enableView(signupWithEmailBtn);
        enableView(signupWithFbBtn);
        enableView(signupWithGoogleBtn);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
        /*
         * Google login action
         * */
            if (requestCode == SIGNUP_WITH_GOOGLE_ACTION) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    if (account != null)
                        mListener.onWelcomeInteraction(SIGNUP_WITH_GOOGLE_ACTION,
                                null, null, account, signupWithGoogleBtn);
                    else Log.d("GOOGLE_SIGN_IN: ", "account is null!!!!");
                } else {
                    signupWithGoogleBtn.revertAnimation(new OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd() {
                            signupWithGoogleBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_google_white,
                                    0, 0, 0);
                        }
                    });
                    enableViews();
                    Toast.makeText(context, "Google sign in failed!", Toast.LENGTH_SHORT).show();
                }
            }
            /*
             * Facebook login action
             * */
            else {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
//        enableViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mListener.onWelcomeInteraction(RESUME_WELCOME_VIDEO_ACTION, null, null, null, null);
//    }

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
        signupWithGoogleBtn.dispose();
        signupWithFbBtn.dispose();
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
        void onWelcomeInteraction(int action,
                                  Profile facebookProfile, Bundle facebookData, @Nullable GoogleSignInAccount googleAccount, ProximaNovaSemiboldButton button);
    }




}