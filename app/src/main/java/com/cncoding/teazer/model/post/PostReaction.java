package com.cncoding.teazer.model.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.react.MediaDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostReaction implements Parcelable {
    private int react_id;
    private int post_id;
    private String react_title;
    private int post_owner_id;
    private int likes;
    public int views;
    private boolean can_like;
    private boolean can_delete;
    private MediaDetail media_detail;
    private MiniProfile react_owner;
    private String reacted_at;
    @SerializedName("my_self")
    @Expose
    private Boolean mySelf;

    public PostReaction(int react_id, String react_title, int post_owner_id, int likes, int views, boolean can_like, boolean can_delete, MediaDetail media_detail, MiniProfile react_owner, String reacted_at) {
        this.react_id = react_id;
        this.react_title = react_title;
        this.post_owner_id = post_owner_id;
        this.likes = likes;
        this.views = views;
        this.can_like = can_like;
        this.can_delete = can_delete;
        this.media_detail = media_detail;
        this.react_owner = react_owner;
        this.reacted_at = reacted_at;
    }

    public Boolean getMySelf()
    {
        return mySelf;
    }

    protected PostReaction(Parcel in) {
        react_id = in.readInt();
        post_id = in.readInt();
        react_title = in.readString();
        post_owner_id = in.readInt();
        likes = in.readInt();
        views = in.readInt();
        can_like = in.readByte() != 0;
        can_delete = in.readByte() != 0;
        media_detail = in.readParcelable(MediaDetail.class.getClassLoader());
        react_owner = in.readParcelable(MiniProfile.class.getClassLoader());
        reacted_at = in.readString();
        byte tmpMySelf = in.readByte();
        mySelf = tmpMySelf == 0 ? null : tmpMySelf == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(react_id);
        dest.writeInt(post_id);
        dest.writeString(react_title);
        dest.writeInt(post_owner_id);
        dest.writeInt(likes);
        dest.writeInt(views);
        dest.writeByte((byte) (can_like ? 1 : 0));
        dest.writeByte((byte) (can_delete ? 1 : 0));
        dest.writeParcelable(media_detail, flags);
        dest.writeParcelable(react_owner, flags);
        dest.writeString(reacted_at);
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

    public int getReactId() {
        return react_id;
    }

    public int getPostId() {
        return post_id;
    }

    public String getReact_title() {
        return react_title;
    }

    public int getPostOwnerId() {
        return post_owner_id;
    }

    public int getLikes() {
        return likes;
    }

    public int getViews() {
        return views;
    }

    public boolean canLike() {
        return can_like;
    }

    public boolean canDelete() {
        return can_delete;
    }

    public MediaDetail getMediaDetail() {
        return media_detail;
    }

    public MiniProfile getReactOwner() {
        return react_owner;
    }

    public String getReactedAt() {
        return reacted_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostReaction that = (PostReaction) o;

        return react_id == that.react_id;
    }

    @Override
    public int hashCode() {
        return react_id;
    }

    public PostReaction(int react_id) {
        this.react_id = react_id;
    }
}