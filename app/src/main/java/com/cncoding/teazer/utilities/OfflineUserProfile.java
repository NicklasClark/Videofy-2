package com.cncoding.teazer.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.cncoding.teazer.utilities.Pojos.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 10/5/2017.
 */

public class OfflineUserProfile {

    public static final String TEAZER = "teazer_preferences";
    private static final String USER_ID = "id";
    private static final String USER_NAME = "username";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String COUNTRY_CODE = "country_code";
    private static final String PASSWORD = "password";
    private static final String IS_ACTIVE = "isActive";
    private static final String PROFILE_PIC_URI = "profile_pic_uri";
    private static final String ACCOUNT_TYPE = "accountType";
    private static final String CREATED_AT = "createdAt";
    private static final String UPDATED_AT = "updatedAt";
    private static final String HAS_PROFILE_MEDIA = "hasProfileMedia";
    private static final String PROFILE_MEDIA = "profileMedia";
    private static final String CATEGORIES = "categories";
    private static final String FOLLOWERS = "followers";
    private static final String FOLLOWINGS = "followings";
    private static final String TOTAL_VIDEOS = "totalVideos";
    private static final String GENDER = "gender";
    private static final String DETAIL = "detail";

    private final SharedPreferences sharedPreferences;

    public OfflineUserProfile(Context context) {
        sharedPreferences = context.getSharedPreferences(TEAZER, Context.MODE_PRIVATE);
    }
    
    public static void saveUserProfileOffline(Pojos.User.Profile userProfile, Context context) {
//        new OfflineUserProfile(context)
//                .setUserId(userProfile.getPublicProfile().getUserId())
//                .setUsername(userProfile.getPublicProfile().getUsername())
//                .setFirstName(userProfile.getPublicProfile().getFirstName())
//                .setLastName(userProfile.getPublicProfile().getLastName())
//                .setEmail(userProfile.getPublicProfile().getEmail())
//                .setPhoneNumber(userProfile.getPublicProfile().getPhoneNumber())
//                .setCountryCode(userProfile.getPublicProfile().getCountryCode())
//                .setPassword(userProfile.getPublicProfile().getPassword())
//                .setIsActive(userProfile.getPublicProfile().isActive())
//                .setAccountType(userProfile.getPublicProfile().getAccountType())
//                .setCreatedAt(userProfile.getPublicProfile().getCreatedAt())
//                .setUpdatedAt(userProfile.getPublicProfile().getUpdatedAt())
//                .setHasProfileMedia(userProfile.getPublicProfile().hasProfileMedia())
//                .setProfileMedia(userProfile.getPublicProfile().getProfileMedia())
//                .setCategories(userProfile.getPublicProfile().getCategories())
//                .setFollowers(userProfile.getFollowers())
//                .setFollowings(userProfile.getFollowings())
//                .setTotalVideos(userProfile.getTotalVideos());
    }
    
//    public PublicProfile getOfflineUserProfile() {
//        return new PublicProfile(
//                getUserId(),
//                getUsername(),
//                getFirstName(),
//                getLastName(),
//                getEmail(),
//                getPhoneNumber(),
//                getCountryCode(),
//                getPassword(),
//                isActive(),
//                getAccountType(),
//                getCreatedAt(),
//                getUpdatedAt(),
//                hasProfileMedia(),
//                getProfileMedia(),
//                getCategories(),
//                getFollowers(),
//                getFollowings(),
//                getTotalVideos(),
//                getGender()
//                ,getDetail());
//
//    }
//    public PublicProfile getOfflineUserProfile() {
//        return new PublicProfile(
//                getUserId(),
//                getUsername(),
//                getFirstName(),
//                getLastName(),
//                getEmail(),
//                getPhoneNumber(),
//                getCountryCode(),
//                getPassword(),
//                isActive(),
//                getAccountType(),
//                getCreatedAt(),
//                getUpdatedAt(),
//                hasProfileMedia(),
//                getProfileMedia(),
//                getCategories(),
//                getFollowers(),
//                getFollowings(),
//                getTotalVideos());
//    }

    private String getString(String id) {
        return sharedPreferences.getString(id, null);
    }

    private void putString(String id, String value) {
        sharedPreferences.edit().putString(id, value).apply();
    }

    private int getInt(String id) {
        return sharedPreferences.getInt(id, -1);
    }

    private void putInt(String id, int value) {
        sharedPreferences.edit().putInt(id, value).apply();
    }

    private boolean getBoolean(String id) {
        return sharedPreferences.getBoolean(id, false);
    }

    private void putBoolean(String id, boolean value) {
        sharedPreferences.edit().putBoolean(id, value).apply();
    }

//    public OfflineUserProfile reset() {
//        putString(USER_ID, "");
//        putString(USER_NAME, "");
//        putString(FIRST_NAME, "");
//        putString(LAST_NAME, "");
//        putString(EMAIL, "");
//        putString(PROFILE_PIC_URI, "");
//        return this;
//    }

