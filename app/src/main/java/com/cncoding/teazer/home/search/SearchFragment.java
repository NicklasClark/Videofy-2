package com.cncoding.teazer.home.search;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.home.search.adapters.FeaturedVideosListAdapter;
import com.cncoding.teazer.home.search.adapters.MostPopularListAdapter;
import com.cncoding.teazer.home.search.adapters.MyInterestsListAdapter;
import com.cncoding.teazer.home.search.adapters.TrendingListAdapter;
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

public class SearchFragment extends BaseFragment {

    public static final int ACTION_VIEW_MY_INTERESTS = 1;
    public static final int ACTION_VIEW_MOST_POPULAR = 2;
    public static final int ACTION_VIEW_TRENDING = 3;

    @BindView(R.id.most_popular_list) RecyclerView mostPopularList;
    @BindView(R.id.my_interests_list) RecyclerView myInterestsList;
    @BindView(R.id.trending_list) RecyclerView trendingList;
    @BindView(R.id.featured_videos_list) RecyclerView featuredVideosList;
//    @BindView(R.id.my_interests_view_all) ProximaNovaBoldTextView viewAllMyInterests;
    @BindView(R.id.no_most_popular) ProximaNovaBoldTextView noMostPopular;
//    @BindView(R.id.no_my_interests) ProximaNovaBoldTextView noMyInterests;
    @BindView(R.id.no_trending) ProximaNovaBoldTextView noTrending;
    @BindView(R.id.no_featured_videos) ProximaNovaBoldTextView noFeaturedVideos;

    private OnSearchInteractionListener mListener;
    private ArrayList<PostDetails> featuredPostsList;
    private LandingPosts landingPosts;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        featuredPostsList = new ArrayList<>();
        landingPosts = new LandingPosts(new ArrayList<PostDetails>(), new ArrayList<Category>(),
                new ArrayList<Category>(), new HashMap<String, ArrayList<PostDetails>>());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        previousTitle = getParentActivity().getToolbarTitle();
        getParentActivity().updateToolbarTitle(getString(R.string.title_notifications));
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);

        initRecyclerViews();

        new GetDiscoverLandingPosts(this).execute();

        new GetFeaturedPosts(this, 1).execute();

        return rootView;
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
        mostPopularList.setAdapter(new MostPopularListAdapter(landingPosts.getMostPopular(), getContext()));

        myInterestsList.setLayoutManager(verticalLinearLayoutManager);
        myInterestsList.setAdapter(new MyInterestsListAdapter(
                landingPosts.getUserInterests(), landingPosts.getMyInterests(), getContext()));

        trendingList.setLayoutManager(horizontalLinearLayoutManager2);
        trendingList.setAdapter(new TrendingListAdapter(landingPosts.getTrendingCategories(), getContext()));

        featuredVideosList.setLayoutManager(staggeredGridLayoutManager);
        featuredVideosList.setAdapter(new FeaturedVideosListAdapter(featuredPostsList, getContext()));
    }

    @OnClick(R.id.my_interests_view_all) public void viewAllMyInterests() {
        mListener.onSearchInteraction(ACTION_VIEW_MY_INTERESTS, landingPosts.getUserInterests(), null);
    }

    @OnClick(R.id.most_popular_view_all) public void viewAllMostPopular() {
        mListener.onSearchInteraction(ACTION_VIEW_MOST_POPULAR, null, landingPosts.getMostPopular());
    }

    private void notifyLandingPostsDataSetChanged() {
        if (landingPosts.getMostPopular().size() == 0) {
            mostPopularList.setVisibility(View.GONE);
            noMostPopular.setVisibility(View.VISIBLE);
        }
//        if (Collections.frequency(landingPosts.getMyInterests().values(), Collections.EMPTY_LIST)
//                == landingPosts.getMyInterests().size()) {
//            myInterestsList.setVisibility(View.GONE);
//            noMyInterests.setVisibility(View.VISIBLE);
//        }
        if (landingPosts.getTrendingCategories().isEmpty()) {
            trendingList.setVisibility(View.GONE);
            noTrending.setVisibility(View.VISIBLE);
        }
        mostPopularList.getAdapter().notifyDataSetChanged();
        myInterestsList.getAdapter().notifyDataSetChanged();
        trendingList.getAdapter().notifyDataSetChanged();
    }

    private static class GetDiscoverLandingPosts extends AsyncTask<Void, Void, Void> {

        private WeakReference<SearchFragment> reference;

        GetDiscoverLandingPosts(SearchFragment context) {
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
                            } else
                                Log.e("GetDiscoverLandingPosts", response.code() + "_" + response.message());
                        }

                        @Override
                        public void onFailure(Call<LandingPosts> call, Throwable t) {
                            Log.e("GetDiscoverLandingPosts", t.getMessage());
                        }
                    });
            return null;
        }
    }

    private static class GetFeaturedPosts extends AsyncTask<Void, Void, Void> {

        private WeakReference<SearchFragment> reference;
        private int page;

        GetFeaturedPosts(SearchFragment context, int page) {
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
                                    reference.get().featuredVideosList.setVisibility(View.GONE);
                                    reference.get().noFeaturedVideos.setVisibility(View.VISIBLE);
                                } else {
                                    reference.get().featuredPostsList.addAll(response.body().getPosts());
                                    reference.get().featuredVideosList.getAdapter().notifyDataSetChanged();
                                }
                            } else
                                Log.e("GetDiscoverLandingPosts", response.code() + "_" + response.message());
                        }

                        @Override
                        public void onFailure(Call<PostList> call, Throwable t) {
                            Log.e("GetFeaturedPosts", t.getMessage());
                        }
                    });
            return null;
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
        void onSearchInteraction(int action, ArrayList<Category> categories, ArrayList<PostDetails> postDetailsArrayList);
    }
}
