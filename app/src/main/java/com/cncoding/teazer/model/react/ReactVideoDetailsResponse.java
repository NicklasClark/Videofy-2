package com.cncoding.teazer.model.react;

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
    private PostReactDetail postReactDetail;

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

    public PostReactDetail getPostReactDetail() {
        return postReactDetail;
    }

    public void setPostReactDetail(PostReactDetail postReactDetail) {
        this.postReactDetail = postReactDetail;
    }

}
