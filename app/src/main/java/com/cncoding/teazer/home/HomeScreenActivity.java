package com.cncoding.teazer.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.widget.FrameLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.camera.CameraActivity;
import com.cncoding.teazer.customViews.BottomDrawer;
import com.cncoding.teazer.home.post.PostDetailsFragment;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cncoding.teazer.home.HomeScreenPostAdapter.ACTION_VIEW_CATEGORY_POSTS;
import static com.cncoding.teazer.home.HomeScreenPostAdapter.ACTION_VIEW_POST;
import static com.cncoding.teazer.home.HomeScreenPostAdapter.ACTION_VIEW_PROFILE;
import static com.cncoding.teazer.home.post.PostDetailsFragment.EXIT_FRAGMENT;

public class HomeScreenActivity extends AppCompatActivity
        implements HomeScreenPostFragment.HomeScreenPostInteractionListener, PostDetailsFragment.OnPostDetailsInteractionListener {

    private static final String TAG_HOME_FRAGMENT = "homeFragment";
    private static final String TAG_SEARCH_FRAGMENT = "searchFragment";
    private static final String TAG_NOTIFICATION_FRAGMENT = "notificationFragment";
    private static final String TAG_USER_PROFILE_FRAGMENT = "userProfileFragment";
    private static final String POST_DETAILS_FRAGMENT = "postDetailsFragment";

    @BindView(R.id.home_screen_fragment_container) FrameLayout fragmentContainer;
    @BindView(R.id.bottom_navigation_bar) BottomDrawer bottomDrawer;
    @BindView(R.id.camera_tab_btn) AppCompatImageView cameraButton;
//    @BindView(R.id.home_tab_btn) RadioButtonPlus homeBtn;

    private FragmentManager fragmentManager;

    private int page;
    private int currentSelectedPosition = 0;

    private BottomDrawer.OnTabSelectionListener tabSelectionListener = new BottomDrawer.OnTabSelectionListener() {
        @Override
        public void onTabSelected(int position) {
            // start your various top-level activities from here
            if (position != getCurrentBottomNavPosition()) {
                setSelectedColor(position);
                switch (position) {
                    case BottomDrawer.POSITION_HOME_TAB:
                        setFragment(TAG_HOME_FRAGMENT);
//                        makeSnackbarWithBottomMargin(bottomDrawer, "home");
//                                    HomeActivity.start(BaseBottomNavActivity.this);
                        break;
                    case BottomDrawer.POSITION_SEARCH_TAB:
                        setFragment(TAG_SEARCH_FRAGMENT);
//                        makeSnackbarWithBottomMargin(bottomDrawer, "search");
//                                    UpdatesActivity.start(BaseBottomNavActivity.this);
                        break;
//                    case BottomDrawer.POSITION_CAMERA_TAB:
//                        makeSnackbarWithBottomMargin(bottomDrawer, "camera");
//                                    BookmarksActivity.start(BaseBottomNavActivity.this);
//                        break;
                    case BottomDrawer.POSITION_NOTIFICATIONS_TAB:
                        setFragment(TAG_NOTIFICATION_FRAGMENT);
//                        startActivity(new Intent(HomeScreenActivity.this, ScrollingActivity.class));
//                        makeSnackbarWithBottomMargin(bottomDrawer, "notifications");
//                                    ProfileActivity.start(BaseBottomNavActivity.this);
                        break;
                    case BottomDrawer.POSITION_USER_PROFILE_TAB:
                        ViewUtils.makeSnackbarWithBottomMargin(HomeScreenActivity.this, bottomDrawer, "profile");
//                                    FinishedStoriesActivity.start(BaseBottomNavActivity.this);
                        break;
                }
            } else {
                onNavBarReselect();
            }
            currentSelectedPosition = position;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        bottomDrawer.setOnTabSelectionListener(tabSelectionListener);
        page = 1;

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.home_screen_fragment_container, HomeScreenPostFragment.newInstance(2), TAG_HOME_FRAGMENT)
                .commit();
//        bottomDrawer.setSelectedTab(BottomDrawer.POSITION_HOME_TAB);
    }

    @OnClick(R.id.camera_tab_btn) public void startCamera() {
        startActivity(new Intent(this, CameraActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomDrawer.setSelectedTab(getCurrentBottomNavPosition());
    }

    /**
     * How re-selecting the same (current) tab will be handled.
     * */
    protected void onNavBarReselect() {
    }

//    /**
//     * e.g. get the bottom nav bar from the child's layout
//     * */
//    protected BottomDrawer bottomDrawer {
//        return bottomDrawer;
//    }

    private void setSelectedColor(int position) {
        bottomDrawer.resetDrawables();
        bottomDrawer.setColor(position);
    }

    private int getCurrentBottomNavPosition(){
        return currentSelectedPosition;
    }

    private boolean isFragmentActive(String tag) {
        return fragmentManager.findFragmentByTag(tag) != null;
    }

    private void setFragment(String tag) {
        if (!isFragmentActive(tag)) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (tag) {
                case TAG_HOME_FRAGMENT:
                    transaction.setCustomAnimations(R.anim.float_up_home, android.R.anim.fade_out,
                            R.anim.float_up, android.R.anim.fade_out);
                    if (isFragmentActive(tag))
                        transaction.replace(R.id.home_screen_fragment_container,
                                HomeScreenPostFragment.newInstance(2), TAG_HOME_FRAGMENT);
                    else
                        transaction.add(R.id.home_screen_fragment_container,
                                HomeScreenPostFragment.newInstance(2), TAG_HOME_FRAGMENT);
                    break;
                case TAG_SEARCH_FRAGMENT:
                    transaction.setCustomAnimations(R.anim.float_up_search, android.R.anim.fade_out,
                            R.anim.float_up, android.R.anim.fade_out)
                            .add(R.id.home_screen_fragment_container_2, new Interests(), TAG_SEARCH_FRAGMENT);
                    break;
                case TAG_NOTIFICATION_FRAGMENT:
                    transaction.setCustomAnimations(R.anim.float_up_notifications, android.R.anim.fade_out,
                            R.anim.float_up, android.R.anim.fade_out);
                    if (isFragmentActive(tag))
                        transaction.replace(R.id.home_screen_fragment_container, PostDetailsFragment.newInstance(2), POST_DETAILS_FRAGMENT)
                                .addToBackStack(POST_DETAILS_FRAGMENT);
                    else
                        transaction.add(R.id.home_screen_fragment_container, PostDetailsFragment.newInstance(2), POST_DETAILS_FRAGMENT)
                                .addToBackStack(POST_DETAILS_FRAGMENT);
                    break;
                case TAG_USER_PROFILE_FRAGMENT:
//                    transaction.setCustomAnimations(R.anim.float_up_user_profile, android.R.anim.fade_out,
//                            R.anim.float_up, android.R.anim.fade_out);
                    break;
                default:
                    break;
            }
            transaction.commit();
        }
    }

    private void removeFragment(String tag) {
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(tag)).commit();
    }

    @Override
    public void onHomeScreenPostInteraction(int action, PostDetails postDetails) {
        switch (action) {
            case ACTION_VIEW_POST:
                break;
            case ACTION_VIEW_PROFILE:
                break;
            case ACTION_VIEW_CATEGORY_POSTS:
                break;
            default:
                break;
        }
    }

    @Override
    public void onPostDetailsInteraction(int action) {
        switch (action) {
            case EXIT_FRAGMENT:
                if (isFragmentActive(POST_DETAILS_FRAGMENT))
                    removeFragment(POST_DETAILS_FRAGMENT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isFragmentActive(TAG_SEARCH_FRAGMENT)) {
            removeFragment(TAG_SEARCH_FRAGMENT);
            bottomDrawer.setSelectedTab(0);
        }
        else
            super.onBackPressed();
    }
}