    public String getUserId() {
        return getString(USER_ID);
    }

    public String getUsername() {
        return getString(USER_NAME);
    }

    public String getFirstName() {
        return getString(FIRST_NAME);
    }

    public String getLastName() {
        return getString(LAST_NAME);
    }

    public String getEmail() {
        return getString(EMAIL);
    }

    public int getCountryCode() {
        return getInt(COUNTRY_CODE);
    }

    public long getPhoneNumber() {
        return sharedPreferences.getLong(PHONE_NUMBER, -1);
    }

    public boolean isActive() {
        return getBoolean(IS_ACTIVE);
    }

    public String getPassword() {
        return getString(PASSWORD);
    }

    public String getProfilePicUri() {
        return getString(PROFILE_PIC_URI);
    }

    public int getAccountType() {
        return getInt(ACCOUNT_TYPE);
    }

    public String getCreatedAt() {
        return getString(CREATED_AT);
    }

    public String getUpdatedAt() {
        return getString(UPDATED_AT);
    }

    public int getGender() {
        return getInt(GENDER);
    }
    public String getDetail()
    {
        return getString(DETAIL);
    }



    public boolean hasProfileMedia() {
        return sharedPreferences.getBoolean(HAS_PROFILE_MEDIA, false);
    }

    public Pojos.ProfileMedia getProfileMedia() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(PROFILE_MEDIA, "");
        return gson.fromJson(json, Pojos.ProfileMedia.class);
    }

    public ArrayList<Category> getCategories() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CATEGORIES, "");
        return gson.fromJson(json, new TypeToken<ArrayList<Category>>(){}.getType());
    }

    public int getFollowers() {
        return getInt(FOLLOWERS);
    }

    public int getFollowings() {
        return getInt(FOLLOWINGS);
    }

    public int getTotalVideos() {
        return getInt(TOTAL_VIDEOS);
    }

    public OfflineUserProfile setUserId(String id) {
        putString(USER_ID, id);
        return this;
    }

    public OfflineUserProfile setUsername(String name) {
        putString(USER_NAME, name);
        return this;
    }

    public OfflineUserProfile setFirstName(String firstName) {
        putString(FIRST_NAME, firstName);
        return this;
    }

    public OfflineUserProfile setLastName(String lastName) {
        putString(LAST_NAME, lastName);
        return this;
    }

    public OfflineUserProfile setEmail(String email) {
        putString(EMAIL, email);
        return this;
    }

    public OfflineUserProfile setCountryCode(int countryCode) {
        putInt(COUNTRY_CODE, countryCode);
        return this;
    }

    public OfflineUserProfile setPhoneNumber(long phoneNumber) {
        sharedPreferences.edit().putLong(PHONE_NUMBER, phoneNumber).apply();
        return this;
    }

    public OfflineUserProfile setPassword(String password) {
        putString(PASSWORD, password);
        return this;
    }

    public OfflineUserProfile setIsActive(boolean isActive) {
        putBoolean(IS_ACTIVE, isActive);
        return this;
    }

    public OfflineUserProfile setProfilePicUri(Uri profilePicUri) {
        putString(PROFILE_PIC_URI, profilePicUri.toString());
        return this;
    }

    public OfflineUserProfile setAccountType(int account_type) {
        putInt(ACCOUNT_TYPE, account_type);
        return this;
    }

    public OfflineUserProfile setCreatedAt(String created_at) {
        putString(CREATED_AT, created_at);
        return this;
    }

    public OfflineUserProfile setUpdatedAt(String updated_at) {
        putString(UPDATED_AT, updated_at);
        return this;
    }

    public OfflineUserProfile setHasProfileMedia(boolean has_profile_media) {
        putBoolean(HAS_PROFILE_MEDIA, has_profile_media);
        return this;
    }

    public OfflineUserProfile setProfileMedia(Pojos.ProfileMedia profile_media) {
        sharedPreferences
                .edit()
                .putString(PROFILE_MEDIA, new Gson().toJson(profile_media, Pojos.ProfileMedia.class))
                .apply();
        return this;
    }

    public OfflineUserProfile setCategories(ArrayList<Category> categories) {
        sharedPreferences
                .edit()
                .putString(CATEGORIES, new Gson().toJson(categories, new TypeToken<ArrayList<Category>>(){}.getType()))
                .apply();
        return this;
    }

    public OfflineUserProfile setFollowers(int followers) {
        putInt(FOLLOWERS, followers);
        return this;
    }

    public OfflineUserProfile setFollowings(int followings) {
        putInt(FOLLOWINGS, followings);
        return this;
    }

    public OfflineUserProfile setTotalVideos(int total_videos) {
        putInt(TOTAL_VIDEOS, total_videos);
        return this;
    }
    public OfflineUserProfile setGender(int gender) {
        putInt(GENDER, gender);
        return this;
    }
    public OfflineUserProfile setDetail(String detail) {
        putString(DETAIL, detail);
        return this;
    }
}