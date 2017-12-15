package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class Dimension implements Parcelable {
    private int height;
    private int width;

    public Dimension(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(height);
        parcel.writeInt(width);
    }

    protected Dimension(Parcel in) {
        height = in.readInt();
        width = in.readInt();
    }

    public static final Creator<Dimension> CREATOR = new Creator<Dimension>() {
        @Override
        public Dimension createFromParcel(Parcel in) {
            return new Dimension(in);
        }

        @Override
        public Dimension[] newArray(int size) {
            return new Dimension[size];
        }
    };
}