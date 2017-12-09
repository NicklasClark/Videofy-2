package com.cncoding.teazer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cncoding.teazer.adapter.FollowersAdapter.OtherProfileListener;
import com.cncoding.teazer.adapter.FollowersCreationAdapter.FollowerCreationListener;
import com.cncoding.teazer.adapter.FollowingAdapter.OtherProfileListenerFollowing;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter.myCreationListener;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.apiCalls.ProgressRequestBody.UploadCallbacks;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.customViews.SignPainterTextView;
import com.cncoding.teazer.home.BaseFragment.FragmentNavigation;
import com.cncoding.teazer.home.discover.DiscoverFragment;
import com.cncoding.teazer.home.discover.DiscoverFragment.OnSearchInteractionListener;
import com.cncoding.teazer.home.discover.SubDiscoverFragment;
import com.cncoding.teazer.home.discover.adapters.SubDiscoverAdapter.OnSubSearchInteractionListener;
import com.cncoding.teazer.home.discover.adapters.TrendingListAdapter.TrendingListInteractionListener;
import com.cncoding.teazer.home.discover.search.DiscoverSearchAdapter.OnDiscoverSearchInteractionListener;
import com.cncoding.teazer.home.notifications.NotificationsAdapter.OnNotificationsInteractionListener;
import com.cncoding.teazer.home.notifications.NotificationsFragment;
import com.cncoding.teazer.home.post.PostDetailsFragment;
import com.cncoding.teazer.home.post.PostDetailsFragment.OnPostDetailsInteractionListener;
import com.cncoding.teazer.home.post.PostsListAdapter.OnPostAdapterInteractionListener;
import com.cncoding.teazer.home.post.PostsListFragment;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.home.profile.ProfileFragment.FollowerListListener;
import com.cncoding.teazer.tagsAndCategories.Interests.OnInterestsInteractionListener;
import com.cncoding.teazer.ui.fragment.activity.FollowersListActivity;
import com.cncoding.teazer.ui.fragment.activity.FollowingListActivities;
import com.cncoding.teazer.ui.fragment.activity.OthersProfileFragment;
import com.cncoding.teazer.utilities.FragmentHistory;
import com.cncoding.teazer.utilities.NavigationController;
import com.cncoding.teazer.utilities.NavigationController.RootFragmentListener;
import com.cncoding.teazer.utilities.NavigationController.TransactionListener;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.UploadParams;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.cncoding.teazer.utilities.ViewUtils;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody.Part;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_MOST_POPULAR;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_MY_INTERESTS;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_TRENDING;
import static com.cncoding.teazer.home.post.PostDetailsFragment.ACTION_DISMISS_PLACEHOLDER;
import static com.cncoding.teazer.home.post.PostDetailsFragment.ACTION_OPEN_REACTION_CAMERA;
import static com.cncoding.teazer.home.post.PostReactionAdapter.PostReactionAdapterListener;
import static com.cncoding.teazer.utilities.AuthUtils.getErrorMessage;
import static com.cncoding.teazer.utilities.NavigationController.TAB1;
import static com.cncoding.teazer.utilities.NavigationController.TAB2;
import static com.cncoding.teazer.utilities.NavigationController.TAB4;
import static com.cncoding.teazer.utilities.NavigationController.TAB5;
import static com.cncoding.teazer.utilities.SharedPrefs.finishVideoUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.getAuthToken;
import static com.cncoding.teazer.utilities.SharedPrefs.getVideoUploadSession;
import static com.cncoding.teazer.utilities.ViewUtils.UPLOAD_PARAMS;
import static com.cncoding.teazer.utilities.ViewUtils.deleteFileFromMediaStoreDatabase;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.launchReactionCamera;
import static com.cncoding.teazer.utilities.ViewUtils.launchVideoUploadCamera;

public class BaseBottomBarActivity extends BaseActivity
//    Navigation listeners
        implements FragmentNavigation, TransactionListener, RootFragmentListener, UploadCallbacks,
//    Post related listeners
        OnPostAdapterInteractionListener, OnPostDetailsInteractionListener, PostReactionAdapterListener, OnInterestsInteractionListener,
//    Discover page listeners
        OnDiscoverSearchInteractionListener, OnSearchInteractionListener, OnSubSearchInteractionListener, TrendingListInteractionListener,
//    Notification listeners
        OnNotificationsInteractionListener,
