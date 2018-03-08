package com.cncoding.teazer.data.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.friends.PublicProfile;
import com.cncoding.teazer.data.model.profile.Preference;
import com.cncoding.teazer.data.model.user.userProfile.TopReactedUser;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class UserProfile extends BaseModel implements Parcelable {

    @SerializedName("user_profile") @Expose private PublicProfile userProfile;
    @SerializedName("followers") @Expose private int followers;
    @SerializedName("followings") @Expose private int followings;
    @SerializedName("total_videos") @Expose private int total_videos;
    @SerializedName("can_change_password") @Expose private boolean canChangePassword;
    @SerializedName("total_reactions") @Expose private Integer totalReactions;
    @SerializedName("total_profile_likes") @Expose private Integer totalProfileLikes;
    @SerializedName("preferences") @Expose private List<Preference> preferences;
    @SerializedName("top_reacted_users") @Expose private List<TopReactedUser> topReactedUsers;

    public UserProfile(Throwable error) {
        this.error = error;
    }

    protected UserProfile(Parcel in) {
        userProfile = in.readParcelable(PublicProfile.class.getClassLoader());
        followers = in.readInt();
        followings = in.readInt();
        total_videos = in.readInt();
        canChangePassword = in.readByte() != 0;
        if (in.readByte() == 0) {
            totalReactions = null;
        } else {
            totalReactions = in.readInt();
        }
        if (in.readByte() == 0) {
            totalProfileLikes = null;
        } else {
            totalProfileLikes = in.readInt();
        }
        preferences = in.createTypedArrayList(Preference.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userProfile, flags);
        dest.writeInt(followers);
        dest.writeInt(followings);
        dest.writeInt(total_videos);
        dest.writeByte((byte) (canChangePassword ? 1 : 0));
        if (totalReactions == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalReactions);
        }
        if (totalProfileLikes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalProfileLikes);
        }
        dest.writeTypedList(preferences);
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

    public UserProfile setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }


    public UserProfile(PublicProfile userProfile, int followers, int followings, int total_videos, boolean canChangePassword) {
        this.userProfile = userProfile;
        this.followers = followers;
        this.followings = followings;
        this.total_videos = total_videos;
        this.canChangePassword = canChangePassword;
    }


    public PublicProfile getUserProfile() {
        return userProfile;
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
        return canChangePassword;
    }

    public Integer getTotalReactions() {
        return totalReactions;
    }

    public void setCanChangePassword(boolean can_change_password) {
        this.canChangePassword = can_change_password;
    }

    public Integer getTotalProfileLikes() {
        return totalProfileLikes;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public List<TopReactedUser> getTopReactedUsers() {
        return topReactedUsers;
    }
}