package com.cncoding.teazer.model.friends;

import com.cncoding.teazer.model.base.Medias;
import com.cncoding.teazer.utilities.Annotations.Gender;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 17/11/17.
 */

public class UserInfo {

    @SerializedName("user_id") @Expose private Integer userId;
    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("first_name") @Expose private String firstName;
    @SerializedName("last_name") @Expose private String lastName;
    @Gender @SerializedName("gender") @Expose private Integer gender;
    @SerializedName("is_blocked_you") @Expose private Boolean isBlockedYou;
    @SerializedName("you_blocked") @Expose private Boolean youBlocked;
    @SerializedName("my_self") @Expose private Boolean mySelf;
    @SerializedName("account_type") @Expose private Integer accountType;
    @SerializedName("has_profile_media") @Expose private Boolean hasProfileMedia;
    @SerializedName("following") @Expose private Boolean following;
    @SerializedName("follower") @Expose private Boolean follower;
    @SerializedName("request_sent") @Expose private Boolean requestSent;
    @SerializedName("request_id") @Expose private Integer requestId;
    @SerializedName("request_recieved") @Expose private Boolean requestRecieved;
    @SerializedName("profile_media") @Expose private Medias profileMedia;

    public UserInfo(Integer userId, String userName, String firstName, String lastName, Integer gender, Boolean isBlockedYou, Boolean youBlocked,
                    Boolean mySelf, Integer accountType, Boolean hasProfileMedia, Boolean following, Boolean follower, Boolean requestSent,
                    Integer requestId, Boolean requestRecieved, Medias profileMedia) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.isBlockedYou = isBlockedYou;
        this.youBlocked = youBlocked;
        this.mySelf = mySelf;
        this.accountType = accountType;
        this.hasProfileMedia = hasProfileMedia;
        this.following = following;
        this.follower = follower;
        this.requestSent = requestSent;
        this.requestId = requestId;
        this.requestRecieved = requestRecieved;
        this.profileMedia = profileMedia;
    }

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

    public Boolean getIsBlockedYou() {
        return isBlockedYou;
    }

    public void setIsBlockedYou(Boolean isBlockedYou) {
        this.isBlockedYou = isBlockedYou;
    }

    public Boolean getYouBlocked() {
        return youBlocked;
    }

    public void setYouBlocked(Boolean youBlocked) {
        this.youBlocked = youBlocked;
    }

    public Boolean getMySelf() {
        return mySelf;
    }

    public void setMySelf(Boolean mySelf) {
        this.mySelf = mySelf;
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

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Boolean getRequestRecieved() {
        return requestRecieved;
    }

    public void setRequestRecieved(Boolean requestRecieved) {
        this.requestRecieved = requestRecieved;
    }

    public Medias getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(Medias profileMedia) {
        this.profileMedia = profileMedia;
    }

    @Gender public Integer getGender() {
        return gender;
    }

    public void setGender(@Gender Integer gender) {
        this.gender = gender;
    }
}

