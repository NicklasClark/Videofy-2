package com.cncoding.teazer.ui.fragment.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
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
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.model.giphy.Images;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.react.MyReactions;
import com.cncoding.teazer.ui.fragment.activity.OthersProfileFragment;
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
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_GIF;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_GIPHY;
import static com.cncoding.teazer.utilities.FabricAnalyticsUtil.logVideoShareEvent;
import static com.cncoding.teazer.utilities.MediaUtils.acquireAudioLock;
import static com.cncoding.teazer.utilities.MediaUtils.releaseAudioLock;
import static com.cncoding.teazer.utilities.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.ViewUtils.enableView;

/**
 *
 * Created by farazhabib on 23/01/18.
 */

public class FragmentReactionPlayer extends BaseFragment {

    public static final int OPENED_FROM_OTHER_SOURCE = 0;
    public static final int OPENED_FROM_PROFILE = 1;

    @BindView(R.id.video_view) SimpleExoPlayerView playerView;
    @BindView(R.id.gif_view) ImageView gifView;
    @BindView(R.id.btnClose) ImageView btnClose;
    @BindView(R.id.reaction_post_caption) ProximaNovaSemiBoldTextView reactionPostCaption;
    @BindView(R.id.reaction_post_dp) CircularAppCompatImageView reactionPostDp;
    @BindView(R.id.reaction_post_name) ProximaNovaSemiBoldTextView reactionPostName;
    @BindView(R.id.reaction_post_likes) ProximaNovaRegularTextView reactionPostLikes;
    @BindView(R.id.reaction_post_views) ProximaNovaRegularTextView reactionPostViews;
    @BindView(R.id.btnLike) ImageView likeBtn;
    @BindView(R.id.reaction_post_name_popularity_layout) RelativeLayout reactionPostNamePopularityLayout;
    @BindView(R.id.postDuration) ProximaNovaRegularTextView postDurationView;
    @BindView(R.id.postImage) ImageView postImage;
    @BindView(R.id.postTitle) ProximaNovaRegularTextView postTitle;
    private String videoURL;
    private PostReaction othersPostDetails;
    private MyReactions selfPostDetails;
    private boolean isLiked;
    private int likesCount;
    private int viewsCount;
    private int reactId;
    private String reactionTitle;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private int playSource;
    MyReactions reactions;
    SimpleExoPlayer player;
    int getPlaySource;
    Context context;
    public static final int POST_REACTION = 0;
    public static final int SELF_REACTION = 1;
    private boolean isGif;

    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private boolean audioAccessGranted = false;
    private static long reactionPlayerCurrentPosition = 0;
    private Handler mHandler;
    public static final String SELF_REACTIONS = "self_reactions";
    public static final String POST_REACTIONS = "post_reactions";
    public static final String IS_GIF = "is_gif";
    ProfileMyCreationAdapter.myCreationListener myCreationListener;

