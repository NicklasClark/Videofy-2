package com.cncoding.teazer.data.repository.remote.react;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionUploadResult;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.react.ReportReaction;

import okhttp3.MultipartBody;

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class ReactRepo implements ReactRepository {
    @Override
    public LiveData<ReactionUploadResult> uploadReaction(MultipartBody.Part video, int postId, String title) {
        return null;
    }

    @Override
    public LiveData<ResultObject> likeDislikeReaction(int reactId, int status) {
        return null;
    }

    @Override
    public LiveData<ResultObject> incrementReactionViewCount(int mediaId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> deleteReaction(int reactId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> reportReaction(ReportReaction reportReaction) {
        return null;
    }

    @Override
    public LiveData<ResultObject> hideOrShowReaction(int reactId, int status) {
        return null;
    }

    @Override
    public LiveData<ReactionsList> getMyReactions(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getHiddenReactions(int page) {
        return null;
    }

    @Override
    public LiveData<ReactionResponse> getReactionDetail(int reactId) {
        return null;
    }
}
