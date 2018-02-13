package com.cncoding.teazer.home.post.homepage;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CustomLinearLayoutManager;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.data.viewmodel.PostDetailsViewModel;
import com.cncoding.teazer.data.viewmodel.factory.AuthTokenViewModelFactory;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.utilities.SharedPrefs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;

public class PostsListFragment extends BaseFragment implements View.OnKeyListener {

//    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.post_list) Container recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.post_load_error) ProximaNovaRegularTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;

    public static boolean isRefreshing;
    public static PostDetails postDetails;
    public static int positionToUpdate = -1;
    private int currentPage;
    private PostDetailsViewModel postDetailsViewModel;
    private PostsListAdapter postListAdapter;

    public PostsListFragment() {
    }
    
    @NonNull
    public static PostsListFragment newInstance() {
        return new PostsListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthTokenViewModelFactory factory = new AuthTokenViewModelFactory(SharedPrefs.getAuthToken(getContext()));
        postDetailsViewModel = ViewModelProviders.of(this, factory).get(PostDetailsViewModel.class);
        currentPage = 1;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posts_list, container, false);
        previousTitle = getParentActivity().getToolbarTitle();
        getParentActivity().updateToolbarTitle(null);
        ButterKnife.bind(this, rootView);

        prepareRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefreshing = true;
                refreshPosts();
            }
        });

        // Handle changes emitted by LiveData
        postDetailsViewModel.getPostList().observe(this, new Observer<PostList>() {
            @Override
            public void onChanged(@Nullable PostList postList) {
                if (postList != null) {
                    if (postList.getError() != null) {
                        handleError(getString(R.string.something_went_wrong));
                    } else {
                        is_next_page = postList.isNextPage();
                        handleResponse(postList.getPosts());
                    }
                } else {
                    handleError(getString(R.string.something_went_wrong));
                }
            }
        });

        return rootView;
    }

    private void prepareRecyclerView() {
        postListAdapter = new PostsListAdapter(getContext());
        recyclerView.setAdapter(postListAdapter);
        bindRecyclerViewAdapter(postListAdapter);
        recyclerView.setSaveEnabled(true);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getParentActivity(), LinearLayoutManager.VERTICAL, false));
        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void loadFirstPage() {
                if (PostsListFragment.this.postDetailsViewModel.getPostList().getValue() == null) {
                    currentPage = 1;
                    refreshPosts();
                }
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page) {
                    currentPage = page;
                    getHomePagePosts(page);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void refreshPosts() {
        if (!swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(true);
        if (!isListAtTop()) recyclerView.scrollToPosition(0);
        toggleRecyclerViewScrolling(false);
        if (scrollListener != null) scrollListener.resetState();
        postDetailsViewModel.clearData();
        getHomePagePosts(1);
    }

    public void getHomePagePosts(int page) {
        postDetailsViewModel.loadPostList(page);
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

    @Override
    public void onResume() {
        super.onResume();
        if (isRefreshing) {
            isRefreshing = false;
            updateOrRemovePostDetailsAtThePosition();
        }
    }

    private void updateOrRemovePostDetailsAtThePosition() {
        if (postDetailsViewModel.getPostList().getValue() != null && postDetails == null) {
//            Item is deleted
            try {
                postDetailsViewModel.getPostList().getValue().getPosts().remove(positionToUpdate);
                postListAdapter.notifyItemRemoved(positionToUpdate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
//            Item is modified
            if (postListAdapter != null) {
                postListAdapter.notifyItemChanged(positionToUpdate, postDetails);
            }
        }
    }

    private void handleResponse(List<PostDetails> postDetailsList) {
        toggleRecyclerViewScrolling(true);
        if (isRefreshing) {
            isRefreshing = false;
            postListAdapter.updateNewPosts(postDetailsList);
        } else {
            postListAdapter.addPosts(currentPage, postDetailsList);
        }
        if (currentPage == 1) {
            if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void handleError(String message) {
        try {
            if (currentPage > 1 || postDetailsViewModel.getPostList().getValue().getPosts().isEmpty()) {
                recyclerView.setVisibility(View.INVISIBLE);
                postLoadErrorLayout.setVisibility(View.VISIBLE);
                postLoadErrorTextView.setText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            recyclerView.setVisibility(View.INVISIBLE);
            postLoadErrorLayout.setVisibility(View.VISIBLE);
            postLoadErrorTextView.setText(message);
        }
    }

    public boolean isListAtTop() {
        return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0;
    }

    private void toggleRecyclerViewScrolling(boolean enabled) {
        CustomLinearLayoutManager manager = (CustomLinearLayoutManager) recyclerView.getLayoutManager();
        manager.setScrollEnabled(enabled);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getParentActivity().updateToolbarTitle(previousTitle);
        positionToUpdate = -1;
    }
}