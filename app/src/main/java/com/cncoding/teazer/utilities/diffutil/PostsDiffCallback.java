package com.cncoding.teazer.utilities.diffutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.cncoding.teazer.model.post.PostDetails;

import java.util.List;
import java.util.Objects;

/**
 *
 * Created by Prem $ on 12/25/2017.
 */

public class PostsDiffCallback extends DiffUtil.Callback {

    public static final String DIFF_POST_DETAILS = "postDetails";
    public static final String DIFF_LIKES = "likes";
    public static final String DIFF_TOTAL_REACTIONS = "totalReactions";
    public static final String DIFF_TOTAL_TAGS = "totalTags";
    public static final String DIFF_HAS_CHECKIN = "hasCheckIn";
    public static final String DIFF_TITLE = "title";
    public static final String DIFF_CAN_REACT = "canReact";
    public static final String DIFF_CAN_LIKE = "canLike";
    public static final String DIFF_CHECKIN = "checkIn";

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
        try {
            return Objects.equals(newPostDetails.get(newItemPosition).getPostId(), oldPostDetails.get(oldItemPosition).getPostId());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        try {
            return Objects.equals(newPostDetails.get(newItemPosition), oldPostDetails.get(oldItemPosition));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        if (!Objects.equals(oldPostDetails.getTotalReactions(), newPostDetails.getTotalReactions()))
            diffBundle.putInt(DIFF_TOTAL_REACTIONS, newPostDetails.getTotalReactions());
        if (!Objects.equals(oldPostDetails.getTotalTags(), newPostDetails.getTotalTags()))
            diffBundle.putInt(DIFF_TOTAL_TAGS, newPostDetails.getTotalTags());
        if (!Objects.equals(oldPostDetails.getTitle(), newPostDetails.getTitle()))
            diffBundle.putString(DIFF_TITLE, newPostDetails.getTitle());
        if (!Objects.equals(oldPostDetails.canReact(), newPostDetails.canReact()))
            diffBundle.putBoolean(DIFF_CAN_REACT, newPostDetails.canReact());
        if (!Objects.equals(oldPostDetails.canLike(), newPostDetails.canLike()))
            diffBundle.putBoolean(DIFF_CAN_LIKE, newPostDetails.canLike());
        if (!Objects.equals(oldPostDetails.hasCheckin(), newPostDetails.hasCheckin()))
            diffBundle.putBoolean(DIFF_HAS_CHECKIN, newPostDetails.hasCheckin());
        if (!Objects.equals(oldPostDetails.getCheckIn(), newPostDetails.getCheckIn()))
            diffBundle.putParcelable(DIFF_CHECKIN, newPostDetails.getCheckIn());
        return diffBundle;
    }
}