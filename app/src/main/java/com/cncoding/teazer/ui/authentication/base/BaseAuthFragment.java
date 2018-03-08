package com.cncoding.teazer.ui.authentication.base;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.data.model.auth.InitiateSignup;
import com.cncoding.teazer.data.model.auth.Login;
import com.cncoding.teazer.data.model.auth.ResetPasswordByOtp;
import com.cncoding.teazer.data.model.auth.ResetPasswordByPhoneNumber;
import com.cncoding.teazer.data.model.auth.SocialSignup;
import com.cncoding.teazer.data.model.auth.VerifyLoginWithOtp;
import com.cncoding.teazer.data.model.auth.VerifySignUp;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.viewmodel.AuthViewModel;
import com.cncoding.teazer.ui.home.profile.activity.ForgotPasswordActivity;
import com.cncoding.teazer.utilities.common.Annotations.ValidationType;

import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.STATUS_FALSE;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseAuthFragment extends Fragment {

    protected Context context;
    protected  boolean isPasswordShown = true;
    AuthViewModel authViewModel;

    public BaseAuthFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getParentActivity();
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
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
                        else handleError(resultObject);
                    }
                    else handleResponse(resultObject);
                }
                else handleError(new ResultObject(new Throwable(context.getString(R.string.something_went_wrong))));
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

    protected abstract void handleResponse(ResultObject resultObject);

    protected abstract void handleError(ResultObject resultObject);

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