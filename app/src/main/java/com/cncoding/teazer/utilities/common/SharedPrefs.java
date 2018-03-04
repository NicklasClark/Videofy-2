package com.cncoding.teazer.utilities.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.model.base.UploadParams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.Map;

/**
 *
 * Created by Prem $ on 10/24/2017.
 */

public class SharedPrefs {

    public static final String TEAZER = "teazer_preferences";
    private static final String AUTH_TOKEN = "authToken";
    private static final String USER_ID = "user_id";
    private static final String CURRENT_PASSWORD = "current_password";
    private static final String FCM_TOKEN = "fcmToken";
    private static final String VIDEO_UPLOAD_SESSION = "videoUploadSession";
//    private static final String BLURRED_PROFILE_PIC = "homePageCache";
    private static final String REACTION_UPLOAD_SESSION = "reactionUploadSession";
    private static final String FOLLOWING_NOTIFICATION = "followingNotificationCount";
    private static final String REQUEST_NOTIFICATION = "requestNotificationCount";
    private static final String SAVE_VIDEO_IN_GALLERY = "saveIntoGallery";
    private static final String ACTIVE = "active";
    private static final String CAN_SAVE_MEDIA_ONLY_ON_WIFI = "canSaveMedia";

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

//    public static void saveBlurredProfilePic(Context context, String path) {
//        getSharedPreferences(context).edit().putString(BLURRED_PROFILE_PIC, path).apply();
//    }
//
//    public static String getBlurredProfilePic(Context context) {
//        return getSharedPreferences(context).getString(BLURRED_PROFILE_PIC, null);
//    }
//
//    public static boolean isBlurredProfilePicSaved(Context context) {
//        return getBlurredProfilePic(context) != null;
//    }
//
//    public static void deleteSavedBlurredProfilePic(Context context) {
//        getSharedPreferences(context).edit().putString(BLURRED_PROFILE_PIC, null).apply();
//    }

    public static void saveAuthToken(Context context, String authToken) {
        getSharedPreferences(context).edit().putString(AUTH_TOKEN, authToken).apply();
    }

