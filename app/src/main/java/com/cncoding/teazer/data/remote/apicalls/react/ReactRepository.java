package com.cncoding.teazer.data.remote.apicalls.react;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.react.GiphyReactionRequest;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.react.ReportReaction;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.utilities.common.Annotations.HideOrShow;
import com.cncoding.teazer.utilities.common.Annotations.LikeDislike;

import okhttp3.MultipartBody;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface ReactRepository {

    LiveData<ReactionResponse> uploadReaction(MultipartBody.Part video, int postId, String title);

    LiveData<ReactionResponse> createReactionByGiphy(GiphyReactionRequest giphyReactionRequest);

    LiveData<ReactionResponse> getReactionDetail(int reactId);

    LiveData<ResultObject> likeDislikeReaction(int reactId, @LikeDislike int status);

    LiveData<ResultObject> incrementReactionViewCount(int mediaId);

    LiveData<ResultObject> deleteReaction(int reactId);

    LiveData<ResultObject> reportReaction(ReportReaction reportReaction);

    LiveData<ResultObject> hideOrShowReaction(int reactId, @HideOrShow int status);

    LiveData<ReactionsList> getMyReactions(int page);
    
    LiveData<ReactionsList> getFriendsReactions(int page, int friend_id);

    LiveData<ReactionsList> getHiddenReactions(int page);

    @Deprecated LiveData<LikedUserList> getOldLikedUsersOfReaction(int reactId, int page);

    @Deprecated LiveData<LikedUserList> getOldLikedUsersOfReactionWithSearchTerm(int reactId, int page, String searchTerm);

    LiveData<LikedUserList> getLikedUsersOfReaction(int reactId, int page);

    LiveData<LikedUserList> getLikedUsersOfReactionWithSearchTerm(int reactId, int page, String searchTerm);
}
