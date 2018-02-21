package com.cncoding.teazer.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by farazhabib on 15/02/18.
 */

public class CoverImageResponse {

    @SerializedName("profile_cover_image")
    @Expose
    private ProfileCoverImage profileCoverImage;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;

    public ProfileCoverImage getProfileCoverImage() {
        return profileCoverImage;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setProfileCoverImage(ProfileCoverImage profileCoverImage) {
        this.profileCoverImage = profileCoverImage;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
