package com.cncoding.teazer.model.profile.followerprofile.postvideos;

/**
 * Created by farazhabib on 13/11/17.
 */


import com.cncoding.teazer.model.profile.followerprofile.MediaDimension_;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("media_id")
    @Expose
    private Integer mediaId;
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
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("media_dimension")
    @Expose
    private MediaDimension_ mediaDimension;

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer mediaId) {
        this.mediaId = mediaId;
    }

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

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public MediaDimension_ getMediaDimension() {
        return mediaDimension;
    }

    public void setMediaDimension(MediaDimension_ mediaDimension) {
        this.mediaDimension = mediaDimension;
    }

}