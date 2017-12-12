package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.cncoding.teazer.adapter.FollowersAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.profile.blockuser.Followers;
import com.cncoding.teazer.model.profile.followers.Follower;
import com.cncoding.teazer.model.profile.followers.ProfileMyFollowers;
import com.cncoding.teazer.model.profile.otherfollower.FreindFollower;
import com.cncoding.teazer.model.profile.otherfollower.OtherFollowers;

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
    List<OtherFollowers> list=new ArrayList<>();
    List<Follower> userfollowerlist;
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.statusbar));
//        }
        recyclerView=view.findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getParentActivity().updateToolbarTitle("Follower List");
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list=new ArrayList<>();
        userfollowerlist=new ArrayList<>();

        if(identifier.equals("Other"))
        {
            getOthersFollowerDetails(Integer.parseInt(followerid));
        }
        else if(identifier.equals("User"))
        {

            getUserfollowerList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }



    public void getUserfollowerList()
    {
        ApiCallingService.Friends.getMyFollowers(userfollowerpage,context).enqueue(new Callback<ProfileMyFollowers>() {
            @Override
            public void onResponse(Call<ProfileMyFollowers> call, Response<ProfileMyFollowers> response) {
                if(response.code()==200)
                {
                    try {
                        userfollowerlist.addAll(response.body().getFollowers());
                        boolean next=response.body().getNextPage();
                        if (userfollowerlist == null || userfollowerlist.size() == 0) {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            nousertext.setVisibility(View.VISIBLE);
                        }
                        else{
                            profileMyFollowerAdapter = new FollowersAdapter(context, userfollowerlist, 100);
                            recyclerView.setAdapter(profileMyFollowerAdapter);
                            if(next)
                            {
                                userfollowerpage++;
                            }
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
            public void onFailure(Call<ProfileMyFollowers> call, Throwable t) {
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

        });

    }
    public void getOthersFollowerDetails(int followerid)
    {
        ApiCallingService.Friends.getFriendsFollowers(otherFollowerpage,followerid,context).enqueue(new Callback<FreindFollower>() {
            @Override
            public void onResponse(Call<FreindFollower> call, Response<FreindFollower> response) {
                if(response.code()==200)
                {
                    try
                    {
                        boolean next=response.body().getNextPage();
                        list.addAll(response.body().getFollowers());
                        if (list == null || list.size() == 0) {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            nousertext.setVisibility(View.VISIBLE);
                        }
                        else {
                            profileMyFollowerAdapter = new FollowersAdapter(context, list);
                            recyclerView.setAdapter(profileMyFollowerAdapter);
                            if (next)
                            {
                                otherFollowerpage++;
                            }
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
            public void onFailure(Call<FreindFollower> call, Throwable t) {
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }

        });

    }





}
