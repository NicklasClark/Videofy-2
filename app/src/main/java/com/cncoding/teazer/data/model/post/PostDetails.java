package com.cncoding.teazer.data.model.post;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

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

@Entity(tableName = "PostDetails"
//        foreignKeys = [(ForeignKey(entity = Dialog.class, parentColumns ["id"], childColumns = ["dialogId"]))]
)
public class PostDetails extends ViewModel implements Parcelable {

    @PrimaryKey @SerializedName("post_id") @Expose private int post_id;
    @SerializedName("posted_by") @Expose private int posted_by;
    @SerializedName("likes") @Expose public int likes;
    @SerializedName("total_reactions") @Expose public int total_reactions;
    @SerializedName("total_tags") @Expose public int total_tags;
    @SerializedName("has_checkin") @Expose public boolean has_checkin;
    @SerializedName("title") @Expose public String title;
    @SerializedName("can_react") @Expose public boolean can_react;
    @SerializedName("can_like") @Expose public boolean can_like;
    @SerializedName("can_delete") @Expose private boolean can_delete;
    @Embedded(prefix = "postOwner_") @SerializedName("post_owner") @Expose private MiniProfile post_owner;
    @SerializedName("created_at") @Expose private String created_at;                  //use DateTime.Now.ToString("yyyy-MM-ddThh:mm:sszzz");
    @Embedded(prefix = "checkIn_") @SerializedName("check_in") @Expose public CheckIn check_in;
    @SerializedName("medias") @Expose public ArrayList<Medias> medias;
    @SerializedName("tagged_users") @Expose private ArrayList<TaggedUser> tagged_users;
    @SerializedName("reacted_users") @Expose private ArrayList<ReactedUser> reacted_users;
    @SerializedName("categories") @Expose private ArrayList<Category> categories;

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

    public void setLikes(int likes) {
        this.likes = likes;
    }
}