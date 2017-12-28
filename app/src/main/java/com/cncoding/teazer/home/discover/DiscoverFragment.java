package com.cncoding.teazer.home.discover;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
//    private LandingPosts landingPosts;
    private ArrayList<PostDetails> mostPopular;
    private ArrayList<Category> userInterests;
    private ArrayList<Category> trendingCategories;
    private HashMap<String, ArrayList<PostDetails>> myInterests;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFeaturedPosts(1);
    }


    @Override
    public void onResume() {
        super.onResume();
        loadPosts();
    }

    private void initMembersIfEmpty() {
        page = 1;

        if (featuredPostsList == null) {
            featuredPostsList = new ArrayList<>();
        }
        if (mostPopular == null) {
            mostPopular = new ArrayList<>();
        }
        if (userInterests == null) {
            userInterests = new ArrayList<>();
        }
        if (trendingCategories == null) {
            trendingCategories = new ArrayList<>();
        }
        if (myInterests == null) {
            myInterests = new HashMap<>();
        }
//        if (landingPosts == null) {
//            landingPosts = new LandingPosts(mostPopular, userInterests, trendingCategories, myInterests);
//        }

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
        mostPopularList.setAdapter(new MostPopularListAdapter(mostPopular, getContext(), mListener));

        myInterestsList.setLayoutManager(verticalLinearLayoutManager);
        myInterestsList.setAdapter(new MyInterestsListAdapter(userInterests, myInterests, getContext(), mListener));

        trendingList.setLayoutManager(horizontalLinearLayoutManager2);
        trendingList.setAdapter(new TrendingListAdapter(trendingCategories, getContext()));

        featuredVideosList.setLayoutManager(staggeredGridLayoutManager);
        featuredVideosList.setAdapter(new FeaturedVideosListAdapter(featuredPostsList, getContext(), mListener));
    }

    private void loadPosts() {
        postLoadErrorLayout.setVisibility(GONE);
        if (!isLandingPostListsNull())
            getDiscoverLandingPosts();

        if (featuredPostsList != null && is_next_page)
            getFeaturedPosts(page);
    }

    private boolean isLandingPostListsNull() {
        return mostPopular == null && userInterests == null && trendingCategories == null && myInterests == null;
    }

    @OnClick(R.id.most_popular_view_all) public void viewAllMostPopular() {
        mListener.onDiscoverInteraction(ACTION_VIEW_MOST_POPULAR, null, mostPopular, null);
    }

    @OnClick(R.id.my_interests_view_all) public void viewAllMyInterests() {
        mListener.onDiscoverInteraction(ACTION_VIEW_MY_INTERESTS, userInterests, null, null);
    }

    @OnClick(R.id.discover_search) public void toggleSearch() {
        getParentActivity().pushFragment(DiscoverSearchFragment.newInstance());
    }

    @OnClick(R.id.post_load_error_layout) public void reloadStuff() {
        page = 1;
        loadPosts();
    }

    private void notifyLandingPostsDataSetChanged() {
        discoverPostsContainer.setVisibility(VISIBLE);
        if (mostPopular.isEmpty()) {
            mostPopularList.setVisibility(GONE);
            noMostPopular.setVisibility(VISIBLE);
        }
//        if (Collections.frequency(landingPosts.getMyInterests().values(), Collections.EMPTY_LIST)
//                == landingPosts.getMyInterests().size()) {
//            myInterestsList.setVisibility(View.GONE);
//            noMyInterests.setVisibility(View.VISIBLE);
//        }
        if (trendingCategories.isEmpty()) {
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
            try {
                Map<String, ArrayList<PostDetails>> tempMap = new HashMap<>();
                tempMap.putAll(reference.get().myInterests);
                for (int i = 0; i < reference.get().myInterests.size(); i++) {
                    String categoryName = reference.get().userInterests.get(i).getCategoryName();
                    if (tempMap.get(categoryName) != null && tempMap.get(categoryName).isEmpty())
                        tempMap.remove(categoryName);
                }
                reference.get().myInterests.clear();
                reference.get().myInterests.putAll(tempMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            reference.get().myInterestsList.getAdapter().notifyDataSetChanged();
        }
    }
    
    private void getDiscoverLandingPosts() {
        landingPostCall = ApiCallingService.Discover.getDiscoverPagePosts(getContext());
        if (!landingPostCall.isExecuted())
            landingPostCall.enqueue(new Callback<LandingPosts>() {
                @Override
                public void onResponse(Call<LandingPosts> call, Response<LandingPosts> response) {
                    try {
                        if (isAdded()) {
                            if (response.code() == 200) {
                                new RefreshLandingPosts(DiscoverFragment.this).execute(response.body());
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

    private static class RefreshLandingPosts extends AsyncTask<LandingPosts, Void, Void> {

        private WeakReference<DiscoverFragment> reference;

        RefreshLandingPosts(DiscoverFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(LandingPosts... landingPosts) {
            reference.get().clearLandingPostData();
            reference.get().mostPopular.addAll(landingPosts[0].getMostPopular());
            reference.get().userInterests.addAll(landingPosts[0].getUserInterests());
            reference.get().trendingCategories.addAll(landingPosts[0].getTrendingCategories());
            reference.get().myInterests.putAll(landingPosts[0].getMyInterests());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            reference.get().notifyLandingPostsDataSetChanged();
        }
    }

    private void clearLandingPostData() {
        if (mostPopular != null) mostPopular.clear();
        if (userInterests != null) userInterests.clear();
        if (trendingCategories != null) trendingCategories.clear();
        if (myInterests != null) myInterests.clear();
    }

    private void getFeaturedPosts(final int page) {
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
        if (isLandingPosts)
            discoverPostsContainer.setVisibility(GONE);
        else
            featuredPostsContainer.setVisibility(GONE);
        postLoadErrorLayout.setVisibility(VISIBLE);
        String errorString = getString(R.string.could_not_load_posts) + "\n" + message;
        postLoadErrorTextView.setText(errorString);
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
        try {
            landingPostCall.cancel();
            featuredPostsCall.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
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