package com.cncoding.teazer.model.post;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.base.ProfileMedia;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@Entity(tableName = "ReactedUser")
public class ReactedUser extends BaseModel implements Parcelable {

    @SerializedName("user_id") @Expose private Integer user_id;
    @SerializedName("user_name") @Expose private String user_name;
    @SerializedName("first_name") @Expose private String first_name;
    @SerializedName("last_name") @Expose private String last_name;
    @SerializedName("is_blocked_you") @Expose private boolean is_blocked_you;
    @SerializedName("my_self") @Expose private boolean my_self;
    @SerializedName("has_profile_media") @Expose private boolean has_profile_media;
    @Embedded(prefix = "profileMedia_") @SerializedName("profile_media") @Expose private ProfileMedia profile_media;

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

    public boolean hasBlockedYou() {
        return is_blocked_you;
    }

    public boolean isMySelf() {
        return my_self;
    }

    public boolean hasProfileMedia() {
        return has_profile_media;
    }

    public ProfileMedia getProfileMedia() {
        return profile_media;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(10, 31)
                .append(user_id)
                .append(user_name)
                .append(first_name)
                .append(last_name)
                .append(my_self)
                .append(is_blocked_you)
                .append(has_profile_media)
                .append(profile_media)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof ReactedUser &&
                new EqualsBuilder()
                        .append(user_id, ((ReactedUser) obj).getUserId())
                        .append(user_name, ((ReactedUser) obj).getUserName())
                        .append(first_name, ((ReactedUser) obj).getFirstName())
                        .append(last_name, ((ReactedUser) obj).getLastName())
                        .append(my_self, ((ReactedUser) obj).isMySelf())
                        .append(is_blocked_you, ((ReactedUser) obj).hasBlockedYou())
                        .append(has_profile_media, ((ReactedUser) obj).hasProfileMedia())
                        .append(profile_media, ((ReactedUser) obj).getProfileMedia())
                        .isEquals();
    }
}