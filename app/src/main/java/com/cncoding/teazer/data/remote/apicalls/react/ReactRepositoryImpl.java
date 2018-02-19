package com.cncoding.teazer.data.remote.apicalls.react;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.model.react.ReactionUploadResult;
import com.cncoding.teazer.model.react.ReactionsList;
import com.cncoding.teazer.model.react.ReportReaction;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.resultObjectCallback;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getRetrofitWithAuthToken;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class ReactRepositoryImpl implements ReactRepository {

    private ReactService reactService;

    public ReactRepositoryImpl(String token) {
        reactService = getRetrofitWithAuthToken(token).create(ReactService.class);
    }

    @Override
    public LiveData<ReactionUploadResult> uploadReaction(MultipartBody.Part video, int postId, String title) {
        final MutableLiveData<ReactionUploadResult> liveData = new MutableLiveData<>();
        reactService.uploadReaction(video, postId, title).enqueue(new Callback<ReactionUploadResult>() {
            @Override
            public void onResponse(Call<ReactionUploadResult> call, Response<ReactionUploadResult> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new ReactionUploadResult(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<ReactionUploadResult> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ReactionUploadResult(new Throwable(FAILED)));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<ResultObject> likeDislikeReaction(int reactId, int status) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.likeDislikeReaction(reactId, status).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> incrementReactionViewCount(int mediaId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.incrementReactionViewCount(mediaId).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> deleteReaction(int reactId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.deleteReaction(reactId).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> reportReaction(ReportReaction reportReaction) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.reportReaction(reportReaction).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> hideOrShowReaction(int reactId, int status) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.hideOrShowReaction(reactId, status).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> getHiddenReactions(int page) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        reactService.getHiddenReactions(page).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ReactionsList> getMyReactions(int page) {
        final MutableLiveData<ReactionsList> liveData = new MutableLiveData<>();
        reactService.getMyReactions(page).enqueue(new Callback<ReactionsList>() {
            @Override
            public void onResponse(Call<ReactionsList> call, Response<ReactionsList> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new ReactionsList(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<ReactionsList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ReactionsList(new Throwable(FAILED)));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<ReactionResponse> getReactionDetail(int reactId) {
        final MutableLiveData<ReactionResponse> liveData = new MutableLiveData<>();
        reactService.getReactionDetail(reactId).enqueue(new Callback<ReactionResponse>() {
            @Override
            public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new ReactionResponse(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<ReactionResponse> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ReactionResponse(new Throwable(FAILED)));
            }
        });
        return liveData;
    }
}
