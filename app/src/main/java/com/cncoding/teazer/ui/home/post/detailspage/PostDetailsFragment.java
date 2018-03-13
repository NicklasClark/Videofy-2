package com.cncoding.teazer.ui.home.post.detailspage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.UploadParams;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.TaggedUsersList;
import com.cncoding.teazer.data.model.react.GiphyReactionRequest;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.receiver.ReactionUploadReceiver;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.common.CustomLinearLayoutManager;
import com.cncoding.teazer.ui.customviews.common.CustomStaggeredGridLayoutManager;
import com.cncoding.teazer.ui.customviews.common.DynamicProgress;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.customviews.exoplayer.PlayerEventListener;
import com.cncoding.teazer.ui.customviews.exoplayer.SimpleExoPlayerView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaBoldButton;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaBoldTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.post.BasePostFragment;
import com.cncoding.teazer.ui.home.profile.fragment.ReportPostDialogFragment;
import com.cncoding.teazer.utilities.asynctasks.AddWaterMarkAsyncTask;
import com.cncoding.teazer.utilities.asynctasks.AddWaterMarkAsyncTask.WatermarkAsyncResponse;
import com.cncoding.teazer.utilities.audio.AudioVolumeContentObserver.OnAudioVolumeChangedListener;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.STREAM_MUSIC;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.data.service.ReactionUploadService.launchReactionUploadService;
import static com.cncoding.teazer.data.service.VideoUploadService.ADD_WATERMARK;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_COMPLETE_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_ERROR_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_IN_PROGRESS_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_PROGRESS;
import static com.cncoding.teazer.data.service.VideoUploadService.VIDEO_PATH;
import static com.cncoding.teazer.ui.customviews.coachMark.MaterialShowcaseView.TYPE_POST_DETAILS;
import static com.cncoding.teazer.ui.customviews.exoplayer.AspectRatioFrameLayout.RESIZE_MODE_ZOOM;
import static com.cncoding.teazer.ui.home.base.BaseBottomBarActivity.COACH_MARK_DELAY;
import static com.cncoding.teazer.ui.home.base.BaseBottomBarActivity.REQUEST_CANCEL_UPLOAD;
import static com.cncoding.teazer.ui.home.post.detailspage.FragmentLikedUser.LIKED_USERS_OF_POSTS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_CREATE_REACTION_BY_GIPHY;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_DELETE_POST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_POST_DETAILS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_REACTIONS_OF_POST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_TAGGED_USERS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_HIDE_OR_SHOW_POST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_INCREMENT_VIEW_COUNT;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_LIKE_DISLIKE_POST;
import static com.cncoding.teazer.utilities.common.Annotations.HIDE;
import static com.cncoding.teazer.utilities.common.Annotations.SEND_DISLIKE;
import static com.cncoding.teazer.utilities.common.Annotations.SEND_LIKE;
import static com.cncoding.teazer.utilities.common.AuthUtils.isConnected;
import static com.cncoding.teazer.utilities.common.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.common.CommonUtilities.deleteFilePermanently;
import static com.cncoding.teazer.utilities.common.CommonWebServicesUtil.fetchReactionDetails;
import static com.cncoding.teazer.utilities.common.FabricAnalyticsUtil.logVideoShareEvent;
import static com.cncoding.teazer.utilities.common.MediaUtils.acquireAudioLock;
import static com.cncoding.teazer.utilities.common.MediaUtils.releaseAudioLock;
import static com.cncoding.teazer.utilities.common.SharedPrefs.deleteMedia;
import static com.cncoding.teazer.utilities.common.SharedPrefs.finishReactionUploadSession;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getMedia;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getReactionUploadSession;
import static com.cncoding.teazer.utilities.common.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.enableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.getCoachMark;
import static com.cncoding.teazer.utilities.common.ViewUtils.launchReactionCamera;
import static com.cncoding.teazer.utilities.common.ViewUtils.openProfile;
import static com.cncoding.teazer.utilities.common.ViewUtils.setTextViewDrawableStart;
import static com.google.android.exoplayer2.Player.STATE_BUFFERING;
import static com.google.android.exoplayer2.Player.STATE_ENDED;
import static com.google.android.exoplayer2.Player.STATE_IDLE;
import static com.google.android.exoplayer2.Player.STATE_READY;

/**
 *
 * Created by farazhabib on 02/01/18.
 */

