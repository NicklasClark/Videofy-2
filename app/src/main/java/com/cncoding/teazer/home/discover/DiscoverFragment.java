package com.cncoding.teazer.home.discover;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.home.discover.adapters.FeaturedVideosListAdapter;
import com.cncoding.teazer.home.discover.adapters.MostPopularListAdapter;
import com.cncoding.teazer.home.discover.adapters.MyInterestsListAdapter;
import com.cncoding.teazer.home.discover.adapters.TrendingListAdapter;
import com.cncoding.teazer.home.discover.search.DiscoverSearchFragment;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.post.LandingPosts;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DiscoverFragment extends BaseFragment {

    public static final int ACTION_VIEW_MY_INTERESTS = 1;
    public static final int ACTION_VIEW_MOST_POPULAR = 2;
    public static final int ACTION_VIEW_TRENDING = 3;
//    private static final String TAG_DISCOVER_SEARCH_FRAGMENT = "discoverSearchFragment";

    @BindView(R.id.root_layout) NestedScrollView nestedScrollView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.discover_posts_container) LinearLayout discoverPostsContainer;
    @BindView(R.id.featured_posts_container) LinearLayout featuredPostsContainer;
    @BindView(R.id.most_popular_list) RecyclerView mostPopularList;
    @BindView(R.id.my_interests_list) RecyclerView myInterestsList;
    @BindView(R.id.trending_list) RecyclerView trendingList;
    @BindView(R.id.featured_videos_list) RecyclerView featuredVideosList;
    @BindView(R.id.no_most_popular) ProximaNovaBoldTextView noMostPopular;
    @BindView(R.id.no_trending) ProximaNovaBoldTextView noTrending;
    @BindView(R.id.no_featured_videos) ProximaNovaBoldTextView noFeaturedVideos;
    @BindView(R.id.post_load_error) ProximaNovaBoldTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;

    public static boolean updateMyInterests = false;
    private int page;
    private OnDiscoverInteractionListener mListener;
    private ArrayList<PostDetails> featuredPostsList;
    private LandingPosts landingPosts;
    private Call<PostList> featuredPostsCall;
    private Call<LandingPosts> landingPostCall;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        previousTitle = getParentActivity().getToolbarTitle();
        getParentActivity().updateToolbarTitle(getString(R.string.discover));
        // Inflate the searchContainer for this fragment
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        ButterKnife.bind(this, rootView);

        initMembersIfEmpty();

        initRecyclerViews();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPosts();
    }

    private void initMembersIfEmpty() {
        page = 1;

        if (featuredPostsList == null) {
            progressBar.setVisibility(VISIBLE);
            featuredPostsList = new ArrayList<>();
        } else
            dismissProgressBar();
        if (landingPosts == null) {
            progressBar.setVisibility(VISIBLE);
            landingPosts = new LandingPosts(new ArrayList<PostDetails>(), new ArrayList<Category>(),
                    new ArrayList<Category>(), new HashMap<String, ArrayList<PostDetails>>());
        } else
            dismissProgressBar();

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight() && is_next_page) {
//                    SCROLLED TO BOTTOM
                    is_next_page = false;
                    getFeaturedPosts(++page);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                is_next_page = false;
                landingPosts.clearData();
                featuredPostsList.clear();
                loadPosts();
            }
        });
    }

    private void initRecyclerViews() {
        LinearLayoutManager horizontalLinearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLinearLayoutManager2 = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager verticalLinearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);

        mostPopularList.setLayoutManager(horizontalLinearLayoutManager);
        mostPopularList.setAdapter(new MostPopularListAdapter(landingPosts.getMostPopular(), getContext(), mListener));

        myInterestsList.setLayoutManager(verticalLinearLayoutManager);
        myInterestsList.setAdapter(new MyInterestsListAdapter(landingPosts.getUserInterests(),
                landingPosts.getMyInterests(), getContext(), mListener));

        trendingList.setLayoutManager(horizontalLinearLayoutManager2);
        trendingList.setAdapter(new TrendingListAdapter(landingPosts.getTrendingCategories(), getContext()));

        featuredVideosList.setLayoutManager(staggeredGridLayoutManager);
        featuredVideosList.setAdapter(new FeaturedVideosListAdapter(featuredPostsList, getContext(), mListener));
    }

    private void loadPosts() {
        postLoadErrorLayout.setVisibility(GONE);
        if (landingPosts != null)
            getDiscoverLandingPosts();

        if (featuredPostsList != null)
            getFeaturedPosts(page);
        swipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.most_popular_view_all) public void viewAllMostPopular() {
        mListener.onDiscoverInteraction(ACTION_VIEW_MOST_POPULAR, null, landingPosts.getMostPopular(),
                null);
    }

    @OnClick(R.id.my_interests_view_all) public void viewAllMyInterests() {
        mListener.onDiscoverInteraction(ACTION_VIEW_MY_INTERESTS, landingPosts.getUserInterests(),
                null, null);
    }

    @OnClick(R.id.discover_search) public void toggleSearch() {
        getParentActivity().pushFragment(DiscoverSearchFragment.newInstance());
    }

    @OnClick(R.id.post_load_error_layout) public void reloadStuff() {
        page = 1;
        loadPosts();
    }

    private void notifyLandingPostsDataSetChanged() {
        dismissProgressBar();
        discoverPostsContainer.setVisibility(VISIBLE);
        if (landingPosts.getMostPopular().isEmpty()) {
            mostPopularList.setVisibility(GONE);
            noMostPopular.setVisibility(VISIBLE);
        }
//        if (Collections.frequency(landingPosts.getMyInterests().values(), Collections.EMPTY_LIST)
//                == landingPosts.getMyInterests().size()) {
//            myInterestsList.setVisibility(View.GONE);
//            noMyInterests.setVisibility(View.VISIBLE);
//        }
        if (landingPosts.getTrendingCategories().isEmpty()) {
            trendingList.setVisibility(GONE);
            noTrending.setVisibility(VISIBLE);
        }
        mostPopularList.getAdapter().notifyDataSetChanged();
        //noinspection unchecked
        new NotifyMyInterestsDataSetChanged(this).execute();
        trendingList.getAdapter().notifyDataSetChanged();
    }

    private static class NotifyMyInterestsDataSetChanged extends AsyncTask<Void, Void, Void> {

        private WeakReference<DiscoverFragment> reference;

        NotifyMyInterestsDataSetChanged(DiscoverFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Map<String, ArrayList<PostDetails>> tempMap = new HashMap<>();
            tempMap.putAll(reference.get().landingPosts.getMyInterests());
            for (int i = 0; i < reference.get().landingPosts.getMyInterests().size(); i++) {
                String categoryName = reference.get().landingPosts.getUserInterests().get(i).getCategoryName();
                if (tempMap.get(categoryName).isEmpty())
                    tempMap.remove(categoryName);
            }
            reference.get().landingPosts.getMyInterests().clear();
            reference.get().landingPosts.getMyInterests().putAll(tempMap);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            reference.get().myInterestsList.getAdapter().notifyDataSetChanged();
        }
    }
    
    private void getDiscoverLandingPosts() {
        landingPosts.clearData();
        landingPostCall = ApiCallingService.Discover.getDiscoverPagePosts(getContext());
        if (!landingPostCall.isExecuted())
            landingPostCall.enqueue(new Callback<LandingPosts>() {
                @Override
                public void onResponse(Call<LandingPosts> call, Response<LandingPosts> response) {
                    try {
                        if (isAdded()) {
                            if (response.code() == 200) {
                                LandingPosts tempLandingPosts = response.body();
                                landingPosts.getMostPopular().addAll(tempLandingPosts.getMostPopular());
                                landingPosts.getUserInterests().addAll(tempLandingPosts.getUserInterests());
                                landingPosts.getTrendingCategories().addAll(tempLandingPosts.getTrendingCategories());
                                landingPosts.getMyInterests().putAll(tempLandingPosts.getMyInterests());
                                notifyLandingPostsDataSetChanged();
                            } else {
                                Log.e("GetDiscoverLandingPosts", response.code() + "_" + response.message());
                                showErrorMessage(getString(R.string.something_went_wrong), true);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<LandingPosts> call, Throwable t) {
                    t.printStackTrace();
                    if (isAdded()) {
                        showErrorMessage(getString(R.string.something_went_wrong), true);
                    }
                }
            });
    }

    private void getFeaturedPosts(final int page) {
        if (page == 1)
            featuredPostsList.clear();

        featuredPostsCall = ApiCallingService.Discover.getFeaturedPosts(page, getContext());
        if (!featuredPostsCall.isExecuted()) {
            featuredPostsCall.enqueue(new Callback<PostList>() {
                @Override
                public void onResponse(Call<PostList> call, Response<PostList> response) {
                    try {
                        if (isAdded()) {
                            if (response.code() == 200) {
                                PostList postList = response.body();
                                if (!postList.getPosts().isEmpty()) {
                                    if (page == 1) featuredPostsList.clear();

                                    is_next_page = postList.isNextPage();

                                    featuredPostsContainer.setVisibility(VISIBLE);
                                    featuredVideosList.setVisibility(VISIBLE);
                                    noFeaturedVideos.setVisibility(GONE);
                                    featuredPostsList.addAll(postList.getPosts());
                                    if (page == 1)
                                        featuredVideosList.getAdapter().notifyDataSetChanged();
                                    else
                                        featuredVideosList.getAdapter()
                                                .notifyItemRangeInserted((page - 1) * 10, postList.getPosts().size());
                                } else if (page == 1){
                                    featuredPostsContainer.setVisibility(VISIBLE);
                                    featuredVideosList.setVisibility(GONE);
                                    noFeaturedVideos.setVisibility(VISIBLE);
                                }
                            } else {
                                Log.e("GetFeaturedPosts", response.code() + "_" + response.message());
                                showErrorMessage(getString(R.string.something_went_wrong), false);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PostList> call, Throwable t) {
                    t.printStackTrace();
                    if (isAdded()) {
                        showErrorMessage(getString(R.string.something_went_wrong), false);
                    }
                }
            });
        }
    }

    private void showErrorMessage(String message, boolean isLandingPosts) {
        dismissProgressBar();
        if (isLandingPosts)
            discoverPostsContainer.setVisibility(GONE);
        else
            featuredPostsContainer.setVisibility(GONE);
        postLoadErrorLayout.setVisibility(VISIBLE);
        String errorString = getString(R.string.could_not_load_posts) + "\n" + message;
        postLoadErrorTextView.setText(errorString);
    }

    public void dismissProgressBar() {
        if (progressBar.getVisibility() == VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDiscoverInteractionListener) {
            mListener = (OnDiscoverInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnUploadFragmentInteractionListener");
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        landingPostCall.cancel();
        featuredPostsCall.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        getParentActivity().updateToolbarTitle(previousTitle);
    }

    public interface OnDiscoverInteractionListener {
        void onDiscoverInteraction(int action, ArrayList<Category> categories,
                                   ArrayList<PostDetails> postDetailsArrayList, PostDetails postDetails);
//        void backPressedAction(ProximaNovaRegularAutoCompleteTextView searchBtn);
    }
}