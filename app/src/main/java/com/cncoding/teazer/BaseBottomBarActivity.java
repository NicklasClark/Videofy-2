package com.cncoding.teazer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.adapter.FollowersAdapter;
import com.cncoding.teazer.adapter.FollowersCreationAdapter;
import com.cncoding.teazer.adapter.FollowingAdapter;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.customViews.SignPainterTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.home.notifications.NotificationsAdapter;
import com.cncoding.teazer.home.notifications.NotificationsFragment;
import com.cncoding.teazer.home.post.PostDetailsFragment;
import com.cncoding.teazer.home.post.PostDetailsFragment.OnPostDetailsInteractionListener;
import com.cncoding.teazer.home.post.PostsListAdapter.OnPostAdapterInteractionListener;
import com.cncoding.teazer.home.post.PostsListFragment;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.home.search.SearchFragment;
import com.cncoding.teazer.home.search.SearchFragment.OnSearchInteractionListener;
import com.cncoding.teazer.home.search.SubSearchFragment;
import com.cncoding.teazer.home.search.adapters.SubSearchAdapter.OnSubSearchInteractionListener;
import com.cncoding.teazer.home.search.adapters.TrendingListAdapter.TrendingListInteractionListener;
import com.cncoding.teazer.ui.fragment.activity.EditProfile;
import com.cncoding.teazer.ui.fragment.activity.FollowerFollowingProfileActivity;
import com.cncoding.teazer.ui.fragment.activity.FollowersListActivity;
import com.cncoding.teazer.ui.fragment.activity.FollowingListActivities;
import com.cncoding.teazer.ui.fragment.activity.Settings;
import com.cncoding.teazer.utilities.FragmentHistory;
import com.cncoding.teazer.utilities.NavigationController;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.UploadParams;
import com.cncoding.teazer.utilities.Pojos.User.Profile;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.facebook.FacebookSdk;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.net.URL;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.home.post.PostDetailsFragment.ACTION_DISMISS_PLACEHOLDER;
import static com.cncoding.teazer.home.post.PostDetailsFragment.ACTION_OPEN_REACTION_CAMERA;
import static com.cncoding.teazer.home.post.PostReactionAdapter.PostReactionAdapterListener;
import static com.cncoding.teazer.home.search.SearchFragment.ACTION_VIEW_TRENDING;
import static com.cncoding.teazer.utilities.NavigationController.TAB1;
import static com.cncoding.teazer.utilities.NavigationController.TAB2;
import static com.cncoding.teazer.utilities.NavigationController.TAB4;
import static com.cncoding.teazer.utilities.NavigationController.TAB5;
import static com.cncoding.teazer.utilities.SharedPrefs.finishVideoUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.getAuthToken;
import static com.cncoding.teazer.utilities.SharedPrefs.getVideoUploadSession;
import static com.cncoding.teazer.utilities.ViewUtils.UPLOAD_PARAMS;
import static com.cncoding.teazer.utilities.ViewUtils.deleteFileFromMediaStoreDatabase;
import static com.cncoding.teazer.utilities.ViewUtils.launchReactionCamera;
import static com.cncoding.teazer.utilities.ViewUtils.launchVideoUploadCamera;

