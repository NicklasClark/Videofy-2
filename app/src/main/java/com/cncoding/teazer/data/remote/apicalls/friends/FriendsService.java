package com.cncoding.teazer.data.remote.apicalls.friends;

import com.cncoding.teazer.data.model.friends.CircleList;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.ProfileInfo;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.data.remote.ResultObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Prem$ on 2/5/2018.
 **/

public interface FriendsService {

    /**
     * Call this service to get the my followings list
     * */
    @GET("/api/v1/friend/my/followings/{page}")
    Call<FollowingsList> getMyFollowing(@Path("page") int page);

    /**
     * Call this service to send a join request by user ID
     * */
    @POST("/api/v1/friend/join/request/by/userid/{user_id}")
    Call<ResultObject> sendJoinRequestByUserId(@Path("user_id") int userId);

    /**
     * Call this service to send a join request by username
     * */
    @POST("/api/v1/friend/join/request/by/username/{user_name}")
    Call<ResultObject> sendJoinRequestByUsername(@Path("user_name") String username);

    /**
     * Call this service to accept the join request
     * */
    @POST("/api/v1/friend/join/request/accept/{notification_id}")
    Call<ResultObject> acceptJoinRequest(@Path("notification_id") int notificationId);

    /**
     * Call this service to delete the join request
     * */
    @DELETE("/api/v1/friend/join/request/delete/{notification_id}")
    Call<ResultObject> deleteJoinRequest(@Path("notification_id") int notificationId);

    /**
     * Call this service to get the my circle list
     * @return If “nextPage” is true some more records present. So, you can call again with increase the page count by 1.
     * If “next_page” is false no more records present.
     * */
    @GET("/api/v1/friend/my/circle/{page}")
    Call<CircleList> getMyCircle(@Path("page") int page);

    /**
     * Get the "my circle" with search term
     * */
    @GET("/api/v1/friend/my/circle")
    Call<CircleList> getMyCircleWithSearchTerm(@Query("page") int page, @Query("searchTerm") String searchTerm);

    /**
     * Call this service to get the my followings list with search term
     * */
    @GET("/api/v1/friend/my/followings")
    Call<FollowingsList> getMyFollowingsWithSearchTerm(@Path("page") int page, @Query("searchTerm") String searchTerm);

    /**
     * Call this service to get the friends followings list
     * */
    @GET("/api/v1/friend/followings/{user_id}/{page}")
    Call<FollowingsList> getFriendsFollowings(@Path("page") int page, @Path("user_id") int userId);

    /**
     * Call this service to get the friends followings list with search term
     * */
    @GET("/api/v1/friend/followings/{user_id}")
    Call<FollowingsList> getFriendsFollowingsWithSearchTerm(
            @Path("user_id") int userId,
            @Query("page") int page,
            @Query("searchTerm") String searchTerm);

    /**
     * Call this service to get the my followers list
     * */
    @GET("/api/v1/friend/my/followers/{page}")
    Call<FollowersList> getMyFollowers(@Path("page") int page);

    /**
     * Call this service to get the my followers list with search term
     * */
    @GET("/api/v1/friend/my/followers")
    Call<FollowersList> getMyFollowersWithSearchTerm(@Query("page") int page, @Query("searchTerm") String searchTerm);

    /**
     * Call this service to get the friends followers list
     * */
    @GET("/api/v1/friend/followers/{user_id}/{page}")
    Call<FollowersList> getFriendsFollowers(@Path("page") int page, @Path("user_id") int userId);

    /**
     * Call this service to get the friends followers list with search term
     * */
    @GET("/api/v1/friend/followers/{user_id}")
    Call<FollowersList> getFriendsFollowersWithSearchTerm(
            @Path("user_id") int userId,
            @Query("page") int page,
            @Query("searchTerm") String searchTerm);

    /**
     * Call this service to unfollow a user
     * */

    @DELETE("/api/v1/friend/unfollow/{user_id}")
    Call<ResultObject> unfollowUser(@Path("user_id") int userId);

    @DELETE("/api/v1/friend/cancel/join/request/{user_id}")
    Call<ResultObject> cancelRequest(@Path("user_id") int userId);


    @POST("/api/v1/friend/join/request/by/userid/{user_id}")
    Call<ResultObject>followUser(@Path("user_id") int userId);

    /**
     * Call this service to get other's profile information
     * @return “account_type” 1 is a Private account, 2 is a Public account.
     *          “can_join” tell whether you peoples are already friends.
     *          Based on “account_type” you can read either private or public profile.
     * */
    @GET("/api/v1/friend/profile/{user_id}")
    Call<ProfileInfo> getOthersProfileInfo(@Path("user_id") int userId);

    /**
     * Call this service to Block/Unblock a user
     * @param status should be 1 for block and 2 for unblock
     */
    @POST("/api/v1/friend/block/{user_id}/{status}")
    Call<ResultObject> blockUnblockUser(@Path("user_id") int userId, @Path("status") int status);

    /**
     * Call this service to get blocked users list by you.
     */
    @GET("/api/v1/friend/blocked/users/{page}")
    Call<BlockedUsersList> getBlockedUsers(@Path("page") int page);

    /**
     * Call this service to get users list to send follow request.
     */
    @GET("/api/v1/friend/application/users/{page}")
    Call<UsersList> getUsersListToFollow(@Path("page") int page);

    /**
     * Call this service to get users list to send follow request with search term.
     */
    @GET("/api/v1/friend/application/users")
    Call<UsersList> getUsersListToFollowWithSearchTerm(@Query("page") int page, @Query("searchTerm") String searchTerm);

    @GET("/api/v1/post/liked/users/{post_id}/{page}")
    Call<LikedUserList>getLikedUsers(@Path("post_id") int postId, @Path("page") int page);
}