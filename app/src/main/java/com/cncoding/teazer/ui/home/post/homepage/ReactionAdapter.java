package com.cncoding.teazer.ui.home.post.homepage;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.giphy.Images;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.customviews.shimmer.ShimmerLinearLayout;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentReactionPlayer;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.LayoutInflater.from;
import static com.bumptech.glide.load.engine.DiskCacheStrategy.RESOURCE;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_GIF;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_GIPHY;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_VIDEO;
import static com.cncoding.teazer.utilities.common.CommonUtilities.decodeUnicodeString;

/**
 *
 * Created by Prem$ on 1/26/2018.
 */

public class ReactionAdapter extends BaseRecyclerView.Adapter {

    private PostsListFragment fragment;
    private ArrayList<PostReaction> reactions;
    private boolean isPostClicked = false;

    ReactionAdapter(PostsListFragment fragment, ArrayList<PostReaction> reactions) {
        this.fragment = fragment;
        this.reactions = reactions;
    }

    @Override
    public ReactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReactionViewHolder(from(parent.getContext()).inflate(R.layout.item_home_screen_post_reaction, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return reactions.size() < 5 ? 5 : reactions.size();
    }

    @Override
    public void release() {
    }

    @Override
    public void notifyDataChanged() {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    class ReactionViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.root_layout) ShimmerLinearLayout layout;
        @BindView(R.id.thumb) ImageView thumb;
        @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
        PostReaction postReaction;

        RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                           DataSource dataSource, boolean isFirstResource) {
                setTitle();
                thumb.setImageDrawable(resource);
                return false;
            }
        };

        ReactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            try {
                layout.startShimmerAnimation();
                if (getAdapterPosition() < reactions.size()) {
                    postReaction = reactions.get(getAdapterPosition());
                    title.setBackgroundResource(R.drawable.bg_shimmer_light);
                    Glide.with(fragment)
                            .load(getRealUrl(postReaction.getMediaDetail().getMediaType()))
                            .apply(RequestOptions.bitmapTransform(new FitCenter())
                                    .placeholder(R.drawable.bg_shimmer_light).diskCacheStrategy(RESOURCE))
                            .listener(requestListener)
                            .into(thumb);
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

        private String getRealUrl(int mediaType) {
            switch (mediaType) {
                case MEDIA_TYPE_VIDEO:
                    return postReaction.getMediaDetail().getReactThumbUrl();
                case MEDIA_TYPE_GIF:
                    return postReaction.getMediaDetail().getReactThumbUrl();
                case MEDIA_TYPE_GIPHY:
                    Images images = new Gson().fromJson(postReaction.getMediaDetail().getExternalMeta(), Images.class);
                    return images.getFixedHeightSmallStill().getUrl();
//                    return images.getFixedHeightSmallStill().getUrl() != null && !images.getFixedHeightSmallStill().getUrl().equals("") ?
//                            images.getFixedHeightSmallStill().getUrl() :
//                            images.getDownsized().getUrl();
                default:
                    return "";
            }
        }

        private void setTitle() {
            title.setBackgroundColor(Color.TRANSPARENT);
            title.setText(decodeUnicodeString(postReaction.getReactTitle()));
        }

        @OnClick(R.id.root_layout) void onReactionClick() {
            if (postReaction != null) {
                if (!isPostClicked) {
                    isPostClicked = true;
                    if (postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
                        fragment.navigation.pushFragment(
                                FragmentReactionPlayer.newInstance(postReaction, true));
                    } else if(postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_VIDEO){
                        fragment.navigation.pushFragment(
                                FragmentReactionPlayer.newInstance(postReaction, false));
                    } else if(postReaction.getMediaDetail().getMediaType() == MEDIA_TYPE_GIPHY){
                        fragment.navigation.pushFragment(
                                FragmentReactionPlayer.newInstance(postReaction, true));
                    }
                    isPostClicked = false;
                }
            }
        }
    }
}