public class BaseBottomBarActivity extends BaseActivity
        implements BaseFragment.FragmentNavigation,
        NavigationController.TransactionListener,
        NavigationController.RootFragmentListener,
        OnPostAdapterInteractionListener, OnPostDetailsInteractionListener,
        PostReactionAdapterListener,
        OnSearchInteractionListener, OnSubSearchInteractionListener, TrendingListInteractionListener,
        NotificationsAdapter.OnNotificationsInteractionListener, ProgressRequestBody.UploadCallbacks,FollowersAdapter.OtherProfileListener,
        ProfileFragment.FollowerListListener,
        ProfileMyCreationAdapter.myCreationListener,FollowingAdapter.OtherProfileListenerFollowing,FollowersCreationAdapter.FollowerCreationListener {

    public static final int ACTION_VIEW_POST = 0;
    public static final int ACTION_VIEW_REACTION = 1;
    public static final int ACTION_VIEW_PROFILE = 2;

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
    @BindView(R.id.settings) ImageView settings;
    @BindView(R.id.report) ImageView report;
    @BindView(R.id.dismiss) AppCompatImageView uploadingNotificationDismiss;

    private NavigationController navigationController;
    private FragmentHistory fragmentHistory;
    private Call<ResultObject> uploadCall;
    private Callback<ResultObject> callback;
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

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseBottomBarActivity.this, Settings.class);


                intent.putExtra("AccountType", String.valueOf(1));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        initTab();
        switchTab(0);
    }

    private void checkIfAnyVideoIsUploading() {
        if (getVideoUploadSession(this) != null) {
            new ResumeUpload(this, getVideoUploadSession(getApplicationContext()), false).execute();
//            resumeUpload(getVideoUploadSession(this), false);
        } else if ((UploadParams) getIntent().getParcelableExtra(UPLOAD_PARAMS) != null) {
            new ResumeUpload(this, (UploadParams) getIntent().getParcelableExtra(UPLOAD_PARAMS), true).execute();
//            resumeUpload((UploadParams) getIntent().getParcelableExtra(UPLOAD_PARAMS), true);
        }
    }

    private void defineUploadCallback(final UploadParams uploadParams) {
        callback = new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                if (response.code() == 201) {


                  //      ShareDialog shareDialog;
                 //       FacebookSdk.sdkInitialize(getApplicationContext());
                     //   shareDialog = new ShareDialog(BaseBottomBarActivity.this);

//                        Uri videoFileUri = Uri.parse("https://www.youtube.com/watch?v=jBfo87raroE");
//                        ShareVideo shareVideo = new ShareVideo.Builder()
//                                .setLocalUrl(videoFileUri)
//                                .build();
//                        ShareVideoContent content = new ShareVideoContent.Builder()
//                                .setVideo(shareVideo)
//                                .build();
//
//                        shareDialog.show(content);


                        final String s = "https://s3.ap-south-1.amazonaws.com/teazer-medias/Teazer/post/2/4/1511202104939_thumb.png";
                        new AsyncTask<Void, Void, Bitmap>() {
                            @Override
                            protected Bitmap doInBackground(final Void... params) {
                                Bitmap bitmap = null;
                                try {
                                    final URL url = new URL(s);
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
                            protected void onPostExecute(final Bitmap result) {

                                SharePhoto photo = new SharePhoto.Builder()
                                        .setBitmap(result)
                                        .build();
                                SharePhotoContent content = new SharePhotoContent.Builder()
                                        .addPhoto(photo)
                                        .build();

                                ShareDialog shareDialog = new ShareDialog(BaseBottomBarActivity.this);
                                shareDialog.show(content);
                                 ShareApi.share(content, null);

                            }
                        }.execute();


                        // Bitmap image = ...

//
//                        Uri videoFileUri = Uri.parse("https://s3.ap-south-1.amazonaws.com/teazer-medias/Teazer/post/2/4/1511202104939.mp4");
//                        ShareVideo video = new ShareVideo.Builder()
//                                .setLocalUrl(videoFileUri)
//                                .build();
//                        ShareVideoContent content = new ShareVideoContent.Builder()
//                                .setVideo(video)
//                                .build();
//                        ShareDialog shareDialog = new ShareDialog(BaseBottomBarActivity.this);
//                        shareDialog.show(content);

                    onUploadFinish();

                    deleteFile(uploadParams.getVideoPath(), uploadParams.isGallery());
                } else {
                    if (response.code() == 200) {
                        if (response.body().getMessage().contains("own video")) {
//                        USER IS REACTING ON HIS OWN VIDEO.


                            uploadingNotificationTextView.setText(response.body().getMessage());
                            deleteFile(uploadParams.getVideoPath(), uploadParams.isGallery());
                            finishVideoUploadSession(getApplicationContext());
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    uploadingStatusLayout.setVisibility(GONE);
                                }
                            }, 1000);
                        }
                        return;
                    }
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void deleteFile(String path, boolean isGallery) {
                if (!isGallery) {
                    deleteFileFromMediaStoreDatabase(BaseBottomBarActivity.this, path);
                    //noinspection ResultOfMethodCallIgnored
                    new File(path).delete();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                onUploadError(t);
            }
        };
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
        pushFragment(FollowerFollowingProfileActivity.newInstance(id, type,username));

    }

    @Override
    public void viewOthersProfileFollowing(String id, String username, String type) {

        pushFragment(FollowerFollowingProfileActivity.newInstance2(id, type,username));

    }


    private static class ResumeUpload extends AsyncTask<Void, Void, Callback<ResultObject>> {

        private WeakReference<BaseBottomBarActivity> reference;
        private boolean isResuming;
        private UploadParams uploadParams;
        private MultipartBody.Part videoPartFile;

        ResumeUpload(BaseBottomBarActivity context, UploadParams uploadParams, boolean isResuming) {
            reference = new WeakReference<>(context);
            this.uploadParams = uploadParams;
            this.isResuming = isResuming;
            reference.get().defineUploadCallback(uploadParams);
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
        protected Callback<ResultObject> doInBackground(Void... voids) {
            SharedPrefs.saveVideoUploadSession(reference.get(), uploadParams);

            File videoFile = new File(uploadParams.getVideoPath());
            ProgressRequestBody videoBody = new ProgressRequestBody(videoFile, reference.get());
            videoPartFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
            String title = uploadParams.getTitle();
            if (!uploadParams.isReaction()) {
//                UPLOADING POST VIDEO
                reference.get().uploadingNotificationTextView.setText(R.string.uploading_your_video);
                reference.get().uploadCall = ApiCallingService.Posts.uploadVideo(
                        videoPartFile, title, uploadParams.getLocation(), uploadParams.getLatitude(),
                        uploadParams.getLongitude(), uploadParams.getTags(), uploadParams.getCategories(), reference.get());
            } else {
//                UPLOADING REACTION VIDEO
                reference.get().uploadingNotificationTextView.setText(R.string.uploading_your_reaction);
                reference.get().uploadCall = ApiCallingService.React.uploadReaction(videoPartFile,
                        uploadParams.getPostDetails().getPostId(), reference.get(), title);
            }
            return reference.get().callback;
        }

        @Override
        protected void onPostExecute(Callback<ResultObject> resultObjectCallback) {
            super.onPostExecute(resultObjectCallback);
            String title = uploadParams.getTitle();
            if (!uploadParams.isReaction()) {
//                UPLOADING POST VIDEO
                reference.get().uploadingNotificationTextView.setText(R.string.uploading_your_video);
                reference.get().uploadCall = ApiCallingService.Posts.uploadVideo(
                        videoPartFile, title, uploadParams.getLocation(), uploadParams.getLatitude(),
                        uploadParams.getLongitude(), uploadParams.getTags(), uploadParams.getCategories(), reference.get());
            } else {
//                UPLOADING REACTION VIDEO
                reference.get().uploadingNotificationTextView.setText(R.string.uploading_your_reaction);
                reference.get().uploadCall = ApiCallingService.React.uploadReaction(videoPartFile,
                        uploadParams.getPostDetails().getPostId(), reference.get(), title);
            }
            reference.get().uploadCall.enqueue(resultObjectCallback);

            if (uploadParams.isReaction() && isResuming) {
                reference.get().pushFragment(PostDetailsFragment.newInstance(uploadParams.getPostDetails(), null));
            }
        }
    }

    @OnClick(R.id.uploading_notification)
    public void retryUpload() {
        if (uploadingNotificationTextView.getCompoundDrawables()[2] != null) {
            uploadingNotificationTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            new ResumeUpload(this, getVideoUploadSession(getApplicationContext()), false).execute();
//            resumeUpload(getVideoUploadSession(this), false);
        }
    }

    @OnClick(R.id.dismiss)
    public void cancelUpload() {
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
        updateTabFocus(position);
        updateIfDiscoverToolbar(position == 1);
    }

    @SuppressWarnings("ConstantConditions")
    private void updateTabFocus(int position) {
        for (int i = 0; i < bottomTabLayout.getTabCount(); i++) {
            if (i != 2) {
                if (i == position)
                    bottomTabLayout.getTabAt(position).getIcon().setTint(Color.parseColor("#26C6DA"));
                else
                    bottomTabLayout.getTabAt(i).getIcon().setTint(Color.parseColor("#333333"));
            }
        }
    }

    public void updateIfDiscoverToolbar(boolean isDiscoverPage) {
        if (isDiscoverPage && navigationController.isRootFragment()) {
            if (appBar.getElevation() != 0.0)
            appBar.setElevation(0.0f);
            if (toolbarPlainTitle.getVisibility() != VISIBLE) {
                updateToolbarTitle(getString(R.string.discover));
                toolbarPlainTitle.setVisibility(VISIBLE);
            }
            if (toolbarCenterTitle.getVisibility() != GONE)
                toolbarCenterTitle.setVisibility(GONE);
        } else {
            if (appBar.getElevation() != 12.0)
                appBar.setElevation(12.0f);
            if (toolbarCenterTitle.getVisibility() != VISIBLE)
                toolbarCenterTitle.setVisibility(VISIBLE);
            if (toolbarPlainTitle.getVisibility() != GONE) {
                toolbarPlainTitle.setVisibility(GONE);
            }
        }
    }

//    public void disappearSearchBar() {
//        if (toolbarCenterTitle.getVisibility() != GONE)
//            toolbarCenterTitle.setVisibility(GONE);
//        if (settings.getVisibility() != GONE)
//            settings.setVisibility(GONE);
//    }
//
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
    }

    /**
     * Updates the toolbar title.
     *
     * @param title The title to be set, if null is passed, then "Teazer" will be set in SignPainter font in the center.
     */
    public void updateToolbarTitle(String title) {
        toolbarPlainTitle.setText(title);
        if (toolbarPlainTitle.getVisibility() != VISIBLE)
            toolbarPlainTitle.setVisibility(VISIBLE);
        if (toolbarCenterTitle.getVisibility() != GONE)
            toolbarCenterTitle.setVisibility(GONE);
    }

    public String getToolbarTitle() {
        return toolbarPlainTitle.getText().toString();
    }

    public AppBarLayout.OnOffsetChangedListener appBarOffsetChangeListener() {
        return new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                bottomTabLayout.setTranslationY((float) (-verticalOffset));
                cameraButton.setTranslationY((float) -(verticalOffset * 1.6));
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

    @Override
    public Fragment getRootFragment(int index) {
        fragment = null;
        switch (index) {
            case TAB1:
                fragment = PostsListFragment.newInstance();
                break;
            case TAB2:
                fragment = SearchFragment.newInstance();
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
                pushFragment(PostDetailsFragment.newInstance(postDetails, image));
                break;
            case ACTION_VIEW_PROFILE:
                pushFragment(new ProfileFragment());
        }
    }

    @Override
    public void onPostDetailsInteraction(int action, PostDetails postDetails) {
        switch (action) {
            case ACTION_DISMISS_PLACEHOLDER:
//                expandedImage.animate().alpha(0).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        expandedImage.setImageDrawable(null);
//                    }
//                }, 250);
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
    }

    @Override
    public void onSearchInteraction(int action, ArrayList<Category> categories, ArrayList<PostDetails> postDetailsArrayList) {
        pushFragment(SubSearchFragment.newInstance(action, categories, postDetailsArrayList));
    }

    @Override
    public void onSubSearchInteraction(PostDetails postDetails, byte[] byteArrayFromImage) {
        pushFragment(PostDetailsFragment.newInstance(postDetails, byteArrayFromImage));
    }

    @Override
    public void onTrendingListInteraction(int categoryId) {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category(categoryId, null));
        pushFragment(SubSearchFragment.newInstance(ACTION_VIEW_TRENDING, categories, null));
    }

    @Override
    public void onNotificationsInteraction(boolean isFollowingTab, PostDetails postDetails, byte[] byteArrayFromImage, Profile profile) {
        if (isFollowingTab) {
            pushFragment(PostDetailsFragment.newInstance(postDetails, null));
        } else {
            pushFragment(ProfileFragment.newInstance());
            Toast.makeText(this, "User Profile fetched, only need to populate it now.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressBar.setProgress(percentage);
    }

    @Override
    public void onUploadError(Throwable throwable) {
        uploadingNotificationTextView.setText(throwable.getMessage());
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
        if(fragment instanceof PostsListFragment)
        {
            ((PostsListFragment)fragment).getHomePagePosts(1,false);
        }

    }

    public void hideAppBar() {
        appBar.setExpanded(false, true);
    }

    public void showAppBar() {
        appBar.setExpanded(true, true);
    }

    @Override
    public void myCreationVideos(int i, PostDetails postDetails) {
        pushFragment(PostDetailsFragment.newInstance(postDetails, null));
    }

    public void hideSettings(boolean flag) {

            settings.setVisibility(View.GONE);
            report.setVisibility(VISIBLE);


    }
    public void hidereport()
    {
        settings.setVisibility(View.VISIBLE);
        report.setVisibility(GONE);
    }

    public void hidesettingsReport()
    {
        settings.setVisibility(View.GONE);
        report.setVisibility(GONE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        callback = null;
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