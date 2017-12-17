package com.cncoding.teazer.model.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.base.ReactionMediaDetail;

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
    private ReactionMediaDetail media_detail;
    private MiniProfile react_owner;
    private String reacted_at;


    protected PostReaction(Parcel in) {
        react_id = in.readInt();
        post_id = in.readInt();
        react_title = in.readString();
        post_owner_id = in.readInt();
        likes = in.readInt();
        views = in.readInt();
        can_like = in.readByte() != 0;
        can_delete = in.readByte() != 0;
        media_detail = in.readParcelable(ReactionMediaDetail.class.getClassLoader());
        react_owner = in.readParcelable(MiniProfile.class.getClassLoader());
        reacted_at = in.readString();
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

    public ReactionMediaDetail getMediaDetail() {
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