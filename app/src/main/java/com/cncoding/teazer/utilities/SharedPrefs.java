package com.cncoding.teazer.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.cncoding.teazer.utilities.Pojos.UploadParams;
import com.google.gson.Gson;

/**
 *
 * Created by Prem $ on 10/24/2017.
 */

public class SharedPrefs {

    public static final String TEAZER = "teazer_preferences";
    private static final String AUTH_TOKEN = "authToken";
    private static final String CURRENT_PASSWORD = "current_password";
    private static final String FCM_TOKEN = "fcmToken";
    private static final String VIDEO_UPLOAD_SESSION = "videoUploadSession";
    private static final String BLURRED_PROFILE_PIC = "homePageCache";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(TEAZER, Context.MODE_PRIVATE);
    }

//    public static void cacheHomePagePosts(Context context, PostList postList) {
//        getSharedPreferences(context)
//                .edit()
//                .putString(BLURRED_PROFILE_PIC, new Gson().toJson(postList))
//                .apply();
//    }
//
//    public static PostList getHomePagePostsCache(Context context) {
//        return new Gson().fromJson(getSharedPreferences(context).getString(BLURRED_PROFILE_PIC, null), PostList.class);
//    }
//
//    public static void clearHomePagePostsCache(Context context) {
//        getSharedPreferences(context)
//                .edit()
//                .putString(BLURRED_PROFILE_PIC, null)
//                .apply();
//    }

    public static void saveBlurredProfilePic(Context context, String path) {
        getSharedPreferences(context).edit().putString(BLURRED_PROFILE_PIC, path).apply();
    }

    public static String getBlurredProfilePic(Context context) {
        return getSharedPreferences(context).getString(BLURRED_PROFILE_PIC, null);
    }

    public static boolean isBlurredProfilePicSaved(Context context) {
        return getBlurredProfilePic(context) != null;
    }

    public static void deleteSavedBlurredProfilePic(Context context) {
        getSharedPreferences(context).edit().putString(BLURRED_PROFILE_PIC, null).apply();
    }

    public static void saveAuthToken(Context context, String authToken) {
        getSharedPreferences(context).edit().putString(AUTH_TOKEN, authToken).apply();
    }

    public static String getAuthToken(Context context) {
        return getSharedPreferences(context)
                .getString(AUTH_TOKEN, null);
    }

    public static void resetAuthToken(Context context) {
        getSharedPreferences(context).edit().putString(AUTH_TOKEN, null).apply();
    }

    public static void saveFcmToken(Context context, String fcmToken) {
        getSharedPreferences(context).edit().putString(FCM_TOKEN, fcmToken).apply();
    }

    static String getFcmToken(Context context) {
        return getSharedPreferences(context).getString(FCM_TOKEN, null);
    }

    public static void saveVideoUploadSession(Context context, UploadParams uploadParams) {
        getSharedPreferences(context).edit().putString(VIDEO_UPLOAD_SESSION, new Gson().toJson(uploadParams)).apply();
    }

    public static void finishVideoUploadSession(Context context) {
        getSharedPreferences(context).edit().putString(VIDEO_UPLOAD_SESSION, null).apply();
    }

    public static UploadParams getVideoUploadSession(Context context) {
        return new Gson().fromJson(getSharedPreferences(context).getString(VIDEO_UPLOAD_SESSION, null), UploadParams.class);
    }

    public static void setCurrentPassword(Context context, String password) {
        getSharedPreferences(context).edit().putString(CURRENT_PASSWORD, password).apply();
    }

    public static String getCurrentPassword(Context context) {
        return getSharedPreferences(context).getString(CURRENT_PASSWORD, null);
    }
}
