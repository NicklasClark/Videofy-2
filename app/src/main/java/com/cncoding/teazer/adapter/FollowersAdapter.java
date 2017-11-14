package com.cncoding.teazer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.profile.followers.Follower;
import com.cncoding.teazer.model.profile.following.Following;
import com.cncoding.teazer.model.profile.following.ProfileMyFollowing;
import com.cncoding.teazer.ui.fragment.activity.FollowerFollowingProfileActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by farazhabib on 10/11/17.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    private List<Follower> list;
    private Context context;
    public static final String UserType = "Follower";
    List<Following>  list2;

    public FollowersAdapter(Context context, List<Follower> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public FollowersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_followers, viewGroup, false);

        return new FollowersAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final FollowersAdapter.ViewHolder viewHolder, int i) {
        Follower cont = list.get(i);
        final String followername = cont.getUserName();
        final int followerId = cont.getUserId();
        final String  followerusernamee = cont.getUserName();
        viewHolder.followersname.setText(followername);
        getuserFollowing(viewHolder,followerusernamee);
        viewHolder.followersname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FollowerFollowingProfileActivity.class);
                intent.putExtra("Username", followername);
                intent.putExtra("FollowerId", String.valueOf(followerId));
                intent.putExtra("UserType", UserType);
                context.startActivity(intent);
            }
        });
    }
    public void  getuserFollowing(final FollowersAdapter.ViewHolder viewHolder ,final String username)
    {
        final int i = 1;
        ApiCallingService.Friends.getMyFollowing(i, context).enqueue(new Callback<ProfileMyFollowing>() {
            @Override
            public void onResponse(Call<ProfileMyFollowing> call, Response<ProfileMyFollowing> response) {
                if (response.code() == 200) {
                    try {
                        list2 = response.body().getFollowings();
                        for(int j=0;j<list2.size();j++)
                        {
                            String followingname=list2.get(j).getUserName();
                            if (username.equals(followingname))
                            {
                                viewHolder.follow.setText("Following");
                                viewHolder.follow.setTextColor(Color.WHITE);
                                viewHolder.follow.setBackgroundColor(viewHolder.follow.getContext().getResources().getColor(R.color.colorTabindicator));
                                break;
                            }
                        }
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView followersname, address;
        Button follow;
        CardView cardview;


        public ViewHolder(View view) {
            super(view);
            followersname = view.findViewById(R.id.followers_name);
            follow = view.findViewById(R.id.follow_button);
            cardview = view.findViewById(R.id.cardview);

        }
    }
}
