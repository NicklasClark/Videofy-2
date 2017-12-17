package com.cncoding.teazer.uploadvideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by farazhabib on 14/12/17.
 */

public class VideoDetailsResultObject {



    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("post_details")
    @Expose
    private PostDetails postDetails;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PostDetails getPostDetails() {
        return postDetails;
    }

    public void setPostDetails(PostDetails postDetails) {
        this.postDetails = postDetails;
    }

}
