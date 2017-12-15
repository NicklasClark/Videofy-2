package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class Category implements Parcelable {
    private int category_id;
    private String category_name;
    private String color;

    public Category(int category_id, String category_name, String color) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.color = color;
    }

    protected Category(Parcel in) {
        category_id = in.readInt();
        category_name = in.readString();
        color = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(category_id);
        dest.writeString(category_name);
        dest.writeString(color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getCategoryId() {
        return category_id;
    }

    public String getCategoryName() {
        return category_name;
    }

    public String getColor() {
        return color;
    }
}