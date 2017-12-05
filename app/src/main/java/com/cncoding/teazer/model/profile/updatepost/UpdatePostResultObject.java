package com.cncoding.teazer.model.profile.updatepost;

import com.cncoding.teazer.model.profile.followerprofile.postvideos.CheckIn;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.Media;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.PostOwner;
import com.cncoding.teazer.utilities.Pojos;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by farazhabib on 04/12/17.
 */

public class UpdatePostResultObject {

    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("posted_by")
    @Expose
    private Integer postedBy;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("total_reactions")
    @Expose
    private Integer totalReactions;
    @SerializedName("total_tags")
    @Expose
    private Integer totalTags;
    @SerializedName("has_checkin")
    @Expose
    private Boolean hasCheckin;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("can_react")
    @Expose
    private Boolean canReact;
    @SerializedName("can_like")
    @Expose
    private Boolean canLike;
    @SerializedName("can_delete")
    @Expose
    private Boolean canDelete;
    @SerializedName("post_owner")
    @Expose
    private PostOwner postOwner;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;
    @SerializedName("check_in")
    @Expose
    private CheckIn checkIn;
    @SerializedName("medias")
    @Expose
    private List<com.cncoding.teazer.model.profile.updatepost.Media> medias = null;
    @SerializedName("tagged_users")
    @Expose
    private List<Object> taggedUsers = null;
    @SerializedName("reacted_users")
    @Expose
    private List<Object> reactedUsers = null;
    @SerializedName("categories")
    @Expose
    private List<Pojos.Category> categories = null;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Integer postedBy) {
        this.postedBy = postedBy;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getTotalReactions() {
        return totalReactions;
    }

    public void setTotalReactions(Integer totalReactions) {
        this.totalReactions = totalReactions;
    }

    public Integer getTotalTags() {
        return totalTags;
    }

    public void setTotalTags(Integer totalTags) {
        this.totalTags = totalTags;
    }

    public Boolean getHasCheckin() {
        return hasCheckin;
    }

    public void setHasCheckin(Boolean hasCheckin) {
        this.hasCheckin = hasCheckin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCanReact() {
        return canReact;
    }

    public void setCanReact(Boolean canReact) {
        this.canReact = canReact;
    }

    public Boolean getCanLike() {
        return canLike;
    }

    public void setCanLike(Boolean canLike) {
        this.canLike = canLike;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public PostOwner getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(PostOwner postOwner) {
        this.postOwner = postOwner;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public CheckIn getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(CheckIn checkIn) {
        this.checkIn = checkIn;
    }

    public List<com.cncoding.teazer.model.profile.updatepost.Media> getMedias() {
        return medias;
    }

    public void setMedias(List<com.cncoding.teazer.model.profile.updatepost.Media> medias) {
        this.medias = medias;
    }

    public List<Object> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(List<Object> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    public List<Object> getReactedUsers() {
        return reactedUsers;
    }

    public void setReactedUsers(List<Object> reactedUsers) {
        this.reactedUsers = reactedUsers;
    }

    public List<Pojos.Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Pojos.Category> categories) {
        this.categories = categories;
    }

}
