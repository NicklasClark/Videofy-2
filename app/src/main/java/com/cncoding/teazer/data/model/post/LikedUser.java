package com.cncoding.teazer.data.model.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.ProfileMedia;
import com.cncoding.teazer.data.model.friends.FollowInfo;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 29/12/17.
 */

public class LikedUser extends BaseModel implements Parcelable {

    @SerializedName("user_id") @Expose private Integer userId;
    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("first_name") @Expose private String firstName;
    @SerializedName("last_name") @Expose private String lastName;
    @SerializedName("gender") @Expose private Integer gender;
    @SerializedName("my_self") @Expose private Boolean mySelf;
    @SerializedName("is_blocked_you") @Expose private Boolean isBlockedYou;
    @SerializedName("has_profile_media") @Expose private Boolean hasProfileMedia;
    @SerializedName("follow_info") @Expose private FollowInfo followInfo;
    @SerializedName("you_blocked") @Expose private Boolean youBlocked;
    @SerializedName("account_type") @Expose private Integer accountType;
    @SerializedName("profile_media") @Expose private ProfileMedia profileMedia;

    public LikedUser(Throwable error) {
        this.error = error;
    }

    protected LikedUser(Parcel in) {
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
        byte tmpMySelf = in.readByte();
        mySelf = tmpMySelf == 0 ? null : tmpMySelf == 1;
        byte tmpIsBlockedYou = in.readByte();
        isBlockedYou = tmpIsBlockedYou == 0 ? null : tmpIsBlockedYou == 1;
        byte tmpHasProfileMedia = in.readByte();
        hasProfileMedia = tmpHasProfileMedia == 0 ? null : tmpHasProfileMedia == 1;
        followInfo = in.readParcelable(FollowInfo.class.getClassLoader());
        byte tmpYouBlocked = in.readByte();
        youBlocked = tmpYouBlocked == 0 ? null : tmpYouBlocked == 1;
        if (in.readByte() == 0) {
            accountType = null;
        } else {
            accountType = in.readInt();
        }
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
        dest.writeByte((byte) (mySelf == null ? 0 : mySelf ? 1 : 2));
        dest.writeByte((byte) (isBlockedYou == null ? 0 : isBlockedYou ? 1 : 2));
        dest.writeByte((byte) (hasProfileMedia == null ? 0 : hasProfileMedia ? 1 : 2));
        dest.writeParcelable(followInfo, flags);
        dest.writeByte((byte) (youBlocked == null ? 0 : youBlocked ? 1 : 2));
        if (accountType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(accountType);
        }
        dest.writeParcelable(profileMedia, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LikedUser> CREATOR = new Creator<LikedUser>() {
        @Override
        public LikedUser createFromParcel(Parcel in) {
            return new LikedUser(in);
        }

        @Override
        public LikedUser[] newArray(int size) {
            return new LikedUser[size];
        }
    };

    public LikedUser setCallType(@CallType int callType) {
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

    public Boolean isMySelf() {
        return mySelf;
    }

    public void setMySelf(Boolean mySelf) {
        this.mySelf = mySelf;
    }

    public Boolean isBlockedYou() {
        return isBlockedYou;
    }

    public void setIsBlockedYou(Boolean isBlockedYou) {
        this.isBlockedYou = isBlockedYou;
    }

    public Boolean hasProfileMedia() {
        return hasProfileMedia;
    }

    public void setHasProfileMedia(Boolean hasProfileMedia) {
        this.hasProfileMedia = hasProfileMedia;
    }

    public Boolean isYouBlocked() {
        return youBlocked;
    }

    public void setYouBlocked(Boolean youBlocked) {
        this.youBlocked = youBlocked;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}