public class PostDetailsFragment extends BasePostFragment implements WatermarkAsyncResponse,
        OnAudioFocusChangeListener, OnAudioVolumeChangedListener {

    public static final String ARG_POST_DETAILS = "postDetails";
    public static final String ARG_REACT_ID = "react_id";

    @BindView(R.id.relative_layout) RelativeLayout relativeLayout;
    @BindView(R.id.tags_container) RelativeLayout tagsLayout;
    @BindView(R.id.progress_bar) DynamicProgress progressBar;
    @BindView(R.id.react_btn) ProximaNovaBoldButton reactBtn;
    @BindView(R.id.like) ProximaNovaRegularCheckedTextView likeBtn;
    @BindView(R.id.no_tagged_users) ProximaNovaRegularTextView noTaggedUsers;
    @BindView(R.id.tagged_user_list) RecyclerView taggedUserListView;
    @BindView(R.id.horizontal_list_view_parent) RelativeLayout horizontalListViewParent;
    @BindView(R.id.tags_badge) ProximaNovaSemiBoldTextView tagsCountBadge;
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.post_load_error) ProximaNovaBoldTextView postLoadErrorTextView;
    @BindView(R.id.reactions_header) ProximaNovaBoldTextView reactionsHeader;
    @BindView(R.id.post_load_error_subtitle) ProximaNovaRegularTextView postLoadErrorSubtitle;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;
    @BindView(R.id.video_view) SimpleExoPlayerView playerView;
    @BindView(R.id.controls) FrameLayout controlsContainer;
    @BindView(R.id.media_controller_caption) ProximaNovaSemiBoldTextView caption;
    @BindView(R.id.media_controller_location) ProximaNovaRegularTextView locationView;
    @BindView(R.id.media_controller_eta) ProximaNovaRegularTextView remainingTime;
    @BindView(R.id.media_controller_play_pause) AppCompatImageButton playPauseButton;
    @BindView(R.id.media_controller_dp) CircularAppCompatImageView profilePic;
    @BindView(R.id.media_controller_name) ProximaNovaRegularTextView profileNameView;
    @BindView(R.id.media_controller_likes) ProximaNovaRegularTextView likesView;
    @BindView(R.id.media_controller_views) ProximaNovaRegularTextView viewsView;
    @BindView(R.id.media_controller_categories) ProximaNovaSemiBoldTextView categoriesView;
    @BindView(R.id.media_controller_reaction_count) ProximaNovaSemiBoldTextView reactionCountView;
    @BindView(R.id.media_controller_reaction_1) CircularAppCompatImageView reaction1Pic;
    @BindView(R.id.media_controller_reaction_2) CircularAppCompatImageView reaction2Pic;
    @BindView(R.id.media_controller_reaction_3) CircularAppCompatImageView reaction3Pic;
    @BindView(R.id.loader) DynamicProgress loader;
    @BindView(R.id.upload_progress_text) ProximaNovaSemiBoldTextView uploadProgressText;
    @BindView(R.id.video_upload_progress) ProgressBar uploadProgress;
    @BindView(R.id.uploading_status_layout) RelativeLayout uploadingStatusLayout;
    @BindView(R.id.liked_user_layout) FrameLayout frameLayout;

    private static boolean isDeepLink = false;
    private int currentVolume;
    private boolean isAudioEnabled;
    private long totalDuration;
    private boolean oneShotFlag;
    private boolean audioAccessGranted = false;
    public long playerCurrentPosition = 0;

    private Handler customHandler = new Handler();
    private Handler mHandler;

    private SimpleExoPlayer player;
    private PostDetails postDetails;
    private PostReactionAdapter postReactionAdapter;
    private TagListAdapter tagListAdapter;
    private AudioManager audioManager;
    private ReactionUploadReceiver reactionUploadReceiver;
    private int taggedUsersPage;
    private boolean taggedUsersIsNextPage;
    private Runnable mDelayedStopRunnable = new Runnable() {
        @Override
        public void run() {
            if (player != null) player.setPlayWhenReady(false);
        }
    };

    public PostDetailsFragment() {}

    public static PostDetailsFragment newInstance(@NonNull PostDetails postDetails, boolean isDeepLink, String react_id) {
        PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_POST_DETAILS, postDetails);
        if (isDeepLink) {
            PostDetailsFragment.isDeepLink = true;
            bundle.putString(ARG_REACT_ID, react_id);
        }
        postDetailsFragment.setArguments(bundle);
        return postDetailsFragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            postDetails = bundle.getParcelable(ARG_POST_DETAILS);
            if (isDeepLink) {
                String reactId = bundle.getString(ARG_REACT_ID);
                if (reactId != null) {
                    fetchReactionDetails(this, Integer.parseInt(reactId));
                }
            }
        }
        taggedUsersPage = 1;
        audioManager = (AudioManager) getParentActivity().getSystemService(Context.AUDIO_SERVICE);
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        ButterKnife.bind(this, view);

        playerView.setShutterBackground(postDetails.getMedias().get(0).getThumbUrl());

        setupServiceReceiver();
        categoriesView.setSelected(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.loadPostDetailsApiCall(postDetails.getPostId());
        oneShotFlag = true;

        updateTextureViewSize(postDetails.getMedias().get(0).getMediaDimension().getWidth(),
                postDetails.getMedias().get(0).getMediaDimension().getHeight());

        likeAction(postDetails.canLike(), false);
        if (!postDetails.canReact()) disableView(reactBtn, true);

        prepareViews();
        prepareRecyclerViews();

        //audio state change listener
        mHandler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    getCoachMark(getParentActivity(), PostDetailsFragment.this, reactBtn, "postDetails",
                            R.string.react, R.string.coach_mark_post_details_body, R.string.okay_got_it, TYPE_POST_DETAILS);
                }
            }
        }, COACH_MARK_DELAY);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getParentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //acquire audio play access(transient)
        audioAccessGranted = acquireAudioLock(getTheContext(), this);

        checkIfAnyReactionIsUploading();

        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }

        if (currentVolume <= 0 && audioManager != null) {
            currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        isAudioEnabled = currentVolume > 0;
    }

    private void prepareViews() {
        if (postDetails != null) {
            String title = postDetails.getTitle();
            caption.setText(decodeUnicodeString(title));

            locationView.setVisibility(postDetails.getCheckIn() != null ? VISIBLE : GONE);
            locationView.setText(locationView.getVisibility() == VISIBLE ?
                    (postDetails.hasCheckin() && postDetails.getCheckIn() != null ?
                            postDetails.getCheckIn().getLocation() : null) : null);
            profileNameView.setText(postDetails.getPostOwner().getUserName());

            String likes = String.valueOf(this.postDetails.getLikes());
            likesView.setText(likes);

            String views = String.valueOf(this.postDetails.getMedias().get(0).getViews());
            viewsView.setText(views);

            String categories = getUserCategories();
            if (categories != null && !categories.isEmpty()) {
                categoriesView.setVisibility(VISIBLE);
                categoriesView.setText(categories);
            } else categoriesView.setVisibility(GONE);

            try {
                String duration = postDetails.getMedias().get(0).getDuration();
                remainingTime.setText(duration);
                setTextViewDrawableStart(remainingTime,
                        audioManager.getStreamVolume(STREAM_MUSIC) == 0 ?
                                R.drawable.ic_volume_off :
                                R.drawable.ic_volume_up);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:ss", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = sdf.parse("1970-01-01 " + duration);
                totalDuration = date.getTime();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            if (postDetails.getTotalReactions() > 3) {
                reactionCountView.setVisibility(VISIBLE);
                String reactionCountText = "+" + String.valueOf(postDetails.getTotalReactions() - 3) + " R";
                reactionCountView.setText(reactionCountText);
            }
            else reactionCountView.setVisibility(GONE);

            tagsCountBadge.setVisibility(postDetails.getTotalTags() != null && postDetails.getTotalTags() > 0 ? VISIBLE : GONE);
            tagsLayout.setVisibility(postDetails.getTotalTags() != null && postDetails.getTotalTags() > 0 ? VISIBLE : GONE);
            if (postDetails.getTotalTags() != null) tagsCountBadge.setText(String.valueOf(postDetails.getTotalTags()));

            Glide.with(this)
                    .load(postDetails.getPostOwner().getProfileMedia() != null ?
                            postDetails.getPostOwner().getProfileMedia().getThumbUrl() : "")
                    .apply(RequestOptions.bitmapTransform(new FitCenter()).placeholder(R.drawable.ic_user_male_dp_small))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                       DataSource dataSource, boolean isFirstResource) {
                            profilePic.setImageDrawable(resource);
                            return false;
                        }
                    })
                    .into(profilePic);
            controlsContainer.setVisibility(VISIBLE);
        }
    }

    private void prepareRecyclerViews() {
//        Preparing reactions list
        CustomStaggeredGridLayoutManager manager1 = new CustomStaggeredGridLayoutManager(2, VERTICAL);
        manager1.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager1);
        postReactionAdapter = new PostReactionAdapter(this);
        recyclerView.setAdapter(postReactionAdapter);
        EndlessRecyclerViewScrollListener scrollListener1 = new EndlessRecyclerViewScrollListener(manager1) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isConnected(context)) {
                    if (is_next_page) {
                        currentPage = page;
                        viewModel.loadReactionsOfPostApiCall(postDetails.getPostId(), page);
                    }
                }
                else Toast.makeText(getTheContext(), R.string.could_not_load_new_posts, Toast.LENGTH_SHORT).show();
            }
        };
        recyclerView.addOnScrollListener(scrollListener1);
        viewModel.loadReactionsOfPostApiCall(postDetails.getPostId(), 1);

