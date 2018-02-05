package com.cncoding.teazer.home.post;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.CustomStaggeredGridLayoutManager;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldButton;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.base.TaggedUser;
import com.cncoding.teazer.model.base.UploadParams;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.post.PostReactionsList;
import com.cncoding.teazer.model.post.TaggedUsersList;
import com.cncoding.teazer.model.react.GiphyReactionRequest;
import com.cncoding.teazer.services.receivers.ReactionUploadReceiver;
import com.cncoding.teazer.ui.fragment.fragment.ReportPostDialogFragment;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import pl.droidsonroids.gif.GifTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static android.util.DisplayMetrics.DENSITY_HIGH;
import static android.util.DisplayMetrics.DENSITY_MEDIUM;
import static android.util.DisplayMetrics.DENSITY_XHIGH;
import static android.util.DisplayMetrics.DENSITY_XXHIGH;
import static android.util.DisplayMetrics.DENSITY_XXXHIGH;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.BaseBottomBarActivity.COACH_MARK_DELAY;
import static com.cncoding.teazer.BaseBottomBarActivity.REQUEST_CANCEL_UPLOAD;
import static com.cncoding.teazer.customViews.coachMark.MaterialShowcaseView.TYPE_POST_DETAILS;
import static com.cncoding.teazer.services.ReactionUploadService.launchReactionUploadService;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_COMPLETE_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_ERROR_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_IN_PROGRESS_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_PROGRESS;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.CommonWebServicesUtil.fetchReactionDetails;
import static com.cncoding.teazer.utilities.FabricAnalyticsUtil.logVideoShareEvent;
import static com.cncoding.teazer.utilities.MediaUtils.acquireAudioLock;
import static com.cncoding.teazer.utilities.MediaUtils.releaseAudioLock;
import static com.cncoding.teazer.utilities.SharedPrefs.finishReactionUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.getReactionUploadSession;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.ViewUtils.enableView;
import static com.cncoding.teazer.utilities.ViewUtils.getCoachMark;
import static com.cncoding.teazer.utilities.ViewUtils.launchReactionCamera;
import static com.cncoding.teazer.utilities.ViewUtils.setTextViewDrawableStart;
import static com.google.android.exoplayer2.ExoPlayer.STATE_BUFFERING;
import static com.google.android.exoplayer2.ExoPlayer.STATE_ENDED;
import static com.google.android.exoplayer2.ExoPlayer.STATE_IDLE;
import static com.google.android.exoplayer2.ExoPlayer.STATE_READY;

/**
 *
 * Created by farazhabib on 02/01/18.
 */

