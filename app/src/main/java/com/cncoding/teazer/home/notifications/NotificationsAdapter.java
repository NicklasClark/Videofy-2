package com.cncoding.teazer.home.notifications;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.customViews.TypeFactory;
import com.cncoding.teazer.customViews.UniversalTextView;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.User.Notification;
import com.cncoding.teazer.utilities.Pojos.User.NotificationsList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.cncoding.teazer.utilities.Pojos.ACCOUNT_TYPE_PUBLIC;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification}
 */
public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOLLOWING = 50;
    private static final int TYPE_REQUESTS = 51;
    private static final int BUTTON_TYPE_ACCEPT = 10;
    public static final int BUTTON_TYPE_FOLLOW = 11;
    public static final int BUTTON_TYPE_FOLLOWING = 12;
    public static final int BUTTON_TYPE_REQUESTED = 13;
    private static final int BUTTON_TYPE_NONE = 14;

    private static final int STARTED_FOLLOWING = 1;
    private static final int ACCEPTED_REQUEST = 2;
    private static final int SENT_YOU_A_FOLLOW_REQUEST = 3;
    private static final int ALSO_STARTED_FOLLOWING = 10;

    private static final int LIKED_YOUR_VIDEO = 5;
    private static final int POSTED_A_VIDEO = 7;
    private static final int TAGGED_YOU_IN_A_VIDEO = 9;

    private static final int REACTED_TO_YOUR_VIDEO = 4;
    private static final int LIKED_YOUR_REACTION = 6;
    private static final int REACTED_TO_A_VIDEO_THAT_YOU_ARE_TAGGED_IN = 8;

    private Context context;
    private boolean isFollowingTab;
    private final NotificationsList notificationsList;
    private OnNotificationsInteractionListener mListener;

    NotificationsAdapter(Context context, boolean isFollowingTab, NotificationsList notificationsList) {
        this.context = context;
        this.isFollowingTab = isFollowingTab;
        this.notificationsList = notificationsList;
        if (context instanceof OnNotificationsInteractionListener) {
            mListener = ((OnNotificationsInteractionListener) context);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_FOLLOWING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications_following, parent, false);
                return new FollowingViewHolder(view);
            case TYPE_REQUESTS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications_request, parent, false);
                return new RequestsViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isFollowingTab ? TYPE_FOLLOWING : TYPE_REQUESTS;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        View.OnClickListener profileListener;

        switch (viewHolder.getItemViewType()) {
            case TYPE_FOLLOWING:
                final FollowingViewHolder holder1 = (FollowingViewHolder) viewHolder;
                holder1.notification = notificationsList.getNotifications().get(position);

                if (holder1.notification.hasProfileMedia())
                    Glide.with(context)
                            .load(holder1.notification.getProfileMedia().getThumbUrl())
                            .placeholder(R.drawable.ic_user_male_dp_small)
                            .crossFade()
                            .into(holder1.dp);

                holder1.content.setText(getString(getHighlights(holder1.notification.getHighlights()), holder1.notification.getMessage()));


//                holder1.dp.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        mListener.onNotificationsInteraction(false, null,
//                                null, holder1.notification.getMetaData().getFromId(),"");
//                    }
//                });

                Glide.with(context)
                        .load(holder1.notification.getMetaData().getThumbUrl())
                        .placeholder(context.getResources().getDrawable(R.drawable.bg_placeholder, null))
                        .crossFade()
                        .into(holder1.thumbnail);

                View.OnClickListener postListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            if (holder1.notification.getNotificationType() == LIKED_YOUR_VIDEO ||
                                    holder1.notification.getNotificationType() == POSTED_A_VIDEO ||
                                    holder1.notification.getNotificationType() == TAGGED_YOU_IN_A_VIDEO) {
                                ApiCallingService.Posts.getPostDetails(holder1.notification.getMetaData().getSourceId(), context)
                                        .enqueue(new Callback<PostDetails>() {
                                            @Override
                                            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                                                if (response.code() == 200)
                                                    mListener.onNotificationsInteraction(isFollowingTab, response.body(),
                                                            null, -1, null);
                                                else if (response.code() == 412 && response.message().contains("Precondition Failed"))
                                                    Toast.makeText(context, "This post no longer exists", Toast.LENGTH_SHORT).show();
                                                else {
                                                    Log.d("FETCHING PostDetails", response.code() + " : " + response.message());
                                                    Toast.makeText(context, "This post no longer exists", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<PostDetails> call, Throwable t) {
                                                Log.d("FAIL - GET PostDetails", t.getMessage());
                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else if (holder1.notification.getNotificationType() == REACTED_TO_YOUR_VIDEO ||
                                    holder1.notification.getNotificationType() == LIKED_YOUR_REACTION ||
                                    holder1.notification.getNotificationType() == REACTED_TO_A_VIDEO_THAT_YOU_ARE_TAGGED_IN) {
                                ApiCallingService.Posts.getPostDetails(holder1.notification.getMetaData().getPostId(), context)
                                        .enqueue(new Callback<PostDetails>() {

                                            @Override
                                            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                                                if (response.code() == 200)
                                                    mListener.onNotificationsInteraction(isFollowingTab, response.body(),
                                                            null, -1, null);
                                                else if (response.code() == 412 && response.message().contains("Precondition Failed"))
                                                    Toast.makeText(context, "This post no longer exists", Toast.LENGTH_SHORT).show();
                                                else {
                                                    Log.d("FETCHING PostDetails", response.code() + " : " + response.message());
                                                    Toast.makeText(context, "This post no longer exists", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<PostDetails> call, Throwable t) {
                                                Log.d("FAIL - GET PostDetails", t.getMessage());
                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    }
                };

                profileListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        mListener.onNotificationsInteraction(false, null,
                                null, holder1.notification.getMetaData().getFromId(), "");
                    }
                };

                holder1.dp.setOnClickListener(profileListener);
                holder1.content.setOnClickListener(postListener);
                holder1.thumbnail.setOnClickListener(postListener);
                break;
            case TYPE_REQUESTS:
                final RequestsViewHolder holder2 = (RequestsViewHolder) viewHolder;
                holder2.notification = notificationsList.getNotifications().get(position);

                if (holder2.notification.hasProfileMedia())
                    Glide.with(context)
                            .load(holder2.notification.getProfileMedia().getThumbUrl())
                            .placeholder(R.drawable.ic_user_male_dp_small)
                            .crossFade()
                            .into(holder2.dp);

                holder2.content.setText(getString(getHighlights(holder2.notification.getHighlights()), holder2.notification.getMessage()));

                holder2.isActioned=notificationsList.getNotifications().get(position).isActioned();
                holder2.accountType=notificationsList.getNotifications().get(position).getAccountType();

             //   if(!holder2.isActioned) {
//                    switch (holder2.notification.getNotificationType()) {

//
//
//                            case STARTED_FOLLOWING:
//                                //setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOW);
//                                holder2.action.setText("Following");
//                                break;
//                            case ACCEPTED_REQUEST:
//                                setActionButton(holder2.action, null, BUTTON_TYPE_NONE);
//                                holder2.action.setText("Requested");
//                                break;
//                            case SENT_YOU_A_FOLLOW_REQUEST:
//                                setActionButton(holder2.action, holder2.declineRequest, BUTTON_TYPE_ACCEPT);
//                                holder2.action.setText("Follow");
//                                break;
//                            case ALSO_STARTED_FOLLOWING:
//                                setActionButton(holder2.action, null, BUTTON_TYPE_NONE);
//                                holder2.action.setText("Following");
//                                break;
//                            default:
//                                break;
//                        }
//
//                    }



                if(holder2.notification.getNotificationType()==3||holder2.notification.getNotificationType()==1)
                    {

                        if(holder2.notification.isActioned() ==true)
                        {

                            if(holder2.notification.isFollowing())
                            {
                                holder2.action.setVisibility(View.VISIBLE);
                                setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOWING);
                            }
                            else if (holder2.notification.isRequest_sent()==true)

                            {
                                holder2.action.setVisibility(View.VISIBLE);

                                setActionButton(holder2.action, null, BUTTON_TYPE_REQUESTED);
                            }
                            else
                            {
                                holder2.action.setVisibility(View.VISIBLE);

                                setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOW);
                            }
                        }
                        else
                            {
                                holder2.action.setVisibility(View.VISIBLE);

                                if(holder2.notification.getNotificationType()==1)setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOW);
                                else setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOW);
                            }
                    }
                    else
                    {
                        setActionButton(holder2.action, null, BUTTON_TYPE_NONE);
                    }


                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.root_layout:
                                if (mListener != null) {
                                    String userType;

                                    if (holder2.notification.getMetaData().getNotificationType() == 1 || holder2.notification.getMetaData().getNotificationType() == 2 || holder2.notification.getMetaData().getNotificationType() == 10) {
                                        userType = "Following";
                                    }
                                    if (holder2.notification.getMetaData().getNotificationType() == 3) {
                                        userType = "Accept";
                                    } else {
                                        userType = "Accept";
                                    }

                                    mListener.onNotificationsInteraction(false, null,
                                            null, holder2.notification.getMetaData().getFromId(), userType);
                                }
                                break;
                            case R.id.notification_action:
                                String text = holder2.action.getText().toString();
                                if (text.equals(context.getString(R.string.follow))) {
//                                    USER IS FOLLOWING ANOTHER USER

                                    followUser( holder2.notification.getMetaData().getFromId(), context,holder2);

                                    ApiCallingService.Friends.acceptJoinRequest(holder2.notification.getNotificationId(), context).enqueue(new Callback<ResultObject>() {
                                        @Override
                                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                            try {
                                                if (response.code() == 200) {
                                                    if (response.body().getStatus()) {
                                                        if (holder2.notification.getAccountType() == ACCOUNT_TYPE_PUBLIC)
                                                            setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOWING);
                                                        else
                                                            setActionButton(holder2.action, null, BUTTON_TYPE_REQUESTED);
                                                        holder2.declineRequest.setVisibility(View.GONE);
                                                    }

                                                    else {
                                                        sendJoinRequest(holder2);
                                                    }
                                                } else
                                                    Log.d("FOLLOW BACK", response.code() + " : " + response.message());
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResultObject> call, Throwable t) {
                                            Log.d("FAIL - FOLLOW BACK", t.getMessage());
                                        }

                                    });

                                } else if (text.equals(context.getString(R.string.accept))) {
//                                    USER IS ACCEPTING A FOLLOW REQUEST
                                    ApiCallingService.Friends.acceptJoinRequest(holder2.notification.getNotificationId(), context)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (response.code() == 200) {
                                                        if (response.body().getStatus())
                                                            setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOW);
                                                        else {
//                                                            Log.d("AcceptJoinRequest", response.code()
//                                                                    + " : " + response.body().getMessage());
                                                        }
                                                    } else {
//                                                        Log.d("AcceptJoinRequest", response.code()
//                                                                + " : " + response.body().getMessage());
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
//                                                    Log.d("FAIL- AcceptJoinRequest", t.getMessage());
                                                }
                                            });
                                } else if (text.equals(context.getString(R.string.following))) {
                                    new AlertDialog.Builder(context)
                                            .setMessage(context.getString(R.string.unfollow_confirmation) +
                                                    holder2.notification.getHighlights().get(0) + "?")
                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ApiCallingService.Friends.unfollowUser(holder2.notification.getMetaData().getSourceId(),
                                                            context).enqueue(new Callback<ResultObject>() {
                                                        @Override
                                                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                            if (response.code() == 200) {
                                                                if (response.body().getStatus()) {
                                                                    setActionButton(holder2.action, null,
                                                                            BUTTON_TYPE_FOLLOW);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResultObject> call, Throwable t) {
                                                            Log.d("FAIL - UnfollowUser", t.getMessage());
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .show();
                                }
//                                else if (text.equals(context.getString(R.string.requested))) {
//                                    new AlertDialog.Builder(context)
//                                            .setMessage(context.getString(R.string.cancel_request_confirmation) +
//                                                    holder2.notification.getHighlights().get(0) + "?")
//                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    ApiCallingService.Friends.unfollowUser(holder2.notification.getMetaData().getSourceId(),
//                                                            context).enqueue(new Callback<ResultObject>() {
//                                                        @Override
//                                                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
//                                                            if (response.code() == 200) {
//                                                                if (response.body().getStatus()) {
//                                                                    setActionButton(holder2.action, null,
//                                                                            BUTTON_TYPE_FOLLOW);
//                                                                }
//                                                                else
//                                                                    Log.d("CancelRequest", response.code()
//                                                                            + " : " + response.body().getMessage());
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onFailure(Call<ResultObject> call, Throwable t) {
//                                                            Log.d("FAIL - CancelRequest", t.getMessage());
//                                                        }
//                                                    });
//                                                }
//                                            })
//                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    dialogInterface.dismiss();
//                                                }
//                                            })
//                                            .show();
//                                }
                                 break;

                            case R.id.notification_decline:
                                ApiCallingService.Friends.deleteJoinRequest(holder2.notification.getNotificationId(), context)
                                        .enqueue(new Callback<ResultObject>() {
                                            @Override
                                            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                if (response.code() == 200) {
                                                    if (response.body().getStatus()) {
                                                        new AlertDialog.Builder(context)
                                                                .setTitle(R.string.confirm)
                                                                .setMessage(R.string.delete_request_confirmation)
                                                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        notificationsList.getNotifications()
                                                                                .remove(holder2.getAdapterPosition());
                                                                        notifyDataSetChanged();
                                                                        dialogInterface.dismiss();
                                                                    }
                                                                })
                                                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        dialogInterface.dismiss();
                                                                    }
                                                                })
                                                                .show();
                                                    } else
                                                        Log.d("DeleteJoinRequest", response.code()
                                                                + " : " + response.body().getMessage());
                                                } else {
                                                }
//                                                    Log.d("DeleteJoinRequest", response.code()
//                                                            + " : " + response.message());
                                            }

                                            @Override
                                            public void onFailure(Call<ResultObject> call, Throwable t) {
                                                Log.d("FAIL- DeleteJoinRequest", t.getMessage());
                                            }
                                        });
                                break;
                            default:
                                break;
                        }
                    }
                };

                holder2.layout.setOnClickListener(onClickListener);
                holder2.action.setOnClickListener(onClickListener);
                if (holder2.declineRequest.getVisibility() != View.GONE)
                    holder2.declineRequest.setOnClickListener(onClickListener);
                break;
            default:
                break;
        }
    }

    private void sendJoinRequest(final RequestsViewHolder holder) {
        ApiCallingService.Friends.sendJoinRequestByUserId(holder.notification.getMetaData().getSourceId(), context)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        if (response.code() == 200) {
                            if (response.body().getStatus()) {
                                if (holder.notification.getAccountType() == ACCOUNT_TYPE_PUBLIC)
                                    setActionButton(holder.action, null, BUTTON_TYPE_FOLLOWING);
                                else
                                    setActionButton(holder.action, null, BUTTON_TYPE_REQUESTED);
                            }
                        } else
                            Log.d("SendJoinRequestByUserId", response.code()
                                    + " : " + response.body().getMessage());
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        Log.d("FAIL-SendJoinRqstUserId", t.getMessage());
                    }
                });
    }

    private SpannableString getString(String boldText, String normalText) {
        normalText = normalText.replace(boldText, "");
        SpannableString string = new SpannableString(boldText + normalText);
        string.setSpan(new StyleSpan(new TypeFactory(context).bold.getStyle()), 0, boldText.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    private String getHighlights(ArrayList<String> highlights) {
        return TextUtils.join(", ", highlights);
    }

    private void setActionButton(ProximaNovaSemiboldButton button, AppCompatImageView declineRequest, int type) {
        switch (type) {
            case BUTTON_TYPE_ACCEPT:
                button.setText(R.string.accept);
                button.setTextColor(Color.parseColor("#546E7A"));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_primary);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if (declineRequest != null)
                    declineRequest.setVisibility(View.VISIBLE);
                break;
            case BUTTON_TYPE_FOLLOW:
                button.setText(R.string.follow);
                button.setTextColor(Color.parseColor("#546E7A"));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_primary);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if (declineRequest != null)
                    declineRequest.setVisibility(View.GONE);
                break;
            case BUTTON_TYPE_FOLLOWING:
                button.setText(R.string.following);
                button.setTextColor(Color.parseColor("#333333"));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_black);
                button.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_check_small),
                        null, null, null);
                if (declineRequest != null)
                    declineRequest.setVisibility(View.GONE);
                break;
            case BUTTON_TYPE_REQUESTED:
                button.setText(R.string.requested);
                button.setTextColor(Color.parseColor("#333333"));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_black);
                button.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_check_small),
                        null, null, null);
                if (declineRequest != null)
                    declineRequest.setVisibility(View.GONE);
                break;
            case BUTTON_TYPE_NONE:
                button.setVisibility(View.GONE);
                if (declineRequest != null)
                    declineRequest.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }



    public void followUser(int userId, final Context context,final RequestsViewHolder holder2) {
        ApiCallingService.Friends.followUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (response.body().getStatus()) {

                            if (holder2.notification.getAccountType() == ACCOUNT_TYPE_PUBLIC)
                                setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOWING);
                            else
                                setActionButton(holder2.action, null, BUTTON_TYPE_REQUESTED);
                            holder2.declineRequest.setVisibility(View.GONE);
                        }
                        else {

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
        return notificationsList.getNotifications().size();
    }

    public class FollowingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.notification_dp) CircularAppCompatImageView dp;
        @BindView(R.id.notification_content) UniversalTextView content;
        @BindView(R.id.notification_thumb) ImageView thumbnail;
        Notification notification;

        FollowingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + content.getText() + "'";
        }
    }

    public class RequestsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.notification_dp) CircularAppCompatImageView dp;
        @BindView(R.id.notification_content) UniversalTextView content;
        @BindView(R.id.notification_action) ProximaNovaSemiboldButton action;
        @BindView(R.id.notification_decline) AppCompatImageView declineRequest;
        Notification notification;
        boolean isActioned;
        int accountType;

        RequestsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + content.getText() + "'";
        }
    }

    public interface OnNotificationsInteractionListener {
        void onNotificationsInteraction(boolean isFollowingTab, PostDetails postDetails, byte[] byteArrayFromImage, int profileId, String userType);
    }
}