package com.cncoding.teazer.ui.home.discover;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.ui.customviews.common.CustomStaggeredGridLayoutManager;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaBoldTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.home.discover.adapters.SubDiscoverAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.VISIBLE;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_ALL_INTERESTED_CATEGORIES_POSTS;

public class MyInterestsFragmentTab extends BaseDiscoverFragment {

    private static final String ARG_CATEGORY = "category";

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_posts) ProximaNovaRegularTextView noPosts;
    @BindView(R.id.no_posts_2) ProximaNovaBoldTextView noPosts2;

    private Category category;

    public MyInterestsFragmentTab() {}

    public static MyInterestsFragmentTab newInstance(Category category) {
        MyInterestsFragmentTab fragment = new MyInterestsFragmentTab();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getParcelable(ARG_CATEGORY);
        }
    }

    @Override protected void handleResponse(BaseModel resultObject) {
        try {
            if (resultObject instanceof PostList &&
                    resultObject.getCallType() == (CALL_ALL_INTERESTED_CATEGORIES_POSTS + category.getCategoryId())) {
                PostList postList = (PostList) resultObject;
                is_next_page = postList.isNextPage();
                if (!postList.getPosts().isEmpty()) {
                    //noinspection unchecked
                    new UpdatePosts(this, currentPage, postList.getPosts()).execute();
                } else if (currentPage == 1){
                    showErrorMessage(R.string.no_videos_tagged, R.string.be_the_first_one_to_upload_one);
                }
            }
            if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override protected void handleError(BaseModel baseModel) {
        baseModel.getError().printStackTrace();
        showErrorMessage(R.string.something_went_wrong, R.string.swipe_down_to_retry);
        if (swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        ButterKnife.bind(this, rootView);

        StaggeredGridLayoutManager manager = new CustomStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new SubDiscoverAdapter(this));
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page) {
                    currentPage = page;
                    getPosts(page);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPosts(1);
                scrollListener.resetState();
            }
        });
        return rootView;
    }

    @Override public void onResume() {
        super.onResume();
        getPosts(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        clearPostListLiveData();
    }

    public Category getCategory() {
        return category;
    }

    private void getPosts(int page) {
        viewModel.loadAllInterestedCategoriesPostsApiCall(page, category.getCategoryId());
    }

    private void showErrorMessage(@StringRes int message1, @StringRes int message2) {
        recyclerView.setVisibility(View.GONE);
        noPosts.setText(message1);
        noPosts2.setText(message2);
        noPosts.setVisibility(VISIBLE);
        noPosts2.setVisibility(VISIBLE);
    }

    private static class UpdatePosts extends AsyncTask<Void, Void, Void> {

        private MyInterestsFragmentTab fragment;
        private int currentPage;
        private List<PostDetails> postDetailsList;

        UpdatePosts(MyInterestsFragmentTab fragment, int currentPage, List<PostDetails> postDetailsList) {
            this.fragment = fragment;
            this.currentPage = currentPage;
            this.postDetailsList = postDetailsList;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ((SubDiscoverAdapter) fragment.recyclerView.getAdapter()).updatePosts(currentPage, postDetailsList);
            return null;
        }
    }
}