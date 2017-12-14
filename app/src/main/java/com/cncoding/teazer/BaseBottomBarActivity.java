package com.cncoding.teazer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.adapter.FollowersAdapter.OtherProfileListener;
import com.cncoding.teazer.adapter.FollowersCreationAdapter.FollowerCreationListener;
import com.cncoding.teazer.adapter.FollowingAdapter.OtherProfileListenerFollowing;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter.myCreationListener;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.NestedCoordinatorLayout;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.customViews.SignPainterTextView;
import com.cncoding.teazer.home.BaseFragment.FragmentNavigation;
import com.cncoding.teazer.home.discover.DiscoverFragment;
import com.cncoding.teazer.home.discover.DiscoverFragment.OnDiscoverInteractionListener;
import com.cncoding.teazer.home.discover.SubDiscoverFragment;
import com.cncoding.teazer.home.discover.adapters.SubDiscoverAdapter.OnSubSearchInteractionListener;
import com.cncoding.teazer.home.discover.adapters.TrendingListAdapter.TrendingListInteractionListener;
import com.cncoding.teazer.home.discover.search.DiscoverSearchAdapter.OnDiscoverSearchInteractionListener;
import com.cncoding.teazer.home.notifications.NotificationsAdapter.OnNotificationsInteractionListener;
import com.cncoding.teazer.home.notifications.NotificationsFragment;
import com.cncoding.teazer.home.post.PostDetailsActivity;
import com.cncoding.teazer.home.post.PostsListAdapter.OnPostAdapterInteractionListener;
import com.cncoding.teazer.home.post.PostsListFragment;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.home.profile.ProfileFragment.FollowerListListener;
import com.cncoding.teazer.services.receivers.VideoUploadReceiver;
import com.cncoding.teazer.home.tagsAndCategories.Interests.OnInterestsInteractionListener;
import com.cncoding.teazer.ui.fragment.activity.FollowersListActivity;
import com.cncoding.teazer.ui.fragment.activity.FollowingListActivities;
import com.cncoding.teazer.ui.fragment.activity.OthersProfileFragment;
import com.cncoding.teazer.utilities.FragmentHistory;
import com.cncoding.teazer.utilities.NavigationController;
import com.cncoding.teazer.utilities.NavigationController.RootFragmentListener;
import com.cncoding.teazer.utilities.NavigationController.TransactionListener;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.UploadParams;
import com.cncoding.teazer.utilities.ViewUtils;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_MOST_POPULAR;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_MY_INTERESTS;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_TRENDING;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_COMPLETE_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_ERROR;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_ERROR_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_IN_PROGRESS_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_PROGRESS;
import static com.cncoding.teazer.services.VideoUploadService.launchVideoUploadService;
import static com.cncoding.teazer.utilities.NavigationController.TAB1;
import static com.cncoding.teazer.utilities.NavigationController.TAB2;
import static com.cncoding.teazer.utilities.NavigationController.TAB4;
import static com.cncoding.teazer.utilities.NavigationController.TAB5;
import static com.cncoding.teazer.utilities.SharedPrefs.finishVideoUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.getAuthToken;
import static com.cncoding.teazer.utilities.SharedPrefs.getVideoUploadSession;
import static com.cncoding.teazer.utilities.ViewUtils.UPLOAD_PARAMS;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.launchVideoUploadCamera;

public class BaseBottomBarActivity extends BaseActivity
//    Navigation listeners
        implements FragmentNavigation, TransactionListener, RootFragmentListener,
//    Post related listeners
        OnPostAdapterInteractionListener, OnInterestsInteractionListener,
//    Discover page listeners
        OnDiscoverSearchInteractionListener, OnDiscoverInteractionListener, OnSubSearchInteractionListener, TrendingListInteractionListener,
//    Notification listeners
        OnNotificationsInteractionListener,
