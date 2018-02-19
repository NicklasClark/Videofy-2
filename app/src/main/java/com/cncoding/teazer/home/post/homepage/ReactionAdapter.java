package com.cncoding.teazer.home.post.homepage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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
import com.cncoding.teazer.home.BaseRecyclerViewAdapter;
import com.cncoding.teazer.home.BaseRecyclerViewHolder;
import com.cncoding.teazer.model.post.PostReaction;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cncoding.teazer.ui.fragment.fragment.FragmentReactionPlayer.OPENED_FROM_OTHER_SOURCE;

/**
 *
 * Created by Prem$ on 1/26/2018.
 */

public class ReactionAdapter extends BaseRecyclerViewAdapter {

    private Context context;
    private ArrayList<PostReaction> reactions;
    private ReactionPlayerListener listener;

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
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return reactions.size() < 6 ? 6 : reactions.size();
    }

    @Override
    public void release() {
    }

    class ViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.root_layout) ShimmerLinearLayout layout;
        @BindView(R.id.thumb) ImageView thumb;
        @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
        PostReaction postReaction;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(int position) {
            try {
                layout.startShimmerAnimation();
                if (position < reactions.size()) {
                    postReaction = reactions.get(position);

                    if (postReaction.getMediaDetail() != null) {
                        title.setBackgroundResource(R.drawable.bg_shimmer_light);
                        Glide.with(context)
                                .load(postReaction.getMediaDetail().getThumbUrl())
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
                                        title.setBackgroundColor(Color.TRANSPARENT);
                                        title.setText(postReaction.getReactTitle());
                                        thumb.setImageDrawable(resource);
                                        return false;
                                    }
                                })
                                .into(thumb);
                    }
                } else {
                    thumb.setImageResource(R.drawable.bg_shimmer_extra_light);
                    title.setBackgroundResource(R.drawable.bg_shimmer_extra_light);
                    title.setText(null);
                }
                layout.stopShimmerAnimation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @OnClick(R.id.root_layout) void onReactionClick() {
            if (postReaction != null && listener != null) {
                listener.reactionPlayer(OPENED_FROM_OTHER_SOURCE, postReaction, null, true);
            }
        }
    }
}