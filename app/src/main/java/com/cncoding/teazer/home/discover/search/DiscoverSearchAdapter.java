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

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
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
    private final ArrayList<MiniProfile> usersList;
    private OnDiscoverSearchInteractionListener mListener;

    DiscoverSearchAdapter(Context context, boolean isVideosTab, ArrayList<MiniProfile> usersList) {
        this.context = context;
        this.isVideosTab = isVideosTab;
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
                break;
            case TYPE_PEOPLE:
                final PeopleViewHolder holder = (PeopleViewHolder) viewHolder;
                holder.user = usersList.get(position);

                holder.username.setText(holder.user.getUserName());
                String name = holder.user.getFirstName() + BLANK_SPACE + holder.user.getLastName();
                holder.name.setText(name);

                switch (holder.user.getAccountType()) {
                    case ACCOUNT_TYPE_PRIVATE:
                        if (holder.user.isFollowing())
                            setActionButton(holder.action, BUTTON_TYPE_FOLLOWING);
                        else if (holder.user.isRequestSent())
                            setActionButton(holder.action, BUTTON_TYPE_REQUESTED);
                        else
                            setActionButton(holder.action, BUTTON_TYPE_FOLLOW);
                        break;
                    case ACCOUNT_TYPE_PUBLIC:
                        if (holder.user.isFollowing())
                            setActionButton(holder.action, BUTTON_TYPE_FOLLOWING);
                        else
                            setActionButton(holder.action, BUTTON_TYPE_FOLLOW);
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
                                    mListener.onDiscoverSearchInteraction(holder.user.getUserId());
                                break;
                            case R.id.action:
                                if (holder.action.getText().equals(context.getString(R.string.follow))) {
                                    ApiCallingService.Friends.sendJoinRequestByUserId(holder.user.getUserId(), context)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (response.code() == 200) {
                                                        switch (holder.user.getAccountType()) {
                                                            case ACCOUNT_TYPE_PRIVATE:
                                                                setActionButton(holder.action, BUTTON_TYPE_REQUESTED);
                                                                break;
                                                            case ACCOUNT_TYPE_PUBLIC:
                                                                setActionButton(holder.action, BUTTON_TYPE_FOLLOWING);
                                                                break;
                                                            default:
                                                                break;
                                                        }
                                                    } else
                                                        Log.e("sendJoinRequest", response.code() + "_" + response.message());

                                                    response.raw().body().close();
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    if (t.getMessage() != null)
                                                        Log.e("sendJoinRequest", t.getMessage());
                                                }
                                            });
                                }
                                else if (holder.action.getText().equals(context.getString(R.string.following))) {
                                    new AlertDialog.Builder(context)
                                            .setMessage(context.getString(R.string.unfollow_confirmation) +
                                                    holder.user.getUserName() + "?")
                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ApiCallingService.Friends.unfollowUser(holder.user.getUserId(), context)
                                                            .enqueue(new Callback<ResultObject>() {
                                                                @Override
                                                                public void onResponse(Call<ResultObject> call,
                                                                                       Response<ResultObject> response) {
                                                                    if (response.code() == 200) {
                                                                        setActionButton(holder.action, BUTTON_TYPE_FOLLOW);
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

                holder.layout.setOnClickListener(listener);
                holder.action.setOnClickListener(listener);
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
                button.setTextColor(Color.parseColor("#000000"));
                button.setBackgroundResource(R.drawable.bg_outline_rounded_black);
                button.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.ic_check_small),
                        null, null, null);
                break;
            case BUTTON_TYPE_REQUESTED:
                button.setText(R.string.requested);
                button.setTextColor(Color.parseColor("#000000"));
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
        return usersList.size();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.content) ProximaNovaRegularTextView content;
        @BindView(R.id.thumbnail) ImageView thumbnail;

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
        void onDiscoverSearchInteraction(int userId);
    }
}