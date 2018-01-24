package com.cncoding.teazer.data.model.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class UpdateProfile implements Parcelable {
    private String first_name;
    private String last_name;
    private String email;
    private long phone_number;
    private int country_code;

    public UpdateProfile(String first_name, String last_name, String email, long phone_number, int country_code) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone_number = phone_number;
        this.country_code = country_code;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public int getCountry_code() {
        return country_code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(email);
        dest.writeLong(phone_number);
        dest.writeInt(country_code);
    }

    protected UpdateProfile(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        phone_number = in.readLong();
        country_code = in.readInt();
    }

    public static final Creator<UpdateProfile> CREATOR = new Creator<UpdateProfile>() {
        @Override
        public UpdateProfile createFromParcel(Parcel in) {
            return new UpdateProfile(in);
        }

        @Override
        public UpdateProfile[] newArray(int size) {
            return new UpdateProfile[size];
        }
    };
}