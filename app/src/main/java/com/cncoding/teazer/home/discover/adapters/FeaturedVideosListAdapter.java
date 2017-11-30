package com.cncoding.teazer.home.discover.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class FeaturedVideosListAdapter extends RecyclerView.Adapter<FeaturedVideosListAdapter.ViewHolder> {

    private final ArrayList<PostDetails> featuredVideosArrayList;
    private final Context context;
    private OnSearchInteractionListener mListener;

    public FeaturedVideosListAdapter(ArrayList<PostDetails> featuredVideosArrayList, Context context, OnSearchInteractionListener mListener) {
        this.featuredVideosArrayList = featuredVideosArrayList;
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public FeaturedVideosListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reaction_post, parent, false);
        return new FeaturedVideosListAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final FeaturedVideosListAdapter.ViewHolder holder, int position) {
        holder.featuredVideo = featuredVideosArrayList.get(position);

        /*Setting title*/
        holder.title.setText(holder.featuredVideo.getTitle());

        /*Setting name*/
        String name = holder.featuredVideo.getPostOwner().getFirstName() + BLANK_SPACE + holder.featuredVideo.getPostOwner().getLastName();
        holder.name.setText(name);

        /*Setting likes*/
        String likes = SPACE + String.valueOf(holder.featuredVideo.getLikes());
        holder.likes.setText(likes);

        /*Setting views*/
        String views = SPACE + String.valueOf(holder.featuredVideo.getMedias().get(0).getViews());
        holder.views.setText(views);

        Glide.with(context)
                .load(holder.featuredVideo.getMedias().get(0).getThumbUrl())
                .placeholder(R.drawable.bg_placeholder)
                .crossFade()
                .into(holder.thumbnail);

        Glide.with(context)
                .load(!holder.featuredVideo.getPostOwner().hasProfileMedia() ? R.drawable.ic_user_male_dp_small :
                        holder.featuredVideo.getPostOwner().getProfileMedia().getThumbUrl())
                .placeholder(R.drawable.ic_user_male_dp_small)
                .crossFade()
                .into(holder.dp);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSearchInteraction(ACTION_VIEW_POST, null, null,
                        holder.featuredVideo, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return featuredVideosArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.reaction_post_caption) ProximaNovaSemiboldTextView title;
        @BindView(R.id.reaction_post_name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.reaction_post_likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.reaction_post_views) ProximaNovaRegularTextView views;
        @BindView(R.id.reaction_post_thumb) ImageView thumbnail;
        @BindView(R.id.reaction_post_dp) CircularAppCompatImageView dp;
        PostDetails featuredVideo;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}