package com.cncoding.teazer.home.discover.search;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
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
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.discover.Videos;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.MainActivity.ACCOUNT_TYPE_PRIVATE;
import static com.cncoding.teazer.MainActivity.ACCOUNT_TYPE_PUBLIC;
import static com.cncoding.teazer.home.notifications.NotificationsAdapter.BUTTON_TYPE_ACCEPT;
import static com.cncoding.teazer.home.notifications.NotificationsAdapter.BUTTON_TYPE_FOLLOW;
import static com.cncoding.teazer.home.notifications.NotificationsAdapter.BUTTON_TYPE_FOLLOWING;
import static com.cncoding.teazer.home.notifications.NotificationsAdapter.BUTTON_TYPE_REQUESTED;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.home.notifications.NotificationsAdapter.BUTTON_TYPE_UNBLOCK;
import static com.cncoding.teazer.utilities.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.ViewUtils.setActionButtonText;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.cncoding.teazer.model.user.Notification}
 */
public class DiscoverSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_VIDEOS = 0;
    private static final int TYPE_PEOPLE = 1;

    private Context context;
    private BaseFragment baseFragment;
    private boolean isVideosTab;
    private boolean isSearchTerm;
    private ArrayList<Videos> videosList;
    private ArrayList<MiniProfile> usersList;
    private SparseIntArray actionArray;
    private OnDiscoverSearchInteractionListener mListener;

    DiscoverSearchAdapter(Context context, BaseFragment baseFragment, boolean isVideosTab, ArrayList<MiniProfile> usersList,
                          ArrayList<Videos> videosList, boolean isSearchTerm) {
        this.context = context;
        this.baseFragment = baseFragment;
        this.isVideosTab = isVideosTab;
        this.isSearchTerm = isSearchTerm;
        actionArray = new SparseIntArray();
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
                holder1.content.setText(decodeUnicodeString(holder1.video.getTitle()));

                holder1.content.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        !isSearchTerm ? R.drawable.ic_trending_up : 0, 0);

                Glide.with(baseFragment.getContext())
                        .load(holder1.video.getPostVideoInfo() != null ? holder1.video.getPostVideoInfo().get(0).getThumbUrl() :
                                R.drawable.bg_placeholder)
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

                holder2.username.setText(decodeUnicodeString(holder2.user.getUserName()));
                String name = holder2.user.getFirstName() + BLANK_SPACE + holder2.user.getLastName();
                holder2.name.setText(name);


                Glide.with(baseFragment.getContext())
                        .load(holder2.user.getProfileMedia() != null ? holder2.user.getProfileMedia().getThumbUrl() :
                                R.drawable.ic_user_male_dp_small)
                        .placeholder(R.drawable.ic_user_male_dp_small)
                        .crossFade()
                        .into(holder2.dp);

                if (actionArray.get(position) == 0) {
                    switch (holder2.user.getAccountType()) {
                        case ACCOUNT_TYPE_PRIVATE:
                            if(holder2.user.getYouBlocked()) {
                                setActionButton(holder2.action, BUTTON_TYPE_UNBLOCK, position, true);
                            }
                            else {
                                if (holder2.user.isFollowing()) {
                                    if (holder2.user.getRequestRecieved()) {

                                        setActionButton(holder2.action, BUTTON_TYPE_ACCEPT, position, true);
                                    }
                                    else {
                                        setActionButton(holder2.action, BUTTON_TYPE_FOLLOWING, position, true);
                                    }
                                }
                                else {
                                    if (holder2.user.isRequestSent()) {

                                        if (holder2.user.getRequestRecieved()) {
                                            setActionButton(holder2.action, BUTTON_TYPE_ACCEPT, position, true);
                                        } else {
                                            setActionButton(holder2.action, BUTTON_TYPE_REQUESTED, position, true);
                                        }
                                    } else {
                                        if (holder2.user.getRequestRecieved()) {

                                            setActionButton(holder2.action, BUTTON_TYPE_ACCEPT, position, true);
                                        } else {
                                            setActionButton(holder2.action, BUTTON_TYPE_FOLLOW, position, true);

                                        }
                                    }
                                }
                            }
                            break;

                        case ACCOUNT_TYPE_PUBLIC:
                            if(holder2.user.getYouBlocked()) {
                                setActionButton(holder2.action, BUTTON_TYPE_UNBLOCK, position, true);
                            }
                            else {
                                if (holder2.user.isFollowing())
                                    setActionButton(holder2.action, BUTTON_TYPE_FOLLOWING, position, true);
                                else
                                    setActionButton(holder2.action, BUTTON_TYPE_FOLLOW, position, true);
                            }
                            break;
                        default:
                            break;
                    }
                } else setActionButton(holder2.action, actionArray.get(position), position, false);

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
                                    ApiCallingService.Friends.sendJoinRequestByUserId(holder2.user.getUserId(), baseFragment.getContext())
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (baseFragment.isAdded()) {
                                                        if (response.code() == 200) {
                                                            switch (holder2.user.getAccountType()) {
                                                                case ACCOUNT_TYPE_PRIVATE:
                                                                    setActionButton(holder2.action, BUTTON_TYPE_REQUESTED,
                                                                            holder2.getAdapterPosition(), true);
                                                                    break;
                                                                case ACCOUNT_TYPE_PUBLIC:
                                                                    setActionButton(holder2.action, BUTTON_TYPE_FOLLOWING,
                                                                            holder2.getAdapterPosition(), true);
                                                                    break;
                                                                default:
                                                                    break;
                                                            }
                                                        } else
                                                            Log.e("sendJoinRequest", response.code() + "_" + response.message());
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    if (baseFragment.isAdded()) {
                                                        if (t.getMessage() != null)
                                                            Log.e("sendJoinRequest", t.getMessage());
                                                    }
                                                }
                                            });
                                }


                                else if (holder2.action.getText().equals(context.getString(R.string.accept))) {

                                    ApiCallingService.Friends.acceptJoinRequest(holder2.user.getRequestId(), context)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    try {

                                                        if (response.code() == 200) {
                                                            if (response.body().getStatus()) {

                                                                Toast.makeText(context, "Request Accepted", Toast.LENGTH_LONG).show();
                                                                if (holder2.user.isFollowing()) {
                                                                    setActionButton(holder2.action, BUTTON_TYPE_FOLLOWING,
                                                                            holder2.getAdapterPosition(), true);

                                                                } else if (holder2.user.isRequestSent()) {
                                                                    setActionButton(holder2.action, BUTTON_TYPE_REQUESTED,
                                                                            holder2.getAdapterPosition(), true);
                                                                } else {

                                                                    setActionButton(holder2.action, BUTTON_TYPE_FOLLOW,
                                                                            holder2.getAdapterPosition(), true);
                                                                }
                                                            }
                                                            else {
                                                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    }
                                                    catch (Exception e) {
                                                        Toast.makeText(context, "Something went wrong, Please try again..", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    t.printStackTrace();

                                                }
                                            });
                                }

                                else if (holder2.action.getText().equals(context.getString(R.string.following))) {
                                    new AlertDialog.Builder(baseFragment.getParentActivity())
                                            .setMessage(context.getString(R.string.unfollow_confirmation) +
                                                    holder2.user.getUserName() + "?")
                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ApiCallingService.Friends.unfollowUser(holder2.user.getUserId(), baseFragment.getContext())
                                                            .enqueue(new Callback<ResultObject>() {
                                                                @Override
                                                                public void onResponse(Call<ResultObject> call,
                                                                                       Response<ResultObject> response) {
                                                                    if (baseFragment.isAdded()) {
                                                                        if (response.code() == 200) {
                                                                            setActionButton(holder2.action, BUTTON_TYPE_FOLLOW,
                                                                                    holder2.getAdapterPosition(), true);
                                                                        } else
                                                                            Log.e("unfollowUser",
                                                                                    response.code() + "_" + response.message());
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                                    if (baseFragment.isAdded()) {
                                                                        if (t.getMessage() != null)
                                                                            Log.e("sendJoinRequest", t.getMessage());
                                                                    }
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
                                else if (holder2.action.getText().equals(context.getString(R.string.requested))) {
                                    new AlertDialog.Builder(baseFragment.getParentActivity())
                                            .setMessage(context.getString(R.string.cancel_request_confirmation) +
                                                    holder2.user.getUserName() + "?")
                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ApiCallingService.Friends.cancelRequest(holder2.user.getUserId(),
                                                            baseFragment.getContext()).enqueue(new Callback<ResultObject>() {
                                                        @Override
                                                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                            if (response.code() == 200) {
                                                                if (response.body().getStatus()) {
                                                                    setActionButton(holder2.action, BUTTON_TYPE_FOLLOW,
                                                                            holder2.getAdapterPosition(), true);
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
                                else if (holder2.action.getText().equals(context.getString(R.string.following))) {
                                    new AlertDialog.Builder(baseFragment.getParentActivity())
                                            .setMessage(context.getString(R.string.unfollow_confirmation) +
                                                    holder2.user.getUserName() + "?")
                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ApiCallingService.Friends.unfollowUser(holder2.user.getUserId(), baseFragment.getContext())
                                                            .enqueue(new Callback<ResultObject>() {
                                                                @Override
                                                                public void onResponse(Call<ResultObject> call,
                                                                                       Response<ResultObject> response) {
                                                                    if (baseFragment.isAdded()) {
                                                                        if (response.code() == 200) {
                                                                            setActionButton(holder2.action, BUTTON_TYPE_FOLLOW,
                                                                                    holder2.getAdapterPosition(), true);
                                                                        } else
                                                                            Log.e("unfollowUser",
                                                                                    response.code() + "_" + response.message());
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                                    if (baseFragment.isAdded()) {
                                                                        if (t.getMessage() != null)
                                                                            Log.e("sendJoinRequest", t.getMessage());
                                                                    }
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


                                else if (holder2.action.getText().equals(context.getString(R.string.unblock))) {
                                    new AlertDialog.Builder(baseFragment.getParentActivity())
                                            .setMessage(context.getString(R.string.unblock_confirmation) +
                                                    holder2.user.getUserName() + "?")
                                            .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    ApiCallingService.Friends.blockUnblockUser(holder2.user.getUserId(), 2, context).enqueue(new Callback<ResultObject>() {
                                                        @Override
                                                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                            try {
                                                                boolean b = response.body().getStatus();
                                                                if (b) {
                                                                    Toast.makeText(context, "You have Unblocked this user", Toast.LENGTH_SHORT).show();
                                                                    setActionButton(holder2.action, BUTTON_TYPE_FOLLOW,
                                                                            holder2.getAdapterPosition(), true);
                                                                } else {

                                                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setActionButton(ProximaNovaSemiBoldTextView button, int type, int position, boolean savePosition) {
        if (savePosition)
            actionArray.put(position, type);
        switch (type) {
            case BUTTON_TYPE_FOLLOW:
                setActionButtonText(context, button, R.string.follow);
                break;
            case BUTTON_TYPE_FOLLOWING:
                setActionButtonText(context, button, R.string.following);
                break;
            case BUTTON_TYPE_REQUESTED:
                setActionButtonText(context, button, R.string.requested);
                break;
            case BUTTON_TYPE_ACCEPT:
                setActionButtonText(context, button, R.string.accept);
                break;
            case BUTTON_TYPE_UNBLOCK :
                setActionButtonText(context, button, R.string.unblock);
                break;
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
        @BindView(R.id.username) ProximaNovaSemiBoldTextView username;
        @BindView(R.id.name) ProximaNovaRegularTextView name;
        @BindView(R.id.action) ProximaNovaSemiBoldTextView action;
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