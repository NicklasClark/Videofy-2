package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

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
import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl;

import javax.inject.Inject;

/**
 * 
 * Created by Prem$ on 2/9/2018.
 */

public class AuthViewModel extends ViewModel {
    
    private MediatorLiveData<ResultObject> resultObjectLiveData;
    private AuthenticationRepository authenticationRepository;
    private Observer<ResultObject> resultObjectObserver;

    @Inject public AuthViewModel(MediatorLiveData<ResultObject> resultObjectLiveData, AuthenticationRepository authenticationRepository,
                                 Observer<ResultObject> resultObjectObserver) {
        this.resultObjectLiveData = resultObjectLiveData;
        this.authenticationRepository = authenticationRepository;
        this.resultObjectObserver = resultObjectObserver;
    }

    public AuthViewModel() {
        resultObjectLiveData = new MediatorLiveData<>();
        authenticationRepository = new AuthenticationRepositoryImpl();
        resultObjectObserver = new Observer<ResultObject>() {
            @Override
            public void onChanged(@Nullable ResultObject resultObject) {
                resultObjectLiveData.setValue(resultObject);
            }
        };
    }

    public MediatorLiveData<ResultObject> getApiResponse() {
        return resultObjectLiveData;
    }

    private void clearLiveDataResponse() {
        if (resultObjectLiveData.getValue() != null) {
            resultObjectLiveData.getValue().clearData();
        }
    }

    public void signUp(InitiateSignup initiateSignup) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.signUp(initiateSignup), resultObjectObserver);
    }

    public void verifySignUp(VerifySignUp verifySignUp) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.verifySignUp(verifySignUp), resultObjectObserver);
    }

    public void socialSignUp(SocialSignup socialSignup) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.socialSignUp(socialSignup), resultObjectObserver);
    }

    public void loginWithPassword(Login login) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.loginWithPassword(login), resultObjectObserver);
    }

    public void loginWithOtp(InitiateLoginWithOtp initiateLoginWithOtp) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.loginWithOtp(initiateLoginWithOtp), resultObjectObserver);
    }

    public void verifyLoginWithOtp(VerifyLoginWithOtp verifyLoginWithOtp) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.verifyLoginWithOtp(verifyLoginWithOtp), resultObjectObserver);
    }

    public void checkUsernameAvailability(String username) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.checkUsernameAvailability(username), resultObjectObserver);
    }

    public void checkEmailAvailability(String email) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.checkEmailAvailability(email), resultObjectObserver);
    }

    public void checkPhoneNumberAvailability(int countryCode, long phoneNumber) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.checkPhoneNumberAvailability(countryCode, phoneNumber), resultObjectObserver);
    }

    public void verifyForgotPasswordOtp(int otp) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.verifyForgotPasswordOtp(otp), resultObjectObserver);
    }

    public void requestResetPasswordByEmail(String email) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.requestResetPasswordByEmail(email), resultObjectObserver);
    }

    public void requestResetPasswordByPhone(ResetPasswordByPhoneNumber resetPasswordByPhoneNumber) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.requestResetPasswordByPhone(resetPasswordByPhoneNumber), resultObjectObserver);
    }

    public void resetPasswordByOtp(ResetPasswordByOtp resetPasswordByOtp) {
        clearLiveDataResponse();
        resultObjectLiveData.addSource(authenticationRepository.resetPasswordByOtp(resetPasswordByOtp), resultObjectObserver);
    }
}