//    Profile listeners
        OtherProfileListener, FollowerListListener, myCreationListener, OtherProfileListenerFollowing, FollowerCreationListener {

    public static final int ACTION_VIEW_POST = 0;
    public static final int ACTION_VIEW_PROFILE = 123;
    public static final String TAB_INDEX = "tabIndex";
//    public static final int ACTION_VIEW_REACTION = 1;

//    private int[] mTabIconsDefault = {
//            R.drawable.ic_home_default,
//            R.drawable.ic_binoculars_default,
////            R.drawable.ic_add_video,
//            R.drawable.ic_notifications_default,
//            R.drawable.ic_person_default
//    };
//
//    private int[] mTabIconsSelected = {
//            R.drawable.ic_home_selected,
//            R.drawable.ic_binoculars_selected,
//            R.drawable.ic_add_video,
//            R.drawable.ic_notifications_selected,
//            R.drawable.ic_person_selected
//    };

    @BindArray(R.array.tab_name) String[] TABS;
    @BindView(R.id.app_bar) AppBarLayout appBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_center_title) SignPainterTextView toolbarCenterTitle;
    @BindView(R.id.toolbar_plain_title) ProximaNovaSemiboldTextView toolbarPlainTitle;
    @BindView(R.id.main_fragment_container) FrameLayout contentFrame;
    @BindView(R.id.bottom_tab_layout) TabLayout bottomTabLayout;
    @BindView(R.id.camera_btn) ImageButton cameraButton;
    @BindView(R.id.uploading_status_layout) LinearLayout uploadingStatusLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.uploading_notification) ProximaNovaBoldTextView uploadingNotificationTextView;

    @BindView(R.id.dismiss) AppCompatImageView uploadingNotificationDismiss;

    private NavigationController navigationController;
    private FragmentHistory fragmentHistory;
    private Call<ResultObject> uploadCall;
    private Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_bottom_bar);
        ButterKnife.bind(this);

        checkIfAnyVideoIsUploading();

        Log.d("AUTH_TOKEN", getAuthToken(getApplicationContext()) == null ? "N/A" : getAuthToken(getApplicationContext()));

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        appBar.addOnOffsetChangedListener(appBarOffsetChangeListener());

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
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (intent.getExtras() != null) {
//            int index = intent.getExtras().getBundle("bundle").getInt(TAB_INDEX);
//            if (index != -1)
//                switchTab(index);
//        }
//    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        initTab();
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras().getBundle("bundle");
            if (bundle != null) {
                int index = bundle.getInt(TAB_INDEX);
                switchTab(index);
            } else switchTab(0);
        } else {
            if (navigationController.getCurrentFragment() instanceof PostsListFragment)
                switchTab(0);
            else if (navigationController.getCurrentFragment() instanceof DiscoverFragment)
                switchTab(1);
            else if (navigationController.getCurrentFragment() instanceof NotificationsFragment)
                switchTab(3);
            else if (navigationController.getCurrentFragment() instanceof ProfileFragment)
                switchTab(4);
        }
    }

    private void checkIfAnyVideoIsUploading() {
        if (getVideoUploadSession(this) != null) {
            new ResumeUpload(this, getVideoUploadSession(getApplicationContext()), false).execute();
//            resumeUpload(getVideoUploadSession(this), false);
        }
        else if (getIntent().getParcelableExtra(UPLOAD_PARAMS) != null ) {
            new ResumeUpload(this, (UploadParams) getIntent().getParcelableExtra(UPLOAD_PARAMS), true).execute();
//            resumeUpload((UploadParams) getIntent().getParcelableExtra(UPLOAD_PARAMS), true);
        }
    }

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
    public void viewUserProfile() {
        pushFragment(ProfileFragment.newInstance());
    }

    private static class ResumeUpload extends AsyncTask<Void, Void, Part> {

        private final Callback<ResultObject> callback;
        private WeakReference<BaseBottomBarActivity> reference;
        private boolean isResuming;
        private UploadParams uploadParams;

        ResumeUpload(BaseBottomBarActivity context, final UploadParams uploadParams, boolean isResuming) {
            reference = new WeakReference<>(context);
            this.uploadParams = uploadParams;
            this.isResuming = isResuming;
            callback = new Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                    try {
                        if (response.code() == 201) {
                            final String s="https://s3.ap-south-1.amazonaws.com/teazer-medias/Teazer/post/2/4/1511202104939_thumb.png";
                            new ShowShareDialog(reference.get()).execute(s);
                            reference.get().onUploadFinish();
                            deleteFile(uploadParams.getVideoPath(), uploadParams.isGallery());
                        } else {
                            if (response.code() == 200) {
                                if (response.body().getMessage().contains("own video")) {
//                        USER IS REACTING ON HIS OWN VIDEO.

                                    reference.get().uploadingNotificationTextView.setText(response.body().getMessage());
                                    deleteFile(uploadParams.getVideoPath(), uploadParams.isGallery());
                                    finishVideoUploadSession(reference.get().getApplicationContext());
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            reference.get().uploadingStatusLayout.setVisibility(GONE);
                                        }
                                    }, 1000);
                                }
                            } else {
                                reference.get().onUploadError(new Throwable(getErrorMessage(response.errorBody())));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                private void deleteFile(String path, boolean isGallery) {
                    if (!isGallery) {
                        deleteFileFromMediaStoreDatabase(reference.get(), path);
                        //noinspection ResultOfMethodCallIgnored
                        new File(path).delete();
                    }
                }

                @Override
                public void onFailure(Call<ResultObject> call, Throwable t) {
                    reference.get().onUploadError(t);
                }
            };
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reference.get().uploadingStatusLayout.setVisibility(VISIBLE);
            reference.get().progressBar.setIndeterminate(false);
            try {
                if (!uploadParams.isReaction())
                    //                UPLOADING POST VIDEO
                    reference.get().uploadingNotificationTextView.setText(R.string.uploading_your_video);
                else
                    //                UPLOADING REACTION VIDEO
                    reference.get().uploadingNotificationTextView.setText(R.string.uploading_your_reaction);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Part doInBackground(Void... voids) {
            SharedPrefs.saveVideoUploadSession(reference.get(), uploadParams);

            File videoFile = new File(uploadParams.getVideoPath());
            ProgressRequestBody videoBody = new ProgressRequestBody(videoFile, reference.get());
            return Part.createFormData("video", videoFile.getName(), videoBody);
        }

        @Override
        protected void onPostExecute(Part part) {
            String title = uploadParams.getTitle();
            if (!uploadParams.isReaction()) {
//                UPLOADING POST VIDEO
                reference.get().uploadingNotificationTextView.setText(R.string.uploading_your_video);
                reference.get().uploadCall = ApiCallingService.Posts.uploadVideo(
                        part, title, uploadParams.getLocation(), uploadParams.getLatitude(),
                        uploadParams.getLongitude(), uploadParams.getTags(), uploadParams.getCategories(), reference.get());
            } else {
//                UPLOADING REACTION VIDEO
                reference.get().uploadingNotificationTextView.setText(R.string.uploading_your_reaction);
                reference.get().uploadCall = ApiCallingService.React.uploadReaction(part,
                        uploadParams.getPostDetails().getPostId(), reference.get(), title);
            }

            if (!reference.get().uploadCall.isExecuted())
                reference.get().uploadCall.enqueue(callback);

            if (uploadParams.isReaction() && isResuming) {
                reference.get().pushFragment(PostDetailsFragment.newInstance(uploadParams.getPostDetails(), null,
                        false, false));
            }
        }
    }

    @OnClick(R.id.uploading_notification) public void retryUpload() {
        if (uploadingNotificationTextView.getCompoundDrawables()[2] != null) {
            uploadingNotificationTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            new ResumeUpload(this, getVideoUploadSession(getApplicationContext()), false).execute();
//            resumeUpload(getVideoUploadSession(this), false);
        }
    }

    @OnClick(R.id.dismiss) public void cancelUpload() {
        if (uploadCall != null) {
            uploadCall.cancel();
        }
        finishVideoUploadSession(getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uploadingStatusLayout.setVisibility(GONE);
            }
        }, 1000);
    }

    @OnClick(R.id.camera_btn)
    public void startCamera() {
        launchVideoUploadCamera(this);
        finish();
    }

