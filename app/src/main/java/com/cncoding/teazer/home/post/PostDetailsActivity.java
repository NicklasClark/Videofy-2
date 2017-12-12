package com.cncoding.teazer.home.post;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.CustomStaggeredGridLayoutManager;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.post.TagListAdapter.TaggedListInteractionListener;
import com.cncoding.teazer.model.profile.delete.DeleteMyVideos;
import com.cncoding.teazer.services.receivers.ReactionUploadReceiver;
import com.cncoding.teazer.ui.fragment.fragment.ReportPostDialogFragment;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostReaction;
import com.cncoding.teazer.utilities.Pojos.Post.PostReactionsList;
import com.cncoding.teazer.utilities.Pojos.Post.TaggedUsersList;
import com.cncoding.teazer.utilities.Pojos.TaggedUser;
import com.cncoding.teazer.utilities.Pojos.UploadParams;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
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
import static android.util.DisplayMetrics.DENSITY_HIGH;
import static android.util.DisplayMetrics.DENSITY_MEDIUM;
import static android.util.DisplayMetrics.DENSITY_XHIGH;
import static android.util.DisplayMetrics.DENSITY_XXHIGH;
import static android.util.DisplayMetrics.DENSITY_XXXHIGH;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.BaseBottomBarActivity.REQUEST_CANCEL_UPLOAD;
import static com.cncoding.teazer.services.ReactionUploadService.launchReactionUploadService;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_COMPLETE_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_ERROR;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_ERROR_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_IN_PROGRESS_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_PROGRESS;
import static com.cncoding.teazer.utilities.SharedPrefs.finishReactionUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.getReactionUploadSession;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.ViewUtils.enableView;
import static com.cncoding.teazer.utilities.ViewUtils.launchReactionCamera;
import static com.cncoding.teazer.utilities.ViewUtils.setTextViewDrawableStart;
import static com.google.android.exoplayer2.ExoPlayer.STATE_BUFFERING;
import static com.google.android.exoplayer2.ExoPlayer.STATE_ENDED;
import static com.google.android.exoplayer2.ExoPlayer.STATE_IDLE;
import static com.google.android.exoplayer2.ExoPlayer.STATE_READY;

public class PostDetailsActivity extends AppCompatActivity implements TaggedListInteractionListener {

    //<editor-fold desc="Constants">
    public static final String SPACE = "  ";
    public static final String ARG_POST_DETAILS = "postDetails";
    public static final String ARG_THUMBNAIL = "thumbnail";
//    private static final String ARG_ENABLE_REACT_BTN = "enableReactBtn";
    public static final String ARG_IS_COMING_FROM_HOME_PAGE = "isComingFromHomePage";
    //</editor-fold>

    //<editor-fold desc="Main layout views">
    //    @BindView(R.id.root_layout) NestedScrollView nestedScrollView;
//    @BindView(R.id.video_container) RelativeLayout videoContainer;
    @BindView(R.id.relative_layout) RelativeLayout relativeLayout;
    @BindView(R.id.placeholder) ImageView placeholder;
    @BindView(R.id.loading) ProgressBar loadingProgressBar;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.react_btn) ProximaNovaSemiboldButton reactBtn;
    @BindView(R.id.like) ProximaNovaRegularCheckedTextView likeBtn;
    @BindView(R.id.no_tagged_users) ProximaNovaRegularTextView noTaggedUsers;
    @BindView(R.id.tagged_user_list) RecyclerView taggedUserListView;
    @BindView(R.id.horizontal_list_view_parent) RelativeLayout horizontalListViewParent;
    @BindView(R.id.tags_badge) ProximaNovaSemiboldTextView tagsCountBadge;
//    @BindView(R.id.menu) ProximaNovaRegularTextView menu;
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.post_load_error) ProximaNovaBoldTextView postLoadErrorTextView;
    @BindView(R.id.reactions_header) ProximaNovaBoldTextView reactionsHeader;
    @BindView(R.id.post_load_error_subtitle) ProximaNovaRegularTextView postLoadErrorSubtitle;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;
