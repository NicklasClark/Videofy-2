package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.FollowingAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.profile.following.Following;
import com.cncoding.teazer.model.profile.following.ProfileMyFollowing;
import com.cncoding.teazer.model.profile.othersfollowing.OtherUserFollowings;
import com.cncoding.teazer.model.profile.othersfollowing.OthersFollowing;
import com.cncoding.teazer.utilities.Pojos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingListActivities extends BaseFragment {
    private static final String ARG_ID ="FollowingId" ;
    private static final String ARG_IDENTIFIER ="identifier" ;
    Context context;
    List<Following> list;
    List<OtherUserFollowings> otherlist;
    RecyclerView recyclerView;
    FollowingAdapter profileMyFollowingAdapter;
    RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.nousertext)
    TextView nousertext;
    int otherfollowingpage=1;
    int userfollowingpage=1;
    String followerid;
    String identifier;


    public static FollowingListActivities newInstance(String id,String identifier) {
        FollowingListActivities followersListActivity = new FollowingListActivities();

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
        View view = inflater.inflate(R.layout.activity_followers, container, false);
        ButterKnife.bind(this,view);
        context=container.getContext();
        getParentActivity().updateToolbarTitle("Following List");
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list=new ArrayList<>();
        otherlist=new ArrayList<>();
        if (identifier.equals("User")) {
            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            getUserfollowinglist();
        }
        else if (identifier.equals("Other"))
        {
            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            getOthersFollowingList(Integer.parseInt(followerid));
        }
    }

    public void getUserfollowinglist() {

        ApiCallingService.Friends.getMyFollowing(userfollowingpage, context).enqueue(new Callback<ProfileMyFollowing>() {
            @Override
            public void onResponse(Call<ProfileMyFollowing> call, Response<ProfileMyFollowing> response) {
                if (response.code() == 200) {

                    try {
                        boolean next=response.body().getNextPage();
                        list.addAll(response.body().getFollowings());
                        if (list == null || list.size() == 0) {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            nousertext.setVisibility(View.VISIBLE);
                        }
                        else {
                            profileMyFollowingAdapter = new FollowingAdapter(context, list, 100);
                            recyclerView.setAdapter(profileMyFollowingAdapter);
                            if(next)
                            {
                                userfollowingpage++;
                                getUserfollowinglist();
                            }
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ProfileMyFollowing> call, Throwable t) {
                Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    public void getOthersFollowingList(final int userId) {

        ApiCallingService.Friends.getFriendsFollowings(otherfollowingpage, userId, context).enqueue(new Callback<OthersFollowing>() {
            @Override
            public void onResponse(Call<OthersFollowing> call, Response<OthersFollowing> response) {
                if (response.code() == 200) {
                    try {
                        boolean next=response.body().getNextPage();
                        otherlist.addAll(response.body().getFollowings());
                        if (otherlist == null || otherlist.size() == 0) {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            nousertext.setVisibility(View.VISIBLE);
                        }
                        else {

                            profileMyFollowingAdapter = new FollowingAdapter(context, otherlist);
                            recyclerView.setAdapter(profileMyFollowingAdapter);
                            if(next)
                            {
                                otherfollowingpage++;
                                getOthersFollowingList(userId);
                            }
                            layout.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(context, "Oops! Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                }
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<OthersFollowing> call, Throwable t) {
                Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

        });


    }
}

