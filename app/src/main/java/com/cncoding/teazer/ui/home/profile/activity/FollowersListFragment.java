package com.cncoding.teazer.ui.home.profile.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.UserFollowerList;
import com.cncoding.teazer.ui.customviews.common.CustomLinearLayoutManager;
import com.cncoding.teazer.ui.customviews.common.DynamicProgress;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.home.base.BaseHomeFragment;
import com.cncoding.teazer.ui.home.profile.adapter.FollowersAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersListFragment extends BaseHomeFragment {

    public static final String ARG_ID = "USERID";
    public static final String ARG_IDENTIFIER = "IDENTIFIER";

    @BindView(R.id.layout) NestedScrollView layout;
    @BindView(R.id.nousertext) TextView noUserText;
    @BindView(R.id.loader) DynamicProgress loader;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    CustomLinearLayoutManager layoutManager;
    String identifier;
    String followerId;
    FollowersAdapter followersAdapter;
    UserFollowerList userFollowerList;

    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";



    public static FollowersListFragment newInstance(String id, String identifier) {
        FollowersListFragment followersListFragment = new FollowersListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ID, id);
        bundle.putString(ARG_IDENTIFIER, identifier);
        followersListFragment.setArguments(bundle);
        return followersListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            followerId = getArguments().getString(ARG_ID);
            identifier = getArguments().getString(ARG_IDENTIFIER);
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        ButterKnife.bind(this, view);
        layoutManager = new CustomLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page) {
                    if (identifier.equals("Other")) {
                        if (page > 2) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        getOthersFollowerDetails(Integer.parseInt(followerId), page);
                    } else {
                        if (page > 2) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        getUserFollowerList(page);
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


            if (identifier.equals("Other")) {
                followersAdapter = new FollowersAdapter(this, false);
                recyclerView.setAdapter(followersAdapter);
                loader.setVisibility(View.VISIBLE);
                getOthersFollowerDetails(Integer.parseInt(followerId), 1);
            } else if (identifier.equals("User")) {
                followersAdapter = new FollowersAdapter(this, true);
                recyclerView.setAdapter(followersAdapter);
                loader.setVisibility(View.VISIBLE);
                getUserFollowerList(1);

            }

        }



    @Override
    public void onPause() {
        super.onPause();
        // save RecyclerView state


        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);



    }

    public void getUserFollowerList(final int page) {
        ApiCallingService.Friends.getMyFollowers(page, context).enqueue(new Callback<UserFollowerList>() {
            @Override
            public void onResponse(Call<UserFollowerList> call, Response<UserFollowerList> response) {
                if (response.code() == 200) {
                    try {
                        userFollowerList = response.body();
                        if (userFollowerList != null) {
                            if (userFollowerList.getFollowers().size() == 0 && page == 1) {
                                showNoOneIsFollowingYou();
                            } else {
                                is_next_page = userFollowerList.getNextPage();
                                if (page == 1)
                                    followersAdapter.updateMyUserInfoPosts(userFollowerList.getFollowers());
                                else followersAdapter.addMyUserInfoPosts(page, userFollowerList.getFollowers());
                                layout.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                            }
                        } else showNoOneIsFollowingYou();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        showSomethingWentWrong();
                    }
                } else {
                    showSomethingWentWrong();
                }
            }

            @Override
            public void onFailure(Call<UserFollowerList> call, Throwable t) {
                showSomethingWentWrong();
            }
        });

    }

    public void getOthersFollowerDetails(int followerId, final int page) {
        ApiCallingService.Friends.getFriendsFollowers(page, followerId, context).enqueue(new Callback<FollowersList>() {
            @Override
            public void onResponse(Call<FollowersList> call, Response<FollowersList> response) {
                if (response.code() == 200) {
                    try {
                        FollowersList followersList = response.body();
                        if (followersList != null) {
                            if (followersList.getUserInfos().size() == 0 && page == 1) {
                                showNoOneIsFollowingYou();
                            } else {
                                is_next_page = response.body().getNextPage();
                                if (page == 1)
                                    followersAdapter.updateUserInfoPosts(followersList.getUserInfos());
                                else followersAdapter.addUserInfoPosts(page, followersList.getUserInfos());
                                layout.setVisibility(View.VISIBLE);
                                loader.setVisibility(View.GONE);
                            }
                        } else showNoOneIsFollowingYou();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        showSomethingWentWrong();
                    }
                } else {
                    showSomethingWentWrong();
                }
            }

            @Override
            public void onFailure(Call<FollowersList> call, Throwable t) {
                showSomethingWentWrong();
            }
        });

    }

    private void showNoOneIsFollowingYou() {
        layout.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        noUserText.setVisibility(View.VISIBLE);
        noUserText.setText(R.string.no_user_following_you);
    }

    private void showSomethingWentWrong() {
        noUserText.setVisibility(View.VISIBLE);
        noUserText.setText(R.string.something_went_wrong);
        layout.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
    }





}