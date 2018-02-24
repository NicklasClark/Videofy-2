package com.cncoding.teazer;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.adapter.FollowersAdapter.OtherProfileListener;
import com.cncoding.teazer.adapter.FollowersCreationAdapter.FollowerCreationListener;
import com.cncoding.teazer.adapter.FollowingAdapter.OtherProfileListenerFollowing;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter.myCreationListener;
import com.cncoding.teazer.adapter.ProfileMyReactionAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.asynctasks.AddWaterMarkAsyncTask;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.NestedCoordinatorLayout;
import com.cncoding.teazer.customViews.coachMark.MaterialShowcaseSequence;
import com.cncoding.teazer.customViews.coachMark.MaterialShowcaseView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.data.receiver.ReactionUploadReceiver;
import com.cncoding.teazer.data.receiver.VideoUploadReceiver;
import com.cncoding.teazer.home.BaseFragment.FragmentNavigation;
import com.cncoding.teazer.home.camera.UploadFragment;
import com.cncoding.teazer.home.discover.DiscoverFragment;
import com.cncoding.teazer.home.discover.SubDiscoverFragment;
import com.cncoding.teazer.home.notifications.NotificationsAdapter.OnNotificationsInteractionListener;
import com.cncoding.teazer.home.notifications.NotificationsFragment;
import com.cncoding.teazer.home.notifications.NotificationsFragment.OnNotificationsFragmentInteractionListener;
import com.cncoding.teazer.home.post.detailspage.FragmentLikedUser;
import com.cncoding.teazer.home.post.detailspage.PostDetailsFragment;
import com.cncoding.teazer.home.post.detailspage.PostDetailsFragment.onPostOptionsClickListener;
import com.cncoding.teazer.home.post.detailspage.TagListAdapter;
import com.cncoding.teazer.home.post.homepage.PostsListFragment;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.home.profile.ProfileFragment.FollowerListListener;
import com.cncoding.teazer.home.tagsAndCategories.Interests.OnInterestsInteractionListener;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.base.UploadParams;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.react.GiphyReactionRequest;
import com.cncoding.teazer.model.react.MyReactions;
import com.cncoding.teazer.model.react.ReactVideoDetailsResponse;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.ui.fragment.activity.FollowersListActivity;
import com.cncoding.teazer.ui.fragment.activity.FollowingListActivities;
import com.cncoding.teazer.ui.fragment.activity.OthersProfileFragment;
import com.cncoding.teazer.ui.fragment.fragment.FragmentReactionPlayer;
import com.cncoding.teazer.utilities.FragmentHistory;
import com.cncoding.teazer.utilities.NavigationController;
import com.cncoding.teazer.utilities.NavigationController.RootFragmentListener;
import com.cncoding.teazer.utilities.NavigationController.TransactionListener;
import com.cncoding.teazer.utilities.NavigationTransactionOptions;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.jetbrains.annotations.Contract;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.R.anim.fast_fade_in;
import static com.cncoding.teazer.R.anim.fast_fade_out;
import static com.cncoding.teazer.R.anim.float_up;
import static com.cncoding.teazer.customViews.coachMark.MaterialShowcaseView.TYPE_DISCOVER;
import static com.cncoding.teazer.customViews.coachMark.MaterialShowcaseView.TYPE_NORMAL;
import static com.cncoding.teazer.data.service.ReactionUploadService.launchReactionUploadService;
import static com.cncoding.teazer.data.service.VideoUploadService.ADD_WATERMARK;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_COMPLETE_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_ERROR_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_IN_PROGRESS_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_PROGRESS;
import static com.cncoding.teazer.data.service.VideoUploadService.VIDEO_PATH;
import static com.cncoding.teazer.data.service.VideoUploadService.launchVideoUploadService;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_MY_INTERESTS;
import static com.cncoding.teazer.ui.fragment.fragment.FragmentReactionPlayer.OPENED_FROM_OTHER_SOURCE;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_GIF;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_GIPHY;
import static com.cncoding.teazer.utilities.CommonUtilities.deleteFilePermanently;
import static com.cncoding.teazer.utilities.CommonWebServicesUtil.fetchPostDetails;
import static com.cncoding.teazer.utilities.NavigationController.TAB1;
import static com.cncoding.teazer.utilities.NavigationController.TAB2;
import static com.cncoding.teazer.utilities.NavigationController.TAB3;
import static com.cncoding.teazer.utilities.NavigationController.TAB4;
import static com.cncoding.teazer.utilities.NavigationController.TAB5;
import static com.cncoding.teazer.utilities.SharedPrefs.finishReactionUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.finishVideoUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.getAuthToken;
import static com.cncoding.teazer.utilities.SharedPrefs.getFollowingNotificationCount;
import static com.cncoding.teazer.utilities.SharedPrefs.getReactionUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.getRequestNotificationCount;
import static com.cncoding.teazer.utilities.SharedPrefs.getVideoUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.isActivityActive;
import static com.cncoding.teazer.utilities.SharedPrefs.onActivityActive;
import static com.cncoding.teazer.utilities.SharedPrefs.onActivityInactive;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.getCoachMark;
import static com.cncoding.teazer.utilities.ViewUtils.getTabChild;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.launchVideoUploadCamera;
import static com.cncoding.teazer.utilities.ViewUtils.showCoachMark;