    public static FragmentReactionPlayer newInstance(int source, PostReaction postReaction, MyReactions selfPostreaction, boolean isGIF) {
        FragmentReactionPlayer fragment = new FragmentReactionPlayer();
        Bundle args = new Bundle();
        args.putParcelable(SELF_REACTIONS, selfPostreaction);
        args.putParcelable(POST_REACTIONS, postReaction);
        args.putBoolean(IS_GIF, isGIF);
        args.putInt("SOURCE", source);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            selfPostDetails = bundle.getParcelable(SELF_REACTIONS);
            othersPostDetails = bundle.getParcelable(POST_REACTIONS);
            playSource = bundle.getInt("SOURCE");
            isGif = bundle.getBoolean(IS_GIF);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_reaction_exo_player, container, false);
        ButterKnife.bind(this,view);
        context = getContext();

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
                            }
                            else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
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
        audioAccessGranted = acquireAudioLock(getActivity(), audioFocusChangeListener);

       // playSource = getIntent().getIntExtra("SOURCE", 0);

        switch (playSource) {
            case OPENED_FROM_OTHER_SOURCE: {
                try {

                    if (othersPostDetails != null) {
                        videoURL = othersPostDetails.getMediaDetail().getReactMediaUrl();

                        if(isGif) {
                            gifView.setVisibility(View.VISIBLE);

                            if(othersPostDetails.getMediaDetail().getMediaType() == MEDIA_TYPE_GIPHY)
                            {
                                Gson gson = new Gson();
                                Images images = gson.fromJson(othersPostDetails.getMediaDetail().getExternalMeta(), Images.class);

                                Glide.with(this)
                                        .load(images.getDownsizedLarge().getUrl())
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                        .into(gifView);
                            }
                            else if(othersPostDetails.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
                                Glide.with(this)
                                        .load(othersPostDetails.getMediaDetail().getReactMediaUrl())
                                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                        .into(gifView);
                            }
                        }
                        else {
                            playerView.setVisibility(View.VISIBLE);
                        }

                        if (othersPostDetails != null) {
                            reactId = othersPostDetails.getReactId();
                            isLiked = !othersPostDetails.canLike();
                            likesCount = othersPostDetails.getLikes();
                            viewsCount = othersPostDetails.getViews();
                            reactionTitle = othersPostDetails.getReactTitle();


                            Glide.with(this)
                                    .load(othersPostDetails.getReactOwner().getProfileMedia() != null ? othersPostDetails.getReactOwner().getProfileMedia().getMediaUrl()
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
                            postDurationView.setText(othersPostDetails.getMediaDetail().getReactDuration());
                            reactionPostName.setText(othersPostDetails.getReactOwner().getFirstName());

                            ApiCallingService.Posts.getPostDetails(othersPostDetails.getPostId(), context)
                                    .enqueue(new Callback<PostDetails>() {
                                        @Override
                                        public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                                            if (response.code() == 200) {
                                                Glide.with(context).load(response.body().getMedias().get(0).getThumbUrl()).into(postImage);
                                                postTitle.setText("Reacted on " + response.body().getTitle());
                                            }
                                            else if (response.code() == 412 && response.message().contains("Precondition Failed"))
                                                Toast.makeText(context, "This post no longer exists", Toast.LENGTH_SHORT).show();
                                            else {
                                                Log.d("FETCHING PostDetails", response.code() + " : " + response.message());
                                                Toast.makeText(context, "Error fetching post", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<PostDetails> call, Throwable t) {
                                            t.printStackTrace();
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        initView();
                        incrementView();
                    } else {
                        Toast.makeText(context, R.string.reaction_no_longer_exists, Toast.LENGTH_SHORT).show();
                        navigation.popFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            }
            case OPENED_FROM_PROFILE: {
                try {
                    if (selfPostDetails != null) {
                        videoURL = selfPostDetails.getMediaDetail().getReactMediaUrl();


                        if (selfPostDetails != null) {

                            if (isGif) {
                                gifView.setVisibility(View.VISIBLE);

                                if (selfPostDetails.getMediaDetail().getMediaType() == MEDIA_TYPE_GIPHY) {
                                    Gson gson = new Gson();
                                    Images images = gson.fromJson(selfPostDetails.getMediaDetail().getExternalMeta(), Images.class);

                                    Glide.with(this)
                                            .load(images.getDownsizedLarge().getUrl())
                                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                            .into(gifView);
                                } else if (selfPostDetails.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
                                    Glide.with(this)
                                            .load(selfPostDetails.getMediaDetail().getReactMediaUrl())
                                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                                            .into(gifView);
                                }
                            } else {
                                playerView.setVisibility(View.VISIBLE);
                            }
                        }
                        if (selfPostDetails != null) {
                            reactId = selfPostDetails.getReactId();
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
                            String title="Reacted on " + selfPostDetails.getPostOwner().getFirstName() + " post";
                            postDurationView.setText(selfPostDetails.getMediaDetail().getReactDuration());
                          //  String udata="Underlined Text";
                            SpannableString content = new SpannableString(title);
                            content.setSpan(new UnderlineSpan(), 0, title.length(), 0);
                            reactionPostName.setText(content);
                           // reactionPostName.setText("Reacted on " + selfPostDetails.getPostOwner().getFirstName() + " post");

                        }

                        initView();
                    } else {
                        Toast.makeText(context, R.string.reaction_no_longer_exists, Toast.LENGTH_SHORT).show();
                        navigation.popFragment();
                    }
//                    incrementView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        reactionPostDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selfPostDetails != null) {
                    navigation.pushFragment(OthersProfileFragment.newInstance(
                            String.valueOf(selfPostDetails.getPostOwner().getUserId()), "", ""));
                }
                 else if(othersPostDetails !=null) {
                    navigation.pushFragment(othersPostDetails.getMySelf() ? ProfileFragment.newInstance() :
                            OthersProfileFragment.newInstance(
                                    String.valueOf(othersPostDetails.getReactOwner().getUserId()), "", ""));
                }
            }
        });

        reactionPostName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selfPostDetails!=null) {
                    myCreationListener.ReactionPost(selfPostDetails.getPostId());
                }
                else if(othersPostDetails !=null)
                {
                    myCreationListener.ReactionPost(othersPostDetails.getPostId());
                }

            }
        });

        postTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selfPostDetails!=null) {
                    myCreationListener.ReactionPost(selfPostDetails.getPostId());
                }
                else if(othersPostDetails !=null)
                {
                    myCreationListener.ReactionPost(othersPostDetails.getPostId());
                }
            }
        });
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selfPostDetails!=null) {
                    myCreationListener.ReactionPost(selfPostDetails.getPostId());
                }
                else if(othersPostDetails !=null)
                {
                    myCreationListener.ReactionPost(othersPostDetails.getPostId());
                }
            }
        });


        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myCreationListener=(ProfileMyCreationAdapter.myCreationListener)context;
    }

    private void incrementView() {
        if (othersPostDetails.getMySelf() && !othersPostDetails.canDelete()) {
            ApiCallingService.React.incrementReactionViewCount(reactId, getActivity())
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
            likeAction(isLiked, false);
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
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

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
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btnClose, R.id.btnLike})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                reactionPlayerCurrentPosition = 0;
                getActivity().onBackPressed();
                break;
            case R.id.btnLike:
                if (!isLiked) {
//            Like the post
                    isLiked = true;
                    likesCount++;
                    initView();
                    disableView(likeBtn, false);
                    ApiCallingService.React.likeDislikeReaction(reactId, 1, getActivity()).enqueue(new Callback<ResultObject>() {
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
                    ApiCallingService.React.likeDislikeReaction(reactId, 2, getActivity()).enqueue(new Callback<ResultObject>() {
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
            likeBtn.setImageResource(R.drawable.ic_like_filled);
            if (animate) {
                switch (playSource) {
                    case POST_REACTION:
//                        if (PostsListFragment.postDetails != null)
//                            PostsListFragment.postDetails.likes++;
                        break;
                    case SELF_REACTION:
                        break;
                }
                likeBtn.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.selected));
            }

        } else {
            likeBtn.setImageResource(R.drawable.ic_like_outline);
            if (animate) {
                switch (playSource) {
                    case POST_REACTION:
//                        if (PostsListFragment.postDetails != null)
//                            PostsListFragment.postDetails.likes++;
                        break;
                    case SELF_REACTION:
                        break;
                }
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getParentActivity().showToolbar();
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
        releaseAudioLock(getActivity(), audioFocusChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        getParentActivity().showToolbar();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
        releaseAudioLock(getActivity(), audioFocusChangeListener);
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
                        .setCanonicalIdentifier(String.valueOf(othersPostDetails.getReactOwner().getUserId()))
                        .setTitle(othersPostDetails.getReactTitle())
                        .setContentDescription("View this awesome video on Teazer app")
                        .setContentImageUrl(othersPostDetails.getMediaDetail().getReactThumbUrl());

                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("facebook")
                        .setFeature("sharing")
                        .addControlParameter("react_id", String.valueOf(othersPostDetails.getReactId()))
                        .addControlParameter("post_id", String.valueOf(othersPostDetails.getPostId()))
                        .addControlParameter("$desktop_url", "https://teazer.in/")
                        .addControlParameter("$ios_url", "https://teazer.in/");

                branchUniversalObject.generateShortUrl(getActivity(), linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {
                            //fabric event
                            logVideoShareEvent("Branch", othersPostDetails.getReactTitle(), "Reaction", String.valueOf(othersPostDetails.getReactId()));

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
            case SELF_REACTION:
            {
                BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                        .setCanonicalIdentifier(String.valueOf(selfPostDetails.getPostOwner().getUserId()))
                        .setTitle(selfPostDetails.getReactTitle())
                        .setContentDescription("View this awesome video on Teazer app")
                        .setContentImageUrl(selfPostDetails.getMediaDetail().getReactThumbUrl());

                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("facebook")
                        .setFeature("sharing")
                        .addControlParameter("react_id", String.valueOf(selfPostDetails.getReactId()))
                        .addControlParameter("post_id", String.valueOf(selfPostDetails.getPostId()))
                        .addControlParameter("$desktop_url", "https://teazer.in/")
                        .addControlParameter("$ios_url", "https://teazer.in/");

                branchUniversalObject.generateShortUrl(getActivity(), linkProperties, new Branch.BranchLinkCreateListener() {
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
