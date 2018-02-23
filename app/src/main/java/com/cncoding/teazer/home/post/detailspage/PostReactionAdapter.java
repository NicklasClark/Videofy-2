package com.cncoding.teazer.home.post.detailspage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileMyReactionAdapter;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.model.base.Dimension;
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.giphy.Images;
import com.cncoding.teazer.model.post.PostReaction;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.ui.fragment.fragment.FragmentReactionPlayer.OPENED_FROM_OTHER_SOURCE;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_GIF;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_GIPHY;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_VIDEO;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.ViewUtils.POST_REACTION;
import static com.cncoding.teazer.utilities.ViewUtils.adjustViewSize;
import static com.cncoding.teazer.utilities.ViewUtils.playOnlineVideoInExoPlayer;

public class PostReactionAdapter extends RecyclerView.Adapter<PostReactionAdapter.ViewHolder> {

    private SparseArray<Dimension> dimensionSparseArray;
    private ArrayList<PostReaction> postReactions;
    private Context context;
    private ProfileMyReactionAdapter.ReactionPlayerListener reactionPlayerListener;

    PostReactionAdapter(ArrayList<PostReaction> postReactions, Context context) {
        this.postReactions = postReactions;
        this.context = context;
        reactionPlayerListener=(ProfileMyReactionAdapter.ReactionPlayerListener)context;
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
            adjustViewSize(context, postReaction.getMediaDetail().getMediaDimension().getWidth(),
                    postReaction.getMediaDetail().getMediaDimension().getHeight(),
                    holder.layout.getLayoutParams(), position, dimensionSparseArray, false);
        } else {
            holder.layout.getLayoutParams().width = dimensionSparseArray.get(position).getWidth();
            holder.layout.getLayoutParams().height = dimensionSparseArray.get(position).getHeight();
        }

        Glide.with(context)
                .load(postOwner.getProfileMedia() != null ? postOwner.getProfileMedia().getThumbUrl() : R.drawable.ic_user_male_dp_small)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_user_male_dp_small)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        holder.profilePic.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(holder.profilePic);

        String title = postReaction.getReactTitle();
        holder.caption.setText(decodeUnicodeString(title));

        holder.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"profile",Toast.LENGTH_SHORT).show();
            }
        });
        holder.caption.setText(decodeUnicodeString(title));
        String nameText = postOwner.getFirstName() + " " + postOwner.getLastName();
        holder.name.setText(nameText);
        String likesText = "  " + postReaction.getLikes();
        holder.likes.setText(likesText);
        String viewsText = "  " + postReaction.getViews();
        holder.views.setText(viewsText);

        if (postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_VIDEO) {
            holder.layout.setEnabled(true);
            Glide.with(context)
                    .load(postReaction.getMediaDetail().getReactThumbUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.shimmerLayout.setVisibility(View.INVISIBLE);
                            holder.vignetteLayout.setVisibility(View.VISIBLE);
                            holder.caption.setVisibility(View.VISIBLE);
                            holder.bottomLayout.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
    //                .animate(R.anim.float_up)
                    .into(holder.postThumbnail);
        } else if (postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
            holder.layout.setEnabled(true);
            Glide.with(context)
                    .load(postReaction.getMediaDetail().getReactMediaUrl())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .apply(RequestOptions.bitmapTransform(new FitCenter()))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.shimmerLayout.setVisibility(View.INVISIBLE);
                            holder.vignetteLayout.setVisibility(View.VISIBLE);
                            holder.caption.setVisibility(View.VISIBLE);
                            holder.bottomLayout.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(holder.postThumbnail);
        } else if (postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_GIPHY) {
            holder.layout.setEnabled(true);

            Gson gson = new Gson();
            Images images = gson.fromJson(postReaction.getMediaDetail().getExternalMeta(), Images.class);

            Glide.with(context)
                    .load(images.getDownsized().getUrl())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .apply(RequestOptions.bitmapTransform(new FitCenter()))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model,
                                                       Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.shimmerLayout.setVisibility(View.INVISIBLE);
                            holder.vignetteLayout.setVisibility(View.VISIBLE);
                            holder.caption.setVisibility(View.VISIBLE);
                            holder.bottomLayout.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(holder.postThumbnail);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.root_layout:
                    //    playOnlineVideoInExoPlayer(context, POST_REACTION, postReaction, null);
                        if (postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
                            reactionPlayerListener.reactionPlayer(OPENED_FROM_OTHER_SOURCE, postReaction, null, true);
                        } else if (postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_GIPHY) {
                            reactionPlayerListener.reactionPlayer(OPENED_FROM_OTHER_SOURCE, postReaction, null, true);
                        } else {
                            reactionPlayerListener.reactionPlayer(OPENED_FROM_OTHER_SOURCE, postReaction, null, false);
                        }
                        break;
                    case R.id.reaction_post_dp | R.id.reaction_post_name:
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
        @BindView(R.id.reaction_post_caption) ProximaNovaSemiBoldTextView caption;
        @BindView(R.id.reaction_post_dp) CircularAppCompatImageView profilePic;
        @BindView(R.id.reaction_post_name) ProximaNovaSemiBoldTextView name;
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
