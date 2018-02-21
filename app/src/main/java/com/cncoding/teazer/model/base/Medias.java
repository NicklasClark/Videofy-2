package com.cncoding.teazer.model.base;

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

    @SerializedName("media_id") @Expose private Integer mediaId;
    @SerializedName("media_url") @Expose private String mediaUrl;
    @SerializedName("thumb_url") @Expose private String thumbUrl;
    @SerializedName("duration") @Expose private String duration;
    @Embedded(prefix = "dimension_") @SerializedName("media_dimension") @Expose private Dimension mediaDimension;
    @SerializedName("is_image") @Expose private Boolean isImage;
    @SerializedName("views") @Expose public Integer views;
    @SerializedName("created_at") @Expose private String createdAt;

    public Medias(Integer mediaId, String mediaUrl, String thumbUrl, String duration, Dimension mediaDimension,
                  Boolean isImage, Integer views, String createdAt) {
        this.mediaId = mediaId;
        this.mediaUrl = mediaUrl;
        this.thumbUrl = thumbUrl;
        this.duration = duration;
        this.mediaDimension = mediaDimension;
        this.isImage = isImage;
        this.views = views;
        this.createdAt = createdAt;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getDuration() {
        return duration;
    }

    public Dimension getMediaDimension() {
        return mediaDimension;
    }

    public Boolean isImage() {
        return isImage;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mediaId);
        parcel.writeString(mediaUrl);
        parcel.writeString(thumbUrl);
        parcel.writeString(duration);
        parcel.writeParcelable(mediaDimension, i);
        parcel.writeByte((byte) (isImage ? 1 : 0));
        parcel.writeInt(views);
        parcel.writeString(createdAt);
    }

    protected Medias(Parcel in) {
        mediaId = in.readInt();
        mediaUrl = in.readString();
        thumbUrl = in.readString();
        duration = in.readString();
        mediaDimension = in.readParcelable(Dimension.class.getClassLoader());
        isImage = in.readByte() != 0;
        views = in.readInt();
        createdAt = in.readString();
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