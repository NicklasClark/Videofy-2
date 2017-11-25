package com.cncoding.teazer.home.search;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.home.search.adapters.SubSearchAdapter;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.home.search.SearchFragment.ACTION_VIEW_MOST_POPULAR;
import static com.cncoding.teazer.home.search.SearchFragment.ACTION_VIEW_MY_INTERESTS;
import static com.cncoding.teazer.home.search.SearchFragment.ACTION_VIEW_TRENDING;

public class SubSearchFragment extends BaseFragment {

    private static final String ARG_CATEGORIES = "categories";
    private static final String ARG_POST_DETAILS = "postDetails";
    private static final String ACTION = "action";

    @BindView(R.id.my_interests_tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.no_posts) ProximaNovaBoldTextView noPosts;

    private ArrayList<Category> categories;
    private String previousTitle;
    private ArrayList<PostDetails> postDetailsArrayList;
    private int action;

    public SubSearchFragment() {
        // Required empty public constructor
    }

    public static SubSearchFragment newInstance(int action, ArrayList<Category> categories,
                                                ArrayList<PostDetails> postDetailsArrayList) {
        SubSearchFragment fragment = new SubSearchFragment();
        Bundle args = new Bundle();
        args.putInt(ACTION, action);
        args.putParcelableArrayList(ARG_CATEGORIES, categories);
        args.putParcelableArrayList(ARG_POST_DETAILS, postDetailsArrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            action = getArguments().getInt(ACTION);
            categories = getArguments().getParcelableArrayList(ARG_CATEGORIES);
            postDetailsArrayList = getArguments().getParcelableArrayList(ARG_POST_DETAILS);
        }
        if (postDetailsArrayList == null)
            postDetailsArrayList = new ArrayList<>();

        previousTitle = getParentActivity().getToolbarTitle();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sub_search, container, false);
        ButterKnife.bind(this, rootView);

        switch (action) {
            case ACTION_VIEW_MOST_POPULAR:
                getParentActivity().updateToolbarTitle(getString(R.string.most_popular));
                prepareMostPopularLayout();
                break;
            case ACTION_VIEW_MY_INTERESTS:
                getParentActivity().updateToolbarTitle(getString(R.string.my_interests));
                prepareMyInterestsLayout();
                break;
            case ACTION_VIEW_TRENDING:
                getParentActivity().updateToolbarTitle(getString(R.string.trending));
                prepareTrendingLayout();
                break;
            default:
                break;
        }
        return rootView;
    }

    private void prepareMostPopularLayout() {
        if (!postDetailsArrayList.isEmpty()) {
            recyclerView.setAdapter(new SubSearchAdapter(postDetailsArrayList, getContext()));
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerView.setLayoutManager(manager);
        } else
            noPosts.setVisibility(View.VISIBLE);
    }

    private void prepareMyInterestsLayout() {
        tabLayout.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);

        populateTabs();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    private void prepareTrendingLayout() {
        recyclerView.setAdapter(new SubSearchAdapter(postDetailsArrayList, getContext()));
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (page > 1)
                    new GetTrendingVideos(SubSearchFragment.this).execute(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                new GetTrendingVideos(SubSearchFragment.this).execute(1);
            }
        });
        new GetTrendingVideos(SubSearchFragment.this).execute(1);
    }

    private void populateTabs() {
        for (int i = 0; i < categories.size(); i++)
            tabLayout.addTab(tabLayout.newTab().setText(categories.get(i).getCategoryName()));
    }

    private static class GetTrendingVideos extends AsyncTask<Integer, Void, Void> {

        private WeakReference<SubSearchFragment> reference;

        GetTrendingVideos(SubSearchFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            if (integers[0] == 1)
                reference.get().postDetailsArrayList.clear();

            ApiCallingService.Discover.getTrendingVideos(integers[0],
                    reference.get().categories.get(0).getCategoryId(), reference.get().getContext())
                    .enqueue(new Callback<PostList>() {
                        @Override
                        public void onResponse(Call<PostList> call, Response<PostList> response) {
                            if (response.code() == 200) {
                                if (!response.body().getPosts().isEmpty()) {
                                    reference.get().postDetailsArrayList.addAll(response.body().getPosts());
                                    reference.get().recyclerView.getAdapter().notifyDataSetChanged();
                                } else
                                    reference.get().noPosts.setVisibility(View.VISIBLE);
                            } else
                                Log.e("GetTrendingVideos", response.code() + "_" + response.message());
                        }

                        @Override
                        public void onFailure(Call<PostList> call, Throwable t) {
                            Log.e("FAIL! GetTrendingVideos", t.getMessage());
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            reference.get().swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getParentActivity().updateToolbarTitle(previousTitle);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MyInterestsFragmentTab.newInstance(categories.get(position).getCategoryId());
        }

        @Override
        public int getCount() {
            return categories.size();
        }
    }

}
