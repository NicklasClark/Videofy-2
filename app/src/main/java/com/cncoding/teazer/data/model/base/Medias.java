package com.cncoding.teazer.data.model.base;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@Entity(tableName = "Medias")
public class Medias extends ViewModel implements Parcelable {

    @SerializedName("media_id") @Expose private int media_id;
    @SerializedName("media_url") @Expose private String media_url;
    @SerializedName("thumb_url") @Expose private String thumb_url;
    @SerializedName("duration") @Expose private String duration;
    @Embedded(prefix = "dimension_") @SerializedName("media_dimension") @Expose private Dimension media_dimension;
    @SerializedName("is_image") @Expose private boolean is_image;
    @SerializedName("views") @Expose public int views;
    @SerializedName("created_at") @Expose private String created_at;

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

    public void setViews(int views) {
        this.views = views;
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