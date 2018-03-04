package com.cncoding.teazer.data.model.react;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.base.MediaDetail;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.base.ProfileMedia;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class MyReactions implements Parcelable {

    @SerializedName("post_id") @Expose private Integer postId;
    @SerializedName("react_id") @Expose private Integer reactId;
    @SerializedName("reacted_by") @Expose private Integer reactedBy;
    @SerializedName("likes") @Expose private Integer likes;
    @SerializedName("views") @Expose private Integer views;
    @SerializedName("can_like") @Expose private Boolean canLike;
    @SerializedName("react_title") @Expose private String reactTitle;
    @SerializedName("profile_media") @Expose private ProfileMedia profileMedia;
    @SerializedName("media_detail") @Expose private MediaDetail mediaDetail;
    @SerializedName("post_owner") @Expose private MiniProfile postOwner;
    @SerializedName("can_delete") @Expose private Boolean canDelete;
    @SerializedName("reacted_at") @Expose private String reactedAt;

    public MyReactions(int postId, int reactId, String reactTitle, int reactedBy, int likes, int views,
                       boolean canLike, ProfileMedia profileMedia, MediaDetail mediaDetail,
                       MiniProfile postOwner, boolean canDelete, String reactedAt) {
        this.postId = postId;
        this.reactId = reactId;
        this.reactTitle = reactTitle;
        this.reactedBy = reactedBy;
        this.likes = likes;
        this.views = views;
        this.canLike = canLike;
        this.profileMedia = profileMedia;
        this.mediaDetail = mediaDetail;
        this.postOwner = postOwner;
        this.canDelete = canDelete;
        this.reactedAt = reactedAt;
    }

    protected MyReactions(Parcel in) {
        if (in.readByte() == 0) {
            postId = null;
        } else {
            postId = in.readInt();
        }
        if (in.readByte() == 0) {
            reactId = null;
        } else {
            reactId = in.readInt();
        }
        if (in.readByte() == 0) {
            reactedBy = null;
        } else {
            reactedBy = in.readInt();
        }
        if (in.readByte() == 0) {
            likes = null;
        } else {
            likes = in.readInt();
        }
        if (in.readByte() == 0) {
            views = null;
        } else {
            views = in.readInt();
        }
        byte tmpCanLike = in.readByte();
        canLike = tmpCanLike == 0 ? null : tmpCanLike == 1;
        reactTitle = in.readString();
        profileMedia = in.readParcelable(ProfileMedia.class.getClassLoader());
        mediaDetail = in.readParcelable(MediaDetail.class.getClassLoader());
        postOwner = in.readParcelable(MiniProfile.class.getClassLoader());
        byte tmpCanDelete = in.readByte();
        canDelete = tmpCanDelete == 0 ? null : tmpCanDelete == 1;
        reactedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (postId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(postId);
        }
        if (reactId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(reactId);
        }
        if (reactedBy == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(reactedBy);
        }
        if (likes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(likes);
        }
        if (views == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(views);
        }
        dest.writeByte((byte) (canLike == null ? 0 : canLike ? 1 : 2));
        dest.writeString(reactTitle);
        dest.writeParcelable(profileMedia, flags);
        dest.writeParcelable(mediaDetail, flags);
        dest.writeParcelable(postOwner, flags);
        dest.writeByte((byte) (canDelete == null ? 0 : canDelete ? 1 : 2));
        dest.writeString(reactedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyReactions> CREATOR = new Creator<MyReactions>() {
        @Override
        public MyReactions createFromParcel(Parcel in) {
            return new MyReactions(in);
        }

        @Override
        public MyReactions[] newArray(int size) {
            return new MyReactions[size];
        }
    };

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getReactId() {
        return reactId;
    }

    public void setReactId(Integer reactId) {
        this.reactId = reactId;
    }

    public Integer getReactedBy() {
        return reactedBy;
    }

    public void setReactedBy(Integer reactedBy) {
        this.reactedBy = reactedBy;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Boolean canLike() {
        return canLike;
    }

    public void setCanLike(Boolean canLike) {
        this.canLike = canLike;
    }

    public String getReactTitle() {
        return reactTitle;
    }

    public void setReactTitle(String reactTitle) {
        this.reactTitle = reactTitle;
    }

    public ProfileMedia getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(ProfileMedia profileMedia) {
        this.profileMedia = profileMedia;
    }

    public MediaDetail getMediaDetail() {
        return mediaDetail;
    }

    public void setMediaDetail(MediaDetail mediaDetail) {
        this.mediaDetail = mediaDetail;
    }

    public MiniProfile getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(MiniProfile postOwner) {
        this.postOwner = postOwner;
    }

    public Boolean canDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getReactedAt() {
        return reactedAt;
    }

    public void setReactedAt(String reactedAt) {
        this.reactedAt = reactedAt;
    }
}