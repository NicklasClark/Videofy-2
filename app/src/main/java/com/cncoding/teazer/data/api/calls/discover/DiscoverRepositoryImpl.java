package com.cncoding.teazer.data.api.calls.discover;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.model.discover.VideosList;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.post.LandingPosts;
import com.cncoding.teazer.model.post.PostList;

import static com.cncoding.teazer.data.api.calls.ClientProvider.getRetrofitWithAuthToken;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class DiscoverRepositoryImpl implements DiscoverRepository {

    private DiscoverService discoverService;

    public DiscoverRepositoryImpl(String token) {
        discoverService = getRetrofitWithAuthToken(token).create(DiscoverService.class);
    }

    @Override
    public LiveData<PostList> getFeaturedPosts(int page) {
        return null;
    }

    @Override
    public LiveData<PostList> getAllInterestedCategoriesVideos(int page, int categoryId) {
        return null;
    }

    @Override
    public LiveData<PostList> getAllMostPopularVideos(int page) {
        return null;
    }

    @Override
    public LiveData<PostList> getTrendingVideos(int page, int categoryId) {
        return null;
    }

    @Override
    public LiveData<LandingPosts> getDiscoverPagePosts() {
        return null;
    }

    @Override
    public LiveData<UsersList> getUsersListToFollow(int page) {
        return null;
    }

    @Override
    public LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<VideosList> getVideosWithSearchTerm(int page, String searchTerm) {
        return null;
    }
}
