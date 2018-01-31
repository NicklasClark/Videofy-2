package com.cncoding.teazer.data.repository.remote.discover;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.model.discover.VideosList;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.LandingPosts;
import com.cncoding.teazer.data.model.post.PostList;

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class DiscoverRepo implements DiscoverRepository {
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
