package com.cncoding.teazer.ui.fragment.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.post.PostsListFragment;
import com.cncoding.teazer.utilities.Pojos;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReactionPlayerActivity extends AppCompatActivity {

    @BindView(R.id.video_view)
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnClose)
    ImageView btnClose;
    @BindView(R.id.reaction_post_caption)
    ProximaNovaSemiboldTextView reactionPostCaption;
    @BindView(R.id.reaction_post_dp)
    CircularAppCompatImageView reactionPostDp;
    @BindView(R.id.reaction_post_name)
    ProximaNovaSemiboldTextView reactionPostName;
    @BindView(R.id.reaction_post_likes)
    ProximaNovaRegularTextView reactionPostLikes;
    @BindView(R.id.reaction_post_views)
    ProximaNovaRegularTextView reactionPostViews;
    @BindView(R.id.btnLike)
    ImageView likeBtn;
    @BindView(R.id.reaction_post_name_popularity_layout)
    RelativeLayout reactionPostNamePopularityLayout;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    @BindView(R.id.postDuration)
    ProximaNovaRegularTextView postDurationView;
    private String videoURL;
    private Pojos.Post.PostReaction postDetails;
    private boolean isLiked;
    private int likesCount;
    private int viewsCount;
    private int reactId;
    private String reactionTitle;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        ButterKnife.bind(this);

        try {
            videoURL = getIntent().getStringExtra("VIDEO_URL");
            postDetails = getIntent().getParcelableExtra("POST_INFO");
            if(postDetails != null)
                reactId = postDetails.getReactId();

            if (null != toolbar) {
                this.setSupportActionBar(toolbar);
                //noinspection ConstantConditions
                this.getSupportActionBar().setDisplayShowTitleEnabled(false);
                this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
    //            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
            }

            isLiked = !postDetails.canLike();
            likesCount = postDetails.getLikes();
            viewsCount = postDetails.getViews();
            reactionTitle = postDetails.getReact_title();

        Glide.with(this)
                .load(postDetails.getReactOwner().getProfileMedia() != null ? postDetails.getReactOwner().getProfileMedia().getMediaUrl()
                        : R.drawable.ic_user_male_dp_small)
                .asBitmap()
                .into(reactionPostDp);
        if (reactionTitle != null) {
            reactionPostCaption.setText(reactionTitle);
        }
        postDurationView.setText(postDetails.getMediaDetail().getReactDuration());
        reactionPostName.setText(postDetails.getReactOwner().getFirstName());

            initView();
            incrementView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void incrementView() {
        ApiCallingService.React.incrementReactionViewCount(reactId, this)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        if (response.code() == 200 && response.body().getStatus())
                            viewsCount++;
                        initView();
                    }
                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    private void initView() {
        try {
            likeAction(isLiked, true);
            reactionPostLikes.setText(String.valueOf(likesCount));
            reactionPostViews.setText(String.valueOf(viewsCount));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initializePlayer() {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            Uri videoURI = Uri.parse(videoURL);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

            playerView.setPlayer(player);
            player.prepare(mediaSource);
            player.setPlayWhenReady(playWhenReady);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btnClose, R.id.btnLike})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                finish();
                break;
            case R.id.btnLike:
                if (!isLiked) {
//            Like the post
                    ApiCallingService.React.likeDislikeReaction(reactId, 1, this).enqueue(new Callback<ResultObject>() {
                        @Override
                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                            try {
                                if(response.body().getStatus())
                                {
                                    isLiked = true;
                                    likesCount++;
                                    likeAction(isLiked, true);
                                    initView();
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
                } else {
//            Unlike the post
                    ApiCallingService.React.likeDislikeReaction(reactId, 2, this).enqueue(new Callback<ResultObject>() {
                                @Override
                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                    try {
                                        if(response.body().getStatus())
                                        {
                                            isLiked = false;
                                            likesCount--;
                                            likeAction(isLiked, true);
                                            initView();
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
                break;
        }
    }
    private void likeAction(boolean isLiked, boolean animate) {
        if (isLiked) {
            likeBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_like_filled));
            if (animate) {
                PostsListFragment.postDetails.likes++;
                likeBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.selected));
//                controller.incrementLikes();
            }

        } else {
            likeBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_like_outline));
            if (animate) {
                PostsListFragment.postDetails.likes++;
                likeBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.selected));
//                controller.incrementLikes();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

}
