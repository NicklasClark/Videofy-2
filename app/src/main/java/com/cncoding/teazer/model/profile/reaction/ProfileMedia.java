package com.cncoding.teazer.model.profile.reaction;


import com.cncoding.teazer.model.profile.followerprofile.MediaDimension;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class ProfileMedia {

    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("dimension")
    @Expose
    private String dimension;
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

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
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

}
