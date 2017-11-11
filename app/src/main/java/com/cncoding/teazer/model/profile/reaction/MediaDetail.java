package com.cncoding.teazer.model.profile.reaction;

/**
 * Created by farazhabib on 10/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MediaDetail {

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
    @SerializedName("react_dimension")
    @Expose
    private String reactDimension;
    @SerializedName("react_is_image")
    @Expose
    private Boolean reactIsImage;
    @SerializedName("media_dimension")
    @Expose
    private MediaDimension mediaDimension;

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

    public String getReactDimension() {
        return reactDimension;
    }

    public void setReactDimension(String reactDimension) {
        this.reactDimension = reactDimension;
    }

    public Boolean getReactIsImage() {
        return reactIsImage;
    }

    public void setReactIsImage(Boolean reactIsImage) {
        this.reactIsImage = reactIsImage;
    }

    public MediaDimension getMediaDimension() {
        return mediaDimension;
    }

    public void setMediaDimension(MediaDimension mediaDimension) {
        this.mediaDimension = mediaDimension;
    }


}
