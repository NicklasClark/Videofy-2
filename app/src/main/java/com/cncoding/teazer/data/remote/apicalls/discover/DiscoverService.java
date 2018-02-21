package com.cncoding.teazer.data.remote.apicalls.discover;

import com.cncoding.teazer.model.discover.VideosList;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.discover.LandingPosts;
import com.cncoding.teazer.model.discover.LandingPostsV2;
import com.cncoding.teazer.model.post.PostList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Prem$ on 2/5/2018.
 **/


public interface DiscoverService {

    /**
     * Call this service to get discover page landing posts.
     * Deprecated, use getNewDiscoverLandingPosts() instead.
     */
    @Deprecated
    @GET("/api/v1/discover/landing")
    Call<LandingPosts> getDiscoverLandingPosts();

    /**
     * Call this service to get the discover page featured videos lists.
     */
    @GET("/api/v1/discover/featured/videos/{page}")
    Call<PostList> getFeaturedPosts(@Path("page") int page);

    /**
     * Call this service to get the discover page interested category videos when user clicks "View all".
     */
    @GET("/api/v1/discover/interested/category/videos/{category_id}/{page}")
    Call<PostList> getAllInterestedCategoriesVideos(@Path("page") int page, @Path("category_id") int categoryId);

    /**
     * Call this service to get the most popular videos. Call this service when user taps "View All".
     */
    @GET("/api/v1/discover/most/popular/videos/{page}")
    Call<PostList> getAllMostPopularVideos(@Path("page") int page);

    /**
     * Call this service to get the discover page trending category videos of the respected category.
     */
    @GET("/api/v1/discover/trending/category/videos/{category_id}/{page}")
    Call<PostList> getTrendingVideos(@Path("page") int page, @Path("category_id") int categoryId);

    /**
     * Call this service to get users list to send follow request.
     */
    @GET("/api/v1/discover/users/{page}")
    Call<UsersList> getUsersListToFollow(@Path("page") int page);

    /**
     * Call this service to get users list to send follow request with search term.
     */
    @GET("/api/v1/discover/users")
    Call<UsersList> getUsersListToFollowWithSearchTerm(@Query("page") int page, @Query("searchTerm") String searchTerm);

    /**
     * Call this service to get users list to send follow request with search term.
     */
    @GET("/api/v1/discover/videos")
    Call<VideosList> getVideosWithSearchTerm(@Query("page") int page, @Query("searchTerm") String searchTerm);

    /**
     * Call this service to get the new discover page landing posts.
     */
    @GET("/api/v2/discover/landing")
    Call<LandingPostsV2> getNewDiscoverLandingPosts();
}