package com.cncoding.teazer.data.remote.apicalls.post;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.model.post.PostReactionsList;
import com.cncoding.teazer.model.post.PostUploadResult;
import com.cncoding.teazer.model.post.ReportPost;
import com.cncoding.teazer.model.post.TaggedUsersList;
import com.cncoding.teazer.model.post.UpdatePostRequest;
import com.cncoding.teazer.model.react.GiphyReactionRequest;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.utilities.Annotations.LikeDislike;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Prem$ on 2/5/2018.
 **/

public interface PostService {
    /**
     * Call this service to post a video
     * */
    @Multipart
    @POST("/api/v1/post/create")
    Call<PostUploadResult> uploadVideo(
            @Part MultipartBody.Part video,
            @Part("title") String title,
            @Part("location") String location,
            @Part("latitude") double latitude,
            @Part("longitude") double longitude,
            @Part("tags") String tags,
            @Part("categories") String categories
    );

    @PUT("/api/v1/post/update")
    Call<PostUploadResult> updatePost(@Body UpdatePostRequest updatePostRequest);

    /**
     * Call this service to like/dislike a video.
     * @param status should be 1 for like and 2 for dislike
     * @return 201 : Liked Successfully
     *      or 200 : Pre validation failed
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     */
    @POST("/api/v1/post/like/{post_id}/{status}")
    Call<ResultObject> likeDislikePost(@Path("post_id") int postId, @LikeDislike @Path("status") int status);

    /**
     * Call this service to increase the video view count
     * @return 200 : status <code>true</code> if the video view count increased successfully and
     *                      <code>false</code> if the video view count increased failed.
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     * */
    @POST("/api/v1/post/increase/view/{media_id}")
    Call<ResultObject> incrementViewCount(@Path("media_id") int mediaId);

    /**
     * Call this service to delete a post
     * @return 200 : status <code>true</code> if the post is deleted successfully and
     *                      <code>false</code> if deleting the post failed.
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     * */
    @DELETE("/api/v1/post/delete/{post_id}")
    Call<ResultObject> deletePost(@Path("post_id") int postId);

    /**
     * Call this service to report a post
     * @return 200 : status <code>true</code> if the post is reported successfully and
     *                      <code>false</code> if reporting the post failed or you have already reported.
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     * */
    @POST("/api/v1/post/report")
    Call<ResultObject> reportPost(@Body ReportPost reportPostDetails);

    /**
     * Call this service to like a video.
     * @param status should be 1 for hiding and 2 for showing the post.
     * @return 200 : status <code>true</code> if the post is Hided/Shown successfully and
     *                      <code>false</code> if hiding/showing the post failed.
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     * */
    @POST("/api/v1/post/hide/{post_id}/{status}")
    Call<ResultObject> hideOrShowPost(@Path("post_id") int postId, @Path("status") int status);

    /**
     * Call this service to get the post details
     * @return 200 : PostDetails details
     *               Returns tagged user to {@link PostDetails}
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     * */
    @GET("api/v1/post/video/details/{post_id}")
    Call<PostDetails> getPostDetails(@Path("post_id") int postId);

    /**
     * Call this service to get the tagged users of post.
     * @return 200 : If “nextPage” is true some more records present so you can call again with increase the page count by 1,
     *               If “next_page” is false no more records present.
     *               Returns tagged user to {@link TaggedUsersList}
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     * */
    @GET("/api/v1/post/tagged/users/{post_id}/{page}")
    Call<TaggedUsersList> getTaggedUsers(@Path("post_id") int postId, @Path("page") int page);

    /**
     * Call this service to get videos hided by the
     * @return 200 : If “nextPage” is true some more records present so you can call again with increase the page count by 1,
     *               If “next_page” is false no more records present.
     *               Returns tagged user to {@link PostDetails}
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     * */
    @GET("/api/v1/post/my/hided/videos/{page}")
    Call<PostList> getHiddenPosts(@Path("page") int page);

    /**
     * Call this service to get the home page post lists
     * @return 200 : If “nextPage” is true some more records present so you can call again with increase the page count by 1,
     *               If “next_page” is false no more records present.
     *               Returns tagged user to {@link PostList}
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     * */
    @GET("/api/v1/post/home/posts/{page}")
    Call<PostList> getHomePagePosts(@Path("page") int page);

    /**
     * Call this service to get the videos posted by user
     * @return 200 : If “nextPage” is true some more records present so you can call again with increase the page count by 1,
     *               If “next_page” is false no more records present.
     *               Returns tagged user to {@link PostList}
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     * */
    @GET("/api/v1/post/my/videos/{page}")
    Call<PostList> getMyPostedVideos(@Path("page") int page);

    @GET("/api/v1/post/friend/videos/{friend_id}/{page}")
    Call<PostList> getVideosPostedByFriend(@Path("page") int page, @Path("friend_id") int friendId);

    /**
     * Call this service to get the reactions of a post.
     * @return 200 : If “nextPage” is true some more records present so you can call again with increase the page count by 1,
     *               If “next_page” is false no more records present.
     *               Returns tagged user to {@link PostReactionsList}
     *      or 401 : Un-Authorized access
     *      or 412 : Validation Failed
     * */
    @GET("/api/v1/post/video/reactions/{post_id}/{page}")
    Call<PostReactionsList> getReactionsOfPost(@Path("post_id") int postId, @Path("page") int page);


    @GET("/api/v1/post/my/hided/videos/{page}")
    Call<PostList>getHiddenVideosList(@Path("page") int page);

    @POST("/api/v1/post/unhide/all/posts/{user_id}")
    Call<ResultObject> getAllHiddenVideosList(@Path("user_id") int postId);

    /**
     * Call this service to post reaction using Giphy.
     */
    @POST("/api/v1/react/by/gif")
    Call<ReactionResponse> createReactionByGiphy(@Body GiphyReactionRequest giphyReactionRequest);
}