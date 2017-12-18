package com.cncoding.teazer.home.discover;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.home.discover.adapters.SubDiscoverAdapter;
import com.cncoding.teazer.home.tagsAndCategories.Interests;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_MOST_POPULAR;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_MY_INTERESTS;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_TRENDING;

public class SubDiscoverFragment extends BaseFragment {

    private static final String ARG_CATEGORIES = "categories";
    private static final String ARG_POST_DETAILS = "postDetails";
    private static final String ACTION = "action";

    @BindView(R.id.my_interests_tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.list_layout) LinearLayout listLayout;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.no_posts) ProximaNovaRegularTextView noPosts;
    @BindView(R.id.no_posts_2) ProximaNovaBoldTextView noPosts2;

    private ArrayList<Category> categories;
    private String previousTitle;
    private ArrayList<PostDetails> postDetailsArrayList;
    private int action;

    public SubDiscoverFragment() {
        // Required empty public constructor
    }

    public static SubDiscoverFragment newInstance(int action, ArrayList<Category> categories,
                                                  ArrayList<PostDetails> postDetailsArrayList) {
        SubDiscoverFragment fragment = new SubDiscoverFragment();
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
        previousTitle = getParentActivity().getToolbarTitle();

        if (action == ACTION_VIEW_MY_INTERESTS)
            setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the searchContainer for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sub_discover, container, false);
        ButterKnife.bind(this, rootView);

        if (postDetailsArrayList == null)
            postDetailsArrayList = new ArrayList<>();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (action) {
            case ACTION_VIEW_MOST_POPULAR:
                getParentActivity().updateToolbarTitle(getString(R.string.most_popular));
                prepareMostPopularLayout();
                break;
            case ACTION_VIEW_MY_INTERESTS:
                postDetailsArrayList.clear();
                getParentActivity().updateToolbarTitle(getString(R.string.my_interests));
                prepareMyInterestsLayout();
                break;
            case ACTION_VIEW_TRENDING:
                getParentActivity().updateToolbarTitle(getString(R.string.trending) +
                        (categories != null && !categories.isEmpty() ?
                                (" " + categories.get(0).getCategoryName()) + " " : " ") + getString(R.string.videos));
                prepareTrendingLayout();
                break;
            default:
                break;
        }
    }

    private void prepareMostPopularLayout() {
        listLayout.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new SubDiscoverAdapter(postDetailsArrayList, getContext()));
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    new GetMostPopularVideos(SubDiscoverFragment.this).execute(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                new GetMostPopularVideos(SubDiscoverFragment.this).execute(1);
            }
        });
        new GetMostPopularVideos(SubDiscoverFragment.this).execute(1);
    }

    private void prepareMyInterestsLayout() {
        if (categories != null && !categories.isEmpty()) {
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);

            populateTabs();
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(categories.get(0).getMyColor()));

            viewPager.setAdapter(sectionsPagerAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
                @Override
                public void onPageSelected(int position) {
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor(categories.get(position).getMyColor()));
                    fadeOtherTabColorsOut(position);
                    setCurrentTabTextColor(position);
                }
            });
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        } else {
            Toast.makeText(getContext(), R.string.no_interests_selected_message, Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getParentActivity().pushFragmentOnto(Interests.newInstance(
                            false, true, categories, null));
                }
            }, 1000);
        }
    }

    private void prepareTrendingLayout() {
        listLayout.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new SubDiscoverAdapter(postDetailsArrayList, getContext()));
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    new GetTrendingVideos(SubDiscoverFragment.this).execute(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                new GetTrendingVideos(SubDiscoverFragment.this).execute(1);
            }
        });
        new GetTrendingVideos(SubDiscoverFragment.this).execute(1);
    }

    private void populateTabs() {
        for (int i = 0; i < categories.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(categories.get(i).getCategoryName()), i);
//            Set tab text color
            LinearLayout linearLayout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(i));
            linearLayout.setPadding(getPixel(), 0, getPixel(), 0);
            AppCompatTextView view = ((AppCompatTextView) linearLayout.getChildAt(1));
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            view.setTextColor(i == 0 ? Color.parseColor(categories.get(i).getMyColor()) : Color.parseColor("#666666"));
        }
    }

    private void setCurrentTabTextColor(int position) {
        LinearLayout linearLayout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(position));
        linearLayout.setPadding(getPixel(), 0, getPixel(), 0);
        AppCompatTextView view = ((AppCompatTextView) linearLayout.getChildAt(1));
        view.setTextColor(Color.parseColor(categories.get(position).getMyColor()));
    }

    private void fadeOtherTabColorsOut(int excludePosition) {
        for (int i = 0; i < categories.size(); i++) {
            if (i != excludePosition) {
                LinearLayout linearLayout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(i));
                linearLayout.setPadding(getPixel(), 0, getPixel(), 0);
                AppCompatTextView view = ((AppCompatTextView) linearLayout.getChildAt(1));
                view.setTextColor(Color.parseColor("#666666"));
            }
        }

    }

    private int getPixel() {
        return (int)((34 * getResources().getDisplayMetrics().density) + 0.5);
    }

    private static class GetTrendingVideos extends AsyncTask<Integer, Void, Void> {

        private WeakReference<SubDiscoverFragment> reference;

        GetTrendingVideos(SubDiscoverFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(final Integer... integers) {
            if (integers[0] == 1)
                reference.get().postDetailsArrayList.clear();

            ApiCallingService.Discover.getTrendingVideos(integers[0],
                    reference.get().categories.get(0).getCategoryId(), reference.get().getContext())
                    .enqueue(new Callback<PostList>() {
                        @Override
                        public void onResponse(Call<PostList> call, Response<PostList> response) {
                            if (reference.get().isAdded()) {
                                if (response.code() == 200) {
                                    PostList postList = response.body();
                                    reference.get().is_next_page = postList.isNextPage();
                                    if (!postList.getPosts().isEmpty()) {
                                        reference.get().postDetailsArrayList.addAll(postList.getPosts());
                                        reference.get().recyclerView.getAdapter().notifyDataSetChanged();
                                    } else if (integers[0] == 1) {
                                        String noVideosText = reference.get().getString(R.string.no_videos_tagged) +
                                                reference.get().categories.get(0).getCategoryName() +
                                                reference.get().getString(R.string.yet_uploaded);
                                        reference.get().noPosts.setText(noVideosText);
                                        reference.get().noPosts.setVisibility(VISIBLE);
                                        reference.get().noPosts2.setVisibility(VISIBLE);
                                    }
                                } else
                                    Log.e("GetTrendingVideos", response.code() + "_" + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<PostList> call, Throwable t) {
                            if (reference.get().isAdded()) {
                                Log.e("FAIL! GetTrendingVideos", t.getMessage() != null ? t.getMessage() : "FAILED!");
                            }
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

    private static class GetMostPopularVideos extends AsyncTask<Integer, Void, Void> {

        private WeakReference<SubDiscoverFragment> reference;

        GetMostPopularVideos(SubDiscoverFragment context) {
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

            ApiCallingService.Discover.getAllMostPopularVideos(integers[0], reference.get().getContext())
                    .enqueue(new Callback<PostList>() {
                        @Override
                        public void onResponse(Call<PostList> call, Response<PostList> response) {
                            if (reference.get().isAdded()) {
                                if (response.code() == 200) {
                                    PostList postList = response.body();
                                    if (postList != null) {
                                        reference.get().is_next_page = postList.isNextPage();
                                        if (!postList.getPosts().isEmpty()) {
                                            reference.get().postDetailsArrayList.addAll(postList.getPosts());
                                            reference.get().recyclerView.getAdapter().notifyDataSetChanged();
                                        } else
                                            reference.get().noPosts.setVisibility(View.VISIBLE);
                                    }
                                } else
                                    Log.e("GetTrendingVideos", response.code() + "_" + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<PostList> call, Throwable t) {
                            if (reference.get().isAdded()) {
                                Log.e("FAIL! GetTrendingVideos", t.getMessage() != null ? t.getMessage() : "FAILED!");
                            }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_my_interests, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_edit) {
            getParentActivity().pushFragmentOnto(Interests.newInstance(false, true, categories, null));
            return true;
        }
        return false;
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
            return MyInterestsFragmentTab.newInstance(categories.get(position));
        }

        @Override
        public int getCount() {
            return categories.size();
        }
    }

}
