package com.cncoding.teazer.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem$ on 2/9/2018.
 */

public class Login extends BaseAuth {
   
    @SerializedName("fcm_token") @Expose private String fcmToken;
    @SerializedName("device_id") @Expose private String deviceId;
    @SerializedName("device_type") @Expose private int deviceType;
    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("password") @Expose private String password;
    private Throwable error;

    public Login(String fcmToken, String deviceId, int deviceType, String userName, String password) {
        this.fcmToken = fcmToken;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.userName = userName;
        this.password = password;
    }

    public Login(Throwable error) {
        this.error = error;
    }

    public Login() {
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public Login setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public Login setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public Login setDeviceType(int deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public Login setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserNameOnly(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public Login setPassword(String password) {
        this.password = password;
        return this;
    }

    public void setPasswordOnly(String password) {
        this.password = password;
    }

    public Throwable getError() {
        return error;
    }
}
