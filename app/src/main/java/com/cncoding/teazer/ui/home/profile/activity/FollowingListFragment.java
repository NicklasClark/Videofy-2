package com.cncoding.teazer.ui.home.profile.activity;

import android.os.Bundle;
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
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.UserInfo;
import com.cncoding.teazer.ui.customviews.common.CustomLinearLayoutManager;
import com.cncoding.teazer.ui.customviews.common.DynamicProgress;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.base.BaseHomeFragment;
import com.cncoding.teazer.ui.home.profile.adapter.FollowingAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.ui.home.profile.activity.FollowersListFragment.ARG_ID;
import static com.cncoding.teazer.ui.home.profile.activity.FollowersListFragment.ARG_IDENTIFIER;

public class FollowingListFragment extends BaseHomeFragment {

    @BindView(R.id.layout) NestedScrollView layout;
    @BindView(R.id.nousertext) TextView noUserText;
    @BindView(R.id.loader) DynamicProgress loader;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.toolbar_plain_title) ProximaNovaSemiBoldTextView toolbarTitle;

    String identifier;
    String followerId;
    List<UserInfo> otherlist;
    FollowingAdapter followingAdapter;

    public static FollowingListFragment newInstance(String id, String identifier) {
        FollowingListFragment followersListActivity = new FollowingListFragment();
        Bundle bundle=new Bundle();
        bundle.putString(ARG_ID,id);
        bundle.putString(ARG_IDENTIFIER,identifier);
        followersListActivity.setArguments(bundle);
        return followersListActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            followerId = getArguments().getString(ARG_ID);
            identifier = getArguments().getString(ARG_IDENTIFIER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        ButterKnife.bind(this,view);
        toolbarTitle.setText(R.string.following);
        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(is_next_page) {
                    if (identifier.equals("User")) {
                        if (page > 2) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        getUserFollowingList(page);
                    } else {
                        if (page > 2) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        getOthersFollowingList(Integer.parseInt(followerId), page);
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
        if (identifier.equals("User")) {
            layout.setVisibility(View.GONE);
            followingAdapter = new FollowingAdapter(this, true);
            recyclerView.setAdapter(followingAdapter);
            loader.setVisibility(View.VISIBLE);
            getUserFollowingList(1);
        }
        else if (identifier.equals("Other")) {
            layout.setVisibility(View.GONE);
            followingAdapter = new FollowingAdapter(this, false);
            recyclerView.setAdapter(followingAdapter);
            loader.setVisibility(View.VISIBLE);
            getOthersFollowingList(Integer.parseInt(followerId),1);
        }
    }

    public void getUserFollowingList(final int page) {
        ApiCallingService.Friends.getMyFollowing(page, context).enqueue(new Callback<FollowingsList>() {
            @Override
            public void onResponse(Call<FollowingsList> call, Response<FollowingsList> response) {
                handleResponse(page, response);
            }
            @Override
            public void onFailure(Call<FollowingsList> call, Throwable t) {
                showSomethingWentWrong();
            }
        });
    }

    public void getOthersFollowingList(final int userId, final int page) {
        ApiCallingService.Friends.getFriendsFollowings(page, userId, context).enqueue(new Callback<FollowingsList>() {
            @Override
            public void onResponse(Call<FollowingsList> call, Response<FollowingsList> response) {
                handleResponse(page, response);
            }
            @Override
            public void onFailure(Call<FollowingsList> call, Throwable t) {
                showSomethingWentWrong();
            }
        });
    }

    private void handleResponse(int page, Response<FollowingsList> response) {
        try {
            if (response.code() == 200) {
                FollowingsList followingsList = response.body();
                if (followingsList != null) {
                    if (followingsList.getFollowings().isEmpty() && page == 1) {
                        showNoOneIsFollowingYou();
                    }
                    else {
                        is_next_page=response.body().getNextPage();
                        if (page == 1)
                            followingAdapter.updateUserInfoPosts(followingsList.getFollowings());
                        else followingAdapter.addUserInfoPosts(page, followingsList.getFollowings());
                        layout.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.GONE);
                    }
                } else showNoOneIsFollowingYou();
            } else {
                showSomethingWentWrong();
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            showSomethingWentWrong();
        }
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