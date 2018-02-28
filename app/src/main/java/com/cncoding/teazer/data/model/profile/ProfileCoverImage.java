package com.cncoding.teazer.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by farazhabib on 15/02/18.
 */

public class ProfileCoverImage {


    @SerializedName("cover_image_id")
    @Expose
    private Integer coverImageId;
    @SerializedName("default_cover_id")
    @Expose
    private Object defaultCoverId;
    @SerializedName("cover_type")
    @Expose
    private Integer coverType;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("dimension")
    @Expose
    private String dimension;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("media_dimension")
    @Expose
    private Object mediaDimension;

    public Integer getCoverImageId() {
        return coverImageId;
    }

    public void setCoverImageId(Integer coverImageId) {
        this.coverImageId = coverImageId;
    }

    public Object getDefaultCoverId() {
        return defaultCoverId;
    }

    public void setDefaultCoverId(Object defaultCoverId) {
        this.defaultCoverId = defaultCoverId;
    }

    public Integer getCoverType() {
        return coverType;
    }

    public void setCoverType(Integer coverType) {
        this.coverType = coverType;
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

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Object getMediaDimension() {
        return mediaDimension;
    }

    public void setMediaDimension(Object mediaDimension) {
        this.mediaDimension = mediaDimension;
    }

}

