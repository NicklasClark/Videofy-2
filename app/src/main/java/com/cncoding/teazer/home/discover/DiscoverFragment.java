package com.cncoding.teazer.home.discover;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.Pojos.Post.LandingPosts;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

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
    private OnSearchInteractionListener mListener;
    private ArrayList<PostDetails> featuredPostsList;
    private LandingPosts landingPosts;

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
        getParentActivity().showAppBar();
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

    private void loadPosts() {
        postLoadErrorLayout.setVisibility(GONE);
        if (landingPosts != null && landingPosts.isEmpty())
            new GetDiscoverLandingPosts(this).execute();
        else if (updateMyInterests) {
            updateMyInterests = false;
            new GetDiscoverLandingPosts(this).execute();
        } else
            notifyLandingPostsDataSetChanged();

        if (featuredPostsList != null && featuredPostsList.isEmpty())
            new GetFeaturedPosts(this, 1).execute();
        else {
            featuredPostsContainer.setVisibility(VISIBLE);
            featuredVideosList.getAdapter().notifyDataSetChanged();
        }
    }

    private void initMembersIfEmpty() {
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

    @OnClick(R.id.most_popular_view_all) public void viewAllMostPopular() {
        mListener.onSearchInteraction(ACTION_VIEW_MOST_POPULAR, null, landingPosts.getMostPopular(),
                null, null);
    }

    @OnClick(R.id.my_interests_view_all) public void viewAllMyInterests() {
        mListener.onSearchInteraction(ACTION_VIEW_MY_INTERESTS, landingPosts.getUserInterests(),
                null, null, null);
    }

    @OnClick(R.id.discover_search) public void toggleSearch() {
        getParentActivity().pushFragment(DiscoverSearchFragment.newInstance());
    }

    @OnClick(R.id.post_load_error_layout) public void reloadStuff() {
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
        myInterestsList.getAdapter().notifyDataSetChanged();
        trendingList.getAdapter().notifyDataSetChanged();
    }

    private static class GetDiscoverLandingPosts extends AsyncTask<Void, Void, Void> {

        private WeakReference<DiscoverFragment> reference;

        GetDiscoverLandingPosts(DiscoverFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reference.get().landingPosts.clearData();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiCallingService.Discover.getDiscoverPagePosts(reference.get().getContext())
                    .enqueue(new Callback<LandingPosts>() {
                        @Override
                        public void onResponse(Call<LandingPosts> call, Response<LandingPosts> response) {
                            if (response.code() == 200) {
                                reference.get().landingPosts.getMostPopular().addAll(response.body().getMostPopular());
                                reference.get().landingPosts.getUserInterests().addAll(response.body().getUserInterests());
                                reference.get().landingPosts.getTrendingCategories().addAll(response.body().getTrendingCategories());
                                reference.get().landingPosts.getMyInterests().putAll(response.body().getMyInterests());
                                reference.get().notifyLandingPostsDataSetChanged();
                            } else {
                                Log.e("GetDiscoverLandingPosts", response.code() + "_" + response.message());
                                reference.get().showErrorMessage(reference.get().getString(R.string.something_went_wrong),
                                        true);
                            }
                        }

                        @Override
                        public void onFailure(Call<LandingPosts> call, Throwable t) {
                            if (reference.get().isAdded()) {
                                Log.e("GetDiscoverLandingPosts", t.getMessage());
                                reference.get().showErrorMessage(t.getMessage(), true);
                            }
                        }
                    });
            return null;
        }
    }

    private static class GetFeaturedPosts extends AsyncTask<Void, Void, Void> {

        private WeakReference<DiscoverFragment> reference;
        private int page;

        GetFeaturedPosts(DiscoverFragment context, int page) {
            reference = new WeakReference<>(context);
            this.page = page;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiCallingService.Discover.getFeaturedPosts(page, reference.get().getContext())
                    .enqueue(new Callback<PostList>() {
                        @Override
                        public void onResponse(Call<PostList> call, Response<PostList> response) {
                            if (response.code() == 200) {
                                if (response.body().getPosts().isEmpty()) {
                                    reference.get().featuredPostsContainer.setVisibility(GONE);
                                    reference.get().featuredVideosList.setVisibility(GONE);
                                    reference.get().noFeaturedVideos.setVisibility(VISIBLE);
                                } else {
                                    reference.get().featuredPostsContainer.setVisibility(VISIBLE);
                                    reference.get().featuredVideosList.setVisibility(VISIBLE);
                                    reference.get().noFeaturedVideos.setVisibility(GONE);
                                    reference.get().featuredPostsList.addAll(response.body().getPosts());
                                    reference.get().featuredVideosList.getAdapter().notifyDataSetChanged();
                                }
                            } else {
                                Log.e("GetFeaturedPosts", response.code() + "_" + response.message());
                                reference.get().showErrorMessage(reference.get().getString(R.string.something_went_wrong),
                                        false);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostList> call, Throwable t) {
                            if (reference.get().isAdded()) {
                                Log.e("GetFeaturedPosts", t.getMessage());
                                reference.get().showErrorMessage(t.getMessage(), false);
                            }
                        }
                    });
            return null;
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
        if (context instanceof OnSearchInteractionListener) {
            mListener = (OnSearchInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnUploadFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        getParentActivity().updateToolbarTitle(previousTitle);
    }

    public interface OnSearchInteractionListener {
        void onSearchInteraction(int action, ArrayList<Category> categories,
                                 ArrayList<PostDetails> postDetailsArrayList, PostDetails postDetails, byte[] image);
//        void backPressedAction(ProximaNovaRegularAutoCompleteTextView searchBtn);
    }
}