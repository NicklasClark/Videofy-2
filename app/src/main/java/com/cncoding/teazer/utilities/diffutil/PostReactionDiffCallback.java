package com.cncoding.teazer.utilities.diffutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.cncoding.teazer.data.model.post.PostReaction;

import java.util.List;
import java.util.Objects;

/**
 * 
 * Created by Prem$ on 2/26/2018.
 */

public class PostReactionDiffCallback extends DiffUtil.Callback {

    public static final String DIFF_POST_REACTION = "postReaction";
    private static final String DIFF_REACT_TITLE = "title";
    private static final String DIFF_LIKES = "likes";
    private static final String DIFF_VIEWS = "views";
    private static final String DIFF_CAN_LIKE = "canLike";
    
    private List<PostReaction> oldPostReactions;
    private List<PostReaction> newPostReactions;

    public PostReactionDiffCallback(List<PostReaction> oldPostReactions, List<PostReaction> newPostReactions) {
        this.oldPostReactions = oldPostReactions;
        this.newPostReactions = newPostReactions;
    }

    @Override
    public int getOldListSize() {
        return oldPostReactions.size();
    }

    @Override
    public int getNewListSize() {
        return newPostReactions.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newPostReactions.get(newItemPosition).getReactId(), oldPostReactions.get(oldItemPosition).getReactId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newPostReactions.get(newItemPosition), oldPostReactions.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        PostReaction oldPostReaction = oldPostReactions.get(oldItemPosition);
        PostReaction newPostReaction = newPostReactions.get(newItemPosition);

        if (oldPostReaction != null && newPostReaction != null) {
            Bundle diffBundle = new Bundle();
            try {
                if (!Objects.equals(oldPostReaction.getPostId(), newPostReaction.getPostId())) {
                    diffBundle.putParcelable(DIFF_POST_REACTION, newPostReaction);
                    return diffBundle;
                } else {
                    return getDiffBundle(oldPostReaction, newPostReaction, diffBundle);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return getDiffBundle(oldPostReaction, newPostReaction, diffBundle);
            }
        } else {
            return null;
        }
    }

    private Object getDiffBundle(PostReaction oldPostReaction, PostReaction newPostReaction, Bundle diffBundle) {
        if (!Objects.equals(oldPostReaction.getLikes(), newPostReaction.getLikes()))
            diffBundle.putInt(DIFF_LIKES, newPostReaction.getLikes());
        if (!Objects.equals(oldPostReaction.getViews(), newPostReaction.getViews()))
            diffBundle.putInt(DIFF_VIEWS, newPostReaction.getViews());
        if (!Objects.equals(oldPostReaction.getReactTitle(), newPostReaction.getReactTitle()))
            diffBundle.putString(DIFF_REACT_TITLE, newPostReaction.getReactTitle());
        if (!Objects.equals(oldPostReaction.canLike(), newPostReaction.canLike()))
            diffBundle.putBoolean(DIFF_CAN_LIKE, newPostReaction.canLike());
        return diffBundle;
    }

    public static void updatePostReactionAccordingToDiffBundle(PostReaction postReaction, Bundle bundle) {
        for (String key : bundle.keySet()) {
            try {
                switch (key) {
                    case DIFF_LIKES:
                        postReaction.setLikes(bundle.getInt(DIFF_LIKES));
                        break;
                    case DIFF_VIEWS:
                        postReaction.setViews(bundle.getInt(DIFF_VIEWS));
                        break;
                    case DIFF_REACT_TITLE:
                        postReaction.setReactTitle(bundle.getString(DIFF_REACT_TITLE));
                        break;
                    case DIFF_CAN_LIKE:
                        postReaction.setCanLike(bundle.getBoolean(DIFF_CAN_LIKE));
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