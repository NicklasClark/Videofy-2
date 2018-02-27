package com.cncoding.teazer.home.post;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.viewmodel.PostViewModel;
import com.cncoding.teazer.data.viewmodel.factory.AuthTokenViewModelFactory;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.model.post.PostReactionsList;
import com.cncoding.teazer.model.post.PostUploadResult;
import com.cncoding.teazer.model.post.ReportPost;
import com.cncoding.teazer.model.post.TaggedUsersList;
import com.cncoding.teazer.model.post.UpdatePostRequest;
import com.cncoding.teazer.model.react.GiphyReactionRequest;
import com.cncoding.teazer.utilities.Annotations.LikeDislike;

import okhttp3.MultipartBody;

import static com.cncoding.teazer.TeazerApplication.get;
import static com.cncoding.teazer.utilities.Annotations.NO_CALL;
import static com.cncoding.teazer.utilities.SharedPrefs.getAuthToken;

/**
 *
 * Created by Prem$ on 2/26/2018.
 */

public abstract class BasePostFragment extends BaseFragment {

    protected PostViewModel viewModel;
    protected int currentPage;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (viewModel == null) {
            viewModel = ViewModelProviders
                    .of(this, new AuthTokenViewModelFactory(get(getParentActivity()), getAuthToken(getContext())))
                    .get(PostViewModel.class);
        }
        currentPage = 1;
    }

    /**
     * Setting observers.
     */
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getPostList().observe(this, new Observer<PostList>() {
            @Override
            public void onChanged(@Nullable PostList postList) {
                handleLiveDataChange(postList);
            }
        });

        viewModel.getResultObjectLiveData().observe(this, new Observer<ResultObject>() {
            @Override
            public void onChanged(@Nullable ResultObject resultObject) {
                handleLiveDataChange(resultObject);
            }
        });

        viewModel.getPostDetailsLiveData().observe(this, new Observer<PostDetails>() {
            @Override
            public void onChanged(@Nullable PostDetails postDetails) {
                handleLiveDataChange(postDetails);
            }
        });

        viewModel.getPostUploadResultLiveData().observe(this, new Observer<PostUploadResult>() {
            @Override
            public void onChanged(@Nullable PostUploadResult postUploadResult) {
                handleLiveDataChange(postUploadResult);
            }
        });

        viewModel.getTaggedUsersListLiveData().observe(this, new Observer<TaggedUsersList>() {
            @Override
            public void onChanged(@Nullable TaggedUsersList taggedUsersList) {
                handleLiveDataChange(taggedUsersList);
            }
        });

        viewModel.getReactionsListLiveData().observe(this, new Observer<PostReactionsList>() {
            @Override
            public void onChanged(@Nullable PostReactionsList postReactionsList) {
                handleLiveDataChange(postReactionsList);
            }
        });
    }

    private void handleLiveDataChange(BaseModel baseModel) {
        if (baseModel != null) {
            if (baseModel.getError() != null) handleError(baseModel);
            else handleResponse(baseModel);
        }
        else handleError(
                new BaseModel()
                        .loadError(new Throwable(getString(R.string.something_went_wrong)))
                        .loadCallType(NO_CALL));
    }

    private void notifyNoInternetConnection() {
        Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
    }

    //region API Calls
    public void loadHomePagePosts(int page) {
        viewModel.loadPostList(page);
    }

    public void uploadVideo(MultipartBody.Part video, String title, String location,
                            double latitude, double longitude, String tags, String categories) {
        viewModel.uploadVideo(video, title, location, latitude, longitude, tags, categories);
    }

    public void createReactionByGiphy(GiphyReactionRequest giphyReactionRequest) {
        viewModel.createReactionByGiphy(giphyReactionRequest);
    }

    public void updatePost(UpdatePostRequest updatePostRequest) {
        viewModel.updatePost(updatePostRequest);
    }

    public void likeDislikePost(int postId, @LikeDislike int status) {
        viewModel.likeDislikePost(postId, status);
    }

    public void incrementViewCount(int mediaId) {
        viewModel.incrementViewCount(mediaId);
    }

    public void deletePost(int postId) {
        viewModel.deletePost(postId);
    }

    public void reportPost(ReportPost reportPostDetails) {
        viewModel.reportPost(reportPostDetails);
    }

    public void hideOrShowPost(int postId, int status) {
        viewModel.hideOrShowPost(postId, status);
    }

    public void loadPostDetails(int postId) {
        viewModel.loadPostDetails(postId);
    }

    public void loadTaggedUsers(int postId, int page) {
        viewModel.loadTaggedUsers(postId, page);
    }

    public void loadHiddenPosts(int page) {
        viewModel.loadHiddenPosts(page);
    }

    public void loadMyPostedVideos(int page) {
        viewModel.loadMyPostedVideos(page);
    }

    public void loadVideosPostedByFriend(int page, int friendId) {
        viewModel.loadVideosPostedByFriend(page, friendId);
    }

    public void loadReactionsOfPost(int postId, int page) {
        viewModel.loadReactionsOfPost(postId, page);
    }

    public void loadHiddenVideosList(int page) {
        viewModel.loadHiddenVideosList(page);
    }

    public void loadAllHiddenVideosList(int postId) {
        viewModel.loadAllHiddenVideosList(postId);
    }
    //endregion

    protected abstract void handleResponse(BaseModel resultObject);

    protected abstract void handleError(BaseModel baseModel);
}