//    @BindView(R.id.share) ProximaNovaRegularTextView share;
    @BindView(R.id.video_view) SimpleExoPlayerView playerView;
    //</editor-fold>

    //<editor-fold desc="Controller views">
    @BindView(R.id.controls) FrameLayout controlsContainer;
    //top layout
    @BindView(R.id.media_controller_caption) ProximaNovaSemiboldTextView caption;
    @BindView(R.id.media_controller_location) ProximaNovaRegularTextView locationView;
    @BindView(R.id.media_controller_eta) ProximaNovaRegularTextView remainingTime;
    //center layout
    @BindView(R.id.media_controller_play_pause) AppCompatImageButton playPauseButton;

    //bottom layout
    @BindView(R.id.media_controller_dp) CircularAppCompatImageView profilePic;
    @BindView(R.id.media_controller_name) ProximaNovaSemiboldTextView profileNameView;
    @BindView(R.id.media_controller_likes) ProximaNovaRegularTextView likesView;
    @BindView(R.id.media_controller_views) ProximaNovaRegularTextView viewsView;
    @BindView(R.id.media_controller_categories) ProximaNovaSemiboldTextView categoriesView;
    @BindView(R.id.media_controller_reaction_count) ProximaNovaSemiboldTextView reactionCountView;
    @BindView(R.id.media_controller_reaction_1) CircularAppCompatImageView reaction1Pic;
    @BindView(R.id.media_controller_reaction_2) CircularAppCompatImageView reaction2Pic;
    @BindView(R.id.media_controller_reaction_3) CircularAppCompatImageView reaction3Pic;
    //</editor-fold>

    //<editor-fold desc="primitive members">
//    private long playbackPosition;
//    private int currentWindow;
//    private boolean enableReactBtn;
    private boolean playWhenReady = true;
    private boolean isComingFromHomePage;
    private byte[] image;
    private boolean is_next_page;
    private int currentVolume;
    private boolean isAudioEnabled;
    private int likes;
    private int views;
    private boolean oneShotFlag;
    //</editor-fold>

    //<editor-fold desc="Objects">
    private SimpleExoPlayer player;
    private PostDetails postDetails;
    private Call<PostReactionsList> postReactionsListCall;
    private ArrayList<PostReaction> postReactions;
    private ArrayList<TaggedUser> taggedUsersList;
    private PostReactionAdapter postReactionAdapter;
    private AudioManager audioManager;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private ReactionUploadReceiver reactionUploadReceiver;
    private static boolean isDeepLink = false;
    private String thumbUrl;
    //</editor-fold>

    public PostDetailsActivity() {
        // Required empty public constructor
    }

    public static void newInstance(Context packageContext, @NonNull PostDetails postDetails, byte[] image,
                                   boolean isComingFromHomePage, boolean isDeepLink, String thumbUrl) {
        Intent intent = new Intent(packageContext, PostDetailsActivity.class);
        intent.putExtra(ARG_POST_DETAILS, postDetails);
        if (!isDeepLink) {
            intent.putExtra(ARG_THUMBNAIL, image);
        } else {
            PostDetailsActivity.isDeepLink = true;
            intent.putExtra(ARG_THUMBNAIL, thumbUrl);
        }
//        intent.putExtra(ARG_ENABLE_REACT_BTN, enableReactBtn);
        intent.putExtra(ARG_IS_COMING_FROM_HOME_PAGE, isComingFromHomePage);
        packageContext.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logTheDensity();

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |                                       // hide nav bar
                        View.SYSTEM_UI_FLAG_FULLSCREEN |                                            // hide status bar
                        View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_post_details);
        ButterKnife.bind(this);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        setupServiceReceiver();

        categoriesView.setSelected(true);
        postReactions = new ArrayList<>();
        taggedUsersList = new ArrayList<>();
        if (getIntent() != null) {
            postDetails = getIntent().getParcelableExtra(ARG_POST_DETAILS);
            if (!isDeepLink) {
                image = getIntent().getByteArrayExtra(ARG_THUMBNAIL);
            }
            else
            {
                thumbUrl = getIntent().getStringExtra(ARG_THUMBNAIL);
            }
//            enableReactBtn = getIntent().getBooleanExtra(ARG_ENABLE_REACT_BTN, true);
            isComingFromHomePage = getIntent().getBooleanExtra(ARG_IS_COMING_FROM_HOME_PAGE, false);
        }
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        oneShotFlag = true;
        updateTextureViewSize(postDetails.getMedias().get(0).getDimension().getWidth(),
                postDetails.getMedias().get(0).getDimension().getHeight());

        if (image != null && !isDeepLink)
            Glide.with(this)
                    .load(image)
                    .asBitmap()
                    .into(placeholder);
        else
            Glide.with(this)
                    .load(thumbUrl)
                    .into(placeholder);

        loadingProgressBar.setVisibility(VISIBLE);

        likeAction(postDetails.canLike(), false);

        if (!postDetails.canReact()) disableView(reactBtn, true);

