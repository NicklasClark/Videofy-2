package com.cncoding.teazer.data.model.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class UpdatePassword implements Parcelable {
    private String old_password;
    private String new_password;

    public UpdatePassword(String old_password, String new_password) {
        this.old_password = old_password;
        this.new_password = new_password;
    }

    public String getOld_password() {
        return old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(old_password);
        parcel.writeString(new_password);
    }

    protected UpdatePassword(Parcel in) {
        old_password = in.readString();
        new_password = in.readString();
    }

    public static final Creator<UpdatePassword> CREATOR = new Creator<UpdatePassword>() {
        @Override
        public UpdatePassword createFromParcel(Parcel in) {
            return new UpdatePassword(in);
        }

        @Override
        public UpdatePassword[] newArray(int size) {
            return new UpdatePassword[size];
        }
    };
}