package com.cncoding.teazer.home.post;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.utilities.PlaceHolderDrawableHelper;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_POST;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link OnPostAdapterInteractionListener}.
 */
public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.ViewHolder> {

    private SparseIntArray dimensionSparseArray;
    private OnPostAdapterInteractionListener listener;
    private List<PostDetails> posts;
    private Context context;
    private PostsListFragment postsListFragment;

    PostsListAdapter(List<PostDetails> posts, Context context, PostsListFragment postsListFragment) {
        this.posts = posts;
        this.context = context;
        this.postsListFragment = postsListFragment;
        dimensionSparseArray = new SparseIntArray();
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
        holder.layout.setVisibility(View.INVISIBLE);
        final PostDetails postDetails = posts.get(position);
        Pojos.MiniProfile postOwner = postDetails.getPostOwner();

        if (dimensionSparseArray.get(position) != 0)
            holder.layout.getLayoutParams().height = dimensionSparseArray.get(position);

        Glide.with(context)
                .load(postDetails.getMedias().get(0).getThumbUrl())
                .crossFade()
                .skipMemoryCache(false)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onResourceReady(final GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        if (!isFromMemoryCache) {
                            Animation animation = AnimationUtils.loadAnimation(context, R.anim.float_up);
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
                        if (holder.getAdapterPosition() == 4 || holder.getAdapterPosition() == posts.size() - 1) {
                            postsListFragment.dismissProgressBar();
                        }
                        return false;
                    }

                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.postThumbnail);

        if (postOwner.hasProfileMedia())
            Glide.with(context)
                    .load(postOwner.getProfileMedia().getThumbUrl())
                    .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                    .crossFade()
                    .into(holder.profilePic);
        else
            Glide.with(context)
                    .load(R.drawable.ic_user_dp_small)
                    .crossFade()
                    .skipMemoryCache(true)
                    .into(holder.profilePic);

        holder.caption.setText(postDetails.getTitle());
        if (postDetails.getCategories().size() > 0)
            holder.category.setText(postDetails.getCategories().get(0).getCategoryName());
        else
            holder.category.setVisibility(View.GONE);
//        holder.name.setText(postOwner.getFirstName() + " " + postOwner.getLastName());
        holder.name.setText("@" + postOwner.getUserName());
        holder.popularity.setText(postDetails.getLikes() + " Likes | " + postDetails.getTotalReactions() + " Reactions");

        if (listener != null) {
            View.OnClickListener viewPostDetails = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostInteraction(ACTION_VIEW_POST, postDetails, holder.postThumbnail,
                            holder.layout, getImage(holder.postThumbnail));
                }
            };
            View.OnClickListener viewProfile = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostInteraction(ACTION_VIEW_PROFILE, postDetails, holder.postThumbnail,
                            holder.layout, getImage(holder.postThumbnail));
                }
            };

            holder.postThumbnail.setOnClickListener(viewPostDetails);
            holder.profilePic.setOnClickListener(viewProfile);
            holder.name.setOnClickListener(viewProfile);
        }
    }

    private byte[] getImage(ImageView imageView) {
        Bitmap bitmap = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.home_screen_post_thumb) ImageView postThumbnail;
        @BindView(R.id.home_screen_post_caption) ProximaNovaSemiboldTextView caption;
        @BindView(R.id.home_screen_post_category) ProximaNovaRegularTextView category;
        @BindView(R.id.home_screen_post_dp) CircularAppCompatImageView profilePic;
        @BindView(R.id.home_screen_post_username) ProximaNovaSemiboldTextView name;
        @BindView(R.id.home_screen_post_popularity) ProximaNovaRegularTextView popularity;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "' : \"" + caption.getText() + "\"";
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (context instanceof OnPostAdapterInteractionListener) {
            listener = (OnPostAdapterInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserAdapterInteractionListener");
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        listener = null;
//        context = null;
//        posts = null;
//        postsListFragment = null;
    }

    public interface OnPostAdapterInteractionListener {
        void onPostInteraction(int action, PostDetails postDetails, ImageView postThumbnail, RelativeLayout layout, byte[] image);
    }
}