public class BaseBottomBarActivity extends BaseActivity
//    Navigation listeners
        implements FragmentNavigation, TransactionListener, RootFragmentListener,
//    Post related listeners
        OnInterestsInteractionListener, onPostOptionsClickListener,
//    Notification listeners
        OnNotificationsInteractionListener, OnNotificationsFragmentInteractionListener,
//    Profile listeners
        OtherProfileListener, FollowerListListener, myCreationListener, OtherProfileListenerFollowing, FollowerCreationListener,
//    Profile listeners LikedUser
        FragmentLikedUser.CallProfileListener,
        TagListAdapter.TaggedListInteractionListener,
        ProfileMyReactionAdapter.ReactionPlayerListener,
        AddWaterMarkAsyncTask.WatermarkAsyncResponse {

    public static final String SOURCE_ID = "source_id";
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String POST_ID = "post_id";
    public static final int REQUEST_CANCEL_UPLOAD = 45;
    public static final int COACH_MARK_DELAY = 1000;

    @BindArray(R.array.tab_name) String[] TABS;
    @BindView(R.id.app_bar) AppBarLayout appBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_center_title) ImageView toolbarCenterTitle;
    @BindView(R.id.toolbar_plain_title) ProximaNovaSemiBoldTextView toolbarPlainTitle;
    @BindView(R.id.main_fragment_container) FrameLayout contentFrame;
    @BindView(R.id.root_layout) NestedCoordinatorLayout rootLayout;
    @BindView(R.id.blur_view) BlurView blurView;
    @BindView(R.id.bottom_tab_layout) TabLayout bottomTabLayout;
    @BindView(R.id.camera_btn) ProximaNovaBoldTextView cameraButton;
    @BindView(R.id.upload_progress_text) ProximaNovaSemiBoldTextView uploadProgressText;
    @BindView(R.id.uploading_thumb) CircularAppCompatImageView uploadThumb;
    @BindView(R.id.video_upload_progress) ProgressBar videoUploadProgress;
    @BindView(R.id.reaction_upload_progress) ProgressBar reactionUploadProgress;
    @BindView(R.id.uploading_status_layout) RelativeLayout uploadingStatusLayout;
    @BindView(R.id.btnToolbarBack) ImageView btnToolbarBack;

    private VideoUploadReceiver videoUploadReceiver;
    private ReactionUploadReceiver reactionUploadReceiver;
    private NavigationController navigationController;
    private FragmentHistory fragmentHistory;
    private Fragment currentFragment;
    private BroadcastReceiver BReceiver;
    public MaterialShowcaseView materialShowcaseView;
    ProfileFragment profilefragment;
    PostDetails postDetails;
    private Call<ReactionResponse> postGiphyCall;

    @Contract(value = " -> !null", pure = true)
    private BaseBottomBarActivity getThis() {
        return this;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (navigationController != null) {
            navigationController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_bottom_bar);
        ButterKnife.bind(this);

//       Log.d("FCM", SharedPrefs.getFcmToken(this));
//        Glide.with(this)
//                .load(R.drawable.ic_loader)
//                .asGif()
//                .into(loader);

        //logging mobiruck event
//        MobiruckEvent mobiruckEvent = new MobiruckEvent();
//
//        mobiruckEvent.setEvent("logged_in");  // event name should match as added in the dashboard.
//
//        MobiruckSdk.getInstance().logEvent(mobiruckEvent);

        Log.d("NOTIFY", "onCreate called");

        blurView.setupWith(rootLayout)
                .windowBackground(getWindow().getDecorView().getBackground())
                .blurAlgorithm(new RenderScriptBlur(this))
                .blurRadius(20);

        Log.d("AUTH_TOKEN", getAuthToken(getApplicationContext()) == null ? "N/A" : getAuthToken(getApplicationContext()));

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        appBar.addOnOffsetChangedListener(appBarOffsetChangeListener());

        fragmentHistory = new FragmentHistory();

        navigationController = NavigationController
                .newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.main_fragment_container)
                .activity(this)
                .defaultTransactionOptions(new NavigationTransactionOptions.Builder()
                        .customAnimations(float_up, fast_fade_out, fast_fade_in, fast_fade_out)
                        .build())
                .transactionListener(this)
                .rootFragmentListener(this, TABS.length)
                .build();

        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentHistory.push(tab.getPosition());
                switchTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                navigationController.clearStack();
                switchTab(tab.getPosition());
                maybeRefreshTab(tab);
            }
        });

        View thirdTab = getTabChild(bottomTabLayout, TAB3);
        if (thirdTab != null) {
            thirdTab.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }

        BReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int postId = Integer.parseInt(intent.getStringExtra("PostID"));
                String postTitle = intent.getStringExtra("PostTitle");
                String postUrl = intent.getStringExtra("PostURL");
                String postOwnerId = intent.getStringExtra("PostOwner");

                BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                        .setCanonicalIdentifier(postOwnerId)
                        .setTitle(postTitle)
                        .setContentDescription("Watch " + postTitle + "awesome video on Teazer app")
                        .setContentImageUrl(postUrl);

                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("facebook")
                        .setFeature("sharing")
                        .addControlParameter("post_id", String.valueOf(postId))
                        .addControlParameter("$desktop_url", "https://teazer.in/")
                        .addControlParameter("$ios_url", "https://teazer.in/");

                branchUniversalObject.generateShortUrl(getApplicationContext(), linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {

                            if (UploadFragment.checkFacebookButtonPressed) {
                                ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse(url)).build();
                                ShareDialog shareDialog = new ShareDialog(getThis());
                                shareDialog.show(content);
                                ShareApi.share(content, null);
                                UploadFragment.checkFacebookButtonPressed = false;
                            }

                            if (UploadFragment.checkedTwitterButton) {
                                shareTwitter(url);
                                UploadFragment.checkFacebookButtonPressed = false;
                            }
                        }
                    }
                });
            }

        };

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                try {
                    Log.d("NOTIFY", "BUNDLE Exists in onCreate");
                    Log.d("NOTIFY", bundle.toString());
                    String notification_type = bundle.getString("notification_type");
                    String source_id = bundle.getString("source_id");
                    String post_id = bundle.getString("post_id");
                    notificationAction(Integer.valueOf(notification_type), Integer.valueOf(source_id), Integer.valueOf(post_id));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("NOTIFY", "BUNDLE not present in onCreate");
            }
        }

        getBranchDynamicLinks();

        int unreadNotificationCount = getFollowingNotificationCount(this) + getRequestNotificationCount(this);
