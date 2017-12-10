package com.cncoding.teazer.model.profile.followerprofile;

/**
 * Created by farazhabib on 13/11/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaDimension_ implements Parcelable {

    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("width")
    @Expose
    private Integer width;

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.height);
        dest.writeValue(this.width);
    }

    public MediaDimension_() {
    }

    protected MediaDimension_(Parcel in) {
        this.height = (Integer) in.readValue(Integer.class.getClassLoader());
        this.width = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<MediaDimension_> CREATOR = new Parcelable.Creator<MediaDimension_>() {
        @Override
        public MediaDimension_ createFromParcel(Parcel source) {
            return new MediaDimension_(source);
        }

        @Override
        public MediaDimension_[] newArray(int size) {
            return new MediaDimension_[size];
        }
    };
}