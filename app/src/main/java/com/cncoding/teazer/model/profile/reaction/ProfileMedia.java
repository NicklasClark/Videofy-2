package com.cncoding.teazer.model.profile.reaction;


import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.profile.followerprofile.MediaDimension;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class ProfileMedia implements Parcelable {

    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("is_image")
    @Expose
    private Boolean isImage;
    @SerializedName("media_dimension")
    @Expose
    private MediaDimension mediaDimension;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(Boolean isImage) {
        this.isImage = isImage;
    }

    public MediaDimension getMediaDimension() {
        return mediaDimension;
    }

    public void setMediaDimension(MediaDimension mediaDimension) {
        this.mediaDimension = mediaDimension;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mediaUrl);
        dest.writeString(this.thumbUrl);
        dest.writeString(this.duration);
        dest.writeValue(this.isImage);
        dest.writeParcelable(this.mediaDimension, flags);
    }

    public ProfileMedia() {
    }

    protected ProfileMedia(Parcel in) {
        this.mediaUrl = in.readString();
        this.thumbUrl = in.readString();
        this.duration = in.readString();
        this.isImage = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.mediaDimension = in.readParcelable(MediaDimension.class.getClassLoader());
    }

    public static final Parcelable.Creator<ProfileMedia> CREATOR = new Parcelable.Creator<ProfileMedia>() {
        @Override
        public ProfileMedia createFromParcel(Parcel source) {
            return new ProfileMedia(source);
        }

        @Override
        public ProfileMedia[] newArray(int size) {
            return new ProfileMedia[size];
        }
    };
}
