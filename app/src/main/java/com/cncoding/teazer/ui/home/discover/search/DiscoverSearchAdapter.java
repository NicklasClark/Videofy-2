package com.cncoding.teazer.ui.home.discover.search;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.discover.Videos;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.viewmodel.FriendsViewModel;
import com.cncoding.teazer.data.viewmodel.factory.AuthTokenViewModelFactory;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.discover.BaseDiscoverFragment;
import com.cncoding.teazer.ui.home.post.detailspage.PostDetailsFragment;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewOtherProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.ui.home.notifications.NotificationsAdapter.BUTTON_TYPE_ACCEPT;
import static com.cncoding.teazer.ui.home.notifications.NotificationsAdapter.BUTTON_TYPE_FOLLOW;
import static com.cncoding.teazer.ui.home.notifications.NotificationsAdapter.BUTTON_TYPE_FOLLOWING;
import static com.cncoding.teazer.ui.home.notifications.NotificationsAdapter.BUTTON_TYPE_REQUESTED;
import static com.cncoding.teazer.ui.home.notifications.NotificationsAdapter.BUTTON_TYPE_UNBLOCK;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_ACCEPT_JOIN_REQUEST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_BLOCK_UNBLOCK_USER;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_CANCEL_REQUEST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_SEND_JOIN_REQUEST_BY_USER_ID;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_UNFOLLOW_USER;
import static com.cncoding.teazer.utilities.common.Annotations.PRIVATE_ACCOUNT;
import static com.cncoding.teazer.utilities.common.Annotations.PUBLIC_ACCOUNT;
import static com.cncoding.teazer.utilities.common.AuthUtils.isConnected;
import static com.cncoding.teazer.utilities.common.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getAuthToken;
import static com.cncoding.teazer.utilities.common.ViewUtils.BLANK_SPACE;
import static com.cncoding.teazer.utilities.common.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.common.ViewUtils.setActionButtonText;

/**
 * {@link RecyclerView.Adapter} that can display a {@link com.cncoding.teazer.data.model.user.Notification}
 */
public class DiscoverSearchAdapter extends BaseRecyclerView.Adapter {

    private static final int TYPE_VIDEOS = 0;
    private static final int TYPE_PEOPLE = 1;

    private boolean isVideosTab;
    private boolean isSearchTerm;
    private BaseDiscoverFragment fragment;
    private FriendsViewModel viewModel;
    private List<Videos> videosList;
    private List<MiniProfile> usersList;
    private SparseIntArray actionArray;

    DiscoverSearchAdapter(BaseDiscoverFragment fragment, boolean isVideosTab, boolean isSearchTerm) {
        this.fragment = fragment;
        this.isVideosTab = isVideosTab;
        this.isSearchTerm = isSearchTerm;
        actionArray = new SparseIntArray();
        if (isVideosTab)
            this.videosList = new ArrayList<>();
        else {
            this.usersList = new ArrayList<>();
            viewModel = ViewModelProviders
                    .of(fragment, new AuthTokenViewModelFactory(getAuthToken(fragment.getContext()), true))
                    .get(FriendsViewModel.class);
        }
    }

    @Override public BaseRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

    @Override public int getItemViewType(int position) {
        return isVideosTab ? TYPE_VIDEOS : TYPE_PEOPLE;
    }

    @Override public int getItemCount() {
        return videosList != null ? videosList.size() : usersList.size();
    }

    @Override public void release() {}

