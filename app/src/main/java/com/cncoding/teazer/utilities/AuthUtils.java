package com.cncoding.teazer.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.View;

import com.cncoding.teazer.MainActivity;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ErrorBody;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.data.local.database.TeazerDB;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.text.TextUtils.isDigitsOnly;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.clearRetrofitWithAuthToken;
import static com.cncoding.teazer.ui.authentication.LoginFragment.EMAIL_FORMAT;
import static com.cncoding.teazer.ui.authentication.LoginFragment.PHONE_NUMBER_FORMAT;
import static com.cncoding.teazer.ui.authentication.LoginFragment.USERNAME_FORMAT;
import static com.cncoding.teazer.ui.authentication.ResetPasswordFragment.COUNTRY_CODE;
import static com.cncoding.teazer.utilities.SharedPrefs.TEAZER;
import static com.cncoding.teazer.utilities.SharedPrefs.clearMedia;
import static com.cncoding.teazer.utilities.SharedPrefs.resetAuthToken;

/**
 *
 * Created by Prem $ on 10/26/2017.
 */

public class AuthUtils {

    public static boolean isConnected(Context c) {
        try {
            //noinspection ConstantConditions
            NetworkInfo info = ((ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            return info != null && info.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isConnectedToWifi(Context c) {
        try {
            //noinspection ConstantConditions
            NetworkInfo info = ((ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(TYPE_WIFI);
            return info != null && info.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isUserLoggedIn(Context context) {
        return SharedPrefs.getAuthToken(context) != null;
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
                countryCodePicker.setCountryForNameCode(getUserCountry(fragmentActivity));
                countryCode = countryCodePicker.getSelectedCountryCodeAsInt();
                setCountryCode(fragmentActivity.getApplicationContext(), countryCode);
            }
        }
        return countryCode;
    }

    @NonNull
    private static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry;
            if (tm != null) {
                simCountry = tm.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                    return simCountry.toLowerCase(Locale.US);
                } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                    String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                        return networkCountry.toLowerCase(Locale.US);
                    }
                }
            }
            return "IN";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "US";
        }
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
    public static int getEnteredUserFormat(String text) {
        if (!text.isEmpty()) {
            if (isDigitsOnly(text)) {
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
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (isDigitsOnly(phoneNumber)) {
            char[] chars = phoneNumber.toCharArray();
            return chars.length >= 4 && chars.length <= 13;
        } else {
            return false;
        }
    }

    public static void removeView(final View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.animate().alpha(0).setDuration(280).start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.INVISIBLE);
                }
            }, 280);
        }
    }

    public static void logout(final Context context, @Nullable final Activity activity) {
        SharedPrefs.finishVideoUploadSession(context);
        ApiCallingService.User.logout("Bearer " + SharedPrefs.getAuthToken(context), context)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        LTFO();
                    }

                    private void LTFO() {
                        resetAuthToken(context);
                        clearMedia(context);
                        clearRetrofitWithAuthToken();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                TeazerDB.getInstance(context.getApplicationContext()).dao().clearTable();
                                TeazerDB.destroyInstance();
                            }
                        }).start();
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
            } catch (Exception e) {
                e.printStackTrace();
                return "Something went wrong, Please try again";
            }
        } else
            return "Something went wrong, Please try again";
    }

//    public static boolean togglePasswordVisibility(ProximaNovaRegularAutoCompleteTextView view, Context context) {
//        if (view.getCompoundDrawables()[2] != null) {
//            switch (view.getInputType()) {
//                case InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT:
//                    view.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                    view.setTypeface(new TypeFactory(context).regular);
//                    view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_viewfilled_cross, 0, 0, 0);
//                    view.setSelection(view.getText().length());
//                    return true;
//                case InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD:
//                    view.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
//                    view.setTypeface(new TypeFactory(context).regular);
//                    view.setSelection(view.getText().length());
//                    view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_filled, 0, 0, 0);
//                    view.setTypeface(new TypeFactory(context).regular);
//                    return true;
//                default:
//                    return false;
//            }
//        }
//        return false;
//    }
}