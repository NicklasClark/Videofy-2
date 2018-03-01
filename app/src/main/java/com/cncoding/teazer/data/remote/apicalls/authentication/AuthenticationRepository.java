package com.cncoding.teazer.data.remote.apicalls.authentication;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.data.model.auth.InitiateSignup;
import com.cncoding.teazer.data.model.auth.Login;
import com.cncoding.teazer.data.model.auth.ResetPasswordByOtp;
import com.cncoding.teazer.data.model.auth.ResetPasswordByPhoneNumber;
import com.cncoding.teazer.data.model.auth.SocialSignup;
import com.cncoding.teazer.data.model.auth.VerifyLoginWithOtp;
import com.cncoding.teazer.data.model.auth.VerifySignUp;
import com.cncoding.teazer.data.remote.ResultObject;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface AuthenticationRepository {

    LiveData<ResultObject> signUp(InitiateSignup initiateSignup);

    LiveData<ResultObject> verifySignUp(VerifySignUp verifySignUp);

    LiveData<ResultObject> socialSignUp(SocialSignup socialSignup);

    LiveData<ResultObject> loginWithPassword(Login login);

    LiveData<ResultObject> loginWithOtp(InitiateLoginWithOtp initiateLoginWithOtp);

    LiveData<ResultObject> verifyLoginWithOtp(VerifyLoginWithOtp verifyLoginWithOtp);

    LiveData<ResultObject> checkUsernameAvailability(String username);

    LiveData<ResultObject> checkEmailAvailability(String email);

    LiveData<ResultObject> checkPhoneNumberAvailability(int countryCode, long phoneNumber);

    LiveData<ResultObject> verifyForgotPasswordOtp(int otp);

    LiveData<ResultObject> requestResetPasswordByEmail(String email);

    LiveData<ResultObject> requestResetPasswordByPhone(ResetPasswordByPhoneNumber resetPasswordByPhoneNumber);

    LiveData<ResultObject> resetPasswordByOtp(ResetPasswordByOtp resetPasswordByOtp);
}
