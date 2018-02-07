package com.cncoding.teazer.model.updatemobilenumber;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public class UpdateMobileNumber {

    @SerializedName("phone_number") @Expose private Long phoneNumber;
    @SerializedName("country_code") @Expose private Integer countryCode;
    @SerializedName("otp") @Expose private Integer otp;

    public Long getPhoneNumber() {
        return phoneNumber;

    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getOtp() {
        return otp;
    }

    public UpdateMobileNumber(Long phoneNumber, Integer countryCode, Integer otp) {

        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
        this.otp = otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }
}
