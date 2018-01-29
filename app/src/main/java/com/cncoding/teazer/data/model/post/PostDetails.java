package com.cncoding.teazer.data.model.post;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.react.ReactionDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.base.CheckIn;
import com.cncoding.teazer.data.model.base.Medias;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.base.TaggedUser;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@Entity(tableName = "PostDetails")
//        foreignKeys = [(ForeignKey(entity = Dialog.class, parentColumns ["id"], childColumns = ["dialogId"]))]
public class PostDetails extends ViewModel implements Parcelable {

    @PrimaryKey @SerializedName("post_id") @Expose private int postId;
    @SerializedName("posted_by") @Expose private Integer postedBy;
    @SerializedName("likes") @Expose public Integer likes;
    @SerializedName("total_reactions") @Expose public Integer totalReactions;
    @SerializedName("total_tags") @Expose public Integer totalTags;
    @SerializedName("has_checkin") @Expose public Boolean hasCheckin;
    @SerializedName("title") @Expose public String title;
    @SerializedName("can_react") @Expose public Boolean canReact;
    @SerializedName("can_like") @Expose public Boolean canLike;
    @SerializedName("can_delete") @Expose private Boolean canDelete;
    @SerializedName("is_hided") @Expose private Boolean isHided;
    @SerializedName("is_hided_all") @Expose private Boolean isHidedAll;
    @Embedded(prefix = "postOwner_") @SerializedName("post_owner") @Expose private MiniProfile postOwner;
    @SerializedName("created_at") @Expose private String createdAt;                  //use DateTime.Now.ToString("yyyy-MM-ddThh:mm:sszzz");
    @Embedded(prefix = "checkIn_") @SerializedName("check_in") @Expose public CheckIn checkIn;
    @SerializedName("medias") @Expose public ArrayList<Medias> medias;
    @SerializedName("reactions") @Expose private ArrayList<ReactionDetails> reactions;
    @SerializedName("tagged_users") @Expose private ArrayList<TaggedUser> taggedUsers;
    @SerializedName("reacted_users") @Expose private ArrayList<ReactedUser> reactedUsers;
    @SerializedName("categories") @Expose private ArrayList<Category> categories;

    protected PostDetails(Parcel in) {
        postId = in.readInt();
        postedBy = in.readByte() == 0 ? null : in.readInt();
        likes = in.readByte() == 0 ? null : in.readInt();
        totalReactions = in.readByte() == 0 ? null : in.readInt();
        totalTags = in.readByte() == 0 ? null : in.readInt();
        byte tmpHasCheckin = in.readByte();
        hasCheckin = tmpHasCheckin == 0 ? null : tmpHasCheckin == 1;
        title = in.readString();
        byte tmpCanReact = in.readByte();
        canReact = tmpCanReact == 0 ? null : tmpCanReact == 1;
        byte tmpCanLike = in.readByte();
        canLike = tmpCanLike == 0 ? null : tmpCanLike == 1;
        byte tmpCanDelete = in.readByte();
        canDelete = tmpCanDelete == 0 ? null : tmpCanDelete == 1;
        byte tmpIsHided = in.readByte();
        isHided = tmpIsHided == 0 ? null : tmpIsHided == 1;
        byte tmpIsHidedAll = in.readByte();
        isHidedAll = tmpIsHidedAll == 0 ? null : tmpIsHidedAll == 1;
        postOwner = in.readParcelable(MiniProfile.class.getClassLoader());
        createdAt = in.readString();
        checkIn = in.readParcelable(CheckIn.class.getClassLoader());
        medias = in.createTypedArrayList(Medias.CREATOR);
        reactions = in.createTypedArrayList(ReactionDetails.CREATOR);
        taggedUsers = in.createTypedArrayList(TaggedUser.CREATOR);
        reactedUsers = in.createTypedArrayList(ReactedUser.CREATOR);
        categories = in.createTypedArrayList(Category.CREATOR);
    }

