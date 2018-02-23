package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class MediaDetail implements Parcelable {

    @SerializedName("media_id") @Expose private Integer mediaId;
    @SerializedName("react_media_url") @Expose private String reactMediaUrl;
    @SerializedName("react_thumb_url") @Expose private String reactThumbUrl;
    @SerializedName("react_duration") @Expose private String reactDuration;
    @SerializedName("external_meta") @Expose private String externalMeta;
    @SerializedName("media_type") @Expose private Integer mediaType;
    @SerializedName("media_dimension") @Expose private Dimension mediaDimension;

    protected MediaDetail(Parcel in) {
        if (in.readByte() == 0) {
            mediaId = null;
        } else {
            mediaId = in.readInt();
        }
        reactMediaUrl = in.readString();
        reactThumbUrl = in.readString();
        reactDuration = in.readString();
        externalMeta = in.readString();
        if (in.readByte() == 0) {
            mediaType = null;
        } else {
            mediaType = in.readInt();
        }
        mediaDimension = in.readParcelable(Dimension.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mediaId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mediaId);
        }
        dest.writeString(reactMediaUrl);
        dest.writeString(reactThumbUrl);
        dest.writeString(reactDuration);
        dest.writeString(externalMeta);
        if (mediaType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mediaType);
        }
        dest.writeParcelable(mediaDimension, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaDetail> CREATOR = new Creator<MediaDetail>() {
        @Override
        public MediaDetail createFromParcel(Parcel in) {
            return new MediaDetail(in);
        }

        @Override
        public MediaDetail[] newArray(int size) {
            return new MediaDetail[size];
        }
    };

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

    public String getReactMediaUrl() {
        return reactMediaUrl;
    }

    public void setReactMediaUrl(String reactMediaUrl) {
        this.reactMediaUrl = reactMediaUrl;
    }

    public String getReactThumbUrl() {
        return reactThumbUrl;
    }

    public void setReactThumbUrl(String reactThumbUrl) {
        this.reactThumbUrl = reactThumbUrl;
    }

    public String getReactDuration() {
        return reactDuration;
    }

    public void setReactDuration(String reactDuration) {
        this.reactDuration = reactDuration;
    }

    public String getExternalMeta() {
        return externalMeta;
    }

    public void setExternalMeta(String externalMeta) {
        this.externalMeta = externalMeta;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    public Dimension getMediaDimension() {
        return mediaDimension;
    }

    public void setMediaDimension(Dimension mediaDimension) {
        this.mediaDimension = mediaDimension;
    }
}