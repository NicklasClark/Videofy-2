package com.cncoding.teazer.home.search.adapters;

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
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class FeaturedVideosListAdapter extends RecyclerView.Adapter<FeaturedVideosListAdapter.ViewHolder> {

    private final ArrayList<PostDetails> featuredVideosArrayList;
    private final Context context;

    public FeaturedVideosListAdapter(ArrayList<PostDetails> featuredVideosArrayList, Context context) {
        this.featuredVideosArrayList = featuredVideosArrayList;
        this.context = context;
    }

    @Override
    public FeaturedVideosListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reaction_post, parent, false);
        return new FeaturedVideosListAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(FeaturedVideosListAdapter.ViewHolder holder, int position) {
        holder.featuredVideos = featuredVideosArrayList.get(position);

        /*Setting title*/
        holder.title.setText(holder.featuredVideos.getTitle());

        /*Setting name*/
        String name = holder.featuredVideos.getPostOwner().getFirstName() + BLANK_SPACE + holder.featuredVideos.getPostOwner().getLastName();
        holder.name.setText(name);

        /*Setting likes*/
        String likes = SPACE + String.valueOf(holder.featuredVideos.getLikes());
        holder.likes.setText(likes);

        /*Setting views*/
        String views = SPACE + String.valueOf(holder.featuredVideos.getMedias().get(0).getViews());
        holder.views.setText(views);

        Glide.with(context)
                .load(holder.featuredVideos.getMedias().get(0).getThumbUrl())
                .crossFade()
                .into(holder.thumbnail);

        if (holder.featuredVideos.getPostOwner().hasProfileMedia())
            Glide.with(context)
                    .load(holder.featuredVideos.getPostOwner().getProfileMedia().getThumbUrl())
                    .placeholder(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .into(holder.dp);
        else
            Glide.with(context)
                    .load("")
                    .placeholder(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .into(holder.dp);
    }

    @Override
    public int getItemCount() {
        return featuredVideosArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reaction_post_caption) ProximaNovaSemiboldTextView title;
        @BindView(R.id.reaction_post_name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.reaction_post_likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.reaction_post_views) ProximaNovaRegularTextView views;
        @BindView(R.id.reaction_post_thumb) ImageView thumbnail;
        @BindView(R.id.reaction_post_dp) CircularAppCompatImageView dp;
        PostDetails featuredVideos;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}