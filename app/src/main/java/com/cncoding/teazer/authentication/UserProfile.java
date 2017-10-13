package com.cncoding.teazer.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 *
 * Created by Prem $ on 10/5/2017.
 */

public class UserProfile {

    public static final String TEAZER = "teazer_preferences";
    private static final String ID = "id";
    private static final String USER_NAME = "username";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String COUNTRY_CODE = "country_code";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String PASSWORD = "password";
    private static final String PROFILE_PIC_URI = "profile_pic_uri";
    
    private SharedPreferences sharedPreferences;

    public UserProfile(Context context) {
        sharedPreferences = context.getSharedPreferences(TEAZER, Context.MODE_PRIVATE);
    }

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

    private long getLong(String id) {
        return sharedPreferences.getLong(id, -1);
    }

    private void putLong(String id, long value) {
        sharedPreferences.edit().putLong(id, value).apply();
    }

    public String getId() {
        return getString(ID);
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
        return getLong(PHONE_NUMBER);
    }

    public String getPassword() {
        return getString(PASSWORD);
    }

    public String getProfilePicUri() {
        return getString(PROFILE_PIC_URI);
    }

    public UserProfile reset() {
        putString(ID, "");
        putString(USER_NAME, "");
        putString(FIRST_NAME, "");
        putString(LAST_NAME, "");
        putString(EMAIL, "");
        putString(PROFILE_PIC_URI, "");
        return this;
    }

    public UserProfile setId(String id) {
        putString(ID, id);
        return this;
    }

    public UserProfile setUsername(String name) {
        putString(USER_NAME, name);
        return this;
    }

    public UserProfile setFirstName(String firstName) {
        putString(FIRST_NAME, firstName);
        return this;
    }

    public UserProfile setLastName(String lastName) {
        putString(LAST_NAME, lastName);
        return this;
    }

    public UserProfile setEmail(String email) {
        putString(EMAIL, email);
        return this;
    }

    public UserProfile setCountryCode(int countryCode) {
        putInt(COUNTRY_CODE, countryCode);
        return this;
    }

    public UserProfile setPhoneNumber(long phoneNumber) {
        putLong(PHONE_NUMBER, phoneNumber);
        return this;
    }

    public UserProfile setPassword(String password) {
        putString(PASSWORD, password);
        return this;
    }

    public UserProfile setProfilePicUri(Uri profilePicUri) {
        putString(PROFILE_PIC_URI, profilePicUri.toString());
        return this;
    }
}
