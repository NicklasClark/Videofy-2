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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.model.profile.followers.Follower;
import com.cncoding.teazer.model.profile.following.Following;
import com.cncoding.teazer.model.profile.otherfollower.OtherFollowers;
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

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    private List<OtherFollowers> list;
    private List<Follower> userlist;
    private Context context;
    //public static final String UserType = "Follower";
    List<Following> list2;
    int counter;
    final static int PrivateAccount = 1;
    final static int PublicAccount = 2;
    OtherProfileListener otherProfileListener;


    public FollowersAdapter(Context context, List<Follower> userlist, int counter) {
        this.context = context;
        this.userlist = userlist;
        this.counter = counter;



            if (context instanceof ProfileFragment.FollowerListListener) {
                otherProfileListener = (OtherProfileListener) context;
            }

    }

    public FollowersAdapter(Context context, List<OtherFollowers> list) {
        this.context = context;
        this.list = list;

        if (context instanceof ProfileFragment.FollowerListListener) {
            otherProfileListener = (OtherProfileListener) context;
        }
    }

    @Override
    public FollowersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_followers, viewGroup, false);
        return new FollowersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FollowersAdapter.ViewHolder viewHolder, final int i) {
        try {

            final int followerId;
            if (counter == 100) {

                final String usertype;
                final Follower cont = userlist.get(i);
                final String followername = cont.getUserName();
                final boolean isfollowersDp=cont.getHasProfileMedia();
                if(isfollowersDp) {
                    String followrsDp = cont.getProfileMedia().getThumbUrl();
                    Picasso.with(context)
                            .load(followrsDp)
                            .fit().centerInside()
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(viewHolder.userDp);

                }
                final boolean folower = cont.getFollower();
                final boolean following = cont.getFollowing();
                final boolean requestsent = cont.getRequestSent();
                followerId = cont.getUserId();
                final int accounttype = cont.getAccountType();
                viewHolder.followersname.setText(followername);


                if (accounttype == 1) {

                    if (following == true) {

                        viewHolder.follow.setText("Following");
                        usertype = "Following";
                        viewHolder.follow.setText("Following");
                    } else {

                        if (requestsent == true) {
                            viewHolder.follow.setText("Requested");
                            usertype = "Requested";
                            viewHolder.follow.setText("Requested");
                            viewHolder.follow.setTextColor(Color.WHITE);
                            viewHolder.follow.setBackgroundColor(viewHolder.follow.getContext().getResources().getColor(R.color.blur));

                        } else {

                            viewHolder.follow.setText("Follow");
                            usertype = "Follow";
                            viewHolder.follow.setText("Follow");
                            viewHolder.follow.setTextColor(Color.WHITE);
                            viewHolder.follow.setBackgroundColor(viewHolder.follow.getContext().getResources().getColor(R.color.colorTabindicator));
                        }
                    }

                } else {
                    if (following == true) {
                        viewHolder.follow.setText("Following");
                        usertype = "Following";
                        viewHolder.follow.setText("Following");
                    }
                    else {
                        if(requestsent==true)
                        {
                            viewHolder.follow.setText("Requested");
                            usertype = "Requested";
                        }
                        else {

                            viewHolder.follow.setText("Follow");
                            usertype = "Follow";
                            viewHolder.follow.setText("Follow");
                            viewHolder.follow.setTextColor(Color.WHITE);
                            viewHolder.follow.setBackgroundColor(viewHolder.follow.getContext().getResources().getColor(R.color.colorTabindicator));
                        }
                    }
                }
                viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        otherProfileListener.viewOthersProfile(String.valueOf(followerId),usertype,followername);
                    }
                });

                viewHolder.follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(viewHolder.follow.getText().equals("Follow"))

                        {

                            followUser(followerId, context, viewHolder,accounttype);

                        }

                    }
                });

            } else {
                final OtherFollowers cont = list.get(i);
                final String usertype;
                final int accounttype = cont.getAccountType();
                final boolean myself = cont.getMySelf();
                final boolean folower = cont.getFollower();
                final boolean following = cont.getFollowing();
                final boolean requestsent = cont.getRequestSent();
                final String followername = cont.getUserName();
                followerId = cont.getUserId();
                viewHolder.followersname.setText(followername);
                final boolean isblockedyou = cont.getIsBlockedYou();

                final boolean isfollowersDp=cont.getHasProfileMedia();
                if(isfollowersDp) {
                    String followrsDp = cont.getProfileMedia().getThumbUrl();
                    Picasso.with(context)
                            .load(followrsDp)
                            .fit().centerInside()
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(viewHolder.userDp);

                }

                if (myself) {
                    viewHolder.followersname.setTextColor(Color.BLUE);
                    viewHolder.follow.setVisibility(View.INVISIBLE);
                    usertype = "";
                }
                else {

                    if (isblockedyou) {
                        viewHolder.followersname.setTextColor(Color.GRAY);
                        viewHolder.follow.setVisibility(View.INVISIBLE);
                        usertype = "";

                    }
                    else
                        {

                        if (accounttype == 1) {

                            if (following == true) {
                                viewHolder.follow.setText("Following");
                                usertype = "Following";

                            } else {

                                if (requestsent == true) {
                                    usertype = "Requested";
                                    viewHolder.follow.setText("Requested");
                                    viewHolder.follow.setTextColor(Color.WHITE);
                                    viewHolder.follow.setBackgroundColor(viewHolder.follow.getContext().getResources().getColor(R.color.blur));

                                } else {

                                    viewHolder.follow.setText("Follow");
                                    usertype = "Follow";
                                    viewHolder.follow.setTextColor(Color.WHITE);
                                    viewHolder.follow.setBackgroundColor(viewHolder.follow.getContext().getResources().getColor(R.color.colorTabindicator));
                                }
                            }

                        } else {

                            if (following == true) {
                                viewHolder.follow.setText("Following");
                                usertype = "Following";
                            } else {
                                if (requestsent == true) {
                                    viewHolder.follow.setText("Requested");
                                    usertype = "Requested";
                                } else {
                                    viewHolder.follow.setText("Follow");
                                    usertype = "Follow";
                                    viewHolder.follow.setTextColor(Color.WHITE);
                                    viewHolder.follow.setBackgroundColor(viewHolder.follow.getContext().getResources().getColor(R.color.colorTabindicator));

                                }


                            }
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
                        } else

                        {
                            if (isblockedyou) {

                                Toast.makeText(context, "you can not view this user profile", Toast.LENGTH_LONG).show();
                            } else {
                                otherProfileListener.viewOthersProfile(String.valueOf(followerId),usertype,followername);
                            }
                        }
                    }
                });


                viewHolder.follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(viewHolder.follow.getText().equals("Follow"))
                        {


                            followUser(followerId, context, viewHolder,accounttype);
                        }
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void followUser(final int userId, final Context context,final ViewHolder viewHolder, final int accounttype) {

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


                            }
                            else {
                                Toast.makeText(context, "You have started following", Toast.LENGTH_LONG).show();
                                viewHolder.follow.setText("Following");
                            }


                        }
                        else {

                            viewHolder.follow.setText("Following");
                            Toast.makeText(context, "You are aleady following", Toast.LENGTH_LONG).show();
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

        if (counter == 100) {

            return userlist.size();
        } else {
            return list.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView followersname, address;
        Button follow;
        CardView cardview;
        ProgressBar progress_bar;
        CircularAppCompatImageView userDp;


        public ViewHolder(View view) {
            super(view);
            followersname = view.findViewById(R.id.followers_name);
            follow = view.findViewById(R.id.follow_button);
            cardview = view.findViewById(R.id.cardview);
            progress_bar = view.findViewById(R.id.progress_bar);
            userDp = view.findViewById(R.id.userDp);

        }
    }

  public  interface OtherProfileListener
    {
        public void viewOthersProfile(String id, String username, String type);

    }
}
