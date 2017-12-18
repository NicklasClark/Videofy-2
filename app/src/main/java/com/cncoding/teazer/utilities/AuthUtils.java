package com.cncoding.teazer.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;

import com.cncoding.teazer.MainActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ErrorBody;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.authentication.ConfirmOtpFragment.OnOtpInteractionListener;
import com.cncoding.teazer.authentication.LoginFragment.LoginInteractionListener;
import com.cncoding.teazer.authentication.SignupFragment2;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.customViews.TypeFactory;
import com.cncoding.teazer.model.base.Authorize;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.MainActivity.DEVICE_TYPE_ANDROID;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_OTP_ACTION;
import static com.cncoding.teazer.authentication.ForgotPasswordResetFragment.COUNTRY_CODE;
import static com.cncoding.teazer.authentication.LoginFragment.EMAIL_FORMAT;
import static com.cncoding.teazer.authentication.LoginFragment.PHONE_NUMBER_FORMAT;
import static com.cncoding.teazer.authentication.LoginFragment.USERNAME_FORMAT;
import static com.cncoding.teazer.utilities.SharedPrefs.TEAZER;
import static com.cncoding.teazer.utilities.ViewUtils.showSnackBar;

/**
 *
 * Created by Prem $ on 10/26/2017.
 */

public class AuthUtils {

    public static boolean isUserLoggedIn(Context context) {
        return SharedPrefs.getAuthToken(context) != null;
    }

