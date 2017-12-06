package com.cncoding.teazer.model.profile.reaction;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.profile.followerprofile.ProfileMedia_;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class PostOwner implements Parcelable {


    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("has_profile_media")
    @Expose
    private Boolean hasProfileMedia;
    @SerializedName("profile_media")
    @Expose
    private ProfileMedia_ profileMedia;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getHasProfileMedia() {
        return hasProfileMedia;
    }

    public void setHasProfileMedia(Boolean hasProfileMedia) {
        this.hasProfileMedia = hasProfileMedia;
    }

    public ProfileMedia_ getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(ProfileMedia_ profileMedia) {
        this.profileMedia = profileMedia;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeValue(this.hasProfileMedia);
        dest.writeParcelable(this.profileMedia, flags);
    }

    public PostOwner() {
    }

    protected PostOwner(Parcel in) {
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userName = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.hasProfileMedia = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.profileMedia = in.readParcelable(ProfileMedia_.class.getClassLoader());
    }

    public static final Parcelable.Creator<PostOwner> CREATOR = new Parcelable.Creator<PostOwner>() {
        @Override
        public PostOwner createFromParcel(Parcel source) {
            return new PostOwner(source);
        }

        @Override
        public PostOwner[] newArray(int size) {
            return new PostOwner[size];
        }
    };
}