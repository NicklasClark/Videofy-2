package com.cncoding.teazer.data.model.friends;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created by farazhabib on 22/11/17.
 */


public class FollowInfo implements Parcelable {

        @SerializedName("user_id") @Expose private Integer userId;
        @SerializedName("request_id") @Expose private Integer requestId;
        @SerializedName("following") @Expose private Boolean following;
        @SerializedName("follower") @Expose private Boolean follower;
        @SerializedName("request_sent") @Expose private Boolean requestSent;
        @SerializedName("request_received") @Expose private Boolean requestReceived;
        @SerializedName("is_blocked_you") @Expose private Boolean isBlockedYou;
        @SerializedName("you_blocked") @Expose private Boolean youBlocked;

    protected FollowInfo(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        if (in.readByte() == 0) {
            requestId = null;
        } else {
            requestId = in.readInt();
        }
        byte tmpFollowing = in.readByte();
        following = tmpFollowing == 0 ? null : tmpFollowing == 1;
        byte tmpFollower = in.readByte();
        follower = tmpFollower == 0 ? null : tmpFollower == 1;
        byte tmpRequestSent = in.readByte();
        requestSent = tmpRequestSent == 0 ? null : tmpRequestSent == 1;
        byte tmpRequestReceived = in.readByte();
        requestReceived = tmpRequestReceived == 0 ? null : tmpRequestReceived == 1;
        byte tmpIsBlockedYou = in.readByte();
        isBlockedYou = tmpIsBlockedYou == 0 ? null : tmpIsBlockedYou == 1;
        byte tmpYouBlocked = in.readByte();
        youBlocked = tmpYouBlocked == 0 ? null : tmpYouBlocked == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        if (requestId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(requestId);
        }
        dest.writeByte((byte) (following == null ? 0 : following ? 1 : 2));
        dest.writeByte((byte) (follower == null ? 0 : follower ? 1 : 2));
        dest.writeByte((byte) (requestSent == null ? 0 : requestSent ? 1 : 2));
        dest.writeByte((byte) (requestReceived == null ? 0 : requestReceived ? 1 : 2));
        dest.writeByte((byte) (isBlockedYou == null ? 0 : isBlockedYou ? 1 : 2));
        dest.writeByte((byte) (youBlocked == null ? 0 : youBlocked ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FollowInfo> CREATOR = new Creator<FollowInfo>() {
        @Override
        public FollowInfo createFromParcel(Parcel in) {
            return new FollowInfo(in);
        }

        @Override
        public FollowInfo[] newArray(int size) {
            return new FollowInfo[size];
        }
    };

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

        public Boolean isRequestReceived() {
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 31)
                .append(userId)
                .append(requestId)
                .append(following)
                .append(follower)
                .append(requestSent)
                .append(requestReceived)
                .append(isBlockedYou)
                .append(youBlocked)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof FollowInfo &&
                new EqualsBuilder()
                        .append(userId, ((FollowInfo) obj).userId)
                        .append(requestId, ((FollowInfo) obj).requestId)
                        .append(following, ((FollowInfo) obj).following)
                        .append(follower, ((FollowInfo) obj).follower)
                        .append(requestSent, ((FollowInfo) obj).requestSent)
                        .append(requestReceived, ((FollowInfo) obj).requestReceived)
                        .append(isBlockedYou, ((FollowInfo) obj).isBlockedYou)
                        .append(youBlocked, ((FollowInfo) obj).youBlocked)
                        .isEquals();
    }
}