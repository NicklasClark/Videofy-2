package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class MiniProfile implements Parcelable  {

    public MiniProfile(int user_id, String user_name, String first_name, String last_name, boolean has_profile_media, ProfileMedia profile_media) {
        this.userId = user_id;
        this.userName = user_name;
        this.firstName = first_name;
        this.lastName = last_name;
        this.hasProfileMedia = has_profile_media;
        this.profileMedia = profile_media;
    }

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
    @SerializedName("account_type")
    @Expose
    private Integer accountType;
    @SerializedName("request_id")
    @Expose
    private Integer requestId;
    @SerializedName("request_recieved")
    @Expose
    private Boolean requestRecieved;
    @SerializedName("following")
    @Expose
    private Boolean following;
    @SerializedName("follower")
    @Expose
    private Boolean follower;
    @SerializedName("request_sent")
    @Expose
    private Boolean requestSent;
    @SerializedName("has_profile_media")
    @Expose
    private Boolean hasProfileMedia;
    @SerializedName("you_blocked")
    @Expose
    private Boolean youBlocked;
    @SerializedName("profile_media")
    @Expose
    private ProfileMedia profileMedia;

//
//    private int user_id;
//    private String user_name;
//    private String first_name;
//    private String last_name;
//    private int account_type;
//    private boolean following;
//    private boolean follower;
//    private boolean request_sent;
//    private boolean has_profile_media;
//    private ProfileMedia profile_media;

//    public MiniProfile(int user_id, String user_name, String first_name, String last_name, int account_type, boolean following,
//                       boolean follower, boolean request_sent, boolean has_profile_media, ProfileMedia profile_media) {
//        this.user_id = user_id;
//        this.user_name = user_name;
//        this.first_name = first_name;
//        this.last_name = last_name;
//        this.account_type = account_type;
//        this.following = following;
//        this.follower = follower;
//        this.request_sent = request_sent;
//        this.has_profile_media = has_profile_media;
//        this.profile_media = profile_media;
//    }
//
//    protected MiniProfile(Parcel in) {
//        user_id = in.readInt();
//        user_name = in.readString();
//        first_name = in.readString();
//        last_name = in.readString();
//        account_type = in.readInt();
//        following = in.readByte() != 0;
//        follower = in.readByte() != 0;
//        request_sent = in.readByte() != 0;
//        has_profile_media = in.readByte() != 0;
//        profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(user_id);
//        dest.writeString(user_name);
//        dest.writeString(first_name);
//        dest.writeString(last_name);
//        dest.writeInt(account_type);
//        dest.writeByte((byte) (following ? 1 : 0));
//        dest.writeByte((byte) (follower ? 1 : 0));
//        dest.writeByte((byte) (request_sent ? 1 : 0));
//        dest.writeByte((byte) (has_profile_media ? 1 : 0));
//        dest.writeParcelable(profile_media, flags);
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<MiniProfile> CREATOR = new Creator<MiniProfile>() {
//        @Override
//        public MiniProfile createFromParcel(Parcel in) {
//            return new MiniProfile(in);
//        }
//
//        @Override
//        public MiniProfile[] newArray(int size) {
//            return new MiniProfile[size];
//        }
//    };
//
    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAccountType() {
        return accountType;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean isFollower() {
        return follower;
    }

    public boolean isRequestSent() {
        return requestSent;
    }

    public boolean hasProfileMedia() {
        return hasProfileMedia;
    }

    public ProfileMedia getProfileMedia() {
        return profileMedia;
    }

    public Boolean getRequestRecieved() {
        return requestRecieved;
    }

    public Boolean getYouBlocked() {
        return youBlocked;
    }

    public MiniProfile(Integer userId, String userName, String firstName, String lastName, Integer accountType, Integer requestId, Boolean requestRecieved, Boolean following, Boolean follower, Boolean requestSent, Boolean hasProfileMedia, Boolean youBlocked, ProfileMedia profileMedia) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
        this.requestId = requestId;
        this.requestRecieved = requestRecieved;
        this.following = following;
        this.follower = follower;
        this.requestSent = requestSent;
        this.hasProfileMedia = hasProfileMedia;
        this.youBlocked = youBlocked;
        this.profileMedia = profileMedia;
    }

    protected MiniProfile(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readInt();
        }
        userName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        if (in.readByte() == 0) {
            accountType = null;
        } else {
            accountType = in.readInt();
        }
        if (in.readByte() == 0) {
            requestId = null;
        } else {
            requestId = in.readInt();
        }
        byte tmpRequestRecieved = in.readByte();
        requestRecieved = tmpRequestRecieved == 0 ? null : tmpRequestRecieved == 1;
        byte tmpFollowing = in.readByte();
        following = tmpFollowing == 0 ? null : tmpFollowing == 1;
        byte tmpFollower = in.readByte();
        follower = tmpFollower == 0 ? null : tmpFollower == 1;
        byte tmpRequestSent = in.readByte();
        requestSent = tmpRequestSent == 0 ? null : tmpRequestSent == 1;
        byte tmpHasProfileMedia = in.readByte();
        hasProfileMedia = tmpHasProfileMedia == 0 ? null : tmpHasProfileMedia == 1;
        byte tmpYouBlocked = in.readByte();
        youBlocked = tmpYouBlocked == 0 ? null : tmpYouBlocked == 1;
        profileMedia = in.readParcelable(ProfileMedia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userId);
        }
        dest.writeString(userName);
        dest.writeString(firstName);
        dest.writeString(lastName);
        if (accountType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(accountType);
        }
        if (requestId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(requestId);
        }
        dest.writeByte((byte) (requestRecieved == null ? 0 : requestRecieved ? 1 : 2));
        dest.writeByte((byte) (following == null ? 0 : following ? 1 : 2));
        dest.writeByte((byte) (follower == null ? 0 : follower ? 1 : 2));
        dest.writeByte((byte) (requestSent == null ? 0 : requestSent ? 1 : 2));
        dest.writeByte((byte) (hasProfileMedia == null ? 0 : hasProfileMedia ? 1 : 2));
        dest.writeByte((byte) (youBlocked == null ? 0 : youBlocked ? 1 : 2));
        dest.writeParcelable(profileMedia, flags);
    }

    public Integer getRequestId() {
        return requestId;
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


}