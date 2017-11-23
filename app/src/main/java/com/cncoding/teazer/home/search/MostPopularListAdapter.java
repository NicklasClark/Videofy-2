package com.cncoding.teazer.home.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.customViews.MediaControllerView.SPACE;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

class MostPopularListAdapter extends RecyclerView.Adapter<MostPopularListAdapter.ViewHolder> {

    private ArrayList<PostDetails> mostPopularList;
    private Context context;

    MostPopularListAdapter(ArrayList<PostDetails> mostPopularList, Context context) {
        this.mostPopularList = mostPopularList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_most_popular_list, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mostPopular = mostPopularList.get(position);

        holder.title.setText(holder.mostPopular.getTitle());
        holder.name.setText(holder.mostPopular.getPostOwner().getUserName());
        holder.duration.setText(holder.mostPopular.getMedias().get(0).getDuration() + " secs");
        holder.likes.setText(SPACE + String.valueOf(holder.mostPopular.getLikes()));
        holder.views.setText(SPACE + String.valueOf(holder.mostPopular.getMedias().get(0).getViews()));
        holder.reactions.setText("+" + String.valueOf(holder.mostPopular.getTotalReactions()) + " R");

        Glide.with(context)
                .load(holder.mostPopular.getMedias().get(0).getThumbUrl())
                .asGif()
                .crossFade()
                .into(holder.thumbnail);

        if (holder.mostPopular.getPostOwner().hasProfileMedia())
            Glide.with(context)
                .load(holder.mostPopular.getPostOwner().getProfileMedia().getThumbUrl())
                .placeholder(R.drawable.ic_user_dp_small)
                .crossFade()
                .into(holder.dp);
        else
            Glide.with(context)
                    .load("")
                    .placeholder(R.drawable.ic_user_dp_small)
                    .crossFade()
                    .into(holder.dp);

        if (holder.mostPopular.getReactedUsers() != null && holder.mostPopular.getReactedUsers().size() > 0) {
            holder.reactionImage1.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(holder.mostPopular.getReactedUsers().get(0).getProfileMedia().getThumbUrl())
                    .placeholder(R.drawable.ic_user_dp_small)
                    .crossFade()
                    .into(holder.reactionImage1);

            if (holder.mostPopular.getReactedUsers().size() > 1) {
                holder.reactionImage2.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(holder.mostPopular.getReactedUsers().get(1).getProfileMedia().getThumbUrl())
                        .placeholder(R.drawable.ic_user_dp_small)
                        .crossFade()
                        .into(holder.reactionImage2);
            }

            if (holder.mostPopular.getReactedUsers().size() > 2) {
                holder.reactionImage3.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(holder.mostPopular.getReactedUsers().get(2).getProfileMedia().getThumbUrl())
                        .placeholder(R.drawable.ic_user_dp_small)
                        .crossFade()
                        .into(holder.reactionImage3);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mostPopularList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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