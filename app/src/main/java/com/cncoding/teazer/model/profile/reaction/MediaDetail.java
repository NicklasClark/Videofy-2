package com.cncoding.teazer.model.profile.reaction;

import com.cncoding.teazer.model.profile.followerprofile.MediaDimension;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

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


}
