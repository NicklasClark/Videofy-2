package com.cncoding.teazer.model.react;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 26/12/17.
 */

public class MediaDetail implements Parcelable{
    @SerializedName("media_id")
    @Expose
    private Integer mediaId;
    @SerializedName("react_media_url")
    @Expose
    private String reactMediaUrl;
    @SerializedName("react_thumb_url")
    @Expose
    private String reactThumbUrl;
    @SerializedName("react_duration")
    @Expose
    private String reactDuration;
    @SerializedName("react_is_image")
    @Expose
    private Boolean reactIsImage;
    @SerializedName("media_dimension")
    @Expose
    private MediaDimension mediaDimension;

    protected MediaDetail(Parcel in) {
        if (in.readByte() == 0) {
            mediaId = null;
        } else {
            mediaId = in.readInt();
        }
        reactMediaUrl = in.readString();
        reactThumbUrl = in.readString();
        reactDuration = in.readString();
        byte tmpReactIsImage = in.readByte();
        reactIsImage = tmpReactIsImage == 0 ? null : tmpReactIsImage == 1;
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
        dest.writeByte((byte) (reactIsImage == null ? 0 : reactIsImage ? 1 : 2));
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

    public String getMediaUrl() {
        return reactMediaUrl;
    }

    public void setReactMediaUrl(String reactMediaUrl) {
        this.reactMediaUrl = reactMediaUrl;
    }

    public String getThumbUrl() {
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

    public Boolean getReactIsImage() {
        return reactIsImage;
    }

    public void setReactIsImage(Boolean reactIsImage) {
        this.reactIsImage = reactIsImage;
    }

    public MediaDimension getReactDimension() {
        return mediaDimension;
    }

    public void setMediaDimension(MediaDimension mediaDimension) {
        this.mediaDimension = mediaDimension;
    }
}
