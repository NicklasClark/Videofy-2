package com.cncoding.teazer.model.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.base.CheckIn;
import com.cncoding.teazer.model.base.Medias;
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.base.TaggedUser;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostDetails implements Parcelable {
    private int post_id;
    private int posted_by;
    public int likes;
    public int total_reactions;
    public int total_tags;
    public boolean has_checkin;
    public String title;
    public boolean can_react;
    public boolean can_like;
    private boolean can_delete;
    private MiniProfile post_owner;
    private String created_at;                  //use DateTime.Now.ToString("yyyy-MM-ddThh:mm:sszzz");
    public CheckIn check_in;
    public ArrayList<Medias> medias;
    private ArrayList<TaggedUser> tagged_users;
    private ArrayList<ReactedUser> reacted_users;
    private ArrayList<Category> categories;

    public PostDetails(int post_id, int posted_by, int likes, int total_reactions, int total_tags, boolean has_checkin,
                       String title, boolean can_react, boolean can_like, boolean can_delete, MiniProfile post_owner,
                       String created_at, CheckIn check_in, ArrayList<Medias> medias, ArrayList<TaggedUser> tagged_users,
                       ArrayList<ReactedUser> reacted_users, ArrayList<Category> categories) {
        this.post_id = post_id;
        this.posted_by = posted_by;
        this.likes = likes;
        this.total_reactions = total_reactions;
        this.total_tags = total_tags;
        this.has_checkin = has_checkin;
        this.title = title;
        this.can_react = can_react;
        this.can_like = can_like;
        this.can_delete = can_delete;
        this.post_owner = post_owner;
        this.created_at = created_at;
        this.check_in = check_in;
        this.medias = medias;
        this.tagged_users = tagged_users;
        this.reacted_users = reacted_users;
        this.categories = categories;
    }

    protected PostDetails(Parcel in) {
        post_id = in.readInt();
        posted_by = in.readInt();
        likes = in.readInt();
        total_reactions = in.readInt();
        total_tags = in.readInt();
        has_checkin = in.readByte() != 0;
        title = in.readString();
        can_react = in.readByte() != 0;
        can_like = in.readByte() != 0;
        can_delete = in.readByte() != 0;
        post_owner = in.readParcelable(MiniProfile.class.getClassLoader());
        created_at = in.readString();
        check_in = in.readParcelable(CheckIn.class.getClassLoader());
        medias = in.createTypedArrayList(Medias.CREATOR);
        tagged_users = in.createTypedArrayList(TaggedUser.CREATOR);
        reacted_users = in.createTypedArrayList(ReactedUser.CREATOR);
        categories = in.createTypedArrayList(Category.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(post_id);
        dest.writeInt(posted_by);
        dest.writeInt(likes);
        dest.writeInt(total_reactions);
        dest.writeInt(total_tags);
        dest.writeByte((byte) (has_checkin ? 1 : 0));
        dest.writeString(title);
        dest.writeByte((byte) (can_react ? 1 : 0));
        dest.writeByte((byte) (can_like ? 1 : 0));
        dest.writeByte((byte) (can_delete ? 1 : 0));
        dest.writeParcelable(post_owner, flags);
        dest.writeString(created_at);
        dest.writeParcelable(check_in, flags);
        dest.writeTypedList(medias);
        dest.writeTypedList(tagged_users);
        dest.writeTypedList(reacted_users);
        dest.writeTypedList(categories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostDetails> CREATOR = new Creator<PostDetails>() {
        @Override
        public PostDetails createFromParcel(Parcel in) {
            return new PostDetails(in);
        }

        @Override
        public PostDetails[] newArray(int size) {
            return new PostDetails[size];
        }
    };

    public int getPostId() {
        return post_id;
    }

    public int getPostedBy() {
        return posted_by;
    }

    public int getLikes() {
        return likes;
    }

    public int getTotalReactions() {
        return total_reactions;
    }

    public int getTotalTags() {
        return total_tags;
    }

    public boolean hasCheckin() {
        return has_checkin;
    }

    public String getTitle() {
        return title;
    }

    public boolean canReact() {
        return can_react;
    }

    public boolean canLike() {
        return can_like;
    }

    public boolean canDelete() {
        return can_delete;
    }

    public MiniProfile getPostOwner() {
        return post_owner;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public CheckIn getCheckIn() {
        return check_in;
    }

    public ArrayList<Medias> getMedias() {
        return medias;
    }

    public ArrayList<TaggedUser> getTaggedUsers() {
        return tagged_users;
    }

    public ArrayList<ReactedUser> getReactedUsers() {
        return reacted_users;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}