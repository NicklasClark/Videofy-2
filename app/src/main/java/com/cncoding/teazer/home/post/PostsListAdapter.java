package com.cncoding.teazer.home.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_POST;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.getByteArrayFromImage;

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
        holder.postDetails = posts.get(position);
        Pojos.MiniProfile postOwner = holder.postDetails.getPostOwner();

        if (dimensionSparseArray.get(position) != 0) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.layout.getLayoutParams();
            params.height = dimensionSparseArray.get(position);
            holder.layout.setLayoutParams(params);
        }

        Glide.with(context)
                .load(holder.postDetails.getMedias().get(0).getThumbUrl())
                .crossFade()
                .placeholder(R.drawable.bg_placeholder)
                .skipMemoryCache(false)
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

                        dimensionSparseArray.put(holder.getAdapterPosition(), height);
//                        holder.layout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fast_fade_in));
                        holder.layout.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.postThumbnail);

        if (postOwner.hasProfileMedia())
            Glide.with(context)
                    .load(postOwner.getProfileMedia().getThumbUrl())
                    .placeholder(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .into(holder.profilePic);
        else
            Glide.with(context)
                    .load(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .skipMemoryCache(true)
                    .into(holder.profilePic);

        String title = holder.postDetails.getTitle();
        holder.caption.setText(title);

        if (holder.postDetails.getCategories().size() > 0)
            holder.category.setText(holder.postDetails.getCategories().get(0).getCategoryName());
        else
            holder.category.setVisibility(View.GONE);

        String name = postOwner.getUserName();
        holder.name.setText(name);

        String likes = BLANK_SPACE + String.valueOf(holder.postDetails.getLikes());
        holder.likes.setText(likes);

        String views = BLANK_SPACE + String.valueOf(holder.postDetails.getMedias().get(0).getViews());
        holder.views.setText(views);

        if (listener != null) {
            View.OnClickListener viewPostDetails = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostInteraction(ACTION_VIEW_POST, holder.postDetails, holder.postThumbnail,
                            holder.layout, getByteArrayFromImage(holder.postThumbnail));
                }
            };
            View.OnClickListener viewProfile = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostInteraction(ACTION_VIEW_PROFILE, holder.postDetails, holder.postThumbnail,
                            holder.layout, getByteArrayFromImage(holder.postThumbnail));
                }
            };

            holder.postThumbnail.setOnClickListener(viewPostDetails);
            holder.profilePic.setOnClickListener(viewProfile);
            holder.name.setOnClickListener(viewProfile);
        }
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
        @BindView(R.id.likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.views) ProximaNovaRegularTextView views;
        PostDetails postDetails;

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
