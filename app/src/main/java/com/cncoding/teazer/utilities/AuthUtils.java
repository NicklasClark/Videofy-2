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
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cncoding.teazer.MainActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.authentication.ConfirmOtpFragment.OnOtpInteractionListener;
import com.cncoding.teazer.authentication.LoginFragment.LoginInteractionListener;
import com.cncoding.teazer.authentication.SignupFragment2;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbb20.CountryCodePicker;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.MainActivity.DEVICE_TYPE_ANDROID;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_OTP_ACTION;
import static com.cncoding.teazer.MainActivity.SIGNUP_WITH_EMAIL_ACTION;
import static com.cncoding.teazer.authentication.ForgotPasswordResetFragment.COUNTRY_CODE;
import static com.cncoding.teazer.authentication.LoginFragment.EMAIL_FORMAT;
import static com.cncoding.teazer.authentication.LoginFragment.PHONE_NUMBER_FORMAT;
import static com.cncoding.teazer.authentication.LoginFragment.USERNAME_FORMAT;
import static com.cncoding.teazer.utilities.OfflineUserProfile.TEAZER;

/**
 *
 * Created by Prem $ on 10/26/2017.
 */

public class AuthUtils {

    public static boolean isUserLoggedIn(Context context) {
        return SharedPrefs.getAuthToken(context) != null;
    }

    public static boolean togglePasswordVisibility(ProximaNovaRegularAutoCompleteTextView view, MotionEvent event) {
        if (view.getCompoundDrawables()[2] != null) {
            if (event.getRawX() >= (view.getRight() - view.getCompoundDrawables()[2].getBounds().width())) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        view.setSelection(view.getText().length());
                        return true;
                    case MotionEvent.ACTION_UP:
                        view.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        view.setSelection(view.getText().length());
                        return true;
                    default:
                        return false;
                }
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
        if (countryCodePicker != null && countryCode != -1) {
            countryCodePicker.setCountryForPhoneCode(countryCode);
        }
        return countryCode;
    }

    @NonNull
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getFcmToken(Context context) {
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
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
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
                                            final Pojos.Authorize authorize, final ProximaNovaSemiboldButton signupBtn) {
        ApiCallingService.Auth.performSignUp(authorize).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        mListener.onFinalEmailSignupInteraction(SIGNUP_WITH_EMAIL_ACTION, authorize);
                    } else {
                        ViewUtils.showSnackBar(signupBtn, "Username, email or phone number already exists.\n" +
                                "Or you may have reached maximum OTP retry attempts");
                    }
                } else
                    ViewUtils.showSnackBar(signupBtn, response.code() + " : " + response.message());
                signupBtn.setEnabled(true);
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                ViewUtils.showSnackBar(signupBtn, t.getMessage());
                signupBtn.setEnabled(true);
            }
        });
    }

    public static void performFinalSignup(final Context context, final Pojos.Authorize verify, final CountDownTimer countDownTimer,
                                          final ProximaNovaRegularTextView otpVerifiedTextView, final OnOtpInteractionListener mListener) {
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
                                ViewUtils.setTextViewDrawableEnd(otpVerifiedTextView, R.drawable.ic_tick_circle);
                                mListener.onOtpInteraction(verify, true);
                                break;
//                    Username, Email or Phone Number already exists
                            case 200:
                                countDownTimer.cancel();
                                otpVerifiedTextView.setText(R.string.already_exists);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mListener.onOtpInteraction(verify, false);
                                    }
                                }, 1500);
                                break;
//                    Failed, Invalid JSON or validation failed
                            default:
                                ViewUtils.showSnackBar(otpVerifiedTextView, response.code() + " : " + response.message());
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        Snackbar.make(otpVerifiedTextView, t.getMessage(), Snackbar.LENGTH_SHORT).show();
//                        logout(context, null);
                    }
                });
    }

    private static void stopCircularReveal(View revealLayout) {
        revealLayout.animate().alpha(0).setDuration(280).start();
    }

    /**
     * Login Step 1.
     * */
    public static void loginWithOtp(final Context context, final String username, final int countryCode,
                                    final LoginInteractionListener mListener, final ProximaNovaSemiboldButton loginBtn,
                                    final LinearLayout revealLayout, final ProximaNovaRegularTextView otpVerifiedTextView,
                                    final CountDownTimer[] countDownTimer, final boolean isResendAction) {
        final Pojos.Authorize authorize = new Pojos.Authorize(Long.parseLong(username), countryCode);
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
                    ViewUtils.showSnackBar(loginBtn, response.code() + " : " + response.message());
                loginBtn.setEnabled(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopCircularReveal(revealLayout);
                    }
                }, 1000);
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                ViewUtils.showSnackBar(loginBtn, t.getMessage());
                loginBtn.setEnabled(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopCircularReveal(revealLayout);
                    }
                }, 1000);
            }
        });
    }

    /**
     * Login step 2.
     * */
    public static void verifyOtpLogin(final Context context, Pojos.Authorize userSignUpDetails, int otp, final CountDownTimer countDownTimer,
                                      final ProximaNovaRegularTextView otpVerifiedTextView,
                                      final OnOtpInteractionListener mListener, final ProximaNovaSemiboldButton otpResendBtn) {
        ApiCallingService.Auth.verifyLoginWithOtp(
                new Pojos.Authorize(
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
                                        mListener.onOtpInteraction(
                                                null, false);
                                    }
                                }, 1000);
                            } else {
                                Snackbar.make(otpVerifiedTextView, R.string.login_through_otp_error,
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        } else
                            ViewUtils.showSnackBar(otpResendBtn, response.code() + " : " + response.message());
                        otpResendBtn.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        ViewUtils.showSnackBar(otpResendBtn, t.getMessage());
                        otpResendBtn.setEnabled(true);
                    }
                });
    }

    public static void logout(final Context context, @Nullable final Activity activity) {
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
                        SharedPrefs.resetAuthToken(context);
                        if (activity != null) {
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

//    public static void onLoginSuccessful(FragmentActivity activity) {
//
//    }
//
//    public static void onSignupSuccessful(FragmentActivity activity) {
//
//    }
}
