package com.cncoding.teazer.data.model.react;

import com.cncoding.teazer.data.model.post.PostReaction;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by farazhabib on 26/01/18.
 */
public class ReactVideoDetailsResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("react_id")
    @Expose
    private Integer reactId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("post_react_detail")
    @Expose
    private PostReaction postReactDetail;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getReactId() {
        return reactId;
    }

    public void setReactId(Integer reactId) {
        this.reactId = reactId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PostReaction getPostReactDetail() {
        return postReactDetail;
    }

    public void setPostReactDetail(PostReaction postReactDetail) {
        this.postReactDetail = postReactDetail;
    }

}
