package com.cncoding.teazer.data.remote.apicalls.post;

import android.arch.lifecycle.LiveData;

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

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface PostsRepository {

    LiveData<PostUploadResult> uploadVideo(MultipartBody.Part video, String title, String location, double latitude, double longitude, String tags, String categories);

    LiveData<ReactionResponse> createReactionByGiphy(GiphyReactionRequest giphyReactionRequest);

    LiveData<PostUploadResult> updatePost(UpdatePostRequest updatePostRequest);

    LiveData<ResultObject> likeDislikePost(int postId, @LikeDislike int status);

    LiveData<ResultObject> incrementViewCount(int mediaId);

    LiveData<ResultObject> deletePost(int postId);

    LiveData<ResultObject> reportPost(ReportPost reportPostDetails);

    LiveData<ResultObject> hideOrShowPost(int postId, int status);

    LiveData<PostDetails> getPostDetails(int postId);

    LiveData<TaggedUsersList> getTaggedUsers(int postId, int page);

    LiveData<PostList> getHiddenPosts(int page);

    LiveData<PostList> getHomePagePosts(int page);

    LiveData<PostList> getMyPostedVideos(int page);

    LiveData<PostList> getVideosPostedByFriend(int page, int friendId);

    LiveData<PostReactionsList> getReactionsOfPost(int postId, int page);

    LiveData<PostList>getHiddenVideosList(int page);

    LiveData<ResultObject> getAllHiddenVideosList(int postId);
}
