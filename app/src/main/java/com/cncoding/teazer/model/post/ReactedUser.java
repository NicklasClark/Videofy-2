package com.cncoding.teazer.model.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.base.ProfileMedia;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReactedUser implements Parcelable {

    private Integer user_id;
    private String user_name;
    private String first_name;
    private String last_name;
    private boolean is_blocked_you;
    private boolean my_self;
    private boolean has_profile_media;
    private ProfileMedia profile_media;

    public ReactedUser(Integer user_id, String user_name, String first_name, String last_name,
                       boolean is_blocked_you, boolean my_self, boolean has_profile_media, ProfileMedia profile_media) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.is_blocked_you = is_blocked_you;
        this.my_self = my_self;
        this.has_profile_media = has_profile_media;
        this.profile_media = profile_media;
    }

    protected ReactedUser(Parcel in) {
        if (in.readByte() == 0) {
            user_id = null;
        } else {
            user_id = in.readInt();
        }
        user_name = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        is_blocked_you = in.readByte() != 0;
        my_self = in.readByte() != 0;
        has_profile_media = in.readByte() != 0;
        profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (user_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(user_id);
        }
        dest.writeString(user_name);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeByte((byte) (is_blocked_you ? 1 : 0));
        dest.writeByte((byte) (my_self ? 1 : 0));
        dest.writeByte((byte) (has_profile_media ? 1 : 0));
        dest.writeParcelable(profile_media, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReactedUser> CREATOR = new Creator<ReactedUser>() {
        @Override
        public ReactedUser createFromParcel(Parcel in) {
            return new ReactedUser(in);
        }

        @Override
        public ReactedUser[] newArray(int size) {
            return new ReactedUser[size];
        }
    };

    public Integer getUserId() {
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

    public Boolean hasBlockedYou() {
        return is_blocked_you;
    }

    public Boolean getMySelf() {
        return my_self;
    }

    public Boolean hasProfileMedia() {
        return has_profile_media;
    }

    public ProfileMedia getProfileMedia() {
        return profile_media;
    }
}