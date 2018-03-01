package com.cncoding.teazer.data.model.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class UpdateCategories implements Parcelable {
    private String categories;

    public UpdateCategories(String categories) {
        this.categories = categories;
    }

    public String getCategories() {
        return categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categories);
    }

    protected UpdateCategories(Parcel in) {
        categories = in.readString();
    }

    public static final Creator<UpdateCategories> CREATOR = new Creator<UpdateCategories>() {
        @Override
        public UpdateCategories createFromParcel(Parcel in) {
            return new UpdateCategories(in);
        }

        @Override
        public UpdateCategories[] newArray(int size) {
            return new UpdateCategories[size];
        }
    };
}