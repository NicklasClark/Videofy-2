package com.cncoding.teazer.data.remote.apicalls.post;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.R;
import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.model.post.PostUploadResult;
import com.cncoding.teazer.data.model.post.ReportPost;
import com.cncoding.teazer.data.model.post.TaggedUsersList;
import com.cncoding.teazer.data.model.post.UpdatePostRequest;
import com.cncoding.teazer.data.model.react.GiphyReactionRequest;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.utilities.common.Annotations.HideOrShow;

import javax.inject.Inject;

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
import static com.cncoding.teazer.utilities.common.Annotations.CALL_CREATE_REACTION_BY_GIPHY;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_DELETE_POST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_ALL_HIDDEN_VIDEOS_LIST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_HIDDEN_POSTS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_HIDDEN_VIDEOS_LIST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_HOME_PAGE_POSTS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_MY_POSTED_VIDEOS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_POST_DETAILS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_REACTIONS_OF_POST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_TAGGED_USERS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_VIDEOS_POSTED_BY_FRIEND;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_HIDE_OR_SHOW_POST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_INCREMENT_VIEW_COUNT;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_LIKE_DISLIKE_POST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_REPORT_POST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_UPDATE_POST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_UPLOAD_VIDEO;
import static com.cncoding.teazer.utilities.common.Annotations.LikeDislike;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class PostsRepositoryImpl implements PostsRepository {

    private PostsService postsService;

    @Inject public PostsRepositoryImpl(PostsService postsService) {
        this.postsService = postsService;
    }

    public PostsRepositoryImpl(String token) {
        postsService = getRetrofitWithAuthToken(token).create(PostsService.class);
    }

    @Override
    public LiveData<PostUploadResult> uploadVideo(MultipartBody.Part video, String title, String location,
                                                  double latitude, double longitude, String tags, String categories) {
        final MutableLiveData<PostUploadResult> liveData = new MutableLiveData<>();
        postsService.uploadVideo(video, title, location, latitude, longitude, tags, categories)
                .enqueue(postUploadResultCallback(liveData, CALL_UPLOAD_VIDEO));
        return liveData;
    }

    @Override
    public LiveData<ReactionResponse> createReactionByGiphy(GiphyReactionRequest giphyReactionRequest) {
        final MutableLiveData<ReactionResponse> liveData = new MutableLiveData<>();
        postsService.createReactionByGiphy(giphyReactionRequest)
                .enqueue(reactionResponseCallback(liveData, CALL_CREATE_REACTION_BY_GIPHY));
        return liveData;
    }

    @Override
    public LiveData<PostUploadResult> updatePost(UpdatePostRequest updatePostRequest) {
        final MutableLiveData<PostUploadResult> liveData = new MutableLiveData<>();
        postsService.updatePost(updatePostRequest).enqueue(postUploadResultCallback(liveData, CALL_UPDATE_POST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> likeDislikePost(int postId, @LikeDislike int status) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postsService.likeDislikePost(postId, status).enqueue(resultObjectCallback(liveData, CALL_LIKE_DISLIKE_POST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> incrementViewCount(int mediaId) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postsService.incrementViewCount(mediaId).enqueue(resultObjectCallback(liveData, CALL_INCREMENT_VIEW_COUNT));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> deletePost(int postId) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postsService.deletePost(postId).enqueue(resultObjectCallback(liveData, CALL_DELETE_POST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> reportPost(ReportPost reportPostDetails) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postsService.reportPost(reportPostDetails).enqueue(resultObjectCallback(liveData, CALL_REPORT_POST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> hideOrShowPost(int postId, @HideOrShow int status) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postsService.hideOrShowPost(postId, status).enqueue(resultObjectCallback(liveData, CALL_HIDE_OR_SHOW_POST));
        return liveData;
    }

    @Override
    public LiveData<PostDetails> getPostDetails(int postId) {
        final MutableLiveData<PostDetails> liveData = new MutableLiveData<>();
        postsService.getPostDetails(postId).enqueue(new Callback<PostDetails>() {
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
        postsService.getTaggedUsers(postId, page).enqueue(new Callback<TaggedUsersList>() {
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
        postsService.getHiddenPosts(page).enqueue(postListCallback(liveData, CALL_GET_HIDDEN_POSTS));
        return liveData;
    }

    @Override
    public LiveData<PostList> getHomePagePosts(final int page) {
        try {
            final MutableLiveData<PostList> liveData = new MutableLiveData<>();
            postsService.getHomePagePosts(page).enqueue(new Callback<PostList>() {
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
        postsService.getMyPostedVideos(page).enqueue(postListCallback(liveData, CALL_GET_MY_POSTED_VIDEOS));
        return liveData;
    }

    @Override
    public LiveData<PostList> getVideosPostedByFriend(int page, int friendId) {
        final MutableLiveData<PostList> liveData = new MutableLiveData<>();
        postsService.getVideosPostedByFriend(page, friendId).enqueue(postListCallback(liveData, CALL_GET_VIDEOS_POSTED_BY_FRIEND));
        return liveData;
    }

    @Override
    public LiveData<ReactionsList> getReactionsOfPost(int postId, int page) {
        final MutableLiveData<ReactionsList> liveData = new MutableLiveData<>();
        postsService.getReactionsOfPost(postId, page).enqueue(new Callback<ReactionsList>() {
            @Override
            public void onResponse(Call<ReactionsList> call, Response<ReactionsList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(CALL_GET_REACTIONS_OF_POST) :
                        new ReactionsList(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_REACTIONS_OF_POST));
            }

            @Override
            public void onFailure(Call<ReactionsList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ReactionsList(new Throwable(FAILED)).setCallType(CALL_GET_REACTIONS_OF_POST));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<PostList> getHiddenVideosList(int page) {
        final MutableLiveData<PostList> liveData = new MutableLiveData<>();
        postsService.getHiddenVideosList(page).enqueue(postListCallback(liveData, CALL_GET_HIDDEN_VIDEOS_LIST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> getAllHiddenVideosList(int postId) {
        final MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        postsService.getAllHiddenVideosList(postId).enqueue(resultObjectCallback(liveData, CALL_GET_ALL_HIDDEN_VIDEOS_LIST));
        return liveData;
    }
}