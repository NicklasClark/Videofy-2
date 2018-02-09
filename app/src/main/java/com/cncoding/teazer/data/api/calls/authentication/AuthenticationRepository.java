package com.cncoding.teazer.data.api.calls.authentication;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.model.base.Authorize;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface AuthenticationRepository {

    LiveData<ResultObject> signUp(Authorize signUpBody);

    LiveData<ResultObject> verifySignUp(Authorize verifySignUp);

    LiveData<ResultObject> socialSignUp(Authorize socialSignUpDetails);

    LiveData<ResultObject> loginWithPassword(Authorize loginWithPassword);

    LiveData<ResultObject> loginWithOtp(Authorize phoneNumberDetails);

    LiveData<ResultObject> verifyLoginWithOtp(Authorize verifyLoginWithOtp);

    LiveData<ResultObject> checkUsernameAvailability(String username);

    LiveData<ResultObject> checkEmailAvailability(String email);

    LiveData<ResultObject> checkPhoneNumberAvailability(int countryCode, long phoneNumber);

    LiveData<ResultObject> verifyForgotPasswordOtp(int otp);

    LiveData<ResultObject> requestResetPasswordByEmail(String email);

    LiveData<ResultObject> requestResetPasswordByPhone(Authorize phoneNumberDetails);

    LiveData<ResultObject> resetPasswordByOtp(Authorize resetPasswordDetails);
}
