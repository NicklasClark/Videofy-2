package com.cncoding.teazer.data.remote.apicalls.discover;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.model.discover.VideosList;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.discover.LandingPosts;
import com.cncoding.teazer.model.discover.LandingPostsV2;
import com.cncoding.teazer.model.post.PostList;

/**
 * 
 * Created by Prem$ on 1/23/2018.
 */

public interface DiscoverRepository {

    LiveData<LandingPosts> getDiscoverLandingPosts();

    LiveData<LandingPostsV2> getNewDiscoverLandingPosts();

    LiveData<PostList> getAllMostPopularVideos(int page);

    LiveData<PostList> getFeaturedPosts(int page);

    LiveData<PostList> getAllInterestedCategoriesVideos(int page, int categoryId);

    LiveData<PostList> getTrendingVideosByCategory(int page, int categoryId);

    LiveData<VideosList> getTrendingVideos(int page);

    LiveData<UsersList> getUsersListToFollow(int page);

    LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm);

    LiveData<VideosList> getVideosWithSearchTerm(int page, String searchTerm);
}