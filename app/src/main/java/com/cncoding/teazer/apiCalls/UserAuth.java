package com.cncoding.teazer.apiCalls;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 10/9/2017.
 */

public class UserAuth {

    public static class SignUp implements Parcelable {
        private String user_name;
        private String first_name;
        private String last_name;
        private String email;
        private String password;
        private long phone_number;
        private int country_code;

        SignUp(Parcel in) {
            user_name = in.readString();
            first_name = in.readString();
            last_name = in.readString();
            email = in.readString();
            password = in.readString();
            phone_number = in.readLong();
            country_code = in.readInt();
        }

        public static final Creator<SignUp> CREATOR = new Creator<SignUp>() {
            @Override
            public SignUp createFromParcel(Parcel in) {
                return new SignUp(in);
            }

            @Override
            public SignUp[] newArray(int size) {
                return new SignUp[size];
            }
        };

        /**
         * Forgot password constructor
         * */
        public SignUp(String user_name) {
            this.user_name = user_name;
        }

        /**
         * Constructor for logging in through username and password
         * */
        public SignUp(String username, String password) {
            this.user_name = username;
            this.password = password;
        }

        /**
         * Constructor for logging in through OTP
         * */
        public SignUp(long phone_number, int country_code) {
            this.phone_number = phone_number;
            this.country_code = country_code;
        }

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

        /**
         * SignUp step 1
         * */
        public SignUp(String user_name, String first_name, String last_name, String email,
                      String password, long phone_number, int country_code) {
            this.user_name = user_name;
            this.first_name = first_name;
            this.last_name = last_name;
            this.email = email;
            this.password = password;
            this.phone_number = phone_number;
            this.country_code = country_code;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(user_name);
            parcel.writeString(first_name);
            parcel.writeString(last_name);
            parcel.writeString(email);
            parcel.writeString(password);
            parcel.writeLong(phone_number);
            parcel.writeInt(country_code);
        }

        public static class Verify {
            private String user_name;
            private String first_name;
            private String last_name;
            private String email;
            private String password;
            private long phone_number;
            private int country_code;
            private int otp;
            private String fcm_token;
            private String device_id;
            private int device_type;

            /**
             * SignUp step 2
             * */
            public Verify(String user_name, String first_name, String last_name, String email,
                          String password, long phone_number, int country_code, int otp,
                          String fcm_token, String device_id, int device_type) {
                this.user_name = user_name;
                this.first_name = first_name;
                this.last_name = last_name;
                this.email = email;
                this.password = password;
                this.phone_number = phone_number;
                this.country_code = country_code;
                this.otp = otp;
                this.fcm_token = fcm_token;
                this.device_id = device_id;
                this.device_type = device_type;
            }

            public String getPassword() {
                return password;
            }

            public String getEmail() {
                return email;
            }

            public String getFirstName() {
                return first_name;
            }

            public String getLastName() {
                return last_name;
            }

            public String getUsername() {
                return user_name;
            }

            public int getCountryCode() {
                return country_code;
            }

            public long getPhoneNumber() {
                return phone_number;
            }
        }

        public static class Social {
            private String fcm_token;
            private String device_id;
            private int device_type;
            private String social_id;
            private int social_login_type;
            private String email;
            private String user_name;
            private String first_name;
            private String last_name;

            public Social(String fcm_token, String device_id, int device_type, String social_id,
                          int social_login_type, String email, String user_name, String first_name, String last_name) {
                this.fcm_token = fcm_token;
                this.device_id = device_id;
                this.device_type = device_type;
                this.social_id = social_id;
                this.social_login_type = social_login_type;
                this.email = email;
                this.user_name = user_name;
                this.first_name = first_name;
                this.last_name = last_name;
            }
        }
    }

    public static class LoginWithPassword {
        private String user_name;
        private String password;
        private String fcm_token;
        private String device_id;
        private int device_type;

        public LoginWithPassword(String user_name, String password, String fcm_token, String device_id, int device_type) {
            this.user_name = user_name;
            this.password = password;
            this.fcm_token = fcm_token;
            this.device_id = device_id;
            this.device_type = device_type;
        }
    }

    public static class PhoneNumberDetails {
        private long phone_number;
        private int country_code;

        /**
         * SignIn with OTP step 1
         * */
        public PhoneNumberDetails(long phone_number, int country_code) {
            this.phone_number = phone_number;
            this.country_code = country_code;
        }

        public static class Verify {
            private long phone_number;
            private int country_code;
            private int otp;
            private String fcm_token;
            private String device_id;
            private int device_type;

            /**
             * SignIn with OTP step 2
             * */
            public Verify(long phone_number, int country_code, int otp, String fcm_token, String device_id, int device_type) {
                this.phone_number = phone_number;
                this.country_code = country_code;
                this.otp = otp;
                this.fcm_token = fcm_token;
                this.device_id = device_id;
                this.device_type = device_type;
            }
        }

        public static class ResetPassword {

        }
    }

    public static class ResetPasswordDetails {
        private String new_password;
        private String email;
        private long phone_number;
        private int country_code;
        private int otp;

        public ResetPasswordDetails(String new_password, String email, long phone_number, int country_code, int otp) {
            this.new_password = new_password;
            this.email = email;
            this.phone_number = phone_number;
            this.country_code = country_code;
            this.otp = otp;
        }
    }
}
