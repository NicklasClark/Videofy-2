package com.cncoding.teazer.home.discover.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.model.base.Dimension;
import com.cncoding.teazer.model.post.PostDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_POST;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUTFUrl;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.adjustViewSize;
import static com.cncoding.teazer.utilities.ViewUtils.initializeShimmer;
import static com.cncoding.teazer.utilities.ViewUtils.prepareLayout;

/**
 *
 * Created by Prem $ on 11/24/2017.
 */

public class SubDiscoverAdapter extends RecyclerView.Adapter<SubDiscoverAdapter.ViewHolder> {

    private OnSubSearchInteractionListener mListener;
    private ArrayList<PostDetails> postDetailsArrayList;
    private SparseArray<Dimension> dimensionSparseArray;
    private Context context;
    private SparseIntArray colorArray;

    public SubDiscoverAdapter(ArrayList<PostDetails> postDetailsArrayList, Context context) {
        this.postDetailsArrayList = postDetailsArrayList;
        this.context = context;
        dimensionSparseArray = new SparseArray<>();
        if (context instanceof OnSubSearchInteractionListener) {
            mListener = (SubDiscoverAdapter.OnSubSearchInteractionListener) context;
        }
        colorArray = new SparseIntArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_discover_post, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            initializeShimmer(holder.shimmerLayout, holder.topLayout, holder.bottomLayout, holder.vignetteLayout);

            holder.postDetails = postDetailsArrayList.get(position);

            /*Adjust view size before loading anything*/
            if (dimensionSparseArray.get(position) == null) {
                adjustViewSize(context, holder.postDetails.getMedias().get(0).getDimension().getWidth(),
                        holder.postDetails.getMedias().get(0).getDimension().getHeight(),
                        holder.layout.getLayoutParams(), position, dimensionSparseArray, false);
            } else {
                holder.layout.getLayoutParams().width = dimensionSparseArray.get(position).getWidth();
                holder.layout.getLayoutParams().height = dimensionSparseArray.get(position).getHeight();
            }

            holder.title.setText(decodeUTFUrl(holder.postDetails.getTitle()));

            holder.category.setVisibility(holder.postDetails.getCategories() != null && holder.postDetails.getCategories().size() > 0 ?
                    View.VISIBLE : View.GONE);
            if (holder.postDetails.getCategories() != null && holder.postDetails.getCategories().size() > 0) {
                holder.category.setText(holder.postDetails.getCategories().get(0).getCategoryName());
                holder.category.setBackground(
                        getBackground(holder.category, position, Color.parseColor(holder.postDetails.getCategories().get(0).getColor())));
            }

            String name = holder.postDetails.getPostOwner().getFirstName() + BLANK_SPACE + holder.postDetails.getPostOwner().getLastName();
            holder.name.setText(name);

            String likes = BLANK_SPACE + String.valueOf(holder.postDetails.getLikes());
            holder.likes.setText(likes);

            String views = BLANK_SPACE + String.valueOf(holder.postDetails.getMedias().get(0).getViews());
            holder.views.setText(views);

            if (holder.postDetails.getPostOwner().hasProfileMedia() && holder.postDetails.getPostOwner().getProfileMedia() != null) {
                Glide.with(context)
                        .load(holder.postDetails.getPostOwner().getProfileMedia().getThumbUrl())
                        .placeholder(R.drawable.ic_user_male_dp_small)
                        .crossFade()
                        .into(holder.dp);
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_user_male_dp_small)
                        .crossFade()
                        .into(holder.dp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Glide.with(context)
                .load(holder.postDetails.getMedias().get(0).getThumbUrl())
                .crossFade()
                .skipMemoryCache(false)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        prepareLayout(holder.layout, holder.shimmerLayout, holder.topLayout, holder.bottomLayout,
                                holder.vignetteLayout, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                        return false;
                    }
                })
                .into(holder.postThumbnail);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.root_layout:
                        mListener.onSubSearchInteraction(ACTION_VIEW_POST, holder.postDetails);
                        break;
                    case R.id.dp:
                        mListener.onSubSearchInteraction(ACTION_VIEW_PROFILE, holder.postDetails);
                        break;
                    case R.id.name:
                        mListener.onSubSearchInteraction(ACTION_VIEW_PROFILE, holder.postDetails);
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
        return postDetailsArrayList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.shimmer_layout) RelativeLayout shimmerLayout;
        @BindView(R.id.vignette_layout) FrameLayout vignetteLayout;
        @BindView(R.id.top_layout) RelativeLayout topLayout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.title) ProximaNovaSemiboldTextView title;
        @BindView(R.id.category) ProximaNovaRegularTextView category;
        @BindView(R.id.name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        @BindView(R.id.thumbnail) ImageView postThumbnail;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        PostDetails postDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnSubSearchInteractionListener {
        void onSubSearchInteraction(int action, PostDetails postDetails);
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
}