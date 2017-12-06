package com.cncoding.teazer.home.post;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CustomStaggeredGridLayoutManager;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.MediaControllerView;
import com.cncoding.teazer.customViews.MediaControllerView.MediaPlayerControlListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.ui.fragment.fragment.ReportPostDialogFragment;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostReaction;
import com.cncoding.teazer.utilities.Pojos.Post.PostReactionsList;
import com.cncoding.teazer.utilities.Pojos.Post.TaggedUsersList;
import com.cncoding.teazer.utilities.Pojos.TaggedUser;
import com.cncoding.teazer.utilities.ViewUtils;
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

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static com.cncoding.teazer.utilities.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.ViewUtils.enableView;

public class PostDetailsFragment extends BaseFragment implements MediaPlayerControlListener,
        SurfaceTextureListener, OnVideoSizeChangedListener {

    private static final String ARG_POST_DETAILS = "postDetails";
    private static final String ARG_THUMBNAIL = "thumbnail";
//    private static final String ARG_HAS_REACTED = "has_reacted";
    public static final int ACTION_DISMISS_PLACEHOLDER = 10;
    public static final int ACTION_OPEN_REACTION_CAMERA = 11;
    private static final String ARG_ENABLE_REACT_BTN = "enableReactBtn";
    private static final String ARG_IS_COMING_FROM_HOME_PAGE = "isComingFromHomePage";

    @BindView(R.id.root_layout)
    NestedScrollView nestedScrollView;
    @BindView(R.id.video_container)
    RelativeLayout videoContainer;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.video_surface)
    TextureView textureView;
    @BindView(R.id.placeholder)
    ImageView placeholder;
    @BindView(R.id.video_surface_container)
    FrameLayout surfaceContainer;
    @BindView(R.id.loading)
    ProgressBar progressBar;
    @BindView(R.id.react_btn)
    ProximaNovaSemiboldButton reactBtn;
    @BindView(R.id.like)
    ProximaNovaRegularCheckedTextView likeBtn;
    @BindView(R.id.no_tagged_users)
    ProximaNovaRegularTextView noTaggedUsers;
    @BindView(R.id.tagged_user_list)
    RecyclerView taggedUserListView;
    @BindView(R.id.horizontal_list_view_parent)
    RelativeLayout horizontalListViewParent;
    @BindView(R.id.tags_badge)
    ProximaNovaSemiboldTextView tagsCountBadge;
    @BindView(R.id.menu)
    ProximaNovaRegularTextView menu;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    //    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.post_load_error)
    ProximaNovaBoldTextView postLoadErrorTextView;
    @BindView(R.id.reactions_header)
    ProximaNovaBoldTextView reactionsHeader;
    @BindView(R.id.post_load_error_subtitle)
    ProximaNovaRegularTextView postLoadErrorSubtitle;
    @BindView(R.id.post_load_error_layout)
    LinearLayout postLoadErrorLayout;
    @BindView(R.id.share)
    ProximaNovaRegularTextView share;

    @BindView(R.id.video_view)
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;

    private Context context;
    private PostDetails postDetails;
    private boolean isComplete;
    private boolean enableReactBtn;
    private boolean isComingFromHomePage;
    private byte[] image;

    private Call<PostReactionsList> postReactionsListCall;
    private ArrayList<PostReaction> postReactions;
    private ArrayList<TaggedUser> taggedUsersList;
    private MediaControllerView controller;
    private MediaPlayer mediaPlayer;
    private OnPostDetailsInteractionListener mListener;
    private PostReactionAdapter postReactionAdapter;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    public static PostDetailsFragment newInstance(PostDetails postDetails, byte[] image, boolean enableReactBtn,
                                                  boolean isComingFromHomePage) {
        PostDetailsFragment fragment = new PostDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_POST_DETAILS, postDetails);
        args.putByteArray(ARG_THUMBNAIL, image);
        args.putBoolean(ARG_ENABLE_REACT_BTN, enableReactBtn);
        args.putBoolean(ARG_IS_COMING_FROM_HOME_PAGE, isComingFromHomePage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postReactions = new ArrayList<>();
        taggedUsersList = new ArrayList<>();
        if (getArguments() != null) {
            postDetails = getArguments().getParcelable(ARG_POST_DETAILS);
            image = getArguments().getByteArray(ARG_THUMBNAIL);
            enableReactBtn = getArguments().getBoolean(ARG_ENABLE_REACT_BTN);
            isComingFromHomePage = getArguments().getBoolean(ARG_IS_COMING_FROM_HOME_PAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getParentActivity().hideBottomBar();
        getParentActivity().updateToolbarTitle(getString(R.string.post));

        View rootView = inflater.inflate(R.layout.fragment_post_details, container, false);
        ButterKnife.bind(this, rootView);
        context = getContext();
//        getParentActivity().hidesettingsReport();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateTextureViewSize(postDetails.getMedias().get(0).getDimension().getWidth(),
                postDetails.getMedias().get(0).getDimension().getHeight());

        if (image != null)
            Glide.with(this)
                    .load(image)
                    .asBitmap()
                    .into(placeholder);

        progressBar.setVisibility(View.VISIBLE);

        likeAction(postDetails.canLike(), false);

        if (!postDetails.canReact()) disableView(reactBtn, true);

        if (!enableReactBtn) disableView(reactBtn, true);
        else enableView(reactBtn);

        tagsCountBadge.setText(String.valueOf(postDetails.getTotalTags()));
        tagsCountBadge.setVisibility(postDetails.getTotalTags() == 0 ? View.GONE : View.VISIBLE);

        prepareController();

        postReactionAdapter = new PostReactionAdapter(postReactions, context);
        CustomStaggeredGridLayoutManager manager = new CustomStaggeredGridLayoutManager(2, VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(postReactionAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getPostReactions(postDetails.getPostId(), page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                scrollListener.resetState();
//                getPostReactions(postDetails.getPostId(), 1);
//            }
//        });

        taggedUserListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        taggedUserListView.setAdapter(new TagListAdapter(context, taggedUsersList));
    }

    @Override
    public void onResume() {
        super.onResume();
        textureView.setSurfaceTextureListener(this);
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            isComplete = false;
            mediaPlayer.start();
        }

        if (postDetails != null) {
            postReactions.clear();
            getPostReactions(postDetails.getPostId(), 1);
        }

        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    private void prepareMediaPlayer(Surface surface) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(postDetails.getMedias().get(0).getMediaUrl());
            mediaPlayer.setSurface(surface);
            mediaPlayer.setOnVideoSizeChangedListener(this);
//            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                dismissProgressBar();
                isComplete = false;
                mediaPlayer.start();
                placeholder.animate().alpha(0).setDuration(400).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        placeholder.setVisibility(View.INVISIBLE);
                    }
                }, 400);
                controller.show(false, true, false);
//                mListener.onPostDetailsInteraction(ACTION_DISMISS_PLACEHOLDER);

//                Increment the video view count (only if this post belongs to someone else)
                if (PostsListFragment.postDetails != null)
                    PostsListFragment.postDetails.getMedias().get(0).views++;
                if (postDetails.canDelete()) {
                    ApiCallingService.Posts.incrementViewCount(postDetails.getMedias().get(0).getMediaId(), context)
                            .enqueue(new Callback<ResultObject>() {
                                @Override
                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                    if (response.code() == 200 && response.body().getStatus())
                                        controller.incrementViews();
                                }

                                @Override
                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                }
                            });
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                isComplete = true;
            }
        });
        mediaPlayer.setLooping(true);
        mediaPlayer.prepareAsync();
    }

    private void prepareController() {
        String profilePicUrl = "";
        if (postDetails.getPostOwner().getProfileMedia() != null)
            profilePicUrl = postDetails.getPostOwner().getProfileMedia().getThumbUrl();
        String location = "";
        if (postDetails.hasCheckin())
            location = postDetails.getCheckIn().getLocation();

        if (postDetails != null) {
            controller = new MediaControllerView.Builder(getActivity(), PostDetailsFragment.this)
                    .withVideoTitle(postDetails.getTitle())
                    .withVideoSurfaceView(textureView)
                    .withLocation(location)
                    .withProfileName(postDetails.getPostOwner().getFirstName() + " " + postDetails.getPostOwner().getLastName())
                    .withProfilePicUrl(profilePicUrl)
                    .withLikes(postDetails.getLikes())
                    .withViews(postDetails.getMedias().get(0).getViews())
                    .withCategories(getUserCategories())
                    .withDuration(postDetails.getMedias().get(0).getDuration())
                    .withReactionCount(postDetails.getTotalReactions())
                    .build(surfaceContainer);
        }

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY != 0) {
                    if (!controller.isShowing())
                        controller.show(true, false, true);
                }
            }
        });
    }

    private String getUserCategories() {
        if (postDetails.getCategories() != null) {
            StringBuilder categories = new StringBuilder();
            for (int i = 0; i < postDetails.getCategories().size(); i++) {
                categories.append(postDetails.getCategories().get(i).getCategoryName());
                if (i < postDetails.getCategories().size() - 1)
                    categories.append(", ");
            }
            return categories.toString();
        } else return null;
    }

    private void getPostReactions(final int postId, final int pageNumber) {
        postReactionsListCall = ApiCallingService.Posts.getReactionsOfPost(postId, pageNumber, context);

        if (postReactionsListCall != null && !postReactionsListCall.isExecuted())
            postReactionsListCall.enqueue(new Callback<PostReactionsList>() {
                @Override
                public void onResponse(Call<PostReactionsList> call, Response<PostReactionsList> response) {
                    switch (response.code()) {
                        case 200:
                            if (response.body().getReactions().size() > 0) {
                                postLoadErrorLayout.setVisibility(View.GONE);
                                is_next_page = response.body().isNextPage();
                                if (pageNumber == 1) postReactions.clear();

                                postReactions.addAll(response.body().getReactions());
//                                    recyclerView.setVisibility(View.VISIBLE);
                                postReactionAdapter.notifyDataSetChanged();
                                if (postReactions.size() > 0) {
                                    if (postReactions.size() >= 1) {
                                        controller.setReaction1Pic(postReactions.get(0).getMediaDetail().getThumbUrl());
                                    }
                                    if (postReactions.size() >= 2) {
                                        controller.setReaction2Pic(postReactions.get(1).getMediaDetail().getThumbUrl());
                                    }
                                    if (postReactions.size() >= 3) {
                                        controller.setReaction3Pic(postReactions.get(2).getMediaDetail().getThumbUrl());
                                    }
                                }
                            } else {
                                controller.setNoReactions();
                                showNoReactionMessage();
                            }
                            break;
                        default:
                            showErrorMessage("Error " + response.code() + ": " + response.message());
                            break;
                    }
                }

                private void showErrorMessage(String message) {
                    dismissProgressBar();
//                        recyclerView.setVisibility(View.INVISIBLE);
                    postLoadErrorLayout.animate().alpha(1).setDuration(280).start();
                    postLoadErrorLayout.setVisibility(View.VISIBLE);
                    message = getString(R.string.could_not_load_posts) + message;
                    postLoadErrorTextView.setText(message);
                    postLoadErrorSubtitle.setText(R.string.tap_to_retry);
                }

                @Override
                public void onFailure(Call<PostReactionsList> call, Throwable t) {
                    if (isAdded()) {
                        ViewUtils.makeSnackbarWithBottomMargin(getActivity(), recyclerView, t.getMessage());
                    }
                }
            });
    }

    private void showNoReactionMessage() {
        dismissProgressBar();
        reactionsHeader.setVisibility(View.GONE);
//        recyclerView.setVisibility(View.INVISIBLE);
        postLoadErrorLayout.animate().alpha(1).setDuration(280).start();
        postLoadErrorLayout.setVisibility(View.VISIBLE);
        postLoadErrorTextView.setText(R.string.no_reactions_yet);
        postLoadErrorSubtitle.setText(R.string.be_the_first_one_to_react);
    }

    private void dismissProgressBar() {
        progressBar.animate().scaleX(0).setDuration(280).setInterpolator(new DecelerateInterpolator()).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, 280);
    }

    @OnClick(R.id.react_btn)
    public void react() {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        mListener.onPostDetailsInteraction(ACTION_OPEN_REACTION_CAMERA, postDetails);
    }

    @OnClick(R.id.like)
    public void likePost() {
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
                if (isAdded()) {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        if (!likeBtn.isChecked()) {
//            Like the post
            ApiCallingService.Posts.likeDislikePost(postDetails.getPostId(), 1, context).enqueue(callback);
        } else {
//            Unlike the post
            ApiCallingService.Posts.likeDislikePost(postDetails.getPostId(), 2, context).enqueue(callback);
        }
        likeAction(likeBtn.isChecked(), true);
    }

    @OnClick(R.id.tags)
    public void getTaggedList() {
        if (horizontalListViewParent.getVisibility() == View.GONE) {
            ApiCallingService.Posts.getTaggedUsers(postDetails.getPostId(), 1, context)
                    .enqueue(new Callback<TaggedUsersList>() {

                        @Override
                        public void onResponse(Call<TaggedUsersList> call, Response<TaggedUsersList> response) {
                            if (response.code() == 200) {
                                horizontalListViewParent.setVisibility(View.VISIBLE);
                                if (response.body().getTaggedUsers().size() > 0) {
                                    taggedUsersList.addAll(response.body().getTaggedUsers());
                                    taggedUserListView.getAdapter().notifyDataSetChanged();
                                } else {
                                    noTaggedUsers.setVisibility(View.VISIBLE);
//                                    taggedUserListView.setLayoutManager(new LinearLayoutManager(context,
//                                            LinearLayoutManager.HORIZONTAL, false));
//                                    taggedUserListView.setAdapter(new TagListAdapter(context, getDummyTaggedUsersList()));
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            horizontalListViewParent.setVisibility(View.GONE);
                                            noTaggedUsers.setVisibility(View.GONE);
                                        }
                                    }, 2000);
                                }
                            } else {
                                Toast.makeText(context, R.string.error_getting_tagged_users, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TaggedUsersList> call, Throwable t) {
                            if (isAdded()) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            horizontalListViewParent.setVisibility(View.GONE);
        }
    }

    private void likeAction(boolean isChecked, boolean animate) {
        if (!isChecked) {
            likeBtn.setChecked(true);
            likeBtn.setText(R.string.liked);
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_filled, 0, 0);
            if (animate) {
                if (PostsListFragment.postDetails != null) {
                    PostsListFragment.postDetails.likes++;
                    PostsListFragment.postDetails.can_like = false;
                }
                likeBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.selected));
                controller.incrementLikes();
            }
        } else {
            likeBtn.setChecked(false);
            likeBtn.setText(R.string.like);
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_outline_dark, 0, 0);
            if (animate) {
                if (PostsListFragment.postDetails != null) {
                    PostsListFragment.postDetails.likes--;
                    PostsListFragment.postDetails.can_like = true;
                }
                likeBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.selected));
                controller.decrementLikes();
            }
        }
    }

    @OnClick(R.id.menu)
    public void showMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener());
        if (postDetails.canDelete())
            popupMenu.inflate(R.menu.menu_post_self);
        else
            popupMenu.inflate(R.menu.menu_post_others);
        popupMenu.show();
    }

    @OnClick(R.id.video_surface_container)
    public void toggleMediaControllerVisibility() {
        try {
//            if (mediaPlayer.isPlaying())
//                mediaPlayer.pause();
//            else mediaPlayer.start();

            if (controller != null)
                controller.toggleControllerView();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);
//        mediaPlayer.setDisplay(surfaceHolder);
//        prepareMediaPlayer(surface);
        initializePlayer();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
//        textureView.setAspectRatio(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        surfaceTexture.release();
        resetMediaPlayer();
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    private void updateTextureViewSize(int viewWidth, int viewHeight) {
        if (getActivity() != null) {
            int systemWidth = getActivity().getWindow().getDecorView().getWidth();
            viewHeight = systemWidth * viewHeight / viewWidth;
            viewWidth = systemWidth;
            if (viewHeight < viewWidth) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
                //noinspection SuspiciousNameCombination
                params.height = viewWidth;
                relativeLayout.setLayoutParams(params);
            }
        }
//        float scaleX = 1.0f;
//        float scaleY = 1.0f;
//
//        if (videoWidth > viewWidth && videoHeight > viewHeight) {
//            scaleX = videoWidth / viewWidth;
//            scaleY = videoHeight / viewHeight;
//        } else if (videoWidth < viewWidth && videoHeight < viewHeight) {
//            scaleY = viewWidth / videoWidth;
//            scaleX = viewHeight / videoHeight;
//        } else if (viewWidth > videoWidth) {
//            scaleY = (viewWidth / videoWidth) / (viewHeight / videoHeight);
//        } else if (viewHeight > videoHeight) {
//            scaleX = (viewHeight / videoHeight) / (viewWidth / videoWidth);
//        }
        /* Calculate pivot points, in our case crop from center*/
        int pivotPointX = viewWidth / 2;
        int pivotPointY = viewHeight / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(1, 1, pivotPointX, pivotPointY);

        textureView.setTransform(matrix);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        textureView.setLayoutParams(params);
        textureView.animate().alpha(1).setDuration(280).start();
        textureView.setVisibility(View.GONE);
        playerView.setLayoutParams(params);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.share)
    public void onViewClicked() {
//        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse("https://v6f43.app.goo.gl/?link="+ postDetails.getPostId()))
//                .setDynamicLinkDomain("v6f43.app.goo.gl")
//                // Open links with this app on Android
//                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
//                // Open links with com.example.ios on iOS
//                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.ios").build())
//                .buildDynamicLink();
//
//        Uri dynamicLinkUri = dynamicLink.getUri();
//
//
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, dynamicLinkUri.toString());
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(postDetails.getPostOwner().getFirstName())
                .setTitle(postDetails.getTitle())
                .setContentDescription("View this awesome video on Teazer app")
                .setContentImageUrl(postDetails.getMedias().get(0).getThumbUrl());

        LinkProperties linkProperties = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing")
                .addControlParameter("post_id", String.valueOf(postDetails.getPostId()))
                .addControlParameter("$desktop_url", "https://teazer.in/")
                .addControlParameter("$ios_url", "https://teazer.in/");

