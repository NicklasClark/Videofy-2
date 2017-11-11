package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.FollowingAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.profile.following.Following;
import com.cncoding.teazer.model.profile.following.ProfileMyFollowing;
import com.cncoding.teazer.utilities.Pojos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingActivities extends AppCompatActivity {
    Context context;
    List<Following> list;
    RecyclerView recyclerView;
    FollowingAdapter profileMyFollowingAdapter;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_activities);

        context=this;
        recyclerView=findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getProfileDetail();
    }
    public void getProfileDetail()
    {
        int i=1;
        ApiCallingService.Friends.getMyFollowing(i,context).enqueue(new Callback<ProfileMyFollowing>() {
            @Override
            public void onResponse(Call<ProfileMyFollowing> call, Response<ProfileMyFollowing> response) {
                if(response.code()==200)
                {
                    try
                    {

                        Toast.makeText(context,"Following Activity Detail fetched",Toast.LENGTH_LONG).show();
                        list= response.body().getFollowings();
                        profileMyFollowingAdapter=new FollowingAdapter(context,list);
                        recyclerView.setAdapter(profileMyFollowingAdapter);

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {

                    Toast.makeText(context,"UserProfile Detail not fetched",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ProfileMyFollowing> call, Throwable t) {

            }
        });

    }
    }

