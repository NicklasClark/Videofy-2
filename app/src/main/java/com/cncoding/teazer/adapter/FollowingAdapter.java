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

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.model.profile.following.Following;
import com.cncoding.teazer.model.profile.otherfollower.OtherFollowers;
import com.cncoding.teazer.model.profile.othersfollowing.OtherUserFollowings;
import com.cncoding.teazer.ui.fragment.activity.FollowerFollowingProfileActivity;

import java.util.List;

/**
 * Created by farazhabib on 10/11/17.
 */

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private List<Following> list;
    private List<OtherUserFollowings> otherlist;
    private Context context;
    String userType;
    int counter;

    public FollowingAdapter(Context context, List<OtherUserFollowings> otherlist) {
        this.context = context;
        this.otherlist = otherlist;
    }
    public FollowingAdapter(Context context, List<Following> list, int counter) {
        this.context = context;
        this.list = list;
        this.counter = counter;
    }
    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_following, viewGroup, false);
        return new FollowingAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final FollowingAdapter.ViewHolder viewHolder, int i) {
        try {

            final int followerId;
            if (counter == 100) {

                final Following cont = list.get(i);
                final String followingname = cont.getUserName();
                userType = "Following";

                followerId = cont.getUserId();
                viewHolder.followingName.setText(followingname);
                viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, FollowerFollowingProfileActivity.class);
                        intent.putExtra("Username", followingname);
                        intent.putExtra("FollowId", String.valueOf(followerId));
                        intent.putExtra("UserType", userType);
                        context.startActivity(intent);
                    }
                });
            } else

            {

                final OtherUserFollowings cont = otherlist.get(i);
                final boolean myself = cont.getMySelf();
                final String followername = cont.getUserName();
                followerId = cont.getUserId();
                viewHolder.followingName.setText(followername);
                final boolean isblockedyou = cont.getIsBlockedYou();
                final boolean isfollower = cont.getFollower();
                final boolean isfollowing = cont.getFollower();

                if (isblockedyou) {
                    viewHolder.followingName.setTextColor(Color.GRAY);
                    viewHolder.follow.setVisibility(View.INVISIBLE);
                }
                if (myself) {

                    viewHolder.followingName.setTextColor(Color.BLUE);
                    viewHolder.follow.setVisibility(View.INVISIBLE);
                } else {
                    if (isfollowing == true) {

                        viewHolder.follow.setText("Following");
                        userType = "Following";

                    } else {

                        if (isfollower == true) {
                            viewHolder.follow.setText("Follower");
                            userType = "Following";

                        } else {
                            viewHolder.follow.setText("Follow");
                            userType = "Follow";
                        }

                    }
                }

                viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myself) {
                            Intent intent = new Intent(context, BaseBottomBarActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        } else {
                            if (isblockedyou) {
                                Toast.makeText(context, "you can not view this user profile", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intent = new Intent(context, FollowerFollowingProfileActivity.class);
                                intent.putExtra("Username", followername);
                                intent.putExtra("FollowId", String.valueOf(followerId));
                                intent.putExtra("UserType", userType);
                                context.startActivity(intent);
                            }
                        }
                    }
                });

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        if(counter==100)
        {
        return list.size();
    }
    else{
            return otherlist.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView followingName, address;
        Button follow;
        CardView cardview;
        public ViewHolder(View view) {
            super(view);
            followingName = view.findViewById(R.id.following_name);
            follow = view.findViewById(R.id.follow_button);
            cardview = view.findViewById(R.id.cardview);

        }
    }
}
