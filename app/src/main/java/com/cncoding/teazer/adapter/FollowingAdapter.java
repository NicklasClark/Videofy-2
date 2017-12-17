package com.cncoding.teazer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.model.friends.UserInfo;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.ViewUtils.setActionButtonText;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private List<UserInfo> list;
    private List<UserInfo> otherlist;
    private Context context;
    private int counter=0;
    private OtherProfileListenerFollowing otherProfileListenerFollowing;

    public FollowingAdapter(Context context, List<UserInfo> otherlist) {
        this.context = context;
        this.otherlist = otherlist;

        if (context instanceof ProfileFragment.FollowerListListener) {
            otherProfileListenerFollowing = (OtherProfileListenerFollowing) context;
        }
    }
    public FollowingAdapter(Context context, List<UserInfo> list, int counter) {
        this.context = context;
        this.list = list;
        this.counter = counter;
        if (context instanceof ProfileFragment.FollowerListListener) {
            otherProfileListenerFollowing = (OtherProfileListenerFollowing) context;
        }
    }

    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_following, viewGroup,
                false);
        return new FollowingAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final FollowingAdapter.ViewHolder viewHolder,final  int i) {
        try {

            final int followerId;
            if (counter == 100) {

                final UserInfo cont = list.get(i);
                final String followingname = cont.getUserName();
                final int accounttype = cont.getAccountType();
                final String userType;
                followerId = cont.getUserId();
                userType=context.getString(R.string.follow);
                final boolean isfollowersDp=cont.getHasProfileMedia();
               // viewHolder.action.setText(context.getString(R.string.following));
                setActionButtonText(context, viewHolder.action, R.string.following);

                if(isfollowersDp) {
                    String followrsDp = cont.getProfileMedia().getMediaUrl();
                    Glide.with(context)
                            .load(followrsDp)
                            .skipMemoryCache(false)
                            .into(viewHolder.dp);
                }

                else
                {
                    Picasso.with(context)
                            .load(R.drawable.ic_user_male_dp_small)
                            .fit().centerInside()
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(viewHolder.dp);
                }



                viewHolder.name.setText(followingname);

                viewHolder.action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(viewHolder.action.getText().equals(context.getString(R.string.follow)))
                        {

                            followUser(followerId, context, viewHolder,accounttype);
                        }

                        if(viewHolder.action.getText().equals(context.getString(R.string.following)))

                        {

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            dialogBuilder.setMessage("Are you sure you want to Unfollow " + followingname + "?");
                            dialogBuilder.setPositiveButton("CONFIRM", null);
                            dialogBuilder.setNegativeButton("CANCEL", null);

                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();

                            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            // override the text color of positive button
                            positiveButton.setTextColor(Color.parseColor("#666666"));
                            // provides custom implementation to positive button click
                            positiveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    unFollowUser(followerId, context, viewHolder, accounttype);
                                    alertDialog.dismiss();


                                }
                            });

                            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                            negativeButton.setTextColor(Color.parseColor("#999999"));

                            negativeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });




                        }



                    }
                });
                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        otherProfileListenerFollowing.viewOthersProfileFollowing(String.valueOf(followerId),userType,followingname);

                    }
                });
            }
            else {
                final UserInfo cont = otherlist.get(i);
                final String usertype;
                final boolean myself = cont.getMySelf();
                final String followername = cont.getUserName();
                final int accounttype = cont.getAccountType();
                final boolean isfollowersDp=cont.getHasProfileMedia();

                if(isfollowersDp) {

                    String followrsDp = cont.getProfileMedia().getMediaUrl();
                    Glide.with(context)
                            .load(followrsDp)
                            .placeholder(R.drawable.ic_user_male_dp_small)
                            .skipMemoryCache(false)
                            .into(viewHolder.dp);
                }
                else
                {
                    Picasso.with(context)
                            .load(R.drawable.ic_user_male_dp_small)
                            .fit().centerInside()
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(viewHolder.dp);
                }
                followerId = cont.getUserId();
                viewHolder.name.setText(followername);
                final boolean isblockedyou = cont.getIsBlockedYou();
                final boolean isfollower = cont.getFollower();
                final boolean isfollowing = cont.getFollowing();
                final boolean isrequestsent=cont.getRequestSent();

                if (isblockedyou) {
                    viewHolder.name.setTextColor(Color.GRAY);
                    viewHolder.action.setVisibility(View.INVISIBLE);
                }
               if (myself) {
                    usertype="";
                   viewHolder.name.setTextColor( Color.parseColor("#333333"));
                    viewHolder.action.setVisibility(View.INVISIBLE);
                }

                else {
                   viewHolder.name.setTextColor( Color.parseColor("#333333"));
                   viewHolder.action.setVisibility(View.VISIBLE);

                   if (isfollowing) {
                       setActionButtonText(context, viewHolder.action, R.string.following);
                       usertype = "Following";
                   }else {
                       if(isrequestsent) {
                            setActionButtonText(context, viewHolder.action, R.string.requested);
                            usertype = "Requested";
                       }
                       else {
                           setActionButtonText(context, viewHolder.action, R.string.follow);
                            usertype = "Follow";
                        }

                    }
                }

                viewHolder.action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(viewHolder.action.getText().equals(context.getString(R.string.follow))) {
                            followUser(followerId, context, viewHolder,accounttype);
                        }

                        if(viewHolder.action.getText().equals(context.getString(R.string.requested)))
                        {
                            cancelRequest(followerId, context, viewHolder,accounttype);

                        }
                        if(viewHolder.action.getText().equals(context.getString(R.string.following))) {


                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            dialogBuilder.setMessage("Are you sure you want to Unfollow " + followername + "?");
                            dialogBuilder.setPositiveButton("CONFIRM", null);
                            dialogBuilder.setNegativeButton("CANCEL", null);

                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();

                            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            // override the text color of positive button
                            positiveButton.setTextColor(Color.parseColor("#666666"));
                            // provides custom implementation to positive button click
                            positiveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    unFollowUser(followerId, context, viewHolder, accounttype);
                                    alertDialog.dismiss();


                                }
                            });

                            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                            negativeButton.setTextColor(Color.parseColor("#999999"));

                            negativeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });


                        }

                    }
                });


                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myself) {

                            otherProfileListenerFollowing.viewUserProfile();

                        } else {
                            if (isblockedyou) {
                                Toast.makeText(context, "you can not view this user profile", Toast.LENGTH_LONG).show();
                            } else {

                                otherProfileListenerFollowing.viewOthersProfileFollowing(
                                        String.valueOf(followerId),usertype,followername);
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

    private void followUser(final int userId, final Context context, final FollowingAdapter.ViewHolder viewHolder,
                            final int accounttype) {
        ApiCallingService.Friends.followUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b) {
                            if (accounttype == 1) {
                                setActionButtonText(context, viewHolder.action, R.string.requested);
                                Toast.makeText(context, "You have sent following request", Toast.LENGTH_LONG).show();


                            } else {
                                setActionButtonText(context, viewHolder.action, R.string.following);
                                Toast.makeText(context, "You have started following", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            setActionButtonText(context, viewHolder.action, R.string.following);
                            Toast.makeText(context, "You are already following", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void cancelRequest(final int userId, final Context context, final FollowingAdapter.ViewHolder viewHolder, final int accounttype) {

        ApiCallingService.Friends.cancelRequest(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b) {

                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "Your request has been cancelled", Toast.LENGTH_LONG).show();
                        }

                        else {
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "Your request has alread been cancelled", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void unFollowUser(int userId,final Context context,final FollowingAdapter.ViewHolder viewHolder, final  int accountType)

    {

        ApiCallingService.Friends.unfollowUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b == true) {

                            Toast.makeText(context, "User has been unfollowed", Toast.LENGTH_LONG).show();
                            //_btnfollow.setText("Follow");
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                        } else {

                            //_btnfollow.setText("Follow");
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "You have already unfollowed", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e) {

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

        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.action) ProximaNovaSemiboldTextView action;
        @BindView(R.id.decline) AppCompatImageView declineBtn;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public  interface OtherProfileListenerFollowing
    {
        public void viewOthersProfileFollowing(String id, String username, String type);
        public void viewUserProfile();
    }



}
