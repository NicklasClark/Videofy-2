package com.cncoding.teazer.ui.home.base;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.base.UploadParams;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.react.GiphyReactionRequest;
import com.cncoding.teazer.data.receiver.ReactionUploadReceiver;
import com.cncoding.teazer.data.receiver.VideoUploadReceiver;
import com.cncoding.teazer.ui.base.BaseViewModelActivity;
import com.cncoding.teazer.ui.base.FragmentNavigation;
import com.cncoding.teazer.ui.common.Interests.OnInterestsInteractionListener;
import com.cncoding.teazer.ui.customviews.coachMark.MaterialShowcaseView;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.discover.DiscoverFragment;
import com.cncoding.teazer.ui.home.discover.SubDiscoverFragment;
import com.cncoding.teazer.ui.home.notification.NotificationsFragment;
import com.cncoding.teazer.ui.home.post.detailspage.PostDetailsFragment;
import com.cncoding.teazer.ui.home.post.homepage.PostsListFragment;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewOtherProfile;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewProfile2;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentReactionPlayer;
import com.cncoding.teazer.utilities.asynctasks.AddWaterMarkAsyncTask;
import com.cncoding.teazer.utilities.asynctasks.AddWaterMarkAsyncTask.WatermarkAsyncResponse;
import com.cncoding.teazer.utilities.common.FragmentHistory;
import com.cncoding.teazer.utilities.common.NavigationController;
import com.cncoding.teazer.utilities.common.NavigationController.RootFragmentListener;
import com.cncoding.teazer.utilities.common.NavigationController.TransactionListener;
import com.cncoding.teazer.utilities.common.NavigationTransactionOptions;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.data.service.ReactionUploadService.launchReactionUploadService;
import static com.cncoding.teazer.data.service.VideoUploadService.ADD_WATERMARK;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_COMPLETE_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_ERROR_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_IN_PROGRESS_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_PROGRESS;
import static com.cncoding.teazer.data.service.VideoUploadService.VIDEO_PATH;
import static com.cncoding.teazer.data.service.VideoUploadService.launchVideoUploadService;
import static com.cncoding.teazer.ui.home.base.IntentHandler.getBranchDynamicLinks;
import static com.cncoding.teazer.ui.home.base.IntentHandler.getBroadcastReceiver;
import static com.cncoding.teazer.ui.home.base.IntentHandler.handleNewIntent;
import static com.cncoding.teazer.ui.home.base.IntentHandler.handleNotificationIntentIfAny;
import static com.cncoding.teazer.ui.home.base.IntentHandler.logMobiruckEvent;
import static com.cncoding.teazer.ui.home.base.IntentHandler.mayeShowCoachMarks;
import static com.cncoding.teazer.utilities.common.CommonUtilities.deleteFilePermanently;
import static com.cncoding.teazer.utilities.common.NavigationController.TAB1;
import static com.cncoding.teazer.utilities.common.NavigationController.TAB2;
import static com.cncoding.teazer.utilities.common.NavigationController.TAB3;
import static com.cncoding.teazer.utilities.common.NavigationController.TAB4;
import static com.cncoding.teazer.utilities.common.NavigationController.TAB5;
import static com.cncoding.teazer.utilities.common.SharedPrefs.finishReactionUploadSession;
import static com.cncoding.teazer.utilities.common.SharedPrefs.finishVideoUploadSession;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getFollowingNotificationCount;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getReactionUploadSession;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getRequestNotificationCount;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getVideoUploadSession;
import static com.cncoding.teazer.utilities.common.SharedPrefs.isActivityActive;
import static com.cncoding.teazer.utilities.common.SharedPrefs.onActivityActive;
import static com.cncoding.teazer.utilities.common.SharedPrefs.onActivityInactive;
import static com.cncoding.teazer.utilities.common.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.common.ViewUtils.getTabChild;
import static com.cncoding.teazer.utilities.common.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.common.ViewUtils.launchVideoUploadCamera;

public class BaseBottomBarActivity extends BaseViewModelActivity
//    Navigation listeners
        implements FragmentNavigation, TransactionListener, RootFragmentListener,
//    Category/Interests listener
        OnInterestsInteractionListener,
