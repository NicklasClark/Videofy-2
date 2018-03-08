package com.cncoding.teazer.data.model.friends;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.ProfileMedia;
import com.cncoding.teazer.utilities.common.Annotations.AccountType;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created by farazhabib on 24/02/18.
 */

public class MyUserInfo extends BaseModel implements Parcelable {

    @SerializedName("user_id") @Expose private Integer userId;
    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("first_name") @Expose private String firstName;
    @SerializedName("last_name") @Expose private String lastName;
    @SerializedName("gender") @Expose private Integer gender;
    @AccountType @SerializedName("account_type") @Expose private Integer accountType;
    @SerializedName("has_profile_media") @Expose private Boolean hasProfileMedia;
    @SerializedName("following") @Expose private Boolean following;
    @SerializedName("follower") @Expose private Boolean follower;
    @SerializedName("request_sent") @Expose private Boolean requestSent;
    @SerializedName("follow_info") @Expose private FollowInfo followInfo;
    @SerializedName("profile_media") @Expose private ProfileMedia profileMedia;

    public MyUserInfo(Throwable error) {
        this.error = error;
    }

    protected MyUserInfo(Parcel in) {
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
        followInfo = in.readParcelable(FollowInfo.class.getClassLoader());
        profileMedia = in.readParcelable(ProfileMedia.class.getClassLoader());
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
        dest.writeParcelable(followInfo, flags);
        dest.writeParcelable(profileMedia, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyUserInfo> CREATOR = new Creator<MyUserInfo>() {
        @Override
        public MyUserInfo createFromParcel(Parcel in) {
            return new MyUserInfo(in);
        }

        @Override
        public MyUserInfo[] newArray(int size) {
            return new MyUserInfo[size];
        }
    };

    public MyUserInfo setCallType(@CallType int callType) {
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @AccountType public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(@AccountType Integer accountType) {
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

    public FollowInfo getFollowInfo() {
        return followInfo;
    }

    public void setFollowInfo(FollowInfo followInfo) {
        this.followInfo = followInfo;
    }

    public ProfileMedia getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(ProfileMedia profileMedia) {
        this.profileMedia = profileMedia;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 31)
                .append(userId)
                .append(userName)
                .append(firstName)
                .append(lastName)
                .append(gender)
                .append(accountType)
                .append(hasProfileMedia)
                .append(following)
                .append(follower)
                .append(requestSent)
                .append(followInfo)
                .append(profileMedia)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof MyUserInfo &&
                new EqualsBuilder()
                        .append(userId, ((MyUserInfo) obj).userId)
                        .append(userName, ((MyUserInfo) obj).userName)
                        .append(firstName, ((MyUserInfo) obj).firstName)
                        .append(lastName, ((MyUserInfo) obj).lastName)
                        .append(gender, ((MyUserInfo) obj).gender)
                        .append(accountType, ((MyUserInfo) obj).accountType)
                        .append(hasProfileMedia, ((MyUserInfo) obj).hasProfileMedia)
                        .append(following, ((MyUserInfo) obj).following)
                        .append(follower, ((MyUserInfo) obj).follower)
                        .append(requestSent, ((MyUserInfo) obj).requestSent)
                        .append(followInfo, ((MyUserInfo) obj).followInfo)
                        .append(profileMedia, ((MyUserInfo) obj).profileMedia)
                        .isEquals();
    }
}