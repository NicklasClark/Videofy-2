package com.cncoding.teazer.model.updatemobilenumber;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by farazhabib on 16/12/17.
 */

public class ChangeMobileNumber {

    @SerializedName("phone_number")
    @Expose
    private Long phoneNumber;

    public ChangeMobileNumber(Long phoneNumber, Integer countryCode) {
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
    }

    @SerializedName("country_code")

    @Expose
    private Integer countryCode;


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
}
