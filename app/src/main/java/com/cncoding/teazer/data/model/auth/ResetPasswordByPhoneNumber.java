package com.cncoding.teazer.data.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * Created by Prem$ on 2/9/2018.
 */

public class ResetPasswordByPhoneNumber extends BaseAuth {

    @SerializedName("phone_number") @Expose private long phoneNumber;
    @SerializedName("country_code") @Expose private int countryCode;
    private Throwable error;

    public ResetPasswordByPhoneNumber(long phoneNumber, int countryCode) {
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
    }

    public ResetPasswordByPhoneNumber(Throwable error) {
        this.error = error;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public ResetPasswordByPhoneNumber setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public ResetPasswordByPhoneNumber setCountryCode(int countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public Throwable getError() {
        return error;
    }
}
