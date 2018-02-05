package com.cncoding.teazer.data.repository.remote.authentication;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.model.base.Authorize;

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class AuthenticationRepo implements AuthenticationRepository {
    @Override
    public LiveData<ResultObject> signUp(Authorize signUpBody) {
        return null;
    }

    @Override
    public LiveData<ResultObject> verifySignUp(Authorize verifySignUp) {
        return null;
    }

    @Override
    public LiveData<ResultObject> socialSignUp(Authorize socialSignUpDetails) {
        return null;
    }

    @Override
    public LiveData<ResultObject> loginWithPassword(Authorize loginWithPassword) {
        return null;
    }

    @Override
    public LiveData<ResultObject> loginWithOtp(Authorize phoneNumberDetails) {
        return null;
    }

    @Override
    public LiveData<ResultObject> verifyLoginWithOtp(Authorize verifyLoginWithOtp) {
        return null;
    }

    @Override
    public LiveData<ResultObject> checkUsernameAvailability(String username) {
        return null;
    }

    @Override
    public LiveData<ResultObject> checkEmailAvailability(String email) {
        return null;
    }

    @Override
    public LiveData<ResultObject> checkPhoneNumberAvailability(int countryCode, long phoneNumber) {
        return null;
    }

    @Override
    public LiveData<ResultObject> verifyForgotPasswordOtp(int otp) {
        return null;
    }

    @Override
    public LiveData<ResultObject> requestResetPasswordByEmail(String email) {
        return null;
    }

    @Override
    public LiveData<ResultObject> requestResetPasswordByPhone(Authorize phoneNumberDetails) {
        return null;
    }

    @Override
    public LiveData<ResultObject> resetPasswordByOtp(Authorize resetPasswordDetails) {
        return null;
    }
}
