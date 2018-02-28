package com.cncoding.teazer.data.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem$ on 2/9/2018.
 */

public class ProceedSignup implements Parcelable {

    private String userName;
    private String password;

    public ProceedSignup(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public ProceedSignup() {
    }

    protected ProceedSignup(Parcel in) {
        userName = in.readString();
        password = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProceedSignup> CREATOR = new Creator<ProceedSignup>() {
        @Override
        public ProceedSignup createFromParcel(Parcel in) {
            return new ProceedSignup(in);
        }

        @Override
        public ProceedSignup[] newArray(int size) {
            return new ProceedSignup[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
