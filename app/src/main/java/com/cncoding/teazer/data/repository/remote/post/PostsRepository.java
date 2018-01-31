package com.cncoding.teazer.data.repository.remote.post;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.model.post.PostReactionsList;
import com.cncoding.teazer.data.model.post.PostUploadResult;
import com.cncoding.teazer.data.model.post.ReportPost;
import com.cncoding.teazer.data.model.post.TaggedUsersList;
import com.cncoding.teazer.data.model.post.UpdatePostRequest;

import okhttp3.MultipartBody;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface PostsRepository {

    LiveData<PostUploadResult> uploadVideo(MultipartBody.Part video, String title, String location, double latitude, double longitude, String tags, String categories);

    LiveData<PostUploadResult> updatePost(UpdatePostRequest updatePostRequest);

    LiveData<ResultObject> likeDislikePost(int postId, int status);

    LiveData<ResultObject> incrementViewCount(int mediaId);

    LiveData<ResultObject> deletePost(int postId);

    LiveData<ResultObject> deletePostVideo(int postId);

    LiveData<ResultObject> reportPost(ReportPost reportPostDetails);

    LiveData<ResultObject> hideOrShowPost(int postId, int status);

    LiveData<PostDetails> getPostDetails(int postId);

    LiveData<TaggedUsersList> getTaggedUsers(int postId, int page);

    LiveData<PostList> getHiddenPosts(int page);

    LiveData<PostList> getHomePagePosts(int page, String authToken);

    LiveData<PostList> getPostedVideos(int page);

    LiveData<PostList> getVideosPostedByFriends(int page, int friendId);

    LiveData<PostList> getVideosPostedByFriend(int page, int friendId);

    LiveData<PostReactionsList> getReactionsOfPost(int postId, int page);

    LiveData<PostList>getHiddenVideosList(int page);

    LiveData<ResultObject> getAllHiddenVideosList(int postId);
}