//    Profile listeners
        OtherProfileListener, FollowerListListener, myCreationListener, OtherProfileListenerFollowing, FollowerCreationListener {

    public static final int ACTION_VIEW_POST = 0;
    public static final int ACTION_VIEW_PROFILE = 123;
    public static final String TAB_INDEX = "tabIndex";
    public static final String SOURCE_ID = "from_id";
    public static final String NOTIFICATIN_TYPE = "notification_type";
    public static final int REQUEST_CANCEL_UPLOAD = 45;

    @BindArray(R.array.tab_name) String[] TABS;
    @BindView(R.id.app_bar) AppBarLayout appBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_center_title) SignPainterTextView toolbarCenterTitle;
    @BindView(R.id.toolbar_plain_title) ProximaNovaSemiboldTextView toolbarPlainTitle;
    @BindView(R.id.main_fragment_container) FrameLayout contentFrame;
    @BindView(R.id.root_layout) NestedCoordinatorLayout rootLayout;
    @BindView(R.id.blur_view) BlurView blurView;
    @BindView(R.id.bottom_tab_layout) TabLayout bottomTabLayout;
    @BindView(R.id.camera_btn) ProximaNovaBoldTextView cameraButton;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    private VideoUploadReceiver videoUploadReceiver;
    private NavigationController navigationController;
    private FragmentHistory fragmentHistory;
    private Fragment fragment;

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

        Log.d("NOTIFYM", "onCreate called");

        blurView.setupWith(rootLayout)
                .windowBackground(getWindow().getDecorView().getBackground())
                .blurAlgorithm(new RenderScriptBlur(this))
                .blurRadius(20);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        setupServiceReceiver();
        checkIfAnyVideoIsUploading();

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
                Drawable icon = tab.getIcon();
                if (icon != null)
                    icon.setTint(Color.BLACK);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                navigationController.clearStack();
                switchTab(tab.getPosition());
            }
        });
        LinearLayout tabStrip = ((LinearLayout) bottomTabLayout.getChildAt(0));
        tabStrip.getChildAt(2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        getBranchDynamicLinks();

//        if (getIntent().getExtras() != null) {
//            Bundle bundle = getIntent().getExtras();
//            if (bundle != null) {
////                int index = bundle.getInt(TAB_INDEX);
////                if (index != -1)
////                    switchTab(index);
//                try {
//                    Log.d("NOTIFYM", bundle.toString());
//                    String notification_type = bundle.getString("notification_type");
//                    String source_id = bundle.getString("source_id");
//                    notificationAction(Integer.parseInt(notification_type), Integer.parseInt(source_id));
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d("NOTIFYM", "onNewIntent called");
        if (intent.getExtras() != null) {
            Bundle bundle = intent.getExtras().getBundle("bundle");
            if (bundle != null) {
                Log.d("NOTIFYM", "BUNDLE Exists on new Intent");
                int notification_type = bundle.getInt(NOTIFICATIN_TYPE);
                int source_id = bundle.getInt(SOURCE_ID);
                notificationAction(notification_type, source_id);
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        initTab();
        Log.d("NOTIFYM", "onPostCreate called");
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras().getBundle("profileBundle");
                if (bundle != null) {
                    int userId = bundle.getInt("userId");
                    boolean isSelf = bundle.getBoolean("isSelf");
                    pushFragment(isSelf ? ProfileFragment.newInstance() :
                            OthersProfileFragment.newInstance(String.valueOf(userId), "identifier", "username"));
                } else switchTabDynamically();
        } else {
            switchTabDynamically();
        }
    }


    private void notificationAction(int notification_type, int source_id) {
        if(notification_type == 1 || notification_type == 2 || notification_type == 3 || notification_type == 10)
        {
            pushFragment(OthersProfileFragment.newInstance(String.valueOf(source_id), "",""));
        }
        else
        {
            ApiCallingService.Posts.getPostDetails(source_id, BaseBottomBarActivity.this)
                    .enqueue(new Callback<PostDetails>() {
                        @Override
                        public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                            if (response.code() == 200) {
                                if (response.body() != null) {
                                    PostDetailsActivity.newInstance(BaseBottomBarActivity.this, response.body(), null, true, true, response.body().getMedias().get(0).getThumbUrl());
                                } else {
                                    Toast.makeText(BaseBottomBarActivity.this, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                                }
                            } else
                                Toast.makeText(BaseBottomBarActivity.this, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<PostDetails> call, Throwable t) {
                            Toast.makeText(BaseBottomBarActivity.this, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void getBranchDynamicLinks() {
        // listener (within Main Activity's onStart)
        //noinspection ConstantConditions
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    try {
                        if (referringParams != null) {
                            Log.d("BranchLog", referringParams.toString());
                            if (referringParams.has("post_id")) {
                                String postId = referringParams.getString("post_id");
                                ApiCallingService.Posts.getPostDetails(Integer.parseInt(postId), BaseBottomBarActivity.this)
                                        .enqueue(new Callback<PostDetails>() {
                                            @Override
                                            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                                                if (response.code() == 200) {
                                                    if (response.body() != null) {
                                                        PostDetailsActivity.newInstance(BaseBottomBarActivity.this, response.body(), null, true, true, response.body().getMedias().get(0).getThumbUrl());
                                                    } else {
                                                        Toast.makeText(BaseBottomBarActivity.this, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else
                                                    Toast.makeText(BaseBottomBarActivity.this, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<PostDetails> call, Throwable t) {
                                                Toast.makeText(BaseBottomBarActivity.this, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                            else if(referringParams.has("user_id"))
                            {
                                String userId = referringParams.getString("user_id");
                                pushFragment(OthersProfileFragment.newInstance(userId, "",""));
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

//    public AppBarLayout.OnOffsetChangedListener appBarOffsetChangeListener() {
//        return new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                bottomTabLayout.setTranslationY((float) (-verticalOffset));
//                cameraButton.setTranslationY((float) -(verticalOffset * 2));
////                int percentage = (Math.abs(verticalOffset)) * 100 / appBarLayout.getTotalScrollRange();
////                if (percentage > 0) {
////                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                } else
////                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            }
//        };
//    }

    //<editor-fold desc="Fragment navigation methods">
    private void switchTabDynamically() {
        if (navigationController.getCurrentFragment() instanceof PostsListFragment)
            switchTab(0);
        else if (navigationController.getCurrentFragment() instanceof DiscoverFragment)
            switchTab(1);
        else if (navigationController.getCurrentFragment() instanceof NotificationsFragment)
            switchTab(3);
        else if (navigationController.getCurrentFragment() instanceof ProfileFragment)
            switchTab(4);
    }

    private void switchTab(final int position) {
        navigationController.switchTab(position);
        updateBottomTabIconFocus(position);
        if (position == 1 || position == 3)
            setAppBarElevation(0);
        else
            setAppBarElevation(1);
    }

    @SuppressWarnings("ConstantConditions")
    private void updateBottomTabIconFocus(int position) {
        for (int i = 0; i < bottomTabLayout.getTabCount(); i++) {
            if (i != 2) {
                if (i == position) {
                    bottomTabLayout.getTabAt(position).getIcon().setTint(getResources().getColor(R.color.colorAccent4));
                    bottomTabLayout.getTabAt(position).getIcon().setAlpha(255);
                } else{
                    bottomTabLayout.getTabAt(i).getIcon().setTint(Color.WHITE);
                    //bottomTabLayout.getTabAt(position).getIcon().setAlpha(127);
                }
            }
        }
    }

    public void setAppBarElevation(float elevation) {
        if (appBar.getElevation() != elevation)
            appBar.setElevation((int)((elevation * getResources().getDisplayMetrics().density) + 0.5));
    }

    public void updateToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(!navigationController.isRootFragment());
            actionBar.setDisplayShowHomeEnabled(!navigationController.isRootFragment());
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }

    /**
     * Updates the toolbar title.
     *
     * @param title The title to be set, if null is passed, then "Teazer" will be set in SignPainter font in the center.
     */
    public void updateToolbarTitle(String title) {
        if (title == null || title.equals("")) {
            if (toolbarPlainTitle.getVisibility() != GONE)
                toolbarPlainTitle.setVisibility(GONE);
            if (toolbarCenterTitle.getVisibility() != VISIBLE)
                toolbarCenterTitle.setVisibility(VISIBLE);
        } else {
            toolbarPlainTitle.setText(title);
            if (toolbarPlainTitle.getVisibility() != VISIBLE)
                toolbarPlainTitle.setVisibility(VISIBLE);
            if (toolbarCenterTitle.getVisibility() != GONE)
                toolbarCenterTitle.setVisibility(GONE);
        }
    }

    public String getToolbarTitle() {
        return toolbarPlainTitle.getText().toString();
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
    public void pushFragment(Fragment fragment) {
        if (navigationController != null) {
            navigationController.pushFragment(fragment);
        }
    }

    public void pushFragmentOnto(Fragment fragment) {
        if (navigationController != null) {
            navigationController.pushFragmentOnto(fragment);
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        fragment = null;
        switch (index) {
            case TAB1:
                fragment = PostsListFragment.newInstance();
                break;
            case TAB2:
                fragment = DiscoverFragment.newInstance();
//            case NavigationController.TAB3:
//                return null;
                break;
            case TAB4:
                fragment = NotificationsFragment.newInstance();
                break;
            case TAB5:
                fragment = ProfileFragment.newInstance();
                break;
            default:
                fragment = PostsListFragment.newInstance();
                break;
        }
        return fragment;
    }
    //</editor-fold>

    //<editor-fold desc="On click methods">
    @OnClick(R.id.camera_btn) public void startCamera() {
        launchVideoUploadCamera(this);
        finish();
    }
    //</editor-fold>

    //<editor-fold desc="Fragment listener implementations">
    @Override
    public void onPostInteraction(int action, final PostDetails postDetails, ImageView postThumbnail,
                                  RelativeLayout layout, final byte[] image) {
        switch (action) {
            case ACTION_VIEW_POST:
                PostDetailsActivity.newInstance(this, postDetails, image,
                        true, false, null);
                break;
            case ACTION_VIEW_PROFILE:
                int postOwnerId=postDetails.getPostOwner().getUserId();
                String username=postDetails.getPostOwner().getUserName();
                String userType="";
                pushFragment(postDetails.canDelete() ? ProfileFragment.newInstance() :
                        OthersProfileFragment.newInstance(String.valueOf(postOwnerId), userType, username));
        }
    }

    @Override
    public void onDiscoverInteraction(int action, ArrayList<Category> categories, ArrayList<PostDetails> postDetailsArrayList,
                                      PostDetails postDetails) {
        switch (action) {
            case ACTION_VIEW_MY_INTERESTS:
                pushFragment(SubDiscoverFragment.newInstance(action, categories, postDetailsArrayList));
                break;
            case ACTION_VIEW_MOST_POPULAR:
                pushFragment(SubDiscoverFragment.newInstance(action, categories, null));
                break;
            case ACTION_VIEW_POST:
                PostDetailsActivity.newInstance(this, postDetails,
                        null, false, false, null);
                break;
            case ACTION_VIEW_PROFILE:
                pushFragment(postDetails.canDelete() ? ProfileFragment.newInstance() :
                        OthersProfileFragment.newInstance(String.valueOf(postDetails.getPostOwner().getUserId()),
                                "", postDetails.getPostOwner().getUserName()));
                break;
        }
    }

    @Override
    public void onSubSearchInteraction(int action, PostDetails postDetails) {
        switch (action) {
            case ACTION_VIEW_POST:
                PostDetailsActivity.newInstance(this, postDetails, null, false, false, null);
                break;
            case ACTION_VIEW_PROFILE:
                pushFragment(postDetails.canDelete() ? ProfileFragment.newInstance() :
                        OthersProfileFragment.newInstance(
                                String.valueOf(postDetails.getPostOwner().getUserId()), "", ""));
                break;
        }
    }

    @Override
    public void onDiscoverSearchInteraction(boolean isVideosTab, int id) {
        ViewUtils.hideKeyboard(this, bottomTabLayout);
        if (isVideosTab) {
            ApiCallingService.Posts.getPostDetails(id, this)
                    .enqueue(new Callback<PostDetails>() {
                        @Override
                        public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                            if (response.code() == 200) {
                                PostDetailsActivity.newInstance(BaseBottomBarActivity.this, response.body(),
                                        null, false, false, null);
                            } else
                                Log.e("Fetching post details", response.code() + "_" + response.message());
                        }

                        @Override
                        public void onFailure(Call<PostDetails> call, Throwable t) {
                            Log.e("Fetching post details", t.getMessage() != null ? t.getMessage() : "Failed!!!");
                        }
                    });
        } else {
            pushFragment(OthersProfileFragment.newInstance(String.valueOf(id), "Other", "username"));
        }
    }

    @Override
    public void onTrendingListInteraction(Category category) {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(category);
        pushFragment(SubDiscoverFragment.newInstance(ACTION_VIEW_TRENDING, categories, null));
    }

    @Override
    public void onNotificationsInteraction(boolean isFollowingTab, PostDetails postDetails,
                                           int profileId, String userType) {
        if (isFollowingTab) {
            PostDetailsActivity.newInstance(this, postDetails, null, false, false, null);
        } else {
            pushFragment(OthersProfileFragment.newInstance(String.valueOf(profileId),userType,"name"));
        }
    }

    @Override
    public void onFollowerListListener(String id,String identifier) {
        pushFragment(FollowersListActivity.newInstance(id, identifier));
    }

    @Override
    public void onFollowingListListener(String id, String identifier) {
        pushFragment(FollowingListActivities.newInstance(id, identifier));
    }

    @Override
    public void viewOthersProfile(String id, String username, String type) {
        pushFragment(OthersProfileFragment.newInstance(id, type,username));
    }

    @Override
    public void viewOthersProfileFollowing(String id, String username, String type) {
        pushFragment(OthersProfileFragment.newInstance2(id, type,username));
    }

    @Override
    public void onInterestsInteraction() {
        DiscoverFragment.updateMyInterests = true;
        navigationController.popFragments(2);
    }

    @Override
    public void onInterestsSelected(String resultToShow, String resultToSend, int count) {
    }

    @Override
    public void myCreationVideos(int i, PostDetails postDetails) {
        PostDetailsActivity.newInstance(this, postDetails, null, false, false, null);
    }
    //</editor-fold>

    //<editor-fold desc="Video upload handler">
    private void setupServiceReceiver() {
        builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setContentTitle("Teazer video upload")
//                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_file_upload)
                .setAutoCancel(false)
//                .setSound(null)
                .setDefaults(0)
                .setOngoing(true)
                .addAction(R.drawable.ic_clear_dark, "Cancel",
                        PendingIntent.getActivity(BaseBottomBarActivity.this, REQUEST_CANCEL_UPLOAD, new Intent(), 0))
                .setProgress(0, 0, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    "Upload notification", NotificationManager.IMPORTANCE_NONE);

            // Configure the notification channel.
            notificationChannel.setDescription("videoUploadChanel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.CYAN);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        videoUploadReceiver = new VideoUploadReceiver(new Handler())
                .setReceiver(new VideoUploadReceiver.Receiver() {
                    @Override
                    public void onReceiverResult(int resultCode, Bundle resultData) {
                        switch(resultCode) {
                            case UPLOAD_IN_PROGRESS_CODE:
                                builder.setProgress(100, resultData.getInt(UPLOAD_PROGRESS), false)
                                        .setContentText(String.valueOf(resultData.getInt(UPLOAD_PROGRESS) + "%"));
                                notifyProgressInNotification();
//                                Log.d(UPLOAD_PROGRESS, String.valueOf(resultData.getInt(UPLOAD_PROGRESS)));
                                break;
                            case UPLOAD_COMPLETE_CODE:
                                builder.setOngoing(false);
                                builder.setContentText("Finished!")
                                        .setProgress(100, 100, false);
                                notifyProgressInNotification();

                                finishVideoUploadSession(getApplicationContext());

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        notificationManager.cancel(0);
                                    }
                                }, 4000);

                                if(fragment instanceof PostsListFragment) {
                                    ((PostsListFragment)fragment).getHomePagePosts(1,false);
                                }
                                break;
                            case UPLOAD_ERROR_CODE:
                                String failedMessage = String.valueOf(resultData.getString(UPLOAD_ERROR));
                                Log.e(UPLOAD_ERROR, failedMessage != null ? failedMessage : "FAILED!!!");
                                builder.setOngoing(false);
                                builder.setContentText("Upload failed!")
                                        .setProgress(100, 0, false)
                                        .setContentText("");
                                notifyProgressInNotification();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        notificationManager.cancel(0);
                                    }
                                }, 4000);

                                break;
                            case REQUEST_CANCEL_UPLOAD:
                                builder.setOngoing(false);
                                Toast.makeText(BaseBottomBarActivity.this, "cancelled", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
    }

    private void checkIfAnyVideoIsUploading() {
        if (getVideoUploadSession(this) != null) {
            launchVideoUploadService(this, getVideoUploadSession(getApplicationContext()), videoUploadReceiver);
//            new ResumeUpload(this, getVideoUploadSession(getApplicationContext()), false).execute();
        }
        else if (getIntent().getParcelableExtra(UPLOAD_PARAMS) != null ) {
            launchVideoUploadService(this, (UploadParams) getIntent().getParcelableExtra(UPLOAD_PARAMS), videoUploadReceiver);
//            new ResumeUpload(this, (UploadParams) getIntent().getParcelableExtra(UPLOAD_PARAMS), true).execute();
        }
    }

    private void notifyProgressInNotification() {
        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }
    }

    @SuppressWarnings("unused")
    private static class ShowShareDialog extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<BaseBottomBarActivity> reference;

        ShowShareDialog(BaseBottomBarActivity context) {
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
    public void onBackPressed() {
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
                        switchTab(0);
                        updateTabSelection(0);
                        fragmentHistory.emptyStack();
                    } else {
                        super.onBackPressed();
                    }
                }
            }
        }
    }

    @Override
    public void viewUserProfile() {
        pushFragment(ProfileFragment.newInstance());
    }
}