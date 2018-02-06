package com.cncoding.teazer.model.react;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.base.MediaDetail;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReactionDetails implements Parcelable {
    private int react_id;
    private int post_id;
    private int post_owner_id;
    private String react_title;
    private int likes;
    private int views;
    private boolean can_like;
    private boolean my_self;
    private boolean can_delete;
    private MediaDetail media_detail;
    private MiniProfile react_owner;
    private String reacted_at;

    protected ReactionDetails(Parcel in) {
        react_id = in.readInt();
        post_id = in.readInt();
        post_owner_id = in.readInt();
        react_title = in.readString();
        likes = in.readInt();
        views = in.readInt();
        can_like = in.readByte() != 0;
        my_self = in.readByte() != 0;
        can_delete = in.readByte() != 0;
        media_detail = in.readParcelable(MediaDetail.class.getClassLoader());
        react_owner = in.readParcelable(MiniProfile.class.getClassLoader());
        reacted_at = in.readString();
    }

    public ReactionDetails(int react_id, int post_id, int post_owner_id, String react_title, int likes, int views,
                           boolean can_like, boolean my_self, boolean can_delete, MediaDetail media_detail,
                           MiniProfile react_owner, String reacted_at) {
        this.react_id = react_id;
        this.post_id = post_id;
        this.post_owner_id = post_owner_id;
        this.react_title = react_title;
        this.likes = likes;
        this.views = views;
        this.can_like = can_like;
        this.my_self = my_self;
        this.can_delete = can_delete;
        this.media_detail = media_detail;
        this.react_owner = react_owner;
        this.reacted_at = reacted_at;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(react_id);
        dest.writeInt(post_id);
        dest.writeInt(post_owner_id);
        dest.writeString(react_title);
        dest.writeInt(likes);
        dest.writeInt(views);
        dest.writeByte((byte) (can_like ? 1 : 0));
        dest.writeByte((byte) (my_self ? 1 : 0));
        dest.writeByte((byte) (can_delete ? 1 : 0));
        dest.writeParcelable(media_detail, flags);
        dest.writeParcelable(react_owner, flags);
        dest.writeString(reacted_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReactionDetails> CREATOR = new Creator<ReactionDetails>() {
        @Override
        public ReactionDetails createFromParcel(Parcel in) {
            return new ReactionDetails(in);
        }

        @Override
        public ReactionDetails[] newArray(int size) {
            return new ReactionDetails[size];
        }
    };

    public int getReactId() {
        return react_id;
    }

    public int getPostId() {
        return post_id;
    }

    public int getPostOwnerId() {
        return post_owner_id;
    }

    public String getReactTitle() {
        return react_title;
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

    public boolean isMySelf() {
        return my_self;
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
}