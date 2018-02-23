package com.cncoding.teazer.model.react;

import com.cncoding.teazer.model.base.MediaDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 26/12/17.
 */

public class PostReactDetail {

    @SerializedName("react_id") @Expose private Integer reactId;
    @SerializedName("post_id") @Expose private Integer postId;
    @SerializedName("post_owner_id") @Expose private Integer postOwnerId;
    @SerializedName("react_title") @Expose private String reactTitle;
    @SerializedName("likes") @Expose private Integer likes;
    @SerializedName("views") @Expose private Integer views;
    @SerializedName("can_like") @Expose private Boolean canLike;
    @SerializedName("my_self") @Expose private Boolean mySelf;
    @SerializedName("can_delete") @Expose private Boolean canDelete;
    @SerializedName("media_detail") @Expose private MediaDetail mediaDetail;
    @SerializedName("react_owner") @Expose private ReactOwner reactOwner;
    @SerializedName("reacted_at") @Expose private String reactedAt;

    public Integer getReactId() {
        return reactId;
    }

    public void setReactId(Integer reactId) {
        this.reactId = reactId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getPostOwnerId() {
        return postOwnerId;
    }

    public void setPostOwnerId(Integer postOwnerId) {
        this.postOwnerId = postOwnerId;
    }

    public String getReactTitle() {
        return reactTitle;
    }

    public void setReactTitle(String reactTitle) {
        this.reactTitle = reactTitle;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Boolean getCanLike() {
        return canLike;
    }

    public void setCanLike(Boolean canLike) {
        this.canLike = canLike;
    }

    public Boolean getMySelf() {
        return mySelf;
    }

    public void setMySelf(Boolean mySelf) {
        this.mySelf = mySelf;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public MediaDetail getMediaDetail() {
        return mediaDetail;
    }

    public void setMediaDetail(MediaDetail mediaDetail) {
        this.mediaDetail = mediaDetail;
    }

    public ReactOwner getReactOwner() {
        return reactOwner;
    }

    public void setReactOwner(ReactOwner reactOwner) {
        this.reactOwner = reactOwner;
    }

    public String getReactedAt() {
        return reactedAt;
    }

    public void setReactedAt(String reactedAt) {
        this.reactedAt = reactedAt;
    }

}
