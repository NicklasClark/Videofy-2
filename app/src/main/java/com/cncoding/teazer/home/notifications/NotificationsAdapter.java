package com.cncoding.teazer.home.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileMyReactionAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.TypeFactory;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.customViews.proximanovaviews.UniversalTextView;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.react.ReactVideoDetailsResponse;
import com.cncoding.teazer.model.user.Notification;
import com.cncoding.teazer.model.user.NotificationsList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.cncoding.teazer.MainActivity.PUBLIC_ACCOUNT;
import static com.cncoding.teazer.ui.fragment.fragment.FragmentReactionplayer.OPENED_FROM_OTHER_SOURCE;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.cncoding.teazer.model.user.Notification}
 */
public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOLLOWING = 50;
    private static final int TYPE_REQUESTS = 51;
    public static final int BUTTON_TYPE_UNBLOCK = 9;
    public static final int BUTTON_TYPE_ACCEPT = 10;
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
    ProfileMyReactionAdapter.ReactionPlayerListener reactionPlayerListener;

    NotificationsAdapter(Context context, boolean isFollowingTab, NotificationsList notificationsList) {
        this.context = context;
        this.isFollowingTab = isFollowingTab;
        this.notificationsList = notificationsList;
        if (context instanceof OnNotificationsInteractionListener) {
            mListener = ((OnNotificationsInteractionListener) context);
        }
        if(context instanceof  ProfileMyReactionAdapter.ReactionPlayerListener) {
            reactionPlayerListener = ((ProfileMyReactionAdapter.ReactionPlayerListener) context);
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
                            .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                               DataSource dataSource, boolean isFirstResource) {
                                    holder1.dp.setImageDrawable(resource);
                                    return false;
                                }
                            })
                            .into(holder1.dp);
                else {
                    Glide.with(context)
                            .load(R.drawable.ic_user_male_dp_small)
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                            .into(holder1.dp);
                }

                holder1.content.setText(getString(getHighlights(holder1.notification.getHighlights()), holder1.notification.getMessage()));

                Glide.with(context)
                        .load(holder1.notification.getMetaData().getThumbUrl())
                        .apply(new RequestOptions().placeholder(context.getResources().getDrawable(R.drawable.bg_placeholder, null)))
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
                                                            -1, null);
                                                else if (response.code() == 412 && response.message().contains("Precondition Failed"))
                                                    Toast.makeText(context, "This post no longer exists", Toast.LENGTH_SHORT).show();
                                                else {
                                                    Log.d("FETCHING PostDetails", response.code() + " : " + response.message());
                                                    Toast.makeText(context, "Error fetching post", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<PostDetails> call, Throwable t) {
                                                t.printStackTrace();
                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }

                            else if(holder1.notification.getNotificationType() == REACTED_TO_YOUR_VIDEO ||   holder1.notification.getNotificationType() == LIKED_YOUR_REACTION ||
                                    holder1.notification.getNotificationType() == REACTED_TO_A_VIDEO_THAT_YOU_ARE_TAGGED_IN ) {

                                if(holder1.notification.getMetaData().getPostId()!=0) {
                                    ApiCallingService.React.getReactionDetail2(holder1.notification.getMetaData().getSourceId(), context)
                                            .enqueue(new Callback<ReactVideoDetailsResponse>() {
                                                @Override
                                                public void onResponse(Call<ReactVideoDetailsResponse> call, Response<ReactVideoDetailsResponse> response) {
                                                    if (response.code() == 200) {
                                                        if (response.body() != null) {
                                                            PostReaction postReactDetail = response.body().getPostReactDetail();
                                                            if (postReactDetail != null) {
                                                                reactionPlayerListener.reactionPlayer(OPENED_FROM_OTHER_SOURCE, postReactDetail, null);
                                                            } else {
                                                                Toast.makeText(context, R.string.reaction_no_longer_exists, Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(context, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else
                                                        Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Call<ReactVideoDetailsResponse> call, Throwable t) {
                                                    Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else {
                                        ApiCallingService.Posts.getPostDetails(holder1.notification.getMetaData().getSourceId(), context)
                                                .enqueue(new Callback<PostDetails>() {
                                                    @Override
                                                    public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                                                        if (response.code() == 200)
                                                            mListener.onNotificationsInteraction(isFollowingTab, response.body(),
                                                                    -1, null);
                                                        else if (response.code() == 412 && response.message().contains("Precondition Failed"))
                                                            Toast.makeText(context, "This post no longer exists", Toast.LENGTH_SHORT).show();
                                                        else {
                                                            Log.d("FETCHING PostDetails", response.code() + " : " + response.message());
                                                            Toast.makeText(context, "Error fetching post", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    @Override
                                                    public void onFailure(Call<PostDetails> call, Throwable t) {
                                                        t.printStackTrace();
                                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                            }



//                            else if (holder1.notification.getNotificationType() == LIKED_YOUR_REACTION ||
//                                    holder1.notification.getNotificationType() == REACTED_TO_A_VIDEO_THAT_YOU_ARE_TAGGED_IN) {
//
//                               // Toast.makeText(context, "This post no longer exists", Toast.LENGTH_SHORT).show();
//
//
////                                ApiCallingService.Posts.getPostDetails(holder1.notification.getMetaData().getSourceId(), context)
////                                        .enqueue(new Callback<PostDetails>() {
////
////                                            @Override
////                                            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
////                                                if (response.code() == 200)
////                                                {
////                                                    mListener.onNotificationsInteraction(isFollowingTab, response.body(),
////                                                            -1, null);
////
////                                                }
////
////
////                                                else if (response.code() == 412 && response.message().contains("Precondition Failed"))
////
////                                                    Toast.makeText(context, "This post no longer exists", Toast.LENGTH_SHORT).show();
////                                                else {
////                                                    Log.d("FETCHING PostDetails", response.code() + " : " + response.message());
////                                                    Toast.makeText(context, "Error fetching post", Toast.LENGTH_SHORT).show();
////                                                }
////                                            }
////
////                                            @Override
////                                            public void onFailure(Call<PostDetails> call, Throwable t) {
////                                                t.printStackTrace();
////                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
////                                            }
////                                        });
//                            }
                        }
                    }
                };

                profileListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onNotificationsInteraction(false, null,
                                holder1.notification.getMetaData().getFromId(), "");
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
                            .into(holder2.dp);
                else {
                    Glide.with(context)
                            .load(R.drawable.ic_user_male_dp_small)
                            .into(holder2.dp);
                }
                holder2.content.setText(getString(getHighlights(holder2.notification.getHighlights()), holder2.notification.getMessage()));
                holder2.isActioned = notificationsList.getNotifications().get(position).isActioned();
                holder2.accountType = notificationsList.getNotifications().get(position).getAccountType();

                if (holder2.notification.getNotificationType() == 3 || holder2.notification.getNotificationType() == 1) {

                    if (holder2.notification.isActioned()) {
                        if (holder2.notification.isFollowing()) {
                            holder2.action.setVisibility(View.VISIBLE);
                            setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOWING);
                        }
                        else if (holder2.notification.isRequestSent()) {
                            holder2.action.setVisibility(View.VISIBLE);
                            setActionButton(holder2.action, null, BUTTON_TYPE_REQUESTED);
                        } else {
                            holder2.action.setVisibility(View.VISIBLE);
                            setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOW);
                        }
                    }

                    else {
                        if (holder2.notification.getNotificationType() == 1) {
                            if (holder2.notification.isFollowing()) {
                                holder2.action.setVisibility(View.VISIBLE);
                                setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOWING);
                            }
                            else if (holder2.notification.isRequestSent()) {
                                holder2.action.setVisibility(View.VISIBLE);
                                setActionButton(holder2.action, null, BUTTON_TYPE_REQUESTED);
                            } else {
                                holder2.action.setVisibility(View.VISIBLE);
                                setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOW);
                            }
                        }
                        else if(holder2.notification.getNotificationType() == 3) {
                            setActionButton(holder2.action, null, BUTTON_TYPE_ACCEPT);
                            holder2.action.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else {
                    setActionButton(holder2.action, null, BUTTON_TYPE_NONE);
                    holder2.action.setVisibility(View.GONE);
                }

                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.root_layout:
                                if (mListener != null) {
                                    String userType;

                                    if (holder2.notification.getMetaData().getNotificationType() == 1 ||
                                            holder2.notification.getMetaData().getNotificationType() == 2 ||
                                            holder2.notification.getMetaData().getNotificationType() == 10) {
                                        userType = "Following";

                                    }
                                    else if (holder2.notification.getMetaData().getNotificationType() == 3) {
                                        userType = "Accept";
                                    }
                                    else {
                                        userType = "Accept";
                                    }

                                    mListener.onNotificationsInteraction(false, null,
                                            holder2.notification.getMetaData().getFromId(), userType);
                                }
                                break;
                            case R.id.action:
                                String text = holder2.action.getText().toString();
                                if (text.equals(context.getString(R.string.follow))) {
                                    ApiCallingService.Friends.acceptJoinRequest(holder2.notification.getNotificationId(), context)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    try {
                                                        if (response.code() == 200) {
                                                            if (response.body().getStatus()) {
                                                                if (holder2.notification.getAccountType() == PUBLIC_ACCOUNT){
                                                                    setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOWING);
                                                                    notificationsList.getNotifications().get(position).setFollowing(true);

                                                                }

                                                                else {
                                                                    setActionButton(holder2.action, null, BUTTON_TYPE_REQUESTED);
                                                                    holder2.declineRequest.setVisibility(View.GONE);
                                                                    holder2.notification.setIs_actioned(true);
                                                                    holder2.notification.setRequest_sent(true);
                                                                }

                                                            } else {

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
                                                    t.printStackTrace();
                                                }

                                            });

                                }
                                else if (text.equals(context.getString(R.string.accept))) {
//                                    USER IS ACCEPTING A FOLLOW REQUEST
                                    ApiCallingService.Friends.acceptJoinRequest(holder2.notification.getNotificationId(), context)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (response.code() == 200) {
                                                        if (response.body().getFollowInfo().getFollowing()) {
                                                            setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOWING);
                                                            holder2.notification.setIs_actioned(true);
                                                            holder2.notification.setFollowing(true);
                                                        } else if (holder2.notification.isRequestSent()) {
                                                            setActionButton(holder2.action, null, BUTTON_TYPE_REQUESTED);
                                                            holder2.notification.setIs_actioned(true);
                                                            holder2.notification.setRequest_sent(true);
                                                        } else {
                                                            setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOW);
                                                            holder2.notification.setIs_actioned(true);
                                                            holder2.notification.setFollowing(false);
                                                            holder2.notification.setRequest_sent(false);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    t.printStackTrace();
                                                }
                                            });
                                } else if (text.equals(context.getString(R.string.following))) {

                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                    //dialogBuilder.setTitle("Confirmation");
                                    dialogBuilder.setMessage("Are you sure you want to Unfollow "+ holder2.notification.getHighlights().get(0) + "?");
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

                                            ApiCallingService.Friends.unfollowUser(holder2.notification.getMetaData().getSourceId(),
                                                    context).enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (response.code() == 200) {
                                                        if (response.body().getStatus()) {
                                                            setActionButton(holder2.action, null,
                                                                    BUTTON_TYPE_FOLLOW);
                                                            holder2.notification.setIs_actioned(true);
                                                            holder2.notification.setFollowing(false);
                                                            holder2.notification.setRequest_sent(false);
                                                            alertDialog.dismiss();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    t.printStackTrace();
                                                }
                                            });
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

                                else if (text.equals(context.getString(R.string.requested))) {
                                    new AlertDialog.Builder(context)
                                            .setMessage(context.getString(R.string.cancel_request_confirmation) +
                                                    holder2.notification.getHighlights().get(0) + "?")
                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ApiCallingService.Friends.cancelRequest(holder2.notification.getMetaData().getSourceId(),
                                                            context).enqueue(new Callback<ResultObject>() {
                                                        @Override
                                                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                            if (response.code() == 200) {
                                                                if (response.body().getStatus()) {
                                                                    setActionButton(holder2.action, null,
                                                                            BUTTON_TYPE_FOLLOW);
                                                                }
                                                                else
                                                                    Log.d("CancelRequest", response.code()
                                                                            + " : " + response.body().getMessage());
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResultObject> call, Throwable t) {
                                                            t.printStackTrace();
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
                                break;

                            case R.id.decline:
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
                                                }
//                                                else {
//                                                }
//                                                    Log.d("DeleteJoinRequest", response.code()
//                                                            + " : " + response.message());
                                            }

                                            @Override
                                            public void onFailure(Call<ResultObject> call, Throwable t) {
                                                t.printStackTrace();
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
                                if (holder.notification.getAccountType() == PUBLIC_ACCOUNT) {
                                    setActionButton(holder.action, null, BUTTON_TYPE_FOLLOWING);
                                    holder.notification.setIs_actioned(true);
                                    holder.notification.setFollowing(true);
                                }

                                else{
                                    setActionButton(holder.action, null, BUTTON_TYPE_REQUESTED);
                                    holder.notification.setRequest_sent(true);
                                    holder.notification.setIs_actioned(true);

                                }
                            }
                        } else
                            Log.d("SendJoinRequestByUserId", response.code()
                                    + " : " + response.body().getMessage());
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
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

    private void setActionButton(ProximaNovaSemiBoldTextView button, AppCompatImageView declineRequest, int type) {
        switch (type) {
            case BUTTON_TYPE_ACCEPT:
                button.setText(R.string.accept);
                button.setTextColor(context.getResources().getColor(R.color.colorAccent));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_primary);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if (declineRequest != null)
                    declineRequest.setVisibility(View.VISIBLE);
                break;
            case BUTTON_TYPE_FOLLOW:
                button.setText(R.string.follow);
                button.setTextColor(context.getResources().getColor(R.color.colorAccent));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_primary);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if (declineRequest != null)
                    declineRequest.setVisibility(View.GONE);
                break;
            case BUTTON_TYPE_FOLLOWING:
                button.setText(R.string.following);
                button.setTextColor(Color.parseColor("#333333"));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_black);
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_dark, 0, 0, 0);
                if (declineRequest != null)
                    declineRequest.setVisibility(View.GONE);
                break;
            case BUTTON_TYPE_REQUESTED:
                button.setText(R.string.requested);
                button.setTextColor(Color.parseColor("#333333"));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_black);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                button.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_check_dark),
//                        null, null, null);
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

    private void followUser(int userId, final Context context, final RequestsViewHolder holder2) {
        ApiCallingService.Friends.followUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        if (response.body().getStatus()) {
                            if (holder2.notification.getAccountType() == PUBLIC_ACCOUNT)
                                setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOWING);
                            else
                                setActionButton(holder2.action, null, BUTTON_TYPE_REQUESTED);
                            holder2.declineRequest.setVisibility(View.GONE);
                        } else {
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
                t.printStackTrace();
                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.getNotifications().size();
    }

    public class FollowingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.name) UniversalTextView content;
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
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.name) UniversalTextView content;
        @BindView(R.id.action) ProximaNovaSemiBoldTextView action;
        @BindView(R.id.decline) AppCompatImageView declineRequest;
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
        void onNotificationsInteraction(boolean isFollowingTab, PostDetails postDetails,
                                        int profileId, String userType);
    }
}