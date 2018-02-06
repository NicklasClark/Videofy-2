package com.cncoding.teazer.home.post;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
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
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.data.viewmodel.PostDetailsViewModel;
import com.cncoding.teazer.data.viewmodel.factory.PostDetailsViewModelFactory;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.utilities.SharedPrefs;

import java.util.ArrayList;
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
    private int currentPage;
    private PostDetailsViewModel postDetailsViewModel;
    public static PostDetails postDetails;
    public static int positionToUpdate = -1;
    private ArrayList<PostDetails> postList;
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
        PostDetailsViewModelFactory factory = new PostDetailsViewModelFactory(SharedPrefs.getAuthToken(getContext()));
        postDetailsViewModel = ViewModelProviders.of(this, factory).get(PostDetailsViewModel.class);
        currentPage = 1;
        if (postList == null)
            postList = new ArrayList<>();
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
        postListAdapter = new PostsListAdapter(postList, getContext());
        recyclerView.setAdapter(postListAdapter);
//        if (savedPosition == null)
//            savedPosition = new int[2];
        recyclerView.setSaveEnabled(true);
//        recyclerView.setOnKeyListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getParentActivity(), LinearLayoutManager.VERTICAL, false));
        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void loadFirstPage() {
                currentPage = 1;
                getHomePagePosts(1);
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

    private void refreshPosts() {
        swipeRefreshLayout.setRefreshing(true);
        getHomePagePosts(1);
        scrollListener.resetState();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
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
        if (postList != null && !isRefreshing) {
            if (!postList.isEmpty()) recyclerView.getAdapter().notifyDataSetChanged();
        }
        else {
            isRefreshing = false;
            updateOrRemovePostDetailsAtThePosition();
            }
//            if (savedPosition[1] > 4)
//                recyclerView.getLayoutManager().scrollToPosition(savedPosition[1]);
        }

    private void updateOrRemovePostDetailsAtThePosition() {
        if (postList != null && postDetails == null) {
            try {
                postList.remove(positionToUpdate);
                postListAdapter.notifyItemRemoved(positionToUpdate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (postListAdapter != null) {
                postListAdapter.notifyItemChanged(positionToUpdate, postDetails);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPositions(savedPosition);
    }

    private void handleResponse(List<PostDetails> postDetailsList) {
        postListAdapter.addPosts(currentPage, postDetailsList);
    }

    private void handleError(String message) {
        if (currentPage > 1 || postList.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
            postLoadErrorLayout.setVisibility(View.VISIBLE);
            postLoadErrorTextView.setText(message);
        }
    }

    public void scrollToTop() {
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getParentActivity().updateToolbarTitle(previousTitle);
//        postListAdapter = null;
        positionToUpdate = -1;
    }
}