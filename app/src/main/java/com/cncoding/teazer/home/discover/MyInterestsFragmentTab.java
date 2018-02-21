package com.cncoding.teazer.home.discover;

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
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.discover.adapters.SubDiscoverAdapter;
import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.post.PostList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.VISIBLE;

public class MyInterestsFragmentTab extends BaseDiscoverFragment {

    private static final String ARG_CATEGORY_ID = "categoryId";

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_posts) ProximaNovaRegularTextView noPosts;
    @BindView(R.id.no_posts_2) ProximaNovaBoldTextView noPosts2;

    private int categoryId;
    private int currentPage;

    public MyInterestsFragmentTab() {}

    public static MyInterestsFragmentTab newInstance(int categoryId) {
        MyInterestsFragmentTab fragment = new MyInterestsFragmentTab();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
            currentPage = 1;
        }
    }

    @Override
    protected void handleResponse(BaseModel resultObject) {
        if (resultObject instanceof PostList) {
            PostList tempPostList = (PostList) resultObject;
            is_next_page = tempPostList.isNextPage();
            if (!tempPostList.getPosts().isEmpty()) {
                ((SubDiscoverAdapter) recyclerView.getAdapter()).updatePosts(tempPostList.getPosts());
            } else if (currentPage == 1){
                showErrorMessage(R.string.no_videos_tagged, R.string.be_the_first_one_to_upload_one);
            }
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void handleError(BaseModel baseModel) {
        baseModel.getError().printStackTrace();
        showErrorMessage(R.string.something_went_wrong, R.string.swipe_down_to_retry);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the searchContainer for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        ButterKnife.bind(this, rootView);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
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
        getPosts(1);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                getPosts(1);
            }
        });

        return rootView;
    }

    private void getPosts(final int page) {
        loadAllInterestedCategoriesPosts(page, categoryId);
    }

    private void showErrorMessage(@StringRes int message1, @StringRes int message2) {
        recyclerView.setVisibility(View.GONE);
        noPosts.setText(message1);
        noPosts2.setText(message2);
        noPosts.setVisibility(VISIBLE);
        noPosts2.setVisibility(VISIBLE);
    }
}
