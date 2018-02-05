package com.cncoding.teazer.model.friends;

import com.cncoding.teazer.model.user.PrivateProfile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 13/11/17.
 */

public class ProfileInfo {

    @SerializedName("total_videos")
    @Expose
    private Integer totalVideos;
    @SerializedName("private_profile")
    @Expose
    private PrivateProfile privateProfile;
    @SerializedName("public_profile")
    @Expose
    private PublicProfile publicProfile;
    @SerializedName("followers")
    @Expose
    private Integer followers;
    @SerializedName("followings")
    @Expose
    private Integer followings;
    @SerializedName("follow_info")
    @Expose
    private FollowInfo followInfo;

    @SerializedName("is_hided_all_posts")
    @Expose
    private Boolean isHidedAllPosts;

    public Integer getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(Integer totalVideos) {
        this.totalVideos = totalVideos;
    }

    public PrivateProfile getPrivateProfile() {
        return privateProfile;
    }

    public void setPrivateProfile(PrivateProfile privateProfile) {
        this.privateProfile = privateProfile;
    }

    public PublicProfile getPublicProfile() {
        return publicProfile;
    }

    public void setPublicProfile(PublicProfile publicProfile) {
        this.publicProfile = publicProfile;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowings() {
        return followings;
    }

    public void setFollowings(Integer followings) {
        this.followings = followings;
    }

    public FollowInfo getFollowInfo() {
        return followInfo;
    }

    public void setFollowInfo(FollowInfo followInfo) {
        this.followInfo = followInfo;
    }

    public Boolean getIsHidedAllPosts() {
        return isHidedAllPosts;
    }

    public void setIsHidedAllPosts(Boolean isHidedAllPosts) {
        this.isHidedAllPosts = isHidedAllPosts;
    }
}