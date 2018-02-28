package com.cncoding.teazer.data.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class Authorize implements Parcelable {
    private String user_name;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String new_password;
    private long phone_number;
    private int country_code;
    private int otp;
    private String fcm_token;
    private String device_id;
    private int device_type;
    private String social_id;
    private int social_login_type;
    private String image_url;

    /**Proceed to second signup page after entering Username and password in the first page**/
    public Authorize(String username, String pass) {
        this.user_name = username;
        this.password = pass;
    }

    /**SignUp step 1**/
    public Authorize(String user_name, String first_name, String last_name, String email,
                     String password, long phone_number, int country_code) {
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.country_code = country_code;
    }

    /**SignUp step 2**/
    public Authorize(String user_name, String first_name, String last_name, String email,
                     String password, long phone_number, int country_code, int otp,
                     String fcm_token, String device_id, int device_type) {
        this.fcm_token = fcm_token;
        this.device_id = device_id;
        this.device_type = device_type;
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.country_code = country_code;
        this.otp = otp;
    }

    /**Login through Username/Email/Phone number and password**/
    public Authorize(String fcmToken, String deviceId, int deviceType, String username, String password) {
        this.fcm_token = fcmToken;
        this.device_id = deviceId;
        this.device_type = deviceType;
        this.user_name = username;
        this.password = password;
    }

    /**
     * Method user in first step of login through OTP,
     * OR, to reset the password by phone number, from forgot password.
     **/
    public Authorize(long phone_number, int country_code) {
        this.phone_number = phone_number;
        this.country_code = country_code;
    }

    /**
     * Second step of login through OTP.
     **/
    public Authorize(String fcmToken, String deviceId, int deviceType, long phone_number, int country_code, int otp) {
        this.fcm_token = fcmToken;
        this.device_id = deviceId;
        this.device_type = deviceType;
        this.phone_number = phone_number;
        this.country_code = country_code;
        this.otp = otp;
    }

    /**Forgot password constructor**/
    public Authorize(String user_name) {
        this.user_name = user_name;
    }

    /**
     * Reset the password by OTP received in email or phone number.
     * Send either
     * @param email or
     * @param phone_number and
     * @param country_code along with OTP and New password.
     **/
    public Authorize(String new_password, String email, long phone_number, int country_code, int otp) {
        this.new_password = new_password;
        this.email = email;
        this.phone_number = phone_number;
        this.country_code = country_code;
        this.otp = otp;
    }

    /**Social SignUp**/
    public Authorize(String fcm_token, String device_id, int device_type, String social_id,
                     int social_login_type, String email, String user_name, String first_name, String last_name, String imageUrl) {
        this.fcm_token = fcm_token;
        this.device_id = device_id;
        this.device_type = device_type;
        this.social_id = social_id;
        this.social_login_type = social_login_type;
        this.email = email;
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.image_url = imageUrl;
    }

    protected Authorize(Parcel in) {
        user_name = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        password = in.readString();
        new_password = in.readString();
        phone_number = in.readLong();
        country_code = in.readInt();
        otp = in.readInt();
        fcm_token = in.readString();
        device_id = in.readString();
        device_type = in.readInt();
        social_id = in.readString();
        social_login_type = in.readInt();
        image_url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_name);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(new_password);
        dest.writeLong(phone_number);
        dest.writeInt(country_code);
        dest.writeInt(otp);
        dest.writeString(fcm_token);
        dest.writeString(device_id);
        dest.writeInt(device_type);
        dest.writeString(social_id);
        dest.writeInt(social_login_type);
        dest.writeString(image_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Authorize> CREATOR = new Creator<Authorize>() {
        @Override
        public Authorize createFromParcel(Parcel in) {
            return new Authorize(in);
        }

        @Override
        public Authorize[] newArray(int size) {
            return new Authorize[size];
        }
    };

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public long getPhoneNumber() {
        return phone_number;
    }

    public int getCountryCode() {
        return country_code;
    }

    public String getUsername() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }

    public int getOtp() {
        return otp;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public String getDevice_id() {
        return device_id;
    }

    public int getDevice_type() {
        return device_type;
    }

    public String getSocial_id() {
        return social_id;
    }

    public int getSocial_login_type() {
        return social_login_type;
    }

    public String getNew_password() {
        return new_password;
    }

    public String getImageUrl() {
        return image_url;
    }

}