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
import static com.cncoding.teazer.customViews.MediaControllerView.SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.getByteArrayFromImage;

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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_most_popular_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            holder.mostPopular = mostPopularList.get(position);

            holder.title.setText(holder.mostPopular.getTitle());
            holder.name.setText(holder.mostPopular.getPostOwner().getUserName());
            String durationText = holder.mostPopular.getMedias().get(0).getDuration() + " secs";
            holder.duration.setText(durationText);
            String likesText = SPACE + String.valueOf(holder.mostPopular.getLikes());
            holder.likes.setText(likesText);
            String viewsText = SPACE + String.valueOf(holder.mostPopular.getMedias().get(0).getViews());
            holder.views.setText(viewsText);

            holder.reactions.setVisibility(holder.mostPopular.getTotalReactions() == 0 ? View.GONE : View.VISIBLE);
            String reactionText;
            if (holder.mostPopular.getTotalReactions() > 4)
                reactionText = "+" + String.valueOf(holder.mostPopular.getTotalReactions() - 3) + " R";
            else
                reactionText = String.valueOf(holder.mostPopular.getTotalReactions()) + " R";
            holder.reactions.setText(reactionText);

            Glide.with(context)
                    .load(holder.mostPopular.getMedias().get(0).getThumbUrl())
                    .placeholder(R.drawable.bg_placeholder)
                    .crossFade()
                    .into(holder.thumbnail);

            Glide.with(context)
                    .load(holder.mostPopular.getPostOwner().getProfileMedia() != null &&
                            holder.mostPopular.getPostOwner().hasProfileMedia() ?
                            holder.mostPopular.getPostOwner().getProfileMedia().getThumbUrl() : "")
                    .placeholder(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .into(holder.dp);

            if (holder.mostPopular.getReactedUsers() != null && holder.mostPopular.getReactedUsers().size() > 2) {
                holder.reactionImage1.setVisibility(View.VISIBLE);
                holder.reactionImage2.setVisibility(View.VISIBLE);
                holder.reactionImage3.setVisibility(View.VISIBLE);
                holder.reactionImage1.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(context)
                                .load(holder.mostPopular.getReactedUsers().get(0).getProfileMedia() != null ?
                                        holder.mostPopular.getReactedUsers().get(0).getProfileMedia().getThumbUrl() : "")
                                .placeholder(R.drawable.ic_user_male_dp_small)
                                .crossFade()
                                .into(holder.reactionImage1);
                    }
                });
                holder.reactionImage2.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(context)
                                .load(holder.mostPopular.getReactedUsers().get(1).getProfileMedia() != null ?
                                        holder.mostPopular.getReactedUsers().get(1).getProfileMedia().getThumbUrl() : "")
                                .placeholder(R.drawable.ic_user_male_dp_small)
                                .crossFade()
                                .into(holder.reactionImage2);
                    }
                });
                holder.reactionImage3.post(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(context)
                                .load(holder.mostPopular.getReactedUsers().get(2).getProfileMedia() != null ?
                                        holder.mostPopular.getReactedUsers().get(2).getProfileMedia().getThumbUrl() : "")
                                .placeholder(R.drawable.ic_user_male_dp_small)
                                .crossFade()
                                .into(holder.reactionImage3);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSearchInteraction(ACTION_VIEW_POST, null, null,
                        holder.mostPopular, getByteArrayFromImage(holder.thumbnail));
            }
        });
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
        @BindView(R.id.reaction_3) CircularAppCompatImageView reactionImage3;
        PostDetails mostPopular;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}