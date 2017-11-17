package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.FollowingAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.profile.following.Following;
import com.cncoding.teazer.model.profile.following.ProfileMyFollowing;
import com.cncoding.teazer.model.profile.othersfollowing.OthersFollowing;
import com.cncoding.teazer.utilities.Pojos;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingListActivities extends AppCompatActivity {
    Context context;
    List<Following> list;
    RecyclerView recyclerView;
    FollowingAdapter profileMyFollowingAdapter;
    RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.layout)
    RelativeLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_activities);
        ButterKnife.bind(this);
        context=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusbar));
        }

        Intent intent = getIntent();

        String identifier = intent.getStringExtra("Identifier");
        int userID = Integer.parseInt(intent.getStringExtra("FollowerId"));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Following List</font>"));

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

            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            getUserfollowinglist();
        }
        else if (identifier.equals("Other"))
        {
            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
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
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
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
                        layout.setVisibility(View.VISIBLE);
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

