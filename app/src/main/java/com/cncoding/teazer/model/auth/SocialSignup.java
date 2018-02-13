package com.cncoding.teazer.model.auth;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.cncoding.teazer.MainActivity.DEVICE_TYPE_ANDROID;
import static com.cncoding.teazer.utilities.AuthUtils.getDeviceId;
import static com.cncoding.teazer.utilities.AuthUtils.getFcmToken;

/**
 *
 * Created by Prem$ on 2/9/2018.
 */

public class SocialSignup extends BaseAuth {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SOCIAL_LOGIN_TYPE_GOOGLE, SOCIAL_LOGIN_TYPE_FACEBOOK})
    public @interface SocialLoginType {}

    public static final int SOCIAL_LOGIN_TYPE_FACEBOOK = 1;
    public static final int SOCIAL_LOGIN_TYPE_GOOGLE = 2;

    @SerializedName("fcm_token") @Expose private String fcmToken;
    @SerializedName("device_id") @Expose private String deviceId;
    @SerializedName("device_type") @Expose private int deviceType;
    @SerializedName("social_id") @Expose private String socialId;
    @SocialLoginType @SerializedName("social_login_type") @Expose private int socialLoginType;
    @SerializedName("email") @Expose private String email;
    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("first_name") @Expose private String firstName;
    @SerializedName("last_name") @Expose private String lastName;
    private String imageUrl;
    private Throwable error;

    public SocialSignup() {
    }

    public String getFCMToken() {
        return fcmToken;
    }

    public SocialSignup setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }

    public String getDeviceID() {
        return deviceId;
    }

    public SocialSignup setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public SocialSignup setDeviceType(int deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getSocialId() {
        return socialId;
    }

    public SocialSignup setSocialId(String socialId) {
        this.socialId = socialId;
        return this;
    }

    @SocialLoginType public int getSocialLoginType() {
        return socialLoginType;
    }

    public SocialSignup setSocialLoginType(@SocialLoginType int socialLoginType) {
        this.socialLoginType = socialLoginType;
        return this;
    }
    public String getEmail() {
        return email;
    }

    public SocialSignup setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public SocialSignup setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public SocialSignup setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public SocialSignup setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Throwable getError() {
        return error;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public SocialSignup setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void extractFromFacebookData(Profile facebookProfile, Bundle facebookData, Context context) {
        setFcmToken(getFcmToken(context))
                .setDeviceId(getDeviceId(context))
                .setDeviceType(DEVICE_TYPE_ANDROID)
                .setSocialId(facebookProfile.getId())
                .setSocialLoginType(SOCIAL_LOGIN_TYPE_FACEBOOK)
                .setEmail(facebookData.getString("email"))
                .setUserName(facebookProfile.getName().replace(" ", ""))
                .setFirstName(facebookProfile.getFirstName())
                .setLastName(facebookProfile.getLastName())
                .setImageUrl(facebookData.getString("profile_pic"));
    }

    public void extractFromGoogleData(GoogleSignInAccount account, Context context) {
        setFcmToken(getFcmToken(context))
                .setDeviceId(getDeviceId(context))
                .setDeviceType(DEVICE_TYPE_ANDROID)
                .setSocialId(account.getId())
                .setSocialLoginType(SocialSignup.SOCIAL_LOGIN_TYPE_GOOGLE)
                .setEmail(account.getEmail())
                .setUserName(account.getDisplayName() != null ? account.getDisplayName().replace(" ", "") :
                        account.getGivenName() + account.getFamilyName())
                .setFirstName(account.getGivenName())
                .setLastName(account.getFamilyName())
                .setImageUrl(account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : null);
    }
}
