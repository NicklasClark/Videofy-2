package com.cncoding.teazer.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * Created by Prem$ on 2/9/2018.
 */

public class InitiateLoginWithOtp extends BaseAuth implements Parcelable {
    
    @SerializedName("phone_number") @Expose private long phoneNumber;
    @SerializedName("country_code") @Expose private int countryCode;
    private Throwable error;

    public InitiateLoginWithOtp(long phoneNumber, int countryCode) {
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
    }

    public InitiateLoginWithOtp(Throwable error) {
        this.error = error;
    }

    protected InitiateLoginWithOtp(Parcel in) {
        phoneNumber = in.readLong();
        countryCode = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(phoneNumber);
        dest.writeInt(countryCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InitiateLoginWithOtp> CREATOR = new Creator<InitiateLoginWithOtp>() {
        @Override
        public InitiateLoginWithOtp createFromParcel(Parcel in) {
            return new InitiateLoginWithOtp(in);
        }

        @Override
        public InitiateLoginWithOtp[] newArray(int size) {
            return new InitiateLoginWithOtp[size];
        }
    };

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public InitiateLoginWithOtp setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public InitiateLoginWithOtp setCountryCode(int countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public Throwable getError() {
        return error;
    }
}