public class FragmentPostDetails extends BaseFragment implements
        AudioManager.OnAudioFocusChangeListener {

    public static final String SPACE = "  ";
    public static final String ARG_POST_DETAILS = "postDetails";
    public static final String ARG_THUMBNAIL = "thumbnail";
    public static final String ARG_REACT_ID = "react_id";
    //    private static final String ARG_ENABLE_REACT_BTN = "enableReactBtn";
    public static final String ARG_IS_COMING_FROM_HOME_PAGE = "isComingFromHomePage";
    CallProfileFromPostDetails callProfileFromPostDetails;

    //<editor-fold desc="Main layout views">
//    @BindView(R.id.root_layout) NestedScrollView nestedScrollView;
//    @BindView(R.id.video_container) RelativeLayout videoContainer;

    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.tags_container)
    RelativeLayout tagsLayout;
    @BindView(R.id.placeholder)
    ImageView placeholder;
    @BindView(R.id.loading)
    ProgressBar loadingProgressBar;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.react_btn)
    ProximaNovaBoldButton reactBtn;
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
    //    @BindView(R.id.menu) ProximaNovaRegularTextView menu;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    @BindView(R.id.post_load_error)
    ProximaNovaBoldTextView postLoadErrorTextView;
    @BindView(R.id.reactions_header)
    ProximaNovaBoldTextView reactionsHeader;
    @BindView(R.id.post_load_error_subtitle)
    ProximaNovaRegularTextView postLoadErrorSubtitle;
    @BindView(R.id.post_load_error_layout)
    LinearLayout postLoadErrorLayout;
    //    @BindView(R.id.share) ProximaNovaRegularTextView share;
    @BindView(R.id.video_view)
    SimpleExoPlayerView playerView;
    //</editor-fold>

    //<editor-fold desc="Controller views">
    @BindView(R.id.controls)
    FrameLayout controlsContainer;
    //top layout
    @BindView(R.id.media_controller_caption)
    ProximaNovaSemiboldTextView caption;
    @BindView(R.id.media_controller_location)
    ProximaNovaRegularTextView locationView;
    @BindView(R.id.media_controller_eta)
    ProximaNovaRegularTextView remainingTime;
    //center layout
    @BindView(R.id.media_controller_play_pause)
    AppCompatImageButton playPauseButton;
    //bottom layout
    @BindView(R.id.media_controller_dp)
    CircularAppCompatImageView profilePic;
    @BindView(R.id.media_controller_name)
    ProximaNovaRegularTextView profileNameView;
    @BindView(R.id.media_controller_likes)
    ProximaNovaRegularTextView likesView;
    @BindView(R.id.media_controller_views)
    ProximaNovaRegularTextView viewsView;
    @BindView(R.id.media_controller_categories)
    ProximaNovaSemiboldTextView categoriesView;
    @BindView(R.id.media_controller_reaction_count)
    ProximaNovaSemiboldTextView reactionCountView;
    @BindView(R.id.media_controller_reaction_1)
    CircularAppCompatImageView reaction1Pic;
    @BindView(R.id.media_controller_reaction_2)
    CircularAppCompatImageView reaction2Pic;
    @BindView(R.id.media_controller_reaction_3)
    CircularAppCompatImageView reaction3Pic;
    @BindView(R.id.loader)
    GifTextView loader;
    @BindView(R.id.uploadProgressText)
    ProximaNovaSemiboldTextView uploadProgressText;
    @BindView(R.id.uploadProgress)
    ProgressBar uploadProgress;
    @BindView(R.id.uploadingStatusLayout)
    RelativeLayout uploadingStatusLayout;
    public static final String USER_PROFILE = "userprofile";
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
    private long totalDuration;
    private boolean oneShotFlag;
    public static boolean isPostDetailActivity = false;
    @BindView(R.id.liked_user_layout)
    FrameLayout frameLayout;
    Context context;

    //    StartCountDownClass startCountDownClass;
    private Handler customHandler = new Handler();
    //</editor-fold>

    //<editor-fold desc="Objects">
    private SimpleExoPlayer player;
    private PostDetails postDetails;
    private Call<PostReactionsList> postReactionsListCall;
    private Call<TaggedUsersList> taggedUsersListCall;
    private ArrayList<PostReaction> postReactions;
    private ArrayList<TaggedUser> taggedUsersList;
    private PostReactionAdapter postReactionAdapter;
    private AudioManager audioManager;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private ReactionUploadReceiver reactionUploadReceiver;
    private static boolean isDeepLink = false;
    private String thumbUrl;
    private String reactId;
    private static boolean isReactionPlayed = false;
    TaggedUsersList taggedList;
    //</editor-fold>

    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private boolean audioAccessGranted = false;
    public long playerCurrentPosition = 0;
    private Handler mHandler;
    private onPostOptionsClickListener mListener;

    public FragmentPostDetails() {

    }

    public static FragmentPostDetails newInstance(@NonNull PostDetails postDetails, byte[] image,
                                                  boolean isComingFromHomePage, boolean isDeepLink, String thumbUrl, String react_id) {

        FragmentPostDetails fragmentPostDetails = new FragmentPostDetails();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_POST_DETAILS, postDetails);
        if (!isDeepLink) {
            bundle.putByteArray(ARG_THUMBNAIL, image);
        } else {
            FragmentPostDetails.isDeepLink = true;
            bundle.putString(ARG_THUMBNAIL, thumbUrl);
            bundle.putString(ARG_REACT_ID, react_id);
        }
        bundle.putBoolean(ARG_IS_COMING_FROM_HOME_PAGE, isComingFromHomePage);
        fragmentPostDetails.setArguments(bundle);
        return fragmentPostDetails;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

