package com.cncoding.teazer.home.discover.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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

    private SparseIntArray colorArray;
    private SparseIntArray dimensionSparseArray;
    private final ArrayList<PostDetails> featuredVideosArrayList;
    private final Context context;
    private OnSearchInteractionListener mListener;

    public FeaturedVideosListAdapter(ArrayList<PostDetails> featuredVideosArrayList, Context context, OnSearchInteractionListener mListener) {
        this.featuredVideosArrayList = featuredVideosArrayList;
        this.context = context;
        this.mListener = mListener;
        dimensionSparseArray = new SparseIntArray();
        colorArray = new SparseIntArray();
    }

    @Override
    public FeaturedVideosListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured_post, parent, false);
        return new FeaturedVideosListAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final FeaturedVideosListAdapter.ViewHolder holder, int position) {
        holder.featuredVideo = featuredVideosArrayList.get(position);

        if (dimensionSparseArray.get(position) != 0) {
            holder.layout.getLayoutParams().height = dimensionSparseArray.get(position);
        }

        /*Setting title*/
        holder.title.setText(holder.featuredVideo.getTitle());

        /*Setting category*/
        if (holder.featuredVideo.getCategories() != null) {
            holder.category.setVisibility(holder.featuredVideo.getCategories().isEmpty() ? View.GONE : View.VISIBLE);
            if (holder.category.getVisibility() == View.VISIBLE) {
                holder.category.setText(holder.featuredVideo.getCategories() != null ?
                        holder.featuredVideo.getCategories().get(0).getCategoryName() : "");
                holder.category.setBackground(
                        getBackground(holder.category, position, Color.parseColor(holder.featuredVideo.getCategories().get(0).getColor())));
            }
        } else holder.category.setVisibility(View.GONE);

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
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        int height = (holder.layout.getWidth() * resource.getIntrinsicHeight()) / resource.getIntrinsicWidth();
                        if (height < holder.layout.getWidth())
                            height = holder.layout.getWidth();

                        holder.layout.getLayoutParams().height = height;

                        dimensionSparseArray.put(holder.getAdapterPosition(), height);
//                        holder.layout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fast_fade_in));
                        holder.layout.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.thumbnail);

        Glide.with(context)
                .load(holder.featuredVideo.getPostOwner().hasProfileMedia() ?
                        holder.featuredVideo.getPostOwner().getProfileMedia().getThumbUrl() : R.drawable.ic_user_male_dp_small)
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

    private GradientDrawable getBackground(ProximaNovaRegularTextView title, int position, int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (colorArray.get(position) == 0) {
            colorArray.put(position, color);
        }
        gradientDrawable.setColor(Color.TRANSPARENT);
        gradientDrawable.setCornerRadius(3);
        gradientDrawable.setStroke(1, colorArray.get(position));
        title.setTextColor(colorArray.get(position));
        return gradientDrawable;
    }

    @Override
    public int getItemCount() {
        return featuredVideosArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.title) ProximaNovaSemiboldTextView title;
        @BindView(R.id.category) ProximaNovaRegularTextView category;
        @BindView(R.id.name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        PostDetails featuredVideo;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}