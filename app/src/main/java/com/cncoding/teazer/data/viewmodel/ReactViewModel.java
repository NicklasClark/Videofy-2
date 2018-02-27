package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.react.ReactRepository;
import com.cncoding.teazer.data.remote.apicalls.react.ReactRepositoryImpl;
import com.cncoding.teazer.model.post.LikedUserList;
import com.cncoding.teazer.model.react.GiphyReactionRequest;
import com.cncoding.teazer.model.react.HiddenReactionsList;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.model.react.ReactionsList;
import com.cncoding.teazer.model.react.ReportReaction;
import com.cncoding.teazer.utilities.Annotations.LikeDislike;

import javax.inject.Inject;

import okhttp3.MultipartBody;

/**
 *
 * Created by Prem$ on 2/26/2018.
 */

public class ReactViewModel extends ViewModel {

    private MediatorLiveData<ResultObject> resultObjectLiveData;
    private MediatorLiveData<ReactionsList> reactionsListLiveData;
    private MediatorLiveData<HiddenReactionsList> hiddenReactionsListLiveData;
    private MediatorLiveData<ReactionResponse> reactionResponseLiveData;
    private MediatorLiveData<LikedUserList> likedUserListLiveData;
    private ReactRepository apiRepository;

    @Inject public ReactViewModel(MediatorLiveData<ResultObject> resultObjectLiveData, MediatorLiveData<ReactionsList> reactionsListLiveData,
                                  MediatorLiveData<HiddenReactionsList> hiddenReactionsListLiveData,
                                  MediatorLiveData<ReactionResponse> reactionResponseLiveData,
                                  MediatorLiveData<LikedUserList> likedUserListLiveData, ReactRepository apiRepository) {
        this.resultObjectLiveData = resultObjectLiveData;
        this.reactionsListLiveData = reactionsListLiveData;
        this.hiddenReactionsListLiveData = hiddenReactionsListLiveData;
        this.reactionResponseLiveData = reactionResponseLiveData;
        this.likedUserListLiveData = likedUserListLiveData;
        this.apiRepository = apiRepository;
    }

    public ReactViewModel(String token) {
        resultObjectLiveData = new MediatorLiveData<>();
        reactionsListLiveData = new MediatorLiveData<>();
        hiddenReactionsListLiveData = new MediatorLiveData<>();
        reactionResponseLiveData = new MediatorLiveData<>();
        likedUserListLiveData = new MediatorLiveData<>();
        apiRepository = new ReactRepositoryImpl(token);
    }

    //region Getters
    public MediatorLiveData<ResultObject> getResultObjectLiveData() {
        return resultObjectLiveData;
    }

    public MediatorLiveData<ReactionsList> getReactionsListLiveData() {
        return reactionsListLiveData;
    }

    public MediatorLiveData<HiddenReactionsList> getHiddenReactionsListLiveData() {
        return hiddenReactionsListLiveData;
    }

    public MediatorLiveData<ReactionResponse> getReactionResponseLiveData() {
        return reactionResponseLiveData;
    }

    public MediatorLiveData<LikedUserList> getLikedUserListLiveData() {
        return likedUserListLiveData;
    }
    //endregion

    //region API Calls
    public void uploadReaction(MultipartBody.Part video, int postId, String title){
        try {
            reactionResponseLiveData.addSource(
                    apiRepository.uploadReaction(video, postId, title),
                    new Observer<ReactionResponse>() {
                        @Override
                        public void onChanged(@Nullable ReactionResponse reactionResponse) {
                            reactionResponseLiveData.setValue(reactionResponse);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createReactionByGiphy(GiphyReactionRequest giphyReactionRequest) {
        try {
            reactionResponseLiveData.addSource(
                    apiRepository.createReactionByGiphy(giphyReactionRequest),
                    new Observer<ReactionResponse>() {
                        @Override
                        public void onChanged(@Nullable ReactionResponse reactionResponse) {
                            reactionResponseLiveData.setValue(reactionResponse);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getReactionDetail(int reactId){
        try {
            reactionResponseLiveData.addSource(
                    apiRepository.getReactionDetail(reactId),
                    new Observer<ReactionResponse>() {
                        @Override
                        public void onChanged(@Nullable ReactionResponse reactionResponse) {
                            reactionResponseLiveData.setValue(reactionResponse);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void likeDislikeReaction(int reactId, @LikeDislike int status){
        try {
            resultObjectLiveData.addSource(
                    apiRepository.likeDislikeReaction(reactId, status),
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

    public void incrementReactionViewCount(int mediaId){
        try {
            resultObjectLiveData.addSource(
                    apiRepository.incrementReactionViewCount(mediaId),
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

    public void deleteReaction(int reactId){
        try {
            resultObjectLiveData.addSource(
                    apiRepository.deleteReaction(reactId),
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

    public void reportReaction(ReportReaction reportReaction){
        try {
            resultObjectLiveData.addSource(
                    apiRepository.reportReaction(reportReaction),
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

    public void hideOrShowReaction(int reactId, int status){
        try {
            resultObjectLiveData.addSource(
                    apiRepository.hideOrShowReaction(reactId, status),
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

    public void getMyReactions(int page){
        try {
            reactionsListLiveData.addSource(
                    apiRepository.getMyReactions(page),
                    new Observer<ReactionsList>() {
                        @Override
                        public void onChanged(@Nullable ReactionsList reactionsList) {
                            reactionsListLiveData.setValue(reactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFriendsReactions(int page, int friend_id){
        try {
            reactionsListLiveData.addSource(
                    apiRepository.getFriendsReactions(page, friend_id),
                    new Observer<ReactionsList>() {
                        @Override
                        public void onChanged(@Nullable ReactionsList reactionsList) {
                            reactionsListLiveData.setValue(reactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getHiddenReactions(int page){
        try {
            hiddenReactionsListLiveData.addSource(
                    apiRepository.getHiddenReactions(page),
                    new Observer<HiddenReactionsList>() {
                        @Override
                        public void onChanged(@Nullable HiddenReactionsList hiddenReactionsList) {
                            hiddenReactionsListLiveData.setValue(hiddenReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated @SuppressWarnings("deprecation") public void getOldLikedUsersOfReaction(int reactId, int page){
        try {
            likedUserListLiveData.addSource(
                    apiRepository.getOldLikedUsersOfReaction(reactId, page),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList hiddenReactionsList) {
                            likedUserListLiveData.setValue(hiddenReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated @SuppressWarnings("deprecation")
    public void getOldLikedUsersOfReactionWithSearchTerm(int reactId, int page, String searchTerm){
        try {
            likedUserListLiveData.addSource(
                    apiRepository.getOldLikedUsersOfReactionWithSearchTerm(reactId, page, searchTerm),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList hiddenReactionsList) {
                            likedUserListLiveData.setValue(hiddenReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLikedUsersOfReaction(int reactId, int page){
        try {
            likedUserListLiveData.addSource(
                    apiRepository.getLikedUsersOfReaction(reactId, page),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList hiddenReactionsList) {
                            likedUserListLiveData.setValue(hiddenReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLikedUsersOfReactionWithSearchTerm(int reactId, int page, String searchTerm){
        try {
            likedUserListLiveData.addSource(
                    apiRepository.getLikedUsersOfReactionWithSearchTerm(reactId, page, searchTerm),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList hiddenReactionsList) {
                            likedUserListLiveData.setValue(hiddenReactionsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion
}