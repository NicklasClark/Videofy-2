package com.cncoding.teazer.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.friends.PublicProfile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class UserProfile implements Parcelable {
    private PublicProfile user_profile;
    private int followers;
    private int followings;
    private int total_videos;
    private boolean can_change_password;
    @SerializedName("total_reactions")
    @Expose
    private Integer totalReactions;



    public UserProfile(PublicProfile user_profile, int followers, int followings, int total_videos, boolean can_change_password) {
        this.user_profile = user_profile;
        this.followers = followers;
        this.followings = followings;
        this.total_videos = total_videos;
        this.can_change_password = can_change_password;
    }

    public UserProfile(PublicProfile user_profile, int followers, int followings, int total_videos, boolean can_change_password, Integer totalReactions) {
        this.user_profile = user_profile;
        this.followers = followers;
        this.followings = followings;
        this.total_videos = total_videos;
        this.can_change_password = can_change_password;
        this.totalReactions = totalReactions;
    }

    protected UserProfile(Parcel in) {
        user_profile = in.readParcelable(PublicProfile.class.getClassLoader());
        followers = in.readInt();
        followings = in.readInt();
        total_videos = in.readInt();
        can_change_password = in.readByte() != 0;
        if (in.readByte() == 0) {
            totalReactions = null;
        } else {
            totalReactions = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user_profile, flags);
        dest.writeInt(followers);
        dest.writeInt(followings);
        dest.writeInt(total_videos);
        dest.writeByte((byte) (can_change_password ? 1 : 0));
        if (totalReactions == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalReactions);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public PublicProfile getUserProfile() {
        return user_profile;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowings() {
        return followings;
    }

    public int getTotalVideos() {
        return total_videos;
    }

    public boolean canChangePassword() {
        return can_change_password;
    }

    public Integer getTotalReactions() {
        return totalReactions;
    }

    public void setcanChangePassword(boolean can_change_password) {
        this.can_change_password = can_change_password;
    }
}