package com.cncoding.teazer.ui.home.profile.fragment;

import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.giphy.Images;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.customviews.common.CheckImageButton;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.profile.ProfileFragment;
import com.cncoding.teazer.ui.home.profile.activity.OthersProfileFragment;
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
import static android.view.View.GONE;
import static com.cncoding.teazer.utilities.common.Annotations.SEND_DISLIKE;
import static com.cncoding.teazer.utilities.common.Annotations.SEND_LIKE;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_GIF;
import static com.cncoding.teazer.utilities.common.CommonUtilities.MEDIA_TYPE_GIPHY;
import static com.cncoding.teazer.utilities.common.CommonWebServicesUtil.fetchPostDetails;
import static com.cncoding.teazer.utilities.common.FabricAnalyticsUtil.logVideoShareEvent;
import static com.cncoding.teazer.utilities.common.MediaUtils.acquireAudioLock;
import static com.cncoding.teazer.utilities.common.MediaUtils.releaseAudioLock;
import static com.cncoding.teazer.utilities.common.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.enableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.getGenderSpecificDpSmall;

/**
 *
 * Created by farazhabib on 23/01/18.
 */

public class FragmentReactionPlayer extends BaseFragment implements OnAudioFocusChangeListener {

    public static final int OPENED_FROM_OTHER_SOURCE = 0;
    public static final int OPENED_FROM_PROFILE = 1;
    public static final String POST_REACTIONS = "post_reactions";
    public static final String IS_GIF = "is_gif";
    private static long reactionPlayerCurrentPosition = 0;

    @BindView(R.id.video_view) SimpleExoPlayerView playerView;
    @BindView(R.id.gif_view) ImageView gifView;
    @BindView(R.id.btnClose) ImageView btnClose;
    @BindView(R.id.reaction_post_caption) ProximaNovaSemiBoldTextView reactionPostCaption;
    @BindView(R.id.reaction_post_dp) CircularAppCompatImageView reactionPostDp;
    @BindView(R.id.reaction_post_name) ProximaNovaSemiBoldTextView reactionPostName;
    @BindView(R.id.reaction_post_likes) ProximaNovaRegularTextView likesView;
    @BindView(R.id.reaction_post_views) ProximaNovaRegularTextView reactionPostViews;
    @BindView(R.id.like) CheckImageButton likeBtn;
    @BindView(R.id.reaction_post_name_popularity_layout) RelativeLayout reactionPostNamePopularityLayout;
    @BindView(R.id.postDuration) ProximaNovaRegularTextView postDurationView;
    @BindView(R.id.postImage) ImageView postImage;
    @BindView(R.id.postTitle) ProximaNovaRegularTextView postTitle;

    private boolean isGif;
    private boolean audioAccessGranted = false;
    private PostReaction postReaction;
    private SimpleExoPlayer player;
    private Handler mHandler;

    private Runnable mDelayedStopRunnable = new Runnable() {
        @Override
        public void run() {
            player.setPlayWhenReady(false);
        }
    };

    public static FragmentReactionPlayer newInstance(PostReaction postReaction, boolean isGIF) {
        FragmentReactionPlayer fragment = new FragmentReactionPlayer();
        Bundle args = new Bundle();
        args.putParcelable(POST_REACTIONS, postReaction);
        args.putBoolean(IS_GIF, isGIF);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            postReaction = bundle.getParcelable(POST_REACTIONS);
            isGif = bundle.getBoolean(IS_GIF);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reaction_exo_player, container, false);
        ButterKnife.bind(this,view);
        mHandler = new Handler();
        //acquire audio play access(transient)
        audioAccessGranted = acquireAudioLock(getActivity(), this);

