package com.cncoding.teazer.home.discover.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.utilities.Pojos.Discover.Videos;
import com.cncoding.teazer.utilities.Pojos.Discover.VideosList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.home.discover.search.DiscoverSearchFragment.SEARCH_TERM;

public class VideosTabFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_posts) ProximaNovaBoldTextView noPosts;

    private Call<VideosList> videosListCall;
    private ArrayList<Videos> videosList;
    private DiscoverSearchAdapter adapter;
    private String searchTerm;
    private BaseBottomBarActivity parentContext;

    public VideosTabFragment() {
    }

    public static VideosTabFragment newInstance(String searchTerm) {
        VideosTabFragment fragment = new VideosTabFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_TERM, searchTerm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchTerm = getArguments().getString(SEARCH_TERM);
        }
        videosList = new ArrayList<>();
        parentContext = getParentActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getVideos(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVideos(1);
                scrollListener.resetState();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (searchTerm != null && !searchTerm.equals("") && videosList != null) {
            getVideos(1);
        } else {
            noPosts.setVisibility(View.VISIBLE);
            noPosts.setText(R.string.search_for_videos);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (searchTerm != null && !searchTerm.equals("") && videosList != null) {
            getVideos(1);
        } else {
            noPosts.setVisibility(View.VISIBLE);
            noPosts.setText(R.string.search_for_videos);
        }
    }

    private void getVideos(final int page) {
        if (page == 1) {
            scrollListener.resetState();
            videosList.clear();
        }

        videosListCall = ApiCallingService.Discover.getVideosWithSearchTerm(page, searchTerm, getContext());
        
        if (!videosListCall.isExecuted())
            videosListCall.enqueue(new Callback<VideosList>() {
                @Override
                public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                    if (isAdded()) {
                        if (response.code() == 200) {
                            is_next_page = response.body().isNextPage();
                            if (response.body().getVideos().size() > 0) {
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                noPosts.setVisibility(View.GONE);
                                videosList.clear();
                                recyclerView.getRecycledViewPool().clear();
                                videosList.addAll(response.body().getVideos());
                                adapter = new DiscoverSearchAdapter(parentContext, VideosTabFragment.this, true, null, videosList);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                if (page == 1 && videosList.isEmpty()) {
                                    swipeRefreshLayout.setVisibility(View.GONE);
                                    noPosts.setText(R.string.no_videos_match_your_search_criteria);
                                    noPosts.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Log.e("videosListCallback", response.code() + "_" + response.message());
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<VideosList> call, Throwable t) {
                    if (isAdded()) {
                        Log.e("videosListCallback", t.getMessage() != null ? t.getMessage() : "FAILED!!!");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            noPosts.setText(R.string.search_for_videos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videosListCall != null && videosListCall.isExecuted())
            videosListCall.cancel();
        adapter = null;
    }
}
