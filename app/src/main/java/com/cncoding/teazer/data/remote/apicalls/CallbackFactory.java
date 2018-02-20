package com.cncoding.teazer.data.remote.apicalls;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.model.friends.CircleList;
import com.cncoding.teazer.model.friends.FollowersList;
import com.cncoding.teazer.model.friends.FollowingsList;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.model.post.PostUploadResult;
import com.cncoding.teazer.model.user.NotificationsList;

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
    public static Callback<PostList> postListCallback(final MutableLiveData<PostList> liveData) {
        return new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new PostList(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new PostList(new Throwable(FAILED)));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<ResultObject> resultObjectCallback(final MutableLiveData<ResultObject> liveData) {
        return new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = response.body();
                if (response.isSuccessful()) {
                    liveData.setValue(result);
                } else liveData.setValue(new ResultObject(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ResultObject(new Throwable(FAILED)));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<PostUploadResult> postUploadResultCallback(final MutableLiveData<PostUploadResult> liveData) {
        return new Callback<PostUploadResult>() {
            @Override
            public void onResponse(Call<PostUploadResult> call, Response<PostUploadResult> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new PostUploadResult(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<PostUploadResult> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new PostUploadResult(new Throwable(FAILED)));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<List<ReportPostTitlesResponse>> reportPostTitlesResponse(
            final MutableLiveData<List<ReportPostTitlesResponse>> liveData) {
        return new Callback<List<ReportPostTitlesResponse>>() {
            @Override
            public void onResponse(Call<List<ReportPostTitlesResponse>> call, Response<List<ReportPostTitlesResponse>> response) {
                if (response.isSuccessful()) {
                    liveData.setValue(response.body());
                } else {
                    ReportPostTitlesResponse response1 = new ReportPostTitlesResponse(new Throwable(NOT_SUCCESSFUL));
                    List<ReportPostTitlesResponse> list = new ArrayList<>();
                    list.add(response1);
                    liveData.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<List<ReportPostTitlesResponse>> call, Throwable t) {
                t.printStackTrace();
                ReportPostTitlesResponse response1 = new ReportPostTitlesResponse(new Throwable(FAILED));
                List<ReportPostTitlesResponse> list = new ArrayList<>();
                list.add(response1);
                liveData.setValue(list);
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<UsersList> usersListCallback(final MutableLiveData<UsersList> liveData) {
        return new Callback<UsersList>() {
            @Override
            public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new UsersList(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<UsersList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new UsersList(new Throwable(FAILED)));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<FollowingsList> followingsListCallback(final MutableLiveData<FollowingsList> liveData) {
        return new Callback<FollowingsList>() {
            @Override
            public void onResponse(Call<FollowingsList> call, Response<FollowingsList> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new FollowingsList(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<FollowingsList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new FollowingsList(new Throwable(FAILED)));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<FollowersList> followersListCallback(final MutableLiveData<FollowersList> liveData) {
        return new Callback<FollowersList>() {
            @Override
            public void onResponse(Call<FollowersList> call, Response<FollowersList> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new FollowersList(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<FollowersList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new FollowersList(new Throwable(FAILED)));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<CircleList> circleListCallback(final MutableLiveData<CircleList> liveData) {
        return new Callback<CircleList>() {
            @Override
            public void onResponse(Call<CircleList> call, Response<CircleList> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new CircleList(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<CircleList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new CircleList(new Throwable(FAILED)));
            }
        };
    }

    @NonNull
    @Contract(pure = true)
    public static Callback<NotificationsList> notificationListCallback(final MutableLiveData<NotificationsList> liveData) {
        return new Callback<NotificationsList>() {
            @Override
            public void onResponse(Call<NotificationsList> call, Response<NotificationsList> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new NotificationsList(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<NotificationsList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new NotificationsList(new Throwable(FAILED)));
            }
        };
    }
}