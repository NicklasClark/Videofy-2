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
import com.cncoding.teazer.adapter.FollowingAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.profile.following.Following;
import com.cncoding.teazer.model.profile.following.ProfileMyFollowing;
import com.cncoding.teazer.model.profile.othersfollowing.OthersFollowing;
import com.cncoding.teazer.utilities.Pojos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingListActivities extends AppCompatActivity {
    Context context;
    List<Following> list;
    RecyclerView recyclerView;
    FollowingAdapter profileMyFollowingAdapter;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_activities);
        context = this;
        Intent intent = getIntent();


        String identifier = intent.getStringExtra("Identifier");
        int userID = Integer.parseInt(intent.getStringExtra("FollowerId"));


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
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (identifier.equals("User")) {

            getUserfollowinglist();
        } else if (identifier.equals("Other")) {

            getOthersFollowingList(userID);

        }
    }

    public void getUserfollowinglist() {
        int i = 1;

        ApiCallingService.Friends.getMyFollowing(i, context).enqueue(new Callback<ProfileMyFollowing>() {
            @Override
            public void onResponse(Call<ProfileMyFollowing> call, Response<ProfileMyFollowing> response) {
                if (response.code() == 200) {
                    try {
                        list = response.body().getFollowings();
                        profileMyFollowingAdapter = new FollowingAdapter(context, list);
                        recyclerView.setAdapter(profileMyFollowingAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileMyFollowing> call, Throwable t) {
                Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void getOthersFollowingList(int userId) {


        int i = 1;
        ApiCallingService.Friends.getFriendsFollowings(i, userId, context).enqueue(new Callback<OthersFollowing>() {
            @Override
            public void onResponse(Call<OthersFollowing> call, Response<OthersFollowing> response) {
                if (response.code() == 200) {
                    try {
                        list = response.body().getFollowings();
                        profileMyFollowingAdapter = new FollowingAdapter(context, list);
                        recyclerView.setAdapter(profileMyFollowingAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OthersFollowing> call, Throwable t) {

            }
        });


    }
}

