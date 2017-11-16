package com.cncoding.teazer.home.notifications;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
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
import com.cncoding.teazer.home.notifications.FollowingNotificationsTabFragment.OnListFragmentInteractionListener;
import com.cncoding.teazer.utilities.PlaceHolderDrawableHelper;
import com.cncoding.teazer.utilities.Pojos.User.Notification;
import com.cncoding.teazer.utilities.Pojos.User.NotificationsList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_FOLLOWING = 0;
    private static final int TYPE_REQUESTS = 1;
    private static final int BUTTON_TYPE_ACCEPT = 10;
    private static final int BUTTON_TYPE_FOLLOW = 11;
    private static final int BUTTON_TYPE_FOLLOWING = 12;
    private static final int BUTTON_TYPE_NONE = 13;

    private static final int STARTED_FOLLOWING = 1;
    private static final int ACCEPTED_REQUEST = 2;
    private static final int SENT_YOU_A_FOLLOW_REQUEST = 3;
//    private static final int REACTED_TO_YOUR_VIDEO = 4;
//    private static final int LIKED_YOUR_VIDEO = 5;
//    private static final int LIKED_YOUR_REACTION = 6;
//    private static final int POSTED_A_VIDEO = 7;
//    private static final int REACTED_TO_A_VIDEO_THAT_YOU_ARE_TAGGED_IN = 8;
//    private static final int TAGGED_YOU_IN_A_VIDEO = 9;
    private static final int ALSO_STARTED_FOLLOWING = 10;

    private Context context;
    private boolean isFollowingTab;
    private final NotificationsList notificationsList;
    private OnNotificationsInteractionListener mListener;

    NotificationsAdapter(Context context, boolean isFollowingTab, NotificationsList notificationsList) {
        this.context = context;
        this.isFollowingTab = isFollowingTab;
        this.notificationsList = notificationsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_FOLLOWING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_following_notifications, parent, false);
                return new FollowingViewHolder(view);
            case TYPE_REQUESTS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_notifications, parent, false);
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TYPE_FOLLOWING:
                final FollowingViewHolder holder1 = (FollowingViewHolder) viewHolder;
                holder1.notification = notificationsList.getNotifications().get(position);

                if (holder1.notification.hasProfileMedia())
                    Glide.with(context)
                            .load(holder1.notification.getProfileMedia().getThumbUrl())
                            .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                            .crossFade()
                            .into(holder1.dp);

                holder1.content.setText(getString(getHighlights(holder1.notification.getHighlights()), holder1.notification.getMessage()));

                Glide.with(context)
                        .load(holder1.notification.getMetaData().getThumbUrl())
                        .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                        .crossFade()
                        .into(holder1.thumbnail);

                holder1.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.onNotificationsInteraction(isFollowingTab, holder1.notification);
                        }
                    }
                });
                break;
            case TYPE_REQUESTS:
                final RequestsViewHolder holder2 = (RequestsViewHolder) viewHolder;
                holder2.notification = notificationsList.getNotifications().get(position);

                if (holder2.notification.hasProfileMedia())
                    Glide.with(context)
                            .load(holder2.notification.getProfileMedia().getThumbUrl())
                            .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                            .crossFade()
                            .into(holder2.dp);

                holder2.content.setText(getString(getHighlights(holder2.notification.getHighlights()), holder2.notification.getMessage()));

                switch (holder2.notification.getNotificationType()) {
                    case STARTED_FOLLOWING:
                        setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOW);
                        break;
                    case ACCEPTED_REQUEST:
                        setActionButton(holder2.action, null, BUTTON_TYPE_NONE);
                        break;
                    case SENT_YOU_A_FOLLOW_REQUEST:
                        setActionButton(holder2.action, holder2.declineRequest, BUTTON_TYPE_ACCEPT);
                        break;
                    case ALSO_STARTED_FOLLOWING:
                        setActionButton(holder2.action, null, BUTTON_TYPE_NONE);
                        break;
                    default:
                        break;
                }

                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.root_layout:
                                if (mListener != null) {
                                    mListener.onNotificationsInteraction(isFollowingTab, holder2.notification);
                                }
                                break;
                            case R.id.notification_action:
                                String text = holder2.action.getText().toString();
                                if (text.equals(context.getString(R.string.follow))) {
//                                    USER IS FOLLOWING ANOTHER USER
                                    ApiCallingService.Friends.acceptJoinRequest(holder2.notification.getNotificationId(), context)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (response.code() == 200) {
                                                        if (response.body().getStatus()) {
                                                            setActionButton(holder2.action, null, BUTTON_TYPE_FOLLOWING);
                                                        } else
                                                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    } else
                                                        Toast.makeText(context, response.code() + " : " + response.body().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                                        else
                                                            Toast.makeText(context, "Failed: false status!", Toast.LENGTH_SHORT).show();
                                                    } else
                                                        Toast.makeText(context, response.code() + " : " + response.body().getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else if (text.equals(context.getString(R.string.following))) {
                                    new AlertDialog.Builder(context)
                                            .setMessage(context.getString(R.string.unfollow_confirmation) +
                                                    holder2.notification.getHighlights().get(0) + "?")
                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ApiCallingService.Friends.unfollowUser(holder2.notification.getMetaData().getFromId(),
                                                            context).enqueue(new Callback<ResultObject>() {
                                                        @Override
                                                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {

                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResultObject> call, Throwable t) {

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
                                                    }
                                                    else
                                                        Toast.makeText(context, "Failed: false status!", Toast.LENGTH_SHORT).show();
                                                } else
                                                    Toast.makeText(context, response.code() + " : " + response.body().getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<ResultObject> call, Throwable t) {
                                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                button.setBackgroundResource(R.drawable.bg_outline_rounded_primary);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                declineRequest.setVisibility(View.VISIBLE);
                break;
            case BUTTON_TYPE_FOLLOW:
                button.setText(R.string.follow);
                button.setBackgroundResource(R.drawable.bg_outline_rounded_primary);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case BUTTON_TYPE_FOLLOWING:
                button.setText(R.string.following);
                button.setBackgroundResource(R.drawable.bg_outline_rounded_primary);
                button.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_check_small),
                        null, null, null);
                break;
            case BUTTON_TYPE_NONE:
                button.setVisibility(View.GONE);
                break;
            default:
                break;
        }
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

        RequestsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + content.getText() + "'";
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (context instanceof OnNotificationsInteractionListener) {
            mListener = ((OnNotificationsInteractionListener) context);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mListener = null;
    }

    interface OnNotificationsInteractionListener {
        void onNotificationsInteraction(boolean isFollowing, Notification notification);
    }
}