    public static boolean togglePasswordVisibility(ProximaNovaRegularAutoCompleteTextView view, Context context) {
        if (view.getCompoundDrawables()[2] != null) {
            switch (view.getInputType()) {
                case InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT:
                    view.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    view.setTypeface(new TypeFactory(context).regular);
                    view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_viewfilled_cross, 0, 0, 0);

                    view.setSelection(view.getText().length());
                    return true;
                case InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD:
                    view.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    view.setTypeface(new TypeFactory(context).regular);
                    view.setSelection(view.getText().length());
                    view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_filled, 0, 0, 0);

                    view.setTypeface(new TypeFactory(context).regular);
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    public static void setCountryCode(Context context, int countryCode) {
        context.getSharedPreferences(TEAZER, Context.MODE_PRIVATE)
                .edit()
                .putInt(COUNTRY_CODE, countryCode)
                .apply();
    }

    public static int getCountryCode(@Nullable CountryCodePicker countryCodePicker, FragmentActivity fragmentActivity) {
        int countryCode = fragmentActivity.getApplicationContext()
                .getSharedPreferences(TEAZER, Context.MODE_PRIVATE)
                .getInt(COUNTRY_CODE, -1);
        if (countryCodePicker != null) {
            if (countryCode != -1)
                countryCodePicker.setCountryForPhoneCode(countryCode);
            else {
                countryCodePicker.setCountryForNameCode(Locale.getDefault().getCountry());
                countryCode = countryCodePicker.getSelectedCountryCodeAsInt();
                setCountryCode(fragmentActivity.getApplicationContext(), countryCode);
            }
        }
        return countryCode;
    }

    @NonNull
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getFcmToken(Context context) {
//        Log.d("FCM Token", FirebaseInstanceId.getInstance().getToken());
        return SharedPrefs.getFcmToken(context) == null ? FirebaseInstanceId.getInstance().getToken() : SharedPrefs.getFcmToken(context);


    }

    /**
     * Check whether the text entered is username, email, or password
     * @param text : the text to check.
     * @return 10, if the text entered is a phone number.
     *         11, if the text entered is an email.
     *         12, if the text entered is a username.
     *         -1, if the entered text is empty.
     * */
    private static int getEnteredUserFormat(String text) {
        if (!text.isEmpty()) {
            if (TextUtils.isDigitsOnly(text)) {
//                    Phone number is entered
                return PHONE_NUMBER_FORMAT;
            }
            else if (isValidEmailAddress(text)) {
//                    Email is entered
                return EMAIL_FORMAT;
            }
            else {
//                    Username is entered
                return USERNAME_FORMAT;
            }
        } else return -1;
    }

    public static boolean isValidEmailAddress(String email) {
//        boolean result = true;
//        try {
//            InternetAddress emailAddress = new InternetAddress(email);
//            emailAddress.validate();
//        } catch (AddressException ex) {
//            result = false;
//        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        char[] chars = phoneNumber.toCharArray();
        return chars.length >= 4 && chars.length <= 13;
    }

    public static void validateUsername(ProximaNovaRegularAutoCompleteTextView usernameView, FragmentActivity activity) {
        if (!usernameView.getText().toString().isEmpty()) {
            switch (getEnteredUserFormat(usernameView.getText().toString())) {
                case PHONE_NUMBER_FORMAT:
//                    countryCode = getCountryCode(countryCodePicker, getActivity());
//                    if (countryCode == -1)
//                        countryCodePicker.launchCountrySelectionDialog();
//                    else
                    if (isValidPhoneNumber(usernameView.getText().toString()))
                        ApiCallingService.Auth.checkPhoneNumber(getCountryCode(null, activity), usernameView, false);
                    else
                        ViewUtils.setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
                    break;
                case EMAIL_FORMAT:
                    if (AuthUtils.isValidEmailAddress(usernameView.getText().toString()))
                        ApiCallingService.Auth.checkEmail(usernameView, false);
                    else
                        ViewUtils.setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
                    break;
                case USERNAME_FORMAT:
                    ApiCallingService.Auth.checkUsername(usernameView, false);
                    break;
                default:
                    break;
            }
        } else {
            ViewUtils.setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
        }
    }

    public static boolean isPasswordValid(ProximaNovaRegularAutoCompleteTextView view) {
        return view.getText().toString().length() >= 5;
    }

    public static void performInitialSignup(final SignupFragment2.OnFinalSignupInteractionListener mListener,
                                            final Authorize authorize, final ProximaNovaSemiboldButton signupBtn,
                                            final String picturePath) {
        ApiCallingService.Auth.performSignUp(authorize).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        mListener.onFinalEmailSignupInteraction(authorize, picturePath);
                    } else {
                        showSnackBar(signupBtn, "Username, email or phone number already exists.\n" +
                                "Or you may have reached maximum OTP retry attempts");
                    }
                } else
                    showSnackBar(signupBtn, getErrorMessage(response.errorBody()));
                signupBtn.setEnabled(true);
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                signupBtn.setEnabled(true);
            }
        });
    }

    public static void performFinalSignup(final Context context, final Authorize verify, final CountDownTimer countDownTimer,
                                          final ProximaNovaRegularTextView otpVerifiedTextView,
                                          final OnOtpInteractionListener mListener, final ProximaNovaSemiboldButton otpResendBtn,
                                          final String picturePath) {
        ApiCallingService.Auth.verifySignUp(verify)

                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, final Response<ResultObject> response) {
                        switch (response.code()) {
//                    Authorize successful
                            case 201:
                                countDownTimer.cancel();
                                otpVerifiedTextView.setText(context.getString(R.string.verified));
                                SharedPrefs.saveAuthToken(context, response.body().getAuthToken());
                                ViewUtils.setTextViewDrawableEnd(otpVerifiedTextView, R.drawable.btn_checked);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mListener.onOtpInteraction(verify, picturePath);
                                    }
                                }, 1000);
                                break;
//                    Username, Email or Phone Number already exists
                            case 200:
                                countDownTimer.cancel();
                                otpVerifiedTextView.setText(R.string.wrong_otp);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        otpVerifiedTextView.setText("");
                                    }
                                }, 2800);
                                otpResendBtn.setEnabled(true);
                                otpResendBtn.setAlpha(1);
                                break;
