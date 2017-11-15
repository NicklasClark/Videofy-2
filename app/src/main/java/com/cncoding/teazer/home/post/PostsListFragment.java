package com.cncoding.teazer.home.post;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsListFragment extends BaseFragment {
    private static final String COLUMN_COUNT = "columnCount";

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.transition_pager) ViewPager pager;
    @BindView(R.id.transition_full_background)  View background;
    @BindView(R.id.post_load_error) ProximaNovaBoldTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;

    public static boolean returningFromUpload = false;

    private ArrayList<PostDetails> postList;
    private int columnCount = 2;
    private PostsListAdapter postListAdapter;
    private StaggeredGridLayoutManager manager;

    public PostsListFragment() {
        // Required empty public constructor
    }

    public static PostsListFragment newInstance(int columnCount) {
        PostsListFragment fragment = new PostsListFragment();
        Bundle args = new Bundle();
        args.putInt(COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            columnCount = getArguments().getInt(COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts_list, container, false);
        ButterKnife.bind(this, rootView);
        postList = new ArrayList<>();
        ((BaseBottomBarActivity) getActivity()).updateToolbarTitle(null);

        postListAdapter = new PostsListAdapter(postList, getContext(), this);
        recyclerView.setAdapter(postListAdapter);
        manager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if (page > 1)
                    getHomePagePosts(page, false);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomePagePosts(1, true);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (postListAdapter != null && returningFromUpload) {
            postListAdapter.notifyDataSetChanged();
            returningFromUpload = false;
        }
        else if (postList != null && postList.size() == 0)
            getHomePagePosts(1, false);

        ((BaseBottomBarActivity) getActivity()).showAppBar();
    }

    private void getHomePagePosts(int page, final boolean isRefreshing) {
        progressBar.setVisibility(View.VISIBLE);
        if (page == 1) postList.clear();
        ApiCallingService.Posts.getHomePagePosts(page, getContext())
                .enqueue(new Callback<PostList>() {
                    @Override
                    public void onResponse(Call<PostList> call, Response<PostList> response) {
                        switch (response.code()) {
                            case 200:
                                postList.addAll(response.body().getPosts());
                                recyclerView.setVisibility(View.VISIBLE);
                                postListAdapter.notifyDataSetChanged();
                                break;
                            default:
                                showErrorMessage("Error " + response.code() +": " + response.message());
                                break;
                        }
                        if (isRefreshing)
                            swipeRefreshLayout.setRefreshing(false);
                    }

                    private void showErrorMessage(String message) {
                        dismissProgressBar();
                        recyclerView.setVisibility(View.INVISIBLE);
                        postLoadErrorLayout.animate().alpha(1).setDuration(280).start();
                        postLoadErrorLayout.setVisibility(View.VISIBLE);
                        postLoadErrorTextView.setText(getString(R.string.could_not_load_posts) + "\n" + message);
                    }

                    @Override
                    public void onFailure(Call<PostList> call, Throwable t) {
                        if (t.getMessage().contains("resolve"))
                            showErrorMessage("No internet connection found!");
                        else showErrorMessage("Error: " + t.getMessage());

                        if (isRefreshing)
                            swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    public void dismissProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.animate().scaleX(0).setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            }, 400);
        }
    }

    @OnClick(R.id.post_load_error_layout) public void reloadPosts() {
        if (postListAdapter != null)
            postListAdapter.notifyDataSetChanged();
        scrollListener.resetState();
        getHomePagePosts(1, false);
    }

    @OnClick(R.id.fab) public void changeLayoutManager() {
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        } else
            recyclerView.setLayoutManager(manager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scrollListener.resetState();
        scrollListener = null;
        postListAdapter = null;
    }
}