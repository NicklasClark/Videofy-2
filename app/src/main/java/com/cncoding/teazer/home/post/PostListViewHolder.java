package com.cncoding.teazer.home.post;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.exoplayer.ExoPlayerViewHelper;
import com.cncoding.teazer.customViews.exoplayer.SimpleExoPlayerView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.customViews.shimmer.ShimmerRelativeLayout;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.utilities.audio.AudioVolumeContentObserver.OnAudioVolumeChangedListener;
import com.cncoding.teazer.utilities.audio.AudioVolumeObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.model.base.MiniProfile.MALE;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.ViewUtils.adjustViewSize;
import static com.cncoding.teazer.utilities.ViewUtils.launchReactionCamera;

/**
 *
 * Created by Prem$ on 2/2/2018.
 */
class PostListViewHolder extends RecyclerView.ViewHolder implements ToroPlayer, OnAudioVolumeChangedListener {

    @LayoutRes static final int LAYOUT_RES = R.layout.item_home_screen_post_new;

    @BindView(R.id.root_layout) LinearLayout layout;
    @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
    @BindView(R.id.location) ProximaNovaRegularTextView location;
    @BindView(R.id.category) ProximaNovaSemiBoldTextView category;
    @BindView(R.id.category_extra) ProximaNovaRegularTextView categoryExtra;
    @BindView(R.id.content) SimpleExoPlayerView playerView;
    @BindView(R.id.volume_control) ImageView volumeControl;
    @BindView(R.id.dp) CircularAppCompatImageView dp;
    @BindView(R.id.username) ProximaNovaSemiBoldTextView username;
    @BindView(R.id.likes) ProximaNovaRegularTextView likes;
    @BindView(R.id.views) ProximaNovaRegularTextView views;
    @BindView(R.id.reactions) ProximaNovaRegularTextView reactions;
    @BindView(R.id.popularity_layout_shimmer) ProximaNovaRegularTextView popularityLayoutShimmer;
    @BindView(R.id.list) RecyclerView reactionListView;
    @BindView(R.id.shimmer_layout_top) ShimmerRelativeLayout shimmerLayoutTop;
    @BindView(R.id.shimmer_layout_mid) ShimmerRelativeLayout shimmerLayoutMid;

    private AudioVolumeObserver audioVolumeObserver;
    private PostsListAdapter postsListAdapter;
    private ExoPlayerViewHelper helper;
    private PostDetails postDetails;

    PostListViewHolder(PostsListAdapter postsListAdapter, View view) {
        super(view);
        this.postsListAdapter = postsListAdapter;
        ButterKnife.bind(this, view);
        playerView.setUseController(false);
        if (audioVolumeObserver == null) {
            audioVolumeObserver = new AudioVolumeObserver(postsListAdapter.context);
        }
        registerAudioObserver();
    }

    @OnClick(R.id.content) void viewPost() {
        if (!postsListAdapter.isPostClicked) {
            postsListAdapter.isPostClicked = true;
            postsListAdapter.fetchPostDetails(postDetails.getPostId(), getAdapterPosition());
        }
    }

    @OnClick(R.id.dp) void viewProfileThroughDp() {
        viewProfile();
    }

    @OnClick(R.id.username) void viewProfileThroughUsername() {
        viewProfile();
    }

    private void viewProfile() {
        if (postsListAdapter.listener != null) {
            postsListAdapter.listener.onPostInteraction(ACTION_VIEW_PROFILE, postDetails);
        }
    }

    @OnClick(R.id.volume_control) void controlVolume() {
        postsListAdapter.isMuted = !postsListAdapter.isMuted;
        if (postsListAdapter.isMuted) postsListAdapter.currentVol = 0;
        playerView.getPlayer().setVolume(postsListAdapter.isMuted ? 0 : postsListAdapter.currentVol);
        adjustVolume(postsListAdapter.currentVol);
    }

    @OnClick(R.id.react_btn) public void react() {
        launchReactionCamera(postsListAdapter.context, postDetails);
    }

    @Override public String toString() {
        return "ExoPlayer{" + hashCode() + " " + getAdapterPosition() + "}";
    }

    @NonNull @Override public View getPlayerView() {
        return playerView;
    }

    @NonNull @Override public PlaybackInfo getCurrentPlaybackInfo() {
        return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override public void initialize(@NonNull Container container, @Nullable PlaybackInfo playbackInfo) {
        if (helper == null)
            helper = new ExoPlayerViewHelper(container, this,
                    Uri.parse(postDetails.getMedias().get(0).getMediaUrl()));
        helper.initialize(playbackInfo);
    }

    @Override public void play() {
        if (helper != null) helper.play();
        adjustVolume(postsListAdapter.currentVol);
    }

    @Override public void pause() {
        if (helper != null) helper.pause();
    }

    @Override public boolean isPlaying() {
        return helper != null && helper.isPlaying();
    }

    @Override public void release() {
        if (helper != null) {
            helper.release();
            helper = null;
        }
        unregisterAudioObserver();
    }

    @Override public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
    }

    @Override public int getPlayerOrder() {
        return getAdapterPosition();
    }