//        Preparing tagged users list
        CustomLinearLayoutManager manager2 = new CustomLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        taggedUserListView.setLayoutManager(manager2);
        tagListAdapter = new TagListAdapter(this);
        taggedUserListView.setAdapter(tagListAdapter);
        EndlessRecyclerViewScrollListener scrollListener2 = new EndlessRecyclerViewScrollListener(manager2) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isConnected(context)) {
                    if (taggedUsersIsNextPage) {
                        taggedUsersPage = page;
                        viewModel.loadTaggedUsersApiCall(postDetails.getPostId(), page);
                    }
                }
                else Toast.makeText(getTheContext(), R.string.could_not_load_new_posts, Toast.LENGTH_SHORT).show();
            }
        };
        taggedUserListView.addOnScrollListener(scrollListener2);
        viewModel.loadTaggedUsersApiCall(postDetails.getPostId(), 1);
    }

    @SuppressLint("SwitchIntDef") @Override
    protected void handleResponse(BaseModel resultObject) {
        try {
            switch (resultObject.getCallType()) {
                case CALL_GET_POST_DETAILS:
                    if (resultObject instanceof PostDetails) {
                        postDetails = (PostDetails) resultObject;
                        prepareViews();
                    }
                    break;
                case CALL_INCREMENT_VIEW_COUNT:
                    incrementViews();
                    viewModel.incrementLocalViews(postDetails.getMedias(), postDetails.getPostId());
                    break;
                case CALL_LIKE_DISLIKE_POST:
                    if (resultObject instanceof ResultObject && ((ResultObject) resultObject).getStatus() &&
                            ((ResultObject) resultObject).getMessage().contains("Unlike"))
                        viewModel.dislikeLocalPost(postDetails.getPostId());
                    else viewModel.likeLocalPost(postDetails.getPostId());
                    break;
                case CALL_GET_REACTIONS_OF_POST:
                    if (resultObject instanceof ReactionsList) {
                        ReactionsList reactionsList = (ReactionsList) resultObject;
                        if (reactionsList.getReactions() != null && !reactionsList.getReactions().isEmpty()) {
    //                        viewModel.updateLocalReactions(reactionsList.getReactions(), postDetails.canReact(), postDetails.getPostId());
                            postLoadErrorLayout.setVisibility(GONE);
                            is_next_page = reactionsList.isNextPage();
                            postReactionAdapter.updateReactions(reactionsList.getReactions());
                            if (reactionsList.getReactions().size() > 0) {
                                if (reactionsList.getReactions().size() >= 1 &&
                                        reactionsList.getReactions().get(0).getOwner().getProfileMedia() != null)
                                    setReactionPic(reactionsList.getReactions().get(0).getOwner().getProfileMedia().getThumbUrl(), reaction1Pic);
                                if (reactionsList.getReactions().size() >= 2 &&
                                        reactionsList.getReactions().get(1).getOwner().getProfileMedia() != null)
                                    setReactionPic(reactionsList.getReactions().get(1).getOwner().getProfileMedia().getThumbUrl(), reaction2Pic);
                                if (reactionsList.getReactions().size() >= 3 &&
                                        reactionsList.getReactions().get(2).getOwner().getProfileMedia() != null)
                                    setReactionPic(reactionsList.getReactions().get(2).getOwner().getProfileMedia().getThumbUrl(), reaction3Pic);
                            }
                        } else {
                            setNoReactions();
                            showNoReactionMessage();
                        }
                    }
                    break;
                case CALL_GET_TAGGED_USERS:
                    if (resultObject instanceof TaggedUsersList) {
                        TaggedUsersList taggedList = (TaggedUsersList) resultObject;
                        taggedUsersIsNextPage = taggedList.isNextPage();
                        tagListAdapter.addPosts(taggedUsersPage, taggedList.getTaggedUsers());
                    }
                    break;
                case CALL_HIDE_OR_SHOW_POST:
                    if (resultObject instanceof ResultObject && ((ResultObject) resultObject).getStatus()) {
                        Toast.makeText(context, R.string.video_hide_successful, Toast.LENGTH_SHORT).show();
                        viewModel.deleteLocalPost(postDetails.getPostId());
                        deleteCachedMedia();
                        navigation.popFragment();
                    } else Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    break;
                case CALL_DELETE_POST:
                    if (resultObject instanceof ResultObject && ((ResultObject) resultObject).getStatus()) {
                        Toast.makeText(context, R.string.video_has_been_deleted, Toast.LENGTH_SHORT).show();
                        viewModel.deleteLocalPost(postDetails.getPostId());
                        deleteCachedMedia();
                        navigation.popFragment();
                    } else Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    break;
                case CALL_CREATE_REACTION_BY_GIPHY:
                    viewModel.loadReactionsOfPostApiCall(postDetails.getPostId(), 1);
                    break;
                default:
                    break;
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SwitchIntDef") @Override
    protected void handleError(BaseModel baseModel) {
        switch (baseModel.getCallType()) {
            case CALL_GET_REACTIONS_OF_POST:
                showErrorMessage(R.string.could_not_load_reactions);
                break;
            case CALL_GET_TAGGED_USERS:
                noTaggedUsers.setVisibility(VISIBLE);
                taggedUserListView.setVisibility(GONE);
                noTaggedUsers.setText(R.string.error_getting_tagged_users);
                break;
            case CALL_HIDE_OR_SHOW_POST:
                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                break;
            case CALL_DELETE_POST:
                Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                break;
            case CALL_CREATE_REACTION_BY_GIPHY:
                break;
            default:
                break;
        }
    }

    private void deleteCachedMedia() {
        deleteMedia(context, postDetails.getMedias().get(0).getMediaUrl());
        deleteMedia(context, postDetails.getMedias().get(0).getThumbUrl());
    }

    private void showErrorMessage(@StringRes int message) {
        postLoadErrorLayout.setVisibility(VISIBLE);
        postLoadErrorTextView.setText(message);
        postLoadErrorSubtitle.setText(R.string.tap_to_retry);
    }

    @Nullable private String getUserCategories() {
        if (postDetails.getCategories() != null) {
            StringBuilder categories = new StringBuilder();
//            categories.append(postDetails.getCategories().size() > 1 ? "Categories : " : "Category : ");
            for (int i = 0; i < postDetails.getCategories().size(); i++) {
                categories.append(postDetails.getCategories().get(i).getCategoryName());
                if (i < postDetails.getCategories().size() - 1)
                    categories.append(", ");
//                categories.append("<font color='"+postDetails.getCategories().get(i).getColor()+"'>"+
//                postDetails.getCategories().get(i).getCategoryName()+"</font> ");
//                categories.append("<font color='"+postDetails.getCategories().get(i).getColor()+"'>"+
//                postDetails.getCategories().get(i).getCategoryName()+"</font> ");
            }
//            categoriesView.setText(Html.fromHtml(categories.toString()), TextView.BufferType.SPANNABLE);
            return categories.toString();
        } else return null;
    }

    private void showNoReactionMessage() {
        reactionsHeader.setVisibility(GONE);
        postLoadErrorLayout.animate().alpha(1).setDuration(280).start();
        postLoadErrorLayout.setVisibility(VISIBLE);
        postLoadErrorSubtitle.setText(postDetails.canDelete() ? R.string.there_is_no_reaction_yet : R.string.be_the_first_one_to_react);
    }

    public void incrementLikes() {
        postDetails.setLikes(postDetails.getLikes() + 1);
        String likesText = String.valueOf(postDetails.getLikes());
        likesView.setText(likesText);
    }

    public void decrementLikes() {
        postDetails.setLikes(postDetails.getLikes() - 1);
        String likesText = String.valueOf(postDetails.getLikes());
        likesView.setText(likesText);
    }

    public void incrementViews() {
        postDetails.getMedias().get(0).setViews(postDetails.getMedias().get(0).getViews() + 1);
        String viewsText = String.valueOf(postDetails.getMedias().get(0).getViews());
        viewsView.setText(viewsText);
    }

    public void setReactionPic(String reactionPicUrl, final AppCompatImageView view) {
        view.setVisibility(VISIBLE);
        Glide.with(this)
                .load(reactionPicUrl)
                .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        view.setImageDrawable(resource);
                        return false;
                    }
                })
                .into(view);
    }

    public void setNoReactions() {
        reactionCountView.setText("");
    }

    @OnClick(R.id.media_controller_likes) public void likeClicked() {
        if(postDetails.getLikes() > 0) {
            navigation.pushFragment(FragmentLikedUser.newInstance(postDetails.getPostId(), LIKED_USERS_OF_POSTS));
        }
    }

    @OnClick(R.id.media_controller_dp) public void profilePicClicked() {
        openProfile(navigation, postDetails.canDelete(), postDetails.getPostOwner().getUserId());
    }

    @OnClick(R.id.media_controller_eta) public void toggleSound() {
        if (audioManager != null) {
            int midVolume = (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) / 2;
            int volume;
            if (isAudioEnabled) {
                volume = 0;
                isAudioEnabled = false;
            } else {
                if (currentVolume > 0)
                    volume = currentVolume;
                else volume = midVolume;
                isAudioEnabled = true;
            }
            setVolumeButton(volume);
            if (volume > midVolume) {
                volume = midVolume;
            }
            if (volume < 0) {
                volume = 0;
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }

    @OnClick(R.id.react_btn) public void react() {
        launchReactionCamera(context, postDetails.getPostId());
    }

    @OnClick(R.id.like) public void likePost() {
        viewModel.likeDislikePostApiCall(postDetails.getPostId(), !likeBtn.isChecked() ? SEND_LIKE : SEND_DISLIKE);
        likeAction(likeBtn.isChecked(), true);
    }

    @OnClick(R.id.tags) public void getTaggedList() {
        if (horizontalListViewParent.getVisibility() == GONE) {
            horizontalListViewParent.setVisibility(VISIBLE);
        } else {
            horizontalListViewParent.setVisibility(GONE);
        }
    }

    @OnClick(R.id.menu) public void showMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(context, anchor);
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener());
        if (postDetails.canDelete())
            popupMenu.inflate(R.menu.menu_post_self);
        else
            popupMenu.inflate(R.menu.menu_post_others);
        popupMenu.show();
    }

    @OnClick({R.id.controls, R.id.media_controller_play_pause}) public void toggleMediaControllerVisibility() {
        try {
            player.setPlayWhenReady(!player.getPlayWhenReady());
            togglePlayPauseBtnVisibility(!player.getPlayWhenReady());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.share) public void onViewClicked() {
        loader.setVisibility(VISIBLE);

        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(String.valueOf(postDetails.getPostOwner().getUserId()))
                .setTitle(postDetails.getTitle())
                .setContentDescription("View this awesome video on Teazer app")
                .setContentImageUrl(postDetails.getMedias().get(0).getThumbUrl());

        LinkProperties linkProperties = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing")
                .addControlParameter("post_id", String.valueOf(postDetails.getPostId()))
                .addControlParameter("$desktop_url", "https://teazer.online/")
                .addControlParameter("$ios_url", "https://teazer.online/");

        branchUniversalObject.generateShortUrl(context, linkProperties, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    //fabric event
                    logVideoShareEvent("Branch", postDetails.getTitle(), "Post", String.valueOf(postDetails.getPostId()));
                    loader.setVisibility(GONE);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                }
                else loader.setVisibility(GONE);
            }
        });
    }

    private void likeAction(boolean isChecked, boolean animate) {
        if (!isChecked) {
            likeBtn.setChecked(true);
            likeBtn.setText(R.string.liked);
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_filled, 0, 0);
            if (animate) {
                likeBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.selected));
                incrementLikes();
            }
        } else {
            likeBtn.setChecked(false);
            likeBtn.setText(R.string.like);
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_likebig, 0, 0);
            if (animate) {
                likeBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.selected));
                decrementLikes();
            }
        }
    }

    private void updateTextureViewSize(int viewWidth, int viewHeight) {
        Point point = new Point();
        getParentActivity().getWindowManager().getDefaultDisplay().getSize(point);
        int systemWidth = point.x;
        viewHeight = systemWidth * viewHeight / viewWidth;
        viewWidth = systemWidth;
        if (viewHeight < viewWidth) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
            //noinspection SuspiciousNameCombination
            params.height = viewWidth;
            relativeLayout.setLayoutParams(params);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        playerView.setLayoutParams(params);
        playerView.setResizeMode(RESIZE_MODE_ZOOM);
    }

    @Override public void onAudioFocusChange(int focusChange) {
        if (player != null) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // Permanent loss of audio focus. Pause playback immediately
                player.setPlayWhenReady(false);
                // Wait 30 seconds before stopping playback
                mHandler.postDelayed(mDelayedStopRunnable, TimeUnit.SECONDS.toMillis(30));
            }
            else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
                // Pause playback
                player.setPlayWhenReady(false);
            }
