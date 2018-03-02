package com.cncoding.teazer.ui.home.profile.adapter;

import android.content.Context;
import android.content.DialogInterface;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.friends.MyUserInfo;
import com.cncoding.teazer.data.model.friends.UserInfo;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.profile.ProfileFragment;
import com.cncoding.teazer.ui.home.profile.activity.FollowersListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.common.ViewUtils.setActionButtonText;

/**
 * Created by farazhabib on 10/11/17.
 */

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    private List<UserInfo> list;
    private List<MyUserInfo> userlist;
    private Context context;
    private final int UNBLOCK_STATUS = 2;
    private int userfollowerstatus;
    private OtherProfileListener otherProfileListener;

    public FollowersAdapter(Context context, List<MyUserInfo> userlist, int userfollowerstatus) {
        this.context = context;
        this.userlist = userlist;
        this.userfollowerstatus = userfollowerstatus;

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


            if (userfollowerstatus == FollowersListFragment.USERS_FOLLOWER) {

                final String usertype;
                final  int requestId;
                final MyUserInfo cont = userlist.get(i);
                final String followername = cont.getUserName();
                final boolean isfollowersDp = cont.getHasProfileMedia();

                if (isfollowersDp) {
                    String followrsDp = cont.getProfileMedia().getMediaUrl();
                    Glide.with(context)
                            .load(followrsDp)
                            .apply(new RequestOptions().skipMemoryCache(false))
                            .into(viewHolder.dp);
                } else {
                    Glide.with(context)
                            .load(R.drawable.ic_user_male_dp_small)
                            .apply(new RequestOptions().centerInside().diskCacheStrategy(DiskCacheStrategy.NONE))
                            .into(viewHolder.dp);
                }

                final boolean following = cont.getFollowing();
                final boolean follower = cont.getFollower();
                final boolean requestsent = cont.getRequestSent();
                final boolean requestRecieved=cont.getFollowInfo().getRequestReceived();
                followerId = cont.getUserId();
                if(requestRecieved) {
                  requestId = cont.getFollowInfo().getRequestId();
                }
                else
                {
                    requestId=0;
                }


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

                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        otherProfileListener.viewOthersProfile(String.valueOf(followerId), usertype, followername);


                    }
                });


                viewHolder.action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (viewHolder.action.getText().equals(context.getString(R.string.follow))) {


                            if (requestRecieved && follower)

                            {
                                acceptUser(requestId, viewHolder, accounttype, following, requestsent, i, false, true);

                            }
                            else {
                                followUser(followerId, context, viewHolder, accounttype, i, true);

                            }

                        }
                        if (viewHolder.action.getText().equals(context.getString(R.string.requested))) {
                            cancelRequest(followerId, context, viewHolder, accounttype, i, true);

                        }

                        if (viewHolder.action.getText().equals(context.getString(R.string.following))) {
                            unFollowUser(followerId, context, viewHolder, accounttype, i, true);
                        }


                    }
                });
            } else {

                final UserInfo cont = list.get(i);
                final String usertype;
                final int requestId;
                final int accounttype = cont.getAccountType();
                final boolean myself = cont.getMySelf();
                final boolean follower = cont.getFollower();
                final boolean following = cont.getFollowing();
                final boolean requestsent = cont.getRequestSent();
                final boolean requestrecived = cont.getRequestRecieved();
                if (requestrecived) {
                    requestId = cont.getRequestId();
                } else requestId = 0;

                final String followername = cont.getUserName();
                followerId = cont.getUserId();
                viewHolder.name.setText(followername);
                final boolean isblockedyou = cont.getIsBlockedYou();
                final boolean isfollowersDp = cont.getHasProfileMedia();
                final boolean youBlocked = cont.getYouBlocked();
                if (isfollowersDp) {
                    String followrsDp = cont.getProfileMedia().getMediaUrl();
                    Glide.with(context)
                            .load(followrsDp)
                            .apply(new RequestOptions().skipMemoryCache(false))
                            .into(viewHolder.dp);
                } else {
                    Glide.with(context)
                            .load(R.drawable.ic_user_male_dp_small)
                            .apply(new RequestOptions().centerInside().diskCacheStrategy(DiskCacheStrategy.NONE))
                            .into(viewHolder.dp);
                }
                if (myself) {
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


                                    if (requestrecived == true && follower) {

                                        setActionButtonText(context, viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    } else if (requestrecived == true && !follower) {
                                        setActionButtonText(context, viewHolder.action, R.string.accept);
                                        usertype = "Accept";

                                    } else if (requestrecived == false && follower) {
                                        setActionButtonText(context, viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    } else if (requestrecived == false) {
                                        setActionButtonText(context, viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    } else {
                                        usertype = "Follow";

                                    }


                                }
                            }
                        } else {
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

                                    if (requestrecived == true && follower) {

                                        setActionButtonText(context, viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    } else if (requestrecived == true && !follower) {
                                        setActionButtonText(context, viewHolder.action, R.string.accept);
                                        usertype = "Accept";

                                    } else if (requestrecived == false && follower) {
                                        setActionButtonText(context, viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    } else if (requestrecived == false) {
                                        setActionButtonText(context, viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    } else {
                                        usertype = "Follow";

                                    }


                                }
                            }
                        }

                    }
                }

                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (myself) {

                            otherProfileListener.viewUserProfile();

                        } else {
                            if (isblockedyou) {

                                Toast.makeText(context, "you can not view this user profile", Toast.LENGTH_LONG).show();
                            }
                            else {
                                otherProfileListener.viewOthersProfile(String.valueOf(followerId), usertype, followername);
                            }
                        }
                    }
                });
                viewHolder.action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (viewHolder.action.getText().equals(context.getString(R.string.accept))) {
                            acceptUser(requestId, viewHolder, accounttype, following, requestsent, i, true,false);
                        }

                        if (viewHolder.action.getText().equals(context.getString(R.string.follow))) {

                            if (requestrecived && follower)

                            {
                                acceptUser(requestId, viewHolder, accounttype, following, requestsent, i, false,false);

                            } else {
                                followUser(followerId, context, viewHolder, accounttype, i, false);

                            }

                        }
                        if (viewHolder.action.getText().equals(context.getString(R.string.requested))) {
                            cancelRequest(followerId, context, viewHolder, accounttype, i, false);
                        }
                        if (viewHolder.action.getText().equals(context.getString(R.string.following))) {
                            unFollowUser(followerId, context, viewHolder, accounttype, i, false);
                        }
                        if (viewHolder.action.getText().equals(context.getString(R.string.unblock))) {
                            blockUnblockUsers(followerId, UNBLOCK_STATUS, followername, viewHolder, i);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptUser(final int requestId, final ViewHolder viewHolder, final int accounttype, final boolean isfollowing, final boolean requestSent, final int i, final boolean isAcceptFollow,final boolean isUser) {
        //  loader.setVisibility(View.VISIBLE);
        ApiCallingService.Friends.acceptJoinRequest(requestId, context)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        try {
                            if (response.code() == 200) {
                                if (response.body().getStatus()) {
                                    if (isAcceptFollow) {
                                        Toast.makeText(context, "Request Accepted", Toast.LENGTH_LONG).show();
                                        if (isfollowing) {
                                            setActionButtonText(context, viewHolder.action, R.string.following);
                                            if(isUser) {
                                                userlist.get(i).setFollowing(true);
                                            }
                                            else
                                            {
                                                list.get(i).setFollowing(true);

                                            }
                                        } else if (requestSent) {
                                            setActionButtonText(context, viewHolder.action, R.string.requested);
                                            if(isUser) {
                                                userlist.get(i).setRequestSent(true);
                                            }else{
                                                list.get(i).setRequestSent(true);
                                            }
                                        } else {
                                            setActionButtonText(context, viewHolder.action, R.string.follow);

                                            if(isUser)
                                            {
                                                userlist.get(i).setFollower(true);}
                                            else{
                                                list.get(i).setFollower(true);
                                            }
                                        }
                                    } else {

                                        if (response.body().getFollowInfo().getFollowing()) {
                                            setActionButtonText(context, viewHolder.action, R.string.following);
                                            if (isUser){
                                                userlist.get(i).setFollowing(true);
                                            }else
                                            {
                                                list.get(i).setFollowing(true);

                                            }

                                            Toast.makeText(context, "You also have started following", Toast.LENGTH_LONG).show();
                                        } else if (response.body().getFollowInfo().getRequestSent()) {
                                            setActionButtonText(context, viewHolder.action, R.string.requested);
                                            if (isUser) {
                                                userlist.get(i).setRequestSent(true);

                                            }else{
                                               list.get(i).setRequestSent(true);

                                            }
                                            Toast.makeText(context, "Your request has been sent", Toast.LENGTH_LONG).show();

                                        } else {
                                            setActionButtonText(context, viewHolder.action, R.string.follow);
                                         if(isUser) {
                                             userlist.get(i).setFollower(true);
                                         }
                                         else {
                                             list.get(i).setFollower(true);
                                         }
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Something went wrong, Please try again..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                        //loader.setVisibility(View.GONE);
                    }
                });
    }


    private void followUser(final int userId, final Context context, final ViewHolder viewHolder, final int accounttype, final int i, final boolean isUser) {

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
                                if (isUser) {
                                    userlist.get(i).setRequestSent(true);
                                } else {
                                    list.get(i).setRequestSent(true);
                                }


                            } else {
                                setActionButtonText(context, viewHolder.action, R.string.following);
                                Toast.makeText(context, "You have started following", Toast.LENGTH_LONG).show();
                                if (isUser) {
                                    userlist.get(i).setFollowing(true);
                                } else {
                                    list.get(i).setFollowing(true);
                                }
                            }


                        } else {
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


    private void cancelRequest(final int userId, final Context context, final ViewHolder viewHolder, final int accounttype, final int i, final boolean isUser) {

        ApiCallingService.Friends.cancelRequest(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b) {

                            if (isUser) {
                                userlist.get(i).setRequestSent(false);
                            } else {
                                list.get(i).setRequestSent(false);
                            }
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "Your request has been cancelled", Toast.LENGTH_LONG).show();

                        } else {
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


    public void unFollowUser(int userId, final Context context, final FollowersAdapter.ViewHolder viewHolder, final int accountType, final int i, final boolean isUser)

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
                            if (isUser) {
                                userlist.get(i).setFollowing(false);
                            } else {
                                list.get(i).setFollowing(false);
                            }
                        } else {

                            //_btnfollow.setText("Follow");
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "You have already unfollowed", Toast.LENGTH_LONG).show();
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

    public void blockUnblockUsers(final int userId, final int status, final String username, final FollowersAdapter.ViewHolder viewHolder, final int i) {
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

                blockunBlock(userId, status, viewHolder, i);
                alertDialog.dismiss();


            }
        });
    }


    public void blockunBlock(int userId, final int status, final FollowersAdapter.ViewHolder holder, final int i) {
        ApiCallingService.Friends.blockUnblockUser(userId, status, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    boolean b = response.body().getStatus();
                    if (b == true) {

                        Toast.makeText(context, "You have Unblocked this user", Toast.LENGTH_SHORT).show();
                        setActionButtonText(context, holder.action, R.string.follow);
                        list.get(i).setYouBlocked(false);
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
        if (userfollowerstatus == FollowersListFragment.USERS_FOLLOWER) {
            return userlist.size();
        } else {
            return list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_layout)
        LinearLayout layout;
        @BindView(R.id.dp)
        CircularAppCompatImageView dp;
        @BindView(R.id.name)
        ProximaNovaSemiBoldTextView name;
        @BindView(R.id.action)
        ProximaNovaSemiBoldTextView action;
//        @BindView(R.id.decline) AppCompatImageView declineBtn;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OtherProfileListener {
        void viewOthersProfile(String id, String username, String type);

        void viewUserProfile();
    }
}
