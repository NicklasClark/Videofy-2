package com.cncoding.teazer.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.local.database.DataAccessTasks;
import com.cncoding.teazer.data.local.database.TeazerDB;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.post.PostsRepository;
import com.cncoding.teazer.data.remote.apicalls.post.PostsRepositoryImpl;
import com.cncoding.teazer.model.base.Medias;
import com.cncoding.teazer.model.base.TaggedUser;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.post.PostReactionsList;
import com.cncoding.teazer.model.post.PostUploadResult;
import com.cncoding.teazer.model.post.ReportPost;
import com.cncoding.teazer.model.post.TaggedUsersList;
import com.cncoding.teazer.model.post.UpdatePostRequest;
import com.cncoding.teazer.model.react.GiphyReactionRequest;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.utilities.Annotations.LikeDislike;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MultipartBody;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@SuppressWarnings("unchecked")
public class PostViewModel extends AndroidViewModel {

    public static final int DELETE = 0;
    public static final int INSERT = 1;
    public static final int LIKE = 2;
    public static final int DISLIKE = 4;
    public static final int TAGS = 5;
    public static final int REACTIONS = 6;
    private MediatorLiveData<PostList> postListLiveData;
    private MediatorLiveData<ResultObject> resultObjectLiveData;
    private MediatorLiveData<PostDetails> postDetailsLiveData;
    private MediatorLiveData<PostUploadResult> postUploadResultLiveData;
    private MediatorLiveData<TaggedUsersList> taggedUsersListLiveData;
    private MediatorLiveData<PostReactionsList> reactionsListLiveData;
    private MediatorLiveData<ReactionResponse> reactionResponseLiveData;
    private PostsRepository apiRepository;
    private TeazerDB database;

    public PostViewModel(Application application, String token) {
        super(application);
        this.apiRepository = new PostsRepositoryImpl(token);
        database = TeazerDB.getInstance(this.getApplication());
        postListLiveData = new MediatorLiveData<>();
        this.resultObjectLiveData = new MediatorLiveData<>();
        this.postDetailsLiveData = new MediatorLiveData<>();
        this.postUploadResultLiveData = new MediatorLiveData<>();
        this.taggedUsersListLiveData = new MediatorLiveData<>();
        this.reactionsListLiveData = new MediatorLiveData<>();
        this.reactionResponseLiveData = new MediatorLiveData<>();
    }

    @Inject PostViewModel(@NonNull Application application, MediatorLiveData<PostList> postListLiveData,
                          MediatorLiveData<ResultObject> resultObjectLiveData, MediatorLiveData<PostDetails> postDetailsLiveData,
                          MediatorLiveData<PostUploadResult> postUploadResultLiveData,
                          MediatorLiveData<TaggedUsersList> taggedUsersListLiveData,
                          MediatorLiveData<PostReactionsList> reactionsListLiveData,
                          MediatorLiveData<ReactionResponse> reactionResponseLiveData,
                          PostsRepository apiRepository, TeazerDB database) {
        super(application);
        this.postListLiveData = postListLiveData;
        this.resultObjectLiveData = resultObjectLiveData;
        this.postDetailsLiveData = postDetailsLiveData;
        this.postUploadResultLiveData = postUploadResultLiveData;
        this.taggedUsersListLiveData = taggedUsersListLiveData;
        this.reactionsListLiveData = reactionsListLiveData;
        this.reactionResponseLiveData = reactionResponseLiveData;
        this.apiRepository = apiRepository;
        this.database = database;
    }

    //region Getters
    @NonNull public MediatorLiveData<PostList> getPostList() {
        return postListLiveData;
    }

    @NonNull public MediatorLiveData<ResultObject> getResultObjectLiveData() {
        return resultObjectLiveData;
    }

    @NonNull public MediatorLiveData<PostDetails> getPostDetailsLiveData() {
        return postDetailsLiveData;
    }

    @NonNull public MediatorLiveData<PostUploadResult> getPostUploadResultLiveData() {
        return postUploadResultLiveData;
    }

    @NonNull public MediatorLiveData<TaggedUsersList> getTaggedUsersListLiveData() {
        return taggedUsersListLiveData;
    }

    @NonNull public MediatorLiveData<PostReactionsList> getReactionsListLiveData() {
        return reactionsListLiveData;
    }

    @NonNull public MediatorLiveData<ReactionResponse> getReactionResponseLiveData() {
        return reactionResponseLiveData;
    }
    //endregion

