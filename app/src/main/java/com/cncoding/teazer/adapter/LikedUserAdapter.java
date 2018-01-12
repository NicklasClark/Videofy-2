package com.cncoding.teazer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.post.FragmentLikedUser;
import com.cncoding.teazer.model.post.LikedUser;
import com.cncoding.teazer.model.post.PostDetails;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.ViewUtils.setActionButtonText;

/**
 * Created by farazhabib on 02/01/18.
 */

public class LikedUserAdapter extends RecyclerView.Adapter<LikedUserAdapter.ViewHolder> {

    private List<RecyclerView.LayoutManager> layoutManager;
    List<LikedUser> list;
    private Context context;
    public static final int UNBLOCK_STATUS = 2;
    public static boolean isLikedUser = false;
    PostDetails postDetails;
    Fragment fragment;

    public LikedUserAdapter(Context context, List<LikedUser> list, PostDetails postDetails, Fragment fragment) {
        this.context = context;
        this.list = list;
        this.postDetails = postDetails;
        this.fragment = fragment;
    }

    @Override
    public LikedUserAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_followers, viewGroup, false);
        return new LikedUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LikedUserAdapter.ViewHolder viewHolder, int i) {
        final LikedUser cont = list.get(i);
        try {

            final int requestId;
            final String likedUsername = cont.getUserName();
            final boolean islikedUserDp = cont.getHasProfileMedia();
            final int userId = cont.getUserId();
            final boolean ismyself = cont.getMySelf();
            final boolean isblockedyou = cont.getFollowInfo().getIsBlockedYou();
            final boolean youBlocked = cont.getFollowInfo().getYouBlocked();
            final int accounttype = cont.getAccountType();
            final boolean folower = cont.getFollowInfo().getFollower();
            final boolean following = cont.getFollowInfo().getFollowing();
            final boolean requestsent = cont.getFollowInfo().getRequestSent();
            final boolean requestrecived = cont.getFollowInfo().getRequestReceived();
            final String usertype;

            viewHolder.name.setText(likedUsername);

            if (requestrecived) {
                requestId = cont.getFollowInfo().getRequestId();
            } else requestId = 0;

            if (islikedUserDp) {
                String LikedUserDp = cont.getProfileMedia().getMediaUrl();

                Glide.with(context)
                        .load(LikedUserDp)
                        .skipMemoryCache(false)
                        .into(viewHolder.dp);
            } else {
                Picasso.with(context)
                        .load(R.drawable.ic_user_male_dp_small)
                        .fit().centerInside()
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(viewHolder.dp);
            }

            if (ismyself) {


                viewHolder.name.setTextColor(Color.parseColor("#333333"));
                viewHolder.action.setVisibility(View.INVISIBLE);
                usertype = "";
            } else {
                if (youBlocked) {
                    viewHolder.name.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    viewHolder.action.setVisibility(View.VISIBLE);
                    setActionButtonText(context, viewHolder.action, R.string.unblock);
                    usertype = "";
                } else if (isblockedyou) {
                    viewHolder.name.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    viewHolder.action.setVisibility(View.INVISIBLE);
                    usertype = "";
                } else {

                    viewHolder.action.setVisibility(View.VISIBLE);
                    viewHolder.name.setTextColor(Color.parseColor("#333333"));
                    if (accounttype == 1) {

                        if (following) {

                            if (requestrecived == true) {
                                setActionButtonText(context, viewHolder.action, R.string.accept);
                                usertype = "Accept";
                            } else {

                                setActionButtonText(context, viewHolder.action, R.string.following);
                                usertype = "Following";
                            }

                        } else {
                            if (requestsent) {
                                if (requestrecived == true) {

                                    setActionButtonText(context, viewHolder.action, R.string.accept);
                                    usertype = "Accept";
                                } else {
                                    setActionButtonText(context, viewHolder.action, R.string.requested);
                                    usertype = "Requested";
                                }
                            } else {

                                if (requestrecived == true) {

                                    setActionButtonText(context, viewHolder.action, R.string.accept);
                                    usertype = "Accept";
                                } else {
                                    setActionButtonText(context, viewHolder.action, R.string.follow);
                                    usertype = "Follow";
                                }
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
            viewHolder.action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (viewHolder.action.getText().equals(context.getString(R.string.accept))) {
                        acceptUser(requestId, viewHolder, accounttype, following, requestsent);
                    }
                    if (viewHolder.action.getText().equals(context.getString(R.string.follow))) {
                        followUser(userId, context, viewHolder, accounttype);
                    }
                    if (viewHolder.action.getText().equals(context.getString(R.string.requested))) {
                        cancelRequest(userId, context, viewHolder, accounttype);
                    }
                    if (viewHolder.action.getText().equals(context.getString(R.string.following))) {
                        unFollowUser(userId, context, viewHolder, accounttype);
                    }
                    if (viewHolder.action.getText().equals(context.getString(R.string.unblock))) {
                        blockUnblockUsers(userId, UNBLOCK_STATUS, likedUsername, viewHolder);
                    }
                }
            });
            viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((FragmentLikedUser) fragment).callFragmentLikedUser(userId, ismyself);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void acceptUser(final int requestId, final LikedUserAdapter.ViewHolder viewHolder, final int accounttype, final boolean isfollowing, final boolean requestSent) {

        ApiCallingService.Friends.acceptJoinRequest(requestId, context)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        try {
                            if (response.code() == 200) {
                                if (response.body().getStatus()) {

                                    Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                                    if (isfollowing) {
                                        setActionButtonText(context, viewHolder.action, R.string.following);
                                        // loader.setVisibility(View.GONE);
                                    } else if (requestSent) {
                                        // loader.setVisibility(View.GONE);
                                        setActionButtonText(context, viewHolder.action, R.string.requested);
                                    } else {
                                        // loader.setVisibility(View.GONE);
                                        setActionButtonText(context, viewHolder.action, R.string.follow);
                                    }
                                } else {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    // loader.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Something went wrong, Please try again..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();

                    }
                });
    }


    private void followUser(final int userId, final Context context, final LikedUserAdapter.ViewHolder viewHolder, final int accounttype) {

        ApiCallingService.Friends.followUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b) {


                            if (accounttype == 1) {
                                setActionButtonText(context, viewHolder.action, R.string.requested);
                                Toast.makeText(context, "You have sent following request", Toast.LENGTH_SHORT).show();


                            } else {
                                setActionButtonText(context, viewHolder.action, R.string.following);
                                Toast.makeText(context, "You have started following", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            setActionButtonText(context, viewHolder.action, R.string.following);
                            Toast.makeText(context, "You are aleady following", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void cancelRequest(final int userId, final Context context, final LikedUserAdapter.ViewHolder viewHolder, final int accounttype) {

        ApiCallingService.Friends.cancelRequest(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b) {

                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "Your request has been cancelled", Toast.LENGTH_SHORT).show();


                        } else {
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "Your request has been cancelled", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void unFollowUser(int userId, final Context context, final LikedUserAdapter.ViewHolder viewHolder, final int accountType) {

        ApiCallingService.Friends.unfollowUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b == true) {

                            Toast.makeText(context, "User has been unfollowed", Toast.LENGTH_SHORT).show();
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                        } else {

                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "You have already unfollowed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();

                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void blockUnblockUsers(final int userId, final int status, final String username, final LikedUserAdapter.ViewHolder viewHolder) {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        dialogBuilder.setMessage("Are you sure you want to Unblock " + username + "?");
        dialogBuilder.setPositiveButton("CONFIRM", null);
        dialogBuilder.setNegativeButton("CANCEL", null);

        final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#666666"));
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blockunBlock(userId, status, viewHolder);
                alertDialog.dismiss();


            }
        });
    }


    public void blockunBlock(int userId, final int status, final LikedUserAdapter.ViewHolder holder) {
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
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout cardview;
        CircularAppCompatImageView dp;

        View line;
        ProximaNovaSemiboldTextView action;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            action = view.findViewById(R.id.action);
            cardview = view.findViewById(R.id.root_layout);
            dp = view.findViewById(R.id.dp);

        }
    }


}