    @Override public void notifyDataChanged() {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private void notifyItemRangeInsert(final int positionStart, final int itemCount) {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeInserted(positionStart, itemCount);
            }
        });
    }

    public void updateVideosList(int page, List<Videos> videosList) {
        if (videosList != null && !videosList.isEmpty()) {
            if (page == 1) {
                this.videosList.clear();
                this.videosList.addAll(videosList);
                notifyDataChanged();
            } else {
                this.videosList.addAll(videosList);
                notifyItemRangeInsert((page - 1) * 10, videosList.size());
            }
        }
    }

    public void updateUsersList(int page, List<MiniProfile> usersList) {
        if (usersList != null && !usersList.isEmpty()) {
            if (page == 1) {
                this.usersList.clear();
                this.usersList.addAll(usersList);
                notifyDataChanged();
            } else {
                this.usersList.addAll(usersList);
                notifyItemRangeInsert((page - 1) * 10, usersList.size());
            }
        }
    }

    public class VideosViewHolder extends BaseRecyclerView.ViewHolder {
        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.content) ProximaNovaRegularTextView content;
        @BindView(R.id.thumbnail) ImageView thumbnail;
        Videos video;

        VideosViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind() {
            video = videosList.get(getAdapterPosition());
            content.setText(decodeUnicodeString(video.getTitle()));

            content.setCompoundDrawablesWithIntrinsicBounds(0, 0, !isSearchTerm ? R.drawable.ic_trending_up : 0, 0);

            Glide.with(fragment)
                    .load(video.getPostVideoInfo() != null ? video.getPostVideoInfo().get(0).getThumbUrl() :
                            R.drawable.bg_placeholder)
                    .apply(new RequestOptions().placeholder(R.drawable.bg_placeholder))
                    .into(thumbnail);
        }

        @OnClick(R.id.root_layout) public void viewPost() {
            hideKeyboard(fragment.getParentActivity(), layout);
            ApiCallingService.Posts.getPostDetails(video.getPostId(), fragment.getContext())
                    .enqueue(new Callback<PostDetails>() {
                        @Override
                        public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                            if (response.code() == 200) {
                                fragment.navigation.pushFragment(PostDetailsFragment.newInstance(response.body(),
                                        null, false, null));
                            } else
                                Log.e("Fetching post details", response.code() + "_" + response.message());
                        }

                        @Override
                        public void onFailure(Call<PostDetails> call, Throwable t) {
                            Log.e("Fetching post details", t.getMessage() != null ? t.getMessage() : "Failed!!!");
                        }
                    });
        }
        
        @Override public String toString() {
            return super.toString() + " '" + video.getPostId() + " : " + video.getTitle() + "'";
        }
    }

    public class PeopleViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.username) ProximaNovaSemiBoldTextView username;
        @BindView(R.id.name) ProximaNovaRegularTextView name;
        @BindView(R.id.action) ProximaNovaSemiBoldTextView action;
        MiniProfile user;

        PeopleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            viewModel.getResultObject().observe(fragment, new Observer<ResultObject>() {
                @Override
                public void onChanged(@Nullable ResultObject resultObject) {
                    if (isConnected(fragment.context)) {
                        if (resultObject != null) {
                            if (resultObject.getError() != null) handleError();
                            else handleResponse(resultObject);
                        }
                        else handleError();
                    }
                    else Toast.makeText(fragment.getContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override public void bind() {
            user = usersList.get(getAdapterPosition());
            username.setText(decodeUnicodeString(user.getUserName()));
            String nameText = user.getFirstName() + BLANK_SPACE + user.getLastName();
            name.setText(nameText);

            Glide.with(fragment)
                    .load(user.getProfileMedia() != null ? user.getProfileMedia().getThumbUrl() :
                            R.drawable.ic_user_male_dp_small)
                    .apply(new RequestOptions().placeholder(R.drawable.bg_placeholder))
                    .into(dp);

            if (actionArray.get(getAdapterPosition()) == 0)
                prepareLayoutButton();
            else
                setActionButton(actionArray.get(getAdapterPosition()), getAdapterPosition(), false);
        }

        private void prepareLayoutButton() {
            switch (user.getAccountType()) {
                case PRIVATE_ACCOUNT:
                    if(user.getYouBlocked())
                        setActionButton(BUTTON_TYPE_UNBLOCK, getAdapterPosition(), true);
                    else {
                        if (user.isFollowing()) {
                            if (user.getRequestRecieved()&& user.isFollower())
                                setActionButton(BUTTON_TYPE_ACCEPT, getAdapterPosition(), true);
                            else
                                setActionButton(BUTTON_TYPE_FOLLOWING, getAdapterPosition(), true);
                        }
                        else {
                            if (user.isRequestSent()) {
                                if (user.getRequestRecieved())
                                    setActionButton(BUTTON_TYPE_ACCEPT, getAdapterPosition(), true);
                                else
                                    setActionButton(BUTTON_TYPE_REQUESTED, getAdapterPosition(), true);
                            }
                            else {
                                if (user.getRequestRecieved() && user.isFollower())
                                    setActionButton(BUTTON_TYPE_FOLLOW, getAdapterPosition(), true);
                                else
                                if(user.getRequestRecieved() && !user.isFollower())
                                    setActionButton(BUTTON_TYPE_ACCEPT, getAdapterPosition(), true);
                                else
                                if(!user.getRequestRecieved()  && user.isFollower())
                                    setActionButton(BUTTON_TYPE_FOLLOW, getAdapterPosition(), true);
                                else
                                if(!user.getRequestRecieved())
                                    setActionButton(BUTTON_TYPE_FOLLOW, getAdapterPosition(), true);
                            }
                        }
                    }
                    break;
                case PUBLIC_ACCOUNT:
                    if(user.getYouBlocked())
                        setActionButton(BUTTON_TYPE_UNBLOCK, getAdapterPosition(), true);
                    else {
                        if (user.isFollowing())
                            setActionButton(BUTTON_TYPE_FOLLOWING, getAdapterPosition(), true);
                        else {
                            if (user.isRequestSent()) {
                                if (user.getRequestRecieved())
                                    setActionButton(BUTTON_TYPE_ACCEPT, getAdapterPosition(), true);
                                else
                                    setActionButton(BUTTON_TYPE_REQUESTED, getAdapterPosition(), true);
                            } else {
                                if (user.getRequestRecieved() && user.isFollower())
                                    setActionButton(BUTTON_TYPE_FOLLOW, getAdapterPosition(), true);
                                else
                                if(user.getRequestRecieved() && !user.isFollower())
                                    setActionButton(BUTTON_TYPE_ACCEPT, getAdapterPosition(), true);
                                else
                                if(!user.getRequestRecieved()  && user.isFollower())
                                    setActionButton(BUTTON_TYPE_FOLLOW, getAdapterPosition(), true);
                                else
                                if(!user.getRequestRecieved())
                                    setActionButton(BUTTON_TYPE_FOLLOW, getAdapterPosition(), true);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        private void setActionButton(int type, int position, boolean savePosition) {
            if (savePosition) actionArray.put(position, type);
            switch (type) {
                case BUTTON_TYPE_FOLLOW:
                    setActionButtonText(fragment.getContext(), action, R.string.follow);
                    break;
                case BUTTON_TYPE_FOLLOWING:
                    setActionButtonText(fragment.getContext(), action, R.string.following);
                    break;
                case BUTTON_TYPE_REQUESTED:
                    setActionButtonText(fragment.getContext(), action, R.string.requested);
                    break;
                case BUTTON_TYPE_ACCEPT:
                    setActionButtonText(fragment.getContext(), action, R.string.accept);
                    break;
                case BUTTON_TYPE_UNBLOCK :
                    setActionButtonText(fragment.getContext(), action, R.string.unblock);
                    break;
                default:
                    break;
            }
        }

        @SuppressLint("SwitchIntDef") public void handleResponse(ResultObject resultObject) {
            if (resultObject.getAdapterPosition() == getAdapterPosition()) {
                switch (resultObject.getCallType()) {
                    case CALL_ACCEPT_JOIN_REQUEST:
                        if (resultObject.getStatus()) {
                            Toast.makeText(fragment.getContext(), "Request Accepted", Toast.LENGTH_LONG).show();
                            if (user.isFollowing())
                                setActionButton(BUTTON_TYPE_FOLLOWING, getAdapterPosition(), true);
                            else if (user.isRequestSent())
                                setActionButton(BUTTON_TYPE_REQUESTED, getAdapterPosition(), true);
                            else
                                setActionButton(BUTTON_TYPE_FOLLOW, getAdapterPosition(), true);
                        }
                        else Toast.makeText(fragment.getContext(), resultObject.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                    case CALL_SEND_JOIN_REQUEST_BY_USER_ID:
                        switch (user.getAccountType()) {
                            case PRIVATE_ACCOUNT:
                                setActionButton(BUTTON_TYPE_REQUESTED, getAdapterPosition(), true);
                                break;
                            case PUBLIC_ACCOUNT:
                                setActionButton(BUTTON_TYPE_FOLLOWING, getAdapterPosition(), true);
                                break;
                            default:
                                break;
                        }
                        break;
                    case CALL_UNFOLLOW_USER:
                        setActionButton(BUTTON_TYPE_FOLLOW, getAdapterPosition(), true);
                        break;
                    case CALL_CANCEL_REQUEST:
                        setActionButton(BUTTON_TYPE_FOLLOW, getAdapterPosition(), true);
                        break;
                    case CALL_BLOCK_UNBLOCK_USER:
                        if (resultObject.getStatus()) {
                            Toast.makeText(fragment.getContext(), "You have Unblocked this user", Toast.LENGTH_SHORT).show();
                            setActionButton(BUTTON_TYPE_FOLLOW, getAdapterPosition(), true);
                        } else {
                            Toast.makeText(fragment.getContext(),
                                    resultObject.getMessage() != null ?
                                            resultObject.getMessage() : fragment.getString(R.string.something_went_wrong),
                                    Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        public void handleError() {
            Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }

        @OnClick(R.id.root_layout) void viewProfile() {
            hideKeyboard(fragment.getParentActivity(), layout);
            fragment.navigation.pushFragment(FragmentNewOtherProfile.newInstance(String.valueOf(user.getUserId())));
        }

        @OnClick(R.id.action) void socialAction() {
            hideKeyboard(fragment.getParentActivity(), action);
            if (action.getText().equals(fragment.getString(R.string.follow))) {
                if(user.getRequestRecieved() && user.isFollower()) acceptJoinRequest(user.getRequestId(), getAdapterPosition());
                else sendJoinRequestByUserId(user.getUserId(), getAdapterPosition());
            }
            else if (action.getText().equals(fragment.getString(R.string.accept))) {
                acceptJoinRequest(user.getRequestId(), getAdapterPosition());
            }
            else if (action.getText().equals(fragment.getString(R.string.following))) {
                showAlertDialog(R.string.unfollow_confirmation, CALL_UNFOLLOW_USER);
            }
            else if (action.getText().equals(fragment.getString(R.string.requested))) {
                showAlertDialog(R.string.cancel_request_confirmation, CALL_CANCEL_REQUEST);
            }
            else if (action.getText().equals(fragment.getString(R.string.unblock))) {
                showAlertDialog(R.string.unblock_confirmation, CALL_BLOCK_UNBLOCK_USER);
            }
        }

        private void showAlertDialog(@StringRes int message, final int confirmAction) {
            new AlertDialog.Builder(fragment.getParentActivity())
                    .setMessage(fragment.getString(message) + user.getUserName() + "?")
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (confirmAction) {
                                case CALL_UNFOLLOW_USER:
                                    unfollowUser(user.getUserId(), getAdapterPosition());
                                    break;
                                case CALL_CANCEL_REQUEST:
                                    cancelRequest(user.getUserId(), getAdapterPosition());
                                    break;
                                case CALL_BLOCK_UNBLOCK_USER:
                                    unblockUser(user.getUserId(), getAdapterPosition());
                                    break;
                            }
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

        @Override public String toString() {
            return super.toString() + " '" + username.getText() + "'";
        }
    }

    private void sendJoinRequestByUserId(int userId, int position) {
        viewModel.sendJoinRequestByUserId(userId, position);
    }

    private void acceptJoinRequest(int notificationId, int position) {
        viewModel.acceptJoinRequest(notificationId, position);
    }

    private void unfollowUser(int userId, int position) {
        viewModel.unfollowUser(userId, position);
    }

    private void cancelRequest(int userId, int position) {
        viewModel.cancelRequest(userId, position);
    }

    private void unblockUser(int userId, int position) {
        viewModel.blockUnblockUser(userId, 2, position);
    }
}