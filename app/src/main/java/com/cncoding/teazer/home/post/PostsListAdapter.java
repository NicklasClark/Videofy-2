package com.cncoding.teazer.home.post;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.model.base.Dimension;
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.post.PostDetails;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUTFUrl;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.adjustViewSize;
import static com.cncoding.teazer.utilities.ViewUtils.initializeShimmer;
import static com.cncoding.teazer.utilities.ViewUtils.prepareLayout;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link OnPostAdapterInteractionListener}.
 */
public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.ViewHolder> {

    private SparseIntArray colorArray;
    private SparseArray<Dimension> dimensionSparseArray;
    private OnPostAdapterInteractionListener listener;
    private ArrayList<PostDetails> posts;
    private Context context;

    PostsListAdapter(ArrayList<PostDetails> posts, Context context) {
        this.posts = posts;
        this.context = context;
        dimensionSparseArray = new SparseArray<>();
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

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            initializeShimmer(holder.shimmerLayout, holder.topLayout, holder.bottomLayout, holder.vignetteLayout);

            final PostDetails postDetails = posts.get(position);
            MiniProfile postOwner = postDetails.getPostOwner();

                /*Adjust view size before loading anything*/
            if (dimensionSparseArray.get(position) == null) {
                adjustViewSize(context, postDetails.getMedias().get(0).getDimension().getWidth(),
                        postDetails.getMedias().get(0).getDimension().getHeight(),
                        holder.layout.getLayoutParams(), position, dimensionSparseArray, true);
            } else {
                holder.layout.getLayoutParams().width = dimensionSparseArray.get(position).getWidth();
                holder.layout.getLayoutParams().height = dimensionSparseArray.get(position).getHeight();
            }

            String title = postDetails.getTitle();
            holder.caption.setText(decodeUTFUrl(title));
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
                String reactions = BLANK_SPACE + String.valueOf(postDetails.getTotalReactions());
                holder.views.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_reaction_home, 0, 0, 0);
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
//                        listener.onPostInteraction(ACTION_VIEW_POST, postDetails);
                        fetchPostDetails(postDetails.getPostId(), holder.getAdapterPosition());
                    }
                };
                View.OnClickListener viewProfile = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onPostInteraction(ACTION_VIEW_PROFILE, postDetails);
                    }
                };

                holder.postThumbnail.setOnClickListener(viewPostDetails);
                holder.profilePic.setOnClickListener(viewProfile);
                holder.name.setOnClickListener(viewProfile);
            }

            Glide.with(context)
                    .load(postOwner.getProfileMedia() != null ? postOwner.getProfileMedia().getMediaUrl() :
                            R.drawable.ic_user_male_dp_small)
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
                    .fitCenter()
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position, List<Object> payloads) {
//        try {
//            if ((payloads != null && payloads.isEmpty()) || payloads == null) {
//
//            }
//            else {
//                Bundle bundle = (Bundle) payloads.get(0);
//                for (String key : bundle.keySet()) {
//                    switch (key) {
//                        case POST_DETAILS:
//                            posts.remove(position);
//                            posts.add(position, (PostDetails) bundle.getParcelable(POST_DETAILS));
//                            return;
//                        case LIKES:
//                            posts.get(position).likes = bundle.getInt(LIKES);
//                            break;
//                        case TOTAL_REACTIONS:
//                            posts.get(position).total_reactions = bundle.getInt(TOTAL_REACTIONS);
//                            break;
//                        case TOTAL_TAGS:
//                            posts.get(position).total_tags = bundle.getInt(TOTAL_TAGS);
//                            break;
//                        case HAS_CHECKIN:
//                            posts.get(position).has_checkin = bundle.getBoolean(HAS_CHECKIN);
//                            break;
//                        case TITLE:
//                            posts.get(position).title = bundle.getString(TITLE);
//                            break;
//                        case CAN_REACT:
//                            posts.get(position).can_react = bundle.getBoolean(CAN_REACT);
//                            break;
//                        case CAN_LIKE:
//                            posts.get(position).can_like = bundle.getBoolean(CAN_LIKE);
//                            break;
//                        case CHECKIN:
//                            posts.get(position).check_in = bundle.getParcelable(CHECKIN);
//                            break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
        @BindView(R.id.home_screen_post_caption)
        ProximaNovaSemiboldTextView caption;
        @BindView(R.id.home_screen_post_category) ProximaNovaRegularTextView category;
        @BindView(R.id.home_screen_post_dp) CircularAppCompatImageView profilePic;
        @BindView(R.id.home_screen_post_username) ProximaNovaRegularTextView name;
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
        void onPostInteraction(int action, PostDetails postDetails);
    }

    private void fetchPostDetails(int postId, final int adapterPosition) {
        ApiCallingService.Posts.getPostDetails(postId, context)
                .enqueue(new Callback<PostDetails>() {
                    @Override
                    public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                PostsListFragment.positionToUpdate = adapterPosition;
                                PostsListFragment.postDetails = response.body();
                                PostDetailsActivity.newInstance(context, response.body(), null, true,
                                        true, response.body().getMedias().get(0).getThumbUrl(), null);
//                                listener.onPostInteraction(ACTION_VIEW_POST, postDetails, holder.postThumbnail, holder.layout);
                            } else {
                                Toast.makeText(context, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<PostDetails> call, Throwable t) {
                        Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}