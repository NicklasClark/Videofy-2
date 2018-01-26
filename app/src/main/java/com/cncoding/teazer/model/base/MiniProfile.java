package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.cncoding.teazer.model.base.MiniProfile.FEMALE;
import static com.cncoding.teazer.model.base.MiniProfile.MALE;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class MiniProfile implements Parcelable  {

    public static final int MALE = 1;
    public static final int FEMALE = 2;

    @SerializedName("user_id") @Expose private Integer userId;
    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("first_name") @Expose private String firstName;
    @SerializedName("last_name") @Expose private String lastName;
    @SerializedName("account_type") @Expose private Integer accountType;
    @SerializedName("request_id") @Expose private Integer requestId;
    @SerializedName("request_recieved") @Expose private Boolean requestRecieved;
    @SerializedName("following") @Expose private Boolean following;
    @SerializedName("follower") @Expose private Boolean follower;
    @SerializedName("request_sent") @Expose private Boolean requestSent;
    @SerializedName("gender") @Expose private Integer gender;
    @SerializedName("has_profile_media") @Expose private Boolean hasProfileMedia;
    @SerializedName("you_blocked") @Expose private Boolean youBlocked;
    @SerializedName("profile_media") @Expose private ProfileMedia profileMedia;

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

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAccountType() {
        return accountType;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isFollower() {
        return follower;
    }

    public boolean isRequestSent() {
        return requestSent;
    }

    public boolean hasProfileMedia() {
        return hasProfileMedia;
    }

    public ProfileMedia getProfileMedia() {
        return profileMedia;
    }

    public Boolean getRequestRecieved() {
        return requestRecieved;
    }

    public Boolean getYouBlocked() {
        return youBlocked;
    }

    public Integer getGender() {
        return gender;
    }
}