package com.cncoding.teazer.ui.home.post.homepage;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.exoplayer.Container;
import com.cncoding.teazer.ui.customviews.exoplayer.ExoPlayerViewHelper;
import com.cncoding.teazer.ui.customviews.exoplayer.SimpleExoPlayerView;
import com.cncoding.teazer.ui.customviews.exoplayer.SimpleExoPlayerView.OnThumbReadyListener;
import com.cncoding.teazer.ui.customviews.exoplayer.ToroPlayer;
import com.cncoding.teazer.ui.customviews.exoplayer.ToroUtil;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaBoldButton;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.customviews.shimmer.ShimmerLinearLayout;
import com.cncoding.teazer.ui.customviews.shimmer.ShimmerRelativeLayout;
import com.cncoding.teazer.ui.home.post.detailspage.PostDetailsFragment;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewOtherProfile;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewProfile2;
import com.cncoding.teazer.utilities.audio.AudioVolumeContentObserver.OnAudioVolumeChangedListener;
import com.cncoding.teazer.utilities.audio.AudioVolumeObserver;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import im.ene.toro.media.PlaybackInfo;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.utilities.common.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getMedia;
import static com.cncoding.teazer.utilities.common.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.enableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.getGenderSpecificDpSmall;
import static com.cncoding.teazer.utilities.common.ViewUtils.getPixels;
import static com.cncoding.teazer.utilities.common.ViewUtils.launchReactionCamera;
import static com.cncoding.teazer.utilities.diffutil.PostsDetailsDiffCallback.DIFF_POST_DETAILS;
import static com.cncoding.teazer.utilities.diffutil.PostsDetailsDiffCallback.updatePostDetailsAccordingToDiffBundle;

/**
 *
 * Created by Prem$ on 2/2/2018.
 */
class PostListViewHolder extends BaseRecyclerView.ViewHolder implements ToroPlayer, OnAudioVolumeChangedListener, OnThumbReadyListener {

    @LayoutRes static final int LAYOUT_RES = R.layout.item_home_screen_post_new;

    @BindView(R.id.title) ProximaNovaSemiBoldTextView title;
    @BindView(R.id.location) ProximaNovaRegularTextView location;
    @BindView(R.id.category) ProximaNovaSemiBoldTextView category;
    @BindView(R.id.category_extra) ProximaNovaRegularTextView categoryExtra;
    @BindView(R.id.content) SimpleExoPlayerView playerView;
    @BindView(R.id.react_btn) ProximaNovaBoldButton reactBtn;
    @BindView(R.id.volume_control) ImageView volumeControl;
    @BindView(R.id.dp) CircularAppCompatImageView dp;
    @BindView(R.id.username) ProximaNovaSemiBoldTextView username;
    @BindView(R.id.likes) ProximaNovaRegularTextView likes;
    @BindView(R.id.views) ProximaNovaRegularTextView views;
    @BindView(R.id.reactions) ProximaNovaRegularTextView reactions;
    @BindView(R.id.popularity_layout_shimmer) ProximaNovaRegularTextView popularityLayoutShimmer;
    @BindView(R.id.list) RecyclerView reactionListView;
    @BindView(R.id.shimmer_layout_top) ShimmerLinearLayout shimmerLayoutTop;
    @BindView(R.id.shimmer_layout_mid) ShimmerRelativeLayout shimmerLayoutMid;

    private AudioVolumeObserver audioVolumeObserver;
    private PostsListAdapter adapter;
    private ExoPlayerViewHelper helper;
    private PostDetails postDetails;
    private BitmapDrawable thumbnailDrawable;

    PostListViewHolder(PostsListAdapter adapter, View view) {
        super(view);
        this.adapter = adapter;
        ButterKnife.bind(this, view);
        playerView.setOnThumbReadyListener(this);
        if (audioVolumeObserver == null) {
            audioVolumeObserver = new AudioVolumeObserver(adapter.fragment.getParentActivity());
        }
        registerAudioObserver();
    }

