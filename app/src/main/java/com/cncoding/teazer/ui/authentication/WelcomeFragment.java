package com.cncoding.teazer.ui.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.auth.SocialSignup;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.ui.authentication.base.BaseAuthFragment;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.utilities.asynctasks.GenerateBitmapFromUrl;
import com.cncoding.teazer.utilities.common.NetworkStateReceiver;
import com.cncoding.teazer.utilities.common.ViewUtils;
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
import static com.cncoding.teazer.data.model.auth.SocialSignup.SOCIAL_LOGIN_TYPE_FACEBOOK;
import static com.cncoding.teazer.data.model.auth.SocialSignup.SOCIAL_LOGIN_TYPE_GOOGLE;
import static com.cncoding.teazer.ui.authentication.base.MainActivity.LOGIN_WITH_PASSWORD_ACTION;
import static com.cncoding.teazer.ui.authentication.base.MainActivity.SIGNUP_WITH_EMAIL_ACTION;
import static com.cncoding.teazer.ui.authentication.base.MainActivity.SOCIAL_SIGNUP_ACTION;
import static com.cncoding.teazer.ui.authentication.base.MainActivity.getBitmapFromVectorDrawable;
import static com.cncoding.teazer.utilities.common.FabricAnalyticsUtil.logLoginEvent;
import static com.cncoding.teazer.utilities.common.FabricAnalyticsUtil.logSignUpEvent;
import static com.cncoding.teazer.utilities.common.SharedPrefs.saveAuthToken;
import static com.cncoding.teazer.utilities.common.SharedPrefs.saveUserId;
import static com.cncoding.teazer.utilities.common.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.enableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.showSnackBar;

public class WelcomeFragment extends BaseAuthFragment implements NetworkStateReceiver.NetworkStateListener {

    @BindView(R.id.login_page_btn) ProximaNovaSemiboldButton loginBtn;
    @BindView(R.id.fb_login_btn) LoginButton fbLoginButton;
    @BindView(R.id.signup_with_facebook) ProximaNovaSemiboldButton signupWithFbBtn;
    @BindView(R.id.signup_with_google) ProximaNovaSemiboldButton signupWithGoogleBtn;
    @BindView(R.id.signup_page_btn) ProximaNovaSemiboldButton signupWithEmailBtn;

    private boolean isConnected;

    private NetworkStateReceiver networkStateReceiver;
    private OnWelcomeInteractionListener mListener;
    private CallbackManager callbackManager;
    private GoogleApiClient googleApiClient;
    private Snackbar notConnectedSnackBar;
    private View rootView;
    private FragmentActivity fragmentActivity;
    private Profile facebookProfile;
    private ProfileTracker profileTracker;
    private SocialSignup socialSignup;
    private Bundle facebookData;
    private GoogleSignInAccount googleAccount;

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
        socialSignup = new SocialSignup();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        ButterKnife.bind(this, rootView);

        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.setReadPermissions("email");
        fbLoginButton.setFragment(this);
        registerFacebookCallback();

        setupGoogleSignIn();

