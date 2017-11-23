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
import com.cncoding.teazer.utilities.Pojos.DummyDiscover.DummyFeaturedVideos;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class FeaturedVideosListAdapter extends RecyclerView.Adapter<FeaturedVideosListAdapter.ViewHolder> {

    private final ArrayList<DummyFeaturedVideos> featuredVideosArrayList;
    private final Context context;

    FeaturedVideosListAdapter(ArrayList<DummyFeaturedVideos> featuredVideosArrayList, Context context) {
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

        holder.title.setText(holder.featuredVideos.getTitle());
        holder.name.setText(holder.featuredVideos.getName());
        holder.likes.setText(" " + String.valueOf(holder.featuredVideos.getLikes()));
        holder.views.setText(" " + String.valueOf(holder.featuredVideos.getViews()));

        if (holder.featuredVideos.getThumbUrl().contains(".gif")) {
            Glide.with(context)
                    .load(holder.featuredVideos.getThumbUrl())
                    .asGif()
                    .crossFade()
                    .into(holder.thumbnail);
        } else
            Glide.with(context)
                    .load(holder.featuredVideos.getThumbUrl())
                    .crossFade()
                    .into(holder.thumbnail);

        Glide.with(context)
                .load(holder.featuredVideos.getProfileThumbUrl())
                .crossFade()
                .into(holder.dp);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reaction_post_caption) ProximaNovaSemiboldTextView title;
        @BindView(R.id.reaction_post_name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.reaction_post_likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.reaction_post_views) ProximaNovaRegularTextView views;
        @BindView(R.id.reaction_post_thumb) ImageView thumbnail;
        @BindView(R.id.reaction_post_dp) CircularAppCompatImageView dp;
        DummyFeaturedVideos featuredVideos;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}