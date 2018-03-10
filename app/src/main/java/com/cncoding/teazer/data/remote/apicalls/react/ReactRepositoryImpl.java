package com.cncoding.teazer.data.remote.apicalls.react;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.react.GiphyReactionRequest;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.react.ReportReaction;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.utilities.common.Annotations.HideOrShow;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.likedUserListCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.reactionListCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.reactionResponseCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.resultObjectCallback;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getRetrofitWithAuthToken;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_CREATE_REACTION_BY_GIPHY;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_DELETE_REACTION;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_HIDDEN_REACTIONS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_LIKED_USERS_OF_REACTION;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_LIKED_USERS_OF_REACTION_WITH_SEARCH_TERM;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_MY_REACTIONS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_OLD_LIKED_USERS_OF_REACTION;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_OLD_LIKED_USERS_OF_REACTION_WITH_SEARCH_TERM;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_REACTION_DETAIL;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_HIDE_OR_SHOW_REACTION;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_INCREMENT_REACTION_VIEW_COUNT;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_LIKE_DISLIKE_REACTION;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_REPORT_REACTION;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_UPLOAD_REACTION;
import static com.cncoding.teazer.utilities.common.Annotations.LikeDislike;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class ReactRepositoryImpl implements ReactRepository {

    private ReactService reactService;

    @Inject public ReactRepositoryImpl(ReactService reactService) {
        this.reactService = reactService;
    }

    public ReactRepositoryImpl(String token) {
        reactService = getRetrofitWithAuthToken(token).create(ReactService.class);
    }

    @Override
    public LiveData<ReactionResponse> uploadReaction(MultipartBody.Part video, int postId, String title) {
        final MutableLiveData<ReactionResponse> liveData = new MutableLiveData<>();
        reactService.uploadReaction(video, postId, title).enqueue(reactionResponseCallback(liveData, CALL_UPLOAD_REACTION));
        return liveData;
    }

    @Override
    public LiveData<ReactionResponse> createReactionByGiphy(GiphyReactionRequest giphyReactionRequest) {
        final MutableLiveData<ReactionResponse> liveData = new MutableLiveData<>();
        reactService.createReactionByGiphy(giphyReactionRequest).enqueue(reactionResponseCallback(liveData, CALL_CREATE_REACTION_BY_GIPHY));
        return liveData;
    }

    @Override
    public LiveData<ReactionResponse> getReactionDetail(int reactId) {
        final MutableLiveData<ReactionResponse> liveData = new MutableLiveData<>();
        reactService.getReactionDetail(reactId).enqueue(reactionResponseCallback(liveData, CALL_GET_REACTION_DETAIL));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> likeDislikeReaction(int reactId, @LikeDislike int status) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.likeDislikeReaction(reactId, status).enqueue(resultObjectCallback(liveData, CALL_LIKE_DISLIKE_REACTION));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> incrementReactionViewCount(int mediaId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.incrementReactionViewCount(mediaId).enqueue(resultObjectCallback(liveData, CALL_INCREMENT_REACTION_VIEW_COUNT));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> deleteReaction(int reactId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.deleteReaction(reactId).enqueue(resultObjectCallback(liveData, CALL_DELETE_REACTION));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> reportReaction(ReportReaction reportReaction) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.reportReaction(reportReaction).enqueue(resultObjectCallback(liveData, CALL_REPORT_REACTION));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> hideOrShowReaction(int reactId, @HideOrShow int status) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.hideOrShowReaction(reactId, status).enqueue(resultObjectCallback(liveData, CALL_HIDE_OR_SHOW_REACTION));
        return liveData;
    }

    @Override
    public LiveData<ReactionsList> getMyReactions(int page) {
        final MutableLiveData<ReactionsList> liveData = new MutableLiveData<>();
        reactService.getMyReactions(page).enqueue(reactionListCallback(liveData, CALL_GET_MY_REACTIONS));
        return liveData;
    }

    @Override
    public LiveData<ReactionsList> getFriendsReactions(int page, int friend_id) {
        final MutableLiveData<ReactionsList> liveData = new MutableLiveData<>();
        reactService.getFriendsReactions(page, friend_id).enqueue(reactionListCallback(liveData, CALL_GET_MY_REACTIONS));
        return liveData;
    }

    @Override
    public LiveData<ReactionsList> getHiddenReactions(int page) {
        final MutableLiveData<ReactionsList> liveData = new MutableLiveData<>();
        reactService.getHiddenReactions(page).enqueue(new Callback<ReactionsList>() {
            @Override
            public void onResponse(Call<ReactionsList> call, Response<ReactionsList> response) {
                ReactionsList result = response.body();
                if (result != null)
                    liveData.setValue(response.isSuccessful() ?
                            result.setCallType(CALL_GET_HIDDEN_REACTIONS) :
                            new ReactionsList(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_HIDDEN_REACTIONS));
            }

            @Override
            public void onFailure(Call<ReactionsList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ReactionsList(new Throwable(FAILED)).setCallType(CALL_GET_HIDDEN_REACTIONS));
            }
        });
        return liveData;
    }

    @Deprecated @SuppressWarnings("deprecation") @Override
    public LiveData<LikedUserList> getOldLikedUsersOfReaction(int reactId, int page) {
        final MutableLiveData<LikedUserList> liveData = new MutableLiveData<>();
        reactService.getOldLikedUsersOfReaction(reactId, page).enqueue(likedUserListCallback(liveData, CALL_GET_OLD_LIKED_USERS_OF_REACTION));
        return liveData;
    }

    @Deprecated @SuppressWarnings("deprecation") @Override
    public LiveData<LikedUserList> getOldLikedUsersOfReactionWithSearchTerm(int reactId, int page, String searchTerm) {
        final MutableLiveData<LikedUserList> liveData = new MutableLiveData<>();
        reactService.getOldLikedUsersOfReactionWithSearchTerm(reactId, page, searchTerm)
                .enqueue(likedUserListCallback(liveData, CALL_GET_OLD_LIKED_USERS_OF_REACTION_WITH_SEARCH_TERM));
        return liveData;
    }

    @Override
    public LiveData<LikedUserList> getLikedUsersOfReaction(int reactId, int page) {
        final MutableLiveData<LikedUserList> liveData = new MutableLiveData<>();
        reactService.getLikedUsersOfReaction(reactId, page).enqueue(likedUserListCallback(liveData, CALL_GET_LIKED_USERS_OF_REACTION));
        return liveData;
    }

    @Override
    public LiveData<LikedUserList> getLikedUsersOfReactionWithSearchTerm(int reactId, int page, String searchTerm) {
        final MutableLiveData<LikedUserList> liveData = new MutableLiveData<>();
        reactService.getLikedUsersOfReactionWithSearchTerm(reactId, page, searchTerm)
                .enqueue(likedUserListCallback(liveData, CALL_GET_LIKED_USERS_OF_REACTION_WITH_SEARCH_TERM));
        return liveData;
    }
}