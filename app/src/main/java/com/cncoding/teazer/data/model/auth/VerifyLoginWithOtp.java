package com.cncoding.teazer.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * Created by Prem$ on 2/9/2018.
 */

public class VerifyLoginWithOtp extends BaseAuth implements Parcelable {

    @SerializedName("fcm_token") @Expose private String fcmToken;
    @SerializedName("device_id") @Expose private String deviceId;
    @SerializedName("device_type") @Expose private int deviceType;
    @SerializedName("phone_number") @Expose private long phoneNumber;
    @SerializedName("country_code") @Expose private int countryCode;
    @SerializedName("otp") @Expose private int otp;
    private Throwable error;

    public VerifyLoginWithOtp(String fcmToken, String deviceId, int deviceType, long phoneNumber, int countryCode, int otp) {
        this.fcmToken = fcmToken;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.otp = otp;
    }

    public VerifyLoginWithOtp(Throwable error) {
        this.error = error;
    }

    protected VerifyLoginWithOtp(Parcel in) {
        fcmToken = in.readString();
        deviceId = in.readString();
        deviceType = in.readInt();
        phoneNumber = in.readLong();
        countryCode = in.readInt();
        otp = in.readInt();
    }

    public VerifyLoginWithOtp() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fcmToken);
        dest.writeString(deviceId);
        dest.writeInt(deviceType);
        dest.writeLong(phoneNumber);
        dest.writeInt(countryCode);
        dest.writeInt(otp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VerifyLoginWithOtp> CREATOR = new Creator<VerifyLoginWithOtp>() {
        @Override
        public VerifyLoginWithOtp createFromParcel(Parcel in) {
            return new VerifyLoginWithOtp(in);
        }

        @Override
        public VerifyLoginWithOtp[] newArray(int size) {
            return new VerifyLoginWithOtp[size];
        }
    };

    public String getFcmToken() {
        return fcmToken;
    }

    public VerifyLoginWithOtp setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public VerifyLoginWithOtp setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public VerifyLoginWithOtp setDeviceType(int deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public VerifyLoginWithOtp setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public VerifyLoginWithOtp setCountryCode(int countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public int getOtp() {
        return otp;
    }

    public VerifyLoginWithOtp setOtp(int otp) {
        this.otp = otp;
        return this;
    }

    public Throwable getError() {
        return error;
    }
}
