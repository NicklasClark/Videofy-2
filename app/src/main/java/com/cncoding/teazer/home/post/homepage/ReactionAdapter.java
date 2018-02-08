package com.cncoding.teazer.home.post.homepage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileMyReactionAdapter.ReactionPlayerListener;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.customViews.shimmer.ShimmerLinearLayout;
import com.cncoding.teazer.model.post.PostReaction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cncoding.teazer.ui.fragment.fragment.FragmentReactionplayer.OPENED_FROM_OTHER_SOURCE;

/**
 *
 * Created by Prem$ on 1/26/2018.
 */

public class ReactionAdapter extends RecyclerView.Adapter<ReactionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostReaction> reactions;
    private ReactionPlayerListener listener;

    /**
     * @param reactions first String in pair contains the thumbUrl, second String contains the title.
     */
    ReactionAdapter(Context context, ArrayList<PostReaction> reactions) {
        this.context = context;
        this.reactions = reactions;
        listener = (ReactionPlayerListener) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_screen_post_reaction, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            holder.layout.startShimmerAnimation();
            if (position < reactions.size()) {
                holder.postReaction = reactions.get(position);

                if (holder.postReaction.getMediaDetail() != null) {
                    holder.title.setBackgroundResource(R.drawable.bg_shimmer_light);
                    Glide.with(context)
                            .load(holder.postReaction.getMediaDetail().getThumbUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.bg_shimmer_light))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                            Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                               DataSource dataSource, boolean isFirstResource) {
                                    holder.title.setBackgroundColor(Color.TRANSPARENT);
                                    holder.title.setText(holder.postReaction.getReactTitle());
                                    holder.thumb.setImageDrawable(resource);
                                    return false;
                                }
                            })
                            .into(holder.thumb);
                }
            } else {
                holder.thumb.setImageResource(R.drawable.bg_shimmer_light);
                holder.title.setBackgroundResource(R.drawable.bg_shimmer_light);
                holder.title.setText(null);
            }
            holder.layout.stopShimmerAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) ShimmerLinearLayout layout;
        @BindView(R.id.thumb) ImageView thumb;
        @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
        PostReaction postReaction;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root_layout) public void onReactionClick() {
            if (postReaction != null && listener != null) {
                listener.reactionPlayer(OPENED_FROM_OTHER_SOURCE, postReaction, null);
            }
        }
    }
}