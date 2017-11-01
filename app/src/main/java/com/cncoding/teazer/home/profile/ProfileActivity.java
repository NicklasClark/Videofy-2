package com.cncoding.teazer.home.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularImageView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseBottomBarActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 25;
    private boolean mIsAvatarShown = true;

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.app_bar) AppBarLayout appbarLayout;
    @BindView(R.id.edit_profile) FloatingActionButton editProfileBtn;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.small_profile_picture) CircularImageView smallProfilePic;
    @BindView(R.id.toolbar_name) ProximaNovaSemiboldTextView toolbarTitle;
    @BindView(R.id.toolbar_settings) AppCompatImageView toolbarSettings;

    private int mMaxScrollSize;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomContentView(getLayoutInflater().inflate(R.layout.activity_profile, null));
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public static void start(Context c) {
        c.startActivity(new Intent(c, ProfileActivity.class));
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            editProfileBtn.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            toolbarSettings.animate()
                    .scaleY(1).scaleX(1)
                    .alpha(1)
                    .setDuration(400)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            toolbarSettings.setVisibility(View.VISIBLE);
        }

        if (percentage < PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            editProfileBtn.animate()
                    .scaleY(1).scaleX(1)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            toolbarSettings.animate()
                    .alpha(0)
                    .setDuration(400)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toolbarSettings.setVisibility(View.INVISIBLE);
                }
            }, 400);
//            toolbarTitle.animate()
//                    .alpha(0)
//                    .setDuration(200)
//                    .start();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    toolbarTitle.setVisibility(View.INVISIBLE);
//                    toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/sign_painter_house_script.ttf"));
//                    toolbarTitle.setText(R.string.app_name);
//                }
//            }, 200);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    toolbarTitle.animate()
//                            .alpha(1)
//                            .setDuration(200)
//                            .start();
//                    toolbarTitle.setVisibility(View.VISIBLE);
//                }
//            }, 300);
        }

        if (percentage >= 75 && smallProfilePic.getVisibility() == View.VISIBLE) {
            smallProfilePic.animate()
                    .scaleX(0).scaleY(0)
                    .setDuration(200)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    smallProfilePic.setVisibility(View.INVISIBLE);
                }
            }, 200);
        }
        if (percentage < 75 && smallProfilePic.getVisibility() != View.VISIBLE) {
            smallProfilePic.animate()
                    .scaleX(1).scaleY(1)
                    .setDuration(200)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
            smallProfilePic.setVisibility(View.VISIBLE);
        }
    }

    private static class TabsAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 2;

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public Fragment getItem(int i) {
            return UserVideosFragment.newInstance();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "My Videos";
                case 1:
                    return "My Reactions";
                default:
                    return "";
            }
        }
    }
}