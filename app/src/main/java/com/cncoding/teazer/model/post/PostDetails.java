package com.cncoding.teazer.model.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.base.CheckIn;
import com.cncoding.teazer.model.base.Medias;
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.base.TaggedUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostDetails implements Parcelable
{
    @SerializedName("is_hided")
    @Expose
    private Boolean isHided;
    @SerializedName("is_hided_all")
    @Expose
    private Boolean isHidedAll;
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
                       ArrayList<ReactedUser> reacted_users, ArrayList<Category> categories,Boolean isHided, Boolean isHidedAll) {
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
        this.isHided = isHided;
        this.isHidedAll = isHidedAll;
    }


    protected PostDetails(Parcel in) {
        byte tmpIsHided = in.readByte();
        isHided = tmpIsHided == 0 ? null : tmpIsHided == 1;
        byte tmpIsHidedAll = in.readByte();
        isHidedAll = tmpIsHidedAll == 0 ? null : tmpIsHidedAll == 1;
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
        dest.writeByte((byte) (isHided == null ? 0 : isHided ? 1 : 2));
        dest.writeByte((byte) (isHidedAll == null ? 0 : isHidedAll ? 1 : 2));
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

    public Boolean getHided() {
        return isHided;
    }

    public Boolean getHidedAll() {
        return isHidedAll;
    }

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

    public void setHided(Boolean hided) {
        isHided = hided;
    }

    public void setHidedAll(Boolean hidedAll) {
        isHidedAll = hidedAll;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public void setPosted_by(int posted_by) {
        this.posted_by = posted_by;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setTotal_reactions(int total_reactions) {
        this.total_reactions = total_reactions;
    }

    public void setTotal_tags(int total_tags) {
        this.total_tags = total_tags;
    }

    public void setHas_checkin(boolean has_checkin) {
        this.has_checkin = has_checkin;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCan_react(boolean can_react) {
        this.can_react = can_react;
    }

    public void setCan_like(boolean can_like) {
        this.can_like = can_like;
    }

    public void setCan_delete(boolean can_delete) {
        this.can_delete = can_delete;
    }

    public void setPost_owner(MiniProfile post_owner) {
        this.post_owner = post_owner;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setCheck_in(CheckIn check_in) {
        this.check_in = check_in;
    }

    public void setMedias(ArrayList<Medias> medias) {
        this.medias = medias;
    }

    public void setTagged_users(ArrayList<TaggedUser> tagged_users) {
        this.tagged_users = tagged_users;
    }

    public void setReacted_users(ArrayList<ReactedUser> reacted_users) {
        this.reacted_users = reacted_users;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}