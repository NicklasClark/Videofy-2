package com.cncoding.teazer.data.remote.apicalls.authentication;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.cncoding.teazer.R;
import com.cncoding.teazer.TeazerApplication;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.model.auth.InitiateSignup;
import com.cncoding.teazer.model.auth.Login;
import com.cncoding.teazer.model.auth.ResetPasswordByOtp;
import com.cncoding.teazer.model.auth.ResetPasswordByPhoneNumber;
import com.cncoding.teazer.model.auth.SocialSignup;
import com.cncoding.teazer.model.auth.VerifyLoginWithOtp;
import com.cncoding.teazer.model.auth.VerifySignUp;

import org.jetbrains.annotations.Contract;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class AuthenticationRepositoryImpl implements AuthenticationRepository {

    public static final int SIGNUP = 10;
    public static final int VERIFY_SIGNUP = 11;
    public static final int SOCIAL_SIGNUP = 12;
    public static final int LOGIN_WITH_PASSWORD = 13;
    public static final int LOGIN_WITH_OTP = 14;
    public static final int VERIFY_LOGIN_WITH_OTP = 15;
    public static final int CHECK_USERNAME_AVAILABILITY = 16;
    public static final int CHECK_EMAIL_AVAILABILITY = 17;
    public static final int CHECK_PHONE_NUMBER_AVAILABILITY = 18;
    public static final int VERIFY_FORGOT_PASSWORD_OTP = 19;
    public static final int REQUEST_RESET_PASSWORD_BY_EMAIL = 20;
    public static final int REQUEST_RESET_PASSWORD_BY_PHONE = 21;
    public static final int RESET_PASSWORD_BY_OTP = 22;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SIGNUP, VERIFY_SIGNUP, SOCIAL_SIGNUP, LOGIN_WITH_PASSWORD, LOGIN_WITH_OTP, VERIFY_LOGIN_WITH_OTP,
            CHECK_USERNAME_AVAILABILITY, CHECK_EMAIL_AVAILABILITY, CHECK_PHONE_NUMBER_AVAILABILITY, VERIFY_FORGOT_PASSWORD_OTP,
            REQUEST_RESET_PASSWORD_BY_EMAIL, REQUEST_RESET_PASSWORD_BY_PHONE, RESET_PASSWORD_BY_OTP})
    public @interface CallType {}

    public static final String STATUS_FALSE = "status false";
    public static final String NOT_SUCCESSFUL = "not successful";
    public static final String FAILED = "failed";

    private AuthenticationService authenticationService;

    public AuthenticationRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TeazerApplication.getContext().getString(R.string.base_url))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                .build();
        authenticationService = retrofit.create(AuthenticationService.class);
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
    private Callback<ResultObject> getCallback(final MutableLiveData<ResultObject> liveData, @CallType final int callType) {
        return new Callback<ResultObject>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    if (response.isSuccessful()) {
                        ResultObject tempResultObject = response.body();
                        tempResultObject.setCallType(callType);
                        if (tempResultObject.getStatus()) {
                            liveData.setValue(tempResultObject);
                        } else {
                            liveData.setValue(tempResultObject.setError(new Throwable(STATUS_FALSE)));
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