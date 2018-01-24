package com.cncoding.teazer.home.discover;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.home.discover.adapters.SubDiscoverAdapter;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyInterestsFragmentTab extends BaseFragment {

    private static final String ARG_CATEGORY_ID = "categoryId";
    private static final String ARG_CATEGORY_NAME = "categoryName";

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_posts) ProximaNovaRegularTextView noPosts;
    @BindView(R.id.no_posts_2) ProximaNovaBoldTextView noPosts2;

    private Call<PostList> postListCall;
    private int categoryId;
//    private String categoryName;
    private ArrayList<PostDetails> postDetailsArrayList;

//    private OnNotificationsFragmentInteractionListener mListener;

    public MyInterestsFragmentTab() {
        // Required empty public constructor
    }

    public static MyInterestsFragmentTab newInstance(Category category) {
        MyInterestsFragmentTab fragment = new MyInterestsFragmentTab();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, category.getCategoryId());
        args.putString(ARG_CATEGORY_NAME, category.getCategoryName());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_CATEGORY_ID);
//            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the searchContainer for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        ButterKnife.bind(this, rootView);

        if (postDetailsArrayList == null)
            postDetailsArrayList = new ArrayList<>();
        else postDetailsArrayList.clear();

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new SubDiscoverAdapter(postDetailsArrayList, getContext()));
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getPosts(page);
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
        try {
            postListCall = ApiCallingService.Discover.getAllInterestedCategoriesVideos(page, categoryId, getContext());

            if (postListCall.isExecuted()) postListCall.cancel();

            postListCall.enqueue(new Callback<PostList>() {
                @Override
                public void onResponse(Call<PostList> call, Response<PostList> response) {
                    try {
                        if (isAdded()) {
                            if (response.code() == 200) {
                                PostList tempPostList = response.body();
                                is_next_page = tempPostList.isNextPage();
                                if (!tempPostList.getPosts().isEmpty()) {
                                    if (page == 1) postDetailsArrayList.clear();

                                    postDetailsArrayList.addAll(tempPostList.getPosts());
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                } else if (page == 1){
                                    recyclerView.setVisibility(View.GONE);
                                    String noVideosText = getString(R.string.no_videos_tagged);
                                    noPosts.setText(noVideosText);
                                    noPosts.setVisibility(View.VISIBLE);
                                    noPosts2.setVisibility(View.INVISIBLE);
                                }
                            } else
                                Log.e("GetPosts", response.code() + "_" + response.message());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PostList> call, Throwable t) {
                    if (isAdded()) {
                        Log.e("FAILED_GetPosts", t.getMessage() != null ? t.getMessage() : "FAILED!");
                    }
                }
            });

            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        if (context instanceof OnNotificationsFragmentInteractionListener) {
////            mListener = (OnNotificationsFragmentInteractionListener) context;
////        }
////        else {
////            throw new RuntimeException(context.toString()
////                    + " must implement OnNotificationsFragmentInteractionListener");
////        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (postListCall != null && postListCall.isExecuted()) {
            postListCall.cancel();
            postListCall = null;
        }
    }

//    public interface OnNotificationsFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
}
