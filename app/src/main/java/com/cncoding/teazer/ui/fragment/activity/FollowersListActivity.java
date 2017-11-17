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
import com.cncoding.teazer.adapter.FollowersAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.profile.blockuser.Followers;
import com.cncoding.teazer.model.profile.followers.Follower;
import com.cncoding.teazer.model.profile.followers.ProfileMyFollowers;
import com.cncoding.teazer.model.profile.otherfollower.FreindFollower;
import com.cncoding.teazer.model.profile.otherfollower.OtherFollowers;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersListActivity extends AppCompatActivity {

    Context context;
    List<OtherFollowers> list;
    List<Follower> list2;
    RecyclerView recyclerView;
    FollowersAdapter profileMyFollowerAdapter;
    RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.layout)
    RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        ButterKnife.bind(this);
        context=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusbar));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Follower List</font>"));
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
            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            getOthersFollowerDetails(followerid);
        }
        else if(identifier.equals("User"))
        {
            layout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            getUserfollowerList();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
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
                        list2= response.body().getFollowers();
                        profileMyFollowerAdapter=new FollowersAdapter(context,list2,100);
                        recyclerView.setAdapter(profileMyFollowerAdapter);
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
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
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
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
