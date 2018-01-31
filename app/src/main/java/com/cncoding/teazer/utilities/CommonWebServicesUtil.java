package com.cncoding.teazer.utilities;

import android.content.Context;
import android.widget.Toast;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.home.post.FragmentPostDetails;
import com.cncoding.teazer.home.post.PostsListFragment;
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.react.PostReactDetail;
import com.cncoding.teazer.model.react.ReactOwner;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.model.user.NotificationsList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.SharedPrefs.setFollowingNotificationCount;
import static com.cncoding.teazer.utilities.SharedPrefs.setRequestNotificationCount;
import static com.cncoding.teazer.utilities.ViewUtils.POST_REACTION;
import static com.cncoding.teazer.utilities.ViewUtils.playOnlineVideoInExoPlayer;

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
                                setFollowingNotificationCount(context ,response.body().getUnreadCount());
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
                                setRequestNotificationCount(context ,response.body().getUnreadCount());
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
                                FragmentPostDetails.newInstance(response.body(), null, true,
                                        true, response.body().getMedias().get(0).getThumbUrl(), null));

                                PostsListFragment.postDetails = response.body();
 //                               listener.onPostInteraction(ACTION_VIEW_POST, postDetails, holder.postThumbnail, holder.layout);
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

    public static void fetchReactionDetails(final Context context, final int postId) {
        ApiCallingService.React.getReactionDetail(postId, context)
                .enqueue(new Callback<ReactionResponse>() {
                    @Override
                    public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                PostReactDetail postReactDetail = response.body().getPostReactDetail();
                                ReactOwner reactOwner = postReactDetail.getReactOwner();
                                MiniProfile miniProfile = new MiniProfile(reactOwner.getUserId(), reactOwner.getUserName(), reactOwner.getFirstName(),
                                        reactOwner.getLastName(), reactOwner.getHasProfileMedia(), reactOwner.getProfileMedia());

                                PostReaction postReaction = new PostReaction(postReactDetail.getReactId(), postReactDetail.getReactTitle(),
                                        postReactDetail.getPostOwnerId(), postReactDetail.getLikes(), postReactDetail.getViews(),
                                        postReactDetail.getCanLike(), postReactDetail.getCanDelete(),
                                        postReactDetail.getMediaDetail(), miniProfile, postReactDetail.getReactedAt());

                                //play video in exo player
                                playOnlineVideoInExoPlayer(context, POST_REACTION, postReaction, null, true);
                            } else {
                                Toast.makeText(context, "Either post is not available or deleted by owner", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ReactionResponse> call, Throwable t) {
                        Toast.makeText(context, "Could not play this video, please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
