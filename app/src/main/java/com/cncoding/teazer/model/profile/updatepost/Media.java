package com.cncoding.teazer.model.profile.updatepost;

import com.cncoding.teazer.model.profile.followerprofile.MediaDimension;
import com.cncoding.teazer.model.profile.followerprofile.MediaDimension_;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by farazhabib on 04/12/17.
 */

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
    @SerializedName("is_image")
    @Expose
    private Boolean isImage;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public MediaDimension_ getMediaDimension() {
        return mediaDimension;
    }

    public void setMediaDimension(MediaDimension_ mediaDimension) {
        this.mediaDimension = mediaDimension;
    }
}
