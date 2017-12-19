package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.FollowersAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.friends.FollowersList;
import com.cncoding.teazer.model.friends.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersListActivity extends BaseFragment{

    private static final String ARG_ID = "USERID";
    private static final String ARG_IDENTIFIER ="IDENTIFIER" ;
    String identifier;
    String followerid;
    Context context;
    List<UserInfo> list;
    List<UserInfo> userfollowerlist;
    RecyclerView recyclerView;
    FollowersAdapter profileMyFollowerAdapter;
    RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.nousertext)
    TextView nousertext;
    int userfollowerpage=1;
    int otherFollowerpage=1;
    protected String previousTitle;
    boolean next;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    public static FollowersListActivity newInstance(String id,String identifier) {
        FollowersListActivity followersListActivity = new FollowersListActivity();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_followers, container, false);
        ButterKnife.bind(this,view);
        context=container.getContext();

        recyclerView=view.findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getParentActivity().updateToolbarTitle("Follower List");

        endlessRecyclerViewScrollListener= new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(next) {

                    if(identifier.equals("Other"))
                        getOthersFollowerDetails(Integer.parseInt(followerid),page);

                    else
                        getUserfollowerList(page);

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
        userfollowerlist=new ArrayList<>();

        if(identifier.equals("Other"))
        {
            profileMyFollowerAdapter = new FollowersAdapter(context, list);
            recyclerView.setAdapter(profileMyFollowerAdapter);
            getOthersFollowerDetails(Integer.parseInt(followerid),1);
        }
        else if(identifier.equals("User"))
        {

            profileMyFollowerAdapter = new FollowersAdapter(context, userfollowerlist, 100);
            recyclerView.setAdapter(profileMyFollowerAdapter);
            getUserfollowerList(1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void getUserfollowerList(final int page)

    {

        ApiCallingService.Friends.getMyFollowers(page,context).enqueue(new Callback<FollowersList>() {
            @Override
            public void onResponse(Call<FollowersList> call, Response<FollowersList> response) {
                if(response.code()==200)
                {
                    try {
                        userfollowerlist.addAll(response.body().getUserInfos());
                        if ((userfollowerlist==null||userfollowerlist.size()==0) && page == 1) {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            nousertext.setVisibility(View.VISIBLE);
                        }
                        else
                            {
                            next=response.body().getNextPage();

                            profileMyFollowerAdapter.notifyItemRangeInserted(profileMyFollowerAdapter.getItemCount(), userfollowerlist.size() - 1);
                                layout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else
                {
                    layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<FollowersList> call, Throwable t) {
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

        });

    }
    public void getOthersFollowerDetails(int followerid, int page)
    {
        ApiCallingService.Friends.getFriendsFollowers(page,followerid,context).enqueue(new Callback<FollowersList>() {
            @Override
            public void onResponse(Call<FollowersList> call, Response<FollowersList> response) {
                if(response.code()==200)
                {
                    try
                    {

                        list.addAll(response.body().getUserInfos());
                        if (list == null || list.size() == 0) {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            nousertext.setVisibility(View.VISIBLE);
                        }
                        else {
                            next=response.body().getNextPage();
                            profileMyFollowerAdapter.notifyItemRangeInserted(profileMyFollowerAdapter.getItemCount(), userfollowerlist.size() - 1);
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else
                {
                    layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            }
            @Override
            public void onFailure(Call<FollowersList> call, Throwable t) {
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

        });

    }





}
