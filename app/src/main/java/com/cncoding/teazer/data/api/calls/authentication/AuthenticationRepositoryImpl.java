package com.cncoding.teazer.data.api.calls.authentication;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.R;
import com.cncoding.teazer.TeazerApplication;
import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.model.base.Authorize;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class AuthenticationRepositoryImpl implements AuthenticationRepository {

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
