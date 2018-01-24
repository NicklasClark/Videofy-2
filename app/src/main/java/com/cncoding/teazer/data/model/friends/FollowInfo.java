package com.cncoding.teazer.data.model.friends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 22/11/17.
 */


public class FollowInfo {

        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("request_id")
        @Expose
        private Integer requestId;
        @SerializedName("following")
        @Expose
        private Boolean following;
        @SerializedName("follower")
        @Expose
        private Boolean follower;
        @SerializedName("request_sent")
        @Expose
        private Boolean requestSent;
        @SerializedName("request_received")
        @Expose
        private Boolean requestReceived;
        @SerializedName("is_blocked_you")
        @Expose
        private Boolean isBlockedYou;
        @SerializedName("you_blocked")
        @Expose
        private Boolean youBlocked;

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getRequestId() {
            return requestId;
        }

        public void setRequestId(Integer requestId) {
            this.requestId = requestId;
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

        public Boolean getRequestReceived() {
            return requestReceived;
        }

        public void setRequestReceived(Boolean requestReceived) {
            this.requestReceived = requestReceived;
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

    }
