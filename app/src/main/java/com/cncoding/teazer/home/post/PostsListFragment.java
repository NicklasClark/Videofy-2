package com.cncoding.teazer.home.post;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.home.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;

public class PostsListFragment extends BaseFragment implements View.OnKeyListener {

//    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.post_list) AAH_CustomRecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.post_load_error) ProximaNovaRegularTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;

    public static boolean isRefreshing;
    public static PostDetails postDetails;
    public static int positionToUpdate = -1;
    private Call<PostList> postListCall;
    private ArrayList<PostDetails> postDetailsList;
    private PostsListAdapter postListAdapter;

    public PostsListFragment() {
    }
    
    public static PostsListFragment newInstance() {
        return new PostsListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (savedPosition == null)
//            savedPosition = new int[2];
        previousTitle = getParentActivity().getToolbarTitle();
        getParentActivity().updateToolbarTitle(null);
        View rootView = inflater.inflate(R.layout.fragment_posts_list, container, false);
        ButterKnife.bind(this, rootView);
        if (postDetailsList == null)
            postDetailsList = new ArrayList<>();
//        recyclerView.setOnKeyListener(this);
        postListAdapter = new PostsListAdapter(postDetailsList, getContext());
        recyclerView.setActivity(getParentActivity());
        recyclerView.setAdapter(postListAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getParentActivity(), LinearLayoutManager.VERTICAL, false));
        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getHomePagePosts(page, false);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPosts();
            }
        });

        return rootView;
    }

    private void refreshPosts() {
        if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() > 0) {
            recyclerView.smoothScrollToPosition(0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getHomePagePosts(1, true);
                    scrollListener.resetState();
                }
            }, 500);
        } else {
            getHomePagePosts(1, true);
            scrollListener.resetState();
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        try {
            if (keyEvent.getAction() == ACTION_DOWN) {
                switch (keyCode) {
                    case KEYCODE_VOLUME_DOWN:
                        recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null,
                                ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() + 1);
                        return true;
                    case KEYCODE_VOLUME_UP:
                        if (((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0)
                            refreshPosts();
                        else
                            recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null,
                                    ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() - 1);
                        return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            refreshPosts();
            return false;
        }
    }

//    @Override
//    public void onPause() {
//        ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPositions(savedPosition);
//        super.onPause();
//    }

    @Override
    public void onResume() {
        super.onResume();
        postListAdapter.registerAudioObserver();
        recyclerView.setSaveEnabled(true);
        recyclerView.setDownloadPath(Environment.getDownloadCacheDirectory() + "/Teazer");
        recyclerView.setDownloadVideos(true);
        recyclerView.setVisiblePercent(60);
        recyclerView.setPlayOnlyFirstVideo(true);

        getHomePagePosts(1, false);

        if (postDetailsList != null && !isRefreshing) {
            if (postDetailsList.isEmpty()) {
                getHomePagePosts(1, false);
            } else {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }
        else {
            isRefreshing = false;
            if (postDetailsList != null && postDetails == null) {
                try {
                    postDetailsList.remove(positionToUpdate);
                    postListAdapter.notifyItemRemoved(positionToUpdate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                postListAdapter.notifyItemChanged(positionToUpdate, postDetails);
            }
//            if (savedPosition[1] > 4)
//                recyclerView.getLayoutManager().scrollToPosition(savedPosition[1]);
        }

        recyclerView.playAvailableVideos(0);
    }

    public void getHomePagePosts(final int page, final boolean isRefreshing) {
        try {
            if (isRefreshing) swipeRefreshLayout.setRefreshing(true);
            final Context context = getParentActivity();
            postListCall = ApiCallingService.Posts.getHomePagePosts(page, context);

            if (!postListCall.isExecuted())
                postListCall.enqueue(new Callback<PostList>() {
                    @Override
                    public void onResponse(Call<PostList> call, Response<PostList> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    PostList tempPostList = response.body();
                                    if (tempPostList.getPosts() != null && tempPostList.getPosts().size() > 0) {
                                        is_next_page = tempPostList.isNextPage();
                                        updatePosts(page, tempPostList);
                                    } else {
                                        if (page == 1 && postDetailsList.isEmpty())
                                            showErrorMessage(Resources.getSystem().getString(R.string.no_posts_available));
                                    }
                                    break;
                                default:
                                    showErrorMessage("Error " + response.code() + " : " + response.message());
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            dismissRefreshView();
                        }
                    }

                    private void updatePosts(int page, PostList tempPostList) {
                        try {
//                            postListAdapter.clearDimensions();
                            if (page == 1) {
                                postDetailsList.clear();
                                postDetailsList.addAll(tempPostList.getPosts());
                                postListAdapter.notifyDataSetChanged();
                                recyclerView.smoothScrollBy(0,1);
                                recyclerView.smoothScrollBy(0,-1);
                            } else {
                                postDetailsList.addAll(tempPostList.getPosts());
                                postListAdapter.notifyItemRangeInserted((page - 1) * 30, tempPostList.getPosts().size());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    private void dismissRefreshView() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (isRefreshing)
                                    swipeRefreshLayout.setRefreshing(false);
                            }
                        }, 1000);
                    }

                    @Override
                    public void onFailure(Call<PostList> call, Throwable t) {
                        t.printStackTrace();
                        try {
                            if (isAdded()) {
                                showErrorMessage(Resources.getSystem().getString(R.string.something_went_wrong));
                                dismissRefreshView();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scrollToTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    private void showErrorMessage(String message) {
        recyclerView.setVisibility(View.INVISIBLE);
        postLoadErrorLayout.setVisibility(View.VISIBLE);
        postLoadErrorTextView.setText(message);
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerView.stopVideos();
        if (postListAdapter != null)
            postListAdapter.unregisterAudioObserver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getParentActivity().updateToolbarTitle(previousTitle);
        if (postListCall != null)
            postListCall.cancel();
        postListAdapter = null;
        positionToUpdate = -1;
//        postDetails = null;
    }
}