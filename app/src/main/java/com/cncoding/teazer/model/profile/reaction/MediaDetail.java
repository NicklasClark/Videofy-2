package com.cncoding.teazer.model.profile.reaction;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.profile.followerprofile.MediaDimension_;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class MediaDetail implements Parcelable {

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
    private MediaDimension_ mediaDimension;

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

    public Boolean getReactIsImage() {
        return reactIsImage;
    }

    public void setReactIsImage(Boolean reactIsImage) {
        this.reactIsImage = reactIsImage;
    }

    public MediaDimension_ getMediaDimension() {
        return mediaDimension;
    }

    public void setMediaDimension(MediaDimension_ mediaDimension) {
        this.mediaDimension = mediaDimension;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mediaId);
        dest.writeString(this.reactMediaUrl);
        dest.writeString(this.reactThumbUrl);
        dest.writeString(this.reactDuration);
        dest.writeValue(this.reactIsImage);
        dest.writeParcelable(this.mediaDimension, flags);
    }

    public MediaDetail() {
    }

    protected MediaDetail(Parcel in) {
        this.mediaId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.reactMediaUrl = in.readString();
        this.reactThumbUrl = in.readString();
        this.reactDuration = in.readString();
        this.reactIsImage = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mediaDimension = in.readParcelable(MediaDimension_.class.getClassLoader());
    }

    public static final Parcelable.Creator<MediaDetail> CREATOR = new Parcelable.Creator<MediaDetail>() {
        @Override
        public MediaDetail createFromParcel(Parcel source) {
            return new MediaDetail(source);
        }

        @Override
        public MediaDetail[] newArray(int size) {
            return new MediaDetail[size];
        }
    };
}