    //region PostList related stuff (Local and Remote).
    /**
     * API call to fetch post list.
     */
    public void loadPostList(final int page) {
        try {
            postListLiveData.addSource(
                    apiRepository.getHomePagePosts(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TeazerDB getDatabase() {
        return database;
    }

    public boolean getAllLocalPosts() {
        try {
            List<PostDetails> postDetailsList = database.dao().getAllPosts();
            boolean result = postDetailsList != null && !postDetailsList.isEmpty();
            if (result) postListLiveData.setValue(new PostList(postDetailsList));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PostDetails getLocalPost(int postId) {
        try {
            return database.dao().getPost(postId).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertAllPostsToDB(List<PostDetails> postDetails) {
        new DataAccessTasks.InsertAllTask(database).execute(postDetails);
    }

    public void updateLocalPost(PostDetails postDetails) {
        new DataAccessTasks.UpdatePostTask(database, postDetails).execute();
    }

    public void likeLocalPost(int postId) {
        new DataAccessTasks.PostUpdateTaskWithId(LIKE, database).execute(postId);
    }

    public void dislikeLocalPost(int postId) {
        new DataAccessTasks.PostUpdateTaskWithId(DISLIKE, database).execute(postId);
    }

    public void deleteLocalPost(int postId) {
        new DataAccessTasks.PostUpdateTaskWithId(DELETE, database).execute(postId);
    }

    public void deleteLocalPost(PostDetails postDetails) {
        new DataAccessTasks.PostUpdateTaskWithObject(DELETE, database).execute(postDetails);
    }

    public void deleteAllLocalPosts() {
        new DataAccessTasks.DeleteAllTask(database).execute();
    }

    public void updateLocalTitle(String title, int postId) {
        new DataAccessTasks.UpdateTitleTask(database, title, postId).execute();
    }

    public void updateLocalTags(ArrayList<TaggedUser> taggedUsers, int postId) {
        new DataAccessTasks.UpdateTagsReactionsTask(database, taggedUsers, null, false, TAGS, postId).execute();
    }

    public void updateLocalReactions(ArrayList<PostReaction> reactions, boolean canReact, int postId) {
        new DataAccessTasks.UpdateTagsReactionsTask(database, null, reactions, true, TAGS, postId).execute();
    }

    public void incrementLocalViews(ArrayList<Medias> medias, int postId) {
        new DataAccessTasks.IncrementViewsTask(database, postId).execute(medias);
    }
    //endregion

    //region Other "Post" API calls
    public void uploadVideo(MultipartBody.Part video, String title, String location,
                            double latitude, double longitude, String tags, String categories) {
        try {
            postUploadResultLiveData.addSource(
                    apiRepository.uploadVideo(video, title, location, latitude, longitude, tags, categories),
                    new Observer<PostUploadResult>() {
                        @Override
                        public void onChanged(@Nullable PostUploadResult postUploadResult) {
                            postUploadResultLiveData.setValue(postUploadResult);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createReactionByGiphy(GiphyReactionRequest giphyReactionRequest) {
        reactionResponseLiveData.addSource(
                apiRepository.createReactionByGiphy(giphyReactionRequest),
                new Observer<ReactionResponse>() {
                    @Override
                    public void onChanged(@Nullable ReactionResponse reactionResponse) {
                        reactionResponseLiveData.setValue(reactionResponse);
                    }
                }
        );
    }

    public void updatePost(UpdatePostRequest updatePostRequest) {
        try {
            postUploadResultLiveData.addSource(
                    apiRepository.updatePost(updatePostRequest),
                    new Observer<PostUploadResult>() {
                        @Override
                        public void onChanged(@Nullable PostUploadResult postUploadResult) {
                            postUploadResultLiveData.setValue(postUploadResult);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void likeDislikePost(int postId, @LikeDislike int status) {
        try {
            resultObjectLiveData.addSource(
                    apiRepository.likeDislikePost(postId, status),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incrementViewCount(int mediaId) {
        try {
            resultObjectLiveData.addSource(
                    apiRepository.incrementViewCount(mediaId),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePost(int postId) {
        try {
            resultObjectLiveData.addSource(
                    apiRepository.deletePost(postId),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportPost(ReportPost reportPostDetails) {
        try {
            resultObjectLiveData.addSource(
                    apiRepository.reportPost(reportPostDetails),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideOrShowPost(int postId, int status) {
        try {
            resultObjectLiveData.addSource(
                    apiRepository.hideOrShowPost(postId, status),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPostDetails(int postId) {
        try {
            postDetailsLiveData.addSource(
                    apiRepository.getPostDetails(postId),
                    new Observer<PostDetails>() {
                        @Override
                        public void onChanged(@Nullable PostDetails postDetails) {
                            postDetailsLiveData.setValue(postDetails);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadTaggedUsers(int postId, int page) {
        try {
            taggedUsersListLiveData.addSource(
                    apiRepository.getTaggedUsers(postId, page),
                    new Observer<TaggedUsersList>() {
                        @Override
                        public void onChanged(@Nullable TaggedUsersList taggedUsersList) {
                            taggedUsersListLiveData.setValue(taggedUsersList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHiddenPosts(int page) {
        try {
            postListLiveData.addSource(
                    apiRepository.getHiddenPosts(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMyPostedVideos(int page) {
        try {
            postListLiveData.addSource(
                    apiRepository.getMyPostedVideos(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadVideosPostedByFriend(int page, int friendId) {
        try {
            postListLiveData.addSource(
                    apiRepository.getVideosPostedByFriend(page, friendId),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadReactionsOfPost(int postId, int page) {
        try {
            reactionsListLiveData.addSource(
                    apiRepository.getReactionsOfPost(postId, page),
                    new Observer<PostReactionsList>() {
                        @Override
                        public void onChanged(@Nullable PostReactionsList postReactionsList) {
                            reactionsListLiveData.setValue(postReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHiddenVideosList(int page) {
        try {
            postListLiveData.addSource(
                    apiRepository.getHiddenVideosList(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            postListLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAllHiddenVideosList(int postId) {
        try {
            resultObjectLiveData.addSource(
                    apiRepository.getAllHiddenVideosList(postId),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion
}