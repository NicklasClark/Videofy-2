package com.cncoding.teazer.data.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * Created by Prem$ on 2/9/2018.
 */

public class ResetPasswordByOtp extends BaseAuth {

    @SerializedName("new_password") @Expose private String newPassword;
    @SerializedName("email") @Expose private String email;
    @SerializedName("phone_number") @Expose private Long phoneNumber;
    @SerializedName("country_code") @Expose private Integer countryCode;
    @SerializedName("otp") @Expose private Integer otp;
    private Throwable error;

    public ResetPasswordByOtp(String newPassword, String email, Long phoneNumber, Integer countryCode, Integer otp) {
        this.newPassword = newPassword;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.otp = otp;
    }

    public ResetPasswordByOtp(Throwable error) {
        this.error = error;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public ResetPasswordByOtp setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ResetPasswordByOtp setEmail(String email) {
        this.email = email;
        return this;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public ResetPasswordByOtp setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public ResetPasswordByOtp setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public Integer getOtp() {
        return otp;
    }

    public ResetPasswordByOtp setOtp(Integer otp) {
        this.otp = otp;
        return this;
    }

    public Throwable getError() {
        return error;
    }
}
