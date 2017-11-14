package com.cncoding.teazer.adapter;

import android.content.Context;
import android.content.Intent;
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

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private List<Following> list;
    private Context context;

    public FollowingAdapter(Context context, List<Following> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_following, viewGroup, false);
        return new FollowingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FollowingAdapter.ViewHolder viewHolder, int i) {
        Following cont = list.get(i);
        final String followingname = cont.getUserName();
        final int followerId = cont.getUserId();
        viewHolder.followingName.setText(followingname);





        viewHolder.followingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.followingName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, FollowerFollowingProfileActivity.class);
                        intent.putExtra("Username", followingname);
                        intent.putExtra("FollowerId", String.valueOf(followerId));
                        intent.putExtra("UserType", "Following");
                        context.startActivity(intent);

                    }
                });

            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView followingName, address;
        Button follow;

        public ViewHolder(View view) {
            super(view);
            followingName = view.findViewById(R.id.following_name);


        }
    }
}
