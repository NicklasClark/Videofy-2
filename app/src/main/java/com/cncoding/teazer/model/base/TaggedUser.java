package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class TaggedUser implements Parcelable {
    private int tag_id;
    private int user_id;
    private String user_name;
    private String first_name;
    private String last_name;
    private boolean my_self;
    private boolean is_blocked_you;
    boolean has_profile_media;
    ProfileMedia profile_media;

    public TaggedUser(int tag_id, int user_id, String user_name, String first_name, String last_name,
                      boolean my_self, boolean is_blocked_you, boolean has_profile_media, ProfileMedia profile_media) {
        this.tag_id = tag_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.my_self = my_self;
        this.is_blocked_you = is_blocked_you;
        this.has_profile_media = has_profile_media;
        this.profile_media = profile_media;
    }

    protected TaggedUser(Parcel in) {
        tag_id = in.readInt();
        user_id = in.readInt();
        user_name = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        my_self = in.readByte() != 0;
        is_blocked_you = in.readByte() != 0;
        has_profile_media = in.readByte() != 0;
        profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tag_id);
        dest.writeInt(user_id);
        dest.writeString(user_name);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeByte((byte) (my_self ? 1 : 0));
        dest.writeByte((byte) (is_blocked_you ? 1 : 0));
        dest.writeByte((byte) (has_profile_media ? 1 : 0));
        dest.writeParcelable(profile_media, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TaggedUser> CREATOR = new Creator<TaggedUser>() {
        @Override
        public TaggedUser createFromParcel(Parcel in) {
            return new TaggedUser(in);
        }

        @Override
        public TaggedUser[] newArray(int size) {
            return new TaggedUser[size];
        }
    };

    public int getTagId() {
        return tag_id;
    }

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

    public boolean isMySelf() {
        return my_self;
    }

    public boolean isBlockedYou() {
        return is_blocked_you;
    }

    public boolean hasProfileMedia() {
        return has_profile_media;
    }

    public ProfileMedia getProfileMedia() {
        return profile_media;
    }
}