//        bottomTabLayout.getTabAt(3).setCustomView(getTabView(unreadNotificationCount));
        TabLayout.Tab tab = bottomTabLayout.getTabAt(3);
        if (tab != null) {
            tab.setCustomView(null);
            tab.setCustomView(getTabView(unreadNotificationCount));
        }
        switchTab(TAB1);
    }

    private void maybeRefreshTab(TabLayout.Tab tab) {
        final View tabButton = getTabChild(bottomTabLayout, tab.getPosition());
        if (tabButton != null) {
            tabButton.setEnabled(false);
            switch (tab.getPosition()) {
                case TAB1:
                    if (navigationController.getCurrentFragment() instanceof PostsListFragment) {
                        PostsListFragment fragment = (PostsListFragment) navigationController.getCurrentFragment();
                        if (!fragment.isListAtTop()) {
                            fragment.manualRefreshTriggered = true;
                            fragment.refreshPosts(true, true);
                        }
                    }
                    break;
                case TAB2:
                    if (navigationController.getCurrentFragment() instanceof DiscoverFragment) {
                        navigationController.replaceFragment(getRootFragment(TAB2));
                    }
                    break;
                case TAB4:
                    if (navigationController.getCurrentFragment() instanceof NotificationsFragment) {
                        navigationController.replaceFragment(getRootFragment(TAB4));
                    }
                    break;
                case TAB5:
                    if (navigationController.getCurrentFragment() instanceof ProfileFragment) {
                        navigationController.replaceFragment(getRootFragment(TAB5));
                    }
                    break;
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tabButton != null) {
                    tabButton.setEnabled(true);
                }
            }
        }, 1000);
    }

    private void shareTwitter(String message) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, message);
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }

        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, message);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(message)));
            startActivity(i);
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf("TAG", "UTF-8 should always be supported", e);
            return "";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfAnyVideoIsUploading();
        LocalBroadcastManager.getInstance(this).registerReceiver(BReceiver, new IntentFilter("message"));
    }

    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(BReceiver);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getBranchDynamicLinks();

        Log.d("NOTIFY", "onNewIntent called");
        try {
            if (intent.getExtras() != null) {
                //noinspection ConstantConditions
                Bundle profileBundle = getIntent().getExtras().getBundle("profileBundle");
                Bundle notificationBundle = intent.getExtras().getBundle("bundle");
//                Bundle likedUserProfile = intent.getExtras().getBundle("LikedUserprofileBundle");

                if (notificationBundle != null) {
                    Log.d("NOTIFY", "BUNDLE Exists on new Intent");
                    int notification_type = notificationBundle.getInt(NOTIFICATION_TYPE);
                    int source_id = notificationBundle.getInt(SOURCE_ID);
                    int post_id = notificationBundle.getInt(POST_ID);
                    notificationAction(notification_type, source_id, post_id);
                } else if (profileBundle != null) {

                    int userId = profileBundle.getInt("userId");
                    boolean isSelf = profileBundle.getBoolean("isSelf");
                    postDetails = profileBundle.getParcelable("PostDetails");
                    if (isSelf) {
                        pushFragment(ProfileFragment.newInstance());
                    } else {
                        pushFragment(OthersProfileFragment.newInstance(String.valueOf(userId), "", ""));
                    }
//                    pushFragment(isSelf ? ProfileFragment.newInstance() :
//                            OthersProfileFragment.newInstance(String.valueOf(userId), "identifier", "username"));
//
                } else {
                    Log.d("NOTIFY", "BUNDLE not present on new Intent");
                    switchTabDynamically();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        onActivityActive(this);

        Log.d("NOTIFY", "onStart called");

        try {
            Bundle notificationBundle = getIntent().getExtras();
            if (notificationBundle != null) {
                Log.d("NOTIFY", "BUNDLE Exists in onStart");
                String notification_type = notificationBundle.getString("notification_type");
                String source_id = notificationBundle.getString("source_id");
                String post_id = notificationBundle.getString("post_id");
                notificationAction(Integer.valueOf(notification_type), Integer.valueOf(source_id), Integer.valueOf(post_id));
            } else
                Log.d("NOTIFY", "BUNDLE not present in onStart");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("InflateParams")
    public View getTabView(int value) {
        View v = null;
        try {
            v = LayoutInflater.from(this).inflate(R.layout.notification_with_badge, null);
            TextView tv = v.findViewById(R.id.notification_badge);
            if (value != 0) {
                tv.setText(String.valueOf(value));
            } else {
                tv.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void notificationAction(int notification_type, int source_id, int post_id) {
        if (notification_type == 11) {
            //do nothing as of now
            Log.d("Notification", String.valueOf(notification_type));
        }
        if (notification_type == 1 || notification_type == 2 || notification_type == 3 || notification_type == 10) {
            pushFragment(OthersProfileFragment.newInstance3(String.valueOf(source_id), String.valueOf(notification_type)));
        } else if (notification_type == 5 || notification_type == 7 || notification_type == 9) {
            ApiCallingService.Posts.getPostDetails(source_id, getThis())
                    .enqueue(new Callback<PostDetails>() {
                        @Override
                        public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    pushFragment(PostDetailsFragment.newInstance(response.body(), null, true, true, response.body().getMedias().get(0).getThumbUrl(), null));
                                } else {
                                    Toast.makeText(getThis(), "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                                }
                            } else
                                Toast.makeText(getThis(), "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<PostDetails> call, Throwable t) {
                            Toast.makeText(getThis(), "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            if (post_id != 0) {
                ApiCallingService.React.getReactionDetail2(source_id, getApplicationContext())
                        .enqueue(new Callback<ReactVideoDetailsResponse>() {
                            @Override
                            public void onResponse(Call<ReactVideoDetailsResponse> call, Response<ReactVideoDetailsResponse> response) {
                                if (response.code() == 200) {
                                    if (response.body() != null) {
                                        PostReaction postReactDetail = response.body().getPostReactDetail();
                                        if (postReactDetail.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF || postReactDetail.getMediaDetail().getMediaType() == MEDIA_TYPE_GIPHY) {
                                            pushFragment(FragmentReactionPlayer.newInstance(OPENED_FROM_OTHER_SOURCE, postReactDetail, null, true));
                                        } else {
                                            pushFragment(FragmentReactionPlayer.newInstance(OPENED_FROM_OTHER_SOURCE, postReactDetail, null, true));
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                                    }
                                } else
                                    Toast.makeText(getApplicationContext(), "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ReactVideoDetailsResponse> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                ApiCallingService.Posts.getPostDetails(source_id, getApplicationContext())
                        .enqueue(new Callback<PostDetails>() {
                            @Override
                            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                                if (response.code() == 200)
                                    pushFragment(PostDetailsFragment.newInstance(postDetails, null, false, false, null, null));

                                else if (response.code() == 412 && response.message().contains("Precondition Failed"))
                                    Toast.makeText(getApplicationContext(), "This post no longer exists", Toast.LENGTH_SHORT).show();
                                else {
                                    Log.d("FETCHING PostDetails", response.code() + " : " + response.message());
                                    Toast.makeText(getApplicationContext(), "Error fetching post", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PostDetails> call, Throwable t) {
                                t.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

            }

        }

    }

    private void getBranchDynamicLinks() {
        //noinspection ConstantConditions
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(final JSONObject referringParams, BranchError error) {
                if (error == null) {
                    try {
                        if (referringParams != null) {
                            Log.d("BranchLog", referringParams.toString());
                            if (referringParams.has("post_id")) {
                                String postId = referringParams.getString("post_id");
                                ApiCallingService.Posts.getPostDetails(Integer.parseInt(postId), getThis())
                                        .enqueue(new Callback<PostDetails>() {
                                            @Override
                                            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                                                if (response.code() == 200) {
                                                    if (response.body() != null) {
                                                        if (referringParams.has("react_id")) {
                                                            try {
                                                                pushFragment(PostDetailsFragment.newInstance(response.body(), null, true, true, response.body().getMedias().get(0).getThumbUrl(), referringParams.getString("react_id")));
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            pushFragment(PostDetailsFragment.newInstance(response.body(), null, true, true, response.body().getMedias().get(0).getThumbUrl(), null));
                                                        }
                                                    } else {
                                                        Toast.makeText(getThis(), "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else
                                                    Toast.makeText(getThis(), "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<PostDetails> call, Throwable t) {
                                                Toast.makeText(getThis(), "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else if (referringParams.has("user_id")) {
                                String userId = referringParams.getString("user_id");
                                if (SharedPrefs.getUserId(getThis()) == Integer.parseInt(userId)) {
                                    pushFragment(ProfileFragment.newInstance());
                                } else {
                                    pushFragment(OthersProfileFragment.newInstance(userId, "", ""));
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard(this, bottomTabLayout);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //<editor-fold desc="Fragment navigation methods">
    private void switchTabDynamically() {
        if (navigationController.getCurrentFragment() instanceof PostsListFragment)
            switchTab(TAB1);
        else if (navigationController.getCurrentFragment() instanceof DiscoverFragment)
            switchTab(TAB2);
        else if (navigationController.getCurrentFragment() instanceof NotificationsFragment)
            switchTab(TAB4);
        else if (navigationController.getCurrentFragment() instanceof ProfileFragment)
            switchTab(TAB5);
    }

    private void switchTab(final int position) {
        navigationController.switchTab(position);
        updateBottomTabIconFocus(position);
        if (position == 1 || position == 3)
            setAppBarElevation(0);
        else
            setAppBarElevation(1);

        switch (position) {
            case TAB1:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentFragment instanceof PostsListFragment && currentFragment.isAdded())
                            getCoachMark(getThis(), currentFragment,
                                    getTabChild(bottomTabLayout, TAB1), "homePage", R.string.welcome_to_teazer,
                                    R.string.coach_mark_post_list_body, R.string.okay_got_it, TYPE_NORMAL);
                    }
                }, COACH_MARK_DELAY);
                break;
            case TAB2:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (currentFragment instanceof DiscoverFragment && currentFragment.isAdded()) {
                                MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(
                                        getThis(), "discoverLanding");
//                                    sequence.setConfig(getShowcaseConfig(getThis(), TYPE_DISCOVER));
                                sequence.addSequenceItem(
                                        showCoachMark(
                                                getThis(),
                                                getTabChild(bottomTabLayout, TAB2),
                                                "discover",
                                                R.string.discover_the_app,
                                                R.string.coach_mark_discover_body,
                                                R.string.next,
                                                TYPE_NORMAL)
                                ).addSequenceItem(
                                        showCoachMark(
                                                getThis(),
                                                ((DiscoverFragment) currentFragment).myInterestsViewAll,
                                                "myInterests",
                                                R.string.my_interests,
                                                R.string.coach_mark_my_interests_body,
                                                R.string.done,
                                                TYPE_DISCOVER));
                                sequence.start();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, COACH_MARK_DELAY);
                break;
            case TAB5:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentFragment instanceof ProfileFragment && currentFragment.isAdded())
                            getCoachMark(getThis(), currentFragment,
                                    getTabChild(bottomTabLayout, TAB5), "profilePage", R.string.my_profile,
                                    R.string.coach_mark_profile_body, R.string.okay_got_it, TYPE_NORMAL);
                    }
                }, COACH_MARK_DELAY);
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void updateBottomTabIconFocus(int position) {
        for (int i = 0; i < bottomTabLayout.getTabCount(); i++) {
            if (i != 2) {
                if (i == position) {
                    bottomTabLayout.getTabAt(position).getIcon().setTint(getResources().getColor(R.color.colorAccent4));
                    bottomTabLayout.getTabAt(position).getIcon().setAlpha(255);
                } else {
                    bottomTabLayout.getTabAt(i).getIcon().setTint(Color.WHITE);
                    //bottomTabLayout.getTabAt(position).getIcon().setAlpha(127);
                }
            }
        }
    }

    public void setAppBarElevation(float elevation) {
        if (appBar.getElevation() != elevation)
            appBar.setElevation((int) ((elevation * getResources().getDisplayMetrics().density) + 0.5));
    }

    public void updateToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (!navigationController.isRootFragment()) {
                btnToolbarBack.setVisibility(VISIBLE);
            } else
                btnToolbarBack.setVisibility(GONE);
//            actionBar.setDisplayHomeAsUpEnabled(!navigationController.isRootFragment());
//            actionBar.setDisplayShowHomeEnabled(!navigationController.isRootFragment());
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        // toggleBottomBar(navigationController.isRootFragment());
    }

    @SuppressWarnings("unused")
    public void toggleBottomBar(boolean isVisible) {
        if (isVisible) {
            if (blurView.getVisibility() != VISIBLE && cameraButton.getVisibility() != VISIBLE) {
                blurView.setVisibility(VISIBLE);
                cameraButton.setVisibility(VISIBLE);
            }
        } else {
            if (blurView.getVisibility() == VISIBLE && cameraButton.getVisibility() == VISIBLE) {
                blurView.setVisibility(GONE);
                cameraButton.setVisibility(GONE);
            }
        }
    }

    public void hideToolbar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar.setVisibility(GONE);
    }

    public void showToolbar() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar.setVisibility(VISIBLE);
    }

    /**
     * Updates the toolbar title.
     *
     * @param title The title to be set, if null is passed, then "Teazer" will be set in SignPainter font in the center.
     */
    public void updateToolbarTitle(String title) {
        if (title == null || title.equals("")) {
            if (toolbarPlainTitle.getVisibility() != GONE) {
                toolbarPlainTitle.setVisibility(GONE);
                btnToolbarBack.setVisibility(GONE);
            }
            if (toolbarCenterTitle.getVisibility() != VISIBLE) {
                toolbarCenterTitle.setVisibility(VISIBLE);
                btnToolbarBack.setVisibility(VISIBLE);
            }
        } else {
            toolbarPlainTitle.setText(title);
            if (toolbarPlainTitle.getVisibility() != VISIBLE) {
                toolbarPlainTitle.setVisibility(VISIBLE);
                btnToolbarBack.setVisibility(VISIBLE);
            }
            if (toolbarCenterTitle.getVisibility() != GONE) {
                toolbarCenterTitle.setVisibility(GONE);
                btnToolbarBack.setVisibility(GONE);
            }
            uploadingStatusLayout.setVisibility(GONE);
        }
    }

    public String getToolbarTitle() {
        try {
            if (toolbarPlainTitle != null)
                return toolbarPlainTitle.getText().toString();
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backStack, show the back button
        if (getSupportActionBar() != null && navigationController != null) {
            updateToolbar();
        }
    }

    @Override
    public void onFragmentTransaction(Fragment fragment, NavigationController.TransactionType transactionType) {
        //Do fragment stuff. Maybe change title.
        // If we have a backStack, show the back button
        if (getSupportActionBar() != null && navigationController != null) {
            updateToolbar();
        }
    }

    @Override
    public void pushFragment(final Fragment fragment) {
        if (navigationController != null) {
            if (fragment instanceof PostDetailsFragment) {
                hideToolbar();
            }
            navigationController.pushFragment(fragment);
        }
    }

    @Override
    public void popFragment() {
        navigationController.popFragment();
    }

    @Override
    public void pushFragmentOnto(Fragment fragment) {
        if (navigationController != null) {
            navigationController.pushFragmentOnto(fragment);
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        currentFragment = null;
        switch (index) {
            case TAB1:
                currentFragment = PostsListFragment.newInstance();
                break;
            case TAB2:
                currentFragment = DiscoverFragment.newInstance();
//            case NavigationController.TAB3:
//                return null;
                break;
            case TAB4:
                currentFragment = NotificationsFragment.newInstance();
                break;
            case TAB5:
                currentFragment = ProfileFragment.newInstance();
                break;
            default:
                currentFragment = PostsListFragment.newInstance();
                break;
        }
        return currentFragment;
    }
    //</editor-fold>

    //<editor-fold desc="On click methods">
    @OnClick(R.id.camera_btn)
    public void startCamera() {
        launchVideoUploadCamera(this);
        finish();
    }
    //</editor-fold>

    //<editor-fold desc="Video upload handler">
    private void checkIfAnyVideoIsUploading() {
        UploadParams videoUploadParams = getVideoUploadSession(getApplicationContext());
        if (videoUploadParams != null) {
            setupVideoUploadServiceReceiver(videoUploadParams);
            launchVideoUploadService(this, videoUploadParams, videoUploadReceiver);
        }
        UploadParams reactionUploadParams = getReactionUploadSession(getApplicationContext());
        if (reactionUploadParams != null) {
            if (!reactionUploadParams.isGiphy()) {
                setupReactionUploadServiceReceiver(reactionUploadParams);
                launchReactionUploadService(this, reactionUploadParams, reactionUploadReceiver);
            } else {
                postGiphyReaction(reactionUploadParams);
            }
        }
    }

    private void setupVideoUploadServiceReceiver(UploadParams videoUploadParams) {
        videoUploadProgress.setVisibility(VISIBLE);
        Glide.with(this)
                .load(Uri.fromFile(new File(videoUploadParams.getVideoPath())))
                .into(uploadThumb);
        if (videoUploadReceiver == null) {
            videoUploadReceiver = new VideoUploadReceiver(new Handler()).setReceiver(new VideoUploadReceiver.Receiver() {
                @Override
                public void onReceiverResult(int resultCode, Bundle resultData) {
                    switch (resultCode) {
                        case UPLOAD_IN_PROGRESS_CODE:
                            if (isActivityActive(getThis()) && navigationController.getCurrentFragment() instanceof PostsListFragment) {
                                uploadingStatusLayout.setVisibility(VISIBLE);
                                uploadProgressText.setText(String.valueOf(getString(R.string.uploading_your_video) +
                                        BLANK_SPACE + resultData.getInt(UPLOAD_PROGRESS) + "%"));
                                videoUploadProgress.setProgress(resultData.getInt(UPLOAD_PROGRESS));
                            } else uploadingStatusLayout.setVisibility(GONE);
                            break;
                        case UPLOAD_COMPLETE_CODE:
                            if (isActivityActive(getThis()) && navigationController.getCurrentFragment() instanceof PostsListFragment) {
                                uploadProgressText.setText(R.string.finished);
                                videoUploadProgress.setVisibility(GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadingStatusLayout.setVisibility(GONE);
                                    }
                                }, 2000);

                                refreshPosts();
                            } else uploadingStatusLayout.setVisibility(GONE);
                            finishVideoUploadSession(getApplicationContext());

                            //add watermark for local creations/reactions
                            if (resultData.getBoolean(ADD_WATERMARK)) {
                                AddWaterMarkAsyncTask addWaterMarkAsyncTask = new AddWaterMarkAsyncTask(BaseBottomBarActivity.this);
                                addWaterMarkAsyncTask.delegate = BaseBottomBarActivity.this;
                                addWaterMarkAsyncTask.execute(resultData.getString(VIDEO_PATH));
                            } else
                                deleteFilePermanently(resultData.getString(VIDEO_PATH));
                            break;
                        case UPLOAD_ERROR_CODE:
                            if (isActivityActive(getThis()) && navigationController.getCurrentFragment() instanceof PostsListFragment) {
                                uploadProgressText.setText(R.string.failed);
                                videoUploadProgress.setVisibility(GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadingStatusLayout.setVisibility(GONE);
                                    }
                                }, 2000);
                            } else uploadingStatusLayout.setVisibility(GONE);
                            finishVideoUploadSession(getApplicationContext());
                            break;
                        case REQUEST_CANCEL_UPLOAD:
                            if (isActivityActive(getThis()) && navigationController.getCurrentFragment() instanceof PostsListFragment)
                                Toast.makeText(getThis(), R.string.upload_cancelled, Toast.LENGTH_SHORT).show();
                            else uploadingStatusLayout.setVisibility(GONE);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void setupReactionUploadServiceReceiver(UploadParams reactionUploadParams) {
        reactionUploadProgress.setVisibility(VISIBLE);
        Glide.with(this)
                .load(Uri.fromFile(new File(reactionUploadParams.getVideoPath())))
                .into(uploadThumb);
        if (reactionUploadReceiver == null) {
            reactionUploadReceiver = new ReactionUploadReceiver(new Handler()).setReceiver(new ReactionUploadReceiver.Receiver() {
                @Override
                public void onReceiverResult(int resultCode, Bundle resultData) {
                    switch (resultCode) {
                        case UPLOAD_IN_PROGRESS_CODE:
                            if (isActivityActive(getThis()) && navigationController.getCurrentFragment() instanceof PostsListFragment) {
                                uploadingStatusLayout.setVisibility(VISIBLE);
                                uploadProgressText.setText(String.valueOf(getString(R.string.uploading_your_reaction) +
                                        BLANK_SPACE + resultData.getInt(UPLOAD_PROGRESS) + "%"));
                                reactionUploadProgress.setProgress(resultData.getInt(UPLOAD_PROGRESS));
                            } else uploadingStatusLayout.setVisibility(GONE);
                            break;
                        case UPLOAD_COMPLETE_CODE:
                            if (isActivityActive(getThis()) && navigationController.getCurrentFragment() instanceof PostsListFragment) {
                                uploadProgressText.setText(R.string.finished);
                                reactionUploadProgress.setVisibility(GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadingStatusLayout.setVisibility(GONE);
                                    }
                                }, 2000);

                                if (PostsListFragment.postDetails != null)
                                    PostsListFragment.postDetails.canReact = false;
                                refreshPosts();
                            } else uploadingStatusLayout.setVisibility(GONE);
                            finishReactionUploadSession(getApplicationContext());
                            //add watermark for local creations/reactions
                            if (resultData.getBoolean(ADD_WATERMARK)) {
                                AddWaterMarkAsyncTask addWaterMarkAsyncTask = new AddWaterMarkAsyncTask(BaseBottomBarActivity.this);
                                addWaterMarkAsyncTask.delegate = BaseBottomBarActivity.this;
                                addWaterMarkAsyncTask.execute(resultData.getString(VIDEO_PATH));
                            } else
                                deleteFilePermanently(resultData.getString(VIDEO_PATH));

                            break;
                        case UPLOAD_ERROR_CODE:
                            if (isActivityActive(getThis()) && navigationController.getCurrentFragment() instanceof PostsListFragment) {
                                uploadProgressText.setText(R.string.failed);
                                reactionUploadProgress.setVisibility(GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        uploadingStatusLayout.setVisibility(GONE);
                                    }
                                }, 2000);
                            } else uploadingStatusLayout.setVisibility(GONE);
                            finishReactionUploadSession(getApplicationContext());
                            break;
                        case REQUEST_CANCEL_UPLOAD:
                            if (isActivityActive(getThis()) && navigationController.getCurrentFragment() instanceof PostsListFragment)
                                Toast.makeText(getThis(), R.string.upload_cancelled, Toast.LENGTH_SHORT).show();
                            else uploadingStatusLayout.setVisibility(GONE);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void refreshPosts() {
        if (navigationController.getCurrentFragment() instanceof PostsListFragment) {
            PostsListFragment fragment = (PostsListFragment) navigationController.getCurrentFragment();
            if (fragment != null) {
                fragment.manualRefreshTriggered = true;
                fragment.refreshPosts(false, true);
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Fragment listener implementations">
    @Override
    public void onNotificationsInteraction(boolean isFollowingTab, PostDetails postDetails,
                                           int profileId, String userType) {
        if (isFollowingTab) {
            pushFragment(PostDetailsFragment.newInstance(postDetails, null, false, false, null, null));
        } else {
            pushFragment(OthersProfileFragment.newInstance(String.valueOf(profileId), userType, "name"));
        }
    }

    @Override
    public void onFollowerListListener(String id, String identifier) {
        pushFragment(FollowersListActivity.newInstance(id, identifier));
    }

    @Override
    public void onFollowingListListener(String id, String identifier) {
        pushFragment(FollowingListActivities.newInstance(id, identifier));
    }

    @Override
    public void viewOthersProfile(String id, String username, String type) {
        pushFragment(OthersProfileFragment.newInstance(id, type, username));
    }

    @Override
    public void viewOthersProfileFollowing(String id, String username, String type) {
        pushFragment(OthersProfileFragment.newInstance2(id, type, username));
    }

    @Override
    public void onInterestsInteraction(boolean isFromDiscover, ArrayList<Category> categories) {
        DiscoverFragment.updateMyInterests = true;
        navigationController.popFragments(2);
        if (!isFromDiscover)
            pushFragment(SubDiscoverFragment.newInstance(ACTION_VIEW_MY_INTERESTS, categories));
        else {
            if (currentFragment instanceof DiscoverFragment && navigationController.isRootFragment()) {
                ((DiscoverFragment) currentFragment).loadPosts();
            }
        }
    }

    @Override
    public void onInterestsSelected(String resultToShow, String resultToSend, int count) {
    }

    @Override
    public void myCreationVideos(int i, PostDetails postDetails) {
        fetchPostDetails(this, postDetails.getPostId());
    }

    @Override
    public void ReactionPost(int postId) {
        fetchPostDetails(this, postId);
    }
    //</editor-fold>

    @Override
    public void waterMarkProcessFinish(String destinationPath, String sourcePath) {
        deleteFilePermanently(sourcePath);
    }

    private void postGiphyReaction(UploadParams uploadParams) {
        GiphyReactionRequest giphyReactionRequest = new GiphyReactionRequest(uploadParams.getPostDetails().getPostId(),
                uploadParams.getTitle(),
                uploadParams.getVideoPath());
        ApiCallingService.React.createReactionByGiphy(giphyReactionRequest, this).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.body().getCode() == 200)
                    Toast.makeText(BaseBottomBarActivity.this, "Reaction posted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(BaseBottomBarActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void callProfileListener(int id, boolean isMyself) {
        pushFragment(isMyself ? ProfileFragment.newInstance() :
                OthersProfileFragment.newInstance(String.valueOf(id), "", ""));
    }

    @Override
    public void onTaggedUserInteraction(int userId, boolean isSelf) {
        pushFragment(isSelf ? ProfileFragment.newInstance() :
                OthersProfileFragment.newInstance(String.valueOf(userId), "", ""));
    }

    @Override
    public void onPostLikedClicked(PostDetails postDetails) {
        pushFragment(FragmentLikedUser.newInstance(postDetails));
    }

    @OnClick(R.id.btnToolbarBack)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    public void reactionPlayer(int selfReaction, PostReaction postReaction, MyReactions reaction, boolean isGif) {
        pushFragment(FragmentReactionPlayer.newInstance(selfReaction, postReaction, reaction, isGif));
    }

    @Override
    public void viewUserProfile() {
        profilefragment = ProfileFragment.newInstance();
        pushFragment(profilefragment);
    }

    @Override
    public void onNotificationFragmentInteraction() {
        try {
            TabLayout.Tab tab = bottomTabLayout.getTabAt(3);
            if (tab != null) tab.setCustomView(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    public void updateTabSelection(int currentTab) {
        for (int i = 0; i < TABS.length; i++) {
            TabLayout.Tab selectedTab = bottomTabLayout.getTabAt(i);
            if (selectedTab != null) {
                if (currentTab != i) {
                    View view = selectedTab.getCustomView();
                    if (view != null) {
                        view.setSelected(false);
                    }
                } else {
                    View view = selectedTab.getCustomView();
                    if (view != null) {
                        view.setSelected(true);
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        onActivityInactive(this);
    }

    @Override
    public void onBackPressed() {
        if (materialShowcaseView != null) {
            materialShowcaseView.hide();
            materialShowcaseView = null;
        } else {
            if (!navigationController.isRootFragment()) {
                navigationController.popFragment();
            } else {
                if (fragmentHistory.isEmpty()) {
                    super.onBackPressed();
                } else {
                    if (fragmentHistory.getStackSize() > 1) {
                        int position = fragmentHistory.popPrevious();
                        switchTab(position);
                        updateTabSelection(position);
                    } else {
                        if (navigationController.getCurrentStackIndex() != TAB1) {
                            switchTab(TAB1);
                            updateTabSelection(0);
                            fragmentHistory.emptyStack();
                        } else {
                            super.onBackPressed();
                        }
                    }
                }
            }
        }
    }
}