    @Nullable
    public static String getAuthToken(Context context) {
        try {
            return getSharedPreferences(context).getString(AUTH_TOKEN, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void resetAuthToken(Context context) {
        getSharedPreferences(context).edit().remove(AUTH_TOKEN).apply();
    }

    public static void saveFcmToken(Context context, String fcmToken) {
        getSharedPreferences(context).edit().putString(FCM_TOKEN, fcmToken).apply();
    }

    static String getFcmToken(Context context) {
        return getSharedPreferences(context).getString(FCM_TOKEN, null);
    }

    public static void saveVideoUploadSession(Context context, UploadParams uploadParams) {
        getSharedPreferences(context)
                .edit()
                .putString(VIDEO_UPLOAD_SESSION, new Gson().toJson(uploadParams, new TypeToken<UploadParams>() {}.getType()))
                .apply();
    }

    public static void finishVideoUploadSession(Context context) {
        getSharedPreferences(context).edit().remove(VIDEO_UPLOAD_SESSION).apply();
    }

    public static UploadParams getVideoUploadSession(Context context) {
        return new Gson().fromJson(getSharedPreferences(context)
                .getString(VIDEO_UPLOAD_SESSION, null), new TypeToken<UploadParams>() {}.getType());
    }

    public static void saveReactionUploadSession(Context context, UploadParams uploadParams) {
        getSharedPreferences(context)
                .edit()
                .putString(REACTION_UPLOAD_SESSION, new Gson().toJson(uploadParams, new TypeToken<UploadParams>() {}.getType()))
                .apply();
    }

    public static void finishReactionUploadSession(Context context) {
        getSharedPreferences(context).edit().remove(REACTION_UPLOAD_SESSION).apply();
    }

    public static UploadParams getReactionUploadSession(Context context) {
        return new Gson().fromJson(getSharedPreferences(context)
                .getString(REACTION_UPLOAD_SESSION, null), new TypeToken<UploadParams>() {}.getType());
    }

    public static void setCurrentPassword(Context context, String password) {
        getSharedPreferences(context).edit().putString(CURRENT_PASSWORD, password).apply();
    }

    public static String getCurrentPassword(Context context) {
        return getSharedPreferences(context).getString(CURRENT_PASSWORD, null);
    }

    public static void setFollowingNotificationCount(Context context, int value) {
        getSharedPreferences(context).edit().putInt(FOLLOWING_NOTIFICATION, value).apply();
    }

    public static int getFollowingNotificationCount(Context context) {
        return getSharedPreferences(context).getInt(FOLLOWING_NOTIFICATION, 0);
    }
    public static void setRequestNotificationCount(Context context, int value) {
        getSharedPreferences(context).edit().putInt(REQUEST_NOTIFICATION, value).apply();
    }

    public static int getRequestNotificationCount(Context context) {
        return getSharedPreferences(context).getInt(REQUEST_NOTIFICATION, 0);
    }

    public static void saveUserId(Context context, int userId) {
        getSharedPreferences(context).edit().putInt(USER_ID, userId).apply();
    }

    public static int getUserId(Context context) {
        try {
            return getSharedPreferences(context).getInt(USER_ID, -1);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void setSaveVideoFlag(Context context, boolean value) {
        getSharedPreferences(context).edit().putBoolean(SAVE_VIDEO_IN_GALLERY, value).apply();
    }

    public static boolean getSaveVideoFlag(Context context) {
        return getSharedPreferences(context).getBoolean(SAVE_VIDEO_IN_GALLERY, false);
    }

    public static void onActivityActive(Activity activity) {
        getSharedPreferences(activity.getApplicationContext())
                .edit()
                .putBoolean(ACTIVE + ViewUtils.BLANK_SPACE + activity.getPackageName(), true)
                .apply();
    }

    public static boolean isActivityActive(Activity activity) {
        return getSharedPreferences(activity.getApplicationContext())
                .getBoolean(ACTIVE + ViewUtils.BLANK_SPACE + activity.getPackageName(), true);
    }

    public static void onActivityInactive(Activity activity) {
        getSharedPreferences(activity.getApplicationContext())
                .edit()
                .putBoolean(ACTIVE + ViewUtils.BLANK_SPACE + activity.getPackageName(), false)
                .apply();
    }

    public static void setCanSaveMediaOnlyOnWiFi(Context context, boolean canSaveMedia) {
        getSharedPreferences(context).edit().putBoolean(CAN_SAVE_MEDIA_ONLY_ON_WIFI, canSaveMedia).apply();
    }

    public static boolean getCanSaveMediaOnlyOnWiFi(Context context) {
        return getSharedPreferences(context).getBoolean(CAN_SAVE_MEDIA_ONLY_ON_WIFI, false);
    }

    public static void saveMedia(Context context, String keyUrl, String valueFilePath) {
        getSharedPreferences(context).edit().putString("media_" + keyUrl, valueFilePath).apply();
    }

    public static String getMedia(Context context, String keyUrl) {
        return getSharedPreferences(context).getString("media_" + keyUrl, null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteMedia(Context context, String keyUrl) {
        File file = new File(getMedia(context, keyUrl));
        if (file.exists()) file.delete();
        getSharedPreferences(context).edit().remove("media_" + keyUrl).apply();
    }

    static void clearMedia(Context context) {
        new ClearMediaTask(getSharedPreferences(context)).execute();
    }

    private static class ClearMediaTask extends AsyncTask<Void, Void, Void> {

        private SharedPreferences preferences;

        private ClearMediaTask(SharedPreferences preferences) {
            this.preferences = preferences;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Map<String,?> keys = preferences.getAll();
            for(Map.Entry<String,?> entry : keys.entrySet())
                if (entry.getKey().contains("media_"))
                    preferences.edit().remove(entry.getKey()).apply();
            return null;
        }
    }
}