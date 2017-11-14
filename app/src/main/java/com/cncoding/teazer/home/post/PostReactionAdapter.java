package com.cncoding.teazer.home.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.utilities.PlaceHolderDrawableHelper;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostReaction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_REACTION;

/**
 * {@link RecyclerView.Adapter} that can display {@link PostDetails} and make a call to the
 * specified {@link PostReactionAdapterListener}.
 */
public class PostReactionAdapter extends RecyclerView.Adapter<PostReactionAdapter.ViewHolder> {

    private ArrayList<PostReaction> postReactions;
    private Context context;
    private PostReactionAdapterListener listener;

    PostReactionAdapter(ArrayList<PostReaction> postReactions, Context context) {
        this.postReactions = postReactions;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reaction_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PostReaction postReaction = postReactions.get(position);
        MiniProfile postOwner = postReaction.getReactOwner();

        Glide.with(context)
                .load(postReaction.getMediaDetail().getThumbUrl())
                .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                .crossFade()
                .animate(R.anim.float_up)
                .into(holder.postThumbnail);

        if (postOwner.hasProfileMedia())
            Glide.with(context)
                    .load(postOwner.getProfileMedia().getThumbUrl())
                    .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                    .crossFade()
                    .into(holder.profilePic);

        holder.caption.setText(postReaction.getReact_title());
        holder.name.setText(postOwner.getFirstName() + " " + postOwner.getLastName());
        holder.likes.setText("  " + postReaction.getLikes());
        holder.views.setText("  " + postReaction.getViews());

        if (listener != null) {
            View.OnClickListener viewPostDetails = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostReactionInteraction(ACTION_VIEW_REACTION, postReaction);
                }
            };
            View.OnClickListener viewProfile = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPostReactionInteraction(ACTION_VIEW_PROFILE, postReaction);
                }
            };

            holder.postThumbnail.setOnClickListener(viewPostDetails);
            holder.likes.setOnClickListener(viewPostDetails);
            holder.profilePic.setOnClickListener(viewProfile);
            holder.name.setOnClickListener(viewProfile);
        }
    }

    @Override
    public int getItemCount() {
        return postReactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.home_screen_post_layout) RelativeLayout layout;
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