//        branchUniversalObject.generateShortUrl(this, linkProperties, new Branch.BranchLinkCreateListener() {
//            @Override
//            public void onLinkCreate(String url, BranchError error) {
//                if (error == null) {
//                    Log.i("MyApp", "got my Branch link to share: " + url);
//                }
//            }
//        });
        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(getActivity(), "Check this out!", "This video is awesome: ")
                .setCopyUrlStyle(getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.INSTAGRAM)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With");

        branchUniversalObject.showShareSheet(getActivity(),
                linkProperties,
                shareSheetStyle,
                new Branch.BranchLinkShareListener() {
                    @Override
                    public void onShareLinkDialogLaunched() {
                    }
                    @Override
                    public void onShareLinkDialogDismissed() {
                    }
                    @Override
                    public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
                    }
                    @Override
                    public void onChannelSelected(String channelName) {
                    }
                });
    }

    private class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_hide:
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.hiding_post)
                            .setMessage(R.string.hide_post_confirm)
                            .setPositiveButton(getString(R.string.yes_hide), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ApiCallingService.Posts.hideOrShowPost(postDetails.getPostId(), 1, getContext())
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    Toast.makeText(context, R.string.video_hide_successful, Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                                    Log.e("hidePost", t.getMessage() != null ? t.getMessage() : "FAILED!");
                                                }
                                            });
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                    return true;
                case R.id.action_delete:
                    Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_profile_report:
                    if (!postDetails.canDelete()) {
                        FragmentManager fragmentManager = getFragmentManager();
                        ReportPostDialogFragment reportPostDialogFragment = ReportPostDialogFragment.
                                newInstance(postDetails.getPostId(), postDetails.canReact());
                        // SETS the target fragment for use later when sending results
                        reportPostDialogFragment.setTargetFragment(PostDetailsFragment.this, 301);
                        if (fragmentManager != null) {
                            reportPostDialogFragment.show(fragmentManager, "fragment_report_post");
                        }
                    } else {
                        Toast.makeText(context, "You can not report your own video", Toast.LENGTH_SHORT).show();
                    }
                    return true;
            }
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPostDetailsInteractionListener) {
            mListener = (OnPostDetailsInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnPostDetailsInteractionListener");
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (mediaPlayer != null)
//            mediaPlayer.pause();

        player.setPlayWhenReady(!player.getPlayWhenReady());

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (isComingFromHomePage)
            PostsListFragment.isRefreshing = true;
        mListener = null;
        exit();
//        resetMediaPlayer();
        controller.exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        postReactionAdapter = null;
        if (postReactionsListCall != null)
            postReactionsListCall.cancel();
        getParentActivity().showBottomBar();
    }

    private void resetMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
//        videoHeight = mediaPlayer.getVideoHeight();
//        videoWidth = mediaPlayer.getVideoWidth();
//        if (videoHeight > 0 && videoWidth > 0)
//            textureView.setAspectRatio(this.mediaPlayer.getVideoWidth(), this.mediaPlayer.getVideoHeight());
//            textureView.adjustSize(videoContainer.getWidth(), videoContainer.getHeight(),
//                    this.mediaPlayer.getVideoWidth(), this.mediaPlayer.getVideoHeight());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (videoWidth > 0 && videoHeight > 0)
//            textureView.setAspectRatio(textureView.getWidth(), textureView.getHeight());
//            textureView.adjustSize(ViewUtils.getDeviceWidth(context), ViewUtils.getDeviceHeight(context),
//                    textureView.getWidth(), textureView.getHeight());
    }

    @Override
    public void start() {
//        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
//            isComplete = false;
//            mediaPlayer.start();
//        }
        player.setPlayWhenReady(!player.getPlayWhenReady());
    }

    @Override
    public void pause() {
//        if (mediaPlayer != null)
//            mediaPlayer.pause();
    }

    @Override
    public String getDuration() {
//        if (mediaPlayer != null)
//            return convert(mediaPlayer.getDuration());
//        else
        return postDetails.getMedias().get(0).getDuration();
    }

