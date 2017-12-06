package com.cncoding.teazer.home.post;

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
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostReaction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.ViewUtils.POST_REACTION;
import static com.cncoding.teazer.utilities.ViewUtils.playOnlineVideoInExoPlayer;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link PostReactionAdapterListener}.
 */
public class PostReactionAdapter extends RecyclerView.Adapter<PostReactionAdapter.ViewHolder> {

    private SparseIntArray dimensionSparseArray;
    private ArrayList<PostReaction> postReactions;
    private Context context;
    private PostReactionAdapterListener listener;

    PostReactionAdapter(ArrayList<PostReaction> postReactions, Context context) {
        this.postReactions = postReactions;
        this.context = context;
        dimensionSparseArray = new SparseIntArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reaction_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PostReaction postReaction = postReactions.get(position);
        MiniProfile postOwner = postReaction.getReactOwner();

        if (dimensionSparseArray.get(position) != 0)
            holder.layout.getLayoutParams().height = dimensionSparseArray.get(position);

        Glide.with(context)
                .load(postReaction.getMediaDetail().getThumbUrl())
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
                        return false;
                    }

                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                })
//                .animate(R.anim.float_up)
                .into(holder.postThumbnail);

        if (postOwner.hasProfileMedia())
            Glide.with(context)
                    .load(postOwner.getProfileMedia().getThumbUrl())
                    .placeholder(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .into(holder.profilePic);

        holder.caption.setText(postReaction.getReact_title());
        String nameText = postOwner.getFirstName() + " " + postOwner.getLastName();
        holder.name.setText(nameText);
        String likesText = "  " + postReaction.getLikes();
        holder.likes.setText(likesText);
        String viewsText = "  " + postReaction.getViews();
        holder.views.setText(viewsText);

        if (listener != null) {
            View.OnClickListener viewReactionDetails = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playOnlineVideoInExoPlayer(context, POST_REACTION, postReaction, null);

                    ApiCallingService.React.incrementReactionViewCount(postReaction.getMediaDetail().getMediaId(), context)
                            .enqueue(new Callback<ResultObject>() {
                                @Override
                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                    if (response.code() == 200 && response.body().getStatus())
                                        postReactions.get(holder.getAdapterPosition()).views++;
                                }

                                @Override
                                public void onFailure(Call<ResultObject> call, Throwable t) {

                                }
                            });
//                    listener.onPostReactionInteraction(ACTION_VIEW_REACTION, postReaction);
                }
            };
//            View.OnClickListener viewProfile = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.onPostReactionInteraction(ACTION_VIEW_PROFILE, postReaction);
//                }
//            };

            holder.postThumbnail.setOnClickListener(viewReactionDetails);
            holder.likes.setOnClickListener(viewReactionDetails);
//            holder.profilePic.setOnClickListener(viewProfile);
//            holder.name.setOnClickListener(viewProfile);
        }
    }

    @Override
    public int getItemCount() {
        return postReactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) RelativeLayout layout;
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (context instanceof PostReactionAdapterListener) {
            listener = (PostReactionAdapterListener) context;
        }
        else {
            throw new RuntimeException(context.toString()
                    + " must implement PostReactionAdapterListener");
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        listener = null;
//        postReactions = null;
//        context = null;
    }

    public interface PostReactionAdapterListener {
        void onPostReactionInteraction(int action, PostReaction postReaction);
    }
}