//        logTheDensity();

//                getActivity().getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
//                     View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                       // View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |                                       // hide nav bar
//                        View.SYSTEM_UI_FLAG_FULLSCREEN |                                            // hide status bar
//                       View.SYSTEM_UI_FLAG_IMMERSIVE
//                );

        //   getActivity().setContentView(R.layout.activity_post_details);
        //   ButterKnife.bind(getActivity());

        View view = inflater.inflate(R.layout.fragment_post_details, container, false);

        ButterKnife.bind(this, view);

        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        setupServiceReceiver();
        categoriesView.setSelected(true);
        postReactions = new ArrayList<>();
        taggedUsersList = new ArrayList<>();
        previousTitle = getParentActivity().getToolbarTitle();

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callProfileFromPostDetails.callProfileListener(postDetails.getPostOwner().getUserId(), postDetails.canDelete());
            }
        });

        likesView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(likes>0) {
//                    ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
//                            .setCustomAnimations(abc_slide_in_bottom, abc_slide_out_top, abc_slide_in_top, abc_slide_out_bottom)
//                            .add(R.id.liked_user_layout, FragmentLikedUser.newInstance(postDetails))
//                            .addToBackStack("FragmentLikedUserPost")
//                            .commit();

                    mListener.onPostLikedClicked(postDetails);
                }
            }
        });

