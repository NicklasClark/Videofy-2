package com.cncoding.teazer.data.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.friends.PublicProfile;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class Profile implements Parcelable {
    private int total_videos;
    private int account_type;
    private boolean can_join;
    private boolean has_send_join_request;
    private int join_request_id;
    private PrivateProfile private_profile;
    private PublicProfile public_profile;
    private int followers;
    private int followings;

    protected Profile(Parcel in) {
        total_videos = in.readInt();
        account_type = in.readInt();
        can_join = in.readByte() != 0;
        has_send_join_request = in.readByte() != 0;
        join_request_id = in.readInt();
        private_profile = in.readParcelable(PrivateProfile.class.getClassLoader());
        public_profile = in.readParcelable(PublicProfile.class.getClassLoader());
        followers = in.readInt();
        followings = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(total_videos);
        dest.writeInt(account_type);
        dest.writeByte((byte) (can_join ? 1 : 0));
        dest.writeByte((byte) (has_send_join_request ? 1 : 0));
        dest.writeInt(join_request_id);
        dest.writeParcelable(private_profile, flags);
        dest.writeParcelable(public_profile, flags);
        dest.writeInt(followers);
        dest.writeInt(followings);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public int getTotalVideos() {
        return total_videos;
    }

    public int getAccountType() {
        return account_type;
    }

    public boolean canJoin() {
        return can_join;
    }

    public boolean hasSentJoinRequest() {
        return has_send_join_request;
    }

    public int getJoinRequestId() {
        return join_request_id;
    }

    public PrivateProfile getPrivateProfile() {
        return private_profile;
    }

    public PublicProfile getPublicProfile() {
        return public_profile;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowings() {
        return followings;
    }
}