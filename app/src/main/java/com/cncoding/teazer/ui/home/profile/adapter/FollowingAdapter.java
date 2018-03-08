package com.cncoding.teazer.ui.home.profile.adapter;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.friends.UserInfo;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewOtherProfile;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewProfile2;
import com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.common.ViewUtils.getGenderSpecificDpSmall;
import static com.cncoding.teazer.utilities.common.ViewUtils.setActionButtonText;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class FollowingAdapter extends BaseRecyclerView.Adapter {

    private List<UserInfo> userInfoList;
    private BaseFragment fragment;
    private boolean isMyself;

    public FollowingAdapter(BaseFragment fragment, boolean isMyself) {
        this.fragment = fragment;
        this.isMyself = isMyself;
        this.userInfoList = new ArrayList<>();
    }

    @Override
    public FollowingAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_following, viewGroup, false);
        return new FollowingAdapterViewHolder(view, isMyself);
    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    @Override
    public void release() {

    }

    @Override
    public void notifyDataChanged() {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private void notifyItemsInserted(final int positionStart, final int itemCount) {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeInserted(positionStart, itemCount);
            }
        });
    }

    private void dispatchUpdates(final DiffUtil.DiffResult result) {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                result.dispatchUpdatesTo(FollowingAdapter.this);
            }
        });
    }

    public void updateUserInfoPosts(List<UserInfo> userInfoList) {
        try {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                    new UserInfoDiffCallback(new ArrayList<>(this.userInfoList.subList(0, 10)), userInfoList));
            this.userInfoList.clear();
            this.userInfoList.addAll(userInfoList);
            dispatchUpdates(result);
        } catch (Exception e) {
            e.printStackTrace();
            addUserInfoPosts(1, userInfoList);
        }
    }

    public void addUserInfoPosts(int page, List<UserInfo> userInfoList) {
        if (page == 1) {
            this.userInfoList.clear();
            this.userInfoList.addAll(userInfoList);
            notifyDataChanged();
        } else {
            this.userInfoList.addAll(userInfoList);
            notifyItemsInserted((page - 1) * 10, userInfoList.size());
        }
    }

    public class FollowingAdapterViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.name) ProximaNovaSemiBoldTextView name;
        @BindView(R.id.action) ProximaNovaSemiBoldTextView action;
        @BindView(R.id.decline) AppCompatImageView declineBtn;
        private UserInfo userInfo;
        private boolean isMyself;
        private int requestId;

        FollowingAdapterViewHolder(View view, boolean isMyself) {
            super(view);
            this.isMyself = isMyself;
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind() {
            try {
                userInfo = userInfoList.get(getAdapterPosition());
                if (isMyself) {
                    setActionButtonText(fragment.getContext(), action, R.string.following);
                    Glide.with(fragment)
                            .load(userInfo.getProfileMedia() != null ?
                                    userInfo.getProfileMedia().getThumbUrl() :
                                    getGenderSpecificDpSmall(userInfo.getGender()))
                            .into(dp);
                    name.setText(userInfo.getUserName());
                } else {
                    requestId = userInfo.isRequestRecieved() ? userInfo.getRequestId() : 0;
                    name.setText(userInfo.getUserName());
                    Glide.with(fragment)
                            .load(userInfo.getProfileMedia() != null ?
                                    userInfo.getProfileMedia().getThumbUrl() :
                                    getGenderSpecificDpSmall(userInfo.getGender()))
                            .into(dp);

                    if (userInfo.isMySelf()) {
                        name.setTextColor(Color.parseColor("#333333"));
                        action.setVisibility(View.INVISIBLE);
                    } else {
                        if (userInfo.isBlockedYou()) {
                            name.setTextColor(Color.GRAY);
                            action.setVisibility(View.INVISIBLE);
                        } else if (userInfo.isYouBlocked()) {
                            name.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                            action.setVisibility(View.VISIBLE);
                            setActionButtonText(fragment.getContext(), action, R.string.unblock);
                        } else {
                            name.setTextColor(Color.parseColor("#333333"));
                            action.setVisibility(View.VISIBLE);
                            if (userInfo.getAccountType() == 1) {

                                if (userInfo.isFollowing()) {
                                    setActionButtonText(fragment.getContext(), action, userInfo.isRequestRecieved() ? R.string.accept : R.string.following);
                                } else {
                                    if (userInfo.isRequestSent()) {
                                        if (userInfo.isRequestRecieved()) {
                                            setActionButtonText(fragment.getContext(), action, R.string.accept);
                                        } else {
                                            setActionButtonText(fragment.getContext(), action, R.string.requested);
                                        }
                                    } else {
                                        if (userInfo.isRequestRecieved() && userInfo.isFollower()) {
                                            setActionButtonText(fragment.getContext(), action, R.string.follow);
                                        }
                                        else if(userInfo.isRequestRecieved()) {
                                            setActionButtonText(fragment.getContext(), action, R.string.accept);
                                        }
                                        else if(userInfo.isFollower()) {
                                            setActionButtonText(fragment.getContext(), action, R.string.follow);
                                        }
                                        else {
                                            setActionButtonText(fragment.getContext(), action, R.string.follow);
                                        }
                                    }
                                }
                            } else {
                                if (userInfo.isFollowing()) {
                                    if (userInfo.isRequestRecieved()) {
                                        setActionButtonText(fragment.getContext(), action, R.string.accept);
                                    }
                                    else {
                                        setActionButtonText(fragment.getContext(), action, R.string.following);
                                    }
                                }
                                else {
                                    if (userInfo.isRequestSent()) {
                                        if (userInfo.isRequestRecieved()) {
                                            setActionButtonText(fragment.getContext(), action, R.string.accept);
                                        } else {
                                            setActionButtonText(fragment.getContext(), action, R.string.requested);
                                        }
                                    } else {
                                        if (userInfo.isRequestRecieved() && userInfo.isFollower()) {
                                            setActionButtonText(fragment.getContext(), action, R.string.follow);
                                        }
                                        else if(userInfo.isRequestRecieved()) {
                                            setActionButtonText(fragment.getContext(), action, R.string.accept);
                                        }
                                        else if(userInfo.isFollower()) {
                                            setActionButtonText(fragment.getContext(), action, R.string.follow);
                                        }
                                        else {
                                            setActionButtonText(fragment.getContext(), action, R.string.follow);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @OnClick(R.id.root_layout) public void rootLayoutClicked() {
            if (isMyself) {
                fragment.navigation.pushFragment(FragmentNewProfile2.newInstance());
            } else {
                if (userInfo.isBlockedYou()) {
                    Toast.makeText(fragment.getContext(), "you can not view this user profile", Toast.LENGTH_LONG).show();
                } else {
                    fragment.navigation.pushFragment(FragmentNewOtherProfile.newInstance(String.valueOf(userInfo.getUserId())));
                }
            }
        }

        @OnClick(R.id.action) public void actionBtnClicked() {
            CharSequence actionText = action.getText();
            if (isMyself) {
                if (Objects.equals(actionText, fragment.getString(R.string.following))) {
                    confirmUnfollow(userInfo.getUserId(), userInfo.getUserName());
                }
            } else {
                if (Objects.equals(actionText, fragment.getString(R.string.follow))) {
                    if (userInfo.isRequestRecieved() && userInfo.isFollower()) {
                        acceptUser(requestId, userInfo.isFollowing(), userInfo.isRequestSent(), false);
                    } else {
                        followUser(userInfo.getUserId(), userInfo.getAccountType());
                    }
                }
                else if (Objects.equals(actionText, fragment.getString(R.string.accept))) {
                    acceptUser(requestId, userInfo.isFollowing(), userInfo.isRequestSent(), true);
                }
                else if (Objects.equals(actionText, fragment.getString(R.string.requested))) {
                    confirmCancelRequest(userInfo.getUserId(), userInfo.getUserName());
                }
                else if (Objects.equals(actionText, fragment.getString(R.string.following))) {
                    confirmUnfollow(userInfo.getUserId(), userInfo.getUserName());
                }
                else if (Objects.equals(actionText, fragment.getString(R.string.unblock))) {
                    confirmBlockUnblock(userInfo.getUserId(), userInfo.getUserName());
                }
            }
        }

        private void acceptUser(final int requestId, final boolean isFollowing, final boolean requestSent, final boolean isAcceptFollow) {
            ApiCallingService.Friends.acceptJoinRequest(requestId, fragment.getContext()).enqueue(new Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                    try {
                        if (response.code() == 200) {
                            if (response.body().getStatus()) {
                                if (isAcceptFollow) {
//                                            Toast.makeText(fragment.getContext(), "Request Accepted", Toast.LENGTH_LONG).show();
                                    if (isFollowing) {
                                        setActionButtonText(fragment.getContext(), action, R.string.following);
                                        setFollowing();
                                    } else if (requestSent) {
                                        setActionButtonText(fragment.getContext(), action, R.string.requested);
                                        setRequestSent();
                                    } else {
                                        setActionButtonText(fragment.getContext(), action, R.string.follow);
                                        setFollower();
                                    }
                                } else {
                                    if (response.body().getFollowInfo().getFollowing()) {
                                        setActionButtonText(fragment.getContext(), action, R.string.following);
                                        setFollowing();
//                                        Toast.makeText(fragment.getContext(), "You also have started following", Toast.LENGTH_LONG).show();
                                    } else if (response.body().getFollowInfo().getRequestSent()) {
                                        setActionButtonText(fragment.getContext(), action, R.string.requested);
                                        setRequestSent();
//                                        Toast.makeText(fragment.getContext(), "Your request has been sent", Toast.LENGTH_LONG).show();
                                    } else {
                                        setActionButtonText(fragment.getContext(), action, R.string.follow);
                                        setFollower();
                                    }
                                }
                            } else {
                                Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResultObject> call, Throwable t) {
                    t.printStackTrace();
                    //loader.setVisibility(View.GONE);
                }
            });
        }

        private void followUser(final int userId, final int accountType) {
            ApiCallingService.Friends.followUser(userId, fragment.getContext()).enqueue(new Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                    if (response.code() == 200) {
                        try {
                            if (response.body().getStatus()) {
                                if (accountType == 1) {
                                    setActionButtonText(fragment.getContext(), action, R.string.requested);
//                                    Toast.makeText(fragment.getContext(), "You have sent following request", Toast.LENGTH_LONG).show();
                                    setRequestSent();
                                } else {
                                    setActionButtonText(fragment.getContext(), action, R.string.following);
//                                    Toast.makeText(fragment.getContext(), "You have started following", Toast.LENGTH_LONG).show();
                                    setFollowing();
                                }
                            } else {
                                setActionButtonText(fragment.getContext(), action, R.string.following);
//                                Toast.makeText(fragment.getContext(), "You are already following", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultObject> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                }
            });
        }

        private void confirmCancelRequest(final int userId, String username) {
            showAlertDialog(fragment.getString(R.string.cancel_request_confirmation) + username + "?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelRequest(userId);
                        }
                    });
        }

        private void cancelRequest(final int userId) {
            ApiCallingService.Friends.cancelRequest(userId, fragment.getContext()).enqueue(new Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                    if (response.code() == 200) {
                        try {
                            if (response.body().getStatus()) {
                                setRequestSent();
                                setActionButtonText(fragment.getContext(), action, R.string.follow);
//                                Toast.makeText(fragment.getContext(), "Your request has been cancelled", Toast.LENGTH_LONG).show();
                            } else {
                                setActionButtonText(fragment.getContext(), action, R.string.follow);
//                                Toast.makeText(fragment.getContext(), "Your request has been cancelled", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultObject> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                }
            });
        }

        private void confirmUnfollow(final int userId, String username) {
            showAlertDialog(fragment.getString(R.string.unfollow_confirmation) + username + "?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            unFollowUser(userId);
                        }
                    });
        }

        private void unFollowUser(int userId) {
            ApiCallingService.Friends.unfollowUser(userId, fragment.getContext()).enqueue(new Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                    if (response.code() == 200) {
                        try {
                            if (response.body().getStatus()) {
//                                Toast.makeText(context, "User has been un-followed", Toast.LENGTH_LONG).show();
                                setActionButtonText(fragment.getContext(), action, R.string.follow);
                                setFollowing();
                                userInfoList.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                            } else {
                                setActionButtonText(fragment.getContext(), action, R.string.follow);
//                                Toast.makeText(context, "You have already un-followed", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResultObject> call, Throwable t) {
                    Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                }
            });
        }

        private void confirmBlockUnblock(final int userId, final String username) {
            showAlertDialog(fragment.getString(R.string.unblock_confirmation) + username + "?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            blockUnblockUser(userId);
                        }
                    });
        }

        private void blockUnblockUser(int userId) {
            ApiCallingService.Friends.blockUnblockUser(userId, 2, fragment.getContext()).enqueue(new Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                    try {
                        if (response.body().getStatus()) {
//                            Toast.makeText(fragment.getContext(), "You have Unblocked this user", Toast.LENGTH_SHORT).show();
                            setActionButtonText(fragment.getContext(), action, R.string.follow);
                            userInfo.setYouBlocked(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResultObject> call, Throwable t) {

                    Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void showAlertDialog(String message, DialogInterface.OnClickListener positiveButtonListener) {
            new AlertDialog.Builder(fragment.getParentActivity())
                    .setMessage(message)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.confirm, positiveButtonListener)
                    .show();
        }

        private void setFollowing() {
            userInfo.setFollowing(true);
        }

        private void setFollower() {
            userInfo.setFollower(true);
        }

        private void setRequestSent() {
            userInfo.setRequestSent(false);
        }
    }
}