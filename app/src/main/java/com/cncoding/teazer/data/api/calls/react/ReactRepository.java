package com.cncoding.teazer.data.api.calls.react;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.model.react.ReactionUploadResult;
import com.cncoding.teazer.model.react.ReactionsList;
import com.cncoding.teazer.model.react.ReportReaction;

import okhttp3.MultipartBody;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface ReactRepository {
    LiveData<ReactionUploadResult> uploadReaction(MultipartBody.Part video, int postId, String title);

    LiveData<ResultObject> likeDislikeReaction(int reactId, int status);

    LiveData<ResultObject> incrementReactionViewCount(int mediaId);

    LiveData<ResultObject> deleteReaction(int reactId);

    LiveData<ResultObject> reportReaction(ReportReaction reportReaction);

    LiveData<ResultObject> hideOrShowReaction(int reactId, int status);

    LiveData<ReactionsList> getMyReactions(int page);

    LiveData<ResultObject> getHiddenReactions(int page);

    LiveData<ReactionResponse> getReactionDetail(int reactId);
}
