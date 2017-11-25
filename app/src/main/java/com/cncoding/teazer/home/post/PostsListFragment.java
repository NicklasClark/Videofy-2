package com.cncoding.teazer.home.post;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
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

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.post_load_error) ProximaNovaBoldTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;
    @BindView(R.id.tap_to_retry) ProximaNovaRegularTextView tapToRetryBtn;

    public static boolean returningFromUpload = false;

    private ArrayList<PostDetails> postList;
    private PostsListAdapter postListAdapter;
    private StaggeredGridLayoutManager manager;
    public PostsListFragment() {
        // Required empty public constructor
    }
    
    public static PostsListFragment newInstance() {
        return new PostsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts_list, container, false);
        ButterKnife.bind(this, rootView);
        postList = new ArrayList<>();
        if (getActivity() != null) {
            ((BaseBottomBarActivity) getActivity()).updateToolbarTitle(null);
        }

        postListAdapter = new PostsListAdapter(postList, getContext(), this);
        recyclerView.setAdapter(postListAdapter);
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                ((BaseBottomBarActivity)getActivity()).hidesettings(false);

                if (page > 1)
                    getHomePagePosts(page, false);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
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

        if (getActivity() != null) {
            ((BaseBottomBarActivity) getActivity()).showAppBar();
        }
    }

    public void getHomePagePosts(int page, final boolean isRefreshing) {
        progressBar.setVisibility(View.VISIBLE);
        if (page == 1) postList.clear();
        ApiCallingService.Posts.getHomePagePosts(page, getContext())
                .enqueue(new Callback<PostList>() {
                    @Override
                    public void onResponse(Call<PostList> call, Response<PostList> response) {
                        switch (response.code()) {
                            case 200:
                                if (response.body().getPosts().size() > 0) {
                                    postList.addAll(response.body().getPosts());
                                    recyclerView.setVisibility(View.VISIBLE);
                                    postListAdapter.notifyDataSetChanged();
                                } else {
                                    showErrorMessage(getString(R.string.no_posts_available));
                                    tapToRetryBtn.setVisibility(View.INVISIBLE);
                                }
                                break;
                            default:
                                showErrorMessage("Error " + response.code() +": " + response.message());
//                                AuthUtils.logout(getContext(), getActivity());
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
        postListAdapter = null;
    }
}