package com.cncoding.teazer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.BaseBottomBarActivity;
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

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    private List<UserInfo> list;
    private List<UserInfo> userlist;
    private Context context;
    private final int UNBLOCK_STATUS=2;
//    List<Following> list2;
    private int counter;
//    final static int PrivateAccount = 1;
//    final static int PublicAccount = 2;
    private OtherProfileListener otherProfileListener;

    public FollowersAdapter(Context context, List<UserInfo> userlist, int counter) {
        this.context = context;
        this.userlist = userlist;
        this.counter = counter;
        if (context instanceof ProfileFragment.FollowerListListener) {
            otherProfileListener = (OtherProfileListener) context;
        }
    }
    public FollowersAdapter(Context context, List<UserInfo> list) {
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
                final UserInfo cont = userlist.get(i);
                final String followername = cont.getFirstName();
                final boolean isfollowersDp=cont.getHasProfileMedia();

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

                final boolean following = cont.getFollowing();
                final boolean requestsent = cont.getRequestSent();
                followerId = cont.getUserId();
                final int accounttype = cont.getAccountType();
                viewHolder.name.setText(followername);


                if (accounttype == 1) {
                    if (following) {
                        setActionButtonText(context, viewHolder.action, R.string.following);
                        usertype = "Following";
                    } else {

                        if (requestsent) {

                            setActionButtonText(context, viewHolder.action, R.string.requested);
                            usertype = "Requested";
                        } else {
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            usertype = "Follow";
                        }
                    }

                } else {
                    if (following) {
                        setActionButtonText(context, viewHolder.action, R.string.following);
                        usertype = "Following";
                    }
                    else {
                        if(requestsent) {
                            setActionButtonText(context, viewHolder.action, R.string.requested);
                            usertype = "Requested";
                        }
                        else {
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            usertype = "Follow";
                        }
                    }
                }
                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        otherProfileListener.viewOthersProfile(String.valueOf(followerId),usertype,followername);
                    }
                });

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

                        if(viewHolder.action.getText().equals(context.getString(R.string.following)))
                        {
                            unFollowUser(followerId, context, viewHolder,accounttype);
                        }



                    }
                });
            }
            else {
                final UserInfo cont = list.get(i);
                final String usertype;
                final int accounttype = cont.getAccountType();
                final boolean myself = cont.getMySelf();
//                final boolean folower = cont.getFollower();
                final boolean following = cont.getFollowing();
                final boolean requestsent = cont.getRequestSent();
                final String followername = cont.getFirstName();
                followerId = cont.getUserId();
                viewHolder.name.setText(followername);
                final boolean isblockedyou = cont.getIsBlockedYou();
                final boolean isfollowersDp=cont.getHasProfileMedia();
                final boolean youBlocked=cont.getYouBlocked();
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


                if (myself) {


                    viewHolder.name.setTextColor( Color.parseColor("#333333"));
                    viewHolder.action.setVisibility(View.INVISIBLE);
                    usertype = "";
                }
                else {
                    if(youBlocked)
                    {
                        viewHolder.name.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                        viewHolder.action.setVisibility(View.VISIBLE);
                        setActionButtonText(context, viewHolder.action, R.string.unblock);
                        usertype = "";
                    }
                    else if (isblockedyou) {
                        viewHolder.name.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                        viewHolder.action.setVisibility(View.INVISIBLE);
                        usertype = "";
                    }
                    else
                        {

                            viewHolder.action.setVisibility(View.VISIBLE);
                            viewHolder.name.setTextColor( Color.parseColor("#333333"));

                        if (accounttype == 1) {
                            if (following) {
                                setActionButtonText(context, viewHolder.action, R.string.following);
                                usertype = "Following";
                            } else {
                                if (requestsent) {
                                    usertype = "Requested";
                                    setActionButtonText(context, viewHolder.action, R.string.requested);
                                } else {
                                    setActionButtonText(context, viewHolder.action, R.string.follow);
                                    usertype = "Follow";
                                }
                            }

                        } else {
                            if (following) {
                                setActionButtonText(context, viewHolder.action, R.string.following);
                                usertype = "Following";
                            } else {
                                if (requestsent) {
                                    setActionButtonText(context, viewHolder.action, R.string.requested);
                                    usertype = "Requested";
                                } else {
                                    setActionButtonText(context, viewHolder.action, R.string.follow);
                                    usertype = "Follow";
                                }
                            }
                        }
                    }
                }

                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
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


                viewHolder.action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(viewHolder.action.getText().equals(context.getString(R.string.follow)))
                        {
                            followUser(followerId, context, viewHolder,accounttype);
                        }
                        if(viewHolder.action.getText().equals(context.getString(R.string.requested)))
                        {
                            cancelRequest(followerId, context, viewHolder,accounttype);

                        }
                        if(viewHolder.action.getText().equals(context.getString(R.string.following)))
                        {
                            unFollowUser(followerId, context, viewHolder,accounttype);
                        }

                        if (viewHolder.action.getText().equals(context.getString(R.string.unblock))) {
                            blockUnblockUsers(followerId, UNBLOCK_STATUS,followername,viewHolder);

                        }
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void followUser(final int userId, final Context context, final ViewHolder viewHolder, final int accounttype) {

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


                            }
                            else {
                                setActionButtonText(context, viewHolder.action, R.string.following);
                                Toast.makeText(context, "You have started following", Toast.LENGTH_LONG).show();
                            }


                        }
                        else {
                            setActionButtonText(context, viewHolder.action, R.string.following);
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
                t.printStackTrace();
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
    }



    private void cancelRequest(final int userId, final Context context, final ViewHolder viewHolder, final int accounttype) {

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
                            Toast.makeText(context, "Your request has been cancelled", Toast.LENGTH_LONG).show();
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


    public void unFollowUser(int userId,final Context context,final FollowersAdapter.ViewHolder viewHolder, final  int accountType)

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

    public void blockUnblockUsers(final int userId, final int status,final String username,final FollowersAdapter.ViewHolder viewHolder) {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        dialogBuilder.setMessage("Are you sure you want to Unblock " +username + "?");
        dialogBuilder.setPositiveButton("CONFIRM", null);
        dialogBuilder.setNegativeButton("CANCEL", null);

        final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#666666"));
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blockunBlock(userId,status, viewHolder);
                alertDialog.dismiss();


            }
        });
    }





    public void blockunBlock(int userId, final int status,final FollowersAdapter.ViewHolder  holder) {
        ApiCallingService.Friends.blockUnblockUser(userId, status, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    boolean b = response.body().getStatus();
                    if (b == true) {

                        Toast.makeText(context, "You have Unblocked this user", Toast.LENGTH_SHORT).show();
                        setActionButtonText(context, holder.action, R.string.follow);
                    } else {
                        Toast.makeText(context, "Already Unblocked this user", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_SHORT).show();
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
        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.name) ProximaNovaSemiboldTextView name;
        @BindView(R.id.action) ProximaNovaSemiboldTextView action;
//        @BindView(R.id.decline) AppCompatImageView declineBtn;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

  public  interface OtherProfileListener {
        void viewOthersProfile(String id, String username, String type);
    }
}
