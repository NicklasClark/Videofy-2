package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.BrokerLiveData;
import com.cncoding.teazer.data.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.data.model.auth.InitiateSignup;
import com.cncoding.teazer.data.model.auth.Login;
import com.cncoding.teazer.data.model.auth.ResetPasswordByOtp;
import com.cncoding.teazer.data.model.auth.ResetPasswordByPhoneNumber;
import com.cncoding.teazer.data.model.auth.SocialSignup;
import com.cncoding.teazer.data.model.auth.VerifyLoginWithOtp;
import com.cncoding.teazer.data.model.auth.VerifySignUp;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepository;

import javax.inject.Inject;

/**
 * 
 * Created by Prem$ on 2/9/2018.
 */

public class AuthViewModel extends ViewModel {
    
    @Inject BrokerLiveData<ResultObject> resultObjectLiveData;
    @Inject AuthenticationRepository authenticationRepository;

    public AuthViewModel(TeazerApplication application) {
        application.getAppComponent().authComponentBuilder().build().inject(this);
    }

    public MediatorLiveData<ResultObject> getApiResponse() {
        return resultObjectLiveData;
    }

    public void signUpApiCall(InitiateSignup initiateSignup) {
        resultObjectLiveData.observeOn(authenticationRepository.signUp(initiateSignup));
    }

    public void verifySignUpApiCall(VerifySignUp verifySignUp) {
        resultObjectLiveData.observeOn(authenticationRepository.verifySignUp(verifySignUp));
    }

    public void socialSignUpApiCall(SocialSignup socialSignup) {
        resultObjectLiveData.observeOn(authenticationRepository.socialSignUp(socialSignup));
    }

    public void loginWithPasswordApiCall(Login login) {
        resultObjectLiveData.observeOn(authenticationRepository.loginWithPassword(login));
    }

    public void loginWithOtpApiCall(InitiateLoginWithOtp initiateLoginWithOtp) {
        resultObjectLiveData.observeOn(authenticationRepository.loginWithOtp(initiateLoginWithOtp));
    }

    public void verifyLoginWithOtpApiCall(VerifyLoginWithOtp verifyLoginWithOtp) {
        resultObjectLiveData.observeOn(authenticationRepository.verifyLoginWithOtp(verifyLoginWithOtp));
    }

    public void checkUsernameAvailabilityApiCall(String username) {
        resultObjectLiveData.observeOn(authenticationRepository.checkUsernameAvailability(username));
    }

    public void checkEmailAvailabilityApiCall(String email) {
        resultObjectLiveData.observeOn(authenticationRepository.checkEmailAvailability(email));
    }

    public void checkPhoneNumberAvailabilityApiCall(int countryCode, long phoneNumber) {
        resultObjectLiveData.observeOn(authenticationRepository.checkPhoneNumberAvailability(countryCode, phoneNumber));
    }

    public void verifyForgotPasswordOtpApiCall(int otp) {
        resultObjectLiveData.observeOn(authenticationRepository.verifyForgotPasswordOtp(otp));
    }

    public void requestResetPasswordByEmailApiCall(String email) {
        resultObjectLiveData.observeOn(authenticationRepository.requestResetPasswordByEmail(email));
    }

    public void requestResetPasswordByPhoneApiCall(ResetPasswordByPhoneNumber resetPasswordByPhoneNumber) {
        resultObjectLiveData.observeOn(authenticationRepository.requestResetPasswordByPhone(resetPasswordByPhoneNumber));
    }

    public void resetPasswordByOtpApiCall(ResetPasswordByOtp resetPasswordByOtp) {
        resultObjectLiveData.observeOn(authenticationRepository.resetPasswordByOtp(resetPasswordByOtp));
    }
}