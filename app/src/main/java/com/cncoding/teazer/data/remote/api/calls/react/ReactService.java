package com.cncoding.teazer.data.remote.api.calls.react;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.model.react.ReactionUploadResult;
import com.cncoding.teazer.model.react.ReactionsList;
import com.cncoding.teazer.model.react.ReportReaction;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * 
 * Created by Prem$ on 2/5/2018.
 */

public interface ReactService {
    /**
     * Call this service to react for a video
     * @return 200 : Reacted to video has failed. Due to incorrect post id, not having access to react, already done a reaction.
     *               On all above cases the “status” will be false.
     *         201 : Reacted to video Successfully.
     *         401 : Un-Authorized access.
     *         412 : Validation failed.
     * */
    @Multipart
    @POST("/api/v1/react/create")
    Call<ReactionUploadResult> uploadReaction(@Part MultipartBody.Part video, @Part("post_id") int postId, @Part("title") String title);

    /**
     * Call this service to like/dislike a reacted video.
     * ‘Status’ should be 1 for like and 2 for dislike
     * @return 200 : Pre validation failed.
     *         201 : Liked Successfully.
     *         401 : Un-Authorized access.
     *         412 : Validation failed.
     * */
    @POST("/api/v1/react/like/{react_id}/{status}")
    Call<ResultObject> likeDislikeReaction(@Path("react_id") int reactId, @Path("status") int status);

    /**
     * Call this service to increase the reacted video view count
     * @return 200 : If “status” is true reacted video view count increased successfully.
     *                                      If “status” is false reacted video view count increased failed.
     *         401 : Un-Authorized access.
     *         412 : Validation failed.
     * */
    @POST("/api/v1/react/increase/view/{media_id}")
    Call<ResultObject> incrementReactionViewCount(@Path("media_id") int mediaId);

    /**
     * Call this service to delete a reaction.
     * Only the Video Owner or Reacted Owner has the delete access.
     * @return 200 : If “status” is true post deleted successfully.
     *                              If “status” is false post deleted failed.
     *         401 : Un-Authorized access.
     *         412 : Validation failed.
     * */
    @DELETE("/api/v1/react/delete/{react_id}")
    Call<ResultObject> deleteReaction(@Path("react_id") int reactId);

    /**
     * Call this service to report the reaction.
     * @return 200 : If “status” is true reported successfully.
     *                                      If “status” is false reported failed or you have already reported.
     *         401 : Un-Authorized access.
     *         412 : Validation failed.
     * */
    @POST("/api/v1/react/report")
    Call<ResultObject> reportReaction(@Body ReportReaction reportReaction);

    /**
     * Call this service to hide or show a reaction.
     * ‘Status’ should be 1 to hide and 2 to show the reaction.
     * @return 200 : If status is true Hided/Un Hided Successfully.
     *                                      If status is false Hide/Un Hide failed.
     *         401 : Un-Authorized access.
     *         412 : Validation failed.
     * */
    @POST("/api/v1/react/hide/{react_id}/{status}")
    Call<ResultObject> hideOrShowReaction(@Path("react_id") int reactId, @Path("status") int status);

    /**
     * Call this service to get the reactions of 
     * @return 200 : If “nextPage” is true some more records present,
     *                                      so you can call again after incrementing the page count by 1,
     *                                      If “next_page” is false no more records present.
     *         401 : Un-Authorized access.
     *         412 : Validation failed.
     * */
    @GET("/api/v1/react/my/reactions/{page}")
    Call<ReactionsList> getMyReactions(@Path("page") int page);
    /**
     * Call this service to get the reactions of friends.
     * @return 200 : If “nextPage” is true some more records present,
     *                                      so you can call again after incrementing the page count by 1,
     *                                      If “next_page” is false no more records present.
     *         401 : Un-Authorized access.
     *         412 : Validation failed.
     * */
    @GET("/api/v1/react/friend/reactions/{friend_id}/{page}")
    Call<ResultObject> getFriendsReactions(@Path("page") int page, @Path("friend_id") int friend_id);

    /**
     * Call this service to get the reactions hidden by 
     * @return 200 : If “nextPage” is true some more records present,
     *                                      so you can call again after incrementing the page count by 1,
     *                                      If “next_page” is false no more records present.
     *         401 : Un-Authorized access.
     *         412 : Validation failed.
     * */
    @GET("/api/v1/react/my/hided/reactions/{page}")
    Call<ResultObject> getHiddenReactions(@Path("page") int page);

    //Call this service to get reaction data by reaction id
    @GET("/api/v1/react/details/{react_id}")
    Call<ReactionResponse> getReactionDetail(@Path("react_id") int reactId);
}