package com.cncoding.teazer.home.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.HomeScreenPostFragment;
import com.cncoding.teazer.utilities.PlaceHolderDrawableHelper;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostReaction;
import com.cncoding.teazer.utilities.Pojos.Post.PostReactionsList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link HomeScreenPostFragment.HomeScreenPostInteractionListener}.
 */
public class PostReactionAdapter extends RecyclerView.Adapter<PostReactionAdapter.ViewHolder> {

    static final int ACTION_VIEW_POST = 0;
    static final int ACTION_VIEW_PROFILE = 1;
    static final int ACTION_VIEW_CATEGORY_POSTS = 2;

    private final PostReactionsList posts;
    private final PostDetailsFragment.OnPostDetailsInteractionListener mListener;
    private Context context;

    public PostReactionAdapter(PostReactionsList posts, PostDetailsFragment.OnPostDetailsInteractionListener listener, Context context) {
        this.posts = posts;
        mListener = listener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_screen_post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PostReaction postReaction = posts.getReactions().get(position);
        MiniProfile postOwner = postReaction.getReactOwner();

        Glide.with(context)
                .load(postReaction.getMediaDetail().getThumbUrl())
                .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                .crossFade()
//                .animate(android.R.anim.fade_in)
                .into(holder.postThumbnail);

        if (postOwner.hasProfileMedia())
            Glide.with(context)
                    .load(postOwner.getProfileMedia().getThumbUrl())
                    .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                    .crossFade()
                    .animate(R.anim.zoom_in)
                    .into(holder.profilePic);

        holder.caption.setText(postReaction.getTitle());
        holder.name.setText(postOwner.getFirstName() + " " + postOwner.getLastName());
        holder.likes.setText("  " + postReaction.getLikes());
        holder.views.setText("  " + postReaction.getViews());

//        if (mListener != null) {
//            View.OnClickListener viewPostDetails = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mListener.onPostDetailsInteraction(ACTION_VIEW_POST);
//                }
//            };
//            View.OnClickListener viewProfile = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mListener.onPostDetailsInteraction(ACTION_VIEW_PROFILE);
//                }
//            };
//            View.OnClickListener viewCategoryPosts = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mListener.onPostDetailsInteraction(ACTION_VIEW_CATEGORY_POSTS);
//                }
//            };
//
//            holder.postThumbnail.setOnClickListener(viewPostDetails);
//            holder.likes.setOnClickListener(viewPostDetails);
//            holder.category.setOnClickListener(viewCategoryPosts);
//            holder.profilePic.setOnClickListener(viewProfile);
//            holder.name.setOnClickListener(viewProfile);
//        }
    }

    @Override
    public int getItemCount() {
        return posts.getReactions().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.home_screen_post_layout) RelativeLayout layout;
        @BindView(R.id.reaction_post_thumb) ImageView postThumbnail;
        @BindView(R.id.reaction_post_caption) ProximaNovaSemiboldTextView caption;
        @BindView(R.id.reaction_post_dp) CircularImageView profilePic;
        @BindView(R.id.home_screen_post_name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.reaction_post_likes) ProximaNovaRegularTextView likes;
        @BindView(R.id.reaction_post_views) ProximaNovaRegularTextView views;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "' : \"" + caption.getText() + "\"";
        }
    }
}
