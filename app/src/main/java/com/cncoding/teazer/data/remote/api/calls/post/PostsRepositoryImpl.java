package com.cncoding.teazer.data.remote.api.calls.post;

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

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.api.calls.ClientProvider.getRetrofitWithAuthToken;

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
        return null;
    }

    @Override
    public LiveData<PostUploadResult> updatePost(UpdatePostRequest updatePostRequest) {
        return null;
    }

    @Override
    public LiveData<ResultObject> likeDislikePost(int postId, int status) {
        return null;
    }

    @Override
    public LiveData<ResultObject> incrementViewCount(int mediaId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> deletePost(int postId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> deletePostVideo(int postId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> reportPost(ReportPost reportPostDetails) {
        return null;
    }

    @Override
    public LiveData<ResultObject> hideOrShowPost(int postId, int status) {
        return null;
    }

    @Override
    public LiveData<PostDetails> getPostDetails(int postId) {
        return null;
    }

    @Override
    public LiveData<TaggedUsersList> getTaggedUsers(int postId, int page) {
        return null;
    }

    @Override
    public LiveData<PostList> getHiddenPosts(int page) {
        return null;
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
                                PostList tempPostList = response.body();
                                if (tempPostList.getPosts() != null && tempPostList.getPosts().size() > 0) {
                                    liveData.setValue(response.body());
                                } else {
                                    if (page == 1 && tempPostList.getPosts().isEmpty())
                                        liveData.setValue(
                                                new PostList(new Throwable(TeazerApplication.getContext().getString(R.string.no_posts_available))));
                                }
                                break;
                            default:
                                liveData.setValue(new PostList(new Throwable("Couldn't load posts")));
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<PostList> call, Throwable t) {
                    liveData.setValue(new PostList(t));
                }
            });
            return liveData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LiveData<PostList> getPostedVideos(int page) {
        return null;
    }

    @Override
    public LiveData<PostList> getVideosPostedByFriends(int page, int friendId) {
        return null;
    }

    @Override
    public LiveData<PostList> getVideosPostedByFriend(int page, int friendId) {
        return null;
    }

    @Override
    public LiveData<PostReactionsList> getReactionsOfPost(int postId, int page) {
        return null;
    }

    @Override
    public LiveData<PostList> getHiddenVideosList(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getAllHiddenVideosList(int postId) {
        return null;
    }
}
