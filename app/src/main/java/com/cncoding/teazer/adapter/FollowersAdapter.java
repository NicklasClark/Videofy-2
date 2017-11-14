package com.cncoding.teazer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.model.profile.followers.Follower;
import com.cncoding.teazer.ui.fragment.activity.FollowerFollowingProfileActivity;

import java.util.List;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    private List<Follower> list;
    private Context context;
    public static final String UserType="Follower";
    public FollowersAdapter(Context context, List<Follower> list) {
        this.context = context;
        this.list =list;
    }
    @Override
    public FollowersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_followers, viewGroup, false);
        return new FollowersAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final FollowersAdapter.ViewHolder viewHolder, int i) {
          Follower  cont = list.get(i);
        final String followername=cont.getUserName();
        final  int followerId=cont.getUserId();
        viewHolder.followersname.setText(followername);
        viewHolder.followersname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, FollowerFollowingProfileActivity.class);
                intent.putExtra("Username",followername);
                intent.putExtra("FollowerId",String.valueOf(followerId));
                intent.putExtra("UserType",UserType);
                context.startActivity(intent);

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
            cardview = view.findViewById(R.id.cardview);



        }
    }
}