//    Profile listeners
        WatermarkAsyncResponse, TabLayout.OnTabSelectedListener {

    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String SOURCE_ID = "source_id";
    public static final String POST_ID = "post_id";
    public static final int REQUEST_CANCEL_UPLOAD = 45;
    public static final int COACH_MARK_DELAY = 1000;

    @BindArray(R.array.tab_name) String[] TABS;
    @BindView(R.id.main_fragment_container) FrameLayout contentFrame;
    @BindView(R.id.root_layout) RelativeLayout rootLayout;
    @BindView(R.id.blur_view) BlurView blurView;
    @BindView(R.id.bottom_tab_layout) TabLayout bottomTabLayout;
    @BindView(R.id.camera_btn) FloatingActionButton cameraButton;
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
    PostDetails postDetails;

    @Contract(value = " -> !null", pure = true)
    private BaseBottomBarActivity getThis() {
        return this;
    }

    //region Lifecycle methods
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

        logMobiruckEvent();
        setupBlurView();
        initializeNavigationController(savedInstanceState);

        bottomTabLayout.addOnTabSelectedListener(this);
        fuckUpThirdTab();

        BReceiver = getBroadcastReceiver(getThis());
        getBranchDynamicLinks(getThis());
        setNotificationsBadgeIfAny(getFollowingNotificationCount(this) + getRequestNotificationCount(this));
        switchTab(TAB1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onActivityActive(this);
        checkIfAnyVideoOrReactionIsUploading();
        LocalBroadcastManager.getInstance(this).registerReceiver(BReceiver, new IntentFilter("message"));
    }

    protected void onPause() {
        super.onPause();
        onActivityInactive(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(BReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        handleNotificationIntentIfAny(getThis());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getBranchDynamicLinks(getThis());
        Log.d("NOTIFY", "onNewIntent called");
        handleNewIntent(getThis(), intent);
    }
    //endregion

    //region Initialization code
    private void setupBlurView() {
        blurView.setupWith(rootLayout)
                .windowBackground(getWindow().getDecorView().getBackground())
                .blurAlgorithm(new RenderScriptBlur(this))
                .blurRadius(20);
    }

    private void initializeNavigationController(Bundle savedInstanceState) {
        fragmentHistory = new FragmentHistory();
        navigationController = NavigationController
                .newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.main_fragment_container)
                .activity(this)
                .defaultTransactionOptions(new NavigationTransactionOptions.Builder()
                        .customAnimations(R.anim.fast_fade_in, R.anim.fast_fade_out, R.anim.fast_fade_in, R.anim.fast_fade_out)
                        .build())
                .transactionListener(this)
                .rootFragmentListener(this, TABS.length)
                .build();
    }

    private void fuckUpThirdTab() {
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
    }

    private void setNotificationsBadgeIfAny(int unreadNotificationCount) {
        TabLayout.Tab tab = bottomTabLayout.getTabAt(3);
        if (tab != null) {
            tab.setCustomView(null);
            tab.setCustomView(getTabView(unreadNotificationCount));
        }
    }
    //endregion

    //region Fragment navigation methods
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        fragmentHistory.push(tab.getPosition());
        switchTab(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        navigationController.clearStack();
        switchTab(tab.getPosition());
        maybeRefreshTab(tab);
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
                        ((DiscoverFragment) navigationController.getCurrentFragment()).nestedScrollView.scrollTo(0, 0);
                        navigationController.replaceFragment(getRootFragment(TAB2));
                        DiscoverFragment.scrollPosition = 0;
                    }
                    break;
                case TAB4:
                    if (navigationController.getCurrentFragment() instanceof NotificationsFragment) {
                        navigationController.replaceFragment(getRootFragment(TAB4));
                    }
                    break;
                case TAB5:
                    if (navigationController.getCurrentFragment() instanceof FragmentNewProfile2) {
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

    void switchTabDynamically() {
        if (navigationController.getCurrentFragment() instanceof PostsListFragment)
            switchTab(TAB1);
        else if (navigationController.getCurrentFragment() instanceof DiscoverFragment)
            switchTab(TAB2);
        else if (navigationController.getCurrentFragment() instanceof NotificationsFragment)
            switchTab(TAB4);
        else if (navigationController.getCurrentFragment() instanceof FragmentNewProfile2)
            switchTab(TAB5);
    }

    private void switchTab(final int position) {
        navigationController.switchTab(position);
        updateBottomTabIconFocus(position);
        try {
//            clear the notification count badge if notifications are there
            if (navigationController.getCurrentFragment() instanceof NotificationsFragment) clearNotificationBadge();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mayeShowCoachMarks(getThis(), position, currentFragment);
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
                }
            }
        }
    }

    public void updateToolbar() {
        if (navigationController.isRootFragment()) {
            btnToolbarBack.setVisibility(GONE);
        } else {
            if (!(navigationController.getCurrentFragment() instanceof FragmentNewProfile2)) {
                btnToolbarBack.setVisibility(VISIBLE);
                if (navigationController.getCurrentFragment() instanceof FragmentNewOtherProfile ||
                        navigationController.getCurrentFragment() instanceof PostDetailsFragment ||
                        navigationController.getCurrentFragment() instanceof FragmentReactionPlayer)
                    btnToolbarBack.setImageResource(R.drawable.ic_arrow_back_white_with_shadow);
                else btnToolbarBack.setImageResource(R.drawable.ic_arrow_back_dark);
            }
            else btnToolbarBack.setVisibility(GONE);
        }
        toggleBottomBar(!(navigationController.getCurrentFragment() instanceof PostDetailsFragment));
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

    @Override
    public void onTabTransaction(Fragment fragment, int index) {
        // If we have a backStack, show the back button
        if (navigationController != null) {
            updateToolbar();
        }
    }

    @Override
    public void onFragmentTransaction(Fragment fragment, NavigationController.TransactionType transactionType) {
        //Do fragment stuff. Maybe change title.
        // If we have a backStack, show the back button
        if (navigationController != null) {
            updateToolbar();
        }
    }

    @Override
    public void pushFragment(final Fragment fragment) {
        if (navigationController != null) {
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
                //currentFragment = ProfileFragment.newInstance();
                currentFragment = FragmentNewProfile2.newInstance();
                break;
            default:
                currentFragment = PostsListFragment.newInstance();
                break;
        }
        return currentFragment;
    }

    public void clearNotificationBadge() {
        try {
            TabLayout.Tab tab = bottomTabLayout.getTabAt(3);
            if (tab != null) tab.setCustomView(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    //endregion

    //region On click methods
    @OnClick(R.id.camera_btn) public void startCamera() {
        launchVideoUploadCamera(this);
        finish();
    }

    @OnClick(R.id.btnToolbarBack) public void onViewClicked() {
        hideKeyboard(this, btnToolbarBack);
        onBackPressed();
    }
    //endregion

    //region Video upload handler
    private void checkIfAnyVideoOrReactionIsUploading() {
        UploadParams videoUploadParams = getVideoUploadSession(getApplicationContext());
        if (videoUploadParams != null) {
            setupVideoUploadServiceReceiver(videoUploadParams);
            launchVideoUploadService(this, videoUploadParams, videoUploadReceiver);
        }
        UploadParams reactionUploadParams = getReactionUploadSession(getApplicationContext());
        if (reactionUploadParams != null && !(navigationController.getCurrentFragment() instanceof PostDetailsFragment)) {
            if (!reactionUploadParams.isGiphy()) {
                setupReactionUploadServiceReceiver(reactionUploadParams);
                launchReactionUploadService(this, reactionUploadParams, reactionUploadReceiver);
            } else {
                postGiphyReaction(reactionUploadParams);
            }
        }
    }

    private void setupVideoUploadServiceReceiver(UploadParams videoUploadParams) {
        uploadingStatusLayout.setVisibility(VISIBLE);
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
                            if (isActivityActive(getThis())) {
                                if (navigationController.getCurrentFragment() instanceof PostsListFragment) {
                                    uploadProgressText.setText(String.valueOf(getString(R.string.uploading_your_video) +
                                            BLANK_SPACE + resultData.getInt(UPLOAD_PROGRESS) + "%"));
                                    videoUploadProgress.setProgress(resultData.getInt(UPLOAD_PROGRESS));
                                } else uploadingStatusLayout.setVisibility(GONE);
                            }
                            break;
                        case UPLOAD_COMPLETE_CODE:
                            if (isActivityActive(getThis())) {
                                if (navigationController.getCurrentFragment() instanceof PostsListFragment) {
                                    uploadProgressText.setText(R.string.finished);
                                    videoUploadProgress.setVisibility(GONE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                uploadingStatusLayout.setVisibility(GONE);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 2000);
                                    refreshPosts();
                                }
                                else uploadingStatusLayout.setVisibility(GONE);
                            }
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
                            if (isActivityActive(getThis())) {
                                if (navigationController.getCurrentFragment() instanceof PostsListFragment) {
                                    uploadProgressText.setText(R.string.failed);
                                    videoUploadProgress.setVisibility(GONE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                uploadingStatusLayout.setVisibility(GONE);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 2000);
                                } else uploadingStatusLayout.setVisibility(GONE);
                            }
                            finishVideoUploadSession(getApplicationContext());
                            break;
                        case REQUEST_CANCEL_UPLOAD:
                            if (isActivityActive(getThis())) {
                                if (navigationController.getCurrentFragment() instanceof PostsListFragment)
                                    Toast.makeText(getThis(), R.string.upload_cancelled, Toast.LENGTH_SHORT).show();
                                else uploadingStatusLayout.setVisibility(GONE);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void setupReactionUploadServiceReceiver(UploadParams reactionUploadParams) {
        uploadingStatusLayout.setVisibility(VISIBLE);
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
                            if (navigationController.getCurrentFragment() instanceof PostsListFragment) {
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
                                        try {
                                            uploadingStatusLayout.setVisibility(GONE);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 2000);
                                refreshPosts();
                            } else uploadingStatusLayout.setVisibility(GONE);
                            finishReactionUploadSession(getApplicationContext());
                            //add watermark for local creations/reactions
                            if (resultData.getBoolean(ADD_WATERMARK)) {
                                AddWaterMarkAsyncTask addWaterMarkAsyncTask = new AddWaterMarkAsyncTask(BaseBottomBarActivity.this);
                                addWaterMarkAsyncTask.delegate = BaseBottomBarActivity.this;
                                addWaterMarkAsyncTask.execute(resultData.getString(VIDEO_PATH));
                            }
                            else deleteFilePermanently(resultData.getString(VIDEO_PATH));
                            break;
                        case UPLOAD_ERROR_CODE:
                            if (isActivityActive(getThis())) {
                                if (navigationController.getCurrentFragment() instanceof PostsListFragment) {
                                    uploadProgressText.setText(R.string.failed);
                                    reactionUploadProgress.setVisibility(GONE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                uploadingStatusLayout.setVisibility(GONE);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, 2000);
                                } else uploadingStatusLayout.setVisibility(GONE);
                            }
                            finishReactionUploadSession(getApplicationContext());
                            break;
                        case REQUEST_CANCEL_UPLOAD:
                            if (isActivityActive(getThis())) {
                                if (navigationController.getCurrentFragment() instanceof PostsListFragment)
                                    Toast.makeText(getThis(), R.string.upload_cancelled, Toast.LENGTH_SHORT).show();
                                else uploadingStatusLayout.setVisibility(GONE);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void postGiphyReaction(UploadParams uploadParams) {
        GiphyReactionRequest giphyReactionRequest = new GiphyReactionRequest(uploadParams.getPostId(),
                uploadParams.getTitle(),
                uploadParams.getVideoPath());
        ApiCallingService.React.createReactionByGiphy(giphyReactionRequest, this).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject resultObject = response.body();
                if (resultObject != null && resultObject.getCode() == 200)
                    Toast.makeText(BaseBottomBarActivity.this, "Reaction posted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(BaseBottomBarActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
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
    //endregion

    //region Fragment listeners
    @Override
    public void onInterestsInteraction(final boolean isFromDiscover, final ArrayList<Category> categories) {
        if (currentFragment instanceof DiscoverFragment)
            ((DiscoverFragment) currentFragment).getLandingPosts().setUserInterests(categories);
        SubDiscoverFragment.updateUserInterests(categories);
        popFragment();
    }

    @Override
    public void onInterestsSelected(String resultToShow, String resultToSend, int count) {}

    @Override
    public void waterMarkProcessFinish(String destinationPath, String sourcePath) {
        deleteFilePermanently(sourcePath);
    }
    //endregion

    @Override
    public void onBackPressed() {
        if (materialShowcaseView != null) {
            materialShowcaseView.hide();
            materialShowcaseView = null;
        } else {
            if (!navigationController.isRootFragment()) {
                popFragment();
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