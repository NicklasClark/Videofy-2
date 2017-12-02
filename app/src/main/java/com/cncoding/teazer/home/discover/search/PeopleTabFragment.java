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
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.utilities.Pojos.Friends.UsersList;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.home.discover.search.DiscoverSearchFragment.SEARCH_TERM;

public class PeopleTabFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_posts) ProximaNovaBoldTextView noPosts;

    private ArrayList<MiniProfile> usersList;
    private DiscoverSearchAdapter adapter;
    private Call<UsersList> usersListCall;
    private Callback<UsersList> usersListCallback;
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
        usersList = new ArrayList<>();

        adapter = new DiscoverSearchAdapter(getContext(), false, usersList, null);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getUsersList(page, searchTerm, isSearchTerm);
//                    new GetUsersListToFollow(PeopleTabFragment.this).execute(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                getUsersList(1, searchTerm, isSearchTerm);
//                new GetUsersListToFollow(PeopleTabFragment.this).execute(1);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (usersListCallback == null) {
            usersListCallback = new Callback<UsersList>() {
                @Override
                public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                    if (response.code() == 200) {
                        UsersList users = response.body();
                        is_next_page = users.isNextPage();
                        if (users.getUsers().size() > 0) {
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            noPosts.setVisibility(View.GONE);
                            usersList.addAll(users.getUsers());
                            recyclerView.getRecycledViewPool().clear();
                            adapter.notifyDataSetChanged();
                        } else {
                            swipeRefreshLayout.setVisibility(View.GONE);
                            noPosts.setText(isSearchTerm ? R.string.no_one_matches_your_search_criteria : R.string.search_for_people);
                            noPosts.setVisibility(View.VISIBLE);
                        }
                    } else {
                        noPosts.setVisibility(View.VISIBLE);
                        noPosts.setText(R.string.error_fetching_data);
                        noPosts.setCompoundDrawablesWithIntrinsicBounds(
                                0, R.drawable.ic_no_data_placeholder, 0, 0);
                        Log.e("getUsersListToFollow", response.code() + "_" + response.message());
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<UsersList> call, Throwable t) {
                    if (isAdded()) {
                        Log.e("getUsersListToFollow", t.getMessage() != null ? t.getMessage() : "FAILED!!!");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            };
        }
        if (usersList != null && usersList.isEmpty())
            getUsersList(1, searchTerm, isSearchTerm);
    }

    private void getUsersList(int page, String searchTerm, boolean isSearchTerm) {
        if (page == 1)
            usersList.clear();

        usersListCall = !isSearchTerm ? ApiCallingService.Discover.getUsersListToFollow(page, getContext()) :
                ApiCallingService.Discover.getUsersListToFollowWithSearchTerm(page, searchTerm, getContext());

        if (!usersListCall.isExecuted())
            usersListCall.enqueue(usersListCallback);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        noPosts.setText(R.string.search_for_videos);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (usersListCall != null && usersListCall.isExecuted())
            usersListCall.cancel();
        adapter = null;
    }
}