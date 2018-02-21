package com.cncoding.teazer.utilities.diffutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.cncoding.teazer.model.base.CheckIn;
import com.cncoding.teazer.model.base.TaggedUser;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.post.ReactedUser;

import java.util.List;
import java.util.Objects;

/**
 *
 * Created by Prem $ on 12/25/2017.
 */

public class PostsDiffCallback extends DiffUtil.Callback {

    public static final String DIFF_POST_DETAILS = "postDetails";
    private static final String DIFF_LIKES = "likes";
    private static final String DIFF_TOTAL_REACTIONS = "totalReactions";
    private static final String DIFF_REACTED_USERS = "reactedUsers";
    private static final String DIFF_TOTAL_TAGS = "totalTags";
    private static final String DIFF_TAGS = "tags";
    private static final String DIFF_HAS_CHECKIN = "hasCheckIn";
    private static final String DIFF_TITLE = "title";
    private static final String DIFF_CAN_REACT = "canReact";
    private static final String DIFF_CAN_LIKE = "canLike";
    private static final String DIFF_IS_HIDED = "isHided";
    private static final String DIFF_IS_HIDED_ALL = "isHidedAll";
    private static final String DIFF_CHECKIN = "checkIn";
    private static final String DIFF_REACTIONS = "reactions";

    private List<PostDetails> oldPostDetails;
    private List<PostDetails> newPostDetails;

    public PostsDiffCallback(List<PostDetails> oldPostDetails, List<PostDetails> newPostDetails) {
        this.oldPostDetails = oldPostDetails;
        this.newPostDetails = newPostDetails;
    }

    @Override
    public int getOldListSize() {
        return oldPostDetails != null ? oldPostDetails.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newPostDetails != null ? newPostDetails.size() : 0;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newPostDetails.get(newItemPosition).getPostId(), oldPostDetails.get(oldItemPosition).getPostId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newPostDetails.get(newItemPosition).getPostId(), oldPostDetails.get(oldItemPosition).getPostId());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        PostDetails oldPostDetails = this.oldPostDetails.get(oldItemPosition);
        PostDetails newPostDetails = this.newPostDetails.get(newItemPosition);

        if (oldPostDetails != null && newPostDetails != null) {
            Bundle diffBundle = new Bundle();
            try {
                if (!Objects.equals(oldPostDetails.getPostId(), newPostDetails.getPostId())) {
                    diffBundle.putParcelable(DIFF_POST_DETAILS, newPostDetails);
                    return diffBundle;
                } else {
                    return getDiffBundle(oldPostDetails, newPostDetails, diffBundle);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return getDiffBundle(oldPostDetails, newPostDetails, diffBundle);
            }
        } else {
            return null;
        }
    }

    private Bundle getDiffBundle(PostDetails oldPostDetails, PostDetails newPostDetails, Bundle diffBundle) {
        if (!Objects.equals(oldPostDetails.getLikes(), newPostDetails.getLikes()))
            diffBundle.putInt(DIFF_LIKES, newPostDetails.getLikes());
        if (!Objects.equals(oldPostDetails.getTitle(), newPostDetails.getTitle()))
            diffBundle.putString(DIFF_TITLE, newPostDetails.getTitle());
        if (!Objects.equals(oldPostDetails.canReact(), newPostDetails.canReact()))
            diffBundle.putBoolean(DIFF_CAN_LIKE, newPostDetails.canLike());
        if (!Objects.equals(oldPostDetails.hasCheckin(), newPostDetails.hasCheckin())) {
            diffBundle.putBoolean(DIFF_HAS_CHECKIN, newPostDetails.hasCheckin());
            diffBundle.putParcelable(DIFF_CHECKIN, newPostDetails.getCheckIn());
        }
        if (!Objects.equals(newPostDetails.getTotalTags(), oldPostDetails.getTotalTags())) {
            diffBundle.putInt(DIFF_TOTAL_TAGS, newPostDetails.getTotalTags());
            diffBundle.putParcelableArrayList(DIFF_TAGS, newPostDetails.getTaggedUsers());
        }
        if (!Objects.equals(oldPostDetails.getTotalReactions(), newPostDetails.getTotalReactions())) {
            diffBundle.putInt(DIFF_TOTAL_REACTIONS, newPostDetails.getTotalReactions());
            diffBundle.putBoolean(DIFF_CAN_REACT, newPostDetails.canReact());
            diffBundle.putParcelableArrayList(DIFF_REACTIONS, newPostDetails.getReactions());
        }
        if (!Objects.equals(newPostDetails.getReactedUsers(), oldPostDetails.getReactedUsers()))
            diffBundle.putParcelableArrayList(DIFF_REACTED_USERS, newPostDetails.getReactedUsers());
        if (!Objects.equals(oldPostDetails.isHided(), newPostDetails.isHided()))
            diffBundle.putBoolean(DIFF_IS_HIDED, newPostDetails.isHided());
        if (!Objects.equals(oldPostDetails.isHidedAll(), newPostDetails.isHidedAll()))
            diffBundle.putBoolean(DIFF_IS_HIDED_ALL, newPostDetails.isHidedAll());
//        if (CollectionUtils.isEqualCollection(oldPostDetails.getReactions(), newPostDetails.getReactions()))
//            diffBundle.putParcelableArrayList(DIFF_REACTIONS, newPostDetails.getReactions());
        return diffBundle;
    }

    public static void updatePostDetailsAccordingToDiffBundle(PostDetails postDetails, Bundle bundle) {
        for (String key : bundle.keySet()) {
            try {
                switch (key) {
                    case DIFF_LIKES:
                        postDetails.setLikes(bundle.getInt(DIFF_LIKES));
                        break;
                    case DIFF_TITLE:
                        postDetails.setTitle(bundle.getString(DIFF_TITLE));
                        break;
                    case DIFF_CAN_LIKE:
                        postDetails.setCanReact(bundle.getBoolean(DIFF_CAN_REACT));
                        break;
                    case DIFF_HAS_CHECKIN:
                        postDetails.setHasCheckin(bundle.getBoolean(DIFF_HAS_CHECKIN));
                        postDetails.setCheckIn((CheckIn) bundle.getParcelable(DIFF_CHECKIN));
                        break;
                    case DIFF_TOTAL_TAGS:
                        postDetails.setTotalTags(bundle.getInt(DIFF_TOTAL_TAGS));
                        postDetails.setTaggedUsers(bundle.<TaggedUser>getParcelableArrayList(DIFF_TAGS));
                        break;
                    case DIFF_TOTAL_REACTIONS:
                        postDetails.setTotalReactions(bundle.getInt(DIFF_TOTAL_REACTIONS));
                        postDetails.setCanReact(bundle.getBoolean(DIFF_CAN_REACT));
                        postDetails.setReactions(bundle.<PostReaction>getParcelableArrayList(DIFF_REACTIONS));
                        break;
                    case DIFF_REACTED_USERS:
                        postDetails.setReactedUsers(bundle.<ReactedUser>getParcelableArrayList(DIFF_REACTED_USERS));
                        break;
                    case DIFF_IS_HIDED:
                        postDetails.setHided(bundle.getBoolean(DIFF_IS_HIDED));
                        break;
                    case DIFF_IS_HIDED_ALL:
                        postDetails.setHidedAll(bundle.getBoolean(DIFF_IS_HIDED_ALL));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}