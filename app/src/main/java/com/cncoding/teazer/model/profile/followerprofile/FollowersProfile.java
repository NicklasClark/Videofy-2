package com.cncoding.teazer.model.profile.followerprofile;

/**
 * Created by farazhabib on 13/11/17.
 */

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class FollowersProfile {

    @SerializedName("total_videos")
    @Expose
    private Integer totalVideos;
    @SerializedName("account_type")
    @Expose
    private Integer accountType;
    @SerializedName("can_join")
    @Expose
    private Boolean canJoin;
    @SerializedName("has_send_join_request")
    @Expose
    private Boolean hasSendJoinRequest;
    @SerializedName("join_request_id")
    @Expose
    private Integer joinRequestId;
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

    public Integer getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(Integer totalVideos) {
        this.totalVideos = totalVideos;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Boolean getCanJoin() {
        return canJoin;
    }

    public void setCanJoin(Boolean canJoin) {
        this.canJoin = canJoin;
    }

    public Boolean getHasSendJoinRequest() {
        return hasSendJoinRequest;
    }

    public void setHasSendJoinRequest(Boolean hasSendJoinRequest) {
        this.hasSendJoinRequest = hasSendJoinRequest;
    }

    public Integer getJoinRequestId() {
        return joinRequestId;
    }

    public void setJoinRequestId(Integer joinRequestId) {
        this.joinRequestId = joinRequestId;
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

}