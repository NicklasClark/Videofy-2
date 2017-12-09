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
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.customViews.UniversalTextView;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.model.profile.following.Following;
import com.cncoding.teazer.model.profile.othersfollowing.OtherUserFollowings;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by farazhabib on 10/11/17.
 */

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private List<Following> list;
    private List<OtherUserFollowings> otherlist;
    private Context context;
    int counter=0;
    OtherProfileListenerFollowing otherProfileListenerFollowing;

    public FollowingAdapter(Context context, List<OtherUserFollowings> otherlist) {
        this.context = context;
        this.otherlist = otherlist;

        if (context instanceof ProfileFragment.FollowerListListener) {
            otherProfileListenerFollowing = (OtherProfileListenerFollowing) context;
        }
    }
    public FollowingAdapter(Context context, List<Following> list, int counter) {
        this.context = context;
        this.list = list;
        this.counter = counter;
        if (context instanceof ProfileFragment.FollowerListListener) {
            otherProfileListenerFollowing = (OtherProfileListenerFollowing) context;
        }
    }

    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_following, viewGroup, false);
        return new FollowingAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final FollowingAdapter.ViewHolder viewHolder,final  int i) {
        try {

            final int followerId;
            if (counter == 100) {

                final Following cont = list.get(i);
                final String followingname = cont.getUserName();
                final int accounttype = cont.getAccountType();
                final String userType;
                followerId = cont.getUserId();
                userType="Following";
                final boolean isfollowersDp=cont.getHasProfileMedia();
                if(isfollowersDp) {
                    String followrsDp = cont.getProfileMedia().getMediaUrl();
                    Picasso.with(context)
                            .load(followrsDp)
                            .fit().centerInside()
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(viewHolder.userDp);
                }
                viewHolder.follow.setText("Following");
                viewHolder.followingName.setText(followingname);

                viewHolder.follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(viewHolder.follow.getText().equals("Follow"))
                        {
                            followUser(followerId, context, viewHolder,accounttype);
                        }
                    }
                });

                viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        otherProfileListenerFollowing.viewOthersProfileFollowing(String.valueOf(followerId),userType,followingname);

                    }
                });
            }
            else
            {

                final OtherUserFollowings cont = otherlist.get(i);
                final String usertype;
                final boolean myself = cont.getMySelf();
                final String followername = cont.getUserName();
                final int accounttype = cont.getAccountType();
                final boolean isfollowersDp=cont.getHasProfileMedia();

                if(isfollowersDp) {
                    String followrsDp = cont.getProfileMedia().getMediaUrl();
                    Picasso.with(context)
                            .load(followrsDp)
                            .fit().centerInside()
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(viewHolder.userDp);
                }
                else
                {
                    Picasso.with(context)
                            .load(R.drawable.ic_user_male_dp_small)
                            .fit().centerInside()
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(viewHolder.userDp);
                }
                followerId = cont.getUserId();
                viewHolder.followingName.setText(followername);

                final boolean isblockedyou = cont.getIsBlockedYou();
                final boolean isfollower = cont.getFollower();
                final boolean isfollowing = cont.getFollowing();
                final boolean isrequestsent=cont.getRequestSent();

                if (isblockedyou) {

                    viewHolder.followingName.setTextColor(Color.GRAY);
                    viewHolder.follow.setVisibility(View.INVISIBLE);
                }

               if (myself)
                {
                    usertype="";
                    viewHolder.followingName.setTextColor(Color.parseColor("#26C6DA"));
                    viewHolder.follow.setVisibility(View.INVISIBLE);

                }
                else {
                   viewHolder.followingName.setTextColor(Color.DKGRAY);
                   viewHolder.follow.setVisibility(View.VISIBLE);

                    if (isfollowing == true) {
                        viewHolder.follow.setText("Following");
                        usertype = "Following";

                    } else {

                        if(isrequestsent)
                        {
                            viewHolder.follow.setText("Requested");
                            usertype = "Requested";
                        }
                        else {

                            viewHolder.follow.setText("Follow");
                            usertype = "Follow";

//                            if (isfollower == true) {
//                                viewHolder.follow.setText("Follow");
//                                usertype = "Follow";
//
//                            } else {
//                                viewHolder.follow.setText("Follow");
//                                usertype = "Follow";
//                            }
                        }

                    }
                }

                viewHolder.follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(viewHolder.follow.getText().equals("Follow"))
                        {
                            followUser(followerId, context, viewHolder,accounttype);
                        }
                    }
                });


                viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myself) {
//                            Intent intent = new Intent(context, BaseBottomBarActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            context.startActivity(intent);
                            otherProfileListenerFollowing.viewUserProfile();

                        } else {
                            if (isblockedyou) {
                                Toast.makeText(context, "you can not view this user profile", Toast.LENGTH_LONG).show();
                            } else {

                                otherProfileListenerFollowing.viewOthersProfileFollowing(String.valueOf(followerId),usertype,followername);
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

    public void followUser(final int userId, final Context context, final FollowingAdapter.ViewHolder viewHolder, final int accounttype) {

        ApiCallingService.Friends.followUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b == true) {


                            if (accounttype == 1) {
                                viewHolder.follow.setText("Requested");
                                Toast.makeText(context, "You have sent following request", Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(context, "You have started following", Toast.LENGTH_LONG).show();
                                viewHolder.follow.setText("Following");
                            }


                        } else {

                            viewHolder.follow.setText("Following");
                            Toast.makeText(context, "You are already following", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
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
        private TextView address;
        private UniversalTextView followingName;
        ProximaNovaSemiboldTextView follow;
        CardView cardview;
        CircularAppCompatImageView userDp;
        public ViewHolder(View view) {
            super(view);
            followingName = view.findViewById(R.id.following_name);
            follow = view.findViewById(R.id.follow_button);
            cardview = view.findViewById(R.id.cardview);
            userDp = view.findViewById(R.id.userDp);

        }
    }
    public  interface OtherProfileListenerFollowing
    {
        public void viewOthersProfileFollowing(String id, String username, String type);
        public void viewUserProfile();

    }
}