//        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                if (next) {
//                    if (identifier.equals("Other")) {
//                        if (page > 2) {
//                            loader.setVisibility(View.VISIBLE);
//                        }
//                        taggedUserListView(Integer.parseInt(followerid), page);
//                    } else {
//                        if (page > 2) {
//                            loader.setVisibility(View.VISIBLE);
//                        }
//                        getUserfollowerList(page);
//
//                    }
//                }
//
//            }
//        };
 //       recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        tagsCountBadge.setText(String.valueOf(postDetails.getTotalTags()));
        tagsCountBadge.setVisibility(postDetails.getTotalTags() == 0 ? GONE : VISIBLE);
        tagsLayout.setVisibility(postDetails.getTotalTags() == 0 ? GONE : VISIBLE);

        prepareController();

        postReactionAdapter = new PostReactionAdapter(postReactions, context);
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
        taggedUserListView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        taggedUserListView.setAdapter(new TagListAdapter(context, taggedUsersList,this));
        getTaggedUsers(1);

        //audio state change listener
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
                                player.seekTo(playerCurrentPosition);
                            }
                        }
                    }
                };
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAdded()) {
                    getCoachMark(getParentActivity(), FragmentPostDetails.this, reactBtn, "postDetails",
                            R.string.react, R.string.coach_mark_post_details_body, R.string.okay_got_it, TYPE_POST_DETAILS);
                }
            }
        }, COACH_MARK_DELAY);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        context = getContext();

        if (bundle != null) {
            postDetails = bundle.getParcelable(ARG_POST_DETAILS);
            if (!isDeepLink) {
                image = bundle.getByteArray(ARG_THUMBNAIL);
            }
            else {
                thumbUrl = bundle.getString(ARG_THUMBNAIL);
                reactId = bundle.getString(ARG_REACT_ID);
                isReactionPlayed = false;

                if (reactId != null) {
                    fetchReactionDetails(context, Integer.parseInt(reactId));
                }
            }
//            enableReactBtn = getIntent().getBooleanExtra(ARG_ENABLE_REACT_BTN, true);
            isComingFromHomePage = bundle.getBoolean(ARG_IS_COMING_FROM_HOME_PAGE, false);
        }

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    }

    private Runnable mDelayedStopRunnable = new Runnable() {
        @Override
        public void run() {
            if (player != null) {
                player.setPlayWhenReady(false);
            }
        }
    };

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

        getParentActivity().hideToolbar();
        //acquire audio play access(transient)
        audioAccessGranted = acquireAudioLock(getContext(), audioFocusChangeListener);

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
            String title = postDetails.getTitle();
            caption.setText(decodeUnicodeString(title));
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
                categories = "  " + categories;
                categoriesView.setText(categories);
            } else categoriesView.setVisibility(GONE);

            try {
                String duration = BLANK_SPACE + postDetails.getMedias().get(0).getDuration();
                remainingTime.setText(duration);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd mm:ss", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = sdf.parse("1970-01-01 " + duration);
                totalDuration = date.getTime();
            }
            catch (ParseException e) {

                e.printStackTrace();
            }

            if (postDetails.getTotalReactions() <= 3) {
                reactionCountView.setText(String.valueOf(postDetails.getTotalReactions()) + " R");
            } else {
                reactionCountView.setText("+" + String.valueOf(postDetails.getTotalReactions()-3) + " R");
            }
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
        }
    }

    private String getUserCategories() {
        if (postDetails.getCategories() != null) {
            StringBuilder categories = new StringBuilder();
            for (int i = 0; i < postDetails.getCategories().size(); i++) {
                categories.append(postDetails.getCategories().get(i).getCategoryName());
                if (i < postDetails.getCategories().size() - 1)
                    categories.append(", ");
//                categories.append("<font color='"+postDetails.getCategories().get(i).getColor()+"'>"+postDetails.getCategories().get(i).getCategoryName()+"</font> ");
//                categories.append("<font color='"+postDetails.getCategories().get(i).getColor()+"'>"+postDetails.getCategories().get(i).getCategoryName()+"</font> ");
            }
//            categoriesView.setText(Html.fromHtml(categories.toString()), TextView.BufferType.SPANNABLE);
            return categories.toString();
        } else return null;
    }

    private void getPostReactions(final int postId, final int pageNumber) {
        postReactionsListCall = ApiCallingService.Posts.getReactionsOfPost(postId, pageNumber, context);

        if (postReactionsListCall != null && !postReactionsListCall.isExecuted())
            postReactionsListCall.enqueue(new Callback<PostReactionsList>() {
                @Override
                public void onResponse(Call<PostReactionsList> call, Response<PostReactionsList> response) {
                    try {
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
                    } catch (Exception e) {
                        e.printStackTrace();
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
        //  postLoadErrorTextView.setText(R.string.no_reactions_yet);
        if (postDetails.canDelete()) {
            postLoadErrorSubtitle.setText(R.string.there_is_no_reaction_yet);

        } else {
            postLoadErrorSubtitle.setText(R.string.be_the_first_one_to_react);

        }
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
        String likesText = FragmentPostDetails.SPACE + ++likes;
        likesView.setText(likesText);
    }

    public void decrementLikes() {
        likes -= likes;
        if (likes < 0) likes = 0;
        String likesText = FragmentPostDetails.SPACE + likes;
        likesView.setText(likesText);
    }

    public void incrementViews() {
        String viewsText = FragmentPostDetails.SPACE + ++views;
        viewsView.setText(viewsText);
    }

    public void decrementViews() {
        String viewsText = FragmentPostDetails.SPACE + --views;
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

    @OnClick(R.id.btnClose)
    public void goBack() {
        customHandler.removeCallbacks(updateTimerThread);
        mHandler.removeCallbacks(mDelayedStopRunnable);
        playerCurrentPosition = 0;
        getParentActivity().onBackPressed();

    }

    @OnClick(R.id.media_controller_eta)
    public void toggleSound() {
        if (audioManager != null) {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

            int volume;

            if (isAudioEnabled) {
                volume = 0;
                setTextViewDrawableStart(remainingTime, R.drawable.ic_volumeoff);
                isAudioEnabled = false;
            } else {
                if (currentVolume > 0)
                    volume = currentVolume;
                else volume = maxVolume;
//                volume = 100 * maxVolume + currentVolume;
                setTextViewDrawableStart(remainingTime, R.drawable.ic_volumeon);
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

    @OnClick(R.id.react_btn)
    public void react() {
        launchReactionCamera(context, postDetails);
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
                t.printStackTrace();
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
        if (horizontalListViewParent.getVisibility() == GONE) {
            horizontalListViewParent.setVisibility(VISIBLE);
        } else {
            horizontalListViewParent.setVisibility(GONE);
        }
    }

    private void getTaggedUsers(final int page) {
        taggedUsersListCall = ApiCallingService.Posts.getTaggedUsers(postDetails.getPostId(), page, context);
        taggedUsersListCall.enqueue(new Callback<TaggedUsersList>() {

            @Override
            public void onResponse(Call<TaggedUsersList> call, Response<TaggedUsersList> response) {

                try
                {
                    if(response.code()==200)
                    {
                        TaggedUsersList taggedList = response.body();
                        boolean next= taggedList.isNextPage();

                        taggedUsersList.addAll(taggedList.getTaggedUsers());

                        if ((taggedUsersList == null || taggedUsersList.size() == 0) && page == 1) {


                        }
                        else
                        {
                            if(next)
                            {
                                int pages=page+1;
                                getTaggedUsers(pages) ;
                            }
                            taggedUserListView.getAdapter().notifyDataSetChanged();
                            taggedUserListView.getAdapter().notifyItemRangeInserted( taggedUserListView.getAdapter().getItemCount(), taggedUsersList.size() - 1);
                        }

                    }
                    else
                    {
                        // Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show();
                        Log.d("PostDetailActivity", getString(R.string.error_getting_tagged_users));
                    }


                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

//                try {
//                    if (response.code() == 200) {
//                        TaggedUsersList taggedList = response.body();
//                        if (taggedList.isNextPage()) {
//                            if (!taggedList.getTaggedUsers().isEmpty()) {
//                                if (page == 1)
//                                    taggedUsersList.clear();
//
//                                taggedUsersList.addAll(taggedList.getTaggedUsers());
//                                getTaggedUsers(page + 1);
//                                noTaggedUsers.setVisibility(GONE);
//                            } else if (page == 1 && taggedList.getTaggedUsers().isEmpty()) {
//                                taggedUsersList.clear();
//                                noTaggedUsers.setVisibility(VISIBLE);
//
//                            }
//                        } else {
//                            if (page == 1 && taggedList.getTaggedUsers().isEmpty()) {
//                                taggedUsersList.clear();
//                                noTaggedUsers.setVisibility(VISIBLE);
//
//                            } else {
//                                noTaggedUsers.setVisibility(GONE);
//                                taggedUsersList.addAll(taggedList.getTaggedUsers());
//                                taggedUserListView.getAdapter().notifyDataSetChanged();
//                            }
//                        }
//                    } else {
//                        Log.d("PostDetailActivity", getString(R.string.error_getting_tagged_users));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
                    PostsListFragment.postDetails.likes += 1;
                    PostsListFragment.postDetails.can_like = false;
                }
                likeBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.selected));
                incrementLikes();
            }
        } else {
            likeBtn.setChecked(false);
            likeBtn.setText(R.string.like);
            likeBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_like_outline_dark, 0, 0);
            if (animate) {
                if (PostsListFragment.postDetails != null) {
                    PostsListFragment.postDetails.likes -= 1;
                    if (PostsListFragment.postDetails.likes < 0) PostsListFragment.postDetails.likes = 0;
                    PostsListFragment.postDetails.can_like = true;
                }
                likeBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.selected));
                decrementLikes();
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

    @OnClick(R.id.controls)
    public void toggleMediaControllerVisibility() {
        try {

//            if(player.getPlayWhenReady())
//                startCountDownClass.pause();
//            else
//                startCountDownClass.resume();

            player.setPlayWhenReady(!player.getPlayWhenReady());


            //  playPauseButton.setImageResource(!player.getPlayWhenReady() ? R.drawable.ic_play_big
            //          : R.drawable.ic_pause_big);
            togglePlayPauseBtnVisibility(!player.getPlayWhenReady());
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextureViewSize(int viewWidth, int viewHeight) {
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(point);
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

    @OnClick(R.id.share)
    public void onViewClicked() {
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
                } else
                    loader.setVisibility(GONE);
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
                            .setPositiveButton(getString(R.string.yes_hide), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ApiCallingService.Posts.hideOrShowPost(postDetails.getPostId(), 1, context)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (response.body().getStatus()) {
                                                        Toast.makeText(context,
                                                                R.string.video_hide_successful,
                                                                Toast.LENGTH_SHORT).show();
                                                        getParentActivity().popFragment();

                                                    } else {
                                                        Toast.makeText(context,
                                                                R.string.something_went_wrong,
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    t.printStackTrace();
                                                    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.confirm)
                            .setMessage("Are you sure you want to delete this video?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    deleteVideo(postDetails.getPostId());
   //                                 PostsListFragment.postDetails = null;
                                    getParentActivity().popFragment();

//                                    getActivity().finish();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
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

    private void deleteVideo(int postId) {
        ApiCallingService.Posts.deletePosts(postId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    if (response.code() == 200) {
                        Toast.makeText(context, "Video has been deleted", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onPause() {
        super.onPause();
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
        if (isComingFromHomePage)
            PostsListFragment.isRefreshing = true;
        postReactionAdapter = null;
        if (postReactionsListCall != null)
            postReactionsListCall.cancel();
        if (taggedUsersListCall != null)
            taggedUsersListCall.cancel();
        customHandler.removeCallbacks(updateTimerThread);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            //acquire audio play access(transient)
            audioAccessGranted = acquireAudioLock(getContext(), audioFocusChangeListener);
            initializePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            customHandler.removeCallbacks(updateTimerThread);
            releasePlayer();
            getParentActivity().showToolbar();
        }
        releaseAudioLock(getContext(), audioFocusChangeListener);
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
            //  playPauseButton.setVisibility(VISIBLE);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    playPauseButton.animate()
                            .scaleY(0)
                            .scaleX(0)
                            .setDuration(200)
                            .setInterpolator(new DecelerateInterpolator())
                            .start();
                    //          playPauseButton.setVisibility(INVISIBLE);
                }
            }, 500);
        }
    }

    private void initializePlayer() {
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            player.addListener(new ExoPlayer.EventListener() {

                @Override
                public void onPlayerStateChanged(boolean b, int i) {
                    switch (i) {
                        case STATE_IDLE:
                            // playPauseButton.setImageResource(R.drawable.ic_play_big);
                            togglePlayPauseBtnVisibility(true);
                            break;
                        case STATE_BUFFERING:
                            playPauseButton.setVisibility(INVISIBLE);
                            progressBar.setVisibility(VISIBLE);
                            break;
                        case STATE_READY:
                            if (player.getPlayWhenReady()) {
                                //  playPauseButton.setImageResource(R.drawable.ic_pause_big);
                                if (playPauseButton.getVisibility() == VISIBLE)
                                    togglePlayPauseBtnVisibility(false);
                                progressBar.setVisibility(GONE);
                                placeholder.setImageBitmap(null);
                                image = null;
//                                startCountDownClass = new StartCountDownClass(player.getDuration(), 1000, remainingTime);
//                                startCountDownClass.start();
                                customHandler.postDelayed(updateTimerThread, 0);
                            }
                            if (oneShotFlag) {
                                oneShotFlag = false;
                                if (!postDetails.canDelete()) {
                                    incrementViews();
                                    ApiCallingService.Posts.incrementViewCount(postDetails.getMedias().get(0).getMediaId(),
                                            context)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    try {
                                                        if (response.code() == 200 && response.body().getStatus()) {
                                                            if (PostsListFragment.postDetails != null)
                                                                PostsListFragment.postDetails.getMedias().get(0).views++;
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        decrementViews();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    decrementViews();
                                                }
                                            });
                                }
                            }
                            break;
                        case STATE_ENDED:
//                            startCountDownClass.cancel();
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
            if (audioAccessGranted) {
                player.setPlayWhenReady(true);
                player.seekTo(player.getCurrentWindowIndex(), playerCurrentPosition);
            }
//            player.getPlaybackState();
            dismissProgressBar();
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
                                if (PostsListFragment.postDetails != null)
                                    PostsListFragment.postDetails.can_react = false;

                                finishReactionUploadSession(context);
                                getPostReactions(postDetails.getPostId(), 1);
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
                                if (PostsListFragment.postDetails != null)
                                    PostsListFragment.postDetails.can_react = true;

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

    private void checkIfAnyReactionIsUploading() {
        UploadParams uploadParams = getReactionUploadSession(context);
        if (uploadParams != null) {
            finishReactionUploadSession(context);
            disableView(reactBtn, true);
            if (PostsListFragment.postDetails != null)
                PostsListFragment.postDetails.can_react = false;

            if (!uploadParams.isGiphy()) {
                launchReactionUploadService(context, uploadParams, reactionUploadReceiver);
            } else {
                postGiphyReaction(uploadParams);
            }
        }
    }

    private void postGiphyReaction(UploadParams uploadParams) {

        GiphyReactionRequest giphyReactionRequest = new GiphyReactionRequest(uploadParams.getPostDetails().getPostId(),
                uploadParams.getTitle(),
                uploadParams.getVideoPath());
        ApiCallingService.React.createReactionByGiphy(giphyReactionRequest, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200 || response.code() == 201) {
                    getPostReactions(postDetails.getPostId(), 1);
                    Toast.makeText(context, "Reaction posted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @SuppressWarnings("unused")
    private static class ShowShareDialog extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<FragmentPostDetails> reference;

        ShowShareDialog(FragmentPostDetails context) {
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
    public void onAttach(Context context) {
        super.onAttach(context);
//        getParentActivity().hideToolbar();

        if (context instanceof CallProfileFromPostDetails) {
            callProfileFromPostDetails = (CallProfileFromPostDetails) context;
            mListener = (onPostOptionsClickListener) context;

        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        try {
            getParentActivity().updateToolbarTitle(previousTitle);
            getParentActivity().showToolbar();
            releaseAudioLock(getContext(), audioFocusChangeListener);
            mHandler.removeCallbacks(mDelayedStopRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void onBackPressed() {
        customHandler.removeCallbacks(updateTimerThread);
        releaseAudioLock(getContext(), audioFocusChangeListener);
        //  onBackPressed();
        playerCurrentPosition = 0;
        mHandler.removeCallbacks(mDelayedStopRunnable);
    }

    //thread to update recording time
    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            try {
                long timeInMilliseconds = totalDuration - player.getCurrentPosition();
                String duration = BLANK_SPACE + String.format(Locale.getDefault(), "%02d:%02d",
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

    public void callUserProfile(int userId, boolean ismyself)
    {
        callProfileFromPostDetails.callProfileListener(userId,ismyself);
    }

    public interface CallProfileFromPostDetails
    {
        public void callProfileListener(int userid, boolean ismyself);
    }

    @Override
    public void onAudioFocusChange(int focusChange)
    {
        if(focusChange == AUDIOFOCUS_LOSS_TRANSIENT)
        {
            // Pause
        }
        else if(focusChange == AudioManager.AUDIOFOCUS_GAIN)
        {
            // Resume
        }
        else if(focusChange == AudioManager.AUDIOFOCUS_LOSS)
        {
            // Stop or pause depending on your need
        }
    }

    public interface onPostOptionsClickListener {
        void onPostLikedClicked(PostDetails postDetails);
    }
}
