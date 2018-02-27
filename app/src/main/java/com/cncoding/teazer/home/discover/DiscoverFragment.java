package com.cncoding.teazer.home.discover;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CustomLinearLayoutManager;
import com.cncoding.teazer.customViews.CustomStaggeredGridLayoutManager;
import com.cncoding.teazer.customViews.StatefulRecyclerView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.discover.adapters.FeaturedPostsListAdapter;
import com.cncoding.teazer.home.discover.adapters.MostPopularListAdapter;
import com.cncoding.teazer.home.discover.adapters.MyInterestsListAdapter;
import com.cncoding.teazer.home.discover.adapters.TrendingListAdapter;
import com.cncoding.teazer.home.discover.search.DiscoverSearchFragment;
import com.cncoding.teazer.home.tagsAndCategories.Interests;
import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.discover.LandingPostsV2;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.utilities.Annotations.CALL_LANDING_POSTS;
import static com.cncoding.teazer.utilities.Annotations.CALL_MOST_POPULAR_POSTS;

public class DiscoverFragment extends BaseDiscoverFragment {

    public static final int ACTION_VIEW_MY_INTERESTS = 1;
    public static final int ACTION_VIEW_FEATURED = 2;
    public static final int ACTION_VIEW_TRENDING = 3;

    @BindView(R.id.root_layout) NestedScrollView nestedScrollView;
    @BindView(R.id.landing_posts_container) LinearLayout landingPostsContainer;
    @BindView(R.id.featured_posts_list) StatefulRecyclerView featuredPostsList;
    @BindView(R.id.no_featured_posts) ProximaNovaBoldTextView noFeaturedPosts;
    @BindView(R.id.my_interests_header_layout) LinearLayout myInterestsHeaderLayout;
    @BindView(R.id.my_interests_view_all) public ProximaNovaSemiBoldTextView myInterestsViewAll;
    @BindView(R.id.my_interests_list) StatefulRecyclerView myInterestsList;
    @BindView(R.id.no_my_interests) ProximaNovaRegularTextView noMyInterests;
    @BindView(R.id.trending_list) StatefulRecyclerView trendingList;
    @BindView(R.id.no_trending) ProximaNovaBoldTextView noTrending;
    @BindView(R.id.most_popular_layout) LinearLayout mostPopularLayout;
    @BindView(R.id.most_popular_list) StatefulRecyclerView mostPopularList;
    @BindView(R.id.recycler_view_progress_bar) ProgressBar mostPopularLoadingProgressBar;
    @BindView(R.id.no_most_popular) ProximaNovaBoldTextView noMostPopularVideos;
    @BindView(R.id.post_load_error) ProximaNovaRegularTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;

