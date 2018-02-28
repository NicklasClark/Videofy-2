package com.cncoding.teazer.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem$ on 2/9/2018.
 */

public class VerifySignUp extends BaseAuth implements Parcelable {

    @SerializedName("fcm_token") @Expose private String fcmToken;
    @SerializedName("device_id") @Expose private String deviceId;
    @SerializedName("device_type") @Expose private int deviceType;
    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("first_name") @Expose private String firstName;
    @SerializedName("last_name") @Expose private String lastName;
    @SerializedName("email") @Expose private String email;
    @SerializedName("password") @Expose private String password;
    @SerializedName("phone_number") @Expose private long phoneNumber;
    @SerializedName("country_code") @Expose private int countryCode;
    @SerializedName("otp") @Expose private int otp;
    private Throwable error;

    public VerifySignUp(String fcmToken, String deviceId, int deviceType, String userName, String firstName,
                        String lastName, String email, String password, long phoneNumber, int countryCode, int otp) {
        this.fcmToken = fcmToken;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.otp = otp;
    }

    public VerifySignUp(Throwable error) {
        this.error = error;
    }

    public VerifySignUp() {
    }

    protected VerifySignUp(Parcel in) {
        fcmToken = in.readString();
        deviceId = in.readString();
        deviceType = in.readInt();
        userName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        password = in.readString();
        phoneNumber = in.readLong();
        countryCode = in.readInt();
        otp = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fcmToken);
        dest.writeString(deviceId);
        dest.writeInt(deviceType);
        dest.writeString(userName);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeLong(phoneNumber);
        dest.writeInt(countryCode);
        dest.writeInt(otp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VerifySignUp> CREATOR = new Creator<VerifySignUp>() {
        @Override
        public VerifySignUp createFromParcel(Parcel in) {
            return new VerifySignUp(in);
        }

        @Override
        public VerifySignUp[] newArray(int size) {
            return new VerifySignUp[size];
        }
    };

    public String getFcmToken() {
        return fcmToken;
    }

    public VerifySignUp setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public VerifySignUp setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public VerifySignUp setDeviceType(int deviceType) {
        this.deviceType = deviceType;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public VerifySignUp setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public VerifySignUp setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public VerifySignUp setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public VerifySignUp setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public VerifySignUp setPassword(String password) {
        this.password = password;
        return this;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public VerifySignUp setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public VerifySignUp setCountryCode(int countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public int getOtp() {
        return otp;
    }

    public VerifySignUp setOtp(int otp) {
        this.otp = otp;
        return this;
    }

    public Throwable getError() {
        return error;
    }

    public VerifySignUp setInitialSignup(InitiateSignup initialSignup) {
        this.userName = initialSignup.getUserName();
        this.firstName = initialSignup.getFirstName();
        this.lastName = initialSignup.getLastName();
        this.email = initialSignup.getEmail();
        this.password = initialSignup.getPassword();
        this.phoneNumber = initialSignup.getPhoneNumber();
        this.countryCode = initialSignup.getCountryCode();
        return this;
    }
}
