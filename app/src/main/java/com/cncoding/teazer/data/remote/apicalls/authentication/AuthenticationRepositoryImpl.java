package com.cncoding.teazer.data.remote.apicalls.authentication;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.cncoding.teazer.data.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.data.model.auth.InitiateSignup;
import com.cncoding.teazer.data.model.auth.Login;
import com.cncoding.teazer.data.model.auth.ResetPasswordByOtp;
import com.cncoding.teazer.data.model.auth.ResetPasswordByPhoneNumber;
import com.cncoding.teazer.data.model.auth.SocialSignup;
import com.cncoding.teazer.data.model.auth.VerifyLoginWithOtp;
import com.cncoding.teazer.data.model.auth.VerifySignUp;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.utilities.common.Annotations.AuthCallType;

import org.jetbrains.annotations.Contract;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getRetrofitWithoutAuthToken;
import static com.cncoding.teazer.utilities.common.Annotations.CHECK_EMAIL_AVAILABILITY;
import static com.cncoding.teazer.utilities.common.Annotations.CHECK_PHONE_NUMBER_AVAILABILITY;
import static com.cncoding.teazer.utilities.common.Annotations.CHECK_USERNAME_AVAILABILITY;
import static com.cncoding.teazer.utilities.common.Annotations.LOGIN_WITH_OTP;
import static com.cncoding.teazer.utilities.common.Annotations.LOGIN_WITH_PASSWORD;
import static com.cncoding.teazer.utilities.common.Annotations.REQUEST_RESET_PASSWORD_BY_EMAIL;
import static com.cncoding.teazer.utilities.common.Annotations.REQUEST_RESET_PASSWORD_BY_PHONE;
import static com.cncoding.teazer.utilities.common.Annotations.RESET_PASSWORD_BY_OTP;
import static com.cncoding.teazer.utilities.common.Annotations.SIGNUP;
import static com.cncoding.teazer.utilities.common.Annotations.SOCIAL_SIGNUP;
import static com.cncoding.teazer.utilities.common.Annotations.VERIFY_FORGOT_PASSWORD_OTP;
import static com.cncoding.teazer.utilities.common.Annotations.VERIFY_LOGIN_WITH_OTP;
import static com.cncoding.teazer.utilities.common.Annotations.VERIFY_SIGNUP;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class AuthenticationRepositoryImpl implements AuthenticationRepository {

    public static final String STATUS_FALSE = "status false";
    public static final String NOT_SUCCESSFUL = "not successful";
    public static final String FAILED = "failed";

    private AuthenticationService authenticationService;

    @Inject public AuthenticationRepositoryImpl(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public AuthenticationRepositoryImpl() {
        authenticationService = getRetrofitWithoutAuthToken().create(AuthenticationService.class);
    }

    @Override
    public LiveData<ResultObject> signUp(InitiateSignup initiateSignup) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.signUp(initiateSignup).enqueue(getCallback(liveData, SIGNUP));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> verifySignUp(VerifySignUp verifySignUp) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.verifySignUp(verifySignUp).enqueue(getCallback(liveData, VERIFY_SIGNUP));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> socialSignUp(SocialSignup socialSignup) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.socialSignUp(socialSignup).enqueue(getCallback(liveData, SOCIAL_SIGNUP));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> loginWithPassword(Login login) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.loginWithPassword(login).enqueue(getCallback(liveData, LOGIN_WITH_PASSWORD));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> loginWithOtp(InitiateLoginWithOtp initiateLoginWithOtp) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.loginWithOtp(initiateLoginWithOtp).enqueue(getCallback(liveData, LOGIN_WITH_OTP));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> verifyLoginWithOtp(VerifyLoginWithOtp verifyLoginWithOtp) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.verifyLoginWithOtp(verifyLoginWithOtp).enqueue(getCallback(liveData, VERIFY_LOGIN_WITH_OTP));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> checkUsernameAvailability(String username) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.checkUsernameAvailability(username).enqueue(getCallback(liveData, CHECK_USERNAME_AVAILABILITY));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> checkEmailAvailability(String email) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.checkEmailAvailability(email).enqueue(getCallback(liveData, CHECK_EMAIL_AVAILABILITY));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> checkPhoneNumberAvailability(int countryCode, long phoneNumber) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.checkPhoneNumberAvailability(countryCode, phoneNumber).enqueue(getCallback(liveData, CHECK_PHONE_NUMBER_AVAILABILITY));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> verifyForgotPasswordOtp(int otp) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.verifyForgotPasswordOtp(otp).enqueue(getCallback(liveData, VERIFY_FORGOT_PASSWORD_OTP));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> requestResetPasswordByEmail(String email) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.requestResetPasswordByEmail(email).enqueue(getCallback(liveData, REQUEST_RESET_PASSWORD_BY_EMAIL));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> requestResetPasswordByPhone(ResetPasswordByPhoneNumber resetPasswordByPhoneNumber) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.requestResetPasswordByPhone(resetPasswordByPhoneNumber)
                .enqueue(getCallback(liveData, REQUEST_RESET_PASSWORD_BY_PHONE));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> resetPasswordByOtp(ResetPasswordByOtp resetPasswordByOtp) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        authenticationService.resetPasswordByOtp(resetPasswordByOtp).enqueue(getCallback(liveData, RESET_PASSWORD_BY_OTP));
        return liveData;
    }

    @NonNull
    @Contract(pure = true)
    private Callback<ResultObject> getCallback(final MutableLiveData<ResultObject> liveData, @AuthCallType final int callType) {
        return new Callback<ResultObject>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    if (response.isSuccessful()) {
                        ResultObject tempResultObject = response.body();
                        tempResultObject.setCodeOnly(response.code());
                        tempResultObject.setAuthCallType(callType);
                        tempResultObject.setStatus(response.body().getStatus());
                        if (tempResultObject.getStatus()) {
                            liveData.setValue(tempResultObject);
                        } else {
                            liveData.setValue(tempResultObject.setErrorOnly(new Throwable(STATUS_FALSE)));
                        }
                    } else
                        liveData.setValue(new ResultObject(new Throwable(NOT_SUCCESSFUL), callType));
                } catch (Exception e) {
                    e.printStackTrace();
                    liveData.setValue(new ResultObject(new Throwable(NOT_SUCCESSFUL), callType));
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ResultObject(new Throwable(FAILED), callType));
            }
        };
    }
}