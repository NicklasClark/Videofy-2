package com.cncoding.teazer.data.model.user.userProfile;

import com.cncoding.teazer.data.model.friends.PublicProfile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 18/11/17.
 */

public class UserProfileResponse {

    @SerializedName("user_profile")
        @Expose
        private PublicProfile userProfile;
        @SerializedName("followers")
        @Expose
        private Integer followers;
        @SerializedName("followings")
        @Expose
        private Integer followings;
        @SerializedName("total_videos")
        @Expose
        private Integer totalVideos;
        @SerializedName("can_change_password")
        @Expose
        private Boolean canChangePassword;

        public PublicProfile getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(PublicProfile userProfile) {
            this.userProfile = userProfile;
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

        public Integer getTotalVideos() {
            return totalVideos;
        }

        public void setTotalVideos(Integer totalVideos) {
            this.totalVideos = totalVideos;
        }

        public Boolean getCanChangePassword() {
            return canChangePassword;
        }

        public void setCanChangePassword(Boolean canChangePassword) {
            this.canChangePassword = canChangePassword;
        }
    }