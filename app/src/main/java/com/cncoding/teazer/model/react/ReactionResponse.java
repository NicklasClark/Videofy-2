package com.cncoding.teazer.model.react;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.utilities.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by amit on 26/12/17.
 */

public class ReactionResponse extends BaseModel {

    @SerializedName("status") @Expose private Boolean status;
    @SerializedName("react_id") @Expose private Integer reactId;
    @SerializedName("message") @Expose private String message;
    @SerializedName("post_react_detail") @Expose private PostReaction postReactDetail;

    public ReactionResponse(Throwable error) {
        this.error = error;
    }

    public ReactionResponse setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

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
