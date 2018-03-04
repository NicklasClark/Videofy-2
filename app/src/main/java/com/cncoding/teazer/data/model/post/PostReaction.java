package com.cncoding.teazer.data.model.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.MediaDetail;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.base.ProfileMedia;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostReaction extends BaseModel implements Parcelable {

    @SerializedName("post_id") @Expose private int postId;
    @SerializedName("react_id") @Expose private int reactId;
    @SerializedName("reacted_by") @Expose private int reactedBy;
    @SerializedName("likes") @Expose private int likes;
    @SerializedName("views") @Expose public int views;
    @SerializedName("can_like") @Expose private boolean canLike;
    @SerializedName("react_title") @Expose private String reactTitle;
    @SerializedName("profile_media") @Expose private ProfileMedia profileMedia;
    @SerializedName("media_detail") @Expose private MediaDetail mediaDetail;
    @SerializedName("react_owner") @Expose private MiniProfile reactOwner;
    @SerializedName("post_owner") @Expose private MiniProfile postOwner;
    @SerializedName("reacted_at") @Expose private String reactedAt;
    @SerializedName("can_delete") @Expose private boolean canDelete;
    @SerializedName("my_self") @Expose private boolean mySelf;
    @SerializedName("post_owner_id") @Expose private int postOwnerId;

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

    public PostReaction(int reactId) {
        this.reactId = reactId;
    }

    public PostReaction(Throwable error) {
        this.error = error;
    }

    protected PostReaction(Parcel in) {
        postId = in.readInt();
        reactId = in.readInt();
        reactedBy = in.readInt();
        likes = in.readInt();
        views = in.readInt();
        canLike = in.readByte() != 0;
        reactTitle = in.readString();
        profileMedia = in.readParcelable(ProfileMedia.class.getClassLoader());
        mediaDetail = in.readParcelable(MediaDetail.class.getClassLoader());
        reactOwner = in.readParcelable(MiniProfile.class.getClassLoader());
        postOwner = in.readParcelable(MiniProfile.class.getClassLoader());
        reactedAt = in.readString();
        canDelete = in.readByte() != 0;
        mySelf = in.readByte() != 0;
        postOwnerId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(postId);
        dest.writeInt(reactId);
        dest.writeInt(reactedBy);
        dest.writeInt(likes);
        dest.writeInt(views);
        dest.writeByte((byte) (canLike ? 1 : 0));
        dest.writeString(reactTitle);
        dest.writeParcelable(profileMedia, flags);
        dest.writeParcelable(mediaDetail, flags);
        dest.writeParcelable(reactOwner, flags);
        dest.writeParcelable(postOwner, flags);
        dest.writeString(reactedAt);
        dest.writeByte((byte) (canDelete ? 1 : 0));
        dest.writeByte((byte) (mySelf ? 1 : 0));
        dest.writeInt(postOwnerId);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public PostReaction setCallType(@CallType int callType) {
        setCall(callType);
        return this;
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

    public MiniProfile getOwner() {
        return reactOwner != null ? reactOwner : postOwner;
    }

    public String getReactedAt() {
        return reactedAt;
    }

    public boolean getMySelf() {
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

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setReactId(int reactId) {
        this.reactId = reactId;
    }

    public int getReactedBy() {
        return reactedBy;
    }

    public void setReactedBy(int reactedBy) {
        this.reactedBy = reactedBy;
    }

    public boolean isCanLike() {
        return canLike;
    }

    public ProfileMedia getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(ProfileMedia profileMedia) {
        this.profileMedia = profileMedia;
    }

    public void setMediaDetail(MediaDetail mediaDetail) {
        this.mediaDetail = mediaDetail;
    }

    public void setReactOwner(MiniProfile reactOwner) {
        this.reactOwner = reactOwner;
    }

    public MiniProfile getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(MiniProfile postOwner) {
        this.postOwner = postOwner;
    }

    public void setReactedAt(String reactedAt) {
        this.reactedAt = reactedAt;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public void setMySelf(boolean mySelf) {
        this.mySelf = mySelf;
    }

    public void setPostOwnerId(int postOwnerId) {
        this.postOwnerId = postOwnerId;
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
        return new HashCodeBuilder(11, 31)
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