package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepository;
import com.cncoding.teazer.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.model.auth.InitiateSignup;
import com.cncoding.teazer.model.auth.Login;
import com.cncoding.teazer.model.auth.ResetPasswordByOtp;
import com.cncoding.teazer.model.auth.ResetPasswordByPhoneNumber;
import com.cncoding.teazer.model.auth.SocialSignup;
import com.cncoding.teazer.model.auth.VerifyLoginWithOtp;
import com.cncoding.teazer.model.auth.VerifySignUp;

import javax.inject.Inject;

/**
 * 
 * Created by Prem$ on 2/9/2018.
 */

public class AuthViewModel extends ViewModel {
    
    private MediatorLiveData<ResultObject> apiResponseLiveData;
    private AuthenticationRepository authenticationRepository;
    private Observer<ResultObject> resultObjectObserver;

    @Inject public AuthViewModel(MediatorLiveData<ResultObject> apiResponseLiveData, AuthenticationRepository authenticationRepository,
                         Observer<ResultObject> resultObjectObserver) {
        this.apiResponseLiveData = apiResponseLiveData;
        this.authenticationRepository = authenticationRepository;
        this.resultObjectObserver = resultObjectObserver;
    }

    public MediatorLiveData<ResultObject> getApiResponse() {
        return apiResponseLiveData;
    }

    private void clearLiveDataResponse() {
        if (apiResponseLiveData.getValue() != null) {
            apiResponseLiveData.getValue().clearData();
        }
    }

    public void signUp(InitiateSignup initiateSignup) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.signUp(initiateSignup), resultObjectObserver);
    }

    public void verifySignUp(VerifySignUp verifySignUp) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.verifySignUp(verifySignUp), resultObjectObserver);
    }

    public void socialSignUp(SocialSignup socialSignup) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.socialSignUp(socialSignup), resultObjectObserver);
    }

    public void loginWithPassword(Login login) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.loginWithPassword(login), resultObjectObserver);
    }

    public void loginWithOtp(InitiateLoginWithOtp initiateLoginWithOtp) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.loginWithOtp(initiateLoginWithOtp), resultObjectObserver);
    }

    public void verifyLoginWithOtp(VerifyLoginWithOtp verifyLoginWithOtp) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.verifyLoginWithOtp(verifyLoginWithOtp), resultObjectObserver);
    }

    public void checkUsernameAvailability(String username) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.checkUsernameAvailability(username), resultObjectObserver);
    }

    public void checkEmailAvailability(String email) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.checkEmailAvailability(email), resultObjectObserver);
    }

    public void checkPhoneNumberAvailability(int countryCode, long phoneNumber) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.checkPhoneNumberAvailability(countryCode, phoneNumber), resultObjectObserver);
    }

    public void verifyForgotPasswordOtp(int otp) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.verifyForgotPasswordOtp(otp), resultObjectObserver);
    }

    public void requestResetPasswordByEmail(String email) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.requestResetPasswordByEmail(email), resultObjectObserver);
    }

    public void requestResetPasswordByPhone(ResetPasswordByPhoneNumber resetPasswordByPhoneNumber) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.requestResetPasswordByPhone(resetPasswordByPhoneNumber), resultObjectObserver);
    }

    public void resetPasswordByOtp(ResetPasswordByOtp resetPasswordByOtp) {
        clearLiveDataResponse();
        apiResponseLiveData.addSource(authenticationRepository.resetPasswordByOtp(resetPasswordByOtp), resultObjectObserver);
    }
}