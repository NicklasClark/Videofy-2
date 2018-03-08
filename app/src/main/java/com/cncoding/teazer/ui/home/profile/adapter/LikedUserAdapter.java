package com.cncoding.teazer.ui.home.profile.adapter;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.post.LikedUser;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.base.BaseHomeFragment;
import com.cncoding.teazer.utilities.diffutil.LikedUserDiffCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.common.ViewUtils.getGenderSpecificDpSmall;
import static com.cncoding.teazer.utilities.common.ViewUtils.openProfile;
import static com.cncoding.teazer.utilities.common.ViewUtils.setActionButtonText;
import static com.cncoding.teazer.utilities.diffutil.LikedUserDiffCallback.DIFF_LIKED_USER;
import static com.cncoding.teazer.utilities.diffutil.LikedUserDiffCallback.updateLikedUserAccordingToDiffBundle;

/**
 *
 * Created by farazhabib on 02/01/18.
 */

public class LikedUserAdapter extends BaseRecyclerView.Adapter {

    private List<LikedUser> likedUserList;
    private BaseHomeFragment fragment;

    public LikedUserAdapter(BaseHomeFragment fragment) {
        this.fragment = fragment;
        this.likedUserList = new ArrayList<>();
    }

    @Override
    public LikedUserAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_followers, viewGroup, false);
        return new LikedUserAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return likedUserList.size();
    }

    @Override
    public void release() {}

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
                result.dispatchUpdatesTo(LikedUserAdapter.this);
            }
        });
    }

    public void updatePosts(List<LikedUser> userInfoList) {
        try {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                    new LikedUserDiffCallback(new ArrayList<>(this.likedUserList.subList(0, 10)), userInfoList));
            this.likedUserList.clear();
            this.likedUserList.addAll(userInfoList);
            dispatchUpdates(result);
        } catch (Exception e) {
            e.printStackTrace();
            addPosts(1, userInfoList);
        }
    }

    public void addPosts(int page, List<LikedUser> userInfoList) {
        if (page == 1) {
            this.likedUserList.clear();
            this.likedUserList.addAll(userInfoList);
            notifyDataChanged();
        } else {
            this.likedUserList.addAll(userInfoList);
            notifyItemsInserted((page - 1) * 10, userInfoList.size());
        }
    }

    public class ViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.name) TextView name;
        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.dp) CircularAppCompatImageView dp;
        @BindView(R.id.action) ProximaNovaSemiBoldTextView action;
        LikedUser likedUser;
        int requestId;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void bind() {
            likedUser = likedUserList.get(getAdapterPosition());
            try {
                name.setText(likedUser.getUserName());

                requestId = likedUser.getFollowInfo().isRequestReceived() ? likedUser.getFollowInfo().getRequestId() : 0;

                Glide.with(fragment)
                        .load(likedUser.getProfileMedia() != null ?
                                likedUser.getProfileMedia().getThumbUrl() :
                                getGenderSpecificDpSmall(likedUser.getGender()))
                        .into(dp);
                if (likedUser.isMySelf()) {
                    name.setTextColor(Color.parseColor("#333333"));
                    action.setVisibility(View.INVISIBLE);
                } else {
                    if (likedUser.getFollowInfo().isYouBlocked()) {
                        name.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                        action.setVisibility(View.VISIBLE);
                        setButtonText(R.string.unblock);
                    } else if (likedUser.getFollowInfo().isBlockedYou()) {
                        name.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                        action.setVisibility(View.INVISIBLE);
                    } else {
                        action.setVisibility(View.VISIBLE);
                        name.setTextColor(Color.parseColor("#333333"));
                        if (likedUser.getAccountType() == 1) {
                            if (likedUser.getFollowInfo().isFollowing()) {
                                setButtonText(likedUser.getFollowInfo().isRequestReceived() ? R.string.accept : R.string.following);
                            } else {
                                if (likedUser.getFollowInfo().isRequestSent()) {
                                    setButtonText(likedUser.getFollowInfo().isRequestReceived() ? R.string.accept : R.string.requested);
                                } else {
                                    setButtonText(likedUser.getFollowInfo().isRequestReceived() ? R.string.accept : R.string.follow);
                                }
                            }
                        } else {
                            if (likedUser.getFollowInfo().isFollowing()) {
                                setButtonText(R.string.following);
                            } else {
                                setButtonText(likedUser.getFollowInfo().isRequestSent() ? R.string.requested : R.string.follow);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void bind(List<Object> payloads) {
            if (payloads.isEmpty()) return;

            if (payloads.get(0) instanceof LikedUser) {
                likedUserList.set(getAdapterPosition(), (LikedUser) payloads.get(0));
                bind();
                return;
            }

            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle.containsKey(DIFF_LIKED_USER)) {
                likedUserList.set(getAdapterPosition(), (LikedUser) bundle.getParcelable(DIFF_LIKED_USER));
                bind();
                return;
            }

            updateLikedUserAccordingToDiffBundle(likedUser, bundle);
        }

        private void setButtonText(@StringRes int stringId) {
            setActionButtonText(fragment.context, action, stringId);
        }

        @OnClick(R.id.action) void actionButtonClicked() {
            if (action.getText().equals(fragment.getString(R.string.accept))) {
                acceptUser(requestId, likedUser.getFollowInfo().isFollowing(), likedUser.getFollowInfo().isRequestSent());
            }
            else if (action.getText().equals(fragment.getString(R.string.follow))) {
                followUser(likedUser.getUserId(), likedUser.getAccountType());
            }
            else if (action.getText().equals(fragment.getString(R.string.requested))) {
                confirmCancelRequest(likedUser.getUserId(), likedUser.getUserName());
            }
            else if (action.getText().equals(fragment.getString(R.string.following))) {
                confirmUnfollow(likedUser.getUserId(), likedUser.getUserName());
            }
            else if (action.getText().equals(fragment.getString(R.string.unblock))) {
                confirmBlockUnblock(likedUser.getUserId(), likedUser.getUserName());
            }
        }

        @OnClick(R.id.root_layout) void openUserProfile() {
            openProfile(fragment.navigation, likedUser.isMySelf(), likedUser.getUserId());
        }

        private void acceptUser(final int requestId, final boolean isFollowing, final boolean requestSent) {
            ApiCallingService.Friends.acceptJoinRequest(requestId, fragment.getContext()).enqueue(new Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                    try {
                        if (response.code() == 200) {
                            if (response.body().getStatus()) {
                                Toast.makeText(fragment.getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
                                if (isFollowing) {
                                    setButtonText(R.string.following);
                                    setFollowing();
                                } else if (requestSent) {
                                    setButtonText(R.string.requested);
                                    setRequestSent();
                                } else {
                                    setButtonText(R.string.follow);
                                    setFollower();
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
                    Toast.makeText(fragment.getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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
                                setFollowing();
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
                                likedUserList.remove(getAdapterPosition());
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
                        setActionButtonText(fragment.getContext(), action, R.string.follow);
                        likedUser.setYouBlocked(false);
//                        if (response.body().getStatus()) {
//                            Toast.makeText(fragment.getContext(), "You have Unblocked this user", Toast.LENGTH_SHORT).show();
//                        }
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
            likedUser.getFollowInfo().setFollowing(true);
        }

        private void setFollower() {
            likedUser.getFollowInfo().setFollower(true);
        }

        private void setRequestSent() {
            likedUser.getFollowInfo().setRequestSent(false);
        }
    }
}