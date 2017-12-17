package com.cncoding.teazer.home.post;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.text.emoji.widget.EmojiAppCompatTextView;
import android.support.v7.widget.RecyclerView;
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
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.utilities.ViewUtils;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_POST;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link OnPostAdapterInteractionListener}.
 */
public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.ViewHolder> {

    private SparseIntArray colorArray;
//    private SparseIntArray dimensionSparseArray;
    private OnPostAdapterInteractionListener listener;
    private ArrayList<PostDetails> posts;
    private Context context;

    PostsListAdapter(ArrayList<PostDetails> posts, Context context) {
        this.posts = posts;
        this.context = context;
//        dimensionSparseArray = new SparseIntArray();
        colorArray = new SparseIntArray();

        if (context instanceof OnPostAdapterInteractionListener) {
            listener = (OnPostAdapterInteractionListener) context;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_screen_post, parent, false);
        return new ViewHolder(view);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.shimmerLayout.setVisibility(View.VISIBLE);
        holder.topLayout.setVisibility(View.INVISIBLE);
        holder.bottomLayout.setVisibility(View.INVISIBLE);
        holder.vignetteLayout.setVisibility(View.INVISIBLE);
        final PostDetails postDetails = posts.get(position);
        MiniProfile postOwner = postDetails.getPostOwner();

        int postWidth = postDetails.getMedias().get(0).getDimension().getWidth();
        int postHeight = postDetails.getMedias().get(0).getDimension().getHeight();
        int width = (ViewUtils.getDeviceWidth(context) -
                (int)((0.5 * context.getResources().getDisplayMetrics().density) + 0.5)) / 2;

        holder.layout.getLayoutParams().width = width;
        if (postHeight < postWidth) {
            holder.layout.setBackgroundResource(R.color.black);
            postHeight = width;
            holder.layout.getLayoutParams().height = postHeight;
        } else {
            holder.layout.setBackgroundResource(R.color.material_grey200);
            holder.layout.getLayoutParams().height = width * postHeight / postWidth;
        }

        String title = postDetails.getTitle();
        holder.caption.setText(title);
        holder.caption.setVisibility(View.VISIBLE);

        if (postDetails.getCategories() != null) {
            holder.category.setVisibility(postDetails.getCategories().isEmpty() ? View.GONE : View.VISIBLE);
            if (holder.category.getVisibility() == View.VISIBLE) {
                holder.category.setText(postDetails.getCategories().get(0).getCategoryName());
                holder.category.setBackground(
                        getBackground(holder.category, position, Color.parseColor(postDetails.getCategories().get(0).getColor())));
            }
        } else holder.category.setVisibility(View.GONE);

        String name = postOwner.getUserName();
        holder.name.setText(name);

        String likes = BLANK_SPACE + String.valueOf(postDetails.getLikes());
        holder.likes.setText(likes);

        if (postDetails.getTotalReactions() > 0) {
            String reactions = "R " + String.valueOf(postDetails.getTotalReactions());
            holder.views.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            holder.views.setText(reactions);
        } else {
            String views = BLANK_SPACE + String.valueOf(postDetails.getMedias().get(0).getViews());
            holder.views.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_view_outline_smaller, 0, 0, 0);
            holder.views.setText(views);
        }

        if (listener != null) {
            View.OnClickListener viewPostDetails = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PostsListFragment.positionToUpdate = holder.getAdapterPosition();
                    PostsListFragment.postDetails = postDetails;
                    listener.onPostInteraction(ACTION_VIEW_POST, postDetails, holder.postThumbnail, holder.layout);
                }
            };
            View.OnClickListener viewProfile = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostInteraction(ACTION_VIEW_PROFILE, postDetails, holder.postThumbnail, holder.layout);
                }
            };

            holder.postThumbnail.setOnClickListener(viewPostDetails);
            holder.profilePic.setOnClickListener(viewProfile);
            holder.name.setOnClickListener(viewProfile);
        }

        Glide.with(context)
                .load(postOwner.getProfileMedia() != null ? postOwner.getProfileMedia().getThumbUrl() : R.drawable.ic_user_male_dp_small)
                .placeholder(R.drawable.ic_user_male_dp_small)
                .crossFade()
                .listener(new RequestListener<Serializable, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Serializable model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Serializable model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.profilePic.setImageDrawable(resource);
                        return true;
                    }
                })
                .into(holder.profilePic);

        Glide.with(context)
                .load(postDetails.getMedias().get(0).getThumbUrl())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.shimmerLayout.setVisibility(View.VISIBLE);
                        holder.topLayout.setVisibility(View.VISIBLE);
                        holder.bottomLayout.setVisibility(View.VISIBLE);
                        holder.vignetteLayout.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.postThumbnail);
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
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.shimmer_layout) RelativeLayout shimmerLayout;
        @BindView(R.id.top_layout) RelativeLayout topLayout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.vignette_layout) FrameLayout vignetteLayout;
        @BindView(R.id.home_screen_post_thumb) ImageView postThumbnail;
        @BindView(R.id.home_screen_post_caption) EmojiAppCompatTextView caption;
        @BindView(R.id.home_screen_post_category) ProximaNovaRegularTextView category;
        @BindView(R.id.home_screen_post_dp) CircularAppCompatImageView profilePic;
        @BindView(R.id.home_screen_post_username) ProximaNovaSemiboldTextView name;
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "' : \"" + caption.getText() + "\"";
        }
    }

    public interface OnPostAdapterInteractionListener {
        void onPostInteraction(int action, PostDetails postDetails, ImageView postThumbnail, RelativeLayout layout);
    }
}