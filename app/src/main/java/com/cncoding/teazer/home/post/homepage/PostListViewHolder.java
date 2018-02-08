package com.cncoding.teazer.home.post.homepage;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.exoplayer.ExoPlayerViewHelper;
import com.cncoding.teazer.customViews.exoplayer.SimpleExoPlayerView;
import com.cncoding.teazer.customViews.exoplayer.SimpleExoPlayerView.OnThumbReadyListener;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaBoldButton;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.customViews.shimmer.ShimmerLinearLayout;
import com.cncoding.teazer.customViews.shimmer.ShimmerRelativeLayout;
import com.cncoding.teazer.home.BaseRecyclerViewHolder;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.BaseBottomBarActivity.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.customViews.exoplayer.AspectRatioFrameLayout.RESIZE_MODE_ZOOM;
import static com.cncoding.teazer.model.base.MiniProfile.MALE;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.ViewUtils.enableView;
import static com.cncoding.teazer.utilities.ViewUtils.launchReactionCamera;

/**
 *
 * Created by Prem$ on 2/2/2018.
 */
class PostListViewHolder extends BaseRecyclerViewHolder implements ToroPlayer, OnAudioVolumeChangedListener, OnThumbReadyListener {

    @LayoutRes static final int LAYOUT_RES = R.layout.item_home_screen_post_new;
    private static final long DOUBLE_CLICK_DURATION = 500;

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

    private Handler handler;
    private Runnable runnable;
    private AudioVolumeObserver audioVolumeObserver;
    private PostsListAdapter postsListAdapter;
    private ExoPlayerViewHelper helper;
    private PostDetails postDetails;
    private BitmapDrawable thumbnailDrawable;
    private boolean doubleClicked;
    private boolean viewed;

    PostListViewHolder(PostsListAdapter postsListAdapter, View view) {
        super(view);
        this.postsListAdapter = postsListAdapter;
        ButterKnife.bind(this, view);
        playerView.setOnThumbReadyListener(this);
        if (audioVolumeObserver == null) {
            audioVolumeObserver = new AudioVolumeObserver(postsListAdapter.context);
        }
        registerAudioObserver();
        viewed = false;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                doubleClicked = false;
                fetchPostDetails(postDetails.getPostId(), thumbnailDrawable != null ? thumbnailDrawable.getBitmap() : null);
            }
        };
    }

    @OnClick(R.id.content) void viewPost() {
        if (doubleClicked) {
            doubleClicked = false;
            handler.removeCallbacks(runnable);
            likeDislikePost();
            return;
        }

        this.doubleClicked = true;
//        Toast.makeText(this, "Hit back again to exit", Toast.LENGTH_SHORT).show();

        handler.postDelayed(runnable, DOUBLE_CLICK_DURATION);
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
        audioVolumeObserver.toggleMute();
        adjustVolumeButtons(audioVolumeObserver.getCurrentVolume());
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
        adjustVolumeButtons(audioVolumeObserver.getCurrentVolume());
        if (!viewed) {
            viewed = true;
            incrementView();
        }
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

    @Override
    public void bind(int position) {
        try {
            postDetails = postsListAdapter.posts.get(position);

            if (!postDetails.canReact()) disableView(reactBtn, true);
            else enableView(reactBtn);

            playerView.setShutterBackground(postDetails.getMedias().get(0).getThumbUrl());
            playerView.setResizeMode(RESIZE_MODE_ZOOM);

            shimmerize(new View[]{title, location, category}, new View[]{username});

//                /*Adjust view size before loading anything*/
//            adjustViewSize(postsListAdapter.context, postDetails.getMedias().get(0).getDimension().getWidth(),
//                    postDetails.getMedias().get(0).getDimension().getHeight(),
//                    playerView.getLayoutParams(), position, null, true);

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
                        postsListAdapter.getBackground(ColorStateList.valueOf(Color.parseColor(postDetails.getCategories().get(0).getMyColor()))));
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
            view.setBackground(postsListAdapter.context.getResources().getDrawable(R.drawable.bg_shimmer_light));
            if (view instanceof TextView)
                ((TextView) view).setText(null);
        }
        for (View view : viewsToShimmerizeDark) {
            view.setBackground(postsListAdapter.context.getResources().getDrawable(R.drawable.bg_shimmer_dark));
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

    private void likeDislikePost() {
        Callback<ResultObject> callback = new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() != 200) {
                    if (response.body() != null)
                        Log.e("LikeDislikePost", response.code() + " : " + response.body().getMessage());
                    else
                        Log.e("LikeDislikePost", response.code() + " : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
            }
        };

        if (postDetails.canLike()) {
//            Like the post
            ApiCallingService.Posts.likeDislikePost(postDetails.getPostId(), 1, postsListAdapter.context).enqueue(callback);
            postDetails.canLike = false;
            postDetails.likes++;
        } else {
//            Unlike the post
            ApiCallingService.Posts.likeDislikePost(postDetails.getPostId(), 2, postsListAdapter.context).enqueue(callback);
            postDetails.canLike = true;
            postDetails.likes--;
        }

        setLikes();
        likes.startAnimation(AnimationUtils.loadAnimation(postsListAdapter.context, R.anim.selected));
    }

    private void incrementView() {
        ApiCallingService.Posts.incrementViewCount(postDetails.getMedias().get(0).getMediaId(), postsListAdapter.context)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        try {
                            if (response.code() == 200 && response.body().getStatus()) {
                                if (PostsListFragment.postDetails != null)
                                    PostsListFragment.postDetails.getMedias().get(0).views++;
                                postDetails.getMedias().get(0).views++;
                                setViews();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
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
        volumeControl.setVisibility(VISIBLE);
        volumeControl.setImageResource(currentVolume == 0 ? R.drawable.ic_volume_off :
                R.drawable.ic_volume_up);
    }

    @Override public void initialVolume(int currentVolume) {
//        adjustVolumeButtons(currentVolume);
    }

    @Override public void onVolumeChanged(int currentVolume) {
        adjustVolumeButtons(currentVolume);
    }

    @Override
    public void onThumbReady(BitmapDrawable thumbnail) {
        thumbnailDrawable = thumbnail;
    }

    private void fetchPostDetails(int postId, final Bitmap thumbnail) {
        ApiCallingService.Posts.getPostDetails(postId, postsListAdapter.context)
                .enqueue(new Callback<PostDetails>() {
                    @Override
                    public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                PostsListFragment.positionToUpdate = getAdapterPosition();
                                PostsListFragment.postDetails = response.body();
//                                listener.onPostInteraction(ACTION_VIEW_POST, postDetails, holder.postThumbnail, holder.layout);

                                postsListAdapter.listener.postDetails(response.body(), thumbnail, true,
                                        false, response.body().getMedias().get(0).getThumbUrl(), null);
                            } else {
                                Toast.makeText(postsListAdapter.context,
                                        "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(postsListAdapter.context,
                                    "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<PostDetails> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(postsListAdapter.context,
                                "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}