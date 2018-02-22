package com.cncoding.teazer.home.discover;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.discover.adapters.SubDiscoverAdapter;
import com.cncoding.teazer.home.tagsAndCategories.Interests;
import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.post.PostList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.VISIBLE;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_FEATURED;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_MY_INTERESTS;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_TRENDING;

public class SubDiscoverFragment extends BaseDiscoverFragment {

    private static final String ARG_CATEGORIES = "categories";
    private static final String ACTION = "action";

    @BindView(R.id.my_interests_tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.list_layout) LinearLayout listLayout;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.no_posts) ProximaNovaRegularTextView noPosts;
    @BindView(R.id.no_posts_2) ProximaNovaBoldTextView noPosts2;

    private int currentPage;
    private ArrayList<Category> categories;
    private int action;

    public SubDiscoverFragment() {}

    public static SubDiscoverFragment newInstance(int action, ArrayList<Category> categories) {
        SubDiscoverFragment fragment = new SubDiscoverFragment();
        Bundle args = new Bundle();
        args.putInt(ACTION, action);
        args.putParcelableArrayList(ARG_CATEGORIES, categories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            action = getArguments().getInt(ACTION);
            if (action == ACTION_VIEW_MY_INTERESTS) setHasOptionsMenu(true);
            categories = getArguments().getParcelableArrayList(ARG_CATEGORIES);
        }
        previousTitle = getParentActivity().getToolbarTitle();
        currentPage = 1;
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub_discover, container, false);
        ButterKnife.bind(this, rootView);

        switch (action) {
            case ACTION_VIEW_FEATURED:
                getParentActivity().updateToolbarTitle(getString(R.string.most_popular));
                prepareFeaturedLayout();
                break;
            case ACTION_VIEW_MY_INTERESTS:
                getParentActivity().updateToolbarTitle(getString(R.string.my_interests));
                prepareMyInterestsLayout();
                break;
            case ACTION_VIEW_TRENDING:
                getParentActivity().updateToolbarTitle((categories != null && !categories.isEmpty()) ?
                        (categories.get(0).getCategoryName()):"Trending");
                prepareTrendingLayout();
                break;
            default:
                break;
        }
        return rootView;
    }

    private void prepareFeaturedLayout() {
        listLayout.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new SubDiscoverAdapter(this));
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page) {
                    currentPage = page;
                    getFeaturedPosts(page);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                getFeaturedPosts(1);
            }
        });

        getFeaturedPosts(1);
    }

    private void prepareMyInterestsLayout() {
        if (categories != null && !categories.isEmpty()) {
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);

            populateTabs();
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(categories.get(0).getMyColor()));

            viewPager.setAdapter(sectionsPagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
                @Override
                public void onPageSelected(int position) {
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor(categories.get(position).getMyColor()));
                    fadeOtherTabColorsOut(position);
                    setCurrentTabTextColor(position);
                }
            });
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        } else {
            Toast.makeText(getContext(), R.string.no_interests_selected_message, Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigation.pushFragmentOnto(Interests.newInstance(
                            false, true, categories, null, false));
                }
            }, 1000);
        }
    }

    private void prepareTrendingLayout() {
        listLayout.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new SubDiscoverAdapter(this));
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page) {
                    currentPage = page;
                    getTrendingVideos(page);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                getTrendingVideos(1);
            }
        });
        
        getTrendingVideos(1);
    }

    private void populateTabs() {
        for (int i = 0; i < categories.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(categories.get(i).getCategoryName()), i);
//            Set tab text color
            LinearLayout linearLayout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(i));
            linearLayout.setPadding(get34dp(), 0, get34dp(), 0);
            AppCompatTextView view = ((AppCompatTextView) linearLayout.getChildAt(1));
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            view.setTextColor(i == 0 ? Color.parseColor(categories.get(i).getMyColor()) : Color.parseColor("#666666"));
        }
    }

    private void setCurrentTabTextColor(int position) {
        LinearLayout linearLayout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(position));
        linearLayout.setPadding(get34dp(), 0, get34dp(), 0);
        AppCompatTextView view = ((AppCompatTextView) linearLayout.getChildAt(1));
        view.setTextColor(Color.parseColor(categories.get(position).getMyColor()));
    }

    private void fadeOtherTabColorsOut(int excludePosition) {
        for (int i = 0; i < categories.size(); i++) {
            if (i != excludePosition) {
                LinearLayout linearLayout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(i));
                linearLayout.setPadding(get34dp(), 0, get34dp(), 0);
                AppCompatTextView view = ((AppCompatTextView) linearLayout.getChildAt(1));
                view.setTextColor(Color.parseColor("#666666"));
            }
        }

    }

    private int get34dp() {
        return (int)((34 * getResources().getDisplayMetrics().density) + 0.5);
    }
    
    private void getTrendingVideos(int page) {
        loadTrendingPostsByCategory(page, categories.get(0).getCategoryId());
    }
    
    private void getFeaturedPosts(int page) {
        loadFeaturedPosts(page);
    }

    @SuppressLint("SwitchIntDef") @Override protected void handleResponse(BaseModel resultObject) {
        if (resultObject instanceof PostList) {
            PostList postList = (PostList) resultObject;
            is_next_page = postList.isNextPage();
            if (!postList.getPosts().isEmpty()) {
                ((SubDiscoverAdapter) recyclerView.getAdapter()).updatePosts(postList.getPosts());
            } else if (currentPage == 1) {
                showErrorMessage(R.string.no_videos_tagged, R.string.be_the_first_one_to_upload_one);
            }
        }
    }

    @Override protected void handleError(BaseModel baseModel) {
        swipeRefreshLayout.setRefreshing(false);
        showErrorMessage(R.string.something_went_wrong, R.string.swipe_down_to_retry);
        baseModel.getError().printStackTrace();
    }

    private void showErrorMessage(@StringRes int message1, @StringRes int message2) {
        noPosts.setText(message1);
        noPosts2.setText(message2);
        noPosts.setVisibility(VISIBLE);
        noPosts2.setVisibility(VISIBLE);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_interests, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            navigation.pushFragmentOnto(Interests.newInstance(false, true, categories, null, false));
            return true;
        }
        return false;
    }

    @Override public void onDetach() {
        super.onDetach();
        getParentActivity().updateToolbarTitle(previousTitle);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MyInterestsFragmentTab.newInstance(categories.get(position).getCategoryId());
        }

        @Override
        public int getCount() {
            return categories.size();
        }
    }
}