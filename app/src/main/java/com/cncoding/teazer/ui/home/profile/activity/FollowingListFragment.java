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
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.UserInfo;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.customviews.common.DynamicProgress;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.profile.adapter.FollowingAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingListFragment extends BaseFragment {

    private static final String ARG_ID ="FollowingId";
    private static final String ARG_IDENTIFIER ="identifier";
    public static final int USER_FOLLOWEING=100;

    @BindView(R.id.layout) NestedScrollView layout;
    @BindView(R.id.nousertext) TextView nousertext;
    @BindView(R.id.loader) DynamicProgress loader;
    @BindView(R.id.toolbar_plain_title) ProximaNovaSemiBoldTextView toolbarTitle;

    String identifier;
    String followerid;
    List<UserInfo> list;
    List<UserInfo> otherlist;
    RecyclerView recyclerView;
    FollowingAdapter profileMyFollowingAdapter;
    RecyclerView.LayoutManager layoutManager;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    boolean next;

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
            followerid  = getArguments().getString(ARG_ID);
            identifier = getArguments().getString(ARG_IDENTIFIER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_followers, container, false);
        ButterKnife.bind(this,view);
        toolbarTitle.setText(R.string.following);
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        endlessRecyclerViewScrollListener= new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(next) {

                    if (identifier.equals("User")) {
                        if(page>2) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        getUserfollowinglist(page);
                    }
                    else
                    {
                        if(page>2) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        getOthersFollowingList(Integer.parseInt(followerid),page);
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
        list=new ArrayList<>();
        otherlist=new ArrayList<>();
        if (identifier.equals("User")) {
            layout.setVisibility(View.GONE);
            profileMyFollowingAdapter = new FollowingAdapter(this, list, USER_FOLLOWEING);
            recyclerView.setAdapter(profileMyFollowingAdapter);
            loader.setVisibility(View.VISIBLE);
            getUserfollowinglist(1);
        }
        else if (identifier.equals("Other")) {
            layout.setVisibility(View.GONE);
            profileMyFollowingAdapter = new FollowingAdapter(this, otherlist);
            recyclerView.setAdapter(profileMyFollowingAdapter);
            loader.setVisibility(View.VISIBLE);
            getOthersFollowingList(Integer.parseInt(followerid),1);
        }
    }
    public void getUserfollowinglist(final int userfollowingpage) {
        ApiCallingService.Friends.getMyFollowing(userfollowingpage, context).enqueue(new Callback<FollowingsList>() {
            @Override
            public void onResponse(Call<FollowingsList> call, Response<FollowingsList> response) {
                if (response.code() == 200) {
                    try {
                        list.addAll(response.body().getFollowings());
                        if ((list==null||list.size()==0) && userfollowingpage == 1) {
                            showNoOneIsFollowingYou();
                        }
                        else {
                            next=response.body().getNextPage();
                            profileMyFollowingAdapter.notifyDataSetChanged();
                            profileMyFollowingAdapter.notifyItemRangeInserted(profileMyFollowingAdapter.getItemCount(), list.size() - 1);
                            layout.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);
                        }
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
            public void onFailure(Call<FollowingsList> call, Throwable t) {
                showSomethingWentWrong();
            }
        });

    }

    public void getOthersFollowingList(final int userId, final int pageId) {
        ApiCallingService.Friends.getFriendsFollowings(pageId, userId, context).enqueue(new Callback<FollowingsList>() {
            @Override
            public void onResponse(Call<FollowingsList> call, Response<FollowingsList> response) {
                if (response.code() == 200) {
                    try {
                        otherlist.addAll(response.body().getFollowings());
                        if ((otherlist==null||otherlist.size()==0) && pageId == 1) {
                            showNoOneIsFollowingYou();
                        }
                        else {
                            next=response.body().getNextPage();
                            profileMyFollowingAdapter.notifyItemRangeInserted(profileMyFollowingAdapter.getItemCount(), otherlist.size() - 1);
                            layout.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showSomethingWentWrong();
                    }
                }
                else {
                    showSomethingWentWrong();
                }
            }
            @Override
            public void onFailure(Call<FollowingsList> call, Throwable t) {
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