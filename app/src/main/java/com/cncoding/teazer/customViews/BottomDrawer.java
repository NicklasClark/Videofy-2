package com.cncoding.teazer.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.databinding.HomeScreenBottomDrawerBinding;

/**
 *
 * Created by Prem $ on 10/23/2017.
 */

public class BottomDrawer extends FrameLayout {
    // These would be names and associated positions of the tabs specific to your app
    public static final int POSITION_HOME_TAB = 0;
    public static final int POSITION_SEARCH_TAB = 1;
//    public static final int POSITION_CAMERA_TAB = 2;
    public static final int POSITION_NOTIFICATIONS_TAB = 3;
    public static final int POSITION_USER_PROFILE_TAB = 4;
    // Passed in from the activities using this, in the setter below
    private OnTabSelectionListener mTabSelectionListener;
    private int mCurrentPosition;
    // For some first-time arrangements of the layout
//    private boolean drawn;
    // Used in initial layout
//    private boolean shadowDrawn;

    private HomeScreenBottomDrawerBinding binding;
    // To store resource ids of the drawable for the icons, the order of tabs above should be the same
    private Integer[] tabResId;

    public BottomDrawer(Context context) {
        super(context);
        initLayout(context);
    }

    public BottomDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public BottomDrawer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomDrawer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLayout(context);
    }

    private void initLayout(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.home_screen_bottom_drawer, this, true);

        tabResId = new Integer[]{
                R.id.home_tab_btn, R.id.search_tab_btn,
//                R.id.camera_tab_btn,
                R.id.notifications_tab_btn, R.id.user_profile_tab_btn};
        binding.homeTabBtn.setOnClickListener(new InternalTabSelectionListener(POSITION_HOME_TAB));
        binding.searchTabBtn.setOnClickListener(new InternalTabSelectionListener(POSITION_SEARCH_TAB));
//        binding.cameraTabBtn.setOnClickListener(new InternalTabSelectionListener(POSITION_CAMERA_TAB));
        binding.notificationsTabBtn.setOnClickListener(new InternalTabSelectionListener(POSITION_NOTIFICATIONS_TAB));
        binding.userProfileTabBtn.setOnClickListener(new InternalTabSelectionListener(POSITION_USER_PROFILE_TAB));

        setWillNotDraw(false);

        setClipToPadding(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setClipToOutline(false);
        }
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (!drawn) {
//            drawn = true;
//            Rect rect = new Rect();
//            ((View) getParent()).getGlobalVisibleRect(rect);
//            setY(rect.bottom - rect.top - getContext().getResources().getReact_dimension(R.dimen.bottom_bar_with_shadow_height));
//            invalidate();
//        } else if (!shadowDrawn) {
//            shadowDrawn = true;
//            binding.shadowGradient.draw(canvas);
//        }
//    }

    public int getBottomBarHeight() {
        return binding.bottomBarLayout.getHeight();
    }

    public void setOnTabSelectionListener(OnTabSelectionListener tabSelectionListener) {
        this.mTabSelectionListener = tabSelectionListener;
    }

    public void setSelectedTab(int position) {
        binding.bottomBarLayout.check(tabResId[position]);
    }
    
    public RadioButtonPlus getSelectedTab() {
        switch (mCurrentPosition) {
            case POSITION_HOME_TAB:
                return binding.homeTabBtn;
            case POSITION_SEARCH_TAB:
                return binding.searchTabBtn;
//            case POSITION_CAMERA_TAB:
//                return binding.cameraTabBtn;
            case POSITION_NOTIFICATIONS_TAB:
                return binding.notificationsTabBtn;
            case POSITION_USER_PROFILE_TAB:
                return binding.userProfileTabBtn;
            default:
                return binding.homeTabBtn;
        }
    }
    
    public void setColor(int position) {
        switch (position) {
            case POSITION_HOME_TAB:
                binding.homeTabBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home, 0, 0);
                break;
            case POSITION_SEARCH_TAB:
                binding.searchTabBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_binoculars, 0, 0);
                break;
//            case POSITION_CAMERA_TAB:
//                binding.cameraTabBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_add_video, 0, 0);
//                break;
            case POSITION_NOTIFICATIONS_TAB:
                binding.notificationsTabBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notifications, 0, 0);
                break;
            case POSITION_USER_PROFILE_TAB:
                binding.userProfileTabBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_person, 0, 0);
                break;
            default:
                break;
        }
    }

    public void resetDrawables() {
        binding.homeTabBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_home_black, 0, 0);
        binding.searchTabBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_binoculars_black, 0, 0);
//        binding.cameraTabBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_add_video_black, 0, 0);
        binding.notificationsTabBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notifications_black, 0, 0);
        binding.userProfileTabBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_person_black, 0, 0);
    }

    public interface OnTabSelectionListener {
        void onTabSelected(int position);
    }

    private class InternalTabSelectionListener implements OnClickListener {

        int currentPosition;

        InternalTabSelectionListener(int currentTabPosition) {
            currentPosition = currentTabPosition;
        }

        @Override
        public void onClick(View view) {
            if (mTabSelectionListener != null) {
//                switch (view.getUserId()) {
//                    case R.id.home_tab_btn:
//                        mTabSelectionListener.onTabSelected(POSITION_HOME_TAB);
//                        break;
//                    case R.id.search_tab_btn:
//                        mTabSelectionListener.onTabSelected(POSITION_SEARCH_TAB);
//                        break;
//                    case R.id.camera_tab_btn:
//                        mTabSelectionListener.onTabSelected(POSITION_CAMERA_TAB);
//                        break;
//                    case R.id.notifications_tab_btn:
//                        mTabSelectionListener.onTabSelected(POSITION_NOTIFICATIONS_TAB);
//                        break;
//                    case R.id.user_profile_tab_btn:
//                        mTabSelectionListener.onTabSelected(POSITION_USER_PROFILE_TAB);
//                        break;
//                    default:
//                        break;
//                }
                mTabSelectionListener.onTabSelected(currentPosition);
            }
        }
    }
}