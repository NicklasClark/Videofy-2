package com.cncoding.teazer.model.profile.following;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class Following {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("account_type")
    @Expose
    private Integer accountType;
    @SerializedName("has_profile_media")
    @Expose
    private Boolean hasProfileMedia;
    @SerializedName("following")
    @Expose
    private Boolean following;
    @SerializedName("follower")
    @Expose
    private Boolean follower;
    @SerializedName("request_sent")
    @Expose
    private Boolean requestSent;
    @SerializedName("profile_media")
    @Expose
    private ProfileMedia profileMedia;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Boolean getHasProfileMedia() {
        return hasProfileMedia;
    }

    public void setHasProfileMedia(Boolean hasProfileMedia) {
        this.hasProfileMedia = hasProfileMedia;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

    public Boolean getFollower() {
        return follower;
    }

    public void setFollower(Boolean follower) {
        this.follower = follower;
    }

    public Boolean getRequestSent() {
        return requestSent;
    }

    public void setRequestSent(Boolean requestSent) {
        this.requestSent = requestSent;
    }

    public ProfileMedia getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(ProfileMedia profileMedia) {
        this.profileMedia = profileMedia;
    }

}