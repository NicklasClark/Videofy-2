package com.cncoding.teazer.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.base.ProfileMedia;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PrivateProfile implements Parcelable {
    private String user_id;
    private String user_name;
    private String first_name;
    private String last_name;
    private int gender;
    private int account_type;
    private boolean has_profile_media;
    private ProfileMedia profile_media;

    protected PrivateProfile(Parcel in) {
        user_id = in.readString();
        user_name = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        gender = in.readInt();
        account_type = in.readInt();
        has_profile_media = in.readByte() != 0;
        profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(user_name);
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeInt(gender);
        dest.writeInt(account_type);
        dest.writeByte((byte) (has_profile_media ? 1 : 0));
        dest.writeParcelable(profile_media, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PrivateProfile> CREATOR = new Creator<PrivateProfile>() {
        @Override
        public PrivateProfile createFromParcel(Parcel in) {
            return new PrivateProfile(in);
        }

        @Override
        public PrivateProfile[] newArray(int size) {
            return new PrivateProfile[size];
        }
    };

    public String getUserId() {
        return user_id;
    }

    public String getUsername() {
        return user_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public int getGender() {
        return gender;
    }

    public int getAccountType() {
        return account_type;
    }

    public boolean hasProfileMedia() {
        return has_profile_media;
    }

    public ProfileMedia getProfileMedia() {
        return profile_media;
    }
}