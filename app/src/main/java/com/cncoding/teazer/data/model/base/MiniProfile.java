package com.cncoding.teazer.data.model.base;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.utilities.common.Annotations.Gender;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@Entity
public class MiniProfile extends BaseModel implements Parcelable {

    @SerializedName("user_id") @Expose private Integer userId;
    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("first_name") @Expose private String firstName;
    @SerializedName("last_name") @Expose private String lastName;
    @Gender @SerializedName("gender") @Expose private Integer gender;
    @SerializedName("account_type") @Expose private Integer accountType;
    @SerializedName("request_id") @Expose private Integer requestId;
    @SerializedName("request_recieved") @Expose private Boolean requestRecieved;
    @SerializedName("following") @Expose private Boolean following;
    @SerializedName("follower") @Expose private Boolean follower;
    @SerializedName("request_sent") @Expose private Boolean requestSent;
    @SerializedName("has_profile_media") @Expose private Boolean hasProfileMedia;
    @SerializedName("you_blocked") @Expose private Boolean youBlocked;
    @Embedded(prefix = "profileMedia_") @SerializedName("profile_media") @Expose private ProfileMedia profileMedia;

    public MiniProfile(int user_id, String user_name, String first_name, String last_name, boolean has_profile_media, ProfileMedia profile_media) {
        this.userId = user_id;
        this.userName = user_name;
        this.firstName = first_name;
        this.lastName = last_name;
        this.hasProfileMedia = has_profile_media;
        this.profileMedia = profile_media;
    }

    protected MiniProfile(Parcel in) {
        userId = in.readByte() == 0 ? null : in.readInt();
        userName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        accountType = in.readByte() == 0 ? null : in.readInt();
        requestId = in.readByte() == 0 ? null : in.readInt();
        byte tmpRequestRecieved = in.readByte();
        requestRecieved = tmpRequestRecieved == 0 ? null : tmpRequestRecieved == 1;
        byte tmpFollowing = in.readByte();
        following = tmpFollowing == 0 ? null : tmpFollowing == 1;
        byte tmpFollower = in.readByte();
        follower = tmpFollower == 0 ? null : tmpFollower == 1;
        byte tmpRequestSent = in.readByte();
        requestSent = tmpRequestSent == 0 ? null : tmpRequestSent == 1;
        gender = in.readByte() == 0 ? null : in.readInt();
        byte tmpHasProfileMedia = in.readByte();
        hasProfileMedia = tmpHasProfileMedia == 0 ? null : tmpHasProfileMedia == 1;
        byte tmpYouBlocked = in.readByte();
        youBlocked = tmpYouBlocked == 0 ? null : tmpYouBlocked == 1;
        profileMedia = in.readParcelable(ProfileMedia.class.getClassLoader());
    }

    public MiniProfile(Integer userId, String userName, String firstName, String lastName, Integer accountType,
                       Integer requestId, Boolean requestRecieved, Boolean following, Boolean follower, Boolean requestSent,
                       @Gender Integer gender, Boolean hasProfileMedia, Boolean youBlocked, ProfileMedia profileMedia) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
        this.requestId = requestId;
        this.requestRecieved = requestRecieved;
        this.following = following;
        this.follower = follower;
        this.requestSent = requestSent;
        this.gender = gender;
        this.hasProfileMedia = hasProfileMedia;
        this.youBlocked = youBlocked;
        this.profileMedia = profileMedia;
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
        if (accountType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(accountType);
        }
        if (requestId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(requestId);
        }
        dest.writeByte((byte) (requestRecieved == null ? 0 : requestRecieved ? 1 : 2));
        dest.writeByte((byte) (following == null ? 0 : following ? 1 : 2));
        dest.writeByte((byte) (follower == null ? 0 : follower ? 1 : 2));
        dest.writeByte((byte) (requestSent == null ? 0 : requestSent ? 1 : 2));
        if (gender == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(gender);
        }
        dest.writeByte((byte) (hasProfileMedia == null ? 0 : hasProfileMedia ? 1 : 2));
        dest.writeByte((byte) (youBlocked == null ? 0 : youBlocked ? 1 : 2));
        dest.writeParcelable(profileMedia, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MiniProfile> CREATOR = new Creator<MiniProfile>() {
        @Override
        public MiniProfile createFromParcel(Parcel in) {
            return new MiniProfile(in);
        }

        @Override
        public MiniProfile[] newArray(int size) {
            return new MiniProfile[size];
        }
    };

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

    @Gender public Integer getGender() {
        return gender;
    }

    public void setGender(@Gender Integer gender) {
        this.gender = gender;
    }

    public Boolean hasProfileMedia() {
        return hasProfileMedia;
    }

    public void setHasProfileMedia(Boolean hasProfileMedia) {
        this.hasProfileMedia = hasProfileMedia;
    }

    public Boolean getYouBlocked() {
        return youBlocked;
    }

    public void setYouBlocked(Boolean youBlocked) {
        this.youBlocked = youBlocked;
    }

    public ProfileMedia getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(ProfileMedia profileMedia) {
        this.profileMedia = profileMedia;
    }
}