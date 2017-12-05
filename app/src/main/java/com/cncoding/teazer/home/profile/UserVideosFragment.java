package com.cncoding.teazer.home.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import com.cncoding.teazer.R;
import com.cncoding.teazer.home.BaseFragment;

import butterknife.ButterKnife;

import static android.view.View.VISIBLE;

public class UserVideosFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

//    @BindView(R.id.main_linear_layout_title) LinearLayout mTitleContainer;
//    @BindView(R.id.title_big) ProximaNovaSemiboldTextView bigTitle;
//    @BindView(R.id.subtitle) ProximaNovaRegularTextView subtitle;
//    @BindView(R.id.main_text_view_title) TextView mTitle;
//    @BindView(R.id.up_btn) AppCompatImageView upBtn;
//    @BindView(R.id.app_bar) AppBarLayout mAppBarLayout;
//    @BindView(R.id.main_toolbar) Toolbar mToolbar;
//    @BindView(R.id.tabs) TabLayout tabLayout;
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRootView;

    public UserVideosFragment() {
        // Required empty public constructor
    }

    public static UserVideosFragment newInstance(int numItems) {
        UserVideosFragment fragment = new UserVideosFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, numItems);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance() {
        return new UserVideosFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//        if (getArguments() != null) {
//            numItems = getArguments().getInt(ARG_PARAM1);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = (RecyclerView) inflater.inflate(R.layout.fragment_user_videos, container, false);
        ButterKnife.bind(this, mRootView);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
//        mAppBarLayout.addOnOffsetChangedListener(this);

//        mToolbar.inflateMenu(R.menu.menu_post_others);
//        startAlphaAnimation(mTitle, 0, INVISIBLE);
//        startAlphaAnimation(upBtn, 0, INVISIBLE);
//        startAlphaAnimation(tabLayout, 0, INVISIBLE);

        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mRootView.setAdapter(new UserVideosAdapter(20));
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_post_others, menu);
////        getActivity().getMenuInflater().inflate(R.menu.menu_post_others, menu);
//    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
//        float percentage = (float) Math.abs(offset) / (float) maxScroll;

//        handleAlphaOnTitle(percentage);
//        handleToolbarTitleVisibility(percentage);
    }

//    private void handleToolbarTitleVisibility(float percentage) {
//        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
//
//            if(!mIsTheTitleVisible) {
//                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, VISIBLE);
//                startAlphaAnimation(upBtn, ALPHA_ANIMATIONS_DURATION, VISIBLE);
//                mIsTheTitleVisible = true;
//            }
//
//        } else {
//
//            if (mIsTheTitleVisible) {
//                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, INVISIBLE);
//                startAlphaAnimation(upBtn, ALPHA_ANIMATIONS_DURATION, INVISIBLE);
//                mIsTheTitleVisible = false;
//            }
//        }
//    }

//    private void handleAlphaOnTitle(float percentage) {
//        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
//            if(mIsTheTitleContainerVisible) {
//                startAlphaAnimation(bigTitle, ALPHA_ANIMATIONS_DURATION, INVISIBLE);
//                startAlphaAnimation(subtitle, ALPHA_ANIMATIONS_DURATION, INVISIBLE);
////                startAlphaAnimation(tabLayout, ALPHA_ANIMATIONS_DURATION, INVISIBLE);
//                mIsTheTitleContainerVisible = false;
//            }
//
//        } else {
//
//            if (!mIsTheTitleContainerVisible) {
//                startAlphaAnimation(bigTitle, ALPHA_ANIMATIONS_DURATION, VISIBLE);
//                startAlphaAnimation(subtitle, ALPHA_ANIMATIONS_DURATION, VISIBLE);
//                startAlphaAnimation(tabLayout, ALPHA_ANIMATIONS_DURATION, VISIBLE);
//                mIsTheTitleContainerVisible = true;
//            }
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof HomeScreenPostsListener) {
//            mListener = (HomeScreenPostsListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement HomeScreenPostsListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == VISIBLE) ? new AlphaAnimation(0f, 1f) : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