//    private String convert(final int duration) {
//        int dur, min, sec, mil;
//
//        dur = duration;
//        min = dur / 60000;
//        dur -= min * 60000;
//        sec = dur / 1000;
//        dur -= sec * 1000;
//        mil = dur;
//
//        return min + ":" + sec + "." + mil;
//    }

    @Override
    public int getCurrentPosition() {
        if (mediaPlayer != null)
            return mediaPlayer.getCurrentPosition();
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public int getBufferPercentage() {
        return 10;
    }

    @Override
    public void exit() {
//        resetMediaPlayer();
        controller.exit();
    }

    public interface OnPostDetailsInteractionListener {
        void onPostDetailsInteraction(int action, PostDetails postDetails);
    }

    interface PostDetailsUpdateListener {
        void onItemUpdated(int position, boolean isLiked, boolean isViewed, boolean isReacted);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        hideSystemUi();
//        if ((Util.SDK_INT <= 23 || player == null)) {
//            initializePlayer();
//        }
//    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (Util.SDK_INT <= 23) {
//            releasePlayer();
//        }
//    }

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
    private void initializePlayer() {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            Uri videoURI = Uri.parse(postDetails.getMedias().get(0).getMediaUrl());

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

            LoopingMediaSource loopingMediaSource = new LoopingMediaSource(mediaSource);
            playerView.setPlayer(player);
            player.prepare(loopingMediaSource);
//            player.setVideoSurface(surface);
            player.setPlayWhenReady(playWhenReady);
            player.getPlaybackState();
            dismissProgressBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}