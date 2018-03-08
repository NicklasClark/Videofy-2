package com.cncoding.teazer.data.remote.apicalls;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.data.model.discover.VideosList;
import com.cncoding.teazer.data.model.friends.CircleList;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.giphy.TrendingGiphy;
import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.model.post.PostUploadResult;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.user.NotificationsList;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.utilities.common.Annotations.CallType;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;

/**
 *
 * Created by Prem$ on 2/14/2018.
 */

public class CallbackFactory {

    @NonNull
    @Contract(pure = true)
    public static Callback<PostList> postListCallback(final MutableLiveData<PostList> liveData,
                                                      @CallType final int callType) {
        return new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                PostList postList = response.body();
                if (postList != null) {
                    postList.setCallType(callType);
                    liveData.setValue(response.isSuccessful() ? postList : new PostList(new Throwable(NOT_SUCCESSFUL)));
                }
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new PostList(new Throwable(FAILED)).setCallType(callType));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<ResultObject> resultObjectCallback(final MutableLiveData<ResultObject> liveData,
                                                              @CallType final int callType) {
        return new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();
                if (result != null)
                    liveData.setValue(response.isSuccessful() ?
                            result.setCode(response.code()).setCallType(callType) :
                            new ResultObject(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ResultObject(new Throwable(FAILED)).setCallType(callType));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<PostUploadResult> postUploadResultCallback(final MutableLiveData<PostUploadResult> liveData,
                                                                      @CallType final int callType) {
        return new Callback<PostUploadResult>() {
            @Override
            public void onResponse(Call<PostUploadResult> call, Response<PostUploadResult> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(callType) :
                        new PostUploadResult(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }

            @Override
            public void onFailure(Call<PostUploadResult> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new PostUploadResult(new Throwable(FAILED)).setCallType(callType));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<List<ReportTypes>> reportTypesCallback(final MutableLiveData<List<ReportTypes>> liveData,
                                                                  @CallType final int callType) {
        return new Callback<List<ReportTypes>>() {
            @Override
            public void onResponse(Call<List<ReportTypes>> call, Response<List<ReportTypes>> response) {
                if (response.isSuccessful()) {
                    List<ReportTypes> reportTypesList = response.body();
                    setListCallType(reportTypesList, callType);
                    liveData.setValue(reportTypesList);
                } else {
                    ReportTypes response1 = new ReportTypes(new Throwable(NOT_SUCCESSFUL)).setCallType(callType);
                    List<ReportTypes> list = new ArrayList<>();
                    list.add(response1);
                    liveData.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<List<ReportTypes>> call, Throwable t) {
                t.printStackTrace();
                ReportTypes response1 = new ReportTypes(new Throwable(FAILED)).setCallType(callType);
                List<ReportTypes> list = new ArrayList<>();
                list.add(response1);
                liveData.setValue(list);
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<UsersList> usersListCallback(final MutableLiveData<UsersList> liveData,
                                                        @CallType final int callType) {
        return new Callback<UsersList>() {
            @Override
            public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                UsersList usersList = response.body();
                if (usersList != null) {
                    liveData.setValue(response.isSuccessful() ?
                            usersList.setCallType(callType) :
                            new UsersList(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
                }
            }

            @Override
            public void onFailure(Call<UsersList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new UsersList(new Throwable(FAILED)).setCallType(callType));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<VideosList> videosListCallback(final MutableLiveData<VideosList> liveData,
                                                        @CallType final int callType) {
        return new Callback<VideosList>() {
            @Override
            public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                VideosList videosList = response.body();
                if (videosList != null) {
                    liveData.setValue(response.isSuccessful() ?
                            videosList.setCallType(callType) :
                            new VideosList(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
                }
            }

            @Override
            public void onFailure(Call<VideosList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new VideosList(new Throwable(FAILED)).setCallType(callType));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<FollowingsList> followingsListCallback(final MutableLiveData<FollowingsList> liveData,
                                                                  @CallType final int callType) {
        return new Callback<FollowingsList>() {
            @Override
            public void onResponse(Call<FollowingsList> call, Response<FollowingsList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(callType) :
                        new FollowingsList(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }

            @Override
            public void onFailure(Call<FollowingsList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new FollowingsList(new Throwable(FAILED)).setCallType(callType));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<FollowersList> followersListCallback(final MutableLiveData<FollowersList> liveData,
                                                                @CallType final int callType) {
        return new Callback<FollowersList>() {
            @Override
            public void onResponse(Call<FollowersList> call, Response<FollowersList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(callType) :
                        new FollowersList(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }

            @Override
            public void onFailure(Call<FollowersList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new FollowersList(new Throwable(FAILED)).setCallType(callType));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<CircleList> circleListCallback(final MutableLiveData<CircleList> liveData,
                                                          @CallType final int callType) {
        return new Callback<CircleList>() {
            @Override
            public void onResponse(Call<CircleList> call, Response<CircleList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(callType) :
                        new CircleList(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }

            @Override
            public void onFailure(Call<CircleList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new CircleList(new Throwable(FAILED)).setCallType(callType));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<NotificationsList> notificationListCallback(final MutableLiveData<NotificationsList> liveData,
                                                                       @CallType final int callType) {
        return new Callback<NotificationsList>() {
            @Override
            public void onResponse(Call<NotificationsList> call, Response<NotificationsList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(callType) :
                        new NotificationsList(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }

            @Override
            public void onFailure(Call<NotificationsList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new NotificationsList(new Throwable(FAILED)).setCallType(callType));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<ReactionResponse> reactionResponseCallback(final MutableLiveData<ReactionResponse> liveData,
                                                                      @CallType final int callType) {
        return new Callback<ReactionResponse>() {
            @Override
            public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(callType) :
                        new ReactionResponse(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }

            @Override
            public void onFailure(Call<ReactionResponse> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ReactionResponse(new Throwable(FAILED)).setCallType(callType));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<ReactionsList> reactionListCallback(final MutableLiveData<ReactionsList> liveData,
                                                                      @CallType final int callType) {
        return new Callback<ReactionsList>() {
            @Override
            public void onResponse(Call<ReactionsList> call, Response<ReactionsList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(callType) :
                        new ReactionsList(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }

            @Override
            public void onFailure(Call<ReactionsList> call, Throwable t) {
                liveData.setValue(new ReactionsList(new Throwable(FAILED)).setCallType(callType));
                t.printStackTrace();
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<LikedUserList> likedUserListCallback(final MutableLiveData<LikedUserList> liveData,
                                                               @CallType final int callType) {
        return new Callback<LikedUserList>() {
            @Override
            public void onResponse(Call<LikedUserList> call, Response<LikedUserList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(callType) :
                        new LikedUserList(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }

            @Override
            public void onFailure(Call<LikedUserList> call, Throwable t) {
                liveData.setValue(new LikedUserList(new Throwable(FAILED)).setCallType(callType));
                t.printStackTrace();
            }
        };
    }

    public static Callback<TrendingGiphy> trendingGiphyCallback(final MutableLiveData<TrendingGiphy> liveData,
                                                                @CallType final int callType) {
        return new Callback<TrendingGiphy>() {
            @Override
            public void onResponse(Call<TrendingGiphy> call, Response<TrendingGiphy> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(callType) :
                        new TrendingGiphy(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }

            @Override
            public void onFailure(Call<TrendingGiphy> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new TrendingGiphy(new Throwable(NOT_SUCCESSFUL)).setCallType(callType));
            }
        };
    }

    public static void setListCallType(List<? extends BaseModel> list, @CallType int callType) {
        list.get(0).setCall(callType);
    }

    public static int getListCallType(List<? extends BaseModel> list) {
        return list.get(0).getCallType();
    }
}