//        if (!enableReactBtn) disableView(reactBtn, true);
//        else enableView(reactBtn);

        tagsCountBadge.setText(String.valueOf(postDetails.getTotalTags()));
        tagsCountBadge.setVisibility(postDetails.getTotalTags() == 0 ? GONE : VISIBLE);

        prepareController();

        postReactionAdapter = new PostReactionAdapter(postReactions, this);
        CustomStaggeredGridLayoutManager manager = new CustomStaggeredGridLayoutManager(2, VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(postReactionAdapter);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getPostReactions(postDetails.getPostId(), page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        taggedUserListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        taggedUserListView.setAdapter(new TagListAdapter(this, taggedUsersList));

        getTaggedUsers(1);
    }

    private void logTheDensity() {
        switch (getResources().getDisplayMetrics().densityDpi) {
            case DENSITY_MEDIUM:
                Log.d("DEVICE DENSITY", "mdpi");
                break;
            case DENSITY_HIGH:
                Log.d("DEVICE DENSITY", "hdpi");
                break;
            case DENSITY_XHIGH:
                Log.d("DEVICE DENSITY", "xhdpi");
                break;
            case DENSITY_XXHIGH:
                Log.d("DEVICE DENSITY", "xxhdpi");
                break;
            case DENSITY_XXXHIGH:
                Log.d("DEVICE DENSITY", "xxhdpi");
                break;
            default:
                Log.d("DEVICE DENSITY DPI", String.valueOf(getResources().getDisplayMetrics().densityDpi));
                Log.d("DEVICE DENSITY", String.valueOf(getResources().getDisplayMetrics().density));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkIfAnyReactionIsUploading();
        if (postDetails != null) {
            postReactions.clear();
            getPostReactions(postDetails.getPostId(), 1);
        }

        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }

        if (currentVolume <= 0 && audioManager != null) {
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        isAudioEnabled = currentVolume > 0;
    }

    private void prepareController() {
        if (postDetails != null) {
            caption.setText(postDetails.getTitle());
            locationView.setVisibility(postDetails.getCheckIn() != null ? VISIBLE : GONE);
            locationView.setText(locationView.getVisibility() == VISIBLE ?
                    (postDetails.hasCheckin() ? SPACE + postDetails.getCheckIn().getLocation() : "") : "");
            profileNameView.setText(postDetails.getPostOwner().getUserName());

            this.likes = postDetails.getLikes();
            String likes = SPACE + this.likes;
            likesView.setText(likes);

            this.views = postDetails.getMedias().get(0).getViews();
            String views = SPACE + this.views;
            viewsView.setText(views);

            String categories = getUserCategories();
            if (categories != null && categories.length() > 1) {
                categories = "Categories:    " + categories;
                categoriesView.setText(categories);
            } else categoriesView.setVisibility(GONE);

            String duration = BLANK_SPACE + postDetails.getMedias().get(0).getDuration() + " secs";
            remainingTime.setText(duration);

            reactionCountView.setText(String.valueOf(postDetails.getTotalReactions()));
            tagsCountBadge.setText(String.valueOf(postDetails.getTotalTags()));

            Glide.with(this)
                    .load(postDetails.getPostOwner().getProfileMedia() != null ?
                            postDetails.getPostOwner().getProfileMedia().getThumbUrl() : "")
                    .placeholder(R.drawable.ic_user_male_dp_small)
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            profilePic.setImageDrawable(resource);
                            return true;
                        }
                    })
                    .into(profilePic);
            controlsContainer.setVisibility(VISIBLE);
//            reaction1Url = builder.reaction1Url;
//            reaction2Url = builder.reaction2Url;
//            reaction3Url = builder.reaction3Url;
//            controller = new MediaControllerView.Builder(this, PostDetailsActivity.this)
//                    .withVideoTitle(postDetails.getTitle())
////                    .withVideoSurfaceView(textureView)
//                    .withLocation(location)
//                    .withProfileName(postDetails.getPostOwner().getFirstName() + " " + postDetails.getPostOwner().getLastName())
//                    .withProfilePicUrl(profilePicUrl)
//                    .withLikes(postDetails.getLikes())
//                    .withViews(postDetails.getMedias().get(0).getViews())
//                    .withCategories(getUserCategories())
//                    .withDuration(postDetails.getMedias().get(0).getDuration())
//                    .withReactionCount(postDetails.getTotalReactions())
//                    .build(controlsContainer);
        }
    }

    private String getUserCategories() {
        if (postDetails.getCategories() != null) {
            StringBuilder categories = new StringBuilder();
            for (int i = 0; i < postDetails.getCategories().size(); i++) {
                categories.append(postDetails.getCategories().get(i).getCategoryName());
                if (i < postDetails.getCategories().size() - 1)
                    categories.append("    ");
            }
            return categories.toString();
        } else return null;
    }

    private void getPostReactions(final int postId, final int pageNumber) {
        postReactionsListCall = ApiCallingService.Posts.getReactionsOfPost(postId, pageNumber, this);

        if (postReactionsListCall != null && !postReactionsListCall.isExecuted())
            postReactionsListCall.enqueue(new Callback<PostReactionsList>() {
                @Override
                public void onResponse(Call<PostReactionsList> call, Response<PostReactionsList> response) {
                    switch (response.code()) {
                        case 200:
                            if (response.body().getReactions().size() > 0) {
                                postLoadErrorLayout.setVisibility(GONE);
                                is_next_page = response.body().isNextPage();
                                if (pageNumber == 1) postReactions.clear();

                                postReactions.addAll(response.body().getReactions());
//                                    recyclerView.setVisibility(View.VISIBLE);
                                postReactionAdapter.notifyDataSetChanged();
                                if (postReactions.size() > 0) {
                                    if (postReactions.size() >= 1) {
                                        setReaction1Pic(postReactions.get(0).getMediaDetail().getThumbUrl());
                                    }
                                    if (postReactions.size() >= 2) {
                                        setReaction2Pic(postReactions.get(1).getMediaDetail().getThumbUrl());
                                    }
                                    if (postReactions.size() >= 3) {
                                        setReaction3Pic(postReactions.get(2).getMediaDetail().getThumbUrl());
                                    }
                                }
                            } else {
                                setNoReactions();
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
                    postLoadErrorLayout.setVisibility(VISIBLE);
                    message = getString(R.string.could_not_load_posts) + message;
                    postLoadErrorTextView.setText(message);
                    postLoadErrorSubtitle.setText(R.string.tap_to_retry);
                }

                @Override
                public void onFailure(Call<PostReactionsList> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

    private void showNoReactionMessage() {
        dismissProgressBar();
        reactionsHeader.setVisibility(GONE);
//        recyclerView.setVisibility(View.INVISIBLE);
        postLoadErrorLayout.animate().alpha(1).setDuration(280).start();
        postLoadErrorLayout.setVisibility(VISIBLE);
        postLoadErrorTextView.setText(R.string.no_reactions_yet);
        postLoadErrorSubtitle.setText(R.string.be_the_first_one_to_react);
    }

    private void dismissProgressBar() {
        loadingProgressBar.animate().scaleX(0).setDuration(280).setInterpolator(new DecelerateInterpolator()).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingProgressBar.setVisibility(INVISIBLE);
            }
        }, 280);
    }

    public void incrementLikes() {
        String likesText = PostDetailsActivity.SPACE + ++likes;
        likesView.setText(likesText);
    }

    public void decrementLikes() {
        String likesText = PostDetailsActivity.SPACE + --likes;
        likesView.setText(likesText);
    }

    public void incrementViews() {
        String viewsText = PostDetailsActivity.SPACE + ++views;
        viewsView.setText(viewsText);
    }

    public void setReaction1Pic(String reaction1PicUrl) {
        reaction1Pic.setVisibility(VISIBLE);
        Glide.with(this)
                .load(reaction1PicUrl)
                .placeholder(R.drawable.ic_user_male_dp_small)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        reaction1Pic.setImageDrawable(resource);
                        return true;
                    }
                })
                .into(reaction1Pic);
    }

    public void setReaction2Pic(String reaction2PicUrl) {
        reaction2Pic.setVisibility(VISIBLE);
        Glide.with(this)
                .load(reaction2PicUrl)
                .placeholder(R.drawable.ic_user_male_dp_small)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        reaction2Pic.setImageDrawable(resource);
                        return true;
                    }
                })
                .into(reaction2Pic);
    }

    public void setReaction3Pic(String reaction3PicUrl) {
        reaction3Pic.setVisibility(VISIBLE);
        Glide.with(this)
                .load(reaction3PicUrl)
                .placeholder(R.drawable.ic_user_male_dp_small)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        reaction3Pic.setImageDrawable(resource);
                        return true;
                    }
                })
                .into(reaction3Pic);
    }

    public void setNoReactions() {
        reactionCountView.setText("");
    }

    @OnClick(R.id.btnClose) public void goBack() {
        finish();
    }

    @OnClick(R.id.media_controller_eta) public void toggleSound() {
        if (audioManager != null) {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            int volume;

            if (isAudioEnabled) {
                volume = 0;
                setTextViewDrawableStart(remainingTime, R.drawable.ic_volume_mute);
                isAudioEnabled = false;
            } else {
                if (currentVolume > 0)
                    volume = currentVolume;
                else volume = maxVolume;
//                volume = 100 * maxVolume + currentVolume;
                setTextViewDrawableStart(remainingTime, R.drawable.ic_volume);
                isAudioEnabled = true;
            }

            if (volume > maxVolume) {
                volume = maxVolume;
            }

            if (volume < 0) {
                volume = 0;
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }

    @OnClick(R.id.react_btn) public void react() {
        launchReactionCamera(this, postDetails);
    }

    @OnClick(R.id.like) public void likePost() {
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
        if (!likeBtn.isChecked()) {
//            Like the post
            ApiCallingService.Posts.likeDislikePost(postDetails.getPostId(), 1, this).enqueue(callback);
        } else {
//            Unlike the post
            ApiCallingService.Posts.likeDislikePost(postDetails.getPostId(), 2, this).enqueue(callback);
        }
        likeAction(likeBtn.isChecked(), true);
    }

    @OnClick(R.id.tags) public void getTaggedList() {
        if (horizontalListViewParent.getVisibility() == GONE) {
            horizontalListViewParent.setVisibility(VISIBLE);
        } else {
            horizontalListViewParent.setVisibility(GONE);
        }
    }

    private void getTaggedUsers(final int page) {
        ApiCallingService.Posts.getTaggedUsers(postDetails.getPostId(), 1, this)
                .enqueue(new Callback<TaggedUsersList>() {

                    @Override
                    public void onResponse(Call<TaggedUsersList> call, Response<TaggedUsersList> response) {
                        if (response.code() == 200) {
                            TaggedUsersList taggedList = response.body();
                            if (taggedList.isNextPage()) {
                                if (!taggedList.getTaggedUsers().isEmpty()) {
                                    if (page == 1)
                                        taggedUsersList.clear();

                                    taggedUsersList.addAll(taggedList.getTaggedUsers());
                                    getTaggedUsers(page + 1);
                                    noTaggedUsers.setVisibility(GONE);
                                }
                                else if(page == 1 && taggedList.getTaggedUsers().isEmpty()) {
                                    taggedUsersList.clear();
                                    noTaggedUsers.setVisibility(VISIBLE);
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            horizontalListViewParent.setVisibility(GONE);
//                                        }
//                                    }, 2000);
                                }
                            } else {
                                if(page == 1 && taggedList.getTaggedUsers().isEmpty()) {
                                    taggedUsersList.clear();
                                    noTaggedUsers.setVisibility(VISIBLE);
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            horizontalListViewParent.setVisibility(GONE);
//                                        }
//                                    }, 2000);
                                } else {
                                    noTaggedUsers.setVisibility(GONE);
                                    taggedUsersList.addAll(taggedList.getTaggedUsers());
                                    taggedUserListView.getAdapter().notifyDataSetChanged();
                                }
                            }
                        } else {
                            Toast.makeText(PostDetailsActivity.this, R.string.error_getting_tagged_users,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TaggedUsersList> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
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
                likeBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.selected));
                incrementLikes();
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
                likeBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.selected));
                decrementLikes();
            }
        }
    }

    @OnClick(R.id.menu) public void showMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener());
        if (postDetails.canDelete())
            popupMenu.inflate(R.menu.menu_post_self);
        else
            popupMenu.inflate(R.menu.menu_post_others);
        popupMenu.show();
    }

    @OnClick(R.id.controls) public void toggleMediaControllerVisibility() {
        try {
            player.setPlayWhenReady(!player.getPlayWhenReady());
            playPauseButton.setImageResource(!player.getPlayWhenReady() ? R.drawable.ic_play_big
                    : R.drawable.ic_pause_big);
            togglePlayPauseBtnVisibility(!player.getPlayWhenReady());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextureViewSize(int viewWidth, int viewHeight) {
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        int systemWidth = point.x;
        viewHeight = systemWidth * viewHeight / viewWidth;
        viewWidth = systemWidth;
        if (viewHeight < viewWidth) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
            //noinspection SuspiciousNameCombination
            params.height = viewWidth;
            relativeLayout.setLayoutParams(params);
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
//        /* Calculate pivot points, in our case crop from center*/
//        int pivotPointX = viewWidth / 2;
//        int pivotPointY = viewHeight / 2;
//        Matrix matrix = new Matrix();
//        matrix.setScale(1, 1, pivotPointX, pivotPointY);
//        textureView.setTransform(matrix);
//        textureView.setLayoutParams(params);
//        textureView.animate().alpha(1).setDuration(280).start();
//        textureView.setVisibility(GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        playerView.setLayoutParams(params);
        playerView.setResizeMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
    }

    @OnClick(R.id.share) public void onViewClicked() {
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
        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(this,
                "Check this out!", "This video is awesome: ")
                .setCopyUrlStyle(getResources().getDrawable(android.R.drawable.ic_menu_send),
                        "Copy", "Added to clipboard")
                .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.INSTAGRAM)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                .setAsFullWidthStyle(true)
                .setSharingTitle("Share With");

        branchUniversalObject.showShareSheet(this,
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

    @Override
    public void onTaggedUserInteraction(int userId, boolean isSelf) {
        isComingFromHomePage = false;
        Intent intent = new Intent(this, BaseBottomBarActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        bundle.putBoolean("isSelf", isSelf);
        intent.putExtra("profileBundle", bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_hide:
                    new AlertDialog.Builder(PostDetailsActivity.this)
                            .setTitle(R.string.hiding_post)
                            .setMessage(R.string.hide_post_confirm)
                            .setPositiveButton(getString(R.string.yes_hide), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ApiCallingService.Posts.hideOrShowPost(postDetails.getPostId(), 1, PostDetailsActivity.this)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (response.code() == 200) {
                                                        Toast.makeText(PostDetailsActivity.this,
                                                                R.string.video_hide_successful,
                                                                Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(PostDetailsActivity.this,
                                                                R.string.something_went_wrong,
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    t.printStackTrace();
                                                    Toast.makeText(PostDetailsActivity.this,
                                                            R.string.something_went_wrong,
                                                            Toast.LENGTH_SHORT).show();
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
                    new AlertDialog.Builder(PostDetailsActivity.this)
                            .setTitle(R.string.confirm)
                            .setMessage("Are you sure you want to delete this video?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,int which) {
                                    deleteVideo(postDetails.getPostId());
                                    PostsListFragment.postDetails = null;
                                    finish();
                                }})
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return true;
                case R.id.action_profile_report:
                    if (!postDetails.canDelete()) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        ReportPostDialogFragment reportPostDialogFragment = ReportPostDialogFragment.
                                newInstance(postDetails.getPostId(), postDetails.canReact());
                        // SETS the target fragment for use later when sending results
//                        reportPostDialogFragment.setTargetFragment(PostDetailsActivity.this, 301);
                        if (fragmentManager != null) {
                            reportPostDialogFragment.show(fragmentManager, "fragment_report_post");
                        }
                    } else {
                        Toast.makeText(PostDetailsActivity.this, "You can not report your own video", Toast.LENGTH_SHORT).show();
                    }
                    return true;
            }
            return false;
        }
    }

    private void deleteVideo(int postId) {
        ApiCallingService.Posts.deletePosts(postId, getApplicationContext()).enqueue(new Callback<DeleteMyVideos>() {
            @Override
            public void onResponse(Call<DeleteMyVideos> call, Response<DeleteMyVideos> response) {
                try {
                    if (response.code() == 200) {
                        Toast.makeText(PostDetailsActivity.this, "Video has been deleted", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(PostDetailsActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DeleteMyVideos> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        player.setPlayWhenReady(!player.getPlayWhenReady());

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isComingFromHomePage)
            PostsListFragment.isRefreshing = true;
        postReactionAdapter = null;
        if (postReactionsListCall != null)
            postReactionsListCall.cancel();
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

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
//            playbackPosition = player.getCurrentPosition();
//            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private void togglePlayPauseBtnVisibility(boolean visible) {
        if (visible) {
            playPauseButton.animate()
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(200)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            playPauseButton.setVisibility(VISIBLE);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    playPauseButton.animate()
                            .scaleY(0)
                            .scaleX(0)
                            .setDuration(200)
                            .setInterpolator(new DecelerateInterpolator())
                            .start();
                    playPauseButton.setVisibility(INVISIBLE);
                }
            }, 500);
        }
    }

    private void initializePlayer() {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            player.addListener(new ExoPlayer.EventListener() {

                @Override
                public void onPlayerStateChanged(boolean b, int i) {
                    switch (i) {
                        case STATE_IDLE:
                            playPauseButton.setImageResource(R.drawable.ic_play_big);
                            togglePlayPauseBtnVisibility(true);
                            break;
                        case STATE_BUFFERING:
                            progressBar.setVisibility(VISIBLE);
                            break;
                        case STATE_READY:
                            if (player.getPlayWhenReady()) {
                                playPauseButton.setImageResource(R.drawable.ic_pause_big);
                                if (playPauseButton.getVisibility() == VISIBLE)
                                    togglePlayPauseBtnVisibility(false);
                                progressBar.setVisibility(GONE);
                                placeholder.setImageBitmap(null);
                                image = null;
                            }
                            if (oneShotFlag) {
                                oneShotFlag = false;
                                if (!postDetails.canDelete()) {
                                    ApiCallingService.Posts.incrementViewCount(postDetails.getMedias().get(0).getMediaId(),
                                            PostDetailsActivity.this)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (response.code() == 200 && response.body().getStatus()) {
                                                        incrementViews();
                                                        if (PostsListFragment.postDetails != null)
                                                            PostsListFragment.postDetails.getMedias().get(0).views++;
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                }
                                            });
                                }
                            }
                            break;
                        case STATE_ENDED:
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onTimelineChanged(Timeline timeline, Object o) {
                }
                @Override
                public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
                }
                @Override
                public void onLoadingChanged(boolean b) {
                }
                @Override
                public void onPlayerError(ExoPlaybackException e) {
                }
                @Override
                public void onPositionDiscontinuity() {
                }
                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                }
            });

            Uri videoURI = Uri.parse(postDetails.getMedias().get(0).getMediaUrl());

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource =
                    new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

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

    //<editor-fold desc="Video upload handler">
    private void setupServiceReceiver() {
        builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle("Teazer reaction upload")
                .setContentText("Uploading")
//                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_file_upload)
                .setAutoCancel(false)
                .setSound(null)
                .setOngoing(true)
                .setDefaults(0)
                .addAction(R.drawable.ic_cancel_dark_small, "Cancel",
                        PendingIntent.getActivity(PostDetailsActivity.this, REQUEST_CANCEL_UPLOAD, new Intent(), 0))
                .setProgress(0, 0, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    "Upload notification", NotificationManager.IMPORTANCE_NONE);

            // Configure the notification channel.
            notificationChannel.setDescription("reactionUploadChanel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.CYAN);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        reactionUploadReceiver = new ReactionUploadReceiver(new Handler())
                .setReceiver(new ReactionUploadReceiver.Receiver() {
                    @Override
                    public void onReceiverResult(int resultCode, Bundle resultData) {
                        switch(resultCode) {
                            case UPLOAD_IN_PROGRESS_CODE:
                                builder.setProgress(100, resultData.getInt(UPLOAD_PROGRESS), false)
                                        .setContentText(String.valueOf(resultData.getInt(UPLOAD_PROGRESS) + "%"));
                                notifyProgressInNotification();

                                if (reactBtn.isEnabled())
                                    disableView(reactBtn, true);
//                                Log.d(UPLOAD_PROGRESS, String.valueOf(resultData.getInt(UPLOAD_PROGRESS)));
                                break;
                            case UPLOAD_COMPLETE_CODE:
                                builder.setOngoing(false);
                                builder.setContentText("Finished!")
                                        .setProgress(0, 0, false);
                                notifyProgressInNotification();

                                disableView(reactBtn, true);
                                if (PostsListFragment.postDetails != null)
                                    PostsListFragment.postDetails.can_react = false;

                                finishReactionUploadSession(getApplicationContext());

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        notificationManager.cancel(0);
                                    }
                                }, 4000);

                                getPostReactions(postDetails.getPostId(), 1);
//                                final String s="https://s3.ap-south-1.amazonaws.com/teazer-medias/Teazer/post/2/4/1511202104939_thumb.png";
//                                new ShowShareDialog().execute(s);
                                break;
                            case UPLOAD_ERROR_CODE:
                                String failedMessage = String.valueOf(resultData.getString(UPLOAD_ERROR));
                                Log.e(UPLOAD_ERROR, failedMessage != null ? failedMessage : "FAILED!!!");

                                builder.setOngoing(false);
                                builder.setContentText("Upload failed!")
                                        .setProgress(0, 0, false);
                                notifyProgressInNotification();

                                enableView(reactBtn);
                                if (PostsListFragment.postDetails != null)
                                    PostsListFragment.postDetails.can_react = true;

                                finishReactionUploadSession(getApplicationContext());

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        notificationManager.cancel(0);
                                    }
                                }, 4000);

                                break;
                            case REQUEST_CANCEL_UPLOAD:
                                Toast.makeText(PostDetailsActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    private void checkIfAnyReactionIsUploading() {
        UploadParams uploadParams = getReactionUploadSession(getApplicationContext());
        if (uploadParams != null) {
            finishReactionUploadSession(getApplicationContext());
            disableView(reactBtn, true);
            if (PostsListFragment.postDetails != null)
                PostsListFragment.postDetails.can_react = false;
            launchReactionUploadService(getApplicationContext(), uploadParams, reactionUploadReceiver);
        }
    }

    private void notifyProgressInNotification() {
        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }
    }

//    private void deleteFile(String path, boolean isGallery) {
//        if (!isGallery) {
//            deleteFileFromMediaStoreDatabase(getApplicationContext(), path);
//            //noinspection ResultOfMethodCallIgnored
//            new File(path).delete();
//        }
//    }

//    if (uploadParams.isReaction() && isResuming) {
//        uploadParams.getPostDetails().can_react = false;
//        PostDetailsActivity.newInstance(reference.get(), uploadParams.getPostDetails(), null, false);
//    }

    @SuppressWarnings("unused")
    private static class ShowShareDialog extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<PostDetailsActivity> reference;

        ShowShareDialog(PostDetailsActivity context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                final URL url = new URL(strings[0]);
                try {
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            ShareDialog shareDialog = new ShareDialog(reference.get());
            shareDialog.show(content);
            // ShareApi.share(content, null);
            super.onPostExecute(bitmap);
        }
    }
    //</editor-fold>

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}