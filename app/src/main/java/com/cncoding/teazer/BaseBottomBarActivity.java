package com.cncoding.teazer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.cncoding.teazer.camera.CameraActivity;
import com.cncoding.teazer.customViews.BottomDrawer;
import com.cncoding.teazer.home.post.HomeScreenPostsActivity;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;

import butterknife.OnClick;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class BaseBottomBarActivity extends AppCompatActivity {

    protected static final String ARG_COLUMN_COUNT = "columnCount";
    protected static final String TAG_HOME_ACTIVITY = "homeActivity";
    protected static final String TAG_SEARCH_ACTIVITY = "searchActivity";
    protected static final String TAG_NOTIFICATION_ACTIVITY = "notificationActivity";
    protected static final String TAG_USER_PROFILE_ACTIVITY = "userProfileActivity";
    protected static final String TAG_POST_DETAILS_ACTIVITY = "postDetailsActivity";
    protected static final String EXTRAS = "extras";

    public static final int ACTION_VIEW_POST = 0;
    public static final int ACTION_VIEW_REACTION = 1;
    public static final int ACTION_VIEW_PROFILE = 2;
//    public static final int ACTION_VIEW_CATEGORY_POSTS = 4;

    protected FrameLayout container;
    protected BottomDrawer bottomDrawer;
    protected AppCompatImageView cameraButton;

//    public FragmentManager fragmentManager;

    private int currentSelectedPosition = 0;

    private BottomDrawer.OnTabSelectionListener tabSelectionListener = new BottomDrawer.OnTabSelectionListener() {
        @Override
        public void onTabSelected(int position) {
            // start your various top-level activities from here
            if (position != getCurrentBottomNavPosition()) {
                setSelectedColor(position);
                switch (position) {
                    case BottomDrawer.POSITION_HOME_TAB:
                        setActivity("", BaseBottomBarActivity.this, HomeScreenPostsActivity.class, 2, null);
                        break;
                    case BottomDrawer.POSITION_SEARCH_TAB:
                        break;
                    case BottomDrawer.POSITION_NOTIFICATIONS_TAB:
                        break;
                    case BottomDrawer.POSITION_USER_PROFILE_TAB:
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
        setContentView(R.layout.activity_base_bottom_bar);
        container = findViewById(R.id.container);
        bottomDrawer = findViewById(R.id.bottom_navigation_bar);
        cameraButton = findViewById(R.id.bottom_camera_btn);
        bottomDrawer.setOnTabSelectionListener(tabSelectionListener);
    }

    protected void setCustomContentView (View view) {
        container.addView(view);
    }

    protected static void setupActionBar(AppCompatActivity activity, Toolbar toolbar, boolean showHomeAsUp) {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null && showHomeAsUp) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_previous);
        }
    }

    protected static Parcelable getParcelableExtra(AppCompatActivity activity, String tag) {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            return bundle.getParcelable(tag);
        }
        else return null;
    }

    protected static int getParcelableInt(AppCompatActivity activity, String tag) {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            return bundle.getInt(tag);
        }
        else return 1;
    }

    @OnClick(R.id.bottom_camera_btn) public void startCamera() {
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

    private void setSelectedColor(int position) {
        bottomDrawer.resetDrawables();
        bottomDrawer.setColor(position);
    }

    private boolean isVisible(View view) {
        return view.getVisibility() == VISIBLE;
    }

    /**
     * e.g. get the bottom nav bar from the child's layout
     * */
    protected BottomDrawer getBottomDrawer() {
        return bottomDrawer;
    }

    public AppCompatImageView getCameraButton() {
        return cameraButton;
    }

    protected void toggleBottonBarVisibility(int visibility) {
        switch (visibility) {
            case VISIBLE:
                if (isVisible(bottomDrawer) && isVisible(cameraButton)) {
                    bottomDrawer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.sink_down));
                    cameraButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_out));
                    bottomDrawer.setVisibility(visibility);
                    cameraButton.setVisibility(visibility);
                }
                break;
            case INVISIBLE:
                if (!isVisible(bottomDrawer) && !isVisible(cameraButton)) {
                    bottomDrawer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_up));
                    cameraButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
                    bottomDrawer.setVisibility(visibility);
                    cameraButton.setVisibility(visibility);
                }
                break;
            default:
                break;
        }
    }

    protected int getCurrentBottomNavPosition(){
        return currentSelectedPosition;
    }

    protected void setActivity(String tag, Context context, Class<? extends BaseBottomBarActivity> cls,
                               int columnCount, PostDetails postDetails) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        switch (tag) {
            case TAG_HOME_ACTIVITY:
                intent.putExtra(ARG_COLUMN_COUNT, (columnCount));
                break;
            case TAG_POST_DETAILS_ACTIVITY:
                intent.putExtra(ARG_COLUMN_COUNT, (columnCount));
                if (postDetails != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(EXTRAS, postDetails);
                    intent.putExtra("bundle", bundle);
                }
                break;
            case TAG_SEARCH_ACTIVITY:
                break;
            case TAG_NOTIFICATION_ACTIVITY:
                break;
            case TAG_USER_PROFILE_ACTIVITY:
                break;
            default:
                break;
        }
        startActivity(intent);
    }
//    private void toggleUpBtnVisibility(int visibility) {
//        switch (visibility) {
//            case View.VISIBLE:
//                if (upBtn.getVisibility() != View.VISIBLE) {
//                    upBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
//                    upBtn.setVisibility(View.VISIBLE);
//                }
//                break;
//            case INVISIBLE:
//                if (upBtn.getVisibility() != INVISIBLE) {
//                    upBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_out));
//                    upBtn.setVisibility(INVISIBLE);
//                }
//                break;
//            default:
//                break;
//        }
//    }

//    @Override
//    public void onHomeScreenPostInteraction(int action, Pojos.Post.PostDetails postDetails) {
//        switch (action) {
//            case ACTION_VIEW_POST:
//                setFragment(TAG_POST_DETAILS_ACTIVITY, postDetails);
//                break;
//            case ACTION_VIEW_PROFILE:
//                break;
//            case ACTION_VIEW_CATEGORY_POSTS:
//                break;
//            default:
//                break;
//        }
//    }

//    @Override
//    public void onPostDetailsInteraction(int action) {
//        switch (action) {
//            case EXIT_FRAGMENT:
//                onBackPressed();
//                break;
//            default:
//                break;
//        }
//    }

//    private void toggleUpBtnVisibility(int visibility) {
//        switch (visibility) {
//            case View.VISIBLE:
//                if (upBtn.getVisibility() != View.VISIBLE) {
//                    upBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
//                    upBtn.setVisibility(View.VISIBLE);
//                }
//                break;
//            case INVISIBLE:
//                if (upBtn.getVisibility() != INVISIBLE) {
//                    upBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_out));
//                    upBtn.setVisibility(INVISIBLE);
//                }
//                break;
//            default:
//                break;
//        }
//    }
}
