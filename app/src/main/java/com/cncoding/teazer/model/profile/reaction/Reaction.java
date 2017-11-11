package com.cncoding.teazer.model.profile.reaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class Reaction {

    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("react_id")
    @Expose
    private Integer reactId;
    @SerializedName("reacted_by")
    @Expose
    private Integer reactedBy;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("can_like")
    @Expose
    private Boolean canLike;
    @SerializedName("react_title")
    @Expose
    private String reactTitle;
    @SerializedName("profile_media")
    @Expose
    private ProfileMedia profileMedia;
    @SerializedName("media_detail")
    @Expose
    private MediaDetail mediaDetail;
    @SerializedName("post_owner")
    @Expose
    private PostOwner postOwner;
    @SerializedName("can_delete")
    @Expose
    private Boolean canDelete;
    @SerializedName("reacted_at")
    @Expose
    private String reactedAt;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getReactId() {
        return reactId;
    }

    public void setReactId(Integer reactId) {
        this.reactId = reactId;
    }

    public Integer getReactedBy() {
        return reactedBy;
    }

    public void setReactedBy(Integer reactedBy) {
        this.reactedBy = reactedBy;
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

    public String getReactTitle() {
        return reactTitle;
    }

    public void setReactTitle(String reactTitle) {
        this.reactTitle = reactTitle;
    }

    public ProfileMedia getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(ProfileMedia profileMedia) {
        this.profileMedia = profileMedia;
    }

    public MediaDetail getMediaDetail() {
        return mediaDetail;
    }

    public void setMediaDetail(MediaDetail mediaDetail) {
        this.mediaDetail = mediaDetail;
    }

    public PostOwner getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(PostOwner postOwner) {
        this.postOwner = postOwner;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getReactedAt() {
        return reactedAt;
    }

    public void setReactedAt(String reactedAt) {
        this.reactedAt = reactedAt;
    }

}