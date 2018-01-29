package com.cncoding.teazer.home.discover.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.friends.UsersList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.home.discover.search.DiscoverSearchFragment.SEARCH_TERM;

public class PeopleTabFragment extends BaseFragment {

    private static final String TAG = "PeopleTagFragment";
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_posts) ProximaNovaRegularTextView noPosts;
    @BindView(R.id.no_posts_2) ProximaNovaBoldTextView noPosts2;

    private ArrayList<MiniProfile> usersList;
    private DiscoverSearchAdapter adapter;
    private Call<UsersList> usersListCall;
    private String searchTerm;
    private boolean isSearchTerm;

    public PeopleTabFragment() {
    }

    public static PeopleTabFragment newInstance(String searchTerm) {
        PeopleTabFragment fragment = new PeopleTabFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_TERM, searchTerm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersList = new ArrayList<>();
        if (getArguments() != null) {
            searchTerm = getArguments().getString(SEARCH_TERM);
            isSearchTerm = searchTerm != null && !searchTerm.equals("");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new DiscoverSearchAdapter(getParentActivity(), this,
                false, usersList, null, isSearchTerm);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getUsersList(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                getUsersList(1);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        if (usersList != null && usersList.isEmpty())
            getUsersList(1);
    }

    private void getUsersList(final int page) {
        if (page == 1)
            usersList.clear();

        if (usersListCall != null && usersListCall.isExecuted())
            usersListCall.cancel();

        usersListCall = !isSearchTerm ? ApiCallingService.Discover.getUsersListToFollow(page, getContext()) :
                ApiCallingService.Discover.getUsersListToFollowWithSearchTerm(page, searchTerm, getContext());

        if (!usersListCall.isExecuted())
            usersListCall.enqueue(new Callback<UsersList>() {
                @Override
                public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                    try {
                        Log.d(TAG, "onResponse try block");
                        if (isAdded()) {
                            if (response.code() == 200) {
                                Log.d(TAG, "onResponse 200");
                                UsersList users = response.body();
                                is_next_page = users.isNextPage();
                                if (users.getUsers() != null && users.getUsers().size() > 0) {
                                    Log.d(TAG, "onResponse user list found");
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    noPosts.setVisibility(View.GONE);
                                    noPosts2.setVisibility(View.GONE);
                                    usersList.addAll(users.getUsers());
                                    recyclerView.getRecycledViewPool().clear();
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Log.d(TAG, "onResponse No match");
                                    if (page == 1 && usersList.isEmpty()) {
                                        swipeRefreshLayout.setVisibility(View.GONE);
                                        noPosts.setText(isSearchTerm ?
                                                R.string.no_one_matches_your_search_criteria : R.string.search_for_people);
                                        noPosts.setVisibility(View.VISIBLE);
                                        noPosts2.setVisibility(View.INVISIBLE);
                                    }
                                }
                            } else {
                                noPosts.setVisibility(View.VISIBLE);
                                noPosts.setText(R.string.error_fetching_data);
                                noPosts.setCompoundDrawablesWithIntrinsicBounds(
                                        0, R.drawable.ic_no_data_placeholder, 0, 0);
                                noPosts2.setVisibility(View.INVISIBLE);
                                Log.e("getUsersListToFollow", response.code() + "_" + response.message());
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            @Override
            public void onFailure(Call<UsersList> call, Throwable t) {
                if (isAdded()) {
                    t.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            noPosts.setText(R.string.search_for_videos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (usersListCall != null && usersListCall.isExecuted())
            usersListCall.cancel();
        adapter = null;
    }
}