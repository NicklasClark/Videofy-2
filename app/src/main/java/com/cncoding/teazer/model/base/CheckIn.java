package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class CheckIn implements Parcelable {
    private int checkin_id;
    private double latitude;
    private double longitude;
    private String location;

    public CheckIn(int checkin_id, int latitude, int longitude, String location) {
        this.checkin_id = checkin_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }

    protected CheckIn(Parcel in) {
        checkin_id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        location = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(checkin_id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(location);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckIn> CREATOR = new Creator<CheckIn>() {
        @Override
        public CheckIn createFromParcel(Parcel in) {
            return new CheckIn(in);
        }

        @Override
        public CheckIn[] newArray(int size) {
            return new CheckIn[size];
        }
    };

    public int getCheckinId() {
        return checkin_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
    }
}