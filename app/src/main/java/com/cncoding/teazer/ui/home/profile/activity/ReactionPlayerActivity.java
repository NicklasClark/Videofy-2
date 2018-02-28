package com.cncoding.teazer.ui.home.profile.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.giphy.Images;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.data.model.react.MyReactions;
import com.cncoding.teazer.ui.customviews.CheckImageButton;
import com.cncoding.teazer.ui.customviews.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentProfileMyCreations;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_GIF;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_GIPHY;
import static com.cncoding.teazer.utilities.common.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.common.FabricAnalyticsUtil.logVideoShareEvent;
import static com.cncoding.teazer.utilities.common.MediaUtils.acquireAudioLock;
import static com.cncoding.teazer.utilities.common.MediaUtils.releaseAudioLock;
import static com.cncoding.teazer.utilities.common.ViewUtils.POST_REACTION;
import static com.cncoding.teazer.utilities.common.ViewUtils.SELF_REACTION;
import static com.cncoding.teazer.utilities.common.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.enableView;

public class ReactionPlayerActivity extends AppCompatActivity {

    @BindView(R.id.btnClose) ImageView btnClose;
    @BindView(R.id.reaction_post_caption) ProximaNovaSemiBoldTextView reactionPostCaption;
    @BindView(R.id.reaction_post_dp) CircularAppCompatImageView reactionPostDp;
    @BindView(R.id.reaction_post_name) ProximaNovaSemiBoldTextView reactionPostName;
    @BindView(R.id.reaction_post_likes) ProximaNovaRegularTextView reactionPostLikes;
    @BindView(R.id.reaction_post_views) ProximaNovaRegularTextView reactionPostViews;
    @BindView(R.id.like) CheckImageButton likeBtn;
    @BindView(R.id.reaction_post_name_popularity_layout) RelativeLayout reactionPostNamePopularityLayout;
    @BindView(R.id.root_layout) RelativeLayout rootLayout;
    @BindView(R.id.postDuration) ProximaNovaRegularTextView postDurationView;
    @BindView(R.id.video_view) SimpleExoPlayerView playerView;
    @BindView(R.id.gif_view) ImageView gifView;

    SimpleExoPlayer player;
    private PostReaction postDetails;
    private MyReactions selfPostDetails;

