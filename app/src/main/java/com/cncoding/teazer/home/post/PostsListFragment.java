package com.cncoding.teazer.home.post;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;

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
    @BindView(R.id.post_list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.post_load_error) ProximaNovaRegularTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;

    public static boolean isRefreshing;
    public static PostDetails postDetails;
    public static int positionToUpdate = -1;
    private Call<PostList> postListCall;
    private ArrayList<PostDetails> postList;
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
        if (postList == null)
            postList = new ArrayList<>();
//        else
//            progressBar.setVisibility(View.GONE);

        postListAdapter = new PostsListAdapter(postList, getContext());
        recyclerView.setAdapter(postListAdapter);
        recyclerView.setSaveEnabled(true);
        recyclerView.setOnKeyListener(this);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
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
//        if (postListCall != null)
//            postListCall.cancel();
        getHomePagePosts(1, true);
        scrollListener.resetState();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        try {
            if (keyEvent.getAction() == ACTION_DOWN) {
                switch (keyCode) {
                    case KEYCODE_VOLUME_DOWN:
                        recyclerView.smoothScrollBy(0, (int) recyclerView.getChildAt(recyclerView.getChildCount() - 2).getY(),
                                new DecelerateInterpolator());
                        return true;
                    case KEYCODE_VOLUME_UP:
                        int[] positions = new int[2];
                        if (((StaggeredGridLayoutManager) recyclerView.getLayoutManager())
                                .findFirstCompletelyVisibleItemPositions(positions)[0] == 0)
                            refreshPosts();
                        else
                            recyclerView.smoothScrollBy(0, -((int) recyclerView.getChildAt(recyclerView.getChildCount() - 1).getY()),
                                new DecelerateInterpolator());
                        return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
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
        getHomePagePosts(1, false);

        if (postList != null && !isRefreshing) {
            if (postList.isEmpty()) {
                getHomePagePosts(1, false);
            } else {
                recyclerView.getAdapter().notifyDataSetChanged();
//                dismissProgressBar();
            }
        }
        else {
            isRefreshing = false;
            if (postList != null && postDetails == null) {
                try {
                    postList.remove(positionToUpdate);
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
    }

    public void getHomePagePosts(final int page, final boolean isRefreshing) {
        if (isRefreshing) swipeRefreshLayout.setRefreshing(true);
//        progressBar.setVisibility(View.VISIBLE);

        postListCall = ApiCallingService.Posts.getHomePagePosts(page, getActivity());

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
                                    if (page == 1 && postList.isEmpty())
                                        showErrorMessage(getContext().getString(R.string.no_posts_available));
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
                        postListAdapter.clearDimensions();
                        if (page == 1) {
                            postList.clear();
                            postList.addAll(tempPostList.getPosts());
                            postListAdapter.notifyDataSetChanged();
                        } else {
                            postList.addAll(tempPostList.getPosts());
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
                    if (getActivity() != null) {
                        showErrorMessage(getActivity().getString(R.string.something_went_wrong));
                    }
                    dismissRefreshView();
                }
            });
    }

    public void scrollToTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    private void showErrorMessage(String message) {
//                    dismissProgressBar();
        recyclerView.setVisibility(View.INVISIBLE);
        postLoadErrorLayout.setVisibility(View.VISIBLE);
        postLoadErrorTextView.setText(message);
    }
//    private void updatePostListItems(List<RealmPostDetails> newPostDetailsList) {
//        if (postList.size() >= newPostDetailsList.size()) {
//            List<RealmPostDetails> oldPostDetailsList = postList.subList(0, newPostDetailsList.size() - 1);
//            PostListDiffCallback diffCallback = new PostListDiffCallback(oldPostDetailsList, newPostDetailsList);
//            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, true);
//            postList.clear();
//            postList.addAll(newPostDetailsList);
//            diffResult.dispatchUpdatesTo(postListAdapter);
//        } else {
//            postList.clear();
//            postList.addAll(newPostDetailsList);
//            postListAdapter.notifyDataSetChanged();
//        }
//    }

//    public void dismissProgressBar() {
//        if (progressBar.getVisibility() == View.VISIBLE) {
//            progressBar.setVisibility(View.GONE);
//        }
//    }

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