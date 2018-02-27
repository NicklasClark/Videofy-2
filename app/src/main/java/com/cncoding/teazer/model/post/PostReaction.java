package com.cncoding.teazer.model.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.base.MediaDetail;
import com.cncoding.teazer.model.base.MiniProfile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostReaction extends BaseModel implements Parcelable {

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

    public Boolean getMySelf() {
        return mySelf;
    }

    public void setReactTitle(String reactTitle) {
        this.reactTitle = reactTitle;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setCanLike(boolean canLike) {
        this.canLike = canLike;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) &&
                o instanceof PostReaction &&
                new EqualsBuilder()
                        .append(reactId, ((PostReaction) o).reactId)
                        .append(postId, ((PostReaction) o).postId)
                        .append(reactTitle, ((PostReaction) o).reactTitle)
                        .append(postOwnerId, ((PostReaction) o).postOwnerId)
                        .append(likes, ((PostReaction) o).likes)
                        .append(views, ((PostReaction) o).views)
                        .append(canLike, ((PostReaction) o).canLike)
                        .append(canDelete, ((PostReaction) o).canDelete)
                        .append(reactedAt, ((PostReaction) o).reactedAt)
                        .append(mySelf, ((PostReaction) o).mySelf)
                        .isEquals();

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(10, 31)
                .append(reactId)
                .append(postId)
                .append(reactTitle)
                .append(postOwnerId)
                .append(likes)
                .append(views)
                .append(canLike)
                .append(canDelete)
                .append(reactedAt)
                .append(mySelf)
                .toHashCode();
    }
}