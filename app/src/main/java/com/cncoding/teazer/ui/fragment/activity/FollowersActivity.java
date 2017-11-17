package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.FollowersAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.profile.followers.Follower;
import com.cncoding.teazer.model.profile.followers.ProfileMyFollowers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity {

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
        recyclerView=findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getProfileDetail();

    }

    public void getProfileDetail()
    {
        int i=1;
        ApiCallingService.Friends.getMyFollowers(i,context).enqueue(new Callback<ProfileMyFollowers>() {
            @Override
            public void onResponse(Call<ProfileMyFollowers> call, Response<ProfileMyFollowers> response) {
                if(response.code()==200)
                {
                    try
                    {

                         Toast.makeText(context,"FollowersActivity Detail fetched",Toast.LENGTH_LONG).show();
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

                    Toast.makeText(context,"PublicProfile Detail not fetched",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ProfileMyFollowers> call, Throwable t) {

            }
        });

    }

}
