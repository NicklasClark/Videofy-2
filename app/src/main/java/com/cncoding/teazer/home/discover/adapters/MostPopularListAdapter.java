package com.cncoding.teazer.home.discover.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.discover.DiscoverFragment.OnSearchInteractionListener;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_POST;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.home.post.PostDetailsActivity.SPACE;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MostPopularListAdapter extends RecyclerView.Adapter<MostPopularListAdapter.ViewHolder> {

    private ArrayList<PostDetails> mostPopularList;
    private Context context;
    private OnSearchInteractionListener mListener;

    public MostPopularListAdapter(ArrayList<PostDetails> mostPopularList, Context context, OnSearchInteractionListener mListener) {
        this.mostPopularList = mostPopularList;
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_most_popular_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            holder.postDetails = mostPopularList.get(position);

            holder.title.setText(holder.postDetails.getTitle());
            holder.name.setText(holder.postDetails.getPostOwner().getUserName());
            String durationText = holder.postDetails.getMedias().get(0).getDuration() + " secs";
            holder.duration.setText(durationText);
            String likesText = SPACE + String.valueOf(holder.postDetails.getLikes());
            holder.likes.setText(likesText);
            String viewsText = SPACE + String.valueOf(holder.postDetails.getMedias().get(0).getViews());
            holder.views.setText(viewsText);

            holder.reactions.setVisibility(holder.postDetails.getTotalReactions() == 0 ? View.INVISIBLE : View.VISIBLE);
            String reactionText = "";
            if (holder.postDetails.getTotalReactions() > 2)
                reactionText = "+" + String.valueOf(holder.postDetails.getTotalReactions() - 2) + " R";
            else
                holder.reactions.getLayoutParams().width = 0;
//                reactionText = String.valueOf(holder.postDetails.getTotalReactions()) + " R";
            holder.reactions.setText(reactionText);

            Glide.with(context)
                    .load(holder.postDetails.getMedias().get(0).getThumbUrl())
                    .placeholder(R.drawable.bg_placeholder)
                    .crossFade()
                    .into(holder.thumbnail);

            Glide.with(context)
                    .load(holder.postDetails.getPostOwner().getProfileMedia() != null &&
                            holder.postDetails.getPostOwner().hasProfileMedia() ?
                            holder.postDetails.getPostOwner().getProfileMedia().getThumbUrl() : "")
                    .placeholder(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .into(holder.dp);

            if (holder.postDetails.getReactedUsers() != null && holder.postDetails.getReactedUsers().size() > 0) {
                holder.reactionImage1.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(holder.postDetails.getReactedUsers().get(0).getProfileMedia() == null ?
                                R.drawable.ic_user_male_dp_small
                                : holder.postDetails.getReactedUsers().get(0).getProfileMedia().getThumbUrl())
                        .placeholder(R.drawable.ic_user_male_dp_small)
                        .crossFade()
                        .into(holder.reactionImage1);
                if (holder.postDetails.getReactedUsers().size() > 1) {
                    holder.reactionImage2.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load(holder.postDetails.getReactedUsers().get(1).getProfileMedia() == null ?
                                    R.drawable.ic_user_male_dp_small
                                    : holder.postDetails.getReactedUsers().get(1).getProfileMedia().getThumbUrl())
                            .placeholder(R.drawable.ic_user_male_dp_small)
                            .crossFade()
                            .into(holder.reactionImage2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.root_layout:
                        mListener.onSearchInteraction(ACTION_VIEW_POST, null, null,
                                holder.postDetails, null);
                        break;
                    case R.id.dp:
                        mListener.onSearchInteraction(ACTION_VIEW_PROFILE, null, null,
                                holder.postDetails, null);
                        break;
                    case R.id.name:
                        mListener.onSearchInteraction(ACTION_VIEW_PROFILE, null, null,
                                holder.postDetails, null);
                        break;
                    default:
                        break;
                }
            }
        };

        holder.layout.setOnClickListener(listener);
        holder.name.setOnClickListener(listener);
        holder.dp.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return mostPopularList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) FrameLayout layout;
        @BindView(R.id.title) ProximaNovaSemiboldTextView title;
        @BindView(R.id.name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.duration) ProximaNovaRegularTextView duration;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.reaction_count) ProximaNovaSemiboldTextView reactions;
        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.reaction_1) CircularAppCompatImageView reactionImage1;
        @BindView(R.id.reaction_2) CircularAppCompatImageView reactionImage2;
        PostDetails postDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}