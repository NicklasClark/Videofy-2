package com.cncoding.teazer.ui.home.profile.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.MyUserInfo;
import com.cncoding.teazer.data.model.friends.UserFollowerList;
import com.cncoding.teazer.data.model.friends.UserInfo;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.customviews.common.DynamicProgress;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.profile.adapter.FollowersAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersListFragment extends BaseFragment {

    private static final String ARG_ID = "USERID";
    private static final String ARG_IDENTIFIER = "IDENTIFIER";
    public static final int USERS_FOLLOWER = 100;

    @BindView(R.id.layout) NestedScrollView layout;
    @BindView(R.id.nousertext) TextView nousertext;
    @BindView(R.id.loader) DynamicProgress loader;
    @BindView(R.id.toolbar_plain_title) ProximaNovaSemiBoldTextView toolbarTitle;

    String identifier;
    String followerid;
    List<UserInfo> list;
    List<MyUserInfo> userfollowerlist;
    RecyclerView recyclerView;
    FollowersAdapter profileMyFollowerAdapter;
    RecyclerView.LayoutManager layoutManager;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    boolean next;

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
            followerid = getArguments().getString(ARG_ID);
            identifier = getArguments().getString(ARG_IDENTIFIER);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        ButterKnife.bind(this, view);
        toolbarTitle.setText(R.string.following);
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (next) {
                    if (identifier.equals("Other")) {
                        if (page > 2) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        getOthersFollowerDetails(Integer.parseInt(followerid), page);
                    } else {
                        if (page > 2) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        getUserfollowerList(page);

                    }
                }

            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
        userfollowerlist = new ArrayList<>();

        if (identifier.equals("Other")) {
            profileMyFollowerAdapter = new FollowersAdapter(context, list);
            recyclerView.setAdapter(profileMyFollowerAdapter);
            loader.setVisibility(View.VISIBLE);
            getOthersFollowerDetails(Integer.parseInt(followerid), 1);
        }
        else if (identifier.equals("User")) {
            profileMyFollowerAdapter = new FollowersAdapter(context, userfollowerlist, USERS_FOLLOWER);
            recyclerView.setAdapter(profileMyFollowerAdapter);
            loader.setVisibility(View.VISIBLE);
            getUserfollowerList(1);

        }
    }

    public void getUserfollowerList(final int page) {
        ApiCallingService.Friends.getMyFollowers(page, context).enqueue(new Callback<UserFollowerList>() {
            @Override
            public void onResponse(Call<UserFollowerList> call, Response<UserFollowerList> response) {
                if (response.code() == 200) {
                    try {
                        userfollowerlist.addAll(response.body().getFollowers());
                        if ((userfollowerlist == null || userfollowerlist.size() == 0) && page == 1) {
                            showNoOneIsFollowingYou();
                        } else {
                            next = response.body().getNextPage();
                            profileMyFollowerAdapter.notifyDataSetChanged();
                            profileMyFollowerAdapter.notifyItemRangeInserted(profileMyFollowerAdapter.getItemCount(), userfollowerlist.size() - 1);
                            layout.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
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

    public void getOthersFollowerDetails(int followerid, int page) {
        ApiCallingService.Friends.getFriendsFollowers(page, followerid, context).enqueue(new Callback<FollowersList>() {
            @Override
            public void onResponse(Call<FollowersList> call, Response<FollowersList> response) {
                if (response.code() == 200) {
                    try {
                        list.addAll(response.body().getUserInfos());
                        if (list == null || list.size() == 0) {
                            showNoOneIsFollowingYou();
                        } else {
                            next = response.body().getNextPage();
                            profileMyFollowerAdapter.notifyDataSetChanged();
                            profileMyFollowerAdapter.notifyItemRangeInserted(profileMyFollowerAdapter.getItemCount(), userfollowerlist.size() - 1);
                            layout.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
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
        nousertext.setVisibility(View.VISIBLE);
        nousertext.setText(R.string.no_user_following_you);
    }

    private void showSomethingWentWrong() {
        nousertext.setVisibility(View.VISIBLE);
        nousertext.setText(R.string.something_went_wrong);
        layout.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
    }
}