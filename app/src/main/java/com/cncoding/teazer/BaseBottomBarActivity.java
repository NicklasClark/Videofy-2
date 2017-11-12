package com.cncoding.teazer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cncoding.teazer.customViews.SignPainterTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.home.notifications.NotificationsFragment;
import com.cncoding.teazer.home.post.PostDetailsFragment;
import com.cncoding.teazer.home.post.PostDetailsFragment.OnPostDetailsInteractionListener;
import com.cncoding.teazer.home.post.PostsListAdapter.OnPostAdapterInteractionListener;
import com.cncoding.teazer.home.post.PostsListFragment;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.home.search.SearchFragment;
import com.cncoding.teazer.utilities.BottomBarUtils;
import com.cncoding.teazer.utilities.FragmentHistory;
import com.cncoding.teazer.utilities.NavigationController;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.SharedPrefs;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.home.post.PostDetailsFragment.ACTION_DISMISS_PLACEHOLDER;
import static com.cncoding.teazer.home.post.PostReactionAdapter.PostReactionAdapterListener;
import static com.cncoding.teazer.utilities.AuthUtils.logout;
import static com.cncoding.teazer.utilities.ViewUtils.launchVideoUploadCamera;

public class BaseBottomBarActivity extends BaseActivity
        implements BaseFragment.FragmentNavigation,
        NavigationController.TransactionListener,
        NavigationController.RootFragmentListener,
        OnPostAdapterInteractionListener, OnPostDetailsInteractionListener,
        PostReactionAdapterListener,ProfileFragment.RemoveAppBar {

    public static final int ACTION_VIEW_POST = 0;
    public static final int ACTION_VIEW_REACTION = 1;
    public static final int ACTION_VIEW_PROFILE = 2;

    private int[] mTabIconsSelected = {
            R.drawable.ic_home_black,
            R.drawable.ic_binoculars_black,
            R.drawable.ic_add_video_black,
            R.drawable.ic_notifications_black,
            R.drawable.ic_person_black
    };

    @BindArray(R.array.tab_name) String[] TABS;
    @BindView(R.id.app_bar) AppBarLayout appBar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) SignPainterTextView toolbarTitle;
    @BindView(R.id.main_fragment_container) FrameLayout contentFrame;
    @BindView(R.id.bottom_tab_layout) TabLayout bottomTabLayout;
    @BindView(R.id.camera_btn) ImageButton cameraButton;
//    @BindView(R.id.logout_btn) ProximaNovaRegularTextView logoutBtn;

    private NavigationController navigationController;
    private FragmentHistory fragmentHistory;
    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_bottom_bar);

        Log.d("AUTH_TOKEN", SharedPrefs.getAuthToken(this));

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

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

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                navigationController.clearStack();
                switchTab(tab.getPosition());
            }
        });
    }

    @OnClick(R.id.logout_btn) public void performLogout() {
        logout(getApplicationContext(), this);
//        ApiCallingService.User.performLogout(SharedPrefs.getAuthToken(this), this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initTab();
        switchTab(0);
    }

    @OnClick(R.id.camera_btn) public void startCamera() {
        launchVideoUploadCamera(this);
    }

    private void initTab() {
        for (int i = 0; i < TABS.length; i++) {
            bottomTabLayout.addTab(bottomTabLayout.newTab().setCustomView(getTabView(i)));
        }
    }

    @SuppressLint("InflateParams")
    private View getTabView(int position) {
        ImageView view = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_tab_bottom, null);
        if (position != 2)
            view.setImageDrawable(BottomBarUtils.setDrawableSelector(this, mTabIconsSelected[position]));
        else cameraButton.setImageDrawable(BottomBarUtils.setDrawableSelector(this, mTabIconsSelected[2]));
        return view;
    }

    private void switchTab(int position) {
        navigationController.switchTab(position);
//        updateToolbarTitle(position);
    }

    private void updateTabSelection(int currentTab){
        for (int i = 0; i <  TABS.length; i++) {
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
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(!navigationController.isRootFragment());
            actionBar.setDisplayShowHomeEnabled(!navigationController.isRootFragment());
            actionBar.setHomeAsUpIndicator(R.drawable.ic_previous);
        }
    }

    /**
     * Updates the toolbar title.
     * @param title The title to be set, if null is passed, then "Teazer" will be set in SignPainter font in the center.
     * */
    public void updateToolbarTitle(@SuppressWarnings("SameParameterValue") final String title) {
        if (title == null) {
            toolbarTitle.animate().alpha(1).setDuration(250).start();
            toolbarTitle.setVisibility(VISIBLE);
        } else {
            toolbarTitle.animate().alpha(0).setDuration(250).start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbarTitle.setVisibility(INVISIBLE);
                    actionBar.setTitle("    " + title);
                }
            }, 250);
        }
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
        switch (index) {
            case NavigationController.TAB1:
                return new PostsListFragment();
            case NavigationController.TAB2:
                return new SearchFragment();
//            case NavigationController.TAB3:
//                return new SearchFragment();
            case NavigationController.TAB4:
                return new NotificationsFragment();
            case NavigationController.TAB5:
                return new ProfileFragment();
        }
        throw new IllegalArgumentException("Need to send an index that we know");
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
    public void onPostInteraction(int action, final PostDetails postDetails, ImageView postThumbnail, RelativeLayout layout) {
        switch (action) {
            case ACTION_VIEW_POST:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pushFragment(PostDetailsFragment.newInstance(2, postDetails));
                    }
                }, 500);
                break;
            case ACTION_VIEW_PROFILE:
                pushFragment(new ProfileFragment());
        }
    }

    @Override
    public void onPostDetailsInteraction(int action) {
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
            default:
                break;
        }
    }

    @Override
    public void onPostReactionInteraction(int action, Pojos.Post.PostReaction postReaction) {
    }

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
                    switchTab(0);
                    updateTabSelection(0);
                    fragmentHistory.emptyStack();
                }
            }
        }
    }

    @Override
    public void removeAppbar() {

  //   getSupportActionBar().hide();
    }
}
