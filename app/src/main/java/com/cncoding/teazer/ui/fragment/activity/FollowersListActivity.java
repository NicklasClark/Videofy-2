package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.FollowersAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.profile.followers.Follower;
import com.cncoding.teazer.model.profile.followers.ProfileMyFollowers;
import com.cncoding.teazer.model.profile.otherfollower.FreindFollower;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersListActivity extends AppCompatActivity {

    Context context;
    List<Follower> list;
    RecyclerView recyclerView;
    FollowersAdapter profileMyFollowerAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        context=this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                onBackPressed();
            }
        });
        recyclerView=findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        int followerid=  Integer.parseInt(intent.getStringExtra("FollowerId"));
        String identifier=intent.getStringExtra("Identifier");

        if(identifier.equals("Other"))
        {
            getOthersFollowerDetails(followerid);
        }
        else if(identifier.equals("User"))
        {
            getUserfollowerList();

        }
    }
    public void getUserfollowerList()
    {
        int i=1;
        ApiCallingService.Friends.getMyFollowers(i,context).enqueue(new Callback<ProfileMyFollowers>() {
            @Override
            public void onResponse(Call<ProfileMyFollowers> call, Response<ProfileMyFollowers> response) {
                if(response.code()==200)
                {
                    try
                    {
                        list= response.body().getFollowers();
                        profileMyFollowerAdapter=new FollowersAdapter(context,list);
                        recyclerView.setAdapter(profileMyFollowerAdapter);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {

                }
            }
            @Override
            public void onFailure(Call<ProfileMyFollowers> call, Throwable t) {

            }

        });

    }

    public void getOthersFollowerDetails(int followerid)
    {

        int i=1;
        ApiCallingService.Friends.getFriendsFollowers(i,followerid,context).enqueue(new Callback<FreindFollower>() {
            @Override
            public void onResponse(Call<FreindFollower> call, Response<FreindFollower> response) {
                if(response.code()==200)
                {
                    try

                    {
                        list= response.body().getFollowers();
                        profileMyFollowerAdapter=new FollowersAdapter(context,list);
                        recyclerView.setAdapter(profileMyFollowerAdapter);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {

                }
            }
            @Override
            public void onFailure(Call<FreindFollower> call, Throwable t) {

            }

        });

    }

}