    @Override public void onSettled(Container container) {
        // Do nothing
    }
    public void bind(int position) {
        try {
            postDetails = postsListAdapter.posts.get(position);

            playerView.setShutterBackground(postDetails.getMedias().get(0).getThumbUrl());
            playerView.setResizeMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);

            shimmerize(new View[]{title, location, category}, new View[]{username},
                    popularityLayoutShimmer, new ShimmerRelativeLayout[]{shimmerLayoutTop, shimmerLayoutMid});

                /*Adjust view size before loading anything*/
            adjustViewSize(postsListAdapter.context, postDetails.getMedias().get(0).getDimension().getWidth(),
                    postDetails.getMedias().get(0).getDimension().getHeight(),
                    playerView.getLayoutParams(), position, null, true);

            @DrawableRes int placeholder = postDetails.getPostOwner().getGender() == MALE ? R.drawable.ic_user_male_dp_small :
                    R.drawable.ic_user_female_dp;

            Glide.with(postsListAdapter.context)
                    .load(postDetails.getPostOwner().getProfileMedia() != null ?
                            postDetails.getPostOwner().getProfileMedia().getThumbUrl() : placeholder)
                    .apply(new RequestOptions()
                            .skipMemoryCache(false)
                            .placeholder(R.drawable.ic_user_male_dp_small))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                       DataSource dataSource, boolean isFirstResource) {
                            setFields();
                            dp.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(dp);

            if (postDetails.getReactions() != null && postDetails.getReactions().size() > 0) {
                reactionListView.setVisibility(VISIBLE);
                reactionListView.setLayoutManager(
                        new LinearLayoutManager(postsListAdapter.context, LinearLayoutManager.HORIZONTAL, false));
                reactionListView.setAdapter(new ReactionAdapter(postsListAdapter.context, postDetails.getReactions()));
            } else {
                reactionListView.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shimmerize(View[] viewsToShimmerizeLight, View[] viewsToShimmerizeDark,
                            View popularityLayoutShimmer, ShimmerRelativeLayout[] shimmers) {
        for (View view : viewsToShimmerizeLight) {
            view.setBackground(postsListAdapter.context.getResources().getDrawable(R.drawable.bg_shimmer_light));
            if (view instanceof TextView)
                ((TextView) view).setText(null);
        }
        for (View view : viewsToShimmerizeDark) {
            view.setBackground(postsListAdapter.context.getResources().getDrawable(R.drawable.bg_shimmer_dark));
            if (view instanceof TextView)
                ((TextView) view).setText(null);
        }
        for (ShimmerRelativeLayout shimmer : shimmers) {
            shimmer.startShimmerAnimation();
        }
        popularityLayoutShimmer.setVisibility(VISIBLE);
    }

    private void setFields() {
        deShimmerize(new View[]{title, location, username}, popularityLayoutShimmer,
                new ShimmerRelativeLayout[]{shimmerLayoutTop, shimmerLayoutMid});

//            SETTING TITLE
        String titleText = postDetails.getTitle();
        title.setText(decodeUnicodeString(titleText));

//            SETTING LOCATION
        if (postDetails.getCheckIn() != null) {
            String locationText = postDetails.getCheckIn().getLocation();
            location.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_place_small, 0, 0, 0);
            location.setText(locationText);
        } else location.setVisibility(GONE);

//            SETTING CATEGORY AND EXTRA CATEGORIES, IF ANY
        if (postDetails.getCategories() != null && !postDetails.getCategories().isEmpty()) {
            category.setVisibility(postDetails.getCategories().isEmpty() ? GONE : VISIBLE);
            if (category.getVisibility() == VISIBLE) {
                category.setText(postDetails.getCategories().get(0).getCategoryName());
                category.setBackground(
                        postsListAdapter.getBackground(category, getAdapterPosition(),
                                Color.parseColor(postDetails.getCategories().get(0).getMyColor())));
                int categoriesSize = postDetails.getCategories().size();
                if (categoriesSize > 1) {
                    categoryExtra.setVisibility(VISIBLE);
                    String extraCategoryCount = "+ " + (categoriesSize - 1) + ((categoriesSize - 1 == 1) ? " Category" : " Categories");
                    categoryExtra.setText(extraCategoryCount);
                } else categoryExtra.setVisibility(GONE);
            }
        } else {
            category.setVisibility(GONE);
            categoryExtra.setVisibility(GONE);
        }

//            SETTING USERNAME
        username.setText(postDetails.getPostOwner().getUserName());

//            SETTING LIKES
        likes.setText(String.valueOf(postDetails.getLikes()));

//            SETTING VIEWS
        views.setText(String.valueOf(postDetails.getMedias().get(0).getViews()));

//            SETTING REACTIONS
        reactions.setText(String.valueOf(postDetails.getTotalReactions()));
    }

    private void deShimmerize(View[] views, View popularityLayoutShimmer, ShimmerRelativeLayout[] shimmers) {
        for (View view : views) {
            view.setBackground(null);
        }
        for (ShimmerRelativeLayout shimmer : shimmers) {
            shimmer.stopShimmerAnimation();
        }
        popularityLayoutShimmer.setVisibility(GONE);
    }

    private void registerAudioObserver() {
        try {
            if (audioVolumeObserver != null)
                audioVolumeObserver.register(AudioManager.STREAM_MUSIC, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterAudioObserver() {
        try {
            if (audioVolumeObserver != null)
                audioVolumeObserver.unregister();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adjustVolume(float currentVolume) {
        postsListAdapter.isMuted = currentVolume == 0;
        postsListAdapter.currentVol = currentVolume;
//        playerView.getPlayer().setVolume(postsListAdapter.isMuted ? 0 : postsListAdapter.currentVol);
        volumeControl.setImageResource(postsListAdapter.isMuted ? R.drawable.ic_volume_off :
                R.drawable.ic_volume_up);
    }

    @Override public void initialVolume(int currentVolume) {
        adjustVolume(currentVolume);
    }

    @Override public void onVolumeChanged(int currentVolume, int maxVolume) {
        adjustVolume(currentVolume);
    }
}