//    private void initTab() {
//        for (int i = 0; i < TABS.length; i++) {
//            bottomTabLayout.addTab(bottomTabLayout.newTab().setCustomView(getTabView(i)));
//        }
//    }

//    @SuppressLint("InflateParams")
//    private View getTabView(int position) {
//        ImageView view = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_tab_bottom, null);
//        StateListDrawable drawable = new StateListDrawable();
//        if (position != 2) {
//            drawable.addState(new int[]{android.R.attr.state_selected}, getDrawable(mTabIconsSelected[position]));
//            drawable.addState(new int[]{android.R.attr.state_enabled}, getDrawable(mTabIconsDefault[position]));
//            view.setImageDrawable(drawable);
//        } else
//            cameraButton.setImageDrawable(getDrawable(mTabIconsSelected[2]));
//        return view;
//    }

    private void switchTab(final int position) {
        navigationController.switchTab(position);
        updateBottomTabIconFocus(position);
//        updateToolbar(position == 1 || position == 3);
        if (position == 1 || position == 3)
            setAppBarElevation(0);
        else
            setAppBarElevation(6);
    }

    @SuppressWarnings("ConstantConditions")
    private void updateBottomTabIconFocus(int position) {
        for (int i = 0; i < bottomTabLayout.getTabCount(); i++) {
            if (i != 2) {
                if (i == position)
                    bottomTabLayout.getTabAt(position).getIcon().setTint(getResources().getColor(R.color.colorAccent));
                else
                    bottomTabLayout.getTabAt(i).getIcon().setTint(Color.parseColor("#999999"));
            }
        }
    }

