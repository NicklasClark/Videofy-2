package com.cncoding.teazer.data.remote.apicalls.discover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.model.discover.VideosList;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.post.LandingPosts;
import com.cncoding.teazer.model.post.LandingPostsV2;
import com.cncoding.teazer.model.post.PostList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.postListCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.usersListCallback;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getRetrofitWithAuthToken;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class DiscoverRepositoryImpl implements DiscoverRepository {

    private DiscoverService discoverService;

    public DiscoverRepositoryImpl(String token) {
        discoverService = getRetrofitWithAuthToken(token).create(DiscoverService.class);
    }

    @Override public LiveData<LandingPostsV2> getNewDiscoverLandingPosts() {
        final MutableLiveData<LandingPostsV2> liveData = new MutableLiveData<>();
        discoverService.getNewDiscoverLandingPosts().enqueue(new Callback<LandingPostsV2>() {
            @Override
            public void onResponse(Call<LandingPostsV2> call, Response<LandingPostsV2> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new LandingPostsV2(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<LandingPostsV2> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new LandingPostsV2((new Throwable(FAILED))));
            }
        });
        return liveData;
    }

    @Override public LiveData<PostList> getAllMostPopularVideos(int page) {
        final MutableLiveData<PostList> liveData = new MutableLiveData<>();
        discoverService.getAllMostPopularVideos(page).enqueue(postListCallback(liveData));
        return liveData;
    }

    @Override public LiveData<PostList> getFeaturedPosts(int page) {
        final MutableLiveData<PostList> liveData = new MutableLiveData<>();
        discoverService.getFeaturedPosts(page).enqueue(postListCallback(liveData));
        return liveData;
    }

    @Override public LiveData<PostList> getTrendingVideos(int page, int categoryId) {
        final MutableLiveData<PostList> liveData = new MutableLiveData<>();
        discoverService.getTrendingVideos(page, categoryId).enqueue(postListCallback(liveData));
        return liveData;
    }

    @Override public LiveData<PostList> getAllInterestedCategoriesVideos(int page, int categoryId) {
        final MutableLiveData<PostList> liveData = new MutableLiveData<>();
        discoverService.getAllInterestedCategoriesVideos(page, categoryId).enqueue(postListCallback(liveData));
        return liveData;
    }
    @Override
    public LiveData<UsersList> getUsersListToFollow(int page) {
        final MutableLiveData<UsersList> liveData = new MutableLiveData<>();
        discoverService.getUsersListToFollow(page).enqueue(usersListCallback(liveData));
        return liveData;
    }

    @Override public LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm) {
        final MutableLiveData<UsersList> liveData = new MutableLiveData<>();
        discoverService.getUsersListToFollowWithSearchTerm(page, searchTerm).enqueue(usersListCallback(liveData));
        return liveData;
    }

    @Override public LiveData<VideosList> getVideosWithSearchTerm(int page, String searchTerm) {
        final MutableLiveData<VideosList> liveData = new MutableLiveData<>();
        discoverService.getVideosWithSearchTerm(page, searchTerm).enqueue(new Callback<VideosList>() {
            @Override
            public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new VideosList(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<VideosList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new VideosList(new Throwable(FAILED)));
            }
        });
        return liveData;
    }

    @Deprecated @SuppressWarnings("deprecation") @Override public LiveData<LandingPosts> getDiscoverLandingPosts() {
        final MutableLiveData<LandingPosts> liveData = new MutableLiveData<>();
        discoverService.getDiscoverLandingPosts().enqueue(new Callback<LandingPosts>() {
            @Override
            public void onResponse(Call<LandingPosts> call, Response<LandingPosts> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new LandingPosts(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<LandingPosts> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new LandingPosts(new Throwable(FAILED)));
            }
        });
        return liveData;
    }
}