    @OnClick(R.id.content) void viewPost() {
        try {
            adapter.fragment.navigation.pushFragment(
                    PostDetailsFragment.newInstance(postDetails, thumbnailDrawable.getBitmap(), true, null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.dp) void viewProfileThroughDp() {
        viewProfile();
    }

    @OnClick(R.id.username) void viewProfileThroughUsername() {
        viewProfile();
    }

    private void viewProfile() {
        adapter.fragment.navigation.pushFragment(postDetails.canDelete() ?
                FragmentNewProfile2.newInstance() :
                FragmentNewOtherProfile.newInstance(
                        String.valueOf(postDetails.getPostOwner().getUserId()), "", postDetails.getPostOwner().getUserName()));
    }

    @OnClick(R.id.volume_control) void controlVolume() {
        audioVolumeObserver.toggleMute();
        adjustVolumeButtons(audioVolumeObserver.getCurrentVolume());
    }

    @OnClick(R.id.react_btn) public void react() {
        launchReactionCamera(adapter.fragment.getParentActivity(), postDetails);
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
        String remoteMediaUrl = postDetails.getMedias().get(0).getMediaUrl();
        String savedMediaPath = getMedia(adapter.fragment.getContext(), remoteMediaUrl);
        if (helper == null) {
            helper = new ExoPlayerViewHelper(container, this,
                    Uri.parse(savedMediaPath != null && new File(savedMediaPath).exists() ? savedMediaPath : remoteMediaUrl));
            helper.initialize(playbackInfo);
        }
    }

    @Override public void play() {
        if (helper != null) helper.play();
        adjustVolumeButtons(audioVolumeObserver.getCurrentVolume());
    }

    @Override public void pause() {
        if (helper != null) helper.pause();
        volumeControl.setVisibility(INVISIBLE);
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

    @Override
    public void onSettled(Container container) {
        // Do nothing
    }

    @Override
    public void bind() {
        try {
            postDetails = adapter.posts.get(getAdapterPosition());

            if (!postDetails.canReact()) disableView(reactBtn, true);
            else enableView(reactBtn);

            playerView.setShutterBackground(postDetails.getMedias().get(0).getThumbUrl());

            shimmerize(new View[]{title, location, category}, new View[]{username});

            @DrawableRes int placeholder = getGenderSpecificDpSmall(postDetails.getPostOwner().getGender());

            Glide.with(adapter.fragment)
                    .load(postDetails.getPostOwner().getProfileMedia() != null ?
                            postDetails.getPostOwner().getProfileMedia().getThumbUrl() : placeholder)
                    .apply(new RequestOptions().skipMemoryCache(false).placeholder(placeholder))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
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
                        new LinearLayoutManager(adapter.fragment.getContext(), LinearLayoutManager.HORIZONTAL, false));
                reactionListView.setAdapter(new ReactionAdapter(adapter.fragment, postDetails.getReactions()));
            } else {
                reactionListView.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bind(List<Object> payloads) {
        if (payloads.isEmpty()) return;

        if (payloads.get(0) instanceof PostDetails) {
            bind();
            return;
        }

        Bundle bundle = (Bundle) payloads.get(0);
        if (bundle.containsKey(DIFF_POST_DETAILS)) {
            postDetails = bundle.getParcelable(DIFF_POST_DETAILS);
            return;
        }

        updatePostDetailsAccordingToDiffBundle(postDetails, bundle);
    }

    private void setFields() {
        deShimmerize(new View[]{title, location, username});

//            SETTING TITLE
        String titleText = postDetails.getTitle();
        title.setText(decodeUnicodeString(titleText));

//            SETTING LOCATION
        if (postDetails.getCheckIn() != null) {
            location.setVisibility(VISIBLE);
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
                        getBackground(ColorStateList.valueOf(Color.parseColor(postDetails.getCategories().get(0).getMyColor()))));
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
        setLikes();

//            SETTING VIEWS
        setViews();

//            SETTING REACTIONS
        reactions.setText(String.valueOf(postDetails.getTotalReactions()));
    }

    private void setLikes() {
        likes.setText(String.valueOf(postDetails.getLikes()));
    }

    private void setViews() {
        views.setText(String.valueOf(postDetails.getMedias().get(0).getViews()));
    }

    private void shimmerize(View[] viewsToShimmerizeLight, View[] viewsToShimmerizeDark) {
        for (View view : viewsToShimmerizeLight) {
            view.setBackground(adapter.fragment.getResources().getDrawable(R.drawable.bg_shimmer_light));
            if (view instanceof TextView)
                ((TextView) view).setText(null);
        }
        for (View view : viewsToShimmerizeDark) {
            view.setBackground(adapter.fragment.getResources().getDrawable(R.drawable.bg_shimmer_dark));
            if (view instanceof TextView)
                ((TextView) view).setText(null);
        }
        shimmerLayoutTop.startShimmerAnimation();
        shimmerLayoutMid.startShimmerAnimation();
        popularityLayoutShimmer.setVisibility(VISIBLE);
    }

    private void deShimmerize(View[] views) {
        for (View view : views) {
            view.setBackground(null);
        }
        shimmerLayoutTop.stopShimmerAnimation();
        shimmerLayoutMid.stopShimmerAnimation();
        popularityLayoutShimmer.setVisibility(GONE);
    }

//    private void likeDislikePost() {
//        Callback<ResultObject> callback = new Callback<ResultObject>() {
//            @Override
//            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
//                if (response.code() != 200) {
//                    if (response.body() != null)
//                        Log.e("LikeDislikePost", response.code() + " : " + response.body().getMessage());
//                    else
//                        Log.e("LikeDislikePost", response.code() + " : " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResultObject> call, Throwable t) {
//                t.printStackTrace();
//            }
//        };
//
//        if (postDetails.canLike()) {
////            Like the post
//            ApiCallingService.Posts.likeDislikePost(postDetails.getPostId(), 1, adapter.context).enqueue(callback);
//            postDetails.canLike = false;
//            postDetails.likes++;
//        } else {
////            Unlike the post
//            ApiCallingService.Posts.likeDislikePost(postDetails.getPostId(), 2, adapter.context).enqueue(callback);
//            postDetails.canLike = true;
//            postDetails.likes--;
//        }
//
//        setLikes();
//        likes.startAnimation(AnimationUtils.loadAnimation(adapter.context, R.anim.selected));
//    }

//    private void incrementView() {
//        if (!postDetails.canDelete()) {
//            ApiCallingService.Posts.incrementViewCount(postDetails.getMedias().get(0).getMediaId(), adapter.context)
//                    .enqueue(new Callback<ResultObject>() {
//                        @Override
//                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
//                            try {
//                                if (response.code() == 200 && response.body().getStatus()) {
//                                    if (PostsListFragment.postDetails != null)
//                                        PostsListFragment.postDetails.getMedias().get(0).views++;
//                                    postDetails.getMedias().get(0).views++;
//                                    setViews();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResultObject> call, Throwable t) {
//                            t.printStackTrace();
//                        }
//                    });
//        }
//    }

    private GradientDrawable getBackground(ColorStateList color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(getPixels(adapter.fragment.getContext(), 2));
        return gradientDrawable;
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

    private void adjustVolumeButtons(float currentVolume) {
        volumeControl.setVisibility(isPlaying() ? VISIBLE : INVISIBLE);
        volumeControl.setImageResource(currentVolume == 0 ? R.drawable.ic_volume_off :
                R.drawable.ic_volume_up);
    }

    @Override public void initialVolume(int currentVolume) {
        adjustVolumeButtons(currentVolume);
    }

    @Override public void onVolumeChanged(int currentVolume) {
        adjustVolumeButtons(currentVolume);
    }

    @Override
    public void onThumbReady(BitmapDrawable thumbnail) {
        thumbnailDrawable = thumbnail;
    }

//    private void fetchPostDetails(int postId, final Bitmap thumbnail) {
//        ApiCallingService.Posts.getPostDetails(postId, adapter.context)
//                .enqueue(new Callback<PostDetails>() {
//                    @Override
//                    public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
//                        if (response.code() == 200) {
//                            if (response.body() != null) {
//                                PostsListFragment.positionToUpdate = getAdapterPosition();
//                                PostsListFragment.postDetails = response.body();
////                                listener.onPostInteraction(ACTION_VIEW_POST, postDetails, holder.postThumbnail, holder.layout);
//
//                                adapter.listener.postDetails(response.body(), thumbnail, true,
//                                        false, response.body().getMedias().get(0).getThumbUrl(), null);
//                            } else {
//                                Toast.makeText(adapter.context,
//                                        "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
//                            }
//                        } else
//                            Toast.makeText(adapter.context,
//                                    "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<PostDetails> call, Throwable t) {
//                        t.printStackTrace();
//                        Toast.makeText(adapter.context,
//                                "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}