//    public void updateToolbar(boolean isDiscoverPage) {
//        if (isDiscoverPage) {
//            if (toolbarPlainTitle.getVisibility() != VISIBLE)
//                toolbarPlainTitle.setVisibility(VISIBLE);
//            if (toolbarCenterTitle.getVisibility() != GONE)
//                toolbarCenterTitle.setVisibility(GONE);
//        }
//        else {
//            if (toolbarCenterTitle.getVisibility() != VISIBLE)
//                toolbarCenterTitle.setVisibility(VISIBLE);
//            if (toolbarPlainTitle.getVisibility() != GONE)
//                toolbarPlainTitle.setVisibility(GONE);
//        }
//    }

    public void setAppBarElevation(float elevation) {
        if (appBar.getElevation() != elevation)
            appBar.setElevation((int)((elevation * getResources().getDisplayMetrics().density) + 0.5));
    }

//    public void disappearSearchBar() {
//        if (toolbarCenterTitle.getVisibility() != GONE)
//            toolbarCenterTitle.setVisibility(GONE);
//        if (settings.getVisibility() != GONE)
//            settings.setVisibility(GONE);
//    }

//    public void reappearSearchBar() {
//        toolbarPlainTitle.setVisibility(VISIBLE);
//    }

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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (navigationController != null) {
            navigationController.onSaveInstanceState(outState);
        }
    }

    public void updateToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(!navigationController.isRootFragment());
            actionBar.setDisplayShowHomeEnabled(!navigationController.isRootFragment());
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (navigationController.getCurrentFragment() instanceof PostsListFragment && uploadCall != null) {
                    uploadingStatusLayout.setVisibility(VISIBLE);
                } else
                    uploadingStatusLayout.setVisibility(GONE);
            }
        }, 200);
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
//        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbarPlainTitle.getLayoutParams();
//        if (navigationController.isRootFragment())
//            params.setMarginStart(getPixel(14));
//        else
//            params.leftMargin = getPixel(20) - getPixel(38);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                toolbarPlainTitle.setLayoutParams(params);
//            }
//        }, 200);
    }

    public String getToolbarTitle() {
        return toolbarPlainTitle.getText().toString();
    }

    public AppBarLayout.OnOffsetChangedListener appBarOffsetChangeListener() {
        return new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                bottomTabLayout.setTranslationY((float) (-verticalOffset));
                cameraButton.setTranslationY((float) -(verticalOffset * 2));
//                int percentage = (Math.abs(verticalOffset)) * 100 / appBarLayout.getTotalScrollRange();
//                if (percentage > 0) {
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                } else
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        };
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

    @Override
    public void onPostInteraction(int action, final PostDetails postDetails, ImageView postThumbnail,
                                  RelativeLayout layout, final byte[] image) {
        switch (action) {
            case ACTION_VIEW_POST:
                pushFragment(PostDetailsFragment.newInstance(postDetails, image, true, true));
                break;
            case ACTION_VIEW_PROFILE:
                int postOwnerId=postDetails.getPostOwner().getUserId();
                String username=postDetails.getPostOwner().getUserName();
                String userType="";
                boolean candelete=postDetails.canDelete();
                if(candelete)
                    pushFragment(ProfileFragment.newInstance());

                else
                    pushFragment(OthersProfileFragment.newInstance(String.valueOf(postOwnerId),userType,username));
        }
    }

    @Override
    public void onPostDetailsInteraction(int action, PostDetails postDetails) {
        switch (action) {
            case ACTION_DISMISS_PLACEHOLDER:
                break;
            case ACTION_OPEN_REACTION_CAMERA:
                launchReactionCamera(this, postDetails);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPostReactionInteraction(int action, Pojos.Post.PostReaction postReaction) {
//        ViewUtils.playVideo(this, postReaction.getMediaDetail().getMediaUrl(), true);
    }

    @Override
    public void onSearchInteraction(int action, ArrayList<Category> categories, ArrayList<PostDetails> postDetailsArrayList,
                                    PostDetails postDetails, byte[] image) {
        switch (action) {
            case ACTION_VIEW_MY_INTERESTS:
                pushFragment(SubDiscoverFragment.newInstance(action, categories, postDetailsArrayList));
                break;
            case ACTION_VIEW_MOST_POPULAR:
                pushFragment(SubDiscoverFragment.newInstance(action, categories, null));
                break;
            case ACTION_VIEW_POST:
                pushFragment(PostDetailsFragment.newInstance(postDetails, image, true, false));
                break;
            case ACTION_VIEW_PROFILE:
                pushFragment(postDetails.canDelete() ? ProfileFragment.newInstance() :
                        OthersProfileFragment.newInstance(
                                String.valueOf(postDetails.getPostOwner().getUserId()), "", ""));
                break;
        }
    }

    @Override
    public void onSubSearchInteraction(int action, PostDetails postDetails) {
        switch (action) {
            case ACTION_VIEW_POST:
                pushFragment(PostDetailsFragment.newInstance(postDetails, null, true, false));
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
                                pushFragment(PostDetailsFragment.newInstance(response.body(), null, true,
                                        false));
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
    public void onNotificationsInteraction(boolean isFollowingTab, PostDetails postDetails, byte[] byteArrayFromImage,
                                           int profileId, String userType) {
        if (isFollowingTab) {
            pushFragment(PostDetailsFragment.newInstance(postDetails, null, true, false));
        } else {
            pushFragment(OthersProfileFragment.newInstance(String.valueOf(profileId),userType,"name"));
        }
    }

    @Override
    public void onInterestsInteraction() {
        DiscoverFragment.updateMyInterests = true;
        navigationController.popFragments(2);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressBar.setProgress(percentage);
    }

    @Override
    public void onUploadError(Throwable throwable) {
        uploadingNotificationTextView.setText(R.string.something_went_wrong);
        Log.e("onUploadError", throwable.getMessage() != null ? throwable.getMessage() : "FAILED!");
        uploadingNotificationTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_retry, 0);
    }

    @Override
    public void onUploadFinish() {
        uploadingNotificationTextView.setText(R.string.finished);
        uploadingNotificationTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick_circle, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uploadingStatusLayout.setVisibility(GONE);
            }
        }, 1000);
        finishVideoUploadSession(this);
        if(fragment instanceof PostsListFragment) {
            ((PostsListFragment)fragment).getHomePagePosts(1,false);
        }
        uploadCall = null;
    }

