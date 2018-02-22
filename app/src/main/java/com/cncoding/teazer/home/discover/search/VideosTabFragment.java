package com.cncoding.teazer.home.discover.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CustomLinearLayoutManager;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.discover.BaseDiscoverFragment;
import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.discover.VideosList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static com.cncoding.teazer.home.discover.search.DiscoverSearchFragment.SEARCH_TERM;

public class VideosTabFragment extends BaseDiscoverFragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_posts) ProximaNovaRegularTextView noPosts;
    @BindView(R.id.no_posts_2) ProximaNovaBoldTextView noPosts2;

    private DiscoverSearchAdapter adapter;
    private String searchTerm;
    private boolean isSearchTerm;

    public VideosTabFragment() {}

    public static VideosTabFragment newInstance(String searchTerm) {
        VideosTabFragment fragment = new VideosTabFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_TERM, searchTerm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchTerm = getArguments().getString(SEARCH_TERM);
            isSearchTerm = searchTerm != null && !searchTerm.isEmpty();
        }
        currentPage = 1;
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        ButterKnife.bind(this, rootView);

        adapter = new DiscoverSearchAdapter(this, true, isSearchTerm);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(context, VERTICAL, false));
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page) {
                    is_next_page = false;
                    currentPage = page;
                    getVideos(page);
                }
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

    @Override public void onResume() {
        super.onResume();
        getVideos(1);
    }

    private void getVideos(int page) {
        if (isSearchTerm) loadVideosWithSearchTerm(page, searchTerm);
        else loadTrendingVideos(page);
    }

    @Override protected void handleResponse(BaseModel resultObject) {
        if (resultObject instanceof VideosList) {
            VideosList videosList = (VideosList) resultObject;
            is_next_page = videosList.isNextPage();
            if (videosList.getVideos() != null && !videosList.getVideos().isEmpty()) {
                dataAvailable();
                adapter.updateVideosList(currentPage, videosList.getVideos());
            }
            else if (currentPage == 1) noDataAvailable();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override protected void handleError(BaseModel baseModel) {
        baseModel.getError().printStackTrace();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void dataAvailable() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        noPosts.setVisibility(View.GONE);
        noPosts2.setVisibility(View.GONE);
    }

    private void noDataAvailable() {
        swipeRefreshLayout.setVisibility(View.GONE);
        noPosts.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_no_data_placeholder, 0, 0);
        noPosts.setText(R.string.no_videos_match_your_search_criteria);
        noPosts.setVisibility(View.VISIBLE);
        noPosts2.setVisibility(View.INVISIBLE);
    }

    @Override public void onDetach() {
        super.onDetach();
        try {
            noPosts.setText(R.string.search_for_videos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}