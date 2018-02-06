package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ProfileMedia implements Parcelable {
    private int picture_id;
    private String media_url;
    private String thumb_url;
    private String duration;
    private Dimension media_dimension;
    private boolean is_image;

    public ProfileMedia(int picture_id, String media_url, String thumb_url, String duration, Dimension media_dimension, boolean is_image) {
        this.picture_id = picture_id;
        this.media_url = media_url;
        this.thumb_url = thumb_url;
        this.duration = duration;
        this.media_dimension = media_dimension;
        this.is_image = is_image;
    }

    protected ProfileMedia(Parcel in) {
        picture_id = in.readInt();
        media_url = in.readString();
        thumb_url = in.readString();
        duration = in.readString();
        media_dimension = in.readParcelable(Dimension.class.getClassLoader());
        is_image = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(picture_id);
        dest.writeString(media_url);
        dest.writeString(thumb_url);
        dest.writeString(duration);
        dest.writeParcelable(media_dimension, flags);
        dest.writeByte((byte) (is_image ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfileMedia> CREATOR = new Creator<ProfileMedia>() {
        @Override
        public ProfileMedia createFromParcel(Parcel in) {
            return new ProfileMedia(in);
        }

        @Override
        public ProfileMedia[] newArray(int size) {
            return new ProfileMedia[size];
        }
    };

    public int getPictureId() {
        return picture_id;
    }

    public String getMediaUrl() {
        return media_url;
    }

    public String getThumbUrl() {
        return thumb_url;
    }

    public String getDuration() {
        return duration;
    }

    public Dimension getDimension() {
        return media_dimension;
    }

    public boolean isImage() {
        return is_image;
    }
}