    private String reactionTitle;
    private String videoURL;
    private boolean isLiked;
    private boolean playWhenReady = true;
    private long playbackPosition;
    private int likesCount;
    private int viewsCount;
    private int reactId;
    private int currentWindow;
    private int playSource;

    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private boolean audioAccessGranted = false;
    private long reactionPlayerCurrentPosition = 0;
    private Handler mHandler;
    private boolean isGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_exo_player);
        ButterKnife.bind(this);

        mHandler = new Handler();
        audioFocusChangeListener =
                new AudioManager.OnAudioFocusChangeListener() {
                    public void onAudioFocusChange(int focusChange) {
                        if (player != null) {
                            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                                // Permanent loss of audio focus
                                // Pause playback immediately
                                player.setPlayWhenReady(false);
                                // Wait 30 seconds before stopping playback
                                mHandler.postDelayed(mDelayedStopRunnable,
                                        TimeUnit.SECONDS.toMillis(30));
                            } else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
                                // Pause playback
                                player.setPlayWhenReady(false);
                            } else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                                // Lower the volume, keep playing
                            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                                // Your app has been granted audio focus again
                                // Raise volume to normal, restart playback if necessary
                                player.setPlayWhenReady(true);
                                player.seekTo(reactionPlayerCurrentPosition);
                            }
                        }
                    }
                };


        //acquire audio play access(transient)
        audioAccessGranted = acquireAudioLock(this, audioFocusChangeListener);

        playSource = getIntent().getIntExtra("SOURCE", 0);
        switch (playSource) {
            case POST_REACTION: {
                try {
                    videoURL = getIntent().getStringExtra("VIDEO_URL");
                    postDetails = getIntent().getParcelableExtra("POST_INFO");
                    isGif = getIntent().getBooleanExtra("IS_GIF", false);
                    if(isGif) {
                        gifView.setVisibility(View.VISIBLE);

                        if(postDetails.getMediaDetail().getMediaType() == MEDIA_TYPE_GIPHY)
                        {
                            Gson gson = new Gson();
                            Images images = gson.fromJson(postDetails.getMediaDetail().getExternalMeta(), Images.class);

                            Glide.with(this)
                                    .load(images.getDownsizedLarge().getUrl())
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                    .into(gifView);
                        }
                        else if(postDetails.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
                            Glide.with(this)
                                    .load(postDetails.getMediaDetail().getReactMediaUrl())
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                    .into(gifView);
                        }
                    }
                    else {
                        playerView.setVisibility(View.VISIBLE);
                    }

                    if (postDetails != null)
                        reactId = postDetails.getReactId();

//                    if (null != toolbar) {
//                        this.setSupportActionBar(toolbar);
//                        //noinspection ConstantConditions
//                        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
//                        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
//                        //            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
//                    }

                    isLiked = !postDetails.canLike();
                    likesCount = postDetails.getLikes();
                    viewsCount = postDetails.getViews();
                    reactionTitle = decodeUnicodeString(postDetails.getReactTitle());

                    Glide.with(this)
                            .load(postDetails.getReactOwner().getProfileMedia() != null ? postDetails.getReactOwner().getProfileMedia().getMediaUrl()
                                    : R.drawable.ic_user_male_dp_small)
                            .into(reactionPostDp);
                    if (reactionTitle != null) {
                        try {
                            reactionTitle = URLDecoder.decode(reactionTitle, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        reactionPostCaption.setText(reactionTitle);
                    }

                    postDurationView.setText(postDetails.getMediaDetail().getReactDuration());
                    reactionPostName.setText(postDetails.getReactOwner().getFirstName());
                    initView();
                    incrementView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            }
            case SELF_REACTION: {
                try {
                    videoURL = getIntent().getStringExtra("VIDEO_URL");
                    selfPostDetails = getIntent().getParcelableExtra("POST_INFO");
                    isGif = getIntent().getBooleanExtra("IS_GIF", false);
                    if(isGif) {
                        gifView.setVisibility(View.VISIBLE);

                        if (selfPostDetails.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
                            Glide.with(this)
                                    .load(selfPostDetails.getMediaDetail().getReactMediaUrl())
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                    .into(gifView);
                        } else if(selfPostDetails.getMediaDetail().getMediaType() == MEDIA_TYPE_GIPHY){
                            Gson gson = new Gson();
                            Images images = gson.fromJson(selfPostDetails.getMediaDetail().getExternalMeta(), Images.class);

                            Glide.with(this)
                                    .load(images.getDownsizedLarge().getUrl())
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                    .into(gifView);
                        }
                    }
                    else {
                        playerView.setVisibility(View.VISIBLE);
                    }
                    if (selfPostDetails != null)
                        reactId = selfPostDetails.getReactId();

//                    if (null != toolbar) {
//                        this.setSupportActionBar(toolbar);
//                        //noinspection ConstantConditions
//                        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
//                        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
//                        //            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
//                    }

                    isLiked = !selfPostDetails.canLike();
                    likesCount = selfPostDetails.getLikes();
                    viewsCount = selfPostDetails.getViews();
                    reactionTitle = selfPostDetails.getReactTitle();

                    Glide.with(this)
                            .load(selfPostDetails.getPostOwner().getProfileMedia() != null ? selfPostDetails.getPostOwner().getProfileMedia().getMediaUrl()
                                    : R.drawable.ic_user_male_dp_small)
                            .into(reactionPostDp);
                    if (reactionTitle != null) {
                        reactionPostCaption.setText(reactionTitle);
                    }
                    postDurationView.setText(selfPostDetails.getMediaDetail().getReactDuration());
                    reactionPostName.setText("Reacted on " + selfPostDetails.getPostOwner().getFirstName() + " post");

                    initView();
//                    incrementView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void incrementView() {
        if (!postDetails.getMySelf()) {
            ApiCallingService.React.incrementReactionViewCount(reactId, this)
                    .enqueue(new Callback<ResultObject>() {
                        @Override
                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                            if (response.body().getStatus())
                                viewsCount++;
                            initView();
                        }

                        @Override
                        public void onFailure(Call<ResultObject> call, Throwable t) {
                            t.printStackTrace();
                            initView();
                        }
                    });
        }
    }

    private Runnable mDelayedStopRunnable = new Runnable() {
        @Override
        public void run() {
            player.setPlayWhenReady(false);
        }
    };

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

            LoopingMediaSource loopingMediaSource = new LoopingMediaSource(mediaSource);
            playerView.setPlayer(player);
            player.prepare(loopingMediaSource);
            playerView.setResizeMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            if (audioAccessGranted) {
                player.setPlayWhenReady(true);
                player.seekTo(reactionPlayerCurrentPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btnClose, R.id.like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                reactionPlayerCurrentPosition = 0;
                releasePlayer();
                onBackPressed();
                break;
            case R.id.like:
                if (!isLiked) {
//            Like the post
                    isLiked = true;
                    likesCount++;
                    initView();
                    disableView(likeBtn, false);
                    ApiCallingService.React.likeDislikeReaction(reactId, 1, this).enqueue(new Callback<ResultObject>() {
                        @Override
                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                            try {
                                if (response.body().getStatus()) {
                                    FragmentProfileMyCreations.checkIsLiked = true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                isLiked = false;
                                likesCount--;
                                initView();
                            }
                            enableView(likeBtn);
                        }

                        @Override
                        public void onFailure(Call<ResultObject> call, Throwable t) {
                            t.printStackTrace();
                            isLiked = false;
                            likesCount--;
                            initView();
                            enableView(likeBtn);
                        }
                    });
                } else {
//            Unlike the post
                    isLiked = false;
                    likesCount--;
                    likeAction(isLiked, true);
                    initView();
                    disableView(likeBtn, false);
                    ApiCallingService.React.likeDislikeReaction(reactId, 2, this).enqueue(new Callback<ResultObject>() {
                        @Override
                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                            try {
                                if (response.body().getStatus()) {
                                    FragmentProfileMyCreations.checkIsLiked = false;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                isLiked = true;
                                likesCount++;
                                initView();
                            }
                            enableView(likeBtn);
                        }

                        @Override
                        public void onFailure(Call<ResultObject> call, Throwable t) {
                            t.printStackTrace();
                            isLiked = true;
                            likesCount++;
                            initView();
                            enableView(likeBtn);
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
                switch (playSource) {
                    case POST_REACTION:
//                        PostsListFragment.postDetails.likes++;
                        break;
                    case SELF_REACTION:
                        break;
                }
                likeBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.selected));
            }

        } else {
            likeBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_like_outline));
            if (animate) {
                switch (playSource) {
                    case POST_REACTION:
//                        PostsListFragment.postDetails.likes++;
                        break;
                    case SELF_REACTION:
                        break;
                }
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
        try {
            reactionPlayerCurrentPosition = player.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        releaseAudioLock(this, audioFocusChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        releaseAudioLock(this, audioFocusChangeListener);
        mHandler.removeCallbacks(mDelayedStopRunnable);
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

    @OnClick(R.id.btnShare)
    public void onViewClicked() {
        switch (playSource) {
            case POST_REACTION: {
                BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                        .setCanonicalIdentifier(String.valueOf(postDetails.getReactOwner().getUserId()))
                        .setTitle(postDetails.getReactTitle())
                        .setContentDescription("View this awesome video on Teazer app")
                        .setContentImageUrl(postDetails.getMediaDetail().getReactThumbUrl());

                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("facebook")
                        .setFeature("sharing")
                        .addControlParameter("react_id", String.valueOf(postDetails.getReactId()))
                        .addControlParameter("post_id", String.valueOf(postDetails.getPostId()))
                        .addControlParameter("$desktop_url", "https://teazer.in/")
                        .addControlParameter("$ios_url", "https://teazer.in/");

                branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {
                            //fabric event
                            logVideoShareEvent("Branch", postDetails.getReactTitle(), "Reaction", String.valueOf(postDetails.getReactId()));

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                            sendIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                        }
                    }
                });
                break;
            }
            case SELF_REACTION: {
                BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                        .setCanonicalIdentifier(String.valueOf(selfPostDetails.getPostOwner().getUserId()))
                        .setTitle(selfPostDetails.getReactTitle())
                        .setContentDescription("View this awesome video on Teazer app")
                        .setContentImageUrl(selfPostDetails.getMediaDetail().getReactMediaUrl());

                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("facebook")
                        .setFeature("sharing")
                        .addControlParameter("react_id", String.valueOf(selfPostDetails.getReactId()))
                        .addControlParameter("post_id", String.valueOf(selfPostDetails.getPostId()))
                        .addControlParameter("$desktop_url", "https://teazer.in/")
                        .addControlParameter("$ios_url", "https://teazer.in/");

                branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                            sendIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                        }
                    }
                });
                break;
            }
        }

    }
}
