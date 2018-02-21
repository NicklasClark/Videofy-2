package com.cncoding.teazer.authentication;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cncoding.teazer.MainActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.viewmodel.AuthViewModel;
import com.cncoding.teazer.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.model.auth.InitiateSignup;
import com.cncoding.teazer.model.auth.Login;
import com.cncoding.teazer.model.auth.ResetPasswordByOtp;
import com.cncoding.teazer.model.auth.ResetPasswordByPhoneNumber;
import com.cncoding.teazer.model.auth.SocialSignup;
import com.cncoding.teazer.model.auth.VerifyLoginWithOtp;
import com.cncoding.teazer.model.auth.VerifySignUp;
import com.cncoding.teazer.ui.fragment.activity.ForgotPasswordActivity;
import com.cncoding.teazer.utilities.NetworkStateReceiver;
import com.cncoding.teazer.utilities.NetworkStateReceiver.NetworkStateListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.STATUS_FALSE;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AuthFragment extends Fragment implements NetworkStateListener {

    /**
     * Field Validation types for any {@link Fragment} extending {@link AuthFragment}.
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CHECK_USERNAME, CHECK_PASSWORD, CHECK_EMAIL, CHECK_PHONE_NUMBER})
    @interface ValidationType {}

    public static final int CHECK_USERNAME = 10;
    public static final int CHECK_PASSWORD = 11;
    public static final int CHECK_EMAIL = 12;
    public static final int CHECK_PHONE_NUMBER = 13;

    protected boolean isConnected;
    protected Context context;
    private NetworkStateReceiver networkStateReceiver;
    protected  boolean isPasswordShown = true;
    AuthViewModel authViewModel;

    public AuthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getParentActivity();
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        context.registerReceiver(networkStateReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authViewModel.getApiResponse().observe(this, new Observer<ResultObject>() {
            @SuppressWarnings("ThrowableNotThrown")
            @Override
            public void onChanged(@Nullable ResultObject resultObject) {
                if (resultObject != null) {
                    if (resultObject.getError() != null) {
                        if (resultObject.getError().getMessage().contains(STATUS_FALSE)) {
                            handleResponse(resultObject);
                        }
                        else handleError(resultObject.getError());
                    }
                    else handleResponse(resultObject);
                }
                else handleError(new Throwable(context.getString(R.string.something_went_wrong)));
            }
        });
    }

    @NonNull
    public FragmentActivity getParentActivity() {
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            return getActivity();
        }
        else if (getActivity() != null && getActivity() instanceof ForgotPasswordActivity)
            return getActivity();
        else throw new IllegalStateException("Fragment is not attached to MainActivity");
    }

    @Override
    public void onNetworkAvailable() {
        isConnected = true;
    }

    @Override
    public void onNetworkUnavailable() {
        isConnected = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        networkStateReceiver.removeListener(this);
        context.unregisterReceiver(networkStateReceiver);
    }

    protected abstract void handleResponse(ResultObject resultObject);

    protected abstract void handleError(Throwable throwable);

    protected abstract void notifyNoInternetConnection();

    protected abstract boolean isFieldValidated(@ValidationType int whichType);

    protected abstract boolean isFieldFilled(@ValidationType int whichType);

    protected void signUp(InitiateSignup initiateSignup) {
        authViewModel.signUp(initiateSignup);
    }

    protected void verifySignUp(VerifySignUp verifySignUp) {
        authViewModel.verifySignUp(verifySignUp);
    }

    protected void socialSignUp(SocialSignup socialSignup) {
        authViewModel.socialSignUp(socialSignup);
    }

    protected void loginWithPassword(Login login) {
        authViewModel.loginWithPassword(login);
    }

    protected void loginWithOtp(InitiateLoginWithOtp initiateLoginWithOtp) {
        authViewModel.loginWithOtp(initiateLoginWithOtp);
    }

    protected void verifyLoginWithOtp(VerifyLoginWithOtp verifyLoginWithOtp) {
        authViewModel.verifyLoginWithOtp(verifyLoginWithOtp);
    }

    protected void checkUsernameAvailability(String username) {
        authViewModel.checkUsernameAvailability(username);
    }

    protected void checkEmailAvailability(String email) {
        authViewModel.checkEmailAvailability(email);
    }

    protected void checkPhoneNumberAvailability(int countryCode, long phoneNumber) {
        authViewModel.checkPhoneNumberAvailability(countryCode, phoneNumber);
    }

    protected void verifyForgotPasswordOtp(int otp) {
        authViewModel.verifyForgotPasswordOtp(otp);
    }

    protected void requestResetPasswordByEmail(String email) {
        authViewModel.requestResetPasswordByEmail(email);
    }

    protected void requestResetPasswordByPhone(ResetPasswordByPhoneNumber resetPasswordByPhoneNumber) {
        authViewModel.requestResetPasswordByPhone(resetPasswordByPhoneNumber);
    }

    protected void resetPasswordByOtp(ResetPasswordByOtp resetPasswordByOtp) {
        authViewModel.resetPasswordByOtp(resetPasswordByOtp);
    }
}