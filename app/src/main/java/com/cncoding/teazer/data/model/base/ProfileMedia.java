package com.cncoding.teazer.data.model.base;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@Entity(tableName = "ProfileMedia")
public class ProfileMedia extends ViewModel implements Parcelable {

    @SerializedName("picture_id") @Expose private int pictureId;
    @SerializedName("media_url") @Expose private String mediaUrl;
    @SerializedName("thumb_url") @Expose private String thumbUrl;
    @SerializedName("duration") @Expose private String duration;
    @Embedded(prefix = "dimension_") @SerializedName("media_dimension") @Expose private Dimension mediaDimension;
    @SerializedName("is_image") @Expose private Boolean isImage;

    public ProfileMedia(int pictureId, String mediaUrl, String thumbUrl, String duration, Dimension mediaDimension, Boolean isImage) {
        this.pictureId = pictureId;
        this.mediaUrl = mediaUrl;
        this.thumbUrl = thumbUrl;
        this.duration = duration;
        this.mediaDimension = mediaDimension;
        this.isImage = isImage;
    }

    protected ProfileMedia(Parcel in) {
        pictureId = in.readInt();
        mediaUrl = in.readString();
        thumbUrl = in.readString();
        duration = in.readString();
        mediaDimension = in.readParcelable(Dimension.class.getClassLoader());
        byte tmpIsImage = in.readByte();
        isImage = tmpIsImage == 0 ? null : tmpIsImage == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pictureId);
        dest.writeString(mediaUrl);
        dest.writeString(thumbUrl);
        dest.writeString(duration);
        dest.writeParcelable(mediaDimension, flags);
        dest.writeByte((byte) (isImage == null ? 0 : isImage ? 1 : 2));
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
        return pictureId;
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

    public void setPictureId(int pictureId) {
        this.pictureId = pictureId;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setMediaDimension(Dimension mediaDimension) {
        this.mediaDimension = mediaDimension;
    }

    public void setImage(Boolean image) {
        isImage = image;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 31)
                .append(pictureId)
                .append(mediaUrl)
                .append(thumbUrl)
                .append(duration)
                .append(mediaDimension)
                .append(isImage)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof ProfileMedia &&
                new EqualsBuilder()
                        .append(pictureId, ((ProfileMedia) obj).getPictureId())
                        .append(mediaUrl, ((ProfileMedia) obj).getMediaUrl())
                        .append(thumbUrl, ((ProfileMedia) obj).getThumbUrl())
                        .append(duration, ((ProfileMedia) obj).getDuration())
                        .append(mediaDimension, ((ProfileMedia) obj).getMediaDimension())
                        .append(isImage, ((ProfileMedia) obj).isImage())
                        .isEquals();
    }
}