    private static int scrollPosition;
    public static boolean updateMyInterests = false;
    private FeaturedPostsListAdapter featuredPostsListAdapter;
    private MyInterestsListAdapter myInterestsListAdapter;
    private TrendingListAdapter trendingListAdapter;
    private MostPopularListAdapter mostPopularListAdapter;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @NonNull public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        previousTitle = getParentActivity().getToolbarTitle();
        getParentActivity().updateToolbarTitle(getString(R.string.discover));
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, rootView);
        initStuff();
        return rootView;
    }

    @Override public void onResume() {
        super.onResume();
        loadPosts();
        mostPopularList.setFocusable(false);
        nestedScrollView.requestFocus();
    }

    @Override public void onPause() {
        super.onPause();
        scrollPosition = nestedScrollView.getScrollY();
    }

    private void initStuff() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() && is_next_page) {
//                    SCROLLED TO BOTTOM
                    is_next_page = false;
                    mostPopularLoadingProgressBar.setVisibility(VISIBLE);
                    loadMostPopularPosts(++currentPage);
                }
            }
        });

        featuredPostsListAdapter = new FeaturedPostsListAdapter(this);
        myInterestsListAdapter = new MyInterestsListAdapter(this);
        trendingListAdapter = new TrendingListAdapter(this);
        mostPopularListAdapter = new MostPopularListAdapter(this);

        initRecyclerViews();
    }

    private void initRecyclerViews() {
        featuredPostsList.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        featuredPostsList.setAdapter(featuredPostsListAdapter);

        myInterestsList.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myInterestsList.setAdapter(myInterestsListAdapter);

        trendingList.setLayoutManager(new CustomLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        trendingList.setAdapter(trendingListAdapter);

        mostPopularList.setLayoutManager(new CustomStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mostPopularList.setAdapter(mostPopularListAdapter);
    }

    public void loadPosts() {
        postLoadErrorLayout.setVisibility(GONE);
        loadLandingPosts();
    }

    @OnClick(R.id.featured_posts_view_all) public void viewAllMostPopular() {
        navigation.pushFragment(SubDiscoverFragment.newInstance(ACTION_VIEW_FEATURED, null));
    }

    @OnClick(R.id.my_interests_view_all) public void viewAllMyInterests() {
        ArrayList<Category> userInterests = getUserInterests();
        if (userInterests != null)
            navigation.pushFragment(SubDiscoverFragment.newInstance(ACTION_VIEW_MY_INTERESTS, userInterests));
        else Log.e("MY_INTERESTS_VIEW_ALL", "userInterests was null");
    }

    @OnClick(R.id.discover_search) public void toggleSearch() {
        navigation.pushFragment(DiscoverSearchFragment.newInstance());
    }

    @OnClick(R.id.post_load_error_layout) public void reloadStuff() {
        currentPage = 1;
        loadPosts();
    }

    @OnClick(R.id.no_my_interests) public void onNoMyInterestsClick() {
        ArrayList<Category> userInterests = getUserInterests();
        if (userInterests != null)
            navigation.pushFragmentOnto(
                    Interests.newInstance(false, true, userInterests, null, true));
        else Log.e("MY_INTERESTS_SELECT", "userInterests was null");
    }

    @SuppressLint("SwitchIntDef") @SuppressWarnings("unchecked") @Override protected void handleResponse(BaseModel resultObject) {
        switch (resultObject.getCallType()) {
            case CALL_LANDING_POSTS:
                if (resultObject instanceof LandingPostsV2)
                    new NotifyLandingDataSetChanged(this, (LandingPostsV2) resultObject).execute();
                break;
            case CALL_MOST_POPULAR_POSTS:
                if (resultObject instanceof PostList) {
                    PostList postList = (PostList) resultObject;
                    if (!postList.getPosts().isEmpty()) {
                        mostPopularListAdapter.updatePosts(currentPage, postList.getPosts());
                        is_next_page = postList.isNextPage();
                        mostPopularLayout.setVisibility(VISIBLE);
                        mostPopularList.setVisibility(VISIBLE);
                        noMostPopularVideos.setVisibility(GONE);
                    } else if (currentPage == 1){
                        mostPopularLayout.setVisibility(VISIBLE);
                        mostPopularList.setVisibility(GONE);
                        noMostPopularVideos.setVisibility(VISIBLE);
                    }
                }
                if (currentPage == 1 && scrollPosition != nestedScrollView.getScrollY()) {
                    nestedScrollView.setScrollY(scrollPosition);
                }
                mostPopularLoadingProgressBar.setVisibility(GONE);
                break;
        }
    }

    @Override protected void handleError(BaseModel baseModel) {
        showErrorMessage(getString(R.string.something_went_wrong), baseModel.getCallType() == CALL_LANDING_POSTS);
        mostPopularLoadingProgressBar.setVisibility(GONE);
    }

    private static class NotifyLandingDataSetChanged extends AsyncTask<Void, Void, Void> {

        private DiscoverFragment fragment;
        private LandingPostsV2 landingPosts;

        NotifyLandingDataSetChanged(DiscoverFragment fragment, LandingPostsV2 landingPosts) {
            this.fragment = fragment;
            this.landingPosts = landingPosts;
        }

        @Override
        protected Void doInBackground(Void... maps) {
            try {
                fragment.featuredPostsListAdapter.updatePosts(landingPosts.getFeaturedVideos());
                fragment.trendingListAdapter.updateCategories(landingPosts.getTrendingCategories());

                Map<String, ArrayList<PostDetails>> tempMap = new HashMap<>();
                tempMap.putAll(landingPosts.getMyInterests());
                for (int i = 0; i < landingPosts.getMyInterests().size(); i++) {
                    String categoryName = fragment.getUserInterests().get(i).getCategoryName();
                    if (tempMap.get(categoryName) != null && tempMap.get(categoryName).isEmpty())
                        tempMap.remove(categoryName);
                }
                landingPosts.getMyInterests().clear();
                landingPosts.getMyInterests().putAll(tempMap);

                fragment.myInterestsListAdapter.addPosts(landingPosts.getMyInterests());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            fragment.landingPostsContainer.setVisibility(VISIBLE);
            if (landingPosts.getUserInterests() == null || landingPosts.getUserInterests().isEmpty() ||
                    landingPosts.getMyInterests() == null || landingPosts.getMyInterests().isEmpty()) {
                fragment.myInterestsList.setVisibility(GONE);
                fragment.noMyInterests.setVisibility(VISIBLE);
            }
        }
    }

    private void showErrorMessage(String message, boolean isLandingPosts) {
        if (isLandingPosts)
            landingPostsContainer.setVisibility(GONE);
        else
            mostPopularLayout.setVisibility(GONE);
        postLoadErrorLayout.setVisibility(VISIBLE);
        postLoadErrorTextView.setText(message);
    }

    @Override public void onDetach() {
        super.onDetach();
        getParentActivity().updateToolbarTitle(previousTitle);
    }
}