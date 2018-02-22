package com.cncoding.teazer.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.friends.PublicProfile;
import com.cncoding.teazer.utilities.Annotations.CallType;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class UserProfile extends BaseModel implements Parcelable {
    private PublicProfile user_profile;
    private int followers;
    private int followings;
    private int total_videos;
    private boolean can_change_password;

    public UserProfile(Throwable error) {
        this.error = error;
    }

    public UserProfile setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    protected UserProfile(Parcel in) {
        user_profile = in.readParcelable(PublicProfile.class.getClassLoader());
        followers = in.readInt();
        followings = in.readInt();
        total_videos = in.readInt();
        can_change_password = in.readByte() != 0;
    }

    public UserProfile(PublicProfile user_profile, int followers, int followings, int total_videos, boolean can_change_password) {
        this.user_profile = user_profile;
        this.followers = followers;
        this.followings = followings;
        this.total_videos = total_videos;
        this.can_change_password = can_change_password;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user_profile, flags);
        dest.writeInt(followers);
        dest.writeInt(followings);
        dest.writeInt(total_videos);
        dest.writeByte((byte) (can_change_password ? 1 : 0));
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

    public void setcanChangePassword(boolean can_change_password) {
        this.can_change_password = can_change_password;
    }
}