        likeAction(postReaction.canLike(), false);
        initViews();
        return view;
    }

    private void initViews() {
        try {
            if (postReaction != null) {
                if (isGif) {
                    gifView.setVisibility(View.VISIBLE);
                    switch (postReaction.getMediaDetail().getMediaType()) {
                        case MEDIA_TYPE_GIPHY:
                            Gson gson = new Gson();
                            Images images = gson.fromJson(postReaction.getMediaDetail().getExternalMeta(), Images.class);
                            Glide.with(this)
                                    .load(images.getDownsizedLarge().getUrl())
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                    .into(gifView);
                            break;
                        case MEDIA_TYPE_GIF:
                            Glide.with(this)
                                    .load(postReaction.getMediaDetail().getReactMediaUrl())
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                    .into(gifView);
                            break;
                    }
                } else playerView.setVisibility(View.VISIBLE);

                if (postReaction != null) {
                    MiniProfile owner = postReaction.getReactOwner() != null ? postReaction.getReactOwner() : postReaction.getPostOwner();

                    Glide.with(this)
                            .load(postReaction.getReactOwner().getProfileMedia() != null ? owner.getProfileMedia().getThumbUrl()
                                    : getGenderSpecificDpSmall(owner.getGender()))
                            .into(reactionPostDp);
                    if (postReaction.getReactTitle() != null) {
                        try {
                            postReaction.setReactTitle(URLDecoder.decode(postReaction.getReactTitle(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        reactionPostCaption.setText(postReaction.getReactTitle());
                    }
                    likesView.setText(String.valueOf(postReaction.getLikes()));
                    reactionPostViews.setText(String.valueOf(postReaction.getViews()));
                    postDurationView.setText(postReaction.getMediaDetail().getReactDuration());
                    reactionPostName.setText(postReaction.getReactOwner().getFirstName());

                    ApiCallingService.Posts.getPostDetails(postReaction.getPostId(), context).enqueue(new Callback<PostDetails>() {
                        @Override
                        public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                            if (response.code() == 200) {
                                PostDetails postDetails = response.body();
                                Glide.with(context).load(postDetails.getMedias().get(0).getThumbUrl()).into(postImage);
                                String reactedOn = "Reacted on " + postDetails.getTitle();
                                postTitle.setText(reactedOn);
                            }
                            else {
                                postTitle.setVisibility(GONE);
                                postImage.setVisibility(GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostDetails> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
            } else {
                Toast.makeText(context, R.string.reaction_no_longer_exists, Toast.LENGTH_SHORT).show();
                navigation.popFragment();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializePlayer() {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            Uri videoURI = Uri.parse(postReaction.getMediaDetail().getReactMediaUrl());

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
            incrementView();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.reaction_post_name, R.id.postTitle, R.id.postImage}) public void viewPost() {
        fetchPostDetails(context, postReaction.getPostId());
    }

    @OnClick(R.id.reaction_post_dp) public void viewProfile() {
        navigation.pushFragment(postReaction.getMySelf() ? ProfileFragment.newInstance() :
                OthersProfileFragment.newInstance(
                        String.valueOf(postReaction.getReactOwner().getUserId()), "", ""));
    }

    @OnClick(R.id.btnClose) public void exit() {
        reactionPlayerCurrentPosition = 0;
        navigation.popFragment();
    }

    @OnClick(R.id.btnShare) public void onShareClicked() {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(String.valueOf(postReaction.getReactOwner().getUserId()))
                .setTitle(postReaction.getReactTitle())
                .setContentDescription("View this awesome video on Teazer app")
                .setContentImageUrl(postReaction.getMediaDetail().getReactThumbUrl());

        LinkProperties linkProperties = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing")
                .addControlParameter("react_id", String.valueOf(postReaction.getReactId()))
                .addControlParameter("post_id", String.valueOf(postReaction.getPostId()))
                .addControlParameter("$desktop_url", "https://teazer.in/")
                .addControlParameter("$ios_url", "https://teazer.in/");

        branchUniversalObject.generateShortUrl(context, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    //fabric event
                    logVideoShareEvent("Branch", postReaction.getReactTitle(),
                            "Reaction", String.valueOf(postReaction.getReactId()));
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                }
            }
        });
    }

    @OnClick(R.id.like) public void onLikeClicked() {
        disableView(likeBtn, false);
        ApiCallingService.React.likeDislikeReaction(postReaction.getReactId(), !likeBtn.isChecked() ? SEND_LIKE : SEND_DISLIKE, getActivity())
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        enableView(likeBtn);
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                        enableView(likeBtn);
                    }
                });
        likeAction(likeBtn.isChecked(), true);
    }

    private void likeAction(boolean isChecked, boolean animate) {
        likeBtn.setChecked(!isChecked);
        if (!isChecked) {
            likeBtn.setImageResource(R.drawable.ic_like_filled);
            if (animate) {
                likeBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.selected));
                incrementLikes();
            }
        } else {
            likeBtn.setImageResource(R.drawable.ic_like_outline);
            if (animate) {
                likeBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.selected));
                decrementLikes();
            }
        }
    }

    public void incrementLikes() {
        postReaction.setLikes(postReaction.getLikes() + 1);
        String likesText = String.valueOf(postReaction.getLikes());
        likesView.setText(likesText);
    }

    public void decrementLikes() {
        postReaction.setLikes(postReaction.getLikes() - 1);
        String likesText = String.valueOf(postReaction.getLikes());
        likesView.setText(likesText);
    }

    public void incrementViews() {
        postReaction.setViews(postReaction.getViews() + 1);
        String viewsText = String.valueOf(postReaction.getViews());
        reactionPostViews.setText(viewsText);
    }

    private void incrementView() {
        if (postReaction.getMySelf() && !postReaction.canDelete()) {
            ApiCallingService.React.incrementReactionViewCount(postReaction.getReactId(), getActivity())
                    .enqueue(new Callback<ResultObject>() {
                        @Override
                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                            if (response.body().getStatus()) incrementViews();
                        }

                        @Override
                        public void onFailure(Call<ResultObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }
    }

    @Override public void onAudioFocusChange(int focusChange) {
        if (player != null) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Permanent loss of audio focus
                // Pause playback immediately
                player.setPlayWhenReady(false);
                // Wait 30 seconds before stopping playback
                mHandler.postDelayed(mDelayedStopRunnable,
                        TimeUnit.SECONDS.toMillis(30));
            }
            else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
                player.setPlayWhenReady(false);
            }
//            else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
//                // Lower the volume, keep playing
//            }
            else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Your app has been granted audio focus again
                // Raise volume to normal, restart playback if necessary
                player.setPlayWhenReady(true);
                player.seekTo(reactionPlayerCurrentPosition);
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
        getParentActivity().hideToolbar();
        //hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

//    @SuppressLint("InlinedApi")
//    private void hideSystemUi() {
//        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//    }

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
        releaseAudioLock(getActivity(), this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getParentActivity().showToolbar();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        releaseAudioLock(getActivity(), this);
        mHandler.removeCallbacks(mDelayedStopRunnable);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getParentActivity().showToolbar();
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}