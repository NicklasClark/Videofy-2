package com.cncoding.teazer.data.remote.apicalls.post;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.R;
import com.cncoding.teazer.TeazerApplication;
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

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.postListCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.postUploadResultCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.reactionResponseCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.resultObjectCallback;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getRetrofitWithAuthToken;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;
import static com.cncoding.teazer.utilities.Annotations.CALL_CREATE_REACTION_BY_GIPHY;
import static com.cncoding.teazer.utilities.Annotations.CALL_DELETE_POST;
import static com.cncoding.teazer.utilities.Annotations.CALL_GET_ALL_HIDDEN_VIDEOS_LIST;
import static com.cncoding.teazer.utilities.Annotations.CALL_GET_HIDDEN_POSTS;
import static com.cncoding.teazer.utilities.Annotations.CALL_GET_HIDDEN_VIDEOS_LIST;
import static com.cncoding.teazer.utilities.Annotations.CALL_GET_HOME_PAGE_POSTS;
import static com.cncoding.teazer.utilities.Annotations.CALL_GET_MY_POSTED_VIDEOS;
import static com.cncoding.teazer.utilities.Annotations.CALL_GET_POST_DETAILS;
import static com.cncoding.teazer.utilities.Annotations.CALL_GET_REACTIONS_OF_POST;
import static com.cncoding.teazer.utilities.Annotations.CALL_GET_TAGGED_USERS;
import static com.cncoding.teazer.utilities.Annotations.CALL_GET_VIDEOS_POSTED_BY_FRIEND;
import static com.cncoding.teazer.utilities.Annotations.CALL_HIDE_OR_SHOW_POST;
import static com.cncoding.teazer.utilities.Annotations.CALL_INCREMENT_VIEW_COUNT;
import static com.cncoding.teazer.utilities.Annotations.CALL_LIKE_DISLIKE_POST;
import static com.cncoding.teazer.utilities.Annotations.CALL_REPORT_POST;
import static com.cncoding.teazer.utilities.Annotations.CALL_UPDATE_POST;
import static com.cncoding.teazer.utilities.Annotations.CALL_UPLOAD_VIDEO;
import static com.cncoding.teazer.utilities.Annotations.LikeDislike;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class PostsRepositoryImpl implements PostsRepository {

    private PostService postService;

    public PostsRepositoryImpl(String token) {
        postService = getRetrofitWithAuthToken(token).create(PostService.class);
    }

    @Override
    public LiveData<PostUploadResult> uploadVideo(MultipartBody.Part video, String title, String location,
                                                  double latitude, double longitude, String tags, String categories) {
        final MutableLiveData<PostUploadResult> liveData = new MutableLiveData<>();
        postService.uploadVideo(video, title, location, latitude, longitude, tags, categories)
                .enqueue(postUploadResultCallback(liveData, CALL_UPLOAD_VIDEO));
        return liveData;
    }

    @Override
    public LiveData<ReactionResponse> createReactionByGiphy(GiphyReactionRequest giphyReactionRequest) {
        final MutableLiveData<ReactionResponse> liveData = new MutableLiveData<>();
        postService.createReactionByGiphy(giphyReactionRequest)
                .enqueue(reactionResponseCallback(liveData, CALL_CREATE_REACTION_BY_GIPHY));
        return liveData;
    }

    @Override
    public LiveData<PostUploadResult> updatePost(UpdatePostRequest updatePostRequest) {
        final MutableLiveData<PostUploadResult> liveData = new MutableLiveData<>();
        postService.updatePost(updatePostRequest).enqueue(postUploadResultCallback(liveData, CALL_UPDATE_POST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> likeDislikePost(int postId, @LikeDislike int status) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postService.likeDislikePost(postId, status).enqueue(resultObjectCallback(liveData, CALL_LIKE_DISLIKE_POST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> incrementViewCount(int mediaId) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postService.incrementViewCount(mediaId).enqueue(resultObjectCallback(liveData, CALL_INCREMENT_VIEW_COUNT));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> deletePost(int postId) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postService.deletePost(postId).enqueue(resultObjectCallback(liveData, CALL_DELETE_POST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> reportPost(ReportPost reportPostDetails) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postService.reportPost(reportPostDetails).enqueue(resultObjectCallback(liveData, CALL_REPORT_POST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> hideOrShowPost(int postId, int status) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postService.hideOrShowPost(postId, status).enqueue(resultObjectCallback(liveData, CALL_HIDE_OR_SHOW_POST));
        return liveData;
    }

    @Override
    public LiveData<PostDetails> getPostDetails(int postId) {
        final MutableLiveData<PostDetails> liveData = new MutableLiveData<>();
        postService.getPostDetails(postId).enqueue(new Callback<PostDetails>() {
            @Override
            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(CALL_GET_POST_DETAILS) :
                        new PostDetails(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_POST_DETAILS));
            }

            @Override
            public void onFailure(Call<PostDetails> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new PostDetails(new Throwable(FAILED)).setCallType(CALL_GET_POST_DETAILS));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<TaggedUsersList> getTaggedUsers(int postId, int page) {
        final MutableLiveData<TaggedUsersList> liveData = new MutableLiveData<>();
        postService.getTaggedUsers(postId, page).enqueue(new Callback<TaggedUsersList>() {
            @Override
            public void onResponse(Call<TaggedUsersList> call, Response<TaggedUsersList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(CALL_GET_TAGGED_USERS) :
                        new TaggedUsersList(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_TAGGED_USERS));
            }

            @Override
            public void onFailure(Call<TaggedUsersList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new TaggedUsersList(new Throwable(FAILED)).setCallType(CALL_GET_TAGGED_USERS));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<PostList> getHiddenPosts(int page) {
        final MutableLiveData<PostList> liveData = new MutableLiveData<>();
        postService.getHiddenPosts(page).enqueue(postListCallback(liveData, CALL_GET_HIDDEN_POSTS));
        return liveData;
    }

    @Override
    public LiveData<PostList> getHomePagePosts(final int page) {
        try {
            final MutableLiveData<PostList> liveData = new MutableLiveData<>();
            postService.getHomePagePosts(page).enqueue(new Callback<PostList>() {
                @Override
                public void onResponse(Call<PostList> call, Response<PostList> response) {
                    try {
                        switch (response.code()) {
                            case 200:
                                PostList tempPostList = response.body().setCallType(CALL_GET_HOME_PAGE_POSTS);
                                if (tempPostList.getPosts() != null && tempPostList.getPosts().size() > 0) {
                                    liveData.setValue(tempPostList);
                                } else {
                                    if (page == 1 && tempPostList.getPosts().isEmpty())
                                        liveData.setValue(new PostList(
                                                new Throwable(TeazerApplication.getContext().getString(R.string.no_posts_available))).setCallType(CALL_GET_HOME_PAGE_POSTS));
                                }
                                break;
                            default:
                                liveData.setValue(new PostList(new Throwable("Couldn't load posts")).setCallType(CALL_GET_HOME_PAGE_POSTS));
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PostList> call, Throwable t) {
                    liveData.setValue(new PostList(t).setCallType(CALL_GET_HOME_PAGE_POSTS));
                }
            });
            return liveData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LiveData<PostList> getMyPostedVideos(int page) {
        final MutableLiveData<PostList> liveData = new MutableLiveData<>();
        postService.getMyPostedVideos(page).enqueue(postListCallback(liveData, CALL_GET_MY_POSTED_VIDEOS));
        return liveData;
    }

    @Override
    public LiveData<PostList> getVideosPostedByFriend(int page, int friendId) {
        final MutableLiveData<PostList> liveData = new MutableLiveData<>();
        postService.getVideosPostedByFriend(page, friendId).enqueue(postListCallback(liveData, CALL_GET_VIDEOS_POSTED_BY_FRIEND));
        return liveData;
    }

    @Override
    public LiveData<PostReactionsList> getReactionsOfPost(int postId, int page) {
        final MutableLiveData<PostReactionsList> liveData = new MutableLiveData<>();
        postService.getReactionsOfPost(postId, page).enqueue(new Callback<PostReactionsList>() {
            @Override
            public void onResponse(Call<PostReactionsList> call, Response<PostReactionsList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(CALL_GET_REACTIONS_OF_POST) :
                        new PostReactionsList(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_REACTIONS_OF_POST));
            }

            @Override
            public void onFailure(Call<PostReactionsList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new PostReactionsList(new Throwable(FAILED)).setCallType(CALL_GET_REACTIONS_OF_POST));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<PostList> getHiddenVideosList(int page) {
        final MutableLiveData<PostList> liveData = new MutableLiveData<>();
        postService.getHiddenVideosList(page).enqueue(postListCallback(liveData, CALL_GET_HIDDEN_VIDEOS_LIST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> getAllHiddenVideosList(int postId) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postService.getAllHiddenVideosList(postId).enqueue(resultObjectCallback(liveData, CALL_GET_ALL_HIDDEN_VIDEOS_LIST));
        return liveData;
    }
}