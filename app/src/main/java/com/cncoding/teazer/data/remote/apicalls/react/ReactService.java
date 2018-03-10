package com.cncoding.teazer.data.remote.apicalls.react;

import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.react.GiphyReactionRequest;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.react.ReportReaction;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.utilities.common.Annotations.LikeDislike;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 
 * Created by Prem$ on 2/5/2018.
 */

public interface ReactService {

    /**
     * Call this service to react for a video
     */
    @Multipart
    @POST("/api/v1/react/create")
    Call<ReactionResponse> uploadReaction(@Part MultipartBody.Part video, @Part("post_id") int postId, @Part("title") String title);

    /**
     * Call this service to post reaction using Giphy.
     */
    @POST("/api/v1/react/by/gif")
    Call<ReactionResponse> createReactionByGiphy(@Body GiphyReactionRequest giphyReactionRequest);

    /**
     * Call this service to get reaction data by reaction id
     */
    @GET("/api/v1/react/details/{react_id}")
    Call<ReactionResponse> getReactionDetail(@Path("react_id") int reactId);

    /**
     * Call this service to like/dislike a reacted video.
     * ‘Status’ should be 1 for like and 2 for dislike.
     */
    @POST("/api/v1/react/like/{react_id}/{status}")
    Call<ResultObject> likeDislikeReaction(@Path("react_id") int reactId, @LikeDislike @Path("status") int status);

    /**
     * Call this service to increase the reacted video view count
     */
    @POST("/api/v1/react/increase/view/{media_id}")
    Call<ResultObject> incrementReactionViewCount(@Path("media_id") int mediaId);

    /**
     * Call this service to delete a reaction.
     * Only the Video Owner or Reacted Owner has the delete access.
     * */
    @DELETE("/api/v1/react/delete/{react_id}")
    Call<ResultObject> deleteReaction(@Path("react_id") int reactId);

    /**
     * Call this service to report the reaction.
     * */
    @POST("/api/v1/react/report")
    Call<ResultObject> reportReaction(@Body ReportReaction reportReaction);

    /**
     * Call this service to hide or show a reaction.
     * ‘Status’ should be 1 to hide and 2 to show the reaction.
     * */
    @POST("/api/v1/react/hide/{react_id}/{status}")
    Call<ResultObject> hideOrShowReaction(@Path("react_id") int reactId, @Path("status") int status);

    /**
     * Call this service to get the reactions of
     */
    @GET("/api/v1/react/my/reactions/{page}")
    Call<ReactionsList> getMyReactions(@Path("page") int page);

    /**
     * Call this service to get the reactions of friends.
     */
    @GET("/api/v1/react/friend/reactions/{friend_id}/{page}")
    Call<ReactionsList> getFriendsReactions(@Path("page") int page, @Path("friend_id") int friend_id);

    /**
     * Call this service to get the reactions hidden by user.
     */
    @GET("/api/v1/react/my/hided/reactions/{page}")
    Call<ReactionsList> getHiddenReactions(@Path("page") int page);

    /**
     * Call this service to get the liked users from a reaction.
     */
    @Deprecated @GET("/api/v1/react/liked/users/{react_id}/{page}")
    Call<LikedUserList> getOldLikedUsersOfReaction(@Path("react_id") int reactId, @Path("page") int page);

    /**
     * Call this service to get the liked users from a reaction.
     */
    @Deprecated @GET("/api/v1/react/liked/users/{react_id}")
    Call<LikedUserList> getOldLikedUsersOfReactionWithSearchTerm(@Path("react_id") int reactId, @Query("page") int page, @Query("searchTerm") String searchTerm);

    /**
     * Call this service to get the liked users from a reaction.
     */
    @GET("/api/v2/react/liked/users/{react_id}/{page}")
    Call<LikedUserList> getLikedUsersOfReaction(@Path("react_id") int reactId, @Path("page") int page);

    /**
     * Call this service to get the liked users from a reaction.
     */
    @GET("/api/v2/react/liked/users/{react_id}")
    Call<LikedUserList> getLikedUsersOfReactionWithSearchTerm(@Path("react_id") int reactId, @Query("page") int page, @Query("searchTerm") String searchTerm);
}