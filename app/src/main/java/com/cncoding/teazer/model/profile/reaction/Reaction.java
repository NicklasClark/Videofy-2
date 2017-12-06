package com.cncoding.teazer.model.profile.reaction;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class Reaction implements Parcelable{


    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("react_id")
    @Expose
    private Integer reactId;
    @SerializedName("reacted_by")
    @Expose
    private Integer reactedBy;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("can_like")
    @Expose
    private Boolean canLike;
    @SerializedName("react_title")
    @Expose
    private String reactTitle;
    @SerializedName("profile_media")
    @Expose
    private ProfileMedia profileMedia;
    @SerializedName("media_detail")
    @Expose
    private MediaDetail mediaDetail;
    @SerializedName("post_owner")
    @Expose
    private PostOwner postOwner;
    @SerializedName("can_delete")
    @Expose
    private Boolean canDelete;
    @SerializedName("reacted_at")
    @Expose
    private String reactedAt;

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

    public Boolean getCanLike() {
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

    public PostOwner getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(PostOwner postOwner) {
        this.postOwner = postOwner;
    }

    public Boolean getCanDelete() {
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.postId);
        dest.writeValue(this.reactId);
        dest.writeValue(this.reactedBy);
        dest.writeValue(this.likes);
        dest.writeValue(this.views);
        dest.writeValue(this.canLike);
        dest.writeString(this.reactTitle);
        dest.writeParcelable(this.profileMedia, flags);
        dest.writeParcelable(this.mediaDetail, flags);
        dest.writeParcelable(this.postOwner, flags);
        dest.writeValue(this.canDelete);
        dest.writeString(this.reactedAt);
    }

    public Reaction() {
    }

    protected Reaction(Parcel in) {
        this.postId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.reactId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.reactedBy = (Integer) in.readValue(Integer.class.getClassLoader());
        this.likes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.views = (Integer) in.readValue(Integer.class.getClassLoader());
        this.canLike = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.reactTitle = in.readString();
        this.profileMedia = in.readParcelable(ProfileMedia.class.getClassLoader());
        this.mediaDetail = in.readParcelable(MediaDetail.class.getClassLoader());
        this.postOwner = in.readParcelable(PostOwner.class.getClassLoader());
        this.canDelete = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.reactedAt = in.readString();
    }

    public static final Creator<Reaction> CREATOR = new Creator<Reaction>() {
        @Override
        public Reaction createFromParcel(Parcel source) {
            return new Reaction(source);
        }

        @Override
        public Reaction[] newArray(int size) {
            return new Reaction[size];
        }
    };
}