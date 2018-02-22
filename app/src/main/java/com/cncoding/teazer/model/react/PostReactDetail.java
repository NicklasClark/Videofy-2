package com.cncoding.teazer.model.react;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by amit on 26/12/17.
 */

public class PostReactDetail implements Parcelable {

    @SerializedName("react_id") @Expose private Integer reactId;
    @SerializedName("post_id") @Expose private Integer postId;
    @SerializedName("post_owner_id") @Expose private Integer postOwnerId;
    @SerializedName("react_title") @Expose private String reactTitle;
    @SerializedName("likes") @Expose private Integer likes;
    @SerializedName("views") @Expose private Integer views;
    @SerializedName("can_like") @Expose private Boolean canLike;
    @SerializedName("my_self") @Expose private Boolean mySelf;
    @SerializedName("can_delete") @Expose private Boolean canDelete;
    @SerializedName("media_detail") @Expose private MediaDetail mediaDetail;
    @SerializedName("react_owner") @Expose private ReactOwner reactOwner;
    @SerializedName("reacted_at") @Expose private String reactedAt;

    protected PostReactDetail(Parcel in) {
        if (in.readByte() == 0) {
            reactId = null;
        } else {
            reactId = in.readInt();
        }
        if (in.readByte() == 0) {
            postId = null;
        } else {
            postId = in.readInt();
        }
        if (in.readByte() == 0) {
            postOwnerId = null;
        } else {
            postOwnerId = in.readInt();
        }
        reactTitle = in.readString();
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
        byte tmpMySelf = in.readByte();
        mySelf = tmpMySelf == 0 ? null : tmpMySelf == 1;
        byte tmpCanDelete = in.readByte();
        canDelete = tmpCanDelete == 0 ? null : tmpCanDelete == 1;
        mediaDetail = in.readParcelable(MediaDetail.class.getClassLoader());
        reactedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (reactId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(reactId);
        }
        if (postId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(postId);
        }
        if (postOwnerId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(postOwnerId);
        }
        dest.writeString(reactTitle);
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
        dest.writeByte((byte) (mySelf == null ? 0 : mySelf ? 1 : 2));
        dest.writeByte((byte) (canDelete == null ? 0 : canDelete ? 1 : 2));
        dest.writeParcelable(mediaDetail, flags);
        dest.writeString(reactedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostReactDetail> CREATOR = new Creator<PostReactDetail>() {
        @Override
        public PostReactDetail createFromParcel(Parcel in) {
            return new PostReactDetail(in);
        }

        @Override
        public PostReactDetail[] newArray(int size) {
            return new PostReactDetail[size];
        }
    };

    public Integer getReactId() {
        return reactId;
    }

    public void setReactId(Integer reactId) {
        this.reactId = reactId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getPostOwnerId() {
        return postOwnerId;
    }

    public void setPostOwnerId(Integer postOwnerId) {
        this.postOwnerId = postOwnerId;
    }

    public String getReactTitle() {
        return reactTitle;
    }

    public void setReactTitle(String reactTitle) {
        this.reactTitle = reactTitle;
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

    public Boolean getCanLike() {
        return canLike;
    }

    public void setCanLike(Boolean canLike) {
        this.canLike = canLike;
    }

    public Boolean getMySelf() {
        return mySelf;
    }

    public void setMySelf(Boolean mySelf) {
        this.mySelf = mySelf;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public MediaDetail getMediaDetail() {
        return mediaDetail;
    }

    public void setMediaDetail(MediaDetail mediaDetail) {
        this.mediaDetail = mediaDetail;
    }

    public ReactOwner getReactOwner() {
        return reactOwner;
    }

    public void setReactOwner(ReactOwner reactOwner) {
        this.reactOwner = reactOwner;
    }

    public String getReactedAt() {
        return reactedAt;
    }

    public void setReactedAt(String reactedAt) {
        this.reactedAt = reactedAt;
    }

}