//                    Failed, Invalid JSON or validation failed
                            default:
                                showSnackBar(otpResendBtn, getErrorMessage(response.errorBody()));
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    public static void stopCircularReveal(final View view) {
        view.animate().alpha(0).setDuration(280).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
            }
        }, 280);
    }

    /**
     * Login Step 1.
     * */
    public static void loginWithOtp(final Context context, final String username, final int countryCode,
                                    final LoginInteractionListener mListener, final ProximaNovaSemiboldButton loginBtn,
                                    final ProgressBar progressBar, final ProximaNovaRegularTextView otpVerifiedTextView,
                                    final CountDownTimer[] countDownTimer, final boolean isResendAction) {
        final Authorize authorize = new Authorize(Long.parseLong(username), countryCode);
        ApiCallingService.Auth.loginWithOtp(authorize).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        if (!isResendAction) {
                            if (mListener != null)
                                mListener.onLoginFragmentInteraction(LOGIN_WITH_OTP_ACTION, authorize);
                        } else {
                            countDownTimer[0] = ViewUtils.startCountDownTimer(context, otpVerifiedTextView, loginBtn);
                            countDownTimer[0].start();
                            Snackbar.make(loginBtn, "New otp sent to " + username, Snackbar.LENGTH_LONG).show();
                        }
                    }
                    else Snackbar.make(loginBtn, R.string.login_through_otp_error, Snackbar.LENGTH_LONG).show();
                } else
                    showSnackBar(loginBtn, getErrorMessage(response.errorBody()));
                loginBtn.setEnabled(true);
                if (!isResendAction)
                    stopCircularReveal(progressBar);
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                loginBtn.setEnabled(true);
                if (!isResendAction)
                    stopCircularReveal(progressBar);
            }
        });
    }

    /**
     * Login step 2.
     * */
    public static void verifyOtpLogin(final Context context, Authorize userSignUpDetails, int otp, final CountDownTimer countDownTimer,
                                      final ProximaNovaRegularTextView otpVerifiedTextView,
                                      final OnOtpInteractionListener mListener, final ProximaNovaSemiboldButton otpResendBtn) {
        ApiCallingService.Auth.verifyLoginWithOtp(
                new Authorize(
                        getFcmToken(context),
                        getDeviceId(context),
                        DEVICE_TYPE_ANDROID,
                        userSignUpDetails.getPhoneNumber(),
                        userSignUpDetails.getCountryCode(),
                        otp))

                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, final Response<ResultObject> response) {
                        if (response.code() == 200) {
                            if (response.body().getStatus()) {
                                SharedPrefs.saveAuthToken(context, response.body().getAuthToken());
                                countDownTimer.cancel();
                                otpVerifiedTextView.setText(context.getString(R.string.verified));
                                ViewUtils.setTextViewDrawableEnd(otpVerifiedTextView, R.drawable.ic_check);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mListener != null)
                                            mListener.onOtpInteraction(null, null);
                                    }
                                }, 1000);
                            } else {
                                Snackbar.make(otpVerifiedTextView, R.string.wrong_otp,
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } else
                            showSnackBar(otpResendBtn, getErrorMessage(response.errorBody()));

                        otpResendBtn.setEnabled(true);
                        otpResendBtn.setAlpha(1);
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                        otpResendBtn.setEnabled(true);
                        otpResendBtn.setAlpha(1);
                    }
                });
    }

    public static void logout(final Context context, @Nullable final Activity activity) {
        SharedPrefs.finishVideoUploadSession(context);
        ApiCallingService.User.logout("Bearer " + SharedPrefs.getAuthToken(context), context)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
//                        if (response.code() == 200) {
//                            if (response.body().getStatus()) {
//                                Toast.makeText(context, "Successfully logged out.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else
//                            Toast.makeText(context, "Logout failed, logging out manually...", Toast.LENGTH_SHORT).show();
                        LTFO();
                    }

                    private void LTFO() {
                        SharedPrefs.resetAuthToken(context);
                        if (activity != null) {
                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);
                            activity.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                        LTFO();
                    }
                });
    }

    @NonNull public static String getErrorMessage(ResponseBody responseBody) {
        if (responseBody != null && responseBody.contentLength() != -1) {
            try {
                ErrorBody errorBody = new Gson().fromJson(responseBody.string(), ErrorBody.class);
                return errorBody.getReason().isEmpty() ? "Something went wrong, Please try again" : errorBody.getReason().get(0);
            } catch (IOException e) {
                e.printStackTrace();
                return "Something went wrong, Please try again";
            }
        } else
            return "Something went wrong, Please try again";
    }

//    public static void onLoginSuccessful(FragmentActivity activity) {
//
//    }
//
//    public static void onSignupSuccessful(FragmentActivity activity) {
//
//    }
}
