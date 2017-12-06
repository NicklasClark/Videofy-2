package com.cncoding.teazer.model.profile.updatetags;

import com.cncoding.teazer.model.profile.followers.ProfileMedia;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by farazhabib on 06/12/17.
 */

public class ReactedUser {


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
        @SerializedName("is_blocked_you")
        @Expose
        private Boolean isBlockedYou;
        @SerializedName("my_self")
        @Expose
        private Boolean mySelf;
        @SerializedName("has_profile_media")
        @Expose
        private Boolean hasProfileMedia;
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

        public Boolean getIsBlockedYou() {
            return isBlockedYou;
        }

        public void setIsBlockedYou(Boolean isBlockedYou) {
            this.isBlockedYou = isBlockedYou;
        }

        public Boolean getMySelf() {
            return mySelf;
        }

        public void setMySelf(Boolean mySelf) {
            this.mySelf = mySelf;
        }

        public Boolean getHasProfileMedia() {
            return hasProfileMedia;
        }

        public void setHasProfileMedia(Boolean hasProfileMedia) {
            this.hasProfileMedia = hasProfileMedia;
        }

        public ProfileMedia getProfileMedia() {
            return profileMedia;
        }

        public void setProfileMedia(ProfileMedia profileMedia) {
            this.profileMedia = profileMedia;
        }

    }