//    public void hideAppBar() {
//        appBar.setExpanded(false, true);
//    }

    public void showAppBar() {
        appBar.setExpanded(true, true);
    }

    public void hideBottomBar() {
        bottomTabLayout.animate().translationY(100).setDuration(280).setInterpolator(new DecelerateInterpolator()).start();
        cameraButton.animate().translationY(100).setDuration(280).setInterpolator(new DecelerateInterpolator()).start();
        bottomTabLayout.setVisibility(GONE);
        cameraButton.setVisibility(GONE);
    }

    public void showBottomBar() {
        bottomTabLayout.animate().translationY(0).setDuration(280).setInterpolator(new DecelerateInterpolator()).start();
        cameraButton.animate().translationY(0).setDuration(280).setInterpolator(new DecelerateInterpolator()).start();
        bottomTabLayout.setVisibility(VISIBLE);
        cameraButton.setVisibility(VISIBLE);
    }

    @Override
    public void myCreationVideos(int i, PostDetails postDetails) {
        pushFragment(PostDetailsFragment.newInstance(postDetails, null, true, false));
    }

//    public void hideSettings(boolean flag) {
//    }
//    public void hidereport() {
//    }
//    public void hidesettingsReport() {
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ViewUtils.unbindDrawables(contentFrame);
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
}