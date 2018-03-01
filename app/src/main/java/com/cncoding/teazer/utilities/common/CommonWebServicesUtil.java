package com.cncoding.teazer.utilities.common;

import android.content.Context;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.data.model.react.PostReactDetail;
import com.cncoding.teazer.data.model.react.ReactOwner;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.user.NotificationsList;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.home.BaseBottomBarActivity;
import com.cncoding.teazer.ui.home.post.detailspage.PostDetailsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amit on 18/12/17.
 */

public class CommonWebServicesUtil {

    public static void getFollowingNotificationsUnreadCount(final Context context, final int page) {
        Call<NotificationsList> notificationsListCall;
        notificationsListCall = ApiCallingService.User.getFollowingNotifications(page, context);

        if (!notificationsListCall.isExecuted())
            notificationsListCall.enqueue(new Callback<NotificationsList>() {
                @Override
                public void onResponse(Call<NotificationsList> call, Response<NotificationsList> response) {
                    try {
                            if (response.code() == 200) {
                                SharedPrefs.setFollowingNotificationCount(context ,response.body().getUnreadCount());
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NotificationsList> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

    public static void getRequestNotificationsUnreadCount(final Context context, final int page) {
        Call<NotificationsList> notificationsListCall;
        notificationsListCall = ApiCallingService.User.getRequestNotifications(page, context);

        if (!notificationsListCall.isExecuted())
            notificationsListCall.enqueue(new Callback<NotificationsList>() {
                @Override
                public void onResponse(Call<NotificationsList> call, Response<NotificationsList> response) {
                    try {
                            if (response.code() == 200) {
                                SharedPrefs.setRequestNotificationCount(context ,response.body().getUnreadCount());
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NotificationsList> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

    public static void fetchPostDetails(final Context context, int postId) {
        ApiCallingService.Posts.getPostDetails(postId, context)
                .enqueue(new Callback<PostDetails>() {
                    @Override
                    public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                ((BaseBottomBarActivity)context).pushFragment(
                                PostDetailsFragment.newInstance(response.body(), null, true, null));
//                                PostsListFragment.postDetails = response.body();
                            } else {
                                Toast.makeText(context, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<PostDetails> call, Throwable t) {
                        Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void fetchReactionDetails(final BaseFragment fragment, final int postId) {
        ApiCallingService.React.getReactionDetail(postId, fragment.getContext())
                .enqueue(new Callback<ReactionResponse>() {
                    @Override
                    public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                PostReactDetail postReactDetail = response.body().getPostReactDetail();
                                ReactOwner reactOwner = postReactDetail.getReactOwner();
                                PostReaction postReaction = new PostReaction(
                                        postReactDetail.getReactId(),
                                        postReactDetail.getReactTitle(),
                                        postReactDetail.getPostOwnerId(),
                                        postReactDetail.getLikes(),
                                        postReactDetail.getViews(),
                                        postReactDetail.getCanLike(),
                                        postReactDetail.getCanDelete(),
                                        postReactDetail.getMediaDetail(),
                                        new MiniProfile(reactOwner.getUserId(),
                                                reactOwner.getUserName(),
                                                reactOwner.getFirstName(),
                                                reactOwner.getLastName(),
                                                reactOwner.getHasProfileMedia(),
                                                reactOwner.getProfileMedia()),
                                        postReactDetail.getReactedAt());
                                //play video in exo player
                                ViewUtils.playOnlineVideoInExoPlayer(fragment, postReaction);
                            } else {
                                Toast.makeText(fragment.getContext(), R.string.reaction_no_longer_exists, Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(fragment.getContext(), "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ReactionResponse> call, Throwable t) {
                        Toast.makeText(fragment.getContext(), "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
