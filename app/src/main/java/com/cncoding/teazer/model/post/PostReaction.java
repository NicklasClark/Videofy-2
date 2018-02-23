package com.cncoding.teazer.model.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.base.MediaDetail;
import com.cncoding.teazer.model.base.MiniProfile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostReaction implements Parcelable {

    @SerializedName("react_id") @Expose private int reactId;
    @SerializedName("post_id") @Expose private int postId;
    @SerializedName("react_title") @Expose private String reactTitle;
    @SerializedName("post_owner_id") @Expose private int postOwnerId;
    @SerializedName("likes") @Expose private int likes;
    @SerializedName("views") @Expose public int views;
    @SerializedName("can_like") @Expose private boolean canLike;
    @SerializedName("can_delete") @Expose private boolean canDelete;
    @SerializedName("media_detail") @Expose private MediaDetail mediaDetail;
    @SerializedName("react_owner") @Expose private MiniProfile reactOwner;
    @SerializedName("reacted_at") @Expose private String reactedAt;
    @SerializedName("my_self") @Expose private Boolean mySelf;

    public PostReaction(int react_id, String reactTitle, int postOwnerId, int likes, int views, boolean canLike,
                        boolean canDelete, MediaDetail mediaDetail, MiniProfile reactOwner, String reactedAt) {
        this.reactId = react_id;
        this.reactTitle = reactTitle;
        this.postOwnerId = postOwnerId;
        this.likes = likes;
        this.views = views;
        this.canLike = canLike;
        this.canDelete = canDelete;
        this.mediaDetail = mediaDetail;
        this.reactOwner = reactOwner;
        this.reactedAt = reactedAt;
    }

    protected PostReaction(Parcel in) {
        reactId = in.readInt();
        postId = in.readInt();
        reactTitle = in.readString();
        postOwnerId = in.readInt();
        likes = in.readInt();
        views = in.readInt();
        canLike = in.readByte() != 0;
        canDelete = in.readByte() != 0;
        mediaDetail = in.readParcelable(MediaDetail.class.getClassLoader());
        reactOwner = in.readParcelable(MiniProfile.class.getClassLoader());
        reactedAt = in.readString();
        byte tmpMySelf = in.readByte();
        mySelf = tmpMySelf == 0 ? null : tmpMySelf == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(reactId);
        dest.writeInt(postId);
        dest.writeString(reactTitle);
        dest.writeInt(postOwnerId);
        dest.writeInt(likes);
        dest.writeInt(views);
        dest.writeByte((byte) (canLike ? 1 : 0));
        dest.writeByte((byte) (canDelete ? 1 : 0));
        dest.writeParcelable(mediaDetail, flags);
        dest.writeParcelable(reactOwner, flags);
        dest.writeString(reactedAt);
        dest.writeByte((byte) (mySelf == null ? 0 : mySelf ? 1 : 2));
    }

    public static final Creator<PostReaction> CREATOR = new Creator<PostReaction>() {
        @Override
        public PostReaction createFromParcel(Parcel in) {
            return new PostReaction(in);
        }

        @Override
        public PostReaction[] newArray(int size) {
            return new PostReaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostReaction that = (PostReaction) o;

        return reactId == that.reactId;
    }

    @Override
    public int hashCode() {
        return reactId;
    }

    public PostReaction(int reactId) {
        this.reactId = reactId;
    }

    public int getReactId() {
        return reactId;
    }

    public int getPostId() {
        return postId;
    }

    public String getReactTitle() {
        return reactTitle;
    }

    public int getPostOwnerId() {
        return postOwnerId;
    }

    public int getLikes() {
        return likes;
    }

    public int getViews() {
        return views;
    }

    public boolean canLike() {
        return canLike;
    }

    public boolean canDelete() {
        return canDelete;
    }

    public MediaDetail getMediaDetail() {
        return mediaDetail;
    }

    public MiniProfile getReactOwner() {
        return reactOwner;
    }

    public String getReactedAt() {
        return reactedAt;
    }

    public Boolean getMySelf()
    {
        return mySelf;
    }
}