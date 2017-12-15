package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReactionMediaDetail implements Parcelable {
    private int media_id;
    private String react_media_url;
    private String react_thumb_url;
    private String react_duration;
    private Dimension media_dimension;
    private boolean react_is_image;

    public ReactionMediaDetail(int media_id, String react_media_url, String react_thumb_url, String react_duration, Dimension media_dimension, boolean react_is_image) {
        this.media_id = media_id;
        this.react_media_url = react_media_url;
        this.react_thumb_url = react_thumb_url;
        this.react_duration = react_duration;
        this.media_dimension = media_dimension;
        this.react_is_image = react_is_image;
    }

    public int getMediaId() {
        return media_id;
    }

    public String getMediaUrl() {
        return react_media_url;
    }

    public String getThumbUrl() {
        return react_thumb_url;
    }

    public String getReactDuration() {
        return react_duration;
    }

    public Dimension getReactDimension() {
        return media_dimension;
    }

    public boolean hasImage() {
        return react_is_image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(media_id);
        parcel.writeString(react_media_url);
        parcel.writeString(react_thumb_url);
        parcel.writeString(react_duration);
        parcel.writeParcelable(media_dimension, i);
        parcel.writeByte((byte) (react_is_image ? 1 : 0));
    }

    protected ReactionMediaDetail(Parcel in) {
        media_id = in.readInt();
        react_media_url = in.readString();
        react_thumb_url = in.readString();
        react_duration = in.readString();
        media_dimension = in.readParcelable(Dimension.class.getClassLoader());
        react_is_image = in.readByte() != 0;
    }

    public static final Creator<ReactionMediaDetail> CREATOR = new Creator<ReactionMediaDetail>() {
        @Override
        public ReactionMediaDetail createFromParcel(Parcel in) {
            return new ReactionMediaDetail(in);
        }

        @Override
        public ReactionMediaDetail[] newArray(int size) {
            return new ReactionMediaDetail[size];
        }
    };
}