package com.cncoding.teazer.home.discover.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.discover.DiscoverFragment.OnDiscoverInteractionListener;
import com.cncoding.teazer.model.base.Dimension;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.utilities.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_POST;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.home.post.detailspage.FragmentPostDetails.SPACE;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.ViewUtils.adjustViewSize;
import static com.cncoding.teazer.utilities.ViewUtils.prepareLayout;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class FeaturedVideosListAdapter extends RecyclerView.Adapter<FeaturedVideosListAdapter.ViewHolder> {

    private SparseIntArray colorArray;
    private SparseArray<Dimension> dimensionSparseArray;
    private final ArrayList<PostDetails> featuredVideosArrayList;
    private final Context context;
    private OnDiscoverInteractionListener mListener;

    public FeaturedVideosListAdapter(ArrayList<PostDetails> featuredVideosArrayList, Context context, OnDiscoverInteractionListener mListener) {
        this.featuredVideosArrayList = featuredVideosArrayList;
        this.context = context;
        this.mListener = mListener;
        dimensionSparseArray = new SparseArray<>();
        colorArray = new SparseIntArray();
    }

    @Override
    public FeaturedVideosListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured_post, parent, false);
        return new FeaturedVideosListAdapter.ViewHolder(itemView);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onBindViewHolder(final FeaturedVideosListAdapter.ViewHolder holder, int position) {
        ViewUtils.initializeShimmer(holder.shimmerLayout, holder.topLayout, holder.bottomLayout, holder.vignetteLayout);

        holder.postDetails = featuredVideosArrayList.get(position);

        /*Adjust view size before loading anything*/
        if (dimensionSparseArray.get(position) == null) {
            adjustViewSize(context, holder.postDetails.getMedias().get(0).getDimension().getWidth(),
                    holder.postDetails.getMedias().get(0).getDimension().getHeight(),
                    holder.layout.getLayoutParams(), position, dimensionSparseArray, false);
        } else {
            holder.layout.getLayoutParams().width = dimensionSparseArray.get(position).getWidth();
            holder.layout.getLayoutParams().height = dimensionSparseArray.get(position).getHeight();
        }

        /*Setting title*/
        holder.title.setText(decodeUnicodeString(holder.postDetails.getTitle()));

        /*Setting category*/
        if (holder.postDetails.getCategories() != null) {
            holder.category.setVisibility(holder.postDetails.getCategories().isEmpty() ? View.GONE : View.VISIBLE);
            if (holder.category.getVisibility() == View.VISIBLE) {
                holder.category.setText(holder.postDetails.getCategories() != null ?
                        holder.postDetails.getCategories().get(0).getCategoryName() : "");
                holder.category.setBackground(
                        getBackground(holder.category, position, Color.parseColor(holder.postDetails.getCategories().get(0).getColor())));
            }
        } else holder.category.setVisibility(View.GONE);

        /*Setting name*/
       // String name = holder.postDetails.getPostOwner().getFirstName() + BLANK_SPACE + holder.postDetails.getPostOwner().getLastName();
        String name = holder.postDetails.getPostOwner().getUserName();
        holder.name.setText(name);

        /*Setting likes*/
        String likes = SPACE + String.valueOf(holder.postDetails.getLikes());
        holder.likes.setText(likes);

        /*Setting views*/
        String views = SPACE + String.valueOf(holder.postDetails.getMedias().get(0).getViews());
        holder.views.setText(views);

        Glide.with(context)
                .load(holder.postDetails.getPostOwner().hasProfileMedia() ?
                        holder.postDetails.getPostOwner().getProfileMedia().getThumbUrl() : R.drawable.ic_user_male_dp_small)
                .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                .into(holder.dp);

        Glide.with(context)
                .load(holder.postDetails.getMedias().get(0).getThumbUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        prepareLayout(holder.layout, holder.shimmerLayout, holder.topLayout, holder.bottomLayout,
                                holder.vignetteLayout, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                        return false;
                    }
                })
                .into(holder.thumbnail);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.root_layout:
                        mListener.onDiscoverInteraction(ACTION_VIEW_POST, null, null,
                                holder.postDetails);
                        break;
                    case R.id.dp:
                        mListener.onDiscoverInteraction(ACTION_VIEW_PROFILE, null, null,
                                holder.postDetails);
                        break;
                    case R.id.name:
                        mListener.onDiscoverInteraction(ACTION_VIEW_PROFILE, null, null,
                                holder.postDetails);
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
        @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
        @BindView(R.id.shimmer_layout) RelativeLayout shimmerLayout;
        @BindView(R.id.vignette_layout) FrameLayout vignetteLayout;
        @BindView(R.id.top_layout) RelativeLayout topLayout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.category) ProximaNovaRegularTextView category;
        @BindView(R.id.name) ProximaNovaSemiBoldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        PostDetails postDetails;
        
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}