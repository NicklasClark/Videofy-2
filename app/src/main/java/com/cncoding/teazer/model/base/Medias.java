package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class Medias implements Parcelable {
    private int media_id;
    private String media_url;
    private String thumb_url;
    private String duration;
    private Dimension media_dimension;
    private boolean is_image;
    public int views;
    private String created_at;

    public Medias(int media_id, String media_url, String thumb_url, String duration,
                  Dimension dimension, boolean is_image, int views, String created_at) {
        this.media_id = media_id;
        this.media_url = media_url;
        this.thumb_url = thumb_url;
        this.duration = duration;
        this.media_dimension = dimension;
        this.is_image = is_image;
        this.views = views;
        this.created_at = created_at;
    }

    public int getMediaId() {
        return media_id;
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

    public int getViews() {
        return views;
    }

    public String getCreatedAt() {
        return created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(media_id);
        parcel.writeString(media_url);
        parcel.writeString(thumb_url);
        parcel.writeString(duration);
        parcel.writeParcelable(media_dimension, i);
        parcel.writeByte((byte) (is_image ? 1 : 0));
        parcel.writeInt(views);
        parcel.writeString(created_at);
    }

    protected Medias(Parcel in) {
        media_id = in.readInt();
        media_url = in.readString();
        thumb_url = in.readString();
        duration = in.readString();
        media_dimension = in.readParcelable(Dimension.class.getClassLoader());
        is_image = in.readByte() != 0;
        views = in.readInt();
        created_at = in.readString();
    }

    public static final Creator<Medias> CREATOR = new Creator<Medias>() {
        @Override
        public Medias createFromParcel(Parcel in) {
            return new Medias(in);
        }

        @Override
        public Medias[] newArray(int size) {
            return new Medias[size];
        }
    };
}