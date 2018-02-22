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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileMyReactionAdapter.ReactionPlayerListener;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.customViews.shimmer.ShimmerLinearLayout;
import com.cncoding.teazer.home.BaseRecyclerViewAdapter;
import com.cncoding.teazer.home.BaseRecyclerViewHolder;
import com.cncoding.teazer.model.giphy.Images;
import com.cncoding.teazer.model.post.PostReaction;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cncoding.teazer.ui.fragment.fragment.FragmentReactionPlayer.OPENED_FROM_OTHER_SOURCE;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_GIF;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_GIFHY;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_VIDEO;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;

/**
 *
 * Created by Prem$ on 1/26/2018.
 */

public class ReactionAdapter extends BaseRecyclerViewAdapter {

    private Context context;
    private ArrayList<PostReaction> reactions;
    private ReactionPlayerListener reactionPlayerListener;
    private boolean isPostClicked = false;

    ReactionAdapter(Context context, ArrayList<PostReaction> reactions) {
        this.context = context;
        this.reactions = reactions;
        reactionPlayerListener = (ReactionPlayerListener) context;
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

                    if (postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_VIDEO) {
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
                                        title.setText(decodeUnicodeString(postReaction.getReactTitle()));
                                        thumb.setImageDrawable(resource);
                                        return false;
                                    }
                                })
                                .into(thumb);
                    }
                    else if (postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
                        title.setBackgroundResource(R.drawable.bg_shimmer_light);
                        Glide.with(context)
                                .load(postReaction.getMediaDetail().getThumbUrl())
                                .apply(new RequestOptions().placeholder(R.drawable.bg_shimmer_light))
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                .apply(RequestOptions.bitmapTransform(new FitCenter()))
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
                                        title.setText(decodeUnicodeString(postReaction.getReactTitle()));
                                        thumb.setImageDrawable(resource);
                                        return false;
                                    }
                                })
                                .into(thumb);
                    }
                    else if (postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_GIFHY) {
                        title.setBackgroundResource(R.drawable.bg_shimmer_light);

                        Gson gson = new Gson();
                        Images images = gson.fromJson(postReaction.getMediaDetail().getExternalMeta(), Images.class);

                        Glide.with(context)
                                .load(images.getFixedHeightSmallStill().getUrl())
                                .apply(new RequestOptions().placeholder(R.drawable.bg_shimmer_light))
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                .apply(RequestOptions.bitmapTransform(new FitCenter()))
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
                                        title.setText(decodeUnicodeString(postReaction.getReactTitle()));
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
            if (postReaction != null && reactionPlayerListener != null) {

                if (!isPostClicked) {
                    isPostClicked = true;
                    if (postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
                        reactionPlayerListener.reactionPlayer(OPENED_FROM_OTHER_SOURCE, postReaction, null, true);
                    } else if(postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_VIDEO){
                        reactionPlayerListener.reactionPlayer(OPENED_FROM_OTHER_SOURCE, postReaction,null, false);
                    } else if(postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_GIFHY){
                        reactionPlayerListener.reactionPlayer(OPENED_FROM_OTHER_SOURCE, postReaction,null, true);
                    }
                    isPostClicked = false;
                }
            }
        }
    }
}