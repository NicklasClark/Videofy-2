package com.cncoding.teazer.model.base;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@Entity(tableName = "CheckIn")
public class CheckIn extends ViewModel implements Parcelable {

    @SerializedName("checkin_id") @Expose private int checkinId;
    @SerializedName("latitude") @Expose private double latitude;
    @SerializedName("longitude") @Expose private double longitude;
    @SerializedName("location") @Expose private String location;

    public CheckIn(int checkinId, double latitude, double longitude, String location) {
        this.checkinId = checkinId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }

    protected CheckIn(Parcel in) {
        checkinId = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        location = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(checkinId);
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
        return checkinId;
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CheckIn && Objects.equals(getCheckinId(), ((CheckIn) obj).getCheckinId());
    }
}