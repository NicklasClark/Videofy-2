package com.cncoding.teazer.apiCalls;

import android.support.design.widget.Snackbar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;

import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cncoding.teazer.MainActivity.BASE_URL;

/**
 *
 * Created by Prem $ on 10/10/2017.
 */

public class ApiCallingService {

    public static final int SUCCESS_OK_TRUE = 1;
    public static final int SUCCESS_OK_FALSE = 2;
    public static final int FAIL = 3;

    public static int uploadVideo(MultipartBody.Part videoPartFile, File videoFile) {
        final int[] returnCode = {-1};

        final String authToken = "Bearer 74621f519ea5f547b398adceb794d19b5e2af43282d7f9914801d11482e11ce3";

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", authToken)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build())
                .build();
        TeazerApiCall.UploadCalls uploadCalls = retrofit.create(TeazerApiCall.UploadCalls.class);

        Call<ResultObject> serverComm = uploadCalls.uploadVideoToServer(
                videoPartFile, videoFile.getName(),
                "Bangalore", 12.971599, 77.594563
        );
        serverComm.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = new ResultObject(response.code(), response.message(), false);
                if (result.getCode() == 201 && result.getMessage().equals("Created")) {
                    returnCode[0] = SUCCESS_OK_TRUE;
                } else {
                    returnCode[0] = SUCCESS_OK_FALSE;
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                returnCode[0] = FAIL;
            }
        });
        return returnCode[0];
    }

    public static void checkUsername(ProximaNovaRegularAutoCompleteTextView usernameView, boolean isSignUp) {
        getAvailabilityServiceCallback(
                getAuthenticationService().checkUsernameAvailability(usernameView.getText().toString()),
                usernameView, isSignUp);
    }

    public static void checkEmail(ProximaNovaRegularAutoCompleteTextView emailView, boolean isSignUp) {
        getAvailabilityServiceCallback(
                getAuthenticationService().checkEmailAvailability(emailView.getText().toString()),
                emailView, isSignUp);
    }

    public static void checkPhoneNumber(int countryCode, ProximaNovaRegularAutoCompleteTextView phoneNumberView, boolean isSignUp) {
        String phoneString = phoneNumberView.getText().toString();
        if (!phoneString.isEmpty()) {
            getAvailabilityServiceCallback(
                    getAuthenticationService().checkPhoneNumberAvailability(
                            countryCode,
                            Long.parseLong(phoneString)),
                    phoneNumberView, isSignUp);
        }
    }

    public static Call<ResultObject> performSignUp(UserAuth.SignUp signUpBody) {
        return getAuthenticationService().signUp(signUpBody);
    }

    public static Call<ResultObject> verifySignUp(UserAuth.SignUp.Verify signUpVerificationBody) {
        return getAuthenticationService().verifySignUp(signUpVerificationBody);
    }

    public static Call<ResultObject> socialSignUp(UserAuth.SignUp.Social socialSignUpDetails) {
        return getAuthenticationService().socialSignUp(socialSignUpDetails);
    }

    public static Call<ResultObject> requestResetPasswordByEmail(String email) {
        return getAuthenticationService().requestResetPasswordByEmail(email);
    }

    public static Call<ResultObject> requestResetPasswordByPhone(long phoneNumber, int countryCode) {
        return getAuthenticationService().requestResetPasswordByPhone(new UserAuth.PhoneNumberDetails(phoneNumber, countryCode));
    }

    public static Call<ResultObject> resetPasswordByOtp(UserAuth.ResetPasswordDetails resetPasswordDetails) {
        return getAuthenticationService().resetPasswordByOtp(resetPasswordDetails);
    }

    public static Call<ResultObject> loginWithPassword(UserAuth.LoginWithPassword loginWithPassword) {
        return getAuthenticationService().loginWithPassword(loginWithPassword);
    }

    public static Call<ResultObject> loginWithOtp(UserAuth.PhoneNumberDetails phoneNumberDetails) {
        return getAuthenticationService().loginWithOtp(phoneNumberDetails);
    }

    public static Call<ResultObject> verifyLoginWithOtp(UserAuth.PhoneNumberDetails.Verify verifyLoginWithOtp) {
        return getAuthenticationService().verifyLoginWithOtp(verifyLoginWithOtp);
    }

    private static TeazerApiCall.AuthenticationCalls getAuthenticationService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(TeazerApiCall.AuthenticationCalls.class);
    }

    private static void getAvailabilityServiceCallback(Call<ResultObject> service,
                                                       final ProximaNovaRegularAutoCompleteTextView view,
                                                       final boolean isSignUp) {
        service.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    if (isSignUp) {
                        if (!response.body().getStatus()) {
                            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick_circle, 0);
                        } else {
                            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cross, 0);
                        }
                    } else {
                        if (!response.body().getStatus()) {
                            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cross, 0);
                        } else {
                            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick_circle, 0);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Snackbar.make(view, "Failed to get availability", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