        return rootView;
    }

    private void showNotConnectedDialog(ProximaNovaSemiboldButton view) {
        final Snackbar snackbar = Snackbar.make(view, R.string.not_connected_message, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        })
                .setActionTextColor(Color.rgb(105, 240, 174));
        snackbar.show();
    }

    @OnClick(R.id.login_page_btn) public void onLoginBtnClick() {
        if (isConnected)
            mListener.onWelcomeInteraction(LOGIN_WITH_PASSWORD_ACTION, null);
        else showNotConnectedDialog(loginBtn);
    }

    @OnClick(R.id.signup_page_btn) public void onSignupOptionClick() {
        if (isConnected)
            mListener.onWelcomeInteraction(SIGNUP_WITH_EMAIL_ACTION, null);
        else showNotConnectedDialog(signupWithEmailBtn);
    }

    @OnClick(R.id.signup_with_google) public void onGoogleSignupClick() {
        if (isConnected) {
            socialSignup.setSocialLoginType(SOCIAL_LOGIN_TYPE_GOOGLE);
            signupWithGoogleBtn.startAnimation();
            disableViews(signupWithGoogleBtn);
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent, SOCIAL_LOGIN_TYPE_GOOGLE);
        }
        else showNotConnectedDialog(signupWithGoogleBtn);
    }

    @OnClick(R.id.signup_with_facebook) public void onFacebookSignupClick() {
        if (isConnected) {
            socialSignup.setSocialLoginType(SOCIAL_LOGIN_TYPE_FACEBOOK);
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
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                facebookProfile = currentProfile;
            }
        };
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                facebookProfile = Profile.getCurrentProfile();
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("registerFbCallback()", response.toString());
                        // Get facebook data from login
                        facebookData = getFacebookData(object);
                        if (facebookData != null) {
                            handleFacebookLogin();
                        }
                        else Log.d("FACEBOOK_LOGIN: ", "account is null!!!!");
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                revertButtonAnimation();
            }

            @Override
            public void onError(FacebookException error) {
                revertButtonAnimation();
            }
        });
    }

    @Nullable private Bundle getFacebookData(JSONObject object) {
        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=400&height=400");
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

    private void handleFacebookLogin() {
        try {
            socialSignup.extractFromFacebookData(facebookProfile, facebookData, context);
            socialSignUp(socialSignup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGoogleSignIn() {
        try {
            socialSignup.extractFromGoogleData(googleAccount, context);
            socialSignUp(socialSignup);
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

    @Override protected void handleResponse(ResultObject resultObject) {
        try {
            switch (resultObject.getCode()) {
                case 201:
                    if (resultObject.getStatus()) {
                        saveAuthToken(getParentActivity().getApplicationContext(), resultObject.getAuthToken());
                        saveUserId(getParentActivity().getApplicationContext(), resultObject.getUserId());
                        verificationSuccessful(false);
                        //updating profile picture
                        GenerateBitmapFromUrl generateBitmapFromUrl = new GenerateBitmapFromUrl(context);
                        if (isFacebook()) {
                            if (facebookData.getString("profile_pic") != null)
                                generateBitmapFromUrl.execute(facebookData.getString("profile_pic"));
                        } else {
                            if (googleAccount.getPhotoUrl() != null)
                                generateBitmapFromUrl.execute(googleAccount.getPhotoUrl().toString());
                        }
                        logSignUpEvent(isFacebook() ? "Facebook" : "Google", true, facebookData.getString("email"));
                    }
                    break;
                case 200:
                    saveAuthToken(getParentActivity().getApplicationContext(), resultObject.getAuthToken());
                    saveUserId(getParentActivity().getApplicationContext(), resultObject.getUserId());
                    if (resultObject.getStatus()) {
                        verificationSuccessful(true);
                        if (isFacebook())
                            logLoginEvent("Facebook", true, facebookData.getString("email"));
                        else
                            logLoginEvent("Google", true, googleAccount.getEmail());
                    }
                    else new ShowUserNameAlreadyAvailableDialog();
                    break;
                default:
                    Log.d(isFacebook() ? "handleFacebookLogin()" : "handleGoogleSignIn()",
                            resultObject.getCode() + "_" + resultObject.getMessage());
                    revertButtonAnimation();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logSignUpEvent("Facebook", false, null);
        }
    }

    @Override
    protected void handleError(Throwable throwable) {
        throwable.printStackTrace();
        revertButtonAnimation();
        showSnackBar(loginBtn, getString(R.string.something_is_not_right));
        logLoginEvent(isFacebook() ? "Facebook" : "Google", false, null);
    }

    @Override
    protected void notifyNoInternetConnection() {
    }

    @Override
    protected boolean isFieldValidated(int whichType) {
        return false;
    }

    @Override
    protected boolean isFieldFilled(int whichType) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
//            Google login action
            if (requestCode == SOCIAL_LOGIN_TYPE_GOOGLE) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    googleAccount = result.getSignInAccount();
                    if (googleAccount != null)
                        handleGoogleSignIn();
                    else Log.d("GOOGLE_SIGN_IN: ", "account is null!!!!");
                } else {
                    revertButtonAnimation();
                    Toast.makeText(context, "Google sign in failed!", Toast.LENGTH_SHORT).show();
                }
            }
//            Facebook login action
            else {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWelcomeInteractionListener) {
            mListener = (OnWelcomeInteractionListener) context;
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onNetworkAvailable() {
        isConnected = true;
        if (notConnectedSnackBar != null && notConnectedSnackBar.isShown())
            notConnectedSnackBar.dismiss();
    }

    @Override
    public void onNetworkUnavailable() {
        isConnected = false;
        notConnectedSnackBar = Snackbar.make(rootView, R.string.not_connected_message, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                })
                .setActionTextColor(Color.rgb(105, 240, 174));
        notConnectedSnackBar.show();
    }

    private class ShowUserNameAlreadyAvailableDialog extends AlertDialog.Builder {

        ShowUserNameAlreadyAvailableDialog() {
            super(context);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            LayoutInflater inflater = getParentActivity().getLayoutInflater();
            @SuppressLint("InflateParams") final View dialogView = inflater.inflate(R.layout.dialog_alternate_email, null);
            dialogBuilder.setView(dialogView);

            final ProximaNovaRegularAutoCompleteTextView editText = dialogView.findViewById(R.id.edit_query);

            setupEditText(editText);

            dialogBuilder.setTitle(R.string.this_is_embarrassing);
            dialogBuilder.setMessage(getString(R.string.the_username) + socialSignup.getUserName()
                    + getString(R.string.username_already_exists));
            dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (!editText.getText().toString().equals("")) {
                        if (isFacebook())
                            handleFacebookLogin();
                        else
                            handleGoogleSignIn();
                    }
                }
            });
            dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    revertButtonAnimation();
                }
            });
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }

        private void setupEditText(final ProximaNovaRegularAutoCompleteTextView editText) {
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (editText.getText().toString().equals("")) {
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
                    } else {
                        if (editText.getCompoundDrawables()[2] != null) {
                            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
                        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
                    } else {
                        if (editText.getCompoundDrawables()[2] != null) {
                            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                }
            });

            editText.requestFocus();

            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    ViewUtils.hideKeyboard(getParentActivity(), textView);
                    return true;
                }
            });
        }
    }

    private void revertButtonAnimation() {
        if (isFacebook()) {
            signupWithFbBtn.revertAnimation(new OnAnimationEndListener() {
                @Override
                public void onAnimationEnd() {
                    signupWithFbBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook_white, 0, 0, 0);
                }
            });
        } else {
            signupWithGoogleBtn.revertAnimation(new OnAnimationEndListener() {
                @Override
                public void onAnimationEnd() {
                    signupWithGoogleBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_google_white, 0, 0, 0);
                }
            });
        }
        enableViews();
    }

    private boolean isFacebook() {
        return socialSignup.getSocialLoginType() == SOCIAL_LOGIN_TYPE_FACEBOOK;
    }

    private void verificationSuccessful(final boolean accountAlreadyExists) {
        if (isFacebook()) {
            signupWithFbBtn.doneLoadingAnimation(Color.parseColor("#4469AF"),
                    getBitmapFromVectorDrawable(context, R.drawable.ic_check_white));
        } else {
            signupWithGoogleBtn.doneLoadingAnimation(Color.parseColor("#DC4E41"),
                    getBitmapFromVectorDrawable(context, R.drawable.ic_check_white));
        }
        mListener.onWelcomeInteraction(SOCIAL_SIGNUP_ACTION, accountAlreadyExists);
    }

    public interface OnWelcomeInteractionListener {
        void onWelcomeInteraction(int action, Boolean accountAlreadyExists);
    }
}