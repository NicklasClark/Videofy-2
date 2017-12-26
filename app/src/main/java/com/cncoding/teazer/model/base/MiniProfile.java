package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class MiniProfile implements Parcelable {

    private int user_id;
    private String user_name;
    private String first_name;
    private String last_name;
    private int account_type;
    private boolean following;
    private boolean follower;
    private boolean request_sent;
    private boolean has_profile_media;
    private ProfileMedia profile_media;

    public MiniProfile(int user_id, String user_name, String first_name, String last_name, int account_type, boolean following,
                       boolean follower, boolean request_sent, boolean has_profile_media, ProfileMedia profile_media) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.account_type = account_type;
        this.following = following;
        this.follower = follower;
        this.request_sent = request_sent;
        this.has_profile_media = has_profile_media;
        this.profile_media = profile_media;
    }

    public MiniProfile(int user_id, String user_name, String first_name, String last_name, boolean has_profile_media, ProfileMedia profile_media) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.has_profile_media = has_profile_media;
        this.profile_media = profile_media;
    }

    protected MiniProfile(Parcel in) {
        user_id = in.readInt();
        user_name = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        account_type = in.readInt();
        following = in.readByte() != 0;
        follower = in.readByte() != 0;
        request_sent = in.readByte() != 0;
        has_profile_media = in.readByte() != 0;
        profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(user_id);
        dest.writeString(user_name);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeInt(account_type);
        dest.writeByte((byte) (following ? 1 : 0));
        dest.writeByte((byte) (follower ? 1 : 0));
        dest.writeByte((byte) (request_sent ? 1 : 0));
        dest.writeByte((byte) (has_profile_media ? 1 : 0));
        dest.writeParcelable(profile_media, flags);
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
        return user_id;
    }

    public String getUserName() {
        return user_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public int getAccountType() {
        return account_type;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isFollower() {
        return follower;
    }

    public boolean isRequestSent() {
        return request_sent;
    }

    public boolean hasProfileMedia() {
        return has_profile_media;
    }

    public ProfileMedia getProfileMedia() {
        return profile_media;
    }
}