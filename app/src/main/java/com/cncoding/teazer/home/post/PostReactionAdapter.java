package com.cncoding.teazer.home.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
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
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.post.PostReaction;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.utilities.ViewUtils.POST_REACTION;
import static com.cncoding.teazer.utilities.ViewUtils.adjustViewSize;
import static com.cncoding.teazer.utilities.ViewUtils.playOnlineVideoInExoPlayer;

public class PostReactionAdapter extends RecyclerView.Adapter<PostReactionAdapter.ViewHolder> {

    private SparseArray<Dimension> dimensionSparseArray;
    private ArrayList<PostReaction> postReactions;
    private Context context;

    PostReactionAdapter(ArrayList<PostReaction> postReactions, Context context) {
        this.postReactions = postReactions;
        this.context = context;
        dimensionSparseArray = new SparseArray<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reaction_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.shimmerLayout.setVisibility(View.VISIBLE);
        holder.vignetteLayout.setVisibility(View.INVISIBLE);
        holder.caption.setVisibility(View.INVISIBLE);
        holder.bottomLayout.setVisibility(View.INVISIBLE);

        final PostReaction postReaction = postReactions.get(position);
        MiniProfile postOwner = postReaction.getReactOwner();

        if (dimensionSparseArray.get(position) == null) {
            adjustViewSize(context, postReaction.getMediaDetail().getReactDimension().getWidth(),
                    postReaction.getMediaDetail().getReactDimension().getHeight(),
                    holder.layout.getLayoutParams(), position, dimensionSparseArray, false);
        } else {
            holder.layout.getLayoutParams().width = dimensionSparseArray.get(position).getWidth();
            holder.layout.getLayoutParams().height = dimensionSparseArray.get(position).getHeight();
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

        holder.caption.setText(postReaction.getReact_title());
        String nameText = postOwner.getFirstName() + " " + postOwner.getLastName();
        holder.name.setText(nameText);
        String likesText = "  " + postReaction.getLikes();
        holder.likes.setText(likesText);
        String viewsText = "  " + postReaction.getViews();
        holder.views.setText(viewsText);

        Glide.with(context)
                .load(postReaction.getMediaDetail().getThumbUrl())
                .crossFade()
                .skipMemoryCache(false)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onResourceReady(final GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.shimmerLayout.setVisibility(View.INVISIBLE);
                        holder.vignetteLayout.setVisibility(View.VISIBLE);
                        holder.caption.setVisibility(View.VISIBLE);
                        holder.bottomLayout.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                })
//                .animate(R.anim.float_up)
                .into(holder.postThumbnail);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.root_layout:
                        playOnlineVideoInExoPlayer(context, POST_REACTION, postReaction, null);
                        break;
                    case R.id.reaction_post_dp | R.id.reaction_post_name:
                        //region TODO
                        // If there is a way to know that the person who posted the reaction
                        // is the user-self or some other, apply that logic here.
                        // call
//                        if (context instanceof PostDetailsActivity) {
//                            ((PostDetailsActivity) context).onTaggedUserInteraction(
//                                    postReaction.getReactOwner().getUserId(),
//                                    /*apply "isSelf logic here"*/);
//                        }
                        //endregion
                        break;
                }
            }
        };
        holder.layout.setOnClickListener(listener);
        holder.profilePic.setOnClickListener(listener);
        holder.name.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return postReactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
        @BindView(R.id.shimmer_layout) RelativeLayout shimmerLayout;
        @BindView(R.id.vignette_layout) FrameLayout vignetteLayout;
        @BindView(R.id.bottom_layout) RelativeLayout bottomLayout;
        @BindView(R.id.reaction_post_thumb) ImageView postThumbnail;
        @BindView(R.id.reaction_post_caption) ProximaNovaSemiboldTextView caption;
        @BindView(R.id.reaction_post_dp) CircularAppCompatImageView profilePic;
        @BindView(R.id.reaction_post_name) ProximaNovaSemiboldTextView name;
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

    void playFromDeepLink(PostReaction postReaction) {
        playOnlineVideoInExoPlayer(context, POST_REACTION, postReaction, null);
    }
}
