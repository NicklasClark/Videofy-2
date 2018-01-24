package com.cncoding.teazer.data.model.base;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@Entity(tableName = "MiniProfile")
//        foreignKeys = {@ForeignKey(entity = PostDetails.class, parentColumns = {"post_id"}, childColumns = {"postId"})})
public class MiniProfile extends ViewModel implements Parcelable {

    @PrimaryKey @SerializedName("user_id") @Expose private int userId;
//    private int postId;
    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("first_name") @Expose private String firstName;
    @SerializedName("last_name") @Expose private String lastName;
    @SerializedName("account_type") @Expose private int accountType;
    @SerializedName("request_id") @Expose private int requestId;
    @SerializedName("request_recieved") @Expose private boolean requestRecieved;
    @SerializedName("following") @Expose private boolean following;
    @SerializedName("follower") @Expose private boolean follower;
    @SerializedName("request_sent") @Expose private boolean requestSent;
    @SerializedName("has_profile_media") @Expose private boolean hasProfileMedia;
    @SerializedName("you_blocked") @Expose private boolean youBlocked;
    @Embedded(prefix = "profileMedia_") @SerializedName("profile_media") @Expose private ProfileMedia profileMedia;

    protected MiniProfile(Parcel in) {
        userId = in.readInt();
//        postId = in.readInt();
        userName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        accountType = in.readInt();
        requestId = in.readInt();
        requestRecieved = in.readByte() != 0;
        following = in.readByte() != 0;
        follower = in.readByte() != 0;
        requestSent = in.readByte() != 0;
        hasProfileMedia = in.readByte() != 0;
        youBlocked = in.readByte() != 0;
        profileMedia = in.readParcelable(ProfileMedia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
//        dest.writeInt(postId);
        dest.writeString(userName);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeInt(accountType);
        dest.writeInt(requestId);
        dest.writeByte((byte) (requestRecieved ? 1 : 0));
        dest.writeByte((byte) (following ? 1 : 0));
        dest.writeByte((byte) (follower ? 1 : 0));
        dest.writeByte((byte) (requestSent ? 1 : 0));
        dest.writeByte((byte) (hasProfileMedia ? 1 : 0));
        dest.writeByte((byte) (youBlocked ? 1 : 0));
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

    public boolean getRequestRecieved() {
        return requestRecieved;
    }

    public boolean getYouBlocked() {
        return youBlocked;
    }

//    public int getPostId() {
//        return postId;
//    }
}