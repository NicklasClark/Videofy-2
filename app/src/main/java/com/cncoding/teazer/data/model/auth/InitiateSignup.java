package com.cncoding.teazer.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem$ on 2/9/2018.
 */

public class InitiateSignup extends BaseAuth implements Parcelable {

    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("first_name") @Expose private String firstName;
    @SerializedName("last_name") @Expose private String lastName;
    @SerializedName("email") @Expose private String email;
    @SerializedName("password") @Expose private String password;
    @SerializedName("phone_number") @Expose private long phoneNumber;
    @SerializedName("country_code") @Expose private int countryCode;
    private Throwable error;

    public InitiateSignup(String userName, String firstName, String lastName, String email, String password, long phoneNumber, int countryCode) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.countryCode = countryCode;
    }

    public InitiateSignup(Throwable error) {
        this.error = error;
    }

    public InitiateSignup() {
    }

    protected InitiateSignup(Parcel in) {
        userName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        password = in.readString();
        phoneNumber = in.readLong();
        countryCode = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeLong(phoneNumber);
        dest.writeInt(countryCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InitiateSignup> CREATOR = new Creator<InitiateSignup>() {
        @Override
        public InitiateSignup createFromParcel(Parcel in) {
            return new InitiateSignup(in);
        }

        @Override
        public InitiateSignup[] newArray(int size) {
            return new InitiateSignup[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public InitiateSignup setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public InitiateSignup setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public InitiateSignup setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public InitiateSignup setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public InitiateSignup setPassword(String password) {
        this.password = password;
        return this;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public InitiateSignup setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public InitiateSignup setCountryCode(int countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public Throwable getError() {
        return error;
    }

    public void setEmailOnly(String email) {
        this.email = email;
    }

    public void setPhoneNumberOnly(String phoneNumber) {
        this.phoneNumber = Long.parseLong(phoneNumber);
    }

    public void setCountryCodeOnly(int countryCode) {
        this.countryCode = countryCode;
    }

    public void setFirstAndLastNames(String[] name) {
        if (name.length > 0) setFirstName(name[0]);
        if (name.length > 1) setLastName(name[1]);
    }
    
    public InitiateSignup extractFromVerifySignUp(VerifySignUp verifySignUp) {
        userName = verifySignUp.getUserName();
        firstName = verifySignUp.getFirstName();
        lastName = verifySignUp.getLastName();
        email = verifySignUp.getEmail();
        password = verifySignUp.getPassword();
        phoneNumber = verifySignUp.getPhoneNumber();
        countryCode = verifySignUp.getCountryCode();
        return this;
    }
}
