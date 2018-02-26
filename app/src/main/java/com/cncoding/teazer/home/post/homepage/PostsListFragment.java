package com.cncoding.teazer.home.post.homepage;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CustomLinearLayoutManager;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.exoplayer.Container;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.data.viewmodel.PostDetailsViewModel;
import com.cncoding.teazer.data.viewmodel.factory.AuthTokenViewModelFactory;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;
import static com.cncoding.teazer.TeazerApplication.get;
import static com.cncoding.teazer.utilities.AuthUtils.isConnectedToWifi;
import static com.cncoding.teazer.utilities.SharedPrefs.getAuthToken;
import static com.cncoding.teazer.utilities.SharedPrefs.getCanSaveMediaOnlyOnWiFi;

public class PostsListFragment extends BaseFragment implements View.OnKeyListener {

    @BindView(R.id.post_list) Container recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.post_load_error) ProximaNovaRegularTextView postLoadErrorTextView;
    @BindView(R.id.post_load_error_layout) LinearLayout postLoadErrorLayout;

    public static boolean isRefreshing;
    public boolean manualRefreshTriggered;
    public static PostDetails postDetails;
    public static int positionToUpdate = -1;
    private int currentPage;
    private PostDetailsViewModel viewModel;
    private PostsListAdapter postListAdapter;

    public PostsListFragment() {}
    
    @NonNull
    public static PostsListFragment newInstance() {
        return new PostsListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthTokenViewModelFactory factory = new AuthTokenViewModelFactory(get(getParentActivity()), getAuthToken(getContext()));
        viewModel = ViewModelProviders.of(this, factory).get(PostDetailsViewModel.class);
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
                manualRefreshTriggered = true;
                refreshPosts(true, true);
            }
        });

        // Handle changes emitted by LiveData
        viewModel.getPostList().observe(this, new Observer<PostList>() {
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

    @Override
    public void onResume() {
        super.onResume();
        if (isRefreshing) {
            isRefreshing = false;
            updateOrRemovePostDetailsAtThePosition();
        }
    }

    private void prepareRecyclerView() {
        postListAdapter = new PostsListAdapter(this);
        recyclerView.setAdapter(postListAdapter);
        bindRecyclerViewAdapter(postListAdapter);
        recyclerView.setSaveEnabled(true);
        recyclerView.setLayoutManager(new CustomLinearLayoutManager(getParentActivity(), LinearLayoutManager.VERTICAL, false));
        if (scrollListener == null)
            scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) recyclerView.getLayoutManager()) {
                @Override
                public void loadFirstPage() {
                    currentPage = 1;
                    new InitialRetrieveTask(PostsListFragment.this).execute();
                }

                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if (isConnected) {
                        if (is_next_page) {
                            currentPage = page;
                            getHomePagePosts(page);
                        }
                    }
                    else Toast.makeText(getContext(), R.string.could_not_load_new_posts, Toast.LENGTH_SHORT).show();
                }
            };
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void refreshPosts(boolean scrollToTop, boolean isRefreshing) {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing() && isRefreshing) swipeRefreshLayout.setRefreshing(true);
        if (!isListAtTop() && scrollToTop) recyclerView.scrollToPosition(0);
        toggleRecyclerViewScrolling(false);
        if (scrollListener != null && scrollToTop) scrollListener.resetState();
        if (isConnected) getHomePagePosts(1);
        else {
            Toast.makeText(getContext(), R.string.no_internet_message, Toast.LENGTH_LONG).show();
            toggleRecyclerViewScrolling(true);
        }
    }

    public void getHomePagePosts(int page) {
        viewModel.loadPostList(page);
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
                            refreshPosts(true, true);
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

    private void updateOrRemovePostDetailsAtThePosition() {
        if (viewModel.getPostList().getValue() != null && postDetails == null) {
//            Item is deleted
            try {
                viewModel.getPostList().getValue().getPosts().remove(positionToUpdate);
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
        preDownloadVideos(postDetailsList);
        toggleRecyclerViewScrolling(true);
        if (manualRefreshTriggered) {
            manualRefreshTriggered = false;
            postListAdapter.updateNewPosts(postDetailsList);
        } else {
            postListAdapter.addPosts(currentPage, postDetailsList);
        }
        viewModel.insertAllPosts(postDetailsList);
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
    }

    @SuppressWarnings("ConstantConditions")
    private void handleError(String message) {
        try {
            if (currentPage > 1 || viewModel.getPostList().getValue().getPosts().isEmpty()) {
                recyclerView.setVisibility(View.INVISIBLE);
                postLoadErrorLayout.setVisibility(View.VISIBLE);
                postLoadErrorTextView.setText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            recyclerView.setVisibility(View.INVISIBLE);
            postLoadErrorLayout.setVisibility(View.VISIBLE);
            postLoadErrorTextView.setText(message);
        } finally {
            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        }
    }

    public boolean isListAtTop() {
        return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0;
    }

    private void toggleRecyclerViewScrolling(boolean enabled) {
        ((CustomLinearLayoutManager) recyclerView.getLayoutManager()).setScrollEnabled(enabled);
    }

    public PostDetailsViewModel getViewModel() {
        return viewModel;
    }

    private void preDownloadVideos(List<PostDetails> postDetailsList) {
        if (isConnected) {
            if (!getCanSaveMediaOnlyOnWiFi(context))
                recyclerView.preDownload(postDetailsList, context);
            else if (isConnectedToWifi(context))
                recyclerView.preDownload(postDetailsList, context);
        }
    }

    public static class InitialRetrieveTask extends AsyncTask<Void, Void, ArrayList<PostDetails>> {

        private PostsListFragment fragment;

        InitialRetrieveTask(PostsListFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        protected ArrayList<PostDetails> doInBackground(Void... voids) {
            return new ArrayList<>(fragment.getViewModel().getDatabase().dao().getAllPosts());
        }

        @Override
        protected void onPostExecute(ArrayList<PostDetails> postDetailsList) {
            if (postDetailsList != null && !postDetailsList.isEmpty()) {
                fragment.getViewModel().getPostList().setValue(new PostList(postDetailsList));
//                Posts available in Room DB, just update new Posts.
                fragment.manualRefreshTriggered = true;
                fragment.refreshPosts(true, false);
            } else {
//                Posts are not available in Room DB, fetch them from server.
                fragment.refreshPosts(false, false);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getParentActivity().updateToolbarTitle(previousTitle);
        positionToUpdate = -1;
    }
}