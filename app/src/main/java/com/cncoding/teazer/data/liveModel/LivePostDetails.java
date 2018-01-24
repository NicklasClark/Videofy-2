package com.cncoding.teazer.data.liveModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import static com.cncoding.teazer.data.liveModel.MutableDataProvider.getMutableData;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@SuppressWarnings("unchecked")
public class LivePostDetails extends ViewModel {

    @SerializedName("post_id") @Expose private MutableLiveData<Integer> post_id;
    @SerializedName("posted_by") @Expose private MutableLiveData<Integer> posted_by;
    @SerializedName("likes") @Expose public MutableLiveData<Integer> likes;
    @SerializedName("total_reactions") @Expose public MutableLiveData<Integer> total_reactions;
    @SerializedName("total_tags") @Expose public MutableLiveData<Integer> total_tags;
    @SerializedName("has_checkin") @Expose public MutableLiveData<Boolean> has_checkin;
    @SerializedName("title") @Expose public MutableLiveData<String> title;
    @SerializedName("can_react") @Expose public MutableLiveData<Boolean> can_react;
    @SerializedName("can_like") @Expose public MutableLiveData<Boolean> can_like;
    @SerializedName("can_delete") @Expose private MutableLiveData<Boolean> can_delete;
    @SerializedName("post_owner") @Expose private MutableLiveData<LiveMiniProfile> post_owner;
    @SerializedName("created_at") @Expose private MutableLiveData<String> created_at;//use DateTime.Now.ToString("yyyy-MM-ddThh:mm:sszzz");
    @SerializedName("check_in") @Expose public MutableLiveData<LiveCheckIn> check_in;
    @SerializedName("medias") @Expose public MutableLiveData<LiveMedias> medias;
    @SerializedName("tagged_users") @Expose private MutableLiveData<LiveTaggedUser> tagged_users;
    @SerializedName("reacted_users") @Expose private MutableLiveData<LiveReactedUser> reacted_users;
    @SerializedName("categories") @Expose private MutableLiveData<LiveCategory> categories;

    public MutableLiveData<Integer> getPostId() {
        return (MutableLiveData<Integer>) getMutableData(post_id);
    }

    public MutableLiveData<Integer> getPostedBy() {
        return (MutableLiveData<Integer>) getMutableData(post_id);
    }

    public MutableLiveData<Integer> getLikes() {
        return (MutableLiveData<Integer>) getMutableData(post_id);
    }

    public MutableLiveData<Integer> getTotalReactions() {
        return (MutableLiveData<Integer>) getMutableData(post_id);
    }

    public MutableLiveData<Integer> getTotalTags() {
        return (MutableLiveData<Integer>) getMutableData(post_id);
    }

    public MutableLiveData<Boolean> hasCheckin() {
        return (MutableLiveData<Boolean>) getMutableData(post_id);
    }

    public MutableLiveData<String> getTitle() {
        return (MutableLiveData<String>) getMutableData(post_id);
    }

    public MutableLiveData<Boolean> canReact() {
        return (MutableLiveData<Boolean>) getMutableData(post_id);
    }

    public MutableLiveData<Boolean> canLike() {
        return (MutableLiveData<Boolean>) getMutableData(post_id);
    }

    public MutableLiveData<Boolean> canDelete() {
        return (MutableLiveData<Boolean>) getMutableData(post_id);
    }

    public MutableLiveData<LiveMiniProfile> getPostOwner() {
        return (MutableLiveData<LiveMiniProfile>) getMutableData(post_owner);
    }

    public MutableLiveData<String> getCreatedAt() {
        return (MutableLiveData<String>) getMutableData(post_id);
    }

    public MutableLiveData<LiveCheckIn> getCheckIn() {
        return (MutableLiveData<LiveCheckIn>) getMutableData(check_in);
    }

    public MutableLiveData<ArrayList<LiveMedias>> getMedias() {
        return (MutableLiveData<ArrayList<LiveMedias>>) getMutableData(medias);
    }

    public MutableLiveData<ArrayList<LiveTaggedUser>> getTaggedUsers() {
        return (MutableLiveData<ArrayList<LiveTaggedUser>>) getMutableData(tagged_users);
    }

    public MutableLiveData<ArrayList<LiveReactedUser>> getReactedUsers() {
        return (MutableLiveData<ArrayList<LiveReactedUser>>) getMutableData(reacted_users);
    }

    public MutableLiveData<ArrayList<LiveCategory>> getCategories() {
        return (MutableLiveData<ArrayList<LiveCategory>>) getMutableData(categories);
    }
}