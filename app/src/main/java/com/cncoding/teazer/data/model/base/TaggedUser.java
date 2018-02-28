package com.cncoding.teazer.data.model.base;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.utilities.common.Annotations.Gender;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@Entity(tableName = "TaggedUser")
public class TaggedUser extends BaseModel implements Parcelable {

    @SerializedName("tag_id") @Expose private int tag_id;
    @SerializedName("user_id") @Expose private int user_id;
    @SerializedName("user_name") @Expose private String user_name;
    @SerializedName("first_name") @Expose private String first_name;
    @SerializedName("last_name") @Expose private String last_name;
    @SerializedName("my_self") @Expose private boolean my_self;
    @Gender @SerializedName("gender") @Expose private int gender;
    @SerializedName("is_blocked_you") @Expose private boolean is_blocked_you;
    @SerializedName("has_profile_media") @Expose private  boolean has_profile_media;
    @Embedded(prefix = "profileMedia_") @SerializedName("profile_media") @Expose private ProfileMedia profile_media;

    public TaggedUser(int tag_id, int user_id, String user_name, String first_name, String last_name,
                      boolean my_self, int gender, boolean is_blocked_you, boolean has_profile_media, ProfileMedia profile_media) {
        this.tag_id = tag_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.my_self = my_self;
        this.gender = gender;
        this.is_blocked_you = is_blocked_you;
        this.has_profile_media = has_profile_media;
        this.profile_media = profile_media;
    }

    private TaggedUser(Parcel in) {
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(10, 31)
                .append(tag_id)
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
        return this == obj || obj instanceof TaggedUser &&
                new EqualsBuilder()
                        .append(tag_id, ((TaggedUser) obj).getTagId())
                        .append(user_id, ((TaggedUser) obj).getUserId())
                        .append(user_name, ((TaggedUser) obj).getUserName())
                        .append(first_name, ((TaggedUser) obj).getFirstName())
                        .append(last_name, ((TaggedUser) obj).getLastName())
                        .append(my_self, ((TaggedUser) obj).isMySelf())
                        .append(is_blocked_you, ((TaggedUser) obj).isBlockedYou())
                        .append(has_profile_media, ((TaggedUser) obj).hasProfileMedia())
                        .append(profile_media, ((TaggedUser) obj).getProfileMedia())
                        .isEquals();
    }
}