//            else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
//                Lower the volume, keep playing
//            }
            else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // Your app has been granted audio focus again. Raise volume to normal, restart playback if necessary
                player.setPlayWhenReady(true);
                player.seekTo(playerCurrentPosition);
            }
        }
    }

    @Override
    public void initialVolume(int currentVolume) {
        setVolumeButton(currentVolume);
    }

    @Override
    public void onVolumeChanged(int currentVolume) {
        setVolumeButton(currentVolume);
    }

    private void setVolumeButton(int currentVolume) {
        setTextViewDrawableStart(remainingTime, currentVolume > 0 ?
                R.drawable.ic_volume_up :
                R.drawable.ic_volume_off);
    }

    private class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_hide:
                    showAlertDialog(R.string.hiding_post, R.string.hide_post_confirm);
                    return true;
                case R.id.action_delete:
                    showAlertDialog(R.string.alert, R.string.delete_post_confirm);
                    return true;
                case R.id.action_profile_report:
                    if (!postDetails.canDelete()) {
                        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                        ReportPostDialogFragment reportPostDialogFragment = ReportPostDialogFragment.
                                newInstance(postDetails.getPostId(), postDetails.canReact(), postDetails.getPostOwner().getUserName());
                        // SETS the target fragment for use later when sending results
//                        reportPostDialogFragment.setTargetFragment(PostDetailsActivity.this, 301);
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

    private void showAlertDialog(@StringRes int title, @StringRes final int message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (message) {
                            case R.string.hide_post_confirm:
                                viewModel.hideOrShowPostApiCall(postDetails.getPostId(), HIDE);
                                break;
                            case R.string.delete_post_confirm:
                                viewModel.deletePostApiCall(postDetails.getPostId());
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
//        getParentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        playerCurrentPosition = player.getCurrentPosition();
        player.setPlayWhenReady(!player.getPlayWhenReady());

        if (Util.SDK_INT <= 23) {
            customHandler.removeCallbacks(updateTimerThread);
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        postReactionAdapter = null;
        customHandler.removeCallbacks(updateTimerThread);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            //acquire audio play access(transient)
            audioAccessGranted = acquireAudioLock(getTheContext(), this);
            initializePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            customHandler.removeCallbacks(updateTimerThread);
            releasePlayer();
        }
        releaseAudioLock(getTheContext(), this);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void togglePlayPauseBtnVisibility(boolean visible) {
        playPauseButton.animate()
                .scaleY(visible ? 1 : 0)
                .scaleX(visible ? 1 : 0)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void initializePlayer() {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            player.addListener(new PlayerEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    switch (playbackState) {
                        case STATE_IDLE:
                            togglePlayPauseBtnVisibility(true);
                            break;
                        case STATE_BUFFERING:
                            togglePlayPauseBtnVisibility(false);
                            progressBar.setVisibility(VISIBLE);
                            break;
                        case STATE_READY:
                            if (player.getPlayWhenReady()) {
                                if (playPauseButton.getVisibility() == VISIBLE)
                                    togglePlayPauseBtnVisibility(false);
                                progressBar.setVisibility(GONE);
                                customHandler.postDelayed(updateTimerThread, 0);
                            }
                            if (oneShotFlag) {
                                oneShotFlag = false;
                                if (!postDetails.canDelete()) {
                                    viewModel.incrementViewCountApiCall(postDetails.getMedias().get(0).getMediaId());
                                }
                            }
                            break;
                        case STATE_ENDED:
                            break;
                        default:
                            break;
                    }
                }
            });

            playerView.setPlayer(player);
            String remoteMediaUrl = postDetails.getMedias().get(0).getMediaUrl();
            String savedMediaPath = getMedia(context, remoteMediaUrl);
            player.prepare(new LoopingMediaSource(
                    new ExtractorMediaSource(Uri.parse(savedMediaPath != null && !savedMediaPath.equals("null") &&
                    new File(savedMediaPath).exists() ? savedMediaPath : remoteMediaUrl),
                    new DefaultHttpDataSourceFactory("exoplayer_video"),
                    new DefaultExtractorsFactory(), null, null)));
            if (audioAccessGranted) {
                player.setPlayWhenReady(true);
                player.seekTo(player.getCurrentWindowIndex(), playerCurrentPosition);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //<editor-fold desc="Video upload handler">
    private void setupServiceReceiver() {
        reactionUploadReceiver = new ReactionUploadReceiver(new Handler())
                .setReceiver(new ReactionUploadReceiver.Receiver() {
                    @Override
                    public void onReceiverResult(int resultCode, Bundle resultData) {
                        switch (resultCode) {
                            case UPLOAD_IN_PROGRESS_CODE:
                                uploadingStatusLayout.setVisibility(VISIBLE);
                                uploadProgressText.setText(String.valueOf("Uploading... " + resultData.getInt(UPLOAD_PROGRESS) + "%"));
                                uploadProgress.setProgress(resultData.getInt(UPLOAD_PROGRESS));

                                if (reactBtn.isEnabled())
                                    disableView(reactBtn, true);
                                break;
                            case UPLOAD_COMPLETE_CODE:
                                uploadProgressText.setText(R.string.finished);
                                uploadProgress.setVisibility(GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadingStatusLayout.setVisibility(GONE);
                                    }
                                }, 2000);

                                disableView(reactBtn, true);

                                finishReactionUploadSession(context);
                                viewModel.loadReactionsOfPostApiCall(postDetails.getPostId(), 1);
                                //add watermark for local creations/reactions
                                if(resultData.getBoolean(ADD_WATERMARK)) {
                                    AddWaterMarkAsyncTask addWaterMarkAsyncTask = new AddWaterMarkAsyncTask(getActivity());
                                    addWaterMarkAsyncTask.delegate = PostDetailsFragment.this;
                                    addWaterMarkAsyncTask.execute(resultData.getString(VIDEO_PATH));
                                }
                                else
                                    deleteFilePermanently(resultData.getString(VIDEO_PATH));

                                break;
                            case UPLOAD_ERROR_CODE:
                                uploadProgressText.setText(R.string.failed);
                                uploadProgress.setVisibility(GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadingStatusLayout.setVisibility(GONE);
                                    }
                                }, 2000);

                                enableView(reactBtn);

                                finishReactionUploadSession(context);
                                break;
                            case REQUEST_CANCEL_UPLOAD:
                                Toast.makeText(context, "cancelled", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    @Override
    public void waterMarkProcessFinish(String destinationPath, String sourcePath) {
        deleteFilePermanently(sourcePath);
    }

    private void checkIfAnyReactionIsUploading() {
        UploadParams uploadParams = getReactionUploadSession(context);
        if (uploadParams != null) {
            finishReactionUploadSession(context);
            disableView(reactBtn, true);

            if (!uploadParams.isGiphy()) {
                launchReactionUploadService(context, uploadParams, reactionUploadReceiver);
            } else {
                postGiphyReaction(uploadParams);
            }
        }
    }

    private void postGiphyReaction(UploadParams uploadParams) {
        viewModel.createReactionByGiphyApiCall(
                new GiphyReactionRequest(uploadParams.getPostId(), uploadParams.getTitle(), uploadParams.getVideoPath()));
    }
    //</editor-fold>

    public PostDetails getPostDetails() {
        return postDetails;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            releaseAudioLock(getTheContext(), this);
            mHandler.removeCallbacks(mDelayedStopRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        customHandler.removeCallbacks(updateTimerThread);
        releaseAudioLock(getTheContext(), this);
        //  onBackPressed();
        playerCurrentPosition = 0;
        mHandler.removeCallbacks(mDelayedStopRunnable);
    }

    //thread to update recording time
    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            try {
                long timeInMilliseconds = totalDuration - player.getCurrentPosition();
                String duration = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds),
                        TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds)) + 1);
                remainingTime.setText(duration);
                customHandler.postDelayed(this, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
}