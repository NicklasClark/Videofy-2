package com.cncoding.teazer.data.model.profile;

import com.cncoding.teazer.data.model.base.Dimension;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by farazhabib on 03/03/18.
 */

public class DefaultCoverMedia {
    @SerializedName("default_cover_id")
    @Expose
    private Integer defaultCoverId;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("dimension")
    @Expose
    private String dimension;
    @SerializedName("media_dimension")
    @Expose
    private Dimension mediaDimension;

    public DefaultCoverMedia(Integer defaultCoverId, String thumbUrl, String mediaUrl, String dimension, Dimension mediaDimension) {
        this.defaultCoverId = defaultCoverId;
        this.thumbUrl = thumbUrl;
        this.mediaUrl = mediaUrl;
        this.dimension = dimension;
        this.mediaDimension = mediaDimension;
    }

    public Integer getDefaultCoverId() {
        return defaultCoverId;
    }

    public void setDefaultCoverId(Integer defaultCoverId) {
        this.defaultCoverId = defaultCoverId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public Dimension getMediaDimension() {
        return mediaDimension;
    }

    public void setMediaDimension(Dimension mediaDimension) {
        this.mediaDimension = mediaDimension;
    }

}
