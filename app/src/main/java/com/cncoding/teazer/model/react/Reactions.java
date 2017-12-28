package com.cncoding.teazer.model.react;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.base.ProfileMedia;
import com.cncoding.teazer.model.base.MediaDetail;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class Reactions implements Parcelable {
    private int post_id;
    private int react_id;
    private String react_title;
    private int reacted_by;
    private int likes;
    private int views;
    private boolean can_like;
    private ProfileMedia profile_media;
    private MediaDetail media_detail;
    private MiniProfile post_owner;
    private boolean can_delete;
    private String reacted_at;

    protected Reactions(Parcel in) {
        post_id = in.readInt();
        react_id = in.readInt();
        react_title = in.readString();
        reacted_by = in.readInt();
        likes = in.readInt();
        views = in.readInt();
        can_like = in.readByte() != 0;
        profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
        media_detail = in.readParcelable(MediaDetail.class.getClassLoader());
        post_owner = in.readParcelable(MiniProfile.class.getClassLoader());
        can_delete = in.readByte() != 0;
        reacted_at = in.readString();
    }

    public Reactions(int post_id, int react_id, String react_title, int reacted_by, int likes, int views,
                     boolean can_like, ProfileMedia profile_media, MediaDetail media_detail,
                     MiniProfile post_owner, boolean can_delete, String reacted_at) {
        this.post_id = post_id;
        this.react_id = react_id;
        this.react_title = react_title;
        this.reacted_by = reacted_by;
        this.likes = likes;
        this.views = views;
        this.can_like = can_like;
        this.profile_media = profile_media;
        this.media_detail = media_detail;
        this.post_owner = post_owner;
        this.can_delete = can_delete;
        this.reacted_at = reacted_at;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(post_id);
        dest.writeInt(react_id);
        dest.writeString(react_title);
        dest.writeInt(reacted_by);
        dest.writeInt(likes);
        dest.writeInt(views);
        dest.writeByte((byte) (can_like ? 1 : 0));
        dest.writeParcelable(profile_media, flags);
        dest.writeParcelable(media_detail, flags);
        dest.writeParcelable(post_owner, flags);
        dest.writeByte((byte) (can_delete ? 1 : 0));
        dest.writeString(reacted_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reactions> CREATOR = new Creator<Reactions>() {
        @Override
        public Reactions createFromParcel(Parcel in) {
            return new Reactions(in);
        }

        @Override
        public Reactions[] newArray(int size) {
            return new Reactions[size];
        }
    };

    public int getPostId() {
        return post_id;
    }

    public int getReactId() {
        return react_id;
    }

    public String getReactTitle() {
        return react_title;
    }

    public int getReactedBy() {
        return reacted_by;
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

    public ProfileMedia getProfileMedia() {
        return profile_media;
    }

    public MediaDetail getMediaDetail() {
        return media_detail;
    }

    public MiniProfile getPostOwner() {
        return post_owner;
    }

    public boolean canDelete() {
        return can_delete;
    }

    public String getReactedAt() {
        return reacted_at;
    }
}