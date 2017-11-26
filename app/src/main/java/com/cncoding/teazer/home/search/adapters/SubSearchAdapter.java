package com.cncoding.teazer.home.search.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.getByteArrayFromImage;

/**
 *
 * Created by Prem $ on 11/24/2017.
 */

public class SubSearchAdapter extends RecyclerView.Adapter<SubSearchAdapter.ViewHolder> {

    private OnSubSearchInteractionListener mListener;
    private ArrayList<PostDetails> postDetailsArrayList;
    private SparseIntArray dimensionSparseArray;
    private Context context;

    public SubSearchAdapter(ArrayList<PostDetails> postDetailsArrayList, Context context) {
        this.postDetailsArrayList = postDetailsArrayList;
        this.context = context;
        dimensionSparseArray = new SparseIntArray();
        if (context instanceof OnSubSearchInteractionListener) {
            mListener = (SubSearchAdapter.OnSubSearchInteractionListener) context;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_interests_post, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.postDetails = postDetailsArrayList.get(position);

        if (dimensionSparseArray.get(position) != 0)
            holder.layout.getLayoutParams().height = dimensionSparseArray.get(position);

        holder.caption.setText(holder.postDetails.getTitle());

        if (holder.postDetails.getCategories().size() > 0)
            holder.category.setText(holder.postDetails.getCategories().get(0).getCategoryName());

        String name = holder.postDetails.getPostOwner().getFirstName() + BLANK_SPACE + holder.postDetails.getPostOwner().getLastName();
        holder.name.setText(name);

        String likes = String.valueOf(holder.postDetails.getLikes());
        holder.likes.setText(likes);

        String views = String.valueOf(holder.postDetails.getMedias().get(0).getViews());
        holder.views.setText(views);

        Glide.with(context)
                .load(holder.postDetails.getMedias().get(0).getThumbUrl())
                .placeholder(R.drawable.bg_placeholder)
                .crossFade()
                .skipMemoryCache(false)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onResourceReady(final GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        if (!isFromMemoryCache) {
                            Animation animation = AnimationUtils.loadAnimation(context, R.anim.float_down);
                            animation.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                    dimensionSparseArray.put(holder.getAdapterPosition(), holder.layout.getHeight());
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    holder.layout.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });
                            holder.layout.startAnimation(animation);
                        } else {
                            holder.layout.animate().alpha(1).setDuration(280).start();
                            holder.layout.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.postThumbnail);

        if (holder.postDetails.getPostOwner().hasProfileMedia()) {
            Glide.with(context)
                    .load(holder.postDetails.getPostOwner().getProfileMedia().getThumbUrl())
                    .placeholder(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .into(holder.profilePic);
        } else {
            Glide.with(context)
                    .load(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .into(holder.profilePic);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onSubSearchInteraction(holder.postDetails, getByteArrayFromImage(holder.postThumbnail));
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.post_thumb) ImageView postThumbnail;
        @BindView(R.id.title) ProximaNovaSemiboldTextView caption;
        @BindView(R.id.category) ProximaNovaRegularTextView category;
        @BindView(R.id.dp) CircularAppCompatImageView profilePic;
        @BindView(R.id.name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        PostDetails postDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnSubSearchInteractionListener {
        void onSubSearchInteraction(PostDetails postDetails, byte[] byteArrayFromImage);
    }
}