package com.cncoding.teazer.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.cncoding.teazer.model.base.UploadParams;
import com.google.gson.Gson;

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
    private static final String BLURRED_PROFILE_PIC = "homePageCache";
    private static final String REACTION_UPLOAD_SESSION = "reactionUploadSession";
    private static final String FOLLOWING_NOTIFICATION = "followingNotificationCount";
    private static final String REQUEST_NOTIFICATION = "requestNotificationCount";
    private static final String SAVE_VIDEO_IN_GALLERY = "saveIntoGallery";
    private static final String FIRST_START_POST_LIST = "firstStartPostList";
    private static final String FIRST_START_DISCOVER = "firstStartDiscover";
    private static final String FIRST_START_PROFILE = "firstStartProfile";
    private static final String FIRST_START_POST_DETAILS = "firstStartPostDetails";
    private static final String FIRST_START_MY_INTERESTS = "firstStartMyInterests";

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

    @Nullable
    public static String getAuthToken(Context context) {
        try {
            return getSharedPreferences(context)
                    .getString(AUTH_TOKEN, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void resetAuthToken(Context context) {
        getSharedPreferences(context).edit().putString(AUTH_TOKEN, null).apply();
    }

    public static void saveFcmToken(Context context, String fcmToken) {
        getSharedPreferences(context).edit().putString(FCM_TOKEN, fcmToken).apply();
    }

    public static String getFcmToken(Context context) {
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

    public static void saveReactionUploadSession(Context context, UploadParams uploadParams) {
        getSharedPreferences(context).edit().putString(REACTION_UPLOAD_SESSION, new Gson().toJson(uploadParams)).apply();
    }

    public static void finishReactionUploadSession(Context context) {
        getSharedPreferences(context).edit().putString(REACTION_UPLOAD_SESSION, null).apply();
    }

    public static UploadParams getReactionUploadSession(Context context) {
        return new Gson().fromJson(getSharedPreferences(context).getString(REACTION_UPLOAD_SESSION, null), UploadParams.class);
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
            return getSharedPreferences(context)
                    .getInt(USER_ID, -1);
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

    public static void setFirstStartPostList(Context context) {
        getSharedPreferences(context).edit().putBoolean(FIRST_START_POST_LIST, false).apply();
    }

    public static boolean isFirstStartPostList(Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_START_POST_LIST, true);
    }

    public static void setFirstStartDiscover(Context context) {
        getSharedPreferences(context).edit().putBoolean(FIRST_START_DISCOVER, false).apply();
    }

    public static boolean isFirstStartDiscover(Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_START_DISCOVER, true);
    }

    public static void setFirstStartProfile(Context context) {
        getSharedPreferences(context).edit().putBoolean(FIRST_START_PROFILE, false).apply();
    }

    public static boolean isFirstStartProfile(Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_START_PROFILE, true);
    }

    public static void setFirstStartPostDetails(Context context) {
        getSharedPreferences(context).edit().putBoolean(FIRST_START_POST_DETAILS, false).apply();
    }

    public static boolean isFirstStartPostDetails(Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_START_POST_DETAILS, true);
    }

    public static void setFirstStartMyInterests(Context context) {
        getSharedPreferences(context).edit().putBoolean(FIRST_START_MY_INTERESTS, false).apply();
    }

    public static boolean isFirstStartMyInterests(Context context) {
        return getSharedPreferences(context).getBoolean(FIRST_START_MY_INTERESTS, true);
    }

}
