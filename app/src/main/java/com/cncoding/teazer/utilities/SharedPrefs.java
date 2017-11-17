package com.cncoding.teazer.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import static com.cncoding.teazer.utilities.OfflineUserProfile.TEAZER;

/**
 *
 * Created by Prem $ on 10/24/2017.
 */

public class SharedPrefs {

    private static final String AUTH_TOKEN = "authToken";
    private static final String FCM_TOKEN = "fcmToken";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(TEAZER, Context.MODE_PRIVATE);
    }

    public static void saveAuthToken(Context context, String authToken) {
        getSharedPreferences(context)
                .edit()
                .putString(AUTH_TOKEN, authToken)
                .apply();
    }

    public static String getAuthToken(Context context) {
        return getSharedPreferences(context)
                .getString(AUTH_TOKEN, null);
    }

    public static void resetAuthToken(Context context) {
        getSharedPreferences(context).edit().putString(AUTH_TOKEN, null).apply();
    }

    public static void saveFcmToken(Context context, String fcmToken) {
        getSharedPreferences(context)
                .edit()
                .putString(FCM_TOKEN, fcmToken)
                .apply();
    }

    public static String getFcmToken(Context context) {
        return getSharedPreferences(context)
                .getString(FCM_TOKEN, null);
    }

    public static void resetFcmToken(Context context) {
        getSharedPreferences(context).edit().putString(FCM_TOKEN, null).apply();
    }
}
