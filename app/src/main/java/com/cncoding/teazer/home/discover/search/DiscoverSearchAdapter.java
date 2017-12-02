package com.cncoding.teazer.home.discover.search;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.utilities.Pojos.Discover.Videos;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;
import com.cncoding.teazer.utilities.Pojos.User.Notification;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.home.notifications.NotificationsAdapter.BUTTON_TYPE_FOLLOW;
import static com.cncoding.teazer.home.notifications.NotificationsAdapter.BUTTON_TYPE_FOLLOWING;
import static com.cncoding.teazer.home.notifications.NotificationsAdapter.BUTTON_TYPE_REQUESTED;
import static com.cncoding.teazer.utilities.Pojos.ACCOUNT_TYPE_PRIVATE;
import static com.cncoding.teazer.utilities.Pojos.ACCOUNT_TYPE_PUBLIC;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Notification}
 */
public class DiscoverSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_VIDEOS = 0;
    private static final int TYPE_PEOPLE = 1;

    private Context context;
    private boolean isVideosTab;
    private ArrayList<Videos> videosList;
    private ArrayList<MiniProfile> usersList;
    private OnDiscoverSearchInteractionListener mListener;

    DiscoverSearchAdapter(Context context, boolean isVideosTab, ArrayList<MiniProfile> usersList, ArrayList<Videos> videosList) {
        this.context = context;
        this.isVideosTab = isVideosTab;
        if (isVideosTab)
            this.videosList = videosList;
        else
            this.usersList = usersList;

        if (context instanceof OnDiscoverSearchInteractionListener)
            mListener = (OnDiscoverSearchInteractionListener) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_VIDEOS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_videos, parent, false);
                return new VideosViewHolder(view);
            case TYPE_PEOPLE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_people, parent, false);
                return new PeopleViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return isVideosTab ? TYPE_VIDEOS : TYPE_PEOPLE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TYPE_VIDEOS:
                final VideosViewHolder holder1 = (VideosViewHolder) viewHolder;
                holder1.video = videosList.get(position);
                holder1.content.setText(holder1.video.getTitle());

                Glide.with(context)
                        .load(holder1.video.getPostVideoInfo().getThumbUrl())
                        .placeholder(R.drawable.bg_placeholder)
                        .crossFade()
                        .into(holder1.thumbnail);

                holder1.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null)
                            mListener.onDiscoverSearchInteraction(isVideosTab, holder1.video.getPostId());
                    }
                });
                break;
            case TYPE_PEOPLE:
                final PeopleViewHolder holder2 = (PeopleViewHolder) viewHolder;
                holder2.user = usersList.get(position);

                holder2.username.setText(holder2.user.getUserName());
                String name = holder2.user.getFirstName() + BLANK_SPACE + holder2.user.getLastName();
                holder2.name.setText(name);

                if (holder2.user.hasProfileMedia() && holder2.user.getProfileMedia() != null) {
                    Glide.with(context)
                            .load(holder2.user.getProfileMedia().getThumbUrl())
                            .placeholder(R.drawable.ic_user_male_dp_small)
                            .crossFade()
                            .into(holder2.dp);
                }

                switch (holder2.user.getAccountType()) {
                    case ACCOUNT_TYPE_PRIVATE:
                        if (holder2.user.isFollowing())
                            setActionButton(holder2.action, BUTTON_TYPE_FOLLOWING);
                        else if (holder2.user.isRequestSent())
                            setActionButton(holder2.action, BUTTON_TYPE_REQUESTED);
                        else
                            setActionButton(holder2.action, BUTTON_TYPE_FOLLOW);
                        break;
                    case ACCOUNT_TYPE_PUBLIC:
                        if (holder2.user.isFollowing())
                            setActionButton(holder2.action, BUTTON_TYPE_FOLLOWING);
                        else
                            setActionButton(holder2.action, BUTTON_TYPE_FOLLOW);
                        break;
                    default:
                        break;
                }

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (view.getId()) {
                            case R.id.root_layout:
                                if (mListener != null)
                                    mListener.onDiscoverSearchInteraction(isVideosTab, holder2.user.getUserId());
                                break;
                            case R.id.action:
                                if (holder2.action.getText().equals(context.getString(R.string.follow))) {
                                    ApiCallingService.Friends.sendJoinRequestByUserId(holder2.user.getUserId(), context)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (response.code() == 200) {
                                                        switch (holder2.user.getAccountType()) {
                                                            case ACCOUNT_TYPE_PRIVATE:
                                                                setActionButton(holder2.action, BUTTON_TYPE_REQUESTED);
                                                                break;
                                                            case ACCOUNT_TYPE_PUBLIC:
                                                                setActionButton(holder2.action, BUTTON_TYPE_FOLLOWING);
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                    } else
                                                        Log.e("sendJoinRequest", response.code() + "_" + response.message());
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    if (t.getMessage() != null)
                                                        Log.e("sendJoinRequest", t.getMessage());
                                                }
                                            });
                                }
                                else if (holder2.action.getText().equals(context.getString(R.string.following))) {
                                    new AlertDialog.Builder(context)
                                            .setMessage(context.getString(R.string.unfollow_confirmation) +
                                                    holder2.user.getUserName() + "?")
                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ApiCallingService.Friends.unfollowUser(holder2.user.getUserId(), context)
                                                            .enqueue(new Callback<ResultObject>() {
                                                                @Override
                                                                public void onResponse(Call<ResultObject> call,
                                                                                       Response<ResultObject> response) {
                                                                    if (response.code() == 200) {
                                                                        setActionButton(holder2.action, BUTTON_TYPE_FOLLOW);
                                                                    } else
                                                                        Log.e("unfollowUser",
                                                                                response.code() + "_" + response.message());
                                                                }

                                                                @Override
                                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                                    if (t.getMessage() != null)
                                                                        Log.e("sendJoinRequest", t.getMessage());
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
                        }
                    }
                };

                holder2.layout.setOnClickListener(listener);
                holder2.action.setOnClickListener(listener);
                break;
            default:
                break;
        }
    }

    private void setActionButton(ProximaNovaSemiboldTextView button, int type) {
        switch (type) {
//            case BUTTON_TYPE_ACCEPT:
//                button.setText(R.string.accept);
//                button.setTextColor(Color.parseColor("#546E7A"));
//                button.setBackgroundResource(R.drawable.bg_outline_rounded_primary);
//                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                break;
            case BUTTON_TYPE_FOLLOW:
                button.setText(R.string.follow);
                button.setTextColor(Color.parseColor("#546E7A"));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_primary);
                button.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case BUTTON_TYPE_FOLLOWING:
                button.setText(R.string.following);
                button.setTextColor(Color.parseColor("#333333"));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_black);
                button.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_check_small),
                        null, null, null);
                break;
            case BUTTON_TYPE_REQUESTED:
                button.setText(R.string.requested);
                button.setTextColor(Color.parseColor("#333333"));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_black);
                button.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_check_small),
                        null, null, null);
                break;
//            case BUTTON_TYPE_NONE:
//                button.setVisibility(View.GONE);
//                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return videosList != null ? videosList.size() : usersList.size();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.content) ProximaNovaRegularTextView content;
        @BindView(R.id.thumbnail) ImageView thumbnail;
        Videos video;

        VideosViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + content.getText() + "'";
        }
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.username) ProximaNovaSemiboldTextView username;
        @BindView(R.id.name) ProximaNovaRegularTextView name;
        @BindView(R.id.action) ProximaNovaSemiboldTextView action;
        MiniProfile user;

        PeopleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + username.getText() + "'";
        }
    }

    public interface OnDiscoverSearchInteractionListener {
        void onDiscoverSearchInteraction(boolean isVideosTab, int id);
    }
}