    public PostDetails(int postId, Integer postedBy, Integer likes, Integer totalReactions, Integer totalTags,
                       Boolean hasCheckin, String title, Boolean canReact, Boolean canLike, Boolean canDelete,
                       Boolean isHided, Boolean isHidedAll, MiniProfile postOwner, String createdAt, CheckIn checkIn,
                       ArrayList<Medias> medias, ArrayList<ReactionDetails> reactions, ArrayList<TaggedUser> taggedUsers,
                       ArrayList<ReactedUser> reactedUsers, ArrayList<Category> categories) {
        this.postId = postId;
        this.postedBy = postedBy;
        this.likes = likes;
        this.totalReactions = totalReactions;
        this.totalTags = totalTags;
        this.hasCheckin = hasCheckin;
        this.title = title;
        this.canReact = canReact;
        this.canLike = canLike;
        this.canDelete = canDelete;
        this.isHided = isHided;
        this.isHidedAll = isHidedAll;
        this.postOwner = postOwner;
        this.createdAt = createdAt;
        this.checkIn = checkIn;
        this.medias = medias;
        this.reactions = reactions;
        this.taggedUsers = taggedUsers;
        this.reactedUsers = reactedUsers;
        this.categories = categories;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(postId);
        if (postedBy == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(postedBy);
        }
        if (likes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(likes);
        }
        if (totalReactions == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalReactions);
        }
        if (totalTags == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalTags);
        }
        dest.writeByte((byte) (hasCheckin == null ? 0 : hasCheckin ? 1 : 2));
        dest.writeString(title);
        dest.writeByte((byte) (canReact == null ? 0 : canReact ? 1 : 2));
        dest.writeByte((byte) (canLike == null ? 0 : canLike ? 1 : 2));
        dest.writeByte((byte) (canDelete == null ? 0 : canDelete ? 1 : 2));
        dest.writeByte((byte) (isHided == null ? 0 : isHided ? 1 : 2));
        dest.writeByte((byte) (isHidedAll == null ? 0 : isHidedAll ? 1 : 2));
        dest.writeParcelable(postOwner, flags);
        dest.writeString(createdAt);
        dest.writeParcelable(checkIn, flags);
        dest.writeTypedList(medias);
        dest.writeTypedList(reactions);
        dest.writeTypedList(taggedUsers);
        dest.writeTypedList(reactedUsers);
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
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Integer getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Integer postedBy) {
        this.postedBy = postedBy;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getTotalReactions() {
        return totalReactions;
    }

    public void setTotalReactions(Integer totalReactions) {
        this.totalReactions = totalReactions;
    }

    public Integer getTotalTags() {
        return totalTags;
    }

    public void setTotalTags(Integer totalTags) {
        this.totalTags = totalTags;
    }

    public Boolean hasCheckin() {
        return hasCheckin;
    }

    public void setHasCheckin(Boolean hasCheckin) {
        this.hasCheckin = hasCheckin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean canReact() {
        return canReact;
    }

    public void setCanReact(Boolean canReact) {
        this.canReact = canReact;
    }

    public Boolean canLike() {
        return canLike;
    }

    public void setCanLike(Boolean canLike) {
        this.canLike = canLike;
    }

    public Boolean canDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public Boolean isHided() {
        return isHided;
    }

    public void setHided(Boolean hided) {
        isHided = hided;
    }

    public Boolean isHidedAll() {
        return isHidedAll;
    }

    public void setHidedAll(Boolean hidedAll) {
        isHidedAll = hidedAll;
    }

    public MiniProfile getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(MiniProfile postOwner) {
        this.postOwner = postOwner;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CheckIn getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(CheckIn checkIn) {
        this.checkIn = checkIn;
    }

    public ArrayList<Medias> getMedias() {
        return medias;
    }

    public void setMedias(ArrayList<Medias> medias) {
        this.medias = medias;
    }

    public ArrayList<ReactionDetails> getReactions() {
        return reactions;
    }

    public void setReactions(ArrayList<ReactionDetails> reactions) {
        this.reactions = reactions;
    }

    public ArrayList<TaggedUser> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(ArrayList<TaggedUser> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    public ArrayList<ReactedUser> getReactedUsers() {
        return reactedUsers;
    }

    public void setReactedUsers(ArrayList<ReactedUser> reactedUsers) {
        this.reactedUsers = reactedUsers;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}