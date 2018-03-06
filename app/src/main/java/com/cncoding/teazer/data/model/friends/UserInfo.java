package com.cncoding.teazer.data.model.friends;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.Medias;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.cncoding.teazer.utilities.common.Annotations.Gender;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created by farazhabib on 17/11/17.
 */

public class UserInfo extends BaseModel implements Parcelable{

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

    public UserInfo(Throwable error) {
        this.error = error;
    }

    protected UserInfo(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        userName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        if (in.readByte() == 0) {
            gender = null;
        } else {
            gender = in.readInt();
        }
        byte tmpIsBlockedYou = in.readByte();
        isBlockedYou = tmpIsBlockedYou == 0 ? null : tmpIsBlockedYou == 1;
        byte tmpYouBlocked = in.readByte();
        youBlocked = tmpYouBlocked == 0 ? null : tmpYouBlocked == 1;
        byte tmpMySelf = in.readByte();
        mySelf = tmpMySelf == 0 ? null : tmpMySelf == 1;
        if (in.readByte() == 0) {
            accountType = null;
        } else {
            accountType = in.readInt();
        }
        byte tmpHasProfileMedia = in.readByte();
        hasProfileMedia = tmpHasProfileMedia == 0 ? null : tmpHasProfileMedia == 1;
        byte tmpFollowing = in.readByte();
        following = tmpFollowing == 0 ? null : tmpFollowing == 1;
        byte tmpFollower = in.readByte();
        follower = tmpFollower == 0 ? null : tmpFollower == 1;
        byte tmpRequestSent = in.readByte();
        requestSent = tmpRequestSent == 0 ? null : tmpRequestSent == 1;
        if (in.readByte() == 0) {
            requestId = null;
        } else {
            requestId = in.readInt();
        }
        byte tmpRequestRecieved = in.readByte();
        requestRecieved = tmpRequestRecieved == 0 ? null : tmpRequestRecieved == 1;
        profileMedia = in.readParcelable(Medias.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(userName);
        dest.writeString(firstName);
        dest.writeString(lastName);
        if (gender == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(gender);
        }
        dest.writeByte((byte) (isBlockedYou == null ? 0 : isBlockedYou ? 1 : 2));
        dest.writeByte((byte) (youBlocked == null ? 0 : youBlocked ? 1 : 2));
        dest.writeByte((byte) (mySelf == null ? 0 : mySelf ? 1 : 2));
        if (accountType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(accountType);
        }
        dest.writeByte((byte) (hasProfileMedia == null ? 0 : hasProfileMedia ? 1 : 2));
        dest.writeByte((byte) (following == null ? 0 : following ? 1 : 2));
        dest.writeByte((byte) (follower == null ? 0 : follower ? 1 : 2));
        dest.writeByte((byte) (requestSent == null ? 0 : requestSent ? 1 : 2));
        if (requestId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(requestId);
        }
        dest.writeByte((byte) (requestRecieved == null ? 0 : requestRecieved ? 1 : 2));
        dest.writeParcelable(profileMedia, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public UserInfo setCallType(@CallType int callType) {
        setCall(callType);
        return this;
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

    public Boolean isBlockedYou() {
        return isBlockedYou;
    }

    public void setIsBlockedYou(Boolean isBlockedYou) {
        this.isBlockedYou = isBlockedYou;
    }

    public Boolean isYouBlocked() {
        return youBlocked;
    }

    public void setYouBlocked(Boolean youBlocked) {
        this.youBlocked = youBlocked;
    }

    public Boolean isMySelf() {
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

    public Boolean hasProfileMedia() {
        return hasProfileMedia;
    }

    public void setHasProfileMedia(Boolean hasProfileMedia) {
        this.hasProfileMedia = hasProfileMedia;
    }

    public Boolean isFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

    public Boolean isFollower() {
        return follower;
    }

    public void setFollower(Boolean follower) {
        this.follower = follower;
    }

    public Boolean isRequestSent() {
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

    public Boolean isRequestRecieved() {
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 31)
                .append(userId)
                .append(userName)
                .append(firstName)
                .append(lastName)
                .append(gender)
                .append(isBlockedYou)
                .append(youBlocked)
                .append(mySelf)
                .append(accountType)
                .append(hasProfileMedia)
                .append(following)
                .append(follower)
                .append(requestSent)
                .append(requestId)
                .append(requestRecieved)
                .append(profileMedia)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof UserInfo &&
                new EqualsBuilder()
                        .append(userId, ((UserInfo) obj).userId)
                        .append(userName, ((UserInfo) obj).userName)
                        .append(firstName, ((UserInfo) obj).firstName)
                        .append(lastName, ((UserInfo) obj).lastName)
                        .append(gender, ((UserInfo) obj).gender)
                        .append(isBlockedYou, ((UserInfo) obj).isBlockedYou)
                        .append(youBlocked, ((UserInfo) obj).youBlocked)
                        .append(mySelf, ((UserInfo) obj).mySelf)
                        .append(accountType, ((UserInfo) obj).accountType)
                        .append(hasProfileMedia, ((UserInfo) obj).hasProfileMedia)
                        .append(following, ((UserInfo) obj).following)
                        .append(follower, ((UserInfo) obj).follower)
                        .append(requestSent, ((UserInfo) obj).requestSent)
                        .append(requestId, ((UserInfo) obj).requestId)
                        .append(requestRecieved, ((UserInfo) obj).requestRecieved)
                        .append(profileMedia, ((UserInfo) obj).profileMedia)
                        .isEquals();
    }
}